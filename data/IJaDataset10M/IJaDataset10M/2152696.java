package ibg.math.plot;

import ibg.math.APoint2D;
import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author mchaberski
 *
 */
public class LabeledPointSet<L> extends PointSet {

    public static final float DEFAULT_LABEL_POSITION = (float) (-Math.PI / 4d);

    public static final int DEFAULT_LABEL_MARGIN = 1;

    protected boolean autoLabel;

    protected Map<APoint2D, L> labelMap;

    protected float labelPosition;

    protected Color labelColor;

    protected boolean labelVisible;

    protected int labelMargin;

    /**
	 * 
	 */
    public LabeledPointSet() {
        super();
        initLabelFields();
    }

    /**
	 * @param visible
	 * @param pointShape
	 * @param pointWidth
	 * @param pointHeight
	 * @param pointColor
	 */
    public LabeledPointSet(boolean visible, PointShape pointShape, int pointWidth, int pointHeight, Color pointColor, Color labelColor) {
        super(visible, pointShape, pointWidth, pointHeight, pointColor);
        initLabelFields();
        setLabelColor(labelColor);
    }

    private void initLabelFields() {
        this.autoLabel = true;
        this.labelMap = new HashMap<APoint2D, L>();
        labelVisible = true;
        labelPosition = DEFAULT_LABEL_POSITION;
        labelColor = color;
        labelMargin = DEFAULT_LABEL_MARGIN;
    }

    public boolean containsKey(Object key) {
        return labelMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return labelMap.containsValue(value);
    }

    public L get(Object key) {
        return labelMap.get(key);
    }

    public Set<APoint2D> keySet() {
        return labelMap.keySet();
    }

    public L put(APoint2D key, L value) {
        return labelMap.put(key, value);
    }

    public void putAll(Map<? extends APoint2D, ? extends L> m) {
        labelMap.putAll(m);
        setAutoLabel(false);
    }

    public void paint(Painter painter, Plot2D plot) {
        int index = 0;
        boolean sameColors = color.equals(labelColor);
        for (APoint2D p : data) {
            String label = null;
            if (autoLabel) {
                label = String.valueOf(index);
            } else {
                L labelObj = get(p);
                if (labelObj != null) {
                    label = labelObj.toString();
                } else {
                    label = new String();
                }
            }
            paintPointAndLabel(painter, plot, p, label, sameColors);
            index++;
        }
    }

    private void paintPointAndLabel(Painter painter, Plot2D plot, APoint2D p, String label, boolean sameColors) {
        if (sameColors) {
            super.paintPointInCurrentColor(painter, p, plot);
            if (labelVisible) {
                paintLabelInCurrentColor(painter, p, label, plot);
            }
        } else {
            painter.setColor(color);
            super.paintPointInCurrentColor(painter, p, plot);
            if (labelVisible) {
                painter.setColor(labelColor);
                paintLabelInCurrentColor(painter, p, label, plot);
            }
        }
    }

    /** Paints the label in the current color of the painter.
	 * @param painter the painter
	 * @param label the label
	 * @param point the point for the label
	 * @param plot the plot
	 */
    protected void paintLabelInCurrentColor(Painter painter, APoint2D point, String label, Plot2D plot) {
        java.awt.Point pixelPoint = painter.getTransformed(point.getX(), point.getY());
        int labelX = pixelPoint.x + (int) Math.round(pointWidth * Math.cos(labelPosition));
        int labelY = pixelPoint.y + (int) Math.round(pointHeight * Math.sin(labelPosition));
        painter.setColor(labelColor);
        painter.drawStringAbsolute(label, labelX, labelY);
    }

    public void setAutoLabel(boolean autoLabel) {
        this.autoLabel = autoLabel;
    }

    public void setLabelColor(Color labelColor) {
        this.labelColor = labelColor;
    }

    public void setLabelPosition(float labelPosition) {
        this.labelPosition = labelPosition;
    }

    public void setLabelVisible(boolean labelVisible) {
        this.labelVisible = labelVisible;
    }

    public void setPointProperties(Color pointColor, Color labelColor, PointShape shape, int width, int height) {
        super.setPointProperties(pointColor, shape, width, height);
        this.labelColor = labelColor;
    }
}
