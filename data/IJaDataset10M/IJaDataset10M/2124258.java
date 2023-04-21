package org.bing.adapter.com.caucho.hessian.io;

import java.io.IOException;
import java.math.BigDecimal;
import org.bing.adapter.com.caucho.hessian.HessianException;

/**
 * Deserializing a BigDecimal
 */
public class BigDecimalDeserializer extends AbstractStringValueDeserializer {

    @Override
    public Class getType() {
        return BigDecimal.class;
    }

    @Override
    protected Object create(String value) {
        return new BigDecimal(value);
    }
}
