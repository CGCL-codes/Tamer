package org.javasock.jssniff.handlerpanel;

import javax.swing.JOptionPane;
import org.javasock.TCPPortFilter;
import org.javasock.jssniff.handlerdata.TCPData;

public class TCPPanel extends HandlerPanel {

    /**
     * The data model this view will use
     * <p>
     *
     * @author John P. Wilson
     *
     * @version 03/20/2007
     */
    private TCPData data = null;

    public TCPPanel(TCPData dataI) {
        data = dataI;
        initComponents();
        initData();
    }

    public boolean okAction() {
        boolean bFilter = activateTCPCB.isSelected();
        String tcpFromPortStr = fromTCPTF.getText().trim();
        short tcpFromPort = TCPPortFilter.UNSPECIFIED_PORT;
        String tcpToPortStr = toTCPTF.getText().trim();
        short tcpToPort = TCPPortFilter.UNSPECIFIED_PORT;
        try {
            if (tcpFromPortStr.equals("")) {
                tcpFromPort = TCPPortFilter.UNSPECIFIED_PORT;
            } else {
                tcpFromPort = Short.parseShort(tcpFromPortStr);
                if (tcpFromPort < 0) {
                    throw new NumberFormatException();
                }
            }
        } catch (NumberFormatException nfe) {
            if (bFilter) {
                JOptionPane.showMessageDialog(this, "You must specify a valid TCP \"From\" port.\n" + "NOTE: \"Any port\" can be specified by entering 0 in the TCP \"From\" port text field.", "TCP Port Error", JOptionPane.ERROR_MESSAGE);
                return false;
            } else {
                tcpFromPort = TCPPortFilter.UNSPECIFIED_PORT;
            }
        }
        try {
            if (tcpToPortStr.equals("")) {
                tcpToPort = TCPPortFilter.UNSPECIFIED_PORT;
            } else {
                tcpToPort = Short.parseShort(tcpToPortStr);
                if (tcpToPort < 0) {
                    throw new NumberFormatException();
                }
            }
        } catch (NumberFormatException nfe) {
            if (bFilter) {
                JOptionPane.showMessageDialog(this, "You must specify a valid TCP \"To\" port.\n" + "NOTE: \"Any port\" can be specified by entering 0 in the TCP \"To\" port text field.", "TCP Port Error", JOptionPane.ERROR_MESSAGE);
                return false;
            } else {
                tcpToPort = TCPPortFilter.UNSPECIFIED_PORT;
            }
        }
        data.setHasFilter(bFilter);
        data.setTCPFromPort(tcpFromPort);
        data.setTCPToPort(tcpToPort);
        return true;
    }

    private void initData() {
        activateTCPCB.setSelected(data.getHasFilter());
        fromTCPTF.setText(Short.toString(data.getTCPFromPort()));
        toTCPTF.setText(Short.toString(data.getTCPToPort()));
    }

    private void initComponents() {
        activateTCPCB = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        fromTCPTF = new javax.swing.JTextField();
        toTCPTF = new javax.swing.JTextField();
        activateTCPCB.setText("Activate TCP filter");
        activateTCPCB.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        activateTCPCB.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jLabel1.setText("From Port:");
        jLabel2.setText("To Port:");
        jLabel3.setText("NOTE: To match on any port, enter 0 in the port text field.");
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(fromTCPTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(toTCPTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE).add(71, 71, 71)).add(activateTCPCB).add(layout.createSequentialGroup().add(jLabel3).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(activateTCPCB).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(fromTCPTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel2).add(toTCPTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel3).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    private javax.swing.JCheckBox activateTCPCB;

    private javax.swing.JTextField fromTCPTF;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JTextField toTCPTF;
}
