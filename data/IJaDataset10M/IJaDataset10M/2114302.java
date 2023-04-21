package org.jhotdraw.app.action;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.jhotdraw.app.Application;
import org.jhotdraw.app.Project;
import org.jhotdraw.gui.JSheet;
import org.jhotdraw.gui.Worker;
import org.jhotdraw.gui.event.SheetEvent;
import org.jhotdraw.gui.event.SheetListener;

/**
 * OpenRecentAction.
 *
 * @author Werner Randelshofer.
 * @version 1.0 June 15, 2006 Created.
 */
public class OpenRecentAction extends AbstractApplicationAction {

    public static final String ID = "openRecent";

    private File file;

    /** Creates a new instance. */
    public OpenRecentAction(Application app, File file) {
        super(app);
        this.file = file;
        putValue(Action.NAME, file.getName());
    }

    public void actionPerformed(ActionEvent evt) {
        final Application app = getApplication();
        if (app.isEnabled()) {
            app.setEnabled(false);
            Project emptyProject = app.getCurrentProject();
            if (emptyProject == null || emptyProject.getFile() != null || emptyProject.hasUnsavedChanges()) {
                emptyProject = null;
            }
            final Project p;
            if (emptyProject == null) {
                p = app.createProject();
                app.add(p);
                app.show(p);
            } else {
                p = emptyProject;
            }
            openFile(p);
        }
    }

    protected void openFile(final Project project) {
        final Application app = getApplication();
        app.setEnabled(true);
        int multipleOpenId = 1;
        for (Project aProject : app.projects()) {
            if (aProject != project && aProject.getFile() != null && aProject.getFile().equals(file)) {
                multipleOpenId = Math.max(multipleOpenId, aProject.getMultipleOpenId() + 1);
            }
        }
        project.setMultipleOpenId(multipleOpenId);
        project.setEnabled(false);
        project.execute(new Worker() {

            public Object construct() {
                try {
                    project.read(file);
                    return null;
                } catch (Throwable e) {
                    return e;
                }
            }

            public void finished(Object value) {
                fileOpened(project, file, value);
            }
        });
    }

    protected void fileOpened(final Project project, File file, Object value) {
        final Application app = getApplication();
        if (value == null) {
            project.setFile(file);
            project.setEnabled(true);
            Frame w = (Frame) SwingUtilities.getWindowAncestor(project.getComponent());
            if (w != null) {
                w.setExtendedState(w.getExtendedState() & ~Frame.ICONIFIED);
                w.toFront();
            }
            project.getComponent().requestFocus();
            if (app != null) {
                app.setEnabled(true);
            }
        } else {
            if (value instanceof Throwable) {
                ((Throwable) value).printStackTrace();
            }
            JSheet.showMessageSheet(project.getComponent(), "<html>" + UIManager.getString("OptionPane.css") + "<b>Couldn't open the file \"" + file + "\".</b><br>" + value, JOptionPane.ERROR_MESSAGE, new SheetListener() {

                public void optionSelected(SheetEvent evt) {
                }
            });
        }
    }
}
