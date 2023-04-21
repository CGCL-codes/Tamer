package org.apache.harmony.awt.gl.windows;

import java.awt.GraphicsDevice;
import java.awt.HeadlessException;
import org.apache.harmony.awt.gl.CommonGraphicsEnvironment;
import org.apache.harmony.awt.wtk.WindowFactory;
import org.apache.harmony.awt.Utils;

/**
 * Windows GraphicsEnvironment implementation
 *
 */
public class WinGraphicsEnvironment extends CommonGraphicsEnvironment {

    WinGraphicsDevice defaultDevice = null;

    WinGraphicsDevice[] devices = null;

    static {
        Utils.loadLibrary("gl");
    }

    public WinGraphicsEnvironment(WindowFactory wf) {
    }

    @Override
    public GraphicsDevice getDefaultScreenDevice() throws HeadlessException {
        if (defaultDevice == null) {
            WinGraphicsDevice[] dvcs = (WinGraphicsDevice[]) getScreenDevices();
            for (WinGraphicsDevice element : dvcs) {
                if (element.isDefaultDevice()) {
                    defaultDevice = element;
                    break;
                }
            }
        }
        return defaultDevice;
    }

    @Override
    public GraphicsDevice[] getScreenDevices() throws HeadlessException {
        if (devices == null) {
            devices = enumerateDisplayDevices();
        }
        return devices;
    }

    /**
     * Enumerates system displays
     * 
     * @return Array of WinGraphicsDevice objects representing system displays
     */
    private native WinGraphicsDevice[] enumerateDisplayDevices();
}
