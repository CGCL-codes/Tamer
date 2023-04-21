package org.mov.quote;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import org.mov.main.Module;
import org.mov.main.ModuleFrame;
import org.mov.prefs.PreferencesManager;
import org.mov.ui.GridBagHelper;
import org.mov.util.Locale;
import org.mov.util.TradingTime;
import org.mov.util.TradingTimeFormatException;

/**
 * The sync Intra-day quotes module allows automatic downloading of
 * Intra-day quotes from the internet into Venice.
 *
 * @author Andrew Leppard
 * @see IDQuoteSync
 */
public class IDQuoteSyncModule extends JPanel implements Module {

    private JDesktopPane desktop;

    private PropertyChangeSupport propertySupport;

    private JCheckBox isEnabledCheckBox;

    private JComboBox sourceComboBox;

    private JTextField symbolListTextField;

    private JTextField openTimeTextField;

    private JTextField closeTimeTextField;

    private JTextField periodTextField;

    private boolean isEnabled;

    private String symbolListText;

    private List symbolList;

    private TradingTime openTime;

    private TradingTime closeTime;

    private int period;

    private PreferencesManager.IDQuoteSyncPreferences prefs = null;

    /**
     * Create a new Intra-day quote sync module.
     *
     * @param desktop the parent desktop
     */
    public IDQuoteSyncModule(JDesktopPane desktop) {
        this.desktop = desktop;
        propertySupport = new PropertyChangeSupport(this);
        setLayout(new BorderLayout());
        buildGUI();
    }

    /**
     * Layout the GUI.
     */
    private void buildGUI() {
        prefs = PreferencesManager.loadIDQuoteSyncPreferences();
        isEnabledCheckBox = new JCheckBox(Locale.getString("ENABLED"));
        isEnabledCheckBox.setSelected(prefs.isEnabled);
        isEnabledCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                checkDisabledStatus();
            }
        });
        TitledBorder titledBorder = new TitledBorder(Locale.getString("SYNC_ID_TITLE"));
        JPanel titledPanel = new JPanel();
        titledPanel.setBorder(titledBorder);
        titledPanel.setLayout(new BorderLayout());
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        titledPanel.setLayout(gridbag);
        c.weightx = 1.0;
        c.ipadx = 5;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        JLabel label = new JLabel(Locale.getString("SOURCE"));
        c.gridwidth = 1;
        gridbag.setConstraints(label, c);
        titledPanel.add(label);
        sourceComboBox = new JComboBox();
        sourceComboBox.addItem(Locale.getString("YAHOO"));
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(sourceComboBox, c);
        titledPanel.add(sourceComboBox);
        symbolListTextField = GridBagHelper.addTextRow(titledPanel, Locale.getString("SYMBOLS"), prefs.symbols, gridbag, c, 11);
        openTimeTextField = GridBagHelper.addTextRow(titledPanel, Locale.getString("OPEN_TIME"), prefs.openTime.toString(), gridbag, c, 11);
        closeTimeTextField = GridBagHelper.addTextRow(titledPanel, Locale.getString("CLOSE_TIME"), prefs.closeTime.toString(), gridbag, c, 11);
        periodTextField = GridBagHelper.addTextRow(titledPanel, Locale.getString("PERIOD_IN_SECONDS"), Integer.toString(prefs.period), gridbag, c, 11);
        add(isEnabledCheckBox, BorderLayout.NORTH);
        add(titledPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton OKButton = new JButton(Locale.getString("OK"));
        OKButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                sync();
            }
        });
        JButton cancelButton = new JButton(Locale.getString("CANCEL"));
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                propertySupport.firePropertyChange(ModuleFrame.WINDOW_CLOSE_PROPERTY, 0, 1);
            }
        });
        buttonPanel.add(OKButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
        checkDisabledStatus();
    }

    /**
     * Enable/disable the appropriate widgets depending on which widgets
     * are checked.
     */
    private void checkDisabledStatus() {
        boolean isEnabled = isEnabledCheckBox.isSelected();
        sourceComboBox.setEnabled(isEnabled);
        symbolListTextField.setEnabled(isEnabled);
        openTimeTextField.setEnabled(isEnabled);
        closeTimeTextField.setEnabled(isEnabled);
        periodTextField.setEnabled(isEnabled);
    }

    /**
     * Configure Venice to sync Intra-day quotes according to the values given on the user
     * interface.
     */
    private void sync() {
        if (parse()) {
            saveConfiguration();
            activateConfiguration();
            propertySupport.firePropertyChange(ModuleFrame.WINDOW_CLOSE_PROPERTY, 0, 1);
        }
    }

    /**
     * Parse the user values from the GUI and store them in class variables.
     *
     * @return TRUE if the values validated OK; FALSE if they were invalid.
     */
    private boolean parse() {
        isEnabled = isEnabledCheckBox.isSelected();
        symbolListText = symbolListTextField.getText();
        try {
            symbolList = new ArrayList(Symbol.toSortedSet(symbolListText, false));
        } catch (SymbolFormatException e) {
            JOptionPane.showInternalMessageDialog(desktop, e.getMessage(), Locale.getString("INVALID_SYMBOL_LIST"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            openTime = new TradingTime(openTimeTextField.getText());
            closeTime = new TradingTime(closeTimeTextField.getText());
        } catch (TradingTimeFormatException e) {
            JOptionPane.showInternalMessageDialog(desktop, Locale.getString("ERROR_PARSING_TIME", e.getTime()), Locale.getString("INVALID_TIME"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            period = Integer.parseInt(periodTextField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showInternalMessageDialog(desktop, Locale.getString("ERROR_PARSING_NUMBER", periodTextField.getText(), e.getMessage()), Locale.getString("INVALID_PERIOD"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Save the configuration on screen to the preferences file
     */
    private void saveConfiguration() {
        prefs.isEnabled = isEnabled;
        prefs.symbols = symbolListText;
        prefs.openTime = openTime;
        prefs.closeTime = closeTime;
        prefs.period = period;
        PreferencesManager.saveIDQuoteSyncPreferences(prefs);
    }

    /**
     * Activate configuration. Call the module that automatically downloads
     * intra-day quotes and update it for the new settings.
     */
    private void activateConfiguration() {
        IDQuoteSync.getInstance().setPeriod(period);
        IDQuoteSync.getInstance().addSymbols(symbolList);
        IDQuoteSync.getInstance().setTimeRange(openTime, closeTime);
        IDQuoteSync.getInstance().setEnabled(isEnabled);
    }

    /**
     * Add a property change listener for module change events.
     *
     * @param	listener	listener
     */
    public void addModuleChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove a property change listener for module change events.
     *
     * @param	listener	listener
     */
    public void removeModuleChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    /**
     * Return displayed component for this module.
     *
     * @return the component to display.
     */
    public JComponent getComponent() {
        return this;
    }

    /**
     * Return menu bar for quote source preferences module.
     *
     * @return	the menu bar.
     */
    public JMenuBar getJMenuBar() {
        return null;
    }

    /**
     * Return frame icon for quote source preferences module.
     *
     * @return	the frame icon.
     */
    public ImageIcon getFrameIcon() {
        return null;
    }

    /**
     * Returns the window title.
     *
     * @return	the window title.
     */
    public String getTitle() {
        return Locale.getString("SYNC_ID_TITLE");
    }

    /**
     * Return whether the module should be enclosed in a scroll pane.
     *
     * @return	enclose module in scroll bar
     */
    public boolean encloseInScrollPane() {
        return true;
    }

    /**
     * Called when window is closing. We handle the saving explicitly so
     * this is only called when the user clicks on the close button in the
     * top right hand of the window. Dont trigger a save event for this.
     */
    public void save() {
    }
}
