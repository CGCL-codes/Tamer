package net.suberic.pooka.jdbcaddr;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.mail.internet.InternetAddress;
import net.suberic.pooka.*;
import net.suberic.pooka.vcard.Vcard;

/**
 * Defines the methods used to store Addresses.
 */
public class JDBCAddressBook implements AddressBook, AddressMatcher {

    static String sTableName = "pookaaddressbook";

    static String sFirstNameColumn = "given_name";

    static String sLastNameColumn = "surname";

    static String sIdColumn = "id";

    static String sAddressColumn = "addresses";

    String mAddressBookID;

    String mDriver;

    String mUrl;

    String mUsername;

    String mPassword;

    TreeMap<String, AddressBookEntry> mEntryMap = new TreeMap<String, AddressBookEntry>(String.CASE_INSENSITIVE_ORDER);

    public void configureAddressBook(String id) {
        mAddressBookID = id;
        mDriver = Pooka.getProperty("AddressBook." + mAddressBookID + ".driver", "");
        mUrl = Pooka.getProperty("AddressBook." + mAddressBookID + ".url", "");
        mUsername = Pooka.getProperty("AddressBook." + mAddressBookID + ".username", "");
        mPassword = Pooka.getProperty("AddressBook." + mAddressBookID + ".password", "");
    }

    /**
   * Gets and appropriate AddressMatcher.
   */
    public AddressMatcher getAddressMatcher() {
        return this;
    }

    /**
   * Adds an AddressBookEntry to the AddressBook.
   */
    public void addAddress(AddressBookEntry newEntry) {
        synchronized (this) {
            if (newEntry != null) {
                mEntryMap.put(newEntry.getID(), newEntry);
            }
            try {
                saveAddressBook();
            } catch (Exception ioe) {
                Pooka.getUIFactory().showError(Pooka.getProperty("error.savingVcard", "Error saving Address Book"), ioe);
            }
        }
    }

    /**
   * Removes an AddressBookEntry from the AddressBook.
   */
    public void removeAddress(AddressBookEntry removeEntry) {
        synchronized (this) {
            if (removeEntry != null) {
                mEntryMap.remove(removeEntry.getID());
            }
            try {
                saveAddressBook();
            } catch (Exception ioe) {
                Pooka.getUIFactory().showError(Pooka.getProperty("error.savingVcard", "Error saving Address Book"), ioe);
            }
        }
    }

    /**
   * Gets the ID for this address book.
   */
    public String getAddressBookID() {
        return mAddressBookID;
    }

    /**
   * Loads the AddressBook.
   */
    public void loadAddressBook() throws java.io.IOException, java.text.ParseException {
        synchronized (this) {
            Connection conn = null;
            TreeMap<String, AddressBookEntry> newEntryMap = new TreeMap<String, AddressBookEntry>(String.CASE_INSENSITIVE_ORDER);
            try {
                conn = getConnection();
                PreparedStatement loadStatement = conn.prepareStatement("select * from ?");
                loadStatement.setString(1, sTableName);
                ResultSet resultSet = loadStatement.executeQuery();
                while (resultSet.next()) {
                    try {
                        AddressBookEntry newEntry = newAddressBookEntry();
                        newEntry.setFirstName(resultSet.getString(sFirstNameColumn));
                        newEntry.setLastName(resultSet.getString(sLastNameColumn));
                        newEntry.setPersonalName(resultSet.getString(sIdColumn));
                        newEntry.setAddresses(InternetAddress.parse(resultSet.getString(sAddressColumn)));
                        newEntryMap.put(newEntry.getPersonalName(), newEntry);
                    } catch (javax.mail.internet.AddressException ae) {
                        System.err.println("exception parsing address:  " + ae.getMessage());
                    }
                }
                mEntryMap = newEntryMap;
            } catch (SQLException se) {
                throw new IOException(se);
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException se) {
                    }
                }
            }
        }
    }

    /**
   * Saves the AddressBook.
   */
    public void saveAddressBook() throws java.io.IOException {
        synchronized (this) {
            Connection conn = null;
            try {
                conn = getConnection();
                PreparedStatement deleteStatement = conn.prepareStatement("delete from ?");
                deleteStatement.setString(1, sTableName);
                deleteStatement.executeUpdate();
                PreparedStatement insertStatement = conn.prepareStatement("insert into " + sTableName + " (" + sIdColumn + ", " + sFirstNameColumn + ", " + sLastNameColumn + ", " + sAddressColumn + ") values (?, ?, ?, ?)");
                Iterator<String> keyIter = mEntryMap.keySet().iterator();
                while (keyIter.hasNext()) {
                    AddressBookEntry entry = mEntryMap.get(keyIter.next());
                    insertStatement.setString(1, entry.getPersonalName());
                    insertStatement.setString(2, entry.getFirstName());
                    insertStatement.setString(3, entry.getLastName());
                    insertStatement.setString(4, InternetAddress.toString(entry.getAddresses()));
                }
            } catch (SQLException se) {
                throw new IOException(se);
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException se) {
                    }
                }
            }
        }
    }

    /**
   * Creates a new, empty AddressBookEntry.
   */
    public AddressBookEntry newAddressBookEntry() {
        return new Vcard();
    }

    /**
   * Returns all of the InternetAddresses which match the given String.
   */
    public AddressBookEntry[] match(String matchString) {
        return match(matchString, false);
    }

    /**
   * Returns all of the InternetAddresses which match the given String.
   */
    public AddressBookEntry[] matchExactly(String matchString) {
        return match(matchString, true);
    }

    /**
   * Returns all of the InternetAddresses which match the given String.
   */
    public AddressBookEntry[] match(String matchString, boolean exactly) {
        if (exactly) {
            AddressBookEntry match = mEntryMap.get(matchString);
            if (match != null) {
                return new AddressBookEntry[] { match };
            } else {
                return new AddressBookEntry[0];
            }
        }
        Map.Entry floorEntry = mEntryMap.floorEntry(matchString);
        return new AddressBookEntry[0];
    }

    /**
   * Returns all of the InternetAddresses whose FirstName matches the given
   * String.
   */
    public AddressBookEntry[] matchFirstName(String matchString) {
        return match(matchString);
    }

    /**
   * Returns all of the InternetAddresses whose LastName matches the given
   * String.
   */
    public AddressBookEntry[] matchLastName(String matchString) {
        return match(matchString);
    }

    /**
   * Returns all of the InternetAddresses whose email addresses match the
   * given String.
   */
    public AddressBookEntry[] matchEmailAddress(String matchString) {
        return match(matchString);
    }

    /**
   * Returns the InternetAddress which follows the given String alphabetically.
   */
    public AddressBookEntry getNextMatch(String matchString) {
        Map.Entry<String, AddressBookEntry> higher = mEntryMap.higherEntry(matchString);
        if (higher != null) {
            return higher.getValue();
        } else {
            return null;
        }
    }

    /**
   * Returns the InternetAddress which precedes the given String
   * alphabetically.
   */
    public AddressBookEntry getPreviousMatch(String matchString) {
        Map.Entry<String, AddressBookEntry> lower = mEntryMap.lowerEntry(matchString);
        if (lower != null) {
            return lower.getValue();
        } else {
            return null;
        }
    }

    /**
   * Returns the Connection for this Preferences object.
   */
    private Connection getConnection() throws SQLException {
        try {
            Class.forName(mDriver).newInstance();
            Connection returnValue = DriverManager.getConnection(mUrl, mUsername, mPassword);
            return returnValue;
        } catch (ClassNotFoundException cnfe) {
            throw new SQLException(cnfe);
        } catch (InstantiationException ie) {
            throw new SQLException(ie);
        } catch (IllegalAccessException iae) {
            throw new SQLException(iae);
        }
    }
}
