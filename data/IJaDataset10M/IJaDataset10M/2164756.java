package org.digitall.common.systemmanager;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import org.digitall.lib.components.JEntry;
import org.digitall.lib.components.JOutry;
import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicInternalFrame;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.common.components.combos.JCombo;

public class PanelNewStreet extends BasicContainerPanel {

    private BasicPanel PanelCountry = new BasicPanel();

    private BasicLabel lblStreet = new BasicLabel();

    private BasicLabel lblLocation = new BasicLabel();

    private BasicLabel lblType = new BasicLabel();

    private JCombo cbType = new JCombo();

    private JEntry tfStreet = new JEntry();

    private JOutry tfLocation = new JOutry();

    private AcceptButton bAccept = new AcceptButton();

    private CloseButton bClose = new CloseButton();

    private final int FRAME = 1;

    private final int INTERNALFRAME = 2;

    private final int DIALOG = 3;

    private int parentType = -1;

    private Component parent;

    private String insert, idlocation = "";

    private int error = 0;

    public PanelNewStreet(BasicInternalFrame _parent, String _idlocation) {
        try {
            parent = _parent;
            parentType = INTERNALFRAME;
            idlocation = _idlocation;
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PanelNewStreet(BasicDialog _parent, String _idlocation) {
        try {
            parent = _parent;
            parentType = DIALOG;
            idlocation = _idlocation;
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PanelNewStreet(JFrame _parent, String _idlocation) {
        try {
            parent = _parent;
            parentType = FRAME;
            idlocation = _idlocation;
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(null);
        this.setSize(new Dimension(286, 159));
        tfStreet.setBounds(new Rectangle(5, 50, 265, 20));
        tfStreet.setFont(new Font("Dialog", 1, 11));
        bAccept.setBounds(new Rectangle(240, 125, 40, 25));
        bAccept.setSize(new Dimension(40, 25));
        bAccept.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                bAccept_actionPerformed(e);
            }
        });
        bClose.setBounds(new Rectangle(5, 125, 40, 25));
        bClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                bClose_actionPerformed(e);
            }
        });
        cbType.setBounds(new Rectangle(5, 85, 185, 20));
        cbType.setFont(new Font("Dialog", 1, 11));
        cbType.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    String accident = org.digitall.lib.sql.LibSQL.getCampo("SELECT accident FROM tabs.location_tabs WHERE accident = '" + cbType.getSelectedItem().toString() + "' ");
                }
            }
        });
        lblType.setText("Type:");
        lblType.setBounds(new Rectangle(5, 75, 185, 10));
        lblType.setFont(new Font("Dialog", 1, 11));
        lblType.setHorizontalAlignment(SwingConstants.LEFT);
        lblLocation.setText("Location:");
        lblLocation.setBounds(new Rectangle(5, 5, 265, 10));
        lblLocation.setFont(new Font("Dialog", 1, 11));
        lblLocation.setHorizontalAlignment(SwingConstants.LEFT);
        tfLocation.setBounds(new Rectangle(5, 15, 265, 20));
        PanelCountry.setBounds(new Rectangle(5, 5, 275, 115));
        PanelCountry.setLayout(null);
        PanelCountry.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        lblStreet.setText("Street:");
        lblStreet.setBounds(new Rectangle(5, 40, 265, 10));
        lblStreet.setFont(new Font("Dialog", 1, 11));
        lblStreet.setHorizontalAlignment(SwingConstants.LEFT);
        PanelCountry.add(lblType, null);
        PanelCountry.add(cbType, null);
        PanelCountry.add(tfLocation, null);
        PanelCountry.add(lblStreet, null);
        PanelCountry.add(tfStreet, null);
        PanelCountry.add(lblLocation, null);
        this.add(PanelCountry, null);
        this.add(bClose, null);
        this.add(bAccept, null);
        seteaDatos();
    }

    private void seteaDatos() {
        String localidad = org.digitall.lib.sql.LibSQL.getCampo("Select name From tabs.location_tabs Where idlocation = " + idlocation);
        tfLocation.setText(localidad);
        cbType.loadJCombo("Select name from tabs.streettype_tabs Where estado <> '*'");
    }

    private void dispose() {
        switch(parentType) {
            case DIALOG:
                ((BasicDialog) parent).dispose();
                break;
            case INTERNALFRAME:
                ((BasicInternalFrame) parent).dispose();
                break;
            case FRAME:
                ((JFrame) parent).dispose();
                break;
        }
    }

    private void bClose_actionPerformed(ActionEvent e) {
        dispose();
    }

    private boolean control() {
        if (tfStreet.getText().equals("")) {
            error = 1;
            return false;
        } else return true;
    }

    private void bAccept_actionPerformed(ActionEvent e) {
        if (control()) {
            String type = org.digitall.lib.sql.LibSQL.getCampo("Select idtype From tabs.streettype_tabs Where name = '" + cbType.getSelectedItem().toString() + "' ");
            insert = "INSERT INTO tabs.street_tabs VALUES ( (select max(idstreet) +1 From tabs.street_tabs)" + " , " + idlocation + " ,'" + tfStreet.getText().toUpperCase().toLowerCase() + "' " + " , " + type + ",'')";
            System.out.println("insert: " + insert);
        } else error();
    }

    private void error() {
        switch(error) {
            case 1:
                org.digitall.lib.components.Advisor.messageBox("Field Street is empty", "Error");
                break;
        }
    }
}
