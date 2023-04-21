package org.ujac.web.servlet;

import org.ujac.form.Form;

/**
 * Name: ActionUnit<br>
 * Description: An interface for bundles of related actions and forms.
 * <br>Log: $Log$
 * <br>Log: Revision 1.1  2004/09/14 07:03:48  lauerc
 * <br>Log: Initial revision.
 * <br>Log:
 * @author $Author: lauerc $
 * @version $Revision: 1654 $
 */
public interface ActionUnit {

    /**
   * Gets all actions, available for this unit.
   * @return The actions that belong to this unit.
   */
    ActionTuple[] getActions();

    /**
   * Gets all forms, available for this unit.
   * @return The forms that belong to this unit.
   */
    Form[] getForms();
}
