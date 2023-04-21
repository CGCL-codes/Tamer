package es.aeat.eett.rubik.core.tableSetting;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TableSettingPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final ResourceBundle localizationResources = ResourceBundle.getBundle("es.aeat.eett.rubik.core.tableSetting.locale.LocalizationBundle");

    private TableSetting tableSetting;

    private JCheckBox JShowParentsRow;

    private JCheckBox JShowParentsCol;

    private JCheckBox JIndentMembersRow;

    private JCheckBox JIndentMembersCol;

    private JComboBox JHierarchyHeaderCol;

    private JComboBox JHierarchyHeaderRow;

    private JComboBox JMemberSpanRow;

    private JComboBox JRowHeader;

    private JComboBox JMemberSpanCol;

    private JComboBox JHeaderSpanRow;

    private JComboBox JHeaderSpanCol;

    private JCheckBox JmostrarMedidasRow;

    private JCheckBox JmostrarMedidasCol;

    private JCheckBox navigable;

    public TableSettingPanel(TableSetting tableSetting) {
        super(new java.awt.GridLayout(0, 3));
        this.tableSetting = tableSetting;
        initComponents();
    }

    public void update() {
        JShowParentsRow.setSelected(tableSetting.isShowParentsRow());
        JShowParentsCol.setSelected(tableSetting.isShowParentsCol());
        JIndentMembersRow.setSelected(tableSetting.isIdentRow());
        JIndentMembersCol.setSelected(tableSetting.isIdentCol());
        JHierarchyHeaderRow.setSelectedIndex(tableSetting.getModeHierarchyHeaderRow());
        JHierarchyHeaderCol.setSelectedIndex(tableSetting.getModeHierarchyHeaderCol());
        JMemberSpanRow.setSelectedIndex(tableSetting.getModeMemberSpanRow());
        JMemberSpanCol.setSelectedIndex(tableSetting.getModeMemberSpanCol());
        JRowHeader.setSelectedIndex(tableSetting.getModeRowHeaderRow());
        JHeaderSpanRow.setSelectedIndex(tableSetting.getModeHeaderSpanRow());
        JHeaderSpanCol.setSelectedIndex(tableSetting.getModeHeaderSpanCol());
        JmostrarMedidasRow.setSelected(!tableSetting.isShowMeasuresRow());
        JmostrarMedidasCol.setSelected(!tableSetting.isShowMeasuresCol());
    }

    private void initComponents() {
        JLabel jLabel1 = new JLabel();
        JLabel jLabel2 = new JLabel(localizationResources.getString("EjeFilas"));
        JLabel jLabel3 = new JLabel(localizationResources.getString("EjeColumnas"));
        JLabel jLabel4 = new JLabel(localizationResources.getString("MostrarPadres"));
        JShowParentsRow = new JCheckBox();
        JShowParentsRow.setSelected(tableSetting.isShowParentsRow());
        JShowParentsRow.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                tableSetting.setShowParentsRow(((JCheckBox) evt.getSource()).isSelected());
            }
        });
        JShowParentsCol = new JCheckBox();
        JShowParentsCol.setSelected(tableSetting.isShowParentsCol());
        JShowParentsCol.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                tableSetting.setShowParentsCol(((JCheckBox) evt.getSource()).isSelected());
            }
        });
        JLabel jLabel5 = new JLabel(localizationResources.getString("SangrarMiembros"));
        JIndentMembersRow = new javax.swing.JCheckBox();
        JIndentMembersRow.setSelected(tableSetting.isIdentRow());
        JIndentMembersRow.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                tableSetting.setIdentRow(((JCheckBox) evt.getSource()).isSelected());
            }
        });
        JIndentMembersCol = new JCheckBox();
        JIndentMembersCol.setSelected(tableSetting.isIdentCol());
        JIndentMembersCol.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                tableSetting.setIdentCol(((JCheckBox) evt.getSource()).isSelected());
            }
        });
        JLabel jLabel6 = new javax.swing.JLabel(localizationResources.getString("CabeceraJerarquia"));
        JHierarchyHeaderRow = new JComboBox(TableSetting.HIERARCHY_HEADER);
        JHierarchyHeaderRow.setSelectedIndex(tableSetting.getModeHierarchyHeaderRow());
        JHierarchyHeaderRow.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableSetting.setModeHierarchyHeaderRow(((JComboBox) evt.getSource()).getSelectedIndex());
            }
        });
        JHierarchyHeaderCol = new JComboBox(TableSetting.HIERARCHY_HEADER);
        JHierarchyHeaderCol.setSelectedIndex(tableSetting.getModeHierarchyHeaderCol());
        JHierarchyHeaderCol.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                tableSetting.setModeHierarchyHeaderCol(((JComboBox) evt.getSource()).getSelectedIndex());
            }
        });
        JLabel jLabel7 = new JLabel(localizationResources.getString("CabeceraFila"));
        JRowHeader = new JComboBox(TableSetting.ROW_HEADER);
        JRowHeader.setSelectedIndex(tableSetting.getModeRowHeaderRow());
        JRowHeader.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                tableSetting.setModeRowHeaderRow(((JComboBox) evt.getSource()).getSelectedIndex());
            }
        });
        JLabel jLabel8 = new JLabel();
        JLabel jLabel9 = new JLabel(localizationResources.getString("DespliegueMiembro"));
        JMemberSpanRow = new JComboBox(TableSetting.MEMBER_SPAN);
        JMemberSpanRow.setSelectedIndex(tableSetting.getModeMemberSpanRow());
        JMemberSpanRow.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                tableSetting.setModeMemberSpanRow(((JComboBox) evt.getSource()).getSelectedIndex());
            }
        });
        JMemberSpanCol = new JComboBox(TableSetting.MEMBER_SPAN);
        JMemberSpanCol.setSelectedIndex(tableSetting.getModeMemberSpanCol());
        JMemberSpanCol.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableSetting.setModeMemberSpanCol(((JComboBox) evt.getSource()).getSelectedIndex());
            }
        });
        JLabel jLabel10 = new JLabel(localizationResources.getString("DespliegueCabecera"));
        JHeaderSpanRow = new JComboBox(TableSetting.HEADER_SPAN);
        JHeaderSpanRow.setSelectedIndex(tableSetting.getModeHeaderSpanRow());
        JHeaderSpanRow.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                tableSetting.setModeHeaderSpanRow(((JComboBox) evt.getSource()).getSelectedIndex());
            }
        });
        JHeaderSpanCol = new JComboBox(TableSetting.HEADER_SPAN);
        JHeaderSpanCol.setSelectedIndex(tableSetting.getModeHeaderSpanCol());
        JHeaderSpanCol.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                tableSetting.setModeHeaderSpanCol(((JComboBox) evt.getSource()).getSelectedIndex());
            }
        });
        JLabel jLabel11 = new JLabel(localizationResources.getString("OcultarMedias"));
        JmostrarMedidasRow = new javax.swing.JCheckBox();
        JmostrarMedidasRow.setSelected(!tableSetting.isShowMeasuresRow());
        JmostrarMedidasRow.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                tableSetting.setShowMeasuresRow(!((JCheckBox) evt.getSource()).isSelected());
            }
        });
        JmostrarMedidasCol = new javax.swing.JCheckBox();
        JmostrarMedidasCol.setSelected(!tableSetting.isShowMeasuresCol());
        JmostrarMedidasCol.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                tableSetting.setShowMeasuresCol(!((JCheckBox) evt.getSource()).isSelected());
            }
        });
        navigable = new javax.swing.JCheckBox();
        navigable.setText(localizationResources.getString(TableSetting.NAVIGABLE));
        navigable.setSelected(tableSetting.isNavigable());
        navigable.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                tableSetting.setNavigable(((JCheckBox) evt.getSource()).isSelected());
            }
        });
        add(jLabel1);
        add(jLabel2);
        add(jLabel3);
        add(jLabel4);
        add(JShowParentsRow);
        add(JShowParentsCol);
        add(jLabel5);
        add(JIndentMembersRow);
        add(JIndentMembersCol);
        add(jLabel6);
        add(JHierarchyHeaderRow);
        add(JHierarchyHeaderCol);
        add(jLabel7);
        add(JRowHeader);
        add(jLabel8);
        add(jLabel9);
        add(JMemberSpanRow);
        add(JMemberSpanCol);
        add(jLabel10);
        add(JHeaderSpanRow);
        add(JHeaderSpanCol);
        add(jLabel11);
        add(JmostrarMedidasRow);
        add(JmostrarMedidasCol);
        add(navigable);
    }
}
