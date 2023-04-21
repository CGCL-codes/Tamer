package configuration.models.game.neurons;

import game.gui.MyConfig;
import java.awt.event.ItemEvent;
import javax.swing.JSlider;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import javax.swing.BoxLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import org.ytoh.configurations.annotations.Property;
import org.ytoh.configurations.ui.CheckBox;
import configuration.CfgBeanAllowable;

/**
 * Class for the IterPolyNeuron unit configuration
 */
public class CombiPolyConfig implements CfgBeanAllowable, MyConfig {

    private int startDegreePolynomial;

    private double addProp;

    private double delProp;

    private double elMul;

    private double penRatio;

    private double mutDegProp;

    private transient JSlider sdp, ap, dp, mp, md, em, pr;

    private transient JCheckBox pc, es;

    private int maxDeg;

    private boolean penalizeComplexity;

    private boolean eStop;

    @Property(name = "Neuron allowed", description = "Include/exclude this neuron from the GAMEconstruction process")
    @CheckBox
    private boolean allowed;

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    /**
     * inicialises parametres to its default values
     */
    public CombiPolyConfig() {
        startDegreePolynomial = 1;
        addProp = 5;
        delProp = 1;
        mutDegProp = 20;
        maxDeg = 20;
        elMul = 0.5;
        penRatio = 50;
        penalizeComplexity = false;
        eStop = true;
    }

    JPanel createPanel(JSlider slider, int multiplication, String text) {
        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        p1.add(slider);
        ChangeListener s1 = new SliderListener(p1, text, multiplication);
        slider.addChangeListener(s1);
        s1.stateChanged(new ChangeEvent(slider));
        return p1;
    }

    public javax.swing.JPanel showPanel() {
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        panel1.setBorder(new EmptyBorder(10, 10, 10, 10));
        sdp = new JSlider(JSlider.HORIZONTAL, 0, 10, startDegreePolynomial);
        panel1.add(createPanel(sdp, 1, "Starting degree of polynomials (new units): "));
        md = new JSlider(JSlider.HORIZONTAL, 1, 100, maxDeg);
        panel1.add(createPanel(md, 1, "Max degree of polynomial that can be reached: "));
        ap = new JSlider(JSlider.HORIZONTAL, 0, 1000, (int) (addProp * 10));
        panel1.add(createPanel(ap, 10, "Probability of adding element: "));
        dp = new JSlider(JSlider.HORIZONTAL, 0, 1000, (int) (delProp * 10));
        panel1.add(createPanel(dp, 10, "Probability of deleting element: "));
        mp = new JSlider(JSlider.HORIZONTAL, 0, 1000, (int) (mutDegProp * 10));
        panel1.add(createPanel(mp, 10, "Probability of degree mutation: "));
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel2.setBorder(new TitledBorder("Penalty for the complexity of transfer function"));
        pc = new JCheckBox();
        pc.setText("Penalize complexity");
        pc.setSelected(penalizeComplexity);
        pc.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                myitemStateChanged(e);
            }
        });
        panel2.add(pc);
        em = new JSlider(JSlider.HORIZONTAL, 0, 1000, (int) (elMul * 100));
        pr = new JSlider(JSlider.HORIZONTAL, 1, 10000, (int) (penRatio * 10));
        panel2.add(createPanel(pr, 10, "Penalization as unitRMS/x: lower number,higher penalty"));
        panel2.add(createPanel(em, 100, "Penalization for number of elements: "));
        em.setEnabled(penalizeComplexity);
        pr.setEnabled(penalizeComplexity);
        es = new JCheckBox();
        es.setText("Early stopping");
        es.setSelected(eStop);
        panel2.add(es);
        panel1.add(panel2);
        return panel1;
    }

    public void setValues() {
        if (sdp != null) {
            startDegreePolynomial = sdp.getValue();
        }
        if (md != null) {
            maxDeg = md.getValue();
        }
        if (ap != null) {
            addProp = ap.getValue() / 10.0;
        }
        if (dp != null) {
            delProp = dp.getValue() / 10.0;
        }
        if (mp != null) {
            mutDegProp = mp.getValue() / 10.0;
        }
        if (em != null) {
            elMul = em.getValue() / 100.0;
        }
        if (pr != null) {
            penRatio = pr.getValue() / 10.0;
        }
        if (pc != null) {
            penalizeComplexity = pc.isSelected();
        }
        if (es != null) {
            eStop = es.isSelected();
        }
    }

    private void myitemStateChanged(ItemEvent e) {
        if (em != null) em.setEnabled(pc.isSelected());
        if (pr != null) pr.setEnabled(pc.isSelected());
    }

    /**
     * function to pass the values of parameters to the unit
     * @return
     * @return
     * @return
     */
    public int getStartDegreePolynomial() {
        return startDegreePolynomial;
    }

    public int getAddElementProbability() {
        return (int) (addProp * 10);
    }

    public int getDelElementProbability() {
        return (int) (delProp * 10);
    }

    public int getMutateDegreeProbability() {
        return (int) (mutDegProp * 10);
    }

    public boolean isEStop() {
        return eStop;
    }

    public boolean isPenalizeComplexity() {
        return penalizeComplexity;
    }

    /**
     * getMaxDegree
     *
     * @return int Maximal degree of polynoms (simplification occurs with increasing probability when approaching maxDegree)
     */
    public int getMaxDegree() {
        return maxDeg;
    }

    /**
     * getPenaltyRatio
     *
     * @return double Penalty Ratio - penalization as the fraction of unit's RMS
     */
    public double getPenaltyRatio() {
        return penRatio;
    }

    /**
     * getElementPenaltyMultiplier
     *
     * @return int Penalization for number of elements
     */
    public double getElementPenaltyMultiplier() {
        return elMul;
    }

    class SliderListener implements ChangeListener {

        JPanel tf;

        String ss;

        int mul;

        public SliderListener(JPanel f, String s, int multiply) {
            tf = f;
            ss = s;
            mul = multiply;
        }

        public void stateChanged(ChangeEvent e) {
            JSlider s1 = (JSlider) e.getSource();
            tf.setBorder(new TitledBorder(ss + "  " + Double.toString((double) s1.getValue() / mul)));
            setValues();
        }
    }
}
