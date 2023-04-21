package guiUI;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.table.*;

/**
 * This class is used to display general information about a 
 * GUINode.  It will display the IP Address information including 
 * default gateway and any Interface specific information such as 
 * the Current IP address and subnetmask.
 * @author Michael
 */
public class NodePropertiesDialog extends JDialog implements ActionListener {

    private JLabel lblNodeNameLabel = new JLabel("Node Name : ");

    private JLabel lblNodeName = new JLabel();

    private JLabel lblGatewayLabel = new JLabel("Default Gateway : ");

    private JLabel lblGatewayAddress = new JLabel();

    private JButton btnOK = new JButton("OK");

    JPanel pnlNodeNameDetails = new JPanel();

    JPanel pnlGatewayDetails = new JPanel();

    JPanel pnlOKButton = new JPanel();

    public NodePropertiesDialog(JFrame frame, Vector inVectorMasterList) {
        super(frame);
        ArrayList aryColumnNames = new ArrayList();
        aryColumnNames.add("Name");
        aryColumnNames.add("Gateway");
        aryColumnNames.add("Interface Name");
        aryColumnNames.add("MAC Address");
        aryColumnNames.add("IP Address");
        aryColumnNames.add("Subnet Mask");
        aryColumnNames.add("Link Name");
        Vector col = new Vector(aryColumnNames);
        JTable tblInfo = new JTable(inVectorMasterList, col);
        TableModel model = tblInfo.getModel();
        lblNodeName.setText(tblInfo.getModel().getValueAt(0, 0).toString());
        tblInfo.getColumnModel().removeColumn(tblInfo.getColumnModel().getColumn(0));
        lblGatewayAddress.setText(tblInfo.getModel().getValueAt(0, 1).toString());
        tblInfo.getColumnModel().removeColumn(tblInfo.getColumnModel().getColumn(0));
        tblInfo.setEnabled(false);
        tblInfo.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrPane = new JScrollPane(tblInfo);
        JPanel pnlPropertiesScreen = (JPanel) this.getContentPane();
        pnlPropertiesScreen.setLayout(new BoxLayout(pnlPropertiesScreen, BoxLayout.Y_AXIS));
        pnlNodeNameDetails.setLayout(new FlowLayout());
        pnlOKButton.setLayout(new FlowLayout());
        pnlNodeNameDetails.setPreferredSize(new Dimension(50, 50));
        pnlNodeNameDetails.add(lblNodeNameLabel);
        pnlNodeNameDetails.add(lblNodeName);
        pnlGatewayDetails.add(lblGatewayLabel);
        pnlGatewayDetails.add(lblGatewayAddress);
        pnlPropertiesScreen.add(pnlNodeNameDetails);
        pnlPropertiesScreen.add(pnlGatewayDetails);
        pnlPropertiesScreen.add(scrPane);
        pnlOKButton.add(btnOK);
        pnlPropertiesScreen.add(pnlOKButton);
        this.setSize(700, 200);
        this.setLocationRelativeTo(null);
        this.setModal(true);
        this.setResizable(false);
        btnOK.setToolTipText("Close the screen");
        this.getRootPane().setDefaultButton(btnOK);
        btnOK.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnOK) {
            this.dispose();
        }
    }
}
