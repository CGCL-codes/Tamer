package org.fm.addressbook.dao;

import java.util.List;
import org.fm.addressbook.businessobject.AddressBook;
import org.fm.addressbook.businessobject.Contact;

/**
 * JContact- online Address Book Management System<a
 * href="http://jcontact.sourceforge.net/">http://jcontact.sourceforge.net/</a>
 * <p>
 * Licensed under the terms of any of the following licenses at your choice: -
 * <br/> GNU General Public License Version 2 or later (the "GPL") <a
 * href="http://www.gnu.org/licenses/gpl.html">http://www.gnu.org/licenses/gpl.html</a> -
 * <br/>GNU Lesser General Public License Version 2.1 or later (the "LGPL") <a
 * href="http://www.gnu.org/licenses/lgpl.html">http://www.gnu.org/licenses/lgpl.html</a>
 * <p>
 * AddressBookDAO interface
 * <p>
 * 
 * @author <a href="mailto:tennyson.varghese@yahoo.co.in">Tennyson Varghese</a>
 *         <br/> <a href="mailto:aneeshadoor2003@yahoo.co.in">Aneesh S</a>
 */
public interface AddressBookDAO {

    public void saveAddressBook(AddressBook addressBook);

    public void updateAddressBook(AddressBook addressBook);

    public void deleteAddressBook(AddressBook addressBook);

    public AddressBook findAddressBook(String searchKey);

    public List<AddressBook> getAllAddressBook();

    public List<Contact> getAllContacts(AddressBook addressBook);
}
