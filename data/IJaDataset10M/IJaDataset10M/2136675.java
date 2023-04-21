package it.jwallpaper.util;

import it.jwallpaper.JWallpaperChanger;
import it.jwallpaper.config.ImageRotationMode;
import it.jwallpaper.uiComponents.enumCombo.EnumComboBoxModel;
import it.jwallpaper.uiComponents.enumCombo.EnumComboBoxRenderer;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import org.apache.commons.lang.StringUtils;

public class UiUtils {

    public static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public static JMenu getMenu(Class bundleClass, String key) {
        JMenu menu = new JMenu();
        setMenuValues(menu, bundleClass, key);
        return menu;
    }

    public static JMenuItem getMenuItem(Class bundleClass, String key) {
        JMenuItem menuItem = new JMenuItem();
        setMenuValues(menuItem, bundleClass, key);
        return menuItem;
    }

    public static void setMenuValues(JMenuItem menuItem, Class bundleClass, String key) {
        String bundleKey;
        bundleKey = key + ".label";
        menuItem.setText(MessageUtils.getMessage(bundleClass, bundleKey));
        bundleKey = key + ".icon";
        if (MessageUtils.hasMessage(bundleClass, bundleKey)) {
            menuItem.setIcon(getIcon(bundleClass, MessageUtils.getMessage(bundleClass, bundleKey)));
        }
        bundleKey = key + ".accelerator";
        if (MessageUtils.hasMessage(bundleClass, bundleKey)) {
            menuItem.setMnemonic(getMnemonic(MessageUtils.getMessage(bundleClass, bundleKey)));
        }
    }

    public static void setActionValues(Action action, Class bundleClass) {
        String actionClass = action.getClass().getSimpleName();
        String bundleKey;
        bundleKey = actionClass + ".label";
        action.putValue(Action.NAME, MessageUtils.getMessage(bundleClass, bundleKey));
        bundleKey = actionClass + ".hoover";
        if (MessageUtils.hasMessage(bundleClass, bundleKey)) {
            action.putValue(Action.SHORT_DESCRIPTION, MessageUtils.getMessage(bundleClass, bundleKey));
        }
        bundleKey = actionClass + ".accelerator";
        if (MessageUtils.hasMessage(bundleClass, bundleKey)) {
            KeyStroke keyStroke = KeyStroke.getKeyStroke(MessageUtils.getMessage(bundleClass, bundleKey));
            action.putValue(Action.ACCELERATOR_KEY, keyStroke);
        }
        bundleKey = actionClass + ".icon";
        if (MessageUtils.hasMessage(bundleClass, bundleKey)) {
            action.putValue(Action.SMALL_ICON, getIcon(bundleClass, MessageUtils.getMessage(bundleClass, bundleKey)));
        }
    }

    public static Icon getIcon(Class loadingClass, String iconName) {
        return new ImageIcon(loadingClass.getResource(iconName));
    }

    public static ImageIcon getImageIcon(Class loadingClass, String iconName) {
        return new ImageIcon(loadingClass.getResource(iconName));
    }

    public static int getMnemonic(String key) {
        return (int) key.charAt(0);
    }

    public static void centerOnScreen(JDialog dialog) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = dialog.getWidth();
        int h = dialog.getHeight();
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        dialog.setLocation(x, y);
    }

    public static void showMessageBox(Component parent, Class resourceBundleClass, String titleKey, String messageKey, int msgType, Object... messageArguments) {
        String msgTitle = MessageUtils.getMessage(resourceBundleClass, titleKey);
        String msgText = MessageUtils.getMessage(resourceBundleClass, messageKey, messageArguments);
        JOptionPane.showMessageDialog(parent, msgText, msgTitle, msgType);
    }

    public static void showMessageBox(Component parent, Class resourceBundleClass, String titleKey, String messageKey, int msgType) {
        String msgTitle = MessageUtils.getMessage(resourceBundleClass, titleKey);
        String msgText = MessageUtils.getMessage(resourceBundleClass, messageKey);
        JOptionPane.showMessageDialog(parent, msgText, msgTitle, msgType);
    }

    public static void showMessageBoxInfo(Component parent, Class resourceBundleClass, String titleKey, String messageKey) {
        showMessageBox(parent, resourceBundleClass, titleKey, messageKey, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showMessageBoxWarning(Component parent, Class resourceBundleClass, String titleKey, String messageKey) {
        showMessageBox(parent, resourceBundleClass, titleKey, messageKey, JOptionPane.WARNING_MESSAGE);
    }

    public static void showMessageBoxError(Component parent, Class resourceBundleClass, String titleKey, String messageKey) {
        showMessageBox(parent, resourceBundleClass, titleKey, messageKey, JOptionPane.ERROR_MESSAGE);
    }

    public static String askUserInput(Component parent, Class resourceBundleClass, String titleKey, String messageKey, Icon icon) {
        String title = MessageUtils.getMessage(resourceBundleClass, titleKey);
        String message = MessageUtils.getMessage(resourceBundleClass, messageKey);
        String ret = (String) JOptionPane.showInputDialog(parent, message, title, JOptionPane.QUESTION_MESSAGE, icon, null, null);
        return StringUtils.isBlank(ret) ? null : ret.trim();
    }

    public static JComboBox getEnumerationComboBox(Class messageResourceClass, Class enumerationClass) {
        JComboBox comboBox = new JComboBox();
        EnumComboBoxModel comboBoxModel = new EnumComboBoxModel(messageResourceClass, enumerationClass);
        comboBox.setModel(comboBoxModel);
        comboBox.setRenderer(new EnumComboBoxRenderer());
        return comboBox;
    }

    public static JComboBox getImageRotationModeComboBox() {
        return getEnumerationComboBox(JWallpaperChanger.class, ImageRotationMode.class);
    }

    public static void addMenuItem(MenuElement parent, JMenuItem item) {
        if (parent instanceof JPopupMenu) {
            ((JPopupMenu) parent).add(item);
        } else {
            ((JMenu) parent).add(item);
        }
    }

    public static void addMenuItem(MenuElement parent, Action action) {
        if (parent instanceof JPopupMenu) {
            ((JPopupMenu) parent).add(action);
        } else {
            ((JMenu) parent).add(action);
        }
    }

    public static void addMenuSeparator(MenuElement parent) {
        if (parent instanceof JPopupMenu) {
            ((JPopupMenu) parent).addSeparator();
        } else {
            ((JMenu) parent).addSeparator();
        }
    }
}
