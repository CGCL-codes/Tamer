package jfun.yan.xml.nuts.optional;

import jfun.yan.Component;
import jfun.yan.xml.nuts.MapEntry;

/**
 * An injection case. Supports "type" and "injection" attributes.
 * <p>
 * @author Ben Yu
 * Dec 17, 2005 12:33:53 PM
 */
public class InjectionCase extends MapEntry {

    public void setType(Class type) {
        super.setKey(type);
    }

    public void setInjection(Component inj) {
        super.setVal(inj);
    }
}
