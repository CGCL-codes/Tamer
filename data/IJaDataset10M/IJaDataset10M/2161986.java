package org.timepedia.chronoscope.client.canvas;

import org.timepedia.chronoscope.client.Chart;
import org.timepedia.chronoscope.client.render.LinearGradient;

/**
 * @author Ray Cromwell <ray@timepedia.org>
 */
public abstract class AbstractLayer implements Layer {

    private Canvas canvas;

    public AbstractLayer() {
    }

    public AbstractLayer(Canvas canvas) {
        this.canvas = canvas;
    }

    public void clear() {
        clearRect(0, 0, getWidth(), getHeight());
    }

    public void clip(double x, double y, double width, double height) {
    }

    public DisplayList createDisplayList(String id) {
        return new DefaultDisplayListImpl(id, this);
    }

    public void drawRotatedText(double x, double y, double v, String label, String fontFamily, String fontWeight, String fontSize, String layerName, Chart chart) {
    }

    public void fillRect() {
        fillRect(0, 0, getWidth(), getHeight());
    }

    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public void rotate(double angle) {
    }

    public int rotatedStringHeight(String str, double rotationAngle, String fontFamily, String fontWeight, String fontSize) {
        rotationAngle = Math.abs(rotationAngle);
        return (int) (Math.sin(rotationAngle) * stringWidth(str, fontFamily, fontWeight, fontSize) + Math.cos(rotationAngle) * stringHeight(str, fontFamily, fontWeight, fontSize));
    }

    public int rotatedStringWidth(String str, double rotationAngle, String fontFamily, String fontWeight, String fontSize) {
        rotationAngle = Math.abs(rotationAngle);
        return (int) (Math.cos(rotationAngle) * stringWidth(str, fontFamily, fontWeight, fontSize) + Math.sin(rotationAngle) * stringHeight(str, fontFamily, fontWeight, fontSize));
    }

    public abstract void setFillColor(Color color);

    public abstract void setStrokeColor(Color color);

    public void setFillColor(PaintStyle p) {
        if (p instanceof Color) {
            setFillColor((Color) p);
        } else if (p instanceof LinearGradient) {
            setLinearGradient((LinearGradient) p);
        } else if (p instanceof RadialGradient) {
            setRadialGradient((RadialGradient) p);
        } else if (p instanceof CanvasPattern) {
            setCanvasPattern((CanvasPattern) p);
        }
    }

    public void setShadowColor(Color shadowColor) {
        setShadowColor(shadowColor.toString());
    }

    public void setStrokeColor(PaintStyle p) {
        if (p instanceof Color) {
            setStrokeColor((Color) p);
        }
    }
}
