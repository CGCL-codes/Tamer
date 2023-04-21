package org.jarp.gui.jhotdraw.figures;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import org.jarp.gui.jhotdraw.io.PNMLStorageFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import CH.ifa.draw.figures.RectangleFigure;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureAttributeConstant;
import CH.ifa.draw.framework.FigureChangeListener;
import CH.ifa.draw.framework.HandleEnumeration;
import CH.ifa.draw.framework.Locator;
import CH.ifa.draw.standard.BoxHandleKit;
import CH.ifa.draw.standard.CompositeFigure;
import CH.ifa.draw.standard.ConnectionHandle;
import CH.ifa.draw.standard.HandleEnumerator;
import CH.ifa.draw.standard.RelativeLocator;
import CH.ifa.draw.util.StorableInput;
import CH.ifa.draw.util.StorableOutput;

/**
 * Petri transition implementation.
 * 
 * @version $Revision: 1.5 $
 * @author <a href="mailto:ricardo_padilha@users.sourceforge.net">Ricardo 
 * Sangoi Padilha</a>
 */
public class TransitionImpl extends RectangleFigure implements Transition, XMLStorable {

    private static final long serialVersionUID = -8817088806777332539L;

    private Text nameFigure;

    private boolean simulating, active;

    private static double zoom = 1.0;

    private static int width = 32, height = 8;

    /**
	 * Constructor for <code>TransitionImpl</code>.
	 * Used for serialization. 
	 */
    public TransitionImpl() {
        init();
        Point origin = new Point(0, 0);
        Point corner = new Point((int) (width * zoom), (int) (height * zoom));
        super.basicDisplayBox(origin, corner);
    }

    /**
	 * Constructor for <code>TransitionImpl</code>.
	 * @param origin starting point of the component
	 * @param corner ending point
	 */
    public TransitionImpl(Point origin, Point end) {
        init();
        Point corner = new Point((int) (origin.getX() + width * zoom), (int) (origin.getY() + height * zoom));
        super.basicDisplayBox(origin, corner);
    }

    /**
	 * Constructor for <code>TransitionImpl</code>.
	 * Used for serialization.
	 * @param new_transition
	 */
    public TransitionImpl(boolean new_transition) {
        this();
        if (new_transition) {
            nameFigure = new Text();
            nameFigure.connect(this);
        }
    }

    /**
	 * @see java.lang.Object#clone()
	 */
    public Object clone() {
        TransitionImpl t = (TransitionImpl) super.clone();
        t.init();
        return t;
    }

    /**
	 * @see CH.ifa.draw.framework.Figure#connectedTextLocator(CH.ifa.draw.framework.Figure)
	 */
    public Locator connectedTextLocator(Figure text) {
        return new RelativeLocator(1.2, 1.2);
    }

    /**
	 * @see org.jarp.components.XMLStorable#createNode(org.w3c.dom.Document)
	 */
    public Node createNode(Document doc) {
        Node node = doc.createElement("transition");
        node.appendChild(PNMLStorageFormat.createDimensionNode(doc, displayBox()));
        Node extNode = PNMLStorageFormat.createValuedNode(doc, "name", nameFigure.getText());
        extNode.appendChild(PNMLStorageFormat.createOffsetNode(doc, displayBox(), nameFigure.displayBox()));
        node.appendChild(extNode);
        java.util.Hashtable table = new java.util.Hashtable();
        table.put("FrameColor", getAttribute(FigureAttributeConstant.FRAME_COLOR));
        table.put("FillColor", getAttribute(FigureAttributeConstant.FILL_COLOR));
        table.put("TextColor", getAttribute(FigureAttributeConstant.TEXT_COLOR));
        node.appendChild(PNMLStorageFormat.createToolSpecificNode(doc, table));
        return node;
    }

    /**
	 * @see CH.ifa.draw.framework.Figure#displayBox(java.awt.Point, java.awt.Point)
	 */
    public void displayBox(Point origin, Point c) {
        Rectangle r = new Rectangle(origin);
        r.add(c);
        if (r.height < 8 && r.height <= r.width) {
            r.width = 32;
            r.height = 8;
        } else if (r.width < 8 && r.width < r.height) {
            r.width = 8;
            r.height = 32;
        }
        double z;
        if (r.width > r.height) {
            z = (double) r.width / width;
        } else {
            z = (double) r.height / height;
        }
        if (z != 0) {
            zoom = z;
        }
        width = r.width;
        height = r.height;
        super.displayBox(new Point(r.x, r.y), new Point(r.x + r.width, r.y + r.height));
    }

    /**
	 * @see CH.ifa.draw.figures.AttributeFigure#drawBackground(java.awt.Graphics)
	 */
    public void drawBackground(Graphics g) {
        if (simulating && active) {
            g.setColor(new Color(0, 255, 0));
        } else if (simulating && !active) {
            g.setColor(new Color(255, 0, 0));
        }
        super.drawBackground(g);
    }

    /**
	 * @see CH.ifa.draw.framework.Figure#getAttribute(FigureAttributeConstant)
	 */
    public Object getAttribute(FigureAttributeConstant attr) {
        if ("Name".equals(attr.getName())) {
            return getName();
        } else if ("TextFigure".equals(attr.getName())) {
            return nameFigure;
        } else if ("Simulating".equals(attr.getName())) {
            return new Boolean(simulating);
        } else if ("Active".equals(attr.getName())) {
            return new Boolean(active);
        } else {
            return super.getAttribute(attr);
        }
    }

    /**
	 * @see org.jarp.components.PetriNetComponent#getName()
	 */
    public String getName() {
        if (nameFigure != null) {
            return nameFigure.getText();
        } else {
            return "";
        }
    }

    /**
	 * @see CH.ifa.draw.framework.Figure#handles()
	 */
    public HandleEnumeration handles() {
        List handles = new Vector();
        BoxHandleKit.addCornerHandles(this, handles);
        handles.add(new ConnectionHandle(this, RelativeLocator.center(), new ArcImpl()));
        return new HandleEnumerator(handles);
    }

    /**
	 * @see org.jarp.components.XMLStorable#parseNode(org.w3c.dom.Node)
	 */
    public void parseNode(Node node) throws IOException {
        Node extNode;
        String extNodeName;
        if (node.hasChildNodes()) {
            NodeList extNodes = node.getChildNodes();
            int l = extNodes.getLength();
            for (int j = 0; j < l; j++) {
                extNode = extNodes.item(j);
                extNodeName = extNode.getNodeName();
                if (extNodeName.equals("#text")) {
                } else if (extNodeName.equals("name")) {
                    nameFigure.setText(PNMLStorageFormat.readValue(extNode));
                    Point p = PNMLStorageFormat.readOffset(extNode);
                    Rectangle r1 = displayBox();
                    Rectangle r2 = nameFigure.displayBox();
                    nameFigure.moveBy(p.x + (r1.x - r2.x), p.y + (r1.y - r2.y));
                } else if (extNodeName.equals("graphics")) {
                    Point p = PNMLStorageFormat.readPosition(extNode);
                    Dimension d = PNMLStorageFormat.readDimension(extNode);
                    Rectangle r;
                    if (d.width == 0 && d.height == 0) {
                        r = displayBox();
                        r.setLocation(p);
                    } else {
                        r = new Rectangle(p.x, p.y, d.width, d.height);
                    }
                    displayBox(r);
                } else if (extNodeName.equals("toolspecific")) {
                    java.util.Map table = PNMLStorageFormat.readToolSpecific(extNode);
                    Object key;
                    for (java.util.Iterator e = table.keySet().iterator(); e.hasNext(); ) {
                        key = e.next();
                        setAttribute(FigureAttributeConstant.getConstant(key.toString()), table.get(key));
                    }
                }
            }
        }
    }

    /**
	 * @see CH.ifa.draw.util.Storable#read(CH.ifa.draw.util.StorableInput)
	 */
    public void read(StorableInput dr) throws IOException {
        super.read(dr);
        String name = dr.readString();
        if (!name.equals("")) {
            nameFigure = new Text();
            nameFigure.setText(name);
            nameFigure.connect(this);
            Rectangle r = displayBox();
            if (r.height != 8) {
                r.height = 8;
                displayBox(r);
            }
        }
    }

    /**
	 * @see CH.ifa.draw.framework.Figure#addToContainer(CH.ifa.draw.framework.FigureChangeListener)
	 */
    public void addToContainer(FigureChangeListener c) {
        super.addToContainer(c);
        if (nameFigure != null && c instanceof CompositeFigure) {
            ((CompositeFigure) c).add(nameFigure);
            nameFigure.connect(this);
        }
    }

    /**
	 * @see CH.ifa.draw.framework.Figure#removeFromContainer(CH.ifa.draw.framework.FigureChangeListener)
	 */
    public void removeFromContainer(FigureChangeListener c) {
        super.removeFromContainer(c);
        if (nameFigure != null && c instanceof CompositeFigure) {
            ((CompositeFigure) c).remove(nameFigure);
            nameFigure.disconnect(this);
        }
    }

    /**
	 * @see CH.ifa.draw.framework.Figure#setAttribute(java.lang.String, java.lang.Object)
	 */
    public void setAttribute(FigureAttributeConstant attr, Object value) {
        super.setAttribute(attr, value);
        if ("Name".equals(attr.getName())) {
            setName(String.valueOf(value));
        } else if ("TextFigure".equals(attr.getName())) {
            addFigureChangeListener((FigureChangeListener) value);
        } else if ("Simulating".equals(attr.getName())) {
            this.simulating = ((Boolean) value).booleanValue();
            changed();
        } else if ("Active".equals(attr.getName())) {
            this.active = ((Boolean) value).booleanValue();
            changed();
        }
    }

    /**
	 * @see org.jarp.components.PetriNetComponent#setName(java.lang.String)
	 */
    public void setName(String name) {
        if (nameFigure == null) {
            nameFigure = new Text();
            nameFigure.connect(this);
        }
        nameFigure.setText(name);
        nameFigure.changed();
    }

    /**
	 * @see CH.ifa.draw.util.Storable#write(CH.ifa.draw.util.StorableOutput)
	 */
    public void write(StorableOutput dw) {
        super.write(dw);
        dw.writeString("");
    }

    /**
	 * @see CH.ifa.draw.standard.AbstractFigure#basicMoveBy(int, int)
	 */
    protected void basicMoveBy(int x, int y) {
        Rectangle r = displayBox();
        r.translate(x, y);
        if (r.x < 0) {
            x = x - r.x;
        }
        if (r.y < 0) {
            y = y - r.y;
        }
        super.basicMoveBy(x, y);
    }

    /**
	 * Initiates an object and do some internal updating.
	 */
    protected void init() {
        setAttribute(FigureAttributeConstant.getConstant("Name"), "");
        setAttribute(FigureAttributeConstant.FILL_COLOR, Color.gray);
        setAttribute(FigureAttributeConstant.getConstant("Simulation"), String.valueOf(false));
    }
}
