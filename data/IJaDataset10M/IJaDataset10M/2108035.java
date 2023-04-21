package net.sf.elbe.core.internal.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.sf.elbe.core.ELBECoreMessages;
import net.sf.elbe.core.events.EmptyValueAddedEvent;
import net.sf.elbe.core.events.EmptyValueDeletedEvent;
import net.sf.elbe.core.events.EntryModificationEvent;
import net.sf.elbe.core.events.EventRegistry;
import net.sf.elbe.core.events.ModelModifier;
import net.sf.elbe.core.events.ValueAddedEvent;
import net.sf.elbe.core.events.ValueDeletedEvent;
import net.sf.elbe.core.events.ValueModifiedEvent;
import net.sf.elbe.core.internal.search.LdapSearchPageScoreComputer;
import net.sf.elbe.core.model.IAttribute;
import net.sf.elbe.core.model.IConnection;
import net.sf.elbe.core.model.IEntry;
import net.sf.elbe.core.model.IValue;
import net.sf.elbe.core.model.ModelModificationException;
import net.sf.elbe.core.model.schema.AttributeTypeDescription;
import net.sf.elbe.core.model.schema.SchemaUtils;
import org.eclipse.search.ui.ISearchPageScoreComputer;

public class Attribute implements IAttribute {

    private static final long serialVersionUID = -5679384884002589786L;

    private AttributeDescription attributeDescription;

    private IEntry entry;

    private List valueList;

    protected Attribute() {
    }

    /**
	 * Creates an Attribute with the given description and no value for the given entry.
	 * 
	 * @param entry The entry of this attribute, mustn't be null
	 * @param description The attribute descrption, mustn't be null or empty.
	 * @throws ModelModificationException if the attribute name is null or empty.
	 */
    public Attribute(IEntry entry, String description) throws ModelModificationException {
        if (entry == null) {
            throw new ModelModificationException(ELBECoreMessages.model__empty_entry);
        }
        if (description == null) {
            throw new ModelModificationException(ELBECoreMessages.model__empty_attribute);
        }
        this.entry = entry;
        this.attributeDescription = new AttributeDescription(description);
        this.valueList = new ArrayList();
    }

    public IEntry getEntry() {
        return this.entry;
    }

    public boolean isConsistent() {
        if (this.valueList.isEmpty()) {
            return false;
        }
        for (Iterator it = this.valueList.iterator(); it.hasNext(); ) {
            IValue value = (IValue) it.next();
            if (value.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isMustAttribute() {
        if (this.isObjectClassAttribute()) {
            return true;
        } else {
            String[] mustAttributeNames = this.entry.getSubschema().getMustAttributeNames();
            for (int i = 0; i < mustAttributeNames.length; i++) {
                String must = mustAttributeNames[i];
                if (must.equalsIgnoreCase(this.getType())) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isMayAttribute() {
        return !isObjectClassAttribute() && !isMustAttribute() && !isOperationalAttribute();
    }

    public boolean isOperationalAttribute() {
        return getAttributeTypeDescription() == null || SchemaUtils.isOperational(getAttributeTypeDescription());
    }

    public boolean isObjectClassAttribute() {
        return OBJECTCLASS_ATTRIBUTE.equalsIgnoreCase(this.getDescription());
    }

    public boolean isString() {
        return !this.isBinary();
    }

    public boolean isBinary() {
        return this.getAttributeTypeDescription().isBinary();
    }

    public void addEmptyValue(ModelModifier source) {
        try {
            IValue emptyValue = new Value(this);
            this.valueList.add(emptyValue);
            this.attributeModified(new EmptyValueAddedEvent(this.entry.getConnection(), this.entry, this, emptyValue, source));
        } catch (ModelModificationException mme) {
        }
    }

    public void deleteEmptyValue(ModelModifier source) {
        for (Iterator it = this.valueList.iterator(); it.hasNext(); ) {
            IValue value = (IValue) it.next();
            if (value.isEmpty()) {
                it.remove();
                this.attributeModified(new EmptyValueDeletedEvent(this.entry.getConnection(), this.entry, this, value, source));
                return;
            }
        }
    }

    private void attributeModified(EntryModificationEvent event) {
        EventRegistry.fireEntryUpdated(event, this.getEntry());
    }

    private void checkValue(IValue value) throws ModelModificationException {
        if (value == null) {
            throw new ModelModificationException(ELBECoreMessages.model__empty_value);
        }
        if (!value.getAttribute().equals(this)) {
            throw new ModelModificationException(ELBECoreMessages.model__values_attribute_is_not_myself);
        }
    }

    private boolean deleteValue(IValue valueToDelete) {
        for (Iterator it = this.valueList.iterator(); it.hasNext(); ) {
            IValue value = (IValue) it.next();
            if (value.equals(valueToDelete)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public void addValue(IValue valueToAdd, ModelModifier source) throws ModelModificationException {
        this.checkValue(valueToAdd);
        this.valueList.add(valueToAdd);
        this.attributeModified(new ValueAddedEvent(this.entry.getConnection(), this.entry, this, valueToAdd, source));
    }

    public void deleteValue(IValue valueToDelete, ModelModifier source) throws ModelModificationException {
        this.checkValue(valueToDelete);
        if (this.deleteValue(valueToDelete)) {
            this.attributeModified(new ValueDeletedEvent(this.entry.getConnection(), this.entry, this, valueToDelete, source));
        }
    }

    public void modifyValue(IValue oldValue, IValue newValue, ModelModifier source) throws ModelModificationException {
        this.checkValue(oldValue);
        this.checkValue(newValue);
        this.deleteValue(oldValue);
        this.valueList.add(newValue);
        this.attributeModified(new ValueModifiedEvent(this.entry.getConnection(), this.entry, this, oldValue, newValue, source));
    }

    public IValue[] getValues() {
        return (IValue[]) this.valueList.toArray(new IValue[0]);
    }

    public int getValueSize() {
        return this.valueList.size();
    }

    public String getDescription() {
        return this.attributeDescription.getDescription();
    }

    public String getType() {
        return this.attributeDescription.getParsedAttributeType();
    }

    public String toString() {
        return this.getDescription();
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof IAttribute)) {
            return false;
        }
        IAttribute a = (IAttribute) o;
        if (!this.getEntry().equals(a.getEntry())) {
            return false;
        }
        return this.getDescription().equals(a.getDescription());
    }

    public int hashCode() {
        return this.getDescription().hashCode();
    }

    public byte[][] getBinaryValues() {
        List binaryValueList = new ArrayList();
        IValue[] values = this.getValues();
        for (int i = 0; i < values.length; i++) {
            binaryValueList.add(values[i].getBinaryValue());
        }
        return (byte[][]) binaryValueList.toArray(new byte[0][]);
    }

    public String getStringValue() {
        if (getValueSize() > 0) {
            return ((IValue) this.valueList.get(0)).getStringValue();
        } else {
            return null;
        }
    }

    public String[] getStringValues() {
        List stringValueList = new ArrayList();
        IValue[] values = this.getValues();
        for (int i = 0; i < values.length; i++) {
            stringValueList.add(values[i].getStringValue());
        }
        return (String[]) stringValueList.toArray(new String[stringValueList.size()]);
    }

    public AttributeTypeDescription getAttributeTypeDescription() {
        return getEntry().getConnection().getSchema().getAttributeTypeDescription(this.getType());
    }

    public Object getAdapter(Class adapter) {
        if (adapter.isAssignableFrom(ISearchPageScoreComputer.class)) {
            return new LdapSearchPageScoreComputer();
        }
        if (adapter == IConnection.class) {
            return this.getConnection();
        }
        if (adapter == IEntry.class) {
            return this.getEntry();
        }
        if (adapter == IAttribute.class) {
            return this;
        }
        return null;
    }

    public IConnection getConnection() {
        return this.entry.getConnection();
    }

    public IAttribute getAttribute() {
        return this;
    }

    public AttributeDescription getAttributeDescription() {
        return attributeDescription;
    }
}
