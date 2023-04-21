package org.softmed.jops.valuelists;

import java.util.List;
import org.openmali.vecmath2.Point3f;
import org.softmed.jops.values.GenericValue;

public class ValueListp3f extends GeneralRandomValueList<Point3f> {

    Point3f temp;

    public ValueListp3f() {
        mainValueList = new SimpleValueListp3f();
        randomValueList = new SimpleValueListp3f();
    }

    @Override
    protected Point3f calculateRandomValue(Point3f original, Point3f random) {
        temp = new Point3f();
        temp.setX(original.getX() + generator.getFloat(random.getX()) - random.getX() * 0.5f);
        temp.setY(original.getY() + generator.getFloat(random.getY()) - random.getY() * 0.5f);
        temp.setZ(original.getZ() + generator.getFloat(random.getZ()) - random.getZ() * 0.5f);
        return temp;
    }

    public ValueListp3f getStandaloneCopy() {
        ValueListp3f copy = new ValueListp3f();
        copy.bias = bias;
        copy.active = active;
        copy.random = random;
        List<GenericValue> values = mainValueList.getValues();
        for (GenericValue value : values) {
            copy.mainValueList.addValue(new Point3f((Point3f) value.getValue()), value.getTime());
            copy.mainValueList.repeat = mainValueList.repeat;
        }
        values = randomValueList.getValues();
        for (GenericValue value : values) {
            copy.randomValueList.addValue(new Point3f((Point3f) value.getValue()), value.getTime());
            copy.randomValueList.repeat = randomValueList.repeat;
        }
        return copy;
    }
}
