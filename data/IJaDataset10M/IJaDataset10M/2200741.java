package org.jofc.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jofc.itf.JOFCAttribute;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.List;

/**
 * 工具方法类
 * 
 * @author 张啸[zzxx513@gmail.com]
 */
public final class JOFCUtils {

    private JOFCUtils() {
    }

    public static void appendJson(JSONArray ja, Object value) {
    }

    /**
     * 添加{@link JOFCAttribute} 对象作为属性，如果JOFCAttribute.getAttributeValue()方法返回null就不添加
     * 
     * @param jo
     * @param attribute
     */
    public static void appendJson(JSONObject jo, JOFCAttribute attribute) {
        if (attribute == null || attribute.getAttributeValue() == null) {
            return;
        } else {
            appendJson(jo, attribute.getAttributeName(), attribute);
        }
    }

    /**
     * 添加属性用的方法，根据value的类型做一些判断，决定是否添加进来
     * 
     * @param jo
     * @param key
     * @param value
     */
    public static void appendJson(JSONObject jo, String key, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof String || value instanceof JOFCAttribute) {
            jo.put(key, value);
        } else if (value instanceof Number) {
            jo.put(key, value);
        } else if (value instanceof JSONArray) {
            if (((JSONArray) value).size() <= 0) {
                return;
            } else {
                jo.put(key, value);
            }
        } else {
            jo.put(key, value);
        }
    }

    /**
     * 返回输入的数据的最小数量级
     * 
     * @param values
     * @return int 数量级对10的开方，如果数量级是个位，则值为0，如果数量级为100，则返回2，如果数量级为0.001，则返回-3
     */
    public static int getMinPrecise(double[] values) {
        return getMinPrecise(0, values, true);
    }

    /**
     * 返回输入的数据的最大数量级，该方法只对y轴坐标值大于1时有效
     * 
     * @param jchart
     * @return int 数量级对10的开方，如果Y轴上的最大值的数量级是n(n>2)，则返回n-2，如果n<=2，返回n
     */
    public static int getMaxPrecise(List<Double> values) {
        int max = 0;
        for (double value : values) {
            String str = ("" + value).split("\\.")[0];
            int lv = str.length() - 1;
            max = max > lv ? max : lv;
        }
        return max;
    }

    /**
     * 返回输入的数据的最小数量级
     * 
     * @param precise
     * @param values
     * @param bigger
     * @return int 数量级对10的开方，如果数量级是个位，则值为0，如果数量级为100，则返回2，如果数量级为0.001，则返回-3
     */
    private static int getMinPrecise(int precise, double[] values, boolean bigger) {
        int length = values.length;
        int newPrecise = precise;
        boolean newBigger = bigger;
        boolean shouldReturn = true;
        for (int i = 0; i < length; i++) {
            if (values[i] == 0) {
                continue;
            }
            shouldReturn = false;
            double realPrecise = newPrecise > 0 ? Math.pow(10, Math.abs(newPrecise)) : 1 / Math.pow(10, Math.abs(newPrecise));
            boolean flag = true;
            if (realPrecise < 1) {
                double tempRealPrecise = BigDecimal.ONE.divide(new BigDecimal("" + realPrecise)).doubleValue();
                double tempValue = values[i] * tempRealPrecise;
                flag = tempValue % 1 == 0 ? true : false;
            } else {
                flag = values[i] % realPrecise == 0;
            }
            if (flag) {
                if (newBigger) {
                    newPrecise++;
                } else {
                    values[i] = 0;
                }
            } else {
                newPrecise--;
                newBigger = false;
            }
        }
        if (shouldReturn) {
            return newPrecise;
        } else {
            return getMinPrecise(newPrecise, values, newBigger);
        }
    }

    /**
     * 使用反射机制递归调用，将任意维数的对象转换成符合javaScript规范的字符串<br>
     * （例如，格式为[["1","2"],["3","4"]]）
     * 
     * @param array
     * @return String
     */
    public static String getJsArray(Object array) {
        if (array == null) {
            return "";
        }
        if (array.getClass().isArray()) {
            JSONArray result = new JSONArray();
            int length = Array.getLength(array);
            for (int i = 0; i < length; i++) {
                result.add(getJsArray(Array.get(array, i)));
            }
            return result.toString();
        } else {
            return array.toString();
        }
    }

    /**
     * 返回最小值和最大值
     * 
     * @param values
     * @return
     */
    public static double[] getMinAndMax(List<Double> values) {
        double min = values.get(0);
        double max = values.get(0);
        for (double value : values) {
            if (value < min) {
                min = value;
            } else if (value > max) {
                max = value;
            }
        }
        return new double[] { min, max };
    }
}
