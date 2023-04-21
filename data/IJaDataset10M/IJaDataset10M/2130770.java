package de.mogwai.common.client.looks;

import java.awt.Component;
import java.awt.Container;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import de.mogwai.common.client.looks.components.renderer.DefaultTableHeaderRenderer;
import de.mogwai.common.i18n.I18NAble;
import de.mogwai.common.i18n.I18NInitializer;

public final class UIInitializer {

    private static UIInitializer me;

    private UIConfiguration configuration;

    private UIInitializer(UIConfiguration aConfiguration) {
        configuration = aConfiguration;
        if (aConfiguration.isApplyConfiguration()) {
            Map<String, Object> theProperties = aConfiguration.getUIManagerConfig();
            for (String theString : theProperties.keySet()) {
                UIManager.put(theString, theProperties.get(theString));
            }
        }
    }

    public static UIInitializer getInstance(UIConfiguration aConfiguration) {
        if (me == null) {
            me = new UIInitializer(aConfiguration);
        }
        return me;
    }

    public static UIInitializer getInstance() {
        return getInstance(new UIConfiguration());
    }

    public void initialize(Container aRoot) {
        if (aRoot == null) {
            return;
        }
        if (aRoot instanceof JFrame) {
            initialize(((JFrame) aRoot).getJMenuBar());
        }
        initializeComponent(aRoot);
        boolean normal = true;
        if (aRoot instanceof JMenuBar) {
            normal = false;
            JMenuBar theMenu = (JMenuBar) aRoot;
            for (int i = 0; i < theMenu.getMenuCount(); i++) {
                initialize(theMenu.getMenu(i));
            }
        }
        if (aRoot instanceof JMenu) {
            normal = false;
            JMenu theMenu = (JMenu) aRoot;
            for (int i = 0; i < theMenu.getItemCount(); i++) {
                initialize(theMenu.getItem(i));
            }
        }
        if (normal) {
            Component[] theComponents = aRoot.getComponents();
            for (int i = 0; i < theComponents.length; i++) {
                initializeComponent(theComponents[i]);
                if (theComponents[i] instanceof Container) {
                    initialize((Container) theComponents[i]);
                }
            }
        }
    }

    public void initializeFontAndColors(Component aComponent) {
        if (aComponent instanceof JComponent) {
            ((JComponent) aComponent).updateUI();
        }
        if (configuration.isApplyConfiguration()) {
            aComponent.setFont(configuration.getDefaultFont());
            if (aComponent instanceof JPanel) {
                aComponent.setBackground(configuration.getDefaultBackgroundColor());
            }
            if (aComponent instanceof AbstractButton) {
                aComponent.setBackground(configuration.getDefaultBackgroundColor());
            }
            if (aComponent instanceof JDesktopPane) {
                aComponent.setBackground(configuration.getDefaultDesktopPaneBackgroundColor());
            }
            if (aComponent instanceof JComboBox) {
                aComponent.setBackground(configuration.getDefaultBackgroundColor());
            }
            if (aComponent instanceof JLabel) {
                aComponent.setBackground(configuration.getDefaultBackgroundColor());
            }
            if (aComponent instanceof JMenuBar) {
                aComponent.setBackground(configuration.getDefaultBackgroundColor());
            }
            if (aComponent instanceof JMenu) {
                aComponent.setBackground(configuration.getDefaultBackgroundColor());
            }
            if (aComponent instanceof JMenuItem) {
                aComponent.setBackground(configuration.getDefaultBackgroundColor());
            }
            if (aComponent instanceof JPopupMenu) {
                aComponent.setBackground(configuration.getDefaultBackgroundColor());
            }
            if (aComponent instanceof JTableHeader) {
                aComponent.setBackground(configuration.getDefaultBackgroundColor());
            }
            if (aComponent instanceof JToolBar) {
                aComponent.setBackground(configuration.getDefaultBackgroundColor());
            }
        }
        if (aComponent instanceof JTable) {
            JTable theTable = (JTable) aComponent;
            for (int count = 0; count < theTable.getColumnCount(); count++) {
                theTable.getColumnModel().getColumn(count).setHeaderRenderer(new DefaultTableHeaderRenderer());
            }
            initializeComponent(theTable.getTableHeader());
        }
    }

    /**
     * @return the configuration
     */
    public UIConfiguration getConfiguration() {
        return configuration;
    }

    public void initializeComponent(Component aComponent) {
        initializeFontAndColors(aComponent);
        if (aComponent instanceof I18NAble) {
            I18NInitializer.initialize((I18NAble) aComponent);
        }
        if (aComponent instanceof JComponent) {
            Border theBorder = ((JComponent) aComponent).getBorder();
            if (theBorder instanceof I18NAble) {
                I18NInitializer.initialize((I18NAble) theBorder);
            }
        }
    }
}
