package org.jenia.faces.chart.taglib;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import org.jenia.faces.chart.component.html.HtmlPieChart3D;

public class PieChart3DTag extends ChartTag {

    private ValueExpression pieDataSet;

    private ValueExpression crush;

    private ValueExpression paint;

    private ValueExpression transparency;

    private ValueExpression plotColor;

    public String getComponentType() {
        return HtmlPieChart3D.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return HtmlPieChart3D.RENDERER_TYPE;
    }

    public void setPieDataSet(ValueExpression pieDataSet) {
        this.pieDataSet = pieDataSet;
    }

    public void setCrush(ValueExpression crush) {
        this.crush = crush;
    }

    public void setPaint(ValueExpression paint) {
        this.paint = paint;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        HtmlPieChart3D chart = null;
        try {
            chart = (HtmlPieChart3D) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: UIPanel.  Perhaps you're missing a tag?");
        }
        if (pieDataSet != null) {
            if (pieDataSet.isLiteralText()) {
                throw new IllegalStateException("Invalid pieDataSet reference." + pieDataSet);
            } else {
                chart.setValueExpression("pieDataSet", pieDataSet);
            }
        }
        if (crush != null) {
            if (crush.isLiteralText()) {
                chart.setCrush(crush.getExpressionString());
            } else {
                chart.setValueExpression("crush", crush);
            }
        }
        if (paint != null) {
            if (paint.isLiteralText()) {
                throw new IllegalStateException("Invalid paint reference : " + paint);
            } else {
                chart.setValueExpression("paint", paint);
            }
        }
        if (transparency != null) {
            if (transparency.isLiteralText()) {
                chart.setTransparency(transparency.getExpressionString());
            } else {
                chart.setValueExpression("transparency", transparency);
            }
        }
        if (plotColor != null) {
            if (plotColor.isLiteralText()) {
                chart.setPlotColor(plotColor.getExpressionString());
            } else {
                chart.setValueExpression("plotColor", plotColor);
            }
        }
    }

    public void setTransparency(ValueExpression transparency) {
        this.transparency = transparency;
    }

    public void setPlotColor(ValueExpression plotColor) {
        this.plotColor = plotColor;
    }
}
