package net.sourceforge.glaf;

import java.util.ArrayList;
import java.util.List;
import javax.swing.UIDefaults;
import javax.swing.plaf.basic.BasicLookAndFeel;

/**
 *
 * @author George Wyatt
 */
public class GlafLookAndFeel extends BasicLookAndFeel {

    @Override
    public String getName() {
        return "GLaF";
    }

    @Override
    public String getID() {
        return "GLaF";
    }

    @Override
    public String getDescription() {
        return "A Look and Feel inspired by Google.";
    }

    @Override
    public boolean isNativeLookAndFeel() {
        return false;
    }

    @Override
    public boolean isSupportedLookAndFeel() {
        return true;
    }

    @Override
    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);
        String packageName = "net.sourceforge.glaf";
        String classPrefix = "Glaf";
        String[] defaultKeys = { "ButtonUI", "CheckBoxUI", "ColorChooserUI", "FormattedTextFieldUI", "MenuBarUI", "MenuUI", "MenuItemUI", "CheckBoxMenuItemUI", "RadioButtonMenuItemUI", "RadioButtonUI", "ToggleButtonUI", "PopupMenuUI", "ProgressBarUI", "ScrollBarUI", "ScrollPaneUI", "SplitPaneUI", "SliderUI", "SeparatorUI", "SpinnerUI", "ToolBarSeparatorUI", "PopupMenuSeparatorUI", "TabbedPaneUI", "TextAreaUI", "TextFieldUI", "PasswordFieldUI", "TextPaneUI", "EditorPaneUI", "TreeUI", "LabelUI", "ListUI", "ToolBarUI", "ToolTipUI", "ComboBoxUI", "TableUI", "TableHeaderUI", "InternalFrameUI", "DesktopPaneUI", "DesktopIconUI", "OptionPaneUI", "PanelUI", "ViewportUI", "RootPaneUI" };
        List<Object> uiDefaults = new ArrayList<Object>();
        for (String defaultKey : defaultKeys) {
            String defaultValue = packageName + "." + classPrefix + defaultKey;
            try {
                Class.forName(defaultValue);
            } catch (ClassNotFoundException ex) {
                System.err.println("Class not found: " + defaultValue);
                continue;
            }
            table.put(defaultKey, defaultValue);
        }
    }
}
