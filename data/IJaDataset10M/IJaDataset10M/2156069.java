package jogamp.newt.macosx;

import com.jogamp.newt.*;
import jogamp.newt.ScreenImpl;
import javax.media.nativewindow.*;

public class MacScreen extends ScreenImpl {

    static {
        MacDisplay.initSingleton();
    }

    public MacScreen() {
    }

    protected void createNativeImpl() {
        aScreen = new DefaultGraphicsScreen(getDisplay().getGraphicsDevice(), screen_idx);
        setScreenSize(getWidthImpl0(screen_idx), getHeightImpl0(screen_idx));
    }

    protected void closeNativeImpl() {
    }

    private static native int getWidthImpl0(int scrn_idx);

    private static native int getHeightImpl0(int scrn_idx);
}
