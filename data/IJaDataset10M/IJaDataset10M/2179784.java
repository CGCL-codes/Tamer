package sce.actions.providers;

import java.text.MessageFormat;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import sce.actions.GenerateActionProvider;
import sce.actions.PreferencesProvider;
import sce.parser.SceneryGenerator;
import sce.util.Messages;
import sce.util.Preferences;
import sce.view.SelectableHolder;
import sce.view.TextHolder;
import sce.view.TextHolderUser;
import sce.view.UndoableEvent;
import vo.Area;
import vo.Scenery;

/**
 * Main implementation of the generator. Provides a dialogs to help users cancel
 * the action and assess progress.
 * 
 * @author Andrew Thornton <art27@cantab.net>
 */
public class GenerateActionProviderImpl implements GenerateActionProvider, TextHolderUser {

    private final class WorkingThread extends Thread {

        private final DialogValues dv;

        private final String[] directories;

        private final SceneryGenerator sceneryGenerator;

        private List<Area> areas;

        private WorkingThread(String[] aliasFrom, String groupName, String[] aliasTo, Scenery scenery, String friendlyPrefix, DialogValues dv, String[] directories) {
            this.dv = dv;
            this.directories = directories;
            this.sceneryGenerator = new SceneryGenerator(scenery, friendlyPrefix, aliasFrom, aliasTo, dv);
        }

        @Override
        public synchronized void run() {
            sceneryGenerator.generateSceneriesFromDirectories(directories);
            if (!dv.isStopped()) {
                areas = sceneryGenerator.getAreas();
                dv.finished();
            } else {
                dv.setNewlyFound(-1);
            }
        }

        public synchronized List<Area> getAreas() {
            if (dv.isFinished) {
                return areas;
            }
            return null;
        }
    }

    /**
     * Implementation of the DialogValues class in SceneryGenerator. Provides an
     * SWT dialog to assess progress.
     * 
     * @author Andrew Thornton <art27@cantab.net>
     */
    private static class DialogValues implements SceneryGenerator.DialogValuesInterface {

        private Label generating;

        private Label discovered;

        private Label totalFound;

        int totalFoundValue = 0;

        int newlyFoundValue = 0;

        private boolean isStopped;

        private String generatingDirectory = "";

        private String discoveredDirectory = "";

        private boolean isFinished;

        /**
         * Creates a thread to update the given labels
         * 
         * @param generating
         *            label to represent directory currently being generated
         *            from
         * @param discovered
         *            label to represent the current directory being examine
         * @param totalFound
         *            label to represent the currently found areas
         */
        public DialogValues(Label generating, Label discovered, Label totalFound) {
            super();
            this.generating = generating;
            this.discovered = discovered;
            this.totalFound = totalFound;
            setupDialogThread();
        }

        private void setupDialogThread() {
            final DialogValues dialogValues = this;
            Thread dialogThread = new Thread() {

                @Override
                public void run() {
                    while (!isStopped()) {
                        if (generating.isDisposed()) {
                            dialogValues.stop();
                        } else {
                            generating.getDisplay().syncExec(new Runnable() {

                                @Override
                                public void run() {
                                    if (!generating.isDisposed()) {
                                        updateDisplay();
                                    }
                                }
                            });
                            Thread.yield();
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                }
            };
            dialogThread.start();
        }

        @Override
        public synchronized void setGenerationDirectory(final String generationDirectory) {
            generatingDirectory = generationDirectory;
        }

        @Override
        public synchronized void setDiscoveredDirectory(final String discoveredDirectory) {
            this.discoveredDirectory = discoveredDirectory;
        }

        @Override
        public synchronized void incrementTotalFound() {
            ++totalFoundValue;
        }

        @Override
        public synchronized void incrementNewlyFound() {
            ++newlyFoundValue;
        }

        @Override
        public synchronized void stop() {
            isStopped = true;
        }

        @Override
        public synchronized boolean isStopped() {
            return isStopped;
        }

        public synchronized int getNewlyFound() {
            return newlyFoundValue;
        }

        public synchronized void setNewlyFound(int i) {
            this.newlyFoundValue = i;
        }

        public synchronized void finished() {
            isFinished = true;
        }

        public synchronized boolean isFinished() {
            return isFinished;
        }

        public void open(Thread thread) {
            Shell shell = generating.getShell();
            Display display = shell.getDisplay();
            shell.pack();
            shell.open();
            thread.start();
            while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                    if (!thread.isAlive()) {
                        shell.close();
                    }
                }
            }
        }

        public synchronized void updateDisplay() {
            if (generating.isDisposed() || discovered.isDisposed() || totalFound.isDisposed()) {
                return;
            }
            if (!isFinished()) {
                generating.setText(generatingDirectory);
                String s = discoveredDirectory.substring(generatingDirectory.length());
                s = Messages.getString("GenerateActionProviderImpl.Elipsis") + s.substring(s.length() > 60 ? s.length() - 60 : 0);
                discovered.setText(s);
            } else {
                generating.setText(Messages.getString("GenerateActionProviderImpl.Finished"));
                discovered.setText("");
            }
            generating.redraw();
            discovered.redraw();
            totalFound.setText(MessageFormat.format(Messages.getString("GenerateActionProviderImpl.CurrentlyDiscoveredMessage"), new Object[] { new Integer(totalFoundValue), new Integer(newlyFoundValue) }));
            totalFound.redraw();
        }
    }

    private TextHolder holder;

    private PreferencesProvider p;

    private Shell parent;

    @Override
    public void setTextHolder(TextHolder holder) {
        this.holder = holder;
    }

    public void setPreferencesProvider(PreferencesProvider p) {
        this.p = p;
    }

    public void setShell(Shell shell) {
        this.parent = shell;
    }

    @Override
    public UndoableEvent generateFrom(String directory, String prefix, String groupName) {
        return generateFrom(new String[] { directory }, prefix, groupName);
    }

    @Override
    public UndoableEvent generateFrom(final String[] directories, final String friendlyPrefix, final String groupName) {
        Preferences preferences = p.getPreferences();
        final String[] aliasFrom = new String[] { preferences.flightSimPath, preferences.aliasFrom };
        final String[] aliasTo = new String[] { "", preferences.aliasTo };
        final DialogValues dv = createDialog();
        WorkingThread workingThread = new WorkingThread(aliasFrom, groupName, aliasTo, holder.getScenery(), friendlyPrefix, dv, directories);
        dv.open(workingThread);
        int numGenerated = dv.getNewlyFound();
        MessageBox mBox = new MessageBox(parent, SWT.OK | SWT.ICON_INFORMATION);
        UndoableEvent event = null;
        if (numGenerated < 0) {
            mBox.setText(Messages.getString("GenerateActionProviderImpl.GenerationCancelledTitle"));
            mBox.setMessage(Messages.getString("GenerateActionProviderImpl.GenerationCancelledMessage"));
        } else {
            final List<Area> areas = workingThread.getAreas();
            event = new UndoableEvent() {

                @Override
                public UndoableEvent run() {
                    final UndoableEvent redo = this;
                    Scenery scenery = holder.getScenery();
                    for (Area area : areas) {
                        area.addGroup(groupName);
                        scenery.add(area);
                    }
                    holder.setScenery(scenery);
                    if (holder instanceof SelectableHolder) {
                        int[] priorities = new int[areas.size()];
                        for (int i = 0; i < areas.size(); priorities[i] = ++i) ;
                        SelectableHolder sHolder = (SelectableHolder) holder;
                        sHolder.selectAreas(priorities);
                    }
                    return new UndoableEvent() {

                        @Override
                        public UndoableEvent run() {
                            Scenery scenery = holder.getScenery();
                            for (Area area : areas) {
                                scenery.remove(scenery.getAreaByNumber(area.getAreaNumber()));
                            }
                            holder.setScenery(scenery);
                            return redo;
                        }
                    };
                }
            };
            mBox.setText(Messages.getString("GenerateActionProviderImpl.GenerationResultsTitle"));
            mBox.setMessage(MessageFormat.format(Messages.getString("GenerateActionProviderImpl.GenerationResultsMessage"), new Object[] { Integer.toString(numGenerated) }));
        }
        mBox.open();
        return event;
    }

    private DialogValues createDialog() {
        final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
        GridLayout shellLayout = new GridLayout();
        shell.setLayout(shellLayout);
        shell.setText(Messages.getString("GenerateActionProviderImpl.Title"));
        Composite basicComposite = new Composite(shell, SWT.NONE);
        basicComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        basicComposite.setLayout(gridLayout);
        Label label = new Label(basicComposite, SWT.NONE);
        label.setText(Messages.getString("GenerateActionProviderImpl.GeneratingFromLabel"));
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 400;
        final Label rootLabel = new Label(basicComposite, SWT.NONE);
        rootLabel.setText("-");
        rootLabel.setLayoutData(data);
        label = new Label(basicComposite, SWT.NONE);
        label.setText(Messages.getString("GenerateActionProviderImpl.ExamingLabel"));
        final Label currentLabel = new Label(basicComposite, SWT.NONE);
        currentLabel.setText("-");
        currentLabel.setLayoutData(data);
        label = new Label(basicComposite, SWT.NONE);
        label.setText(Messages.getString("GenerateActionProviderImpl.CurrentlyDiscoveredLabel"));
        final Label discoveredLabel = new Label(basicComposite, SWT.NONE);
        discoveredLabel.setText(MessageFormat.format(Messages.getString("GenerateActionProviderImpl.CurrentlyDiscoveredMessage"), new Object[] { "-", "-" }));
        discoveredLabel.setLayoutData(data);
        Composite buttonComposite = new Composite(shell, SWT.NONE);
        buttonComposite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        buttonComposite.setLayout(layout);
        Button cancelButton = new Button(buttonComposite, SWT.PUSH);
        cancelButton.setText(Messages.getString("GenerateActionProviderImpl.Cancel"));
        cancelButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.close();
            }
        });
        final DialogValues dv = new DialogValues(rootLabel, currentLabel, discoveredLabel);
        shell.addShellListener(new ShellAdapter() {

            @Override
            public void shellClosed(ShellEvent e) {
                if (!dv.isStopped()) {
                    dv.stop();
                }
                e.doit = true;
            }
        });
        return dv;
    }
}
