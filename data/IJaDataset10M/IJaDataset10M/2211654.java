package net.sourceforge.jruntimedesigner.utils;

import java.awt.Toolkit;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * This class implements static utility methods to configure actions
 */
public class ActionUtilities {

    private static final String INTERNAL_NAME = "InternalName";

    /**
   * sets the internal name of the action
   *
   * @param a - an action
   * @param internalName - the new value
   */
    public static void setInternalName(Action a, String internalName) {
        a.putValue(INTERNAL_NAME, internalName);
    }

    /**
   * Returns the internal name of the action
   *
   * @param a - an action
   * @return - the internal name of the action
   */
    public static Object getInternalName(Action a) {
        return a.getValue(INTERNAL_NAME);
    }

    /**
   * Loads action configuration from a ResourceBundle and sets the actions internal settings
   *
   * @param a - an action
   * @param res - the ResourceBundle to load action setting from
   * @param internalName - the new value
   */
    public static void actionConfigure(Action a, ResourceBundle res, String internalName) {
        String s;
        try {
            s = res.getString(internalName + "." + Action.NAME);
            a.putValue(Action.NAME, s);
        } catch (MissingResourceException ex) {
        }
        try {
            s = res.getString(internalName + "." + Action.MNEMONIC_KEY);
            if (s != null && s.length() > 0) {
                s = s.toUpperCase();
                a.putValue(Action.MNEMONIC_KEY, new Integer(s.charAt(0)));
            }
        } catch (MissingResourceException ex) {
        }
        try {
            s = res.getString(internalName + "." + Action.SHORT_DESCRIPTION);
            a.putValue(Action.SHORT_DESCRIPTION, s);
        } catch (MissingResourceException ex) {
        }
        try {
            s = res.getString(internalName + "." + Action.LONG_DESCRIPTION);
            a.putValue(Action.LONG_DESCRIPTION, s);
        } catch (MissingResourceException ex) {
        }
        try {
            ImageIcon myIcon = null;
            s = res.getString(internalName + "." + Action.SMALL_ICON);
            try {
                java.net.URL url = ActionUtilities.class.getClassLoader().getResource(s);
                if (url != null) myIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(url));
            } catch (Exception e) {
            }
            if ((myIcon != null) && (myIcon.getIconHeight() > 0) && (myIcon.getIconWidth() > 0)) a.putValue(Action.SMALL_ICON, myIcon);
        } catch (MissingResourceException ex) {
        }
        try {
            s = res.getString(internalName + "." + Action.ACCELERATOR_KEY);
            if (s != null && s.length() > 0) a.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(s));
        } catch (MissingResourceException ex) {
        }
        setInternalName(a, internalName);
    }

    /**
   * Applies the actions settings to a gui component. The configuration information is taken
   * from the actions NAME value
   *
   * @param a - an action
   * @param comp - a gui component
   * @param res - the ResourceBundle to load action setting from
   */
    public static void setActionForComponent(Action a, JComponent comp, ResourceBundle res) {
        setActionForComponent(a, comp, res, null);
    }

    /**
   * Applies the actions settings to a gui component. The configuration information is taken
   * from the ResourceBundle with the key of internalName
   *
   * @param a - an action
   * @param comp - a gui component
   * @param res - the ResourceBundle to load action setting from
   * @param internalName - the new value
   */
    public static void setActionForComponent(Action a, JComponent comp, ResourceBundle res, String internalName) {
        if (internalName == null) {
            internalName = (String) a.getValue(Action.NAME);
        }
        if (comp instanceof AbstractButton) {
            ((AbstractButton) comp).setAction(a);
            actionConfigure(a, res, internalName);
        }
        if (comp instanceof JMenuItem) {
            KeyStroke stroke = (KeyStroke) a.getValue(Action.ACCELERATOR_KEY);
            if (stroke != null) {
                ((JMenuItem) comp).setAccelerator(stroke);
            }
        }
    }

    /**
   * Enables the accelerator key on a JButton for actions not attached
   * to any menu item.
   * <br><b>NOTE:</b>
   * Calling this method is not necessary if the action is attached to a menu item;  
   * in this case the accelerator key is activated by Swing.
   * <p>The method gets the accelerator key from action 
   * property <tt>Action.ACCELERATOR_KEY</tt> and binds it directly to
   * the JButton.
   *
   * @param btn the Button
   * @param a  the action
   */
    public static final void activateAcceleratorKeyOnButton(JButton btn, Action a) {
        KeyStroke ks = (KeyStroke) a.getValue(Action.ACCELERATOR_KEY);
        if (ks != null) {
            InputMap inputMap = btn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap actionMap = btn.getActionMap();
            if (inputMap != null && actionMap != null) {
                Object name = a.getValue(Action.NAME);
                inputMap.put(ks, name);
                actionMap.put(name, a);
            }
        }
    }

    /**
   * Enables the accelerator key on a JComponent for actions not attached
   * to any menu item.
   * <br><b>NOTE:</b>
   * Calling this method is not necessary if the action is attached to a menu item;  
   * in this case the accelerator key is activated by Swing.
   * <p>The method gets the accelerator key from action 
   * property <tt>Action.ACCELERATOR_KEY</tt> and binds it directly to
   * the JButton.
   *
   * @param comp the Component
   * @param a  the action
   */
    public static final void activateAcceleratorKeyOnComponent(JComponent comp, Action a) {
        KeyStroke ks = (KeyStroke) a.getValue(Action.ACCELERATOR_KEY);
        if (ks != null) {
            InputMap inputMap = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap actionMap = comp.getActionMap();
            if (inputMap != null && actionMap != null) {
                Object name = a.getValue(Action.NAME);
                inputMap.put(ks, name);
                actionMap.put(name, a);
            }
        }
    }
}
