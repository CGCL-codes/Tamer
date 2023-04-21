package org.intranet.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Neil McKellar and Chris Dailey
 *
 */
public class FloatParameter extends SingleValueParameter {

    private float value;

    public FloatParameter(String desc, float defaultValue) {
        super(desc);
        value = defaultValue;
    }

    public void setValueFromUI(Object param) {
        value = Float.parseFloat((String) param);
    }

    public Object getUIValue() {
        return String.valueOf(value);
    }

    public float getFloatValue() {
        return value;
    }

    void setFloatValue(float newValue) {
        value = newValue;
    }

    public List getValues(String min, String max, String inc) {
        float minFloat = Float.parseFloat(min);
        float maxFloat = Float.parseFloat(max);
        float incFloat = Float.parseFloat(inc);
        if (minFloat > maxFloat) {
            float temp = minFloat;
            minFloat = maxFloat;
            maxFloat = temp;
        }
        int capacity = (int) ((maxFloat - minFloat) / incFloat + 1);
        List intValues = new ArrayList(capacity);
        for (float val = minFloat; val <= maxFloat; val += incFloat) intValues.add(Float.toString(val));
        return intValues;
    }
}
