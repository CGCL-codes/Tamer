package openfield.informes.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import openfield.informes.utils.ReportInvocator;

public final class ParcelasReport implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        ReportInvocator report = new ReportInvocator("reportparcela2");
        report.invoke();
    }
}
