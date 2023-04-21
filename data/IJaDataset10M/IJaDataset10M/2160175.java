package com.study.pepper.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class Help extends JFrame {

    Help() {
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        JPanel p = new JPanel();
        p.setLayout(null);
        TitledBorder titled = BorderFactory.createTitledBorder(loweredetched, "О программе");
        titled.setTitleJustification(TitledBorder.LEFT);
        p.setBorder(titled);
        JLabel l1 = new JLabel("Администрирование сети кинотеатров");
        l1.setBounds(10, 15, 250, 30);
        p.add(l1);
        JLabel l2 = new JLabel("Авторы:");
        l2.setBounds(30, 40, 250, 30);
        p.add(l2);
        JLabel l3 = new JLabel("Романенко Д.А.");
        l3.setBounds(50, 65, 250, 30);
        p.add(l3);
        JLabel l4 = new JLabel("Сорокин М.Е.");
        l4.setBounds(50, 90, 250, 30);
        p.add(l4);
        JLabel l5 = new JLabel("Рак Е.Ю.");
        l5.setBounds(50, 115, 250, 30);
        p.add(l5);
        JButton ok = new JButton("Ok");
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        });
        ok.setBounds(90, 160, 80, 30);
        p.add(ok);
        p.setBackground(Color.white);
        add(p);
        setSize(270, 240);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
