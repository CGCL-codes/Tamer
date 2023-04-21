package org.datanucleus.store.types.converters;

import java.util.Currency;

/**
 * Class to handle the conversion between java.util.Currency and a String form.
 */
public class CurrencyStringConverter implements TypeConverter<Currency, String> {

    public Currency toMemberType(String str) {
        if (str == null) {
            return null;
        }
        return java.util.Currency.getInstance(str);
    }

    public String toDatastoreType(Currency curr) {
        return curr != null ? curr.toString() : null;
    }
}
