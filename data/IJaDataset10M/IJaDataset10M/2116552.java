package de.grogra.blocks;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple2f;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3d;
import de.grogra.graph.Instantiator;
import de.grogra.graph.impl.Edge;
import de.grogra.graph.impl.Node;
import de.grogra.imp.View;
import de.grogra.imp3d.objects.GlobalTransformation;
import de.grogra.imp3d.objects.Null;
import de.grogra.imp3d.objects.Sphere;
import de.grogra.turtle.RL;
import de.grogra.turtle.Rotate;
import de.grogra.turtle.Scale;
import de.grogra.math.BSpline;
import de.grogra.math.BSplineCurve;
import de.grogra.math.Circle;
import de.grogra.math.Cos;
import de.grogra.math.Ellipse;
import de.grogra.math.Id;
import de.grogra.math.SplineFunction;
import de.grogra.persistence.PersistenceField;
import de.grogra.persistence.Transaction;
import de.grogra.rgg.Library;
import de.grogra.rgg.model.Instantiation;
import de.grogra.vecmath.Matrix34d;
import de.grogra.xl.lang.FloatToFloat;

public class Hydra extends Sphere implements de.grogra.xl.modules.Instantiator<Instantiation> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    CustomFunction number = new CustomFunction(10, 1, 100);

    BSplineCurve trajectory = new Circle(3);

    CustomFunction twist2 = new CustomFunction(0, -Math.PI, Math.PI);

    CustomFunction twist1 = new CustomFunction(0, -Math.PI, Math.PI);

    FloatToFloat twistMode = new Id();

    CustomFunction spin2 = new CustomFunction(0, -Math.PI, Math.PI);

    CustomFunction spin1 = new CustomFunction(0, -Math.PI, Math.PI);

    FloatToFloat spinMode = new Id();

    CustomFunction scale2 = new CustomFunction(0, -Math.PI / 2.0, Math.PI / 2.0);

    CustomFunction scale1 = new CustomFunction(0, -Math.PI / 2.0, Math.PI / 2.0);

    FloatToFloat scaleMode = new Cos();

    FloatToFloat slopeFunction = (FloatToFloat) BlockTools.getObject("/objects/math/functions/HydraFunctions/slopeHydra", new Id());

    HydraLOD lod = new HydraLOD();

    LocationParameterBase locationParameter = new LocationParameterBase();

    boolean initAll = false;

    private Point2f ids = new Point2f(0, 0);

    ;

    private Float densityValue = new Float(0);

    private Tuple2f height = new Point2f(0, 0);

    private Tuple3f nutrientsValues = new Point3f(0, 0, 0);

    private int childId = 0;

    private static final RL RL90 = new RL(90);

    private static final RL RL_90 = new RL(-90);

    public Hydra() {
        super(BlockTools.RADIUS_SPHERE);
        super.setLayer(1);
        initFunctionsToHermit();
    }

    public Hydra(float n) {
        super(BlockTools.RADIUS_SPHERE);
        super.setLayer(1);
        number.setFunction(n);
        initFunctionsToHermit();
    }

    public Hydra(boolean type) {
        super(BlockTools.RADIUS_SPHERE);
        super.setLayer(1);
        if (!type) {
            slopeFunction = (FloatToFloat) BlockTools.getObject("/objects/math/functions/HydraFunctions/slopeWreath", new Id());
        }
        initFunctionsToHermit();
    }

    public Hydra(float n, boolean type) {
        super(BlockTools.RADIUS_SPHERE);
        super.setLayer(1);
        number.setFunction(n);
        if (!type) {
            slopeFunction = (FloatToFloat) BlockTools.getObject("/objects/math/functions/HydraFunctions/slopeWreath", new Id());
        }
        initFunctionsToHermit();
    }

    private void initAttributes() {
        number = new CustomFunction(10, 1, 100);
        trajectory = new Circle(3);
        twist2 = new CustomFunction(0, -Math.PI, Math.PI);
        twist1 = new CustomFunction(0, -Math.PI, Math.PI);
        twistMode = new Id();
        spin1 = new CustomFunction(0, -Math.PI, Math.PI);
        spin2 = new CustomFunction(0, -Math.PI, Math.PI);
        spinMode = new Id();
        scale2 = new CustomFunction(0, -Math.PI / 2.0, Math.PI / 2.0);
        scale1 = new CustomFunction(0, -Math.PI / 2.0, Math.PI / 2.0);
        scaleMode = new Cos();
        slopeFunction = (FloatToFloat) BlockTools.getObject("/objects/math/functions/HydraFunctions/slopeHydra", new Id());
        lod = new HydraLOD();
        locationParameter = new LocationParameterBase();
        initFunctionsToHermit();
    }

    private void initFunctionsToHermit() {
        ((SplineFunction) slopeFunction).setType(SplineFunction.HERMITE);
    }

    public Instantiator getInstantiator() {
        return de.grogra.rgg.model.Instantiation.INSTANTIATOR;
    }

    public void instantiate(Instantiation state) {
        Instantiation inst = (Instantiation) state;
        Library.setSeed(hashCode());
        ids = (Point2f) inst.getGraphState().getObjectDefault(this, true, Attributes.ID, null);
        densityValue = (Float) inst.getGraphState().getObjectDefault(this, true, Attributes.DENSITY, 1f);
        Matrix34d m = GlobalTransformation.get(this, true, inst.getGraphState(), false);
        Vector3d v = new Vector3d();
        m.get(v);
        height = new Point2f((float) v.z, (Float) inst.getGraphState().getObjectDefault(this, true, Attributes.HEIGHT, 1f));
        lod.set(View.get(inst.getGraphState()), v, 1);
        nutrientsValues = (Tuple3f) inst.getGraphState().getObjectDefault(this, true, Attributes.LOCATIONPARAMETER, null);
        Tuple3f nutrientsSetValues = locationParameter.setLocationParameter(nutrientsValues);
        final int n2 = lod.numberToLod(number.evaluateFloat(ids, nutrientsValues, height, densityValue));
        float f_twist1 = 2 * twist1.evaluateFloat(ids, nutrientsValues, height, densityValue) / n2;
        float f_twist2 = 2 * twist2.evaluateFloat(ids, nutrientsValues, height, densityValue) / n2;
        float f_spin1 = spin1.evaluateFloat(ids, nutrientsValues, height, densityValue) / n2;
        float f_spin2 = spin2.evaluateFloat(ids, nutrientsValues, height, densityValue) / n2;
        float f_scale1 = scale1.evaluateFloat(ids, nutrientsValues, height, densityValue) / n2;
        float f_scale2 = scale2.evaluateFloat(ids, nutrientsValues, height, densityValue) / n2;
        float spin = 0;
        float[] distrbution = new float[n2];
        for (int i = 0; i < n2; i++) {
            distrbution[i] = (float) ((float) i / (float) n2);
        }
        float[] trajPoint1 = new float[4];
        Vector3d vv2 = new Vector3d(0, 0, 0);
        for (int i = 0; i < n2; i++) {
            childId = i;
            inst.getGraphState().setInstanceAttribute(Attributes.NUTRIENTS_TUPLE3F, nutrientsSetValues);
            inst.getGraphState().setInstanceAttribute(Attributes.NUMBER_INT, new Integer(n2));
            int idx = trajectory.getDimension(inst.getGraphState()) - 1;
            BSpline.evaluate(trajPoint1, trajectory, distrbution[i], inst.getGraphState());
            if (trajectory.isRational(inst.getGraphState())) {
                for (int k = idx - 1; k >= 0; k--) {
                    trajPoint1[k] /= trajPoint1[idx];
                }
                trajPoint1[idx] = 0;
                vv2 = kartesisch2kugel(trajPoint1);
            } else {
                vv2 = kartesisch2kugel(trajPoint1);
            }
            float twist = ModeList.function(i, n2, f_twist1, f_twist2, twistMode, ids, nutrientsValues, height, densityValue);
            spin = spin + i * n2 * f_spin2 + ModeList.function(i, n2, f_spin1, f_spin2, spinMode, ids, nutrientsValues, height, densityValue);
            float scale = lod.scaleToLod(ModeList.function(i, n2, f_scale1, f_scale2, scaleMode, ids, nutrientsValues, height, densityValue));
            float slope = (float) (slopeFunction.evaluateFloat(distrbution[i]) * 2 * Math.PI - 1.5 * Math.PI);
            inst.producer$push();
            inst.instantiate(new Null(trajPoint1[0], trajPoint1[1], trajPoint1[2]));
            inst.instantiate(RL90);
            inst.instantiate(new Rotate(0, (float) (vv2.y + (slope + 2.5 * Math.PI)) * (float) (180 / Math.PI), slope * (float) (180 / Math.PI)));
            inst.instantiate(RL_90);
            inst.instantiate(new Rotate(0, twist * (float) (180 / Math.PI), spin * (float) (180 / Math.PI)));
            Scale scaleN = new Scale(scale);
            inst.instantiate(scaleN);
            setHeightLocal(inst, scaleN, height.x);
            for (Edge e = getFirstEdge(); e != null; e = e.getNext(this)) {
                Node n = e.getTarget();
                if ((n != this) && (e.testEdgeBits(BlockConst.MULTIPLY) || e.testEdgeBits(BlockConst.CHILD))) {
                    inst.producer$push();
                    inst.getGraphState().setInstanceAttribute(Attributes.ID_TUPLE2F, new Point2f(i, ids.x));
                    inst.instantiate(n);
                    inst.producer$pop(null);
                }
            }
            inst.producer$pop(null);
        }
    }

    private Vector3d kartesisch2kugel(float[] v) {
        double r = Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
        double phi = 0;
        if (v[1] >= 0) {
            phi = Math.acos(v[0] / Math.sqrt(v[0] * v[0] + v[1] * v[1]));
        } else {
            phi = 2 * Math.PI - Math.acos(v[0] / Math.sqrt(v[0] * v[0] + v[1] * v[1]));
        }
        double t = Math.PI / 2.0 - Math.atan(v[2] / Math.sqrt(v[0] * v[0] + v[1] * v[1]));
        return new Vector3d(r, phi, t);
    }

    private void setHeightLocal(Instantiation inst, Node n, float height0) {
        Matrix34d m = GlobalTransformation.get(n, true, inst.getGraphState(), false);
        Vector3d heighti = new Vector3d();
        m.get(heighti);
        height.y = (float) Math.abs(heighti.z - height0);
        inst.getGraphState().setInstanceAttribute(Attributes.HEIGHT_FLOAT, new Float(height.y));
    }

    public void setRadius(float firstRadius, float secondRadius) {
        trajectory = new Ellipse(firstRadius, secondRadius);
    }

    public void setRadius(float value) {
        if (trajectory instanceof Circle) {
            ((Circle) trajectory).setRadius((float) value);
        }
    }

    public float getRadius() {
        if (trajectory instanceof Circle) {
            return ((Circle) trajectory).getRadius();
        }
        return 0;
    }

    public void fieldModified(PersistenceField field, int[] indices, Transaction t) {
        super.fieldModified(field, indices, t);
        if (!Transaction.isApplying(t)) {
            if (field.overlaps(indices, initAll$FIELD, null)) {
                if (isInitAll()) {
                    initAttributes();
                    setInitAll(false);
                }
            }
        }
    }

    public float getNumber() {
        return number.evaluateZerro();
    }

    public void setNumber(double value) {
        number.setFunction(value);
    }

    public float getTwist2() {
        return twist2.evaluateZerro();
    }

    public void setTwist2(double value) {
        twist2.setFunction(value);
    }

    public float getTwist1() {
        return twist1.evaluateZerro();
    }

    public void setTwist1(double value) {
        twist1.setFunction(value);
    }

    public void setTwist(double value1, double value2) {
        twist1.setFunction(value1);
        twist2.setFunction(value2);
    }

    public float getSpin1() {
        return spin1.evaluateZerro();
    }

    public void setSpin1(double value) {
        spin1.setFunction(value);
    }

    public float getSpin2() {
        return spin2.evaluateZerro();
    }

    public void setSpin2(double value) {
        spin2.setFunction(value);
    }

    public void setSpin(double value1, double value2) {
        spin1.setFunction(value1);
        spin2.setFunction(value2);
    }

    public float getScale2() {
        return scale2.evaluateZerro();
    }

    public void setScale2(double value) {
        scale2.setFunction(value);
    }

    public float getScale1() {
        return scale1.evaluateZerro();
    }

    public void setScale1(double value) {
        scale1.setFunction(value);
    }

    public void setScale(double value1, double value2) {
        scale1.setFunction(value1);
        scale2.setFunction(value2);
    }

    public void setNumber(String value) {
        number.setFunction(value);
    }

    public void setTwist2(String value) {
        twist2.setFunction(value);
    }

    public void setTwist1(String value) {
        twist1.setFunction(value);
    }

    public void setTwist(String value1, String value2) {
        twist1.setFunction(value1);
        twist2.setFunction(value2);
    }

    public void setSpin1(String value) {
        spin1.setFunction(value);
    }

    public void setSpin2(String value) {
        spin2.setFunction(value);
    }

    public void setSpin(String value1, String value2) {
        spin1.setFunction(value1);
        spin2.setFunction(value2);
    }

    public void setScale2(String value) {
        scale2.setFunction(value);
    }

    public void setScale1(String value) {
        scale1.setFunction(value);
    }

    public void setScale(String value1, String value2) {
        scale1.setFunction(value1);
        scale2.setFunction(value2);
    }

    public void useLod(boolean value) {
        lod.setUseLOD(value);
    }

    public int getChildId() {
        return childId;
    }

    public int getParentId() {
        return (int) ids.x;
    }

    public int getThisId() {
        return (int) ids.y;
    }

    public float getDensity() {
        return densityValue;
    }

    public float getAbsoluteHeight() {
        return height.x;
    }

    public float getLocalHeight() {
        return height.y;
    }

    public float getN1() {
        return nutrientsValues.x;
    }

    public float getN2() {
        return nutrientsValues.y;
    }

    public float getN3() {
        return nutrientsValues.z;
    }

    public static final NType $TYPE;

    public static final NType.Field number$FIELD;

    public static final NType.Field trajectory$FIELD;

    public static final NType.Field twist2$FIELD;

    public static final NType.Field twist1$FIELD;

    public static final NType.Field twistMode$FIELD;

    public static final NType.Field spin2$FIELD;

    public static final NType.Field spin1$FIELD;

    public static final NType.Field spinMode$FIELD;

    public static final NType.Field scale2$FIELD;

    public static final NType.Field scale1$FIELD;

    public static final NType.Field scaleMode$FIELD;

    public static final NType.Field slopeFunction$FIELD;

    public static final NType.Field lod$FIELD;

    public static final NType.Field locationParameter$FIELD;

    public static final NType.Field initAll$FIELD;

    private static final class _Field extends NType.Field {

        private final int id;

        _Field(String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            super(Hydra.$TYPE, name, modifiers, type, componentType);
            this.id = id;
        }

        @Override
        public void setBoolean(Object o, boolean value) {
            switch(id) {
                case 14:
                    ((Hydra) o).initAll = (boolean) value;
                    return;
            }
            super.setBoolean(o, value);
        }

        @Override
        public boolean getBoolean(Object o) {
            switch(id) {
                case 14:
                    return ((Hydra) o).isInitAll();
            }
            return super.getBoolean(o);
        }

        @Override
        protected void setObjectImpl(Object o, Object value) {
            switch(id) {
                case 0:
                    ((Hydra) o).number = (CustomFunction) value;
                    return;
                case 1:
                    ((Hydra) o).trajectory = (BSplineCurve) value;
                    return;
                case 2:
                    ((Hydra) o).twist2 = (CustomFunction) value;
                    return;
                case 3:
                    ((Hydra) o).twist1 = (CustomFunction) value;
                    return;
                case 4:
                    ((Hydra) o).twistMode = (FloatToFloat) value;
                    return;
                case 5:
                    ((Hydra) o).spin2 = (CustomFunction) value;
                    return;
                case 6:
                    ((Hydra) o).spin1 = (CustomFunction) value;
                    return;
                case 7:
                    ((Hydra) o).spinMode = (FloatToFloat) value;
                    return;
                case 8:
                    ((Hydra) o).scale2 = (CustomFunction) value;
                    return;
                case 9:
                    ((Hydra) o).scale1 = (CustomFunction) value;
                    return;
                case 10:
                    ((Hydra) o).scaleMode = (FloatToFloat) value;
                    return;
                case 11:
                    ((Hydra) o).slopeFunction = (FloatToFloat) value;
                    return;
                case 12:
                    ((Hydra) o).lod = (HydraLOD) value;
                    return;
                case 13:
                    ((Hydra) o).locationParameter = (LocationParameterBase) value;
                    return;
            }
            super.setObjectImpl(o, value);
        }

        @Override
        public Object getObject(Object o) {
            switch(id) {
                case 0:
                    return ((Hydra) o).number;
                case 1:
                    return ((Hydra) o).getTrajectory();
                case 2:
                    return ((Hydra) o).twist2;
                case 3:
                    return ((Hydra) o).twist1;
                case 4:
                    return ((Hydra) o).getTwistMode();
                case 5:
                    return ((Hydra) o).spin2;
                case 6:
                    return ((Hydra) o).spin1;
                case 7:
                    return ((Hydra) o).getSpinMode();
                case 8:
                    return ((Hydra) o).scale2;
                case 9:
                    return ((Hydra) o).scale1;
                case 10:
                    return ((Hydra) o).getScaleMode();
                case 11:
                    return ((Hydra) o).getSlopeFunction();
                case 12:
                    return ((Hydra) o).getLod();
                case 13:
                    return ((Hydra) o).getLocationParameter();
            }
            return super.getObject(o);
        }
    }

    static {
        $TYPE = new NType(new Hydra());
        $TYPE.addManagedField(number$FIELD = new _Field("number", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(CustomFunction.class), null, 0));
        $TYPE.addManagedField(trajectory$FIELD = new _Field("trajectory", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(BSplineCurve.class), null, 1));
        $TYPE.addManagedField(twist2$FIELD = new _Field("twist2", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(CustomFunction.class), null, 2));
        $TYPE.addManagedField(twist1$FIELD = new _Field("twist1", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(CustomFunction.class), null, 3));
        $TYPE.addManagedField(twistMode$FIELD = new _Field("twistMode", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(FloatToFloat.class), null, 4));
        $TYPE.addManagedField(spin2$FIELD = new _Field("spin2", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(CustomFunction.class), null, 5));
        $TYPE.addManagedField(spin1$FIELD = new _Field("spin1", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(CustomFunction.class), null, 6));
        $TYPE.addManagedField(spinMode$FIELD = new _Field("spinMode", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(FloatToFloat.class), null, 7));
        $TYPE.addManagedField(scale2$FIELD = new _Field("scale2", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(CustomFunction.class), null, 8));
        $TYPE.addManagedField(scale1$FIELD = new _Field("scale1", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(CustomFunction.class), null, 9));
        $TYPE.addManagedField(scaleMode$FIELD = new _Field("scaleMode", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(FloatToFloat.class), null, 10));
        $TYPE.addManagedField(slopeFunction$FIELD = new _Field("slopeFunction", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(FloatToFloat.class), null, 11));
        $TYPE.addManagedField(lod$FIELD = new _Field("lod", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(HydraLOD.class), null, 12));
        $TYPE.addManagedField(locationParameter$FIELD = new _Field("locationParameter", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(LocationParameterBase.class), null, 13));
        $TYPE.addManagedField(initAll$FIELD = new _Field("initAll", 0 | _Field.SCO, de.grogra.reflect.Type.BOOLEAN, null, 14));
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new Hydra();
    }

    public boolean isInitAll() {
        return initAll;
    }

    public void setInitAll(boolean value) {
        this.initAll = (boolean) value;
    }

    public void setNumber(CustomFunction value) {
        number$FIELD.setObject(this, value);
    }

    public BSplineCurve getTrajectory() {
        return trajectory;
    }

    public void setTrajectory(BSplineCurve value) {
        trajectory$FIELD.setObject(this, value);
    }

    public void setTwist2(CustomFunction value) {
        twist2$FIELD.setObject(this, value);
    }

    public void setTwist1(CustomFunction value) {
        twist1$FIELD.setObject(this, value);
    }

    public FloatToFloat getTwistMode() {
        return twistMode;
    }

    public void setTwistMode(FloatToFloat value) {
        twistMode$FIELD.setObject(this, value);
    }

    public void setSpin2(CustomFunction value) {
        spin2$FIELD.setObject(this, value);
    }

    public void setSpin1(CustomFunction value) {
        spin1$FIELD.setObject(this, value);
    }

    public FloatToFloat getSpinMode() {
        return spinMode;
    }

    public void setSpinMode(FloatToFloat value) {
        spinMode$FIELD.setObject(this, value);
    }

    public void setScale2(CustomFunction value) {
        scale2$FIELD.setObject(this, value);
    }

    public void setScale1(CustomFunction value) {
        scale1$FIELD.setObject(this, value);
    }

    public FloatToFloat getScaleMode() {
        return scaleMode;
    }

    public void setScaleMode(FloatToFloat value) {
        scaleMode$FIELD.setObject(this, value);
    }

    public FloatToFloat getSlopeFunction() {
        return slopeFunction;
    }

    public void setSlopeFunction(FloatToFloat value) {
        slopeFunction$FIELD.setObject(this, value);
    }

    public HydraLOD getLod() {
        return lod;
    }

    public void setLod(HydraLOD value) {
        lod$FIELD.setObject(this, value);
    }

    public LocationParameterBase getLocationParameter() {
        return locationParameter;
    }

    public void setLocationParameter(LocationParameterBase value) {
        locationParameter$FIELD.setObject(this, value);
    }
}
