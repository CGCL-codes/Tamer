package Encoder.listeners;

import javax.swing.JCheckBox;
import com.phidgets.event.InputChangeEvent;
import com.phidgets.event.InputChangeListener;

public class EncoderInputListener implements InputChangeListener {

    private JCheckBox inputChk;

    /**
     * Creates a new instance of EncoderInputListener
     */
    public EncoderInputListener(JCheckBox inputChk) {
        this.inputChk = inputChk;
    }

    public void inputChanged(InputChangeEvent inputChangeEvent) {
        inputChk.setSelected(inputChangeEvent.getState());
    }
}
