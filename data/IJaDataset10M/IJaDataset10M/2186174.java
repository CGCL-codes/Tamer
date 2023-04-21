package org.apache.harmony.awt.wtk.windows;

import java.awt.Dimension;
import java.awt.Image;
import org.apache.harmony.awt.nativebridge.windows.Win32;
import org.apache.harmony.awt.wtk.CursorFactory;
import org.apache.harmony.awt.wtk.NativeCursor;
import org.apache.harmony.awt.nativebridge.windows.WindowsDefs;

/**
 * Implementation of CursorFactory for Windows platform.
 */
public class WinCursorFactory extends CursorFactory {

    static final Win32 win32 = Win32.getInstance();

    final WinEventQueue eventQueue;

    /**
     * Java to native type translation table:
     * native id for LoadCursor(commented native id for LoadImage(from winuser.h)),
     * commented Java cursor type
     */
    static final long[] predefined = { WindowsDefs.IDC_ARROW, WindowsDefs.IDC_CROSS, WindowsDefs.IDC_IBEAM, WindowsDefs.IDC_WAIT, WindowsDefs.IDC_SIZENESW, WindowsDefs.IDC_SIZENWSE, WindowsDefs.IDC_SIZENWSE, WindowsDefs.IDC_SIZENESW, WindowsDefs.IDC_SIZENS, WindowsDefs.IDC_SIZENS, WindowsDefs.IDC_SIZEWE, WindowsDefs.IDC_SIZEWE, WindowsDefs.IDC_HAND, WindowsDefs.IDC_SIZEALL };

    WinCursorFactory(WinEventQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    /**
     * @see org.apache.harmony.awt.wtk.CursorFactory#createCursor(int)
     */
    @Override
    public NativeCursor createCursor(int type) {
        if (type >= 0 && type < predefined.length) {
            long hCursor = win32.LoadCursorW(0l, predefined[type]);
            return new WinCursor(eventQueue, hCursor);
        }
        return null;
    }

    /**
     * @see org.apache.harmony.awt.wtk.CursorFactory#createCustomCursor(java.awt.Image, int, int)
     */
    @Override
    public NativeCursor createCustomCursor(Image img, int xHotSpot, int yHotSpot) {
        long hCursor = WinIcons.createIcon(false, img, xHotSpot, yHotSpot);
        return new WinCursor(eventQueue, hCursor, false);
    }

    /**
     * @see org.apache.harmony.awt.wtk.CursorFactory#getBestCursorSize(int, int)
     */
    @Override
    public Dimension getBestCursorSize(int prefWidth, int prefHeight) {
        return new Dimension(win32.GetSystemMetrics(WindowsDefs.SM_CXCURSOR), win32.GetSystemMetrics(WindowsDefs.SM_CYCURSOR));
    }

    /**
     * @see org.apache.harmony.awt.wtk.CursorFactory#getMaximumCursorColors()
     */
    @Override
    public int getMaximumCursorColors() {
        long screenDC = win32.GetDC(0);
        int nColors = win32.GetDeviceCaps(screenDC, WindowsDefs.NUMCOLORS);
        if (nColors < 0) {
            final int COLORS_PER_PLANE = 256;
            int nPlanes = win32.GetDeviceCaps(screenDC, WindowsDefs.PLANES);
            nColors = COLORS_PER_PLANE * nPlanes;
        }
        win32.ReleaseDC(0, screenDC);
        return nColors;
    }
}
