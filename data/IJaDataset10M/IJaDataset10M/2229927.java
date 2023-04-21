package fr.cantor.addressbook.ui.uiv1;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import javax.swing.table.AbstractTableModel;
import fr.cantor.addressbook.generated.AddressBook_Contact;
import fr.cantor.addressbook.utils.ToolsBox;
import fr.cantor.commore.CommoreExecutionException;
import fr.cantor.commore.CommoreInternalException;
import fr.cantor.commore.util.Out;

/**
 * CustomContactTableModel Version 1
 * =================================
 * Defined a table model of UI version 1
 * @author Daniel DA COSTA.
 */
public class CustomContactTableModel extends AbstractTableModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 449567144726524215L;

    private final UIAddressBook ui;

    private List<Integer> allListContact = new ArrayList<Integer>();

    private boolean isSearchFailed = false;

    private String message = null;

    private boolean isFirstLaunch = false;

    private Integer idModify = null;

    private boolean hasBeenModify;

    private final TreeSet<ContactInformation> treeSetContact = new TreeSet<ContactInformation>();

    /**
	 * This class is used in the tree set contact.
	 */
    public class ContactInformation implements Comparable<ContactInformation> {

        private final String lastName;

        private final String firstName;

        private final Integer id;

        @Override
        public String toString() {
            return "id = " + id + " Name = " + lastName + " firstName= " + firstName;
        }

        public ContactInformation(AddressBook_Contact contact, Integer id) {
            if (contact != null) {
                lastName = contact.getLastName();
                firstName = contact.getFirstName();
            } else {
                lastName = "";
                firstName = "";
            }
            this.id = id;
        }

        public final String getName() {
            return lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        @Override
        public int compareTo(ContactInformation o) {
            if (id.equals(o.id)) {
                return 0;
            }
            if (this.lastName.equals(o.lastName)) {
                int compare = this.firstName.compareTo(o.firstName);
                if (compare == 0) {
                    return 1;
                }
                return compare;
            }
            return this.lastName.compareTo(o.lastName);
        }

        public Integer getId() {
            return id;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((id == null) ? 0 : id.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            ContactInformation other = (ContactInformation) obj;
            return id.equals(other.id);
        }
    }

    public CustomContactTableModel(UIAddressBook ui) {
        this.ui = ui;
        isFirstLaunch = true;
        if (!updateContact(null, false)) {
            throw new InternalError();
        }
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case 0:
                return "Last Name";
            case 1:
                return "First Name";
        }
        return null;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public int getRowCount() {
        if (isSearchFailed) {
            return 1;
        }
        return allListContact.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (idModify != null && hasBeenModify) {
            hasBeenModify = false;
            setSelectionInTable(idModify);
        }
        if (isSearchFailed) {
            switch(columnIndex) {
                case 0:
                    return message;
                default:
                    return null;
            }
        }
        if (rowIndex < allListContact.size()) {
            Integer id = getIdFromLineInSet(rowIndex);
            switch(columnIndex) {
                case 0:
                    return getNameFromIdInSet(id);
                case 1:
                    return getFirstNameFromIdInSet(id);
            }
        }
        return null;
    }

    public String getFirstNameFromIdInSet(Integer id) {
        for (ContactInformation contact : treeSetContact) {
            if (contact.getId().equals(id)) {
                return contact.getFirstName();
            }
        }
        return "";
    }

    public String getNameFromIdInSet(Integer id) {
        for (ContactInformation contact : treeSetContact) {
            if (contact.getId().equals(id)) {
                return contact.getName();
            }
        }
        return "";
    }

    public Integer getIdFromLineInSet(Integer line) {
        synchronized (treeSetContact) {
            int i = 0;
            for (ContactInformation contact : treeSetContact) {
                if (i == line) {
                    return contact.getId();
                }
                i++;
            }
            return null;
        }
    }

    public Integer getLineFromIdInSet(int id) {
        int i = 0;
        for (ContactInformation contact : treeSetContact) {
            if (contact.getId().equals(id)) {
                return i;
            }
            i++;
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void updateAllInfoContact() {
        treeSetContact.clear();
        try {
            buildTreeSetContact(allListContact);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSelectionInTable(Integer id) {
        Integer selectedLine = ui.getSelectedLine();
        if (selectedLine != null) {
            Out<Boolean> isContactExist = new Out<Boolean>();
            Integer line = getLineFromIdInSet(id);
            try {
                ui.getAddressbook().isConctactExist(id, isContactExist);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isContactExist != null && isContactExist.get()) {
                ui.displayContactInPanel();
                ui.getContactTable().getSelectionModel().addSelectionInterval(line, line);
            } else {
                ui.clearContactPanel();
            }
        } else if (ui.isAddContact()) {
            Integer line = getLineFromIdInSet(id);
            ui.getContactTable().getSelectionModel().addSelectionInterval(line, line);
        } else {
            ui.clearContactPanel();
        }
    }

    public void updateInfoContactWithId(Integer id) {
        AddressBook_Contact contact = new AddressBook_Contact();
        Integer selectedLine = ui.getSelectedLine();
        Integer lineIdInSet;
        try {
            ui.getAddressbook().read(id, contact);
            ContactInformation infoContact = new ContactInformation(contact, id);
            if (!allListContact.contains(id)) {
                allListContact.add(id);
                treeSetContact.add(infoContact);
                lineIdInSet = getLineFromIdInSet(id);
                fireTableRowsInserted(lineIdInSet, lineIdInSet);
            } else {
                removeContactInSet(id);
                treeSetContact.add(infoContact);
                getLineFromIdInSet(id);
                fireTableRowsUpdated(0, allListContact.size());
            }
        } catch (Exception e) {
            lineIdInSet = removeContactInSet(id);
            allListContact.remove(id);
            if (selectedLine != null && getIdFromLineInSet(selectedLine).equals(id)) {
                ui.setSelectedLine(null);
            }
            fireTableRowsDeleted(lineIdInSet, lineIdInSet);
        }
    }

    public void updateWithContactList(List<Integer> list) {
        isSearchFailed = false;
        allListContact.clear();
        allListContact.addAll(list);
        if (hasBeenModify && idModify != null) {
            updateInfoContactWithId(idModify);
        } else if (isFirstLaunch) {
            isFirstLaunch = false;
            updateAllInfoContact();
            super.fireTableDataChanged();
        } else {
            updateAllInfoContact();
            super.fireTableDataChanged();
        }
    }

    public boolean updateContact(Integer id, boolean hasBeenModify) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        idModify = id;
        this.hasBeenModify = hasBeenModify;
        if (id == null) {
            try {
                ui.getAddressbook().getAll(list);
            } catch (Exception e) {
                ui.getJFrame().setVisible(false);
                ToolsBox.showConnectionErrorV1(ui.getJFrame(), e.getMessage());
                return false;
            }
        } else {
            list.addAll(allListContact);
        }
        updateWithContactList(list);
        return true;
    }

    public void failedSearch(String message) {
        allListContact.clear();
        isSearchFailed = true;
        this.message = message;
        super.fireTableDataChanged();
    }

    /**
	 * This function tree a list of contact.
	 * @param listId
	 * @throws CommoreInternalException
	 * @throws CommoreExecutionException
	 */
    public void buildTreeSetContact(List<Integer> listId) {
        for (Integer id : listId) {
            AddressBook_Contact contact = new AddressBook_Contact();
            try {
                ui.getAddressbook().read(id, contact);
                treeSetContact.add(new ContactInformation(contact, id));
            } catch (Exception e) {
            }
        }
    }

    public int removeContactInSet(Integer id) {
        int i = 0;
        for (ContactInformation contact : treeSetContact) {
            if (contact.id.equals(id)) {
                treeSetContact.remove(contact);
                return i;
            }
            i++;
        }
        return -1;
    }
}
