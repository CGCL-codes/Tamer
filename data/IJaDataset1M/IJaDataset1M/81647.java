package org.jnetpcap.packet.structure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jnetpcap.packet.annotate.Field.Property;

/**
 * @author Mark Bednarczyk
 * @author Sly Technologies, Inc.
 */
public class AnnotatedFieldRuntime {

    private final Map<Property, AnnotatedFieldMethod> map = new HashMap<Property, AnnotatedFieldMethod>();

    private final AnnotatedField parent;

    public AnnotatedFieldRuntime(AnnotatedField parent) {
        this.parent = parent;
    }

    /**
	 * 
	 */
    public void finishProcessing(List<HeaderDefinitionError> errors) {
        for (Property f : Property.values()) {
            try {
                if (map.containsKey(f) == false) {
                    map.put(f, AnnotatedFieldMethod.generateFunction(f, parent));
                }
            } catch (HeaderDefinitionError e) {
                errors.add(e);
            }
        }
    }

    /**
	 * @return
	 */
    public Map<Property, AnnotatedFieldMethod> getFunctionMap() {
        return map;
    }

    public void setFunction(AnnotatedFieldMethod method) {
        final Property function = method.getFunction();
        if (map.containsKey(function)) {
            throw new HeaderDefinitionError(method.getMethod().getDeclaringClass(), "duplicate " + function + " method declarations for field " + parent.getName());
        }
        if (method.isMapped == false) {
            method.configFromField(parent);
        }
        map.put(function, method);
    }

    /**
	 * @param methods
	 */
    public void setFunction(Map<Property, AnnotatedFieldMethod> methods) {
        for (AnnotatedFieldMethod f : methods.values()) {
            setFunction(f);
        }
    }
}
