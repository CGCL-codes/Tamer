package org.moogiesoft.buddi.plugins.skinchooser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.homeunix.thecave.buddi.model.prefs.PrefsModel;
import org.homeunix.thecave.buddi.plugin.api.BuddiPreferencePlugin;
import org.homeunix.thecave.buddi.plugin.api.exception.PluginException;
import ca.digitalcave.moss.common.Version;
import com.sun.org.apache.bcel.internal.util.ClassLoader;

/**
 * SkinChooserPreferencePane represents the pane on the Buddi Peferences Panel 
 * 
 * @author nklaebe
 *
 */
public class SkinChooserPreferencePane extends BuddiPreferencePlugin implements ActionListener, ListSelectionListener {

    private JTextField selectedLNFTextField;

    private JList lnfList;

    private JButton addButton;

    private JButton removeButton;

    private JPanel panel;

    /** index of the currently selected item in the list of LnFs */
    private int selectedIndex;

    /** currently selected LnF name */
    private String selectedLNF;

    /** currently selected LnF class name */
    private String lnfClass;

    /** currently selected LnF JAR path */
    private String lnfJAR;

    /**
	 * Constructor
	 *
	 */
    public SkinChooserPreferencePane() {
        lnfList = new JList();
        lnfList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lnfList.addListSelectionListener(this);
        addButton = new JButton("Add");
        addButton.addActionListener(this);
        removeButton = new JButton("Remove");
        removeButton.addActionListener(this);
        selectedLNFTextField = new JTextField(20);
        selectedLNFTextField.setEnabled(false);
        selectedIndex = -1;
    }

    public boolean save() throws PluginException {
        boolean ret = PrefsModel.getInstance().getPluginPreference(Keys.SELECTED_LNF) == null || !PrefsModel.getInstance().getPluginPreference(Keys.SELECTED_LNF).equals(selectedLNFTextField.getText());
        PrefsModel.getInstance().putPluginPreference(Keys.SELECTED_LNF, selectedLNFTextField.getText());
        return ret;
    }

    public void load() throws PluginException {
        String selectedLNF = getPreference(Keys.SELECTED_LNF);
        selectedLNFTextField.setText(selectedLNF);
        if (selectedLNF == null) selectedLNF = "NONE"; else if (!selectedLNF.equals("NONE")) {
            update();
        }
        List<String> knownLNF = getListPreference(Keys.LIST_OF_LNF);
        if (knownLNF == null) {
            knownLNF = new ArrayList<String>();
        }
        Collections.sort(knownLNF);
        DefaultListModel model = new DefaultListModel();
        for (String s : knownLNF) {
            if (s != null) model.addElement(s); else System.out.println("SkinChooserPreferencePane.load(): ArrayList containted a NULL reference");
        }
        model.addElement("NONE");
        lnfList.setModel(model);
    }

    public JPanel getPreferencesPanel() {
        panel = new JPanel(new BorderLayout());
        JPanel centrePanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 2));
        topPanel.add(new JLabel("Active LnF:"));
        topPanel.add(selectedLNFTextField);
        bottomPanel.add(addButton);
        bottomPanel.add(removeButton);
        JScrollPane listPanel = new JScrollPane(lnfList);
        listPanel.setPreferredSize(new Dimension(300, 300));
        listPanel.setBorder(BorderFactory.createEtchedBorder());
        centrePanel.add(new JLabel("Please Select LnF"), BorderLayout.NORTH);
        centrePanel.add(listPanel, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centrePanel);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    public String getName() {
        return "Skin Chooser";
    }

    public Version getMinimumVersion() {
        return null;
    }

    public Version getMaximumVersion() {
        return null;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == addButton) {
                File f = FileDialogLauncher.getFile();
                if (f != null) {
                    SkinChangerPlugin.addURL(f.toURI().toURL());
                    JarFile jar = new JarFile(f);
                    Enumeration<JarEntry> enumeration = jar.entries();
                    boolean valid = false;
                    while (enumeration.hasMoreElements()) {
                        String str = enumeration.nextElement().getName();
                        if (str.toLowerCase().endsWith(".class")) {
                            str = str.replaceAll("/", "\\.");
                            str = str.substring(0, str.length() - 6);
                            Class<?> c = null;
                            try {
                                c = ClassLoader.getSystemClassLoader().loadClass(str);
                                Class<?> superClass = c.getSuperclass();
                                while (superClass != null && !superClass.getName().equals("javax.swing.LookAndFeel")) {
                                    superClass = superClass.getSuperclass();
                                }
                                if (superClass != null) {
                                    c.newInstance();
                                    String lnfName = f.getName().substring(0, f.getName().length() - 4) + "_" + c.getSimpleName();
                                    if (lnfName != null) {
                                        if (getPreference(Keys.LNF_CLASS + lnfName) == null) {
                                            PrefsModel.getInstance().putPluginPreference(Keys.LNF_CLASS + lnfName, c.getName());
                                            PrefsModel.getInstance().putPluginPreference(Keys.LNF_JAR + lnfName, f.getAbsolutePath());
                                            List<String> knownLNF = getListPreference(Keys.LIST_OF_LNF);
                                            if (knownLNF == null) {
                                                knownLNF = new ArrayList<String>();
                                            }
                                            knownLNF.add(lnfName);
                                            PrefsModel.getInstance().putPluginListPreference(Keys.LIST_OF_LNF, knownLNF);
                                            ((DefaultListModel) lnfList.getModel()).addElement(lnfName);
                                            valid = true;
                                        } else {
                                            JOptionPane.showMessageDialog(null, lnfName + " Already exists in list.", "Skin Chooser Plugin ERROR", JOptionPane.ERROR_MESSAGE);
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Error with JAR. Look and Feel name equals null", "Skin Chooser Plugin ERROR", JOptionPane.ERROR_MESSAGE);
                                        valid = true;
                                    }
                                }
                            } catch (InstantiationException ee) {
                            } catch (NoClassDefFoundError ncde) {
                            }
                        }
                    }
                    if (!valid) JOptionPane.showMessageDialog(null, "Not a valid Look and Feel JAR", "Skin Chooser Plugin ERROR", JOptionPane.ERROR_MESSAGE); else {
                        JOptionPane.showMessageDialog(null, f.getName() + " successfully added.", "Skin Chooser Plugin Infomation", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else {
                if (selectedIndex != -1) {
                    List<String> knownLNF = getListPreference(Keys.LIST_OF_LNF);
                    if (knownLNF == null) {
                        knownLNF = new ArrayList<String>();
                    }
                    String selectedLNFString = (String) lnfList.getModel().getElementAt(selectedIndex);
                    if (selectedLNFString.equals("NONE")) {
                        JOptionPane.showMessageDialog(null, "You are not allowed to remove this option.", "Skin Chooser Plugin ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        String LnFToDelete = null;
                        for (String s : knownLNF) {
                            if (s != null && s.equals(selectedLNFString)) {
                                LnFToDelete = s;
                                break;
                            }
                        }
                        if (LnFToDelete != null) knownLNF.remove(LnFToDelete);
                        PrefsModel.getInstance().putPluginPreference(Keys.LNF_CLASS + selectedLNFString, null);
                        PrefsModel.getInstance().putPluginPreference(Keys.LNF_JAR + selectedLNFString, null);
                        PrefsModel.getInstance().putPluginListPreference(Keys.LIST_OF_LNF, knownLNF);
                        ((DefaultListModel) lnfList.getModel()).remove(selectedIndex);
                        selectedIndex = -1;
                        lnfList.setSelectedValue(lnfList.getSelectedIndex(), false);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "A Look and Feel Must Be Selected to be able to remove.", "Skin Chooser Plugin ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error setting look and feel: " + ex);
            String str = "Error setting look and feel: " + ex + "\n";
            for (int i = 0; i < ex.getStackTrace().length && i < 25; i++) {
                str = str + ex.getStackTrace()[i].toString() + "\n";
            }
            JOptionPane.showMessageDialog(null, str, "Skin Chooser Plugin ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        JList src = (JList) e.getSource();
        if (selectedIndex != src.getSelectedIndex()) {
            selectedIndex = src.getSelectedIndex();
            if (selectedIndex != -1) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        selectedLNFTextField.setText((String) lnfList.getModel().getElementAt(selectedIndex));
                        update();
                        try {
                            SkinChangerPlugin.changeLnF(lnfJAR, lnfClass);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            System.out.println("Error setting look and feel: " + ex);
                            String str = "Error setting look and feel: " + ex + "\n";
                            for (int i = 0; i < ex.getStackTrace().length && i < 25; i++) {
                                str = str + ex.getStackTrace()[i].toString() + "\n";
                            }
                            Logger.getLogger(this.getClass().getName()).warning(str);
                            JOptionPane.showMessageDialog(null, str, "Skin Chooser Plugin ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
            }
        }
    }

    /**
	 * updates the class name and JAR path to correspond with the current selected Look and Feel.
	 *
	 */
    private void update() {
        selectedLNF = selectedLNFTextField.getText();
        if (selectedLNF == null) selectedLNF = "NONE"; else if (!selectedLNF.equals("NONE")) {
            lnfClass = getPreference(Keys.LNF_CLASS + selectedLNF);
            lnfJAR = getPreference(Keys.LNF_JAR + selectedLNF);
            if (lnfClass == null || lnfJAR == null) {
                JOptionPane.showMessageDialog(null, "There is a problem with " + selectedLNF + " Look and Feel.\nAssociated Information cannot be found.\nPlease remove and re-add the look and feel", "Skin Chooser Plugin ERROR", JOptionPane.ERROR_MESSAGE);
                selectedLNF = "NONE";
            } else {
                File file = new File(lnfJAR);
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(null, "There is a problem with " + selectedLNF + " Look and Feel.\nThe Associated Information JAR file cannot be found.\nPlease remove and re-add the look and feel", "Skin Chooser Plugin ERROR", JOptionPane.ERROR_MESSAGE);
                    selectedLNF = "NONE";
                } else return;
            }
        }
        lnfClass = null;
        lnfJAR = null;
        selectedLNFTextField.setText(selectedLNF);
    }
}
