package com.phloc.types.dyntypes.datetime;

import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.joda.time.LocalDate;
import com.phloc.commons.state.ESuccess;
import com.phloc.datetime.PDTFactory;
import com.phloc.datetime.format.PDTToString;
import com.phloc.types.datatype.impl.SimpleDataTypeRegistry;
import com.phloc.types.dyntypes.impl.AbstractDynamicValue;

/**
 * Dynamic value for objects of class {@link LocalDate}.
 * 
 * @author philip
 */
public final class DynamicValueLocalDate extends AbstractDynamicValue<LocalDate> {

    public DynamicValueLocalDate() {
        this(null);
    }

    public DynamicValueLocalDate(@Nullable final LocalDate aLocalDate) {
        super(SimpleDataTypeRegistry.DT_LOCALDATE, aLocalDate);
    }

    @Nullable
    public String getAsSerializationText() {
        final LocalDate aValue = getValue();
        return aValue == null ? null : Long.toString(PDTFactory.createDateTime(aValue).getMillis());
    }

    @Nonnull
    public ESuccess setAsSerializationText(@Nullable final String sText) {
        if (sText == null) setValue(null); else try {
            setValue(PDTFactory.createLocalDateFromMillis(Long.parseLong(sText)));
        } catch (final NumberFormatException ex) {
            return ESuccess.FAILURE;
        }
        return ESuccess.SUCCESS;
    }

    @Nullable
    public String getAsDisplayText(@Nonnull final Locale aDisplayLocale) {
        return PDTToString.getAsString(getValue(), aDisplayLocale);
    }

    @Nonnull
    public DynamicValueLocalDate getClone() {
        return new DynamicValueLocalDate(getValue());
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
