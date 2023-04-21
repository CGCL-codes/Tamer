package net.deytan.wofee.gae.persistence.translator;

import net.deytan.wofee.exception.TranslatorException;
import com.google.appengine.api.datastore.Text;

public class TextTranslator extends ObjectTranslator {

    public TextTranslator() {
        super(String.class, Text.class);
    }

    @Override
    public Object toDatastore(final Object value) throws TranslatorException {
        this.assertIsJavaType(value);
        return value != null ? new Text((String) value) : null;
    }

    @Override
    public Object fromDatastore(final Object value) throws TranslatorException {
        this.assertIsDatastoreType(value);
        return value != null ? ((Text) value).getValue() : null;
    }
}
