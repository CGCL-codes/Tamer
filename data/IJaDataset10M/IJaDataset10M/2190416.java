package ec.edu.epn.fis.uil4midp.views;

import ec.edu.epn.fis.uil4midp.actions.ActionListener;
import ec.edu.epn.fis.uil4midp.components.controls.Button;
import ec.edu.epn.fis.uil4midp.util.ResourceManager;
import javax.microedition.lcdui.Canvas;

/**
 * A MessageDialog is a kind of Dialog which shows a informative message which
 * only can be accepted.
 * @author Carlos Andrés Oquendo
 */
public class MessageDialog extends Dialog {

    private Button btnOk;

    /**
     * Creates a MessageDialog showing a message. It contains a OK button that
     * dismisses the Dialog when fired.
     * @param title Title of the Dialog
     * @param message Message to be shown on the Dialog.
     */
    public MessageDialog(String title, String message) {
        super(title, message);
        btnOk = new Button(ResourceManager.loadImage(tm.getOkYesIconPath()));
        btnOk.setFocused(true);
        btnOk.setActionListener(new ActionListener() {

            public void execute() {
                dialogResult = DIALOG_OK;
                getController().dismissDialog();
            }
        });
    }

    /**
     * Handles the key events.
     * @param action Canvas' key action number.
     * @param keyCode Pressed key code. This code may be device-specific.
     * @return True if the key event was handled, else, False.
     */
    public final boolean keyPressed(int action, int keyCode) {
        switch(action) {
            case Canvas.FIRE:
                return btnOk.keyPressed(action, keyCode);
            default:
                return false;
        }
    }

    /**
     * Initializes additional components of the MessageDialog.
     */
    public final void initialize() {
        super.initialize();
        addVisualComponent(btnOk);
    }
}
