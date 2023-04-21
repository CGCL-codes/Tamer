package org.apache.harmony.awt.wtk.windows;

import java.awt.Image;
import java.awt.image.BufferedImage;
import org.apache.harmony.awt.gl.Utils;
import org.apache.harmony.awt.nativebridge.Int32Pointer;
import org.apache.harmony.awt.nativebridge.NativeBridge;
import org.apache.harmony.awt.nativebridge.PointerPointer;
import org.apache.harmony.awt.nativebridge.windows.Win32;
import org.apache.harmony.awt.nativebridge.windows.WindowsDefs;

class WinIcons {

    static final NativeBridge bridge = NativeBridge.getInstance();

    static final Win32 win32 = Win32.getInstance();

    static long createIcon(boolean icon, Image img, int xHotSpot, int yHotSpot) {
        BufferedImage bufImg = Utils.getBufferedImage(img);
        if (bufImg == null) {
            return 0;
        }
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        int size = width * height;
        Win32.BITMAPINFO bmi = win32.createBITMAPINFO(false);
        Win32.BITMAPINFOHEADER bmiHeader = bmi.get_bmiHeader();
        bmiHeader.set_biSize(bmiHeader.size());
        bmiHeader.set_biWidth(width);
        bmiHeader.set_biHeight(-height);
        bmiHeader.set_biPlanes((short) 1);
        bmiHeader.set_biBitCount((short) 32);
        bmiHeader.set_biCompression(WindowsDefs.BI_RGB);
        long screenDC = win32.GetDC(0);
        PointerPointer valuesPtr = bridge.createPointerPointer(1, false);
        long hBMPColorMask = win32.CreateDIBSection(screenDC, bmi, WindowsDefs.DIB_RGB_COLORS, valuesPtr, null, 0);
        int[] rgb = bufImg.getRGB(0, 0, width, height, null, 0, width);
        Int32Pointer values = bridge.createInt32Pointer(valuesPtr.get(0));
        values.set(rgb, 0, size);
        long hBMPAlphaMask = win32.CreateDIBSection(screenDC, bmi, WindowsDefs.DIB_RGB_COLORS, valuesPtr, null, 0);
        win32.ReleaseDC(0, screenDC);
        int[] maskArray = new int[size];
        for (int i = 0; i < size; i++) {
            if ((rgb[i] & 0xFF000000) != 0) {
                maskArray[i] = 0xFFFFFF;
            } else {
                maskArray[i] = 0;
            }
        }
        values = bridge.createInt32Pointer(valuesPtr.get(0));
        values.set(maskArray, 0, size);
        Win32.ICONINFO iconInfo = win32.createICONINFO(false);
        iconInfo.set_fIcon(icon ? 1 : 0);
        if (!icon) {
            iconInfo.set_xHotspot(xHotSpot);
            iconInfo.set_yHotspot(yHotSpot);
        }
        iconInfo.set_hbmMask(hBMPAlphaMask);
        iconInfo.set_hbmColor(hBMPColorMask);
        final long hIcon = win32.CreateIconIndirect(iconInfo);
        win32.DeleteObject(hBMPAlphaMask);
        win32.DeleteObject(hBMPColorMask);
        return hIcon;
    }
}
