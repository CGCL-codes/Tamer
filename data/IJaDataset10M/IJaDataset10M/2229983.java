package audimus.notation;

import java.awt.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class NewDoc3 extends JPanel {

    private JLabel blankSpace;

    private JLabel jLabel1;

    private JLabel jLabel2;

    private JLabel jLabel3;

    private JLabel jLabel4;

    private JLabel welcomeTitle;

    private JPanel contentPanel;

    private JLabel iconLabel;

    private ImageIcon icon;

    private JTextField txtTitle;

    private JTextField txtAuthor;

    private JTextArea txtDescr;

    private JScrollPane scroll;

    private ResourceBundle messages;

    public NewDoc3() {
        Locale currentLocale;
        AppSettings inst = AppSettings.read();
        currentLocale = new Locale(inst.getLang(), inst.getCount());
        messages = ResourceBundle.getBundle("audimus.notation.MessagesBundle", currentLocale);
        iconLabel = new JLabel();
        contentPanel = getContentPanel();
        contentPanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        icon = getImageIcon();
        setLayout(new java.awt.BorderLayout());
        if (icon != null) iconLabel.setIcon(icon);
        iconLabel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        JPanel secondaryPanel = new JPanel();
        secondaryPanel.add(contentPanel, BorderLayout.NORTH);
        add(secondaryPanel, BorderLayout.CENTER);
    }

    private JPanel getContentPanel() {
        JPanel contentPanel1 = new JPanel();
        JPanel jPanel1 = new JPanel();
        welcomeTitle = new JLabel();
        blankSpace = new JLabel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jPanel1.setLayout(null);
        txtTitle = new JTextField(messages.getString("Title"));
        txtAuthor = new JTextField(messages.getString("Author"));
        jLabel3.setText(messages.getString("enter_title"));
        txtDescr = new JTextArea(3, 8);
        scroll = new JScrollPane(txtDescr);
        jLabel4.setText(messages.getString("enter_author"));
        jLabel1.setText(messages.getString("enter_description"));
        int xoffset = 20;
        int x = xoffset;
        int y = 20;
        jLabel3.setBounds(x, y, 100, 30);
        x += 150;
        txtTitle.setBounds(x, y, 200, 30);
        y += 40;
        x = xoffset;
        jLabel4.setBounds(x, y, 100, 30);
        x += 150;
        txtAuthor.setBounds(x, y, 200, 30);
        y += 40;
        x = xoffset;
        jLabel1.setBounds(x, y, 150, 30);
        x += 150;
        scroll.setBounds(x, y, 200, 100);
        jPanel1.add(jLabel3);
        jPanel1.add(txtTitle);
        jPanel1.add(jLabel4);
        jPanel1.add(txtAuthor);
        jPanel1.add(jLabel1);
        jPanel1.add(scroll);
        jPanel1.setPreferredSize(new Dimension(400, 300));
        contentPanel1.setLayout(new java.awt.BorderLayout());
        welcomeTitle.setFont(new java.awt.Font("Verdana", Font.BOLD, 11));
        welcomeTitle.setText("Document properties");
        contentPanel1.add(welcomeTitle, java.awt.BorderLayout.NORTH);
        contentPanel1.add(jPanel1, java.awt.BorderLayout.CENTER);
        return contentPanel1;
    }

    private ImageIcon getImageIcon() {
        return new ImageIcon((URL) getResource("clouds.jpg"));
    }

    private Object getResource(String key) {
        URL url = null;
        String name = key;
        if (name != null) {
            try {
                Class c = Class.forName("audimus.notation.Main");
                url = c.getResource(name);
            } catch (ClassNotFoundException cnfe) {
                System.err.println("Unable to find Main class");
            }
            return url;
        } else return null;
    }

    public String getTitle() {
        String out;
        out = txtTitle.getText();
        return out;
    }

    public String getAuthor() {
        String out;
        out = txtAuthor.getText();
        return out;
    }
}
