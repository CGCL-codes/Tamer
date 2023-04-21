package org.iisc.mile.indickeyboards.linux;

public class LinuxLibraries {

    /**
     * This native method identifies the interface to the keyboard. Generally,
     * the path to the interface would be in the form <em>/dev/input/eventX</em>
     * where X=0, 1, 2 etc.
     * @param cmd The parameter will be <em>/dev/input/eventX</em>, X=0, 1, 2..
     * @return Returns a string which will have the description of the keyboard present.
     */
    public native String identify(String cmd);

    /**
     * Once the event interface to the keyboard is obtained, grab the keyboard and
     * start monitoring all the key presses. This is a native method.
     * @param KB The correct path to the keyboard's event interface obtained from
     * the native method <code>identify()</code>
     */
    public native void grab(String KB);

    /**
     * Native method which controls the AutoRepeat property of the keyboard.
     * This is checked using the AutoRepeat boolean variable in the
     * IndicKeyboards class. Xlib used to control this feature.
     * @see org.iisc.mile.indickeyboards.linux.InitLinux#AutoRepeat AutoRepeat
     * @param flag
     */
    public native void keyrepeat(int flag);

    /**
    * Calls the native method which outputs the character onto the active
    * window. Java Native Interface (JNI) is used to call the this method. X11
    * libraries are used to achieve this.
    * @param ucode Contains the Unicode code point value which will be put onto the active window.
    */
    public static native void OutputActiveWindow(String ucode);
}
