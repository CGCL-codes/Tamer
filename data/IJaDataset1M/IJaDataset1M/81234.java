package configuration.models.game.trainers;

import game.gui.MyConfig;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.ImageIcon;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.ytoh.configurations.annotations.Property;
import org.ytoh.configurations.ui.CheckBox;
import configuration.CfgBeanAllowable;

public class RandomConfig implements CfgBeanAllowable, MyConfig, ActionListener {

    @Property(name = "Trainer allowed", description = "Include/exclude this trainer from the optimization process")
    @CheckBox
    private boolean allowed;

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    private static final long serialVersionUID = 1L;

    private int maxIterations;

    private int maxStagnation;

    private boolean debugOn;

    private double min;

    private double max;

    private double gradientWeight;

    private int cycle;

    private JTextField textMaxIterations;

    private JTextField textMaxStagnation;

    private JCheckBox chckDebugOn;

    private JTextField textMin;

    private JTextField textMax;

    private JTextField textGradientWeight;

    private JTextField textCycle;

    private JButton bSave;

    private JPanel p;

    private JPanel p1;

    /**inicialises parametres to its default values*/
    public RandomConfig() {
        maxIterations = 2000;
        maxStagnation = 500;
        debugOn = false;
        min = -20.0;
        max = 20.0;
        gradientWeight = 0.0;
        cycle = 10;
    }

    void makelabel(String name, GridBagLayout gridbag, GridBagConstraints c) {
        JLabel label = new JLabel(name);
        gridbag.setConstraints(label, c);
        p1.add(label);
    }

    public JPanel showPanel() {
        p = new JPanel();
        p1 = new JPanel();
        p.setLayout(new java.awt.BorderLayout());
        GridBagLayout grid = new GridBagLayout();
        p1.setLayout(grid);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        makelabel("Maximum iterations:", grid, c);
        c.gridx = 1;
        c.gridy = 0;
        textMaxIterations = new JTextField(Integer.toString(maxIterations));
        grid.setConstraints(textMaxIterations, c);
        p1.add(textMaxIterations);
        c.gridx = 0;
        c.gridy = 1;
        makelabel("Maximum stagnation:", grid, c);
        c.gridx = 1;
        c.gridy = 1;
        textMaxStagnation = new JTextField(Integer.toString(maxStagnation));
        grid.setConstraints(textMaxStagnation, c);
        p1.add(textMaxStagnation);
        c.gridx = 0;
        c.gridy = 2;
        makelabel("Variable minimum:", grid, c);
        c.gridx = 1;
        c.gridy = 2;
        textMin = new JTextField(Double.toString(min));
        grid.setConstraints(textMin, c);
        p1.add(textMin);
        c.gridx = 0;
        c.gridy = 3;
        makelabel("Variable maximum:", grid, c);
        c.gridx = 1;
        c.gridy = 3;
        textMax = new JTextField(Double.toString(max));
        grid.setConstraints(textMax, c);
        p1.add(textMax);
        c.gridx = 0;
        c.gridy = 4;
        makelabel("Gradient weight:", grid, c);
        c.gridx = 1;
        c.gridy = 4;
        textGradientWeight = new JTextField(Double.toString(gradientWeight));
        grid.setConstraints(textGradientWeight, c);
        p1.add(textGradientWeight);
        c.gridx = 0;
        c.gridy = 5;
        makelabel("Randomization cycle:", grid, c);
        c.gridx = 1;
        c.gridy = 5;
        textCycle = new JTextField(Integer.toString(cycle));
        grid.setConstraints(textCycle, c);
        p1.add(textCycle);
        c.gridx = 0;
        c.gridy = 6;
        makelabel("Debug on:", grid, c);
        c.gridx = 1;
        c.gridy = 6;
        chckDebugOn = new JCheckBox();
        chckDebugOn.setSelected(debugOn);
        grid.setConstraints(chckDebugOn, c);
        p1.add(chckDebugOn);
        p.add(p1, "North");
        bSave = new JButton("Save values");
        bSave.addActionListener(this);
        p.add(bSave, "South");
        p.setBorder(new javax.swing.border.TitledBorder("Configuration of Random training algorithm"));
        return p;
    }

    public void actionPerformed(ActionEvent e) {
        if ((e.getSource() == bSave)) {
            setValues();
        }
    }

    /** gets values from their text fields */
    public void setValues() {
        maxIterations = Integer.valueOf(textMaxIterations.getText());
        maxStagnation = Integer.valueOf(textMaxStagnation.getText());
        debugOn = chckDebugOn.isSelected();
        min = Double.valueOf(textMin.getText());
        max = Double.valueOf(textMax.getText());
        gradientWeight = Double.valueOf(textGradientWeight.getText());
        cycle = Integer.valueOf(textCycle.getText());
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public int getMaxStagnation() {
        return maxStagnation;
    }

    public boolean getDebugOn() {
        return debugOn;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getGradientWeight() {
        return gradientWeight;
    }

    public int getCycle() {
        return cycle;
    }

    /** Returns an ImageIcon, or null if the path was invalid.
     * @param path
     * @param description*/
    protected static ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = RandomConfig.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("File not found: " + path);
            return null;
        }
    }
}
