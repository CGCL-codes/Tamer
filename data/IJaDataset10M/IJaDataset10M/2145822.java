package si.mk.k3;

import javax.swing.JFrame;
import si.mk.k3.ctrl.Controller;
import si.mk.k3.model.ParameterContainer;

/**
 * Provides access to application objects needed by many subsystems.
 */
public interface ApplicationAccess {

    Controller getActiveController();

    JFrame getActiveFrame();

    /**
     * This method provides access to applicatin parameters. Use it only, when
     * undo functionality does not make sense - for example, when data is
     * modified by dialog from the main menu, and does not affect visual
     * appearance of the model. Example of such data is name of the user, who
     * created the model.
     * 
     * @return
     */
    ParameterContainer getApplicationParameters();
}
