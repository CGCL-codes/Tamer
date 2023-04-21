package gui;

import java.awt.event.ActionListener;
import javax.swing.event.ChangeListener;

/**
 *
 * @author  dwyatte
 */
public class ThresholdPanel extends javax.swing.JPanel {

    private boolean dirty = false;

    FitEyeModelSetup parent;

    private int crThresh = 128;

    private int pupilThresh = 128;

    public void setCrThresh(int crThresh) {
        this.crThresh = crThresh;
        this.crThreshSlider.setValue(crThresh);
    }

    public void setPupilThresh(int pupilThresh) {
        this.pupilThresh = pupilThresh;
        this.pupilThreshSlider.setValue(pupilThresh);
    }

    /** @return true when threshold values change but no pupil estimation is run */
    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean isDirty) {
        this.dirty = isDirty;
    }

    public enum ThresholdType {

        NO_THRESH_TYPE, PUPIL_THRESH_TYPE, CR_THRESH_TYPE
    }

    ThresholdType threshType = ThresholdType.NO_THRESH_TYPE;

    /** Creates new form ThresholdPanel */
    public ThresholdPanel() {
        initComponents();
        setNoThreshold();
    }

    public void setParent(FitEyeModelSetup parent) {
        this.parent = parent;
    }

    public void setNoThreshold() {
        noThreshButton.setSelected(true);
        threshType = ThresholdType.NO_THRESH_TYPE;
    }

    public ThresholdType getThresholdType() {
        return threshType;
    }

    public int getCRThresh() {
        return crThresh;
    }

    public int getPupilThresh() {
        return pupilThresh;
    }

    public void addPupilThreshSliderStateChangeListener(ChangeListener listener) {
        pupilThreshSlider.addChangeListener(listener);
    }

    public void addEstimatePupilLocationButtonActionListener(ActionListener listener) {
        this.estimatePupilLocationButton.addActionListener(listener);
    }

    public void addLoadPupilLocationButtonActionListener(ActionListener listener) {
        this.loadEstimatedPupilLocationButton.addActionListener(listener);
    }

    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        crThreshButton = new javax.swing.JRadioButton();
        estimatePupilLocationButton = new javax.swing.JButton();
        noThreshButton = new javax.swing.JRadioButton();
        threshTypeLabel = new javax.swing.JLabel();
        loadEstimatedPupilLocationButton = new javax.swing.JButton();
        crThreshSlider = new javax.swing.JSlider();
        crThreshLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        pupilThreshLabel = new javax.swing.JLabel();
        pupilThreshSlider = new javax.swing.JSlider();
        pupilThreshButton = new javax.swing.JRadioButton();
        setPreferredSize(new java.awt.Dimension(50, 50));
        buttonGroup1.add(crThreshButton);
        crThreshButton.setText("CR");
        crThreshButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crThreshButtonActionPerformed(evt);
            }
        });
        estimatePupilLocationButton.setText("Estimate pupil locations");
        estimatePupilLocationButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                estimatePupilLocationButtonActionPerformed(evt);
            }
        });
        buttonGroup1.add(noThreshButton);
        noThreshButton.setText("None");
        noThreshButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noThreshButtonActionPerformed(evt);
            }
        });
        threshTypeLabel.setText("Threshold Type");
        loadEstimatedPupilLocationButton.setText("Load pupil locations");
        loadEstimatedPupilLocationButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadEstimatedPupilLocationButtonActionPerformed(evt);
            }
        });
        crThreshSlider.setMaximum(255);
        crThreshSlider.setValue(204);
        crThreshSlider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                crThreshSliderStateChanged(evt);
            }
        });
        crThreshLabel.setText("CR Threshold");
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("resources/ThresholdPanel");
        jLabel1.setText(bundle.getString("Instruction Label"));
        jLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pupilThreshLabel.setText("Pupil Threshold");
        pupilThreshSlider.setMaximum(255);
        pupilThreshSlider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                pupilThreshSliderStateChanged(evt);
            }
        });
        buttonGroup1.add(pupilThreshButton);
        pupilThreshButton.setText("Pupil");
        pupilThreshButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pupilThreshButtonActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(loadEstimatedPupilLocationButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(estimatePupilLocationButton)).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(threshTypeLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(crThreshLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(pupilThreshLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(21, 21, 21).add(noThreshButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(pupilThreshButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(crThreshButton)).add(crThreshSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, pupilThreshSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE))).add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(pupilThreshSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(pupilThreshLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(crThreshLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE).add(crThreshSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(threshTypeLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(noThreshButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(pupilThreshButton).add(crThreshButton))).add(32, 32, 32).add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(loadEstimatedPupilLocationButton).add(estimatePupilLocationButton)).add(213, 213, 213)));
    }

    private void crThreshSliderStateChanged(javax.swing.event.ChangeEvent evt) {
        crThresh = crThreshSlider.getValue();
        parent.setFrame(parent.getFrame());
        this.dirty = true;
    }

    private void pupilThreshSliderStateChanged(javax.swing.event.ChangeEvent evt) {
        pupilThresh = pupilThreshSlider.getValue();
        parent.setFrame(parent.getFrame());
        this.dirty = true;
    }

    private void crThreshButtonActionPerformed(java.awt.event.ActionEvent evt) {
        threshType = ThresholdType.CR_THRESH_TYPE;
        parent.setFrame(parent.getFrame());
    }

    private void noThreshButtonActionPerformed(java.awt.event.ActionEvent evt) {
        threshType = ThresholdType.NO_THRESH_TYPE;
        parent.setFrame(parent.getFrame());
    }

    private void pupilThreshButtonActionPerformed(java.awt.event.ActionEvent evt) {
        threshType = ThresholdType.PUPIL_THRESH_TYPE;
        parent.setFrame(parent.getFrame());
    }

    private void estimatePupilLocationButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.dirty = false;
    }

    private void loadEstimatedPupilLocationButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.dirty = false;
    }

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.JRadioButton crThreshButton;

    private javax.swing.JLabel crThreshLabel;

    private javax.swing.JSlider crThreshSlider;

    private javax.swing.JButton estimatePupilLocationButton;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JButton loadEstimatedPupilLocationButton;

    private javax.swing.JRadioButton noThreshButton;

    private javax.swing.JRadioButton pupilThreshButton;

    private javax.swing.JLabel pupilThreshLabel;

    private javax.swing.JSlider pupilThreshSlider;

    private javax.swing.JLabel threshTypeLabel;
}
