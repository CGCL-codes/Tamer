package clavicom.gui.mouse;

import java.awt.image.BufferedImage;
import clavicom.core.keygroup.CKey;
import clavicom.core.keygroup.mouse.CMouseKey;
import clavicom.core.keygroup.mouse.CMouseKeyMove;
import clavicom.gui.keyboard.key.UIKey;

public class UIKeyMouse extends UIKey {

    CMouseKey mouseKey;

    BufferedImage captionImage;

    boolean reload;

    public UIKeyMouse(CMouseKey myMouseKey) {
        mouseKey = myMouseKey;
        reload = true;
    }

    @Override
    public CKey getCoreKey() {
        return mouseKey;
    }

    @Override
    protected void addListeners() {
    }

    @Override
    public void onBoundsChanged() {
    }

    @Override
    protected String getCaptionText() {
        return mouseKey.getCaption();
    }

    @Override
    protected BufferedImage getCaptionImage() {
        if (getCoreKey().isCaptionImage() == false) return null;
        if (reload == true) {
            reload = false;
            captionImage = loadCaptionImage(getCaptionText());
        }
        return captionImage;
    }

    @Override
    protected void alertCaptionChanged() {
    }

    @Override
    protected void clickCoreKey() {
        if (getCoreKey() instanceof CMouseKeyMove) {
            super.clickCoreKey();
        } else {
            if ((getMousePosition().getX() < 0) || (getMousePosition().getX() > getWidth()) || (getMousePosition().getY() < 0) || (getMousePosition().getX() > getHeight())) {
                super.clickCoreKey();
            }
        }
    }
}
