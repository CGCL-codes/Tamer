package de.michabrandt.timeview.renderer;

import java.awt.RenderingHints;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import de.michabrandt.timeview.common.Dialog;

/**
 *
 */
public class PNGRenderer extends StreamRenderer {

    private boolean antialias = true;

    private boolean antialias_text = true;

    public PNGRenderer(Dialog dialog) {
        super(dialog);
    }

    public void setAntialias(boolean aa, boolean aa_text) {
        antialias = aa;
        antialias_text = aa_text;
    }

    public void render(OutputStream os) throws IOException {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, (antialias) ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, (antialias_text) ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        super.render();
        ImageIO.write(img, "png", os);
    }

    @Override
    public String getFileSuffix() {
        return ".png";
    }
}
