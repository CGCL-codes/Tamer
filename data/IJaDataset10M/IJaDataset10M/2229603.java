package org.mitre.dm.qud.domain;

import org.mitre.midiki.state.*;
import org.mitre.midiki.logic.*;
import java.util.*;

/**
 *
 * @author CBURKE
 */
public class EnumerationAttributeEvaluator implements AttributeEvaluator {

    private Collection enumeration;

    /** Creates a new instance of EnumerationAttributeEvaluator */
    public EnumerationAttributeEvaluator(Collection en) {
        enumeration = en;
    }

    /**
     * Simple evaluation of the value for this type specification.
     * Does not consider current context. Does not consider faceted
     * type specs, varying across types and/or acros individuals.
     *
     * @returns true if the value is compatible with this type.
     */
    public boolean evaluate(Object value) {
        if (value == null) return false;
        Iterator it = enumeration.iterator();
        while (it.hasNext()) {
            if (value.equals(it.next())) return true;
        }
        return false;
    }

    /**
     * Simple evaluation of the value for this type specification.
     * Does not consider current context. Does not consider faceted
     * type specs, varying across types and/or acros individuals.
     * Use this method if you want to extract multiple bindings?
     *
     * @returns true if the value is compatible with this type.
     */
    public boolean evaluate(Object value, String slotName, String className, Object instanceId, int modality, InfoState infoState, Bindings bindings) {
        return evaluate(value);
    }
}
