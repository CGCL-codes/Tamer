package gumbo.tech.math.util;

import gumbo.core.life.ObjectPool;
import gumbo.core.life.impl.ObjectPoolImpl;
import gumbo.core.util.Maker;
import gumbo.core.util.NullObject;
import gumbo.tech.math.Size3;
import gumbo.tech.math.Tuple3;
import gumbo.tech.math.impl.Size3Impl;

/**
 * Constants, functions, and wrappers for Size3.
 * @author jonb
 */
public class Size3Util {

    private Size3Util() {
    }

    /** Size with zero for all dimension values. Immutable. */
    public static final Size3.Constant ZERO = Size3Util.constant(0.0, 0.0, 0.0);

    /** Size with positive infinity for all dimension values. Immutable. */
    public static final Size3.Constant MAX = Size3Util.constant(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

    /** Size with zero for all dimension values. Immutable. */
    public static final Size3.Constant MIN = ZERO;

    /** Size with NaN for all dimension values. Immutable. */
    public static final Size3.Constant NAN = Size3Util.constant(Double.NaN, Double.NaN, Double.NaN);

    public static Maker.FromNone<Size3.Exposed> getExposedMaker() {
        return EXPOSED_MAKER;
    }

    private static final Maker.FromNone<Size3.Exposed> EXPOSED_MAKER = new Maker.FromNone<Size3.Exposed>() {

        @Override
        public Size3.Exposed make() {
            return new Size3Impl.Exposed();
        }
    };

    public static ObjectPool<Size3.Exposed> getPool() {
        return OBJECT_POOL;
    }

    private static final ObjectPool<Size3.Exposed> OBJECT_POOL = new ObjectPoolImpl<Size3.Exposed>(getExposedMaker());

    public static Size3.Constant constant(double valX, double valY, double valZ) {
        return new Size3Impl.Constant(valX, valY, valZ);
    }

    public static Size3.Constant constant(double... val) {
        return new Size3Impl.Constant(val);
    }

    public static Size3.Constant constant(Tuple3 val) {
        return new Size3Impl.Constant(val);
    }

    public static Size3.Exposed exposed() {
        return new Size3Impl.Exposed();
    }

    public static Size3.Exposed exposed(double valX, double valY, double valZ) {
        return new Size3Impl.Exposed(valX, valY, valZ);
    }

    public static Size3.Exposed exposed(double... val) {
        return new Size3Impl.Exposed(val);
    }

    public static Size3.Exposed exposed(Tuple3 val) {
        return new Size3Impl.Exposed(val);
    }

    public static Size3Util.Null getNull() {
        if (NULL == null) {
            NULL = new Size3Util.Null();
        }
        return NULL;
    }

    private static Size3Util.Null NULL = null;

    public static Size3Util.ExposedNull getExposedNull() {
        if (EXPOSED_NULL == null) {
            EXPOSED_NULL = new Size3Util.ExposedNull();
        }
        return EXPOSED_NULL;
    }

    private static Size3Util.ExposedNull EXPOSED_NULL = null;

    public static class Null extends Size3Impl.Constant implements NullObject {

        /**
		 * For extension only.
		 */
        protected Null() {
            super();
        }

        private Object readResolve() {
            return getNull();
        }

        private static final long serialVersionUID = 1L;
    }

    protected static class ExposedNull extends Size3Impl.Exposed implements NullObject {

        /**
		 * For extension only.
		 */
        protected ExposedNull() {
            super(new Size3Impl.Helper() {

                @Override
                protected void implSuperUpdate(double valX, double valY, double valZ) {
                }

                private static final long serialVersionUID = 1L;
            });
        }

        private Object readResolve() {
            return EXPOSED_NULL;
        }

        private static final long serialVersionUID = 1L;
    }
}
