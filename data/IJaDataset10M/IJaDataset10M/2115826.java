package org.ofbiz.minilang.method.entityops;

import org.w3c.dom.*;
import org.ofbiz.minilang.*;
import org.ofbiz.minilang.method.*;

/**
 * Creates a java.sql.Timestamp with the current date/time in it and puts it in the env
 */
public class NowTimestampToEnv extends MethodOperation {

    ContextAccessor envAcsr;

    public NowTimestampToEnv(Element element, SimpleMethod simpleMethod) {
        super(element, simpleMethod);
        envAcsr = new ContextAccessor(element.getAttribute("env-name"));
    }

    public boolean exec(MethodContext methodContext) {
        envAcsr.put(methodContext, new java.sql.Timestamp(System.currentTimeMillis()));
        return true;
    }

    public String rawString() {
        return "<now-timestamp-to-env/>";
    }

    public String expandedString(MethodContext methodContext) {
        return this.rawString();
    }
}
