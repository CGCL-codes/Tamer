package net.deytan.wofee.persistence.translator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.deytan.wofee.exception.PersistenceException;
import net.deytan.wofee.persistence.PersistableField;

public class TranslatorFactory {

    private final Map<String, Translator> translators;

    private Translator defaultTranslator;

    private Class<Translator> enumTranslatorClass;

    public TranslatorFactory() {
        this.translators = new HashMap<String, Translator>();
    }

    public void setTranslator(final PersistableField persistableField) throws PersistenceException {
        if (persistableField.getJavaType().isEnum()) {
            try {
                persistableField.setTranslator(this.enumTranslatorClass.getConstructor(Class.class).newInstance(persistableField.getJavaType()));
            } catch (Exception exception) {
                throw new PersistenceException("Creating enum translator failed", exception);
            }
        } else {
            persistableField.setTranslator(this.translators.get(persistableField.getJavaType() + "_" + persistableField.getDatastoreType()));
            if ((persistableField.getTranslator() == null) && persistableField.isIterable()) {
                persistableField.setTranslator(this.translators.get(persistableField.getJavaGenericType() + "_" + persistableField.getDatastoreGenericType()));
            }
            if (persistableField.getTranslator() == null) {
                persistableField.setTranslator(this.defaultTranslator);
            }
        }
    }

    public void setTranslators(final List<Translator> translators) {
        for (Translator translator : translators) {
            this.translators.put(translator.getJavaType() + "_" + translator.getDatastoreType(), translator);
        }
    }

    public void setDefaultTranslator(final Translator defaultTranslator) {
        this.defaultTranslator = defaultTranslator;
    }

    public void setEnumTranslatorClass(final Class<Translator> enumTranslatorClass) {
        this.enumTranslatorClass = enumTranslatorClass;
    }
}
