package blue.soundObject.editor.jmask;

import javax.swing.SpinnerNumberModel;
import blue.soundObject.jmask.Random;

/**
 * 
 * @author steven
 */
public class RandomEditor extends javax.swing.JPanel implements DurationSettable {

    Random random;

    /** Creates new form RandomEditor */
    public RandomEditor(final Random random) {
        this.random = random;
        initComponents();
        minSpinner.setModel(new SpinnerNumberModel(random.getMin(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1.0) {

            public void setValue(Object value) {
                if ((value == null) || !(value instanceof Number)) {
                    throw new IllegalArgumentException("illegal value");
                }
                double val = ((Double) value).doubleValue();
                if (val > random.getMax()) {
                    throw new IllegalArgumentException("illegal value");
                }
                super.setValue(value);
            }
        });
        maxSpinner.setModel(new SpinnerNumberModel(random.getMax(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1.0) {

            public void setValue(Object value) {
                if ((value == null) || !(value instanceof Number)) {
                    throw new IllegalArgumentException("illegal value");
                }
                double val = ((Double) value).doubleValue();
                if (val < random.getMin()) {
                    throw new IllegalArgumentException("illegal value");
                }
                super.setValue(value);
            }
        });
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        minSpinner = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        maxSpinner = new javax.swing.JSpinner();
        jLabel1.setText("Random");
        jLabel2.setText("Min");
        minSpinner.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                minSpinnerStateChanged(evt);
            }
        });
        jLabel3.setText("Max");
        maxSpinner.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                maxSpinnerStateChanged(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel1).add(layout.createSequentialGroup().add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(minSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 125, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel3).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(maxSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.linkSize(new java.awt.Component[] { maxSpinner, minSpinner }, org.jdesktop.layout.GroupLayout.HORIZONTAL);
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel2).add(minSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel3).add(maxSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    private void maxSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {
        double val = ((Double) maxSpinner.getValue()).doubleValue();
        if (val >= random.getMin()) {
            random.setMax(val);
        }
    }

    private void minSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {
        double val = ((Double) minSpinner.getValue()).doubleValue();
        if (val <= random.getMax()) {
            random.setMin(val);
        }
    }

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JSpinner maxSpinner;

    private javax.swing.JSpinner minSpinner;

    public void setDuration(double duration) {
    }
}
