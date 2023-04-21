package normal.engine.implementation.jni.subcomplex;

import normal.engine.implementation.jni.*;
import normal.engine.subcomplex.*;

public class NJNILayeredLensSpace extends JNIShareableObject implements NLayeredLensSpace {

    public NJNILayeredLensSpace(Sentry s) {
        super(s);
    }

    public native NLayeredLensSpace cloneMe();

    public native long getP();

    public native long getQ();

    public native NLayeredSolidTorus getTorus();

    public native int getMobiusBoundaryGroup();

    public native boolean isSnapped();

    public native boolean isTwisted();
}
