package lvlEditor;

import game.Node;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Tile {

    private ImageIcon image;

    private String imageName = "";

    private String target = "";

    private String type = "Unset type";

    private int locX;

    private int locY;

    static final int SQUARESIDE = 16;

    public void setX(int x) {
        locX = x;
    }

    public void setY(int y) {
        locY = y;
    }

    public int getX() {
        return locX;
    }

    public int getY() {
        return locY;
    }

    public String getLocation() {
        return Integer.toString(locX) + ", " + Integer.toString(locY);
    }

    public void setImage(String s) {
        image = new ImageIcon("./tileImages/" + s);
        imageName = s;
    }

    public String getImage() {
        return imageName;
    }

    public void setTarget(String s) {
        target = s;
    }

    public String getTarget() {
        return target;
    }

    public void setType(String s) {
        type = s;
    }

    public String getType() {
        return type;
    }

    Tile(int x, int y) {
        locX = x;
        locY = y;
        setImage("Null.PNG");
        type = "obstacle";
    }

    public Node asNode() {
        Node tileNode = new Node("tile");
        tileNode.addSubnode("image", imageName);
        tileNode.addSubnode("target", target);
        tileNode.addSubnode("type", type);
        tileNode.addSubnode("x", "" + locX);
        tileNode.addSubnode("y", "" + locY);
        return tileNode;
    }

    static Tile fromNode(Node tileNode) {
        int x = new Integer(tileNode.contentOf("x"));
        int y = new Integer(tileNode.contentOf("y"));
        Tile t = new Tile(x, y);
        t.setImage(tileNode.contentOf("image"));
        t.setType(tileNode.contentOf("type"));
        t.setTarget(tileNode.contentOf("target"));
        return t;
    }

    public void drawImage(Component c, Graphics g) {
        if (image != null) image.paintIcon(c, g, locX * SQUARESIDE, locY * SQUARESIDE);
    }

    public String toString() {
        return "a Tile(" + +locX + "," + locY + "," + imageName + "," + type + "," + target + ")";
    }
}
