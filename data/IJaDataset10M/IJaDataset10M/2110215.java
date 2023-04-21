package net.sourceforge.ubcdcreator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RegisterPanel extends JDialog implements ActionListener {

    private double questVer = 1.0;

    private String buttonClicked = null;

    private JTextField name = new JTextField();

    private JTextField os = new JTextField();

    private JTextField jre = new JTextField();

    private JTextField email = new JTextField();

    private JButton registerBtn = new JButton("Register");

    private JButton cancelBtn = new JButton("Cancel");

    private JButton neverBtn = new JButton("Never Register");

    private JCheckBox opt1 = new JCheckBox("Apply a patch to the UBCD.");

    private JCheckBox opt2 = new JCheckBox("Modify the UBCD image.");

    private JCheckBox opt3 = new JCheckBox("I am waiting for the Plugins capability.");

    public RegisterPanel(JFrame frame) {
        super(frame, "Register Software", true);
        String osStr1 = System.getProperty("os.name");
        if (osStr1 == null) osStr1 = "";
        String osStr2 = System.getProperty("os.arch");
        if (osStr1 == null) osStr1 = "";
        String osStr3 = System.getProperty("os.version");
        if (osStr1 == null) osStr1 = "";
        os.setText(osStr1 + " " + osStr2 + " " + osStr3);
        String jreStr1 = System.getProperty("java.vendor");
        if (jreStr1 == null) jreStr1 = "";
        String jreStr2 = System.getProperty("java.version");
        if (jreStr1 == null) jreStr1 = "";
        jre.setText(jreStr1 + " " + jreStr2);
        os.setEditable(false);
        jre.setEditable(false);
        JPanel panel = new JPanel();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(3, 3, 3, 3);
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weighty = 0;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        addRow(new JLabel("Name:"), name, panel, c);
        addRow(new JLabel("Operating System:"), os, panel, c);
        addRow(new JLabel("Java Version:"), jre, panel, c);
        addRow(new JLabel("Email:"), email, panel, c);
        c.gridwidth = 2;
        panel.add(new JLabel("Please tell us how you will use this software."), c);
        c.gridy++;
        panel.add(opt1, c);
        c.gridy++;
        panel.add(opt2, c);
        c.gridy++;
        panel.add(opt3, c);
        c.gridy++;
        panel.add(createButtonPanel(), c);
        Dimension d = new Dimension(600, 300);
        setMinimumSize(d);
        setMaximumSize(d);
        setPreferredSize(d);
        pack();
    }

    public String getButtonClicked() {
        return buttonClicked;
    }

    private void addRow(JLabel label, JTextField field, JPanel panel, GridBagConstraints c) {
        panel.add(label, c);
        c.gridx++;
        panel.add(field, c);
        c.gridy++;
        c.gridx = 0;
    }

    private JPanel createButtonPanel() {
        registerBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        neverBtn.addActionListener(this);
        registerBtn.setActionCommand("register");
        cancelBtn.setActionCommand("cancel");
        neverBtn.setActionCommand("never");
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 0, 0, 0);
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weighty = 0;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(neverBtn, c);
        c.gridx++;
        panel.add(cancelBtn, c);
        c.gridx++;
        panel.add(registerBtn, c);
        return panel;
    }

    private int getAnswers() {
        int ret = 0;
        if (opt1.isSelected()) ret = ret | 0x1;
        if (opt2.isSelected()) ret = ret | 0x2;
        if (opt3.isSelected()) ret = ret | 0x4;
        return ret;
    }

    public void actionPerformed(ActionEvent e) {
        if ("register".equals(e.getActionCommand())) {
            buttonClicked = "register";
            try {
                String data = URLEncoder.encode("ver", "UTF-8") + "=" + URLEncoder.encode(Double.toString(questVer), "UTF-8");
                data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name.getText(), "UTF-8");
                data += "&" + URLEncoder.encode("os", "UTF-8") + "=" + URLEncoder.encode(os.getText(), "UTF-8");
                data += "&" + URLEncoder.encode("jre", "UTF-8") + "=" + URLEncoder.encode(jre.getText(), "UTF-8");
                data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email.getText(), "UTF-8");
                data += "&" + URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode("Qr7SchF", "UTF-8");
                data += "&" + URLEncoder.encode("answers", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(getAnswers()), "UTF-8");
                URL url = new URL("http://ubcdcreator.sourceforge.net/register.php");
                URLConnection conn = url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                }
                rd.close();
                wr.close();
            } catch (Exception ex) {
            }
            setVisible(false);
        } else if ("cancel".equals(e.getActionCommand())) {
            buttonClicked = "cancel";
            setVisible(false);
        } else if ("never".equals(e.getActionCommand())) {
            buttonClicked = "never";
            setVisible(false);
        }
    }
}
