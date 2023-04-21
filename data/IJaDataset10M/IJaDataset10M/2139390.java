package org.sodbeans.controller.impl.readers;

import java.awt.event.KeyEvent;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import org.sodbeans.controller.impl.processors.CheckboxProcessor;
import org.sodbeans.controller.impl.processors.NullProcessor;
import org.sodbeans.controller.impl.processors.SliderProcessor;
import org.sodbeans.phonemic.SpeechProcessor;

/**
 *  Reads JSliders to the reader.
 * 
 * @author Jeff Wilson
 */
public class JSliderReader extends AbstractScreenReader {

    private JCheckBox checkBox;

    private JSlider slider;

    @Override
    protected SpeechProcessor getKeyEventProcessor() {
        if (slider == null || (getUberEvent().key.getKeyCode() != KeyEvent.VK_LEFT && getUberEvent().key.getKeyCode() != KeyEvent.VK_RIGHT)) return new NullProcessor();
        SliderProcessor proc = new SliderProcessor();
        proc.setKeyEvent(true);
        String name = slider.getAccessibleContext().getAccessibleName();
        String desc = slider.getAccessibleContext().getAccessibleDescription();
        if (name != null && !name.isEmpty()) {
            proc.setText(name);
        } else if (desc != null && !desc.isEmpty()) {
            proc.setText(desc);
        } else {
            proc.setText("slider");
        }
        proc.setValue(slider.getValue());
        proc.setMinimum(slider.getMinimum());
        proc.setMaximum(slider.getMaximum());
        return proc;
    }

    @Override
    protected SpeechProcessor getFocusEventProcessor() {
        SliderProcessor proc = new SliderProcessor();
        proc.setKeyEvent(false);
        String name = slider.getAccessibleContext().getAccessibleName();
        String desc = slider.getAccessibleContext().getAccessibleDescription();
        if (name != null && !name.isEmpty()) {
            proc.setText(name);
        } else if (desc != null && !desc.isEmpty()) {
            proc.setText(desc);
        } else {
            proc.setText("slider");
        }
        proc.setValue(slider.getValue());
        proc.setMinimum(slider.getMinimum());
        proc.setMaximum(slider.getMaximum());
        return proc;
    }

    public void setObject(Object object) {
        slider = null;
        slider = (JSlider) object;
    }
}
