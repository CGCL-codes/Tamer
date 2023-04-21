package de.erdesignerng.visual.scaffolding;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.widgetprocessor.binding.beanutils.BeanUtilsBindingProcessor;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;
import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Set;
import static org.metawidget.inspector.InspectionResultConstants.NAME;
import static org.metawidget.inspector.InspectionResultConstants.NO_SETTER;
import static org.metawidget.inspector.InspectionResultConstants.PROPERTY;
import static org.metawidget.inspector.InspectionResultConstants.TRUE;

public class ERDesignerBeanUtilsBindingProcessor extends BeanUtilsBindingProcessor {

    /**
     * A better implementation for the binding converter.
     * <p/>
     * This will not use the ConvertUtils, as here for every enum a converter
     * must be registered. To make life easier, enums are not handled by
     * ConvertUtils.
     */
    @Override
    public Object convertFromString(String value, Class<?> expectedType) {
        if (expectedType.isEnum()) {
            return Enum.valueOf((Class<Enum>) expectedType, value);
        }
        if (!expectedType.isPrimitive()) {
            if (value == null) {
                return null;
            }
        }
        return ConvertUtils.convert(value, expectedType);
    }

    @Override
    public void onStartBuild(SwingMetawidget metawidget) {
        metawidget.putClientProperty(BeanUtilsBindingProcessor.class, null);
    }

    @Override
    public JComponent processWidget(JComponent component, String elementName, Map<String, String> attributes, SwingMetawidget metawidget) {
        JComponent componentToBind = component;
        if (componentToBind instanceof JScrollPane) componentToBind = (JComponent) ((JScrollPane) componentToBind).getViewport().getView();
        String componentProperty = metawidget.getValueProperty(componentToBind);
        if (componentProperty == null) return component;
        String path = metawidget.getPath();
        if (PROPERTY.equals(elementName)) path += StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + attributes.get(NAME);
        try {
            String names = PathUtils.parsePath(path, StringUtils.SEPARATOR_FORWARD_SLASH_CHAR).getNames().replace(StringUtils.SEPARATOR_FORWARD_SLASH_CHAR, StringUtils.SEPARATOR_DOT_CHAR);
            Object sourceValue;
            try {
                sourceValue = retrieveValueFromObject(metawidget, metawidget.getToInspect(), names);
            } catch (NoSuchMethodException e) {
                throw WidgetProcessorException.newException("Property '" + names + "' has no getter");
            }
            SavedBinding binding = new SavedBinding(componentToBind, componentProperty, names, TRUE.equals(attributes.get(NO_SETTER)));
            saveValueToWidget(binding, sourceValue);
            State state = getState(metawidget);
            if (state.bindings == null) state.bindings = CollectionUtils.newHashSet();
            state.bindings.add(binding);
        } catch (Exception e) {
            throw WidgetProcessorException.newException(e);
        }
        return component;
    }

    /**
     * Rebinds the Metawidget to the given Object.
     * <p/>
     * This method is an optimization that allows clients to load a new object
     * into the binding <em>without</em> calling setToInspect, and therefore
     * without reinspecting the object or recreating the components. It is the
     * client's responsibility to ensure the rebound object is compatible with
     * the original setToInspect.
     */
    @Override
    public void rebind(Object toRebind, SwingMetawidget metawidget) {
        metawidget.updateToInspectWithoutInvalidate(toRebind);
        State state = getState(metawidget);
        if (state.bindings != null) {
            try {
                for (SavedBinding binding : state.bindings) {
                    Object sourceValue;
                    String names = binding.getNames();
                    try {
                        sourceValue = retrieveValueFromObject(metawidget, toRebind, names);
                    } catch (NoSuchMethodException e) {
                        throw WidgetProcessorException.newException("Property '" + names + "' has no getter");
                    }
                    saveValueToWidget(binding, sourceValue);
                }
            } catch (Exception e) {
                throw WidgetProcessorException.newException(e);
            }
        }
        for (Component component : metawidget.getComponents()) {
            if (component instanceof SwingMetawidget) rebind(toRebind, (SwingMetawidget) component);
        }
    }

    @Override
    public void save(SwingMetawidget metawidget) {
        State state = getState(metawidget);
        if (state.bindings != null) {
            try {
                for (SavedBinding binding : state.bindings) {
                    if (!binding.isSettable()) continue;
                    Object componentValue = retrieveValueFromWidget(binding);
                    saveValueToObject(metawidget, binding.getNames(), componentValue);
                }
            } catch (Exception e) {
                throw WidgetProcessorException.newException(e);
            }
        }
        for (Component component : metawidget.getComponents()) {
            if (component instanceof SwingMetawidget) save((SwingMetawidget) component);
        }
    }

    /**
     * Retrieve value identified by the given names from the given source.
     * <p/>
     * Clients may override this method to incorporate their own getter
     * convention.
     */
    @Override
    protected Object retrieveValueFromObject(SwingMetawidget metawidget, Object source, String names) throws Exception {
        return PropertyUtils.getProperty(source, names);
    }

    /**
     * Save the given value into the given source at the location specified by
     * the given names.
     * <p/>
     * Clients may override this method to incorporate their own setter
     * convention.
     *
     * @param componentValue the raw value from the <code>JComponent</code>
     */
    @Override
    protected void saveValueToObject(SwingMetawidget metawidget, String names, Object componentValue) throws Exception {
        Object source = metawidget.getToInspect();
        if ("".equals(componentValue)) {
            componentValue = null;
        }
        Class theTargetType = PropertyUtils.getPropertyType(source, names);
        if (componentValue instanceof String && theTargetType != String.class) {
            componentValue = ConvertUtils.convert((String) componentValue, theTargetType);
        }
        PropertyUtils.setSimpleProperty(source, names, componentValue);
    }

    protected Object retrieveValueFromWidget(SavedBinding binding) throws Exception {
        return PropertyUtils.getProperty(binding.getComponent(), binding.getComponentProperty());
    }

    protected void saveValueToWidget(SavedBinding binding, Object sourceValue) throws Exception {
        Class theTargetProperty = PropertyUtils.getPropertyType(binding.getComponent(), binding.getComponentProperty());
        if (theTargetProperty == String.class) {
            sourceValue = ConvertUtils.convert(sourceValue);
        }
        PropertyUtils.setProperty(binding.getComponent(), binding.getComponentProperty(), sourceValue);
    }

    private State getState(SwingMetawidget metawidget) {
        State state = (State) metawidget.getClientProperty(BeanUtilsBindingProcessor.class);
        if (state == null) {
            state = new State();
            metawidget.putClientProperty(BeanUtilsBindingProcessor.class, state);
        }
        return state;
    }

    private static class State {

        Set<SavedBinding> bindings;
    }

    private static class SavedBinding {

        private final Component mComponent;

        private final String mComponentProperty;

        private final String mNames;

        private final boolean mNoSetter;

        public SavedBinding(Component component, String componentProperty, String names, boolean noSetter) {
            mComponent = component;
            mComponentProperty = componentProperty;
            mNames = names;
            mNoSetter = noSetter;
        }

        public Component getComponent() {
            return mComponent;
        }

        public String getComponentProperty() {
            return mComponentProperty;
        }

        public String getNames() {
            return mNames;
        }

        public boolean isSettable() {
            return !mNoSetter;
        }
    }
}
