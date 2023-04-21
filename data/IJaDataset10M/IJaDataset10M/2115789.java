package org.fao.waicent.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XPoint implements Cloneable, XMLable {

    public static final int POINT_NONE = -1;

    public static final int POINT_POINT = 0;

    public static final int POINT_SHAPE = 1;

    public static final int POINT_ICON = 2;

    public static final String[] pointNames = { "None", "Point", "Shape", "Icon" };

    public static final int[] pointIndexes = { POINT_NONE, POINT_POINT, POINT_SHAPE, POINT_ICON };

    protected int type = POINT_NONE;

    public boolean proportional;

    public boolean scalable;

    public boolean smooth;

    XIcon icon = null;

    XShape shape = null;

    public static int drawNormal = 0;

    public static int drawScaled = 1;

    public static int drawClipped = 2;

    public static int drawFitted = 4;

    public static int drawPreview = 8;

    public synchronized Object clone() throws CloneNotSupportedException {
        XPoint x = (XPoint) super.clone();
        x.type = type;
        if (icon != null) {
            x.icon = (XIcon) icon.clone();
        }
        if (shape != null) {
            x.shape = (XShape) shape.clone();
        }
        return x;
    }

    public XPoint(Document doc, Element ele) throws IOException {
        load(doc, ele);
    }

    public void load(Document doc, Element ele) throws IOException {
        XMLUtil.checkType(doc, ele, this);
        type = POINT_NONE;
        try {
            type = Integer.valueOf(ele.getAttribute("point_type")).intValue();
        } catch (Exception e) {
        }
        icon = null;
        shape = null;
        for (int i = 0; i < ele.getElementsByTagName("Icon").getLength(); i++) {
            icon = new XIcon(doc, (Element) ele.getElementsByTagName("Icon").item(i));
        }
        for (int i = 0; i < ele.getElementsByTagName("Shape").getLength(); i++) {
            shape = new XShape(doc, (Element) ele.getElementsByTagName("Shape").item(i));
        }
    }

    public void save(Document doc, Element ele) throws IOException {
        XMLUtil.setType(doc, ele, this);
        ele.setAttribute("point_type", Integer.toString(type));
        switch(this.type) {
            case POINT_ICON:
                if (icon != null && icon.getType() != XIcon.ICON_NONE) {
                    Element icon_ele = doc.createElement("Icon");
                    ele.appendChild(icon_ele);
                    icon.save(doc, icon_ele);
                }
                break;
            case POINT_SHAPE:
                if (shape != null && shape.getType() != XShape.SHAPE_NONE) {
                    Element shape_ele = doc.createElement("Shape");
                    ele.appendChild(shape_ele);
                    shape.save(doc, shape_ele);
                }
                break;
        }
    }

    public String toString() {
        String output = "<XPoint type=\"" + this.type + "\">";
        if (icon != null) output += icon.toString();
        if (shape != null) output += shape.toString();
        output += "</XPoint>";
        return output;
    }

    public void load(DataInputStream in) throws IOException {
        type = in.readInt();
        icon = new XIcon(in);
        shape = new XShape(in);
    }

    public void load(DataInputStream in, int version) throws IOException {
        type = in.readInt();
        icon = new XIcon(in, version);
        shape = new XShape(in, version);
    }

    public void save(DataOutputStream out) throws IOException {
        out.writeInt(type);
        if (icon == null) icon = new XIcon();
        icon.save(out);
        if (shape == null) shape = new XShape();
        shape.save(out);
    }

    public XPoint(Element element) throws Exception {
        int type = Integer.valueOf(element.getAttribute("type")).intValue();
        Element icon_element = (Element) XPathAPI.selectSingleNode(element, "XIcon");
        icon = new XIcon(icon_element);
        Element shape_element = (Element) XPathAPI.selectSingleNode(element, "XShape");
        shape = new XShape(shape_element);
    }

    /**
	 *  added to support storing Input Stream into DOM
	 */
    public XPoint(DataInputStream in, Document doc, Element element) throws IOException {
        load(in);
        toXML(doc, element);
    }

    public XPoint(DataInputStream in, Document doc, Element element, int version) throws IOException {
        load(in, version);
        toXML(doc, element);
    }

    /**
	 *  constructor to read datastream and build this object.
	 */
    public XPoint(DataInputStream in) throws IOException {
        load(in);
    }

    public XPoint(DataInputStream in, int version) throws IOException {
        load(in, version);
    }

    public XPoint(String type, String icontype, String iconname, String iconfilename, String iconimglocation, String shapetype, String shapesize, String shapename) {
        this(type, icontype, iconname, iconfilename, iconimglocation, shapetype, shapesize, shapename, "false");
    }

    public XPoint(String type, String icontype, String iconname, String iconfilename, String iconimglocation, String shapetype, String shapesize, String shapename, String scalable) {
        this(type, icontype, iconname, iconfilename, iconimglocation, shapetype, shapesize, shapename, scalable, "");
    }

    public XPoint(String type, String icontype, String iconname, String iconfilename, String iconimglocation, String shapetype, String shapesize, String shapename, String scalable, String shapeunit) {
        this(type, icontype, iconname, iconfilename, iconimglocation, shapetype, shapesize, shapename, scalable, shapeunit, "false");
    }

    public XPoint(String type, String icontype, String iconname, String iconfilename, String iconimglocation, String shapetype, String shapesize, String shapename, String scalable, String shapeunit, String smooth) {
        int point_type = XPoint.POINT_NONE;
        int icon_type = XIcon.ICON_NONE;
        int shape_type = XShape.SHAPE_NONE;
        int shape_size = XShape.DEFAULT_SIZE;
        boolean point_scalable = false;
        boolean point_smooth = false;
        try {
            point_type = Integer.parseInt(type);
            icon_type = Integer.parseInt(icontype);
            shape_type = Integer.parseInt(shapetype);
            shape_size = Integer.parseInt(shapesize);
            point_scalable = Boolean.parseBoolean(scalable);
            point_smooth = Boolean.parseBoolean(smooth);
        } catch (Exception e) {
        }
        this.icon = null;
        this.shape = null;
        this.type = point_type;
        if (point_type != XPoint.POINT_NONE) {
            if (point_type == POINT_ICON && icon_type != XIcon.ICON_NONE) {
                this.icon = new XIcon(icon_type, iconname, iconfilename, iconimglocation);
            }
            if (point_type == POINT_SHAPE && shape_type != XShape.SHAPE_NONE) {
                this.shape = new XShape(shape_type, shape_size, shapename, point_scalable, shapeunit, point_smooth);
            }
        }
    }

    public XPoint() {
        this((XIcon) null, (XShape) null);
    }

    public XPoint(XIcon icon) {
        this(icon, null);
    }

    public XPoint(XShape shape) {
        this(null, shape);
    }

    public XPoint(XIcon icon, XShape shape) {
        this.type = POINT_NONE;
        this.icon = icon;
        this.shape = shape;
        if (icon != null) {
            if (icon.getType() != XIcon.ICON_NONE) this.type = POINT_ICON;
        }
        if (shape != null) {
            if (shape.getType() != XShape.SHAPE_NONE) this.type = POINT_SHAPE;
        }
    }

    public XPoint(int type, XIcon icon, XShape shape) {
        this.type = type;
        this.icon = icon;
        this.shape = shape;
    }

    public void setIcon(XIcon icon) {
        this.icon = icon;
    }

    public void setShape(XShape shape) {
        this.shape = shape;
    }

    public XIcon getIcon() {
        return icon;
    }

    public XShape getShape() {
        return shape;
    }

    public void setType(int t) {
        this.type = t;
    }

    public int getType() {
        return type;
    }

    public Shape drawPoint(Graphics2D g, int x, int y, XPatternPaint p, XOutline o) {
        return this.drawPoint(g, null, x, y, p, o);
    }

    public Shape drawPoint(Graphics2D g, int x, int y, Color fill_colour, Color line_colour, byte line_density) {
        return this.drawPoint(g, null, this, x, x, fill_colour, line_colour, line_density);
    }

    public Shape drawPoint(Graphics2D g, Rectangle clip, int x, int y, XPatternPaint p, XOutline o) {
        return this.drawPoint(g, clip, this, x, y, p.getBackgroundColor(), o.getColor(), o.getDensity());
    }

    public Shape drawPoint(Graphics2D g, Rectangle clip, int x, int y, XPatternPaint p, XOutline o, int drawtype) {
        return this.drawPoint(g, clip, this, x, y, p.getBackgroundColor(), o.getColor(), o.getDensity(), drawtype);
    }

    public Shape drawPoint(Graphics2D g, Rectangle clip, Shape s, int x, int y, XPatternPaint p, XOutline o, int drawtype) {
        return this.drawPoint(g, clip, s, this, x, y, p.getBackgroundColor(), o.getColor(), o.getDensity(), drawtype);
    }

    public Shape drawPoint(Graphics2D g, Rectangle clip, Shape s, int x, int y, XPatternPaint p, XOutline o, int drawtype, double scalefactor) {
        return this.drawPoint(g, clip, s, this, x, y, p.getBackgroundColor(), o.getColor(), o.getDensity(), drawtype, scalefactor);
    }

    public static Shape drawPoint(Graphics2D g, Rectangle clip, XPoint pt, int x, int y, Color fill_colour, Color line_colour, byte line_density) {
        return pt.drawPoint(g, clip, pt, x, y, fill_colour, line_colour, line_density, pt.drawNormal);
    }

    public static Shape drawPoint(Graphics2D g, Rectangle clip, XPoint pt, int x, int y, Color fill_colour, Color line_colour, byte line_density, int drawtype) {
        return pt.drawPoint(g, clip, null, pt, x, y, fill_colour, line_colour, line_density, drawtype);
    }

    public static Shape drawPoint(Graphics2D g, Rectangle clip, Shape sh, XPoint pt, int x, int y, Color fill_colour, Color line_colour, byte line_density, int drawtype) {
        return pt.drawPoint(g, clip, sh, pt, x, y, fill_colour, line_colour, line_density, drawtype, 1.0d);
    }

    public static Shape drawPoint(Graphics2D g, Rectangle clip, Shape sh, XPoint pt, int x, int y, Color fill_colour, Color line_colour, byte line_density, int drawtype, double scalefactor) {
        if (clip != null && !clip.contains(x, y)) return null;
        Shape saveclip = g.getClip();
        if (clip != null && drawtype == XPoint.drawClipped) g.setClip(clip);
        Shape pointshape = null;
        switch(pt.getType()) {
            case POINT_NONE:
                break;
            case POINT_POINT:
                g.setStroke(new BasicStroke(line_density));
                g.setColor(line_colour);
                int size = 8;
                g.fillOval(x - size / 2, y - size / 2, size, size);
                g.drawOval(x - size / 2, y - size / 2, size, size);
                pointshape = pt.getPointShape(new Point2D.Float(x, y), drawtype, clip);
                break;
            case POINT_ICON:
                if (pt.getIcon() != null) pointshape = XIcon.drawIcon(g, clip, pt.getIcon(), x, y, drawtype, scalefactor);
                break;
            case POINT_SHAPE:
                if (pt.getShape() != null) pointshape = XShape.drawShape(g, clip, sh, pt.getShape(), x, y, fill_colour, line_colour, line_density, drawtype, scalefactor);
                break;
        }
        g.setClip(saveclip);
        return pointshape;
    }

    public void paintLegendIcon(Graphics2D g, Rectangle c, XPatternPaint p, XOutline o) {
        Stroke savedStroke = g.getStroke();
        Color savedColour = g.getColor();
        g.setStroke(new BasicStroke(1));
        g.setColor(Color.GRAY);
        g.drawRect(c.x, c.y, c.width, c.height);
        this.drawPoint(g, c, c.x + c.width / 2, c.y + c.height / 2, p, o, XPoint.drawFitted);
        g.setStroke(savedStroke);
        g.setColor(savedColour);
    }

    public void paintPreview(Graphics2D g, Shape s, XPatternPaint p, XOutline o) {
        Stroke savedStroke = g.getStroke();
        Color savedColour = g.getColor();
        Rectangle preview = (Rectangle) s;
        if (g.getClip() == null) g.setClip(preview);
        Rectangle clip = g.getClipBounds();
        int thick = 2;
        clip.setBounds(clip.x + thick / 2, clip.y + thick / 2, clip.width - thick, clip.height - thick);
        g.setColor(new Color(0xF0, 0xF0, 0xF0));
        g.fillRect(preview.x, preview.y, preview.width, preview.height);
        g.setStroke(new BasicStroke(thick));
        g.setColor(new Color(0xDE, 0xDE, 0xDE));
        g.drawRect(clip.x, clip.y, clip.width, clip.height);
        this.drawPoint(g, preview, preview.width / 2, preview.height / 2, p, o, XPoint.drawPreview);
        g.setStroke(savedStroke);
        g.setColor(savedColour);
    }

    public void toXML(Document doc, Element parent_element) {
        try {
            Element element = doc.createElement("XPoint");
            save(doc, element);
            parent_element.appendChild(element);
        } catch (Exception e) {
        }
    }

    public static String getString(XPoint point) {
        String ptype = "" + XPoint.POINT_NONE;
        String picontype = "" + XIcon.ICON_NONE;
        String piconname = "";
        String piconfilename = "";
        String piconimgloc = "";
        String pshapetype = "" + XShape.SHAPE_NONE;
        String pshapesize = "";
        String pshapename = "";
        String pshapescalable = "";
        String pshapeunit = "";
        String pshapesmooth = "";
        if (point != null) {
            ptype = Integer.toString(point.getType());
            if (point.getIcon() != null) {
                picontype = Integer.toString(point.getIcon().getType());
                piconname = point.getIcon().getName();
                piconfilename = point.getIcon().getFilename();
                piconimgloc = point.getIcon().getImageLocation();
            }
            if (point.getShape() != null) {
                pshapetype = Integer.toString(point.getShape().getType());
                pshapesize = Integer.toString(point.getShape().getSize());
                pshapename = point.getShape().getName();
                pshapescalable = Boolean.toString(point.getShape().getScalable());
                pshapeunit = point.getShape().getUnit().getName();
                pshapesmooth = Boolean.toString(point.getShape().getSmooth());
            }
        }
        return ("" + ptype + ";" + picontype + ";" + piconname + ";" + piconfilename + ";" + piconimgloc + ";" + pshapetype + ";" + pshapesize + ";" + pshapename + ";" + pshapescalable + ";" + pshapeunit + ";" + pshapesmooth);
    }

    public Shape getPointShape() {
        return getPointShape(new Point2D.Double(0, 0));
    }

    public Shape getPointShape(int drawtype, Rectangle clip) {
        return getPointShape(new Point2D.Double(0, 0), drawtype, clip);
    }

    public Shape getPointShape(Point2D point) {
        return getPointShape(point, XPoint.drawNormal, null);
    }

    public Shape getPointShape(Point2D point, int drawtype, Rectangle clip) {
        Shape shape = null;
        switch(this.getType()) {
            case POINT_NONE:
                break;
            case POINT_POINT:
                float diameter = 15;
                float radius = diameter / 2;
                shape = new Ellipse2D.Float((float) point.getX() - radius, (float) point.getY() - radius, diameter, diameter);
                break;
            case POINT_ICON:
                if (this.getIcon() != null) shape = this.getIcon().getIconShape(point, drawtype, clip);
                break;
            case POINT_SHAPE:
                if (this.getShape() != null) shape = this.getShape().getShapeShape(point, drawtype, clip);
                break;
        }
        return shape;
    }
}
