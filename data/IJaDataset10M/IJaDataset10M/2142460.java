package org.brandao.brutos.type;

import java.io.IOException;
import org.brandao.brutos.ConfigurableApplicationContext;
import org.brandao.brutos.Invoker;
import org.brandao.brutos.MvcResponse;

/**
 *
 * @author Afonso Brandao
 */
public class BooleanWrapperType implements Type {

    public BooleanWrapperType() {
    }

    public Class getClassType() {
        return Boolean.class;
    }

    public Object getValue(Object value) {
        if (value instanceof Boolean) return value; else if (value instanceof String) return Boolean.valueOf((String) value); else if (value == null) return null; else throw new UnknownTypeException(value.getClass().getName());
    }

    public void setValue(Object value) throws IOException {
        ConfigurableApplicationContext app = (ConfigurableApplicationContext) Invoker.getApplicationContext();
        MvcResponse response = app.getMvcResponse();
        response.process(value);
    }
}
