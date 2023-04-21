package net.sf.mailsomething.gui.mail;

import java.util.Vector;
import javax.swing.AbstractListModel;
import net.sf.mailsomething.mail.AddressBook;

/**
 * This is the class that serves as the data model for GUI components that show
 * AddressList names in the AddressListViewer. 
 * @author louisedm
 * @created 12-07-2003
 * 
 */
public class AddressNameModel extends AbstractListModel {

    private AddressBook addressBook;

    private String[] listNames;

    protected Vector elements;

    public AddressNameModel(AddressBook addressBook) {
        this.addressBook = addressBook;
        this.listNames = addressBook.getNames();
        this.elements = new Vector();
        for (int i = 0; i < listNames.length; i++) {
            elements.add(listNames[i]);
        }
    }

    public AddressBook getAddressBook() {
        return this.addressBook;
    }

    public boolean addAddressList(String newName) {
        boolean operationComplete = false;
        if (addressBook.addAddressList(newName)) {
            elements.add(newName);
            fireContentsChanged(this, 0, elements.size());
            operationComplete = true;
        }
        return operationComplete;
    }

    public void deleteAddressList(String name) {
        addressBook.deleteAddressList(name);
        elements.remove(name);
        fireContentsChanged(this, 0, elements.size());
    }

    public void setAddressListName(String oldname, String newname) {
        addressBook.changeAddressListName(oldname, newname);
        elements.remove(oldname);
        elements.add(newname);
        fireContentsChanged(this, 0, elements.size());
    }

    public int getSize() {
        return elements.size();
    }

    public Object getElementAt(int arg) {
        return elements.get(arg);
    }
}
