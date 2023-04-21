package listeners;

import com.phidgets.event.InputChangeListener;
import com.phidgets.event.InputChangeEvent;
import javax.swing.JCheckBox;

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
