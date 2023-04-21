package normal.engine.implementation.jni.file;

import normal.engine.implementation.jni.*;
import normal.engine.file.*;

public class NJNIFileInfo extends JNIShareableObject implements NFileInfo {

    public NJNIFileInfo(Sentry s) {
        super(s);
    }

    public native String getPathname();

    public native int getType();

    public native String getTypeDescription();

    public native String getEngine();

    public native boolean isCompressed();

    public native boolean isInvalid();
}
