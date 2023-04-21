package com.phloc.types.dyntypes.base;

import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.phloc.commons.locale.LocaleFormatter;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.types.datatype.impl.SimpleDataTypeRegistry;
import com.phloc.types.dyntypes.impl.AbstractDynamicValue;

/**
 * Dynamic value for objects of class {@link Integer}.
 * 
 * @author philip
 */
public final class DynamicValueInteger extends AbstractDynamicValue<Integer> {

    public DynamicValueInteger() {
        this(null);
    }

    public DynamicValueInteger(final int nValue) {
        this(Integer.valueOf(nValue));
    }

    public DynamicValueInteger(@Nullable final Integer aValue) {
        super(SimpleDataTypeRegistry.DT_INT, aValue);
    }

    @Nonnull
    public EChange setValue(final int nValue) {
        return setValue(Integer.valueOf(nValue));
    }

    @Nullable
    public String getAsSerializationText() {
        final Integer aValue = getValue();
        return aValue == null ? null : aValue.toString();
    }

    @Nonnull
    public ESuccess setAsSerializationText(@Nullable final String sText) {
        if (sText == null) setValue(null); else try {
            setValue(Integer.valueOf(sText));
        } catch (final NumberFormatException ex) {
            return ESuccess.FAILURE;
        }
        return ESuccess.SUCCESS;
    }

    @Nullable
    public String getAsDisplayText(@Nonnull final Locale aDisplayLocale) {
        final Integer aValue = getValue();
        return aValue == null ? null : LocaleFormatter.getFormatted(aValue.intValue(), aDisplayLocale);
    }

    @Nonnull
    public DynamicValueInteger getClone() {
        return new DynamicValueInteger(getValue());
    }

    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
