package org.sulweb.infureports;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.beans.*;

/**
 * <p>Title: InfuGraph</p>
 * <p>Description: Frontend for Infumon database log display</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Elaborazione Dati Pinerolo srl</p>
 * @author unascribed
 * @version 1.0
 */
public class ConfigDlg extends JDialog {

    private File configFile;

    private Map properties;

    private static final String hydrobalanceExternalAppKey = "hydrobalanceApp", hydrobalanceExternalAppDefault = "", hydrobalanceExternalAppParamsKey = "hydrobalanceAppParams", hydrobalanceExternalAppParamsDefault = "", hydrobalanceSplitParamsKey = "hydrobalanceSplitParams", hydrobalanceSplitParamsDefault = "0", hydrobalanceSavefileKey = "hydrobalaceSavefile", hydrobalanceSavefileDefault = "", hydrobalanceSavefileTypeKey = "hydrobalanceSavetype", hydrobalanceSavefileTypeDefault = "CSV";

    private JLabel externalAppLabel, externalAppParamsLabel, savefileLabel;

    private JTextField externalAppField, externalAppParamsField, savefileField;

    private JButton save, revert, cancel, browseExternalApp, browseSavefile;

    private JCheckBox hydrobalanceSplitParams;

    public ConfigDlg(Frame owner) throws IOException {
        super(owner, "Configurazione bilancio idrico");
        properties = new HashMap();
        String userHome = System.getProperty("user.home");
        configFile = new File(userHome, ".infureportsrc");
        externalAppLabel = new JLabel("Applicazione");
        externalAppField = new JTextField(20);
        externalAppParamsLabel = new JLabel("Paramtetri");
        externalAppParamsField = new JTextField(20);
        hydrobalanceSplitParams = new JCheckBox("Separa i parametri");
        savefileLabel = new JLabel("File d'appoggio");
        savefileField = new JTextField(20);
        save = new JButton("Salva impostazioni");
        revert = new JButton("Annulla modifiche");
        cancel = new JButton("Chiudi senza salvare");
        browseExternalApp = new JButton("Sfoglia...");
        browseSavefile = new JButton("Sfoglia...");
        final Container cpane = getContentPane();
        cpane.setLayout(new GridLayout(4, 3));
        cpane.add(externalAppLabel);
        cpane.add(externalAppField);
        cpane.add(browseExternalApp);
        cpane.add(externalAppParamsLabel);
        cpane.add(externalAppParamsField);
        cpane.add(hydrobalanceSplitParams);
        cpane.add(savefileLabel);
        cpane.add(savefileField);
        cpane.add(browseSavefile);
        cpane.add(save);
        cpane.add(revert);
        cpane.add(cancel);
        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    storeSettings();
                    setVisible(false);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    JOptionPane.showMessageDialog(cpane, "Impossibile salvare sul file " + configFile.getAbsolutePath(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        revert.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                safeInitFromProperties();
                repaint();
            }
        });
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                safeInitFromProperties();
                setVisible(false);
            }
        });
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
                safeInitFromProperties();
                setVisible(false);
            }
        });
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        browseExternalApp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    externalAppField.setText(chooseFile(externalAppField.getText(), true));
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    JOptionPane.showMessageDialog(cpane, "Problemi nel visualizzare la finestra di navigazione del filesystem", "Avviso", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        browseSavefile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    savefileField.setText(chooseFile(savefileField.getText(), false));
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    JOptionPane.showMessageDialog(cpane, "Problemi nel visualizzare la finestra di navigazione del filesystem", "Avviso", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        pack();
        readSettings();
    }

    private void readSettings() {
        try {
            FileInputStream fis = new FileInputStream(configFile);
            XMLDecoder xmld = new XMLDecoder(fis);
            properties = (Map) xmld.readObject();
            xmld.close();
        } catch (IOException e) {
            properties.put(hydrobalanceExternalAppKey, hydrobalanceExternalAppDefault);
            properties.put(hydrobalanceSavefileKey, hydrobalanceSavefileDefault);
            properties.put(hydrobalanceSavefileTypeKey, hydrobalanceSavefileTypeDefault);
        }
        safeInitFromProperties();
    }

    private void initFromProperties() {
        externalAppField.setText(properties.get(hydrobalanceExternalAppKey).toString());
        externalAppParamsField.setText(properties.get(hydrobalanceExternalAppParamsKey).toString());
        boolean selected = false;
        String strSel = properties.get(hydrobalanceSplitParamsKey).toString();
        if (strSel != null && !strSel.equals(hydrobalanceSplitParamsDefault)) selected = true;
        hydrobalanceSplitParams.setSelected(selected);
        savefileField.setText(properties.get(hydrobalanceSavefileKey).toString());
    }

    private void storeSettings() throws IOException {
        setIntoProperties();
        if (!configFile.exists()) configFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(configFile);
        XMLEncoder xmle = new XMLEncoder(fos);
        xmle.writeObject(properties);
        xmle.close();
    }

    private void setIntoProperties() {
        properties.put(hydrobalanceExternalAppKey, externalAppField.getText());
        properties.put(hydrobalanceExternalAppParamsKey, externalAppParamsField.getText());
        String split = hydrobalanceExternalAppParamsDefault;
        if (hydrobalanceSplitParams.isSelected()) split = "1";
        properties.put(hydrobalanceSplitParamsKey, split);
        properties.put(hydrobalanceSavefileKey, savefileField.getText());
    }

    private String chooseFile(String initial, boolean existing) throws IOException {
        JFileChooser jfc = new JFileChooser();
        if (initial != null && initial.length() > 0) {
            File f = new File(initial);
            jfc.setCurrentDirectory(f.getParentFile());
            jfc.setSelectedFile(f);
        }
        if (existing) jfc.setDialogType(JFileChooser.OPEN_DIALOG); else jfc.setDialogType(JFileChooser.SAVE_DIALOG);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setMultiSelectionEnabled(false);
        jfc.showDialog(this, "Seleziona");
        File sel = jfc.getSelectedFile();
        String result = initial;
        if (sel != null) result = sel.getAbsolutePath();
        return result;
    }

    public String getExecPath() {
        return (String) properties.get(hydrobalanceExternalAppKey);
    }

    public String getSavefilePath() {
        return (String) properties.get(hydrobalanceSavefileKey);
    }

    public String getParams() {
        return (String) properties.get(hydrobalanceExternalAppParamsKey);
    }

    public boolean getSplitParams() {
        boolean result = false;
        String strSplit = (String) properties.get(hydrobalanceExternalAppParamsKey);
        if (strSplit != null && !strSplit.equals(hydrobalanceExternalAppParamsDefault)) result = true;
        return result;
    }

    private void safeInitFromProperties() {
        try {
            initFromProperties();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(AppFrame.getApplicationFrame(), "Il formato del file di configurazione è cambiato. Riconfigurare.", "InfuReports", JOptionPane.WARNING_MESSAGE);
        }
    }
}
