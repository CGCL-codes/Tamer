package org.patterncoder.wizard.panels;

import java.util.Observer;
import java.util.Observable;
import javax.swing.JEditorPane;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.MutableAttributeSet;
import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
import org.patterncoder.wizard.Wizard;
import org.patterncoder.pattern.PatternClass;
import org.patterncoder.pattern.PatternModel;
import org.patterncoder.PatternCoderException;

public class ClassPanel extends AbstractWizardPanel {

    private PatternModel model;

    private PatternClass thisComp;

    private Object compId;

    private Wizard parent;

    /**
     * Creates new form ClassPanel, supplying the PatternModel object being used and the Object id of the component to which this panel is to relate.
     * @param model the pattern model being used.
     * @param compId the id of the component to which this panel is related.
     */
    public ClassPanel(Wizard parent, PatternModel model, Object compId) {
        this.parent = parent;
        this.model = model;
        this.compId = compId;
        model.addObserver(this);
        this.thisComp = model.getPatternComp(compId);
        initComponents();
        setComponentFields();
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        pnlComponent = new javax.swing.JPanel();
        lblComp = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtComponent = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lblInfo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtInfo = new javax.swing.JEditorPane();
        patternImage1 = new org.patterncoder.PatternImage();
        lblPatternName = new javax.swing.JLabel();
        setLayout(new java.awt.GridBagLayout());
        pnlComponent.setLayout(new java.awt.GridBagLayout());
        pnlComponent.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        pnlComponent.setToolTipText("Rename");
        pnlComponent.setPreferredSize(new java.awt.Dimension(376, 187));
        lblComp.setFont(new java.awt.Font("Arial", 1, 12));
        lblComp.setText("Component:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 12;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 18, 0, 0);
        pnlComponent.add(lblComp, gridBagConstraints);
        lblName.setFont(new java.awt.Font("Arial", 1, 12));
        lblName.setText("Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 24;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 18, 0, 0);
        pnlComponent.add(lblName, gridBagConstraints);
        txtName.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNameKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 230;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 10, 0, 20);
        pnlComponent.add(txtName, gridBagConstraints);
        txtComponent.setText("comp");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(14, 12, 0, 20);
        pnlComponent.add(txtComponent, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(9, 20, 81, 0);
        pnlComponent.add(jPanel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = -7;
        gridBagConstraints.ipady = -16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(13, 9, 0, 13);
        add(pnlComponent, gridBagConstraints);
        jPanel1.setLayout(new java.awt.GridBagLayout());
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lblInfo.setFont(new java.awt.Font("Arial", 1, 12));
        lblInfo.setText("Component Role:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 481;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 10, 0, 16);
        jPanel1.add(lblInfo, gridBagConstraints);
        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 153, 153)));
        jScrollPane1.setFont(new java.awt.Font("Arial", 0, 10));
        txtInfo.setEditable(false);
        txtInfo.setContentType("text/html");
        txtInfo.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(txtInfo);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 557;
        gridBagConstraints.ipady = 97;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 10, 10, 10);
        jPanel1.add(jScrollPane1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = -30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 17, 13);
        add(jPanel1, gridBagConstraints);
        patternImage1.addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentResized(java.awt.event.ComponentEvent evt) {
                patternImage1ComponentResized(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 217;
        gridBagConstraints.ipady = 157;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 10, 0, 0);
        add(patternImage1, gridBagConstraints);
        lblPatternName.setFont(new java.awt.Font("Tahoma", 1, 14));
        lblPatternName.setText("Design Pattern name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 425;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 0, 0);
        add(lblPatternName, gridBagConstraints);
    }

    private void txtNameKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_ENTER) {
            if (parent.getWizardModel().getCurrentPanelDesc().getNextPanelID() == null) {
                parent.finish();
            } else {
                parent.next();
            }
        }
    }

    private void patternImage1ComponentResized(java.awt.event.ComponentEvent evt) {
        setImage(model.getImage());
    }

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JLabel lblComp;

    private javax.swing.JLabel lblInfo;

    private javax.swing.JLabel lblName;

    private javax.swing.JLabel lblPatternName;

    private org.patterncoder.PatternImage patternImage1;

    private javax.swing.JPanel pnlComponent;

    private javax.swing.JLabel txtComponent;

    private javax.swing.JEditorPane txtInfo;

    private javax.swing.JTextField txtName;

    /**
     * Called when the PatternModel, to which this class is registered a listener with, is changed.
     * This is use to update the image displayed throughout the wizard.
     * @param t
     * @param o
     */
    public void update(Observable t, Object o) {
        setImage(model.getImage());
    }

    /**
     * Sets the image of the current pattern
     * @param s the image name to display.
     */
    private void setImage(String s) {
        patternImage1.setImage(s);
    }

    private void setComponentFields() {
        lblPatternName.setText(model.getCurrentPatternName() + " Pattern");
        txtComponent.setText(thisComp.getCompType());
        txtName.setText(thisComp.getName());
        txtInfo.setText("<style type=\"text/css\">body " + "{font-size: 12pt; font-family: san-serif;color: #008800 }" + "</style>" + "<body>" + thisComp.getDescription() + "</body>");
        try {
            txtInfo.setCaretPosition(0);
        } catch (Exception e) {
        }
    }

    public void closingPanel() throws PatternCoderException {
        if (org.patterncoder.util.NameVerifier.verifyName(txtName.getText())) {
            model.getPatternComp(compId).setClassName(txtName.getText());
        } else {
            throw new PatternCoderException("The name you have entered is invalid. \nPlease enter a valid name to proceed");
        }
    }

    public void displayingPanel() throws PatternCoderException {
    }
}
