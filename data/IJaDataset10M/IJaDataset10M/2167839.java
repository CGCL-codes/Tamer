package tr.appl.prefs;

import javax.swing.DefaultComboBoxModel;
import org.openide.util.NbBundle;

final class ApplicationPanel extends javax.swing.JPanel {

    private final ApplicationOptionsPanelController controller;

    private static String loc(String key) {
        return NbBundle.getMessage(ApplicationPanel.class, key);
    }

    private static final String[] periods = new String[] { loc("CTL_Update_every_startup"), loc("CTL_Update_every_day"), loc("CTL_Update_every_week"), loc("CTL_Update_every_fortnight"), loc("CTL_Update_every_month"), loc("CTL_Update_never") };

    ApplicationPanel(ApplicationOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
    }

    void load() {
        checkPeriodComboBox.setModel(new DefaultComboBoxModel(periods));
        checkPeriodComboBox.setMaximumRowCount(periods.length);
        checkPeriodComboBox.setSelectedIndex(ApplicationPrefs.getVersionCheckPeriod());
    }

    void store() {
        ApplicationPrefs.setVersionCheckPeriod(checkPeriodComboBox.getSelectedIndex());
    }

    boolean valid() {
        return true;
    }

    private void initComponents() {
        checkPeriodLabel = new javax.swing.JLabel();
        checkPeriodComboBox = new javax.swing.JComboBox();
        org.openide.awt.Mnemonics.setLocalizedText(checkPeriodLabel, org.openide.util.NbBundle.getMessage(ApplicationPanel.class, "prefs.version.label"));
        checkPeriodLabel.setMaximumSize(new java.awt.Dimension(270, 23));
        checkPeriodLabel.setMinimumSize(new java.awt.Dimension(270, 23));
        checkPeriodLabel.setPreferredSize(new java.awt.Dimension(270, 23));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(checkPeriodLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(checkPeriodComboBox, 0, 221, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(checkPeriodLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(checkPeriodComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    private javax.swing.JComboBox checkPeriodComboBox;

    private javax.swing.JLabel checkPeriodLabel;
}
