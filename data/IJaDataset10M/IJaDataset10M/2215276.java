package ie.omk.jest;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import javax.swing.*;
import ie.omk.smpp.message.*;
import ie.omk.debug.Debug;

public class MessagePanel extends javax.swing.JPanel implements java.awt.event.MouseListener, java.awt.event.ItemListener, PacketPanel {

    protected JFrame fparent;

    public String serviceType;

    public SmeAddress srcAddr;

    public SmeAddress dstAddr;

    public SmeAddress dstAddrs[];

    public int esmClass;

    public int protocol;

    public boolean priority;

    public Date deliveryTime;

    public Date expiryTime;

    public boolean registered;

    public boolean replaceIfPresent;

    public int dataCoding;

    public int defaultMsg;

    public String message;

    public int msgId;

    public Date finalDate;

    public int msgStatus;

    public int errorCode;

    JPopupMenu pMenu = null;

    TfConvertor tf = null;

    JPanel pl[] = new JPanel[2];

    GridBagLayout gb[] = new GridBagLayout[2];

    GridBagConstraints gbc1 = null;

    GridBagConstraints gbc2 = null;

    JLabel[] label = { new JLabel("Service Type:"), new JLabel("Source:"), new JLabel("Destination:"), new JLabel("ESM Class:"), new JLabel("Protocol Id:"), new JLabel("Delivery Time:"), new JLabel("Expiry Time:"), new JLabel("GSM Data Coding:"), new JLabel("Default Message Id:"), new JLabel("Message Text:"), new JLabel("Message Id:"), new JLabel("Final date:"), new JLabel("Message Status:"), new JLabel("Error code:") };

    JLabel bLabel[] = { new JLabel(""), new JLabel(" "), new JLabel(" "), new JLabel(" ") };

    DefaultListModel listModel = null;

    JTextField f_serviceType;

    AddrPanel f_srcAddr;

    AddrPanel f_dstAddr;

    JScrollPane f_destinations;

    JList f_dstList;

    JButton f_dstButton;

    JTextField f_esmClass;

    JTextField f_protocol;

    JCheckBox f_priority;

    JCheckBox f_deliveryBox;

    JButton f_deliveryTime;

    JPanel f_deliveryPanel;

    JCheckBox f_expiryBox;

    JButton f_expiryTime;

    JPanel f_expiryPanel;

    JCheckBox f_registered;

    JCheckBox f_replaceIfPresent;

    JTextField f_dataCoding;

    JTextField f_defaultMsg;

    JTextArea f_message;

    JScrollPane f_messageScroller;

    JTextField f_msgId;

    JCheckBox f_finalBox;

    JButton f_finalDate;

    JPanel f_finalPanel;

    JTextField f_msgStatus;

    JTextField f_errorCode;

    SMPPPacket pak;

    String timeString = null;

    public static final int SERVICE_TYPE = 0x00000001;

    public static final int SOURCE_ADDR = 0x00000002;

    public static final int DEST_ADDR = 0x00000004;

    public static final int ESM_CLASS = 0x00000008;

    public static final int PROTOCOL_ID = 0x00000010;

    public static final int PRIORITY = 0x00000020;

    public static final int DELIVERY_TIME = 0x00000040;

    public static final int VALID_TIME = 0x00000080;

    public static final int REGISTERED = 0x00000100;

    public static final int REPLACE_IF_P = 0x00000200;

    public static final int DATA_CODING = 0x00000400;

    public static final int DEFAULT_MSG = 0x00000800;

    public static final int MESSAGE = 0x00001000;

    public static final int MSG_ID = 0x00002000;

    public static final int FINAL_DATE = 0x00004000;

    public static final int MSG_STATUS = 0x00008000;

    public static final int MSG_ERROR = 0x00010000;

    public static final int MULTI_DESTS = 0x00020000;

    int packet_flags = 0;

    public MessagePanel(SMPPPacket p, boolean editable) {
        if (p == null) {
            System.err.println("MessagePanel<init>: p is null");
            throw new NullPointerException("Packet cannot be null");
        }
        tf = new TfConvertor();
        pMenu = tf.getTfMenu();
        add(pMenu);
        f_serviceType = new JTextField();
        f_srcAddr = new AddrPanel(true);
        f_dstAddr = new AddrPanel(true);
        f_esmClass = new JTextField();
        f_protocol = new JTextField();
        f_priority = new JCheckBox("Priority delivery");
        f_deliveryBox = new JCheckBox();
        timeString = (editable) ? "Change..." : "View...";
        f_deliveryTime = new JButton(timeString);
        f_deliveryBox.addItemListener(this);
        f_deliveryTime.addMouseListener(this);
        f_deliveryPanel = new JPanel();
        f_deliveryPanel.setLayout(new BorderLayout());
        f_deliveryPanel.add("Center", f_deliveryTime);
        f_deliveryPanel.add("West", f_deliveryBox);
        f_expiryBox = new JCheckBox();
        f_expiryTime = new JButton(timeString);
        f_expiryBox.addItemListener(this);
        f_expiryTime.addMouseListener(this);
        f_expiryPanel = new JPanel();
        f_expiryPanel.setLayout(new BorderLayout());
        f_expiryPanel.add("Center", f_expiryTime);
        f_expiryPanel.add("West", f_expiryBox);
        f_registered = new JCheckBox("Registered delivery");
        f_replaceIfPresent = new JCheckBox("Replace if present");
        f_dataCoding = new JTextField();
        f_defaultMsg = new JTextField();
        f_message = new JTextArea(4, 40);
        f_messageScroller = new JScrollPane(f_message);
        f_msgId = new JTextField();
        f_finalBox = new JCheckBox();
        f_finalDate = new JButton(timeString);
        f_finalBox.addItemListener(this);
        f_finalDate.addMouseListener(this);
        f_finalPanel = new JPanel();
        f_finalPanel.setLayout(new BorderLayout());
        f_finalPanel.add("Center", f_finalDate);
        f_finalPanel.add("West", f_finalBox);
        f_msgStatus = new JTextField();
        f_errorCode = new JTextField();
        listModel = new DefaultListModel();
        f_dstList = new JList(listModel);
        f_destinations = new JScrollPane(f_dstList);
        f_dstList.setPrototypeCellValue(new JLabel("ISDN(0, 0, 000000000000"));
        f_dstList.setVisibleRowCount(4);
        f_dstButton = new JButton("Add Destination");
        f_dstButton.addMouseListener(this);
        f_esmClass.addMouseListener(tf);
        f_protocol.addMouseListener(tf);
        f_dataCoding.addMouseListener(tf);
        f_defaultMsg.addMouseListener(tf);
        f_msgId.addMouseListener(tf);
        f_errorCode.addMouseListener(tf);
        f_msgStatus.addMouseListener(tf);
        f_errorCode.addMouseListener(tf);
        gb[0] = new GridBagLayout();
        gb[1] = new GridBagLayout();
        gbc1 = new GridBagConstraints();
        gbc2 = new GridBagConstraints();
        pl[0] = new JPanel();
        pl[1] = new JPanel();
        pl[0].setLayout(gb[0]);
        pl[1].setLayout(gb[1]);
        gbc1.gridwidth = gbc1.RELATIVE;
        gbc1.fill = gbc1.HORIZONTAL;
        gbc1.weightx = 0.0;
        gbc1.weighty = 0.0;
        gbc2.gridwidth = gbc2.REMAINDER;
        gbc2.fill = gbc2.HORIZONTAL;
        gbc2.weightx = 1.0;
        gbc2.weighty = 1.0;
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        pak = new BindTransmitter(0x01);
        setComponents(p, editable);
    }

    public void setComponents(SMPPPacket p, boolean editable) {
        int oldId = pak.getCommandId();
        packet_flags = SOURCE_ADDR;
        int i = 0, loop = 0;
        if (p == null) throw new NullPointerException("Packet cannot be null");
        pak = p;
        i = p.getCommandId();
        if (i == SMPPPacket.ESME_SUB_SM) {
            packet_flags |= (SERVICE_TYPE | DEST_ADDR | PROTOCOL_ID | PRIORITY | DELIVERY_TIME | VALID_TIME | REGISTERED | REPLACE_IF_P | DATA_CODING | DEFAULT_MSG | MESSAGE);
            SubmitSM s = (SubmitSM) p;
            serviceType = s.getServiceType();
            srcAddr = s.getSource();
            dstAddr = s.getDestination();
            protocol = s.getProtocolId();
            priority = s.isPriority();
            deliveryTime = s.getDeliveryTime();
            expiryTime = s.getExpiryTime();
            registered = s.isRegistered();
            replaceIfPresent = s.isReplaceIfPresent();
            dataCoding = s.getDataCoding();
            defaultMsg = s.getDefaultMsgId();
            message = s.getMessageText();
        } else if (i == SMPPPacket.ESME_SUB_MULTI) {
            packet_flags |= (SERVICE_TYPE | MULTI_DESTS | PROTOCOL_ID | PRIORITY | DELIVERY_TIME | VALID_TIME | REGISTERED | REPLACE_IF_P | DATA_CODING | DEFAULT_MSG | MESSAGE);
            SubmitMulti s = (SubmitMulti) p;
            serviceType = s.getServiceType();
            srcAddr = s.getSource();
            dstAddrs = s.getDestAddresses();
            protocol = s.getProtocolId();
            priority = s.isPriority();
            deliveryTime = s.getDeliveryTime();
            expiryTime = s.getExpiryTime();
            registered = s.isRegistered();
            replaceIfPresent = s.isReplaceIfPresent();
            dataCoding = s.getDataCoding();
            defaultMsg = s.getDefaultMsgId();
            message = s.getMessageText();
        } else if (i == SMPPPacket.SMSC_DELIVER_SM) {
            packet_flags |= (SERVICE_TYPE | DEST_ADDR | ESM_CLASS | PROTOCOL_ID | DATA_CODING | MESSAGE);
            DeliverSM s = (DeliverSM) p;
            serviceType = s.getServiceType();
            srcAddr = s.getSource();
            dstAddr = s.getDestination();
            esmClass = s.getEsmClass();
            protocol = s.getProtocolId();
            dataCoding = s.getDataCoding();
            message = s.getMessageText();
        } else if (i == SMPPPacket.ESME_QUERY_SM_RESP) {
            packet_flags |= (MSG_ID | FINAL_DATE | MSG_STATUS | MSG_ERROR);
            QuerySMResp s = (QuerySMResp) p;
            msgId = s.getMessageId();
            finalDate = s.getFinalDate();
            msgStatus = s.getMessageStatus();
            errorCode = s.getErrorCode();
        } else if (i == SMPPPacket.ESME_QUERY_MSG_DETAILS_RESP) {
            packet_flags |= (SERVICE_TYPE | MULTI_DESTS | PROTOCOL_ID | PRIORITY | DELIVERY_TIME | VALID_TIME | REGISTERED | DATA_CODING | MESSAGE | MSG_ID | FINAL_DATE | MSG_STATUS | MSG_ERROR);
            QueryMsgDetailsResp s = (QueryMsgDetailsResp) p;
            serviceType = s.getServiceType();
            srcAddr = s.getSource();
            dstAddrs = s.getDestAddresses();
            protocol = s.getProtocolId();
            priority = s.isPriority();
            deliveryTime = s.getDeliveryTime();
            expiryTime = s.getExpiryTime();
            registered = s.isRegistered();
            dataCoding = s.getDataCoding();
            message = s.getMessageText();
            msgId = s.getMessageId();
            finalDate = s.getFinalDate();
            msgStatus = s.getMessageStatus();
            errorCode = s.getErrorCode();
        } else if (i == SMPPPacket.ESME_CANCEL_SM) {
            packet_flags |= (SERVICE_TYPE | MSG_ID | DEST_ADDR);
            CancelSM s = (CancelSM) p;
            serviceType = s.getServiceType();
            srcAddr = s.getSource();
            dstAddr = s.getDestination();
            msgId = s.getMessageId();
        } else if (i == SMPPPacket.ESME_REPLACE_SM) {
            packet_flags |= (MSG_ID | DELIVERY_TIME | VALID_TIME | REGISTERED | DEFAULT_MSG | MESSAGE);
            ReplaceSM s = (ReplaceSM) p;
            srcAddr = s.getSource();
            deliveryTime = s.getDeliveryTime();
            expiryTime = s.getExpiryTime();
            registered = s.isRegistered();
            message = s.getMessageText();
            msgId = s.getMessageId();
            defaultMsg = s.getDefaultMsgId();
        }
        f_dstAddr.setAddr(dstAddr);
        f_srcAddr.setAddr(srcAddr);
        if (dstAddrs != null) for (loop = 0; loop < dstAddrs.length; loop++) listModel.addElement(dstAddrs[loop]);
        f_serviceType.setText(serviceType);
        f_esmClass.setText(String.valueOf(esmClass));
        f_protocol.setText(String.valueOf(protocol));
        f_dataCoding.setText(String.valueOf(dataCoding));
        f_defaultMsg.setText(String.valueOf(defaultMsg));
        f_msgId.setText("0x" + Integer.toHexString(msgId));
        f_msgStatus.setText(String.valueOf(msgStatus));
        f_errorCode.setText(String.valueOf(errorCode));
        f_message.setText(message);
        if (i == oldId) return;
        f_serviceType.setEditable(editable);
        f_esmClass.setEditable(editable);
        f_protocol.setEditable(editable);
        f_priority.setEnabled(editable);
        if (deliveryTime != null) f_deliveryBox.setSelected(true); else f_deliveryTime.setEnabled(false);
        f_deliveryBox.setEnabled(editable);
        if (expiryTime != null) f_expiryBox.setSelected(true); else f_expiryTime.setEnabled(false);
        f_expiryBox.setEnabled(editable);
        f_registered.setEnabled(editable);
        f_replaceIfPresent.setEnabled(editable);
        f_dataCoding.setEditable(editable);
        f_defaultMsg.setEditable(editable);
        f_message.setEditable(editable);
        f_msgId.setEditable(editable);
        if (finalDate != null) f_finalBox.setSelected(true); else f_finalDate.setEnabled(false);
        f_finalBox.setEnabled(editable);
        f_msgStatus.setEditable(editable);
        f_errorCode.setEditable(editable);
        removeAll();
        pl[0].removeAll();
        pl[1].removeAll();
        add(pMenu);
        int curLayout = 0;
        if ((packet_flags & SOURCE_ADDR) != 0) {
            gb[curLayout].setConstraints(label[1], gbc1);
            gb[curLayout].setConstraints(f_srcAddr, gbc2);
            pl[curLayout].add(label[1]);
            pl[curLayout].add(f_srcAddr);
            curLayout = 1 - curLayout;
        }
        if ((packet_flags & DEST_ADDR) != 0) {
            gb[curLayout].setConstraints(label[2], gbc1);
            gb[curLayout].setConstraints(f_dstAddr, gbc2);
            pl[curLayout].add(label[2]);
            pl[curLayout].add(f_dstAddr);
            curLayout = 1 - curLayout;
        }
        if ((packet_flags & MULTI_DESTS) != 0) {
            int fill = gbc2.fill;
            int anchor = gbc2.anchor;
            gbc2.fill = gbc2.BOTH;
            gbc2.anchor = gbc2.NORTH;
            JLabel blankLabel = new JLabel(" ");
            gb[curLayout].setConstraints(label[2], gbc1);
            gb[curLayout].setConstraints(f_destinations, gbc2);
            pl[curLayout].add(label[2]);
            pl[curLayout].add(f_destinations);
            if (editable) {
                gbc2.fill = gbc2.HORIZONTAL;
                gbc2.anchor = gbc2.NORTH;
                gb[curLayout].setConstraints(blankLabel, gbc1);
                gb[curLayout].setConstraints(f_dstButton, gbc2);
                pl[curLayout].add(blankLabel);
                pl[curLayout].add(f_dstButton);
            }
            gbc2.fill = fill;
            gbc2.anchor = anchor;
            curLayout = 1 - curLayout;
        }
        if ((packet_flags & SERVICE_TYPE) != 0) {
            gb[curLayout].setConstraints(label[0], gbc1);
            gb[curLayout].setConstraints(f_serviceType, gbc2);
            pl[curLayout].add(label[0]);
            pl[curLayout].add(f_serviceType);
            curLayout = 1 - curLayout;
        }
        if ((packet_flags & ESM_CLASS) != 0) {
            gb[curLayout].setConstraints(label[3], gbc1);
            gb[curLayout].setConstraints(f_esmClass, gbc2);
            pl[curLayout].add(label[3]);
            pl[curLayout].add(f_esmClass);
            curLayout = 1 - curLayout;
        }
        if ((packet_flags & PROTOCOL_ID) != 0) {
            gb[curLayout].setConstraints(label[4], gbc1);
            gb[curLayout].setConstraints(f_protocol, gbc2);
            pl[curLayout].add(label[4]);
            pl[curLayout].add(f_protocol);
            curLayout = 1 - curLayout;
        }
        if ((packet_flags & VALID_TIME) != 0) {
            gb[curLayout].setConstraints(label[6], gbc1);
            gb[curLayout].setConstraints(f_expiryPanel, gbc2);
            pl[curLayout].add(label[6]);
            pl[curLayout].add(f_expiryPanel);
            curLayout = 1 - curLayout;
        }
        if ((packet_flags & DELIVERY_TIME) != 0) {
            gb[curLayout].setConstraints(label[5], gbc1);
            gb[curLayout].setConstraints(f_deliveryPanel, gbc2);
            pl[curLayout].add(label[5]);
            pl[curLayout].add(f_deliveryPanel);
            curLayout = 1 - curLayout;
        }
        if ((packet_flags & DATA_CODING) != 0) {
            gb[curLayout].setConstraints(label[7], gbc1);
            gb[curLayout].setConstraints(f_dataCoding, gbc2);
            pl[curLayout].add(label[7]);
            pl[curLayout].add(f_dataCoding);
            curLayout = 1 - curLayout;
        }
        if ((packet_flags & DEFAULT_MSG) != 0) {
            gb[curLayout].setConstraints(label[8], gbc1);
            gb[curLayout].setConstraints(f_defaultMsg, gbc2);
            pl[curLayout].add(label[8]);
            pl[curLayout].add(f_defaultMsg);
            curLayout = 1 - curLayout;
        }
        if ((packet_flags & MSG_ID) != 0) {
            gb[curLayout].setConstraints(label[10], gbc1);
            gb[curLayout].setConstraints(f_msgId, gbc2);
            pl[curLayout].add(label[10]);
            pl[curLayout].add(f_msgId);
            curLayout = 1 - curLayout;
        }
        if ((packet_flags & FINAL_DATE) != 0) {
            gb[curLayout].setConstraints(label[11], gbc1);
            gb[curLayout].setConstraints(f_finalPanel, gbc2);
            pl[curLayout].add(label[11]);
            pl[curLayout].add(f_finalPanel);
            curLayout = 1 - curLayout;
        }
        if ((packet_flags & MSG_STATUS) != 0) {
            gb[curLayout].setConstraints(label[12], gbc1);
            gb[curLayout].setConstraints(f_msgStatus, gbc2);
            pl[curLayout].add(label[12]);
            pl[curLayout].add(f_msgStatus);
            curLayout = 1 - curLayout;
        }
        if ((packet_flags & MSG_ERROR) != 0) {
            gb[curLayout].setConstraints(label[13], gbc1);
            gb[curLayout].setConstraints(f_errorCode, gbc2);
            pl[curLayout].add(label[13]);
            pl[curLayout].add(f_errorCode);
            curLayout = 1 - curLayout;
        }
        if (curLayout == 1) {
            gb[1].setConstraints(bLabel[0], gbc2);
            pl[1].add(bLabel[0]);
        }
        if ((packet_flags & PRIORITY) != 0) {
            f_priority.setSelected(priority);
            gb[0].setConstraints(bLabel[1], gbc2);
            gb[1].setConstraints(f_priority, gbc2);
            pl[0].add(bLabel[1]);
            pl[1].add(f_priority);
        }
        if ((packet_flags & REGISTERED) != 0) {
            f_registered.setSelected(registered);
            gb[0].setConstraints(bLabel[2], gbc2);
            gb[1].setConstraints(f_registered, gbc2);
            pl[0].add(bLabel[2]);
            pl[1].add(f_registered);
        }
        if ((packet_flags & REPLACE_IF_P) != 0) {
            f_replaceIfPresent.setSelected(replaceIfPresent);
            gb[curLayout].setConstraints(f_replaceIfPresent, gbc2);
            gb[0].setConstraints(bLabel[3], gbc2);
            gb[1].setConstraints(f_replaceIfPresent, gbc2);
            pl[0].add(bLabel[3]);
            pl[1].add(f_replaceIfPresent);
        }
        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(1, 2));
        p1.add(pl[0]);
        p1.add(pl[1]);
        setLayout(new BorderLayout());
        add("Center", p1);
        if ((packet_flags & MESSAGE) != 0) {
            add("South", f_messageScroller);
        }
    }

    public void setDialogParent(JFrame f) {
        if (f == null) return;
        fparent = f;
    }

    public void getDate(Date d, int id, Point pos) {
        if (fparent == null) return;
        fparent.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        DateDialog dlg = new DateDialog(fparent, d);
        dlg.setModal(true);
        dlg.setBounds(pos.x, pos.y, 300, 150);
        dlg.setVisible(true);
        d = dlg.getDate();
        dlg.dispose();
        if (d == null) {
            JOptionPane.showMessageDialog(this, "The date you typed is invalid", "Date Error", JOptionPane.ERROR_MESSAGE);
        } else {
            switch(id) {
                case 1:
                    deliveryTime = d;
                    break;
                case 2:
                    expiryTime = d;
                    break;
                case 3:
                    finalDate = d;
                    break;
            }
        }
        fparent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        return;
    }

    public void addSme(Point pos) {
        if (fparent == null) return;
        SmeAddress addr = null;
        AddrDialog dlg = new AddrDialog(fparent);
        dlg.setModal(true);
        dlg.setBounds(pos.x, pos.y, 400, 300);
        dlg.setTitle("Add an address");
        dlg.setVisible(true);
        addr = dlg.getAddr();
        if (addr == null || addr.addr == null) return;
        listModel.addElement(addr);
        dlg.dispose();
    }

    public void mouseClicked(MouseEvent e) {
        Object o = e.getSource();
        if (o.equals(f_deliveryTime)) getDate(deliveryTime, 1, f_deliveryTime.getLocationOnScreen()); else if (o.equals(f_expiryTime)) getDate(expiryTime, 2, f_expiryTime.getLocationOnScreen()); else if (o.equals(f_finalDate)) getDate(finalDate, 3, f_finalDate.getLocationOnScreen()); else if (o.equals(f_dstButton)) addSme(f_dstButton.getLocationOnScreen()); else return;
    }

    public void itemStateChanged(ItemEvent e) {
        Object o = e.getSource();
        if (o.equals(f_deliveryBox)) f_deliveryTime.setEnabled(f_deliveryBox.isSelected()); else if (o.equals(f_expiryBox)) f_expiryTime.setEnabled(f_expiryBox.isSelected()); else if (o.equals(f_finalBox)) f_finalDate.setEnabled(f_finalBox.isSelected());
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public boolean fillFields() {
        boolean fieldsOk = true;
        int loop = 0;
        if ((packet_flags & SERVICE_TYPE) != 0) {
            serviceType = f_serviceType.getText();
            if (serviceType.equals("")) serviceType = null;
        }
        if ((packet_flags & SOURCE_ADDR) != 0) {
            f_srcAddr.fillFields();
            srcAddr = f_srcAddr.addr;
        }
        if ((packet_flags & DEST_ADDR) != 0) {
            f_dstAddr.fillFields();
            dstAddr = f_dstAddr.addr;
        }
        if ((packet_flags & ESM_CLASS) != 0) {
            try {
                String s = f_esmClass.getText();
                if (s.startsWith("0x")) esmClass = Integer.parseInt(s.substring(2), 16); else esmClass = Integer.parseInt(s, 10);
            } catch (NumberFormatException nx) {
                fieldsOk = false;
            }
        }
        if ((packet_flags & PROTOCOL_ID) != 0) {
            try {
                String s = f_protocol.getText();
                if (s.startsWith("0x")) protocol = Integer.parseInt(s.substring(2), 16); else protocol = Integer.parseInt(s, 10);
            } catch (NumberFormatException nx) {
                fieldsOk = false;
            }
        }
        if ((packet_flags & PRIORITY) != 0) {
            priority = f_priority.isSelected();
        }
        if ((packet_flags & DELIVERY_TIME) != 0) {
        }
        if ((packet_flags & VALID_TIME) != 0) {
        }
        if ((packet_flags & REGISTERED) != 0) {
            registered = f_registered.isSelected();
        }
        if ((packet_flags & REPLACE_IF_P) != 0) {
            replaceIfPresent = f_replaceIfPresent.isSelected();
        }
        if ((packet_flags & DATA_CODING) != 0) {
            try {
                String s = f_dataCoding.getText();
                if (s.startsWith("0x")) dataCoding = Integer.parseInt(s.substring(2), 16); else dataCoding = Integer.parseInt(s, 10);
            } catch (NumberFormatException nx) {
                fieldsOk = false;
            }
        }
        if ((packet_flags & DEFAULT_MSG) != 0) {
            try {
                String s = f_defaultMsg.getText();
                if (s.startsWith("0x")) defaultMsg = Integer.parseInt(s.substring(2), 16); else defaultMsg = Integer.parseInt(s, 10);
            } catch (NumberFormatException nx) {
                fieldsOk = false;
            }
        }
        if ((packet_flags & MESSAGE) != 0) {
            message = f_message.getText();
            if (message.equals("")) message = null;
        }
        if ((packet_flags & MSG_ID) != 0) {
            try {
                String s = f_msgId.getText();
                if (s.startsWith("0x")) msgId = (int) Long.parseLong(s.substring(2), 16); else msgId = (int) Long.parseLong(s, 10);
            } catch (NumberFormatException nx) {
                fieldsOk = false;
            }
        }
        if ((packet_flags & FINAL_DATE) != 0) {
        }
        if ((packet_flags & MSG_STATUS) != 0) {
            try {
                String s = f_msgStatus.getText();
                if (s.startsWith("0x")) msgStatus = Integer.parseInt(s.substring(2), 16); else msgStatus = Integer.parseInt(s);
            } catch (NumberFormatException nx) {
                fieldsOk = false;
            }
        }
        if ((packet_flags & MSG_ERROR) != 0) {
            try {
                String s = f_errorCode.getText();
                if (s.startsWith("0x")) errorCode = Integer.parseInt(s.substring(2), 16); else errorCode = Integer.parseInt(s);
            } catch (NumberFormatException nx) {
                fieldsOk = false;
            }
        }
        if ((packet_flags & MULTI_DESTS) != 0) {
            int size = listModel.size();
            dstAddrs = new SmeAddress[size];
            for (loop = 0; loop < size; loop++) dstAddrs[loop] = (SmeAddress) listModel.elementAt(loop);
        }
        return fieldsOk;
    }

    public SMPPPacket getPacket() {
        if (!fillFields()) return null;
        switch(pak.getCommandId()) {
            case SMPPPacket.ESME_SUB_SM:
                SubmitSM s = new SubmitSM(pak.getSeqNo());
                s.setServiceType(serviceType);
                s.setSource(srcAddr);
                s.setDestination(dstAddr);
                s.setProtocolId(protocol);
                s.setPriority(priority);
                s.setRegistered(registered);
                s.setDeliveryTime(deliveryTime);
                s.setExpiryTime(expiryTime);
                s.setReplaceIfPresent(replaceIfPresent);
                s.setDataCoding(dataCoding);
                s.setDefaultMsg(defaultMsg);
                s.setMessageText(message);
                return s;
            case SMPPPacket.SMSC_DELIVER_SM:
                DeliverSM s1 = new DeliverSM(pak.getSeqNo());
                s1.setServiceType(serviceType);
                s1.setSource(srcAddr);
                s1.setDestination(dstAddr);
                s1.setProtocolId(protocol);
                s1.setEsmClass(esmClass);
                s1.setDataCoding(dataCoding);
                s1.setMessageText(message);
                return s1;
            case SMPPPacket.ESME_SUB_MULTI:
                SubmitMulti s2 = new SubmitMulti(pak.getSeqNo());
                s2.setServiceType(serviceType);
                s2.setSource(srcAddr);
                s2.setProtocolId(protocol);
                s2.setPriority(priority);
                s2.setRegistered(registered);
                s2.setDeliveryTime(deliveryTime);
                s2.setExpiryTime(expiryTime);
                s2.setReplaceIfPresent(replaceIfPresent);
                s2.setDataCoding(dataCoding);
                s2.setDefaultMsg(defaultMsg);
                s2.setMessageText(message);
                s2.setDestAddresses(dstAddrs);
                return s2;
            case SMPPPacket.ESME_REPLACE_SM:
                ReplaceSM s3 = new ReplaceSM(pak.getSeqNo());
                s3.setSource(srcAddr);
                s3.setMessageId(msgId);
                s3.setDeliveryTime(deliveryTime);
                s3.setExpiryTime(expiryTime);
                s3.setRegistered(registered);
                s3.setDefaultMsg(defaultMsg);
                s3.setMessageText(message);
                return s3;
            case SMPPPacket.ESME_CANCEL_SM:
                CancelSM s4 = new CancelSM(pak.getSeqNo());
                s4.setServiceType(serviceType);
                s4.setMessageId(msgId);
                s4.setSource(srcAddr);
                s4.setDestination(dstAddr);
                return s4;
            case SMPPPacket.ESME_QUERY_SM_RESP:
                QuerySMResp s5 = new QuerySMResp(pak.getSeqNo());
                s5.setMessageId(msgId);
                s5.setFinalDate(finalDate);
                s5.setMessageStatus(msgStatus);
                s5.setErrorCode(errorCode);
                return s5;
            case SMPPPacket.ESME_QUERY_MSG_DETAILS_RESP:
                QueryMsgDetailsResp s6 = new QueryMsgDetailsResp(pak.getSeqNo());
                s6.setServiceType(serviceType);
                s6.setSource(srcAddr);
                s6.setProtocolId(protocol);
                s6.setDeliveryTime(deliveryTime);
                s6.setExpiryTime(expiryTime);
                s6.setPriority(priority);
                s6.setRegistered(registered);
                s6.setDataCoding(dataCoding);
                s6.setMessageText(message);
                s6.setMessageId(msgId);
                s6.setFinalDate(finalDate);
                s6.setMessageStatus(msgStatus);
                s6.setErrorCode(errorCode);
                s6.setDestAddresses(dstAddrs);
                return s6;
            default:
                return null;
        }
    }
}
