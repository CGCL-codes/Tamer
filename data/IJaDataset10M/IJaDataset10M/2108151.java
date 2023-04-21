package listeners;

import com.phidgets.RFIDPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.DetachListener;
import com.phidgets.event.DetachEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

public class RFIDDetachListener implements DetachListener {

    private JFrame appFrame;

    private JTextField attachedTxt;

    private JTextArea nameTxt;

    private JTextField serialTxt;

    private JTextField versionTxt;

    private JTextField numOutputsTxt;

    private JCheckBox antennaChk;

    private JCheckBox ledChk;

    private JCheckBox out0Chk;

    private JCheckBox out1Chk;

    /** Creates a new instance of RFIDDetachListener */
    public RFIDDetachListener(JFrame appFrame, JTextField attachedTxt, JTextArea nameTxt, JTextField serialTxt, JTextField versionTxt, JTextField numOutputsTxt, JCheckBox antennaChk, JCheckBox ledChk, JCheckBox out0Chk, JCheckBox out1Chk) {
        this.appFrame = appFrame;
        this.attachedTxt = attachedTxt;
        this.nameTxt = nameTxt;
        this.serialTxt = serialTxt;
        this.versionTxt = versionTxt;
        this.numOutputsTxt = numOutputsTxt;
        this.antennaChk = antennaChk;
        this.ledChk = ledChk;
        this.out0Chk = out0Chk;
        this.out1Chk = out1Chk;
    }

    public void detached(DetachEvent de) {
        try {
            RFIDPhidget detached = (RFIDPhidget) de.getSource();
            attachedTxt.setText(Boolean.toString(detached.isAttached()));
            nameTxt.setText("");
            serialTxt.setText("");
            versionTxt.setText("");
            numOutputsTxt.setText("");
            antennaChk.setEnabled(false);
            antennaChk.setSelected(false);
            ledChk.setEnabled(false);
            ledChk.setSelected(false);
            out0Chk.setEnabled(false);
            out0Chk.setSelected(false);
            out1Chk.setEnabled(false);
            out1Chk.setSelected(false);
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(appFrame, ex.getDescription(), "Phidget error " + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
        }
    }
}
