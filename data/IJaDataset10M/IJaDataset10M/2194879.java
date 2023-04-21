package org.jofc.attribute.chart.element;

import java.util.List;
import org.jofc.attribute.chart.element.value.CandleValue;
import org.jofc.itf.JOFCElement;

public class CandleElement implements JOFCElement<CandleValue> {

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        return null;
    }

    @Override
    public void setValues(List<CandleValue> jOFCValues) {
    }
}
