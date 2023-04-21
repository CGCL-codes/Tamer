package de.rockon.fuzzy.simulation.view.frames;

import java.text.DecimalFormat;
import mdes.slick.sui.Label;
import mdes.slick.sui.ScrollConstants;
import mdes.slick.sui.Slider;
import mdes.slick.sui.event.ChangeEvent;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import de.rockon.fuzzy.controller.util.factories.IconFactory;
import de.rockon.fuzzy.simulation.cases.BaseSimulation;

/**
 * Distortion Frame
 * 
 * <pre>
 *  [======x======]
 *  Distortion: 50         
 *  
 *  activate
 * </pre>
 */
public class DistortionFrame extends FuzzyFrame {

    private DecimalFormat dfDistortion = new DecimalFormat("0.0");

    /** GUI Elemente */
    private Slider sliderDistortion;

    private Label lblDistortion;

    private float distortion;

    /**
	 * Konstruktor
	 * 
	 * @param parent
	 *            Die Simulation
	 */
    public DistortionFrame(BaseSimulation parent) {
        super("Distortion", parent);
        setToolTipText("add a distortion");
        setFrameIcon(IconFactory.ICON_FRAME_DISTORTION);
        setBackground(new Color(1.0f, 1.0f, 1.0f, 0.3f));
        setResizable(false);
        setMinimumSize(120, 100);
        setMaximumSize(120, 100);
    }

    /**
	 * @param value
	 * @return liefert den Wertebereich der St�rung zur�ck
	 */
    public float calcDomainDistortion(float value) {
        return value * 4 - 2;
    }

    /**
	 * @return liefert die St�rung zur�ck
	 */
    public float getDistortion() {
        if (distortion >= -0.14814818f && distortion <= 0.12345672f) {
            distortion = 0f;
        }
        return distortion;
    }

    /**
	 * @return Liefert Slider Distortion
	 */
    public Slider getSliderDistortion() {
        return sliderDistortion;
    }

    @Override
    public void initFrame() {
        sliderDistortion = new Slider(ScrollConstants.HORIZONTAL);
        sliderDistortion.setLocation(5, 5);
        sliderDistortion.setSize(90, 16);
        sliderDistortion.setThumbSize(.10f);
        sliderDistortion.setValue(0.5f);
        sliderDistortion.addChangeListener(this);
        sliderDistortion.addChangeListener(parent);
        add(sliderDistortion);
        lblDistortion = new Label("Distortion: 0");
        lblDistortion.setToolTipText("adds a random Distortion to the fuzzy thing");
        lblDistortion.pack();
        lblDistortion.setLocation(5, 20);
        add(lblDistortion);
    }

    @Override
    public void paint(Graphics g) {
    }

    @Override
    public void reset() {
        sliderDistortion.setValue(0.5f);
        lblDistortion.setText("Distortion: 0.0");
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        Slider sl = (Slider) event.getSource();
        if (sl == sliderDistortion) {
            distortion = calcDomainDistortion(sl.getValue());
            lblDistortion.setText("Distortion: " + dfDistortion.format(distortion));
            lblDistortion.pack();
        }
    }
}
