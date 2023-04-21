package motej.demos.balanceboard;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import motejx.extensions.balanceboard.BalanceBoardCalibrationData;
import motejx.extensions.balanceboard.BalanceBoardCalibrationData.Sensor;
import motejx.extensions.balanceboard.BalanceBoardCalibrationData.Weight;

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class BalanceBoardCalibrationDataPanel {

    private JPanel panel;

    private JTable table;

    public BalanceBoardCalibrationDataPanel(BalanceBoardCalibrationData cal) {
        String[][] data = new String[][] { { "", "KG 0", "KG 17", "KG 34" }, { "Sensor A", "" + cal.getCalibration(Sensor.A, Weight.KG_0), "" + cal.getCalibration(Sensor.A, Weight.KG_17), "" + cal.getCalibration(Sensor.A, Weight.KG_34) }, { "Sensor B", "" + cal.getCalibration(Sensor.B, Weight.KG_0), "" + cal.getCalibration(Sensor.B, Weight.KG_17), "" + cal.getCalibration(Sensor.B, Weight.KG_34) }, { "Sensor C", "" + cal.getCalibration(Sensor.C, Weight.KG_0), "" + cal.getCalibration(Sensor.C, Weight.KG_17), "" + cal.getCalibration(Sensor.C, Weight.KG_34) }, { "Sensor D", "" + cal.getCalibration(Sensor.D, Weight.KG_0), "" + cal.getCalibration(Sensor.D, Weight.KG_17), "" + cal.getCalibration(Sensor.D, Weight.KG_34) } };
        table = new JTable(data, new String[] { "", "", "", "" });
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(table, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return panel;
    }
}
