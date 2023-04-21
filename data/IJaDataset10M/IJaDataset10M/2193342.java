package forms.field;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import forms.AnnotationUtilities;
import forms.ReflectionException;

public class FieldDescriptionFactory {

    private static FieldDescriptionFactory instance = new FieldDescriptionFactory();

    public FieldDescriptionFactory() {
    }

    public static FieldDescriptionFactory getInstance() {
        return instance;
    }

    public static List<FieldDescription> createFieldDescriptions(Class<?> clazz) {
        try {
            List<FieldDescription> props = new ArrayList<FieldDescription>();
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            for (PropertyDescriptor prop : beanInfo.getPropertyDescriptors()) {
                if (prop.getName().equals("class")) {
                } else {
                    PropertyUI desc = AnnotationUtilities.getAnnotation(PropertyUI.class, prop, clazz);
                    if (desc != null) {
                        if (desc.propertyEditorClass() != null && !desc.propertyEditorClass().isEmpty()) {
                            Class<?> propEditorClass = Class.forName(desc.propertyEditorClass());
                            prop.setPropertyEditorClass(propEditorClass);
                        }
                        if (!desc.name().equals("")) {
                            prop.setDisplayName(desc.name());
                        }
                        if (!desc.description().equals("")) {
                            prop.setShortDescription(desc.description());
                        }
                        prop.setHidden(!desc.visible());
                        prop.setValue(FieldDescriptionConstants.SHOW_LABEL, desc.showLabel());
                        prop.setValue(FieldDescriptionConstants.INDEX, desc.index());
                        prop.setValue(FieldDescriptionConstants.EDITABLE, desc.editable());
                    } else {
                        String name = prop.getName();
                        prop.setValue(FieldDescriptionConstants.INDEX, Integer.MAX_VALUE);
                        name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                        prop.setDisplayName(name);
                    }
                    props.add(getFieldDescription(clazz, prop));
                }
            }
            System.out.println("------------------");
            for (FieldDescription fieldDescription : props) {
                System.out.println(fieldDescription.getPropertyDescription().getName() + fieldDescription.getPropertyDescription().getValue(FieldDescriptionConstants.INDEX));
            }
            System.out.println("Sortiere!!!");
            Collections.sort(props, new PropsComperator());
            for (FieldDescription fieldDescription : props) {
                System.out.println(fieldDescription.getPropertyDescription().getName() + fieldDescription.getPropertyDescription().getValue(FieldDescriptionConstants.INDEX));
            }
            System.out.println("------------------");
            return props;
        } catch (Exception e) {
            throw new ReflectionException(e, "Fehler beim laden der Felder.");
        }
    }

    private static class PropsComperator implements Comparator<FieldDescription> {

        @Override
        public int compare(FieldDescription desc1, FieldDescription desc2) {
            Integer o1 = (Integer) desc1.getPropertyDescription().getValue(FieldDescriptionConstants.INDEX);
            Integer o2 = (Integer) desc2.getPropertyDescription().getValue(FieldDescriptionConstants.INDEX);
            return o1.intValue() - o2.intValue();
        }
    }

    private static FieldDescription getPredefinedDescription(Class<?> clazz, String propName) {
        return null;
    }

    private static FieldDescription getFieldDescription(Class<?> clazz, PropertyDescriptor prop) throws InstantiationException, IllegalAccessException {
        FieldDescription ret = getPredefinedDescription(clazz, prop.getName());
        if (ret == null) {
            ret = createFieldDescription(clazz, prop);
        }
        return ret;
    }

    private static FieldDescription createFieldDescription(Class<?> clazz, PropertyDescriptor prop) throws InstantiationException, IllegalAccessException {
        FieldDescription ret = new FieldDescription(clazz, prop);
        return ret;
    }
}
