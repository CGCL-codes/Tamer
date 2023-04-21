package com.jlect.swebing.renderers.gwt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author Sergey Kozmin
 * @todo set description
 * @since 15.11.2007 10:54:43
 */
public class SwingTest {

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.validate();
        f.setVisible(true);
        final Container container = f.getContentPane();
        container.setLayout(new java.awt.BorderLayout() {

            public void layoutContainer(Container target) {
                super.layoutContainer(target);
            }

            public Dimension preferredLayoutSize(Container target) {
                return super.preferredLayoutSize(target);
            }

            public Dimension minimumLayoutSize(Container target) {
                return super.minimumLayoutSize(target);
            }

            public Dimension maximumLayoutSize(Container target) {
                return super.maximumLayoutSize(target);
            }
        });
        JButton center = new JButton("Center");
        center.setPreferredSize(new java.awt.Dimension(200, 50));
        container.add(center, java.awt.BorderLayout.CENTER);
        JButton north = new JButton("North");
        north.setPreferredSize(new java.awt.Dimension(200, 50));
        container.add(north, BorderLayout.NORTH);
        JButton east = new JButton("East");
        east.setPreferredSize(new java.awt.Dimension(200, 50));
        container.add(east, BorderLayout.EAST);
        JButton west = new JButton("West");
        west.setPreferredSize(new java.awt.Dimension(200, 50));
        container.add(west, BorderLayout.WEST);
        final JButton south = new JButton("South");
        south.setPreferredSize(new java.awt.Dimension(200, 50));
        container.add(south, BorderLayout.SOUTH);
        south.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                Dimension preferredSize = south.getPreferredSize();
                preferredSize.width += 10;
                preferredSize.height += 10;
                south.setPreferredSize(preferredSize);
            }
        });
        center.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                container.setSize(new Dimension(container.getWidth() - 10, container.getHeight() + 10));
            }
        });
    }
}
