package ssg.tools.common.fileUtilities.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JMenuItem;

/**
 * Declares core enumerations for FC actions (domain, action, availability).
 * Lists methods (with useful overloads) for actions validation and execution.
 * Declares method to retrieve registered actions - listActions().
 * Additionally provides utility methods to convert texts to/from actionCommand .
 * @author ssg
 */
public interface IActionExecutor extends ActionListener {

    public static enum ACTION_AVAILABILITY {

        enabled, disabled, notAllowed
    }

    public static enum ACTION_DOMAIN {

        DS, EOP, FFI, NE, Other
    }

    public static enum ACTION_BASIC {

        New, Edit, Delete, Append, Cut, Insert, Replace, Other
    }

    void performAction(ActionEvent event);

    void performAction(JMenuItem mi, String[] parameters);

    void performAction(JMenuItem mi, Object item);

    void performAction(String domain, String action, Object item);

    ACTION_AVAILABILITY validateAction(ActionEvent event);

    ACTION_AVAILABILITY validateAction(JMenuItem mi, String[] parameters);

    ACTION_AVAILABILITY validateAction(JMenuItem mi, Object item);

    ACTION_AVAILABILITY validateAction(String domain, String action, Object item);

    List<FCAction> listActions(String actionCommand);

    ACTION_DOMAIN stringToDomain(String actionCommand);

    ACTION_BASIC stringToAction(String actionCommand);

    String actionToCommand(String domain, String action);
}
