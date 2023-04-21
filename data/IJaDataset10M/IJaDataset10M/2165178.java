package blueprint4j.db;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.sql.ResultSet;
import blueprint4j.utils.*;

/**
 * This Field combines sets of other fields and stores their values in a 
 * properties field
 */
public class FieldSetCombiner extends FieldProperties implements BindSetCombinerInterface {

    private VectorGroup groups = new VectorGroup();

    private BindableProperties typeprop = new BindableProperties();

    public BindString type = new BindString("TYPE", "Type", null);

    /**
	 * public Field( String p_name, String p_description, boolean p_key_field
	 *
	 * Name: Name of field -> equivalent to table.field_name
	 * Description: Description of field
	 * key_field: Is this field a key field in the table (index field)
	 */
    public FieldSetCombiner(String name, int p_field_type, String p_description, Entity entity) {
        this(name, p_field_type, p_description, true, entity);
    }

    public FieldSetCombiner(String name, int p_field_type, String p_description, boolean save_type, Entity entity) {
        super(name, p_field_type, p_description, entity);
        if (save_type) {
            typeprop.addField(type);
        }
    }

    public String getTypeAsString() {
        if (type.get() == null || type.get().length() == 0) {
            type.set(groups.get(0).name);
        }
        return type.get();
    }

    public BindStringInterface getType() {
        return (BindStringInterface) type;
    }

    public String[] getTypes() {
        String[] types = new String[groups.size()];
        for (int i = 0; i < groups.size(); i++) {
            types[i] = groups.get(i).name;
        }
        return types;
    }

    protected void setFieldValue(ResultSet result_set) throws DataException {
        try {
            super.setFieldValue(result_set);
            if (get() != null) {
                typeprop.loadFrom(get());
                for (int i = 0; i < groups.size(); i++) {
                    groups.get(i).store.loadFrom(get());
                }
            }
        } catch (Exception e) {
            throw new DataException(e);
        }
    }

    public void setSerializable(String value) throws BindException {
        try {
            super.setSerializable(value);
            if (get() != null) {
                typeprop.loadFrom(get());
                for (int i = 0; i < groups.size(); i++) {
                    groups.get(i).store.loadFrom(get());
                }
            }
        } catch (DataException de) {
            throw new BindException(de);
        }
    }

    public boolean hasAnyFieldChanged() {
        return groups.hasAnyFieldChanged();
    }

    public void setChanged(boolean changed) {
        super.setChanged(changed);
        if (!changed) {
            type.setChanged(changed);
            groups.setChanged(changed);
        }
    }

    public BindableProperties[] getStore() {
        BindableProperties[] stores = new BindableProperties[groups.size() + 1];
        stores[0] = typeprop;
        for (int i = 1; i <= groups.size(); i++) {
            stores[i] = groups.get(i).store;
        }
        return stores;
    }

    public BindableProperties getStoreForType() {
        for (int i = 0; i < groups.size(); i++) {
            if (type.get().equals(groups.get(i).name)) {
                return groups.get(i).store;
            }
        }
        return null;
    }

    public void reload() throws BindException {
        typeprop.reload();
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).store.reload();
        }
    }

    /**
	 * This pushes the values of all the entity sets into the properties
	 */
    public void setInto() throws BindException {
        Properties store = new Properties();
        typeprop.saveBindable();
        store.putAll(typeprop.getProperties());
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).store.saveBindable();
            store.putAll(groups.get(i).store.getProperties());
        }
        super.set(store);
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public static class Group implements Bindable {

        public String type = null, name = null;

        public VectorBindFieldInterface fields = new VectorBindFieldInterface();

        public BindableProperties store = new BindableProperties();

        public Group(String name, FieldSetCombiner field_combiner) {
            field_combiner.addGroup(this);
            this.type = Utils.replaceAll(name, " ", "_");
            ;
            this.name = name;
        }

        public String getSourceName() {
            return name;
        }

        public void setChanged(boolean changed) {
            for (int i = 0; i < fields.size(); i++) {
                fields.get(i).setChanged(changed);
            }
        }

        public boolean hasAnyFieldChanged() {
            for (int i = 0; i < fields.size(); i++) {
                if (fields.get(i).isChanged()) {
                    return true;
                }
            }
            return false;
        }

        public void addField(BindFieldInterface field) {
            if (store == null) {
                store = new BindableProperties();
            }
            field.setName(type + "_" + field.getName());
            fields.add(field);
            store.addField(field);
            field.setBindable(store);
        }

        public boolean shouldSave() {
            return true;
        }

        public void save() throws BindException {
        }

        public void delete() throws BindException {
        }

        public void commit() throws BindException {
        }

        public void reload() throws BindException {
        }

        public boolean isLoaded() throws BindException {
            return false;
        }

        public Bindable getNextBindable() throws BindException {
            return null;
        }

        public BindFieldInterface[] getBindFields() throws BindException {
            return null;
        }

        public boolean find(BindFieldInterface bind_fields[], boolean allow_multiple_rows, boolean match_on_any_fields) throws BindException {
            return false;
        }

        public VectorBindable getListOfAllBindables() throws BindException {
            return null;
        }

        public Bindable getNewBindable() throws BindException {
            return null;
        }
    }

    public static class VectorGroup {

        private Vector store = new Vector();

        public Group get(int pos) {
            return (Group) store.get(pos);
        }

        public void add(Group item) {
            store.add(item);
        }

        public boolean remove(Group item) {
            return store.remove(item);
        }

        public Group remove(int pos) {
            return (Group) store.remove(pos);
        }

        public int size() {
            return store.size();
        }

        public void setChanged(boolean changed) {
            for (int i = 0; i < size(); i++) {
                get(i).setChanged(changed);
            }
        }

        public boolean hasAnyFieldChanged() {
            for (int i = 0; i < size(); i++) {
                if (get(i).hasAnyFieldChanged()) {
                    return true;
                }
            }
            return false;
        }
    }
}
