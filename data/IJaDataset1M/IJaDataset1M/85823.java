package mipt.crec.vaadin;

import mipt.reflect.DefaultFieldSetter;
import mipt.reflect.FieldSetter;
import mipt.util.log.Log;
import mipt.data.Data;
import com.vaadin.data.Property;

/**
 * {@link FormCreator} using {@link FieldSetter} implementation to set attributes to {@link BeanModelProperty}
 * (NOT USED NOW) and to any other objects (especially UI objects like Layouts and Fields).
 * This class can be considered a facade to AAF-reflection classes around {@link FieldSetter}.
 * The main thing remain abstract is determining the model.
 * @author Evdokimov
 */
public abstract class ReflectFormCreator extends FormCreator {

    private FieldSetter fieldSetter;

    public ReflectFormCreator(Object formId) {
        super(formId);
    }

    /**
	 * @return fieldSetter
	 */
    public final FieldSetter getFieldSetter() {
        if (fieldSetter == null) fieldSetter = initFieldSetter();
        return fieldSetter;
    }

    protected FieldSetter initFieldSetter() {
        return new DefaultFieldSetter();
    }

    /**
	 * @param fieldSetter
	 */
    public void setFieldSetter(FieldSetter fieldSetter) {
        this.fieldSetter = fieldSetter;
    }

    protected void setAttribute(Object object, String fieldName, Object fieldValue) {
        try {
            getFieldSetter().setValue(object, fieldName, DefaultFieldSetter.getSetterName(fieldName), fieldValue);
        } catch (Exception e) {
            Log.getLog().log(Log.WARNING, "Wrong UI attribute name for " + object.getClass().getSimpleName() + ": " + fieldName, e);
        }
    }

    protected Object createUIObject(String className) {
        try {
            if (className == null) return null;
            return createClass(className, "com.vaadin.ui.").newInstance();
        } catch (Exception e) {
            Log.getLog().log(Log.ERROR, "Wrong UI class name: " + className, e);
            return null;
        }
    }

    protected Class createClass(String className, String defaultPackage) throws ClassNotFoundException {
        if (className.indexOf('.') < 0) className = defaultPackage + className;
        return Class.forName(className);
    }

    /**
	 * Can be overridden to process the case when typeName is short (and the class is not in java.lang.).
	 * typeName can be null if the field is a Form
	 */
    protected Class getPropertyType(Data field) {
        String typeName = field.getString("type");
        if (typeName == null) return null;
        try {
            return createClass(typeName, "java.lang.");
        } catch (ClassNotFoundException e) {
            Log.getLog().log(Log.WARNING, "Wrong 'type' attribute for a " + field.getType() + ": " + typeName, e);
            return Object.class;
        }
    }

    /**
	 * @see mipt.crec.vaadin.FormCreator#createChildCreator(com.vaadin.data.Property, java.lang.Object)
	 */
    protected ReflectFormCreator createChildCreator(Object propertyId, Property property) {
        ReflectFormCreator child = createChildCreatorImpl(propertyId, property);
        if (fieldSetter != null) child.setFieldSetter(fieldSetter);
        return child;
    }

    /**
	 * Factory method.
	 * Do not use this.formId here (this would be needed if UI names were organized recursively).
	 */
    protected abstract ReflectFormCreator createChildCreatorImpl(Object propertyId, Property property);
}
