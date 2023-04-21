package org.xaware.ide.xadev.gui.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.part.ViewPart;
import org.xaware.ide.shared.EclipseGuiLogHandler;
import org.xaware.ide.xadev.XA_Designer_Plugin;
import org.xaware.ide.xadev.common.ControlFactory;
import org.xaware.ide.xadev.common.GlobalConstants;
import org.xaware.ide.xadev.gui.DocSearchTool;
import org.xaware.ide.xadev.runtime.RuntimeStatePrefs;
import org.xaware.shared.i18n.Translator;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * The Log pane appears along the bottom of the default XA-Designer window. All
 * log messages are displayed in this pane including date and time the log
 * message occured.
 *
 * @author Saritha
 * @version 1.0
 */
public class LogView extends ViewPart {

    public static final String ID = "org.xaware.ide.plugin.view.LogView";

    /** LogView reference */
    private static LogView instance;

    /** For localization */
    private static final Translator translator = XA_Designer_Plugin.getTranslator();

    /** GUI Log level */
    private static Level logLevel;

    /** Dynamic Help Context ID */
    private static LogViewContextProvider contextProvider = null;

    /** Class level logger */
    protected final XAwareLogger logger = XAwareLogger.getXAwareLogger(LogView.class.getName());

    /** Composite which holds all Log view controls */
    private Composite logPanelComp;

    /** The location from which search will start */
    protected int searchStartPos = 0;

    /** Holds GUI Log messages */
    protected StyledText logAreaTxt;

    /** For finding entered text */
    private Button findBtn;

    /** Clearing only GUI Log messages */
    private Button clearBtn;

    /** String searched in GUI Log textarea */
    protected String findStr = null;

    /** Decides whether find is casesensitive or not */
    protected Button caseMatchChk;

    /** Stores find history */
    private Combo searchCmb;

    /** Last performed Search text */
    private String logSearchStr = null;

    /**
	 * Class which locates the find string and highligths string with unique
	 * background colour
	 */
    protected DocSearchTool docSearchTool = null;

    /** No.of words will be added to find history */
    private final int HISTORY_LENGTH = RuntimeStatePrefs.getSearchHistorySize(RuntimeStatePrefs.LOG_WINDOW) - 1;

    /** Label containg the present log level */
    private Label logLevelLbl;

    /**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
    public LogView() {
        super();
        logLevel = RuntimeStatePrefs.getDefaultGuiLogLevel();
    }

    /**
	 * This is a callback that will allow us to create the viewer and
	 * initialize it.
	 *
	 * @param aParentComp Composite
	 */
    @Override
    public void createPartControl(final Composite aParentComp) {
        try {
            aParentComp.setLayout(new GridLayout());
            logPanelComp = new Composite(aParentComp, 0);
            final GridLayout gridLayout = new GridLayout();
            gridLayout.numColumns = 7;
            logPanelComp.setLayout(gridLayout);
            logPanelComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            logLevelLbl = new Label(logPanelComp, SWT.NONE);
            logLevelLbl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            logLevelLbl.setText(translator.getString(" Log (Level = " + logLevel + "):"));
            final Label empty1Lbl = new Label(logPanelComp, SWT.NONE);
            empty1Lbl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            caseMatchChk = new Button(logPanelComp, SWT.CHECK);
            caseMatchChk.setText("Match Case");
            searchCmb = new Combo(logPanelComp, SWT.DROP_DOWN);
            final GridData gridData_1 = new GridData(GridData.FILL_HORIZONTAL);
            gridData_1.horizontalSpan = 2;
            gridData_1.widthHint = 150;
            searchCmb.setLayoutData(gridData_1);
            final List histList = RuntimeStatePrefs.getSearchHistory(RuntimeStatePrefs.LOG_WINDOW);
            if ((null != histList) & (histList.size() > 0)) {
                String[] searchHistoryItems;
                int i = 0;
                searchHistoryItems = new String[histList.size()];
                Iterator it;
                for (it = histList.iterator(); it.hasNext(); ) {
                    searchHistoryItems[i++] = (String) it.next();
                    ;
                }
                searchCmb.setItems(searchHistoryItems);
                searchCmb.setText(searchCmb.getItem(0));
            }
            findBtn = ControlFactory.createButton(logPanelComp, "Find");
            findBtn.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent evt) {
                    try {
                        initSearchTool();
                        if (isFindValid()) {
                            searchStartPos = docSearchTool.calculateStartPosition();
                            docSearchTool.searchNext(findStr, searchStartPos, caseMatchChk.getSelection());
                        }
                    } catch (final Exception e) {
                        logger.printStackTrace(e);
                    }
                }
            });
            clearBtn = ControlFactory.createButton(logPanelComp, "Clear");
            logAreaTxt = new StyledText(aParentComp, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.WRAP);
            logAreaTxt.setText("");
            final Menu logAreaTxtMenu = new Menu(logAreaTxt);
            final MenuItem cutitem = new MenuItem(logAreaTxtMenu, SWT.PUSH);
            cutitem.setText("Cut");
            cutitem.setEnabled(false);
            final MenuItem copyItem = new MenuItem(logAreaTxtMenu, SWT.PUSH);
            copyItem.setText("Copy");
            copyItem.setAccelerator(SWT.MOD1 + 'C');
            copyItem.setEnabled(true);
            copyItem.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent event) {
                    logAreaTxt.copy();
                }
            });
            final MenuItem pasteItem = new MenuItem(logAreaTxtMenu, SWT.PUSH);
            pasteItem.setText("Paste");
            pasteItem.setEnabled(false);
            logAreaTxt.setMenu(logAreaTxtMenu);
            logAreaTxt.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_BOTH));
            logAreaTxt.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
            logAreaTxt.setFont(new Font(Display.getCurrent(), GlobalConstants.LOG_FONT_NAME, GlobalConstants.LOG_FONT_HEIGHT, SWT.NORMAL));
            clearBtn.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    logAreaTxt.setText("");
                }
            });
            instance = this;
            XAwareLogger.addHandler(EclipseGuiLogHandler.GUI_LOG, new EclipseGuiLogHandler());
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Initializes DocSearchTool class.
	 */
    protected void initSearchTool() {
        if (docSearchTool == null) {
            if (null != logAreaTxt) {
                docSearchTool = new DocSearchTool(logAreaTxt);
            }
        }
    }

    /**
	 * Validates the find combo box and sets class variable findStr
	 *
	 * @return boolean
	 */
    protected boolean isFindValid() {
        findStr = searchCmb.getText();
        if (findStr.equals("")) {
            ControlFactory.showMessageDialog(translator.getString("Please enter find text."), translator.getString("Information"));
            return false;
        }
        addToHistory(searchCmb, findStr);
        return (true);
    }

    /**
	 * Manages combo box's selection list by capturing search and replace
	 * history.  It also removes the oldest search or replace string once the
	 * size setting within method is reached.
	 *
	 * @param history Vector
	 * @param newVal String
	 */
    protected void addToHistory(final Combo history, final String newVal) {
        if (newVal.trim().length() <= 0) {
            return;
        }
        boolean isExisting = false;
        int i = 0;
        final ArrayList list = new ArrayList(HISTORY_LENGTH + 1);
        for (i = 0; i < history.getItemCount(); i++) {
            final String searchStr = history.getItem(i);
            if (newVal.equals(searchStr)) {
                isExisting = true;
                break;
            }
            list.add(searchStr);
        }
        if (!isExisting) {
            history.add(newVal);
            list.add(newVal);
        }
        if (i > HISTORY_LENGTH) {
            history.remove(0);
            list.remove(0);
        }
        if (!isExisting) {
            RuntimeStatePrefs.setSearchHistory(RuntimeStatePrefs.LOG_WINDOW, list);
        }
    }

    /**
	 * Returns present log level
	 *
	 * @return Level
	 */
    public static Level getLogLevel() {
        return logLevel;
    }

    /**
	 * Sets log level
	 *
	 * @param level Level
	 */
    public void setLogLevel(final Level level) {
        logLevel = level;
        setLogLevelTitle(translator.getString(" Log (Level = " + logLevel + "):"));
    }

    /**
	 * Sets loglevel label with the current Log level name
	 *
	 * @param title String
	 */
    public void setLogLevelTitle(final String title) {
        logLevelLbl.setText(title);
    }

    /**
	 * Passing the focus request to the viewer's control.
	 */
    @Override
    public void setFocus() {
    }

    /**
	 * Returns LogView instance. this reference is used to get LogArea for
	 * appending log messages
	 *
	 * @return ViewPart
	 */
    public static LogView getInstance() {
        return instance;
    }

    /**
	 * Returns Log textarea
	 *
	 * @return StyledText
	 */
    public StyledText getLogTextArea() {
        return logAreaTxt;
    }

    /**
	 * Called by the garbage collector when there are no more references to
	 * this object
	 *
	 * @throws Throwable Throwable
	 */
    @Override
    protected void finalize() throws Throwable {
        final Control[] controls = logPanelComp.getChildren();
        final int noOfChildren = controls.length;
        for (int i = 0; i < noOfChildren; i++) {
            if (!controls[i].isDisposed()) {
                controls[i].dispose();
            }
        }
        if (!logPanelComp.isDisposed()) {
            logPanelComp.dispose();
        }
        super.finalize();
    }

    /**
	 * Returns Search combo text
	 *
	 * @return String
	 */
    public String getLogSearchStr() {
        if (!searchCmb.isDisposed()) {
            logSearchStr = searchCmb.getText();
        } else {
            logSearchStr = "";
        }
        return logSearchStr;
    }

    /**
	 * Sets search string to visiable in search combo
	 *
	 * @param searchStr String
	 */
    public void setLogSearchStr(final String searchStr) {
        if (!searchCmb.isDisposed()) {
            if (searchStr != null) {
                searchCmb.setText(searchStr);
            } else if (searchCmb.getItemCount() > 0) {
                searchCmb.setText(searchCmb.getItem(0));
            }
        }
    }

    /**
	 * Get Adapter method providing access to the dynamic help's context
	 * provider for the XAware Log viewer.
	 */
    @Override
    public Object getAdapter(final Class adapter) {
        if (adapter.equals(IContextProvider.class)) {
            if (contextProvider == null) {
                contextProvider = new LogViewContextProvider();
            }
            return contextProvider;
        }
        return super.getAdapter(adapter);
    }

    /**
	 * Context provider for the XAware Log view
	 */
    protected class LogViewContextProvider implements IContextProvider {

        private static final String contextIdText = "org.xaware.help.xaware_log_view_context";

        private final IContext context = HelpSystem.getContext(contextIdText);

        public IContext getContext(final Object target) {
            return context;
        }

        public int getContextChangeMask() {
            return IContextProvider.NONE;
        }

        public String getSearchExpression(final Object target) {
            return null;
        }
    }
}
