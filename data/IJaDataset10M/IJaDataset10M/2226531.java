package se.uu.it.cats.pc.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import se.uu.it.cats.pc.actor.Area;
import se.uu.it.cats.pc.network.ConnectionManager;
import se.uu.it.cats.brick.network.packet.CloseConnection;
import se.uu.it.cats.brick.network.packet.SettingUpdate;
import se.uu.it.cats.brick.network.packet.Timestamp;
import se.uu.it.cats.brick.network.packet.SimpleMeasurement;

public class PanelBluetooth extends JPanel {

    private static JTextArea _infoBox;

    private static String AbsolutePositionUpdateText = "";

    private static String MeanAndCovarianceUpdateText = "";

    private static String SimpleMeasurementText = "";

    private static String LatestSightingUpdateText = "";

    private ConnectionPanel[] _connectionPanels = new ConnectionPanel[Area.CAT_COUNT];

    public PanelBluetooth() {
        super(new BorderLayout());
        setVisible(true);
        setBorder(new TitledBorder(new LineBorder(Color.gray, 1, false), "Connectivity", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION));
        JPanel panel = new JPanel(new BorderLayout());
        _connectionPanels[0] = new ConnectionPanel("cat0");
        panel.add(_connectionPanels[0], BorderLayout.NORTH);
        _connectionPanels[1] = new ConnectionPanel("cat1");
        panel.add(_connectionPanels[1], BorderLayout.CENTER);
        _connectionPanels[2] = new ConnectionPanel("cat2");
        panel.add(_connectionPanels[2], BorderLayout.SOUTH);
        add(panel, BorderLayout.WEST);
        panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(new LineBorder(Color.gray, 1, false), "Network log", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION));
        _infoBox = new JTextArea("Log window for incomming packets.", 4, 43);
        panel.add(new JScrollPane(_infoBox), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JButton savefileButton = new JButton("Save");
        savefileButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                writeFile(AbsolutePositionUpdateText, "AbsolutePositionUpdate.txt");
                writeFile(MeanAndCovarianceUpdateText, "MeanAndCovarianceUpdate.txt");
                writeFile(SimpleMeasurementText, "SimpleMeasurement.txt");
                writeFile(LatestSightingUpdateText, "LatestSightingUpdate.txt");
            }
        });
        buttonPanel.add(savefileButton, BorderLayout.NORTH);
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                _infoBox.setText("");
                AbsolutePositionUpdateText = "";
                MeanAndCovarianceUpdateText = "";
                SimpleMeasurementText = "";
                LatestSightingUpdateText = "";
            }
        });
        buttonPanel.add(clearButton, BorderLayout.SOUTH);
        panel.add(buttonPanel, BorderLayout.EAST);
        panel.add(new PacketPanel(), BorderLayout.SOUTH);
        add(panel, BorderLayout.CENTER);
    }

    public static void updatePacket(String newPacket) {
        char c = newPacket.charAt(0);
        if (c == 'S') {
            SimpleMeasurementText = SimpleMeasurementText + "\n" + newPacket;
        } else if (c == 'A') {
            AbsolutePositionUpdateText = AbsolutePositionUpdateText + "\n" + newPacket;
        } else if (c == 'M') {
            MeanAndCovarianceUpdateText = MeanAndCovarianceUpdateText + "\n" + newPacket;
        } else if (c == 'L') {
            LatestSightingUpdateText = LatestSightingUpdateText + "\n" + newPacket;
        }
        _infoBox.append(newPacket + "\n");
    }

    public void writeFile(String datatext, String filename) {
        try {
            FileWriter fw = new FileWriter(filename);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter outFile = new PrintWriter(bw);
            outFile.println(datatext);
            outFile.close();
        } catch (FileNotFoundException e1) {
            System.out.println("File does not exist");
        } catch (IOException e2) {
            System.out.println(e2);
        }
    }

    public void repaint() {
        super.repaint();
        try {
            for (ConnectionPanel cp : _connectionPanels) cp.repaint();
        } catch (NullPointerException ex) {
        }
    }

    private class ConnectionPanel extends JPanel implements ActionListener {

        String[] _catNames = Area.getInstance().getCatNames();

        String _catName = null;

        JButton connectionButton = null;

        public ConnectionPanel(String catName) {
            _catName = catName;
            setBorder(new TitledBorder(new LineBorder(Color.gray, 1, false), catName, TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION));
            connectionButton = new JButton("Connect") {

                public void repaint() {
                    int i = ConnectionManager.getInstance().getIdByName(_catName);
                    if (ConnectionManager.getInstance().isAlive(i)) this.setText("Connected"); else if (ConnectionManager.getInstance().isCreated(i)) this.setText("Connecting"); else this.setText("Connect");
                    super.repaint();
                }
            };
            connectionButton.addActionListener(this);
            add(connectionButton);
            add(new JLabel("FW traffic to:"));
            ActionListener ae = new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    JCheckBox checkBox = (JCheckBox) ae.getSource();
                    if (checkBox.isSelected()) ConnectionManager.getInstance().remIgnore(_catName, checkBox.getText()); else ConnectionManager.getInstance().addIgnore(_catName, checkBox.getText());
                }
            };
            for (String name : _catNames) {
                if (!name.equals(catName)) {
                    JCheckBox checkBox = new JCheckBox(name);
                    checkBox.setSelected(true);
                    checkBox.addActionListener(ae);
                    add(checkBox);
                }
            }
        }

        public void actionPerformed(ActionEvent ae) {
            ConnectionManager.getInstance().openConnection(_catName);
        }

        public void repaint() {
            try {
                super.repaint();
                connectionButton.repaint();
            } catch (NullPointerException ex) {
            }
        }
    }

    private class PacketPanel extends JPanel implements ActionListener {

        JTextField _param1;

        JTextField _param2;

        JTextField _param3;

        JTextField _param4;

        JTextField _param5;

        JComboBox _packetList = null;

        ButtonGroup _receiverGroup = null;

        public PacketPanel() {
            super(new BorderLayout());
            setBorder(new TitledBorder(new LineBorder(Color.gray, 1, false), "Send packet", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION));
            String[] packetStrings = { "-1 CloseConnection", "0x00 TimeStamp", "0x01 SettingUpdate", "0x02 SimpleMeasurement" };
            JPanel panel = new JPanel();
            _packetList = new JComboBox(packetStrings);
            _packetList.addActionListener(this);
            panel.add(_packetList);
            _param1 = new JTextField(5);
            _param2 = new JTextField(5);
            _param3 = new JTextField(5);
            _param4 = new JTextField(5);
            _param5 = new JTextField(5);
            panel.add(_param1);
            panel.add(_param2);
            panel.add(_param3);
            panel.add(_param4);
            panel.add(_param5);
            add(panel, BorderLayout.NORTH);
            _receiverGroup = new ButtonGroup();
            panel = new JPanel();
            JRadioButton catButton = new JRadioButton("cat0");
            catButton.setSelected(true);
            catButton.setActionCommand("cat0");
            _receiverGroup.add(catButton);
            panel.add(catButton);
            catButton = new JRadioButton("cat1");
            catButton.setActionCommand("cat1");
            _receiverGroup.add(catButton);
            panel.add(catButton);
            catButton = new JRadioButton("cat2");
            catButton.setActionCommand("cat2");
            _receiverGroup.add(catButton);
            panel.add(catButton);
            JButton sendButton = new JButton("Send");
            sendButton.addActionListener(this);
            panel.add(sendButton);
            add(panel, BorderLayout.SOUTH);
        }

        public void actionPerformed(ActionEvent e) {
            String packetName = (String) _packetList.getSelectedItem();
            String receiver = _receiverGroup.getSelection().getActionCommand();
            if (packetName.equals("-1 CloseConnection")) {
                if (e.getSource() instanceof JComboBox) {
                    _param1.setText("");
                    _param2.setText("");
                    _param3.setText("");
                    _param4.setText("");
                    _param5.setText("");
                } else if (e.getSource() instanceof JButton) {
                    ConnectionManager.getInstance().sendPacketTo(receiver, new CloseConnection());
                }
            } else if (packetName.equals("0x00 TimeStamp")) {
                if (e.getSource() instanceof JComboBox) {
                    _param1.setText("timestamp");
                    _param2.setText("roundTripTime");
                    _param3.setText("");
                    _param4.setText("");
                    _param5.setText("");
                } else if (e.getSource() instanceof JButton) {
                    ConnectionManager.getInstance().sendPacketTo(receiver, new Timestamp(Integer.valueOf(_param1.getText())));
                }
            } else if (packetName.equals("0x01 SettingUpdate")) {
                if (e.getSource() instanceof JComboBox) {
                    _param1.setText("setting");
                    _param2.setText("value");
                } else if (e.getSource() instanceof JButton) {
                    ConnectionManager.getInstance().sendPacketTo(receiver, new SettingUpdate(Integer.valueOf(_param1.getText()), Integer.valueOf(_param2.getText())));
                }
            } else if (packetName.equals("0x02 SimpleMeasurement")) {
                if (e.getSource() instanceof JComboBox) {
                    _param1.setText("id");
                    _param2.setText("angle");
                    _param3.setText("camAngle");
                    _param4.setText("");
                    _param5.setText("");
                } else if (e.getSource() instanceof JButton) {
                    ConnectionManager.getInstance().sendPacketTo(receiver, new SimpleMeasurement(Integer.valueOf(_param1.getText()), Float.valueOf(_param2.getText()), Float.valueOf(_param3.getText())));
                }
            }
        }
    }
}
