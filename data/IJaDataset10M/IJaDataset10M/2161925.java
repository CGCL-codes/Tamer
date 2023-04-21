package com.google.code.jtracert.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JTracertAboutDialog extends JDialog {

    public JTracertAboutDialog(Frame owner, boolean modal) throws HeadlessException {
        super(owner, modal);
        createGUI();
    }

    private void createGUI() {
        setTitle("About jTracert");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(5, 5));
        JLabel jTracertLabel = new JLabel("<html>jTracert GUI version 0.1.3<br/>(c) Dmitry Bedrin 2009<br/>http://jtracert.googlecode.com/");
        jTracertLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.add(jTracertLabel, BorderLayout.NORTH);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        contentPane.add(okButton, BorderLayout.SOUTH);
        setSize(300, 125);
        setResizable(false);
        setModal(true);
        setBounds(getFrameBounds());
    }

    /**
     * @return
     */
    private Rectangle getFrameBounds() {
        Dimension wizardDimension = getSize();
        double width = wizardDimension.getWidth();
        double height = wizardDimension.getHeight();
        Rectangle rectangle = new Rectangle(new Point(0, 0), Toolkit.getDefaultToolkit().getScreenSize());
        double f = 1.0F;
        int i = (int) (f * width);
        int j = (int) (f * height);
        int k = rectangle.width - (i != -1 ? i : getWidth());
        int l = rectangle.height - (j != -1 ? j : getHeight());
        rectangle.x += k / 2;
        rectangle.width -= k;
        rectangle.y += l / 2;
        rectangle.height -= l;
        return rectangle;
    }
}
