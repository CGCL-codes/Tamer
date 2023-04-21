package org.auramp.ui.config.subtitles;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class JamSubtitlesConfig implements com.trolltech.qt.QUiForm<QWidget> {

    public QGridLayout gridLayout;

    public QLabel label_6;

    public QComboBox cmbStreamSelectMode;

    public QLabel label;

    public QComboBox cmbFontBorderFactor;

    public QLabel label_3;

    public QComboBox cmbAutomaticSubtitleLoading;

    public QLabel label_4;

    public QLineEdit txtPreferredSubLang;

    public QLabel label_5;

    public QDoubleSpinBox spnSubDelay;

    public QSpacerItem horizontalSpacer;

    public QLabel label_2;

    public QSpinBox spnOSDDuration;

    public QCheckBox chkASS;

    public QCheckBox chkEmbeddedFonts;

    public QCheckBox chkFontConfig;

    public QCheckBox chkAllowOverlap;

    public QSpacerItem verticalSpacer;

    public JamSubtitlesConfig() {
        super();
    }

    public void setupUi(QWidget SubtitlesConfig) {
        SubtitlesConfig.setObjectName("SubtitlesConfig");
        SubtitlesConfig.resize(new QSize(421, 356).expandedTo(SubtitlesConfig.minimumSizeHint()));
        gridLayout = new QGridLayout(SubtitlesConfig);
        gridLayout.setObjectName("gridLayout");
        label_6 = new QLabel(SubtitlesConfig);
        label_6.setObjectName("label_6");
        gridLayout.addWidget(label_6, 0, 0, 1, 1);
        cmbStreamSelectMode = new QComboBox(SubtitlesConfig);
        cmbStreamSelectMode.setObjectName("cmbStreamSelectMode");
        QSizePolicy sizePolicy = new QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Fixed);
        sizePolicy.setHorizontalStretch((byte) 0);
        sizePolicy.setVerticalStretch((byte) 0);
        sizePolicy.setHeightForWidth(cmbStreamSelectMode.sizePolicy().hasHeightForWidth());
        cmbStreamSelectMode.setSizePolicy(sizePolicy);
        gridLayout.addWidget(cmbStreamSelectMode, 0, 1, 1, 2);
        label = new QLabel(SubtitlesConfig);
        label.setObjectName("label");
        gridLayout.addWidget(label, 1, 0, 1, 1);
        cmbFontBorderFactor = new QComboBox(SubtitlesConfig);
        cmbFontBorderFactor.setObjectName("cmbFontBorderFactor");
        gridLayout.addWidget(cmbFontBorderFactor, 1, 1, 1, 2);
        label_3 = new QLabel(SubtitlesConfig);
        label_3.setObjectName("label_3");
        gridLayout.addWidget(label_3, 2, 0, 1, 1);
        cmbAutomaticSubtitleLoading = new QComboBox(SubtitlesConfig);
        cmbAutomaticSubtitleLoading.setObjectName("cmbAutomaticSubtitleLoading");
        gridLayout.addWidget(cmbAutomaticSubtitleLoading, 2, 1, 1, 2);
        label_4 = new QLabel(SubtitlesConfig);
        label_4.setObjectName("label_4");
        gridLayout.addWidget(label_4, 3, 0, 1, 1);
        txtPreferredSubLang = new QLineEdit(SubtitlesConfig);
        txtPreferredSubLang.setObjectName("txtPreferredSubLang");
        gridLayout.addWidget(txtPreferredSubLang, 3, 1, 1, 2);
        label_5 = new QLabel(SubtitlesConfig);
        label_5.setObjectName("label_5");
        gridLayout.addWidget(label_5, 4, 0, 1, 1);
        spnSubDelay = new QDoubleSpinBox(SubtitlesConfig);
        spnSubDelay.setObjectName("spnSubDelay");
        spnSubDelay.setMinimum(-100000);
        spnSubDelay.setMaximum(100000);
        spnSubDelay.setSingleStep(0.25);
        gridLayout.addWidget(spnSubDelay, 4, 1, 1, 1);
        horizontalSpacer = new QSpacerItem(63, 20, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum);
        gridLayout.addItem(horizontalSpacer, 4, 2, 1, 1);
        label_2 = new QLabel(SubtitlesConfig);
        label_2.setObjectName("label_2");
        gridLayout.addWidget(label_2, 5, 0, 1, 1);
        spnOSDDuration = new QSpinBox(SubtitlesConfig);
        spnOSDDuration.setObjectName("spnOSDDuration");
        spnOSDDuration.setMaximum(99999999);
        spnOSDDuration.setSingleStep(500);
        spnOSDDuration.setValue(1000);
        gridLayout.addWidget(spnOSDDuration, 5, 1, 1, 1);
        chkASS = new QCheckBox(SubtitlesConfig);
        chkASS.setObjectName("chkASS");
        chkASS.setChecked(true);
        gridLayout.addWidget(chkASS, 6, 0, 1, 2);
        chkEmbeddedFonts = new QCheckBox(SubtitlesConfig);
        chkEmbeddedFonts.setObjectName("chkEmbeddedFonts");
        chkEmbeddedFonts.setChecked(true);
        gridLayout.addWidget(chkEmbeddedFonts, 7, 0, 1, 1);
        chkFontConfig = new QCheckBox(SubtitlesConfig);
        chkFontConfig.setObjectName("chkFontConfig");
        chkFontConfig.setChecked(true);
        gridLayout.addWidget(chkFontConfig, 8, 0, 1, 1);
        chkAllowOverlap = new QCheckBox(SubtitlesConfig);
        chkAllowOverlap.setObjectName("chkAllowOverlap");
        gridLayout.addWidget(chkAllowOverlap, 9, 0, 1, 1);
        verticalSpacer = new QSpacerItem(20, 62, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding);
        gridLayout.addItem(verticalSpacer, 10, 0, 1, 1);
        retranslateUi(SubtitlesConfig);
        cmbFontBorderFactor.setCurrentIndex(1);
        cmbAutomaticSubtitleLoading.setCurrentIndex(2);
        SubtitlesConfig.connectSlotsByName();
    }

    void retranslateUi(QWidget SubtitlesConfig) {
        SubtitlesConfig.setWindowTitle(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Form", null));
        label_6.setText(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Stream Selection Mode:", null));
        cmbStreamSelectMode.clear();
        cmbStreamSelectMode.addItem(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Auto", null));
        cmbStreamSelectMode.addItem(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Slave", null));
        cmbStreamSelectMode.addItem(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Reload", null));
        label.setText(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Font Border Factor:", null));
        cmbFontBorderFactor.clear();
        cmbFontBorderFactor.addItem(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Plain White", null));
        cmbFontBorderFactor.addItem(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Very Narrow Outline", null));
        cmbFontBorderFactor.addItem(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Narrow Outline", null));
        cmbFontBorderFactor.addItem(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Bold Outline", null));
        label_3.setText(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Automatic Subtitle Loading:", null));
        cmbAutomaticSubtitleLoading.clear();
        cmbAutomaticSubtitleLoading.addItem(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Don't Auto Load", null));
        cmbAutomaticSubtitleLoading.addItem(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Exact Match", null));
        cmbAutomaticSubtitleLoading.addItem(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Contains Name", null));
        cmbAutomaticSubtitleLoading.addItem(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "All From Directory", null));
        label_4.setText(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Preferred Subtitle Languages:", null));
        txtPreferredSubLang.setText(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "en,eng,english", null));
        label_5.setText(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Subtitle Delay:", null));
        spnSubDelay.setSuffix(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", " Seconds", null));
        label_2.setText(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "OSD Display Duration:", null));
        spnOSDDuration.setSuffix(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", " ms", null));
        chkASS.setText(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Enable SSA/ASS Subtitle Renderer", null));
        chkEmbeddedFonts.setText(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Use Embedded Fonts", null));
        chkFontConfig.setText(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Enable FontConfig", null));
        chkAllowOverlap.setText(com.trolltech.qt.core.QCoreApplication.translate("SubtitlesConfig", "Allow subtitles to overlap", null));
    }
}
