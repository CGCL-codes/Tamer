package com.vektorsoft.acapulco.gui.menu.edit.extensions;

import com.vektorsoft.acapulco.core.annotation.ExtensionPoint;
import com.vektorsoft.acapulco.core.annotation.Listener;
import com.vektorsoft.acapulco.gui.menu.edit.shared.EditMenuConstants;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * <p>
 *  Action for handling Paste operation. Subclasses should override {@link #onPaste() } method in order to 
 * add custom logic.
 * </p>
 *
 * @author Vladimir Djurovic
 */
@Listener(componentNames = { EditMenuConstants.EDIT_PASTE_NAME, EditMenuConstants.EDIT_TOOLBAR_PASTE_NAME })
@ExtensionPoint
public class PasteAction extends AbstractAction {

    /** Clipboard used for paste operation. */
    protected Clipboard clipboard;

    /**
     * Creates new instance.
     */
    public PasteAction() {
        super();
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    /**
     * Handles the event. Subclasses should override protected methods to customize behavior.
     * 
     * @param e event
     */
    @Override
    public final void actionPerformed(ActionEvent e) {
        onPaste();
    }

    /**
     * <p>
     * Override this method to add custom paste logic. A sample code is given bellow:
     * </p>
     * <p>
     *  <pre>
     *      <code>
     *          &#64;Override
     *          protected void onPaste(){
     *              Object contents = clipboard.getContents(this);
     *              // do something with clipboard contents
     *          }
     *      </code>
     *  </pre>
     * </p>
     */
    protected void onPaste() {
    }
}
