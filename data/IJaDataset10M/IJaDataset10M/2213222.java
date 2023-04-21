package com.tdcs.lords.client.display;

import com.tdcs.lords.obj.Patient;
import java.util.Vector;

/**
 *
 * @author  david
 */
public class RecordsReview extends javax.swing.JPanel {

    private String pre = "<html><body><p>The listed records will be moved/combined into the following:  <b><font color=\"red\"";

    private String post = "</font></b></p><p></p><p>If this is correct, select 'Next' to continue.</p></body></html>";

    /** Creates new form RecordsReview */
    public RecordsReview() {
        initComponents();
    }

    public void setData(Vector<Patient> selPatients, Patient patient) {
        list.setListData(selPatients);
        label.setText(pre + patient.getRecord() + post);
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        label = new javax.swing.JLabel();
        setLayout(new java.awt.GridLayout(0, 1));
        list.setBorder(javax.swing.BorderFactory.createTitledBorder("Records to be transferred"));
        list.setModel(new javax.swing.AbstractListModel() {

            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        jScrollPane1.setViewportView(list);
        add(jScrollPane1);
        add(label);
    }

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JLabel label;

    private javax.swing.JList list;
}
