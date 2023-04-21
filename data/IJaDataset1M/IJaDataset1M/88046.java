package com.ufnasoft.dms.admin;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
import com.ufnasoft.dms.gui.DOMUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import com.ufnasoft.dms.gui.*;

public class EditUserDialog extends InitDMS {

    DmsEncryptDecrypt dmsEncDec = new DmsEncryptDecrypt();

    JDialog dialog;

    Container c;

    GridBagConstraints secondPanelConstraints;

    GridBagConstraints containerConstraints = new GridBagConstraints();

    JPanel secondPanel = new JPanel();

    JLabel lblSelectUser = new JLabel("Select User");

    JLabel lblMachineName = new JLabel("Machine Name");

    JLabel lblEmail = new JLabel("Email");

    JLabel lblPassword = new JLabel("Password");

    JLabel lblFirstName = new JLabel("First Name");

    JLabel lblLastName = new JLabel("Last Name");

    JLabel lblDMMethod = new JLabel("Default Message Method");

    JComboBox cmbSelectUser;

    JComboBox cmbDMMethod;

    JPasswordField pswPassword = new JPasswordField(20);

    JTextField txtMachineName = new JTextField(20);

    JTextField txtEmail = new JTextField(20);

    JTextField txtFirstName = new JTextField(20);

    JTextField txtLastName = new JTextField(20);

    Hashtable selectUsersHash = new Hashtable();

    Hashtable messageMethodHash = new Hashtable();

    JButton butApply = new JButton("Apply");

    JButton butClose = new JButton("Close");

    JPanel userPanel;

    String strUser;

    String strPreviousUser = "";

    public void applyFont() {
        lblSelectUser.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblMachineName.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblFirstName.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblLastName.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblDMMethod.setFont(new Font("Tahoma", Font.PLAIN, 11));
    }

    public JPanel makeSelectPanel() {
        JPanel selectPanel = new JPanel();
        selectPanel.setLayout(new GridBagLayout());
        GridBagConstraints selectPanelConstraints = new GridBagConstraints();
        selectPanelConstraints.insets = new Insets(1, 1, 1, 1);
        selectPanelConstraints.gridx = 0;
        selectPanelConstraints.gridy = 0;
        selectPanelConstraints.anchor = GridBagConstraints.EAST;
        selectPanel.add(lblSelectUser, selectPanelConstraints);
        cmbSelectUser = comboSelectUser();
        cmbSelectUser.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                String strUserName = (String) cmbSelectUser.getSelectedItem();
                strPreviousUser = strUserName;
                int intUserId = 0;
                try {
                    intUserId = (new Integer((String) selectUsersHash.get(strUserName))).intValue();
                } catch (NumberFormatException e) {
                    System.out.println(e);
                }
                c.remove(userPanel);
                userPanel = makeUserPanel(intUserId);
                containerConstraints.gridx = 0;
                containerConstraints.gridy = 2;
                containerConstraints.anchor = GridBagConstraints.CENTER;
                c.add(userPanel, containerConstraints);
                userPanel.revalidate();
            }
        });
        selectPanelConstraints.gridx = 1;
        selectPanelConstraints.gridy = 0;
        selectPanelConstraints.anchor = GridBagConstraints.EAST;
        selectPanel.add(cmbSelectUser, selectPanelConstraints);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(selectPanel, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(390, 60));
        TitledBorder tbSelectPanel = new TitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Select User");
        panel.setBorder(tbSelectPanel);
        return panel;
    }

    String strMachineName = "";

    String strEmail = "";

    String strPassword = "";

    String strFirstName = "";

    String strLastName = "";

    int intMMethodId = 0;

    public JPanel makeUserPanel(int intUserId) {
        String urlString = dms_url + "/servlet/com.ufnasoft.dms.server.ServerGetUserById?username=" + username + "&key=" + key + "&userid=" + intUserId;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            URL u = new URL(urlString);
            URLConnection uc = u.openConnection();
            InputStream inputstream = uc.getInputStream();
            int length = uc.getContentLength();
            String xmlPacket = "";
            if (length != -1) {
                byte incomingData[] = new byte[length];
                inputstream.read(incomingData);
                xmlPacket = new String(incomingData);
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(xmlPacket.getBytes());
            Document document = parser.parse(bais);
            NodeList nodelist = document.getElementsByTagName("user");
            int num = nodelist.getLength();
            for (int i = 0; i < num; i++) {
                strMachineName = DOMUtil.getSimpleElementText((Element) nodelist.item(i), "machinename");
                strEmail = DOMUtil.getSimpleElementText((Element) nodelist.item(i), "email");
                strPassword = DOMUtil.getSimpleElementText((Element) nodelist.item(i), "password");
                strPassword = dmsEncDec.decrypt(strPassword);
                if (isDebug.equalsIgnoreCase("yes")) {
                    System.out.println("strPassword:" + strPassword);
                }
                strFirstName = DOMUtil.getSimpleElementText((Element) nodelist.item(i), "firstname");
                strLastName = DOMUtil.getSimpleElementText((Element) nodelist.item(i), "lastname");
                intMMethodId = (new Integer(DOMUtil.getSimpleElementText((Element) nodelist.item(i), "defaultmessagemethodid"))).intValue();
            }
        } catch (MalformedURLException ex) {
            System.out.println(ex);
        } catch (ParserConfigurationException ex) {
            System.out.println(ex);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        txtMachineName.setText(strMachineName);
        txtEmail.setText(strEmail);
        pswPassword.setText(strPassword);
        txtFirstName.setText(strFirstName);
        txtLastName.setText(strLastName);
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new GridBagLayout());
        GridBagConstraints userPanelConstraints = new GridBagConstraints();
        userPanelConstraints.insets = new Insets(6, 6, 6, 6);
        userPanelConstraints.gridx = 0;
        userPanelConstraints.gridy = 0;
        userPanelConstraints.anchor = GridBagConstraints.EAST;
        userPanel.add(lblMachineName, userPanelConstraints);
        userPanelConstraints.gridx = 1;
        userPanelConstraints.gridy = 0;
        userPanelConstraints.anchor = GridBagConstraints.WEST;
        userPanel.add(txtMachineName, userPanelConstraints);
        userPanelConstraints.gridx = 0;
        userPanelConstraints.gridy = 1;
        userPanelConstraints.anchor = GridBagConstraints.EAST;
        userPanel.add(lblEmail, userPanelConstraints);
        userPanelConstraints.gridx = 1;
        userPanelConstraints.gridy = 1;
        userPanelConstraints.anchor = GridBagConstraints.WEST;
        userPanel.add(txtEmail, userPanelConstraints);
        userPanelConstraints.gridx = 0;
        userPanelConstraints.gridy = 2;
        userPanelConstraints.anchor = GridBagConstraints.EAST;
        userPanel.add(lblPassword, userPanelConstraints);
        userPanelConstraints.gridx = 1;
        userPanelConstraints.gridy = 2;
        userPanelConstraints.anchor = GridBagConstraints.WEST;
        userPanel.add(pswPassword, userPanelConstraints);
        userPanelConstraints.gridx = 0;
        userPanelConstraints.gridy = 3;
        userPanelConstraints.anchor = GridBagConstraints.EAST;
        userPanel.add(lblFirstName, userPanelConstraints);
        userPanelConstraints.gridx = 1;
        userPanelConstraints.gridy = 3;
        userPanelConstraints.anchor = GridBagConstraints.WEST;
        userPanel.add(txtFirstName, userPanelConstraints);
        userPanelConstraints.gridx = 0;
        userPanelConstraints.gridy = 4;
        userPanelConstraints.anchor = GridBagConstraints.EAST;
        userPanel.add(lblLastName, userPanelConstraints);
        userPanelConstraints.gridx = 1;
        userPanelConstraints.gridy = 4;
        userPanelConstraints.anchor = GridBagConstraints.WEST;
        userPanel.add(txtLastName, userPanelConstraints);
        userPanelConstraints.gridx = 0;
        userPanelConstraints.gridy = 5;
        userPanelConstraints.anchor = GridBagConstraints.EAST;
        userPanel.add(lblDMMethod, userPanelConstraints);
        userPanelConstraints.gridx = 1;
        userPanelConstraints.gridy = 5;
        userPanelConstraints.anchor = GridBagConstraints.WEST;
        cmbDMMethod = comboDMMethod(intMMethodId);
        userPanel.add(comboDMMethod(intMMethodId), userPanelConstraints);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(userPanel, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(390, 240));
        TitledBorder tbSelectPanel = new TitledBorder(BorderFactory.createLineBorder(Color.GRAY), "User");
        panel.setBorder(tbSelectPanel);
        return panel;
    }

    int selectedGroupId = 0;

    int selectedUserId = 0;

    public JPanel makeButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        butApply.setPreferredSize(new Dimension(100, 25));
        butClose.setPreferredSize(new Dimension(100, 25));
        butApply.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                strMachineName = txtMachineName.getText();
                strPassword = pswPassword.getText();
                strPassword = dmsEncDec.encrypt(strPassword);
                strFirstName = new String(txtFirstName.getText());
                strLastName = txtLastName.getText();
                String strDefaultmessagemethodid = (String) cmbDMMethod.getSelectedItem();
                try {
                    intMMethodId = (new Integer((String) messageMethodHash.get(strDefaultmessagemethodid))).intValue();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                String strUser = (String) cmbSelectUser.getSelectedItem();
                try {
                    selectedUserId = (new Integer((String) selectUsersHash.get(strUser))).intValue();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                String urlString = dms_url + "/servlet/com.ufnasoft.dms.server.ServerUpdateUserByUserId";
                String rvalue = "";
                try {
                    String urldata = urlString + "?username=" + URLEncoder.encode(username, "UTF-8") + "&key=" + URLEncoder.encode(key, "UTF-8") + "&machinename=" + URLEncoder.encode(strMachineName, "UTF-8") + "&password=" + URLEncoder.encode(strPassword, "UTF-8") + "&firstname=" + URLEncoder.encode(strFirstName, "UTF-8") + "&lastname=" + URLEncoder.encode(strLastName, "UTF-8") + "&defaultmessagemethodid=" + intMMethodId + "&userid=" + selectedUserId;
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder parser = factory.newDocumentBuilder();
                    URL u = new URL(urldata);
                    URLConnection uc = u.openConnection();
                    InputStream inputstream = uc.getInputStream();
                    int length = uc.getContentLength();
                    String xmlPacket = "";
                    if (length != -1) {
                        byte incomingData[] = new byte[length];
                        inputstream.read(incomingData);
                        xmlPacket = new String(incomingData);
                    }
                    ByteArrayInputStream bais = new ByteArrayInputStream(xmlPacket.getBytes());
                    Document document = parser.parse(bais);
                    NodeList nodelist = document.getElementsByTagName("dms");
                    int num = nodelist.getLength();
                    for (int i = 0; i < num; i++) {
                        rvalue = DOMUtil.getSimpleElementText((Element) nodelist.item(i), "rvalue");
                    }
                } catch (MalformedURLException ex) {
                    System.out.println(ex);
                } catch (ParserConfigurationException ex) {
                    System.out.println(ex);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });
        buttonsPanel.add(butApply);
        butClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                dialog.dispose();
            }
        });
        buttonsPanel.add(butClose);
        return buttonsPanel;
    }

    public void makeDialog(JFrame frame) {
        applyFont();
        dialog = new JDialog(frame, "Edit User", true);
        dialog.setSize(408, 474);
        c = dialog.getContentPane();
        final ImageIcon icon = new ImageIcon(getClass().getResource("/com/ufnasoft/dms/images/banners/edituser.gif"));
        JPanel bannerPanel = new JPanel() {

            protected void paintComponent(Graphics g) {
                g.drawImage(icon.getImage(), 0, 0, null);
                super.paintComponent(g);
            }
        };
        Container c = dialog.getContentPane();
        bannerPanel.setLayout(new GridBagLayout());
        GridBagConstraints bannerConstraints = new GridBagConstraints();
        bannerConstraints.insets = new Insets(1, 1, 1, 1);
        JLabel bannerHead = new JLabel("Edit User");
        bannerHead.setFont(new Font("Tahoma", Font.BOLD, 13));
        JLabel bannerD = new JLabel("");
        bannerD.setFont(new Font("Tahoma", Font.PLAIN, 11));
        bannerConstraints.gridx = 0;
        bannerConstraints.gridy = 0;
        bannerConstraints.anchor = GridBagConstraints.NORTHWEST;
        bannerPanel.add(bannerHead, bannerConstraints);
        bannerConstraints.gridx = 0;
        bannerConstraints.gridy = 1;
        bannerConstraints.anchor = GridBagConstraints.CENTER;
        bannerPanel.add(bannerD, bannerConstraints);
        bannerPanel.setMinimumSize(new Dimension(400, 70));
        bannerPanel.setPreferredSize(new Dimension(400, 70));
        bannerPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        bannerPanel.setOpaque(false);
        c.setLayout(new GridBagLayout());
        containerConstraints.insets = new Insets(1, 1, 1, 1);
        ;
        containerConstraints.gridx = 0;
        containerConstraints.gridy = 0;
        containerConstraints.anchor = GridBagConstraints.WEST;
        c.add(bannerPanel, containerConstraints);
        containerConstraints.gridx = 0;
        containerConstraints.gridy = 1;
        containerConstraints.anchor = GridBagConstraints.WEST;
        c.add(makeSelectPanel(), containerConstraints);
        String strUser = (String) cmbSelectUser.getSelectedItem();
        try {
            selectedUserId = (new Integer((String) selectUsersHash.get(strUser))).intValue();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        containerConstraints.gridx = 0;
        containerConstraints.gridy = 2;
        containerConstraints.anchor = GridBagConstraints.CENTER;
        userPanel = makeUserPanel(selectedUserId);
        c.add(userPanel, containerConstraints);
        containerConstraints.gridx = 0;
        containerConstraints.gridy = 3;
        containerConstraints.anchor = GridBagConstraints.CENTER;
        c.add(makeButtonsPanel(), containerConstraints);
        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    public JComboBox comboSelectUser() {
        String urlString = dms_url + "/servlet/com.ufnasoft.dms.server.ServerGetUsers?username=" + username + "&key=" + key;
        System.out.println(urlString);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            URL u = new URL(urlString);
            URLConnection uc = u.openConnection();
            InputStream inputstream = uc.getInputStream();
            int length = uc.getContentLength();
            String xmlPacket = "";
            if (length != -1) {
                byte incomingData[] = new byte[length];
                inputstream.read(incomingData);
                xmlPacket = new String(incomingData);
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(xmlPacket.getBytes());
            Document document = parser.parse(bais);
            NodeList nodelist = document.getElementsByTagName("user");
            int num = nodelist.getLength();
            String[] usersTitle = new String[num];
            for (int i = 0; i < num; i++) {
                String userid = DOMUtil.getSimpleElementText((Element) nodelist.item(i), "userid");
                String firstname = DOMUtil.getSimpleElementText((Element) nodelist.item(i), "firstname");
                String lastname = DOMUtil.getSimpleElementText((Element) nodelist.item(i), "lastname");
                selectUsersHash.put(firstname + " " + lastname, userid);
                usersTitle[i] = new String(firstname + " " + lastname);
            }
            cmbSelectUser = new JComboBox(usersTitle);
        } catch (MalformedURLException ex) {
            System.out.println(ex);
        } catch (ParserConfigurationException ex) {
            System.out.println(ex);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        cmbSelectUser.setSelectedIndex(0);
        return cmbSelectUser;
    }

    int selectedMidIdex = 0;

    public JComboBox comboDMMethod(final int userMId) {
        String urlString = dms_url + "/servlet/com.ufnasoft.dms.server.ServerMessageMethods?username=" + username + "&key=" + key;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            URL u = new URL(urlString);
            URLConnection uc = u.openConnection();
            InputStream inputstream = uc.getInputStream();
            int length = uc.getContentLength();
            String xmlPacket = "";
            if (length != -1) {
                byte incomingData[] = new byte[length];
                inputstream.read(incomingData);
                xmlPacket = new String(incomingData);
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(xmlPacket.getBytes());
            Document document = parser.parse(bais);
            NodeList nodelist = document.getElementsByTagName("messagemethod");
            int num = nodelist.getLength();
            String[] defaultmessagemethodTitle = new String[num];
            for (int i = 0; i < num; i++) {
                String defaultmessagemethodid = DOMUtil.getSimpleElementText((Element) nodelist.item(i), "defaultmessagemethodid");
                final String defaultmessagemethod = DOMUtil.getSimpleElementText((Element) nodelist.item(i), "defaultmessagemethod");
                if ((new Integer(defaultmessagemethodid)).intValue() == userMId) {
                    selectedMidIdex = i;
                }
                messageMethodHash.put(defaultmessagemethod, defaultmessagemethodid);
                defaultmessagemethodTitle[i] = new String(defaultmessagemethod);
            }
            cmbDMMethod = new JComboBox(defaultmessagemethodTitle);
        } catch (MalformedURLException ex) {
            System.out.println(ex);
        } catch (ParserConfigurationException ex) {
            System.out.println(ex);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        cmbDMMethod.setSelectedIndex(selectedMidIdex);
        return cmbDMMethod;
    }
}
