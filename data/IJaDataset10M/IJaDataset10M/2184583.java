package superweapons;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import java.awt.Point;
import java.awt.geom.Point2D;

public class NukePlane extends SuperWeapon {

    private Point dropPoint;

    private boolean bombsAway = false;

    private SuperweaponNuke nuke;

    private static final ImageIcon image = new ImageIcon(Firestorm.class.getResource("../images/airplane.png"));

    public NukePlane() {
        super(new Dimension(image.getIconWidth(), image.getIconHeight()), image);
    }

    public void setPoint(int x, int y) {
        dropPoint = new Point(x, y);
    }

    public void draw(Graphics g, Rectangle area, Component c) {
        super.draw(g, area, c);
        if (-5 > dropPoint.x - this.position.x && dropPoint.x - this.position.x < 5 && !bombsAway) {
            nuke = new SuperweaponNuke(position.x, position.y, image.getIconWidth(), image.getIconHeight());
        }
    }

    public SuperweaponNuke getNuke() {
        return nuke;
    }

    public Point getPoint() {
        return dropPoint;
    }

    public boolean getBombsAway() {
        return bombsAway;
    }

    public boolean update() {
        super.update();
        this.setPosition(new Point2D.Double(this.getPosition().x + 5, this.getPosition().y));
        return true;
    }

    public void setBombsAway(Boolean t) {
        bombsAway = t;
    }
}
