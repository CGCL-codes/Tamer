package de.hpi.eworld.gui;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QWidget;

public class AboutDialog extends QDialog {

    public AboutDialog(QWidget parent) {
        QGridLayout layout = new QGridLayout(this);
        setLayout(layout);
        setWindowIcon(new QIcon("classpath:images/earth.png"));
        QLabel img = new QLabel(this);
        img.setPixmap(new QPixmap("classpath:images/hpi.png"));
        layout.addWidget(img, 3, 3, 11, 1, new Qt.Alignment(Qt.AlignmentFlag.AlignRight, Qt.AlignmentFlag.AlignBottom));
        img = new QLabel(this);
        img.setPixmap(new QPixmap("classpath:images/logo.png"));
        layout.addWidget(img, 0, 0, 9, 5, Qt.AlignmentFlag.AlignTop);
        layout.setRowMinimumHeight(1, 140);
        layout.setColumnMinimumWidth(4, 20);
        layout.addWidget(new QLabel("eWorld is brought to you by:"), 2, 1, 1, 2);
        layout.addWidget(new QLabel("    Martin Beck"), 3, 1, 1, 2);
        layout.addWidget(new QLabel("    Sebastian Enderlein"), 4, 1, 1, 2);
        layout.addWidget(new QLabel("    Christian Holz"), 5, 1, 1, 2);
        layout.addWidget(new QLabel("    Bernd Sch�ufele"), 6, 1, 1, 2);
        layout.addWidget(new QLabel("    Martin Wolf"), 7, 1, 1, 2);
        layout.setRowMinimumHeight(8, 10);
        layout.addWidget(new QLabel("adviser:"), 9, 1, 1, 2);
        layout.addWidget(new QLabel("    Bj�rn Sch�nemann"), 10, 1, 1, 2);
        layout.setRowMinimumHeight(11, 10);
        layout.addWidget(new QLabel("web:"), 12, 1, 1, 2);
        layout.addWidget(new QLabel("    <a href=\"http://eworld.sourceforge.net/\">http://eworld.sourceforge.net/</a>"), 13, 1, 1, 2);
        layout.setRowMinimumHeight(14, 10);
        QPushButton button = new QPushButton("OK", this);
        button.clicked.connect(this, "accept()");
        layout.addWidget(button, 15, 0, 1, 5, Qt.AlignmentFlag.AlignCenter);
        button.setMaximumWidth(100);
        setWindowTitle("About eWorld");
        setModal(true);
    }
}
