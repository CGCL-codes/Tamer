package javaframework.uilayer.swing.popupmessages;

import java.awt.Component;
import javaframework.uilayer.image.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Represent a popup dialog to interact with the user. 
 *
 * <br/><br/>
 *
 * <b><u>Dependencies</u></b><br/>
 * Base
 * Image
 * 
 * <br/><br/>
 * 
 * <b><u>Design notes</u></b><br/>
 * -
 * <br/><br/>
 *
 * <b>· Creation time:</b> 10/11/2011<br/>
 * <b>· Revisions:</b><br/><br/>
 * <b><u>State</u></b><br/>
 * <b>· Debugged:</b> Yes<br/>
 * <b>· Structural tests:</b> -<br/>
 * <b>· Functional tests:</b> -<br/>
 *
 * @author Francisco Pérez R. de V. (franjfw{@literal @}yahoo.es)
 * @version JavaFramework.0.1.0.en
 * @version PopupMessage.0.0.1
 * @since JavaFramework.0.1.0.en
 * @see <a href=””></a>
 *
 */
public final class PopupMessage implements InterfacePopupMessage {

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final void showMessage(final Component asociatedWindow, final String title, final String message, final TypesOfMessage typeOfMessage, final Icon icon) {
        ImageIcon imgIcon = null;
        if (icon != null) imgIcon = icon.getIconInFile();
        JOptionPane.showMessageDialog(asociatedWindow, message, title, typeOfMessage.getValue(), imgIcon);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final SelectedOption showConfirmationMessage(final Component associatedWindow, final String title, final String message, final TypesOfDialog typeOfDialog, final TypesOfMessage typeOfMessage, final Icon icon) {
        ImageIcon imgIcon = null;
        if (icon != null) imgIcon = icon.getIconInFile();
        return SelectedOption.getName(JOptionPane.showConfirmDialog(associatedWindow, message, title, typeOfDialog.getValue(), typeOfMessage.getValue(), imgIcon));
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final String showInputMessage(final Component associatedWindow, final String title, final String message, final TypesOfMessage typeOfMessage, final Icon icon, String[] shownValues, String defaultValue) {
        ImageIcon imgIcon = null;
        if (icon != null) imgIcon = icon.getIconInFile();
        return (String) (JOptionPane.showInputDialog(associatedWindow, message, title, typeOfMessage.getValue(), imgIcon, shownValues, defaultValue));
    }
}
