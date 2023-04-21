package biber.core;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Vector;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.w3c.dom.*;
import biber.api.*;

public class EntryImpl implements Cloneable, TableModel, biber.api.Entry {

    private Set<EntryProperty> properties = new TreeSet<EntryProperty>();

    private HashSet<String> required = new HashSet<String>();

    private HashSet<String> optionals = new HashSet<String>();

    private List<TableModelListener> tableModelListeners = new Vector<TableModelListener>();

    private EntryType entryType = null;

    private UUID id = null;

    /**
	 * <p>Entries are created by calling the {@see #createEntry(Class)} factory
	 * method instead of the constructor.</p>
	 */
    public EntryImpl(EntryType entryType) {
        this(entryType, UUID.randomUUID());
    }

    /**
	 * <p>Entries are created by calling the {@see #createEntry(Class)} factory
	 * method instead of the constructor.</p>
	 */
    protected EntryImpl(EntryType entryType, UUID id) {
        this.entryType = entryType;
        this.id = id;
        entryType.initProperties(this);
    }

    public EntryType getType() {
        return entryType;
    }

    public String getTypeName() {
        return entryType == null ? null : entryType.getName();
    }

    public boolean hasProperty(String name) {
        return getProperty(name) != null;
    }

    public EntryProperty getProperty(String name) {
        if (name == null) {
            return null;
        }
        for (EntryProperty property : properties) {
            if (property.getName().equals(name)) {
                return property;
            }
        }
        return null;
    }

    public void setPropertyValue(String name, String value) {
        EntryProperty property = getProperty(name);
        if (property != null) {
            property.setValue(value);
        }
    }

    public String getPropertyValue(String name) {
        EntryProperty property = getProperty(name);
        if (property == null) {
            return null;
        }
        return property.getValue();
    }

    public void addRequiredProperty(EntryProperty property) {
        properties.add(property);
        required.add(property.getName());
    }

    public void addOptionalProperty(EntryProperty property) {
        properties.add(property);
        optionals.add(property.getName());
    }

    public void removeOptionalProperty(String name) {
        if (optionals.contains(name)) {
            EntryProperty property = getProperty(name);
            if (property == null) {
                return;
            }
            properties.remove(property);
            optionals.remove(name);
        }
    }

    public boolean isRequiredProperty(String name) {
        return required.contains(name);
    }

    public String getID() {
        return id.toString();
    }

    public String getCaption() {
        return getID();
    }

    public String toString() {
        return getCaption();
    }

    /**
	 * <p>Clone this entry.</p>
	 *
	 * @return a exact copy of this entry
	 */
    public Object clone() {
        Entry cloneEntry = new EntryImpl(entryType, id);
        for (EntryProperty property : properties) {
            String propName = property.getName();
            if (cloneEntry.hasProperty(propName)) {
                cloneEntry.setPropertyValue(property.getName(), property.getValue());
            } else {
                EntryProperty cloneProp = (EntryProperty) property.clone();
                if (isRequiredProperty(propName)) {
                    cloneEntry.addRequiredProperty(cloneProp);
                } else {
                    cloneEntry.addOptionalProperty(cloneProp);
                }
            }
        }
        return cloneEntry;
    }

    /**
	 * Returns the names of all required properties.
	 */
    public Set<String> getRequiredPropertyNames() {
        return (Set<String>) required.clone();
    }

    /**
	 * Returns the names of all optional properties.
	 */
    public Set<String> getOptionalPropertyNames() {
        return (Set<String>) optionals.clone();
    }

    public TableCellEditor getPropertyEditor(int rowIndex) {
        EntryProperty property = getPropertyAt(rowIndex);
        if (property == null) {
            return null;
        } else {
            return (TableCellEditor) property.getProperty(EntryProperty.CELL_EDITOR);
        }
    }

    public TableCellRenderer getPropertyRenderer(int rowIndex) {
        EntryProperty property = getPropertyAt(rowIndex);
        if (property == null) {
            return null;
        } else {
            return (TableCellRenderer) property.getProperty(EntryProperty.CELL_RENDERER);
        }
    }

    public int getPropertyHeight(int rowIndex) {
        EntryProperty property = getPropertyAt(rowIndex);
        if (property == null) {
            return -1;
        } else {
            Integer height = (Integer) property.getProperty(EntryProperty.CELL_HEIGHT);
            return (height == null) ? -1 : height;
        }
    }

    public EntryProperty getPropertyAt(int rowIndex) {
        int row = 0;
        for (EntryProperty property : properties) {
            if (row == rowIndex) {
                return property;
            }
            row++;
        }
        return null;
    }

    public String getPropertyNameAt(int rowIndex) {
        EntryProperty property = getPropertyAt(rowIndex);
        if (property != null) {
            return property.getName();
        } else {
            return null;
        }
    }

    public boolean checkConstraints() {
        return true;
    }

    public Set<EntryProperty> getProperties() {
        Set<EntryProperty> props = new TreeSet<EntryProperty>();
        for (EntryProperty prop : properties) {
            props.add(prop);
        }
        return props;
    }

    public Set<EntryProperty> getAdditionalProperties() {
        Set<EntryProperty> props = new TreeSet<EntryProperty>();
        Set<String> nativePropNames = entryType.getPropertyNames();
        for (EntryProperty prop : properties) {
            if (!nativePropNames.contains(prop.getName())) {
                props.add(prop);
            }
        }
        return props;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof EntryImpl)) {
            return false;
        }
        EntryImpl otherEntry = (EntryImpl) obj;
        if (!getID().equals(otherEntry.getID())) {
            return false;
        }
        if (!getType().equals(otherEntry.getType())) {
            return false;
        }
        if (otherEntry.getProperties().size() != properties.size()) {
            return false;
        }
        for (EntryProperty prop : getProperties()) {
            if (!prop.equals(otherEntry.getProperty(prop.getName()))) {
                return false;
            }
        }
        return true;
    }

    /**
	 * Returns the most specific superclass for all the cell values in the column.
	 *
	 * @see javax.swing.table.TableModel
	 */
    public Class getColumnClass(int columnIndex) {
        return String.class;
    }

    /**
	 * Returns the number of columns in the model.
	 *
	 * @see javax.swing.table.TableModel
	 */
    public int getColumnCount() {
        return 2;
    }

    /**
	 * Returns the name of the column at columnIndex.
	 *
	 * @see javax.swing.table.TableModel
	 */
    public String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return Resources.getString("EntryEditor.NameColumn.caption");
            case 1:
                return Resources.getString("EntryEditor.ValueColumn.caption");
        }
        return null;
    }

    /**
	 * Returns the number of rows in the model.
	 *
	 * @see javax.swing.table.TableModel
	 */
    public int getRowCount() {
        return properties.size();
    }

    /**
	 * Sets the value in the cell at columnIndex and rowIndex to aValue.
	 *
	 * @see javax.swing.table.TableModel
	 */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        EntryProperty property = getPropertyAt(rowIndex);
        switch(columnIndex) {
            case 1:
                property.setValue(aValue != null ? aValue.toString() : null);
        }
    }

    /**
	 * Returns the value for the cell at columnIndex and rowIndex.
	 *
	 * @see javax.swing.table.TableModel
	 */
    public Object getValueAt(int rowIndex, int columnIndex) {
        EntryProperty property = getPropertyAt(rowIndex);
        switch(columnIndex) {
            case 0:
                return property.getProperty(EntryProperty.DISPLAY_NAME);
            case 1:
                return property.getValue();
        }
        return null;
    }

    /**
	 * Returns true if the cell at rowIndex and columnIndex is editable.
	 *
	 * @see javax.swing.table.TableModel
	 */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1;
    }

    /**
	 * Adds a listener to the list that is notified each time a change to the data model occurs.
	 *
	 * @see javax.swing.table.TableModel
	 */
    public void addTableModelListener(TableModelListener l) {
        tableModelListeners.add(l);
    }

    /**
	 * Removes a listener from the list that is notified each time a change to the data model occurs.
	 *
	 * @see javax.swing.table.TableModel
	 */
    public void removeTableModelListener(TableModelListener l) {
        tableModelListeners.remove(l);
    }

    public Element serializeXML(Document doc) {
        Element el = doc.createElement("Entry");
        el.setAttribute("type", getTypeName());
        el.setAttribute("id", id.toString());
        for (EntryProperty property : properties) {
            Element elProp = doc.createElement("Property");
            elProp.setAttribute("name", property.getName());
            elProp.appendChild(doc.createTextNode(property.getValue()));
            el.appendChild(elProp);
        }
        return el;
    }

    public void unserializeXML(Element root) {
        NodeList propList = root.getElementsByTagName("Property");
        for (int i = 0; i < propList.getLength(); i++) {
            Element propEl = (Element) propList.item(i);
            propEl.normalize();
            Node tn = propEl.getFirstChild();
            String value = "";
            if (tn != null && tn.getNodeType() == Node.TEXT_NODE) {
                value = tn.getNodeValue();
            }
            setPropertyValue(propEl.getAttribute("name"), value);
        }
    }
}
