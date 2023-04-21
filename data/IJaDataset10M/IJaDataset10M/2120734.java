package org.jenmo.core.descriptor;

import java.util.Set;
import org.jenmo.core.adapter.AdapterCmd;
import org.jenmo.core.adapter.AdapterFactory;
import org.jenmo.core.adapter.IAdapterCmd;
import org.jenmo.core.adapter.AbstractAdapter.AdapterToSingle;
import org.jenmo.core.descriptor.IPropertyDescriptor.IPropDescAsSingle;
import org.jenmo.core.domain.Node;
import org.jenmo.core.domain.NodeProperty;
import org.jenmo.core.domain.Property;
import org.jenmo.core.util.CastUtils;

/**
 * Package access level class. Implementation of {@link IPropDescAsSingle<T>}.
 * 
 * @author Nicolas Ocquidant
 * @since 1.0
 */
class PropDescAsSingle<T> implements IPropDescAsSingle<T> {

    private Class<T> clazz;

    public PropDescAsSingle(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public AdapterToSingle<T, NodeProperty> instantiateAdapter(final Node node, final Property prop, final INodeCallback<NodeProperty> callbacks) {
        IAdapterCmd<Object, T, NodeProperty> cmd = new AdapterCmd<Object, T, NodeProperty>() {

            @Override
            public boolean accept(NodeProperty arg) {
                return (arg.getProperty() == prop);
            }

            @Override
            public T getValue(NodeProperty arg) {
                return CastUtils.fromString(clazz, arg.getValue());
            }

            @Override
            public NodeProperty instantiateAndAdd(Object index, T value, Set<? super NodeProperty> target) {
                String val = CastUtils.toString(value);
                NodeProperty newInstance = NodeProperty.newInstance(node, prop, val);
                if (target != null) {
                    target.add(newInstance);
                }
                return newInstance;
            }
        };
        return AdapterFactory.single(cmd);
    }
}
