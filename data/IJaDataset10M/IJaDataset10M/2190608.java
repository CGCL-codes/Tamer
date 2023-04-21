package org.jofc.attribute.chart;

import org.jofc.attribute.chart.element.RadarElement;
import org.jofc.attribute.chart.element.value.RadarValue;
import org.jofc.itf.JOFCChart;
import java.util.Arrays;
import java.util.List;

public class RadarChart implements JOFCChart<RadarElement, RadarValue> {

    public void setElements(RadarElement[] radarElements) {
        this.setElements(Arrays.asList(radarElements));
    }

    @Override
    public String getAttributeName() {
        return "elements";
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        return null;
    }

    @Override
    public void appendValues(List<RadarValue> values) {
    }

    @Override
    public void setElement(int index, RadarElement element) {
    }

    @Override
    public void setElements(List<RadarElement> jOFCElements) {
    }

    @Override
    public void setValues(int index, List<RadarValue> values) {
    }

    @Override
    public List<RadarElement> getElements() {
        return null;
    }

    @Override
    public Object getAttributeValue() {
        return null;
    }

    @Override
    public void appendElement(RadarElement element) {
    }
}
