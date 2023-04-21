package info.metlos.jcdc.config.ui.qt;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class _ui_FileListProviderConfiguration {

    public QVBoxLayout vboxLayout;

    public QSplitter splitter;

    public QWidget wdgLocations;

    public QVBoxLayout vboxLayout1;

    public QVBoxLayout vboxLayout2;

    public QLabel lblLocations;

    public QHBoxLayout hboxLayout;

    public QListWidget lstLocations;

    public QVBoxLayout vboxLayout3;

    public QPushButton btnAddLocation;

    public QPushButton btnEditLocation;

    public QPushButton btnDeleteLocation;

    public QSpacerItem spacerItem;

    public QWidget wdgSharePreview;

    public QVBoxLayout vboxLayout4;

    public QHBoxLayout hboxLayout1;

    public QLabel lblSharePreview;

    public QSpacerItem spacerItem1;

    public QPushButton btnRestartScan;

    public QTreeView treeSharePreview;

    public _ui_FileListProviderConfiguration() {
        super();
    }

    public void setupUi(QWidget FileListProviderConfiguration) {
        FileListProviderConfiguration.setObjectName("FileListProviderConfiguration");
        FileListProviderConfiguration.resize(new QSize(648, 417).expandedTo(FileListProviderConfiguration.minimumSizeHint()));
        vboxLayout = new QVBoxLayout(FileListProviderConfiguration);
        vboxLayout.setObjectName("vboxLayout");
        vboxLayout.setContentsMargins(0, 0, 0, 0);
        splitter = new QSplitter(FileListProviderConfiguration);
        splitter.setObjectName("splitter");
        splitter.setOrientation(com.trolltech.qt.core.Qt.Orientation.Vertical);
        wdgLocations = new QWidget(splitter);
        wdgLocations.setObjectName("wdgLocations");
        vboxLayout1 = new QVBoxLayout(wdgLocations);
        vboxLayout1.setObjectName("vboxLayout1");
        vboxLayout1.setContentsMargins(0, 0, 0, 0);
        vboxLayout2 = new QVBoxLayout();
        vboxLayout2.setObjectName("vboxLayout2");
        vboxLayout2.setContentsMargins(0, 0, 0, 0);
        lblLocations = new QLabel(wdgLocations);
        lblLocations.setObjectName("lblLocations");
        vboxLayout2.addWidget(lblLocations);
        hboxLayout = new QHBoxLayout();
        hboxLayout.setObjectName("hboxLayout");
        hboxLayout.setContentsMargins(0, 0, 0, 0);
        lstLocations = new QListWidget(wdgLocations);
        lstLocations.setObjectName("lstLocations");
        lstLocations.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        hboxLayout.addWidget(lstLocations);
        vboxLayout3 = new QVBoxLayout();
        vboxLayout3.setObjectName("vboxLayout3");
        vboxLayout3.setContentsMargins(0, 0, 0, 0);
        btnAddLocation = new QPushButton(wdgLocations);
        btnAddLocation.setObjectName("btnAddLocation");
        btnAddLocation.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);
        vboxLayout3.addWidget(btnAddLocation);
        btnEditLocation = new QPushButton(wdgLocations);
        btnEditLocation.setObjectName("btnEditLocation");
        btnEditLocation.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);
        vboxLayout3.addWidget(btnEditLocation);
        btnDeleteLocation = new QPushButton(wdgLocations);
        btnDeleteLocation.setObjectName("btnDeleteLocation");
        btnDeleteLocation.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);
        vboxLayout3.addWidget(btnDeleteLocation);
        spacerItem = new QSpacerItem(20, 40, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding);
        vboxLayout3.addItem(spacerItem);
        hboxLayout.addLayout(vboxLayout3);
        vboxLayout2.addLayout(hboxLayout);
        vboxLayout1.addLayout(vboxLayout2);
        splitter.addWidget(wdgLocations);
        wdgSharePreview = new QWidget(splitter);
        wdgSharePreview.setObjectName("wdgSharePreview");
        vboxLayout4 = new QVBoxLayout(wdgSharePreview);
        vboxLayout4.setObjectName("vboxLayout4");
        vboxLayout4.setContentsMargins(0, 0, 0, 0);
        hboxLayout1 = new QHBoxLayout();
        hboxLayout1.setObjectName("hboxLayout1");
        hboxLayout1.setContentsMargins(0, 0, 0, 0);
        lblSharePreview = new QLabel(wdgSharePreview);
        lblSharePreview.setObjectName("lblSharePreview");
        hboxLayout1.addWidget(lblSharePreview);
        spacerItem1 = new QSpacerItem(40, 20, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum);
        hboxLayout1.addItem(spacerItem1);
        btnRestartScan = new QPushButton(wdgSharePreview);
        btnRestartScan.setObjectName("btnRestartScan");
        btnRestartScan.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);
        btnRestartScan.setIcon(new QIcon(new QPixmap("classpath:info/metlos/jcdc/resources/refresh-16x16.png")));
        hboxLayout1.addWidget(btnRestartScan);
        vboxLayout4.addLayout(hboxLayout1);
        treeSharePreview = new QTreeView(wdgSharePreview);
        treeSharePreview.setObjectName("treeSharePreview");
        treeSharePreview.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        vboxLayout4.addWidget(treeSharePreview);
        splitter.addWidget(wdgSharePreview);
        vboxLayout.addWidget(splitter);
        retranslateUi(FileListProviderConfiguration);
        FileListProviderConfiguration.connectSlotsByName();
    }

    void retranslateUi(QWidget FileListProviderConfiguration) {
        FileListProviderConfiguration.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("_ui_FileListProviderConfiguration", "Form"));
        lblLocations.setText(com.trolltech.qt.core.QCoreApplication.translate("_ui_FileListProviderConfiguration", "Shared Locations"));
        btnAddLocation.setText(com.trolltech.qt.core.QCoreApplication.translate("_ui_FileListProviderConfiguration", "Add"));
        btnEditLocation.setText(com.trolltech.qt.core.QCoreApplication.translate("_ui_FileListProviderConfiguration", "Edit"));
        btnDeleteLocation.setText(com.trolltech.qt.core.QCoreApplication.translate("_ui_FileListProviderConfiguration", "Delete"));
        lblSharePreview.setText(com.trolltech.qt.core.QCoreApplication.translate("_ui_FileListProviderConfiguration", "Share Preview"));
        btnRestartScan.setText(com.trolltech.qt.core.QCoreApplication.translate("_ui_FileListProviderConfiguration", ""));
    }
}
