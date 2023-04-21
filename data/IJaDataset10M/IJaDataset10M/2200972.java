package com.ufnasoft.dms.admin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.ufnasoft.dms.gui.*;

public class AddGroupDialog extends InitDMS {

    JDialog dialog;

    JTextField txtGroupName;

    /**
	 * <b>Add goup dialog is used to add a new Group to the DMS Groups List.</b>
	 * @param Takes frame of parent window as Argument.
	 * @return The AddGroupDialog.
	 */
    public void makeDialog(JFrame frame) {
        dialog = new JDialog(frame, "Add Group", true);
        txtGroupName = new JTextField(20);
        dialog.setSize(408, 205);
        JPanel secondPanel = new JPanel();
        secondPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(1, 1, 1, 1);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy++;
        JLabel lblGroupName = new JLabel("Group Name");
        lblGroupName.setFont(new Font("Tahoma", Font.PLAIN, 11));
        secondPanel.add(lblGroupName, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        secondPanel.add(txtGroupName, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy++;
        JButton addUser = new JButton("Add");
        addUser.setPreferredSize(new Dimension(100, 25));
        addUser.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                String strGroupName = txtGroupName.getText();
                String urlString = dms_url + "/servlet/com.ufnasoft.dms.server.ServerAddGroup";
                String rvalue = "";
                try {
                    String urldata = urlString + "?username=" + URLEncoder.encode(username, "UTF-8") + "&key=" + URLEncoder.encode(key, "UTF-8") + "&groupname=" + URLEncoder.encode(strGroupName, "UTF-8");
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
                dialog.dispose();
            }
        });
        secondPanel.add(addUser, gbc);
        JButton butCancel = new JButton("Cancel");
        butCancel.setPreferredSize(new Dimension(100, 25));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        butCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        secondPanel.add(butCancel, gbc);
        final ImageIcon icon = new ImageIcon(getClass().getResource("/com/ufnasoft/dms/images/banners/addgroup.gif"));
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
        JLabel bannerHead = new JLabel("Add Group");
        bannerHead.setFont(new Font("Tahoma", Font.BOLD, 13));
        JLabel bannerD = new JLabel("Add a new group of users in DMS");
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
}
