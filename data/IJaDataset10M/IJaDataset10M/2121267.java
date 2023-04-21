package com.volantis.mcs.runtime.configuration.xml.digester;

import our.apache.commons.digester.CallMethodRule;

/**
 * Extension to {@link CallMethodRule} which handles {@link Enabled} objects.
 */
public class EnabledCallMethodRule extends CallMethodRule {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public EnabledCallMethodRule(String methodName, int paramCount, Class paramTypes[]) {
        super(methodName, paramCount, paramTypes);
    }

    public void end() throws Exception {
        Object o = digester.peek();
        if (!(o instanceof EnabledObjectCreateRule)) {
            super.end();
        }
    }
}
