package org.lirc.bt;

import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class RemoteControlCanvas extends Canvas {

    int x = 0;

    int y = 0;

    int code = 0;

    Image ui = null;

    private final Main main;

    public RemoteControlCanvas(Main main) {
        super();
        this.main = main;
        setTitle(Main.DEFAULT_NAME);
        try {
            ui = Image.createImage("/ui128.png");
        } catch (IOException e) {
        }
    }

    protected void paint(Graphics g) {
        if (ui != null) {
            g.drawImage(ui, 0, 0, Graphics.TOP | Graphics.LEFT);
        } else {
            g.setColor(0xffffff);
            g.drawString("Failed to load UI image!", 0, 0, Graphics.TOP | Graphics.LEFT);
        }
    }

    protected void keyPressed(int keyCode) {
        main.sendEvent(keyCode);
    }

    protected void keyRepeated(int keyCode) {
        keyPressed(keyCode);
    }
}
