package net.sf.swingdocking;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 * @author Arne Limburg
 */
public class TabPage extends JPanel {

    public TabPage(JInternalFrame frame) {
        setLayout(new TabPageLayout(frame));
    }

    public JInternalFrame getInternalFrame() {
        return ((TabPageLayout) getLayout()).getInternalFrame();
    }

    public boolean isShowing() {
        return getParent() != null && getParent().isShowing();
    }

    private class TabPageLayout implements LayoutManager {

        private JInternalFrame frame;

        private Component component;

        public TabPageLayout(JInternalFrame internalFrame) {
            frame = internalFrame;
        }

        public JInternalFrame getInternalFrame() {
            return frame;
        }

        public void layoutContainer(Container parent) {
            if (component == null || !frame.isVisible()) {
                return;
            }
            Insets insets = parent.getInsets();
            int x = insets.left;
            int y = insets.top;
            int width = parent.getWidth() - insets.left - insets.right;
            int height = parent.getHeight() - insets.top - insets.bottom;
            Component contentPane = frame.getContentPane();
            if (frame.isShowing() && contentPane.isShowing()) {
                Point frameLocation = frame.getLocationOnScreen();
                Point contentPaneLocation = contentPane.getLocationOnScreen();
                x -= contentPaneLocation.x - frameLocation.x;
                y -= contentPaneLocation.y - frameLocation.y;
            }
            width += frame.getWidth() - contentPane.getWidth();
            height += frame.getHeight() - contentPane.getHeight();
            component.setBounds(x, y, width, height);
        }

        public Dimension minimumLayoutSize(Container parent) {
            return frame.getContentPane().getMinimumSize();
        }

        public Dimension preferredLayoutSize(Container parent) {
            return frame.getContentPane().getPreferredSize();
        }

        public void addLayoutComponent(String name, Component layoutComponent) {
            component = layoutComponent;
        }

        public void removeLayoutComponent(Component layoutComponent) {
            if (component == layoutComponent) {
                component = null;
            }
        }
    }
}
