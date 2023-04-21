package com.centraview.mail;

import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.ejb.EJBObject;

public interface MailImport extends EJBObject {

    /**
   * Sets the target datasource to be used for DB interaction.
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
    public void setDataSource(String ds) throws RemoteException;

    /**
   * Takes the body of the message, and parses all the import
   * information from it, returning an ArrayList containing
   * HashMaps representing the fields and values to be imported.
   * Only valid fields are returned in the ArrayList (ie: if
   * "Height:" is in the body, but is not a valid field, it will
   * not be returned in the ArrayList.
   * @param body String representing the message body to be parsed.
   * @param type String representing the type of import of this
   *        message (ie: "Contact", "Ticket, etc).
   * @return ArrayList containing HashMaps representing the
   *         fields and values to be imported.
   */
    public ArrayList parseMessageBody(String body, String type) throws RemoteException;

    /**
   * Returns an ArrayList containing String objects, each representing
   * a valid field name for the given <code>importType</code>. The
   * ArrayList is used to determine which fields from the message body
   * are valid to be imported.
   * @param importType String representing the type of import for
   *        which to return valid field names (ie: "Contact").
   * @return ArrayList of Strings with valid field names.
   */
    public ArrayList getValidFields(String importType) throws RemoteException;

    /**
   * Returns an ArrayList containing String objects, each representing
   * a valid field name for a custom field. The ArrayList is used to
   * determine which fields from the message body are valid custom fields
   * to be imported.
   * @param fieldType The type of custom fields to return ("Entity", "Individual", "Both")
   * @return ArrayList of Strings with valid field names.
   */
    public ArrayList getValidCustomFields(String fieldType) throws RemoteException;
}
