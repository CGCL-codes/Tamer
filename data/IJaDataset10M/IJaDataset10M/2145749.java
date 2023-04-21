package com.clumsybird.nb.osgi.platform.wizard;

import javax.swing.JPanel;

public final class OSGIVisualPanel3 extends JPanel {

    /** Creates new form OSGIVisualPanel3 */
    public OSGIVisualPanel3() {
        initComponents();
    }

    @Override
    public String getName() {
        return "Step #3";
    }

    private void initComponents() {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 300, Short.MAX_VALUE));
    }
}
