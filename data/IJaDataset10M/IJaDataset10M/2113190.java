package net.sos.translator;

import java.util.Map;
import java.util.Properties;

public class PropertiesTranslatorImpl implements IPropertiesTranslator {

    protected ITranslator translator;

    @Override
    public void setTranslator(ITranslator translator) {
        this.translator = translator;
    }

    @Override
    public Properties translateProperties(Properties properties) {
        Properties translatedProperties = new Properties();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            translatedProperties.put(entry.getKey(), translator.translate(entry.getValue().toString()));
        }
        return translatedProperties;
    }
}
