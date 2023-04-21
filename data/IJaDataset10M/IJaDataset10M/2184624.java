package org.photovault.swingui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 This action changes the selection in PhotoCollectionThumbView to the next or
 previous photo.
 */
public class ChangeSelectionAction extends AbstractAction implements SelectionChangeListener {

    /**
     View this action is controlling
     */
    private PhotoCollectionThumbView view;

    /**
     Move to next photo when this action is performed
     */
    public static final int MOVE_BACK = 1;

    /**
     Move to previous photo when this action is performed
     */
    public static final int MOVE_FWD = 2;

    /**
     Direction we are moving, must be either either MOV_BACK or MOVE_FWD
     */
    private int direction = 0;

    /** 
     Creates a new instance of ChangeSelectionAction 
     @param view The view this action controls
     @param direction Either MOVE_BACK or MOVE_FWD
     @param text Text to show for this action in menus etc.
     @param desc Description of this action
     @param mnemonic Mnemonic to use for this action in menus
     @param accelerator Accelerator key stroke for this action
     */
    public ChangeSelectionAction(PhotoCollectionThumbView view, int direction, String text, ImageIcon icon, String desc, int mnemonic, KeyStroke accelerator) {
        super(text, icon);
        this.view = view;
        this.direction = direction;
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, new Integer(mnemonic));
        putValue(ACCELERATOR_KEY, accelerator);
        view.addSelectionChangeListener(this);
        setEnabled(view.getSelectedCount() == 1);
    }

    /**
     Called when the action is performed. Moves selection to next or previous photo,
     depending on @see direction.
     @param e The action event
     */
    public void actionPerformed(ActionEvent e) {
        switch(direction) {
            case MOVE_BACK:
                view.selectPreviousPhoto();
                break;
            case MOVE_FWD:
                view.selectNextPhoto();
                break;
            default:
                JOptionPane.showMessageDialog(null, "Wrong direction value= " + direction, "Internal error", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    /**
     Called when selection in @see vew is changed. Enables this action if exactly 1
     photo is selected, disables otherwise.
     @param e The selection change event that triggered this call.
     */
    public void selectionChanged(SelectionChangeEvent e) {
        setEnabled(view.getSelectedCount() == 1);
    }
}
