package collab.fm.server.bean.persist.entity;

import collab.fm.server.bean.transfer.DataItem2;
import collab.fm.server.bean.transfer.NumericAttributeType2;

public class NumericAttributeType extends AttributeType {

    private float min;

    private float max;

    private String unit;

    public NumericAttributeType() {
        super();
        this.typeName = AttributeType.TYPE_NUMBER;
    }

    @Override
    public void transfer(DataItem2 a) {
        NumericAttributeType2 that = (NumericAttributeType2) a;
        super.transfer(that);
        that.setMin(this.getMin());
        that.setMax(this.getMax());
        that.setUnit(this.getUnit());
    }

    @Override
    public boolean valueConformsToType(String v) {
        try {
            Float val = Float.valueOf(v);
            return !val.isNaN() && val.compareTo(min) >= 0 && val.compareTo(max) <= 0;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
