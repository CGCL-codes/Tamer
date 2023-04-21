package game.trainers;

import game.gui.MyConfig;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Miroslav Janosik (janosm2@fel.cvut.cz)
 */
public class DifferentialEvolutionConfig implements MyConfig {

    int NP = 10;

    double F = 0.6;

    double CR = 0.85;

    int GEN = 300;

    int MAXGENWITHOUTCHANGE = 20;

    double INITMAX = 10;

    private transient JSlider np, f, cr, gen, maxgenwchange, initmax;

    /**
     * Creates a new instance of DifferentialEvolutionConfig
     */
    public DifferentialEvolutionConfig() {
    }

    /**
     * returns JPanel with a slider
     */
    private JPanel createPanel(JSlider slider, int multiplication, String text) {
        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        p1.add(slider);
        ChangeListener s1 = new SliderListener(p1, text, multiplication);
        slider.addChangeListener(s1);
        s1.stateChanged(new ChangeEvent(slider));
        return p1;
    }

    /**
     * creates main configuration JPanel
     */
    public JPanel showPanel() {
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        panel1.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.np = new JSlider(JSlider.HORIZONTAL, 4, 200, this.NP);
        panel1.add(createPanel(this.np, 1, "NP (Number of Parents) - size of population: "));
        this.f = new JSlider(JSlider.HORIZONTAL, 0, 200, (int) (this.F * 100));
        panel1.add(createPanel(this.f, 100, "F (mutation constant): "));
        this.cr = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) (this.CR * 100));
        panel1.add(createPanel(this.cr, 100, "CR (Crossover rating)\n[0.1 - like parrent; 0.9 - like mutatant]: "));
        this.gen = new JSlider(JSlider.HORIZONTAL, 3, 500, this.GEN);
        panel1.add(createPanel(this.gen, 1, "GEN (maximal GENeration count): "));
        this.maxgenwchange = new JSlider(JSlider.HORIZONTAL, 10, 100, this.MAXGENWITHOUTCHANGE);
        panel1.add(createPanel(this.maxgenwchange, 1, "END_GEN (End if last n generation aren't better): "));
        this.initmax = new JSlider(JSlider.HORIZONTAL, 10, 3000, (int) (this.INITMAX * 100));
        panel1.add(createPanel(this.initmax, 100, "X (random value for first generations initialization: <-rnd(X) , +rnd(X)> ): "));
        return panel1;
    }

    /**
     * function to pass the values of parameters to the unit
     */
    public int getDraw() {
        return 10;
    }

    /**
     * function to pass the values of parameters to the unit
     */
    public int getRec() {
        return 100;
    }

    /**
     * upgrades instantion parameters from gui components
     */
    public void setValues() {
        if (this.np != null) {
            this.NP = np.getValue();
        }
        if (this.f != null) {
            this.F = f.getValue() / 100.0;
        }
        if (this.cr != null) {
            this.CR = cr.getValue() / 100.0;
        }
        if (this.gen != null) {
            this.GEN = gen.getValue();
        }
        if (this.maxgenwchange != null) {
            this.MAXGENWITHOUTCHANGE = maxgenwchange.getValue();
        }
        if (this.initmax != null) {
            this.INITMAX = this.initmax.getValue() / 100.0;
        }
    }

    /**
     * slider listener
     */
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
