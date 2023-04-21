package org.colombbus.tangara.commons.resinject;

import static org.colombbus.tangara.commons.resinject.ResourceConstant.*;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import org.apache.commons.lang.Validate;

/**
 * Inject properties into a {@link JLabel}
 *
 * <pre>
 * Use following suffixes :
 *  - {@link ResourceConstant#TEXT_SUFFIX}
 *  - {@link ResourceConstant#ICON_SUFFIX}
 *  - {@link ResourceConstant#TOOL_TIP_SUFFIX}
 *  - {@link ResourceConstant#MNEMONIC_KEY_SUFFIX}
 *  - {@link ResourceConstant#FOREGROUND_SUFFIX}
 *  - {@link ResourceConstant#BACKGROUND_SUFFIX}
 *  - {@link ResourceConstant#FONT_SUFFIX}
 *</pre>
 *
 * @author gwen
 */
class JMenuInjecter {

    private ClassResource classResource;

    private String key;

    private JMenuItem menu;

    public void setClassResource(ClassResource classResource) {
        Validate.notNull(classResource, "classResource argument is null");
        this.classResource = classResource;
    }

    public void setKey(String key) {
        Validate.notEmpty(key, "key argument is null");
        this.key = key;
    }

    public void setMenu(JMenuItem menu) {
        Validate.notNull(menu, "menu argument is null");
        this.menu = menu;
    }

    public void inject() {
        injectText();
        injectIcon();
        injectToolTip();
        injectMnemonicKey();
        injectForeground();
        injectBackground();
        injectFont();
    }

    private void injectText() {
        String textKey = key + TEXT_SUFFIX;
        if (classResource.containsKey(textKey)) {
            String textValue = classResource.getString(textKey);
            menu.setText(textValue);
        }
    }

    private void injectToolTip() {
        String toolTipKey = key + TOOL_TIP_SUFFIX;
        if (classResource.containsKey(toolTipKey)) {
            String toolTipValue = classResource.getString(toolTipKey);
            menu.setToolTipText(toolTipValue);
        }
    }

    private void injectIcon() {
        String smallIconKey = key + ICON_SUFFIX;
        if (classResource.containsKey(smallIconKey)) {
            Icon iconValue = classResource.getImageIcon(smallIconKey);
            menu.setIcon(iconValue);
        }
    }

    private void injectMnemonicKey() {
        String mnemonicKey = key + MNEMONIC_KEY_SUFFIX;
        if (classResource.containsKey(mnemonicKey)) {
            KeyStroke mnemonicValue = classResource.getKeyStroke(mnemonicKey);
            menu.setMnemonic(mnemonicValue.getKeyCode());
        }
    }

    private void injectForeground() {
        String foregroundKey = key + FOREGROUND_SUFFIX;
        if (classResource.containsKey(foregroundKey)) {
            Color foregroundValue = classResource.getColor(foregroundKey);
            menu.setForeground(foregroundValue);
        }
    }

    private void injectBackground() {
        String backgroundKey = key + BACKGROUND_SUFFIX;
        if (classResource.containsKey(backgroundKey)) {
            Color backgroundValue = classResource.getColor(backgroundKey);
            menu.setBackground(backgroundValue);
        }
    }

    private void injectFont() {
        String fontKey = key + FONT_SUFFIX;
        if (classResource.containsKey(fontKey)) {
            Font fontValue = classResource.getFont(fontKey);
            menu.setFont(fontValue);
        }
    }
}
