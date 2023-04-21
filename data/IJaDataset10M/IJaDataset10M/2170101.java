package de.grogra.imp3d.spectral;

public class RGBSpectralCurve extends SpectralCurve {

    private float r;

    private float g;

    private float b;

    public static final Type $TYPE;

    public static final Type.Field r$FIELD;

    public static final Type.Field g$FIELD;

    public static final Type.Field b$FIELD;

    public static class Type extends de.grogra.persistence.SCOType {

        public Type(Class c, de.grogra.persistence.SCOType supertype) {
            super(c, supertype);
        }

        public Type(RGBSpectralCurve representative, de.grogra.persistence.SCOType supertype) {
            super(representative, supertype);
        }

        Type(Class c) {
            super(c, de.grogra.persistence.SCOType.$TYPE);
        }

        private static final int SUPER_FIELD_COUNT = de.grogra.persistence.SCOType.FIELD_COUNT;

        protected static final int FIELD_COUNT = de.grogra.persistence.SCOType.FIELD_COUNT + 3;

        static Field _addManagedField(Type t, String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            return t.addManagedField(name, modifiers, type, componentType, id);
        }

        @Override
        protected void setFloat(Object o, int id, float value) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 0:
                    ((RGBSpectralCurve) o).r = (float) value;
                    return;
                case Type.SUPER_FIELD_COUNT + 1:
                    ((RGBSpectralCurve) o).g = (float) value;
                    return;
                case Type.SUPER_FIELD_COUNT + 2:
                    ((RGBSpectralCurve) o).b = (float) value;
                    return;
            }
            super.setFloat(o, id, value);
        }

        @Override
        protected float getFloat(Object o, int id) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 0:
                    return ((RGBSpectralCurve) o).r;
                case Type.SUPER_FIELD_COUNT + 1:
                    return ((RGBSpectralCurve) o).g;
                case Type.SUPER_FIELD_COUNT + 2:
                    return ((RGBSpectralCurve) o).b;
            }
            return super.getFloat(o, id);
        }

        @Override
        public Object newInstance() {
            return new RGBSpectralCurve();
        }
    }

    public de.grogra.persistence.ManageableType getManageableType() {
        return $TYPE;
    }

    static {
        $TYPE = new Type(RGBSpectralCurve.class);
        r$FIELD = Type._addManagedField($TYPE, "r", Type.Field.PRIVATE | Type.Field.SCO, de.grogra.reflect.Type.FLOAT, null, Type.SUPER_FIELD_COUNT + 0);
        g$FIELD = Type._addManagedField($TYPE, "g", Type.Field.PRIVATE | Type.Field.SCO, de.grogra.reflect.Type.FLOAT, null, Type.SUPER_FIELD_COUNT + 1);
        b$FIELD = Type._addManagedField($TYPE, "b", Type.Field.PRIVATE | Type.Field.SCO, de.grogra.reflect.Type.FLOAT, null, Type.SUPER_FIELD_COUNT + 2);
        $TYPE.validate();
    }

    public RGBSpectralCurve() {
    }

    private static final int SMITS_WHITE_BASE_FUNC = 0;

    private static final int SMITS_CYAN_BASE_FUNC = 1;

    private static final int SMITS_MAGENTA_BASE_FUNC = 2;

    private static final int SMITS_YELLOW_BASE_FUNC = 3;

    private static final int SMITS_RED_BASE_FUNC = 4;

    private static final int SMITS_GREEN_BASE_FUNC = 5;

    private static final int SMITS_BLUE_BASE_FUNC = 6;

    private static final float SMITS_MAX_LAMBDA = 720.f;

    private static final float SMITS_MIN_LAMBDA = 380.f;

    private static final int SMITS_BINS = 10;

    private static final float SMITS_SCALE = 1.0f;

    private static final float smits_lut[][] = { { 1.0000f, 1.0000f, 0.9999f, 0.9993f, 0.9992f, 0.9998f, 1.0000f, 1.0000f, 1.0000f, 1.0000f }, { 0.9710f, 0.9426f, 1.0007f, 1.0007f, 1.0007f, 1.0007f, 0.1564f, 0.0000f, 0.0000f, 0.0000f }, { 1.0000f, 1.0000f, 0.9685f, 0.2229f, 0.0000f, 0.0458f, 0.8369f, 1.0000f, 1.0000f, 0.9959f }, { 0.0001f, 0.0000f, 0.1088f, 0.6651f, 1.0000f, 1.0000f, 0.9996f, 0.9586f, 0.9685f, 0.9840f }, { 0.1012f, 0.0515f, 0.0000f, 0.0000f, 0.0000f, 0.0000f, 0.8325f, 1.0149f, 1.0149f, 1.0149f }, { 0.0000f, 0.0000f, 0.0273f, 0.7937f, 1.0000f, 0.9418f, 0.1719f, 0.0000f, 0.0000f, 0.0025f }, { 1.0000f, 1.0000f, 0.8916f, 0.3323f, 0.0000f, 0.0000f, 0.0003f, 0.0369f, 0.0483f, 0.0496f } };

    static final int complement_lut[] = { SMITS_CYAN_BASE_FUNC, SMITS_MAGENTA_BASE_FUNC, SMITS_YELLOW_BASE_FUNC };

    static final int primary_lut[] = { SMITS_RED_BASE_FUNC, SMITS_GREEN_BASE_FUNC, SMITS_BLUE_BASE_FUNC };

    public RGBSpectralCurve(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    private int SampleSpectralIntervals1(float lambda, float minLambda, float maxLambda, int bins) {
        int id = (int) (((lambda - minLambda) / (maxLambda - minLambda)) * bins);
        id = Math.min(id, (int) (bins - 1));
        id = Math.max(id, (int) (0));
        return id;
    }

    private int v3dominis() {
        return Math.abs(r) <= Math.abs(g) ? (Math.abs(r) <= Math.abs(b) ? 0 : 2) : (Math.abs(g) <= Math.abs(b) ? 1 : 2);
    }

    private float getChannel(int idx) {
        switch(idx) {
            case 0:
                return r;
            case 1:
                return g;
            case 2:
                return b;
        }
        return 0.f;
    }

    @Override
    public float sample(float lambda) {
        float power;
        int interval_idx = SampleSpectralIntervals1(lambda, SMITS_MIN_LAMBDA, SMITS_MAX_LAMBDA, SMITS_BINS);
        int min_idx = v3dominis();
        power = getChannel(min_idx) * smits_lut[SMITS_WHITE_BASE_FUNC][interval_idx] * SMITS_SCALE;
        int idx_1 = (min_idx + 1) % 3;
        int idx_2 = (min_idx + 2) % 3;
        if (getChannel(idx_1) > getChannel(idx_2)) {
            int tmp = idx_1;
            idx_1 = idx_2;
            idx_2 = tmp;
        }
        power += (getChannel(idx_1) - getChannel(min_idx)) * smits_lut[complement_lut[min_idx]][interval_idx] * SMITS_SCALE;
        power += (getChannel(idx_2) - getChannel(idx_1)) * smits_lut[primary_lut[idx_2]][interval_idx] * SMITS_SCALE;
        return power;
    }
}
