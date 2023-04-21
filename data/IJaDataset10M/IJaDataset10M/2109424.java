package org.jfarrell.classes;

import java.io.Serializable;
import java.awt.Point;
import javax.swing.ImageIcon;
import java.awt.Image;

/**
 * @author Jason Farrell
 * Utility class for transferring data to the Main Server from the GUI
 * 
 * TODO
 * Add serializable traits to the object
 *
 */
public class PointImage implements Serializable {

    private ImageIcon m_theImage = null;

    public Image getImage() {
        return this.m_theImage.getImage();
    }

    public void setImage(Image i) {
        this.m_theImage = new ImageIcon(i);
    }

    private Point m_thePoint = null;

    public Point getPoint() {
        return this.m_thePoint;
    }

    public void setPoint(Point p) {
        this.m_thePoint = p;
    }

    public PointImage(Image pImage, Point pPoint) {
        this.m_theImage = new ImageIcon(pImage);
        this.m_thePoint = pPoint;
    }
}
