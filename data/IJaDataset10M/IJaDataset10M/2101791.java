package org.tm4j.admintool;

import org.tm4j.topicmap.TopicMap;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

/**
 * @author Kal
 *
 * Describe TMConfigurationDialog here.
 */
public class TMConfigurationDialog extends JDialog {

    public static final int STATUS_CANCEL = 0;

    public static final int STATUS_OK = 1;

    private JTextField m_tmName;

    private int m_status = STATUS_CANCEL;

    public TMConfigurationDialog(TopicMap tm, String tmName, String providerName) {
        initComponents(tm, tmName, providerName);
        setSize(new Dimension(300, 200));
    }

    public void initComponents(TopicMap tm, String tmName, String providerName) {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.getContentPane().setLayout(layout);
        JLabel l = new JLabel("Name: ");
        c.insets = new Insets(5, 3, 3, 3);
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        layout.setConstraints(l, c);
        this.getContentPane().add(l);
        m_tmName = new JTextField();
        m_tmName.setText(tmName);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        layout.setConstraints(m_tmName, c);
        this.getContentPane().add(m_tmName);
        l = new JLabel("Provider: ");
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        layout.setConstraints(l, c);
        this.getContentPane().add(l);
        JTextField tf = new JTextField();
        tf.setText(providerName);
        tf.setEnabled(false);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        layout.setConstraints(tf, c);
        this.getContentPane().add(tf);
        l = new JLabel("Locator: ");
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        layout.setConstraints(l, c);
        this.getContentPane().add(l);
        tf = new JTextField();
        tf.setText(tm.getBaseLocator().getAddress());
        tf.setEnabled(false);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        layout.setConstraints(tf, c);
        this.getContentPane().add(tf);
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                m_status = STATUS_OK;
                dispose();
            }
        });
        buttons.add(ok);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                m_status = STATUS_CANCEL;
                dispose();
            }
        });
        buttons.add(cancel);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(3, 3, 3, 3);
        c.anchor = GridBagConstraints.SOUTH;
        layout.setConstraints(buttons, c);
        this.getContentPane().add(buttons);
        buttons.setBorder(new EtchedBorder());
        setTitle("Topic Map Configuration");
    }

    public String getTMName() {
        return m_tmName.getText();
    }

    public int getStatus() {
        return m_status;
    }
}
