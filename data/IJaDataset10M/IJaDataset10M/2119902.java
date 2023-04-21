package gcr.mmm2.model;

import gcr.mmm2.rdb.RDBConnection;
import gcr.mmm2.rdb.ResultSetWrapper;
import gcr.mmm2.util.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Simon King
 * @author Jeff Towle
 * 
 */
public class ContactManager {

    /**
     * @param id
     * @return
     */
    public static IContact getContactByID(int id) {
        return CacheManager.getContact(id);
    }

    public static IContact getContactByPerson(IPerson p, IUser u) {
        IContact contact = null;
        String query = "select id from contact where person_id=" + p.getID() + " and created_by=" + u.getID();
        List matching_contacts = listContacts(query);
        if (matching_contacts.size() > 0) {
            if (matching_contacts.size() > 1) System.out.println("more than one matching contact for pid=" + p.getID() + " and uid=" + u.getID());
            contact = (IContact) matching_contacts.get(0);
        }
        return contact;
    }

    /**
     * @param id
     * @return
     */
    public static IContactList getContactListByID(int id) {
        try {
            return new ContactListImpl(id);
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param contact
     * @param firstName
     */
    public static void setFirstName(IContact contact, String firstName) {
        String query = "update person set first=? where id=?";
        Object[] params = { StringUtils.URLEncode(firstName), new Integer(contact.getID()) };
        RDBConnection.executePrepared(query, params);
    }

    /**
     * @param contact
     * @param lastName
     */
    public static void setLastName(IContact contact, String lastName) {
        String query = "update person set last=? where id=?";
        Object[] params = { StringUtils.URLEncode(lastName), new Integer(contact.getID()) };
        RDBConnection.executePrepared(query, params);
    }

    /**
     * @param person
     * @param creator
     * @return
     */
    public static IContact createContact(IPerson person, IUser creator) {
        int id = RDBConnection.getNextVal("contact_id_seq");
        try {
            String query = "insert into contact (id, person_id, created_by, date_added) values (?, ?, ?, now() )";
            Object[] params = { new Integer(id), new Integer(person.getID()), new Integer(creator.getID()) };
            RDBConnection.executePrepared(query, params);
            return new ContactImpl(id);
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param creator
     * @param name
     * @return
     */
    public static IContactList createContactList(IUser creator, String name) {
        int id = RDBConnection.getNextVal("consumer_id_seq");
        try {
            String query = "insert into contact_list (id, name, created_by, date_added) values (?, ?, ?, now() )";
            Object[] params = { new Integer(id), StringUtils.URLEncode(name), new Integer(creator.getID()) };
            RDBConnection.executePrepared(query, params);
            return new ContactListImpl(id);
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param owner
     * @return
     */
    public static List listContacts(IUser owner) {
        String query = "select distinct id from contact where date_inactive is null and created_by=" + owner.getID();
        return listContacts(query);
    }

    /**
     * @param query
     * @return
     */
    protected static List listContacts(String query) {
        ResultSetWrapper rsw = RDBConnection.executeQuery(query);
        ResultSet rs = rsw.getResultSet();
        ArrayList l = new ArrayList();
        try {
            while (rs.next()) {
                int id = rs.getInt(1);
                IContact c = getContactByID(id);
                if (c != null) {
                    l.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rsw.closeAll();
        }
        Collections.sort(l, ObjectSorter.personByFirstName(ObjectSorter.ALPHABETICAL));
        return l;
    }

    /**
     * @param owner
     * @return
     */
    public static List listContactLists(IUser owner) {
        String query = "select distinct id from contact_list where created_by=" + owner.getID() + " and date_inactive is null";
        ResultSetWrapper rsw = RDBConnection.executeQuery(query);
        ResultSet rs = rsw.getResultSet();
        ArrayList l = new ArrayList();
        try {
            while (rs.next()) {
                int id = rs.getInt(1);
                IContactList cl = getContactListByID(id);
                if (cl != null) {
                    l.add(cl);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rsw.closeAll();
        }
        return l;
    }

    /**
     * @param contact
     */
    protected static void deleteContact(IContact contact) {
        String query = "update contact set date_inactive=now() where id=?";
        Object[] params = { new Integer(contact.getContactID()) };
        RDBConnection.executePrepared(query, params);
    }

    /**
     * @param contactList
     */
    protected static void deleteContactList(IContactList contactList) {
        String query = "update contact_list set date_inactive=now() where id=?";
        Object[] params = { new Integer(contactList.getID()) };
        RDBConnection.executePrepared(query, params);
    }
}
