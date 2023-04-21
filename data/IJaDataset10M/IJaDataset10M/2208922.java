package de.grogra.blocks.arrangeBlock;

import de.grogra.blocks.Attributes;
import de.grogra.persistence.SCOType;
import de.grogra.util.EnumerationType;

public class HalftoningArrange extends ShareableArrangeBase implements ArrangeMethod {

    private static final EnumerationType TYPE = new EnumerationType("halftoningType", Attributes.I18N, 12);

    int method = 0;

    float threshold = 0.5f;

    float maxThreshold = 100;

    public void calculate() {
        ArrangeMethod arranger = null;
        switch(method) {
            case 0:
                arranger = new DotByDotDither(maxX, maxY, threshold, maxThreshold, densityField);
                break;
            case 1:
                arranger = new RandomDither(maxX, maxY, threshold, maxThreshold, densityField);
                break;
            case 2:
                arranger = new AverageDither(maxX, maxY, threshold, maxThreshold, densityField);
                break;
            case 3:
                arranger = new HilbertDither(maxX, maxY, threshold, maxThreshold, densityField);
                break;
            case 4:
                arranger = new FloydSteinberg(maxX, maxY, threshold, maxThreshold, densityField);
                break;
            case 5:
                arranger = new Stucki6Dither(maxX, maxY, threshold, maxThreshold, densityField);
                break;
            case 6:
                arranger = new Stucki12Dither(maxX, maxY, threshold, maxThreshold, densityField);
                break;
            case 7:
                arranger = new Jarvis12Dither(maxX, maxY, threshold, maxThreshold, densityField);
                break;
            case 8:
                arranger = new BayerDither(maxX, maxY, threshold, maxThreshold, densityField);
                break;
            case 9:
                arranger = new SpiralDither(maxX, maxY, threshold, maxThreshold, densityField);
                break;
            case 10:
                arranger = new ClassicalDither(maxX, maxY, threshold, maxThreshold, densityField);
                break;
            case 11:
                arranger = new LineScreenDither(maxX, maxY, threshold, maxThreshold, densityField);
                break;
        }
        if (arranger != null) {
            xx = arranger.getXx();
            yy = arranger.getYy();
        }
    }

    public static final Type $TYPE;

    public static final Type.Field method$FIELD;

    public static final Type.Field threshold$FIELD;

    public static final Type.Field maxThreshold$FIELD;

    public static class Type extends SCOType {

        public Type(Class c, de.grogra.persistence.SCOType supertype) {
            super(c, supertype);
        }

        public Type(HalftoningArrange representative, de.grogra.persistence.SCOType supertype) {
            super(representative, supertype);
        }

        Type(Class c) {
            super(c, SCOType.$TYPE);
        }

        private static final int SUPER_FIELD_COUNT = SCOType.FIELD_COUNT;

        protected static final int FIELD_COUNT = SCOType.FIELD_COUNT + 3;

        static Field _addManagedField(Type t, String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            return t.addManagedField(name, modifiers, type, componentType, id);
        }

        @Override
        protected void setInt(Object o, int id, int value) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 0:
                    ((HalftoningArrange) o).method = (int) value;
                    return;
            }
            super.setInt(o, id, value);
        }

        @Override
        protected int getInt(Object o, int id) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 0:
                    return ((HalftoningArrange) o).getMethod();
            }
            return super.getInt(o, id);
        }

        @Override
        protected void setFloat(Object o, int id, float value) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 1:
                    ((HalftoningArrange) o).threshold = (float) value;
                    return;
                case Type.SUPER_FIELD_COUNT + 2:
                    ((HalftoningArrange) o).maxThreshold = (float) value;
                    return;
            }
            super.setFloat(o, id, value);
        }

        @Override
        protected float getFloat(Object o, int id) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 1:
                    return ((HalftoningArrange) o).getThreshold();
                case Type.SUPER_FIELD_COUNT + 2:
                    return ((HalftoningArrange) o).getMaxThreshold();
            }
            return super.getFloat(o, id);
        }

        @Override
        public Object newInstance() {
            return new HalftoningArrange();
        }
    }

    public de.grogra.persistence.ManageableType getManageableType() {
        return $TYPE;
    }

    static {
        $TYPE = new Type(HalftoningArrange.class);
        method$FIELD = Type._addManagedField($TYPE, "method", 0 | Type.Field.SCO, TYPE, null, Type.SUPER_FIELD_COUNT + 0);
        threshold$FIELD = Type._addManagedField($TYPE, "threshold", 0 | Type.Field.SCO, de.grogra.reflect.Type.FLOAT, null, Type.SUPER_FIELD_COUNT + 1);
        maxThreshold$FIELD = Type._addManagedField($TYPE, "maxThreshold", 0 | Type.Field.SCO, de.grogra.reflect.Type.FLOAT, null, Type.SUPER_FIELD_COUNT + 2);
        threshold$FIELD.setMinValue(new Float(0));
        threshold$FIELD.setMaxValue(new Float(15));
        maxThreshold$FIELD.setMinValue(new Float(0));
        maxThreshold$FIELD.setMaxValue(new Float(100));
        $TYPE.validate();
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int value) {
        this.method = (int) value;
    }

    public float getThreshold() {
        return threshold;
    }

    public void setThreshold(float value) {
        this.threshold = (float) value;
    }

    public float getMaxThreshold() {
        return maxThreshold;
    }

    public void setMaxThreshold(float value) {
        this.maxThreshold = (float) value;
    }
}
