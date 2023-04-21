package com.ixora.common.ui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * Status bar component.
 * @author Daniel Moraru (internet initial source)
 */
public abstract class StatusBar extends JPanel {

    private static final long serialVersionUID = 3335949148732524119L;

    /** Left panel */
    private Box leftPanel;

    /** Right panel */
    private Box rightPanel;

    /** Center component */
    private JComponent centerComponent;

    /**
	 * Constructor.
	 */
    protected StatusBar() {
        super();
        setLayout(new BorderLayout(0, 0));
    }

    /**
	 * Adds a new component to the left hand side of the status bar.
	 * @param c
	 */
    protected void addLeftComponent(JComponent c) {
        if (leftPanel == null) {
            leftPanel = new Box(BoxLayout.X_AXIS);
            add(leftPanel, BorderLayout.WEST);
        }
        JPanel container = new JPanel(new BorderLayout(0, 0));
        container.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 0, 0, 2), new ThinBevelBorder(BevelBorder.LOWERED)));
        container.add(c, BorderLayout.CENTER);
        leftPanel.add(container);
    }

    /**
	 * Removes the component from the left hand side of the status bar.
	 */
    protected void removeLeftComponent(JComponent c) {
        if (leftPanel != null) {
            for (int i = 0; i < leftPanel.getComponentCount(); ++i) {
                JComponent container = (JComponent) leftPanel.getComponent(i);
                if (container.isAncestorOf(c)) {
                    leftPanel.remove(container);
                    break;
                }
            }
            if (leftPanel.getComponentCount() == 0) {
                remove(leftPanel);
                leftPanel = null;
            }
        }
    }

    /**
	 * Removes all the components from the left side of the status bar.
	 **/
    protected void removeLeftComponents() {
        if (leftPanel != null) {
            remove(leftPanel);
            leftPanel = null;
        }
    }

    /**
	 * @return	center component
	 */
    protected JComponent getCenterComponent() {
        return centerComponent;
    }

    /**
	 * Sets the component to be displayed in the center area of the status bar.
	 * @param c
	 */
    protected void setCenterComponent(JComponent c) {
        if (c == centerComponent) {
            return;
        }
        if (centerComponent != null) {
            remove(centerComponent);
        }
        centerComponent = c;
        centerComponent.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0), new ThinBevelBorder(BevelBorder.LOWERED)));
        add(centerComponent, BorderLayout.CENTER);
    }

    /**
	 * Adds a new component to the right side of the status bar.
	 * @param c
	 */
    protected void addRightComponent(JComponent c) {
        addRightComponent(c, rightPanel == null ? 0 : rightPanel.getComponentCount());
    }

    /**
	 * Adds a new component to the right side of the status bar at the specified index.
	 * @param c
	 * @param idx 0 is the first component to the right
	 */
    protected void addRightComponent(JComponent c, int idx) {
        if (rightPanel == null) {
            rightPanel = new Box(BoxLayout.X_AXIS);
            add(rightPanel, BorderLayout.EAST);
        }
        JPanel container = new JPanel(new BorderLayout(0, 0));
        container.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 2, 0, 0), new ThinBevelBorder(BevelBorder.LOWERED)));
        container.add(c, BorderLayout.CENTER);
        rightPanel.add(container, idx);
    }

    /**
	 * Removes the component from the right side of the status bar.
	 */
    protected void removeRightComponent(JComponent c) {
        if (rightPanel != null) {
            for (int i = 0; i < rightPanel.getComponentCount(); ++i) {
                JComponent container = (JComponent) rightPanel.getComponent(i);
                if (container.isAncestorOf(c)) {
                    rightPanel.remove(container);
                    break;
                }
            }
            if (rightPanel.getComponentCount() == 0) {
                remove(rightPanel);
                rightPanel = null;
            }
        }
    }

    /**
	 * Removes all the components from the right hand side of the status bar.
	 */
    protected void removeRightComponents() {
        if (rightPanel != null) {
            remove(rightPanel);
            rightPanel = null;
        }
    }
}
