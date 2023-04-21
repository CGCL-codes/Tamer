package listeners;

import com.phidgets.EncoderPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.EncoderPositionChangeListener;
import com.phidgets.event.EncoderPositionChangeEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class EncoderPositionListener implements EncoderPositionChangeListener {

    private JFrame appFrame;

    private JTextField relChngTxt;

    private JTextField timeTxt;

    private JTextField posTxt;

    /**
     * Creates a new instance of EncoderPositionListener
     */
    public EncoderPositionListener(JFrame appFrame, JTextField relChngTxt, JTextField timeTxt, JTextField posTxt) {
        this.appFrame = appFrame;
        this.relChngTxt = relChngTxt;
        this.timeTxt = timeTxt;
        this.posTxt = posTxt;
    }

    public void encoderPositionChanged(EncoderPositionChangeEvent encoderPositionChangeEvent) {
        try {
            EncoderPhidget source = (EncoderPhidget) encoderPositionChangeEvent.getSource();
            relChngTxt.setText(Integer.toString(encoderPositionChangeEvent.getValue()));
            timeTxt.setText(Integer.toString(encoderPositionChangeEvent.getTime()));
            posTxt.setText(Integer.toString(source.getPosition(encoderPositionChangeEvent.getIndex())));
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(appFrame, ex.getDescription(), "Phidget error " + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
        }
    }
}
