package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import org.droiddraw.AndroidEditor;
import org.droiddraw.gui.ImageResources;
import org.droiddraw.gui.NineWayImage;
import org.droiddraw.property.BooleanProperty;

public class Spinner extends AbstractWidget {

    public static final String TAG_NAME = "Spinner";

    NineWayImage img;

    NineWayImage arrows;

    Image image_base;

    Image arrs;

    BooleanProperty onTop;

    Font f;

    public Spinner() {
        super(TAG_NAME);
        image_base = null;
        String theme = AndroidEditor.instance().getTheme();
        if (theme == null || theme.equals("default")) {
            image_base = ImageResources.instance().getImage("def/spinner_normal.9");
            img = new NineWayImage(image_base, 10, 10);
            arrs = ImageResources.instance().getImage("def/btn_dropdown_neither.9");
            arrows = null;
        } else if (theme.equals("light")) {
            image_base = ImageResources.instance().getImage("light/spinnerbox_background_focus_yellow.9");
            if (image_base != null) {
                img = new NineWayImage(image_base, 10, 10, 28, 10);
                arrs = ImageResources.instance().getImage("light/spinnerbox_arrow_middle.9");
                arrows = new NineWayImage(arrs, 1, 1, 22, 1);
            }
        }
        onTop = new BooleanProperty("Selector on Top", "android:drawSelectorOnTop", false);
        props.add(onTop);
        f = new Font("Arial", Font.PLAIN, 14);
        apply();
    }

    @Override
    protected int getContentHeight() {
        return image_base.getHeight(null);
    }

    @Override
    protected int getContentWidth() {
        return 100;
    }

    public void paint(Graphics g) {
        if (img != null) {
            img.paint(g, getX(), getY(), getWidth(), getHeight());
            if (arrows != null) arrows.paint(g, getX(), getY(), getWidth(), getHeight()); else g.drawImage(arrs, getX() + getWidth() - 38, getY() + getHeight() / 2 - arrs.getHeight(null) / 2, null);
        }
        g.setColor(Color.black);
        g.setFont(f);
        if (arrows != null) g.drawString("Spinner", getX() + 10, getY() + 16);
    }
}
