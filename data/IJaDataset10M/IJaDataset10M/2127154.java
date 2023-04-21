package net.etherstorm.jOpenRPG;

import org.jdom.*;
import java.awt.*;
import javax.swing.*;
import java.net.URL;
import java.util.*;
import java.io.*;

/**
 * Miniature is the class representing minis on the map.
 *
 *
 * @author $Author: tedberg $
 * @version $Revision: 415 $
 */
public class Miniature implements Serializable {

    public static String NEW = "new";

    public static String UPDATE = "update";

    public static String DELETE = "del";

    public static final int HIDE_NO = 0;

    public static final int HIDE_YES = 1;

    public static final int ALIGN_NO = 0;

    public static final int ALIGN_YES = 1;

    public static final int LOCKED_NO = 0;

    public static final int LOCKED_YES = 1;

    public static final int HEADING_NONE = 0;

    public static final int HEADING_NORTH = 1;

    public static final int HEADING_NORTHEAST = 2;

    public static final int HEADING_EAST = 3;

    public static final int HEADING_SOUTHEAST = 4;

    public static final int HEADING_SOUTH = 5;

    public static final int HEADING_SOUTHWEST = 6;

    public static final int HEADING_WEST = 7;

    public static final int HEADING_NORTHWEST = 8;

    public static final int FACING_NONE = 0;

    public static final int FACING_NORTH = 1;

    public static final int FACING_NORTHEAST = 2;

    public static final int FACING_EAST = 3;

    public static final int FACING_SOUTHEAST = 4;

    public static final int FACING_SOUTH = 5;

    public static final int FACING_SOUTHWEST = 6;

    public static final int FACING_WEST = 7;

    public static final int FACING_NORTHWEST = 8;

    ImageIcon image;

    Image ghost;

    Rectangle boundingRect;

    int centerx, centery;

    int xoffset, yoffset;

    protected int hide;

    protected String id = "xx-xx";

    protected String path;

    protected int posy = -1;

    protected int posx = -1;

    protected int zorder;

    protected String label;

    protected int face;

    protected int heading;

    protected int align;

    protected int locked;

    protected String action;

    protected int labelWidth = -1;

    protected int labelHeight = -1;

    protected JMapPanel panel;

    protected Color bgColor = new Color(0, 0, 0, 0);

    protected Element delta;

    protected boolean updating;

    protected boolean selected;

    public Miniature() {
    }

    public void setMapPanel(JMapPanel p) {
        panel = p;
    }

    public JMapPanel getMapPanel() {
        return panel;
    }

    /**
	 * Constructor declaration
	 *
	 *
	 * @param p
	 *
	 */
    public Miniature(JMapPanel p) {
        panel = p;
        setAction(NEW);
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param e
	 *
	 */
    public void fromXML(Element e) {
        beginUpdate();
        Iterator iter = e.getAttributes().iterator();
        setAction(UPDATE);
        while (iter.hasNext()) {
            try {
                Attribute attrib = (Attribute) iter.next();
                if (attrib.getName().equals("hide")) {
                    setHide(attrib.getIntValue());
                } else if (attrib.getName().equals("id")) {
                    setId(attrib.getValue());
                } else if (attrib.getName().equals("path")) {
                    setPath(attrib.getValue());
                } else if (attrib.getName().equals("posy")) {
                    setPosy(attrib.getIntValue());
                } else if (attrib.getName().equals("posx")) {
                    setPosx(attrib.getIntValue());
                } else if (attrib.getName().equals("zorder")) {
                    setZorder(attrib.getIntValue());
                } else if (attrib.getName().equals("label")) {
                    setLabel(attrib.getValue());
                } else if (attrib.getName().equals("face")) {
                    setFace(attrib.getIntValue());
                } else if (attrib.getName().equals("heading")) {
                    setHeading(attrib.getIntValue());
                } else if (attrib.getName().equals("align")) {
                    setAlign(attrib.getIntValue());
                } else if (attrib.getName().equals("locked")) {
                    setLocked(attrib.getIntValue());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        endUpdate();
    }

    /**
	 * Method declaration
	 *
	 *
	 * @return locked
	 *
	 */
    public int getLocked() {
        return this.locked;
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param locked
	 *
	 */
    public void setLocked(int locked) {
        if (this.locked == locked) return;
        this.locked = locked;
        if (!isUpdating()) {
            getDelta().setAttribute("locked", String.valueOf(locked));
        }
    }

    /**
	 * Method declaration
	 *
	 *
	 * @return align
	 *
	 */
    public int getAlign() {
        return this.align;
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param align
	 *
	 */
    public void setAlign(int align) {
        if (this.align == align) return;
        this.align = align;
        if (!isUpdating()) getDelta().setAttribute("align", String.valueOf(align));
    }

    /**
	 * Method declaration
	 *
	 *
	 * @return heading
	 *
	 */
    public int getHeading() {
        return this.heading;
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param heading
	 *
	 */
    public void setHeading(int heading) {
        if (this.heading == heading) return;
        this.heading = heading;
        if (!isUpdating()) getDelta().setAttribute("heading", String.valueOf(heading));
    }

    /**
	 * Method declaration
	 *
	 *
	 * @return face
	 *
	 */
    public int getFace() {
        return this.face;
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param face
	 *
	 */
    public void setFace(int face) {
        if (this.face == face) return;
        this.face = face;
        if (!isUpdating()) getDelta().setAttribute("face", String.valueOf(face));
    }

    /**
	 * Method declaration
	 *
	 *
	 * @return label
	 *
	 */
    public String getLabel() {
        return this.label;
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param label
	 *
	 */
    public void setLabel(String label) {
        if (this.label != null && this.label.equals(label)) return;
        this.label = label;
        labelWidth = -1;
        labelHeight = -1;
        if (!isUpdating()) getDelta().setAttribute("label", label);
    }

    /**
	 * Method declaration
	 *
	 *
	 * @return zorder
	 *
	 */
    public int getZorder() {
        return this.zorder;
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param zorder
	 *
	 */
    public void setZorder(int zorder) {
        if (this.zorder == zorder) return;
        this.zorder = zorder;
        if (!isUpdating()) getDelta().setAttribute("zorder", String.valueOf(zorder));
    }

    /**
	 * Method declaration
	 *
	 *
	 * @return posx
	 *
	 */
    public int getPosx() {
        return this.posx;
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param posx
	 *
	 */
    public void setPosx(int x) {
        if (posx == x) {
            System.err.println("setPosx value unchanged (" + posx + "), aborting");
            return;
        }
        posx = x;
        centerx = getWidth() / 2 + posx;
        if (!isUpdating()) {
            System.err.println("posx changed, recording");
            getDelta().setAttribute("posx", String.valueOf(posx));
        } else System.err.println("posx not changed");
    }

    /**
	 * Method declaration
	 *
	 *
	 * @return posy
	 *
	 */
    public int getPosy() {
        return this.posy;
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param posy
	 *
	 */
    public void setPosy(int y) {
        if (posy == y) {
            System.err.println("setPosy value unchanged (" + posy + "), aborting");
            return;
        }
        posy = y;
        centery = getHeight() / 2 + posy;
        if (!isUpdating()) {
            System.err.println("posy changed, recording");
            getDelta().setAttribute("posy", String.valueOf(posy));
        } else System.err.println("posy not changed");
    }

    /**
	 * Method declaration
	 *
	 *
	 * @return path
	 *
	 */
    public String getPath() {
        return this.path;
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param path
	 *
	 */
    public void setPath(final String path) {
        if (this.path != null && this.path.equals(path)) return;
        this.path = path;
        setImageIcon(ImageLib.loadImage("/images/icons/fetching.png"));
        Thread t = new Thread() {

            public void run() {
                try {
                    final ImageIcon i = ImageLib.loadUrlImage(new URL(path));
                    System.out.println("loaded " + path);
                    Runnable r = new Runnable() {

                        public void run() {
                            setImageIcon(i);
                            centerx = getWidth() / 2 + getPosx();
                            centery = getHeight() / 2 + getPosy();
                            System.out.println("icon set to " + path);
                            panel.repaint();
                        }
                    };
                    javax.swing.SwingUtilities.invokeLater(r);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            ;
        };
        t.start();
        if (!isUpdating()) getDelta().setAttribute("path", path);
    }

    /**
	 * Method declaration
	 *
	 *
	 * @return id
	 *
	 */
    public String getId() {
        return this.id;
    }

    /**
	 * This should not normally be called, unless a new miniature is being
	 * created by a remote client.
	 *
	 *
	 * @param id
	 *
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * Method declaration
	 *
	 *
	 * @return hide
	 *
	 */
    public int getHide() {
        return this.hide;
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param hide
	 *
	 */
    public void setHide(int hide) {
        if (this.hide == hide) return;
        this.hide = hide;
        if (!isUpdating()) getDelta().setAttribute("hide", String.valueOf(hide));
    }

    /**
	 * Method declaration
	 *
	 *
	 * @return image
	 *
	 */
    public ImageIcon getImageIcon() {
        return this.image;
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param image
	 *
	 */
    public synchronized void setImageIcon(ImageIcon image) {
        this.image = image;
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param g
	 *
	 */
    public void paint(Graphics2D g) {
        if (getHide() == HIDE_YES) {
            return;
        }
        Color old = g.getColor();
        if (getImageIcon() != null) {
            g.drawImage(getImageIcon().getImage(), getPosx(), getPosy(), panel);
            if (!(xoffset == 0 && yoffset == 0)) g.drawImage(ghost, getPosx() + xoffset, getPosy() + yoffset, panel);
        }
        if (isSelected()) drawBoundingBox(g);
        if (getFace() != FACING_NONE) drawFacing(g);
        if (getHeading() != HEADING_NONE) drawHeading(g);
        drawLabel(g);
        g.setColor(old);
    }

    /**
	 * Draws a 'selected' rectangle around the image
	 *
	 * @param g The graphics object on which to draw the bounding box
	 */
    protected void drawBoundingBox(Graphics2D g) {
        g.setColor(panel.SELECT_COLOR);
        g.draw(getBoundingRect());
        g.fillOval(centerx, centery, 5, 5);
    }

    /**
	 * Draws the heading triangle for this image
	 * @param g the graphics object on which to draw the rectangle
	 */
    protected void drawHeading(Graphics2D g) {
        MyPoly poly = new MyPoly();
        int x_adjust = 0;
        int y_adjust = 4;
        int x_half = getHalfWidth();
        int y_half = getHalfHeight() / 2;
        int x_quarter = x_half / 2;
        int y_quarter = y_half / 2;
        int x_3quarter = x_quarter * 3;
        int y_3quarter = y_quarter * 3;
        int x_full = getWidth();
        int y_full = getHeight();
        int x_center = getPosx() + x_half;
        int y_center = getPosy() + y_half;
        switch(getHeading()) {
            case HEADING_NORTH:
                poly.add(x_center - x_quarter, y_center - y_half);
                poly.add(x_center, y_center - y_3quarter);
                poly.add(x_center + x_quarter, y_center - y_half);
                break;
            case HEADING_SOUTH:
                poly.add(x_center - x_quarter, y_center + y_half);
                poly.add(x_center, y_center + y_3quarter);
                poly.add(x_center + x_quarter, y_center + y_half);
                break;
            case HEADING_NORTHEAST:
                poly.add(x_center + x_quarter, y_center - y_half);
                poly.add(x_center + x_3quarter, y_center - y_3quarter);
                poly.add(x_center + x_half, y_center - y_quarter);
                break;
            case HEADING_EAST:
                poly.add(x_center + x_half, y_center - y_quarter);
                poly.add(x_center + x_3quarter, y_center);
                poly.add(x_center + x_half, y_center + y_quarter);
                break;
            case HEADING_SOUTHEAST:
                poly.add(x_center + x_half, y_center + y_quarter);
                poly.add(x_center + x_3quarter, y_center + y_3quarter);
                poly.add(x_center + x_quarter, y_center + y_half);
                break;
            case HEADING_SOUTHWEST:
                poly.add(x_center - x_quarter, y_center + y_half);
                poly.add(x_center - x_3quarter, y_center + y_3quarter);
                poly.add(x_center - x_half, y_center + y_quarter);
                break;
            case HEADING_WEST:
                poly.add(x_center - x_half, y_center + y_quarter);
                poly.add(x_center - x_3quarter, y_center);
                poly.add(x_center - x_half, y_center - y_quarter);
                break;
            case HEADING_NORTHWEST:
                poly.add(x_center - x_half, y_center - y_quarter);
                poly.add(x_center - x_3quarter, y_center - y_3quarter);
                poly.add(x_center - x_quarter, y_center - y_half);
                break;
            default:
                break;
        }
        g.setColor(Color.blue.brighter());
        g.fill(poly);
        g.setColor(Color.black);
        g.draw(poly);
    }

    /**
	 * Draws the facing indicator
	 * @param g the graphics object on which to draw the indicator
	 */
    protected void drawFacing(Graphics2D g) {
        MyPoly poly = new MyPoly();
        int wid = getWidth();
        int hei = getHeight();
        int x_mid = (int) (wid / 2) + getPosx();
        int x_right = (int) (getPosx() + wid);
        int y_mid = (int) (hei / 2) + getPosy();
        int y_bottom = (int) (getPosy() + hei);
        switch(getFace()) {
            case FACING_NORTH:
                poly.add(getPosx(), getPosy());
                poly.add(x_mid, getPosy() - 5);
                poly.add(x_right, getPosy());
                break;
            case FACING_NORTHEAST:
                poly.add(x_mid, getPosy());
                poly.add(x_right + 5, getPosy() - 5);
                poly.add(x_right, y_mid);
                poly.add(x_right, getPosy());
                break;
            case FACING_EAST:
                poly.add(x_right, getPosy());
                poly.add(x_right + 5, y_mid);
                poly.add(x_right, y_bottom);
                break;
            case FACING_SOUTHEAST:
                poly.add(x_right, y_mid);
                poly.add(x_right + 5, y_bottom + 5);
                poly.add(x_mid, y_bottom);
                poly.add(x_right, y_bottom);
                break;
            case FACING_SOUTH:
                poly.add(getPosx(), y_bottom);
                poly.add(x_mid, y_bottom + 5);
                poly.add(x_right, y_bottom);
                break;
            case FACING_SOUTHWEST:
                poly.add(x_mid, y_bottom);
                poly.add(getPosx() - 5, y_bottom + 5);
                poly.add(getPosx(), y_mid);
                poly.add(getPosx(), y_bottom);
                break;
            case FACING_WEST:
                poly.add(getPosx(), getPosy());
                poly.add(getPosx() - 5, y_mid);
                poly.add(getPosx(), y_bottom);
                break;
            case FACING_NORTHWEST:
                poly.add(getPosx(), y_mid);
                poly.add(getPosx() - 5, getPosy() - 5);
                poly.add(x_mid, getPosy());
                poly.add(getPosx(), getPosy());
                break;
            default:
                break;
        }
        g.setColor(Color.red.brighter());
        g.fill(poly);
        g.setColor(Color.black);
        g.draw(poly);
    }

    /**
	 * Draws this images' label.
	 * @param g the graphics object on which to draw the label
	 */
    protected void drawLabel(Graphics2D g) {
        if (label == null) return;
        if (labelWidth == -1) {
            labelWidth = g.getFontMetrics().bytesWidth(getLabel().getBytes(), 0, getLabel().length());
            labelHeight = g.getFontMetrics().getHeight();
        }
        int fudge = 2;
        int x = (int) ((getWidth() - labelWidth) / 2);
        int y = fudge + labelHeight + getHeight();
        g.setColor(Color.white);
        g.fillRect(x + getPosx(), y + getPosy() - labelHeight + fudge + fudge, labelWidth, labelHeight);
        g.setColor(Color.red);
        g.drawBytes(getLabel().getBytes(), 0, getLabel().length(), x + getPosx(), y + getPosy());
    }

    /**
	 * Sets the updating status of the map layer
	 * @param state the new updating status
	 */
    protected void setUpdating(boolean state) {
        updating = state;
    }

    /**
	 * Convenience method.	Sets updating to true.
	 */
    protected void beginUpdate() {
        setUpdating(true);
    }

    /**
	 * Convenience method.	Sets updating to false.
	 *
	 */
    protected void endUpdate() {
        setUpdating(false);
    }

    /**
	 * Tests if a map layer is current updating due to a received message.
	 *
	 * @return true if the map layer is updating from an external event, false otherwise.
	 *
	 */
    public boolean isUpdating() {
        return updating;
    }

    /**
	 * Tests a map layer for the existance of a delta.
	 *
	 * @return true if a delta exists, false otherwise
	 *
	 */
    public boolean hasDelta() {
        return delta != null;
    }

    /**
	 * Returns the delta element, creating it if one does not exist. This method
	 * should only be called by parent layers if hasDelta() returns true.  To
	 * call it otherwise generates a false delta.
	 *
	 */
    protected Element getDelta(String name) {
        if (delta == null) delta = new Element(name);
        return delta;
    }

    /**
	 * Returns the delta element customised for this map layer.
	 * @see AbstractLayer#getDelta(String)
	 */
    public Element getDelta() {
        return getDelta("miniature").setAttribute("id", getId());
    }

    /**
	 * clears all delta information
	 */
    public void clearDelta() {
        System.err.println("Clearing delta");
        delta = null;
    }

    /**
	 * Sets the selected state of this image.  If the selectd state is true, a
	 * bounding box will be draws around this image on the map.
	 * @param value the new selected state of the image
	 */
    public void setSelected(boolean value) {
        selected = value;
    }

    /**
	 * Returns the selected state of this image.
	 * @return true if selected, false otherwise.
	 */
    public boolean isSelected() {
        return selected;
    }

    public void setAction(String action) {
        if (this.action != null && this.action.equals(action)) return;
        this.action = action;
        if (!updating) getDelta().setAttribute("action", action);
    }

    public String getAction() {
        return action;
    }

    public Rectangle getBoundingRect() {
        if (boundingRect == null) try {
            boundingRect = new Rectangle(getPosx(), getPosy(), 1, 1);
        } catch (NullPointerException npe) {
        }
        try {
            boundingRect.setLocation(getPosx(), getPosy());
            boundingRect.setSize(getImageIcon().getIconWidth(), getImageIcon().getIconHeight());
        } catch (Exception ex) {
        }
        return boundingRect;
    }

    public boolean contains(Point p) {
        return getBoundingRect().contains(p);
    }

    public void offsetBy(int x, int y) {
        xoffset = x;
        yoffset = y;
        if (ghost == null) ghost = GrayFilter.createDisabledImage(getImageIcon().getImage());
    }

    public void moveToOffset() {
        int foo = getPosx() + xoffset;
        int bar = getPosy() + yoffset;
        xoffset = 0;
        yoffset = 0;
        setPosx(foo);
        setPosy(bar);
    }

    public int getOffsetX() {
        return yoffset;
    }

    public int getOffsetY() {
        return yoffset;
    }

    public void moveBy(int x, int y) {
        int foo = getPosx() + x;
        int bar = getPosy() + y;
        setPosx(foo);
        setPosy(bar);
        ghost = null;
    }

    public int getHalfWidth() {
        return (int) (getWidth() / 2);
    }

    public int getHalfHeight() {
        return (int) (getHeight() / 2);
    }

    public int getWidth() {
        return (int) getBoundingRect().getWidth();
    }

    public int getHeight() {
        return (int) getBoundingRect().getHeight();
    }

    public Element toXml() {
        Element e = new Element("miniature");
        e.setAttribute("align", String.valueOf(getAlign()));
        e.setAttribute("face", String.valueOf(getFace()));
        e.setAttribute("heading", String.valueOf(getHeading()));
        e.setAttribute("hide", String.valueOf(getHide()));
        e.setAttribute("id", getId());
        e.setAttribute("label", getLabel());
        e.setAttribute("locked", String.valueOf(getLocked()));
        e.setAttribute("path", getPath());
        e.setAttribute("posx", String.valueOf(getPosx()));
        e.setAttribute("posy", String.valueOf(getPosy()));
        e.setAttribute("zorder", String.valueOf(getZorder()));
        e.setAttribute("action", action);
        return e;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }
}
