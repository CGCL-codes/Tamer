package jcontrol.eib.extended.ai_layer.jini;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.rmi.RemoteException;
import jcontrol.eib.extended.ai_layer.AI_GroupDataService;
import jcontrol.eib.extended.ai_layer.AI_Service;
import jcontrol.eib.extended.ai_layer.GroupExhaustionException;
import jcontrol.util.Helper;
import net.jini.core.lookup.ServiceItem;

class MainUI extends JPanel {

    private int count = 0;

    private AI_Service stack;

    private AI_GroupDataService.AccessPoint gSAP = null;

    private int ga = -1;

    private JLabel gaLabel = new JLabel("Group Address");

    private JTextField gaField = new JTextField();

    private JCheckBox readableBox = new JCheckBox("Is this group object readable?");

    private JButton clearButton = new JButton("Clear Log");

    private JLabel actValueLabel = new JLabel("Actual Group Value");

    private JTextField actValueField = new JTextField();

    private JButton readButton = new JButton("Read Request");

    private JLabel setValueLabel = new JLabel("Group Value to Write");

    private JTextField setValueField = new JTextField();

    private JButton writeButton = new JButton("Write Request");

    private JEditorPane logPane = new JEditorPane();

    private JScrollPane logScrollPane = new JScrollPane();

    public MainUI(ServiceItem item) {
        stack = (AI_Service) item.service;
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gridbag);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.insets = new Insets(0, 2, 0, 2);
        c.ipadx = 0;
        c.anchor = GridBagConstraints.EAST;
        gridbag.setConstraints(gaLabel, c);
        add(gaLabel);
        gaField.setToolTipText("Group Address (2 Byte HEX)");
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.ipadx = 40;
        c.anchor = GridBagConstraints.WEST;
        gridbag.setConstraints(gaField, c);
        add(gaField);
        readableBox.setToolTipText("If selected, the actual value is send back on incoming read requests.");
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.ipadx = 0;
        c.anchor = GridBagConstraints.CENTER;
        gridbag.setConstraints(readableBox, c);
        add(readableBox);
        clearButton.setToolTipText("Clears contents of the logging window below.");
        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.ipadx = 0;
        c.anchor = GridBagConstraints.CENTER;
        gridbag.setConstraints(clearButton, c);
        add(clearButton);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.ipadx = 0;
        c.anchor = GridBagConstraints.EAST;
        gridbag.setConstraints(actValueLabel, c);
        add(actValueLabel);
        actValueField.setEditable(false);
        actValueField.setToolTipText("Actual group value (HEX).");
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.ipadx = 0;
        c.anchor = GridBagConstraints.WEST;
        gridbag.setConstraints(actValueField, c);
        add(actValueField);
        readButton.setToolTipText("Sends a read request to get the actual group value from network.");
        c.gridx = 3;
        c.gridy = 1;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.ipadx = 0;
        c.anchor = GridBagConstraints.CENTER;
        gridbag.setConstraints(readButton, c);
        add(readButton);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.ipadx = 0;
        c.anchor = GridBagConstraints.EAST;
        gridbag.setConstraints(setValueLabel, c);
        add(setValueLabel);
        setValueField.setToolTipText("Group value to be written to the network (min. 1 Byte HEX).");
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.ipadx = 0;
        c.anchor = GridBagConstraints.WEST;
        gridbag.setConstraints(setValueField, c);
        add(setValueField);
        writeButton.setToolTipText("Sends a write request to set a new group value.");
        c.gridx = 3;
        c.gridy = 2;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.ipadx = 0;
        c.anchor = GridBagConstraints.CENTER;
        gridbag.setConstraints(writeButton, c);
        add(writeButton);
        logPane.setContentType("text/plain");
        logPane.setEditable(false);
        logPane.setFont(new Font("Monospaced", Font.PLAIN, 11));
        logPane.setText("");
        logPane.setToolTipText("Log of all messages.");
        logScrollPane.setOpaque(true);
        logScrollPane.setBounds(0, 0, 500, 200);
        logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        logScrollPane.getViewport().add(logPane);
        logScrollPane.setToolTipText("Log of all messages.");
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.ipadx = 0;
        c.ipady = 300;
        gridbag.setConstraints(logScrollPane, c);
        add(logScrollPane);
        gaField.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent ev) {
            }

            public void focusLost(FocusEvent ev) {
                byte[] gaArray = Helper.hexStringtoArray(gaField.getText(), 2);
                int newGA = ((gaArray[0] & 0xFF) << 8) + (gaArray[1] & 0xFF);
                if (newGA != ga) {
                    unsubscribe();
                    ga = newGA;
                    if (ga != 0) {
                        try {
                            gSAP = stack.subscribeGroup(ga, new GroupListener());
                            setUIClosedListener();
                        } catch (GroupExhaustionException e) {
                            gSAP = null;
                            ga = 0;
                            logPane.setText(logPane.getText() + count + " - No new groups can be added!\n");
                            count++;
                        } catch (RemoteException e) {
                            gSAP = null;
                            ga = 0;
                        }
                    }
                }
                if (ga != 0) {
                    gaField.setText(Helper.toHex(ga).substring(4));
                } else {
                    gaField.setText("");
                }
            }
        });
        clearButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                count = 0;
                synchronized (logPane) {
                    logPane.setText("");
                }
            }
        });
        readButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                String newLog = count + " - Send Read Request: ";
                if (gSAP != null) {
                    newLog += "wait for response ...";
                    new ReadThread(count);
                } else {
                    newLog += "no group subscribed";
                }
                synchronized (logPane) {
                    logPane.setText(logPane.getText() + newLog + "\n");
                }
                count++;
            }
        });
        setValueField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                byte[] data = Helper.hexStringtoArray(setValueField.getText());
                actValueField.setText(Helper.toHex(data));
            }
        });
        writeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                String newLog = count + " - Send Write Request: ";
                if (gSAP != null) {
                    byte[] data = Helper.hexStringtoArray(setValueField.getText());
                    if (data.length > 0) {
                        newLog += Helper.toHex(data) + "-> ";
                        synchronized (logPane) {
                            logPane.setText(logPane.getText() + newLog);
                        }
                        try {
                            gSAP.writeGroupValue(data, true);
                            newLog = "ok";
                            actValueField.setText(Helper.toHex(data));
                        } catch (IOException e) {
                            newLog = "!!! " + e;
                        }
                    } else {
                        newLog += "nothing to write";
                    }
                } else {
                    newLog += "no group subscribed";
                }
                synchronized (logPane) {
                    logPane.setText(logPane.getText() + newLog + "\n");
                }
                count++;
            }
        });
        validate();
        setVisible(true);
    }

    /**
     * Is started by the readButton action event listener to wait for the
     * awaited read response, so that the gui is not blocked during read
     * requests.
     */
    private class ReadThread implements Runnable {

        private int count;

        ReadThread(int count) {
            this.count = count;
            new Thread(this).start();
        }

        public void run() {
            String newLog = count + " - ";
            try {
                byte[] data = gSAP.readGroupValue();
                newLog += "Read Response: " + Helper.toHex(data);
                actValueField.setText(Helper.toHex(data));
            } catch (IOException e) {
                newLog += "Read Request failed: " + e;
            }
            synchronized (logPane) {
                logPane.setText(logPane.getText() + newLog + "\n");
            }
        }
    }

    /**
     *
     */
    private class GroupListener implements AI_GroupDataService.Listener {

        public void writeGroupValue(int sa, int pr, int hc, byte[] data) {
            String hexString = Helper.toHex(data);
            synchronized (logPane) {
                logPane.setText(logPane.getText() + count + " - incoming WriteRequest (sa=" + Helper.toHex(sa).substring(4) + ",pr=" + pr + ",hc=" + hc + "): " + hexString + "\n");
            }
            actValueField.setText(hexString);
            count++;
        }

        public byte[] readGroupValue(int sa, int pr, int hc) {
            byte[] data = null;
            String newLog = count + " - incoming ReadRequest (sa=" + Helper.toHex(sa).substring(4) + ",pr=" + pr + ",hc=" + hc + ") -> ";
            if (!readableBox.isSelected()) {
                newLog += "not readable";
            } else {
                data = Helper.hexStringtoArray(actValueField.getText());
                if (data.length > 0) {
                    newLog += Helper.toHex(data);
                } else {
                    data = null;
                    newLog += "nothing to return";
                }
            }
            synchronized (logPane) {
                logPane.setText(logPane.getText() + newLog + "\n");
            }
            count++;
            return data;
        }
    }

    private void unsubscribe() {
        if (gSAP != null) {
            try {
                gSAP.unsubscribe();
            } catch (RemoteException e) {
            }
            gSAP = null;
        }
    }

    private boolean uiClosedListenerSet = false;

    private void setUIClosedListener() {
        if (!uiClosedListenerSet) {
            Container parent = this;
            do {
                parent = parent.getParent();
                System.out.println("::: " + parent.getClass().getName());
            } while ((parent != null) && !(parent instanceof Window));
            if (parent != null) {
                Window win = (Window) parent;
                win.addWindowListener(new WindowListener() {

                    public void windowActivated(WindowEvent e) {
                    }

                    public void windowClosed(WindowEvent e) {
                        unsubscribe();
                    }

                    public void windowClosing(WindowEvent e) {
                    }

                    public void windowDeactivated(WindowEvent e) {
                    }

                    public void windowDeiconified(WindowEvent e) {
                    }

                    public void windowIconified(WindowEvent e) {
                    }

                    public void windowOpened(WindowEvent e) {
                    }
                });
            }
            uiClosedListenerSet = true;
        }
    }

    public void finalize() throws Throwable {
        unsubscribe();
        super.finalize();
    }
}
