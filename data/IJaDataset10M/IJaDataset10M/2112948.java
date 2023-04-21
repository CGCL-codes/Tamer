package com.sun.javafx.newt;

import javax.media.nativewindow.*;
import java.util.ArrayList;
import java.util.Iterator;
import com.sun.nativewindow.impl.jvm.JVMUtil;

public abstract class NewtFactory {

    static {
        JVMUtil.initSingleton();
        Window.init(NativeWindowFactory.getNativeWindowType(true));
    }

    static Class getCustomClass(String packageName, String classBaseName) {
        Class clazz = null;
        if (packageName != null || classBaseName != null) {
            String clazzName = packageName + "." + classBaseName;
            try {
                clazz = Class.forName(clazzName);
            } catch (Throwable t) {
            }
        }
        return clazz;
    }

    private static boolean useEDT = false;

    /** 
     * Toggles the usage of an EventDispatchThread while creating a Display.<br>
     * The default is enabled.<br>
     * The EventDispatchThread is thread local to the Display instance.<br>
     */
    public static synchronized void setUseEDT(boolean onoff) {
        useEDT = onoff;
    }

    /** @see #setUseEDT(boolean) */
    public static boolean useEDT() {
        return useEDT;
    }

    /**
     * Create a Display entity, incl native creation
     */
    public static Display createDisplay(String name) {
        return Display.create(NativeWindowFactory.getNativeWindowType(true), name);
    }

    /**
     * Create a Display entity using the given implementation type, incl native creation
     */
    public static Display createDisplay(String type, String name) {
        return Display.create(type, name);
    }

    /**
     * Create a Screen entity, incl native creation
     */
    public static Screen createScreen(Display display, int index) {
        return Screen.create(NativeWindowFactory.getNativeWindowType(true), display, index);
    }

    /**
     * Create a Screen entity using the given implementation type, incl native creation
     */
    public static Screen createScreen(String type, Display display, int index) {
        return Screen.create(type, display, index);
    }

    /**
     * Create a Window entity, incl native creation
     */
    public static Window createWindow(Screen screen, Capabilities caps) {
        return Window.create(NativeWindowFactory.getNativeWindowType(true), 0, screen, caps, false, true);
    }

    public static Window createWindow(Screen screen, Capabilities caps, boolean undecorated) {
        return Window.create(NativeWindowFactory.getNativeWindowType(true), 0, screen, caps, undecorated, true);
    }

    public static Window createWindow(Screen screen, Capabilities caps, boolean undecorated, boolean opaque) {
        return Window.create(NativeWindowFactory.getNativeWindowType(true), 0, screen, caps, undecorated, opaque);
    }

    public static Window createWindow(long parentWindowHandle, Screen screen, Capabilities caps, boolean undecorated) {
        return Window.create(NativeWindowFactory.getNativeWindowType(true), parentWindowHandle, screen, caps, undecorated, true);
    }

    /**
     * Ability to try a Window type with a construnctor argument, if supported ..<p>
     * Currently only valid is <code> AWTWindow(Frame frame) </code>,
     * to support an external created AWT Frame, ie the browsers embedded frame.
     */
    public static Window createWindow(Object[] cstrArguments, Screen screen, Capabilities caps, boolean undecorated) {
        return Window.create(NativeWindowFactory.getNativeWindowType(true), cstrArguments, screen, caps, undecorated, true);
    }

    /**
     * Create a Window entity using the given implementation type, incl native creation
     */
    public static Window createWindow(String type, Screen screen, Capabilities caps) {
        return Window.create(type, 0, screen, caps, false, true);
    }

    public static Window createWindow(String type, Screen screen, Capabilities caps, boolean undecorated) {
        return Window.create(type, 0, screen, caps, undecorated, true);
    }

    public static Window createWindow(String type, long parentWindowHandle, Screen screen, Capabilities caps, boolean undecorated) {
        return Window.create(type, parentWindowHandle, screen, caps, undecorated, true);
    }

    public static Window createWindow(String type, Object[] cstrArguments, Screen screen, Capabilities caps, boolean undecorated) {
        return Window.create(type, cstrArguments, screen, caps, undecorated, true);
    }

    /**
     * Instantiate a Display entity using the native handle.
     */
    public static Display wrapDisplay(String name, AbstractGraphicsDevice device) {
        return Display.wrapHandle(NativeWindowFactory.getNativeWindowType(true), name, device);
    }

    /**
     * Instantiate a Screen entity using the native handle.
     */
    public static Screen wrapScreen(Display display, AbstractGraphicsScreen screen) {
        return Screen.wrapHandle(NativeWindowFactory.getNativeWindowType(true), display, screen);
    }

    /**
     * Instantiate a Window entity using the native handle.
     */
    public static Window wrapWindow(Screen screen, AbstractGraphicsConfiguration config, long windowHandle, boolean fullscreen, boolean visible, int x, int y, int width, int height) {
        return Window.wrapHandle(NativeWindowFactory.getNativeWindowType(true), screen, config, windowHandle, fullscreen, visible, x, y, width, height);
    }

    private static final boolean instanceOf(Object obj, String clazzName) {
        Class clazz = obj.getClass();
        do {
            if (clazz.getName().equals(clazzName)) {
                return true;
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return false;
    }
}
