package com.xiledsystems.AlternateJavaBridgelib.components.variants;

import java.util.Calendar;

public final class DateVariant extends Variant {

    private Calendar value;

    public static final DateVariant getDateVariant(Calendar value) {
        return new DateVariant(value);
    }

    private DateVariant(Calendar value) {
        super(VARIANT_DATE);
        this.value = value;
    }

    public Calendar getDate() {
        return this.value;
    }

    public boolean identical(Variant rightOp) {
        return cmp(rightOp) == 0;
    }

    public int cmp(Variant rightOp) {
        if (rightOp.getKind() != 11) {
            return super.cmp(rightOp);
        }
        return this.value.compareTo(rightOp.getDate());
    }

    public boolean typeof(Class<?> type) {
        return type.isInstance(this.value);
    }
}
