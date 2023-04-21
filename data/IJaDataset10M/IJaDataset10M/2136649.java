package net.sf.cardic.dice;

/**
 *
 * @author Patrik Karlsson <patrik@trixon.se>
 */
final class DicePanel extends javax.swing.JPanel {

    private final DiceOptionsPanelController controller;

    DicePanel(DiceOptionsPanelController controller) {
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
