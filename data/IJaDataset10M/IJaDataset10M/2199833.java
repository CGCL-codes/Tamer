package com.android.ddmuilib.log.event;

import com.android.ddmlib.Client;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.log.EventContainer;
import com.android.ddmlib.log.EventLogParser;
import com.android.ddmlib.log.LogReceiver;
import com.android.ddmlib.log.LogReceiver.ILogListener;
import com.android.ddmlib.log.LogReceiver.LogEntry;
import com.android.ddmuilib.DdmUiPreferences;
import com.android.ddmuilib.IImageLoader;
import com.android.ddmuilib.TablePanel;
import com.android.ddmuilib.actions.ICommonAction;
import com.android.ddmuilib.annotation.UiThread;
import com.android.ddmuilib.annotation.WorkerThread;
import com.android.ddmuilib.log.event.EventDisplay.ILogColumnListener;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Event log viewer
 */
public class EventLogPanel extends TablePanel implements ILogListener, ILogColumnListener {

    private static final String TAG_FILE_EXT = ".tag";

    private static final String PREFS_EVENT_DISPLAY = "EventLogPanel.eventDisplay";

    private static final String EVENT_DISPLAY_STORAGE_SEPARATOR = "|";

    static final String PREFS_DISPLAY_WIDTH = "EventLogPanel.width";

    static final String PREFS_DISPLAY_HEIGHT = "EventLogPanel.height";

    private static final int DEFAULT_DISPLAY_WIDTH = 500;

    private static final int DEFAULT_DISPLAY_HEIGHT = 400;

    private IImageLoader mImageLoader;

    private IDevice mCurrentLoggedDevice;

    private String mCurrentLogFile;

    private LogReceiver mCurrentLogReceiver;

    private EventLogParser mCurrentEventLogParser;

    private Object mLock = new Object();

    /** list of all the events. */
    private final ArrayList<EventContainer> mEvents = new ArrayList<EventContainer>();

    /** list of all the new events, that have yet to be displayed by the ui */
    private final ArrayList<EventContainer> mNewEvents = new ArrayList<EventContainer>();

    /** indicates a pending ui thread display */
    private boolean mPendingDisplay = false;

    /** list of all the custom event displays */
    private final ArrayList<EventDisplay> mEventDisplays = new ArrayList<EventDisplay>();

    private final NumberFormat mFormatter = NumberFormat.getInstance();

    private Composite mParent;

    private ScrolledComposite mBottomParentPanel;

    private Composite mBottomPanel;

    private ICommonAction mOptionsAction;

    private ICommonAction mClearAction;

    private ICommonAction mSaveAction;

    private ICommonAction mLoadAction;

    private ICommonAction mImportAction;

    /** file containing the current log raw data. */
    private File mTempFile = null;

    public EventLogPanel(IImageLoader imageLoader) {
        super();
        mImageLoader = imageLoader;
        mFormatter.setGroupingUsed(true);
    }

    /**
     * Sets the external actions.
     * <p/>This method sets up the {@link ICommonAction} objects to execute the proper code
     * when triggered by using {@link ICommonAction#setRunnable(Runnable)}.
     * <p/>It will also make sure they are enabled only when possible.
     * @param optionsAction
     * @param clearAction
     * @param saveAction
     * @param loadAction
     * @param importAction
     */
    public void setActions(ICommonAction optionsAction, ICommonAction clearAction, ICommonAction saveAction, ICommonAction loadAction, ICommonAction importAction) {
        mOptionsAction = optionsAction;
        mOptionsAction.setRunnable(new Runnable() {

            public void run() {
                openOptionPanel();
            }
        });
        mClearAction = clearAction;
        mClearAction.setRunnable(new Runnable() {

            public void run() {
                clearLog();
            }
        });
        mSaveAction = saveAction;
        mSaveAction.setRunnable(new Runnable() {

            public void run() {
                try {
                    FileDialog fileDialog = new FileDialog(mParent.getShell(), SWT.SAVE);
                    fileDialog.setText("Save Event Log");
                    fileDialog.setFileName("event.log");
                    String fileName = fileDialog.open();
                    if (fileName != null) {
                        saveLog(fileName);
                    }
                } catch (IOException e1) {
                }
            }
        });
        mLoadAction = loadAction;
        mLoadAction.setRunnable(new Runnable() {

            public void run() {
                FileDialog fileDialog = new FileDialog(mParent.getShell(), SWT.OPEN);
                fileDialog.setText("Load Event Log");
                String fileName = fileDialog.open();
                if (fileName != null) {
                    loadLog(fileName);
                }
            }
        });
        mImportAction = importAction;
        mImportAction.setRunnable(new Runnable() {

            public void run() {
                FileDialog fileDialog = new FileDialog(mParent.getShell(), SWT.OPEN);
                fileDialog.setText("Import Bug Report");
                String fileName = fileDialog.open();
                if (fileName != null) {
                    importBugReport(fileName);
                }
            }
        });
        mOptionsAction.setEnabled(false);
        mClearAction.setEnabled(false);
        mSaveAction.setEnabled(false);
    }

    /**
     * Opens the option panel.
     * </p>
     * <b>This must be called from the UI thread</b>
     */
    @UiThread
    public void openOptionPanel() {
        try {
            EventDisplayOptions dialog = new EventDisplayOptions(mImageLoader, mParent.getShell());
            if (dialog.open(mCurrentEventLogParser, mEventDisplays, mEvents)) {
                synchronized (mLock) {
                    mEventDisplays.clear();
                    mEventDisplays.addAll(dialog.getEventDisplays());
                    saveEventDisplays();
                    rebuildUi();
                }
            }
        } catch (SWTException e) {
            Log.e("EventLog", e);
        }
    }

    /**
     * Clears the log.
     * <p/>
     * <b>This must be called from the UI thread</b>
     */
    public void clearLog() {
        try {
            synchronized (mLock) {
                mEvents.clear();
                mNewEvents.clear();
                mPendingDisplay = false;
                for (EventDisplay eventDisplay : mEventDisplays) {
                    eventDisplay.resetUI();
                }
            }
        } catch (SWTException e) {
            Log.e("EventLog", e);
        }
    }

    /**
     * Saves the content of the event log into a file. The log is saved in the same
     * binary format than on the device.
     * @param filePath
     * @throws IOException
     */
    public void saveLog(String filePath) throws IOException {
        if (mCurrentLoggedDevice != null && mCurrentEventLogParser != null) {
            File destFile = new File(filePath);
            destFile.createNewFile();
            FileInputStream fis = new FileInputStream(mTempFile);
            FileOutputStream fos = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int count;
            while ((count = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, count);
            }
            fos.close();
            fis.close();
            filePath = filePath + TAG_FILE_EXT;
            mCurrentEventLogParser.saveTags(filePath);
        }
    }

    /**
     * Loads a binary event log (if has associated .tag file) or
     * otherwise loads a textual event log.
     * @param filePath Event log path (and base of potential tag file)
     */
    public void loadLog(String filePath) {
        if ((new File(filePath + TAG_FILE_EXT)).exists()) {
            startEventLogFromFiles(filePath);
        } else {
            try {
                EventLogImporter importer = new EventLogImporter(filePath);
                String[] tags = importer.getTags();
                String[] log = importer.getLog();
                startEventLogFromContent(tags, log);
            } catch (FileNotFoundException e) {
                Log.logAndDisplay(Log.LogLevel.ERROR, "EventLog", String.format("Failure to read %1$s", filePath + TAG_FILE_EXT));
            }
        }
    }

    public void importBugReport(String filePath) {
        try {
            BugReportImporter importer = new BugReportImporter(filePath);
            String[] tags = importer.getTags();
            String[] log = importer.getLog();
            startEventLogFromContent(tags, log);
        } catch (FileNotFoundException e) {
            Log.logAndDisplay(LogLevel.ERROR, "Import", "Unable to import bug report: " + e.getMessage());
        }
    }

    @Override
    public void clientSelected() {
    }

    @Override
    public void deviceSelected() {
        startEventLog(getCurrentDevice());
    }

    public void clientChanged(Client client, int changeMask) {
    }

    @Override
    protected Control createControl(Composite parent) {
        mParent = parent;
        mParent.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                synchronized (mLock) {
                    if (mCurrentLogReceiver != null) {
                        mCurrentLogReceiver.cancel();
                        mCurrentLogReceiver = null;
                        mCurrentEventLogParser = null;
                        mCurrentLoggedDevice = null;
                        mEventDisplays.clear();
                        mEvents.clear();
                    }
                }
            }
        });
        final IPreferenceStore store = DdmUiPreferences.getStore();
        store.setDefault(PREFS_DISPLAY_WIDTH, DEFAULT_DISPLAY_WIDTH);
        store.setDefault(PREFS_DISPLAY_HEIGHT, DEFAULT_DISPLAY_HEIGHT);
        mBottomParentPanel = new ScrolledComposite(parent, SWT.V_SCROLL);
        mBottomParentPanel.setLayoutData(new GridData(GridData.FILL_BOTH));
        mBottomParentPanel.setExpandHorizontal(true);
        mBottomParentPanel.setExpandVertical(true);
        mBottomParentPanel.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(ControlEvent e) {
                if (mBottomPanel != null) {
                    Rectangle r = mBottomParentPanel.getClientArea();
                    mBottomParentPanel.setMinSize(mBottomPanel.computeSize(r.width, SWT.DEFAULT));
                }
            }
        });
        prepareDisplayUi();
        loadEventDisplays();
        createDisplayUi();
        return mBottomParentPanel;
    }

    @Override
    protected void postCreation() {
    }

    @Override
    public void setFocus() {
        mBottomParentPanel.setFocus();
    }

    /**
     * Starts a new logcat and set mCurrentLogCat as the current receiver.
     * @param device the device to connect logcat to.
     */
    private void startEventLog(final IDevice device) {
        if (device == mCurrentLoggedDevice) {
            return;
        }
        if (mCurrentLogReceiver != null) {
            stopEventLog(false);
        }
        mCurrentLoggedDevice = null;
        mCurrentLogFile = null;
        if (device != null) {
            mCurrentLogReceiver = new LogReceiver(this);
            new Thread("EventLog") {

                @Override
                public void run() {
                    while (device.isOnline() == false && mCurrentLogReceiver != null && mCurrentLogReceiver.isCancelled() == false) {
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    if (mCurrentLogReceiver == null || mCurrentLogReceiver.isCancelled()) {
                        return;
                    }
                    try {
                        mCurrentLoggedDevice = device;
                        synchronized (mLock) {
                            mCurrentEventLogParser = new EventLogParser();
                            mCurrentEventLogParser.init(device);
                        }
                        updateEventDisplays();
                        mTempFile = File.createTempFile("android-event-", ".log");
                        device.runEventLogService(mCurrentLogReceiver);
                    } catch (Exception e) {
                        Log.e("EventLog", e);
                    } finally {
                    }
                }
            }.start();
        }
    }

    private void startEventLogFromFiles(final String fileName) {
        if (mCurrentLogReceiver != null) {
            stopEventLog(false);
        }
        mCurrentLoggedDevice = null;
        mCurrentLogFile = null;
        mCurrentLogReceiver = new LogReceiver(this);
        mSaveAction.setEnabled(false);
        new Thread("EventLog") {

            @Override
            public void run() {
                try {
                    mCurrentLogFile = fileName;
                    synchronized (mLock) {
                        mCurrentEventLogParser = new EventLogParser();
                        if (mCurrentEventLogParser.init(fileName + TAG_FILE_EXT) == false) {
                            mCurrentEventLogParser = null;
                            Log.logAndDisplay(LogLevel.ERROR, "EventLog", String.format("Failure to read %1$s", fileName + TAG_FILE_EXT));
                            return;
                        }
                    }
                    updateEventDisplays();
                    runLocalEventLogService(fileName, mCurrentLogReceiver);
                } catch (Exception e) {
                    Log.e("EventLog", e);
                } finally {
                }
            }
        }.start();
    }

    private void startEventLogFromContent(final String[] tags, final String[] log) {
        if (mCurrentLogReceiver != null) {
            stopEventLog(false);
        }
        mCurrentLoggedDevice = null;
        mCurrentLogFile = null;
        mCurrentLogReceiver = new LogReceiver(this);
        mSaveAction.setEnabled(false);
        new Thread("EventLog") {

            @Override
            public void run() {
                try {
                    synchronized (mLock) {
                        mCurrentEventLogParser = new EventLogParser();
                        if (mCurrentEventLogParser.init(tags) == false) {
                            mCurrentEventLogParser = null;
                            return;
                        }
                    }
                    updateEventDisplays();
                    runLocalEventLogService(log, mCurrentLogReceiver);
                } catch (Exception e) {
                    Log.e("EventLog", e);
                } finally {
                }
            }
        }.start();
    }

    public void stopEventLog(boolean inUiThread) {
        if (mCurrentLogReceiver != null) {
            mCurrentLogReceiver.cancel();
            synchronized (mLock) {
                mCurrentLogReceiver = null;
                mCurrentEventLogParser = null;
                mCurrentLoggedDevice = null;
                mEvents.clear();
                mNewEvents.clear();
                mPendingDisplay = false;
            }
            resetUI(inUiThread);
        }
        if (mTempFile != null) {
            mTempFile.delete();
            mTempFile = null;
        }
    }

    private void resetUI(boolean inUiThread) {
        mEvents.clear();
        if (inUiThread) {
            resetUiFromUiThread();
        } else {
            try {
                Display d = mBottomParentPanel.getDisplay();
                d.syncExec(new Runnable() {

                    public void run() {
                        if (mBottomParentPanel.isDisposed() == false) {
                            resetUiFromUiThread();
                        }
                    }
                });
            } catch (SWTException e) {
            }
        }
    }

    private void resetUiFromUiThread() {
        synchronized (mLock) {
            for (EventDisplay eventDisplay : mEventDisplays) {
                eventDisplay.resetUI();
            }
        }
        mOptionsAction.setEnabled(false);
        mClearAction.setEnabled(false);
        mSaveAction.setEnabled(false);
    }

    private void prepareDisplayUi() {
        mBottomPanel = new Composite(mBottomParentPanel, SWT.NONE);
        mBottomParentPanel.setContent(mBottomPanel);
    }

    private void createDisplayUi() {
        RowLayout rowLayout = new RowLayout();
        rowLayout.wrap = true;
        rowLayout.pack = false;
        rowLayout.justify = true;
        rowLayout.fill = true;
        rowLayout.type = SWT.HORIZONTAL;
        mBottomPanel.setLayout(rowLayout);
        IPreferenceStore store = DdmUiPreferences.getStore();
        int displayWidth = store.getInt(PREFS_DISPLAY_WIDTH);
        int displayHeight = store.getInt(PREFS_DISPLAY_HEIGHT);
        for (EventDisplay eventDisplay : mEventDisplays) {
            Control c = eventDisplay.createComposite(mBottomPanel, mCurrentEventLogParser, this);
            if (c != null) {
                RowData rd = new RowData();
                rd.height = displayHeight;
                rd.width = displayWidth;
                c.setLayoutData(rd);
            }
            Table table = eventDisplay.getTable();
            if (table != null) {
                addTableToFocusListener(table);
            }
        }
        mBottomPanel.layout();
        mBottomParentPanel.setMinSize(mBottomPanel.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        mBottomParentPanel.layout();
    }

    /**
     * Rebuild the display ui.
     */
    @UiThread
    private void rebuildUi() {
        synchronized (mLock) {
            mBottomPanel.dispose();
            mBottomPanel = null;
            prepareDisplayUi();
            createDisplayUi();
            boolean start_event = false;
            synchronized (mNewEvents) {
                mNewEvents.addAll(0, mEvents);
                if (mPendingDisplay == false) {
                    mPendingDisplay = true;
                    start_event = true;
                }
            }
            if (start_event) {
                scheduleUIEventHandler();
            }
            Rectangle r = mBottomParentPanel.getClientArea();
            mBottomParentPanel.setMinSize(mBottomPanel.computeSize(r.width, SWT.DEFAULT));
        }
    }

    /**
     * Processes a new {@link LogEntry} by parsing it with {@link EventLogParser} and displaying it.
     * @param entry The new log entry
     * @see LogReceiver.ILogListener#newEntry(LogEntry)
     */
    @WorkerThread
    public void newEntry(LogEntry entry) {
        synchronized (mLock) {
            if (mCurrentEventLogParser != null) {
                EventContainer event = mCurrentEventLogParser.parse(entry);
                if (event != null) {
                    handleNewEvent(event);
                }
            }
        }
    }

    @WorkerThread
    private void handleNewEvent(EventContainer event) {
        mEvents.add(event);
        boolean start_event = false;
        synchronized (mNewEvents) {
            mNewEvents.add(event);
            if (mPendingDisplay == false) {
                mPendingDisplay = true;
                start_event = true;
            }
        }
        if (start_event == false) {
            return;
        }
        scheduleUIEventHandler();
    }

    /**
     * Schedules the UI thread to execute a {@link Runnable} calling {@link #displayNewEvents()}.
     */
    private void scheduleUIEventHandler() {
        try {
            Display d = mBottomParentPanel.getDisplay();
            d.asyncExec(new Runnable() {

                public void run() {
                    if (mBottomParentPanel.isDisposed() == false) {
                        if (mCurrentEventLogParser != null) {
                            displayNewEvents();
                        }
                    }
                }
            });
        } catch (SWTException e) {
        }
    }

    /**
     * Processes raw data coming from the log service.
     * @see LogReceiver.ILogListener#newData(byte[], int, int)
     */
    public void newData(byte[] data, int offset, int length) {
        if (mTempFile != null) {
            try {
                FileOutputStream fos = new FileOutputStream(mTempFile, true);
                fos.write(data, offset, length);
                fos.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }
    }

    @UiThread
    private void displayNewEvents() {
        int count = 0;
        for (EventDisplay eventDisplay : mEventDisplays) {
            eventDisplay.startMultiEventDisplay();
        }
        EventContainer event = null;
        boolean need_to_reloop = false;
        do {
            synchronized (mNewEvents) {
                if (mNewEvents.size() > 0) {
                    if (count > 200) {
                        need_to_reloop = true;
                        event = null;
                    } else {
                        event = mNewEvents.remove(0);
                        count++;
                    }
                } else {
                    event = null;
                    mPendingDisplay = false;
                }
            }
            if (event != null) {
                for (EventDisplay eventDisplay : mEventDisplays) {
                    eventDisplay.newEvent(event, mCurrentEventLogParser);
                }
            }
        } while (event != null);
        for (EventDisplay eventDisplay : mEventDisplays) {
            eventDisplay.endMultiEventDisplay();
        }
        if (need_to_reloop) {
            scheduleUIEventHandler();
        }
    }

    /**
     * Loads the {@link EventDisplay}s from the preference store.
     */
    private void loadEventDisplays() {
        IPreferenceStore store = DdmUiPreferences.getStore();
        String storage = store.getString(PREFS_EVENT_DISPLAY);
        if (storage.length() > 0) {
            String[] values = storage.split(Pattern.quote(EVENT_DISPLAY_STORAGE_SEPARATOR));
            for (String value : values) {
                EventDisplay eventDisplay = EventDisplay.load(value);
                if (eventDisplay != null) {
                    mEventDisplays.add(eventDisplay);
                }
            }
        }
    }

    /**
     * Saves the {@link EventDisplay}s into the {@link DdmUiPreferences} store.
     */
    private void saveEventDisplays() {
        IPreferenceStore store = DdmUiPreferences.getStore();
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        for (EventDisplay eventDisplay : mEventDisplays) {
            String storage = eventDisplay.getStorageString();
            if (storage != null) {
                if (first == false) {
                    sb.append(EVENT_DISPLAY_STORAGE_SEPARATOR);
                } else {
                    first = false;
                }
                sb.append(storage);
            }
        }
        store.setValue(PREFS_EVENT_DISPLAY, sb.toString());
    }

    /**
     * Updates the {@link EventDisplay} with the new {@link EventLogParser}.
     * <p/>
     * This will run asynchronously in the UI thread.
     */
    @WorkerThread
    private void updateEventDisplays() {
        try {
            Display d = mBottomParentPanel.getDisplay();
            d.asyncExec(new Runnable() {

                public void run() {
                    if (mBottomParentPanel.isDisposed() == false) {
                        for (EventDisplay eventDisplay : mEventDisplays) {
                            eventDisplay.setNewLogParser(mCurrentEventLogParser);
                        }
                        mOptionsAction.setEnabled(true);
                        mClearAction.setEnabled(true);
                        if (mCurrentLogFile == null) {
                            mSaveAction.setEnabled(true);
                        } else {
                            mSaveAction.setEnabled(false);
                        }
                    }
                }
            });
        } catch (SWTException e) {
        }
    }

    @UiThread
    public void columnResized(int index, TableColumn sourceColumn) {
        for (EventDisplay eventDisplay : mEventDisplays) {
            eventDisplay.resizeColumn(index, sourceColumn);
        }
    }

    /**
     * Runs an event log service out of a local file.
     * @param fileName the full file name of the local file containing the event log.
     * @param logReceiver the receiver that will handle the log
     * @throws IOException
     */
    @WorkerThread
    private void runLocalEventLogService(String fileName, LogReceiver logReceiver) throws IOException {
        byte[] buffer = new byte[256];
        FileInputStream fis = new FileInputStream(fileName);
        int count;
        while ((count = fis.read(buffer)) != -1) {
            logReceiver.parseNewData(buffer, 0, count);
        }
    }

    @WorkerThread
    private void runLocalEventLogService(String[] log, LogReceiver currentLogReceiver) {
        synchronized (mLock) {
            for (String line : log) {
                EventContainer event = mCurrentEventLogParser.parse(line);
                if (event != null) {
                    handleNewEvent(event);
                }
            }
        }
    }
}
