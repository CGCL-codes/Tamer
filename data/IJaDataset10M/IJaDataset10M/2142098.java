package jmri.jmrix.lenz.swing.stackmon;

import jmri.jmrix.lenz.*;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;

/**
 * This frame provides a method for searching the command station stack.
 * Current functionality is to search the stack and delete entries.  
 * Future capabilities will include the ability to set the status of 
 * function buttons
 * <P>
 *
 * @author	Paul Bender   Copyright (C) 2005-2010
 * @version	$Revision: 1.2 $
 */
public class StackMonFrame extends jmri.util.JmriJFrame implements XNetListener {

    JButton nextButton = new JButton("Next Entry");

    JButton previousButton = new JButton("Previous Entry");

    JButton deleteButton = new JButton("Delete Entry");

    JButton refreshButton = new JButton("Refresh");

    JLabel CurrentStatus = new JLabel(" ");

    JTextField adrTextField = new javax.swing.JTextField(4);

    StackMonDataModel stackModel = null;

    javax.swing.JTable stackTable = null;

    private ResourceBundle rb = ResourceBundle.getBundle("jmri.jmrix.lenz.swing.stackmon.StackMonBundle");

    private boolean _getAll = false;

    protected XNetTrafficController tc = null;

    public StackMonFrame(jmri.jmrix.lenz.XNetSystemConnectionMemo memo) {
        super();
        stackModel = new StackMonDataModel(1, 4, memo);
        stackTable = new javax.swing.JTable(stackModel);
        nextButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                getNextEntry();
            }
        });
        nextButton.setText(rb.getString("NextButtonLabel"));
        nextButton.setVisible(true);
        previousButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                getPreviousEntry();
            }
        });
        previousButton.setText(rb.getString("PreviousButtonLabel"));
        previousButton.setVisible(true);
        previousButton.setEnabled(false);
        deleteButton.setText(rb.getString("DeleteButtonLabel"));
        deleteButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                deleteEntry();
            }
        });
        deleteButton.setVisible(true);
        refreshButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                getAllEntries();
            }
        });
        refreshButton.setText(rb.getString("RefreshButtonLabel"));
        refreshButton.setVisible(true);
        adrTextField.setVisible(true);
        setTitle(rb.getString("StackMonitorTitle"));
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JPanel pane1 = new JPanel();
        pane1.setLayout(new FlowLayout());
        pane1.add(refreshButton);
        getContentPane().add(pane1);
        JPanel manualPanel = new JPanel();
        manualPanel.setLayout(new FlowLayout());
        manualPanel.add(nextButton);
        manualPanel.add(previousButton);
        manualPanel.add(deleteButton);
        JPanel pane2 = new JPanel();
        pane2.setLayout(new FlowLayout());
        pane2.add(adrTextField);
        JPanel pane3 = new JPanel();
        pane3.setLayout(new FlowLayout());
        pane3.add(CurrentStatus);
        JScrollPane stackPane = new JScrollPane(stackTable);
        stackPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        stackModel.initTable(stackTable, this);
        getContentPane().add(stackPane);
        addHelpMenu("package.jmri.jmrix.lenz.stackmon.StackMonFrame", true);
        pack();
        tc = memo.getXNetTrafficController();
        tc.addXNetListener(~0, this);
    }

    public void addNotify() {
        super.addNotify();
        JMenuBar jMenuBar = getJMenuBar();
        if (jMenuBar != null) {
            int jMenuBarHeight = jMenuBar.getPreferredSize().height;
            Dimension dimension = getSize();
            dimension.height += jMenuBarHeight;
            setSize(dimension);
        }
    }

    private void getAllEntries() {
        stackModel.clearData();
        _getAll = true;
        getNextEntry();
    }

    private void getNextEntry() {
        int address = 0;
        if (!adrTextField.getText().equals("")) address = Integer.parseInt(adrTextField.getText());
        XNetMessage msg = XNetMessage.getNextAddressOnStackMsg(address, true);
        tc.sendXNetMessage(msg, this);
    }

    private void getNextEntry(int address) {
        XNetMessage msg = XNetMessage.getNextAddressOnStackMsg(address, true);
        tc.sendXNetMessage(msg, this);
    }

    private void getPreviousEntry() {
        int address = 0;
        if (!adrTextField.getText().equals("")) address = Integer.parseInt(adrTextField.getText());
        XNetMessage msg = XNetMessage.getNextAddressOnStackMsg(address, false);
        tc.sendXNetMessage(msg, this);
    }

    private void deleteEntry() {
        int address = 0;
        if (!adrTextField.getText().equals("")) {
            address = Integer.parseInt(adrTextField.getText());
            XNetMessage msg = XNetMessage.getDeleteAddressOnStackMsg(address);
            tc.sendXNetMessage(msg, this);
        }
    }

    @SuppressWarnings("unused")
    private void requestStatus() {
        int address = 0;
        if (!adrTextField.getText().equals("")) {
            address = Integer.parseInt(adrTextField.getText());
            XNetMessage msg = XNetMessage.getLocomotiveInfoRequestMsg(address);
            tc.sendXNetMessage(msg, this);
        }
    }

    @SuppressWarnings("unused")
    private void requestFunctionStatus() {
        int address = 0;
        if (!adrTextField.getText().equals("")) {
            address = Integer.parseInt(adrTextField.getText());
            XNetMessage msg = XNetMessage.getLocomotiveFunctionStatusMsg(address);
            tc.sendXNetMessage(msg, this);
        }
    }

    public void message(XNetReply r) {
        if (r.getElement(0) == XNetConstants.LOCO_INFO_RESPONSE) {
            int address = r.getThrottleMsgAddr();
            Integer intAddress = Integer.valueOf(address);
            switch(r.getElement(1)) {
                case XNetConstants.LOCO_SEARCH_RESPONSE_N:
                    CurrentStatus.setText(rb.getString("SearchNormal"));
                    adrTextField.setText("" + address);
                    stackModel.updateData(intAddress, rb.getString("SearchNormal"));
                    if (_getAll) getNextEntry(address);
                    break;
                case XNetConstants.LOCO_SEARCH_RESPONSE_DH:
                    CurrentStatus.setText(rb.getString("SearchDH"));
                    adrTextField.setText("" + r.getThrottleMsgAddr());
                    stackModel.updateData(intAddress, rb.getString("SearchDH"));
                    if (_getAll) getNextEntry(address);
                    break;
                case XNetConstants.LOCO_SEARCH_RESPONSE_MU_BASE:
                    CurrentStatus.setText(rb.getString("SearchMUBase"));
                    adrTextField.setText("" + r.getThrottleMsgAddr());
                    stackModel.updateData(intAddress, rb.getString("SearchMUBase"));
                    if (_getAll) getNextEntry(address);
                    break;
                case XNetConstants.LOCO_SEARCH_RESPONSE_MU:
                    CurrentStatus.setText(rb.getString("SearchMU"));
                    adrTextField.setText("" + r.getThrottleMsgAddr());
                    stackModel.updateData(intAddress, rb.getString("SearchMU"));
                    if (_getAll) getNextEntry(address);
                    break;
                case XNetConstants.LOCO_SEARCH_NO_RESULT:
                    CurrentStatus.setText(rb.getString("SearchFail"));
                    adrTextField.setText("" + r.getThrottleMsgAddr());
                    if (_getAll) _getAll = false;
                    break;
                default:
                    if (log.isDebugEnabled()) log.debug("not search result");
            }
        }
    }

    public void message(XNetMessage m) {
    }

    public void notifyTimeout(XNetMessage msg) {
        if (log.isDebugEnabled()) log.debug("Notified of timeout on message" + msg.toString());
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StackMonFrame.class.getName());
}
