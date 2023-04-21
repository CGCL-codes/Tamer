package org.gamegineer.common.ui.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;

/**
 * A page in a multi-page dialog.
 */
public interface IDialogPage {

    /**
     * Creates the dialog page.
     * 
     * <p>
     * The top-level component created by this method must be accessible via
     * {@link #getContent()}.
     * </p>
     * 
     * @param parent
     *        The parent container for the dialog page; must not be {@code null}
     *        .
     * 
     * @throws java.lang.NullPointerException
     *         If {@code parent} is {@code null}.
     */
    public void create(Container parent);

    /**
     * Disposes the resources used by the dialog page.
     */
    public void dispose();

    public Component getContent();

    public String getDescription();

    public DialogMessage getMessage();

    public Window getShell();

    public String getTitle();

    /**
     * Sets the dialog page message.
     * 
     * @param message
     *        The dialog page message or {@code null} to clear the message.
     */
    public void setMessage(DialogMessage message);

    /**
     * Sets the visibility of the dialog page.
     * 
     * @param isVisible
     *        {@code true} to make the dialog page visible or {@code false} to
     *        hide it.
     */
    public void setVisible(boolean isVisible);
}
