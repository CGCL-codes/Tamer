package com.volantis.mcs.themes.types;

import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.model.validation.ValidationContext;

/**
 *  The StyleType interface provides the base for all CSS Style Types.
 *
 * @mock.generate
 */
public interface StyleType {

    void validate(ValidationContext context, StyleValue value);

    void accept(StyleTypeVisitor visitor);
}
