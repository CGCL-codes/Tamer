package animawork;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import anima.dcc.AnimaFeature;
import anima.dcc.AnimaMessageStructure;
import anima.dcc.AnimaObjectElement;
import anima.dcc.EventType;
import anima.dcc.MultiLangString;
import anima.dcc.OperationType;
import anima.dcc.PropertyType;
import anima.info.InfoDataNode;

/**
 * Title:        Anima Assistant
 * Description:  Assistant for building Anima model files.
 * @author Andr� Santanch�
 * @version 1.0
 *
 *
 */
public class PanelElement extends JPanel {

    InfoDataNode it;

    String workshopVisibility[], animaVisibility[];

    String workshopType[], animaType[];

    String workshopCategory[], animaCategory[];

    AnimaObjectElement lastElement = null;

    ButtonGroup visibilityGroup = new ButtonGroup();

    JLabel labelId = new JLabel();

    JTextField fieldId = new JTextField();

    JLabel labelTitle = new JLabel();

    JTextField fieldTitle = new JTextField();

    JLabel labelDescription = new JLabel();

    JTextField fieldDescription = new JTextField();

    JLabel labelVisibility = new JLabel();

    JComboBox fieldVisibility;

    JLabel labelType = new JLabel();

    JComboBox fieldType;

    JLabel labelInitial = new JLabel();

    JTextField fieldInitial = new JTextField();

    JLabel changeableLabel = new JLabel();

    JCheckBox verifyChangeable = new JCheckBox();

    JLabel labelCategory = new JLabel();

    JComboBox fieldCategory;

    JLabel labelLabel = new JLabel();

    JTextField fieldLabel = new JTextField();

    JPanel inputPanel = new JPanel();

    GridLayout gridInput = new GridLayout();

    public PanelElement(InfoDataNode it, String workshopVisibility[], String animaVisibility[], String workshopType[], String animaType[], String workshopCategory[], String animaCategory[]) {
        this.it = it;
        this.workshopVisibility = workshopVisibility;
        this.animaVisibility = animaVisibility;
        this.workshopType = workshopType;
        this.animaType = animaType;
        this.workshopCategory = workshopCategory;
        this.animaCategory = animaCategory;
        fieldType = new JComboBox(workshopType);
        fieldVisibility = new JComboBox(workshopVisibility);
        fieldCategory = new JComboBox(workshopCategory);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setBackground(Constant.defaultColor);
        this.setPreferredSize(new Dimension(300, 120));
        labelId.setHorizontalAlignment(SwingConstants.RIGHT);
        fieldId.setPreferredSize(new Dimension(150, 50));
        labelTitle.setHorizontalAlignment(SwingConstants.RIGHT);
        labelTitle.setText(it.ft("panel element/title"));
        fieldTitle.setToolTipText(it.fd("panel element/title"));
        labelDescription.setHorizontalAlignment(SwingConstants.RIGHT);
        labelDescription.setText(it.ft("panel element/description"));
        fieldDescription.setToolTipText(it.fd("panel element/description"));
        labelVisibility.setHorizontalAlignment(SwingConstants.RIGHT);
        labelVisibility.setText(it.ft("panel feature/visibility"));
        labelVisibility.setToolTipText(it.fd("panel feature/visibility"));
        fieldVisibility.setBackground(Constant.defaultColor);
        fieldVisibility.setToolTipText(it.fd("panel feature/visibility"));
        labelType.setHorizontalAlignment(SwingConstants.RIGHT);
        labelType.setText(it.ft("panel feature/constraint"));
        fieldType.setBackground(Constant.defaultColor);
        fieldType.setToolTipText(it.fd("panel feature/constraint"));
        labelInitial.setHorizontalAlignment(SwingConstants.RIGHT);
        labelInitial.setText(it.ft("panel property/initial"));
        fieldInitial.setToolTipText(it.fd("panel property/initial"));
        verifyChangeable.setBackground(Constant.defaultColor);
        verifyChangeable.setHorizontalAlignment(SwingConstants.CENTER);
        verifyChangeable.setText(it.ft("panel property/changeable"));
        verifyChangeable.setToolTipText(it.fd("panel property/changeable"));
        labelCategory.setHorizontalAlignment(SwingConstants.RIGHT);
        fieldCategory.setBackground(Constant.defaultColor);
        labelLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        inputPanel.setLayout(gridInput);
        gridInput.setRows(9);
        gridInput.setColumns(2);
        gridInput.setHgap(5);
        inputPanel.setBackground(Constant.defaultColor);
        inputPanel.setMaximumSize(new Dimension(500, 200));
        inputPanel.setPreferredSize(new Dimension(500, 200));
        this.add(inputPanel, null);
        inputPanel.add(labelId, null);
        inputPanel.add(fieldId, null);
        inputPanel.add(labelTitle, null);
        inputPanel.add(fieldTitle, null);
        inputPanel.add(labelDescription, null);
        inputPanel.add(fieldDescription, null);
        inputPanel.add(labelVisibility, null);
        inputPanel.add(fieldVisibility, null);
        inputPanel.add(labelType, null);
        inputPanel.add(fieldType, null);
        inputPanel.add(labelInitial, null);
        inputPanel.add(fieldInitial, null);
        inputPanel.add(changeableLabel, null);
        inputPanel.add(verifyChangeable, null);
        inputPanel.add(labelCategory, null);
        inputPanel.add(fieldCategory, null);
        inputPanel.add(labelLabel, null);
        inputPanel.add(fieldLabel, null);
    }

    public void defineElement(AnimaObjectElement ao) {
        lastElement = ao;
        fieldTitle.setText(MultiLangString.notNullTranslation(ao.getTitle(), Constant.DEFAULT_LANGUAGE));
        fieldDescription.setText(MultiLangString.notNullTranslation(ao.getDescription(), Constant.DEFAULT_LANGUAGE));
        if (ao instanceof AnimaFeature) {
            labelId.setText(it.ft("panel element/identification"));
            fieldId.setToolTipText(it.fd("panel element/identification"));
            fieldId.setText(((AnimaFeature) ao).getName());
            fieldVisibility.setSelectedIndex(anima.util.ArrayUtil.sequencialSearchIgnoreCase(animaVisibility, ((AnimaFeature) ao).getVisibility()));
            labelVisibility.setVisible(true);
            fieldVisibility.setVisible(true);
        } else {
            labelVisibility.setVisible(false);
            fieldVisibility.setVisible(false);
        }
        if (ao instanceof PropertyType) {
            labelType.setVisible(true);
            fieldType.setSelectedIndex(anima.util.ArrayUtil.sequencialSearchIgnoreCase(animaType, ((PropertyType) ao).getConstraint()));
            fieldInitial.setText(((PropertyType) ao).getInitial());
            verifyChangeable.setSelected(((PropertyType) ao).isChangeable());
            fieldType.setVisible(true);
            labelInitial.setVisible(true);
            fieldInitial.setVisible(true);
            changeableLabel.setVisible(true);
            verifyChangeable.setVisible(true);
        } else {
            labelType.setVisible(false);
            fieldType.setVisible(false);
            labelInitial.setVisible(false);
            fieldInitial.setVisible(false);
            changeableLabel.setVisible(false);
            verifyChangeable.setVisible(false);
        }
        if (ao instanceof OperationType || ao instanceof EventType) {
            AnimaMessageStructure ams = null;
            if (ao instanceof OperationType) {
                labelLabel.setText(it.ft("panel operation/trigger"));
                fieldLabel.setToolTipText(it.fd("panel operation/trigger"));
                ams = ((OperationType) ao).getTrigger(0);
                labelCategory.setText(it.ft("panel operation/category"));
                labelCategory.setToolTipText(it.fd("panel operation/category"));
                fieldCategory.setToolTipText(it.fd("panel operation/category"));
            }
            if (ao instanceof EventType) {
                labelId.setText(it.ft("panel event/name"));
                fieldId.setToolTipText(it.fd("panel event/name"));
                fieldId.setText(((EventType) ao).getId());
                labelLabel.setText(it.ft("panel event/production"));
                fieldLabel.setToolTipText(it.fd("panel event/production"));
                ams = ((EventType) ao).getTrigger(0);
                labelCategory.setText(it.ft("panel event/category"));
                labelCategory.setToolTipText(it.fd("panel event/category"));
                fieldCategory.setToolTipText(it.fd("panel event/category"));
            }
            if (ams == null) {
                fieldCategory.setSelectedIndex(0);
                fieldLabel.setText("");
            } else {
                fieldCategory.setSelectedIndex(anima.util.ArrayUtil.sequencialSearchIgnoreCase(animaCategory, ams.getCategory()));
                fieldLabel.setText(ams.getLabel());
            }
            labelCategory.setVisible(true);
            fieldCategory.setVisible(true);
            labelLabel.setVisible(true);
            fieldLabel.setVisible(true);
        } else {
            labelCategory.setVisible(false);
            fieldCategory.setVisible(false);
            labelLabel.setVisible(false);
            fieldLabel.setVisible(false);
        }
    }

    public AnimaObjectElement retrieveElement() {
        MultiLangString title = (fieldTitle.getText().length() > 0) ? new MultiLangString(Constant.DEFAULT_LANGUAGE, fieldTitle.getText()) : null;
        MultiLangString description = (fieldDescription.getText().length() > 0) ? new MultiLangString(Constant.DEFAULT_LANGUAGE, fieldDescription.getText()) : null;
        if (lastElement instanceof PropertyType) {
            lastElement = new PropertyType(fieldId.getText(), false, animaType[fieldType.getSelectedIndex()], fieldInitial.getText());
            ((PropertyType) lastElement).setVisibility(animaVisibility[fieldVisibility.getSelectedIndex()]);
            lastElement.setTitle(title);
            lastElement.setDescription(description);
            ((PropertyType) lastElement).setChangeable(verifyChangeable.isSelected());
        } else if (lastElement instanceof OperationType) {
            lastElement = new OperationType(fieldId.getText(), null, null);
            ((OperationType) lastElement).setVisibility(animaVisibility[fieldVisibility.getSelectedIndex()]);
            lastElement.setTitle(title);
            lastElement.setDescription(description);
            AnimaMessageStructure vtrigger[] = { new AnimaMessageStructure(animaCategory[fieldCategory.getSelectedIndex()], null, fieldLabel.getText()) };
            ((OperationType) lastElement).setTrigger(vtrigger);
        } else if (lastElement instanceof EventType) {
            lastElement = new EventType(fieldId.getText(), null);
            lastElement.setTitle(title);
            lastElement.setDescription(description);
            AnimaMessageStructure vproduction[] = { new AnimaMessageStructure(animaCategory[fieldCategory.getSelectedIndex()], null, fieldLabel.getText()) };
            ((EventType) lastElement).setTrigger(vproduction);
        } else lastElement = null;
        return lastElement;
    }

    public String getId() {
        return fieldId.getText();
    }

    public String getTitle() {
        return fieldTitle.getText();
    }

    public String getDescription() {
        return fieldDescription.getText();
    }

    public int getVisibility() {
        return fieldVisibility.getSelectedIndex();
    }

    public int getType() {
        return fieldType.getSelectedIndex();
    }

    public String getInitial() {
        return fieldInitial.getText();
    }

    public boolean getChangeable() {
        return verifyChangeable.isSelected();
    }

    public int getCategory() {
        return fieldCategory.getSelectedIndex();
    }

    public String getLabel() {
        return fieldLabel.getText();
    }
}
