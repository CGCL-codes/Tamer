package net.innig.macker.example.layering.gui;

import net.innig.macker.example.layering.model.*;
import net.innig.macker.example.layering.persistence.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ThingerUI {

    public static void main(String[] args) throws PersistenceException {
        new ThingerUI();
    }

    public ThingerUI() throws PersistenceException {
        JFrame f = new JFrame();
        JPanel panel = new JPanel(new FlowLayout());
        f.getContentPane().add(panel);
        list = new JList();
        list.setPreferredSize(new Dimension(300, 400));
        refresh();
        panel.add(list);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });
        panel.add(refresh);
        newName = new JTextField();
        newName.setPreferredSize(new Dimension(150, 24));
        panel.add(newName);
        JButton add = new JButton("Add Thinger");
        add.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                add(newName.getText());
            }
        });
        panel.add(add);
        f.pack();
        f.show();
    }

    public void refresh() {
        try {
            list.setListData(Thinger.getAll().toArray());
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
    }

    public void add(String name) {
        try {
            new Thinger(name).store();
            refresh();
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
    }

    private JTextField newName;

    private JList list;
}
