package org.galaxy.gpf.event;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.galaxy.gpf.application.Project;
import org.galaxy.gpf.ui.ProjectCreator;

/**
 * Action for creating a new file.
 * 
 * @author Cheng Liang
 * @version 1.0.0
 */
public class FileCreateAction extends AbstractApplicationEvent {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8570931128820554937L;

    /** The action id. */
    public static final String id = "action.file.new";

    /** The action. */
    private static FileCreateAction action = new FileCreateAction();

    /**
	 * Instantiates a new file new action.
	 */
    private FileCreateAction() {
        super(id);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ProjectCreator dialog = new ProjectCreator();
        Project project = dialog.show();
        getModel().addProject(project);
    }

    /**
	 * Gets the singleton action in application.
	 * 
	 * @return the singleton action
	 */
    public static Action getAction() {
        return action;
    }
}
