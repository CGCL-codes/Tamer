package org.amplafi.flow.flowproperty;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.NavigableMap;
import org.amplafi.flow.translator.CharSequenceFlowTranslator;
import org.amplafi.flow.translator.FlowTranslator;
import org.amplafi.flow.FlowPropertyDefinition;
import org.amplafi.flow.DataClassDefinition;
import org.amplafi.json.IJsonWriter;
import org.apache.commons.lang.builder.EqualsBuilder;
import com.sworddance.beans.PropertyDefinitionImpl;

public class DataClassDefinitionImpl extends PropertyDefinitionImpl implements DataClassDefinition {

    @SuppressWarnings("unchecked")
    private FlowTranslator flowTranslator;

    public static final DataClassDefinitionImpl DEFAULT;

    static {
        DEFAULT = new DataClassDefinitionImpl(String.class);
        DEFAULT.setFlowTranslator(CharSequenceFlowTranslator.INSTANCE);
    }

    public DataClassDefinitionImpl() {
    }

    /**
     * Used to define Map structures that have complex keys.
     * @param mapClass
     * @param keyClassDefinition
     * @param elementClassDefinition
     */
    @SuppressWarnings("unchecked")
    public DataClassDefinitionImpl(Class<? extends Map> mapClass, DataClassDefinition keyClassDefinition, DataClassDefinition elementClassDefinition) {
        super(mapClass, keyClassDefinition, elementClassDefinition);
    }

    /**
     * clone ctor
     * @param dataClassDefinition
     */
    public DataClassDefinitionImpl(DataClassDefinition dataClassDefinition) {
        super(dataClassDefinition.getPropertyClass(), dataClassDefinition.isKeyPropertyDefinitionSet() ? new DataClassDefinitionImpl(dataClassDefinition.getKeyPropertyDefinition()) : null, dataClassDefinition.isElementPropertyDefinitionSet() ? new DataClassDefinitionImpl(dataClassDefinition.getElementPropertyDefinition()) : null);
        if (dataClassDefinition.isFlowTranslatorSet()) {
            this.flowTranslator = dataClassDefinition.getFlowTranslator();
        }
    }

    public DataClassDefinitionImpl(Class<?> element, Class<?>... collections) {
        if (collections.length == 0) {
            this.setDataClass(element);
        } else if (collections.length == 1) {
            this.setDataClass(collections[0]);
            this.setElementDataClassDefinition(new DataClassDefinitionImpl(element));
        } else {
            this.setDataClass(collections[0]);
            this.setElementDataClassDefinition(new DataClassDefinitionImpl(element, Arrays.copyOfRange(collections, 1, collections.length)));
        }
    }

    /**
     * Helper to define a map.
     * @param keyClass
     * @param elementClass
     * @param collectionClasses
     * @return a {@link DataClassDefinitionImpl} that defines a {@link Map} property.
     */
    public static DataClassDefinitionImpl map(Class<?> keyClass, Class<?> elementClass, Class<?>... collectionClasses) {
        return new DataClassDefinitionImpl(Map.class, new DataClassDefinitionImpl(keyClass), new DataClassDefinitionImpl(elementClass, collectionClasses));
    }

    /**
     * Helper to define a NavigableMap (TreeMap for example)
     * @param keyClass
     * @param elementClass
     * @param collectionClasses
     * @return a {@link DataClassDefinitionImpl} that defines a {@link NavigableMap} property.
     */
    public static DataClassDefinitionImpl navigableMap(Class<?> keyClass, Class<?> elementClass, Class<?>... collectionClasses) {
        return new DataClassDefinitionImpl(NavigableMap.class, new DataClassDefinitionImpl(keyClass), new DataClassDefinitionImpl(elementClass, collectionClasses));
    }

    /**
     * Helper to define a map.
     * @param keyClass
     * @param elementDataClassDefinition
     * @return a {@link DataClassDefinitionImpl} that defines a {@link Map} property.
     */
    public static DataClassDefinitionImpl map(Class<?> keyClass, DataClassDefinitionImpl elementDataClassDefinition) {
        return new DataClassDefinitionImpl(Map.class, new DataClassDefinitionImpl(keyClass), elementDataClassDefinition);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(FlowPropertyProvider flowPropertyProvider, FlowPropertyDefinition flowPropertyDefinition, Object value) {
        if (!this.isFlowTranslatorSet() && this.getDataClass().isInstance(value)) {
            return (T) value;
        }
        return (T) this.getFlowTranslator().deserialize(flowPropertyProvider, flowPropertyDefinition, this, value);
    }

    @Override
    public <T> Object serialize(FlowPropertyDefinition flowPropertyDefinition, T value) {
        if (value == null) {
            return null;
        } else {
            IJsonWriter jsonWriter = this.serialize(flowPropertyDefinition, null, value);
            String strV = jsonWriter.toString();
            if (strV != null && strV.startsWith("\"") && strV.endsWith("\"")) {
                strV = strV.substring(1, strV.length() - 1);
            }
            return strV;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> IJsonWriter serialize(FlowPropertyDefinition flowPropertyDefinition, IJsonWriter jsonWriter, T value) {
        return this.getFlowTranslator().serialize(flowPropertyDefinition, this, jsonWriter, value);
    }

    /**
     * @param dataClassDefinition
     */
    public void merge(DataClassDefinition dataClassDefinition) {
        if (dataClassDefinition == null) {
            return;
        }
        if (getDataClassReplaced(dataClassDefinition) == Boolean.TRUE) {
            this.setPropertyClass(dataClassDefinition.getPropertyClass());
        }
        this.setElementDataClassDefinition(mergeIt(this.getElementPropertyDefinition(), dataClassDefinition.getElementPropertyDefinition()));
        this.setKeyDataClassDefinition(mergeIt(this.getKeyPropertyDefinition(), dataClassDefinition.getKeyPropertyDefinition()));
    }

    /**
     * @param dataClassDefinition
     */
    private DataClassDefinition mergeIt(DataClassDefinition original, DataClassDefinition dataClassDefinition) {
        if (dataClassDefinition != null) {
            if (original == null) {
                return new DataClassDefinitionImpl(dataClassDefinition);
            } else {
                original.merge(dataClassDefinition);
            }
        }
        return original;
    }

    @Override
    public boolean isMergable(DataClassDefinition dataClassDefinition) {
        if (equals(dataClassDefinition) || dataClassDefinition == null) {
            return true;
        } else if (dataClassDefinition.isDataClassDefined() && getDataClassReplaced(dataClassDefinition) == null) {
            return false;
        } else if (this.isElementPropertyDefinitionSet() && !this.getElementPropertyDefinition().isMergable(dataClassDefinition.getElementPropertyDefinition())) {
            return false;
        } else if (this.isKeyPropertyDefinitionSet() && !this.getKeyPropertyDefinition().isMergable(dataClassDefinition.getKeyPropertyDefinition())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param dataClassDefinition
     * @return
     */
    private Boolean getDataClassReplaced(DataClassDefinition dataClassDefinition) {
        if (dataClassDefinition == null || this.isSameDataClass(dataClassDefinition) || !dataClassDefinition.isDataClassDefined()) {
            return false;
        } else if (!this.isDataClassDefined()) {
            return true;
        } else {
            return this.isAssignableFrom(dataClassDefinition) ? true : null;
        }
    }

    /**
     * if this represents a collection then the {@link #elementPropertyDefinition} will not be null.
     * @return the collection
     */
    public Class<?> getCollection() {
        return this.isCollection() ? this.getDataClass() : null;
    }

    public Class<?> getElementClass() {
        return this.isCollection() ? this.getElementDataClassDefinition().getElementClass() : this.getDataClass();
    }

    public Class<?> getKeyClass() {
        return this.isCollection() && this.getKeyDataClassDefinition() != null ? this.getKeyDataClassDefinition().getDataClass() : null;
    }

    /**
     * @param dataClass the dataClass to set
     */
    public void setDataClass(Class<?> dataClass) {
        super.setPropertyClass(dataClass != String.class && dataClass != CharSequence.class ? dataClass : null);
        this.flowTranslator = null;
    }

    /**
     * @return the dataClass
     */
    @Override
    public Class<?> getDataClass() {
        if (super.getPropertyClass() != null) {
            return super.getPropertyClass();
        } else if (isMap()) {
            return Map.class;
        } else if (isCollection()) {
            return Collection.class;
        } else {
            return String.class;
        }
    }

    public boolean isDataClassDefined() {
        return super.isPropertyClassDefined();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o == null || !(o instanceof DataClassDefinitionImpl)) {
            return false;
        }
        DataClassDefinitionImpl dataClassDefinition = (DataClassDefinitionImpl) o;
        EqualsBuilder equalsBuilder = new EqualsBuilder().append(this.getDataClass(), dataClassDefinition.getDataClass()).append(this.getElementPropertyDefinition(), dataClassDefinition.getElementPropertyDefinition()).append(this.getKeyPropertyDefinition(), dataClassDefinition.getKeyPropertyDefinition()).append(this.getFlowTranslator(), dataClassDefinition.getFlowTranslator());
        return equalsBuilder.isEquals();
    }

    /**
     * @param keyDataClassDefinition the keyPropertyDefinition to set
     */
    public void setKeyDataClassDefinition(DataClassDefinition keyDataClassDefinition) {
        this.setKeyPropertyDefinition(keyDataClassDefinition);
    }

    /**
     * @return the keyPropertyDefinition
     */
    public DataClassDefinition getKeyDataClassDefinition() {
        if (isKeyPropertyDefinitionSet()) {
            return getKeyPropertyDefinition();
        } else if (isMap()) {
            return DEFAULT;
        } else {
            return null;
        }
    }

    /**
     * @param elementDataClassDefinition the elementPropertyDefinition to set
     */
    public void setElementDataClassDefinition(DataClassDefinition elementDataClassDefinition) {
        this.setElementPropertyDefinition(elementDataClassDefinition);
    }

    /**
     * @return the elementPropertyDefinition
     */
    public DataClassDefinition getElementDataClassDefinition() {
        if (isElementPropertyDefinitionSet()) {
            return getElementPropertyDefinition();
        } else if (isCollection()) {
            return DEFAULT;
        } else {
            return null;
        }
    }

    @Override
    public DataClassDefinitionImpl getKeyPropertyDefinition() {
        return (DataClassDefinitionImpl) super.getKeyPropertyDefinition();
    }

    @Override
    public DataClassDefinitionImpl getElementPropertyDefinition() {
        return (DataClassDefinitionImpl) super.getElementPropertyDefinition();
    }

    /**
     * @param flowTranslator the flowTranslator to set
     */
    @SuppressWarnings("unchecked")
    public void setFlowTranslator(FlowTranslator flowTranslator) {
        this.flowTranslator = flowTranslator;
    }

    /**
     * @return the flowTranslator
     */
    @SuppressWarnings("unchecked")
    public FlowTranslator getFlowTranslator() {
        if (this.flowTranslator == null) {
            return CharSequenceFlowTranslator.INSTANCE;
        } else {
            return flowTranslator;
        }
    }

    /**
     * @param clazz
     * @return true if objects of 'clazz' can be stored in the structure defined by this.
     */
    @SuppressWarnings("unchecked")
    public boolean isAssignableFrom(Class<?> clazz) {
        return getFlowTranslator().isAssignableFrom(clazz);
    }

    @Override
    public String toString() {
        StringBuilder name = new StringBuilder(this.getDataClass().getName());
        if (isCollection()) {
            name.append("<");
            if (this.isKeyPropertyDefinitionSet() || Map.class.isAssignableFrom(this.getDataClass())) {
                name.append(this.getKeyDataClassDefinition()).append(", ");
            }
            if (isCollection()) {
                name.append(this.getElementDataClassDefinition());
            }
            name.append(">");
        }
        return name.toString();
    }

    public boolean isCollection() {
        return this.isElementPropertyDefinitionSet() || isMap() || (this.isDataClassDefined() && Collection.class.isAssignableFrom(super.getPropertyClass()));
    }

    /**
     * @return this represents a map
     */
    public boolean isMap() {
        return this.isKeyPropertyDefinitionSet() || (this.isDataClassDefined() && Map.class.isAssignableFrom(super.getPropertyClass()));
    }

    /**
     * @param flowPropertyDefinition
     * @param value
     * @return true if value can be deserialized
     */
    public boolean isDeserializable(FlowPropertyDefinition flowPropertyDefinition, Object value) {
        return value == null || this.getFlowTranslator().isDeserializable(flowPropertyDefinition, this, value);
    }

    /**
     * @return true if the flowTranslator is set or {@link #getDataClass()} == String.class
     */
    public boolean isFlowTranslatorSet() {
        return this.flowTranslator != null || this.getDataClass() == String.class;
    }
}
