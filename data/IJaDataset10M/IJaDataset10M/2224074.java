package com.qasystems.qstudio.java.gui.observation;

import com.qasystems.international.MessageResource;
import com.qasystems.qstudio.java.model.QualityAttributeModel;
import com.qasystems.qstudio.java.model.QualityAttributeModelEvent;
import com.qasystems.qstudio.java.model.QualityAttributeModelListener;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * This class creates a gui dialog to configure a quality attributes filter
 * (used to filter Observations in the observation table on impact level,
 * quality attribute or quality subattribute)
 */
public class AttributeSelectorView implements QualityAttributeModelListener {

    private static final MessageResource RESOURCES = MessageResource.getClientInstance();

    private QualityAttributeModel model = null;

    private AttributeAction[] actions = null;

    private Action selectAll;

    private Action selectUser;

    private JPanel panel = null;

    private JPanel selectionModePanel = null;

    private JPanel selectorPanel = null;

    private JScrollPane selectorPane = null;

    private JLabel selectionLabel = new JLabel(RESOURCES.getString("LABEL_033"));

    private JRadioButton btnAll;

    private JRadioButton btnUser;

    private ButtonGroup btnGroup = null;

    private JCheckBox[] selectors = null;

    /**
   * The constructor to use
   * @param qualityAttributeModel The QualityAttributeModel to configure
   * @param attributeActions Action objects used for creating checkboxes
   */
    public AttributeSelectorView(QualityAttributeModel qualityAttributeModel, AttributeAction[] attributeActions) {
        super();
        model = qualityAttributeModel;
        model.addQualityAttributeModelListener(this);
        actions = attributeActions;
        createActions();
        createGui();
        synchronizeGuiWithModel();
    }

    /**
   * Gets the gui panel
   * @return The panel
   */
    public JPanel getPanel() {
        return (panel);
    }

    /**
   * Creates the Action objects for the radiobuttons
   */
    private void createActions() {
        selectAll = new AbstractAction(RESOURCES.getString("BUTTON_017")) {

            public void actionPerformed(ActionEvent event) {
                model.setSelectionMode(QualityAttributeModel.SELECTION_MODE_ALL);
            }
        };
        selectUser = new AbstractAction(RESOURCES.getString("BUTTON_018")) {

            public void actionPerformed(ActionEvent event) {
                model.setSelectionMode(QualityAttributeModel.SELECTION_MODE_USER);
            }
        };
    }

    /**
   * Implements the QualityAttributeModelListener interface
   * @param event The event.
   */
    public void qualityAttributeModelChanged(QualityAttributeModelEvent event) {
        synchronizeGuiWithModel();
    }

    /**
   * Updates the gui objects with the state of the QualityAttributeModel
   */
    private void synchronizeGuiWithModel() {
        if (model.getSelectionMode() == QualityAttributeModel.SELECTION_MODE_ALL) {
            btnAll.setSelected(true);
            setSelectorsEnabled(false);
        }
        if (model.getSelectionMode() == QualityAttributeModel.SELECTION_MODE_USER) {
            btnUser.setSelected(true);
            setSelectorsEnabled(true);
        }
    }

    /**
   * Enables or disables the selection checkboxes
   * @param enabled The enabled state
   */
    private void setSelectorsEnabled(boolean enabled) {
        for (int i = 0; i < selectors.length; i++) {
            selectors[i].setEnabled(enabled);
        }
    }

    /**
   * Create the gui.
   */
    private void createGui() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        selectionModePanel = new JPanel();
        selectionModePanel.add(selectionLabel);
        btnAll = new JRadioButton(selectAll);
        selectionModePanel.add(btnAll);
        btnUser = new JRadioButton(selectUser);
        selectionModePanel.add(btnUser);
        btnGroup = new ButtonGroup();
        btnGroup.add(btnAll);
        btnGroup.add(btnUser);
        panel.add(selectionModePanel, BorderLayout.WEST);
        selectorPanel = new JPanel();
        selectorPanel.setLayout(new BoxLayout(selectorPanel, BoxLayout.Y_AXIS));
        selectors = new JCheckBox[actions.length];
        for (int i = 0; i < actions.length; i++) {
            selectors[i] = new JCheckBox(actions[i]);
            actions[i].addStateButton(selectors[i]);
            selectors[i].setSelected(actions[i].getState());
            selectorPanel.add(selectors[i]);
        }
        selectorPane = new JScrollPane(selectorPanel);
        panel.add(selectorPane, BorderLayout.CENTER);
    }
}
