package com.xdh.export.ui;

import com.xdh.export.CVTGetterManager;
import com.xdh.export.ConnectionManager;
import com.xdh.export.DBConfig;
import com.xdh.export.DTConvertManager;
import java.awt.Font;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import javax.swing.UIManager;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

public class ExportApp extends SingleFrameApplication {

    protected void startup() {
        show(new ExportView(this));
    }

    protected void configureWindow(Window root) {
    }

    public static ExportApp getApplication() {
        return ((ExportApp) Application.getInstance(ExportApp.class));
    }

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        setGlobalFont();
        DBConfig.config();
        CVTGetterManager.init();
        DTConvertManager.init();
        ConnectionManager.init();
        launch(ExportApp.class, new String[] { "121fs" });
    }

    public static void setGlobalFont() {
        Font vFont = new Font("Dialog", 0, 13);
        UIManager.put("ToolTip.font", vFont);
        UIManager.put("Table.font", vFont);
        UIManager.put("TableHeader.font", vFont);
        UIManager.put("TextField.font", vFont);
        UIManager.put("ComboBox.font", vFont);
        UIManager.put("TextField.font", vFont);
        UIManager.put("PasswordField.font", vFont);
        UIManager.put("TextArea.font", vFont);
        UIManager.put("TextPane.font", vFont);
        UIManager.put("EditorPane.font", vFont);
        UIManager.put("FormattedTextField.font", vFont);
        UIManager.put("Button.font", vFont);
        UIManager.put("CheckBox.font", vFont);
        UIManager.put("RadioButton.font", vFont);
        UIManager.put("ToggleButton.font", vFont);
        UIManager.put("ProgressBar.font", vFont);
        UIManager.put("DesktopIcon.font", vFont);
        UIManager.put("TitledBorder.font", vFont);
        UIManager.put("Label.font", vFont);
        UIManager.put("List.font", vFont);
        UIManager.put("TabbedPane.font", vFont);
        UIManager.put("MenuBar.font", vFont);
        UIManager.put("Menu.font", vFont);
        UIManager.put("MenuItem.font", vFont);
        UIManager.put("PopupMenu.font", vFont);
        UIManager.put("CheckBoxMenuItem.font", vFont);
        UIManager.put("RadioButtonMenuItem.font", vFont);
        UIManager.put("Spinner.font", vFont);
        UIManager.put("Tree.font", vFont);
        UIManager.put("ToolBar.font", vFont);
        UIManager.put("OptionPane.messageFont", vFont);
        UIManager.put("OptionPane.buttonFont", vFont);
    }
}
