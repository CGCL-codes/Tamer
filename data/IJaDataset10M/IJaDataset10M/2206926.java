package generated;

import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QDialogButtonBox;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QWidget;

public class Ui_Dialog {

    public QWidget horizontalLayout;

    public QHBoxLayout mainLayout;

    public QHBoxLayout hboxLayout;

    public QTableWidget tableWidget;

    public QDialogButtonBox buttonBox;

    public QGroupBox groupBox;

    public Ui_Dialog() {
        super();
    }

    public void setupUi(QDialog Dialog) {
        Dialog.setWindowIcon(new QIcon("app.png"));
        groupBox = new QGroupBox(Dialog);
        groupBox.setTitle("Liste des Classes");
        tableWidget = new QTableWidget(Dialog);
        buttonBox = new QDialogButtonBox(Dialog);
        buttonBox.setOrientation(com.trolltech.qt.core.Qt.Orientation.Vertical);
        buttonBox.setStandardButtons(com.trolltech.qt.gui.QDialogButtonBox.StandardButton.createQFlags(com.trolltech.qt.gui.QDialogButtonBox.StandardButton.Cancel, com.trolltech.qt.gui.QDialogButtonBox.StandardButton.NoButton, com.trolltech.qt.gui.QDialogButtonBox.StandardButton.Ok));
        hboxLayout = new QHBoxLayout();
        hboxLayout.addWidget(tableWidget);
        hboxLayout.addWidget(buttonBox);
        groupBox.setLayout(hboxLayout);
        mainLayout = new QHBoxLayout();
        mainLayout.addWidget(groupBox);
        Dialog.setLayout(mainLayout);
        retranslateUi(Dialog);
        Dialog.connectSlotsByName();
    }

    void retranslateUi(QDialog Dialog) {
        Dialog.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("Dialog", "S�lection des classes � afficher"));
        tableWidget.clear();
        tableWidget.setColumnCount(0);
        tableWidget.setRowCount(0);
    }
}
