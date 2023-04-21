package proper.gui.core.dialog;

import proper.gui.core.frame.DummyFrame;
import proper.gui.core.frame.ProperFrame;

/**
* This class represents a MessageBox with a information.
*
* @author      FracPete
* @version $Revision: 1.2 $
*/
public class InformationBox extends MessageBox {

    /**
   * initializes the dialog
   */
    public InformationBox(String title, String text) {
        this(new DummyFrame(), title, text);
    }

    /**
   * initializes the dialog
   */
    public InformationBox(ProperFrame parent, String title, String text) {
        super(parent, title, text, MessageBox.BUTTONS_OK, MessageBox.ICON_INFORMATION);
    }
}
