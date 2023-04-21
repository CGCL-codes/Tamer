package gui;

import gui.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class CommonDialog extends JDialog {

    private Hashtable<String, String> m_results;

    private Hashtable<String, JComponent> m_comps;

    private Hashtable<String, Object> m_objects;

    private Frame m_parent;

    public CommonDialog(Frame parent, boolean modal, String titleText) {
        super(parent, titleText, modal);
        m_results = new Hashtable<String, String>();
        m_comps = new Hashtable<String, JComponent>();
        m_objects = new Hashtable<String, Object>();
        m_parent = parent;
    }

    public void init() {
        setLayout(new FormLayout(5, 5));
        Enumeration<String> en = m_objects.keys();
        while (en.hasMoreElements()) {
            String key = en.nextElement();
            Object dataObject = m_objects.get(key);
            JLabel lb = new JLabel(key);
            add(lb);
            JComponent comp = null;
            if (dataObject instanceof String[]) {
                comp = new JComboBox((String[]) dataObject);
            } else if (dataObject instanceof String) {
                comp = new JTextField((String) dataObject, 10);
            }
            add(comp);
            m_comps.put(key, comp);
        }
        final JButton b = new JButton("OK");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                Enumeration<String> en = m_comps.keys();
                while (en.hasMoreElements()) {
                    String key = en.nextElement();
                    JComponent m_comp = m_comps.get(key);
                    if (m_comp instanceof JComboBox) {
                        m_results.put(key, (String) ((JComboBox) m_comp).getSelectedItem());
                    } else if (m_comp instanceof JTextField) {
                        m_results.put(key, (String) ((JTextField) m_comp).getText());
                    }
                }
                setVisible(false);
                dispose();
            }
        });
        add(b);
        pack();
        setLocationRelativeTo(m_parent);
    }

    public void addField(String label, Object data) {
        m_objects.put(label, data);
    }

    public String getResult(String key) {
        return m_results.get(key);
    }
}
