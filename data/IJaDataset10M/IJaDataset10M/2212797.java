package org.aplikator.server.descriptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.aplikator.client.data.ListItem;
import org.aplikator.client.data.Record;
import org.aplikator.client.descriptor.PropertyDTO;
import org.aplikator.server.Context;
import org.aplikator.server.util.Configurator;
import org.aplikator.shared.data.BinaryData;

public class Property<T extends Serializable> extends ServerDescriptorBase implements Cloneable {

    private final Class<T> type;

    private final Entity entity;

    private Reference<? extends Entity> refferedThrough;

    private ListProvider<T> listProvider;

    private final double size;

    private final boolean required;

    private boolean editable;

    public Property(String id, Class<T> type, double size, boolean required, Entity entity) {
        super(id);
        this.type = type;
        this.entity = entity;
        this.size = size;
        this.required = required;
        this.setEditable(true);
    }

    public Class<T> getType() {
        return type;
    }

    public PropertyDTO<T> clientClone(Context ctx) {
        PropertyDTO<?> clientRefThrough = null;
        if (refferedThrough != null) {
            clientRefThrough = refferedThrough.clientClone(ctx);
        }
        PropertyDTO<T> retval = new PropertyDTO<T>(getId(), getLocalizedName(ctx), getType().getName(), getSize(), clientRefThrough, editable);
        if (listProvider != null) {
            List<ListItem<T>> clientListValues = new ArrayList<ListItem<T>>(listProvider.getListValues().size());
            for (ListItem<T> item : listProvider.getListValues()) {
                ListItem<T> clientItem = new ListItem.Default<T>(item.getValue(), Configurator.get().getLocalizedString(item.getName(), ctx.getUserLocale()));
                clientListValues.add(clientItem);
            }
            retval.setListValues(clientListValues);
        }
        return retval;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Property<T> clone() throws CloneNotSupportedException {
        return (Property<T>) super.clone();
    }

    public Entity getEntity() {
        return entity;
    }

    public ListProvider<T> getListProvider() {
        return listProvider;
    }

    public Property<T> setListProvider(ListProvider<T> listProvider) {
        this.listProvider = listProvider;
        return this;
    }

    public double getSize() {
        return size;
    }

    public boolean isRequired() {
        return required;
    }

    public Property<T> setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }

    /**
     * Creates the default widget for this property, based on its type.
     *
     * @return The default widget or null (for unsupported types, currently
     *         Reference and Collection) TODO - add support for references and
     *         collections, avoid infinite recursion
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Widget widget() {
        Widget retval = null;
        if (listProvider != null) {
            retval = new ComboBox(this);
        } else if (type.equals(Boolean.class)) {
            retval = new CheckBox((Property<Boolean>) this);
        } else if (type.equals(Date.class)) {
            retval = new DateField((Property<Date>) this);
        } else if (type.equals(BinaryData.class)) {
            retval = new BinaryField((Property<BinaryData>) this);
        } else if (type.equals(Date.class)) {
            retval = new DateField((Property<Date>) this);
        } else if (this instanceof Reference) {
            retval = null;
        } else if (this instanceof Collection) {
            retval = null;
        } else if (type.equals(String.class) && size == 0) {
            retval = new TextArea((Property<String>) this);
        } else {
            retval = new TextField(this);
        }
        return retval;
    }

    public Reference<? extends Entity> getRefferedThrough() {
        return refferedThrough;
    }

    void setRefferedThrough(Reference<? extends Entity> refferedThrough) {
        this.refferedThrough = refferedThrough;
    }

    String getRecordId() {
        if (getRefferedThrough() != null) {
            return getRefferedThrough().getRecordId() + PATH_DELIMITER + getId();
        } else {
            return getId();
        }
    }

    @SuppressWarnings("unchecked")
    public T getValue(Record record) {
        return (T) record.getValue(getRecordId());
    }

    public void setValue(Record record, T value) {
        record.setValue(getRecordId(), value);
    }
}
