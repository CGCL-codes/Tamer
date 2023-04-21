package com.greentea.relaxation.jnmf.gui.components.project.network;

import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.Spacer;
import com.greentea.relaxation.jnmf.gui.components.AbstractComponent;
import com.greentea.relaxation.jnmf.gui.components.project.network.qualitycontrol.QualityControlParentComponent;
import com.greentea.relaxation.jnmf.gui.utils.NotEditableDefaultTableModel;
import com.greentea.relaxation.jnmf.gui.utils.GuiUtils;
import com.greentea.relaxation.jnmf.gui.utils.InfoDialog;
import com.greentea.relaxation.jnmf.gui.MainFrame;
import com.greentea.relaxation.jnmf.localization.Localizer;
import com.greentea.relaxation.jnmf.localization.StringId;
import com.greentea.relaxation.jnmf.model.ForecastGroup;
import com.greentea.relaxation.jnmf.model.IForecastPerformer;
import com.greentea.relaxation.jnmf.parameters.annotations.NotParameter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA. User: GreenTea Date: 07.10.2009 Time: 22:57:02 To change this template
 * use File | Settings | File Templates.
 */
public class ForecastGroupComponent extends AbstractComponent {

    private static ForecastPerformerInfo findForecasetPerformerInfo(String name, List<ForecastPerformerInfo> fpInfos) {
        for (ForecastPerformerInfo fp : fpInfos) {
            if (fp.getName().equals(name)) {
                return fp;
            }
        }
        return null;
    }

    private IForecastGroupOperations componentOperations;

    private JPanel controlPanel;

    private JTextField inputsCountTextField;

    private JTable availableForecastPerformersTable;

    private JTable groupForecastPerformersTable;

    private JButton addForecastPerformerButton;

    private JButton removeForecastPerformerButton;

    private JButton applyChangesButton;

    private JTextField outputsCountTextField;

    private JLabel takeDataFromLabel;

    private JComboBox takeDataFromComboBox;

    private JLabel inputsCountLabel;

    private JLabel outputsCountLabel;

    private JScrollPane allForecastPerformersScrollPane;

    private JScrollPane groupForecastPerformersScrollPane;

    private List<ForecastPerformerInfo> allForecastPerformerInfos = new ArrayList<ForecastPerformerInfo>();

    private List<ForecastPerformerInfo> groupForecastPerformerInfos = new ArrayList<ForecastPerformerInfo>();

    private ForecastGroup forecastGroup;

    @NotParameter
    private QualityControlParentComponent qualityControlComponent;

    public ForecastGroupComponent(String groupName, IForecastGroupOperations componentOperations) {
        super(groupName);
        this.componentOperations = componentOperations;
        $$$setupUI$$$();
        qualityControlComponent = new QualityControlParentComponent();
        addChild(qualityControlComponent);
        setControlPanel(controlPanel);
        setMenu(createMenu());
        addForecastPerformerButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addForecastPerformers();
            }
        });
        removeForecastPerformerButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeForecastPerformers();
            }
        });
        applyChangesButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateForecastGroup();
                testGroup();
            }
        });
    }

    private void createUIComponents() {
        final String networkColumnName = Localizer.getString(StringId.NETWORK);
        DefaultTableModel model = new NotEditableDefaultTableModel(new Object[][] {}, new Object[] { networkColumnName, Localizer.getString(StringId.INPUTS), Localizer.getString(StringId.OUTPUTS) });
        availableForecastPerformersTable = GuiUtils.createJTable(model, false, false, false);
        availableForecastPerformersTable.getColumn(networkColumnName).setPreferredWidth(125);
        model = new NotEditableDefaultTableModel(new Object[][] {}, new Object[] { networkColumnName });
        groupForecastPerformersTable = GuiUtils.createJTable(model, false, false, false);
        addForecastPerformerButton = new JButton(new ImageIcon(MainFrame.ICONS_DIR + "1.png"));
        removeForecastPerformerButton = new JButton(new ImageIcon(MainFrame.ICONS_DIR + "2.png"));
        addForecastPerformerButton.setToolTipText(Localizer.getString(StringId.ADD_TO_GROUP));
        removeForecastPerformerButton.setToolTipText(Localizer.getString(StringId.REMOVE_FROM_GROP));
    }

    private JPopupMenu createMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem deleteProjectMenuItem = new JMenuItem(Localizer.getString(StringId.DELETE));
        deleteProjectMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                componentOperations.delete(ForecastGroupComponent.this);
            }
        });
        menu.add(deleteProjectMenuItem);
        return menu;
    }

    @Override
    public void onComponentSelected() {
        super.onComponentSelected();
        updateForecastPerformersInfos(componentOperations.getForecastPerformerInfos());
    }

    private void updateForecastPerformersInfos(List<ForecastPerformerInfo> availableForecastPerformerInfos) {
        List<ForecastPerformerInfo> newAllForecastPerformerInfos = new ArrayList<ForecastPerformerInfo>();
        List<ForecastPerformerInfo> newGroupForecastPerformerInfos = new ArrayList<ForecastPerformerInfo>();
        for (ForecastPerformerInfo fp : availableForecastPerformerInfos) {
            boolean presentInGroup = false;
            for (ForecastPerformerInfo fpInGroup : groupForecastPerformerInfos) {
                if (fp.getName().equals(fpInGroup.getName())) {
                    presentInGroup = true;
                    newGroupForecastPerformerInfos.add(fp);
                    break;
                }
            }
            if (!presentInGroup) {
                newAllForecastPerformerInfos.add(fp);
            }
        }
        allForecastPerformerInfos = newAllForecastPerformerInfos;
        groupForecastPerformerInfos = newGroupForecastPerformerInfos;
        if (!validateForecastPerformersInGroup()) {
            clearGroup();
        }
        updateTables();
    }

    private void updateForecastGroup() {
        forecastGroup = new ForecastGroup();
        for (ForecastPerformerInfo fpInfo : groupForecastPerformerInfos) {
            forecastGroup.addForecastPerformer(fpInfo.getForecastPerformer());
        }
        applyChangesButton.setEnabled(forecastGroup.isReady());
    }

    private void clearGroup() {
        allForecastPerformerInfos.addAll(groupForecastPerformerInfos);
        groupForecastPerformerInfos.clear();
        forecastGroup = null;
        inputsCountTextField.setText("");
        outputsCountTextField.setText("");
        takeDataFromComboBox.removeAllItems();
        applyChangesButton.setEnabled(false);
    }

    private void updateTables() {
        Collections.sort(allForecastPerformerInfos);
        Collections.sort(groupForecastPerformerInfos);
        GuiUtils.removeAllRaws(availableForecastPerformersTable);
        DefaultTableModel model = GuiUtils.resolveDefaultTableModel(availableForecastPerformersTable);
        for (ForecastPerformerInfo fpInfo : allForecastPerformerInfos) {
            model.addRow(new Object[] { fpInfo.getName(), fpInfo.getForecastPerformer().getInputsCount(), fpInfo.getForecastPerformer().getOutputsCount() });
        }
        GuiUtils.removeAllRaws(groupForecastPerformersTable);
        model = GuiUtils.resolveDefaultTableModel(groupForecastPerformersTable);
        for (ForecastPerformerInfo fpInfo : groupForecastPerformerInfos) {
            model.addRow(new Object[] { fpInfo.getName() });
        }
    }

    private boolean validateForecastPerformersInGroup() {
        boolean res = true;
        if (groupForecastPerformerInfos.size() > 1) {
            final IForecastPerformer firstForecastPerformer = groupForecastPerformerInfos.get(0).getForecastPerformer();
            int inputsCount = firstForecastPerformer.getInputsCount();
            int outputsCount = firstForecastPerformer.getOutputsCount();
            for (int i = 2; i < groupForecastPerformerInfos.size(); ++i) {
                IForecastPerformer fp = groupForecastPerformerInfos.get(i).getForecastPerformer();
                if (fp.getInputsCount() != inputsCount || fp.getOutputsCount() != outputsCount) {
                    res = false;
                    break;
                }
            }
        }
        return res;
    }

    private void addForecastPerformers() {
        int[] selectedRows = availableForecastPerformersTable.getSelectedRows();
        if (selectedRows.length == 0) {
            return;
        }
        DefaultTableModel model = GuiUtils.resolveDefaultTableModel(availableForecastPerformersTable);
        int inputsCount = -1;
        int outputsCount = -1;
        List<ForecastPerformerInfo> fpInfosToAdd = new ArrayList<ForecastPerformerInfo>();
        for (int i = 0; i < selectedRows.length; ++i) {
            String name = (String) model.getValueAt(selectedRows[i], 0);
            ForecastPerformerInfo fpInfo = findForecasetPerformerInfo(name, allForecastPerformerInfos);
            fpInfosToAdd.add(fpInfo);
            final IForecastPerformer fp = fpInfo.getForecastPerformer();
            if (inputsCount == -1) {
                inputsCount = fp.getInputsCount();
                outputsCount = fp.getOutputsCount();
            } else if (inputsCount != fp.getInputsCount() || outputsCount != fp.getOutputsCount()) {
                InfoDialog.showUserError(Localizer.getString(StringId.NETWORKS_GROP_FAIL_VALIDATION1));
                return;
            }
        }
        if (groupForecastPerformerInfos.size() > 0 && (getInputsCount() != inputsCount || getOutputsCount() != outputsCount)) {
            InfoDialog.showUserError(Localizer.getString(StringId.NETWORKS_GROP_FAIL_VALIDATION2));
            return;
        }
        inputsCountTextField.setText("" + inputsCount);
        outputsCountTextField.setText("" + outputsCount);
        applyChangesButton.setEnabled(true);
        allForecastPerformerInfos.removeAll(fpInfosToAdd);
        groupForecastPerformerInfos.addAll(fpInfosToAdd);
        updateTables();
        updateTakeDataFromComboBox();
    }

    private void updateTakeDataFromComboBox() {
        String takeDataFrom = (String) takeDataFromComboBox.getSelectedItem();
        takeDataFromComboBox.removeAllItems();
        boolean contains = false;
        for (ForecastPerformerInfo fpInfo : groupForecastPerformerInfos) {
            takeDataFromComboBox.addItem(fpInfo.getName());
            if (fpInfo.getName().equals(takeDataFrom)) {
                contains = true;
            }
        }
        if (contains) {
            takeDataFromComboBox.setSelectedItem(takeDataFrom);
        } else if (groupForecastPerformerInfos.size() > 0) {
            takeDataFromComboBox.setSelectedItem(groupForecastPerformerInfos.get(0).getName());
        }
    }

    private void removeForecastPerformers() {
        int[] selectedRows = groupForecastPerformersTable.getSelectedRows();
        if (selectedRows.length == 0) {
            return;
        }
        DefaultTableModel model = GuiUtils.resolveDefaultTableModel(groupForecastPerformersTable);
        List<ForecastPerformerInfo> fpInfosToRemove = new ArrayList<ForecastPerformerInfo>();
        for (int i = 0; i < selectedRows.length; ++i) {
            String name = (String) model.getValueAt(selectedRows[i], 0);
            ForecastPerformerInfo fpInfo = findForecasetPerformerInfo(name, groupForecastPerformerInfos);
            fpInfosToRemove.add(fpInfo);
        }
        allForecastPerformerInfos.addAll(fpInfosToRemove);
        groupForecastPerformerInfos.removeAll(fpInfosToRemove);
        if (groupForecastPerformerInfos.size() == 0) {
            inputsCountTextField.setText("");
            outputsCountTextField.setText("");
            takeDataFromComboBox.removeAllItems();
            applyChangesButton.setEnabled(false);
        }
        updateTables();
        updateTakeDataFromComboBox();
    }

    private int getInputsCount() {
        return groupForecastPerformerInfos.size() > 0 ? groupForecastPerformerInfos.get(0).getForecastPerformer().getInputsCount() : -1;
    }

    private int getOutputsCount() {
        return groupForecastPerformerInfos.size() > 0 ? groupForecastPerformerInfos.get(0).getForecastPerformer().getOutputsCount() : -1;
    }

    private void testGroup() {
        if (forecastGroup != null && forecastGroup.isReady()) {
            prepareQualityControlComponent();
            qualityControlComponent.analize();
        }
    }

    private void prepareQualityControlComponent() {
        qualityControlComponent.setForecastPerformer(forecastGroup);
        DataInfo dataInfo = componentOperations.createData((String) takeDataFromComboBox.getSelectedItem());
        qualityControlComponent.setAllData(dataInfo.getAllData());
        qualityControlComponent.setLearningData(dataInfo.getLearningData());
        qualityControlComponent.setTestData(dataInfo.getTestData());
    }

    @Override
    protected void loadLocalizedCaptions() {
        inputsCountLabel.setText(Localizer.getString(StringId.COUNT) + " " + Localizer.getString(StringId.OF_INPUTS));
        outputsCountLabel.setText(Localizer.getString(StringId.COUNT) + " " + Localizer.getString(StringId.OF_OUTPUTS));
        takeDataFromLabel.setText(Localizer.getString(StringId.TAKE_DATA_FROM) + ": ");
        GuiUtils.setBorderTitle(allForecastPerformersScrollPane, Localizer.getString(StringId.TRAINED_NETWORKS));
        GuiUtils.setBorderTitle(groupForecastPerformersScrollPane, Localizer.getString(StringId.NETWORKS_IN_GROUP));
        applyChangesButton.setText(Localizer.getString(StringId.APPLY_CHANGES_AND_TEST));
    }

    /**
    * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
    * call it in your code!
    *
    * @noinspection ALL
    */
    private void $$$setupUI$$$() {
        createUIComponents();
        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        controlPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        allForecastPerformersScrollPane = new JScrollPane();
        panel1.add(allForecastPerformersScrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(300, -1), null, new Dimension(300, -1), 0, false));
        allForecastPerformersScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "��������� ����", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(allForecastPerformersScrollPane.getFont().getName(), allForecastPerformersScrollPane.getFont().getStyle(), allForecastPerformersScrollPane.getFont().getSize())));
        availableForecastPerformersTable.setPreferredScrollableViewportSize(new Dimension(300, 400));
        allForecastPerformersScrollPane.setViewportView(availableForecastPerformersTable);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        addForecastPerformerButton.setText("");
        panel2.add(addForecastPerformerButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 75), new Dimension(30, -1), 0, false));
        removeForecastPerformerButton.setText("");
        panel2.add(removeForecastPerformerButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 75), new Dimension(30, -1), 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        inputsCountLabel = new JLabel();
        inputsCountLabel.setText("���-�� ������: ");
        panel4.add(inputsCountLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        inputsCountTextField = new JTextField();
        inputsCountTextField.setEditable(false);
        panel4.add(inputsCountTextField, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        outputsCountLabel = new JLabel();
        outputsCountLabel.setText("���-�� �������: ");
        panel4.add(outputsCountLabel, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 2, false));
        outputsCountTextField = new JTextField();
        outputsCountTextField.setEditable(false);
        panel4.add(outputsCountTextField, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel4.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        groupForecastPerformersScrollPane = new JScrollPane();
        panel3.add(groupForecastPerformersScrollPane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        groupForecastPerformersScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "����, �������� � ������"));
        groupForecastPerformersScrollPane.setViewportView(groupForecastPerformersTable);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(2, 1, new Insets(10, 0, 0, 0), -1, -1));
        controlPanel.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        applyChangesButton = new JButton();
        applyChangesButton.setEnabled(false);
        applyChangesButton.setText("��������� ��������� � ��������� ���� ������");
        panel5.add(applyChangesButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        takeDataFromLabel = new JLabel();
        takeDataFromLabel.setText(" ������ ��� ����� ����� ����� � ����: ");
        panel6.add(takeDataFromLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel6.add(spacer3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        takeDataFromComboBox = new JComboBox();
        panel6.add(takeDataFromComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
    * @noinspection ALL
    */
    public JComponent $$$getRootComponent$$$() {
        return controlPanel;
    }
}
