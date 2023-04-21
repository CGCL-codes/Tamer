package gumbo.tech.math.util;

import gumbo.core.life.ObjectPool;
import gumbo.core.life.impl.ObjectPoolImpl;
import gumbo.core.util.Maker;
import gumbo.core.util.NullObject;
import gumbo.tech.math.Point2;
import gumbo.tech.math.Tuple2;
import gumbo.tech.math.impl.Point2Impl;

/**
 * Constants, functions, and wrappers for Point2.
 * @author jonb
 */
public class Point2Util {

    private Point2Util() {
    }

    public static Maker.FromNone<Point2.Exposed> getExposedMaker() {
        return EXPOSED_MAKER;
    }

    private static final Maker.FromNone<Point2.Exposed> EXPOSED_MAKER = new Maker.FromNone<Point2.Exposed>() {

        @Override
        public Point2.Exposed make() {
            return new Point2Impl.Exposed();
        }
    };

    public static ObjectPool<Point2.Exposed> getPool() {
        return OBJECT_POOL;
    }

    private static final ObjectPool<Point2.Exposed> OBJECT_POOL = new ObjectPoolImpl<Point2.Exposed>(getExposedMaker());

    public static Point2.Constant constant(double valX, double valY) {
        return new Point2Impl.Constant(valX, valY);
    }

    public static Point2.Constant constant(double... val) {
        return new Point2Impl.Constant(val);
    }

    public static Point2.Constant constant(Tuple2 val) {
        return new Point2Impl.Constant(val);
    }

    public static Point2.Exposed exposed() {
        return new Point2Impl.Exposed();
    }

    public static Point2.Exposed exposed(double valX, double valY) {
        return new Point2Impl.Exposed(valX, valY);
    }

    public static Point2.Exposed exposed(double... val) {
        return new Point2Impl.Exposed(val);
    }

    public static Point2.Exposed exposed(Tuple2 val) {
        return new Point2Impl.Exposed(val);
    }

    public static Point2Util.Null getNull() {
        if (NULL == null) {
            NULL = new Point2Util.Null();
        }
        return NULL;
    }

    private static Point2Util.Null NULL = null;

    public static Point2Util.ExposedNull getExposedNull() {
        if (EXPOSED_NULL == null) {
            EXPOSED_NULL = new Point2Util.ExposedNull();
        }
        return EXPOSED_NULL;
    }

    private static Point2Util.ExposedNull EXPOSED_NULL = null;

    public static class Null extends Point2Impl.Constant implements NullObject {

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

    protected static class ExposedNull extends Point2Impl.Exposed implements NullObject {

        /**
		 * For extension only.
		 */
        protected ExposedNull() {
            super(new Point2Impl.Helper() {

                @Override
                protected void implSuperUpdate(double valX, double valY) {
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
