package midi;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LEDSliderListener implements ChangeListener {

    private LEDFrame lf;

    private int index;

    public LEDSliderListener(LEDFrame lf, int index) {
        this.lf = lf;
        this.index = index;
    }

    public void stateChanged(ChangeEvent e) {
        lf.updateSlider(index);
    }
}
