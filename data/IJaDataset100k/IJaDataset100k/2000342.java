package homura.hde.scenegraph.animation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides the ability to compose an instance of a certain data type
 * (e.g. {@code Point2D}) from an array of double values, and vice versa.
 * This class is used by {@link KeyFrames} to accept and produce data of
 * any type, provided that a {@code Composer} is registered to handle that
 * data type.
 * <p>
 * <b>Most developers need not deal directly with or implement this class.</b>
 * Only applications that animate custom data types need to make use of this
 * class (see below for tips on implementing a custom {@code Composer}).
 * <p>
 * This class has built-in support for the following data types:
 * <code>
 * <ul>
 * <li> java.lang.Byte
 * <li> java.lang.Short
 * <li> java.lang.Integer
 * <li> java.lang.Long
 * <li> java.lang.Float
 * <li> java.lang.Double
 * <li> java.lang.Boolean
 * <li> java.awt.Color
 * <li> java.awt.geom.Point2D
 * <li> java.awt.geom.Line2D
 * <li> java.awt.geom.Dimension2D
 * <li> java.awt.geom.Rectangle2D
 * <li> java.awt.geom.RoundRectangle2D
 * <li> java.awt.geom.Ellipse2D
 * <li> java.awt.geom.Arc2D
 * <li> java.awt.geom.QuadCurve2D
 * <li> java.awt.geom.CubicCurve2D
 * </ul>
 * </code>
 * <p>
 * Applications that animate data types not listed above can register a
 * custom {@code Composer} instance, which will henceforth be chosen
 * automatically when an instance of that type is used by {@code KeyFrames}.
 * <p>
 * The following example demonstrates how to implement a custom {@code Composer}
 * for a hypothetical three-dimensional data type called {@code Vec3f}:
 * <pre>
 *     public class Vec3f {
 *         private float x, y, z;
 *         // getters and setters omitted...
 *     }
 *
 *     public class ComposerVec3f extends Composer<Vec3f> {
 *         public ComposerVec3f() {
 *             super(3);
 *         }
 *         public double[] decompose(Vec3f o, double[] v) {
 *             v[0] = o.getX();
 *             v[1] = o.getY();
 *             v[2] = o.getZ();
 *             return v;
 *         }
 *         public Vec3f compose(double[] v) {
 *             return new Vec3f(v[0], v[1], v[2]);
 *         }
 *     }
 *
 *     // the Composer instance can be registered at startup, for example...
 *     static {
 *         Composer.register(new ComposerVec3f());
 *     }
 * </pre>
 *
 * @author Chris Campbell
 * @author Chet Haase
 */
public abstract class Composer<T> {

    /**
     * HashMap that holds all registered composers.
     */
    private static final Map<Class<?>, Composer> impls = new HashMap<Class<?>, Composer>();

    /**
     * Static registration of pre-defined composers.
     */
    static {
        register(Byte.class, ComposerByte.class);
        register(Short.class, ComposerShort.class);
        register(Integer.class, ComposerInteger.class);
        register(Long.class, ComposerLong.class);
        register(Float.class, ComposerFloat.class);
        register(Double.class, ComposerDouble.class);
        register(Boolean.class, ComposerBoolean.class);
        register(Color.class, ComposerColor.class);
        register(Point2D.class, ComposerPoint2D.class);
        register(Line2D.class, ComposerLine2D.class);
        register(Dimension2D.class, ComposerDimension2D.class);
        register(Rectangle2D.class, ComposerRectangle2D.class);
        register(RoundRectangle2D.class, ComposerRoundRectangle2D.class);
        register(Ellipse2D.class, ComposerEllipse2D.class);
        register(Arc2D.class, ComposerArc2D.class);
        register(QuadCurve2D.class, ComposerQuadCurve2D.class);
        register(CubicCurve2D.class, ComposerCubicCurve2D.class);
    }

    /**
     * Registers a particular {@code Composer} implementation for a given
     * data type, which will be automatically discovered and used whenever
     * an object of that type is used by {@code KeyFrames} and related
     * classes.
     *
     * @param type the class of objects on which the given {@code Composer}
     *     implementation operates
     * @param implClass the class of the {@code Composer} implementation to be
     *     registered
     */
    public static void register(Class<?> type, Class<? extends Composer> implClass) {
        Composer impl;
        try {
            Constructor<? extends Composer> ctor = implClass.getConstructor();
            impl = (Composer) ctor.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Problem constructing " + "appropriate Composer for type " + type + ":", e);
        }
        impls.put(type, impl);
    }

    /**
     * Returns a {@code Composer} instance that operates on objects of
     * the given {@code Class}.
     * 
     * @param type the data type for which to getInstance a new {@code Composer}
     *     instance
     * @return a new {@code Composer} instance
     * @see #register
     * @throws IllegalArgumentException if no {@code Composer} can be found
     *     in the registry for the given data type, or if a problem is
     *     encountered when constructing the new {@code Composer} instance
     */
    public static <T> Composer<T> getInstance(Class<?> type) {
        Class<? extends Composer> compClass = null;
        for (Class<?> klass : impls.keySet()) {
            if (klass.isAssignableFrom(type)) {
                return impls.get(klass);
            }
        }
        throw new IllegalArgumentException("No Composer" + " can be found for type " + type + "; consider using" + " different types for your values or supplying a custom" + " Composer");
    }

    private final int numVals;

    /**
     * Protected constructor.
     *
     * @param numVals the number of values used by this {@code Composer}
     */
    protected Composer(int numVals) {
        this.numVals = numVals;
    }

    /**
     * Returns the number of values in the target object that this
     * {@code Composer} evaluates.
     *
     * @return the number of values used by this {@code Composer}
     */
    public int getNumVals() {
        return numVals;
    }

    /**
     * Decomposes the given object {@code o} into its constituent
     * fields and stores them in the provided array {@code v}.  The
     * same array is also returned for convenience.
     *
     * @param o the object to be decomposed
     * @param v the array that will contain the decomposed values
     * @return the same array passed as {@code v}
     * @throws ArrayIndexOutOfBoundsException if {@code v.length} is
     *     less than the value returned by {@code getNumVals()}
     * @throws NullPointerException if {@code o} or {@code v}
     *     is null
     */
    public abstract double[] decompose(T o, double[] v);

    /**
     * Composes a new object from the values in the provided array
     * {@code v}, and returns that resulting object.
     *
     * @param v the array that contains the values to be composed into
     *    a new object of type {@code T}
     * @return an object composed from the values provided in {@code v}
     * @throws ArrayIndexOutOfBoundsException if {@code v.length} is
     *     less than the value returned by {@code getNumVals()}
     * @throws NullPointerException if {@code v} is null
     */
    public abstract T compose(double[] v);
}

class ComposerByte extends Composer<Byte> {

    public ComposerByte() {
        super(1);
    }

    public double[] decompose(Byte o, double[] v) {
        v[0] = o;
        return v;
    }

    public Byte compose(double[] v) {
        return (byte) v[0];
    }
}

class ComposerShort extends Composer<Short> {

    public ComposerShort() {
        super(1);
    }

    public double[] decompose(Short o, double[] v) {
        v[0] = o;
        return v;
    }

    public Short compose(double[] v) {
        return (short) v[0];
    }
}

class ComposerInteger extends Composer<Integer> {

    public ComposerInteger() {
        super(1);
    }

    public double[] decompose(Integer o, double[] v) {
        v[0] = o;
        return v;
    }

    public Integer compose(double[] v) {
        return (int) v[0];
    }
}

class ComposerLong extends Composer<Long> {

    public ComposerLong() {
        super(1);
    }

    public double[] decompose(Long o, double[] v) {
        v[0] = o;
        return v;
    }

    public Long compose(double[] v) {
        return (long) v[0];
    }
}

class ComposerFloat extends Composer<Float> {

    public ComposerFloat() {
        super(1);
    }

    public double[] decompose(Float o, double[] v) {
        v[0] = o;
        return v;
    }

    public Float compose(double[] v) {
        return (float) v[0];
    }
}

class ComposerDouble extends Composer<Double> {

    public ComposerDouble() {
        super(1);
    }

    public double[] decompose(Double o, double[] v) {
        v[0] = o;
        return v;
    }

    public Double compose(double[] v) {
        return v[0];
    }
}

class ComposerBoolean extends Composer<Boolean> {

    public ComposerBoolean() {
        super(1);
    }

    public double[] decompose(Boolean o, double[] v) {
        v[0] = o ? 1.0 : 0.0;
        return v;
    }

    public Boolean compose(double[] v) {
        return (v[0] == 1.0 ? true : false);
    }
}

class ComposerColor extends Composer<Color> {

    private float[] comps = new float[4];

    public ComposerColor() {
        super(4);
    }

    public double[] decompose(Color o, double[] v) {
        comps = o.getComponents(comps);
        for (int i = 0; i < 4; i++) {
            v[i] = comps[i];
        }
        return v;
    }

    public Color compose(double[] v) {
        return new Color((float) v[0], (float) v[1], (float) v[2], (float) v[3]);
    }
}

class ComposerPoint2D extends Composer<Point2D> {

    public ComposerPoint2D() {
        super(2);
    }

    public double[] decompose(Point2D o, double[] v) {
        v[0] = o.getX();
        v[1] = o.getY();
        return v;
    }

    public Point2D compose(double[] v) {
        return new Point2D.Float((float) v[0], (float) v[1]);
    }
}

class ComposerLine2D extends Composer<Line2D> {

    public ComposerLine2D() {
        super(4);
    }

    public double[] decompose(Line2D o, double[] v) {
        v[0] = o.getX1();
        v[1] = o.getY1();
        v[2] = o.getX2();
        v[3] = o.getY2();
        return v;
    }

    public Line2D compose(double[] v) {
        return new Line2D.Float((float) v[0], (float) v[1], (float) v[2], (float) v[3]);
    }
}

class ComposerDimension2D extends Composer<Dimension2D> {

    public ComposerDimension2D() {
        super(2);
    }

    public double[] decompose(Dimension2D o, double[] v) {
        v[0] = o.getWidth();
        v[1] = o.getHeight();
        return v;
    }

    public Dimension2D compose(double[] v) {
        return new Dimension((int) v[0], (int) v[1]);
    }
}

class ComposerRectangle2D extends Composer<Rectangle2D> {

    public ComposerRectangle2D() {
        super(4);
    }

    public double[] decompose(Rectangle2D o, double[] v) {
        v[0] = o.getX();
        v[1] = o.getY();
        v[2] = o.getWidth();
        v[3] = o.getHeight();
        return v;
    }

    public Rectangle2D compose(double[] v) {
        return new Rectangle2D.Float((float) v[0], (float) v[1], (float) v[2], (float) v[3]);
    }
}

class ComposerRoundRectangle2D extends Composer<RoundRectangle2D> {

    public ComposerRoundRectangle2D() {
        super(6);
    }

    public double[] decompose(RoundRectangle2D o, double[] v) {
        v[0] = o.getX();
        v[1] = o.getY();
        v[2] = o.getWidth();
        v[3] = o.getHeight();
        v[4] = o.getArcWidth();
        v[5] = o.getArcHeight();
        return v;
    }

    public RoundRectangle2D compose(double[] v) {
        return new RoundRectangle2D.Float((float) v[0], (float) v[1], (float) v[2], (float) v[3], (float) v[4], (float) v[5]);
    }
}

class ComposerEllipse2D extends Composer<Ellipse2D> {

    public ComposerEllipse2D() {
        super(4);
    }

    public double[] decompose(Ellipse2D o, double[] v) {
        v[0] = o.getX();
        v[1] = o.getY();
        v[2] = o.getWidth();
        v[3] = o.getHeight();
        return v;
    }

    public Ellipse2D compose(double[] v) {
        return new Ellipse2D.Float((float) v[0], (float) v[1], (float) v[2], (float) v[3]);
    }
}

class ComposerArc2D extends Composer<Arc2D> {

    public ComposerArc2D() {
        super(6);
    }

    public double[] decompose(Arc2D o, double[] v) {
        v[0] = o.getX();
        v[1] = o.getY();
        v[2] = o.getWidth();
        v[3] = o.getHeight();
        v[4] = o.getAngleStart();
        v[5] = o.getAngleExtent();
        return v;
    }

    public Arc2D compose(double[] v) {
        return new Arc2D.Float((float) v[0], (float) v[1], (float) v[2], (float) v[3], (float) v[4], (float) v[5], Arc2D.OPEN);
    }
}

class ComposerQuadCurve2D extends Composer<QuadCurve2D> {

    public ComposerQuadCurve2D() {
        super(6);
    }

    public double[] decompose(QuadCurve2D o, double[] v) {
        v[0] = o.getX1();
        v[1] = o.getY1();
        v[2] = o.getCtrlX();
        v[3] = o.getCtrlY();
        v[4] = o.getX2();
        v[5] = o.getY2();
        return v;
    }

    public QuadCurve2D compose(double[] v) {
        return new QuadCurve2D.Float((float) v[0], (float) v[1], (float) v[2], (float) v[3], (float) v[4], (float) v[5]);
    }
}

class ComposerCubicCurve2D extends Composer<CubicCurve2D> {

    public ComposerCubicCurve2D() {
        super(8);
    }

    public double[] decompose(CubicCurve2D o, double[] v) {
        v[0] = o.getX1();
        v[1] = o.getY1();
        v[2] = o.getCtrlX1();
        v[3] = o.getCtrlY1();
        v[4] = o.getCtrlX2();
        v[5] = o.getCtrlY2();
        v[6] = o.getX2();
        v[7] = o.getY2();
        return v;
    }

    public CubicCurve2D compose(double[] v) {
        return new CubicCurve2D.Float((float) v[0], (float) v[1], (float) v[2], (float) v[3], (float) v[4], (float) v[5], (float) v[6], (float) v[7]);
    }
}
