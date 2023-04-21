package org.dmd.mvw.tools.mvwgenerator.types;

import java.io.Serializable;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcHashedAttribute;
import org.dmd.dmc.DmcInputStreamIF;
import org.dmd.dmc.DmcOutputStreamIF;
import org.dmd.dmc.DmcValueException;

@SuppressWarnings("serial")
public abstract class DmcTypeMethodWithArgs extends DmcHashedAttribute<MethodWithArgs> implements Serializable {

    public DmcTypeMethodWithArgs() {
    }

    public DmcTypeMethodWithArgs(DmcAttributeInfo ai) {
        super(ai);
    }

    @Override
    public MethodWithArgs cloneValue(MethodWithArgs original) {
        return (new MethodWithArgs(original));
    }

    @Override
    public MethodWithArgs deserializeValue(DmcInputStreamIF dis) throws Exception {
        MethodWithArgs rc = new MethodWithArgs();
        rc.deserializeIt(dis);
        return (rc);
    }

    @Override
    public void serializeValue(DmcOutputStreamIF dos, MethodWithArgs value) throws Exception {
        value.serializeIt(dos);
    }

    @Override
    public MethodWithArgs typeCheck(Object value) throws DmcValueException {
        if (value instanceof MethodWithArgs) return ((MethodWithArgs) value);
        if (value instanceof String) return (new MethodWithArgs((String) value));
        throw (new DmcValueException("Object of class: " + value.getClass().getName() + " passed where object compatible with MethodWithArgs or String expected."));
    }
}
