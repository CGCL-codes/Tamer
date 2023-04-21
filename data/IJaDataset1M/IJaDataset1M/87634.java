package com.umc.gui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;
import com.umc.gui.GuiController;
import com.umc.gui.content.IUmcTab;

/**
 * Component to be used as tabComponent;
 * Contains a JLabel to show the text and 
 * a JButton to close the tab it belongs to 
 */
public class ButtonTabComponent extends JPanel {

    private static final long serialVersionUID = 3563605968615855798L;

    private TabButton button;

    private JLabel label;

    @SuppressWarnings("serial")
    public ButtonTabComponent(final JTabbedPane pane, final JPanel content, final String tabId) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        setOpaque(false);
        label = new JLabel() {

            public String getText() {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    return pane.getTitleAt(i);
                }
                return null;
            }
        };
        label.setForeground(new Color(16, 93, 23));
        add(label);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        button = new TabButton();
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (((IUmcTab) content).tabClosing()) {
                    GuiController.getInstance().getMasterPanel().removeTab(tabId);
                }
            }
        });
        add(button);
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }

    public void setEnabled(boolean aState) {
        button.setEnabled(aState);
        label.setEnabled(aState);
    }

    @SuppressWarnings("serial")
    private class TabButton extends ExtButton {

        public TabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("Close");
            setUI(new BasicButtonUI());
            setContentAreaFilled(false);
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            setRolloverEnabled(true);
        }

        public void updateUI() {
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) {
                g2.setColor(Color.ORANGE);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }

    @SuppressWarnings("unused")
    private static final MouseListener buttonMouseListener = new MouseAdapter() {

        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }

        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };
}
