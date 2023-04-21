package org.nomadpim.module.money.currency;

import org.nomadpim.core.entity.IEntity;
import org.nomadpim.core.entity.io.IFieldConverter;
import org.nomadpim.core.entity.io.ILocalResolver;
import org.nomadpim.core.util.key.Key;
import org.nomadpim.core.util.text.ParseException;

public class CurrencyPropertyConverter implements IFieldConverter<IEntity> {

    public String format(IEntity t) {
        return Long.toString(t.getKey().getID());
    }

    public IEntity parse(String value, ILocalResolver resolver) throws ParseException {
        try {
            long id = Long.parseLong(value);
            if (id == Key.NULL_ID) {
                return new NullCurrency();
            }
            return resolver.get(Key.createKey(Currency.TYPE_NAME, id));
        } catch (NumberFormatException e) {
            return new NullCurrency();
        }
    }
}
