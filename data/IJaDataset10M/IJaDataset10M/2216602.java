package dcartes.image;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author  dcartes
 */
public class AffinePanel extends javax.swing.JPanel {

    protected JButton affineButtonCancel;

    protected JButton affineButtonOk;

    protected JComboBox interpolationCombo;

    protected JLabel interpolationLabel;

    protected JLabel scaleXLabel;

    protected JTextField scaleXTextField;

    protected JLabel scaleYLabel;

    protected JTextField scaleYTextField;

    protected JLabel shearXLabel;

    protected JTextField shearXTextField;

    protected JLabel shearYLabel;

    protected JTextField shearYTextField;

    protected JLabel translateXLabel;

    protected JTextField translateXTextField;

    protected JLabel translateYLabel;

    protected JTextField translateYTextField;

    protected static final int INTERP_NEAREST = 0;

    protected static final int INTERP_BILINEAR = 1;

    protected static final int INTERP_BICUBIC = 2;

    protected static final int INTERP_BICUBIC_2 = 3;

    protected final int[] interpolationChoice = { INTERP_NEAREST, INTERP_BILINEAR, INTERP_BICUBIC, INTERP_BICUBIC_2 };

    protected String[] affineTransformString = { "scaleX", "scaleY", "shearX", "shearY", "translateX", "translateY" };

    protected float scaleX, scaleY, shearX, shearY, translateX, translateY;

    protected int affineInterpolation;

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public AffinePanel() {
        GridBagConstraints gridBagConstraints;
        scaleXLabel = new JLabel();
        scaleYLabel = new JLabel();
        shearXLabel = new JLabel();
        shearYLabel = new JLabel();
        translateXLabel = new JLabel();
        translateYLabel = new JLabel();
        setScaleXTextField(new JTextField());
        setScaleYTextField(new JTextField());
        setShearXTextField(new JTextField());
        setShearYTextField(new JTextField());
        translateXTextField = new JTextField();
        translateYTextField = new JTextField();
        setAffineButtonOk(new JButton());
        setAffineButtonCancel(new JButton());
        interpolationCombo = new JComboBox();
        interpolationLabel = new JLabel();
        setLayout(new GridBagLayout());
        setToolTipText("AffineTransform Panel");
        setPreferredSize(new Dimension(300, 300));
        scaleXLabel.setFont(new Font("Tahoma", 0, 11));
        scaleXLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scaleXLabel.setLabelFor(getScaleXTextField());
        scaleXLabel.setText("Scale X :");
        scaleXLabel.setToolTipText("The x coordinate scale element!");
        scaleXLabel.setDoubleBuffered(true);
        scaleXLabel.setOpaque(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(7, 7, 7, 7);
        add(scaleXLabel, gridBagConstraints);
        scaleYLabel.setFont(new Font("Tahoma", 0, 11));
        scaleYLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scaleYLabel.setLabelFor(getScaleYTextField());
        scaleYLabel.setText("Scale Y :");
        scaleYLabel.setToolTipText("The y coordinate scale element!");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(8, 8, 8, 8);
        add(scaleYLabel, gridBagConstraints);
        shearXLabel.setFont(new Font("Tahoma", 0, 11));
        shearXLabel.setHorizontalAlignment(SwingConstants.CENTER);
        shearXLabel.setLabelFor(getShearXTextField());
        shearXLabel.setText("Shear X :");
        shearXLabel.setToolTipText("The x coordinate shear element!");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(7, 7, 7, 7);
        add(shearXLabel, gridBagConstraints);
        shearYLabel.setFont(new Font("Tahoma", 0, 11));
        shearYLabel.setHorizontalAlignment(SwingConstants.CENTER);
        shearYLabel.setLabelFor(getShearYTextField());
        shearYLabel.setText("Shear Y :");
        shearYLabel.setToolTipText("The y coordinate shear element!");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new Insets(7, 7, 7, 7);
        add(shearYLabel, gridBagConstraints);
        translateXLabel.setFont(new Font("Tahoma", 0, 11));
        translateXLabel.setHorizontalAlignment(SwingConstants.CENTER);
        translateXLabel.setLabelFor(translateXTextField);
        translateXLabel.setText("Translate X :");
        translateXLabel.setToolTipText("The x coordinate translate element!");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new Insets(7, 7, 7, 7);
        add(translateXLabel, gridBagConstraints);
        translateYLabel.setFont(new Font("Tahoma", 0, 11));
        translateYLabel.setHorizontalAlignment(SwingConstants.CENTER);
        translateYLabel.setLabelFor(translateYTextField);
        translateYLabel.setText("Translate Y :");
        translateYLabel.setToolTipText("The y coordinate translate element!");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        add(translateYLabel, gridBagConstraints);
        getScaleXTextField().setColumns(8);
        getScaleXTextField().setFont(new Font("Tahoma", 0, 11));
        getScaleXTextField().setText("1.0");
        getScaleXTextField().setToolTipText("The x coordinate scale element!");
        getScaleXTextField().setDoubleBuffered(true);
        getScaleXTextField().setDragEnabled(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(6, 6, 6, 6);
        add(getScaleXTextField(), gridBagConstraints);
        getScaleYTextField().setColumns(8);
        getScaleYTextField().setFont(new Font("Tahoma", 0, 11));
        getScaleYTextField().setText("1.0");
        getScaleYTextField().setToolTipText("The y coordinate scale element!");
        getScaleYTextField().setDoubleBuffered(true);
        getScaleYTextField().setDragEnabled(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(7, 7, 7, 7);
        add(getScaleYTextField(), gridBagConstraints);
        getShearXTextField().setColumns(8);
        getShearXTextField().setFont(new Font("Tahoma", 0, 11));
        getShearXTextField().setText("0.0");
        getShearXTextField().setToolTipText("The x coordinate shear element!");
        getShearXTextField().setDoubleBuffered(true);
        getShearXTextField().setDragEnabled(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        add(getShearXTextField(), gridBagConstraints);
        getShearYTextField().setColumns(8);
        getShearYTextField().setFont(new Font("Tahoma", 0, 11));
        getShearYTextField().setText("0.0");
        getShearYTextField().setToolTipText("The y coordinate shear element!");
        getShearYTextField().setDoubleBuffered(true);
        getShearYTextField().setDragEnabled(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new Insets(6, 6, 6, 6);
        add(getShearYTextField(), gridBagConstraints);
        translateXTextField.setColumns(8);
        translateXTextField.setFont(new Font("Tahoma", 0, 11));
        translateXTextField.setText("0.0");
        translateXTextField.setToolTipText("The x coordinate translate element!");
        translateXTextField.setDoubleBuffered(true);
        translateXTextField.setDragEnabled(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new Insets(6, 6, 6, 6);
        add(translateXTextField, gridBagConstraints);
        translateYTextField.setColumns(8);
        translateYTextField.setFont(new Font("Tahoma", 0, 11));
        translateYTextField.setText("0.0");
        translateYTextField.setToolTipText("The y coordinate translate element!");
        translateYTextField.setDoubleBuffered(true);
        translateYTextField.setDragEnabled(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new Insets(6, 6, 6, 6);
        add(translateYTextField, gridBagConstraints);
        getAffineButtonOk().setFont(new Font("Tahoma", 0, 11));
        getAffineButtonOk().setText("Ok");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new Insets(8, 8, 8, 8);
        add(getAffineButtonOk(), gridBagConstraints);
        getAffineButtonCancel().setFont(new Font("Tahoma", 0, 11));
        getAffineButtonCancel().setText("Cancel");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new Insets(8, 8, 8, 8);
        add(getAffineButtonCancel(), gridBagConstraints);
        interpolationCombo.setMaximumRowCount(4);
        interpolationCombo.setModel(new DefaultComboBoxModel(new String[] { "BILINEAR", "NEAREST", "BICUBIC", "BICUBIC2" }));
        interpolationCombo.setToolTipText("Make your choice!");
        interpolationCombo.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        interpolationCombo.setDoubleBuffered(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        add(interpolationCombo, gridBagConstraints);
        interpolationLabel.setFont(new Font("Tahoma", 0, 11));
        interpolationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        interpolationLabel.setLabelFor(interpolationCombo);
        interpolationLabel.setText("Interpolation:");
        interpolationLabel.setToolTipText("The interpolation method for resampling!");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(interpolationLabel, gridBagConstraints);
        getAffineButtonOk().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                try {
                    setScaleX(Float.parseFloat(getScaleXTextField().getText()));
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, new StringBuffer().append(nfe.getMessage()).append(" scale x ").toString(), "Ooops!", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    setScaleY(Float.parseFloat(getScaleYTextField().getText()));
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, new StringBuffer().append(nfe.getMessage()).append(" scale y ").toString(), "Ooops!", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    setShearX(Float.parseFloat(getShearXTextField().getText()));
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, new StringBuffer().append(nfe.getMessage()).append(" shear x ").toString(), "Ooops!", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    setShearY(Float.parseFloat(getShearYTextField().getText()));
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, new StringBuffer().append(nfe.getMessage()).append(" shear y ").toString(), "Ooops!", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    setTranslateX(Float.parseFloat(translateYTextField.getText()));
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, new StringBuffer().append(nfe.getMessage()).append(" translate x ").toString(), "Ooops!", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    setTranslateY(Float.parseFloat(translateYTextField.getText()));
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, new StringBuffer().append(nfe.getMessage()).append(" translate y ").toString(), "Ooops!", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    setAffineInterpolation(interpolationChoice[interpolationCombo.getSelectedIndex()]);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, new StringBuffer().append(nfe.getMessage()).append(" interpolation! ").toString(), "Ooops!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public JButton getAffineButtonOk() {
        return affineButtonOk;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public JButton getAffineButtonCancel() {
        return affineButtonCancel;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public void setAffineButtonCancel(JButton affineButtonCancel) {
        this.affineButtonCancel = affineButtonCancel;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public void setAffineButtonOk(JButton affineButtonOk) {
        this.affineButtonOk = affineButtonOk;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public JTextField getScaleXTextField() {
        return scaleXTextField;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public void setScaleXTextField(JTextField scaleXTextField) {
        this.scaleXTextField = scaleXTextField;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public JTextField getScaleYTextField() {
        return scaleYTextField;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public void setScaleYTextField(JTextField scaleYTextField) {
        this.scaleYTextField = scaleYTextField;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public JTextField getShearXTextField() {
        return shearXTextField;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public void setShearXTextField(JTextField shearXTextField) {
        this.shearXTextField = shearXTextField;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public JTextField getShearYTextField() {
        return shearYTextField;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public void setShearYTextField(JTextField shearYTextField) {
        this.shearYTextField = shearYTextField;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public float getScaleX() {
        return scaleX;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public float getScaleY() {
        return scaleY;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public float getShearX() {
        return shearX;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public void setShearX(float shearX) {
        this.shearX = shearX;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public float getShearY() {
        return shearY;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public void setShearY(float shearY) {
        this.shearY = shearY;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public float getTranslateX() {
        return translateX;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public void setTranslateX(float translateX) {
        this.translateX = translateX;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public float getTranslateY() {
        return translateY;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public void setTranslateY(float translateY) {
        this.translateY = translateY;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public int getAffineInterpolation() {
        return affineInterpolation;
    }

    /**
     * M�todo vazio servindo apenas para refer�ncia
     */
    public void setAffineInterpolation(int affineInterpolation) {
        this.affineInterpolation = affineInterpolation;
    }
}
