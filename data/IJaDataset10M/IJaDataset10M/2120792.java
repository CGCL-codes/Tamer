package trstudio.beansmetric.gui;

final class TRBeansMetricPanel extends javax.swing.JPanel {

    private final TRBeansMetricOptionsPanelController controller;

    TRBeansMetricPanel(TRBeansMetricOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 202, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 68, Short.MAX_VALUE));
    }

    void load() {
    }

    void store() {
    }

    boolean valid() {
        return true;
    }
}
