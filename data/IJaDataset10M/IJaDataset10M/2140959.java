package org.ujac.web.servlet;

import org.ujac.form.Form;

/**
 * Name: ActionRegistry<br>
 * Description: Interface for action registries.
 * <br>Log: $Log$
 * <br>Log: Revision 1.1  2004/09/14 06:24:12  lauerc
 * <br>Log: Initial revision.
 * <br>Log:
 * @author $Author: lauerc $
 * @version $Revision: 1652 $
 */
public interface ActionRegistry {

    /**
   * Gets an action.
   * @param name The action name.
   * @return The according action or null.
   */
    Action getAction(String name);

    /**
   * Creates a form instance.
   * @param name The name of the form prototype.
   * @return The according form or null.
   */
    Form createForm(String name);
}
