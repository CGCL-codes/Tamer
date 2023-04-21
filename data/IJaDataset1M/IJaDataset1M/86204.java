package javax.media.nativewindow.macosx;

import javax.media.nativewindow.*;

/** Encapsulates a graphics device on MacOSX platforms.
 */
public class MacOSXGraphicsDevice extends DefaultGraphicsDevice implements Cloneable {

    /** Constructs a new MacOSXGraphicsDevice */
    public MacOSXGraphicsDevice(int unitID) {
        super(NativeWindowFactory.TYPE_MACOSX, AbstractGraphicsDevice.DEFAULT_CONNECTION, unitID);
    }

    public Object clone() {
        return super.clone();
    }
}
