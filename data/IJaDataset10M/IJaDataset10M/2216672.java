package ararat.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import ararat.core.Contact;

/**
 * <code>ContactsWriter</code> is responsible for storing contacts
 * to secondary storage. The contacts are written to the address
 * book stored as an XML file. The address book file is defined by
 * the <code>SystemInfo.ADDRESS_BOOK</code> field.
 * 
 * @author	Arman Charif
 * @version	07-Jul-2004
 */
public class ContactsWriter {

    private BufferedWriter bw;

    private static final String NL = System.getProperty("line.separator");

    private static final String Q = "\"";

    /**
	 * Marshalls a list of <code>Contact</code>s into an XML file.
	 * The data is saved to the address book file defined by
	 * the <code>SystemInfo.ADDRESS_BOOK</code> field.
	 * 
	 * @param contacts	list of contacts to save.
	 * @throws IOException if an error occurs during the write.
	 */
    public final synchronized void marshall(List contacts) throws IOException {
        bw = new BufferedWriter(new FileWriter(SystemInfo.ADDRESS_BOOK));
        bw.write("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" + NL);
        bw.write("<?DOCTYPE Document SYSTEM \"contacts.dtd\"?>" + NL);
        bw.write("<contacts>" + NL);
        Contact c;
        for (int i = 0; i < contacts.size(); i++) {
            c = (Contact) contacts.get(i);
            bw.write("\t<contact>" + NL);
            bw.write("\t\t<name>" + c.getName() + "</name>" + NL);
            bw.write("\t\t<email>" + c.getEmail().getAddress() + "</email>" + NL);
            bw.write("\t\t<dob>" + c.getDateOfBirth() + "</dob>" + NL);
            bw.write("\t\t<primaryTel>" + c.getPrimaryTelephone() + "</primaryTel>" + NL);
            bw.write("\t\t<secondaryTel>" + c.getSecondaryTelephone() + "</secondaryTel>" + NL);
            bw.write("\t\t<fax>" + c.getFax() + "</fax>" + NL);
            bw.write("\t\t<address>" + c.getPostalAddress() + "</address>" + NL);
            bw.write("\t\t<homepage>" + c.getHomepage() + "</homepage>" + NL);
            bw.write("\t</contact>" + NL);
        }
        bw.write("</contacts>");
        bw.close();
    }
}
