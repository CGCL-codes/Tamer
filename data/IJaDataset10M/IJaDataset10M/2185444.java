package de.miethxml.toolkit.ui;

import java.awt.Component;
import javax.swing.Action;
import javax.swing.JComponent;

/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth </a>
 *
 */
public interface ToolBarManager {

    public static final String ROLE = ToolBarManager.class.getName();

    public static final int LAST = -3;

    public static final int APPEND = -1;

    public static final int BEFORE_LAST = -2;

    public void addAction(Action action);

    public void addAction(Action action, int index);

    public void addComponent(Component comp);

    public void addComponent(Component comp, int index);

    public JComponent getToolBar();
}
