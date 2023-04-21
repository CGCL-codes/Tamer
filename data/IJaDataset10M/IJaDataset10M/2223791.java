package model.style;

/**
 * @author dlegland
 *
 */
public class DrawStyleImpl implements DrawStyle {

    private MarkerStyle markerStyle;

    private LineStyle lineStyle;

    private FillStyle fillStyle;

    public DrawStyleImpl() {
        markerStyle = new MarkerStyleImpl();
        lineStyle = new LineStyleImpl();
        fillStyle = new FillStyleImpl();
    }

    public DrawStyleImpl(MarkerStyle markerStyle, LineStyle lineStyle, FillStyle fillStyle) {
        this.markerStyle = markerStyle;
        this.lineStyle = lineStyle;
        this.fillStyle = fillStyle;
    }

    public DrawStyleImpl(DrawStyle baseStyle) {
        this.markerStyle = baseStyle.getMarkerStyle();
        this.lineStyle = baseStyle.getLineStyle();
        this.fillStyle = baseStyle.getFillStyle();
    }

    public MarkerStyle getMarkerStyle() {
        return markerStyle;
    }

    public void setMarkerStyle(MarkerStyle markerStyle) {
        this.markerStyle = markerStyle;
    }

    public LineStyle getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(LineStyle lineStyle) {
        this.lineStyle = lineStyle;
    }

    public FillStyle getFillStyle() {
        return fillStyle;
    }

    public void setFillStyle(FillStyle fillStyle) {
        this.fillStyle = fillStyle;
    }
}
