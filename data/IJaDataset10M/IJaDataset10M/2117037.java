package tiwarriors.client;

import java.awt.*;
import java.io.*;

/**
 * Texts intended to show in the center of the screen.
 */
public class CenterText extends TiWObject {

    private static TiWArena arena;

    private transient int lifeTime = 3000;

    private transient boolean notToRemove = false;

    private transient long startTime = System.currentTimeMillis();

    private Color actualColor = Color.green;

    private int size = 42;

    private String text;

    public CenterText(String t, TiWArena arena) {
        super(0, 0, 0, 0, arena.getWidth(), arena.getHeight());
        text = t;
        this.arena = arena;
        makeCenterPositioning();
    }

    public void makeCenterPositioning() {
        setPosition((arena.getWidth() / 2.0 - text.length() * size / 3.2), (arena.getHeight() / 2.0));
    }

    public void render(Graphics2D g) {
        g.setFont(new Font("Monospaced", Font.PLAIN, size));
        g.setPaint(actualColor);
        g.drawString(text, (int) getX(), (int) getY());
    }

    public void updatePosition() {
        long now = System.currentTimeMillis();
        if (notToRemove) {
            return;
        }
        if (now - startTime > lifeTime) {
            arena.removeObject(this);
        }
    }

    public void setColor(Color c) {
        super.setColor(c);
        actualColor = c;
    }

    public void setSize(int s) {
        size = s;
    }

    public void setText(String t) {
        text = t;
    }

    public void setLife(int v) {
        lifeTime = v;
    }

    public void restartLife() {
        startTime = System.currentTimeMillis();
    }

    public void setNotToRemove() {
        notToRemove = true;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(text);
        out.writeObject(actualColor);
        out.writeInt(size);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        text = in.readUTF();
        actualColor = (Color) in.readObject();
        size = in.readInt();
    }
}
