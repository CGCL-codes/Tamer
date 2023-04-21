package com.pallas.unicore.client.plugins.proxycertificate;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import com.pallas.unicore.client.controls.LayoutTools;
import com.pallas.unicore.client.dialogs.GenericDialog;
import com.pallas.unicore.resourcemanager.ResourceManager;
import com.pallas.unicore.utility.UserMessages;

/**
 * User interface to change default settings
 * 
 * @author Thomas Kentemich
 * @version $Id: ProxyCertificateDefaultsDialog.java,v 1.6 2004/05/17 10:32:33
 *          rraterin Exp $
 */
public class ProxyCertificateDefaultsDialog extends GenericDialog {

    private static DateFormat formatter = ResourceManager.getTimeFormatter();

    private GregorianCalendar validTime = new GregorianCalendar();

    private ProxyCertificateDefaults proxyCertificateDefaults;

    private JCheckBox limitedProxy;

    private JFormattedTextField timeField;

    private JLabel timeLabel;

    private JLabel limitedProxyLabel;

    private JRadioButton button512;

    private JRadioButton button1024;

    private JRadioButton button2048;

    private JRadioButton button4096;

    /**
	 * Constructor takes parent frame as input and builds non-modal dialog
	 * 
	 * @param parentFrame
	 *            reference to parent JFrame
	 * @param proxyCertificateDefaults
	 *  
	 */
    public ProxyCertificateDefaultsDialog(JFrame parentFrame, ProxyCertificateDefaults proxyCertificateDefaults) {
        super(parentFrame, "UNICORE: Proxycertificate Plugin Defaults", false, OK_CANCEL);
        this.proxyCertificateDefaults = proxyCertificateDefaults;
        buildComponents();
        setDefaults();
        setStatusMessage("Enter default values for UNICORE proxy certificate plugin");
        pack();
    }

    private void setDefaults() {
        validTime.setTimeInMillis(proxyCertificateDefaults.getValidTime() - 3600000);
        timeField.setValue(validTime.getTime());
        limitedProxy.setSelected(proxyCertificateDefaults.getLimited());
        int keysize = proxyCertificateDefaults.getKeysize();
        if (keysize == 512) {
            button512.setSelected(true);
        } else if (keysize == 1024) {
            button1024.setSelected(true);
        } else if (keysize == 2048) {
            button2048.setSelected(true);
        } else if (keysize == 4096) {
            button4096.setSelected(true);
        } else {
            button512.setSelected(true);
        }
    }

    /**
	 * Overwrite show method to let email text field always grab focus.
	 */
    public void show() {
        getRootPane().setDefaultButton(okButton);
        super.show();
    }

    /**
	 * Method will be called from parent class
	 * 
	 * @return true if applying was successful
	 */
    protected boolean applyValues() {
        proxyCertificateDefaults.setLimited(limitedProxy.isSelected());
        proxyCertificateDefaults.setKeysize(1024);
        String dateString = this.timeField.getText().trim();
        ParsePosition pos = new ParsePosition(0);
        Date result = formatter.parse(dateString, pos);
        validTime.setTime(result);
        proxyCertificateDefaults.setValidTime(validTime.getTimeInMillis() + 3600000);
        if (!proxyCertificateDefaults.writeToFile()) {
            UserMessages.error("Could not write proxy certificate plugin  defaults to file");
            setStatusMessage("Could not write proxy certificate plugin defaults to file");
        } else {
            setStatusMessage("Successfully stored proxy certificate plugin defaults.");
        }
        return true;
    }

    /**
	 * When cancel was pressed reload user defaults from file
	 * 
	 * @return if canceling was successful
	 */
    protected boolean cancelValues() {
        proxyCertificateDefaults.loadFromFile();
        setDefaults();
        return true;
    }

    /**
	 * Build user panel
	 */
    private void buildComponents() {
        GridBagLayout g = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        JPanel mainPanel = new JPanel(g);
        timeLabel = new JLabel("Proxy is valid for (hh:mm:ss): ");
        c = LayoutTools.makegbc(0, 0, 1, 0, false);
        mainPanel.add(timeLabel, c);
        timeField = new JFormattedTextField(formatter);
        c = LayoutTools.makegbc(1, 0, 2, 100, true);
        mainPanel.add(timeField, c);
        JLabel boxLabel = new JLabel("Key size (bytes): ");
        ButtonGroup grp = new ButtonGroup();
        button512 = new JRadioButton("512");
        button1024 = new JRadioButton("1024");
        button2048 = new JRadioButton("2048");
        button4096 = new JRadioButton("4096");
        grp.add(button512);
        grp.add(button1024);
        grp.add(button2048);
        grp.add(button4096);
        RadioButtonListener listener = new RadioButtonListener();
        button512.addItemListener(listener);
        button1024.addItemListener(listener);
        button2048.addItemListener(listener);
        button4096.addItemListener(listener);
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.X_AXIS));
        radioPanel.add(button512);
        radioPanel.add(button1024);
        radioPanel.add(button2048);
        radioPanel.add(button4096);
        c = LayoutTools.makegbc(0, 1, 1, 0, false);
        mainPanel.add(boxLabel, c);
        c = LayoutTools.makegbc(1, 1, 1, 0, false);
        mainPanel.add(radioPanel, c);
        limitedProxyLabel = new JLabel("Limited proxy: ");
        c = LayoutTools.makegbc(0, 2, 1, 0, false);
        mainPanel.add(limitedProxyLabel, c);
        limitedProxy = new JCheckBox();
        c = LayoutTools.makegbc(1, 2, 2, 100, true);
        mainPanel.add(limitedProxy, c);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    /**
	 * Add tooltips to buttons from super class
	 */
    private void addTooltipsToButtons() {
        okButton.setToolTipText("Store current settings as defaults to disk");
        applyButton.setToolTipText("Store current settings as defaults to " + "disk and close dialog");
        cancelButton.setToolTipText("Close dialog and dismiss changes");
    }

    private class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
        }
    }

    private class RadioButtonListener implements ItemListener {

        public void itemStateChanged(ItemEvent evt) {
            if (evt.getStateChange() == ItemEvent.DESELECTED) {
                return;
            }
        }
    }
}
