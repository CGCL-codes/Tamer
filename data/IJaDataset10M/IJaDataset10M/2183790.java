package org.goniolab.config;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.almondframework.commons.ATabbedPane;
import org.almondframework.ui.dialog.AButtonPanel.ADialogButton;
import org.almondframework.ui.dialog.AConfigure;
import org.goniolab.GonioLab;
import org.goniolab.Settings;

/**
 *
 * @author Patrik Karlsson
 */
public class Config extends AConfigure {

    private static GonioLab gonioLab;

    private Settings newSettings = new Settings();

    private ConfigModule[] modules = new ConfigModule[ConfigModuleItem.values().length];

    private ATabbedPane tabbedPane = new ATabbedPane();

    public Config(boolean modal) {
        super(modal);
        init();
    }

    public Config(Dialog owner, boolean modal) {
        super(owner, modal);
        init();
    }

    public Config(Dialog owner) {
        super(owner);
        init();
    }

    public static void showConfigDialog(GonioLab aGonioLab, boolean modal) {
        gonioLab = aGonioLab;
        Config config = new Config(modal);
        config.getUI().centerInOwner();
        config.getUI().pack();
    }

    private void init() {
        initModules();
        initPanels();
        initButtonListeners();
        ADialogButton[] dialogButtons = { ADialogButton.APPLY, ADialogButton.DEFAULTS, ADialogButton.RESET, ADialogButton.OK, ADialogButton.CANCEL };
        getUI().getDialogButtonPanel().setButtons(dialogButtons);
        setLocale(getAApplication().getLocale());
        getUI().setPreferredSize(new java.awt.Dimension(600, 420));
        getUI().getDialogButtonPanel().setFocusedButton(ADialogButton.OK);
        getUI().pack();
        newSettings = (Settings) gonioLab.getSettings().clone();
        uiSetValues();
        setLocale(getAApplication().getLocale());
    }

    private void initButtonListeners() {
        getUI().getDialogButtonPanel().getButton(ADialogButton.APPLY).addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                uiGetValues();
                gonioLab.applySettings(newSettings);
            }
        });
        getUI().getDialogButtonPanel().getButton(ADialogButton.CANCEL).addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                getUI().setVisible(false);
                System.exit(1);
            }
        });
        getUI().getDialogButtonPanel().getButton(ADialogButton.DEFAULTS).addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                newSettings.restoreDefaults();
                uiSetValues();
            }
        });
        getUI().getDialogButtonPanel().getButton(ADialogButton.OK).addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                getUI().getDialogButtonPanel().getButton(ADialogButton.APPLY).doClick();
                getUI().setVisible(false);
            }
        });
        getUI().getDialogButtonPanel().getButton(ADialogButton.RESET).addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                newSettings = (Settings) gonioLab.getSettings().clone();
                uiSetValues();
            }
        });
    }

    private void initModules() {
        modules[ConfigModuleItem.LAYOUT.ordinal()] = new ModuleLayout();
        modules[ConfigModuleItem.PATHS.ordinal()] = new ModulePaths();
        modules[ConfigModuleItem.UNITS.ordinal()] = new ModuleUnits();
        for (ConfigModuleItem configModuleItem : ConfigModuleItem.values()) {
            tabbedPane.add(modules[configModuleItem.ordinal()].getUI().getPanel());
            tabbedPane.setTitleAt(configModuleItem.ordinal(), GonioLab.getLexicon()._(configModuleItem.toString()));
        }
    }

    private void initPanels() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
        mainPanel.add(tabbedPane);
        getUI().add(mainPanel);
    }

    private void uiGetValues() {
    }

    private void uiSetValues() {
    }
}
