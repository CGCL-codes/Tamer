package xml.gui;

import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.GUIUtilities;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import java.awt.Dimension;
import java.net.URL;

/**
 * Convenience abstract GUI action.
 * copied from the XSLT Plugin : xslt/XsltAction.java
 *
 * @author Robert McKinnon - robmckinnon@users.sourceforge.net
 */
public abstract class UsefulAction extends AbstractAction {

    private String actionType;

    public UsefulAction(String actionType) {
        this.actionType = actionType;
        String actionName = jEdit.getProperty(actionType + ".name");
        String shortcut = jEdit.getProperty(actionType + ".shortcut");
        String shortDescription = jEdit.getProperty(actionType + ".short-desc");
        String iconName = jEdit.getProperty(actionType + ".small-icon");
        shortDescription = (shortcut != null) ? shortDescription + " - " + shortcut : shortDescription;
        putValue(Action.ACTION_COMMAND_KEY, actionType);
        putValue(Action.NAME, actionName);
        putValue(Action.SHORT_DESCRIPTION, shortDescription);
        if (iconName != null) {
            Icon icon;
            if (iconName.startsWith("/")) {
                URL u = getClass().getResource(iconName);
                icon = new ImageIcon(u);
            } else {
                icon = GUIUtilities.loadIcon(iconName);
            }
            putValue(Action.SMALL_ICON, icon);
        }
    }

    public JButton getButton() {
        JButton button = new JButton(this);
        button.setText("");
        button.setName(actionType);
        Dimension dimension = getButtonDimension();
        button.setMinimumSize(dimension);
        button.setPreferredSize(dimension);
        button.setMaximumSize(dimension);
        return button;
    }

    public JRadioButton getRadioButton(String text) {
        JRadioButton button = new JRadioButton(this);
        button.setName(text);
        button.setText(jEdit.getProperty(text));
        return button;
    }

    protected Dimension getButtonDimension() {
        Dimension dimension = new Dimension(30, 30);
        return dimension;
    }

    public JMenuItem getMenuItem() {
        JMenuItem item = new JMenuItem(this);
        item.setIcon(null);
        return item;
    }

    public static JPopupMenu initMenu(Object[] actions) {
        JPopupMenu menu = new JPopupMenu();
        for (int i = 0; i < actions.length; i++) {
            Object action = actions[i];
            if (action == null) {
                menu.addSeparator();
            } else {
                menu.add(((UsefulAction) action).getMenuItem());
            }
        }
        return menu;
    }
}
