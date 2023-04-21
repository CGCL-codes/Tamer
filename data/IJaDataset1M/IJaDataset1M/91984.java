package jp.ac.tokyo_ct.asteragy;

import com.nttdocomo.ui.*;

final class PaintString {

    private final String string;

    private final CanvasControl canvas;

    PaintString(CanvasControl canvas, String string) {
        this.canvas = canvas;
        this.string = string;
    }

    void paint(Graphics g) {
        g.setColor(Graphics.getColorOfName(Graphics.WHITE));
        g.fillRect(10, canvas.getHeight() / 3, canvas.getWidth() - 20, canvas.getHeight() / 3);
        g.setColor(Graphics.getColorOfName(Graphics.BLACK));
        g.drawString(string, (canvas.getWidth() - Font.getDefaultFont().stringWidth(string)) / 2, (canvas.getHeight() + Font.getDefaultFont().getHeight()) / 2);
    }
}
