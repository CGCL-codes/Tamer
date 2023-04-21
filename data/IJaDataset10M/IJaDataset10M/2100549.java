package synthlabgui.presentation.configPanel.knob.numberKnob;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import java.util.Observable;
import javax.swing.JPanel;
import synthlab.api.Port;
import synthlabgui.presentation.configPanel.AbstractConfigPanel;
import synthlabgui.presentation.configPanel.knob.AbstractKnob;
import synthlabgui.presentation.configPanel.knob.KnobEvent;
import synthlabgui.presentation.configPanel.knob.KnobListener;

public class NumberKnobPanel extends JPanel implements KnobListener, AbstractConfigPanel {

    private static final long serialVersionUID = 1L;

    /**
     * valeur actuelle
     * */
    protected double value;

    protected double maxValue;

    protected double minValue;

    protected String unit;

    protected String pattern;

    protected NumberKnob knob;

    protected int numberDisplaySize = 20;

    public static int TITLE_HEIGHT = 10;

    protected Dimension size = new Dimension(AbstractKnob.size.width + 35 + 2, 20 + 2 + AbstractKnob.size.height + TITLE_HEIGHT + numberDisplaySize);

    protected String title;

    protected boolean continous = true;

    protected Port inputPort;

    /**
     * @param title
     *            le titre de panneau
     * 
     * @param min
     *            valeur minimale
     * @param max
     *            valeur maximale
     * @param unit
     *            l'unité de mesure à afficher
     * @param pattern
     *            pattern des chiffres
     * 
     * @param enable
     *            vrai si ce panneau est active et desative sinon
     * 
     * @param continuous
     *            vrai pour générer les valeurs continus et discrètes sison
     * */
    public NumberKnobPanel(String title, double min, double max, String unit, String pattern, boolean enable, boolean continuous) {
        this.title = title;
        maxValue = max;
        minValue = min;
        this.unit = unit;
        this.pattern = pattern;
        this.continous = continuous;
        setLayout(null);
        setMinimumSize(size);
        setPreferredSize(size);
        setSize(size);
        knob = new NumberKnob();
        knob.setActive(enable);
        knob.addKnobListener(this);
        add(knob);
        knob.setLocation((size.width - AbstractKnob.size.width) / 2, TITLE_HEIGHT + 5);
        value = computeValue(knob.getValue());
    }

    public void paint(Graphics gc) {
        super.paint(gc);
        RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        ((Graphics2D) gc).setRenderingHints(renderHints);
        gc.setColor(Color.darkGray);
        gc.setFont(new Font("Arial", Font.PLAIN, 11));
        gc.drawString(title, (int) ((getWidth() - gc.getFontMetrics().stringWidth(title)) / 2.) - 2, 10);
        paintValue(gc);
    }

    protected void paintValue(Graphics gc) {
        DecimalFormat df = new DecimalFormat(pattern);
        String str = df.format(value) + " " + unit;
        gc.setColor(Color.gray);
        gc.setFont(new Font("Arial", Font.PLAIN, 10));
        gc.drawString(str, (getWidth() - gc.getFontMetrics().stringWidth(str)) / 2, 75);
    }

    @Override
    public void knobTurned(KnobEvent e) {
        int scale = e.getValue();
        value = computeValue(scale);
        notifyPort(value);
        repaint(0, 0, getWidth(), 20);
    }

    protected double computeValue(int rawValue) {
        if (continous) {
            double piece = (maxValue - minValue) / 10000;
            return minValue + piece * rawValue;
        } else {
            int piece = (int) (10000.0 / (maxValue - minValue));
            return rawValue / piece;
        }
    }

    public void notifyPort(double value) {
        if (inputPort != null && !inputPort.isLinked()) {
            inputPort.setValues(value);
        }
    }

    public void setPort(Port port) {
        inputPort = port;
        if (port != null) {
            port.addObserver(this);
            update(port, null);
        }
    }

    public void setState(boolean enabled) {
        knob.setActive(enabled);
    }

    @Override
    public void update(Observable o, Object arg) {
        value = ((Port) o).getValues().getDouble(0);
        repaint();
    }
}
