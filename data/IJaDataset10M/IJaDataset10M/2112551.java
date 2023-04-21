package desview.graphics.opengl.lines2D;

import java.awt.Color;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXLabel;

/**
 * This class represents a clickable popup.
 * @author Diones Rossetto.
 * @author Luiz Mello.
 * @since 23/05/2010.
 * @version 1.0
 */
public class Popup extends JXFrame {

    private static final long serialVersionUID = 98222515111L;

    /**
     * Default constructor of class Popup.
     */
    public Popup() {
        initComponents();
    }

    /**
     * Constructor of class Popup.
     * @param name the variable name.
     * @param value the variable current value.
     * @param upper the variable upper value.
     * @param lower the variable lower value.
     * @param status the variable status.
     */
    public Popup(String name, String value, String upper, String lower, int status) {
        initComponents();
        setUI(name, value, upper, lower, status);
    }

    private void initComponents() {
        variableName = new org.jdesktop.swingx.JXLabel();
        labelUpper = new org.jdesktop.swingx.JXLabel();
        upperValue = new org.jdesktop.swingx.JXLabel();
        labelValue = new org.jdesktop.swingx.JXLabel();
        currentValue = new org.jdesktop.swingx.JXLabel();
        labelLower = new org.jdesktop.swingx.JXLabel();
        lowerValue = new org.jdesktop.swingx.JXLabel();
        labelStatus = new org.jdesktop.swingx.JXLabel();
        inutil2 = new org.jdesktop.swingx.JXLabel();
        inutil1 = new org.jdesktop.swingx.JXLabel();
        status = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        variableName.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        variableName.setText(" ");
        getContentPane().add(variableName, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 373, -1));
        labelUpper.setText("Upper value");
        getContentPane().add(labelUpper, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 100, -1));
        upperValue.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        upperValue.setText(" ");
        getContentPane().add(upperValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 263, -1));
        labelValue.setText("Current value");
        getContentPane().add(labelValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 100, -1));
        currentValue.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        currentValue.setText(" ");
        getContentPane().add(currentValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 263, -1));
        labelLower.setText("Lower value");
        getContentPane().add(labelLower, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 100, -1));
        lowerValue.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lowerValue.setText(" ");
        getContentPane().add(lowerValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 263, -1));
        labelStatus.setText("Status");
        getContentPane().add(labelStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 91, -1));
        inutil2.setText(" ");
        getContentPane().add(inutil2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, 299, 10));
        inutil1.setText(" ");
        getContentPane().add(inutil1, new org.netbeans.lib.awtextra.AbsoluteConstraints(389, 11, 10, 100));
        status.setBackground(new java.awt.Color(0, 0, 255));
        status.setText(" ");
        status.setOpaque(true);
        getContentPane().add(status, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 90, 80, -1));
        pack();
    }

    private void formMouseClicked(java.awt.event.MouseEvent evt) {
        dispose();
    }

    private org.jdesktop.swingx.JXLabel currentValue;

    private org.jdesktop.swingx.JXLabel inutil1;

    private org.jdesktop.swingx.JXLabel inutil2;

    private org.jdesktop.swingx.JXLabel labelLower;

    private org.jdesktop.swingx.JXLabel labelStatus;

    private org.jdesktop.swingx.JXLabel labelUpper;

    private org.jdesktop.swingx.JXLabel labelValue;

    private org.jdesktop.swingx.JXLabel lowerValue;

    private javax.swing.JLabel status;

    private org.jdesktop.swingx.JXLabel upperValue;

    private org.jdesktop.swingx.JXLabel variableName;

    private void setUI(String name, String value, String upper, String lower, int statusI) {
        variableName.setText(name);
        lowerValue.setText(lower);
        upperValue.setText(upper);
        currentValue.setText(value);
        if (statusI == -1) {
            status.setBackground(Color.orange);
        } else if (statusI == 0) {
            status.setBackground(Color.blue);
        } else if (statusI == 1) {
            status.setBackground(Color.red);
        } else {
            status.setBackground(Color.black);
        }
    }

    public void hideComponents() {
        labelUpper.setVisible(false);
        upperValue.setVisible(false);
        labelValue.setText("Value");
        labelLower.setVisible(false);
        lowerValue.setVisible(false);
        labelStatus.setVisible(false);
        status.setVisible(false);
    }

    public JXLabel getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValueText(String currentValue) {
        this.currentValue.setText(currentValue);
    }

    public JXLabel getLowerValue() {
        return lowerValue;
    }

    public void setLowerValueText(String lowerValue) {
        this.lowerValue.setText(lowerValue);
    }

    public JXLabel getUpperValue() {
        return upperValue;
    }

    public void setUpperValueText(String upperValue) {
        this.upperValue.setText(upperValue);
    }

    public JXLabel getVariableName() {
        return variableName;
    }

    public void setVariableNameText(String variableName) {
        this.variableName.setText(variableName);
    }
}
