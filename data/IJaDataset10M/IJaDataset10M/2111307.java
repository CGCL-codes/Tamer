package de.grogra.imp3d.shading;

public class Julia extends VolumeFunction {

    int iterations = 10;

    float cx = 0.6f;

    float cy = 0.6f;

    public static final NType $TYPE;

    public static final NType.Field iterations$FIELD;

    public static final NType.Field cx$FIELD;

    public static final NType.Field cy$FIELD;

    private static final class _Field extends NType.Field {

        private final int id;

        _Field(String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            super(Julia.$TYPE, name, modifiers, type, componentType);
            this.id = id;
        }

        @Override
        public void setInt(Object o, int value) {
            switch(id) {
                case 0:
                    ((Julia) o).iterations = (int) value;
                    return;
            }
            super.setInt(o, value);
        }

        @Override
        public int getInt(Object o) {
            switch(id) {
                case 0:
                    return ((Julia) o).getIterations();
            }
            return super.getInt(o);
        }

        @Override
        public void setFloat(Object o, float value) {
            switch(id) {
                case 1:
                    ((Julia) o).cx = (float) value;
                    return;
                case 2:
                    ((Julia) o).cy = (float) value;
                    return;
            }
            super.setFloat(o, value);
        }

        @Override
        public float getFloat(Object o) {
            switch(id) {
                case 1:
                    return ((Julia) o).getCx();
                case 2:
                    return ((Julia) o).getCy();
            }
            return super.getFloat(o);
        }
    }

    static {
        $TYPE = new NType(new Julia());
        $TYPE.addManagedField(iterations$FIELD = new _Field("iterations", 0 | _Field.SCO, de.grogra.reflect.Type.INT, null, 0));
        $TYPE.addManagedField(cx$FIELD = new _Field("cx", 0 | _Field.SCO, de.grogra.reflect.Type.FLOAT, null, 1));
        $TYPE.addManagedField(cy$FIELD = new _Field("cy", 0 | _Field.SCO, de.grogra.reflect.Type.FLOAT, null, 2));
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new Julia();
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int value) {
        this.iterations = (int) value;
    }

    public float getCx() {
        return cx;
    }

    public void setCx(float value) {
        this.cx = (float) value;
    }

    public float getCy() {
        return cy;
    }

    public void setCy(float value) {
        this.cy = (float) value;
    }

    @Override
    protected float getFloatValue(float x, float y, float z) {
        x = ((x > 0) ? x - (int) x : x + (1 + (int) -x)) * 4 - 2;
        y = ((y > 0) ? y - (int) y : y + (1 + (int) -y)) * 4 - 2;
        float a = x;
        float b = y;
        float a2 = a * a;
        float b2 = b * b;
        float dist2;
        int i;
        for (i = 0; i < iterations; i++) {
            b = 2.0f * a * b + cy;
            a = a2 - b2 + cx;
            a2 = a * a;
            b2 = b * b;
            dist2 = a2 + b2;
            if (dist2 > 4.0f) {
                break;
            }
        }
        return (float) (2 * i - iterations) / (float) iterations;
    }
}
