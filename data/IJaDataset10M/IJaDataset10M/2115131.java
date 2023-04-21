package j3dworkbench.props.common;

import j3dworkbench.proxy.NodeProxy;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class ValueProperty extends AbstractProperty {

    public enum ValuePropertyEnum implements Descriptor {

        VALUE("A multipurpose numerical value that can be changed through" + " interaction with other objects"), DEFAULT_VALUE("The default value is applied to the VALUE" + " on scene load and reset"), INCREMENT_VALUE_ON_ENABLE("When true, VALUE is incremented each time" + " this object is enabled (incompatible with some triggers)"), INCREMENT_VALUE("The value to add to VALUE when incrementing on enable"), INCREMENT_VALUE_ON_TRIGGER("When true, VALUE is incremented each time" + " this object is triggered by another (whose VALUE is added " + "to ours, see TRIGGERS)."), THRESHOLD_VALUE("A limit value at which a trigger may be configured" + " to fire when VALUE is greater than or equal to it (see TRIGGERS)");

        String descr;

        private ValuePropertyEnum(String d) {
            descr = d;
        }

        public String getDescription() {
            return descr;
        }
    }

    NodeProxy nodeProxy = null;

    public ValueProperty(NodeProxy np) {
        nodeProxy = np;
    }

    @Override
    protected Object getProperty(Object id) {
        switch((ValuePropertyEnum) id) {
            case VALUE:
                return String.valueOf(nodeProxy.getValue());
            case DEFAULT_VALUE:
                return String.valueOf(nodeProxy.getDefaultValue());
            case INCREMENT_VALUE_ON_ENABLE:
                return nodeProxy.isIncrementValueOnEnable();
            case INCREMENT_VALUE:
                return String.valueOf(nodeProxy.getIncrementValue());
            case INCREMENT_VALUE_ON_TRIGGER:
                return nodeProxy.isIncrementValueOnTrigger();
            case THRESHOLD_VALUE:
                return String.valueOf(nodeProxy.getThresholdValue());
        }
        return null;
    }

    @Override
    protected void setProperty(Object id, Object value) {
        switch((ValuePropertyEnum) id) {
            case VALUE:
                return;
            case DEFAULT_VALUE:
                nodeProxy.setDefaultValue(Float.valueOf((String) value));
                return;
            case INCREMENT_VALUE_ON_ENABLE:
                nodeProxy.setIncrementValueOnEnable((Boolean) value);
                return;
            case INCREMENT_VALUE:
                nodeProxy.setIncrementValue(Float.valueOf((String) value));
                return;
            case INCREMENT_VALUE_ON_TRIGGER:
                nodeProxy.setIncrementValueOnTrigger((Boolean) value);
                return;
            case THRESHOLD_VALUE:
                nodeProxy.setThresholdValue(Float.valueOf((String) value));
                return;
        }
    }

    @Override
    protected List<IPropertyDescriptor> getDescriptors() {
        List<IPropertyDescriptor> props = new ArrayList<IPropertyDescriptor>(4);
        props.add(new PropertyDescriptor(ValuePropertyEnum.VALUE));
        props.add(new NumberPropertyDescriptor(ValuePropertyEnum.DEFAULT_VALUE));
        if (!nodeProxy.isIncrementValueOnTrigger()) {
            props.add(new BooleanPropertyDescriptor(ValuePropertyEnum.INCREMENT_VALUE_ON_ENABLE));
            props.add(new NumberPropertyDescriptor(ValuePropertyEnum.INCREMENT_VALUE));
        }
        if (!nodeProxy.isIncrementValueOnEnable()) {
            props.add(new BooleanPropertyDescriptor(ValuePropertyEnum.INCREMENT_VALUE_ON_TRIGGER));
        }
        if (nodeProxy.isTriggeredOnValue()) {
            props.add(new NumberPropertyDescriptor(ValuePropertyEnum.THRESHOLD_VALUE));
        }
        return props;
    }
}
