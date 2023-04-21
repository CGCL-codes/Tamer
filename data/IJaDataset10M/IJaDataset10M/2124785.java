package com.ienjinia.vc.util;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Selector {

    public static String select(String[] options, String title, String messageSelect, String messageCreate) {
        final JDialog jd = new JDialog((Frame) null, true);
        jd.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        jd.setTitle(title);
        Container c = jd.getContentPane();
        JPanel pn = new JPanel();
        c.add(pn);
        pn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pn.setLayout(new BoxLayout(pn, BoxLayout.Y_AXIS));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        pn.add(panel);
        panel.add(new JLabel(messageSelect));
        final JList jl = new JList(options);
        jl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane js = new JScrollPane(jl);
        panel.add(js);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        pn.add(panel);
        panel.add(new JLabel(messageCreate));
        final JTextField name = new JTextField(20);
        panel.add(name);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        pn.add(panel);
        final JButton okBtn = new JButton("OK");
        okBtn.setEnabled(false);
        panel.add(okBtn);
        JButton cancelBtn = new JButton("Cancel");
        panel.add(cancelBtn);
        jl.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                name.setText((String) jl.getSelectedValue());
            }
        });
        jl.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    name.setText((String) jl.getSelectedValue());
                    jd.dispose();
                }
            }
        });
        name.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                okBtn.setEnabled(name.getText().length() > 0);
            }

            public void removeUpdate(DocumentEvent e) {
                okBtn.setEnabled(name.getText().length() > 0);
            }

            public void changedUpdate(DocumentEvent e) {
            }
        });
        name.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jd.dispose();
            }
        });
        okBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jd.dispose();
            }
        });
        cancelBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                name.setText("");
                jd.dispose();
            }
        });
        jd.pack();
        jd.setResizable(false);
        jd.setVisible(true);
        return name.getText();
    }

    public static String select(String[] options, String title, String messageSelect) {
        final JDialog jd = new JDialog((Frame) null, true);
        jd.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        jd.setTitle(title);
        Container c = jd.getContentPane();
        JPanel pn = new JPanel();
        c.add(pn);
        pn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pn.setLayout(new BoxLayout(pn, BoxLayout.Y_AXIS));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        pn.add(panel);
        panel.add(new JLabel(messageSelect));
        final JList jl = new JList(options);
        jl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane js = new JScrollPane(jl);
        panel.add(js);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        pn.add(panel);
        final JButton okBtn = new JButton("OK");
        okBtn.setEnabled(false);
        panel.add(okBtn);
        JButton cancelBtn = new JButton("Cancel");
        panel.add(cancelBtn);
        final JTextField name = new JTextField();
        jl.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                name.setText((String) jl.getSelectedValue());
            }
        });
        jl.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    name.setText((String) jl.getSelectedValue());
                    jd.dispose();
                }
            }
        });
        name.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                okBtn.setEnabled(name.getText().length() > 0);
            }

            public void removeUpdate(DocumentEvent e) {
                okBtn.setEnabled(name.getText().length() > 0);
            }

            public void changedUpdate(DocumentEvent e) {
            }
        });
        okBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jd.dispose();
            }
        });
        cancelBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                name.setText("");
                jd.dispose();
            }
        });
        jd.pack();
        jd.setResizable(false);
        jd.setVisible(true);
        return name.getText();
    }
}
