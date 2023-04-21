package sk.fiit.mitandao.modules.inputs.dbreader.donestep;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import sk.fiit.mitandao.modules.inputs.dbreader.DBReaderController;

public class DoneController implements ActionListener {

    private DBReaderController controller = null;

    private DonePanel panel = null;

    private String pk_value = null;

    private String fk_value = null;

    private String zoznam = null;

    @Override
    public void actionPerformed(ActionEvent e) {
        getDataFromScreen();
        setDataToController();
    }

    private void setDataToController() {
        controller.performLastStep(pk_value, fk_value, zoznam);
    }

    public void setDBReaderController(DBReaderController controller) {
        this.controller = controller;
    }

    public void setDonePanel(DonePanel panel) {
        this.panel = panel;
    }

    private void getDataFromScreen() {
        pk_value = panel.getC1Select().getSelectedItem().toString();
        fk_value = panel.getC2Select().getSelectedItem().toString();
        for (int i = 0; i < panel.getList().getModel().getSize(); i++) {
            zoznam = zoznam + panel.getList().getModel().getElementAt(i) + "|";
        }
    }
}
