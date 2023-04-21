package net.sf.refactorit.ui;

import net.sf.refactorit.classmodel.Project;
import net.sf.refactorit.common.util.ProgressListener;
import net.sf.refactorit.commonIDE.IDEController;
import net.sf.refactorit.source.SourceParsingException;
import javax.swing.SwingUtilities;

/**
 * Thread for the parsing of sources by parser API.
 *
 * @author Vladislav Vislogubov
 */
public class ParsingThread extends Thread {

    ParsingMonitor monitor;

    private Project project;

    private boolean clean;

    private boolean showCriticalErrorsIfNeeded;

    public ParsingThread(Project project, ParsingMonitor monitor, boolean clean, boolean showCriticalErrorsIfNeeded) {
        this.project = project;
        this.monitor = monitor;
        this.clean = clean;
        this.showCriticalErrorsIfNeeded = showCriticalErrorsIfNeeded;
    }

    public void run() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                monitor.parsingStarting();
            }
        });
        ProgressListener p = new ProgressListener() {

            public void showMessage(final String message) {
            }

            public void progressHappened(final float percents) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        monitor.parsingProgress(percents);
                    }
                });
            }
        };
        try {
            if (clean) {
                project.clean();
            } else {
                project.getProjectLoader().build(p, true);
            }
        } catch (Exception e) {
            if (e instanceof SourceParsingException && ((SourceParsingException) e).justInformsThatUserFriendlyErrorsExist()) {
            } else {
                e.printStackTrace();
            }
        }
        if (showCriticalErrorsIfNeeded && (project.getProjectLoader().getErrorCollector()).hasCriticalUserErrors()) {
            DialogManager.getInstance().showCriticalError(IDEController.getInstance().createProjectContext(), project);
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                monitor.parsingFinished(false);
            }
        });
    }
}
