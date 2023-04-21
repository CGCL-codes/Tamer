package org.dmd.mvw.tools.mvwgenerator.generated.types;

import java.io.Serializable;
import org.dmd.dmc.DmcInputStreamIF;
import org.dmd.dmc.DmcOutputStreamIF;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;
import org.dmd.dmc.DmcAttribute;

@SuppressWarnings("serial")
public abstract class DmcTypeEventSpec extends DmcAttribute<EventSpec> implements Serializable {

    /**
     * Default constructor.
     */
    public DmcTypeEventSpec() {
    }

    /**
     * Default constructor.
     */
    public DmcTypeEventSpec(DmcAttributeInfo ai) {
        super(ai);
    }

    protected EventSpec typeCheck(Object value) throws DmcValueException {
        EventSpec rc = null;
        if (value instanceof EventSpec) {
            rc = (EventSpec) value;
        } else if (value instanceof String) {
            rc = new EventSpec((String) value);
        } else {
            throw (new DmcValueException("Object of class: " + value.getClass().getName() + " passed where object compatible with EventSpec expected."));
        }
        return (rc);
    }

    /**
     * Returns a clone of a value associated with this type.
     */
    public EventSpec cloneValue(EventSpec val) {
        return (new EventSpec(val));
    }

    /**
     * Writes a EventSpec.
     */
    @Override
    public void serializeValue(DmcOutputStreamIF dos, EventSpec value) throws Exception {
        value.serializeIt(dos);
    }

    /**
     * Reads a EventSpec.
     */
    @Override
    public EventSpec deserializeValue(DmcInputStreamIF dis) throws Exception {
        EventSpec rc = new EventSpec();
        rc.deserializeIt(dis);
        return (rc);
    }
}
