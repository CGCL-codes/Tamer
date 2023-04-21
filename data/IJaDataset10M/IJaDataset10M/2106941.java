package ca.sqlpower.architect.swingui.action;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.ProgressMonitorInputStream;
import org.apache.log4j.Logger;
import ca.sqlpower.architect.swingui.ASUtils;
import ca.sqlpower.architect.swingui.ArchitectFrame;
import ca.sqlpower.architect.swingui.ArchitectSwingSession;
import ca.sqlpower.architect.swingui.ArchitectSwingSessionContext;
import ca.sqlpower.architect.swingui.dbtree.DBTreeModel;
import ca.sqlpower.sqlobject.SQLObjectException;
import ca.sqlpower.swingui.RecentMenu;
import ca.sqlpower.swingui.SPSUtils;
import ca.sqlpower.swingui.SPSwingWorker;

public class OpenProjectAction extends AbstractArchitectAction {

    private static final Logger logger = Logger.getLogger(OpenProjectAction.class);

    public static interface FileLoader {

        public void open(ArchitectSwingSession newSession, File f, ArchitectSwingSession openingSession, boolean separateThread);

        public void open(ArchitectSwingSession newSession, InputStream in, ArchitectSwingSession openingSession, boolean separateThread);
    }

    /**
     * Opens a project file into the given session using a separate worker
     * thread. A dialog box with a progress bar will be displayed during the
     * load process, and any errors that are encountered during the load will be
     * displayed in additional dialogs.
     * <p>
     * Note that this method always returns immediately, so as the caller of
     * this method you have no way of knowing if the load has worked/will work.
     * 
     * @param newSession
     *            The session in which to load the project into.
     * @param f
     *            The project file to load.
     * @param openingSession
     *            The session from which this openAsynchronously call is made.
     *            If the session being opened is the first session being
     *            created, then simply set to null. If the
     *            openingSession.isNew() returns true, (i.e. it's an new, empty,
     *            and unmodified project) then openingSession.close() will be
     *            called once the project is finished loading.
     */
    private static FileLoader fileLoader = new FileLoader() {

        public void open(ArchitectSwingSession newSession, File f, ArchitectSwingSession openingSession, boolean separateThread) {
            LoadFileWorker worker;
            try {
                worker = new LoadFileWorker(f, newSession, openingSession);
                if (separateThread) {
                    new Thread(worker).start();
                } else {
                    worker.run();
                }
            } catch (Exception e1) {
                ASUtils.showExceptionDialogNoReport(Messages.getString("OpenProjectAction.errorLoadingFile"), e1);
            }
        }

        public void open(ArchitectSwingSession newSession, InputStream in, ArchitectSwingSession openingSession, boolean separateThread) {
            LoadFileWorker worker;
            try {
                worker = new LoadFileWorker(in, newSession, openingSession);
                if (separateThread) {
                    new Thread(worker).start();
                } else {
                    worker.run();
                }
            } catch (Exception e1) {
                ASUtils.showExceptionDialogNoReport(Messages.getString("OpenProjectAction.errorLoadingFile"), e1);
            }
        }
    };

    public static void setFileLoader(FileLoader loader) {
        fileLoader = loader;
    }

    public static FileLoader getFileLoader() {
        return fileLoader;
    }

    public OpenProjectAction(ArchitectFrame frame) {
        super(frame, Messages.getString("OpenProjectAction.name"), Messages.getString("OpenProjectAction.description"), "folder");
        putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }

    public void actionPerformed(ActionEvent e) {
        File f;
        if (e.getActionCommand() == null || !e.getActionCommand().startsWith("file:")) {
            JFileChooser chooser = new JFileChooser(getSession().getRecentMenu().getMostRecentFile());
            chooser.addChoosableFileFilter(SPSUtils.ARCHITECT_FILE_FILTER);
            int returnVal = chooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                f = chooser.getSelectedFile();
            } else {
                return;
            }
        } else {
            f = new File(e.getActionCommand().substring("file:".length()));
        }
        try {
            fileLoader.open(getSession().getContext().createSession(), f, getSession(), true);
        } catch (SQLObjectException ex) {
            SPSUtils.showExceptionDialogNoReport(getSession().getArchitectFrame(), Messages.getString("OpenProjectAction.failedToOpenProjectFile"), ex);
        }
    }

    /**
     * A worker for asynchronously loading a new project file.
     */
    public static class LoadFileWorker extends SPSwingWorker {

        private final ArchitectSwingSessionContext context;

        private final InputStream in;

        private final File file;

        private final RecentMenu recent;

        private final ArchitectSwingSession openingSession;

        /**
         * The session that will get created if loading the file in doStuff() is
         * successful.
         */
        private ArchitectSwingSession session;

        /**
         * Load file worker creates a new worker and opens the given file.
         * 
         * @param file
         *            this file gets opened in the constructor
         * @param newSession
         *            The session in which the project file should be opened
         * @param openingSession
         *            The session from which the open project operation is being
         *            called. Should not be null and should have a frame. If the
         *            openingSession.isNew() returns true, (i.e. it's an new,
         *            empty, and unmodified project) then openingSession.close()
         *            will be called once the project is finished loading.
         * @throws SQLObjectException
         *             when the project creation fails.
         * @throws FileNotFoundException
         *             if file doesn't exist
         */
        public LoadFileWorker(File file, ArchitectSwingSession newSession, ArchitectSwingSession openingSession) throws SQLObjectException, FileNotFoundException {
            super(newSession);
            this.context = newSession.getContext();
            this.file = file;
            this.recent = newSession.getRecentMenu();
            this.openingSession = openingSession;
            this.session = newSession;
            in = new BufferedInputStream(new ProgressMonitorInputStream(openingSession.getArchitectFrame(), Messages.getString("OpenProjectAction.reading") + file.getName(), new FileInputStream(file)));
        }

        public LoadFileWorker(InputStream in, ArchitectSwingSession newSession, ArchitectSwingSession openingSession) {
            super(newSession);
            this.context = newSession.getContext();
            file = null;
            this.recent = newSession.getRecentMenu();
            this.openingSession = openingSession;
            this.session = newSession;
            this.in = in;
        }

        @Override
        public void doStuff() throws Exception {
            session.getProjectLoader().load(in, session.getDataSources(), openingSession);
            session.getProjectLoader().setFile(file);
        }

        @Override
        public void cleanup() throws SQLObjectException {
            if (getDoStuffException() != null) {
                Throwable cause = getDoStuffException().getCause();
                if (!(getDoStuffException() instanceof InterruptedIOException) && (!(cause instanceof InterruptedIOException) || !(cause.getMessage().equals("progress")))) {
                    ASUtils.showExceptionDialogNoReport(Messages.getString("OpenProjectAction.cannotOpenProjectFile") + file.getAbsolutePath(), getDoStuffException());
                    logger.error("Got exception while opening a project", getDoStuffException());
                }
                session.removeSwingWorker(this);
                if (session.getContext().getSessions().size() > 1) {
                    session.getProjectLoader().setModified(false);
                    session.close();
                }
            } else {
                if (file != null) {
                    recent.putRecentFileName(file.getAbsolutePath());
                }
                if (openingSession != null) {
                    openingSession.getArchitectFrame().addSession(session);
                    openingSession.getArchitectFrame().setCurrentSession(session);
                }
                ((DBTreeModel) session.getDBTree().getModel()).refreshTreeStructure();
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ie) {
                logger.error("got exception while closing project file", ie);
            }
        }
    }
}
