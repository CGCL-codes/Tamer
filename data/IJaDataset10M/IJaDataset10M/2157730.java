package apollo.gui.synteny;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import apollo.datamodel.FeaturePair;
import apollo.datamodel.FeaturePairI;
import apollo.datamodel.Link;
import apollo.datamodel.Range;
import apollo.datamodel.SeqFeatureI;
import apollo.config.Config;
import apollo.config.FeatureProperty;
import apollo.config.PropertyScheme;

/** A DrawableLink that draws the link as a Polygon, where the 2 ends are the regions of
    similarity */
class SyntenyLinkPolygon extends Polygon implements DrawableLink {

    private Link link;

    protected String name;

    protected FeatureProperty property;

    protected double size;

    private int queryWidth;

    private int queryY;

    private SyntenyLinkPanel syntenyLinkPanel;

    private boolean selected = false;

    static PropertyScheme pscheme = Config.getPropertyScheme();

    SyntenyLinkPolygon(Link link, String name, SyntenyLinkPanel s) {
        super();
        setLink(link);
        setProperty();
        this.syntenyLinkPanel = s;
    }

    /** setQueryPoint has to be called before setHitPoint */
    public void setQueryPixels(int x1, int x2, int y) {
        addPoint(x1, y);
        addPoint(x2, y);
        queryWidth = Math.abs(x2 - x1);
        queryY = y;
    }

    /** Point p1 to the left of(less than) point p2 */
    public void setHitPixels(int x1, int x2, int y) {
        int xSmaller = x1 < x2 ? x1 : x2;
        int xLarger = x1 > x2 ? x1 : x2;
        addPoint(xLarger, y);
        addPoint(xSmaller, y);
        setSize((xLarger - xSmaller + queryWidth) / 2 * Math.abs(y - queryY));
    }

    void setLink(Link link) {
        this.link = link;
    }

    public Link getLink() {
        return link;
    }

    private double getPercentIdentity() {
        return getLink().getPercentIdentity();
    }

    /** Return property color if not selected. If selected return selected color. 
      If we have no property - gff doesnt seem to - just return blue */
    public Color getColour() {
        if (property == null) return Color.BLUE;
        return syntenyLinkPanel.getColor(property, selected);
    }

    protected void setProperty() {
        if (link != null && link.getType() != null) {
            this.property = getFeatureType(link.getType());
        } else {
            this.property = null;
        }
    }

    protected FeatureProperty getProperty() {
        return property;
    }

    protected FeatureProperty getFeatureType(String type) {
        FeatureProperty prop = null;
        if ((prop = pscheme.getFeatureProperty(type)) == null) {
            System.out.println("No type " + type);
        }
        return prop;
    }

    public boolean isVisible() {
        if (this.property == null) {
            return true;
        } else {
            return property.getTier().isVisible();
        }
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getSize() {
        return size;
    }

    private Polygon getPolygon() {
        return this;
    }

    /** Do the paint score ranges need to be configged? Or figured from the range
      in .tiers? 
      Changed so if <=0 it does low match point - syntenic blocks have "0" score
      they should probably have shade by perc id disabled but at least they
      wont disappear now
  */
    private void setPaintByScore(Graphics2D g2d) {
        if (getPercentIdentity() > 80) g2d.setPaint(syntenyLinkPanel.getVeryHighMatchPaint(getProperty(), selected)); else if (getPercentIdentity() > 65) g2d.setPaint(syntenyLinkPanel.getHighMatchPaint(getProperty(), selected)); else if (getPercentIdentity() > 50) g2d.setPaint(syntenyLinkPanel.getMediumMatchPaint(getProperty(), selected)); else g2d.setPaint(syntenyLinkPanel.getLowMatchPaint(getProperty(), selected));
    }

    public void draw(Graphics g, boolean full) {
        if (!isVisible()) return;
        Graphics2D g2d = null;
        if (g instanceof Graphics2D) {
            g2d = (Graphics2D) g;
        }
        Polygon p = getPolygon();
        if (full) {
            g.setColor(Config.getEdgematchColor());
            if (full) g.drawPolygon(p);
        }
        if (syntenyLinkPanel.getShadeByPercId() && getLink().hasPercentIdentity() && g2d != null) {
            setPaintByScore((Graphics2D) g2d);
        } else {
            if (g2d != null) {
                g2d.setPaint(getColour());
            } else {
                g.setColor(getColour());
            }
        }
        if (p.xpoints[0] == p.xpoints[1] && p.xpoints[2] == p.xpoints[3]) {
            g.drawLine(p.xpoints[0], p.ypoints[0], p.xpoints[2], p.ypoints[2]);
        } else {
            g.fillPolygon(p);
        }
    }

    /** Returns the mid point in user coords on the query end of link(top) */
    public Point getQueryMidUserPoint() {
        int xMidPixel = (xpoints[0] + xpoints[1]) / 2;
        int yMid = 0;
        return syntenyLinkPanel.getQueryTransformer().toUser(xMidPixel, yMid);
    }

    /** Returns the mid point in user coords on the hit end of link(bottom) */
    public Point getHitMidUserPoint() {
        int xMidPixel = (xpoints[2] + xpoints[3]) / 2;
        int yMid = 0;
        return syntenyLinkPanel.getHitTransformer().toUser(xMidPixel, yMid);
    }

    public boolean intersects(Rectangle r) {
        return getPolygon().intersects(r);
    }

    /** Get string used for status bar: score:queryName-hitName */
    public String getStatusBarString() {
        String scoreString = "";
        if (getPercentIdentity() > 0) scoreString = String.valueOf(getPercentIdentity()) + ":";
        String name1 = getLink().getName1();
        String name2 = getLink().getName2();
        if (name1 == null || name1.equals(Range.NO_NAME)) {
            name1 = "(" + getLink().getLow1() + "-" + getLink().getHigh1() + ")";
            name2 = "(" + getLink().getLow2() + "-" + getLink().getHigh2() + ")";
        }
        return scoreString + name1 + "-" + name2;
    }

    /** Returns true if query and hit have names - this is a precondition for firing
      name selection */
    public boolean hasNames() {
        return getLink().hasNames();
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
