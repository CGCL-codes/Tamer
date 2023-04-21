package ru.amse.baltijsky.javascheme.frames.main;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;

/**
 * Button tab component that renders tab header with an exit button in the right corner.
 * The text in the header mimics the title of the corresponding tab.
 */
class ButtonTabComponent extends JPanel {

    /**
     * The tabbed pane for which the component renders close buttons.
     */
    private final JTabbedPane pane;

    /**
     * Creates a button tab component for the specified tabbed pane.
     *
     * @param pane the tabbed pane for the tabs of which to render close button
     */
    public ButtonTabComponent(final JTabbedPane pane) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.pane = pane;
        setOpaque(false);
        JLabel label = new JLabel() {

            public String getText() {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    return pane.getTitleAt(i);
                }
                return null;
            }
        };
        add(label);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        add(new TabButton());
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }

    /**
     * The close button itself.
     */
    private class TabButton extends JButton implements ActionListener {

        /**
         * Size of the close button.
         */
        private static final int BUTTON_SIZE = 17;

        /**
         * Tooltip text.
         */
        private static final String tooltipText = "Close tab";

        public TabButton() {
            setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
            setToolTipText(tooltipText);
            setUI(new BasicButtonUI());
            setContentAreaFilled(false);
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1) {
                pane.remove(i);
            }
        }

        public void updateUI() {
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.DARK_GRAY);
            if (getModel().isRollover()) {
                g2.setColor(Color.BLACK);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }

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
