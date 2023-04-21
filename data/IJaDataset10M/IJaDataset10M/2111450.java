package pierre.configurationTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.net.URL;
import java.net.MalformedURLException;
import pedro.util.SystemLog;
import pedro.util.Parameter;
import pierre.system.PierreResources;
import pierre.model.*;
import pierre.util.*;
import pierre.configurationTool.comments.CommentsManager;
import pierre.util.HelpAndCommentsPanel;

/**
*
* Copyright (c) 2005 University of Manchester.
* @author Kevin Garwood (garwood@cs.man.ac.uk)
* @version 1.0
*
*/
public class DatabaseViewPanel extends JPanel implements ActionListener {

    private CommentsManager commentsManager;

    private DatabaseModel databaseModel;

    private JTextArea instructions;

    private JTextField databaseClassField;

    private ClassPathDependenciesPanel databaseDependenciesPanel;

    private ParameterListPanel parameterListPanel;

    private JButton loadDefaultModel;

    private Font plainFont;

    public DatabaseViewPanel() {
        setLayout(new GridBagLayout());
        Font normalFont = (new JLabel()).getFont();
        plainFont = normalFont.deriveFont(Font.PLAIN);
        GridBagConstraints panelGC = new GridBagConstraints();
        panelGC.gridx = 0;
        panelGC.gridy = 0;
        panelGC.weightx = 0;
        panelGC.anchor = GridBagConstraints.NORTHWEST;
        panelGC.fill = GridBagConstraints.NONE;
        add(createTopButtonPanel(), panelGC);
        panelGC.gridy++;
        panelGC.weightx = 100;
        panelGC.fill = GridBagConstraints.HORIZONTAL;
        add(createDatabaseFieldPanel(), panelGC);
        panelGC.gridy++;
        panelGC.fill = GridBagConstraints.BOTH;
        panelGC.weighty = 50;
        parameterListPanel = new ParameterListPanel();
        add(parameterListPanel, panelGC);
        panelGC.gridy++;
        panelGC.fill = GridBagConstraints.BOTH;
        panelGC.weighty = 50;
        add(createLibrarySelectionPanel(), panelGC);
        panelGC.anchor = GridBagConstraints.SOUTHWEST;
        panelGC.fill = GridBagConstraints.HORIZONTAL;
        panelGC.weightx = 100;
        panelGC.weighty = 0;
        panelGC.gridy++;
        commentsManager = new CommentsManager();
        HelpAndCommentsPanel helpAndCommentsPanel = new HelpAndCommentsPanel(commentsManager.getCommentsButton());
        add(helpAndCommentsPanel, panelGC);
    }

    private JPanel createTopButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints panelGC = new GridBagConstraints();
        panelGC.gridx = 0;
        panelGC.gridy = 0;
        panelGC.weightx = 0;
        panelGC.anchor = GridBagConstraints.NORTHWEST;
        panelGC.fill = GridBagConstraints.NONE;
        String loadDefaultModelText = PierreResources.getMessage("pierre.database.loadDefaultModel");
        loadDefaultModel = new JButton(loadDefaultModelText);
        loadDefaultModel.setFont(plainFont);
        loadDefaultModel.addActionListener(this);
        panel.add(loadDefaultModel, panelGC);
        return panel;
    }

    private JPanel createDatabaseFieldPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints panelGC = new GridBagConstraints();
        panelGC.gridx = 0;
        panelGC.gridy = 0;
        panelGC.weightx = 0;
        panelGC.anchor = GridBagConstraints.NORTHWEST;
        panelGC.fill = GridBagConstraints.NONE;
        String databaseClassText = PierreResources.getMessage("pierre.database.class");
        JLabel databaseClassLabel = new JLabel(databaseClassText);
        databaseClassLabel.setFont(plainFont);
        panel.add(databaseClassLabel, panelGC);
        panelGC.gridx++;
        panelGC.fill = GridBagConstraints.HORIZONTAL;
        panelGC.weightx = 100;
        databaseClassField = new JTextField(30);
        panel.add(databaseClassField, panelGC);
        return panel;
    }

    private JPanel createLibrarySelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints panelGC = new GridBagConstraints();
        panelGC.gridx = 0;
        panelGC.gridy = 0;
        panelGC.weightx = 100;
        panelGC.anchor = GridBagConstraints.NORTHWEST;
        panelGC.fill = GridBagConstraints.HORIZONTAL;
        JTextArea instructions = new JTextArea(5, 30);
        instructions.setEditable(false);
        instructions.setBackground(panel.getBackground());
        instructions.setLineWrap(true);
        instructions.setWrapStyleWord(true);
        String instructionsText = PierreResources.getMessage("pierre.database.libraryInstructions");
        instructions.setText(instructionsText);
        JScrollPane scrollPane = new JScrollPane(instructions);
        panel.add(scrollPane, panelGC);
        panelGC.gridy++;
        databaseDependenciesPanel = new ClassPathDependenciesPanel();
        panelGC.fill = GridBagConstraints.BOTH;
        panelGC.weightx = 100;
        panelGC.weighty = 100;
        panel.add(databaseDependenciesPanel, panelGC);
        return panel;
    }

    public DatabaseModel getDatabaseModel() {
        return databaseModel;
    }

    public boolean isDirty() {
        boolean isDirty = false;
        String currentClassName = databaseClassField.getText().trim();
        String savedClassName = databaseModel.getDatabaseClassName();
        if (savedClassName == null) {
            if (currentClassName.equals("") == false) {
                isDirty = true;
            }
        }
        if (savedClassName != null) {
            if (currentClassName.equals(savedClassName) == false) {
                if (isDirty != true) {
                    isDirty = true;
                }
            }
        }
        if (parameterListPanel.isDirty() == true) {
            if (isDirty != true) {
                isDirty = true;
            }
        }
        if (databaseDependenciesPanel.isDirty() == true) {
            if (isDirty != true) {
                isDirty = true;
            }
        }
        if (commentsManager.isDirty() == true) {
            return true;
        }
        return isDirty;
    }

    public void setDatabaseModel(DatabaseModel databaseModel) {
        this.databaseModel = databaseModel;
        commentsManager.setCommentProvider(databaseModel);
        String databaseClassName = databaseModel.getDatabaseClassName();
        if (databaseClassName == null) {
            databaseClassField.setText("");
        } else {
            databaseClassField.setText(databaseClassName);
        }
        parameterListPanel.setParameters(databaseModel.getParameters());
        String[] dependencies = databaseModel.getFileNameDependencies();
        databaseDependenciesPanel.setDependencies(dependencies);
    }

    public void save() {
        try {
            ClassLoaderUtility classLoaderUtility = new ClassLoaderUtility();
            classLoaderUtility.addURLs(databaseDependenciesPanel.getDependencies());
            String className = databaseClassField.getText().trim();
            if (className.equals("") == true) {
                databaseModel.setDatabaseClassName(null);
            } else {
                databaseModel.setDatabaseClassName(className);
            }
            String[] dependencies = databaseDependenciesPanel.getDependencies();
            databaseModel.setDependencies(dependencies);
            databaseDependenciesPanel.setDirty(false);
            Parameter[] parameters = parameterListPanel.getParameters();
            databaseModel.setParameters(parameters);
            parameterListPanel.setDirty(false);
            commentsManager.save();
        } catch (Exception err) {
            SystemLog.addError(err);
        }
    }

    public String validateForm() {
        return null;
    }

    public void actionPerformed(ActionEvent event) {
        DummyDatabaseModel dummyDatabaseModel = new DummyDatabaseModel();
        setDatabaseModel(dummyDatabaseModel);
        parameterListPanel.setDirty(true);
        databaseDependenciesPanel.setDirty(true);
    }
}
