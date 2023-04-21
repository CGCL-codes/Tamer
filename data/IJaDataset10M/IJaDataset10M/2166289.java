package com.tightvnc;

import java.awt.*;
import java.io.*;

class VncCanvas2 extends VncCanvas {

    public VncCanvas2(VncViewer v) throws IOException {
        super(v);
        disableFocusTraversalKeys();
    }

    public VncCanvas2(VncViewer v, int maxWidth_, int maxHeight_) throws IOException {
        super(v, maxWidth_, maxHeight_);
        disableFocusTraversalKeys();
    }

    public void paintScaledFrameBuffer(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.drawImage(memImage, 0, 0, scaledWidth, scaledHeight, null);
    }

    private void disableFocusTraversalKeys() {
        try {
            Class[] argClasses = { Boolean.TYPE };
            java.lang.reflect.Method method = getClass().getMethod("setFocusTraversalKeysEnabled", argClasses);
            Object[] argObjects = { new Boolean(false) };
            method.invoke(this, argObjects);
        } catch (Exception e) {
        }
    }
}
