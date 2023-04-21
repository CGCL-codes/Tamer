package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import org.argouml.ui.ProjectBrowser;

/**
 * Reopens a project with respect of the calling event handler - should be
 * used with menu item.
 *
 * @author  Frank Jelinek
 * @since 10. November 2003 (0.15.2)
 */
public class ActionReopenProject extends AbstractAction {

    private String filename;

    /**
     * Constructor.
     *
     * @param theFilename The name of the file.
     */
    public ActionReopenProject(String theFilename) {
        super("action.reopen-project");
        filename = theFilename;
    }

    /**
     * Get the filename for comparing during menu creation.
     *
     * @return The filename.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Performs the save and reload of a project.
     *
     * @param e e should old the event and the eventsource. Event
     * source is the menu item, the text is used for opening the
     * project
     */
    public void actionPerformed(ActionEvent e) {
        if (!ProjectBrowser.getInstance().askConfirmationAndSave()) return;
        File toOpen = new File(filename);
        ProjectBrowser.getInstance().loadProjectWithProgressMonitor(toOpen, true);
    }
}
