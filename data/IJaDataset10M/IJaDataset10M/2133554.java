package configuration.models.game.neurons;

import game.gui.MyConfig;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import org.ytoh.configurations.annotations.Property;
import org.ytoh.configurations.ui.CheckBox;
import configuration.CfgBeanAllowable;

/**
 * Class for the IterPolyNeuron unit configuration
 */
public class PolyFractConfig implements CfgBeanAllowable, MyConfig {

    @Property(name = "Neuron allowed", description = "Include/exclude this neuron from the GAMEconstruction process")
    @CheckBox
    private boolean allowed;

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    private int coefs;

    private transient JTextField coef;

    /**
     * inicialises parametres to its default values
     */
    public PolyFractConfig() {
        coefs = 1;
    }

    public JPanel showPanel() {
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        coef = new JTextField(Integer.toString(coefs));
        p.add(new JLabel("Extra elements in polynomial equation:"));
        p.add(coef);
        return p;
    }

    public void setValues() {
        if (coef != null) {
            coefs = Integer.valueOf(coef.getText());
        }
    }

    /**
     * function to pass the values of parameters to the unit
     */
    public int getCoef() {
        return coefs;
    }
}
