package com.ufnasoft.dms.admin;

import com.ufnasoft.dms.gui.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Hashtable;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class EditGroupDialog extends InitDMS {

    /**
	 * @param args
	 */
    JDialog dialog;

    JTextField txtNewGroupName;

    JComboBox cmbGroupName;

    /**
	 * 
	 * @param frame
	 */
    public void makeDialog(JFrame frame) {
        dialog = new JDialog(frame, "Edit Group", true);
        dialog.setSize(408, 244);
        JPanel secondPanel = new JPanel();
        secondPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(1, 1, 1, 1);
        JLabel lblGroupName = new JLabel("Group Name");
        lblGroupName.setFont(new Font("Tahoma", Font.PLAIN, 11));
        JLabel lblNewGroupName = new JLabel("New Group Name");
        lblNewGroupName.setFont(new Font("Tahoma", Font.PLAIN, 11));
        String[] str = { "test", "test" };
        final Hashtable groupsHash = new Hashtable();
        String urlString = dms_url + "/servlet/com.ufnasoft.dms.server.ServerGetGroups?username=" + username + "&key=" + key;
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
            System.out.println(xmlPacket);
            ByteArrayInputStream bais = new ByteArrayInputStream(xmlPacket.getBytes());
            Document document = parser.parse(bais);
            NodeList nodelist = document.getElementsByTagName("group");
            int num = nodelist.getLength();
            String[] groupsTitle = new String[num];
            for (int i = 0; i < num; i++) {
                String groupid = DOMUtil.getSimpleElementText((Element) nodelist.item(i), "groupid");
                String groupname = DOMUtil.getSimpleElementText((Element) nodelist.item(i), "groupname");
                groupsHash.put(groupname, groupid);
                groupsTitle[i] = new String(groupname);
            }
            cmbGroupName = new JComboBox(groupsTitle);
        } catch (MalformedURLException ex) {
            System.out.println(ex);
        } catch (ParserConfigurationException ex) {
            System.out.println(ex);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        txtNewGroupName = new JTextField(20);
        JButton butUpdate = new JButton("Update");
        butUpdate.setPreferredSize(new Dimension(100, 25));
        JButton butCancel = new JButton("Cancel");
        butCancel.setPreferredSize(new Dimension(100, 25));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        secondPanel.add(lblGroupName, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        secondPanel.add(cmbGroupName, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        secondPanel.add(lblNewGroupName, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        secondPanel.add(txtNewGroupName, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        butUpdate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                String strGroupName = (String) cmbGroupName.getSelectedItem();
                int groupid = (new Integer((String) groupsHash.get(strGroupName))).intValue();
                String strNewGroupName = txtNewGroupName.getText();
                String urlString = dms_url + "/servlet/com.ufnasoft.dms.server.ServerUpdateGroup";
                String rvalue = "";
                try {
                    String urldata = urlString + "?username=" + URLEncoder.encode(username, "UTF-8") + "&key=" + URLEncoder.encode(key, "UTF-8") + "&groupid=" + groupid + "&groupname=" + URLEncoder.encode(strNewGroupName, "UTF-8");
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
                System.out.println(rvalue);
                if (rvalue.equalsIgnoreCase("yes")) {
                }
                dialog.dispose();
            }
        });
        secondPanel.add(butUpdate, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        butCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                dialog.dispose();
            }
        });
        secondPanel.add(butCancel, gbc);
        final ImageIcon icon = new ImageIcon(getClass().getResource("/com/ufnasoft/dms/images/banners/editgroup.gif"));
        JPanel bannerPanel = new JPanel() {

            protected void paintComponent(Graphics g) {
                g.drawImage(icon.getImage(), 0, 0, null);
                super.paintComponent(g);
            }
        };
        Container c = dialog.getContentPane();
        bannerPanel.setLayout(new GridBagLayout());
        GridBagConstraints bannerConstraints = new GridBagConstraints();
        bannerConstraints.insets = new Insets(2, 2, 2, 2);
        JLabel bannerHead = new JLabel("Edit Group");
        bannerHead.setFont(new Font("Tahoma", Font.BOLD, 13));
        JLabel bannerD = new JLabel("Edit a group of users in DMS");
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
        GridBagConstraints containerConstraints = new GridBagConstraints();
        containerConstraints.insets = new Insets(0, 0, 20, 0);
        containerConstraints.gridx = 0;
        containerConstraints.gridy = 0;
        containerConstraints.anchor = GridBagConstraints.WEST;
        c.add(bannerPanel, containerConstraints);
        containerConstraints.gridx = 0;
        containerConstraints.gridy = 1;
        containerConstraints.anchor = GridBagConstraints.CENTER;
        c.add(secondPanel, containerConstraints);
        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame();
        EditGroupDialog d = new EditGroupDialog();
        d.makeDialog(frame);
    }
}
