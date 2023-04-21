package org.jofc.attribute.chart.element.value;

import org.jofc.enums.LineValueType;
import org.jofc.itf.JOFCValue;
import org.jofc.util.JOFCUtils;
import net.sf.json.JSONObject;

public class LineValue implements JOFCValue {

    private LineValueType type;

    private double value;

    private String colour;

    private String tip;

    public LineValue(double value) {
        this.value = value;
        type = null;
    }

    public LineValue() {
        value = 1.00;
        type = null;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public LineValueType getType() {
        return type;
    }

    public void setType(LineValueType type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        if (type == null) {
            return null;
        }
        JSONObject jo = new JSONObject();
        JOFCUtils.appendJson(jo, "type", type.value);
        JOFCUtils.appendJson(jo, "value", value);
        JOFCUtils.appendJson(jo, "colour", colour);
        JOFCUtils.appendJson(jo, "tip", tip == null ? null : "#val#<br>" + tip);
        return jo.toString();
    }
}
