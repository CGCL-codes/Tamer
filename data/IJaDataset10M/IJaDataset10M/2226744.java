package net.sf.javascribe.generator.accessor.impl;

import java.util.HashMap;
import java.util.Iterator;
import net.sf.javascribe.ProcessingException;
import net.sf.javascribe.generator.accessor.ControllerEventTypeAccessor;
import net.sf.javascribe.generator.accessor.ControllerTierTypesAccessor;
import net.sf.javascribe.generator.accessor.ProcessorTypesAccessor;
import net.sf.javascribe.generator.accessor.JavaTypeAccessor;
import net.sf.javascribe.generator.util.AttributeHolder;

/**
 * This class provides access to all view type accessors available in a single view definition.
 * @author DCS
 *
 */
public class ControllerTierTypesAccessorImpl implements ControllerTierTypesAccessor {

    HashMap<String, HashMap<String, ControllerEventTypeAccessor>> eventGroups = null;

    HashMap<String, JavaTypeAccessor> controllerStateTypes = null;

    public ControllerTierTypesAccessorImpl() {
        eventGroups = new HashMap<String, HashMap<String, ControllerEventTypeAccessor>>();
        controllerStateTypes = new HashMap<String, JavaTypeAccessor>();
    }

    public void addControllerStateType(String type, AttributeHolder acc) {
        controllerStateTypes.put(type, (JavaTypeAccessor) acc);
    }

    public void addEventGroup(String eventGroupName) {
        eventGroups.put(eventGroupName, new HashMap<String, ControllerEventTypeAccessor>());
    }

    public void addEvent(String eventGroupName, ControllerEventTypeAccessor acc) {
        HashMap<String, ControllerEventTypeAccessor> m = null;
        m = eventGroups.get(eventGroupName);
        m.put(acc.getName(), acc);
    }

    public AttributeHolder getControllerStateTypeAccessor(String name) {
        return (AttributeHolder) controllerStateTypes.get(name);
    }

    public ControllerEventTypeAccessor getControllerEventTypeAccessor(String eventGroupName, String eventName) throws ProcessingException {
        ControllerEventTypeAccessor ret = null;
        HashMap m = null;
        m = (HashMap) eventGroups.get(eventGroupName);
        ret = (ControllerEventTypeAccessor) m.get(eventName);
        return ret;
    }

    public String getControllerDefinitionName() {
        return ProcessorTypesAccessor.DEFAULT_DEFINITION;
    }

    public JavaTypeAccessor getTypeAccessor(String type) {
        HashMap m = null;
        Iterator groups = null;
        String group = null;
        JavaTypeAccessor ret = null;
        ret = (JavaTypeAccessor) controllerStateTypes.get(type);
        groups = eventGroups.keySet().iterator();
        while ((ret == null) && (groups.hasNext())) {
            group = (String) groups.next();
            m = (HashMap) eventGroups.get(group);
            ret = (JavaTypeAccessor) m.get(type);
        }
        return ret;
    }
}
