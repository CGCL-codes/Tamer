package org.brandao.brutos.type;

import java.io.IOException;
import org.brandao.brutos.ConfigurableApplicationContext;
import org.brandao.brutos.Invoker;
import org.brandao.brutos.MvcResponse;

/**
 *
 * @author Afonso Brandao
 */
public class IntegerType implements Type {

    public IntegerType() {
    }

    public Class getClassType() {
        return Integer.TYPE;
    }

    public Object getValue(Object value) {
        if (value instanceof Integer) return value; else if (value instanceof String) return Integer.valueOf((String) value); else if (value == null) return null; else throw new UnknownTypeException();
    }

    public void setValue(Object value) throws IOException {
        ConfigurableApplicationContext app = (ConfigurableApplicationContext) Invoker.getApplicationContext();
        MvcResponse response = app.getMvcResponse();
        response.process(value);
    }
}
