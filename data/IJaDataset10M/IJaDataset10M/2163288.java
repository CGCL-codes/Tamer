package Encoder.listeners;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.phidgets.event.ErrorEvent;
import com.phidgets.event.ErrorListener;

public class EncoderErrorListener implements ErrorListener {

    private JFrame appFrame;

    /**
     * Creates a new instance of EncoderErrorListener
     */
    public EncoderErrorListener(JFrame appFrame) {
        this.appFrame = appFrame;
    }

    public void error(ErrorEvent errorEvent) {
        JOptionPane.showMessageDialog(appFrame, errorEvent.toString(), "Encoder Error Event", JOptionPane.ERROR_MESSAGE);
    }
}
