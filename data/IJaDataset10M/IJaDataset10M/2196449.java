package org.node.perf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Random;
import org.node.perf.util.FileUtils;
import org.node.perf.util.StringUtils;

/**
 * Typically a large object is an object greater than
 * 64kb, 1MB, 64MB in size.
 *
 */
public final class TinyObject implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Additional Information:
     * 
     * java.lang.Object shell size in bytes:
     ----------------------------------------
        public static final int OBJECT_SHELL_SIZE   = 8;
        public static final int OBJREF_SIZE         = 4;
        public static final int LONG_FIELD_SIZE     = 8;
        public static final int INT_FIELD_SIZE      = 4;
        public static final int SHORT_FIELD_SIZE    = 2;
        public static final int CHAR_FIELD_SIZE     = 2;
        public static final int BYTE_FIELD_SIZE     = 1;
        public static final int BOOLEAN_FIELD_SIZE  = 1;
        public static final int DOUBLE_FIELD_SIZE   = 8;
        public static final int FLOAT_FIELD_SIZE    = 4;
    ----------------------------------------            
     * 
     */
    private static final Random random = new Random(System.currentTimeMillis());

    public static final int kb = 1024;

    private Object obj1 = StringUtils.randomString(random, (4 * kb));

    private BigDecimal[] d1 = createBigDecimal(100);

    /**
     * @return the obj1
     */
    public final Object getObj1() {
        return obj1;
    }

    /**
     * @param obj1 the obj1 to set
     */
    public final void setObj1(Object obj1) {
        this.obj1 = obj1;
    }

    /**
     * @return the d1
     */
    public final BigDecimal[] getD1() {
        return d1;
    }

    /**
     * @param d1 the d1 to set
     */
    public final void setD1(BigDecimal[] d1) {
        this.d1 = d1;
    }

    public TinyObject() {
    }

    public static final BigDecimal[] createBigDecimal(final int size) {
        final BigDecimal[] arr = new BigDecimal[size];
        for (int i = 0; i < size; i++) {
            arr[i] = new BigDecimal(random.nextDouble());
        }
        return arr;
    }

    public static final void main(final String[] args) {
        final TinyObject obj = new TinyObject();
        final int size = FileUtils.getObjectSizeAppr(obj);
        System.out.println("Approximate Size : " + size);
    }

    public String toString() {
        return StringUtils.concat(super.toString(), "Appr Size: ", "" + FileUtils.getObjectSizeAppr(this));
    }
}
