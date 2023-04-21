package org.japura.gui.touchscreen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Copyright (C) 2009 Carlos Eduardo Leite de Andrade
 * <P>
 * This library is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <P>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <P>
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 * <P>
 * For more information, contact: www.japura.org<BR>
 * 
 * @author Carlos Eduardo Leite de Andrade
 */
public class Button extends JLabel {

    private Status status = Status.NORMAL;

    private static int arc = 30;

    public Button(String text) {
        setPreferredSize(new Dimension(50, 50));
        setText(text);
        setHorizontalAlignment(SwingConstants.CENTER);
        setNormalMode();
        addMouseListener(new MouseListener() {

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
                setPressedMode();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setNormalMode();
            }
        });
    }

    private void setPressedMode() {
        status = Status.MOUSE_PRESSED;
        setFont(getFont().deriveFont(23f));
    }

    private void setNormalMode() {
        status = Status.NORMAL;
        setFont(getFont().deriveFont(26f));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (status.equals(Status.NORMAL)) paintNormal(g2d); else paintPressed(g2d);
        super.paintComponent(g);
    }

    private void paintNormal(Graphics2D g2d) {
        GradientPaint gp = new GradientPaint(0, 4, Color.lightGray, 0, getHeight() - 9, Color.black);
        g2d.setPaint(gp);
        g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
        gp = new GradientPaint(0, 4, Color.lightGray, 0, getHeight() - 9, Color.white);
        g2d.setPaint(gp);
        g2d.fillRoundRect(2, 2, getWidth() - 5, getHeight() - 7, arc, arc);
    }

    private void paintPressed(Graphics2D g2d) {
        g2d.setColor(Color.gray);
        g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
        GradientPaint gp = new GradientPaint(0, 4, Color.lightGray, 0, getHeight() - 9, Color.black);
        g2d.setPaint(gp);
        g2d.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 3, arc, arc);
        gp = new GradientPaint(0, 4, Color.lightGray.darker(), 0, getHeight() - 9, Color.white);
        g2d.setPaint(gp);
        g2d.fillRoundRect(3, 3, getWidth() - 7, getHeight() - 7, arc, arc);
    }

    private static enum Status {

        NORMAL, MOUSE_PRESSED
    }

    public static void main(String args[]) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p = new JPanel();
        p.add(new Button("1"));
        p.add(new Button("2"));
        p.add(new Button("3"));
        p.add(new TextPanel());
        f.add(p);
        f.pack();
        f.setVisible(true);
    }
}
