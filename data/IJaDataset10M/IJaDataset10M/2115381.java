package de.jlab.ui.modules.panels.dcg.characteristiccurve;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import de.jlab.GlobalsLocator;
import de.jlab.boards.Board;
import de.jlab.boards.DCGBoard;
import de.jlab.config.DCGParameter;
import de.jlab.config.characteristiccurve.CharCurveCondition;
import de.jlab.config.characteristiccurve.CharCurveParameter;
import de.jlab.config.characteristiccurve.CharCurveXAxis;
import de.jlab.config.characteristiccurve.CharCurveYAxis;
import de.jlab.config.characteristiccurve.CharacteristicCurveConfig;
import de.jlab.lab.Lab;
import de.jlab.ui.tools.JLabFormattedDoubleTextField;
import de.jlab.ui.tools.OKCancelDialogPlausiChecker;

public class CharacteristicCurveEditPanel extends JPanel implements OKCancelDialogPlausiChecker {

    Lab theLab = null;

    List<DCGParam> dcgParameters = new ArrayList<DCGParam>();

    JPanel jPanelHeader = new JPanel();

    JPanel jPanelStaticConfig = new JPanel();

    JPanel jPanelParameterConfig = new JPanel();

    JPanel jPanelConditionConfig = new JPanel();

    JPanel jPanelConfig = new JPanel();

    JPanel jPanelControl = new JPanel();

    JLabel jLabelXDCG = new JLabel(GlobalsLocator.translate("char-curve-x-axis"));

    JLabel jLabelXFormat = new JLabel(GlobalsLocator.translate("char-curve-axis-format"));

    JLabel jLabelYFormat = new JLabel(GlobalsLocator.translate("char-curve-axis-format"));

    JTextField jTextFieldXName = new JTextField(20);

    JTextField jTextFieldXFormat = new JTextField(10);

    JComboBox jComboBoxXDCG = new JComboBox();

    JLabel jLabelXMin = new JLabel(GlobalsLocator.translate("char-curve-x-min"));

    JLabFormattedDoubleTextField jTextFieldXMin = new JLabFormattedDoubleTextField(new DecimalFormat("#0.###"), 5);

    JLabel jLabelXMax = new JLabel(GlobalsLocator.translate("char-curve-x-max"));

    JLabFormattedDoubleTextField jTextFieldXMax = new JLabFormattedDoubleTextField(new DecimalFormat("#0.###"), 5);

    JLabel jLabelMeasurements = new JLabel(GlobalsLocator.translate("char-curve-x-measurements"));

    JFormattedTextField jTextFieldMeasurements = new JFormattedTextField(new DecimalFormat("#0"));

    JLabel jLabelYDCG = new JLabel(GlobalsLocator.translate("char-curve-y-axis"));

    JTextField jTextFieldYName = new JTextField(20);

    JTextField jTextFieldYFormat = new JTextField(10);

    JComboBox jComboBoxYDCG = new JComboBox();

    JLabel jLabelCharacteristic = new JLabel(GlobalsLocator.translate("char-curve-title"));

    JTextField jTextFieldCharacteristicName = new JTextField();

    JButton jButtonNewParameter = new JButton(GlobalsLocator.translate("char-curve-button-add-parameter"));

    JButton jButtonNewCondition = new JButton(GlobalsLocator.translate("char-curve-button-add-condition"));

    List<CharParamPanel> parampanels = new ArrayList<CharParamPanel>();

    List<CharConditionPanel> conditionpanels = new ArrayList<CharConditionPanel>();

    public CharacteristicCurveEditPanel(Lab theLab) {
        this.theLab = theLab;
        initUI();
    }

    public void initUI() {
        jTextFieldCharacteristicName.setColumns(20);
        jTextFieldMeasurements.setColumns(4);
        jTextFieldMeasurements.setValue(100);
        jTextFieldXFormat.setText("#0.###");
        jTextFieldYFormat.setText("#0.###");
        this.setLayout(new GridBagLayout());
        jPanelHeader.setLayout(new GridBagLayout());
        jPanelStaticConfig.setLayout(new GridBagLayout());
        jPanelParameterConfig.setLayout(new GridBagLayout());
        jPanelConditionConfig.setLayout(new GridBagLayout());
        jPanelConfig.setLayout(new GridBagLayout());
        jPanelConfig.add(jPanelStaticConfig, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelConfig.add(jPanelParameterConfig, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelConfig.add(jPanelConditionConfig, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        this.add(jPanelHeader, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        this.add(jPanelConfig, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        this.add(jPanelControl, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        jPanelStaticConfig.add(jLabelXDCG, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelStaticConfig.add(jTextFieldXName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelStaticConfig.add(jComboBoxXDCG, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelStaticConfig.add(jLabelXMin, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelStaticConfig.add(jTextFieldXMin, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelStaticConfig.add(jLabelXMax, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelStaticConfig.add(jTextFieldXMax, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelStaticConfig.add(jLabelMeasurements, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelStaticConfig.add(jTextFieldMeasurements, new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelStaticConfig.add(jLabelXFormat, new GridBagConstraints(9, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelStaticConfig.add(jTextFieldXFormat, new GridBagConstraints(10, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelStaticConfig.add(jLabelYDCG, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelStaticConfig.add(jTextFieldYName, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelStaticConfig.add(jComboBoxYDCG, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelStaticConfig.add(jLabelYFormat, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelStaticConfig.add(jTextFieldYFormat, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelHeader.add(jLabelCharacteristic, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelHeader.add(jTextFieldCharacteristicName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelControl.add(jButtonNewParameter);
        jPanelControl.add(jButtonNewCondition);
        calcDCGParameters();
        for (DCGParam currDCGParam : dcgParameters) {
            jComboBoxXDCG.addItem(currDCGParam);
            jComboBoxYDCG.addItem(currDCGParam);
        }
        jComboBoxXDCG.setRenderer(new DCGListCellRenderer());
        jComboBoxYDCG.setRenderer(new DCGListCellRenderer());
        jButtonNewParameter.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                newParam();
            }
        });
        jButtonNewCondition.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                newCondition();
            }
        });
    }

    private void newParam() {
        CharParamPanel newParamPanel = new CharParamPanel(dcgParameters, this);
        parampanels.add(newParamPanel);
        jPanelParameterConfig.add(newParamPanel, new GridBagConstraints(0, parampanels.size() - 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelParameterConfig.revalidate();
    }

    private void newCondition() {
        CharConditionPanel newConstraintPanel = new CharConditionPanel(dcgParameters, this);
        conditionpanels.add(newConstraintPanel);
        jPanelConditionConfig.add(newConstraintPanel, new GridBagConstraints(0, conditionpanels.size() - 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        jPanelConditionConfig.revalidate();
    }

    private void calcDCGParameters() {
        for (Board currBoard : theLab.getAllBoardsFound()) {
            if (currBoard instanceof DCGBoard) {
                dcgParameters.add(new DCGParam(currBoard, DCGParameter.TYPE.CURRENT));
                dcgParameters.add(new DCGParam(currBoard, DCGParameter.TYPE.VOLTAGE));
            }
        }
    }

    public void removeParameter(CharParamPanel panel) {
        parampanels.remove(panel);
        jPanelParameterConfig.removeAll();
        int i = 0;
        for (CharParamPanel currParamPanel : parampanels) {
            jPanelParameterConfig.add(currParamPanel, new GridBagConstraints(0, i, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
            ++i;
        }
        jPanelParameterConfig.revalidate();
    }

    public void removeCondition(CharConditionPanel panel) {
        conditionpanels.remove(panel);
        jPanelConditionConfig.removeAll();
        int i = 0;
        for (CharConditionPanel currContraintPanel : conditionpanels) {
            jPanelConditionConfig.add(currContraintPanel, new GridBagConstraints(0, i, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
            ++i;
        }
        jPanelConditionConfig.revalidate();
    }

    public CharacteristicCurveConfig getCharacteristicCurve() {
        CharacteristicCurveConfig newCurve = new CharacteristicCurveConfig();
        newCurve.setName(jTextFieldCharacteristicName.getText());
        int measurementCount = 100;
        Object measurements = jTextFieldMeasurements.getValue();
        if (measurements instanceof Long) measurementCount = ((Long) measurements).intValue(); else measurementCount = ((Integer) measurements).intValue();
        newCurve.setXAxis(new CharCurveXAxis(jTextFieldXName.getText(), jTextFieldXMin.getDoubleValue(), jTextFieldXMax.getDoubleValue(), jTextFieldXFormat.getText(), createParameterByItem((DCGParam) jComboBoxXDCG.getSelectedItem()), measurementCount));
        newCurve.setYAxis(new CharCurveYAxis(jTextFieldYName.getText(), jTextFieldYFormat.getText(), createParameterByItem((DCGParam) jComboBoxYDCG.getSelectedItem())));
        for (CharParamPanel currParamPanel : parampanels) {
            newCurve.addParameter(currParamPanel.getParameter());
        }
        for (CharConditionPanel currConditionPanel : conditionpanels) {
            newCurve.addCondition(currConditionPanel.getCondition());
        }
        return newCurve;
    }

    public void setCharacteristics(CharacteristicCurveConfig characteristicConfig) {
        this.parampanels.clear();
        this.conditionpanels.clear();
        jPanelParameterConfig.removeAll();
        jPanelConditionConfig.removeAll();
        DCGParam xDCG = createParamByConfig(characteristicConfig.getXAxis().getDcgParameter());
        DCGParam yDCG = createParamByConfig(characteristicConfig.getYAxis().getDcgParameter());
        jComboBoxXDCG.setSelectedItem(xDCG);
        jComboBoxYDCG.setSelectedItem(yDCG);
        jTextFieldXMin.setValue(characteristicConfig.getXAxis().getMin());
        jTextFieldXMax.setValue(characteristicConfig.getXAxis().getMax());
        jTextFieldMeasurements.setValue(characteristicConfig.getXAxis().getMeasurementcount());
        jTextFieldCharacteristicName.setText(characteristicConfig.getName());
        jTextFieldXName.setText(characteristicConfig.getXAxis().getName());
        jTextFieldYName.setText(characteristicConfig.getYAxis().getName());
        jTextFieldXFormat.setText(characteristicConfig.getXAxis().getFormat());
        jTextFieldYFormat.setText(characteristicConfig.getYAxis().getFormat());
        if (characteristicConfig.getParameters() != null) {
            for (CharCurveParameter currParam : characteristicConfig.getParameters()) {
                CharParamPanel newParamPanel = new CharParamPanel(dcgParameters, this);
                parampanels.add(newParamPanel);
                jPanelParameterConfig.add(newParamPanel, new GridBagConstraints(0, parampanels.size() - 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
                newParamPanel.setParam(currParam);
            }
        }
        if (characteristicConfig.getConditions() != null) {
            for (CharCurveCondition currCondition : characteristicConfig.getConditions()) {
                CharConditionPanel newConditionPanel = new CharConditionPanel(dcgParameters, this);
                conditionpanels.add(newConditionPanel);
                jPanelConditionConfig.add(newConditionPanel, new GridBagConstraints(0, conditionpanels.size() - 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
                newConditionPanel.setConstraint(currCondition);
            }
        }
    }

    public static DCGParam createParamByConfig(DCGParameter parameter) {
        DCGParam param = new DCGParam(parameter.getCommChannelName(), parameter.getAddress(), parameter.getType());
        return param;
    }

    public static DCGParameter createParameterByItem(DCGParam param) {
        DCGParameter parameter = new DCGParameter(param.getCommChannelName(), param.getAddress(), param.getType());
        return parameter;
    }

    public String checkPlausi() {
        CharacteristicCurveConfig characteristicConfig = this.getCharacteristicCurve();
        HashMap<DCGParameter, String> usedDCGParameters = new HashMap<DCGParameter, String>();
        StringBuilder errors = new StringBuilder();
        if (characteristicConfig.getXAxis().getMin() < 0) errors.append(GlobalsLocator.translate("char-curve-plausi-error-xmin-positive") + "\n");
        if (characteristicConfig.getXAxis().getMax() < 0) errors.append(GlobalsLocator.translate("char-curve-plausi-error-xmax-positive") + "\n");
        if (characteristicConfig.getXAxis().getMax() < characteristicConfig.getXAxis().getMin()) errors.append(GlobalsLocator.translate("char-curve-plausi-error-xmax-greater-than-xmin") + "\n");
        usedDCGParameters.put(characteristicConfig.getXAxis().getDcgParameter(), GlobalsLocator.translate("char-curve-x-axis"));
        if (characteristicConfig.getParameters() != null) {
            for (CharCurveParameter currParam : characteristicConfig.getParameters()) {
                String alreadyUsedIn = usedDCGParameters.get(currParam.getDcgParameter());
                if (alreadyUsedIn != null) errors.append(GlobalsLocator.translate("char-curve-parameter") + " : " + currParam.getName() + " DCGParameter " + currParam.getDcgParameter().getDisplayString() + " " + GlobalsLocator.translate("char-curve-plausi-error-dcg-already-used") + " " + alreadyUsedIn + "\n"); else usedDCGParameters.put(currParam.getDcgParameter(), GlobalsLocator.translate("char-curve-parameter") + " " + currParam.getName());
                if (currParam.getMin() < 0) errors.append(GlobalsLocator.translate("char-curve-parameter") + " : " + currParam.getName() + " " + GlobalsLocator.translate("char-curve-plausi-error-parammin-positive") + " " + "\n");
                if (currParam.getMax() < 0) errors.append(GlobalsLocator.translate("char-curve-parameter") + " : " + currParam.getName() + " " + GlobalsLocator.translate("char-curve-plausi-error-parammax-positive") + " " + "\n");
                if (currParam.getStepping() < 0) errors.append(GlobalsLocator.translate("char-curve-parameter") + " : " + currParam.getName() + " " + GlobalsLocator.translate("char-curve-plausi-error-paramstepping-positive") + " " + "\n");
                if (currParam.getMin() >= currParam.getMax()) errors.append(GlobalsLocator.translate("char-curve-parameter") + " : " + currParam.getName() + " " + GlobalsLocator.translate("char-curve-plausi-error-param-max-greater-than-min") + " " + "\n");
            }
        }
        if (characteristicConfig.getConditions() != null) {
            for (CharCurveCondition currCondition : characteristicConfig.getConditions()) {
                if (currCondition.getMax() < 0) errors.append(GlobalsLocator.translate("char-curve-condition") + " : " + currCondition.getName() + " " + GlobalsLocator.translate("char-curve-plausi-error-condition-value-negative") + "\n");
                String alreadyUsedIn = usedDCGParameters.get(currCondition.getDcgParameter());
                if (alreadyUsedIn != null) errors.append(GlobalsLocator.translate("char-curve-condition") + " : " + currCondition.getName() + " DCGParameter " + currCondition.getDcgParameter().getDisplayString() + " " + GlobalsLocator.translate("char-curve-plausi-error-dcg-already-used") + " " + alreadyUsedIn + "\n"); else usedDCGParameters.put(currCondition.getDcgParameter(), GlobalsLocator.translate("char-curve-condition") + " " + currCondition.getName());
            }
        }
        return errors.toString();
    }
}
