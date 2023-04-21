package cn.myapps.ui;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import cn.myapps.ui.util.GraphicesTool;

public class RadioOption extends BaseOption {

    public RadioOption(String text) {
        this(text, text);
    }

    public RadioOption(String name, String value) {
        this(name, value, null);
    }

    public RadioOption(String name, String value, Image imgPort) {
        this(name, value, imgPort, false);
    }

    public RadioOption(String name, String value, Image imgPort, boolean selected) {
        super(name, value, imgPort, selected);
        bounds = new int[4];
    }

    void paint(Graphics g, int width, int height) {
        GraphicesTool gTool = new GraphicesTool(g);
        Image img = getImage();
        g.translate(Component.BORDER_PAD, Component.BORDER_PAD);
        if (group.hasFocus) if (!focus) {
            gTool.setColor(11513775);
        } else {
            if (img != null) {
                gTool.drawFocus(0, 0, img.getWidth(), img.getHeight());
            } else gTool.drawFocus(0, 0, width - 2 * Component.BORDER_PAD, height - 2 * Component.BORDER_PAD);
        }
        int imgw = 0;
        if (img != null) {
            g.drawImage(img, 0, 0, Graphics.TOP | Graphics.LEFT);
            imgw = img.getWidth() + Component.BORDER_PAD;
        }
        gTool.setColor(group.enabled ? Container.FG_COLOR : 11513775);
        g.translate(imgw, 0);
        gTool.drawString(name, Component.BORDER_PAD + 1, 1, width - (2 * Component.BORDER_PAD + 2), height - (2 * Component.BORDER_PAD + 2), 0, 0);
        g.translate(-imgw, 0);
        g.translate(-Component.BORDER_PAD, -Component.BORDER_PAD);
        g.setColor(Container.FG_COLOR);
    }

    public Image getImage() {
        Image img = null;
        String path;
        if (!selected) {
            path = RadioField.NORMAL_IMAGE;
        } else {
            path = RadioField.CHECKED_IMAGE;
        }
        try {
            img = Image.createImage(path);
        } catch (IOException e) {
        }
        return img;
    }
}
