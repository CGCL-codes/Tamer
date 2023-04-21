package info.metlos.jcdc.views.ui.qt;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QSpinBox;
import com.trolltech.qt.gui.QSplitter;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;

public class _ui_SearchViewUi {

    public QVBoxLayout vboxLayout;

    public QSplitter splitter;

    public QWidget layoutWidget;

    public QVBoxLayout vboxLayout1;

    public QComboBox cmbSearch;

    public QPushButton btnSearch;

    public QHBoxLayout hboxLayout;

    public QComboBox cmbFileSizeType;

    public QSpinBox spnFileSize;

    public QComboBox cmbFileSizeUnit;

    public QComboBox cmbFileType;

    public QGroupBox grpSearchHubs;

    public QVBoxLayout vboxLayout2;

    public QTableWidget tblSearchHubs;

    public QTableWidget tblResults;

    public _ui_SearchViewUi() {
        super();
    }

    public void setupUi(QWidget _ui_SearchViewUi) {
        _ui_SearchViewUi.setObjectName("_ui_SearchViewUi");
        _ui_SearchViewUi.resize(new QSize(563, 475).expandedTo(_ui_SearchViewUi.minimumSizeHint()));
        QSizePolicy sizePolicy = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Preferred);
        sizePolicy.setHorizontalStretch((byte) 0);
        sizePolicy.setVerticalStretch((byte) 0);
        sizePolicy.setHeightForWidth(_ui_SearchViewUi.sizePolicy().hasHeightForWidth());
        _ui_SearchViewUi.setSizePolicy(sizePolicy);
        vboxLayout = new QVBoxLayout(_ui_SearchViewUi);
        vboxLayout.setMargin(2);
        vboxLayout.setObjectName("vboxLayout");
        splitter = new QSplitter(_ui_SearchViewUi);
        splitter.setObjectName("splitter");
        splitter.setOrientation(com.trolltech.qt.core.Qt.Orientation.Horizontal);
        layoutWidget = new QWidget(splitter);
        layoutWidget.setObjectName("layoutWidget");
        layoutWidget.setMinimumSize(new QSize(220, 0));
        layoutWidget.setMaximumSize(new QSize(220, 16777215));
        vboxLayout1 = new QVBoxLayout(layoutWidget);
        vboxLayout1.setMargin(0);
        vboxLayout1.setObjectName("vboxLayout1");
        cmbSearch = new QComboBox(layoutWidget);
        cmbSearch.setObjectName("cmbSearch");
        cmbSearch.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        cmbSearch.setEditable(true);
        vboxLayout1.addWidget(cmbSearch);
        btnSearch = new QPushButton(layoutWidget);
        btnSearch.setObjectName("btnSearch");
        btnSearch.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.StrongFocus);
        vboxLayout1.addWidget(btnSearch);
        hboxLayout = new QHBoxLayout();
        hboxLayout.setMargin(0);
        hboxLayout.setObjectName("hboxLayout");
        cmbFileSizeType = new QComboBox(layoutWidget);
        cmbFileSizeType.setObjectName("cmbFileSizeType");
        QSizePolicy sizePolicy1 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy1.setHorizontalStretch((byte) 0);
        sizePolicy1.setVerticalStretch((byte) 0);
        sizePolicy1.setHeightForWidth(cmbFileSizeType.sizePolicy().hasHeightForWidth());
        cmbFileSizeType.setSizePolicy(sizePolicy1);
        cmbFileSizeType.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        hboxLayout.addWidget(cmbFileSizeType);
        spnFileSize = new QSpinBox(layoutWidget);
        spnFileSize.setObjectName("spnFileSize");
        QSizePolicy sizePolicy2 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy2.setHorizontalStretch((byte) 0);
        sizePolicy2.setVerticalStretch((byte) 0);
        sizePolicy2.setHeightForWidth(spnFileSize.sizePolicy().hasHeightForWidth());
        spnFileSize.setSizePolicy(sizePolicy2);
        spnFileSize.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        spnFileSize.setAlignment(com.trolltech.qt.core.Qt.AlignmentFlag.createQFlags(com.trolltech.qt.core.Qt.AlignmentFlag.AlignBottom, com.trolltech.qt.core.Qt.AlignmentFlag.AlignRight));
        hboxLayout.addWidget(spnFileSize);
        cmbFileSizeUnit = new QComboBox(layoutWidget);
        cmbFileSizeUnit.setObjectName("cmbFileSizeUnit");
        QSizePolicy sizePolicy3 = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy3.setHorizontalStretch((byte) 0);
        sizePolicy3.setVerticalStretch((byte) 0);
        sizePolicy3.setHeightForWidth(cmbFileSizeUnit.sizePolicy().hasHeightForWidth());
        cmbFileSizeUnit.setSizePolicy(sizePolicy3);
        cmbFileSizeUnit.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        hboxLayout.addWidget(cmbFileSizeUnit);
        vboxLayout1.addLayout(hboxLayout);
        cmbFileType = new QComboBox(layoutWidget);
        cmbFileType.setObjectName("cmbFileType");
        cmbFileType.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        vboxLayout1.addWidget(cmbFileType);
        grpSearchHubs = new QGroupBox(layoutWidget);
        grpSearchHubs.setObjectName("grpSearchHubs");
        vboxLayout2 = new QVBoxLayout(grpSearchHubs);
        vboxLayout2.setMargin(0);
        vboxLayout2.setObjectName("vboxLayout2");
        tblSearchHubs = new QTableWidget(grpSearchHubs);
        tblSearchHubs.setObjectName("tblSearchHubs");
        tblSearchHubs.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        tblSearchHubs.setFrameShape(com.trolltech.qt.gui.QFrame.Shape.NoFrame);
        vboxLayout2.addWidget(tblSearchHubs);
        vboxLayout1.addWidget(grpSearchHubs);
        splitter.addWidget(layoutWidget);
        tblResults = new QTableWidget(splitter);
        tblResults.setObjectName("tblResults");
        tblResults.setFocusPolicy(com.trolltech.qt.core.Qt.FocusPolicy.WheelFocus);
        tblResults.setEditTriggers(com.trolltech.qt.gui.QAbstractItemView.EditTrigger.createQFlags(com.trolltech.qt.gui.QAbstractItemView.EditTrigger.NoEditTriggers));
        tblResults.setAlternatingRowColors(true);
        tblResults.setSortingEnabled(true);
        tblResults.setCornerButtonEnabled(false);
        tblResults.setColumnCount(6);
        splitter.addWidget(tblResults);
        vboxLayout.addWidget(splitter);
        retranslateUi(_ui_SearchViewUi);
        _ui_SearchViewUi.connectSlotsByName();
    }

    void retranslateUi(QWidget _ui_SearchViewUi) {
        _ui_SearchViewUi.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "Form"));
        btnSearch.setText(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "Search"));
        cmbFileSizeType.clear();
        cmbFileSizeType.addItem(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "File Size"));
        cmbFileSizeType.addItem(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "Min"));
        cmbFileSizeType.addItem(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "Max"));
        cmbFileSizeUnit.clear();
        cmbFileSizeUnit.addItem(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "B"));
        cmbFileSizeUnit.addItem(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "KB"));
        cmbFileSizeUnit.addItem(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "MB"));
        cmbFileSizeUnit.addItem(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "GB"));
        cmbFileType.clear();
        cmbFileType.addItem(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "File Type"));
        cmbFileType.addItem(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "Audio"));
        cmbFileType.addItem(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "Video"));
        cmbFileType.addItem(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "Documents"));
        cmbFileType.addItem(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "Archives"));
        cmbFileType.addItem(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "Executables"));
        grpSearchHubs.setTitle(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "Included hubs"));
        tblSearchHubs.clear();
        tblSearchHubs.setColumnCount(0);
        tblSearchHubs.setRowCount(0);
        tblResults.clear();
        tblResults.setColumnCount(6);
        QTableWidgetItem __colItem = new QTableWidgetItem();
        __colItem.setText(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "File name"));
        tblResults.setHorizontalHeaderItem(0, __colItem);
        QTableWidgetItem __colItem1 = new QTableWidgetItem();
        __colItem1.setText(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "User"));
        tblResults.setHorizontalHeaderItem(1, __colItem1);
        QTableWidgetItem __colItem2 = new QTableWidgetItem();
        __colItem2.setText(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "Slots"));
        tblResults.setHorizontalHeaderItem(2, __colItem2);
        QTableWidgetItem __colItem3 = new QTableWidgetItem();
        __colItem3.setText(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "Size"));
        tblResults.setHorizontalHeaderItem(3, __colItem3);
        QTableWidgetItem __colItem4 = new QTableWidgetItem();
        __colItem4.setText(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "Path"));
        tblResults.setHorizontalHeaderItem(4, __colItem4);
        QTableWidgetItem __colItem5 = new QTableWidgetItem();
        __colItem5.setText(com.trolltech.qt.core.QCoreApplication.translate("_ui_SearchViewUi", "Hub"));
        tblResults.setHorizontalHeaderItem(5, __colItem5);
        tblResults.setRowCount(0);
    }
}
