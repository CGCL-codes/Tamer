package org.viewaframework.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicButtonUI;
import org.viewaframework.view.ViewContainer;
import org.viewaframework.view.ViewException;
import org.viewaframework.view.perspective.PerspectiveConstraint;

/**
 * Component to be used as tabComponent;
 * Contains a JLabel to show the text and 
 * a JButton to close the tab it belongs to 
 */
public class ButtonTabComponent extends JPanel {

    private static final long serialVersionUID = 1765202489761914157L;

    private final JTabbedPane pane;

    private final JSplitPane rightToLeft;

    private final JSplitPane topBottom;

    private final ViewContainer viewContainer;

    public ButtonTabComponent(final JSplitPane rigthToLeft, final JSplitPane topBottom, final JTabbedPane pane, final ViewContainer viewContainer, final Icon iconImage) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.pane = pane;
        this.rightToLeft = rigthToLeft;
        this.topBottom = topBottom;
        this.viewContainer = viewContainer;
        setOpaque(false);
        JLabel label = new JLabel() {

            private static final long serialVersionUID = 1L;

            public String getText() {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    return pane.getTitleAt(i);
                }
                return null;
            }
        };
        if (iconImage != null) {
            label.setIcon(iconImage);
        }
        add(label);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        add(new TabButton("org/viewaframework/widget/icon/window/detach.png", new MaximizeListener()));
        add(new TabButton("org/viewaframework/widget/icon/window/fix.png", new RestoreListener()));
        add(new TabButton("org/viewaframework/widget/icon/window/close.png", new ClosingListener()));
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }

    private class TabButton extends JButton {

        private static final long serialVersionUID = 1L;

        public TabButton(String iconClassPath, ActionListener listener) {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setUI(new BasicButtonUI());
            setContentAreaFilled(false);
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            setIcon(ResourceLocator.getImageIcon(ButtonTabComponent.class, iconClassPath));
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            addActionListener(listener);
        }

        public void updateUI() {
        }
    }

    private class MaximizeListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    String name = pane.getName();
                    int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                    if (name.equals(PerspectiveConstraint.RIGHT.name())) {
                        rightToLeft.getLeftComponent().setVisible(false);
                        topBottom.getBottomComponent().setVisible(false);
                    } else if (name.equals(PerspectiveConstraint.LEFT.name())) {
                        rightToLeft.getRightComponent().setVisible(false);
                    } else if (name.equals(PerspectiveConstraint.BOTTOM.name())) {
                        rightToLeft.getLeftComponent().setVisible(false);
                        topBottom.getTopComponent().setVisible(false);
                    }
                    pane.setSelectedIndex(i);
                    rightToLeft.getParent().validate();
                    rightToLeft.getParent().repaint();
                }
            });
        }
    }

    private class RestoreListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                    rightToLeft.resetToPreferredSizes();
                    topBottom.resetToPreferredSizes();
                    rightToLeft.getLeftComponent().setVisible(true);
                    rightToLeft.getRightComponent().setVisible(true);
                    topBottom.getBottomComponent().setVisible(true);
                    topBottom.getTopComponent().setVisible(true);
                    pane.setSelectedIndex(i);
                }
            });
        }
    }

    private class ClosingListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int i = ButtonTabComponent.this.pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        try {
                            ButtonTabComponent.this.viewContainer.getApplication().getViewManager().removeView(ButtonTabComponent.this.viewContainer);
                        } catch (ViewException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
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
