package gameframework.base;

import java.awt.Graphics;
import java.util.Vector;

public class DrawableComposite implements Drawable {

    protected Vector<Drawable> drawables = new Vector<Drawable>();

    public void add(Drawable e) {
        drawables.addElement(e);
    }

    public void remove(Drawable e) {
        drawables.removeElement(e);
    }

    public void draw(Graphics g) {
        for (Drawable elem : drawables) {
            elem.draw(g);
        }
    }
}
