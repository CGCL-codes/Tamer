package radui;

import javax.microedition.lcdui.Graphics;

public class View {

    public void paint(Graphics g, int w, int h) {
    }

    public void keyPressed(ScreenCanvas sc, int keyCode) {
    }

    public void keyRepeated(ScreenCanvas sc, int keyCode) {
    }

    public int getMaxWidth() {
        return 0;
    }

    public int getMaxHeight() {
        return 0;
    }

    public int getHeight(int w) {
        return Math.min(w, getMaxHeight());
    }
}
