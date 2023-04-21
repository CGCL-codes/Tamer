package com.Dictionary;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

/**
 *
 * @author BunEri0463
 */
public class Driver {

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        final JFrame frame = new JFrame("Definitions");
        JRootPane rootPane = frame.getRootPane();
        String def;
        final JTextArea area = new JTextArea(10, 30);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        final JScrollPane scroll = new JScrollPane(area);
        final JLabel word = new JLabel("Word: ");
        final JTextField wod = new JTextField(10);
        wod.setText("Input Word");
        final JPanel search = new JPanel();
        FlowLayout flow = new FlowLayout();
        flow.setAlignment(FlowLayout.TRAILING);
        search.setLayout(flow);
        search.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        class helper extends AbstractAction {

            public helper(String str, Icon icn) {
                super(str);
            }

            public void actionPerformed(ActionEvent e) {
                Help.helpmenu(frame);
            }
        }
        Icon a = new ImageIcon("images/help.png");
        AbstractAction helpme = new helper("Help", a);
        final JButton helpyou = new JButton(helpme);
        search.add(helpyou);
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "helpclick");
        rootPane.getActionMap().put("helpclick", helpme);
        class info extends AbstractAction {

            public info(String str) {
                super(str);
            }

            public void actionPerformed(ActionEvent e) {
                JFrame about = new JFrame("About");
                paints p = new paints();
                about.add(p);
                about.setResizable(false);
                about.setSize(200, 200);
                about.setLocationRelativeTo(frame);
                about.setVisible(true);
            }

            class paints extends JComponent {

                @Override
                public void paint(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.drawString("All definitions from definr.com.", 1, 15);
                    g2.drawString("Created by Eric Bunton for Thesis", 1, 30);
                    g2.drawString("If you wish to report a bug send an", 1, 60);
                    g2.drawString("email to ugotemailed@gmail.com", 1, 75);
                    g2.drawString("with this in the subject line", 1, 90);
                    g2.drawString("\'EasyDict Question/Bug\'", 1, 105);
                }
            }
        }
        AbstractAction aboutcrack = new info("About");
        final JButton about = new JButton(aboutcrack);
        search.add(about);
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_DOWN_MASK), "aboutclick");
        rootPane.getActionMap().put("aboutclick", aboutcrack);
        search.add(word);
        search.add(wod);
        class actions extends AbstractAction {

            public actions(String str) {
                super(str);
            }

            public void actionPerformed(ActionEvent e) {
                Parser z = new Parser(wod.getText());
                try {
                    area.setText(z.parse(z.retrieve()));
                } catch (Exception ee) {
                    area.setText(ee + "\n\nEither you are not connected to the Internet " + "or the word is not found.\n\nIf you want more " + "information about the error you are receiveing " + "or wish to report a bug " + "send an email to ugotemailed@gmail.com with this " + "output in it. In the subject line put 'EasyDict Question/Bugs'." + "\n\nThank you.\nEric Bunton");
                }
            }
        }
        AbstractAction define = new actions("Define");
        final JButton find = new JButton(define);
        search.add(find);
        rootPane.setDefaultButton(find);
        class copy extends AbstractAction {

            public copy(String str, Icon icn) {
                super(str);
            }

            public void actionPerformed(ActionEvent e) {
                AddText.main(area.getText());
            }
        }
        Icon cpimg = new ImageIcon("src/images/copy.png");
        AbstractAction addtodoc = new copy("Copy", cpimg);
        final JButton addto = new JButton(addtodoc);
        search.add(addto);
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), "copy");
        rootPane.getActionMap().put("copy", addtodoc);
        frame.add(search, BorderLayout.NORTH);
        frame.add(scroll);
        frame.setSize(455, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension window = frame.getSize();
        int x = (screenSize.width / 2) - (window.width / 2);
        int y = (screenSize.height / 2) - (window.height / 2);
        frame.setLocation(x, y);
    }
}
