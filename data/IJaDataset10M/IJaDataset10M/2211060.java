package org.gamegineer.table.internal.ui.databinding.conversion;

import net.jcip.annotations.Immutable;
import org.eclipse.core.databinding.conversion.Converter;

/**
 * A data binding converter from values of type {@link java.lang.Integer#TYPE}
 * to values of type {@link java.lang.String}.
 */
@Immutable
public final class PrimitiveIntegerToStringConverter extends Converter {

    /**
     * Initializes a new instance of the {@code
     * PrimitiveIntegerToStringConverter} class.
     */
    public PrimitiveIntegerToStringConverter() {
        super(Integer.TYPE, String.class);
    }

    @Override
    public Object convert(final Object fromObject) {
        return String.valueOf(fromObject);
    }
}
