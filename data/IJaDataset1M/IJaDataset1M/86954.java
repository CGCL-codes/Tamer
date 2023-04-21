package com.sun.opengl.impl.macosx.cgl;

import java.util.*;
import javax.media.nativewindow.*;
import javax.media.opengl.*;
import com.sun.opengl.impl.*;
import com.sun.gluegen.runtime.NativeLibrary;

public class MacOSXCGLGraphicsConfiguration extends DefaultGraphicsConfiguration implements Cloneable {

    public MacOSXCGLGraphicsConfiguration(AbstractGraphicsScreen screen, GLCapabilities capsChosen, GLCapabilities capsRequested) {
        super(screen, capsChosen, capsRequested);
    }

    public Object clone() {
        return super.clone();
    }

    protected void setChosenCapabilities(GLCapabilities caps) {
        super.setChosenCapabilities(caps);
    }
}
