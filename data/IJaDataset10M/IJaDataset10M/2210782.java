package org.armedbear.j.mail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;
import org.armedbear.j.Directories;
import org.armedbear.j.Editor;
import org.armedbear.j.File;
import org.armedbear.j.Log;
import org.armedbear.j.Utilities;

public final class AddressBook {

    private static AddressBook addressBook;

    private static File file;

    private static File backupFile;

    private Vector entries;

    private AddressBook() {
        entries = new Vector();
    }

    public static AddressBook getGlobalAddressBook() {
        if (addressBook == null) {
            file = File.getInstance(Directories.getEditorDirectory(), "addresses");
            backupFile = File.getInstance(Directories.getEditorDirectory(), "addresses~");
            addressBook = new AddressBook();
            InputStream inputStream = null;
            try {
                if (file != null && file.isFile()) inputStream = file.getInputStream(); else if (backupFile != null && backupFile.isFile()) {
                    Log.debug("getGlobalAddressBook loading backup file");
                    inputStream = backupFile.getInputStream();
                }
                if (inputStream != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String s;
                    while ((s = reader.readLine()) != null) {
                        AddressBookEntry entry = AddressBookEntry.parseAddressBookEntry(s);
                        if (entry != null) addressBook.addEntry(entry);
                    }
                    reader.close();
                }
            } catch (IOException e) {
                Log.error(e);
            }
        }
        return addressBook;
    }

    public static void saveGlobalAddressBook() {
        try {
            File tempFile = Utilities.getTempFile();
            OutputStream outputStream = tempFile.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            final int limit = addressBook.size();
            for (int i = 0; i < limit; i++) {
                AddressBookEntry entry = addressBook.getEntry(i);
                writer.write(entry.toString());
                writer.write("\n");
            }
            writer.flush();
            writer.close();
            if (!Utilities.deleteRename(file, backupFile)) {
                Log.error("saveGlobalAddressBook deleteRename error file = " + file + " backupFile = " + backupFile);
            }
            if (!Utilities.deleteRename(tempFile, file)) {
                Log.error("saveGlobalAddressBook deleteRename error tempFile = " + tempFile + " file = " + file);
            }
        } catch (IOException e) {
            Log.error(e);
        }
    }

    public final int size() {
        return entries.size();
    }

    public void maybeAddMailAddress(MailAddress a) {
        String address = AddressBookEntry.canonicalizeAddress(a.getAddress());
        if (address == null) return;
        String personal = AddressBookEntry.canonicalizePersonal(a.getPersonal());
        for (int i = entries.size() - 1; i >= 0; i--) {
            AddressBookEntry entry = getEntry(i);
            if (address.equalsIgnoreCase(entry.getAddress())) {
                if (Utilities.isLowerCase(address)) entry.setAddress(address);
                if (personal == null) return;
                if (entry.getPersonal() == null) {
                    entry.setPersonal(personal);
                    return;
                }
                if (entry.getPersonal().equals(personal)) return;
            }
        }
        Log.debug("calling addEntry a.getAddress() = |" + a.getAddress() + "|");
        Log.debug("calling addEntry a.getPersonal() = |" + a.getPersonal() + "|");
        Log.debug("calling addEntry personal = |" + personal + "| address = |" + address + "|");
        addEntry(new AddressBookEntry(personal, address));
    }

    public void promote(MailAddress a) {
        String address = AddressBookEntry.canonicalizeAddress(a.getAddress());
        if (address == null) return;
        String personal = AddressBookEntry.canonicalizePersonal(a.getPersonal());
        AddressBookEntry toBePromoted = new AddressBookEntry(personal, address);
        for (int i = entries.size() - 1; i >= 0; i--) {
            if (toBePromoted.equals(getEntry(i))) {
                entries.removeElementAt(i);
                entries.insertElementAt(toBePromoted, 0);
                return;
            }
        }
    }

    private final void addEntry(AddressBookEntry entry) {
        entries.add(entry);
    }

    public final AddressBookEntry getEntry(int i) {
        return (AddressBookEntry) entries.get(i);
    }
}
