package listeners;

import com.phidgets.PhidgetException;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ManagerAttachListener implements AttachListener {

    private JFrame appFrame;

    private JTable deviceTbl;

    /** Creates a new instance of ManagerAttachListener */
    public ManagerAttachListener(JFrame appFrame, JTable deviceTbl) {
        this.appFrame = appFrame;
        this.deviceTbl = deviceTbl;
    }

    public void attached(AttachEvent ae) {
        try {
            Vector row = new Vector();
            row.add(0, ae.getSource().getDeviceName());
            row.add(1, Integer.toString(ae.getSource().getSerialNumber()));
            row.add(2, Integer.toString(ae.getSource().getDeviceVersion()));
            row.add(3, Boolean.toString(ae.getSource().isAttached()));
            ((DefaultTableModel) deviceTbl.getModel()).addRow(row);
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(appFrame, ex.getDescription(), "Phidget error " + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
        }
    }
}
