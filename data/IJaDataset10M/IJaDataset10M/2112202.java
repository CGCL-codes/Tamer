package de.ios.framework.gui.builder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GuiItemFrame extends JFrame implements ActionListener, WindowListener {

    GridBagLayout gbl = new GridBagLayout();

    GridBagConstraints gbc = new GridBagConstraints();

    JButton okB, addB, closeB;

    JList itemLI;

    JLabel l;

    JTextField positionTF, textTF;

    Component c;

    Vector elementsV = new Vector();

    public GuiItemFrame(Component c) {
        this.c = c;
        buildMask();
        setConfiguration();
        okB.addActionListener(this);
        closeB.addActionListener(this);
        addB.addActionListener(this);
        addWindowListener(this);
        setVisible(true);
        setTitle("Items hinzuf�gen");
    }

    public void buildMask() {
        this.getContentPane().setLayout(gbl);
        gbc.fill = gbc.BOTH;
        gbc.anchor = gbc.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        l = new JLabel("Text");
        gbl.setConstraints(l, gbc);
        this.getContentPane().add(l);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        textTF = new JTextField(20);
        gbl.setConstraints(textTF, gbc);
        this.getContentPane().add(textTF);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        l = new JLabel("Position");
        gbl.setConstraints(l, gbc);
        this.getContentPane().add(l);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        positionTF = new JTextField(2);
        gbl.setConstraints(positionTF, gbc);
        this.getContentPane().add(positionTF);
        gbc.fill = gbc.NONE;
        gbc.anchor = gbc.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        addB = new JButton("Hinzuf�gen");
        gbl.setConstraints(addB, gbc);
        this.getContentPane().add(addB);
        gbc.fill = gbc.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        itemLI = new JList();
        JScrollPane sp = new JScrollPane(itemLI);
        gbl.setConstraints(sp, gbc);
        this.getContentPane().add(sp);
        gbc.anchor = gbc.WEST;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        okB = new JButton("Ok");
        gbl.setConstraints(okB, gbc);
        this.getContentPane().add(okB);
        gbc.fill = gbc.NONE;
        gbc.anchor = gbc.EAST;
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        closeB = new JButton("Schlie�en");
        gbl.setConstraints(closeB, gbc);
        this.getContentPane().add(closeB);
    }

    public void setConfiguration() {
        if (c instanceof List) {
            for (int i = 0; i < ((List) c).getItemCount(); i++) elementsV.addElement(((List) c).getItem(i));
        } else if (c instanceof JComboBox) {
            for (int k = 0; k < ((JComboBox) c).getItemCount(); k++) elementsV.addElement(((JComboBox) c).getItemAt(k));
        } else {
            ListModel l = ((JList) c).getModel();
            for (int i = 0; i < l.getSize(); i++) elementsV.addElement(l.getElementAt(i));
        }
        itemLI.setListData(elementsV);
        positionTF.setText(String.valueOf(elementsV.size()));
    }

    public void addItem() {
        elementsV.insertElementAt(textTF.getText(), Integer.parseInt(positionTF.getText()));
        positionTF.setText(String.valueOf(elementsV.size()));
        textTF.setText("");
        itemLI.setListData(elementsV);
    }

    public void setChanges() {
        if (c instanceof List) {
            ((List) c).removeAll();
            for (int i = 0; i < elementsV.size(); i++) ((List) c).add((String) elementsV.elementAt(i));
        } else if (c instanceof JComboBox) {
            ((JComboBox) c).removeAll();
            for (int k = 0; k < elementsV.size(); k++) ((JComboBox) c).addItem((String) elementsV.elementAt(k));
        } else {
            ((JList) c).setListData(elementsV);
        }
    }

    public void close() {
        setVisible(false);
        dispose();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addB) {
            addItem();
        } else if (e.getSource() == okB) {
            setChanges();
            close();
        } else if (e.getSource() == closeB) {
            close();
        }
    }

    public void windowClosing(WindowEvent e) {
        close();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }
}
