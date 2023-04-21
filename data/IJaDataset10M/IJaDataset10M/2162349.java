package icm.unicore.plugins.dbbrowser.pdb;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.pallas.unicore.client.Client;
import icm.unicore.plugins.dbbrowser.*;
import icm.unicore.plugins.dbbrowser.util.*;
import icm.unicore.plugins.dbbrowser.visualization.*;

/**
 *@author     Michal Wronski
 *@created    17. March 2002
 */
public class PDBSettingsFrame extends JFrame {

    static final int VISUALIZATION_JMOL = 0;

    static final int VISUALIZATION_JMV = 1;

    static final int VISUALIZATION_RASMOL = 2;

    static final int VISUALIZATION_OTHER = 3;

    static final int VISUALIZATION_OTHER_APPLET = 0;

    static final int VISUALIZATION_OTHER_STANDALONE = 1;

    private JFrame pluginFrame;

    private Client client;

    private PluginDefaults pluginDefaults;

    private ButtonListener buttonListener;

    private DefaultListModel mirrorsListModel;

    public PDBSettingsFrame(Client client, PluginDefaults pluginDefaults) {
        super("PDB Settings");
        this.client = client;
        this.pluginDefaults = pluginDefaults;
        pluginFrame = this;
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                dispose();
                return;
            }
        });
        buttonListener = new ButtonListener();
        mirrorsListModel = new DefaultListModel();
        initComponents();
    }

    public void show() {
        int xpos = (int) (client.getLocation().getX() + client.getWidth() / 2 - getPreferredSize().getWidth() / 2);
        int ypos = (int) (client.getLocation().getY() + client.getHeight() / 2 - getPreferredSize().getHeight() / 2);
        setLocation(xpos, ypos);
        pluginDefaults.loadFromFile();
        resetAllValues();
        configPluginTabbedPane.setSelectedIndex(0);
        super.show();
        if (configUseJMVRadioButton.isSelected()) configVisualizationTabbedPane.setSelectedIndex(VISUALIZATION_JMV); else if (configUseRasmolRadioButton.isSelected()) configVisualizationTabbedPane.setSelectedIndex(VISUALIZATION_RASMOL); else if (configUseOtherRadioButton.isSelected()) configVisualizationTabbedPane.setSelectedIndex(VISUALIZATION_OTHER); else configVisualizationTabbedPane.setSelectedIndex(VISUALIZATION_JMOL);
        configOtherTabbedPane.setSelectedIndex(configOtherStandaloneRadioButton.isSelected() ? VISUALIZATION_OTHER_STANDALONE : VISUALIZATION_OTHER_APPLET);
    }

    private void resetAllValues() {
        configPDBIdCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_PDBID_KEY)) == 0);
        configPDBCitAuthorCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_CITAUTHOR_KEY)) == 0);
        configPDBChainTypeCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_CHAINTYPE_KEY)) == 0);
        configPDBHeaderCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_PDBHEADER_KEY)) == 0);
        configPDBExperimentalCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_EXPERIMENTAL_KEY)) == 0);
        configPDBTextCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_TEXT_KEY)) == 0);
        configPDBDateCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_DATE_KEY)) == 0);
        configPDBCitationCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_CITATION_KEY)) == 0);
        configPDBCompInfCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_COMPINF_KEY)) == 0);
        configPDBTitleCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_TITLE_KEY)) == 0);
        configPDBECCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_EC_KEY)) == 0);
        configPDBLigandsCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_LIGANDS_KEY)) == 0);
        configPDBSourceCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_SOURCE_KEY)) == 0);
        configPDBExperDataCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_EXPERDATA_KEY)) == 0);
        configPDBChainLenCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_CHAINLEN_KEY)) == 0);
        configPDBShortSeqCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_SHORTSEQ_KEY)) == 0);
        configPDBSecStructCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_SECSTRUCT_KEY)) == 0);
        configPDBFASTACheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_FASTA_KEY)) == 0);
        configPDBResolutionCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_RESOLUTION_KEY)) == 0);
        configPDBUnitCellCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_UNITCELL_KEY)) == 0);
        configPDBSpaceGroupCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_SPACEGROUP_KEY)) == 0);
        configPDBRefinementCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_REFINEMENT_KEY)) == 0);
        configPDBSelOptsCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_SELOPTS_KEY)) == 0);
        mirrorsListModel.removeAllElements();
        if (pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR0_KEY).length() > 0) mirrorsListModel.addElement(pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR0_KEY));
        if (pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR1_KEY).length() > 0) mirrorsListModel.addElement(pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR1_KEY));
        if (pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR2_KEY).length() > 0) mirrorsListModel.addElement(pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR2_KEY));
        if (pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR3_KEY).length() > 0) mirrorsListModel.addElement(pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR3_KEY));
        if (pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR4_KEY).length() > 0) mirrorsListModel.addElement(pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR4_KEY));
        if (pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR5_KEY).length() > 0) mirrorsListModel.addElement(pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR5_KEY));
        if (pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR6_KEY).length() > 0) mirrorsListModel.addElement(pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR6_KEY));
        if (pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR7_KEY).length() > 0) mirrorsListModel.addElement(pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR7_KEY));
        if (pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR8_KEY).length() > 0) mirrorsListModel.addElement(pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR8_KEY));
        if (pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR9_KEY).length() > 0) mirrorsListModel.addElement(pluginDefaults.getProperty(pluginDefaults.PDB_MIRROR9_KEY));
        configServerQoSCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(pluginDefaults.PDB_QOS_KEY)) == 0);
        configNoVisRadioButton.setSelected(true);
        configUseJmolRadioButton.setSelected("true".compareTo(pluginDefaults.getProperty(JmolApplet.USEJMOL_KEY)) == 0);
        configJmolPathTextField.setText(pluginDefaults.getProperty(JmolApplet.JMOL_PATH_KEY));
        configJmolStyleComboBox.setSelectedIndex(Integer.parseInt(pluginDefaults.getProperty(JmolApplet.JMOL_STYLE_KEY)));
        configJmolBondsComboBox.setSelectedIndex(Integer.parseInt(pluginDefaults.getProperty(JmolApplet.JMOL_BONDS_KEY)));
        configJmolZoomTextField.setText(pluginDefaults.getProperty(JmolApplet.JMOL_ZOOM_KEY));
        configJmolAtomTextField.setText(pluginDefaults.getProperty(JmolApplet.JMOL_ATOM_KEY));
        configJmolCustomTextField.setText(pluginDefaults.getProperty(JmolApplet.JMOL_CUSTOM_KEY));
        configJmolBgColButton.setBackground(new java.awt.Color(Integer.parseInt(pluginDefaults.getProperty(JmolApplet.JMOL_BGCOLOR_KEY))));
        configJmolFgColButton.setBackground(new java.awt.Color(Integer.parseInt(pluginDefaults.getProperty(JmolApplet.JMOL_FGCOLOR_KEY))));
        configJmolWireframeCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(JmolApplet.JMOL_WIREFRAME_KEY)) == 0);
        configUseJMVRadioButton.setSelected("true".compareTo(pluginDefaults.getProperty(JmvApplet.USEJMV_KEY)) == 0);
        configJMVPathTextField.setText(pluginDefaults.getProperty(JmvApplet.JMV_PATH_KEY));
        configJMVRepComboBox.setSelectedIndex(Integer.parseInt(pluginDefaults.getProperty(JmvApplet.JMV_REP_KEY)));
        configJMVGradientComboBox.setSelectedIndex(Integer.parseInt(pluginDefaults.getProperty(JmvApplet.JMV_GRADIENT_KEY)));
        configJMVWidthTextField.setText(pluginDefaults.getProperty(JmvApplet.JMV_WIDTH_KEY));
        configJMVHeightTextField.setText(pluginDefaults.getProperty(JmvApplet.JMV_HEIGHT_KEY));
        configJMVControlsCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(JmvApplet.JMV_CONTROLS_KEY)) == 0);
        configJMVColorComboBox.setSelectedIndex(Integer.parseInt(pluginDefaults.getProperty(JmvApplet.JMV_COLOR_KEY)));
        configUseRasmolRadioButton.setSelected("true".compareTo(pluginDefaults.getProperty(Rasmol.USERASMOL_KEY)) == 0);
        configRasmolPathTextField.setText(pluginDefaults.getProperty(Rasmol.RASMOL_PATH_KEY));
        configRasmolScriptTextField.setText(pluginDefaults.getProperty(Rasmol.RASMOL_SCRIPT_KEY));
        configRasmolDisplayCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(Rasmol.RASMOL_NODISPLAY_KEY)) == 0);
        configRasmolGzippedCheckBox.setSelected("true".compareTo(pluginDefaults.getProperty(Rasmol.RASMOL_GZIPPED_KEY)) == 0);
        configUseOtherRadioButton.setSelected(("true".compareTo(pluginDefaults.getProperty(PluginDefaults.PDB_USE_GENAPPLET)) == 0) || ("true".compareTo(pluginDefaults.getProperty(PluginDefaults.PDB_USE_GENSTANDALONE)) == 0));
        configOtherAppletRadioButton.setSelected(true);
        configOtherStandaloneRadioButton.setSelected("true".compareTo(pluginDefaults.getProperty(PluginDefaults.PDB_USE_GENSTANDALONE)) == 0);
        otherAppletPathTextField.setText(pluginDefaults.getProperty(PluginDefaults.PDB_GENAPPLET_PATH));
        otherAppletClassTextField.setText(pluginDefaults.getProperty(PluginDefaults.PDB_GENAPPLET_CLASSNAME));
        otherAppletParmsTextField.setText(pluginDefaults.getProperty(PluginDefaults.PDB_GENAPPLET_PARMS));
        configOtherStandalonePathTextField.setText(pluginDefaults.getProperty(PluginDefaults.PDB_GENSTANDALONE_PATH));
        configOtherStandaloneParmsTextField.setText(pluginDefaults.getProperty(PluginDefaults.PDB_GENSTANDALONE_PARMS));
        setEnabledJmolButtons("true".compareTo(pluginDefaults.getProperty(JmolApplet.USEJMOL_KEY)) == 0);
        setEnabledJmvButtons("true".compareTo(pluginDefaults.getProperty(JmvApplet.USEJMV_KEY)) == 0);
        setEnabledRasmolButtons("true".compareTo(pluginDefaults.getProperty(Rasmol.USERASMOL_KEY)) == 0);
        setEnabledOtherButtons("true".compareTo(pluginDefaults.getProperty(PluginDefaults.PDB_USE_GENAPPLET)) == 0, "true".compareTo(pluginDefaults.getProperty(PluginDefaults.PDB_USE_GENSTANDALONE)) == 0);
    }

    private int setNewDefaults() {
        String val[] = { "true", "false" };
        if (!(configPDBIdCheckBox.isSelected() || configPDBCitAuthorCheckBox.isSelected() || configPDBChainTypeCheckBox.isSelected() || configPDBHeaderCheckBox.isSelected() || configPDBExperimentalCheckBox.isSelected() || configPDBTextCheckBox.isSelected() || configPDBDateCheckBox.isSelected() || configPDBCitationCheckBox.isSelected() || configPDBCompInfCheckBox.isSelected() || configPDBTitleCheckBox.isSelected() || configPDBECCheckBox.isSelected() || configPDBLigandsCheckBox.isSelected() || configPDBSourceCheckBox.isSelected() || configPDBExperDataCheckBox.isSelected() || configPDBChainLenCheckBox.isSelected() || configPDBShortSeqCheckBox.isSelected() || configPDBSecStructCheckBox.isSelected() || configPDBFASTACheckBox.isSelected() || configPDBResolutionCheckBox.isSelected() || configPDBUnitCellCheckBox.isSelected() || configPDBSpaceGroupCheckBox.isSelected() || configPDBRefinementCheckBox.isSelected())) {
            JOptionPane.showMessageDialog(pluginFrame, "At least one item should be selected!", "Error", JOptionPane.ERROR_MESSAGE);
            return 1;
        } else if (mirrorsListModel.size() == 0) {
            JOptionPane.showMessageDialog(pluginFrame, "At least one PDB mirror should be added!", "Error", JOptionPane.ERROR_MESSAGE);
            return 1;
        } else {
            pluginDefaults.setProperty(pluginDefaults.PDB_PDBID_KEY, val[configPDBIdCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_CITAUTHOR_KEY, val[configPDBCitAuthorCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_CHAINTYPE_KEY, val[configPDBChainTypeCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_PDBHEADER_KEY, val[configPDBHeaderCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_EXPERIMENTAL_KEY, val[configPDBExperimentalCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_TEXT_KEY, val[configPDBTextCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_DATE_KEY, val[configPDBDateCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_CITATION_KEY, val[configPDBCitationCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_COMPINF_KEY, val[configPDBCompInfCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_TITLE_KEY, val[configPDBTitleCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_EC_KEY, val[configPDBECCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_LIGANDS_KEY, val[configPDBLigandsCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_SOURCE_KEY, val[configPDBSourceCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_EXPERDATA_KEY, val[configPDBExperDataCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_CHAINLEN_KEY, val[configPDBChainLenCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_SHORTSEQ_KEY, val[configPDBShortSeqCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_SECSTRUCT_KEY, val[configPDBSecStructCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_FASTA_KEY, val[configPDBFASTACheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_RESOLUTION_KEY, val[configPDBResolutionCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_UNITCELL_KEY, val[configPDBUnitCellCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_SPACEGROUP_KEY, val[configPDBSpaceGroupCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_REFINEMENT_KEY, val[configPDBRefinementCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(pluginDefaults.PDB_SELOPTS_KEY, val[configPDBSelOptsCheckBox.isSelected() ? 0 : 1]);
            int s = mirrorsListModel.size();
            pluginDefaults.setProperty(pluginDefaults.PDB_MIRROR0_KEY, (s > 0) ? (String) mirrorsListModel.elementAt(0) : "");
            pluginDefaults.setProperty(pluginDefaults.PDB_MIRROR1_KEY, (s > 1) ? (String) mirrorsListModel.elementAt(1) : "");
            pluginDefaults.setProperty(pluginDefaults.PDB_MIRROR2_KEY, (s > 2) ? (String) mirrorsListModel.elementAt(2) : "");
            pluginDefaults.setProperty(pluginDefaults.PDB_MIRROR3_KEY, (s > 3) ? (String) mirrorsListModel.elementAt(3) : "");
            pluginDefaults.setProperty(pluginDefaults.PDB_MIRROR4_KEY, (s > 4) ? (String) mirrorsListModel.elementAt(4) : "");
            pluginDefaults.setProperty(pluginDefaults.PDB_MIRROR5_KEY, (s > 5) ? (String) mirrorsListModel.elementAt(5) : "");
            pluginDefaults.setProperty(pluginDefaults.PDB_MIRROR6_KEY, (s > 6) ? (String) mirrorsListModel.elementAt(6) : "");
            pluginDefaults.setProperty(pluginDefaults.PDB_MIRROR7_KEY, (s > 7) ? (String) mirrorsListModel.elementAt(7) : "");
            pluginDefaults.setProperty(pluginDefaults.PDB_MIRROR8_KEY, (s > 8) ? (String) mirrorsListModel.elementAt(8) : "");
            pluginDefaults.setProperty(pluginDefaults.PDB_MIRROR9_KEY, (s > 9) ? (String) mirrorsListModel.elementAt(9) : "");
            pluginDefaults.setProperty(pluginDefaults.PDB_QOS_KEY, val[configServerQoSCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(JmolApplet.USEJMOL_KEY, val[configUseJmolRadioButton.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(JmolApplet.JMOL_PATH_KEY, configJmolPathTextField.getText());
            pluginDefaults.setProperty(JmolApplet.JMOL_STYLE_KEY, configJmolStyleComboBox.getSelectedIndex() + "");
            pluginDefaults.setProperty(JmolApplet.JMOL_BONDS_KEY, configJmolBondsComboBox.getSelectedIndex() + "");
            pluginDefaults.setProperty(JmolApplet.JMOL_ZOOM_KEY, configJmolZoomTextField.getText());
            pluginDefaults.setProperty(JmolApplet.JMOL_ATOM_KEY, configJmolAtomTextField.getText());
            pluginDefaults.setProperty(JmolApplet.JMOL_CUSTOM_KEY, configJmolCustomTextField.getText());
            pluginDefaults.setProperty(JmolApplet.JMOL_BGCOLOR_KEY, configJmolBgColButton.getBackground().getRGB() + "");
            pluginDefaults.setProperty(JmolApplet.JMOL_FGCOLOR_KEY, configJmolFgColButton.getBackground().getRGB() + "");
            pluginDefaults.setProperty(JmolApplet.JMOL_WIREFRAME_KEY, val[configJmolWireframeCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(JmvApplet.USEJMV_KEY, val[configUseJMVRadioButton.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(JmvApplet.JMV_PATH_KEY, configJMVPathTextField.getText());
            pluginDefaults.setProperty(JmvApplet.JMV_CONTROLS_KEY, val[configJMVControlsCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(JmvApplet.JMV_REP_KEY, configJMVRepComboBox.getSelectedIndex() + "");
            pluginDefaults.setProperty(JmvApplet.JMV_GRADIENT_KEY, configJMVGradientComboBox.getSelectedIndex() + "");
            pluginDefaults.setProperty(JmvApplet.JMV_COLOR_KEY, configJMVColorComboBox.getSelectedIndex() + "");
            pluginDefaults.setProperty(JmvApplet.JMV_WIDTH_KEY, configJMVWidthTextField.getText());
            pluginDefaults.setProperty(JmvApplet.JMV_HEIGHT_KEY, configJMVHeightTextField.getText());
            pluginDefaults.setProperty(Rasmol.USERASMOL_KEY, val[configUseRasmolRadioButton.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(Rasmol.RASMOL_PATH_KEY, configRasmolPathTextField.getText());
            pluginDefaults.setProperty(Rasmol.RASMOL_SCRIPT_KEY, configRasmolScriptTextField.getText());
            pluginDefaults.setProperty(Rasmol.RASMOL_NODISPLAY_KEY, val[configRasmolDisplayCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(Rasmol.RASMOL_GZIPPED_KEY, val[configRasmolGzippedCheckBox.isSelected() ? 0 : 1]);
            pluginDefaults.setProperty(PluginDefaults.PDB_USE_GENAPPLET, val[(configUseOtherRadioButton.isSelected() && configOtherAppletRadioButton.isSelected()) ? 0 : 1]);
            pluginDefaults.setProperty(PluginDefaults.PDB_USE_GENSTANDALONE, val[(configUseOtherRadioButton.isSelected() && configOtherStandaloneRadioButton.isSelected()) ? 0 : 1]);
            pluginDefaults.setProperty(PluginDefaults.PDB_GENAPPLET_PATH, otherAppletPathTextField.getText());
            pluginDefaults.setProperty(PluginDefaults.PDB_GENAPPLET_CLASSNAME, otherAppletClassTextField.getText());
            pluginDefaults.setProperty(PluginDefaults.PDB_GENAPPLET_PARMS, otherAppletParmsTextField.getText());
            pluginDefaults.setProperty(PluginDefaults.PDB_GENSTANDALONE_PATH, configOtherStandalonePathTextField.getText());
            pluginDefaults.setProperty(PluginDefaults.PDB_GENSTANDALONE_PARMS, configOtherStandaloneParmsTextField.getText());
        }
        return 0;
    }

    private void setEnabledJmolButtons(boolean state) {
        configJmolPathLabel.setEnabled(state);
        configJmolPathTextField.setEnabled(state);
        configJmolPathButton.setEnabled(state);
        configJmolStyleLabel.setEnabled(state);
        configJmolStyleComboBox.setEnabled(state);
        configJmolBondsLabel.setEnabled(state);
        configJmolBondsComboBox.setEnabled(state);
        configJmolZoomLabel.setEnabled(state);
        configJmolZoomTextField.setEnabled(state);
        configJmolAtomLabel.setEnabled(state);
        configJmolAtomTextField.setEnabled(state);
        configJmolCustomLabel.setEnabled(state);
        configJmolCustomTextField.setEnabled(state);
        configJmolBgColLabel.setEnabled(state);
        configJmolBgColButton.setEnabled(state);
        configJmolFgColLabel.setEnabled(state);
        configJmolFgColButton.setEnabled(state);
        configJmolWireframeLabel.setEnabled(state);
        configJmolWireframeCheckBox.setEnabled(state);
    }

    private void setEnabledJmvButtons(boolean state) {
        configJMVPathLabel.setEnabled(state);
        configJMVPathTextField.setEnabled(state);
        configJMVPathButton.setEnabled(state);
        configJMVRepLabel.setEnabled(state);
        configJMVRepComboBox.setEnabled(state);
        configJMVGradientLabel.setEnabled(state);
        configJMVGradientComboBox.setEnabled(state);
        configJMVWidthLabel.setEnabled(state);
        configJMVWidthTextField.setEnabled(state);
        configJMVHeightLabel.setEnabled(state);
        configJMVHeightTextField.setEnabled(state);
        configJMVControlsLabel.setEnabled(state);
        configJMVControlsCheckBox.setEnabled(state);
        configJMVColorLabel.setEnabled(state);
        configJMVColorComboBox.setEnabled(state);
    }

    private void setEnabledRasmolButtons(boolean state) {
        configRasmolPathLabel.setEnabled(state);
        configRasmolPathTextField.setEnabled(state);
        configRasmolPathButton.setEnabled(state);
        configRasmolDisplayLabel.setEnabled(state);
        configRasmolDisplayCheckBox.setEnabled(state);
        configRasmolScriptTextField.setEnabled(state);
        configRasmolScriptButton.setEnabled(state);
        configRasmolScriptLabel.setEnabled(state);
        configRasmolGzippedLabel.setEnabled(state);
        configRasmolDoesntworkLabel.setEnabled(state);
        configRasmolGzippedCheckBox.setEnabled(state);
    }

    private void setEnabledOtherButtons(boolean applet_state, boolean standalone_state) {
        configOtherAppletRadioButton.setEnabled(applet_state || standalone_state);
        configOtherStandaloneRadioButton.setEnabled(applet_state || standalone_state);
        otherAppletPathLabel.setEnabled(applet_state);
        otherAppletPathTextField.setEnabled(applet_state);
        otherAppletPathButton.setEnabled(applet_state);
        otherAppletClassLabel.setEnabled(applet_state);
        otherAppletParmsLabel.setEnabled(applet_state);
        otherAppletClassTextField.setEnabled(applet_state);
        otherAppletParmsTextField.setEnabled(applet_state);
        configOtherStandalonePathLabel.setEnabled(standalone_state);
        configOtherStandalonePathTextField.setEnabled(standalone_state);
        configOtherStandalonePathButton.setEnabled(standalone_state);
        configOtherStandaloneParmsLabel.setEnabled(standalone_state);
        configOtherStandaloneParmsTextField.setEnabled(standalone_state);
    }

    /**
	 *  Setup GUI components
	 */
    private void initComponents() {
        visualizationButtonGroup = new javax.swing.ButtonGroup();
        otherButtonGroup = new javax.swing.ButtonGroup();
        configPluginTabbedPane = new javax.swing.JTabbedPane();
        configPluginOkPanel = new javax.swing.JPanel();
        configPluginOkButton = new javax.swing.JButton();
        configPluginCancelButton = new javax.swing.JButton();
        configPDBScrollPane = new javax.swing.JScrollPane();
        configPDBMainPanel = new javax.swing.JPanel();
        configPDBGeneralPanel = new javax.swing.JPanel();
        configPDBGeneralLeftPanel = new javax.swing.JPanel();
        configPDBIdCheckBox = new javax.swing.JCheckBox();
        configPDBCitAuthorCheckBox = new javax.swing.JCheckBox();
        configPDBChainTypeCheckBox = new javax.swing.JCheckBox();
        configPDBHeaderCheckBox = new javax.swing.JCheckBox();
        configPDBExperimentalCheckBox = new javax.swing.JCheckBox();
        configPDBTextCheckBox = new javax.swing.JCheckBox();
        configPDBDateCheckBox = new javax.swing.JCheckBox();
        configPDBGeneralRightPanel = new javax.swing.JPanel();
        configPDBCitationCheckBox = new javax.swing.JCheckBox();
        configPDBCompInfCheckBox = new javax.swing.JCheckBox();
        configPDBTitleCheckBox = new javax.swing.JCheckBox();
        configPDBECCheckBox = new javax.swing.JCheckBox();
        configPDBLigandsCheckBox = new javax.swing.JCheckBox();
        configPDBSourceCheckBox = new javax.swing.JCheckBox();
        configPDBExperDataCheckBox = new javax.swing.JCheckBox();
        configPDBMiddlePanel = new javax.swing.JPanel();
        configPDBSequencePanel = new javax.swing.JPanel();
        configPDBChainLenCheckBox = new javax.swing.JCheckBox();
        configPDBShortSeqCheckBox = new javax.swing.JCheckBox();
        configPDBSecStructCheckBox = new javax.swing.JCheckBox();
        configPDBFASTACheckBox = new javax.swing.JCheckBox();
        configPDBExperimentalPanel = new javax.swing.JPanel();
        configPDBResolutionCheckBox = new javax.swing.JCheckBox();
        configPDBUnitCellCheckBox = new javax.swing.JCheckBox();
        configPDBSpaceGroupCheckBox = new javax.swing.JCheckBox();
        configPDBRefinementCheckBox = new javax.swing.JCheckBox();
        configPDBDisplayPanel = new javax.swing.JPanel();
        configPDBSelOptsCheckBox = new javax.swing.JCheckBox();
        configServerScrollPane = new javax.swing.JScrollPane();
        configServerMainPanel = new javax.swing.JPanel();
        configServerPanel = new javax.swing.JPanel();
        configServerLeftPanel = new javax.swing.JPanel();
        configServerURLPanel = new javax.swing.JPanel();
        configServerURLLabel = new javax.swing.JLabel();
        configServerURLTextField = new javax.swing.JTextField();
        configServersScrollPane = new javax.swing.JScrollPane();
        configServersList = new javax.swing.JList();
        configServerRightPanel = new javax.swing.JPanel();
        configServerAddButton = new javax.swing.JButton();
        configServerEditButton = new javax.swing.JButton();
        configServerRemoveButton = new javax.swing.JButton();
        configServerSeparator = new javax.swing.JSeparator();
        configServerSeparator2 = new javax.swing.JSeparator();
        configServerUpButton = new javax.swing.JButton();
        configServerDownButton = new javax.swing.JButton();
        configServerTestButton = new javax.swing.JButton();
        configServerQoSPanel = new javax.swing.JPanel();
        configServerQoSCheckBox = new javax.swing.JCheckBox();
        qosLabel = new javax.swing.JLabel();
        configVisualizationScrollPane = new javax.swing.JScrollPane();
        configVisualizationMainPanel = new javax.swing.JPanel();
        configVisualizationUsePanel = new javax.swing.JPanel();
        configNoVisRadioButton = new javax.swing.JRadioButton();
        configUseJmolRadioButton = new javax.swing.JRadioButton();
        configUseJMVRadioButton = new javax.swing.JRadioButton();
        configUseRasmolRadioButton = new javax.swing.JRadioButton();
        configVisualizationTabbedPane = new javax.swing.JTabbedPane();
        configJmolPanel = new javax.swing.JPanel();
        configJmolPathPanel = new javax.swing.JPanel();
        configJmolPathLabel = new javax.swing.JLabel();
        configJmolPathTextField = new javax.swing.JTextField();
        configJmolPathButton = new javax.swing.JButton();
        configJmolOptsPanel = new javax.swing.JPanel();
        configJmolStyleLabel = new javax.swing.JLabel();
        configJmolStyleComboBox = new javax.swing.JComboBox();
        configJmolBondsLabel = new javax.swing.JLabel();
        configJmolBondsComboBox = new javax.swing.JComboBox();
        configJmolZoomLabel = new javax.swing.JLabel();
        configJmolZoomTextField = new javax.swing.JTextField();
        configJmolAtomLabel = new javax.swing.JLabel();
        configJmolAtomTextField = new javax.swing.JTextField();
        configJmolCustomLabel = new javax.swing.JLabel();
        configJmolCustomTextField = new javax.swing.JTextField();
        configJmolBgColLabel = new javax.swing.JLabel();
        configJmolBgColButton = new javax.swing.JButton();
        configJmolFgColLabel = new javax.swing.JLabel();
        configJmolFgColButton = new javax.swing.JButton();
        configJmolWireframeLabel = new javax.swing.JLabel();
        configJmolWireframeCheckBox = new javax.swing.JCheckBox();
        configJMVPanel = new javax.swing.JPanel();
        configJMVPathPanel = new javax.swing.JPanel();
        configJMVPathLabel = new javax.swing.JLabel();
        configJMVPathTextField = new javax.swing.JTextField();
        configJMVPathButton = new javax.swing.JButton();
        configJMVOptsPanel = new javax.swing.JPanel();
        configJMVRepLabel = new javax.swing.JLabel();
        configJMVRepComboBox = new javax.swing.JComboBox();
        configJMVGradientLabel = new javax.swing.JLabel();
        configJMVGradientComboBox = new javax.swing.JComboBox();
        configJMVWidthLabel = new javax.swing.JLabel();
        configJMVWidthTextField = new javax.swing.JTextField();
        configJMVHeightLabel = new javax.swing.JLabel();
        configJMVHeightTextField = new javax.swing.JTextField();
        configJMVControlsLabel = new javax.swing.JLabel();
        configJMVControlsCheckBox = new javax.swing.JCheckBox();
        configJMVColorLabel = new javax.swing.JLabel();
        configJMVColorComboBox = new javax.swing.JComboBox();
        configRasmolPanel = new javax.swing.JPanel();
        configRasmolPathPanel = new javax.swing.JPanel();
        configRasmolPathLabel = new javax.swing.JLabel();
        configRasmolPathTextField = new javax.swing.JTextField();
        configRasmolPathButton = new javax.swing.JButton();
        configRasmolOptsPanel = new javax.swing.JPanel();
        configRasmolDisplayLabel = new javax.swing.JLabel();
        configRasmolDisplayCheckBox = new javax.swing.JCheckBox();
        configRasmolSriptPanel = new javax.swing.JPanel();
        configRasmolScriptTextField = new javax.swing.JTextField();
        configRasmolScriptButton = new javax.swing.JButton();
        configRasmolScriptLabel = new javax.swing.JLabel();
        configRasmolGzippedPanel = new javax.swing.JPanel();
        configRasmolGzippedLabel = new javax.swing.JLabel();
        configRasmolGzippedCheckBox = new javax.swing.JCheckBox();
        configRasmolDoesntworkLabel = new javax.swing.JLabel();
        configUseOtherRadioButton = new javax.swing.JRadioButton();
        configOtherPanel = new javax.swing.JPanel();
        configOtherUpperPanel = new javax.swing.JPanel();
        configOtherAppletRadioButton = new javax.swing.JRadioButton();
        configOtherStandaloneRadioButton = new javax.swing.JRadioButton();
        configOtherTabbedPane = new javax.swing.JTabbedPane();
        configOtherAppletPanel = new javax.swing.JPanel();
        otherAppletPathPanel = new javax.swing.JPanel();
        otherAppletPathLabel = new javax.swing.JLabel();
        otherAppletPathTextField = new javax.swing.JTextField();
        otherAppletPathButton = new javax.swing.JButton();
        otherAppletBottomPanel = new javax.swing.JPanel();
        otherAppletClassLabel = new javax.swing.JLabel();
        otherAppletParmsLabel = new javax.swing.JLabel();
        otherAppletClassTextField = new javax.swing.JTextField();
        otherAppletParmsTextField = new javax.swing.JTextField();
        configOtherStandalonePanel = new javax.swing.JPanel();
        configOtherStandalonePathPanel = new javax.swing.JPanel();
        configOtherStandalonePathLabel = new javax.swing.JLabel();
        configOtherStandalonePathTextField = new javax.swing.JTextField();
        configOtherStandalonePathButton = new javax.swing.JButton();
        configOtherStandaloneParmsPanel = new javax.swing.JPanel();
        configOtherStandaloneParmsLabel = new javax.swing.JLabel();
        configOtherStandaloneParmsTextField = new javax.swing.JTextField();
        configPluginTabbedPane.setPreferredSize(new java.awt.Dimension(600, 465));
        configPDBMainPanel.setLayout(new javax.swing.BoxLayout(configPDBMainPanel, javax.swing.BoxLayout.Y_AXIS));
        configPDBGeneralPanel.setLayout(new javax.swing.BoxLayout(configPDBGeneralPanel, javax.swing.BoxLayout.X_AXIS));
        configPDBGeneralPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "General Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10), java.awt.Color.blue));
        configPDBGeneralLeftPanel.setLayout(new javax.swing.BoxLayout(configPDBGeneralLeftPanel, javax.swing.BoxLayout.Y_AXIS));
        configPDBIdCheckBox.setText("PDB Identifier");
        configPDBGeneralLeftPanel.add(configPDBIdCheckBox);
        configPDBCitAuthorCheckBox.setText("Citation Author");
        configPDBGeneralLeftPanel.add(configPDBCitAuthorCheckBox);
        configPDBChainTypeCheckBox.setText("Contains Chain Type (protein, DNA etc.) ");
        configPDBGeneralLeftPanel.add(configPDBChainTypeCheckBox);
        configPDBHeaderCheckBox.setText("PDB HEADER");
        configPDBGeneralLeftPanel.add(configPDBHeaderCheckBox);
        configPDBExperimentalCheckBox.setText("Experimental Technique");
        configPDBGeneralLeftPanel.add(configPDBExperimentalCheckBox);
        configPDBTextCheckBox.setText("Text Search");
        configPDBGeneralLeftPanel.add(configPDBTextCheckBox);
        configPDBDateCheckBox.setText("Deposition/Release Date");
        configPDBGeneralLeftPanel.add(configPDBDateCheckBox);
        configPDBGeneralPanel.add(configPDBGeneralLeftPanel);
        configPDBGeneralRightPanel.setLayout(new javax.swing.BoxLayout(configPDBGeneralRightPanel, javax.swing.BoxLayout.Y_AXIS));
        configPDBCitationCheckBox.setText("Citation");
        configPDBGeneralRightPanel.add(configPDBCitationCheckBox);
        configPDBCompInfCheckBox.setText("Compound Information");
        configPDBGeneralRightPanel.add(configPDBCompInfCheckBox);
        configPDBTitleCheckBox.setText("Title");
        configPDBGeneralRightPanel.add(configPDBTitleCheckBox);
        configPDBECCheckBox.setText("EC Number and Classification");
        configPDBGeneralRightPanel.add(configPDBECCheckBox);
        configPDBLigandsCheckBox.setText("Ligands and Prosthetic Groups");
        configPDBGeneralRightPanel.add(configPDBLigandsCheckBox);
        configPDBSourceCheckBox.setText("Source");
        configPDBGeneralRightPanel.add(configPDBSourceCheckBox);
        configPDBExperDataCheckBox.setText("Experimental Data Availability");
        configPDBGeneralRightPanel.add(configPDBExperDataCheckBox);
        configPDBGeneralPanel.add(configPDBGeneralRightPanel);
        configPDBMainPanel.add(configPDBGeneralPanel);
        configPDBMiddlePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 5));
        configPDBSequencePanel.setLayout(new javax.swing.BoxLayout(configPDBSequencePanel, javax.swing.BoxLayout.Y_AXIS));
        configPDBSequencePanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Sequence and Secondary Structure ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10), java.awt.Color.blue));
        configPDBChainLenCheckBox.setText("Number of Chains and Chain Length");
        configPDBSequencePanel.add(configPDBChainLenCheckBox);
        configPDBShortSeqCheckBox.setText("Short Sequence Pattern ");
        configPDBSequencePanel.add(configPDBShortSeqCheckBox);
        configPDBSecStructCheckBox.setText("Secondary Structure Content");
        configPDBSequencePanel.add(configPDBSecStructCheckBox);
        configPDBFASTACheckBox.setText("FASTA search");
        configPDBSequencePanel.add(configPDBFASTACheckBox);
        configPDBMiddlePanel.add(configPDBSequencePanel);
        configPDBExperimentalPanel.setLayout(new javax.swing.BoxLayout(configPDBExperimentalPanel, javax.swing.BoxLayout.Y_AXIS));
        configPDBExperimentalPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Crystallographic Experimental Information ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10), java.awt.Color.blue));
        configPDBResolutionCheckBox.setText("Resolution");
        configPDBExperimentalPanel.add(configPDBResolutionCheckBox);
        configPDBUnitCellCheckBox.setText("Unit Cell Dimensions");
        configPDBExperimentalPanel.add(configPDBUnitCellCheckBox);
        configPDBSpaceGroupCheckBox.setText("Space Group");
        configPDBExperimentalPanel.add(configPDBSpaceGroupCheckBox);
        configPDBRefinementCheckBox.setText("Refinement Parameters");
        configPDBExperimentalPanel.add(configPDBRefinementCheckBox);
        configPDBMiddlePanel.add(configPDBExperimentalPanel);
        configPDBMainPanel.add(configPDBMiddlePanel);
        configPDBDisplayPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Display Options ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10), java.awt.Color.blue));
        configPDBSelOptsCheckBox.setText("Selecting Options");
        configPDBDisplayPanel.add(configPDBSelOptsCheckBox);
        configPDBMainPanel.add(configPDBDisplayPanel);
        configPDBScrollPane.setViewportView(configPDBMainPanel);
        configPluginTabbedPane.addTab("Query Form", configPDBScrollPane);
        configServerMainPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;
        configServerPanel.setLayout(new javax.swing.BoxLayout(configServerPanel, javax.swing.BoxLayout.X_AXIS));
        configServerPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Mirrors", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10), java.awt.Color.blue));
        ;
        configServerLeftPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints2;
        configServerURLPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        configServerURLLabel.setText("URL:");
        configServerURLPanel.add(configServerURLLabel);
        configServerURLTextField.setColumns(35);
        configServerURLPanel.add(configServerURLTextField);
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 2, 6);
        configServerLeftPanel.add(configServerURLPanel, gridBagConstraints2);
        configServersList.setModel(mirrorsListModel);
        configServersList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        configServersList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                int sel = configServersList.getSelectedIndex();
                if (sel != -1) {
                    configServerUpButton.setEnabled(sel > 0);
                    configServerDownButton.setEnabled(sel < mirrorsListModel.size() - 1);
                    configServerEditButton.setEnabled(true);
                    configServerRemoveButton.setEnabled(true);
                    configServerURLTextField.setText((String) (configServersList.getSelectedValue()));
                } else {
                    configServerUpButton.setEnabled(false);
                    configServerDownButton.setEnabled(false);
                    configServerEditButton.setEnabled(false);
                    configServerRemoveButton.setEnabled(false);
                }
            }
        });
        configServersScrollPane.setViewportView(configServersList);
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 6);
        configServerLeftPanel.add(configServersScrollPane, gridBagConstraints2);
        configServerPanel.add(configServerLeftPanel);
        configServerRightPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints3;
        configServerAddButton.setText("Add...");
        configServerAddButton.addActionListener(buttonListener);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(1, 4, 1, 4);
        configServerRightPanel.add(configServerAddButton, gridBagConstraints3);
        configServerEditButton.setText("Edit...");
        configServerEditButton.setEnabled(false);
        configServerEditButton.addActionListener(buttonListener);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(1, 4, 1, 4);
        configServerRightPanel.add(configServerEditButton, gridBagConstraints3);
        configServerRemoveButton.setText("Remove");
        configServerRemoveButton.setEnabled(false);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 2;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(1, 4, 1, 4);
        configServerRightPanel.add(configServerRemoveButton, gridBagConstraints3);
        configServerRemoveButton.addActionListener(buttonListener);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 3;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(5, 0, 5, 0);
        configServerRightPanel.add(configServerSeparator, gridBagConstraints3);
        configServerUpButton.setText("Up");
        configServerUpButton.setEnabled(false);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 4;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(1, 4, 1, 4);
        configServerRightPanel.add(configServerUpButton, gridBagConstraints3);
        configServerUpButton.addActionListener(buttonListener);
        configServerDownButton.setText("Down");
        configServerDownButton.setEnabled(false);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 5;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(1, 4, 1, 4);
        configServerRightPanel.add(configServerDownButton, gridBagConstraints3);
        configServerDownButton.addActionListener(buttonListener);
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 6;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(5, 0, 5, 0);
        configServerRightPanel.add(configServerSeparator2, gridBagConstraints3);
        configServerTestButton.setText("Test All");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 7;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets(1, 4, 1, 4);
        configServerRightPanel.add(configServerTestButton, gridBagConstraints3);
        configServerTestButton.addActionListener(buttonListener);
        configServerPanel.add(configServerRightPanel);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 0, 5, 0);
        configServerMainPanel.add(configServerPanel, gridBagConstraints1);
        configServerQoSPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints6;
        configServerQoSCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints6 = new java.awt.GridBagConstraints();
        gridBagConstraints6.gridx = 1;
        gridBagConstraints6.gridy = 0;
        gridBagConstraints6.insets = new java.awt.Insets(0, 10, 4, 0);
        gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
        configServerQoSPanel.add(configServerQoSCheckBox, gridBagConstraints6);
        qosLabel.setText("Quality of Service");
        gridBagConstraints6 = new java.awt.GridBagConstraints();
        gridBagConstraints6.gridx = 0;
        gridBagConstraints6.gridy = 0;
        gridBagConstraints6.insets = new java.awt.Insets(0, 0, 4, 0);
        gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
        configServerQoSPanel.add(qosLabel, gridBagConstraints6);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new java.awt.Insets(6, 0, 17, 0);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        configServerMainPanel.add(configServerQoSPanel, gridBagConstraints1);
        configServerScrollPane.setViewportView(configServerMainPanel);
        configPluginTabbedPane.addTab("Mirrors", configServerScrollPane);
        configVisualizationMainPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints5;
        configVisualizationUsePanel.setLayout(new java.awt.GridLayout(0, 1));
        configNoVisRadioButton.setSelected(true);
        configNoVisRadioButton.setText("No Visualization");
        visualizationButtonGroup.add(configNoVisRadioButton);
        configNoVisRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setEnabledJmolButtons(configUseJmolRadioButton.isSelected());
                setEnabledJmvButtons(configUseJMVRadioButton.isSelected());
                setEnabledRasmolButtons(configUseRasmolRadioButton.isSelected());
                boolean s = configUseOtherRadioButton.isSelected();
                setEnabledOtherButtons(s && configOtherAppletRadioButton.isSelected(), s && configOtherStandaloneRadioButton.isSelected());
            }
        });
        configVisualizationUsePanel.add(configNoVisRadioButton);
        configUseJmolRadioButton.setText("JmolApplet");
        visualizationButtonGroup.add(configUseJmolRadioButton);
        configUseJmolRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setEnabledJmolButtons(configUseJmolRadioButton.isSelected());
                setEnabledJmvButtons(configUseJMVRadioButton.isSelected());
                setEnabledRasmolButtons(configUseRasmolRadioButton.isSelected());
                boolean s = configUseOtherRadioButton.isSelected();
                setEnabledOtherButtons(s && configOtherAppletRadioButton.isSelected(), s && configOtherStandaloneRadioButton.isSelected());
                configVisualizationTabbedPane.setSelectedIndex(VISUALIZATION_JMOL);
            }
        });
        configVisualizationUsePanel.add(configUseJmolRadioButton);
        configUseJMVRadioButton.setText("JMV");
        visualizationButtonGroup.add(configUseJMVRadioButton);
        configUseJMVRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setEnabledJmolButtons(configUseJmolRadioButton.isSelected());
                setEnabledJmvButtons(configUseJMVRadioButton.isSelected());
                setEnabledRasmolButtons(configUseRasmolRadioButton.isSelected());
                boolean s = configUseOtherRadioButton.isSelected();
                setEnabledOtherButtons(s && configOtherAppletRadioButton.isSelected(), s && configOtherStandaloneRadioButton.isSelected());
                configVisualizationTabbedPane.setSelectedIndex(VISUALIZATION_JMV);
            }
        });
        configVisualizationUsePanel.add(configUseJMVRadioButton);
        configUseRasmolRadioButton.setText("RasMol");
        visualizationButtonGroup.add(configUseRasmolRadioButton);
        configUseRasmolRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setEnabledJmolButtons(configUseJmolRadioButton.isSelected());
                setEnabledJmvButtons(configUseJMVRadioButton.isSelected());
                setEnabledRasmolButtons(configUseRasmolRadioButton.isSelected());
                boolean s = configUseOtherRadioButton.isSelected();
                setEnabledOtherButtons(s && configOtherAppletRadioButton.isSelected(), s && configOtherStandaloneRadioButton.isSelected());
                configVisualizationTabbedPane.setSelectedIndex(VISUALIZATION_RASMOL);
            }
        });
        configVisualizationUsePanel.add(configUseRasmolRadioButton);
        configUseOtherRadioButton.setText("Other");
        visualizationButtonGroup.add(configUseOtherRadioButton);
        configUseOtherRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setEnabledJmolButtons(configUseJmolRadioButton.isSelected());
                setEnabledJmvButtons(configUseJMVRadioButton.isSelected());
                setEnabledRasmolButtons(configUseRasmolRadioButton.isSelected());
                boolean s = configUseOtherRadioButton.isSelected();
                setEnabledOtherButtons(s && configOtherAppletRadioButton.isSelected(), s && configOtherStandaloneRadioButton.isSelected());
                configVisualizationTabbedPane.setSelectedIndex(VISUALIZATION_OTHER);
            }
        });
        configVisualizationUsePanel.add(configUseOtherRadioButton);
        gridBagConstraints5 = new java.awt.GridBagConstraints();
        gridBagConstraints5.insets = new java.awt.Insets(3, 0, 3, 0);
        configVisualizationMainPanel.add(configVisualizationUsePanel, gridBagConstraints5);
        configJmolPanel.setLayout(new javax.swing.BoxLayout(configJmolPanel, javax.swing.BoxLayout.Y_AXIS));
        configJmolPathPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        configJmolPathLabel.setText("JmolApplet Path");
        configJmolPathLabel.setPreferredSize(new java.awt.Dimension(120, 20));
        configJmolPathPanel.add(configJmolPathLabel);
        configJmolPathTextField.setColumns(31);
        configJmolPathTextField.setToolTipText("Path to JmolApplet jars");
        configJmolPathPanel.add(configJmolPathTextField);
        configJmolPathButton.setIcon(new ImageIcon(com.pallas.unicore.resourcemanager.ResourceManager.getImage("open.gif")));
        configJmolPathButton.addActionListener(buttonListener);
        configJmolPathPanel.add(configJmolPathButton);
        gridBagConstraints5 = new java.awt.GridBagConstraints();
        gridBagConstraints5.gridx = 0;
        gridBagConstraints5.gridy = 1;
        gridBagConstraints5.insets = new java.awt.Insets(3, 0, 3, 0);
        configJmolPanel.add(configJmolPathPanel, gridBagConstraints5);
        configJmolOptsPanel.setLayout(new java.awt.GridBagLayout());
        configJmolOptsPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Jmol Options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10), java.awt.Color.blue));
        configJmolStyleLabel.setText("Style");
        gridBagConstraints5 = new java.awt.GridBagConstraints();
        gridBagConstraints5.insets = new java.awt.Insets(1, 0, 3, 0);
        gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
        configJmolOptsPanel.add(configJmolStyleLabel, gridBagConstraints5);
        configJmolStyleComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Shaded", "Wireframe", "Quickdraw" }));
        configJmolStyleComboBox.setToolTipText("The rendering style");
        gridBagConstraints5 = new java.awt.GridBagConstraints();
        gridBagConstraints5.insets = new java.awt.Insets(1, 0, 3, 0);
        gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
        configJmolOptsPanel.add(configJmolStyleComboBox, gridBagConstraints5);
        configJmolBondsComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "On", "Off", "Never" }));
        configJmolBgColLabel.setText("Background Color");
        gridBagConstraints5 = new java.awt.GridBagConstraints();
        gridBagConstraints5.gridx = 0;
        gridBagConstraints5.gridy = 6;
        gridBagConstraints5.insets = new java.awt.Insets(1, 0, 3, 0);
        gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
        configJmolOptsPanel.add(configJmolBgColLabel, gridBagConstraints5);
        configJmolBgColButton.setPreferredSize(new java.awt.Dimension(30, 20));
        configJmolBgColButton.setToolTipText("The color of the background");
        configJmolBgColButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                java.awt.Color sel = JColorChooser.showDialog(pluginFrame, "Select background color", configJmolBgColButton.getBackground());
                if (sel != null) configJmolBgColButton.setBackground(sel);
            }
        });
        gridBagConstraints5 = new java.awt.GridBagConstraints();
        gridBagConstraints5.gridx = 1;
        gridBagConstraints5.gridy = 6;
        gridBagConstraints5.insets = new java.awt.Insets(1, 0, 3, 0);
        gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
        configJmolOptsPanel.add(configJmolBgColButton, gridBagConstraints5);
        configJmolWireframeLabel.setText("Wireframe Rotation");
        gridBagConstraints5 = new java.awt.GridBagConstraints();
        gridBagConstraints5.gridx = 0;
        gridBagConstraints5.gridy = 5;
        gridBagConstraints5.insets = new java.awt.Insets(1, 0, 3, 10);
        gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
        configJmolOptsPanel.add(configJmolWireframeLabel, gridBagConstraints5);
        gridBagConstraints5 = new java.awt.GridBagConstraints();
        gridBagConstraints5.gridx = 1;
        gridBagConstraints5.gridy = 5;
        gridBagConstraints5.insets = new java.awt.Insets(1, 0, 3, 0);
        gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
        configJmolOptsPanel.add(configJmolWireframeCheckBox, gridBagConstraints5);
        gridBagConstraints5 = new java.awt.GridBagConstraints();
        gridBagConstraints5.gridx = 0;
        gridBagConstraints5.gridy = 2;
        gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints5.insets = new java.awt.Insets(3, 0, 3, 0);
        configJmolPanel.add(configJmolOptsPanel, gridBagConstraints5);
        configVisualizationTabbedPane.addTab("JmolApplet", configJmolPanel);
        configJMVPanel.setLayout(new javax.swing.BoxLayout(configJMVPanel, javax.swing.BoxLayout.Y_AXIS));
        configJMVPathPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        configJMVPathLabel.setText("JMV Path");
        configJMVPathLabel.setPreferredSize(new java.awt.Dimension(120, 20));
        configJMVPathPanel.add(configJMVPathLabel);
        configJMVPathTextField.setColumns(31);
        configJMVPathTextField.setToolTipText("Path to JMV jars");
        configJMVPathPanel.add(configJMVPathTextField);
        configJMVPathButton.setIcon(new ImageIcon(com.pallas.unicore.resourcemanager.ResourceManager.getImage("open.gif")));
        configJMVPathButton.addActionListener(buttonListener);
        configJMVPathPanel.add(configJMVPathButton);
        configJMVPanel.add(configJMVPathPanel);
        configJMVOptsPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints7;
        configJMVOptsPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "JMV Options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10), java.awt.Color.blue));
        configJMVRepLabel.setText("Representation");
        gridBagConstraints7 = new java.awt.GridBagConstraints();
        gridBagConstraints7.gridx = 0;
        gridBagConstraints7.gridy = 1;
        gridBagConstraints7.insets = new java.awt.Insets(3, 0, 6, 10);
        gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
        configJMVOptsPanel.add(configJMVRepLabel, gridBagConstraints7);
        configJMVRepComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Lines", "VDW", "Bonds", "CPK", "Trace", "Tube", "Licorice" }));
        configJMVRepComboBox.setToolTipText("The rendering style");
        gridBagConstraints7 = new java.awt.GridBagConstraints();
        gridBagConstraints7.gridx = 1;
        gridBagConstraints7.gridy = 1;
        gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints7.insets = new java.awt.Insets(3, 0, 6, 0);
        gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
        configJMVOptsPanel.add(configJMVRepComboBox, gridBagConstraints7);
        configJMVGradientLabel.setText("Gradient");
        gridBagConstraints7 = new java.awt.GridBagConstraints();
        gridBagConstraints7.gridx = 0;
        gridBagConstraints7.gridy = 3;
        gridBagConstraints7.insets = new java.awt.Insets(3, 0, 6, 10);
        gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
        configJMVOptsPanel.add(configJMVGradientLabel, gridBagConstraints7);
        configJMVGradientComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Red->Green->Blue", "Blue->Green->Red", "Red->White->Blue", "Blue->White->Red", "Black->White", "White->Black" }));
        gridBagConstraints7 = new java.awt.GridBagConstraints();
        gridBagConstraints7.gridx = 1;
        gridBagConstraints7.gridy = 3;
        gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints7.insets = new java.awt.Insets(3, 0, 6, 0);
        gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
        configJMVOptsPanel.add(configJMVGradientComboBox, gridBagConstraints7);
        configJMVWidthLabel.setText("Width");
        gridBagConstraints7 = new java.awt.GridBagConstraints();
        gridBagConstraints7.gridx = 0;
        gridBagConstraints7.gridy = 4;
        gridBagConstraints7.insets = new java.awt.Insets(3, 0, 6, 10);
        gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
        configJMVOptsPanel.add(configJMVWidthLabel, gridBagConstraints7);
        configJMVWidthTextField.setColumns(5);
        gridBagConstraints7 = new java.awt.GridBagConstraints();
        gridBagConstraints7.gridx = 1;
        gridBagConstraints7.gridy = 4;
        gridBagConstraints7.insets = new java.awt.Insets(3, 0, 6, 0);
        gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
        configJMVOptsPanel.add(configJMVWidthTextField, gridBagConstraints7);
        configJMVHeightLabel.setText("Height");
        gridBagConstraints7 = new java.awt.GridBagConstraints();
        gridBagConstraints7.gridx = 0;
        gridBagConstraints7.gridy = 5;
        gridBagConstraints7.insets = new java.awt.Insets(3, 0, 6, 10);
        gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
        configJMVOptsPanel.add(configJMVHeightLabel, gridBagConstraints7);
        configJMVHeightTextField.setColumns(5);
        gridBagConstraints7 = new java.awt.GridBagConstraints();
        gridBagConstraints7.gridx = 1;
        gridBagConstraints7.gridy = 5;
        gridBagConstraints7.insets = new java.awt.Insets(3, 0, 6, 0);
        gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
        configJMVOptsPanel.add(configJMVHeightTextField, gridBagConstraints7);
        configJMVControlsLabel.setText("Controls");
        gridBagConstraints7 = new java.awt.GridBagConstraints();
        gridBagConstraints7.gridx = 0;
        gridBagConstraints7.gridy = 0;
        gridBagConstraints7.insets = new java.awt.Insets(3, 0, 6, 10);
        gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
        configJMVOptsPanel.add(configJMVControlsLabel, gridBagConstraints7);
        gridBagConstraints7 = new java.awt.GridBagConstraints();
        gridBagConstraints7.gridx = 1;
        gridBagConstraints7.gridy = 0;
        gridBagConstraints7.insets = new java.awt.Insets(3, 0, 6, 0);
        gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
        configJMVOptsPanel.add(configJMVControlsCheckBox, gridBagConstraints7);
        configJMVColorLabel.setText("Color");
        gridBagConstraints7 = new java.awt.GridBagConstraints();
        gridBagConstraints7.gridx = 0;
        gridBagConstraints7.gridy = 2;
        gridBagConstraints7.insets = new java.awt.Insets(3, 0, 6, 10);
        gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
        configJMVOptsPanel.add(configJMVColorLabel, gridBagConstraints7);
        configJMVColorComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "by Name", "by Index", "by Residue Name", "by Chain", "by Segment Name", "by Structure" }));
        gridBagConstraints7 = new java.awt.GridBagConstraints();
        gridBagConstraints7.gridx = 1;
        gridBagConstraints7.gridy = 2;
        gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints7.insets = new java.awt.Insets(3, 0, 6, 0);
        gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
        configJMVOptsPanel.add(configJMVColorComboBox, gridBagConstraints7);
        configJMVPanel.add(configJMVOptsPanel);
        configVisualizationTabbedPane.addTab("JMV", configJMVPanel);
        configRasmolPanel.setLayout(new javax.swing.BoxLayout(configRasmolPanel, javax.swing.BoxLayout.Y_AXIS));
        configRasmolPathPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        configRasmolPathLabel.setText("Command");
        configRasmolPathLabel.setPreferredSize(new java.awt.Dimension(120, 20));
        configRasmolPathPanel.add(configRasmolPathLabel);
        configRasmolPathTextField.setColumns(31);
        configRasmolPathPanel.add(configRasmolPathTextField);
        configRasmolPathButton.setIcon(new ImageIcon(com.pallas.unicore.resourcemanager.ResourceManager.getImage("open.gif")));
        configRasmolPathButton.addActionListener(buttonListener);
        configRasmolPathPanel.add(configRasmolPathButton);
        configRasmolPanel.add(configRasmolPathPanel);
        configRasmolOptsPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints8;
        configRasmolOptsPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "RasMol Options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10), java.awt.Color.blue));
        configRasmolDisplayLabel.setText("No Display");
        gridBagConstraints8 = new java.awt.GridBagConstraints();
        gridBagConstraints8.gridx = 0;
        gridBagConstraints8.gridy = 0;
        gridBagConstraints8.insets = new java.awt.Insets(3, 0, 5, 7);
        gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
        configRasmolOptsPanel.add(configRasmolDisplayLabel, gridBagConstraints8);
        gridBagConstraints8 = new java.awt.GridBagConstraints();
        gridBagConstraints8.gridx = 1;
        gridBagConstraints8.gridy = 0;
        gridBagConstraints8.insets = new java.awt.Insets(3, 5, 5, 0);
        gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
        configRasmolOptsPanel.add(configRasmolDisplayCheckBox, gridBagConstraints8);
        configRasmolSriptPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        configRasmolScriptTextField.setColumns(34);
        configRasmolSriptPanel.add(configRasmolScriptTextField);
        configRasmolScriptButton.setIcon(new ImageIcon(com.pallas.unicore.resourcemanager.ResourceManager.getImage("open.gif")));
        configRasmolScriptButton.addActionListener(buttonListener);
        configRasmolSriptPanel.add(configRasmolScriptButton);
        gridBagConstraints8 = new java.awt.GridBagConstraints();
        gridBagConstraints8.gridx = 1;
        gridBagConstraints8.gridy = 1;
        gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
        configRasmolOptsPanel.add(configRasmolSriptPanel, gridBagConstraints8);
        configRasmolScriptLabel.setText("Script");
        gridBagConstraints8 = new java.awt.GridBagConstraints();
        gridBagConstraints8.gridx = 0;
        gridBagConstraints8.gridy = 1;
        gridBagConstraints8.insets = new java.awt.Insets(0, 0, 0, 7);
        gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
        configRasmolOptsPanel.add(configRasmolScriptLabel, gridBagConstraints8);
        configRasmolGzippedPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints9;
        configRasmolGzippedLabel.setText("Download gzipped files");
        gridBagConstraints9 = new java.awt.GridBagConstraints();
        gridBagConstraints9.gridx = 0;
        gridBagConstraints9.gridy = 0;
        configRasmolGzippedPanel.add(configRasmolGzippedLabel, gridBagConstraints9);
        configRasmolGzippedCheckBox.setFont(new java.awt.Font("Dialog", 0, 10));
        gridBagConstraints9 = new java.awt.GridBagConstraints();
        gridBagConstraints9.gridx = 1;
        gridBagConstraints9.gridy = 0;
        gridBagConstraints9.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints9.insets = new java.awt.Insets(0, 12, 0, 0);
        configRasmolGzippedPanel.add(configRasmolGzippedCheckBox, gridBagConstraints9);
        configRasmolDoesntworkLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        configRasmolDoesntworkLabel.setText("( does NOT work with RasWin )");
        gridBagConstraints9 = new java.awt.GridBagConstraints();
        gridBagConstraints9.gridx = 0;
        gridBagConstraints9.gridy = 1;
        configRasmolGzippedPanel.add(configRasmolDoesntworkLabel, gridBagConstraints9);
        gridBagConstraints8 = new java.awt.GridBagConstraints();
        gridBagConstraints8.gridx = 0;
        gridBagConstraints8.gridy = 2;
        gridBagConstraints8.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
        configRasmolOptsPanel.add(configRasmolGzippedPanel, gridBagConstraints8);
        configRasmolPanel.add(configRasmolOptsPanel);
        configVisualizationTabbedPane.addTab("RasMol", configRasmolPanel);
        configOtherPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints10;
        configOtherAppletRadioButton.setText("Applet");
        otherButtonGroup.add(configOtherAppletRadioButton);
        configOtherAppletRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setEnabledOtherButtons(configOtherAppletRadioButton.isSelected(), configOtherStandaloneRadioButton.isSelected());
                configOtherTabbedPane.setSelectedIndex(VISUALIZATION_OTHER_APPLET);
            }
        });
        configOtherUpperPanel.add(configOtherAppletRadioButton);
        configOtherStandaloneRadioButton.setText("Standalone Application");
        otherButtonGroup.add(configOtherStandaloneRadioButton);
        configOtherStandaloneRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setEnabledOtherButtons(configOtherAppletRadioButton.isSelected(), configOtherStandaloneRadioButton.isSelected());
                configOtherTabbedPane.setSelectedIndex(VISUALIZATION_OTHER_STANDALONE);
            }
        });
        configOtherUpperPanel.add(configOtherStandaloneRadioButton);
        gridBagConstraints10 = new java.awt.GridBagConstraints();
        configOtherPanel.add(configOtherUpperPanel, gridBagConstraints10);
        configOtherAppletPanel.setLayout(new javax.swing.BoxLayout(configOtherAppletPanel, javax.swing.BoxLayout.Y_AXIS));
        otherAppletPathPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 10));
        otherAppletPathLabel.setText("Path");
        otherAppletPathPanel.add(otherAppletPathLabel);
        otherAppletPathTextField.setColumns(39);
        otherAppletPathPanel.add(otherAppletPathTextField);
        otherAppletPathButton.setIcon(new ImageIcon(com.pallas.unicore.resourcemanager.ResourceManager.getImage("open.gif")));
        otherAppletPathButton.addActionListener(buttonListener);
        otherAppletPathPanel.add(otherAppletPathButton);
        configOtherAppletPanel.add(otherAppletPathPanel);
        otherAppletBottomPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints11;
        otherAppletClassLabel.setText("Class Name");
        gridBagConstraints11 = new java.awt.GridBagConstraints();
        gridBagConstraints11.insets = new java.awt.Insets(9, 0, 0, 6);
        gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
        otherAppletBottomPanel.add(otherAppletClassLabel, gridBagConstraints11);
        otherAppletParmsLabel.setText("Parameters");
        gridBagConstraints11 = new java.awt.GridBagConstraints();
        gridBagConstraints11.gridx = 0;
        gridBagConstraints11.gridy = 1;
        gridBagConstraints11.insets = new java.awt.Insets(6, 0, 0, 6);
        gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
        otherAppletBottomPanel.add(otherAppletParmsLabel, gridBagConstraints11);
        otherAppletClassTextField.setColumns(40);
        gridBagConstraints11 = new java.awt.GridBagConstraints();
        gridBagConstraints11.insets = new java.awt.Insets(9, 0, 0, 0);
        otherAppletBottomPanel.add(otherAppletClassTextField, gridBagConstraints11);
        otherAppletParmsTextField.setColumns(40);
        gridBagConstraints11 = new java.awt.GridBagConstraints();
        gridBagConstraints11.gridx = 1;
        gridBagConstraints11.gridy = 1;
        gridBagConstraints11.insets = new java.awt.Insets(6, 0, 0, 0);
        otherAppletBottomPanel.add(otherAppletParmsTextField, gridBagConstraints11);
        configOtherAppletPanel.add(otherAppletBottomPanel);
        configOtherTabbedPane.addTab("Applet", configOtherAppletPanel);
        configOtherStandalonePanel.setLayout(new javax.swing.BoxLayout(configOtherStandalonePanel, javax.swing.BoxLayout.Y_AXIS));
        configOtherStandalonePathPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 10));
        configOtherStandalonePathLabel.setText("Path");
        configOtherStandalonePathPanel.add(configOtherStandalonePathLabel);
        configOtherStandalonePathTextField.setColumns(39);
        configOtherStandalonePathPanel.add(configOtherStandalonePathTextField);
        configOtherStandalonePathButton.setIcon(new ImageIcon(com.pallas.unicore.resourcemanager.ResourceManager.getImage("open.gif")));
        configOtherStandalonePathButton.addActionListener(buttonListener);
        configOtherStandalonePathPanel.add(configOtherStandalonePathButton);
        configOtherStandalonePanel.add(configOtherStandalonePathPanel);
        configOtherStandaloneParmsPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints12;
        configOtherStandaloneParmsLabel.setText("Parameters");
        gridBagConstraints12 = new java.awt.GridBagConstraints();
        gridBagConstraints12.gridx = 0;
        gridBagConstraints12.gridy = 0;
        gridBagConstraints12.insets = new java.awt.Insets(3, 0, 0, 6);
        gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
        configOtherStandaloneParmsPanel.add(configOtherStandaloneParmsLabel, gridBagConstraints12);
        configOtherStandaloneParmsTextField.setColumns(40);
        gridBagConstraints12 = new java.awt.GridBagConstraints();
        gridBagConstraints12.gridx = 1;
        gridBagConstraints12.gridy = 0;
        gridBagConstraints12.insets = new java.awt.Insets(3, 0, 0, 0);
        configOtherStandaloneParmsPanel.add(configOtherStandaloneParmsTextField, gridBagConstraints12);
        configOtherStandalonePanel.add(configOtherStandaloneParmsPanel);
        configOtherTabbedPane.addTab("Standalone App", configOtherStandalonePanel);
        gridBagConstraints10 = new java.awt.GridBagConstraints();
        gridBagConstraints10.gridx = 0;
        gridBagConstraints10.gridy = 1;
        gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
        configOtherPanel.add(configOtherTabbedPane, gridBagConstraints10);
        configVisualizationTabbedPane.addTab("Other", configOtherPanel);
        gridBagConstraints5 = new java.awt.GridBagConstraints();
        gridBagConstraints5.gridx = 0;
        gridBagConstraints5.gridy = 1;
        configVisualizationMainPanel.add(configVisualizationTabbedPane, gridBagConstraints5);
        configVisualizationScrollPane.setViewportView(configVisualizationMainPanel);
        configPluginTabbedPane.addTab("Visualization", configVisualizationScrollPane);
        configPluginOkButton.setText("OK");
        configPluginOkButton.addActionListener(buttonListener);
        configPluginOkPanel.add(configPluginOkButton);
        configPluginCancelButton.setText("Cancel");
        configPluginCancelButton.addActionListener(buttonListener);
        configPluginOkPanel.add(configPluginCancelButton);
        getContentPane().add(configPluginTabbedPane, java.awt.BorderLayout.CENTER);
        getContentPane().add(configPluginOkPanel, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void testAllMirrors() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                java.util.Vector results = new java.util.Vector();
                java.util.Vector mirrors = new java.util.Vector();
                for (int j = 0; j < mirrorsListModel.size(); j++) mirrors.add(mirrorsListModel.get(j));
                QoSChecker.testServers(mirrors, results, QoSChecker.PDB);
                String out = "<html>";
                for (int i = 0; i < mirrorsListModel.size(); i++) out = out + (String) results.get(i) + "<br>";
                out = out + "</html>";
                JOptionPane.showMessageDialog(pluginFrame, out, "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private class ButtonListener implements ActionListener {

        private boolean isAlready(String s) {
            int i = 0;
            while ((i < mirrorsListModel.size()) && (((String) mirrorsListModel.getElementAt(i)).compareTo(s) != 0)) i++;
            return i < mirrorsListModel.size();
        }

        public void actionPerformed(java.awt.event.ActionEvent e) {
            Object src = e.getSource();
            if (src == configPluginOkButton) {
                if ((setNewDefaults() == 0) && (pluginDefaults.writeToFile() == true)) dispose();
            } else if (src == configPluginCancelButton) {
                dispose();
            } else if ((src == configJmolPathButton) || (src == configJMVPathButton) || (src == otherAppletPathButton)) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Applet Path");
                MyFileFilter filter = new MyFileFilter(new String[] { "jar" }, "Jar Archives");
                chooser.addChoosableFileFilter(filter);
                chooser.setFileFilter(filter);
                if (chooser.showOpenDialog(pluginFrame) == JFileChooser.APPROVE_OPTION) {
                    if (src == configJmolPathButton) configJmolPathTextField.setText(chooser.getSelectedFile().getPath()); else if (src == configJMVPathButton) configJMVPathTextField.setText(chooser.getSelectedFile().getPath()); else if (src == otherAppletPathButton) otherAppletPathTextField.setText(chooser.getSelectedFile().getPath());
                }
            } else if ((src == configRasmolPathButton) || (src == configRasmolScriptButton) || (src == configOtherStandalonePathButton)) {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(pluginFrame) == JFileChooser.APPROVE_OPTION) {
                    if (src == configRasmolPathButton) configRasmolPathTextField.setText(chooser.getSelectedFile().getPath()); else if (src == configRasmolScriptButton) configRasmolScriptTextField.setText(chooser.getSelectedFile().getPath()); else if (src == configOtherStandalonePathButton) configOtherStandalonePathTextField.setText(chooser.getSelectedFile().getPath());
                }
            } else if (src == configServerAddButton && (mirrorsListModel.size() < 10)) {
                String url = configServerURLTextField.getText().trim();
                if ((url.length() > 0) && (!isAlready(url))) mirrorsListModel.addElement(url);
                int sel = configServersList.getSelectedIndex();
                if (sel != -1) {
                    configServerUpButton.setEnabled(sel > 0);
                    configServerDownButton.setEnabled(sel < mirrorsListModel.size() - 1);
                } else {
                    configServerUpButton.setEnabled(false);
                    configServerDownButton.setEnabled(false);
                }
            } else if (src == configServerEditButton) {
                String url = configServerURLTextField.getText().trim();
                if (url.length() > 0) mirrorsListModel.setElementAt(url, configServersList.getSelectedIndex());
            } else if (src == configServerRemoveButton) {
                int sel = configServersList.getSelectedIndex();
                mirrorsListModel.removeElementAt(sel);
            } else if (src == configServerUpButton) {
                int sel = configServersList.getSelectedIndex();
                Object tmp = mirrorsListModel.remove(sel);
                mirrorsListModel.add(sel - 1, tmp);
            } else if (src == configServerDownButton) {
                int sel = configServersList.getSelectedIndex();
                Object tmp = mirrorsListModel.remove(sel);
                mirrorsListModel.add(sel + 1, tmp);
            } else if (src == configServerTestButton) {
                testAllMirrors();
            }
        }
    }

    private javax.swing.ButtonGroup visualizationButtonGroup;

    private javax.swing.ButtonGroup otherButtonGroup;

    private javax.swing.JTabbedPane configPluginTabbedPane;

    private javax.swing.JScrollPane configPDBScrollPane;

    private javax.swing.JPanel configPDBMainPanel;

    private javax.swing.JPanel configPDBGeneralPanel;

    private javax.swing.JPanel configPDBGeneralLeftPanel;

    private javax.swing.JCheckBox configPDBIdCheckBox;

    private javax.swing.JCheckBox configPDBCitAuthorCheckBox;

    private javax.swing.JCheckBox configPDBChainTypeCheckBox;

    private javax.swing.JCheckBox configPDBHeaderCheckBox;

    private javax.swing.JCheckBox configPDBExperimentalCheckBox;

    private javax.swing.JCheckBox configPDBTextCheckBox;

    private javax.swing.JCheckBox configPDBDateCheckBox;

    private javax.swing.JPanel configPDBGeneralRightPanel;

    private javax.swing.JCheckBox configPDBCitationCheckBox;

    private javax.swing.JCheckBox configPDBCompInfCheckBox;

    private javax.swing.JCheckBox configPDBTitleCheckBox;

    private javax.swing.JCheckBox configPDBECCheckBox;

    private javax.swing.JCheckBox configPDBLigandsCheckBox;

    private javax.swing.JCheckBox configPDBSourceCheckBox;

    private javax.swing.JCheckBox configPDBExperDataCheckBox;

    private javax.swing.JPanel configPDBMiddlePanel;

    private javax.swing.JPanel configPDBSequencePanel;

    private javax.swing.JCheckBox configPDBChainLenCheckBox;

    private javax.swing.JCheckBox configPDBShortSeqCheckBox;

    private javax.swing.JCheckBox configPDBSecStructCheckBox;

    private javax.swing.JCheckBox configPDBFASTACheckBox;

    private javax.swing.JPanel configPDBExperimentalPanel;

    private javax.swing.JCheckBox configPDBResolutionCheckBox;

    private javax.swing.JCheckBox configPDBUnitCellCheckBox;

    private javax.swing.JCheckBox configPDBSpaceGroupCheckBox;

    private javax.swing.JCheckBox configPDBRefinementCheckBox;

    private javax.swing.JPanel configPDBDisplayPanel;

    private javax.swing.JCheckBox configPDBSelOptsCheckBox;

    private javax.swing.JScrollPane configServerScrollPane;

    private javax.swing.JPanel configServerMainPanel;

    private javax.swing.JPanel configServerPanel;

    private javax.swing.JPanel configServerLeftPanel;

    private javax.swing.JPanel configServerURLPanel;

    private javax.swing.JLabel configServerURLLabel;

    private javax.swing.JTextField configServerURLTextField;

    private javax.swing.JScrollPane configServersScrollPane;

    private javax.swing.JList configServersList;

    private javax.swing.JPanel configServerRightPanel;

    private javax.swing.JButton configServerAddButton;

    private javax.swing.JButton configServerEditButton;

    private javax.swing.JButton configServerRemoveButton;

    private javax.swing.JSeparator configServerSeparator;

    private javax.swing.JSeparator configServerSeparator2;

    private javax.swing.JButton configServerUpButton;

    private javax.swing.JButton configServerDownButton;

    private javax.swing.JButton configServerTestButton;

    private javax.swing.JPanel configServerQoSPanel;

    private javax.swing.JCheckBox configServerQoSCheckBox;

    private javax.swing.JLabel qosLabel;

    private javax.swing.JScrollPane configVisualizationScrollPane;

    private javax.swing.JPanel configVisualizationMainPanel;

    private javax.swing.JPanel configVisualizationUsePanel;

    private javax.swing.JRadioButton configNoVisRadioButton;

    private javax.swing.JRadioButton configUseJmolRadioButton;

    private javax.swing.JRadioButton configUseJMVRadioButton;

    private javax.swing.JRadioButton configUseRasmolRadioButton;

    private javax.swing.JTabbedPane configVisualizationTabbedPane;

    private javax.swing.JPanel configJmolPanel;

    private javax.swing.JPanel configJmolPathPanel;

    private javax.swing.JLabel configJmolPathLabel;

    private javax.swing.JTextField configJmolPathTextField;

    private javax.swing.JButton configJmolPathButton;

    private javax.swing.JPanel configJmolOptsPanel;

    private javax.swing.JLabel configJmolStyleLabel;

    private javax.swing.JComboBox configJmolStyleComboBox;

    private javax.swing.JLabel configJmolBondsLabel;

    private javax.swing.JComboBox configJmolBondsComboBox;

    private javax.swing.JLabel configJmolZoomLabel;

    private javax.swing.JTextField configJmolZoomTextField;

    private javax.swing.JLabel configJmolAtomLabel;

    private javax.swing.JTextField configJmolAtomTextField;

    private javax.swing.JLabel configJmolCustomLabel;

    private javax.swing.JTextField configJmolCustomTextField;

    private javax.swing.JLabel configJmolBgColLabel;

    private javax.swing.JButton configJmolBgColButton;

    private javax.swing.JLabel configJmolFgColLabel;

    private javax.swing.JButton configJmolFgColButton;

    private javax.swing.JLabel configJmolWireframeLabel;

    private javax.swing.JCheckBox configJmolWireframeCheckBox;

    private javax.swing.JPanel configJMVPanel;

    private javax.swing.JPanel configJMVPathPanel;

    private javax.swing.JLabel configJMVPathLabel;

    private javax.swing.JTextField configJMVPathTextField;

    private javax.swing.JButton configJMVPathButton;

    private javax.swing.JPanel configJMVOptsPanel;

    private javax.swing.JLabel configJMVRepLabel;

    private javax.swing.JComboBox configJMVRepComboBox;

    private javax.swing.JLabel configJMVGradientLabel;

    private javax.swing.JComboBox configJMVGradientComboBox;

    private javax.swing.JLabel configJMVWidthLabel;

    private javax.swing.JTextField configJMVWidthTextField;

    private javax.swing.JLabel configJMVHeightLabel;

    private javax.swing.JTextField configJMVHeightTextField;

    private javax.swing.JLabel configJMVControlsLabel;

    private javax.swing.JCheckBox configJMVControlsCheckBox;

    private javax.swing.JLabel configJMVColorLabel;

    private javax.swing.JComboBox configJMVColorComboBox;

    private javax.swing.JPanel configRasmolPanel;

    private javax.swing.JPanel configRasmolPathPanel;

    private javax.swing.JLabel configRasmolPathLabel;

    private javax.swing.JTextField configRasmolPathTextField;

    private javax.swing.JButton configRasmolPathButton;

    private javax.swing.JPanel configRasmolOptsPanel;

    private javax.swing.JLabel configRasmolDisplayLabel;

    private javax.swing.JCheckBox configRasmolDisplayCheckBox;

    private javax.swing.JPanel configRasmolSriptPanel;

    private javax.swing.JTextField configRasmolScriptTextField;

    private javax.swing.JButton configRasmolScriptButton;

    private javax.swing.JLabel configRasmolScriptLabel;

    private javax.swing.JPanel configRasmolGzippedPanel;

    private javax.swing.JLabel configRasmolGzippedLabel;

    private javax.swing.JCheckBox configRasmolGzippedCheckBox;

    private javax.swing.JLabel configRasmolDoesntworkLabel;

    private javax.swing.JRadioButton configUseOtherRadioButton;

    private javax.swing.JPanel configOtherPanel;

    private javax.swing.JPanel configOtherUpperPanel;

    private javax.swing.JRadioButton configOtherAppletRadioButton;

    private javax.swing.JRadioButton configOtherStandaloneRadioButton;

    private javax.swing.JTabbedPane configOtherTabbedPane;

    private javax.swing.JPanel configOtherAppletPanel;

    private javax.swing.JPanel otherAppletPathPanel;

    private javax.swing.JLabel otherAppletPathLabel;

    private javax.swing.JTextField otherAppletPathTextField;

    private javax.swing.JButton otherAppletPathButton;

    private javax.swing.JPanel otherAppletBottomPanel;

    private javax.swing.JLabel otherAppletClassLabel;

    private javax.swing.JLabel otherAppletParmsLabel;

    private javax.swing.JTextField otherAppletClassTextField;

    private javax.swing.JTextField otherAppletParmsTextField;

    private javax.swing.JPanel configOtherStandalonePanel;

    private javax.swing.JPanel configOtherStandalonePathPanel;

    private javax.swing.JLabel configOtherStandalonePathLabel;

    private javax.swing.JTextField configOtherStandalonePathTextField;

    private javax.swing.JButton configOtherStandalonePathButton;

    private javax.swing.JPanel configOtherStandaloneParmsPanel;

    private javax.swing.JLabel configOtherStandaloneParmsLabel;

    private javax.swing.JTextField configOtherStandaloneParmsTextField;

    private javax.swing.JPanel configPluginOkPanel;

    private javax.swing.JButton configPluginOkButton;

    private javax.swing.JButton configPluginCancelButton;
}
