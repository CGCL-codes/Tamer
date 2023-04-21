package org.tigr.seq.tdb.display;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import org.tigr.seq.display.*;
import org.tigr.seq.log.*;

/**
 *
 * This class provides user the UI for selecting bac id
 *
 * <p>Copyright &copy; 2003, The Institute for Genomic Research (TIGR).
 * <br>All rights reserved. 
 *
 * @author     Yongmei Zhao
 *
 * <pre>
 * $RCSfile: BacIdDialog.java,v $
 * $Revision: 1.5 $
 * $Date: 2005/12/09 18:27:31 $
 * $Author: dkatzel $
 * </pre>
 * 
 */
public class BacIdDialog extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 3418963545232145734L;

    private JTextField idField;

    private int bacID;

    private String selectedBacId;

    private ArrayList IDs;

    private JButton okButton;

    /**
         * Constructor 
         *
         * @param  pParent   a <code>JDialog</code> instance as parent for this dialog
         * @param  pIDs      a <code>ArrayList</code> of avaible bac_ids
         */
    public BacIdDialog(JDialog pParent, ArrayList pIDs) {
        super(pParent, "", true);
        this.IDs = pIDs;
        this.layoutUI();
    }

    /**
        * lay out the UI components
        */
    private void layoutUI() {
        this.setVisible(false);
        this.setSize(260, 185);
        this.setResizable(false);
        this.setTitle(ResourceUtil.getResource(BacIdDialog.class, "text.dialog.title"));
        AppUtil.center(this);
        Container container = getContentPane();
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BorderLayout(10, 20));
        outerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(10, 10, 10, 15)));
        JLabel dbLabel = new JLabel(ResourceUtil.getResource(BacIdDialog.class, "text.label"));
        this.idField = new JTextField(8);
        this.idField.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent fe) {
                BacIdDialog.this.idField.selectAll();
            }

            public void focusLost(FocusEvent fe) {
            }
        });
        this.idField.requestFocusInWindow();
        String defaultBacId = "";
        if (!this.IDs.isEmpty()) {
            defaultBacId = (Integer) this.IDs.get(0) + "";
        }
        this.idField.setText(defaultBacId);
        this.idField.setEditable(true);
        this.selectedBacId = defaultBacId;
        this.okButton = new JButton(ResourceUtil.getResource(BacIdDialog.class, "text.button.ok"));
        this.okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                BacIdDialog.this.selectedBacId = BacIdDialog.this.idField.getText().trim();
                if (BacIdDialog.this.isBacIdValid(BacIdDialog.this.selectedBacId)) {
                    BacIdDialog.this.setVisible(false);
                }
            }
        });
        this.getRootPane().setDefaultButton(this.okButton);
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(5, 1));
        JLabel space1 = new JLabel("        ");
        space1.setSize(new Dimension(100, 30));
        JLabel space2 = new JLabel("        ");
        space2.setSize(new Dimension(100, 30));
        rightPanel.add(dbLabel);
        rightPanel.add(this.idField);
        rightPanel.add(space1);
        rightPanel.add(this.okButton);
        rightPanel.add(space2);
        JPanel idListPanel = new JPanel();
        String title = ResourceUtil.getResource(BacIdDialog.class, "text.idlist.title");
        idListPanel.setBorder(BorderFactory.createTitledBorder(title));
        JList idlist = this.createJList(this.IDs, defaultBacId);
        idlist.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent evt) {
                if (evt.getValueIsAdjusting()) {
                    return;
                }
                JList source = (JList) evt.getSource();
                if (source.isSelectionEmpty()) {
                } else {
                    Object[] values = source.getSelectedValues();
                    BacIdDialog.this.selectedBacId = ((String) values[0]).trim();
                    System.out.println("selected bac id is  " + selectedBacId);
                    BacIdDialog.this.idField.setText(BacIdDialog.this.selectedBacId);
                }
            }
        });
        JScrollPane listp = new JScrollPane(idlist);
        listp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        listp.setViewportView(idlist);
        JViewport vp = listp.getViewport();
        vp.setViewPosition(new Point(0, 0));
        listp.setPreferredSize(new Dimension(90, 80));
        idListPanel.add(listp);
        outerPanel.add(idListPanel, "West", 0);
        outerPanel.add(rightPanel, "Center", 1);
        container.add(outerPanel);
        this.validate();
    }

    /**
        *  Create JList bac_id list, which stores the names of avaiable bac_ids  
        *  for the selected result assembly
        * 
        *  @param data   a  <code>Collection </code>  value
        *  @param pId    a <code> String </code> value
        *    
        *  @return  JList
        */
    private JList createJList(Collection data, String pId) {
        DefaultListModel lmodel = new DefaultListModel();
        int selectedIndex = -1;
        if (data != null) {
            Iterator iter = data.iterator();
            int index = 0;
            while (iter.hasNext()) {
                String bac = (Integer) iter.next() + "";
                if (bac.trim().equals(pId)) {
                    selectedIndex = index;
                }
                lmodel.addElement("   " + bac);
                index++;
            }
        }
        JList list = new JList(lmodel);
        list.setSelectedIndex(selectedIndex);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return list;
    }

    /**
         * Method to see if the selected bac id is valid or not
         *
         * @return a <code>boolean<code> value
         * 
         */
    private boolean isBacIdValid(String pId) {
        boolean valid = false;
        try {
            int bacId = Integer.parseInt(pId);
            this.bacID = bacId;
            valid = (bacId >= 0) ? true : false;
        } catch (NumberFormatException nfx) {
            valid = false;
        }
        if (!valid) {
            JOptionPane.showMessageDialog(this, ResourceUtil.getResource(BacIdDialog.class, "message.invalid_id"));
        }
        return valid;
    }

    /**
         * Get bacID
         *
         * @return a <code>int</code> value
         */
    public int getBacID() {
        return this.bacID;
    }
}
