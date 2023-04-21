package cn.easyact.tdl.ui;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import cn.easyact.tdl.sprite.Sprite;

public abstract class Component {

    protected static final int LEFT_TOP = Graphics.LEFT | Graphics.TOP;

    public static final Object ADDING = new Object() {

        public String toString() {
            return "NULL OBJECT: ADDING";
        }
    };

    private int width = 0;

    private int height = 0;

    private Sprite sprite;

    public void paint(Graphics g) {
        paintComponent(g);
        deTranslate(g);
    }

    protected abstract void paintComponent(Graphics g);

    public void setData(Vector v) {
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        width = sprite.getWidth();
        height = sprite.getHeight();
    }

    protected Sprite getSprite() {
        return sprite;
    }

    protected void deTranslate(Graphics g) {
        g.translate(-g.getTranslateX(), -g.getTranslateY());
    }

    public void setData(Object[] objects) {
        Vector v = new Vector();
        for (int i = 0; i < objects.length; i++) {
            v.addElement(objects[i]);
        }
        setData(v);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
