package com.umc.gui.scanner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.effect.BufferedImageOpEffect;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;
import com.lowagie.text.Font;
import com.umc.gui.scanner.components.ExtButton;
import com.umc.gui.scanner.components.ExtLabel;
import com.umc.gui.scanner.components.ExtPanel;
import com.umc.helper.UMCConstants;
import com.umc.helper.UMCLanguage;
import com.umc.helper.UPNP;
import de.umcProject.xmlbeans.SkinDocument;
import de.umcProject.xmlbeans.UmcConfigDocument;
import de.umcProject.xmlbeans.UmcConfigDocument.UmcConfig.Libraries.Library;
import de.umcProject.xmlbeans.UmcConfigDocument.UmcConfig.Libraries.Library.MovieScanDir;
import com.jhlabs.image.GaussianFilter;

public class WizardFrame2 extends JDialog {

    private static Logger log = Logger.getLogger("com.umc.file");

    private static String selectedLanguage = "EN";

    private static boolean nimbusFound = true;

    private JPanel stepsGroup = null;

    private float blurRadius = 15f;

    private LockableUI lockableUIStep = null;

    private JXLayer<JComponent> l = null;

    private BufferedImageOpEffect blurEffect = null;

    private static JEditorPane leftCol = new JEditorPane("text/html", "");

    private static JPanel step1Panel = null;

    private static JPanel step2Panel = null;

    private static JPanel step3Panel = null;

    private static JPanel step4Panel = null;

    private static JButton btnEdit = null;

    private static JButton btnScan = null;

    private JComboBox languageCombo = null;

    private static JCheckBox typeCbx1 = null;

    private static JCheckBox typeCbx2 = null;

    private static JCheckBox typeCbx3 = null;

    private static JTextField scanDirFld = null;

    private static ExtButton btnDir = null;

    private static JComboBox shareCombo = null;

    private static JTextField shareComboAdditionalPath = null;

    private static JLabel labelNTMPath = null;

    public static JTextField txtShare = null;

    /**Für Step 3*/
    JComboBox targetCombo = null;

    /**Für Step 4*/
    JComboBox skinCombo = null;

    public WizardFrame2(JFrame owner) {
        try {
            try {
                MainFrame.wizardConfigDoc = UmcConfigDocument.Factory.parse(WizardFrame2.class.getResource("/com/umc/umc-config[default].xml"));
                MainFrame.wizardConfig = MainFrame.wizardConfigDoc.getUmcConfig();
                MainFrame.wizardConfig.setName("[Wizard Config]");
                MainFrame.wizardConfig.setDescription("Created by the wizard");
                MainFrame.wizardConfig.setLanguage(MainFrame.selectedLanguage.getLanguage().toUpperCase());
            } catch (IOException exc) {
                JOptionPane.showMessageDialog(null, "Default configuration could not be created " + exc, "Error", JOptionPane.ERROR_MESSAGE);
            } catch (XmlException exc) {
                JOptionPane.showMessageDialog(null, "Default configuration could not be created: " + exc, "Error", JOptionPane.ERROR_MESSAGE);
            }
            try {
                if (nimbusFound) UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); else UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (UnsupportedLookAndFeelException exc) {
            } catch (IllegalAccessException exc) {
            } catch (InstantiationException exc) {
            } catch (ClassNotFoundException exc) {
            }
            setTitle(UMCConstants.versionLong + " - " + UMCLanguage.getText("gui.wizard.title", MainFrame.selectedLanguage));
            setAlwaysOnTop(false);
            setSize(1000, 600);
            setResizable(false);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            setModal(true);
            addWindowListener(new WindowListener() {

                public void windowActivated(WindowEvent arg0) {
                }

                public void windowClosed(WindowEvent arg0) {
                }

                public void windowClosing(WindowEvent arg0) {
                    if (JOptionPane.showConfirmDialog(null, UMCLanguage.getText("gui.wizard.question.1.text", MainFrame.selectedLanguage), UMCLanguage.getText("gui.wizard.question.1.title", MainFrame.selectedLanguage), JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                        setVisible(false);
                        dispose();
                    }
                    return;
                }

                public void windowDeactivated(WindowEvent arg0) {
                }

                public void windowDeiconified(WindowEvent arg0) {
                }

                public void windowIconified(WindowEvent arg0) {
                }

                public void windowOpened(WindowEvent arg0) {
                }
            });
            setLayout(new GridBagLayout());
            GridBagConstraints c = null;
            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BorderLayout());
            leftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.lightGray));
            if (nimbusFound) leftCol.setBackground(new Color(232, 232, 255, 0));
            leftCol.setFont(new java.awt.Font("Dialog", Font.NORMAL, 11));
            leftCol.setEditable(false);
            leftCol.setOpaque(false);
            leftCol.setSize(300, 600);
            leftCol.setPreferredSize(new Dimension(300, 600));
            leftCol.setMinimumSize(new Dimension(300, 600));
            HTMLEditorKit kit = new HTMLEditorKit();
            StyleSheet myStyle = new StyleSheet();
            myStyle.addRule("body { font-family: Arial; font-size: 12pt; color: black; text-align: left; vertical-align: top; }");
            myStyle.addRule("a { color: #cc0066; text-decoration: none; }");
            kit.setStyleSheet(myStyle);
            HTMLDocument myDoc = (HTMLDocument) (kit.createDefaultDocument());
            leftCol.setDocument(myDoc);
            leftCol.setText(UMCLanguage.getText("gui.wizard.step1.text1", MainFrame.selectedLanguage));
            leftPanel.add(leftCol, BorderLayout.CENTER);
            JPanel group = new JPanel();
            group.setLayout(new BorderLayout());
            ImageIcon flagIcon = null;
            ActionListener al = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    JButton jbtn = (JButton) e.getSource();
                    String name = jbtn.getName();
                    MainFrame.selectedLanguage = new Locale(name);
                    leftCol.setText(UMCLanguage.getText("gui.wizard.step1.text1", MainFrame.selectedLanguage));
                    remove(l);
                    getSteps(false);
                    GridBagConstraints c = new GridBagConstraints();
                    c.anchor = GridBagConstraints.NORTHWEST;
                    c.fill = GridBagConstraints.BOTH;
                    c.gridx = 1;
                    c.gridy = 0;
                    c.weightx = 1.0;
                    c.weighty = 1.0;
                    c.gridheight = 4;
                    c.insets = new Insets(0, 4, 0, 4);
                    add(l, c);
                    validate();
                    repaint();
                    btnEdit.setEnabled(true);
                    btnScan.setEnabled(true);
                }
            };
            JPanel flags = new JPanel();
            JButton flag = null;
            for (String isoLanguage : UMCConstants.movieGuiLanguages.values()) {
                flag = new JButton();
                flag.setName(isoLanguage.toUpperCase());
                flag.addActionListener(al);
                flagIcon = new ImageIcon(MainFrame.uiu.loadImage(System.getProperty("user.dir") + UMCConstants.fileSeparator + "resources" + UMCConstants.fileSeparator + "Gui" + UMCConstants.fileSeparator + "flags24" + UMCConstants.fileSeparator + isoLanguage.toLowerCase() + ".png"));
                flag.setIcon(flagIcon);
                flags.add(flag);
            }
            group.add(flags, BorderLayout.CENTER);
            leftPanel.add(group, BorderLayout.SOUTH);
            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.NORTHWEST;
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 1.0;
            c.weighty = 1.0;
            c.gridheight = 5;
            c.insets = new Insets(0, 4, 0, 4);
            add(leftPanel, c);
            getSteps(true);
            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.NORTHWEST;
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 1;
            c.gridy = 0;
            c.weightx = 1.0;
            c.weighty = 1.0;
            c.gridheight = 4;
            c.insets = new Insets(0, 4, 0, 4);
            add(l, c);
            al = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    StringBuffer sb = new StringBuffer();
                    MainFrame.wizardConfig.setBackgroundworker(new BigInteger("10"));
                    MainFrame.wizardConfig.setLanguage(UMCConstants.movieInformationLanguages.get(languageCombo.getSelectedItem().toString()));
                    String device = targetCombo.getSelectedItem().toString();
                    if (device.endsWith(" (PCH-A100,PCH-A110, HDX,...)")) device = device.replaceAll(" (PCH-A100,PCH-A110, HDX,...)", "").trim();
                    MainFrame.wizardConfig.setDevice(device);
                    switch(targetCombo.getSelectedIndex()) {
                        case 0:
                            MainFrame.wizardConfig.setPhp("#!/mnt/syb8634/server/php5-cgi");
                            MainFrame.wizardConfig.getMediacenter().setShare(MainFrame.wizardConfig.getMediacenter().getShare().replaceAll("HARD_DISK", "SATA_DISK"));
                            break;
                        case 1:
                            MainFrame.wizardConfig.setPhp("#!/nmt/apps/server/php5-cgi");
                            MainFrame.wizardConfig.getMediacenter().setShare(MainFrame.wizardConfig.getMediacenter().getShare().replaceAll("HARD_DISK", "SATA_DISK"));
                            break;
                        case 2:
                            MainFrame.wizardConfig.setPhp("#!/mnt/syb8634/server/php5-cgi");
                            MainFrame.wizardConfig.getMediacenter().setShare(MainFrame.wizardConfig.getMediacenter().getShare().replaceAll("SATA_DISK", "HARD_DISK"));
                            break;
                    }
                    if (!typeCbx1.isSelected() && !typeCbx2.isSelected() && !typeCbx3.isSelected()) {
                        typeCbx1.setBackground(Color.red);
                        typeCbx2.setBackground(Color.red);
                        typeCbx3.setBackground(Color.red);
                        sb.append("<b><font color='red'>- at least one checkbox must be selected</b></font><br>");
                    }
                    if (StringUtils.isEmpty(scanDirFld.getText()) || !new File(scanDirFld.getText()).exists()) {
                        scanDirFld.setBackground(Color.red);
                        sb.append("<b><font color='red'>- scan dir " + scanDirFld.getText() + " is not valid</font></b><br>");
                    }
                    String value = shareCombo.getSelectedItem() + "";
                    if (StringUtils.isEmpty(value) || value.endsWith("[...]")) {
                        shareCombo.setBackground(Color.red);
                        sb.append("<b><font color='red'>- nmt path not valid</font></b><br>");
                    }
                    String s1 = shareCombo.getSelectedItem().toString();
                    String s2 = "";
                    if (StringUtils.isNotEmpty(s1)) {
                        if (!s1.endsWith("/")) s1 = s1 + "/";
                        if (shareComboAdditionalPath != null) {
                            s2 = shareComboAdditionalPath.getText();
                            if (StringUtils.isNotEmpty(s2)) {
                                s2 = s2.replaceAll("\\\\", "/");
                                if (s2.startsWith("/")) s2 = s2.substring(1);
                            }
                        }
                    }
                    if (shareComboAdditionalPath != null) shareComboAdditionalPath.setText(s2);
                    if (!s1.startsWith("file:///opt/sybhttpd/localhost.drives/")) s1 = "file:///opt/sybhttpd/localhost.drives/NETWORK_SHARE/" + s1;
                    if (MainFrame.wizardConfig.getLibraries().getLibraryArray().length == 0) {
                        Library l = MainFrame.wizardConfig.getLibraries().addNewLibrary();
                        l.setName("Wizard Lib");
                        l.setDescription("Created by the wizard");
                        l.setIgnore(false);
                        MovieScanDir msd = l.addNewMovieScanDir();
                        msd.setPcDir(scanDirFld.getText());
                        msd.setPchDir((s1 + s2).trim());
                        msd.setScanType(0);
                        msd.setSubdirs(true);
                        l.setMovieScanDir(msd);
                    } else {
                        MainFrame.wizardConfig.getLibraries().getLibraryArray()[0].getMovieScanDir().setPcDir(scanDirFld.getText());
                        MainFrame.wizardConfig.getLibraries().getLibraryArray()[0].getMovieScanDir().setPchDir((s1 + s2).trim());
                    }
                    if (StringUtils.isEmpty(skinCombo.getSelectedItem() + "")) sb.append("<b><font color='red'>- select a skin</font></b><br>"); else MainFrame.wizardConfig.getMediacenter().setSkin(skinCombo.getSelectedItem() + "");
                    String errors = sb.toString();
                    if (StringUtils.isNotEmpty(errors)) {
                        leftCol.setText(sb.toString());
                    } else {
                        try {
                            dispose();
                            String xmlPath = System.getProperty("user.dir") + UMCConstants.fileSeparator + "settings" + UMCConstants.fileSeparator + "umc-config[wizard].xml";
                            XmlOptions options = new XmlOptions();
                            options.setSaveAggressiveNamespaces();
                            options.setSavePrettyPrint();
                            MainFrame.wizardConfigDoc.save(new File(xmlPath), options);
                            MainFrame.loadConfig(xmlPath);
                            JButton selectedBtn = (JButton) e.getSource();
                            if (selectedBtn.getName().equals("edit")) {
                                MainFrame.getInstance().showSettings();
                            } else {
                                MainFrame.getInstance().startScan();
                            }
                        } catch (IOException exc) {
                            leftCol.setText("<b><font color='red'>Error saving configuration</font></b><br/>" + exc);
                        }
                    }
                }
            };
            JPanel btnGroup = new JPanel();
            if (nimbusFound) btnGroup.setBackground(new Color(232, 232, 255, 0));
            btnEdit = new JButton(UMCLanguage.getText("gui.wizard.btn.edit", MainFrame.selectedLanguage));
            btnEdit.setName("edit");
            btnEdit.setEnabled(false);
            btnEdit.addActionListener(al);
            btnGroup.add(btnEdit);
            btnScan = new JButton(UMCLanguage.getText("gui.wizard.btn.scan", MainFrame.selectedLanguage));
            btnScan.setName("scan");
            btnScan.setEnabled(false);
            btnScan.addActionListener(al);
            btnGroup.add(btnScan);
            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.SOUTHEAST;
            c.gridx = 1;
            c.gridy = 4;
            add(btnGroup, c);
            setVisible(true);
        } catch (Exception exc) {
            log.error(exc);
        }
    }

    private JPanel getStep1() {
        step1Panel = new JPanel();
        if (nimbusFound) step1Panel.setBackground(new Color(232, 232, 255, 0));
        JPanel result = new JPanel();
        result.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.0;
        c.weightx = 0.0;
        c.insets = new Insets(0, 8, 0, 4);
        result.add(getHelpPanel(1), c);
        try {
            step1Panel.setLayout(new GridBagLayout());
            step1Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), UMCLanguage.getText("gui.wizard.step2.title", MainFrame.selectedLanguage), TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", Font.BOLD, 12)));
            JEditorPane jep = new JEditorPane("text/html", "");
            if (nimbusFound) jep.setBackground(new Color(232, 232, 255, 0));
            jep.setFont(new java.awt.Font("Dialog", Font.NORMAL, 11));
            jep.setEditable(false);
            jep.setOpaque(false);
            jep.setBorder(null);
            HTMLEditorKit kit = new HTMLEditorKit();
            StyleSheet myStyle = new StyleSheet();
            myStyle.addRule("body { font-family: Arial; font-size: 18pt; color: black;    background-color: white;text-align: left; vertical-align: top; }");
            myStyle.addRule("a { color: #cc0066; text-decoration: none; }");
            HTMLDocument myDoc = (HTMLDocument) (kit.createDefaultDocument());
            jep.setDocument(myDoc);
            jep.setText(UMCLanguage.getText("gui.wizard.step2.text1", MainFrame.selectedLanguage));
            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(0, 4, 0, 4);
            step1Panel.add(jep, c);
            languageCombo = new JComboBox();
            int selectedItem = -1;
            int i = -1;
            Iterator<String> ite = UMCConstants.movieInformationLanguages.keySet().iterator();
            while (ite.hasNext()) {
                String key = ite.next();
                languageCombo.addItem(key);
                i++;
                if (UMCConstants.movieInformationLanguages.get(key).equalsIgnoreCase((MainFrame.wizardConfig.getLanguage()))) {
                    selectedItem = i;
                }
            }
            languageCombo.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String key = (String) e.getItem();
                        String value = UMCConstants.movieInformationLanguages.get(key);
                        MainFrame.wizardConfig.setLanguage(StringUtils.isNotEmpty(value) ? value : "en");
                    }
                }
            });
            languageCombo.setSelectedIndex(selectedItem != -1 ? selectedItem : 0);
            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.NORTHWEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            c.weighty = 1.0;
            c.gridx = 1;
            c.gridy = 0;
            c.gridwidth = 1;
            c.insets = new Insets(0, 4, 0, 4);
            step1Panel.add(languageCombo, c);
        } catch (Exception exc) {
            log.error("Error in Wizard - Step 1", exc);
        }
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        result.add(step1Panel, c);
        return result;
    }

    private JPanel getStep2() {
        step2Panel = new JPanel();
        if (nimbusFound) step2Panel.setBackground(new Color(232, 232, 255, 0));
        JPanel result = new JPanel();
        result.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.0;
        c.weightx = 0.0;
        c.insets = new Insets(0, 8, 0, 4);
        result.add(getHelpPanel(2), c);
        try {
            step2Panel.setLayout(new GridBagLayout());
            step2Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), UMCLanguage.getText("gui.wizard.step7.title", MainFrame.selectedLanguage), TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", Font.BOLD, 12)));
            JLabel jl = new JLabel(UMCLanguage.getText("gui.wizard.step7.text2", MainFrame.selectedLanguage));
            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(0, 4, 0, 4);
            step2Panel.add(jl, c);
            if (targetCombo == null) {
                targetCombo = new JComboBox();
                targetCombo.addItem("PCH-B110");
                targetCombo.addItem("PCH-C200");
                targetCombo.addItem("Other NMT (PCH-A100,PCH-A110, HDX,...)");
                targetCombo.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                    }
                });
                targetCombo.setSelectedIndex(2);
            }
            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.NORTHWEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            c.weighty = 1.0;
            c.gridx = 1;
            c.gridy = 0;
            c.insets = new Insets(0, 4, 0, 4);
            step2Panel.add(targetCombo, c);
        } catch (Exception exc) {
            log.error("Error in Wizard - Step 2", exc);
        }
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        result.add(step2Panel, c);
        return result;
    }

    private JPanel getStep3() {
        step3Panel = new JPanel();
        if (nimbusFound) step3Panel.setBackground(new Color(232, 232, 255, 0));
        JPanel result = new JPanel();
        result.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.0;
        c.weightx = 0.0;
        c.insets = new Insets(0, 8, 0, 4);
        result.add(getHelpPanel(3), c);
        try {
            step3Panel.setLayout(new GridBagLayout());
            step3Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), UMCLanguage.getText("gui.wizard.step3.title", MainFrame.selectedLanguage), TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", Font.BOLD, 12)));
            ItemListener il = new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    JOptionPane.showMessageDialog(null, "AAA");
                }
            };
            typeCbx1 = new JCheckBox(UMCLanguage.getText("gui.wizard.step3.text2", MainFrame.selectedLanguage));
            typeCbx1.setSelected(true);
            typeCbx1.setEnabled(false);
            typeCbx1.addItemListener(il);
            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.NORTHWEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(0, 4, 0, 4);
            step3Panel.add(typeCbx1, c);
            scanDirFld = new JTextField(scanDirFld != null ? scanDirFld.getText() : "");
            scanDirFld.setEditable(false);
            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.NORTHWEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            c.gridx = 1;
            c.gridy = 0;
            c.insets = new Insets(0, 4, 0, 4);
            step3Panel.add(scanDirFld, c);
            btnDir = new ExtButton(UMCLanguage.getText("gui.wizard.step3.text5", MainFrame.selectedLanguage));
            btnDir.setTag(scanDirFld);
            btnDir.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    try {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setDialogTitle(UMCLanguage.getText("gui.wizard.step3.text6", MainFrame.selectedLanguage));
                        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        File f = new File(new File(".").getCanonicalPath());
                        fileChooser.setCurrentDirectory(f);
                        int returnValue = fileChooser.showOpenDialog(null);
                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            File selectedFile = fileChooser.getSelectedFile();
                            ((JTextField) ((ExtButton) ae.getSource()).getTag()).setText(selectedFile.getAbsolutePath());
                        }
                    } catch (IOException exc) {
                    }
                }
            });
            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.NORTHWEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 2;
            c.gridy = 0;
            c.insets = new Insets(0, 4, 0, 4);
            step3Panel.add(btnDir, c);
            JPanel group = new JPanel();
            group.setLayout(new GridBagLayout());
            group.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            if (nimbusFound) group.setBackground(new Color(232, 232, 255, 0));
            JLabel jl = new JLabel(UMCLanguage.getText("gui.wizard.step3.text7", MainFrame.selectedLanguage));
            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(0, 4, 0, 4);
            group.add(jl, c);
            labelNTMPath = new JLabel();
            shareCombo = new JComboBox();
            shareCombo.setEditable(true);
            shareCombo.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String s = "file:///opt/sybhttpd/localhost.drives/NETWORK_SHARE/";
                        s += ((JComboBox) e.getSource()).getSelectedItem().toString();
                        if (shareComboAdditionalPath != null) {
                            s += "/" + shareComboAdditionalPath.getText();
                        }
                        labelNTMPath.setText(s);
                    }
                }
            });
            JLabel jlNote = null;
            if (UPNP.getInstance().getNetworkShares() != null && UPNP.getInstance().getNetworkShares().getSize() > 0) {
                shareCombo.setModel(UPNP.getInstance().getNetworkShares());
                String s = shareComboAdditionalPath != null ? shareComboAdditionalPath.getText() : "";
                shareComboAdditionalPath = new JTextField();
                shareComboAdditionalPath.setColumns(25);
                shareComboAdditionalPath.setText(s);
                shareComboAdditionalPath.getDocument().addDocumentListener(new DocumentListener() {

                    public void changedUpdate(DocumentEvent event) {
                    }

                    public void insertUpdate(DocumentEvent event) {
                        labelNTMPath.setText("file:///opt/sybhttpd/localhost.drives/NETWORK_SHARE/" + shareCombo.getSelectedItem().toString() + "/" + shareComboAdditionalPath.getText());
                    }

                    public void removeUpdate(DocumentEvent event) {
                    }
                });
            } else {
                shareCombo.addItem("file:///opt/sybhttpd/localhost.drives/[...]");
                shareCombo.setBackground(Color.red);
                jlNote = new JLabel("(NMT device not found. Please provide the full nmt path to the provided pc dir.)");
                jlNote.setForeground(Color.red);
            }
            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.NORTHWEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            c.gridy = 0;
            c.weightx = 1.0;
            c.gridwidth = 2;
            c.insets = new Insets(0, 4, 0, 4);
            group.add(shareCombo, c);
            if (jlNote != null) {
                c = new GridBagConstraints();
                c.anchor = GridBagConstraints.CENTER;
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 2;
                c.gridy = 0;
                c.insets = new Insets(0, 4, 0, 4);
                group.add(jlNote, c);
            } else {
                c = new GridBagConstraints();
                c.anchor = GridBagConstraints.CENTER;
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 0;
                c.gridy = 2;
                c.insets = new Insets(0, 4, 0, 4);
                group.add(new JLabel("restlicher Pfad"), c);
                c = new GridBagConstraints();
                c.anchor = GridBagConstraints.CENTER;
                c.gridx = 1;
                c.gridy = 2;
                c.insets = new Insets(0, 4, 0, 4);
                group.add(new JLabel("/"), c);
                c = new GridBagConstraints();
                c.anchor = GridBagConstraints.NORTHWEST;
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 2;
                c.gridy = 2;
                c.weightx = 1.0;
                c.insets = new Insets(0, 4, 0, 4);
                group.add(shareComboAdditionalPath, c);
                labelNTMPath.setEnabled(false);
                labelNTMPath.setText("file:///opt/sybhttpd/localhost.drives/NETWORK_SHARE/");
                c = new GridBagConstraints();
                c.anchor = GridBagConstraints.NORTHWEST;
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 0;
                c.gridy = 3;
                c.gridwidth = 5;
                c.insets = new Insets(0, 4, 0, 4);
                group.add(labelNTMPath, c);
            }
            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.NORTHWEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = 5;
            c.weightx = 1.0;
            c.weighty = 1.0;
            c.insets = new Insets(0, 4, 0, 4);
            step3Panel.add(group, c);
        } catch (Exception exc) {
            log.error("Error in Wizard - Step 3", exc);
        }
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        result.add(step3Panel, c);
        return result;
    }

    private JPanel getStep4() {
        step4Panel = new JPanel();
        if (nimbusFound) step4Panel.setBackground(new Color(232, 232, 255, 0));
        JPanel result = new JPanel();
        result.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.0;
        c.weightx = 0.0;
        c.insets = new Insets(0, 8, 0, 4);
        result.add(getHelpPanel(4), c);
        try {
            step4Panel.setLayout(new GridBagLayout());
            step4Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), UMCLanguage.getText("gui.wizard.step6.title", MainFrame.selectedLanguage), TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", Font.BOLD, 12)));
            JLabel jlbl = new JLabel(UMCLanguage.getText("gui.wizard.step6.text2", MainFrame.selectedLanguage));
            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.NORTHWEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            c.weighty = 1.0;
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 1;
            c.insets = new Insets(0, 4, 0, 4);
            step4Panel.add(jlbl, c);
            skinCombo = new JComboBox();
            int i = -1;
            int selectedSkin = -1;
            File[] skindDirs = new File(System.getProperty("user.dir") + UMCConstants.fileSeparator + "resources" + UMCConstants.fileSeparator + "Skins").listFiles();
            for (File skinDir : skindDirs) {
                if (skinDir.isDirectory()) {
                    File skinXML = new File(skinDir.getAbsolutePath() + UMCConstants.fileSeparator + "skin.xml");
                    if (skinXML.exists()) {
                        try {
                            ArrayList<String> validationErrors = new ArrayList<String>();
                            XmlOptions validationOptions = new XmlOptions();
                            validationOptions.setErrorListener(validationErrors);
                            SkinDocument sd = SkinDocument.Factory.parse(skinXML);
                            boolean isValid = sd.validate(validationOptions);
                            if (!isValid) {
                                StringBuffer sb = new StringBuffer();
                                Iterator<String> iter = validationErrors.iterator();
                                String line = "";
                                while (iter.hasNext()) {
                                    line = ">> " + iter.next();
                                    log.error(line);
                                    sb.append(line + "\n");
                                }
                                JOptionPane.showMessageDialog(null, "The skin configuration " + skinDir.getName() + " is not valid. Please contact the skin authors!\n\n" + sb.toString(), "Invalid skin configuration", JOptionPane.ERROR_MESSAGE);
                            } else {
                                i++;
                                if (MainFrame.wizardConfig.getMediacenter().getSkin().equals(sd.getSkin().getName())) selectedSkin = i;
                                skinCombo.addItem(sd.getSkin().getName());
                            }
                        } catch (Exception exc) {
                            log.error("skin.xml could not be parsed", exc);
                            JOptionPane.showMessageDialog(null, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
            skinCombo.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    String skinName = (String) e.getItem();
                    String skinPath = System.getProperty("user.dir") + UMCConstants.fileSeparator + "resources" + UMCConstants.fileSeparator + "Skins" + UMCConstants.fileSeparator + skinName + UMCConstants.fileSeparator;
                    File skinXML = new File(skinPath + "skin.xml");
                    MainFrame.wizardConfig.getMediacenter().setSkin(skinName);
                }
            });
            skinCombo.setSelectedIndex(selectedSkin != -1 ? selectedSkin : 0);
            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.NORTHWEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            c.weighty = 1.0;
            c.gridx = 1;
            c.gridy = 0;
            c.gridwidth = 1;
            c.insets = new Insets(0, 4, 0, 4);
            step4Panel.add(skinCombo, c);
        } catch (Exception exc) {
            log.error("Error in Wizard - Step 4", exc);
        }
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        result.add(step4Panel, c);
        return result;
    }

    private void getSteps(boolean locked) {
        stepsGroup = new JPanel();
        stepsGroup.setLayout(new GridBagLayout());
        lockableUIStep = new LockableUI();
        l = new JXLayer<JComponent>(stepsGroup, lockableUIStep);
        blurEffect = new BufferedImageOpEffect(createBlurEffect());
        lockableUIStep.setLockedEffects(blurEffect);
        lockableUIStep.setLocked(locked);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0;
        c.insets = new Insets(20, 4, 20, 4);
        stepsGroup.add(getStep1(), c);
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1.0;
        c.insets = new Insets(20, 4, 20, 4);
        stepsGroup.add(getStep2(), c);
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1.0;
        c.insets = new Insets(20, 4, 20, 4);
        stepsGroup.add(getStep3(), c);
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 1.0;
        c.insets = new Insets(20, 4, 20, 4);
        stepsGroup.add(getStep4(), c);
    }

    /**
	 * Aktiviert bzw. deaktiviert alle Elemente eines Panels
	 * 
	 * @param panel Nummer des Panels (Step 1 = 1, Step = 2, Step 3 = 3, .....)
	 * @param b true/false
	 */
    private void enableAllPanelsComponents(int panel, boolean b) {
        switch(panel) {
            case 1:
                for (Component component : step1Panel.getComponents()) {
                    component.setEnabled(b);
                }
                break;
            case 2:
                for (Component component : step2Panel.getComponents()) component.setEnabled(b);
                break;
            case 3:
                for (Component component : step3Panel.getComponents()) component.setEnabled(b);
                break;
            case 4:
                for (Component component : step4Panel.getComponents()) component.setEnabled(b);
                break;
        }
    }

    private JPanel getHelpPanel(int step) {
        JPanel help = new JPanel();
        help.setName(step + "");
        help.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.gray));
        help.setBackground(Color.lightGray);
        MouseListener ml = new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                Cursor mouseCursor = new Cursor(Cursor.HAND_CURSOR);
                setCursor(mouseCursor);
            }

            public void mouseExited(MouseEvent e) {
                Cursor mouseCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                setCursor(mouseCursor);
            }

            public void mouseClicked(MouseEvent e) {
                switch(Integer.parseInt(((JPanel) e.getSource()).getName())) {
                    case 1:
                        leftCol.setText("<u><b>Step 1</b></u><br/><br/>" + UMCLanguage.getText("gui.wizard.step2.text1", MainFrame.selectedLanguage));
                        break;
                    case 2:
                        leftCol.setText("<u><b>Step 2</b></u><br/><br/>" + UMCLanguage.getText("gui.wizard.step7.text1", MainFrame.selectedLanguage));
                        break;
                    case 3:
                        StringBuffer sb = new StringBuffer();
                        sb.append("<u><b>Step 3</b></u><br><br>");
                        sb.append("<span style='word-break:break-all;word-wrap:break-word'><font color='red'>" + UMCLanguage.getText("gui.wizard.step3.text8", MainFrame.selectedLanguage) + "</font></span>");
                        sb.append("<br><hr><br>");
                        sb.append("<span style='word-break:break-all;word-wrap:break-word'>" + UMCLanguage.getText("gui.wizard.step3.text1", MainFrame.selectedLanguage) + "</span>");
                        sb.append("<br><br>");
                        sb.append("<u>NMT path examples</u><br>");
                        sb.append("<b>HDD</b><br>");
                        sb.append("<font size='2'>file:///opt/sybhttpd/localhost.drives/HARD_DISK/Videos</font><br><br>");
                        sb.append("<b>Samba</b><br>");
                        sb.append("<font size='2'>- file:///opt/sybhttpd/localhost.drives/NETWORK_SHARE/192.168.0.1:<br> Media/Videos</font><br>");
                        sb.append("<font size='2'>- file:///opt/sybhttpd/localhost.drives/NETWORK_SHARE/<font color='red'>Share name on<br> the nmt</font>/Videos</font><br><br>");
                        sb.append("<b>NFS</b><br>");
                        sb.append("<font size='2'>- file:///opt/sybhttpd/localhost.drives/NETWORK_SHARE/192.168.0.1::<br> Media/Videos</font><br>");
                        sb.append("<font size='2'>- file:///opt/sybhttpd/localhost.drives/NETWORK_SHARE/<font color='red'>Share name on<br> the nmt</font>/Videos</font>>");
                        leftCol.setText(sb.toString());
                        break;
                    case 4:
                        leftCol.setText("<u><b>Step 4</b></u><br/><br/>" + UMCLanguage.getText("gui.wizard.step6.text1", MainFrame.selectedLanguage));
                        break;
                }
            }
        };
        help.addMouseListener(ml);
        JLabel jlbl = new JLabel();
        jlbl.setFont(new java.awt.Font("Helvetica", Font.BOLD, 10));
        jlbl.setText("Help / Information");
        help.add(jlbl);
        return help;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        WizardFrame2 wf2 = new WizardFrame2(null);
    }

    private BufferedImageOp createBlurEffect() {
        return new GaussianFilter(blurRadius);
    }
}
