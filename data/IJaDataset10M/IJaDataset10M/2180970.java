package net.java.sip.communicator.gui;

import java.io.*;
import javax.swing.*;
import net.java.sip.communicator.common.*;

/**
 * <p>Title: SIP COMMUNICATOR-1.1</p>
 * <p>Description: JAIN-SIP-1.1 Audio/Video Phone Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Organisation: LSIIT Laboratory (http://lsiit.u-strasbg.fr)</p>
 * </p>Network Research Team (http://www-r2.u-strasbg.fr))</p>
 * </p>Louis Pasteur University - Strasbourg - France</p>
 * @author Emil Ivov
 * @version 1.1
 */
public class ContactsComboBoxModel extends DefaultComboBoxModel {

    private static final Console console = Console.getConsole(ContactsComboBoxModel.class);

    public ContactsComboBoxModel() {
        loadDialHistory();
    }

    private void loadDialHistory() {
        try {
            FileReader fReader = new FileReader(Utils.getProperty("user.home") + File.separator + "dialhistory.txt");
            BufferedReader dialHistory = new BufferedReader(fReader);
            String entry = null;
            while ((entry = dialHistory.readLine()) != null) {
                super.insertElementAt(entry, 0);
            }
            dialHistory.close();
            fReader.close();
        } catch (IOException ex) {
            console.error(ex.getMessage());
        }
    }

    public void addElement(Object element) {
        if (super.getIndexOf(element) != -1) {
            return;
        }
        if (element == null || element.toString().trim().length() == 0) {
            return;
        }
        try {
            FileWriter fWriter = new FileWriter(Utils.getProperty("user.home") + "/dialhistory.txt", true);
            PrintWriter dialHistory = new PrintWriter(fWriter);
            dialHistory.print(element.toString() + "\r\n");
            dialHistory.close();
            fWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        super.insertElementAt(element, 0);
    }
}
