package visugraph.gview.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import visugraph.data.Data;
import visugraph.data.DataUtils;
import visugraph.gview.GraphComponent;
import visugraph.plugin.UserProperty;
import visugraph.plugin.UserPlugin;

/**
 * Renderer très simple dont les paramètres sont communs à toutes les arêtes.
 */
@UserPlugin("Renderer simple")
public class SimpleEdgeRenderer<N, E> extends AbstractRenderer<E, N, E> {

    private DefaultEdgeRenderer<N, E> parentRenderer;

    private Color selectColor, highlightColor;

    private LineStroke lineStyle;

    private LabelPosition labelPosition;

    private ArrowShape arrowShape;

    private int width, labelGap;

    private Color color, labelColor;

    /**
	 * Construit un nouveau SimpleEdgeRenderer.
	 * @param component composant lié à ce renderer.
	 */
    public SimpleEdgeRenderer(GraphComponent<N, E> component) {
        super(component);
        this.parentRenderer = new DefaultEdgeRenderer<N, E>(component);
        this.setDetailsFactor(1);
        this.setColor(Color.GRAY);
        this.setSelectColor(new Color(0x80, 0x00, 0x00));
        this.setHighlightColor(new Color(0x00, 0x80, 0x00));
        this.setLineStyle(LineStroke.Solid);
        this.setWidth(1);
        this.setArrowShape(ArrowShape.Arrow);
        this.setLabelColor(Color.BLACK);
        this.setLabelPosition(LabelPosition.West);
        this.setLabelGap(5);
    }

    @UserProperty("Style des arêtes")
    public LineStroke getLineStyle() {
        return lineStyle;
    }

    @UserProperty("Style des arêtes")
    public void setLineStyle(LineStroke lineStyle) {
        this.parentRenderer.setLineStyleData(this.newConstData(lineStyle));
        this.lineStyle = lineStyle;
    }

    @UserProperty("Position des labels")
    public LabelPosition getLabelPosition() {
        return labelPosition;
    }

    @UserProperty("Position des labels")
    public void setLabelPosition(LabelPosition labelPosition) {
        this.parentRenderer.setLabelPositionData(this.newConstData(labelPosition));
        this.labelPosition = labelPosition;
    }

    @UserProperty("Symbole des flèches")
    public ArrowShape getArrowShape() {
        return arrowShape;
    }

    @UserProperty("Symbole des flèches")
    public void setArrowShape(ArrowShape shape) {
        this.parentRenderer.setArrowShapeData(this.newConstData(shape));
        this.arrowShape = shape;
    }

    @UserProperty("Epaisseur des arêtes")
    public int getWidth() {
        return this.width;
    }

    @UserProperty("Epaisseur des arêtes")
    public void setWidth(int width) {
        this.assertParam(width >= 0, "L'épaisseur de la ligne ne peut-être négative");
        this.parentRenderer.setWidthData(this.newConstData(width));
        this.width = width;
    }

    @UserProperty("Couleur des arêtes")
    public Color getColor() {
        return color;
    }

    @UserProperty("Couleur des arêtes")
    public void setColor(Color color) {
        this.parentRenderer.setPaintData(this.newConstData(color));
        this.color = color;
    }

    @UserProperty("Couleur des labels")
    public Color getLabelColor() {
        return labelColor;
    }

    @UserProperty("Couleur des labels")
    public void setLabelColor(Color labelColor) {
        this.parentRenderer.setLabelPaintData(this.newConstData(labelColor));
        this.labelColor = labelColor;
    }

    @UserProperty("Couleur des arêtes sélectionnées")
    public void setSelectColor(Color selectColor) {
        this.parentRenderer.setSelectPaint(this.selectColor);
        this.selectColor = selectColor;
    }

    @UserProperty("Couleur des arêtes sélectionnées")
    public Color getSelectColor() {
        return this.selectColor;
    }

    @UserProperty("Couleur des arêtes surlignées")
    public void setHighlightColor(Color hlColor) {
        this.parentRenderer.setHighlightPaint(hlColor);
        this.highlightColor = selectColor;
    }

    @UserProperty("Couleur des arêtes surlignées")
    public Color getHighlightColor() {
        return this.highlightColor;
    }

    @UserProperty("Espace entre arêtes et labels")
    public void setLabelGap(int gap) {
        this.parentRenderer.setLabelGap(gap);
        this.labelGap = gap;
    }

    @UserProperty("Espace entre arêtes et labels")
    public int getLabelGap() {
        return this.labelGap;
    }

    /**
	 * {@inheritDoc}
	 */
    public void draw(E edge, Graphics g) {
        this.parentRenderer.draw(edge, g);
    }

    /**
	 * {@inheritDoc}
	 */
    public Rectangle2D getBounds(E edge) {
        return this.parentRenderer.getBounds(edge);
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean isPresentIn(E edge, Rectangle2D rec) {
        return this.parentRenderer.isPresentIn(edge, rec);
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean isPresentOn(E edge, Point2D p) {
        return this.parentRenderer.isPresentOn(edge, p);
    }

    protected void repaintOnNeed(E elem) {
        this.getComponent().repaintEdgeArea(elem);
    }

    /**
	 * Construit une nouvelle donnée constante pour les arêtes.
	 * @param value valeur de la nouvelle donnée
	 */
    private <T> Data<E, T> newConstData(T value) {
        return DataUtils.constData(value);
    }
}
