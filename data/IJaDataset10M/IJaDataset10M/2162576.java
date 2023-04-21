package genericAlgorithm.framework.appcontroller.impl;

import genericAlgorithm.framework.appcontroller.AbstractContext;

/**
 *
 * @author OZS
 */
public class SimpleRequestContext extends AbstractContext {

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SimpleRequestContext[");
        buffer.append(super.toString());
        buffer.append("]");
        return buffer.toString();
    }
}
