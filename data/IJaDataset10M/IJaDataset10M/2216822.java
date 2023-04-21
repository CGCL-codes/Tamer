package org.initialize4j.bean;

import org.initialize4j.Initialize;
import org.initialize4j.conditions.impl.EmptyString;
import org.initialize4j.conditions.impl.Null;

/**
 * @author <a href="hillger.t@gmail.com">hillger.t</a>
 */
public class ScopedBean {

    @Initialize(when = Null.class, value = "Hello world", scope = { "a" })
    private String scopeValueA = null;

    @Initialize(when = EmptyString.class, value = "Hello world", scope = { "b" })
    private String scopeValueB = "";

    public String getScopeValueA() {
        return scopeValueA;
    }

    public void setScopeValueA(String value) {
        this.scopeValueA = value;
    }

    public String getScopeValueB() {
        return scopeValueB;
    }

    public void setScopeValueB(String emptValue) {
        this.scopeValueB = emptValue;
    }
}
