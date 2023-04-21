package edu.gsbme.wasabi.UI.Dialog.System;

import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Element;
import edu.gsbme.MMLParser2.Vocabulary.Attributes;

/**
 * State variable system dialog
 * @author David
 *
 */
public class StateVariableSystemDialog extends GenericSystemVariableDialog {

    public StateVariableSystemDialog(Shell shell, Element element) {
        super(shell, "State Variable Dialog", "State Variable Attributes", "", element.getAttribute(Attributes.name.toString()), element.getAttribute(Attributes.units.toString()));
    }

    public StateVariableSystemDialog(Shell shell) {
        super(shell, "State Variable Dialog", "State Variable Attributes", "", "", "");
    }
}
