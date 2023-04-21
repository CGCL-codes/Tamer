package phonebook.entry;

import java.awt.*;
import javax.swing.*;

public class EntryPanel extends JPanel {

    GridBagLayout gridBagLayout1 = new GridBagLayout();

    JLabel jLabel1 = new JLabel();

    JTextField jTextField1 = new JTextField();

    JButton DeleteNumberBtn = new JButton();

    JButton AddNumberBtn = new JButton();

    JTable PhonesTbl = new JTable();

    private Person m_contact;

    public EntryPanel(Person contact) {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        m_contact = contact;
        PhonesTbl.setModel(m_contact.getNumbersModel());
    }

    private void jbInit() throws Exception {
        jLabel1.setText("jLabel1");
        this.setLayout(gridBagLayout1);
        this.setOpaque(true);
        jTextField1.setText("jTextField1");
        DeleteNumberBtn.setAlignmentX((float) 0.0);
        DeleteNumberBtn.setAlignmentY((float) 2.0);
        DeleteNumberBtn.setText("Delete Number");
        AddNumberBtn.setAlignmentY((float) 1.0);
        AddNumberBtn.setDebugGraphicsOptions(0);
        AddNumberBtn.setText("Add Number");
        this.add(jTextField1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel1, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(DeleteNumberBtn, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(AddNumberBtn, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(PhonesTbl, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }
}
