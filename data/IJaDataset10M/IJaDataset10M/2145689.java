package jfun.yan.xml.nuts.optional;

import jfun.yan.Component;
import jfun.yan.Components;
import jfun.yan.xml.nut.ComponentNut;

/**
 * Use this tag to reference any component in the current container.
 * The reference is not checked by the compilation and is not affected by namespace.
 * 
 * <p>
 * @author Ben Yu
 * Dec 21, 2005 1:43:34 PM
 */
public class RefNut extends ComponentNut {

    private Object to;

    private Class type;

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public Object getTo() {
        return to;
    }

    public void setTo(Object ref) {
        this.to = ref;
    }

    public Component eval() {
        if (to != null) return Components.useKey(to); else {
            if (type == null) {
                raise("either 'to' or 'type' has to be specified");
            }
            return Components.useType(type);
        }
    }
}
