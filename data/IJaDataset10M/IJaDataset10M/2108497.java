package corny.FritzPhoneBook.gui.FritzContactTable;

import java.util.Collection;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import corny.FritzPhoneBook.data.ActionTarget;
import corny.FritzPhoneBook.data.contact.CombinedFritzContact;
import corny.FritzPhoneBook.data.contact.FritzContact;
import corny.FritzPhoneBook.data.contact.FritzContact.PhoneLabel;
import corny.FritzPhoneBook.data.contact.FritzContactImpl;
import corny.FritzPhoneBook.utils.ContactUtils;
import corny.addressbook.data.ContactProperty;
import corny.addressbook.data.MultiValue;
import corny.addressbook.data.MultiValue.KeyValuePair;
import corny.addressbook.data.MultiValue.MultiValueLabel;
import corny.addressbook.gui.ContactDisplay.ContactDisplayPanel.ContactDisplayPanelDataSource;

public class FritzContactTableModel<T extends ActionTarget> extends DefaultTableModel {

    private static final String NAME_COLUMN = "Name";

    private static final String DEFAULT_CALL_COLUMN = "Standard";

    private static final long serialVersionUID = 9183851539032390002L;

    private final Vector<T> contacts = new Vector<T>();

    private final boolean hasDefaultCallColumn;

    private ContactProperty[] columns = new ContactProperty[] { ContactProperty.DISPLAYED_NAME_LAST_NAME_FIRST, ContactProperty.PHONE, ContactProperty.PHONE, ContactProperty.PHONE, ContactProperty.EMAIL, ContactProperty.HAS_PICTURE };

    private MultiValueLabel[] labels = new MultiValueLabel[] { null, MultiValueLabel.HOME, MultiValueLabel.MOBILE, MultiValueLabel.WORK, null, null };

    public FritzContactTableModel(boolean hasDefaultCallColumn) {
        this.hasDefaultCallColumn = hasDefaultCallColumn;
    }

    @Override
    public int getColumnCount() {
        return hasDefaultCallColumn ? columns.length + 2 : columns.length + 1;
    }

    @Override
    public int getRowCount() {
        return contacts != null ? contacts.size() : 0;
    }

    private int columnIndexToArrayIndex(int col) {
        return hasDefaultCallColumn && col > getDefaultCallColumn() ? col - 2 : col - 1;
    }

    public int getDefaultSortedColumn() {
        return 1;
    }

    public int getDefaultCallColumn() {
        return 5;
    }

    @Override
    public String getColumnName(int col) {
        if (col == 0) {
            return "";
        } else if (col == 1) {
            return NAME_COLUMN;
        } else if (hasDefaultCallColumn && col == getDefaultCallColumn()) {
            return DEFAULT_CALL_COLUMN;
        }
        col = columnIndexToArrayIndex(col);
        ContactProperty prop = columns[col];
        if (prop.isMultiValue() && labels[col] != null) {
            return prop.getHumanReadableName() + " (" + labels[col].getHumanReadableName() + ")";
        } else {
            return prop.getHumanReadableName();
        }
    }

    @Override
    public Object getValueAt(final int row, int col) {
        final ActionTarget person = contacts.get(row);
        if (col == 0) {
            return ContactUtils.getContactStatus(person).getIcon();
        } else if (hasDefaultCallColumn && col == getDefaultCallColumn()) {
            CombinedFritzContact fritzContact = (CombinedFritzContact) person;
            return fritzContact.getDefaultCall();
        }
        col = columnIndexToArrayIndex(col);
        String label = labels[col] == null ? null : labels[col].getHumanReadableName();
        Object value = person.getValueForProperty(columns[col], null);
        if (value instanceof MultiValue<?>) {
            MultiValue<?> multiValue = (MultiValue<?>) value;
            if (label != null) {
                for (KeyValuePair<?> pair : multiValue) {
                    if (pair.getKey().startsWith(label)) {
                        return pair.getValue();
                    }
                }
                return null;
            } else {
                return multiValue.getFirstValue();
            }
        } else {
            return value;
        }
    }

    @Override
    public boolean isCellEditable(final int row, final int column) {
        return hasDefaultCallColumn && column == getDefaultCallColumn();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? ImageIcon.class : (hasDefaultCallColumn && columnIndex == getDefaultCallColumn()) ? PhoneLabel.class : columns[columnIndexToArrayIndex(columnIndex)].getClasses().get(0);
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (hasDefaultCallColumn && column == getDefaultCallColumn()) {
            CombinedFritzContact fritzContact = (CombinedFritzContact) contacts.get(row);
            fritzContact.setDefaultCall((PhoneLabel) aValue);
        }
    }

    public Vector<T> getContacts() {
        return contacts;
    }

    public void clear() {
        contacts.clear();
        fireTableDataChanged();
    }

    public void setContacts(Collection<T> contacts) {
        this.contacts.clear();
        this.contacts.addAll(contacts);
        fireTableDataChanged();
    }
}
