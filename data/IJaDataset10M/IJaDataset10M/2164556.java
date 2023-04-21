package org.fonteditor.sliders;

import org.fonteditor.utilities.general.For;

/**
 * Extracts a section from a SliderManager...
 */
public class SliderManagerSplitter {

    public static SliderManager extract(SliderManager slider_manager, int min, int max) {
        SliderManager output = new SliderManager();
        int number = slider_manager.getNumber();
        for (int i = 0; i < number; i++) {
            Slider s = slider_manager.getSlider(i);
            if ((s.getPosition() >= min) && (s.getPosition() <= max)) {
                try {
                    output.add((Slider) s.clone());
                } catch (CloneNotSupportedException e) {
                    For.get(e);
                }
            }
        }
        return output;
    }
}
