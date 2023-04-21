package mainview;

import controller.RMTController;
import model.retrieval.OnlineSource;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;

/**
 * Settings dialog allows the user to specify settings for the system
 * <p>
 * If the user clicks OK these settings are stored in the model, and the
 * system is notified that changes have been made to settings.
 *
 * @author  cbride
 */
public class SettingsDialog extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;

    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    /**
	 * Collection of dynamically created swing components for online sources
	 */
    private ArrayList<JCheckBox> onlineSourceCheckBoxes;

    private ArrayList<JSpinner> onlineSourceSpinners;

    /**
	 * Reference to the RMTController
	 */
    private RMTController controller;

    /** Creates new form SettingsDialog */
    public SettingsDialog(java.awt.Frame parent, boolean modal, RMTController controller) {
        super(parent, modal);
        this.controller = controller;
        initComponents();
        panelScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        createSearchSwingElements();
        settingsList.setSelectedIndex(0);
        setStorageModels();
        setVisualisationModels();
        setStorageLoadedValues();
        setSearchLoadedValues();
        setVisualisationLoadedValues();
    }

    /**
	 * Sets the loaded values for the storage settings
	 */
    public void setStorageLoadedValues() {
        Long interval = controller.getAutosaveInterval() / 1000 / 60;
        ((SpinnerNumberModel) saveIntervalSpinner.getModel()).setValue(interval.intValue());
        saveLocationTextField.setText(controller.getAutosaveLocation());
        saveEnabledCheckBox.setSelected(controller.getAutosaveOn());
    }

    /**
	 * Sets the loaded values for the search settings
	 */
    public void setSearchLoadedValues() {
        for (JCheckBox checkBox : onlineSourceCheckBoxes) {
            String sourceName = checkBox.getName().replaceAll("CheckBox", "");
            checkBox.setSelected(controller.getSourceEnabledSetting(sourceName));
        }
        for (JSpinner spinner : onlineSourceSpinners) {
            String sourceName = spinner.getName().replaceAll("Spinner", "");
            spinner.setValue(controller.getSourceResultsSetting(sourceName));
        }
        searchMaxResultsSpinner.setValue(controller.getGlobalMaxResults());
        searchSourceRadioButton.setSelected(controller.getMaxResultsMethodSetting());
        searchGlobalRadioButton.setSelected(!controller.getMaxResultsMethodSetting());
    }

    /**
	 * Sets the loaded values for the visualisation settings
	 */
    public void setVisualisationLoadedValues() {
        Color colours[] = controller.getVisualisationColourSettings();
        visualisationBackgroundPanel.setBackground(colours[0]);
        visualisationNodePanel.setBackground(colours[1]);
        visualisationCitationConnectionsPanel.setBackground(colours[2]);
        visualisationAssociationConnectionsPanel.setBackground(colours[3]);
        visualisationTimelinePanel.setBackground(colours[4]);
        Object fontNames[] = controller.getVisualisationFontSettings();
        visualisationArticleFontComboBox.setSelectedItem((String) fontNames[0]);
        visualisationTimelineFontComboBox.setSelectedItem((String) fontNames[1]);
        visualisationFontTitleSpinner.setValue((Integer) fontNames[2]);
        visualisationFontAuthorSpinner.setValue((Integer) fontNames[3]);
        visualisationFontYearSpinner.setValue((Integer) fontNames[4]);
        Object layoutValues[] = controller.getVisualisationLayoutSettings();
        visualisationMaximumNodeDistanceSpinner.setValue((Double) layoutValues[0]);
        visualisationTimescaleSpinner.setValue((Double) layoutValues[1]);
        visualisationDampingSpinner.setValue((Double) layoutValues[2]);
        visualisationRepulsionSpinner.setValue((Double) layoutValues[3]);
        visualisationCitationAttractionSpinner.setValue((Double) layoutValues[4]);
        visualisationAssociationAttractionSpinner.setValue((Double) layoutValues[5]);
        visualisationZoomDistanceSpinner.setValue(layoutValues[6]);
        visualisationColourDyeDepthSpinner.setValue(layoutValues[7]);
    }

    /**
	 * Creates the models for the storage settings GUI components
	 */
    public void setStorageModels() {
        saveIntervalSpinner.setModel(new SpinnerNumberModel(1, 1, 120, 1));
    }

    /**
	 * Creates the models for the visualisation settings GUI components
	 */
    public void setVisualisationModels() {
        visualisationMaximumNodeDistanceSpinner.setModel(new SpinnerNumberModel(50, 50, 500, 1.0));
        visualisationTimescaleSpinner.setModel(new SpinnerNumberModel(0.2, 0.2, 5, 0.1));
        visualisationDampingSpinner.setModel(new SpinnerNumberModel(0, 0, 1, 0.01));
        visualisationRepulsionSpinner.setModel(new SpinnerNumberModel(0, 0, 1, 0.1));
        visualisationCitationAttractionSpinner.setModel(new SpinnerNumberModel(0.001, 0, 1, 0.001));
        visualisationAssociationAttractionSpinner.setModel(new SpinnerNumberModel(0.001, 0, 1, 0.001));
        visualisationZoomDistanceSpinner.setModel(new SpinnerNumberModel(2, 2, 30, 1));
        visualisationColourDyeDepthSpinner.setModel(new SpinnerNumberModel(1, 1, 10, 1));
        String basicFonts[] = { "Monospaced", "Sans Serif", "Serif" };
        java.awt.GraphicsEnvironment ge = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts = ge.getAvailableFontFamilyNames();
        System.arraycopy(basicFonts, 0, fonts, 0, 3);
        visualisationArticleFontComboBox.setModel(new DefaultComboBoxModel(fonts));
        visualisationTimelineFontComboBox.setModel(new DefaultComboBoxModel(fonts));
        visualisationFontTitleSpinner.setModel(new SpinnerNumberModel(1, 1, 10, 1));
        visualisationFontAuthorSpinner.setModel(new SpinnerNumberModel(1, 1, 10, 1));
        visualisationFontYearSpinner.setModel(new SpinnerNumberModel(1, 1, 10, 1));
    }

    private void saveSettings() {
        Long interval = (Integer) saveIntervalSpinner.getValue() * 1000L * 60L;
        controller.saveStorageSettings(saveEnabledCheckBox.isSelected(), saveLocationTextField.getText(), interval);
        for (JCheckBox checkBox : onlineSourceCheckBoxes) {
            String sourceName = checkBox.getName().replaceAll("CheckBox", "");
            controller.saveSourceEnabledSetting(sourceName, checkBox.isSelected());
        }
        HashMap<String, Integer> sourcesResults = new HashMap<String, Integer>();
        for (JSpinner spinner : onlineSourceSpinners) {
            String sourceName = spinner.getName().replaceAll("Spinner", "");
            sourcesResults.put(sourceName, (Integer) spinner.getValue());
        }
        controller.saveSourceResultsSetting(sourcesResults);
        controller.saveMaxResultsMethodSetting(searchSourceRadioButton.isSelected());
        controller.setGlobalMaxResults((Integer) searchMaxResultsSpinner.getValue());
        controller.saveVisualisationColourSettings(visualisationBackgroundPanel.getBackground(), visualisationNodePanel.getBackground(), visualisationCitationConnectionsPanel.getBackground(), visualisationAssociationConnectionsPanel.getBackground(), visualisationTimelinePanel.getBackground());
        controller.saveVisualisationFontSettings((String) visualisationArticleFontComboBox.getSelectedItem(), (String) visualisationTimelineFontComboBox.getSelectedItem(), (Integer) visualisationFontTitleSpinner.getValue(), (Integer) visualisationFontAuthorSpinner.getValue(), (Integer) visualisationFontYearSpinner.getValue());
        controller.saveVisualisationLayoutSettings((Double) visualisationMaximumNodeDistanceSpinner.getValue(), (Double) visualisationTimescaleSpinner.getValue(), (Double) visualisationDampingSpinner.getValue(), (Double) visualisationRepulsionSpinner.getValue(), (Double) visualisationCitationAttractionSpinner.getValue(), (Double) visualisationAssociationAttractionSpinner.getValue(), (Integer) visualisationZoomDistanceSpinner.getValue(), (Integer) visualisationColourDyeDepthSpinner.getValue());
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    /** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        searchPanel = new javax.swing.JPanel();
        searchLabel = new javax.swing.JLabel();
        onlineSourcesPanel = new javax.swing.JPanel();
        searchResultsPanel = new javax.swing.JPanel();
        searchMethodLabel = new javax.swing.JLabel();
        searchSourceRadioButton = new javax.swing.JRadioButton();
        searchGlobalRadioButton = new javax.swing.JRadioButton();
        searchMaxResultsLabel = new javax.swing.JLabel();
        searchMaxResultsSpinner = new javax.swing.JSpinner();
        savePanel = new javax.swing.JPanel();
        saveLabel = new javax.swing.JLabel();
        automaticSavePanel = new javax.swing.JPanel();
        saveEnabledCheckBox = new javax.swing.JCheckBox();
        saveLocationLabel = new javax.swing.JLabel();
        saveLocationTextField = new javax.swing.JTextField();
        saveLocationButton = new javax.swing.JButton();
        saveIntervalLabel = new javax.swing.JLabel();
        saveIntervalSpinner = new javax.swing.JSpinner();
        saveIntervalLabel2 = new javax.swing.JLabel();
        visualisationPanel = new javax.swing.JPanel();
        visualisationLabel = new javax.swing.JLabel();
        visualisationColourPanel = new javax.swing.JPanel();
        visualisationBackgroundLabel = new javax.swing.JLabel();
        visualisationBackgroundPanel = new javax.swing.JPanel();
        visualisationNodeLabel = new javax.swing.JLabel();
        visualisationNodePanel = new javax.swing.JPanel();
        visualisationTimelineLabel = new javax.swing.JLabel();
        visualisationTimelinePanel = new javax.swing.JPanel();
        visualisationCitationConnectionsLabel = new javax.swing.JLabel();
        visualisationCitationConnectionsPanel = new javax.swing.JPanel();
        visualisationAssociationConnectionsLabel = new javax.swing.JLabel();
        visualisationAssociationConnectionsPanel = new javax.swing.JPanel();
        visualisationFontPanel = new javax.swing.JPanel();
        visualisationArticleFontLabel = new javax.swing.JLabel();
        visualisationArticleFontComboBox = new javax.swing.JComboBox();
        visualisationTimelineFontLabel = new javax.swing.JLabel();
        visualisationTimelineFontComboBox = new javax.swing.JComboBox();
        visualisationFontTitleLabel = new javax.swing.JLabel();
        visualisationFontTitleSpinner = new javax.swing.JSpinner();
        visualisationFontAuthorLabel = new javax.swing.JLabel();
        visualisationFontAuthorSpinner = new javax.swing.JSpinner();
        visualisationFontYearLabel = new javax.swing.JLabel();
        visualisationFontYearSpinner = new javax.swing.JSpinner();
        visualisationLayoutPanel = new javax.swing.JPanel();
        maximumNodeDistanceLabel = new javax.swing.JLabel();
        visualisationMaximumNodeDistanceSpinner = new javax.swing.JSpinner();
        visualisationTimescaleLabel = new javax.swing.JLabel();
        visualisationTimescaleSpinner = new javax.swing.JSpinner();
        visualisationDampingLabel = new javax.swing.JLabel();
        visualisationDampingSpinner = new javax.swing.JSpinner();
        visualisationRepulsionLabel = new javax.swing.JLabel();
        visualisationRepulsionSpinner = new javax.swing.JSpinner();
        visualisationCitationAttractionLabel = new javax.swing.JLabel();
        visualisationCitationAttractionSpinner = new javax.swing.JSpinner();
        visualisationAssociationAttractionLabel = new javax.swing.JLabel();
        visualisationAssociationAttractionSpinner = new javax.swing.JSpinner();
        visualisationZoomDistanceLabel = new javax.swing.JLabel();
        visualisationZoomDistanceSpinner = new javax.swing.JSpinner();
        visualisationColourDyeDepthLabel = new javax.swing.JLabel();
        visualisationColourDyeDepthSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        zoomMovementButtonGroup = new javax.swing.ButtonGroup();
        searchResultsButtonGroup = new javax.swing.ButtonGroup();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        splitPane = new javax.swing.JSplitPane();
        settingsListScrollPane = new javax.swing.JScrollPane();
        settingsList = new javax.swing.JList();
        panelScrollPane = new javax.swing.JScrollPane();
        searchPanel.setName("searchPanel");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(mainview.MainviewApp.class).getContext().getResourceMap(SettingsDialog.class);
        searchLabel.setFont(resourceMap.getFont("searchLabel.font"));
        searchLabel.setText(resourceMap.getString("searchLabel.text"));
        searchLabel.setName("searchLabel");
        onlineSourcesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("onlineSourcesPanel.border.title")));
        onlineSourcesPanel.setName("onlineSourcesPanel");
        javax.swing.GroupLayout onlineSourcesPanelLayout = new javax.swing.GroupLayout(onlineSourcesPanel);
        onlineSourcesPanel.setLayout(onlineSourcesPanelLayout);
        onlineSourcesPanelLayout.setHorizontalGroup(onlineSourcesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 377, Short.MAX_VALUE));
        onlineSourcesPanelLayout.setVerticalGroup(onlineSourcesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 0, Short.MAX_VALUE));
        searchResultsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("searchResultsPanel.border.title")));
        searchResultsPanel.setName("searchResultsPanel");
        searchMethodLabel.setText(resourceMap.getString("searchMethodLabel.text"));
        searchMethodLabel.setName("searchMethodLabel");
        searchResultsButtonGroup.add(searchSourceRadioButton);
        searchSourceRadioButton.setText(resourceMap.getString("searchSourceRadioButton.text"));
        searchSourceRadioButton.setName("searchSourceRadioButton");
        searchResultsButtonGroup.add(searchGlobalRadioButton);
        searchGlobalRadioButton.setText(resourceMap.getString("searchGlobalRadioButton.text"));
        searchGlobalRadioButton.setName("searchGlobalRadioButton");
        searchMaxResultsLabel.setText(resourceMap.getString("searchMaxResultsLabel.text"));
        searchMaxResultsLabel.setName("searchMaxResultsLabel");
        searchMaxResultsSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 1000, 1));
        searchMaxResultsSpinner.setName("searchMaxResultsSpinner");
        javax.swing.GroupLayout searchResultsPanelLayout = new javax.swing.GroupLayout(searchResultsPanel);
        searchResultsPanel.setLayout(searchResultsPanelLayout);
        searchResultsPanelLayout.setHorizontalGroup(searchResultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(searchResultsPanelLayout.createSequentialGroup().addContainerGap().addGroup(searchResultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(searchMethodLabel).addComponent(searchMaxResultsLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(searchResultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(searchGlobalRadioButton).addComponent(searchSourceRadioButton).addComponent(searchMaxResultsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        searchResultsPanelLayout.setVerticalGroup(searchResultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(searchResultsPanelLayout.createSequentialGroup().addGroup(searchResultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(searchMethodLabel).addComponent(searchSourceRadioButton)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(searchGlobalRadioButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(searchResultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(searchMaxResultsLabel).addComponent(searchMaxResultsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, searchPanelLayout.createSequentialGroup().addContainerGap().addComponent(searchLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)).addComponent(onlineSourcesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(searchResultsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        searchPanelLayout.setVerticalGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(searchPanelLayout.createSequentialGroup().addContainerGap().addComponent(searchLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(onlineSourcesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(searchResultsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        savePanel.setName("savePanel");
        saveLabel.setFont(resourceMap.getFont("saveLabel.font"));
        saveLabel.setText(resourceMap.getString("saveLabel.text"));
        saveLabel.setName("saveLabel");
        automaticSavePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("automaticSavePanel.border.title")));
        automaticSavePanel.setName("automaticSavePanel");
        saveEnabledCheckBox.setText(resourceMap.getString("saveEnabledCheckBox.text"));
        saveEnabledCheckBox.setName("saveEnabledCheckBox");
        saveLocationLabel.setText(resourceMap.getString("saveLocationLabel.text"));
        saveLocationLabel.setName("saveLocationLabel");
        saveLocationTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        saveLocationTextField.setText(resourceMap.getString("saveLocationTextField.text"));
        saveLocationTextField.setName("saveLocationTextField");
        saveLocationButton.setText(resourceMap.getString("saveLocationButton.text"));
        saveLocationButton.setName("saveLocationButton");
        saveLocationButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveLocationButtonActionPerformed(evt);
            }
        });
        saveIntervalLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        saveIntervalLabel.setText(resourceMap.getString("saveIntervalLabel.text"));
        saveIntervalLabel.setName("saveIntervalLabel");
        saveIntervalSpinner.setName("saveIntervalSpinner");
        saveIntervalLabel2.setText(resourceMap.getString("saveIntervalLabel2.text"));
        saveIntervalLabel2.setName("saveIntervalLabel2");
        javax.swing.GroupLayout automaticSavePanelLayout = new javax.swing.GroupLayout(automaticSavePanel);
        automaticSavePanel.setLayout(automaticSavePanelLayout);
        automaticSavePanelLayout.setHorizontalGroup(automaticSavePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(automaticSavePanelLayout.createSequentialGroup().addGroup(automaticSavePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(saveEnabledCheckBox).addGroup(automaticSavePanelLayout.createSequentialGroup().addGap(21, 21, 21).addGroup(automaticSavePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(saveIntervalLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(saveLocationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(automaticSavePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(automaticSavePanelLayout.createSequentialGroup().addComponent(saveLocationTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(saveLocationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(automaticSavePanelLayout.createSequentialGroup().addComponent(saveIntervalSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(saveIntervalLabel2))))).addContainerGap()));
        automaticSavePanelLayout.setVerticalGroup(automaticSavePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(automaticSavePanelLayout.createSequentialGroup().addComponent(saveEnabledCheckBox).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(automaticSavePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(saveLocationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(saveLocationButton).addComponent(saveLocationLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(automaticSavePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(saveIntervalSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(saveIntervalLabel2).addComponent(saveIntervalLabel)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        javax.swing.GroupLayout savePanelLayout = new javax.swing.GroupLayout(savePanel);
        savePanel.setLayout(savePanelLayout);
        savePanelLayout.setHorizontalGroup(savePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(automaticSavePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, savePanelLayout.createSequentialGroup().addContainerGap().addComponent(saveLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)));
        savePanelLayout.setVerticalGroup(savePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(savePanelLayout.createSequentialGroup().addContainerGap().addComponent(saveLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(automaticSavePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        visualisationPanel.setName("visualisationPanel");
        visualisationLabel.setFont(resourceMap.getFont("visualisationLabel.font"));
        visualisationLabel.setText(resourceMap.getString("visualisationLabel.text"));
        visualisationLabel.setName("visualisationLabel");
        visualisationColourPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("visualisationColourPanel.border.title")));
        visualisationColourPanel.setName("visualisationColourPanel");
        visualisationBackgroundLabel.setText(resourceMap.getString("visualisationBackgroundLabel.text"));
        visualisationBackgroundLabel.setName("visualisationBackgroundLabel");
        visualisationBackgroundPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        visualisationBackgroundPanel.setName("visualisationBackgroundPanel");
        visualisationBackgroundPanel.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                visualisationBackgroundPanelMouseReleased(evt);
            }
        });
        javax.swing.GroupLayout visualisationBackgroundPanelLayout = new javax.swing.GroupLayout(visualisationBackgroundPanel);
        visualisationBackgroundPanel.setLayout(visualisationBackgroundPanelLayout);
        visualisationBackgroundPanelLayout.setHorizontalGroup(visualisationBackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 20, Short.MAX_VALUE));
        visualisationBackgroundPanelLayout.setVerticalGroup(visualisationBackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 20, Short.MAX_VALUE));
        visualisationNodeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        visualisationNodeLabel.setText(resourceMap.getString("visualisationNodeLabel.text"));
        visualisationNodeLabel.setName("visualisationNodeLabel");
        visualisationNodePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        visualisationNodePanel.setName("visualisationNodePanel");
        visualisationNodePanel.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                visualisationNodePanelMouseReleased(evt);
            }
        });
        javax.swing.GroupLayout visualisationNodePanelLayout = new javax.swing.GroupLayout(visualisationNodePanel);
        visualisationNodePanel.setLayout(visualisationNodePanelLayout);
        visualisationNodePanelLayout.setHorizontalGroup(visualisationNodePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 20, Short.MAX_VALUE));
        visualisationNodePanelLayout.setVerticalGroup(visualisationNodePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 20, Short.MAX_VALUE));
        visualisationTimelineLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        visualisationTimelineLabel.setText(resourceMap.getString("visualisationTimelineLabel.text"));
        visualisationTimelineLabel.setName("visualisationTimelineLabel");
        visualisationTimelinePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        visualisationTimelinePanel.setName("visualisationTimelinePanel");
        visualisationTimelinePanel.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                visualisationTimelinePanelMouseReleased(evt);
            }
        });
        javax.swing.GroupLayout visualisationTimelinePanelLayout = new javax.swing.GroupLayout(visualisationTimelinePanel);
        visualisationTimelinePanel.setLayout(visualisationTimelinePanelLayout);
        visualisationTimelinePanelLayout.setHorizontalGroup(visualisationTimelinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 20, Short.MAX_VALUE));
        visualisationTimelinePanelLayout.setVerticalGroup(visualisationTimelinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 20, Short.MAX_VALUE));
        visualisationCitationConnectionsLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        visualisationCitationConnectionsLabel.setText(resourceMap.getString("visualisationCitationConnectionsLabel.text"));
        visualisationCitationConnectionsLabel.setMaximumSize(null);
        visualisationCitationConnectionsLabel.setMinimumSize(null);
        visualisationCitationConnectionsLabel.setName("visualisationCitationConnectionsLabel");
        visualisationCitationConnectionsLabel.setPreferredSize(null);
        visualisationCitationConnectionsLabel.setRequestFocusEnabled(false);
        visualisationCitationConnectionsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        visualisationCitationConnectionsPanel.setName("visualisationCitationConnectionsPanel");
        visualisationCitationConnectionsPanel.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                visualisationCitationConnectionsPanelMouseReleased(evt);
            }
        });
        javax.swing.GroupLayout visualisationCitationConnectionsPanelLayout = new javax.swing.GroupLayout(visualisationCitationConnectionsPanel);
        visualisationCitationConnectionsPanel.setLayout(visualisationCitationConnectionsPanelLayout);
        visualisationCitationConnectionsPanelLayout.setHorizontalGroup(visualisationCitationConnectionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 20, Short.MAX_VALUE));
        visualisationCitationConnectionsPanelLayout.setVerticalGroup(visualisationCitationConnectionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 20, Short.MAX_VALUE));
        visualisationAssociationConnectionsLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        visualisationAssociationConnectionsLabel.setText(resourceMap.getString("visualisationAssociationConnectionsLabel.text"));
        visualisationAssociationConnectionsLabel.setName("visualisationAssociationConnectionsLabel");
        visualisationAssociationConnectionsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        visualisationAssociationConnectionsPanel.setName("visualisationAssociationConnectionsPanel");
        visualisationAssociationConnectionsPanel.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                visualisationAssociationConnectionsPanelMouseReleased(evt);
            }
        });
        javax.swing.GroupLayout visualisationAssociationConnectionsPanelLayout = new javax.swing.GroupLayout(visualisationAssociationConnectionsPanel);
        visualisationAssociationConnectionsPanel.setLayout(visualisationAssociationConnectionsPanelLayout);
        visualisationAssociationConnectionsPanelLayout.setHorizontalGroup(visualisationAssociationConnectionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 20, Short.MAX_VALUE));
        visualisationAssociationConnectionsPanelLayout.setVerticalGroup(visualisationAssociationConnectionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 20, Short.MAX_VALUE));
        javax.swing.GroupLayout visualisationColourPanelLayout = new javax.swing.GroupLayout(visualisationColourPanel);
        visualisationColourPanel.setLayout(visualisationColourPanelLayout);
        visualisationColourPanelLayout.setHorizontalGroup(visualisationColourPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(visualisationColourPanelLayout.createSequentialGroup().addContainerGap().addGroup(visualisationColourPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(visualisationColourPanelLayout.createSequentialGroup().addComponent(visualisationBackgroundLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(visualisationBackgroundPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, visualisationColourPanelLayout.createSequentialGroup().addComponent(visualisationNodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(visualisationNodePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(visualisationColourPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(visualisationColourPanelLayout.createSequentialGroup().addGap(27, 27, 27).addComponent(visualisationCitationConnectionsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(visualisationCitationConnectionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(10, 10, 10).addComponent(visualisationTimelineLabel).addGap(4, 4, 4).addComponent(visualisationTimelinePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(visualisationColourPanelLayout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(visualisationAssociationConnectionsLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(visualisationAssociationConnectionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        visualisationColourPanelLayout.setVerticalGroup(visualisationColourPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(visualisationColourPanelLayout.createSequentialGroup().addGroup(visualisationColourPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(visualisationTimelinePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(visualisationColourPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(visualisationBackgroundLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(visualisationBackgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addComponent(visualisationTimelineLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE).addComponent(visualisationCitationConnectionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(visualisationCitationConnectionsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(visualisationColourPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(visualisationAssociationConnectionsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE).addGroup(visualisationColourPanelLayout.createSequentialGroup().addGroup(visualisationColourPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(visualisationNodeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(visualisationNodePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGap(1, 1, 1)).addComponent(visualisationAssociationConnectionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        visualisationFontPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("visualisationFontPanel.border.title")));
        visualisationFontPanel.setName("visualisationFontPanel");
        visualisationArticleFontLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        visualisationArticleFontLabel.setText(resourceMap.getString("visualisationArticleFontLabel.text"));
        visualisationArticleFontLabel.setName("visualisationArticleFontLabel");
        visualisationArticleFontComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        visualisationArticleFontComboBox.setName("visualisationArticleFontComboBox");
        visualisationTimelineFontLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        visualisationTimelineFontLabel.setText(resourceMap.getString("visualisationTimelineFontLabel.text"));
        visualisationTimelineFontLabel.setName("visualisationTimelineFontLabel");
        visualisationTimelineFontComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        visualisationTimelineFontComboBox.setName("visualisationTimelineFontComboBox");
        visualisationFontTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        visualisationFontTitleLabel.setText(resourceMap.getString("visualisationFontTitleLabel.text"));
        visualisationFontTitleLabel.setName("visualisationFontTitleLabel");
        visualisationFontTitleSpinner.setName("visualisationFontTitleSpinner");
        visualisationFontAuthorLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        visualisationFontAuthorLabel.setText(resourceMap.getString("visualisationFontAuthorLabel.text"));
        visualisationFontAuthorLabel.setName("visualisationFontAuthorLabel");
        visualisationFontAuthorSpinner.setName("visualisationFontAuthorSpinner");
        visualisationFontYearLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        visualisationFontYearLabel.setText(resourceMap.getString("visualisationFontYearLabel.text"));
        visualisationFontYearLabel.setName("visualisationFontYearLabel");
        visualisationFontYearSpinner.setName("visualisationFontYearSpinner");
        javax.swing.GroupLayout visualisationFontPanelLayout = new javax.swing.GroupLayout(visualisationFontPanel);
        visualisationFontPanel.setLayout(visualisationFontPanelLayout);
        visualisationFontPanelLayout.setHorizontalGroup(visualisationFontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(visualisationFontPanelLayout.createSequentialGroup().addContainerGap().addGroup(visualisationFontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(visualisationFontYearLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(visualisationArticleFontLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(visualisationTimelineFontLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(visualisationFontTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(visualisationFontAuthorLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(visualisationFontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(visualisationFontYearSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(visualisationFontAuthorSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(visualisationFontTitleSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(visualisationTimelineFontComboBox, 0, 207, Short.MAX_VALUE).addComponent(visualisationArticleFontComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(33, Short.MAX_VALUE)));
        visualisationFontPanelLayout.setVerticalGroup(visualisationFontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(visualisationFontPanelLayout.createSequentialGroup().addGroup(visualisationFontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(visualisationArticleFontComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(visualisationArticleFontLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(visualisationFontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(visualisationTimelineFontComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(visualisationTimelineFontLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(visualisationFontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(visualisationFontTitleSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(visualisationFontTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(visualisationFontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(visualisationFontAuthorSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(visualisationFontAuthorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(visualisationFontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(visualisationFontYearSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(visualisationFontYearLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        visualisationLayoutPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("visualisationLayoutPanel.border.title")));
        visualisationLayoutPanel.setName("visualisationLayoutPanel");
        maximumNodeDistanceLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        maximumNodeDistanceLabel.setText(resourceMap.getString("maximumNodeDistanceLabel.text"));
        maximumNodeDistanceLabel.setName("maximumNodeDistanceLabel");
        visualisationMaximumNodeDistanceSpinner.setName("visualisationMaximumNodeDistanceSpinner");
        visualisationTimescaleLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        visualisationTimescaleLabel.setText(resourceMap.getString("visualisationTimescaleLabel.text"));
        visualisationTimescaleLabel.setName("visualisationTimescaleLabel");
        visualisationTimescaleSpinner.setName("visualisationTimescaleSpinner");
        visualisationDampingLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        visualisationDampingLabel.setText(resourceMap.getString("visualisationDampingLabel.text"));
        visualisationDampingLabel.setName("visualisationDampingLabel");
        visualisationDampingSpinner.setName("visualisationDampingSpinner");
        visualisationRepulsionLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        visualisationRepulsionLabel.setText(resourceMap.getString("visualisationRepulsionLabel.text"));
        visualisationRepulsionLabel.setName("visualisationRepulsionLabel");
        visualisationRepulsionSpinner.setName("visualisationRepulsionSpinner");
        visualisationCitationAttractionLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        visualisationCitationAttractionLabel.setText(resourceMap.getString("visualisationCitationAttractionLabel.text"));
        visualisationCitationAttractionLabel.setName("visualisationCitationAttractionLabel");
        visualisationCitationAttractionSpinner.setName("visualisationCitationAttractionSpinner");
        visualisationAssociationAttractionLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        visualisationAssociationAttractionLabel.setText(resourceMap.getString("visualisationAssociationAttractionLabel.text"));
        visualisationAssociationAttractionLabel.setName("visualisationAssociationAttractionLabel");
        visualisationAssociationAttractionSpinner.setName("visualisationAssociationAttractionSpinner");
        visualisationZoomDistanceLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        visualisationZoomDistanceLabel.setText(resourceMap.getString("visualisationZoomDistanceLabel.text"));
        visualisationZoomDistanceLabel.setName("visualisationZoomDistanceLabel");
        visualisationZoomDistanceSpinner.setName("visualisationZoomDistanceSpinner");
        visualisationColourDyeDepthLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        visualisationColourDyeDepthLabel.setText(resourceMap.getString("visualisationColourDyeDepthLabel.text"));
        visualisationColourDyeDepthLabel.setName("visualisationColourDyeDepthLabel");
        visualisationColourDyeDepthSpinner.setName("visualisationColourDyeDepthSpinner");
        javax.swing.GroupLayout visualisationLayoutPanelLayout = new javax.swing.GroupLayout(visualisationLayoutPanel);
        visualisationLayoutPanel.setLayout(visualisationLayoutPanelLayout);
        visualisationLayoutPanelLayout.setHorizontalGroup(visualisationLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(visualisationLayoutPanelLayout.createSequentialGroup().addContainerGap().addGroup(visualisationLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(maximumNodeDistanceLabel).addGroup(visualisationLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(visualisationZoomDistanceLabel).addComponent(visualisationRepulsionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(visualisationCitationAttractionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(visualisationAssociationAttractionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(visualisationDampingLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addComponent(visualisationTimescaleLabel).addComponent(visualisationColourDyeDepthLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(visualisationLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(visualisationColourDyeDepthSpinner, javax.swing.GroupLayout.Alignment.LEADING).addComponent(visualisationMaximumNodeDistanceSpinner, javax.swing.GroupLayout.Alignment.LEADING).addComponent(visualisationZoomDistanceSpinner, javax.swing.GroupLayout.Alignment.LEADING).addComponent(visualisationDampingSpinner, javax.swing.GroupLayout.Alignment.LEADING).addComponent(visualisationTimescaleSpinner, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE).addComponent(visualisationRepulsionSpinner, javax.swing.GroupLayout.Alignment.LEADING).addComponent(visualisationCitationAttractionSpinner, javax.swing.GroupLayout.Alignment.LEADING).addComponent(visualisationAssociationAttractionSpinner, javax.swing.GroupLayout.Alignment.LEADING)).addContainerGap(145, Short.MAX_VALUE)));
        visualisationLayoutPanelLayout.setVerticalGroup(visualisationLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(visualisationLayoutPanelLayout.createSequentialGroup().addContainerGap().addGroup(visualisationLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(maximumNodeDistanceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(visualisationMaximumNodeDistanceSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(visualisationLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(visualisationTimescaleSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(visualisationTimescaleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(visualisationLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(visualisationDampingSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(visualisationDampingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(visualisationLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(visualisationRepulsionSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(visualisationRepulsionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(visualisationLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(visualisationCitationAttractionSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(visualisationCitationAttractionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(visualisationLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(visualisationAssociationAttractionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(visualisationAssociationAttractionSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(visualisationLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(visualisationZoomDistanceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(visualisationZoomDistanceSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(visualisationLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(visualisationColourDyeDepthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(visualisationColourDyeDepthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        javax.swing.GroupLayout visualisationPanelLayout = new javax.swing.GroupLayout(visualisationPanel);
        visualisationPanel.setLayout(visualisationPanelLayout);
        visualisationPanelLayout.setHorizontalGroup(visualisationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(visualisationPanelLayout.createSequentialGroup().addContainerGap().addComponent(visualisationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE).addContainerGap()).addComponent(visualisationColourPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE).addComponent(visualisationFontPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(visualisationLayoutPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(visualisationPanelLayout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        visualisationPanelLayout.setVerticalGroup(visualisationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(visualisationPanelLayout.createSequentialGroup().addContainerGap().addComponent(visualisationLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(visualisationColourPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(visualisationFontPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(visualisationLayoutPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel1).addContainerGap(14, Short.MAX_VALUE)));
        setTitle(resourceMap.getString("Form.title"));
        setIconImage(resourceMap.getImageIcon("Dialog.icon").getImage());
        setMinimumSize(new java.awt.Dimension(400, 400));
        setName("Form");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        okButton.setText(resourceMap.getString("okButton.text"));
        okButton.setName("okButton");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        cancelButton.setText(resourceMap.getString("cancelButton.text"));
        cancelButton.setName("cancelButton");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        splitPane.setDividerLocation(120);
        splitPane.setMinimumSize(new java.awt.Dimension(120, 0));
        splitPane.setName("splitPane");
        splitPane.setPreferredSize(new java.awt.Dimension(150, 0));
        settingsListScrollPane.setName("settingsListScrollPane");
        settingsList.setModel(new javax.swing.AbstractListModel() {

            String[] strings = { "Saving", "Searching", "Visualisation" };

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        settingsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        settingsList.setName("settingsList");
        settingsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                settingsListValueChanged(evt);
            }
        });
        settingsListScrollPane.setViewportView(settingsList);
        splitPane.setLeftComponent(settingsListScrollPane);
        panelScrollPane.setName("panelScrollPane");
        splitPane.setRightComponent(panelScrollPane);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap(350, Short.MAX_VALUE).addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton).addContainerGap()).addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE));
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { cancelButton, okButton });
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cancelButton).addComponent(okButton)).addContainerGap()));
        pack();
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        saveSettings();
        doClose(RET_OK);
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        doClose(RET_CANCEL);
    }

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        doClose(RET_CANCEL);
    }

    private void settingsListValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (settingsList.getSelectedValue().equals("Saving")) {
            panelScrollPane.setViewportView(savePanel);
        } else if (settingsList.getSelectedValue().equals("Searching")) {
            panelScrollPane.setViewportView(searchPanel);
        } else if (settingsList.getSelectedValue().equals("Visualisation")) {
            panelScrollPane.setViewportView(visualisationPanel);
        }
    }

    private void visualisationBackgroundPanelMouseReleased(java.awt.event.MouseEvent evt) {
        visualisationBackgroundPanel.setBackground(launchColourPicker(visualisationBackgroundPanel.getBackground()));
    }

    private void visualisationNodePanelMouseReleased(java.awt.event.MouseEvent evt) {
        visualisationNodePanel.setBackground(launchColourPicker(visualisationNodePanel.getBackground()));
    }

    private void visualisationCitationConnectionsPanelMouseReleased(java.awt.event.MouseEvent evt) {
        visualisationCitationConnectionsPanel.setBackground(launchColourPicker(visualisationCitationConnectionsPanel.getBackground()));
    }

    private void visualisationAssociationConnectionsPanelMouseReleased(java.awt.event.MouseEvent evt) {
        visualisationAssociationConnectionsPanel.setBackground(launchColourPicker(visualisationAssociationConnectionsPanel.getBackground()));
    }

    private void visualisationTimelinePanelMouseReleased(java.awt.event.MouseEvent evt) {
        visualisationTimelinePanel.setBackground(launchColourPicker(visualisationTimelinePanel.getBackground()));
    }

    /**
	 * Displays a file chooser which allows the user to select a directory to save
	 * the automatic save file to. If the user has selected a directory in the past then
	 * the file chooser is opened at this location. 
	 */
    private void saveLocationButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new RmtFileFilter());
        fileChooser.setDialogTitle("Choose Save File");
        File currentLocation = null;
        if (!saveLocationTextField.getText().isEmpty()) {
            currentLocation = new File(saveLocationTextField.getText());
            fileChooser.setSelectedFile(currentLocation);
        }
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getParent();
            String fileName = selectedFile.getName();
            if (!fileName.endsWith(".xrm")) {
                fileName = fileName + ".xrm";
            }
            saveLocationTextField.setText(path + "\\" + fileName);
        }
    }

    /**
	 * Launches a  colour chooser with <code>defaultColour</code> selected as 
	 * the current colour and returns the colour selected by the user.
	 * <p>
	 * @param defaultColour colour to set as current in the JColorChooser
	 * @return colour selected by user
	 */
    private Color launchColourPicker(Color defaultColour) {
        return JColorChooser.showDialog(null, "Choose Colour", defaultColour);
    }

    /**
	 * Create swing elements for each metadata field for the given artifact type.
	 * Swing elements are stored in an Array list of artifactSwingElements type
	 * and added to the dialog box.
	 * 
	 * The method is called multiple times to create the metadata fields in different
	 * panels (which are given a border and title). As all the metadata fields,
	 * no matter what panel they are in, are stored in the collection swingElements
	 * the index of the collection is returned so the next time we call the method
	 * we can create the next item.
	 */
    private void createSearchSwingElements() {
        HashMap<String, OnlineSource> sources = controller.getAllSources();
        onlineSourceCheckBoxes = new ArrayList<JCheckBox>();
        onlineSourceSpinners = new ArrayList<JSpinner>();
        GroupLayout layout = new GroupLayout(onlineSourcesPanel);
        onlineSourcesPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        GroupLayout.ParallelGroup parallelGroupLabel = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);
        GroupLayout.ParallelGroup parallelGroupCheckBoxes = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        GroupLayout.ParallelGroup parallelGroupSpinners = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        JLabel enabledLabel = new JLabel("Enabled:");
        JLabel resultsLabel = new JLabel("Maximum Results:");
        parallelGroupCheckBoxes.addComponent(enabledLabel);
        parallelGroupSpinners.addComponent(resultsLabel);
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(enabledLabel).addComponent(resultsLabel));
        for (Map.Entry<String, OnlineSource> source : sources.entrySet()) {
            String sourceName = source.getKey();
            JLabel label = new JLabel();
            label.setText(sourceName);
            label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            label.setName(sourceName + "Label");
            label.setMinimumSize(new java.awt.Dimension(60, 0));
            JCheckBox checkBox = new JCheckBox();
            checkBox.setName(sourceName + "CheckBox");
            onlineSourceCheckBoxes.add(checkBox);
            JSpinner spinner = new JSpinner();
            spinner.setName(sourceName + "Spinner");
            spinner.setModel(new SpinnerNumberModel(0, 0, 200, 1));
            onlineSourceSpinners.add(spinner);
            parallelGroupLabel.addComponent(label);
            parallelGroupCheckBoxes.addComponent(checkBox);
            parallelGroupSpinners.addComponent(spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE);
            vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(checkBox, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(spinner, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        }
        hGroup.addGroup(parallelGroupLabel);
        hGroup.addGroup(parallelGroupCheckBoxes);
        hGroup.addGroup(parallelGroupSpinners);
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);
        pack();
    }

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    private javax.swing.JPanel automaticSavePanel;

    private javax.swing.JButton cancelButton;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel maximumNodeDistanceLabel;

    private javax.swing.JButton okButton;

    private javax.swing.JPanel onlineSourcesPanel;

    private javax.swing.JScrollPane panelScrollPane;

    private javax.swing.JCheckBox saveEnabledCheckBox;

    private javax.swing.JLabel saveIntervalLabel;

    private javax.swing.JLabel saveIntervalLabel2;

    private javax.swing.JSpinner saveIntervalSpinner;

    private javax.swing.JLabel saveLabel;

    private javax.swing.JButton saveLocationButton;

    private javax.swing.JLabel saveLocationLabel;

    private javax.swing.JTextField saveLocationTextField;

    private javax.swing.JPanel savePanel;

    private javax.swing.JRadioButton searchGlobalRadioButton;

    private javax.swing.JLabel searchLabel;

    private javax.swing.JLabel searchMaxResultsLabel;

    private javax.swing.JSpinner searchMaxResultsSpinner;

    private javax.swing.JLabel searchMethodLabel;

    private javax.swing.JPanel searchPanel;

    private javax.swing.ButtonGroup searchResultsButtonGroup;

    private javax.swing.JPanel searchResultsPanel;

    private javax.swing.JRadioButton searchSourceRadioButton;

    private javax.swing.JList settingsList;

    private javax.swing.JScrollPane settingsListScrollPane;

    private javax.swing.JSplitPane splitPane;

    private javax.swing.JComboBox visualisationArticleFontComboBox;

    private javax.swing.JLabel visualisationArticleFontLabel;

    private javax.swing.JLabel visualisationAssociationAttractionLabel;

    private javax.swing.JSpinner visualisationAssociationAttractionSpinner;

    private javax.swing.JLabel visualisationAssociationConnectionsLabel;

    private javax.swing.JPanel visualisationAssociationConnectionsPanel;

    private javax.swing.JLabel visualisationBackgroundLabel;

    private javax.swing.JPanel visualisationBackgroundPanel;

    private javax.swing.JLabel visualisationCitationAttractionLabel;

    private javax.swing.JSpinner visualisationCitationAttractionSpinner;

    private javax.swing.JLabel visualisationCitationConnectionsLabel;

    private javax.swing.JPanel visualisationCitationConnectionsPanel;

    private javax.swing.JLabel visualisationColourDyeDepthLabel;

    private javax.swing.JSpinner visualisationColourDyeDepthSpinner;

    private javax.swing.JPanel visualisationColourPanel;

    private javax.swing.JLabel visualisationDampingLabel;

    private javax.swing.JSpinner visualisationDampingSpinner;

    private javax.swing.JLabel visualisationFontAuthorLabel;

    private javax.swing.JSpinner visualisationFontAuthorSpinner;

    private javax.swing.JPanel visualisationFontPanel;

    private javax.swing.JLabel visualisationFontTitleLabel;

    private javax.swing.JSpinner visualisationFontTitleSpinner;

    private javax.swing.JLabel visualisationFontYearLabel;

    private javax.swing.JSpinner visualisationFontYearSpinner;

    private javax.swing.JLabel visualisationLabel;

    private javax.swing.JPanel visualisationLayoutPanel;

    private javax.swing.JSpinner visualisationMaximumNodeDistanceSpinner;

    private javax.swing.JLabel visualisationNodeLabel;

    private javax.swing.JPanel visualisationNodePanel;

    private javax.swing.JPanel visualisationPanel;

    private javax.swing.JLabel visualisationRepulsionLabel;

    private javax.swing.JSpinner visualisationRepulsionSpinner;

    private javax.swing.JComboBox visualisationTimelineFontComboBox;

    private javax.swing.JLabel visualisationTimelineFontLabel;

    private javax.swing.JLabel visualisationTimelineLabel;

    private javax.swing.JPanel visualisationTimelinePanel;

    private javax.swing.JLabel visualisationTimescaleLabel;

    private javax.swing.JSpinner visualisationTimescaleSpinner;

    private javax.swing.JLabel visualisationZoomDistanceLabel;

    private javax.swing.JSpinner visualisationZoomDistanceSpinner;

    private javax.swing.ButtonGroup zoomMovementButtonGroup;

    private int returnStatus = RET_CANCEL;
}
