package net.nothinginteresting.datamappers3.fields;

import net.nothinginteresting.datamappers3.DM;
import net.nothinginteresting.datamappers3.meta.JavaType;

/**
 * @author Dmitriy Gorbenko
 * 
 */
public class LongField<T> extends NumericField<T, Long> {

    public LongField(DM inner) {
        super(inner);
    }

    @Override
    public JavaType getJavaType() {
        return JavaType.LONG;
    }
}
