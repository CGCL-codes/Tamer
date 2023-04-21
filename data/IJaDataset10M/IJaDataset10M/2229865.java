package com.volantis.mcs.themes.impl.types;

import com.volantis.mcs.themes.types.StyleTimeType;
import com.volantis.mcs.themes.types.StyleTypeVisitor;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleTime;
import com.volantis.mcs.model.validation.ValidationContext;

/**
 *
 */
public class StyleTimeTypeImpl extends AbstractSingleStyleType implements StyleTimeType {

    public StyleTimeTypeImpl() {
        super(StyleValueType.TIME);
    }

    public void accept(StyleTypeVisitor visitor) {
        visitor.visitStyleTimeType(this);
    }

    protected void validateSupportedValue(ValidationContext context, StyleValue value) {
        StyleTime time = (StyleTime) value;
    }
}
