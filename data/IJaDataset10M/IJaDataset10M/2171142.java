package nk.client;

import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import nk.xml.NetworkAdress;
import nk.xml.Xml;

public class ClientConfigView extends javax.swing.JFrame {

    ArrayList listData = null;

    String[] knowndata = null;

    int servCount = 0;

    Main main;

    /** Creates new form ClientConfigView */
    public ClientConfigView(Main reference) {
        initComponents();
        Xml serializer = new Xml();
        main = reference;
        NetworkAdress networkData;
        String[] adressIp;
        String[] port;
        String[] def;
        try {
            networkData = serializer.xmlRead("clientConfig.xml");
            adressIp = networkData.getAdresIP();
            port = networkData.getPort();
            def = networkData.getDefault();
        } catch (FileNotFoundException e) {
            adressIp = new String[] { "127.0.0.1" };
            port = new String[] { "7721" };
            def = new String[] { "true" };
        }
        servCount = adressIp.length;
        listData = new ArrayList();
        for (int i = 0; i <= servCount - 1; i++) {
            listData.add((String) adressIp[i] + ":" + port[i] + ":" + def[i]);
            if (def[i].compareTo("true") == 0) {
                main.setHost(adressIp[i]);
                main.setPort(Integer.parseInt(port[i]));
            }
        }
        knownServersList.setListData(listData.toArray());
        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2;
        setLocation(x, y);
    }

    private void initComponents() {
        jSlider1 = new javax.swing.JSlider();
        jPanel1 = new javax.swing.JPanel();
        knownServersLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        knownServersList = new javax.swing.JList();
        removeButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        defaultButton = new javax.swing.JButton();
        addServerLabel = new javax.swing.JLabel();
        serverIpLabel = new javax.swing.JLabel();
        serverIpTextField = new javax.swing.JTextField();
        serverPortLabel = new javax.swing.JLabel();
        serverPortTextField = new javax.swing.JTextField();
        addButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        cancelButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        okButton = new javax.swing.JButton();
        knownServersLabel.setText("Known Servers");
        jScrollPane1.setViewportView(knownServersList);
        removeButton.setText("Remove");
        removeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });
        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });
        defaultButton.setText("Set as Default");
        defaultButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultButtonActionPerformed(evt);
            }
        });
        addServerLabel.setText("Add new Server");
        serverIpLabel.setText("Server IP");
        serverPortLabel.setText("Server Port");
        serverPortTextField.setText("7721");
        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(knownServersLabel).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(serverIpTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(serverIpLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(serverPortLabel).addComponent(serverPortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(clearButton, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE).addComponent(removeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(defaultButton))).addComponent(addServerLabel)).addGap(1084, 1084, 1084)).addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1471, Short.MAX_VALUE).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 678, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(jPanel1Layout.createSequentialGroup().addGap(250, 250, 250).addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton).addContainerGap(24, Short.MAX_VALUE)))));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(knownServersLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(addServerLabel)).addGroup(jPanel1Layout.createSequentialGroup().addComponent(removeButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(clearButton).addGap(35, 35, 35).addComponent(defaultButton))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(13, 13, 13).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(serverIpLabel).addComponent(serverPortLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(serverIpTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(addButton).addComponent(serverPortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cancelButton).addComponent(okButton)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        pack();
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Xml serializer = new Xml();
        int size = listData.size();
        String[] adressIp = new String[size];
        String[] port = new String[size];
        String[] def = new String[size];
        for (int i = 0; i <= size - 1; i++) {
            String dataline = (String) listData.get(i);
            String[] tmp = dataline.split(":");
            adressIp[i] = tmp[0];
            port[i] = tmp[1];
            def[i] = tmp[2];
            if (def[i].compareTo("true") == 0) {
                main.setHost(adressIp[i]);
                int po = Integer.parseInt(port[i]);
                main.setPort(po);
                main.reconnect();
            }
        }
        serializer.xmlWrite("clientConfig.xml", adressIp, port, def);
        setVisible(false);
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
    }

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String adressIp = serverIpTextField.getText();
        String port = serverPortTextField.getText();
        listData.add((String) adressIp + ":" + port + ":" + "false");
        serverIpTextField.setText("");
        knownServersList.setListData(listData.toArray());
    }

    private void defaultButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int size = listData.size();
        for (int i = 0; i <= size - 1; i++) {
            String dataline = (String) listData.get(i);
            if (dataline.endsWith("true") == true) {
                listData.add(i, dataline.replace("true", "false"));
                listData.remove(i + 1);
            }
        }
        String line = (String) knownServersList.getSelectedValue();
        int index = knownServersList.getSelectedIndex();
        listData.remove(line);
        listData.add(index, line.substring(0, line.length() - 5) + "true");
        knownServersList.setListData(listData.toArray());
    }

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {
        listData.removeAll(listData);
        knownServersList.setListData(listData.toArray());
    }

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String line = (String) knownServersList.getSelectedValue();
        listData.remove(line);
        knownServersList.setListData(listData.toArray());
    }

    private javax.swing.JButton addButton;

    private javax.swing.JLabel addServerLabel;

    private javax.swing.JButton cancelButton;

    private javax.swing.JButton clearButton;

    private javax.swing.JButton defaultButton;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JSlider jSlider1;

    private javax.swing.JLabel knownServersLabel;

    private javax.swing.JList knownServersList;

    private javax.swing.JButton okButton;

    private javax.swing.JButton removeButton;

    private javax.swing.JLabel serverIpLabel;

    private javax.swing.JTextField serverIpTextField;

    private javax.swing.JLabel serverPortLabel;

    private javax.swing.JTextField serverPortTextField;
}
