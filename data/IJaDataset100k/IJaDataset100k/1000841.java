package net.sourceforge.pseudoq.gui;

import java.util.Hashtable;
import javax.swing.JComponent;

/**
 * Second page of the {@link NewPuzzleWizard}.  This would be an inner class,
 * except Netbeans' GUI form designer cannot handle that.
 * @author <a href="http://sourceforge.net/users/stevensa">Andrew Stevens</a>
 */
public class NewPuzzleWizardStep2 extends org.pietschy.wizard.PanelWizardStep {

    private NewPuzzleWizard.Model model;

    /**
     * Creates new form NewPuzzleWizardStep1 
     */
    public NewPuzzleWizardStep2() {
        super("Generate or Design?", "Select whether to generate a random puzzle or design one yourself");
        initComponents();
    }

    public void applyState() throws org.pietschy.wizard.InvalidStateException {
        model.setGenerate(jRadioButton1.isSelected());
        model.setSymmetrical(jRadioButton3.isSelected());
        model.setDifficulty(jSlider1.getValue());
        super.applyState();
    }

    public void init(org.pietschy.wizard.WizardModel wizardModel) {
        this.model = (NewPuzzleWizard.Model) wizardModel;
        super.init(wizardModel);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel3.setText("Easy");
        jLabel4.setText("Medium");
        jLabel5.setText("Hard");
        jLabel6.setText("Fiendish");
        setLayout(new java.awt.BorderLayout());
        setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
        setComplete(true);
        jLabel1.setText("Creation method:");
        add(jLabel1, java.awt.BorderLayout.NORTH);
        jPanel1.setLayout(new java.awt.BorderLayout());
        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setMnemonic('g');
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Generate automatically");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jRadioButton1, java.awt.BorderLayout.NORTH);
        jPanel2.setLayout(new java.awt.GridBagLayout());
        jPanel2.setBorder(new javax.swing.border.EtchedBorder());
        jLabel7.setText("Placement pattern:");
        jPanel2.add(jLabel7, new java.awt.GridBagConstraints());
        buttonGroup2.add(jRadioButton3);
        jRadioButton3.setSelected(true);
        jRadioButton3.setText("Symmetrical");
        jPanel2.add(jRadioButton3, new java.awt.GridBagConstraints());
        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setText("Random");
        jPanel2.add(jRadioButton4, new java.awt.GridBagConstraints());
        jLabel2.setText("Difficulty:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel2, gridBagConstraints);
        jSlider1.setMajorTickSpacing(2);
        jSlider1.setMaximum(7);
        jSlider1.setMinimum(1);
        jSlider1.setMinorTickSpacing(1);
        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        jSlider1.setSnapToTicks(true);
        jSlider1.setValue(3);
        Hashtable<Integer, JComponent> dictionary = new Hashtable<Integer, JComponent>();
        dictionary.put(Integer.valueOf(1), jLabel3);
        dictionary.put(Integer.valueOf(3), jLabel4);
        dictionary.put(Integer.valueOf(5), jLabel5);
        dictionary.put(Integer.valueOf(7), jLabel6);
        jSlider1.setLabelTable(dictionary);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        jPanel2.add(jSlider1, gridBagConstraints);
        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);
        add(jPanel1, java.awt.BorderLayout.CENTER);
        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setMnemonic('d');
        jRadioButton2.setText("Design manually");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        add(jRadioButton2, java.awt.BorderLayout.SOUTH);
    }

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        jRadioButton3.setEnabled(true);
        jRadioButton4.setEnabled(true);
        jSlider1.setEnabled(true);
    }

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        jRadioButton3.setEnabled(false);
        jRadioButton4.setEnabled(false);
        jSlider1.setEnabled(false);
    }

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.ButtonGroup buttonGroup2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JRadioButton jRadioButton1;

    private javax.swing.JRadioButton jRadioButton2;

    private javax.swing.JRadioButton jRadioButton3;

    private javax.swing.JRadioButton jRadioButton4;

    private javax.swing.JSlider jSlider1;
}
