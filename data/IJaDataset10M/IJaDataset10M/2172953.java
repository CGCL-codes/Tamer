package corner.orm.hibernate.v3;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;
import corner.util.StringUtils;
import corner.util.VectorUtils;

/**
 * 矩阵的行记录.
 * @author <a href="mailto:jun.tsai@bjmaxinfo.com">Jun Tsai</a>
 * @version $Revision: 4285 $
 * @since 2.2.2
 */
public class MatrixRow<T> extends Vector<T> {

    /**
	 * 取得字符型的返回值时候的defaultValue
	 */
    private static final String MATRIX_STRING_ZERO_STR = "0";

    /**
	 * 
	 */
    private static final long serialVersionUID = -4481128241802168063L;

    /**
	 * 取得MatrixRow中,数字的和.
	 * TODO 目前通过捕获异常的形式进行判断是否是Double类型.期待更好的方法.
	 * @return
	 */
    public double getRowSum() {
        Iterator it = this.iterator();
        Object value = null;
        while (it.hasNext()) {
            value = it.next();
            if (value instanceof String) {
                try {
                    Double.parseDouble((String) value);
                } catch (NumberFormatException ex) {
                    return 0;
                }
            }
        }
        return VectorUtils.sum(this);
    }

    public MatrixRow(T... ts) {
        this();
        this.addAll(Arrays.asList(ts));
    }

    public MatrixRow() {
        super();
    }

    /**
	 * 以double类型返回matrix中的一个对象
	 * 如果给定的index超过了当前MatrixRow的最大索引值,返回defaultValue
	 * @param index
	 * @return
	 */
    public double getDouble(int index) {
        return getDouble(index, 0.0);
    }

    /**
	 * 以double类型返回matrix中的一个对象,当matrix中对象为空时,返回defaultValue
	 * 如果给定的index超过了当前MatrixRow的最大索引值,返回defaultValue
	 * @param index
	 * @param defaultValue
	 * @return
	 */
    public double getDouble(int index, double defaultValue) {
        if (isOverFlow(index)) {
            return defaultValue;
        }
        T t = this.get(index);
        if (t == null || StringUtils.blank(t.toString())) {
            return defaultValue;
        }
        return Double.parseDouble(t.toString());
    }

    /**
	 * 以int类型返回matrix中的一个对象
	 * 如果给定的index超过了当前MatrixRow的最大索引值,返回defaultValue
	 * @param index
	 * @return int
	 */
    public int getInt(int index) {
        return getInt(index, 0);
    }

    /**
	 * 以int类型返回matrix中的一个对象,当matrix中对象为空时,返回defaultValue
	 * 如果给定的index超过了当前MatrixRow的最大索引值,返回defaultValue
	 * @param index
	 * @param defaultValue
	 * @return int
	 */
    public int getInt(int index, int defaultValue) {
        if (isOverFlow(index)) {
            return defaultValue;
        }
        T t = this.get(index);
        if (t == null || StringUtils.blank(t.toString())) {
            return defaultValue;
        }
        return Integer.parseInt(t.toString());
    }

    /**
	 * 以String类型返回matrix中的一个对象
	 * 如果给定的index超过了当前MatrixRow的最大索引值,返回defaultValue
	 * @param index
	 * @return {@link String}
	 */
    public String getString(int index) {
        return getString(index, MATRIX_STRING_ZERO_STR);
    }

    /**
	 * 以String类型返回matrix中的一个对象,当matrix中对象为空时,返回defaultValue
	 * 如果给定的index超过了当前MatrixRow的最大索引值,返回defaultValue
	 * @param index
	 * @param defaultValue
	 * @return {@link String}
	 */
    public String getString(int index, String defaultValue) {
        if (isOverFlow(index)) {
            return defaultValue;
        }
        T t = this.get(index);
        if (t == null || StringUtils.blank(t.toString())) {
            return defaultValue;
        }
        return t.toString();
    }

    /**
	 * 总是得到一个值，无论是超过边界.
	 * @param index 索引。
	 * @param defaultValue 默认值。
	 * @return 数值.
	 */
    public double getDoubleWithAnyway(int index, double defaultValue) {
        if (index < this.size()) {
            return getDouble(index, defaultValue);
        }
        return defaultValue;
    }

    /**
	 * 根据索引返回集合元素
	 * @param index
	 * @return 指定索引的集合元素,不存在则返回null
	 */
    public String printToPdf(int index) {
        if (this.size() > index) {
            String value = this.get(index).toString().trim();
            if (StringUtils.notBlank(value)) {
                return value;
            }
        }
        return null;
    }

    /**
	 * 判断给定的所以是否超出当前MatrixRow的最大索引值
	 * 超出最大索引: true  没有超过最大索引:false
	 * @param index
	 * @return
	 */
    private boolean isOverFlow(int index) {
        int avableIndex = this.size() - 1;
        if (index > avableIndex) {
            return true;
        }
        return false;
    }
}
