package org.jlense.uiworks.dialogs;

import java.awt.Image;
import javax.swing.JComponent;

/**
 * Interface for a page in a multi-page dialog.
 */
public interface IDialogPage {

    /**
     * Creates the top level control for this dialog
     * page under the given parent composite.
     * <p>
     * Implementors are responsible for ensuring that
     * the created control can be accessed via <code>getControl</code>
     * </p>
     *
     * @param parent the parent composite
     */
    public JComponent createControl();

    /**
     * Returns the top level control for this dialog page.
     *
     * @return the top level control
     */
    public JComponent getControl();

    /**
     * Returns this dialog page's description text.
     *
     * @return the description text for this dialog page, 
     *  or <code>null</code> if none
     */
    public String getDescription();

    /**
     * Returns the current error message for this dialog page.
     * May be <code>null</null> to indicate no error message.
     * <p>
     * An error message should describe some error state,
     * as opposed to a message which may simply provide instruction
     * or information to the user.
     * </p>
     * 
     * @return the error message, or <code>null</code> if none
     */
    public String getErrorMessage();

    /**
     * Returns this dialog page's image.
     *
     * @return the image for this dialog page, or <code>null</code>
     *  if none
     */
    public Image getImage();

    /**
     * Returns the current message for this wizard page.
     * <p>
     * A message provides instruction or information to the 
     * user, as opposed to an error message which should 
     * describe some error state.
     * </p>
     * 
     * @return the message, or <code>null</code> if none
     */
    public String getMessage();

    /**
     * Returns this dialog page's title.
     *
     * @return the title of this dialog page, 
     *  or <code>null</code> if none
     */
    public String getTitle();

    /**
     * Notifies that help has been requested for this dialog page.
     */
    public void performHelp();

    /**
     * Sets this dialog page's description text.
     * 
     * @param description the description text for this dialog
     *  page, or <code>null</code> if none
     */
    public void setDescription(String description);

    /**
     * Sets this dialog page's image.
     *
     * @param image the image for this dialog page, 
     *  or <code>null</code> if none
     */
    public void setImage(Image image);

    /**
     * Set this dialog page's title.
     *
     * @param title the title of this dialog page, 
     *  or <code>null</code> if none
     */
    public void setTitle(String title);

    /**
     * Sets the visibility of this dialog page.
     *
     * @param visible <code>true</code> to make this page visible,
     *  and <code>false</code> to hide it
     */
    public void setVisible(boolean visible);
}
