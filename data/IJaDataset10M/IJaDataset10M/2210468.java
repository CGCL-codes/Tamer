package org.dijkstromania.modell;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Point;
import java.io.Serializable;

/**
 * Modell f�r einen Knoten
 * Zwei Knoten sind gleich, wenn sie dieselbe Bezeichnung haben.
 * @author Hong-Son Dang Nguyen, Mayooran Thillainathan, Firas Jradi
 *
 */
public class Vertex implements Serializable {

    private boolean isStartknoten = false;

    private boolean marked = false;

    private boolean visited = false;

    private boolean cc = true;

    private int posX, posY;

    private int width, height;

    private Color bgColor = new Color(22, 38, 104);

    private Color fgColor = new Color(255, 255, 255);

    private String caption = "-";

    private Font font = new Font(Font.SANS_SERIF, Font.BOLD, 12);

    private int captionHeight, captionWidth;

    /**
	 * Einfacher Konstruktor
	 * @param c Bezeichnung f�r den Knoten
	 */
    public Vertex(String c, int x, int y, boolean s) {
        this.caption = c;
        this.posX = x;
        this.posY = y;
        this.isStartknoten = s;
    }

    /**
	 * Komplexer Konstruktor
	 * @param c Bezeichnung
	 * @param x Position x
	 * @param y Position y
	 * @param w Breite (optional)
	 * @param h Hoehe (optional)
	 * @param b Hintergrundfarbe (optional)
	 * @param f Vordergrundfarbe (optional)
	 */
    public Vertex(String c, int x, int y, Color b, Color f) {
        this.caption = c;
        this.posX = x;
        this.posY = y;
        this.bgColor = b;
        this.fgColor = f;
    }

    /**
	 * Zeichnet Knoten
	 * @param g Graphics2D
	 */
    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        Color c = g.getColor();
        if (cc) setNewBounds(g);
        if (this.marked == true) {
            g.setColor(Color.red);
            g.fillOval(posX - 1, posY - 1, this.width + 2, this.height + 2);
        }
        if (this.visited == true) {
            g.setColor(Color.red);
        } else {
            if (this.isStartknoten) g.setColor(Color.blue); else g.setColor(bgColor);
        }
        g.fillOval(posX, posY, this.width, this.height);
        g.setColor(fgColor);
        g.setFont(this.font);
        g.drawString(this.caption, posX + 10, posY + captionHeight);
        g.setColor(c);
    }

    /**
	 * Vergleicht zwei Knoten miteinander
	 * Zwei Knoten Sind gleich wenn Sie dieselbe Bezeichnugn haben.
	 * hashValue muss noch angepasst werden !!!
	 * @param v Knoten
	 * @return true wenn gleich
	 */
    public boolean equals(Vertex v) {
        if (this.caption.equals(v.caption)) return true; else return false;
    }

    /**
	 * clonen
	 * @return Vertex cloned
	 */
    public Vertex clone() {
        return new Vertex(this.caption, this.posX, this.posY, this.isStartknoten);
    }

    /**
	 * �berpr�fen ob Knoten angepeilt/geklickt wurde
	 * @param x x position des klicks
	 * @param y y position des klicks
	 * @return true wenn getroffen..
	 */
    public boolean isTargeted(int x, int y) {
        if ((this.posX < x && x < (this.posX + this.width)) && (this.posY < y && y < (this.posY + this.height))) {
            return true;
        } else return false;
    }

    private void setNewBounds(Graphics2D g) {
        if (this.caption != null) this.captionWidth = g.getFontMetrics(font).stringWidth(this.caption);
        this.width = 20 + this.captionWidth;
        this.captionHeight = g.getFontMetrics(font).getHeight();
        this.height = 10 + this.captionHeight;
        cc = false;
    }

    public boolean isStartknoten() {
        return this.isStartknoten;
    }

    public void setStartknoten(boolean s) {
        this.isStartknoten = s;
    }

    public void setCaption(String c) {
        this.caption = c;
        cc = true;
    }

    public void setMarked(boolean s) {
        this.marked = s;
    }

    public boolean getMarked() {
        return this.marked;
    }

    public void setVisited(boolean s) {
        this.visited = s;
    }

    public boolean getVisited() {
        return this.visited;
    }

    public String getCaption() {
        return this.caption;
    }

    public Point getCenter() {
        return new Point(posX + width / 2, posY + height / 2);
    }

    public Point getLocation() {
        return new Point(posX, posY);
    }

    public void setLocation(Point p) {
        this.posX = p.x;
        this.posY = p.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String toString() {
        return this.getCaption();
    }
}
