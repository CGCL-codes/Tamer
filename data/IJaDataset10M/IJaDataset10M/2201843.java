package de.shandschuh.jaolt.gui.settings;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import com.jgoodies.forms.builder.PanelBuilder;
import de.shandschuh.jaolt.core.Language;
import de.shandschuh.jaolt.core.Member;
import de.shandschuh.jaolt.core.Settings;
import de.shandschuh.jaolt.core.UpdateChannel;
import de.shandschuh.jaolt.core.member.PictureSettings;
import de.shandschuh.jaolt.gui.FormManager;
import de.shandschuh.jaolt.gui.Lister;
import de.shandschuh.jaolt.gui.core.buttons.SelectDirectoryJButton;
import de.shandschuh.jaolt.gui.listener.dialogs.importdialogs.SelectActionListener;

public class CommonSettingFormManager extends FormManager {

    private static final long serialVersionUID = 1L;

    private JCheckBox copyImagesJCheckBox;

    private JCheckBox resizeImagesJCheckBox;

    private JSpinner sizeJSpinner;

    private JLabel maxSizeJLabel;

    private JCheckBox disableHTMLEditorJCheckBox;

    private JSpinner saveIntervalJSpinner;

    private JCheckBox presetStandardJCheckBox;

    private JCheckBox searchNewVersionJCheckBox;

    private JCheckBox searchNewStaticDataJCheckBox;

    private JComboBox lookAndFeelJComboBox;

    private JCheckBox notifyCancelJCheckBox;

    private SelectDirectoryJButton selectPictureDirJButton;

    private Member member;

    public CommonSettingFormManager(Member member) {
        this.member = member;
        PictureSettings pictureSettings = member.getPictureSettings() == null ? new PictureSettings() : member.getPictureSettings();
        copyImagesJCheckBox = new JCheckBox(Language.translateStatic("PICTURESETTINGS_COPY"));
        copyImagesJCheckBox.setSelected(pictureSettings.isCopy());
        disableHTMLEditorJCheckBox = new JCheckBox(Language.translateStatic("DISBALEHTMLEDITOR"));
        disableHTMLEditorJCheckBox.setSelected(member.isDisableHTMLEditor());
        saveIntervalJSpinner = new JSpinner(new SpinnerNumberModel(member.getAutoSaveInterval(), 1, 120, 5));
        resizeImagesJCheckBox = new JCheckBox(Language.translateStatic("PICTURESETTINGS_RESIZE"));
        maxSizeJLabel = new JLabel(Language.translateStatic("PICTURESETTINGS_MAXSIZE"));
        sizeJSpinner = new JSpinner(new SpinnerNumberModel(600, 100, 1200, 100));
        if (pictureSettings.isScale()) {
            resizeImagesJCheckBox.setSelected(true);
        } else {
            resizeImagesJCheckBox.setSelected(false);
            maxSizeJLabel.setEnabled(false);
            sizeJSpinner.setEnabled(false);
        }
        int scaleSize = pictureSettings.getScaleSize();
        if (scaleSize > 99 && scaleSize < 1201) {
            sizeJSpinner.setValue(scaleSize);
        }
        resizeImagesJCheckBox.addActionListener(new SelectActionListener(resizeImagesJCheckBox, new JComponent[] { maxSizeJLabel, sizeJSpinner }));
        presetStandardJCheckBox = new JCheckBox(Language.translateStatic("PRESET_STANDARD"), member.isPresetStandard());
        Settings settings = Lister.getCurrentInstance().getSettings();
        searchNewVersionJCheckBox = new JCheckBox(Language.translateStatic("SEARCHFORNEWVERSIONS"), settings.isSearchNewVersion());
        if (!UpdateChannel.getCurrentChannel().hasUpdateInformation()) {
            searchNewVersionJCheckBox.setSelected(false);
            searchNewVersionJCheckBox.setEnabled(false);
        }
        searchNewStaticDataJCheckBox = new JCheckBox(Language.translateStatic("SEARCHFORNEWSTATICDATA"), settings.isSearchNewStaticData());
        lookAndFeelJComboBox = new JComboBox(UIManager.getInstalledLookAndFeels());
        lookAndFeelJComboBox.setRenderer(new LookAndFeelInfoListCellRenderer(lookAndFeelJComboBox.getRenderer()));
        lookAndFeelJComboBox.setSelectedIndex(getLafIndex(settings.getLafClassName()));
        notifyCancelJCheckBox = new JCheckBox(Language.translateStatic("NOTIFYCANCEL"));
        notifyCancelJCheckBox.setSelected(settings.isNotifyCancel());
        selectPictureDirJButton = new SelectDirectoryJButton(settings.getPictureDir());
    }

    private int getLafIndex(String classname) {
        if (classname == null || classname.length() == 0) {
            classname = UIManager.getLookAndFeel().getClass().getName();
        }
        for (int n = 0, i = UIManager.getInstalledLookAndFeels().length; n < i; n++) {
            if (UIManager.getInstalledLookAndFeels()[n].getClassName().equals(classname)) {
                return n;
            }
        }
        return -1;
    }

    @Override
    protected void addPanelBuilderComponents(PanelBuilder panelBuilder) {
        panelBuilder.addSeparator(Language.translateStatic("PICTURESETTINGS"), getCellConstraints(1, 1, 5));
        panelBuilder.add(copyImagesJCheckBox, getCellConstraints(2, 3, 3));
        panelBuilder.add(resizeImagesJCheckBox, getCellConstraints(2, 5));
        panelBuilder.add(maxSizeJLabel, getCellConstraints(2, 7));
        panelBuilder.add(sizeJSpinner, getCellConstraints(4, 7));
        panelBuilder.addSeparator(Language.translateStatic("HTMLEDITOR"), getCellConstraints(1, 9, 5));
        panelBuilder.add(disableHTMLEditorJCheckBox, getCellConstraints(2, 11, 3));
        panelBuilder.addSeparator(Language.translateStatic("AUTOSAVE"), getCellConstraints(1, 13, 5));
        panelBuilder.addLabel(Language.translateStatic("AUTOSAVEINTERVAL"), getCellConstraints(2, 15));
        panelBuilder.add(saveIntervalJSpinner, getCellConstraints(4, 15));
        panelBuilder.addSeparator(Language.translateStatic("STANDARDSETTINGS"), getCellConstraints(1, 17, 5));
        panelBuilder.add(presetStandardJCheckBox, getCellConstraints(2, 19, 3));
        panelBuilder.add(searchNewVersionJCheckBox, getCellConstraints(2, 21, 3));
        panelBuilder.add(searchNewStaticDataJCheckBox, getCellConstraints(2, 23, 3));
        panelBuilder.add(notifyCancelJCheckBox, getCellConstraints(2, 25, 3));
        panelBuilder.addLabel(Language.translateStatic("PICTUREDIR"), getCellConstraints(2, 27));
        panelBuilder.add(selectPictureDirJButton, getCellConstraints(4, 27));
        panelBuilder.addLabel(Language.translateStatic("LOOKANDFEEL"), getCellConstraints(2, 29));
        panelBuilder.add(lookAndFeelJComboBox, getCellConstraints(4, 29));
        panelBuilder.addLabel(Language.translateStatic("EFFECT_AFTER_RESTART"), getCellConstraints(4, 31));
    }

    @Override
    protected String getColumnLayout() {
        return "5dlu, p, 4dlu, p, fill:4dlu:grow";
    }

    @Override
    public String getName() {
        return Language.translateStatic("COMMONSETTINGS");
    }

    @Override
    protected String getRowLayout() {
        return "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p";
    }

    @Override
    protected void reloadLocal(boolean rebuild) {
    }

    @Override
    protected void saveLocal() throws Exception {
        if (member.getPictureSettings() == null) {
            member.setPictureSettings(new PictureSettings());
        }
        PictureSettings pictureSettings = member.getPictureSettings();
        pictureSettings.setCopy(copyImagesJCheckBox.isSelected());
        pictureSettings.setScale(resizeImagesJCheckBox.isSelected());
        pictureSettings.setScaleSize((Integer) sizeJSpinner.getValue());
        member.setDisableHTMLEditor(disableHTMLEditorJCheckBox.isSelected());
        member.setAutoSaveInterval((Integer) saveIntervalJSpinner.getValue());
        member.setPresetStandard(presetStandardJCheckBox.isSelected());
        Settings settings = Lister.getCurrentInstance().getSettings();
        settings.setSearchNewVersion(searchNewVersionJCheckBox.isSelected());
        settings.setSearchNewStaticData(searchNewStaticDataJCheckBox.isSelected());
        settings.setLafClassName(lookAndFeelJComboBox.getSelectedIndex() > -1 ? ((LookAndFeelInfo) lookAndFeelJComboBox.getSelectedItem()).getClassName() : "");
        settings.setNotifyCancel(notifyCancelJCheckBox.isSelected());
        settings.setPictureDir(selectPictureDirJButton.getDir());
    }

    @Override
    protected void validateLocal() throws Exception {
    }

    @Override
    public boolean rebuildNeeded() {
        return false;
    }
}
