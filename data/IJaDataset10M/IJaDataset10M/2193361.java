package org.jazzteam.edu.lang.swing.framesSamples;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class MyFrame {

    static JFrame frame = new JFrame("Frame");

    static JComponent component = new JComponent() {

        @Override
        public void paint(Graphics g) {
            g.setColor(Color.BLACK);
            g.drawRect(60, 30, 30, 30);
        }
    };

    public static void modifFrame() {
        frame.setSize(500, 500);
        frame.setLocation(500, 250);
        frame.add(component);
        frame.show();
    }

    public class Listener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
    }
}
