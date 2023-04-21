package net.sf.balm.spring.io;

import java.util.List;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.converters.AbstractArrayConverter;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

/**
 * @author dz
 */
public class ResourceArrayConverter extends AbstractArrayConverter {

    public ResourceArrayConverter() {
        this.defaultValue = null;
        this.useDefault = false;
    }

    public ResourceArrayConverter(Object defaultValue) {
        this.defaultValue = defaultValue;
        this.useDefault = true;
    }

    private static Resource model[] = new Resource[0];

    public Object convert(Class type, Object value) {
        if (value == null) {
            if (useDefault) {
                return (defaultValue);
            } else {
                throw new ConversionException("No value specified");
            }
        }
        if (model.getClass() == value.getClass()) {
            return (value);
        }
        if (strings.getClass() == value.getClass()) {
            try {
                String values[] = (String[]) value;
                Resource results[] = new Resource[values.length];
                for (int i = 0; i < values.length; i++) {
                    results[i] = new DefaultResourceLoader().getResource(values[i]);
                }
                return (results);
            } catch (Exception e) {
                if (useDefault) {
                    return (defaultValue);
                } else {
                    throw new ConversionException(value.toString(), e);
                }
            }
        }
        try {
            List list = parseElements(value.toString());
            Resource results[] = new Resource[list.size()];
            for (int i = 0; i < results.length; i++) {
                results[i] = new DefaultResourceLoader().getResource((String) list.get(i));
            }
            return (results);
        } catch (Exception e) {
            if (useDefault) {
                return (defaultValue);
            } else {
                throw new ConversionException(value.toString(), e);
            }
        }
    }
}
