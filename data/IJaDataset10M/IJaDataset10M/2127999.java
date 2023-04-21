package net.sf.amemailchecker.ext.addressbook;

public interface AddressBookDataSourceReader {

    void read(AddressBook addressBook, String filePath) throws Exception;
}
