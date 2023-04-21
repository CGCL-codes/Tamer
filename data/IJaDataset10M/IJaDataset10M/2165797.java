package pl.swmud.ns.swaedit.gui;

import java.util.List;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QMainWindow;

public class RenumberWarningsWidget extends QMainWindow {

    private Ui_RenumberWarningsWidget ui = new Ui_RenumberWarningsWidget();

    public RenumberWarningsWidget(List<String> warnings) {
        ui.setupUi(this);
        SWAEdit.setChildPosition(this);
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose);
        setWindowModality(Qt.WindowModality.NonModal);
        int warnNo = 0;
        for (String warning : warnings) {
            new QListWidgetItem(warning, ui.warningsListWidget);
            warnNo++;
        }
        statusBar().showMessage("Number of warnings: " + warnNo + ".");
    }
}
