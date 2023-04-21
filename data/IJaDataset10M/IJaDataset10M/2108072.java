package Vistes;

import Domini.*;
import java.awt.*;
import javax.swing.*;
import com.borland.jbcl.layout.*;
import java.awt.event.*;
import GDisc.*;
import Errors.*;

public class VMtpEsd extends JDialog {

    int nfet = 0;

    tipusEsdeveniment otesd;

    JPanel panel = new JPanel();

    XYLayout layout = new XYLayout();

    JTextField txPO = new JTextField();

    JLabel lNom = new JLabel();

    JLabel lPO = new JLabel();

    JButton bCancel = new JButton();

    JButton bAcp = new JButton();

    JTextField txNom = new JTextField();

    public VMtpEsd(Frame frame, String title, boolean modal) {
        super(frame, title, modal);
        try {
            jbInit();
            pack();
        } catch (Exception ex) {
        }
    }

    public VMtpEsd() {
        this(null, "", false);
    }

    void jbInit() throws Exception {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        panel.setLayout(layout);
        lNom.setText("Nom:");
        lPO.setText("P�blic Objectiu:");
        bCancel.setText("Cancel�lar");
        bCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                bCancel_actionPerformed(e);
            }
        });
        bAcp.setText("Acceptar");
        bAcp.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                bAcp_actionPerformed(e);
            }
        });
        this.setModal(true);
        this.setResizable(false);
        this.setTitle("Alta Tipus d\'Esdeveniment");
        getContentPane().add(panel);
        panel.add(txPO, new XYConstraints(122, 63, 222, 29));
        panel.add(lNom, new XYConstraints(13, 22, 94, 29));
        panel.add(lPO, new XYConstraints(10, 65, 93, 29));
        panel.add(bCancel, new XYConstraints(243, 108, 102, 35));
        panel.add(bAcp, new XYConstraints(133, 108, 102, 35));
        panel.add(txNom, new XYConstraints(121, 21, 222, 29));
    }

    void bAcp_actionPerformed(ActionEvent e) {
        if (comprovarparams()) {
            try {
                otesd = new tipusEsdeveniment(txNom.getText());
                otesd.setPublicObjectiu(txPO.getText());
                GDBtipusEsdeveniment gdbte = new GDBtipusEsdeveniment();
                gdbte.guarda(otesd);
                nfet = 1;
                dispose();
            } catch (excepcio f) {
            }
        } else {
            VisMensaje vis = new VisMensaje("Par�metres Incorrectes", 1);
        }
    }

    void bCancel_actionPerformed(ActionEvent e) {
        nfet = -1;
        dispose();
    }

    public int getFet() {
        return this.nfet;
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            nfet = -1;
            dispose();
        }
    }

    boolean comprovarparams() {
        int nerrors, nprovi, i;
        String serrors;
        nerrors = 0;
        serrors = txNom.getText();
        nprovi = serrors.length();
        if (nprovi == 0) nerrors++;
        serrors = txPO.getText();
        nprovi = serrors.length();
        if (nprovi == 0) nerrors++;
        if (nerrors == 0) return true; else return false;
    }
}
