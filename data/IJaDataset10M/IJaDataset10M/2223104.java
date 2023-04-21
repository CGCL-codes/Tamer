package courselog;

import ewe.fx.IImage;
import ewe.ui.KeyEvent;
import ewe.ui.mButton;

/**
 *
 * @author rigal
 */
public class ActionKeySensitiveButton extends mButton {

    /** Action key dispatcher which gets the key event and handles it. */
    ActionKeysDispatcher myAKD = null;

    /** mButton constructor. */
    public ActionKeySensitiveButton() {
        super();
    }

    /** mButton constructor from an Image. */
    public ActionKeySensitiveButton(IImage image) {
        super(image);
    }

    /** mButton constructor from a String. */
    public ActionKeySensitiveButton(String txt) {
        super(txt);
    }

    /** mButton constructor from an image and text. */
    public ActionKeySensitiveButton(String text, String imageName, Object maskOrColor) {
        super(text, imageName, maskOrColor);
    }

    /** Links the ActionKeySensitiveButton to a Dispatcher. */
    public void setMyActioKeyDispatcher(ActionKeysDispatcher akd) {
        myAKD = akd;
    }

    public void onKeyEvent(KeyEvent ev) {
        if (myAKD != null) {
            myAKD.onEvent(ev);
        }
        super.onKeyEvent(ev);
    }
}
