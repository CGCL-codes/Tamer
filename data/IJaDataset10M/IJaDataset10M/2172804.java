package listeners;

import graphics.MotionGraphPanel;
import com.phidgets.AccelerometerPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.AttachListener;
import com.phidgets.event.AttachEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AccelAttachListener implements AttachListener {

    private JFrame appFrame;

    private MotionGraphPanel graphPanel;

    private JTextField attachedTxt;

    private JTextArea nameTxt;

    private JTextField serialTxt;

    private JTextField versionTxt;

    private JTextField numAxesTxt;

    private JTextField rangeTxt;

    private JSlider axis1SensitivityScrl;

    private JSlider axis2SensitivityScrl;

    private JSlider axis3SensitivityScrl;

    private JLabel jLabel13;

    private JLabel jLabel14;

    private JLabel jLabel15;

    /** Creates a new instance of AccelAttachListener */
    public AccelAttachListener(JFrame appFrame, MotionGraphPanel graphPanel, JTextField attachedTxt, JTextArea nameTxt, JTextField serialTxt, JTextField versionTxt, JTextField numAxesTxt, JTextField rangeTxt, JSlider axis1SensitivityScrl, JSlider axis2SensitivityScrl, JSlider axis3SensitivityScrl, JLabel jLabel13, JLabel jLabel14, JLabel jLabel15) {
        this.appFrame = appFrame;
        this.graphPanel = graphPanel;
        this.attachedTxt = attachedTxt;
        this.nameTxt = nameTxt;
        this.serialTxt = serialTxt;
        this.versionTxt = versionTxt;
        this.numAxesTxt = numAxesTxt;
        this.rangeTxt = rangeTxt;
        this.axis1SensitivityScrl = axis1SensitivityScrl;
        this.axis2SensitivityScrl = axis2SensitivityScrl;
        this.axis3SensitivityScrl = axis3SensitivityScrl;
        this.jLabel13 = jLabel13;
        this.jLabel14 = jLabel14;
        this.jLabel15 = jLabel15;
    }

    public void attached(AttachEvent ae) {
        try {
            AccelerometerPhidget attached = (AccelerometerPhidget) ae.getSource();
            attachedTxt.setText(Boolean.toString(attached.isAttached()));
            nameTxt.setText(attached.getDeviceName());
            serialTxt.setText(Integer.toString(attached.getSerialNumber()));
            versionTxt.setText(Integer.toString(attached.getDeviceVersion()));
            numAxesTxt.setText(Integer.toString(attached.getAxisCount()));
            String range = attached.getAccelerationMin(0) == -attached.getAccelerationMax(0) ? "�" + attached.getAccelerationMax(0) + "g" : attached.getAccelerationMin(0) + "g to " + attached.getAccelerationMax(0) + "g";
            rangeTxt.setText(range);
            attached.setAccelerationChangeTrigger(0, 0);
            attached.setAccelerationChangeTrigger(1, 0);
            axis1SensitivityScrl.setEnabled(true);
            axis2SensitivityScrl.setEnabled(true);
            if (attached.getAxisCount() > 2) {
                attached.setAccelerationChangeTrigger(2, 0);
                axis3SensitivityScrl.setEnabled(true);
                graphPanel.setZAxisExist(true);
            } else {
                axis3SensitivityScrl.setEnabled(false);
                graphPanel.setZAxisExist(false);
            }
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(appFrame, ex.getDescription(), "Phidget error " + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
        }
    }
}
