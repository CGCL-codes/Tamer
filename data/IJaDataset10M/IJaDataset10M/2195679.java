package ngamejava2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Node;

public class GameObject<T extends GameObjectContainer> implements iGameLifeCicleListener {

    private T parent;

    private Rectangle2D.Float bounds;

    private boolean collidable;

    private boolean discarted;

    public GameObject() {
        bounds = new Rectangle2D.Float();
        discarted = false;
        collidable = true;
    }

    public void load() {
    }

    public void unload() {
    }

    public void update(long currentTick) {
    }

    public void render(Graphics2D g, long currentFrame) {
        g.setColor(Color.red);
        g.draw(bounds);
    }

    public Rectangle2D.Float getBounds() {
        return bounds;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public void setCollidable(boolean value) {
        collidable = value;
    }

    public boolean isDiscarted() {
        return discarted;
    }

    public void discart() {
        discarted = true;
    }

    public void setParent(T aParent) {
        parent = aParent;
    }

    public T getParent() {
        return parent;
    }

    public void read(GameReader gr, Node objectNode) throws XPathExpressionException {
        bounds = gr.readRectangle2D(objectNode, "rectangle[@name='bounds']");
        collidable = gr.readBoolean(objectNode, "boolean[@name='collidable']");
        discarted = gr.readBoolean(objectNode, "boolean[@name='discarted']");
    }

    public void write(GameWriter gw) throws IOException {
        gw.write("bounds", bounds);
        gw.write("collidable", collidable);
        gw.write("discarted", discarted);
    }
}
