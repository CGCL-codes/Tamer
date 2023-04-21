package net.ramponi.perfmeter;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import java.awt.FlowLayout;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import javax.swing.JRadioButton;

/**
 * @author ARAMPONI
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimpleGraphConfigurator extends JPanel implements Configurator {

    static final long serialVersionUID = -2265360255850381468L;

    private JPanel jPanel10 = null;

    private JLabel jLabelColor1 = null;

    private JButton jButtonColor1 = null;

    private JLabel jLabelColor2 = null;

    private JButton jButtonColor2 = null;

    private JLabel jLabelColor3 = null;

    private JButton jButtonColor3 = null;

    private JLabel jLabelBackground = null;

    private JButton jButtonBackground = null;

    private JLabel jLabelText = null;

    private JButton jButtonGrid = null;

    private JLabel jLabeGrid = null;

    private JButton jButtonText = null;

    private JCheckBox jCheckBoxShareScale = null;

    private JButton jButtonRestoreDefaults = null;

    private JPanel jPanel1 = null;

    /**
	 * This is the default constructor
	 */
    public SimpleGraphConfigurator() {
        super();
        initialize();
    }

    static Colors defaults = new Colors();

    Colors colors = defaults;

    boolean vertical = true;

    boolean shareScale = false;

    private JPanel jPanel2 = null;

    private JRadioButton jRadioButtonHorizontal = null;

    private JRadioButton jRadioButtonVertical = null;

    public static class Colors implements Serializable {

        static final long serialVersionUID = -7695885325902202146L;

        public Color color1 = new Color(51, 102, 153);

        public Color color2 = new Color(255, 153, 51);

        public Color color3 = new Color(153, 102, 153);

        public Color color4 = new Color(51, 153, 51);

        public Color background = new Color(0, 0, 0);

        ;

        public Color text = new Color(51, 153, 51);

        public Color grid = new Color(80, 80, 80);

        public Color[][] getColorsAsArray() {
            return new Color[][] { new Color[] {}, new Color[] { color1 }, new Color[] { color1, color2 }, new Color[] { color1, color2, color3 }, new Color[] { color1, color2, color3, color4 } };
        }
    }

    public Colors getColors() {
        return colors;
    }

    public JPanel getPanel() {
        return this;
    }

    public String getName() {
        return "SimpleGraph";
    }

    public String getDescription() {
        return "Simple Graph";
    }

    public void actionOk() {
        colors.color1 = getJButtonColor1().getBackground();
        colors.color2 = getJButtonColor2().getBackground();
        colors.color3 = getJButtonColor3().getBackground();
        colors.background = getJButtonBackground().getBackground();
        colors.text = getJButtonText().getBackground();
        colors.grid = getJButtonGrid().getBackground();
        vertical = jRadioButtonVertical.isSelected();
        shareScale = jCheckBoxShareScale.isSelected();
    }

    public void actionCancel() {
    }

    public void actionApply() {
        actionOk();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setSize(300, 306);
        this.setMaximumSize(new java.awt.Dimension(32767, 306));
        this.add(getJPanel2(), null);
        this.add(getJPanel10(), null);
        this.add(getJPanel1(), null);
    }

    /**
     * This method initializes jPanel10 
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanel10() {
        if (jPanel10 == null) {
            jLabelText = new JLabel();
            jLabelBackground = new JLabel();
            jLabelColor3 = new JLabel();
            jLabelColor2 = new JLabel();
            jLabelColor1 = new JLabel();
            jLabeGrid = new JLabel();
            GridLayout gridLayout1 = new GridLayout();
            jPanel10 = new JPanel();
            jPanel10.setLayout(gridLayout1);
            jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Colors", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            jLabelColor1.setText("Color1");
            gridLayout1.setRows(6);
            gridLayout1.setColumns(2);
            jLabelColor2.setText("Color2");
            jLabelColor3.setText("Color3");
            jLabelBackground.setText("Background");
            jLabelText.setText("Text");
            jPanel10.setMaximumSize(new java.awt.Dimension(32767, 160));
            jPanel10.setMinimumSize(new java.awt.Dimension(146, 160));
            jPanel10.setPreferredSize(new java.awt.Dimension(146, 160));
            jLabeGrid.setText("Grid");
            jPanel10.add(jLabelColor1, null);
            jPanel10.add(getJButtonColor1(), null);
            jPanel10.add(jLabelColor2, null);
            jPanel10.add(getJButtonColor2(), null);
            jPanel10.add(jLabelColor3, null);
            jPanel10.add(getJButtonColor3(), null);
            jPanel10.add(jLabelBackground, null);
            jPanel10.add(getJButtonBackground(), null);
            jPanel10.add(jLabeGrid, null);
            jPanel10.add(getJButtonGrid(), null);
            jPanel10.add(jLabelText, null);
            jPanel10.add(getJButtonText(), null);
        }
        return jPanel10;
    }

    /**
     * This method initializes jButtonColor1    
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonColor1() {
        if (jButtonColor1 == null) {
            jButtonColor1 = new JButton();
            jButtonColor1.setMaximumSize(new java.awt.Dimension(68, 16));
            jButtonColor1.setMinimumSize(new java.awt.Dimension(68, 16));
            jButtonColor1.setPreferredSize(new java.awt.Dimension(68, 16));
            jButtonColor1.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setButtonColor(jButtonColor1);
                }
            });
        }
        return jButtonColor1;
    }

    /**
     * This method initializes jButtonColor2    
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonColor2() {
        if (jButtonColor2 == null) {
            jButtonColor2 = new JButton();
            jButtonColor2.setMaximumSize(new java.awt.Dimension(68, 16));
            jButtonColor2.setMinimumSize(new java.awt.Dimension(68, 16));
            jButtonColor2.setPreferredSize(new java.awt.Dimension(68, 16));
            jButtonColor2.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setButtonColor(jButtonColor2);
                }
            });
        }
        return jButtonColor2;
    }

    /**
     * This method initializes jButtonColor3    
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonColor3() {
        if (jButtonColor3 == null) {
            jButtonColor3 = new JButton();
            jButtonColor3.setMaximumSize(new java.awt.Dimension(68, 16));
            jButtonColor3.setMinimumSize(new java.awt.Dimension(68, 16));
            jButtonColor3.setPreferredSize(new java.awt.Dimension(68, 16));
            jButtonColor3.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setButtonColor(jButtonColor3);
                }
            });
        }
        return jButtonColor3;
    }

    /**
     * This method initializes jButtonBackground    
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonBackground() {
        if (jButtonBackground == null) {
            jButtonBackground = new JButton();
            jButtonBackground.setMaximumSize(new java.awt.Dimension(68, 16));
            jButtonBackground.setMinimumSize(new java.awt.Dimension(68, 16));
            jButtonBackground.setPreferredSize(new java.awt.Dimension(68, 16));
            jButtonBackground.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setButtonColor(jButtonBackground);
                }
            });
        }
        return jButtonBackground;
    }

    /**
     * This method initializes jButtonGrid  
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonGrid() {
        if (jButtonGrid == null) {
            jButtonGrid = new JButton();
            jButtonGrid.setMaximumSize(new java.awt.Dimension(68, 16));
            jButtonGrid.setMinimumSize(new java.awt.Dimension(68, 16));
            jButtonGrid.setPreferredSize(new java.awt.Dimension(68, 16));
            jButtonGrid.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setButtonColor(jButtonGrid);
                }
            });
        }
        return jButtonGrid;
    }

    /**
     * This method initializes jButtonText  
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonText() {
        if (jButtonText == null) {
            jButtonText = new JButton();
            jButtonText.setMaximumSize(new java.awt.Dimension(68, 16));
            jButtonText.setMinimumSize(new java.awt.Dimension(68, 16));
            jButtonText.setPreferredSize(new java.awt.Dimension(68, 16));
            jButtonText.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setButtonColor(jButtonText);
                }
            });
        }
        return jButtonText;
    }

    /**
     * This method initializes jCheckBox	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private void setButtonColor(JButton button) {
        Color newColor = JColorChooser.showDialog(SimpleGraphConfigurator.this, "titi", button.getBackground());
        if (newColor != null) button.setBackground(newColor);
    }

    /**
     * This method initializes jButtonRestoreDefaults	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonRestoreDefaults() {
        if (jButtonRestoreDefaults == null) {
            jButtonRestoreDefaults = new JButton("Restore Default Colors");
            jButtonRestoreDefaults.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    mapColorsToButtons(defaults);
                }
            });
        }
        return jButtonRestoreDefaults;
    }

    /**
     * This method initializes jPanel1	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            FlowLayout flowLayout = new FlowLayout();
            flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
            jPanel1 = new JPanel();
            jPanel1.setLayout(flowLayout);
            jPanel1.add(getJButtonRestoreDefaults(), null);
        }
        return jPanel1;
    }

    private void mapColorsToButtons(Colors colors) {
        getJButtonColor1().setBackground(colors.color1);
        getJButtonColor2().setBackground(colors.color2);
        getJButtonColor3().setBackground(colors.color3);
        getJButtonBackground().setBackground(colors.background);
        getJButtonGrid().setBackground(colors.grid);
        getJButtonText().setBackground(colors.text);
    }

    public void map() {
        mapColorsToButtons(colors);
        jRadioButtonHorizontal.setSelected(!vertical);
        jRadioButtonVertical.setSelected(vertical);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        colors = (Colors) in.readObject();
        vertical = in.readBoolean();
        shareScale = in.readBoolean();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(colors);
        out.writeBoolean(vertical);
        out.writeBoolean(shareScale);
    }

    /**
     * This method initializes jPanel2	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            JLabel jLabel = new JLabel();
            jLabel.setText("Direction");
            GridLayout gridLayout2 = new GridLayout();
            gridLayout2.setRows(3);
            gridLayout2.setColumns(2);
            jPanel2 = new JPanel();
            jPanel2.setMaximumSize(new java.awt.Dimension(32767, 110));
            jPanel2.setMinimumSize(new java.awt.Dimension(238, 110));
            jPanel2.setPreferredSize(new java.awt.Dimension(238, 85));
            jPanel2.setLayout(gridLayout2);
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Parameters", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            jPanel2.add(jLabel, null);
            jRadioButtonHorizontal = new JRadioButton();
            jRadioButtonHorizontal.setText("Horizontal");
            jPanel2.add(jRadioButtonHorizontal, null);
            jLabel = new JLabel();
            jPanel2.add(jLabel, null);
            jRadioButtonVertical = new JRadioButton();
            jRadioButtonVertical.setText("Vertical");
            jPanel2.add(jRadioButtonVertical, null);
            ButtonGroup group = new ButtonGroup();
            group.add(jRadioButtonHorizontal);
            group.add(jRadioButtonVertical);
            jLabel = new JLabel();
            jLabel.setText("Scale");
            jPanel2.add(jLabel, null);
            jCheckBoxShareScale = new JCheckBox();
            jCheckBoxShareScale.setMaximumSize(new java.awt.Dimension(21, 16));
            jCheckBoxShareScale.setPreferredSize(new java.awt.Dimension(21, 16));
            jCheckBoxShareScale.setText("Share the same Scale");
            jCheckBoxShareScale.setMinimumSize(new java.awt.Dimension(21, 16));
            jPanel2.add(jCheckBoxShareScale, null);
        }
        return jPanel2;
    }

    public boolean isShareScale() {
        return shareScale;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void initFromArgs(String args[]) {
    }
}
