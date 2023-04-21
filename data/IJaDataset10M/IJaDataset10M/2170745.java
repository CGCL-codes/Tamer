package org.tagbox.config;

import java.util.List;
import java.util.ArrayList;

public abstract class ConfigurableInfo extends Info {

    private List properties;

    private List operations;

    private List constructors;

    public ConfigurableInfo(String name) {
        super(name);
        properties = new ArrayList();
        operations = new ArrayList();
        constructors = new ArrayList();
    }

    public void addProperty(PropertyInfo info) {
        properties.add(info);
    }

    public PropertyInfo[] getProperties() {
        PropertyInfo[] props = new PropertyInfo[properties.size()];
        properties.toArray(props);
        return props;
    }

    public void addOperation(OperationInfo info) {
        operations.add(info);
    }

    public OperationInfo[] getOperations() {
        OperationInfo[] ops = new OperationInfo[operations.size()];
        operations.toArray(ops);
        return ops;
    }

    public void addConstructor(ConstructorInfo info) {
        constructors.add(info);
    }

    public ConstructorInfo[] getConstructors() {
        ConstructorInfo[] ctors = new ConstructorInfo[constructors.size()];
        constructors.toArray(ctors);
        return ctors;
    }

    public String toString() {
        StringBuffer msg = new StringBuffer(super.toString());
        msg.append("[");
        ConstructorInfo[] ctors = getConstructors();
        for (int i = 0; i < ctors.length; ++i) msg.append(ctors[i]);
        PropertyInfo[] props = getProperties();
        for (int i = 0; i < props.length; ++i) msg.append(props[i]);
        OperationInfo[] ops = getOperations();
        for (int i = 0; i < ops.length; ++i) msg.append(ops[i]);
        msg.append("]");
        return msg.toString();
    }

    public abstract Configurable getConfigurable();
}
