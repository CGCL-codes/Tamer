package org.opoo.oqs.criterion;

import java.util.Arrays;
import org.opoo.oqs.type.Type;
import org.opoo.oqs.type.TypeFactory;
import org.opoo.util.Assert;
import org.opoo.util.StringUtils;

public class In implements Criterion {

    private final String qs;

    private final Object[] values;

    private final Type[] types;

    public In(String name, Object[] values) {
        this(name, values, null);
    }

    /**
     * ��������顣
     * @param name String
     * @param values Object[]
     * @param type Type
     */
    public In(String name, Object[] values, Type type) {
        Assert.notNull(name, "criterion name can not be null");
        Assert.notNull(values, "criterion values cannot be null");
        this.values = values;
        qs = name + " in (" + StringUtils.fillToString("?", ", ", values.length) + ")";
        if (type == null) {
            for (int i = 0; i < values.length; i++) {
                if (values[i] != null) {
                    type = TypeFactory.guessType(values[i]);
                    break;
                }
            }
            if (type == null) {
                type = Type.SERIALIZABLE;
            }
        }
        types = new Type[values.length];
        if (values.length > 0) {
            Arrays.fill(types, type);
        }
    }

    public Object[] getValues() {
        return values;
    }

    public String toString() {
        return qs;
    }

    public Type[] getTypes() {
        return types;
    }
}
