package org.slasoi.businessManager.postSale.reporting.impl.mapping;

import java.lang.reflect.Method;

/**
 * Class representing a query to the database plus the mapping of the output to a parameter name
 */
public class Query {

    Method method = null;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String toString() {
        String aux = method.toString();
        return aux;
    }
}
