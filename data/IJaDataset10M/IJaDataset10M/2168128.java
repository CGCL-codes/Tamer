package org.qsari.effectopedia.core.embeddedobjects;

import java.util.HashMap;
import org.jdom.Element;
import org.jdom.Namespace;
import org.qsari.effectopedia.base.EffectopediaObject;
import org.qsari.effectopedia.base.XMLExportable;
import org.qsari.effectopedia.base.XMLImportable;
import org.qsari.effectopedia.data.XMLFileDS;
import org.qsari.effectopedia.defaults.DefaultTextProperties;
import org.qsari.effectopedia.search.SearchableItem;

public class ObjectProperty implements XMLImportable, XMLExportable, Cloneable {

    public ObjectProperty(EffectopediaObject owner, ObjectPropertyType type) {
        super();
        this.owner = owner;
        this.type = type;
        this.unit = (type != null) ? type.defaultUnit : null;
    }

    public void cloneFieldsTo(ObjectProperty clone) {
        clone.type = this.type;
        clone.value = (value != null) ? this.value.clone() : null;
        clone.unit = (unit != null) ? this.unit.clone() : null;
    }

    public void getContainedIDs(HashMap<Long, EffectopediaObject> containedIDs) {
        type.getContainedIDs(containedIDs);
    }

    public void getContainedExternalIDs(HashMap<Long, EffectopediaObject> containedIDs) {
        type.getContainedExternalIDs(containedIDs);
    }

    public ObjectProperty clone() {
        ObjectProperty clone = new ObjectProperty(this.owner, this.type);
        cloneFieldsTo(clone);
        return clone;
    }

    public ObjectProperty clone(EffectopediaObject owner) {
        ObjectProperty clone = new ObjectProperty(owner, this.type);
        cloneFieldsTo(clone);
        return clone;
    }

    public void loadFromXMLElement(Element element, Namespace namespace) {
        if (element != null) {
            Element type = element.getChild("type", namespace);
            if (type != null) this.type = (ObjectPropertyType) XMLFileDS.load(ObjectPropertyType.class, null, type, namespace);
            Element value = element.getChild("value", namespace);
            if ((value != null) && (value.getText().length() != 0)) {
                if (this.value == null) this.value = DataValue.newValue(getSearchableItem(), this.type.baseValueType, this.type.fixedValuesList);
                this.value.loadFromXMLElement(value, namespace);
            }
            Element unit = element.getChild("unit", namespace);
            if (unit != null) if (this.unit == null) this.unit = new DataUnit(unit.getChildText("caption", namespace)); else this.unit.setCaption(unit.getChildText("caption", namespace));
        }
    }

    public void updateExternalIDFromXMLElement(Element element, Namespace namespace) {
        type.updateExternalIDFromXMLElement(element.getChild("type", namespace), namespace);
    }

    public Element storeToXMLElement(Element element, Namespace namespace, boolean visualAttributes) {
        if (type != null) element.addContent(type.storeToXMLElement(new Element("type", namespace), namespace, visualAttributes));
        if (value != null) element.addContent(value.storeToXMLElement(new Element("value", namespace), namespace, visualAttributes));
        if (unit != null) element.addContent(unit.storeToXMLElement(new Element("unit", namespace), namespace, visualAttributes));
        return element;
    }

    public DataValue<?> getValue() {
        return value;
    }

    public String getDisplayValue() {
        if (value == null) return DefaultTextProperties.INSTANCE.getDefault(type.getFullName()); else return value.getDisplayValue();
    }

    public String getDisplayUnit() {
        if (unit == null) return " "; else return unit.getCaption();
    }

    public SearchableItem getSearchableItem() {
        if ((type.searchable) && (searchableItem == null)) searchableItem = new SearchableItem(owner, type.propertyID, type.searchName);
        return searchableItem;
    }

    public void setSearchableItem(SearchableItem searchableItem) {
        this.searchableItem = searchableItem;
    }

    public EffectopediaObject getOwner() {
        return owner;
    }

    public void setValue(DataValue<?> value) {
        if (((value == null) && (this.value != null)) || (!value.equals(this.value))) {
            owner.beforeUpdate(true, type.actionTypeID);
            this.value = value;
        }
    }

    public void setValue(String value) {
        boolean different = ((value == null) && (this.value != null)) || (this.value == null) && (value != null);
        if ((value != null) && (this.value != null)) different = !(value.equals(this.value.toString()));
        if (different) {
            owner.beforeUpdate(true, type.actionTypeID);
            if (value != null) {
                if (this.value == null) this.value = DataValue.newValue(getSearchableItem(), type.baseValueType, type.fixedValuesList);
                this.value.parseString(value);
            } else this.value = null;
        }
    }

    public DataUnit getUnit() {
        return unit;
    }

    public void setUnit(DataUnit unit) {
        this.unit = unit;
    }

    public void setUnit(String unit) {
        boolean different = ((unit == null) && (this.unit != null)) || (this.unit == null) && (unit != null);
        if ((unit != null) && (this.unit != null)) different = !(unit.equals(this.unit.getCaption()));
        if (different) {
            owner.beforeUpdate(true, type.actionTypeID);
            if (this.unit == null) this.unit = new DataUnit(unit); else this.unit.setCaption(unit);
        }
    }

    protected ObjectPropertyType type;

    protected SearchableItem searchableItem = null;

    protected EffectopediaObject owner;

    protected DataValue<?> value;

    protected DataUnit unit;
}
