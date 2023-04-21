package org.qfirst.batavia.mime.ui;

import org.qfirst.options.ui.*;
import org.qfirst.batavia.mime.*;
import javax.swing.*;
import java.util.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.event.*;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

public class MimeOption implements DisplayableOption {

    private JPanel panel = new JPanel();

    private String id;

    private JLabel typeLabel;

    private JTextField textField;

    private JList list;

    private DefaultListModel dlm = new DefaultListModel();

    private JButton addButton = new JButton("Add");

    private JButton editButton = new JButton("Edit");

    private JButton removeButton = new JButton("Remove");

    private JButton upButton = new JButton("Move Up");

    private JButton downButton = new JButton("Move Down");

    public MimeOption() {
        typeLabel = new JLabel();
        textField = new JTextField();
        typeLabel.setText("Handlers for type: ");
        textField.setEditable(false);
        list = new JList(dlm);
        Box box = Box.createHorizontalBox();
        box.add(typeLabel);
        box.add(Box.createHorizontalStrut(10));
        box.add(textField);
        panel.setLayout(new BorderLayout(5, 5));
        panel.add(box, BorderLayout.NORTH);
        JScrollPane sp = new JScrollPane();
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp.setViewportView(list);
        panel.add(sp, BorderLayout.CENTER);
        Box buttons = Box.createVerticalBox();
        buttons.add(Box.createVerticalStrut(5));
        buttons.add(addButton);
        buttons.add(Box.createVerticalStrut(5));
        buttons.add(editButton);
        buttons.add(Box.createVerticalStrut(5));
        buttons.add(removeButton);
        buttons.add(Box.createVerticalStrut(5));
        buttons.add(upButton);
        buttons.add(Box.createVerticalStrut(5));
        buttons.add(downButton);
        buttons.add(Box.createVerticalStrut(5));
        panel.add(buttons, BorderLayout.EAST);
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                addHandler();
            }
        });
        editButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                editHandler();
            }
        });
        removeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                remove();
            }
        });
        upButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                moveUp();
            }
        });
        downButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                moveDown();
            }
        });
    }

    private void addHandler() {
        MimeDialog d = createDialog();
        d.setVisible(true);
        if (!d.isCancelled()) {
            dlm.addElement(d.getValue());
        }
    }

    private void editHandler() {
        if (list.getSelectedIndex() == -1) {
            return;
        }
        MimeDialog d = createDialog();
        d.setValue((MimeHandler) list.getSelectedValue());
        d.setVisible(true);
        if (!d.isCancelled()) {
            dlm.setElementAt(d.getValue(), list.getSelectedIndex());
        }
    }

    private void remove() {
        if (list.getSelectedIndex() == -1) {
            return;
        }
        dlm.remove(list.getSelectedIndex());
    }

    private void moveDown() {
        if (list.getSelectedIndex() == -1 || list.getSelectedIndex() == dlm.getSize() - 1) {
            return;
        }
        Object sel = list.getSelectedValue();
        Object x = dlm.get(list.getSelectedIndex() + 1);
        dlm.set(list.getSelectedIndex(), x);
        dlm.set(list.getSelectedIndex() + 1, sel);
        list.setSelectedIndex(list.getSelectedIndex() + 1);
    }

    private void moveUp() {
        if (list.getSelectedIndex() <= 0) {
            return;
        }
        Object sel = list.getSelectedValue();
        Object x = dlm.get(list.getSelectedIndex() - 1);
        dlm.set(list.getSelectedIndex(), x);
        dlm.set(list.getSelectedIndex() - 1, sel);
        list.setSelectedIndex(list.getSelectedIndex() - 1);
    }

    private MimeDialog createDialog() {
        MimeDialog d;
        Window w;
        w = getContainerWindow();
        if (w instanceof Frame) {
            d = new MimeDialog((Frame) w);
        } else {
            d = new MimeDialog((Dialog) w);
        }
        return d;
    }

    private Window getContainerWindow() {
        return (Window) panel.getTopLevelAncestor();
    }

    public Object extractValue() {
        MimeConfiguration mimeConfiguration = new MimeConfiguration();
        mimeConfiguration.setMimeType(textField.getText());
        for (int i = 0; i < list.getModel().getSize(); i++) {
            mimeConfiguration.addHandler((MimeHandler) list.getModel().getElementAt(i));
        }
        return mimeConfiguration;
    }

    public void updateGUI(Object value) {
        MimeConfiguration mimeConfiguration = (MimeConfiguration) value;
        List handlers = mimeConfiguration == null ? new ArrayList() : mimeConfiguration.getHandlers();
        dlm.removeAllElements();
        for (int i = 0; i < handlers.size(); i++) {
            dlm.addElement(handlers.get(i));
        }
        if (mimeConfiguration != null) {
            textField.setText(mimeConfiguration.getMimeType());
        }
    }

    public Object getComponentAt(int index) {
        return panel;
    }

    public int getComponentCount() {
        return 1;
    }

    public void addProperty(String name, String value) {
    }

    public void setProperty(String name, String value) {
        if ("type.label".equals(name)) {
            textField.setText(value);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
