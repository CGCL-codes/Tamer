package mx.ipn.presentacion.radioperadora.ui;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class Ui_EliminarUnidad {

    public QDialogButtonBox buttonBox;

    public QTableWidget tableWidget;

    public QPushButton pushButton;

    public Ui_EliminarUnidad() {
        super();
    }

    public void setupUi(QDialog EliminarUnidad) {
        EliminarUnidad.setObjectName("EliminarUnidad");
        EliminarUnidad.resize(new QSize(732, 347).expandedTo(EliminarUnidad.minimumSizeHint()));
        buttonBox = new QDialogButtonBox(EliminarUnidad);
        buttonBox.setObjectName("buttonBox");
        buttonBox.setGeometry(new QRect(150, 290, 231, 31));
        buttonBox.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.TabFocus);
        buttonBox.setStandardButtons(com.trolltech.qt.gui.QDialogButtonBox.StandardButton.createQFlags(com.trolltech.qt.gui.QDialogButtonBox.StandardButton.Cancel, com.trolltech.qt.gui.QDialogButtonBox.StandardButton.NoButton, com.trolltech.qt.gui.QDialogButtonBox.StandardButton.Ok));
        tableWidget = new QTableWidget(EliminarUnidad);
        tableWidget.setObjectName("tableWidget");
        tableWidget.setGeometry(new QRect(0, 60, 721, 192));
        tableWidget.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        tableWidget.setRowCount(1);
        tableWidget.setColumnCount(7);
        pushButton = new QPushButton(EliminarUnidad);
        pushButton.setObjectName("pushButton");
        pushButton.setGeometry(new QRect(390, 293, 77, 25));
        pushButton.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);
        retranslateUi(EliminarUnidad);
        buttonBox.accepted.connect(EliminarUnidad, "accept()");
        buttonBox.rejected.connect(EliminarUnidad, "reject()");
        EliminarUnidad.connectSlotsByName();
    }

    void retranslateUi(QDialog EliminarUnidad) {
        EliminarUnidad.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("EliminarUnidad", "Dialog"));
        tableWidget.clear();
        tableWidget.setColumnCount(7);
        tableWidget.setRowCount(1);
        pushButton.setText(com.trolltech.qt.core.QCoreApplication.translate("EliminarUnidad", "Modificar"));
    }
}
