package org.spockframework.builder;

import java.lang.reflect.Type;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.spockframework.util.MopUtil;
import groovy.lang.MetaProperty;

public class SetterSlotFactory implements ISlotFactory {

    public ISlot create(Object owner, Type ownerType, String name) {
        MetaProperty property = InvokerHelper.getMetaClass(owner).getMetaProperty(name);
        return property != null && MopUtil.isWriteable(property) ? new PropertySlot(owner, ownerType, property) : null;
    }
}
