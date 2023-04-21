package org.netbeans.modules.visual.border;

import org.netbeans.api.visual.border.Border;
import java.awt.*;

/**
 * @author David Kaspar
 */
public final class EmptyBorder implements Border {

    private Insets insets;

    private boolean opaque;

    public EmptyBorder(int top, int left, int bottom, int right, boolean opaque) {
        insets = new Insets(top, left, bottom, right);
        this.opaque = opaque;
    }

    public Insets getInsets() {
        return insets;
    }

    public void paint(Graphics2D gr, Rectangle bounds) {
    }

    public boolean isOpaque() {
        return opaque;
    }
}
