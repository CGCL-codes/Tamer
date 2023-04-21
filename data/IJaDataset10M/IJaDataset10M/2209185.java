package com.sun.opengl.impl.egl;

import java.util.*;
import javax.media.nativewindow.*;
import javax.media.opengl.*;
import com.sun.opengl.impl.*;
import com.sun.nativewindow.impl.*;
import com.sun.gluegen.runtime.NativeLibrary;

/**
 * Implementation of the EGLDynamicLookupHelper for ES2.
 */
public class EGLES2DynamicLookupHelper extends EGLDynamicLookupHelper {

    protected EGLES2DynamicLookupHelper() {
        super();
    }

    protected int getESProfile() {
        return 2;
    }

    protected List getGLESLibNames() {
        List glesLibNames = new ArrayList();
        glesLibNames.add("GLES20");
        glesLibNames.add("GLESv2");
        glesLibNames.add("GLESv2_CM");
        glesLibNames.add("libGLES20");
        glesLibNames.add("libGLESv2");
        glesLibNames.add("libGLESv2_CM");
        return glesLibNames;
    }
}
