package mipt.crec.vaadin;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import mipt.data.Data;
import mipt.data.MutableData;
import mipt.reflect.DefaultFieldSetter;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * Unlike superclass, can produce not only Form but ComponentContainer (any, but Panel is not reasonable).
 *   ModelProperty is created in both cases but the model for the container case is null.
 *   By default, containers can contain forms/subcontainers only (do not contain fields immediately).
 * The class assumes the following Data structure:
 * form_or_container_id (the id of root container (= rootData.getType() = root element name) does not matter)
 * |- [isContainer] (true or false; if absent then false - this is a form)
 * |- [surround] (supported only for forms and fields now, although it's not hard to support for containers)
 * | |- [caption] (if absent, the data type (element name) is used as caption key to get from UI bundle; "none" or "NONE" means no caption)
 * | |- [border] (String: "none" or some other style name suffix (see {@link FormCreator#getBorderStyleSuffix()}))
 * |- [layout] (if omitted for a form, FormLayout will be used; can't be omitted for containers: this is info to create the container itself)
 * | |- class (full class name of a ComponentContainer or short class name if the class in com.vaadin.ui; e.g. GridLayout or TabSheet)
 * | |- [columns] (for GridLayout)
 * | |- [rows] (for -//-)
 * |- [some_field_id] (id is usually used as the key for UI name; the behavior is the same for forms and containers, not only for fields)
 * | |- [surround] (see above)
 * | |- type (full class name of a Field or short class name if the class is in java.lang.; e.g. String or java.util.Date) 
 * | |- [readOnly=true] (false by default)
 * | |- gridX (for GridLayout only)
 * | |- gridY (for GridLayout or AbstractOrderedLayouts including default FormLayout)
 * | |- gridWidth (for GridLayout only)
 * | |- gridHeight (for GridLayout only)
 * |- ... 
 * |- [some_subform_or_subcontainer_id] (i.e. everything without type attribute and with name not in {isContainer,surround,layout})
 * | |- [layout] (see above)
 * | |- ... (see above; all attributes of fields except type is usable here)
 * A class mentioned in field "class" should have public default constructor.
 * The automatic field setting (inherited from {@link ReflectFormCreator}, based on {@link DefaultFieldSetter}),
 *  is applied for Layouts but it's NOT USED to set fields to (Bean)ModelProperty.
 * Note: instead of the format above, we could use mipt.aaf.edit.form.data.ObjectByDataCreator
 *  but 1) its format would be more complicated for user editing; 2) the overall UI creation process could not
 *  be coded in Java; 3) the ability to "save" object state to Data (DataByObjectCreator) is not needed.
 *  
 *  Getting field values by name is delegated to Map model (note: the model is typically specific 
 *    - i.e. not the same source as metadata source like in AAF forms!).
 *   Nevertheless the model does not define the set of fields in the form: this is defined
 *    by metadata passed in constructor (field names - propertyIds - must exist in the model).
 *  
 * The main thing remain abstract is determining the model (as the source of metadata it not known);
 *  see also {@link #initModel(Data)}.
 * Subclass can also limit a set if Data fields converted to properties in {@link #getPropertyNames()}.
 * @author Evdokimov
 */
public abstract class DataFormCreator extends ReflectFormCreator {

    private DataModelProperty property;

    protected ComponentContainer container;

    /**
	 * A wrapper to the model's Data (which must be MutableData too) to be used in every {@link ModelProperty}.
	 * The model should be used by FormCreators if the model is contained in a single Data.
	 * Subclass can also limit a set if Data fields in {@link #getFieldNames()}
	 *  or combine models for several Datas.
	 */
    public static class Model extends MapModel {

        protected Data data;

        public Model(Data data) {
            this.data = data;
        }

        /**
		 * @see mipt.crec.vaadin.MapModel#put(java.lang.Object, java.lang.Object)
		 */
        public Object put(Object field, Object value) {
            ((MutableData) data).set(value, field.toString());
            return value;
        }

        /**
		 * @see mipt.crec.vaadin.MapModel#get(java.lang.Object)
		 */
        public final Object get(Object field) {
            if (field == null) return null;
            return getFieldValue(field.toString());
        }

        protected Object getFieldValue(String fieldName) {
            return data.get(fieldName);
        }

        protected LinkedList<String> getFieldNames() {
            LinkedList<String> list = new LinkedList<String>();
            for (String fieldName : data.getFieldNames()) {
                list.add(fieldName);
            }
            return list;
        }

        @Override
        public Set<Entry> entrySet() {
            HashSet<Entry> set = new HashSet<Entry>();
            for (String fieldName : getFieldNames()) {
                set.add(new Entry(fieldName, getFieldValue(fieldName)));
            }
            return set;
        }

        /**
		 * Copied from HashMap.Entry (next and hash attributes are removed)
		 */
        public static class Entry<K, V> implements Map.Entry<K, V> {

            final K key;

            V value;

            public Entry(K k, V v) {
                value = v;
                key = k;
            }

            public final K getKey() {
                return key;
            }

            public final V getValue() {
                return value;
            }

            public final V setValue(V newValue) {
                V oldValue = value;
                value = newValue;
                return oldValue;
            }

            public final boolean equals(Object o) {
                if (!(o instanceof Map.Entry)) return false;
                Map.Entry e = (Map.Entry) o;
                Object k1 = getKey();
                Object k2 = e.getKey();
                if (k1 == k2 || (k1 != null && k1.equals(k2))) {
                    Object v1 = getValue();
                    Object v2 = e.getValue();
                    if (v1 == v2 || (v1 != null && v1.equals(v2))) return true;
                }
                return false;
            }

            public final int hashCode() {
                return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
            }

            public final String toString() {
                return getKey() + "=" + getValue();
            }
        }
    }

    /**
	 * @param property Can't be null (even for root creator) since is used for get metadata.
	 */
    public DataFormCreator(Object formId, DataModelProperty property) {
        super(formId);
        this.property = property;
    }

    /**
	 * May be overridden to avoid having inconvenient isContainer attribute
	 */
    public boolean isContainer() {
        return getMetaData().getBoolean("isContainer");
    }

    /**
	 * @return metaData
	 */
    public final Data getMetaData() {
        return property.getMetaData();
    }

    /**
	 * @return property
	 */
    public final DataModelProperty getProperty() {
        return this.property;
    }

    /**
	 * Initializes the object by fields of the given data.
	 * If the object is null, creates it from the special field "class" of the data.
	 * @param object Object to fill with field values; can be null
	 * @param data Can be null
	 * @return Can be null
	 */
    protected Object initUIObject(Object object, Data data) {
        if (object == null) {
            if (data == null) return null;
            object = createUIObject(data.getString("class"));
        }
        if (object == null || data == null) return null;
        String fieldNames[] = data.getFieldNames();
        for (String name : fieldNames) {
            if (name.equals("class") || name.equals("surround")) continue;
            setAttribute(object, name, data.get(name));
        }
        return object;
    }

    @Override
    protected String getBorderStyleSuffix(Property property, Object propertyId) {
        Data c = getSurround(property);
        return c == null ? null : c.getString("border");
    }

    @Override
    protected String getCaption(Property property, Object propertyId) {
        String caption = getSurroundCaption(property);
        if (caption == null) return super.getCaption(property, propertyId);
        if (caption.equalsIgnoreCase("none")) return null;
        return caption;
    }

    protected final String getSurroundCaption(Property property) {
        Data c = getSurround(property);
        return c == null ? null : c.getString("caption");
    }

    protected final Data getSurround(Property property) {
        if (property instanceof DataModelProperty) return ((DataModelProperty) property).getMetaData("surround");
        return null;
    }

    /**
	 * @see mipt.crec.vaadin.FormCreator#initProperties()
	 */
    protected Iterable<Property> initProperties() {
        LinkedList<Property> list = new LinkedList<Property>();
        Map model = isContainer() ? null : initModel();
        for (String id : getPropertyIds()) {
            list.add(initProperty(id, model));
        }
        return list;
    }

    /**
	 * Usually overridden to remove unnecessary fields. By default removes only isContainer, layout and surround.
	 */
    protected Iterable<String> getPropertyIds() {
        LinkedList<String> list = new LinkedList<String>();
        for (String id : getMetaData().getFieldNames()) {
            if (isNotProperty(id)) continue;
            list.add(id);
        }
        return list;
    }

    /**
	 * Returns true for all names used by this class as metadata options (UI attributes).
	 * I.e. returns false for those metadata fields which are for actual components (fields or forms, creating from Properties).
	 */
    protected boolean isNotProperty(String id) {
        if (id.startsWith("grid")) {
            return id.equals("gridX") || id.equals("gridY") || id.equals("gridWidth") || id.equals("gridHeight");
        }
        return id.equals("layout") || id.equals("isContainer") || id.equals("surround") || id.equals("readOnly") || id.equals("type");
    }

    /**
	 * Called for fields, forms and even containers (but not for layout pseudo-field).
	 * By default all the properties created is DataModelProperty.
	 */
    protected Property initProperty(String id, Map model) {
        Data metaData = getMetaData().getData(id);
        AbstractProperty p = new DataModelProperty(id, model == null ? null : getPropertyType(metaData), model, metaData);
        p.setReadOnly(metaData.getBoolean("readOnly"));
        return p;
    }

    /**
	 * {@link Model} class is recommended to instantiate here.
	 */
    protected abstract Map initModel();

    /**
	 * For use in {@link #initModel()}
	 */
    protected Model initModel(Data data) {
        return new Model(data);
    }

    /**
	 * @see mipt.crec.vaadin.FormCreator#initLayout()
	 */
    protected Layout initLayout() {
        Layout layout = (Layout) createContainer("layout");
        if (layout == null) {
            if (getMetaData().get("array") == null) {
                layout = new FormLayout();
            } else {
                layout = new VerticalLayout();
            }
        }
        layout.setMargin(false, true, false, true);
        return layout;
    }

    protected ComponentContainer createContainer(String attribute) {
        return (ComponentContainer) initUIObject(null, getMetaData().getData(attribute));
    }

    public ComponentContainer initContainerView() {
        this.container = createContainer("layout");
        for (Property p : initProperties()) {
            Object propertyId = ((ModelProperty) p).getId();
            FormCreator creator = createChildCreator(propertyId, p);
            Component c = creator.initView();
            if (c instanceof Field) initField((Field) c, p, propertyId);
            addComponent(container, p, c);
            if (c instanceof Form || c instanceof ComponentContainer) {
                setMaxSize(c);
            }
        }
        container.setSizeFull();
        return container;
    }

    public Form initFormView() {
        return (Form) super.initView();
    }

    @Override
    public Component initView() {
        if (isContainer()) {
            return initContainerView();
        } else {
            return initFormView();
        }
    }

    /**
	 * Can return not null only called after initView()
	 */
    public final ComponentContainer getContainer() {
        return container;
    }

    /**
	 * @see ReflectFormCreator#createChildCreatorImpl(Object, Property)
	 */
    protected DataFormCreator createChildCreatorImpl(Object propertyId, Property property) {
        return createChildCreator(propertyId, (DataModelProperty) property);
    }

    /**
	 * Factory method (can't be implemented for forms because the model is not known).
	 * Do not use this.formId here (this would be needed if UI names were organized recursively).
	 */
    protected DataFormCreator createChildCreator(Object propertyId, DataModelProperty childProperty) {
        if (childProperty.getMetaData().getBoolean("isContainer")) return new DataFormCreator(propertyId, childProperty) {

            protected Map initModel() {
                return null;
            }
        };
        throw new UnsupportedOperationException("Override DataFormCreator.createChildCreator to support subforms");
    }

    /**
	 * This overriding solves the almost impossible problem (b) described in {@link #getUIItems(Class, Object)}
	 *  at the price of adding "itemsAreNames" boolean attribute to field attributes.
	 */
    @Override
    protected Object[] getUIItems(Class type, Object propertyId) {
        if (getMetaData().getData(propertyId.toString()).getBoolean("itemsAreNames")) return getItems(type, propertyId);
        return super.getUIItems(type, propertyId);
    }

    /**
	 * We can't remain it for specific subclass because the fields with one name often have different items;
	 *  so the items should be set in metadata.
	 */
    @Override
    protected Object[] getItems(Class type, Object propertyId) {
        return (Object[]) getMetaData().getData(propertyId.toString()).get("items");
    }

    /**
	 * Array fields are denoted by Data type.
	 * Do not forget to override {@link #createArrayContainer(Property, Object)} in subclass!
	 */
    @Override
    protected boolean isArray(Property property, Class type, Object propertyId) {
        return Data.class.isAssignableFrom(type);
    }
}
