package edu.yale.csgp.vitapad.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import edu.yale.csgp.vitapad.TypeManager;
import edu.yale.csgp.vitapad.db.DBManager;
import edu.yale.csgp.vitapad.visual.LabeledRenderer;
import edu.yale.csgp.vitapad.visual.Renderer;
import edu.yale.csgp.vitapad.visual.VertexRenderer;
import edu.yale.csgp.vitapad.visual.paint.Painter;
import edu.yale.csgp.vitapad.visual.type.Type;

/**
 * A basic implementation of the <code>Vertex</code> interface.
 * While, <code>PWVertex</code> is ultimately used to represent
 * compounds on the pathway graph, <code>VertexImp</code> is useful
 * as a temporary form in preview windows and so forth.
 * 
 * @see edu.yale.graph.Vertex
 * @see edu.yale.graph.PWVertex
 */
public class PathwayVertex extends AbstractVertex implements LabeledRenderer, LabeledElement, PathwayElement {

    private DataClass dataClass;

    private double onLinkPosition;

    private int onLink;

    private final LabeledElementImp labeler;

    private Renderer renderer;

    private int xPosition;

    private int yPosition;

    private Type initialType, type;

    /**
     * The visual type used if none is specified. Currently this is
     * DefaultVertexType1.
     */
    public static final Type DEFAULT_TYPE = (Type) TypeManager.getDefaultType(Type.VERTEX);

    /**
     * @param vertex
     */
    public PathwayVertex(PathwayVertex vertex) {
        this.graph = vertex.getGraph();
        this.id = vertex.getID();
        obj = vertex.getDataClass();
        this.dataClass = vertex.getDataClass();
        this.xPosition = vertex.getXPosition();
        this.yPosition = vertex.getYPosition();
        this.onLink = vertex.getOnLink();
        this.onLinkPosition = vertex.getOnLinkPosition();
        initialType = vertex.getInitialType();
        String label = vertex.getLabel();
        labeler = new LabeledElementImp(label);
        renderer = new VertexRenderer((VertexRenderer) vertex.getRenderer(), this);
        this.type = renderer.getType();
        rescale();
    }

    public PathwayVertex(Graph graph, int id, DataClass dcl, int xPosition, int yPosition) {
        this(graph, id, dcl, null, xPosition, yPosition, -1, -1);
    }

    public PathwayVertex(Graph graph, int id, DataClass dcl, Type type, int xPosition, int yPosition) {
        this(graph, id, dcl, type, xPosition, yPosition, -1, -1);
    }

    public PathwayVertex(Graph graph, int id, DataClass dcl, int xPosition, int yPosition, int onLink, double onLinkPosition) {
        this(graph, id, dcl, null, xPosition, yPosition, onLink, onLinkPosition);
    }

    public PathwayVertex(int id, DataClass dcl, Type type) {
        this(null, id, dcl, type, 0, 0, -1, -1);
    }

    public PathwayVertex(Graph graph, int id, DataClass dataClass, Type type, int xPosition, int yPosition, int onLink, double onLinkPosition) {
        this.graph = graph;
        this.id = id;
        obj = dataClass;
        this.dataClass = dataClass;
        this.type = type;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.onLink = onLink;
        this.onLinkPosition = onLinkPosition;
        initialType = (dataClass.getStoredType() != null) ? dataClass.getStoredType() : DEFAULT_TYPE;
        if (this.type == null) this.type = initialType;
        String label = (String) dataClass.get("name");
        labeler = new LabeledElementImp(label);
        renderer = new VertexRenderer(this);
        rescale();
    }

    public Type getInitialType() {
        return initialType;
    }

    public String getLabel() {
        return dataClass.get("name") != null ? (String) dataClass.get("name") : "";
    }

    public boolean hasLabel() {
        return dataClass.get("name") != null;
    }

    public void setLabel(String label) {
        labeler.setLabel(label);
    }

    /**
     * @return
     */
    public LabeledElementImp getLabeler() {
        return labeler;
    }

    /**
     * @return
     */
    public Renderer getRenderer() {
        return renderer;
    }

    /**
     * @return
     */
    public Type getType() {
        return type;
    }

    /**
     * @return
     */
    public int getXPosition() {
        return xPosition;
    }

    /**
     * @return
     */
    public int getYPosition() {
        return yPosition;
    }

    /**
     * @param type
     */
    public void setType(Type type) {
        this.type = type;
        renderer.setType(type);
    }

    /**
     * @param i
     */
    public void setXPosition(int i) {
        xPosition = i;
    }

    /**
     * @param i
     */
    public void setYPosition(int i) {
        yPosition = i;
    }

    /**
     * @return
     */
    public int getOnLink() {
        return onLink;
    }

    /**
     * @return
     */
    public double getOnLinkPosition() {
        return onLinkPosition;
    }

    /**
     * @param i
     */
    public void setOnLink(int i) {
        onLink = i;
    }

    /**
     * @param i
     */
    public void setOnLinkPosition(double d) {
        onLinkPosition = d;
    }

    public Color getFillColor() {
        return ((VertexRenderer) renderer).getFillColor();
    }

    public Font getFont() {
        return ((VertexRenderer) renderer).getFont();
    }

    public Color getFontColor() {
        return ((VertexRenderer) renderer).getFontColor();
    }

    public FontMetrics getFontMetrics() {
        return ((VertexRenderer) renderer).getFontMetrics();
    }

    public void rescale() {
        ((VertexRenderer) renderer).rescale();
    }

    public void setFillColor(Color fillColor) {
        ((VertexRenderer) renderer).setFillColor(fillColor);
    }

    public void setFont(Font font) {
        ((VertexRenderer) renderer).setFont(font);
    }

    public void setFontColor(Color fontColor) {
        ((VertexRenderer) renderer).setFontColor(fontColor);
    }

    public void setLocation(double x, double y) {
        ((VertexRenderer) renderer).setLocation(x, y);
    }

    public boolean containsPoint(int x, int y) {
        return ((VertexRenderer) renderer).containsPoint(x, y);
    }

    public Rectangle getBounds() {
        return ((VertexRenderer) renderer).getBounds();
    }

    public Rectangle2D getBounds2D() {
        return ((VertexRenderer) renderer).getBounds2D();
    }

    public GeneralPath getGeneralPath() {
        return ((VertexRenderer) renderer).getGeneralPath();
    }

    public Color getOutlineColor() {
        return ((VertexRenderer) renderer).getOutlineColor();
    }

    public Painter getPainter() {
        return ((VertexRenderer) renderer).getPainter();
    }

    public TexturePaint getTexturePaint() {
        return ((VertexRenderer) renderer).getTexturePaint();
    }

    public void paint(Graphics2D g2) {
        ((VertexRenderer) renderer).paint(g2);
    }

    public void setGeneralPath(GeneralPath path) {
        ((VertexRenderer) renderer).setGeneralPath(path);
    }

    public void setOutlineColor(Color outlineColor) {
        ((VertexRenderer) renderer).setOutlineColor(outlineColor);
    }

    public void setPainter(Painter painter) {
        ((VertexRenderer) renderer).setPainter(painter);
    }

    public void setTexturePaint(TexturePaint tp) {
        ((VertexRenderer) renderer).setTexturePaint(tp);
    }

    /**
     * @return
     */
    public String getShape() {
        return ((VertexRenderer) renderer).getShape();
    }

    /**
     * @param shape
     */
    public void setShape(String shape) {
        ((VertexRenderer) renderer).setShape(shape);
    }

    public DataClass getDataClass() {
        return dataClass;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public void setDataClass(DataClass dataClass) {
        this.dataClass = dataClass;
        String label = (String) dataClass.get("name");
        labeler.setLabel(label);
        rescale();
    }

    public PathwayElement getElement() {
        return renderer.getElement();
    }

    public void setElement(PathwayElement element) {
        renderer.setElement(element);
    }

    public int getElementType() {
        return DBManager.VERTEX;
    }

    public PathwayElement copy() {
        return new PathwayVertex(this);
    }

    public void details() {
        System.out.println("VERTEX::" + getLabel());
    }
}
