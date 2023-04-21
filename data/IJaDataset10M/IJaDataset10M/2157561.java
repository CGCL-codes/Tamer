package phex.gui.dialogs.options;

import java.awt.BorderLayout;
import java.util.HashMap;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import phex.gui.common.GUIRegistry;
import phex.gui.prefs.InterfacePrefs;
import phex.utils.Localizer;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class DisplayPromptsPane extends OptionsSettingsPane {

    private GUIRegistry guiRegistry;

    private JCheckBox showCloseOptionsChkbx;

    private JCheckBox showBetaUpdateNotificationChkbx;

    private JCheckBox showCopyrightLawsWarningChkbx;

    public DisplayPromptsPane() {
        super("PromptSettings_Prompts");
        guiRegistry = GUIRegistry.getInstance();
    }

    /**
     * Called when preparing this settings pane for display the first time. Can
     * be overriden to implement the look of the settings pane.
     */
    @Override
    protected void prepareComponent() {
        setLayout(new BorderLayout());
        JPanel contentPanel = new JPanel();
        add(contentPanel, BorderLayout.CENTER);
        FormLayout layout = new FormLayout("10dlu, d, 6dlu, d, 2dlu:grow", "p, 3dlu, p, 3dlu, p, 3dlu, p");
        layout.setRowGroups(new int[][] { { 3, 5 } });
        contentPanel.setLayout(layout);
        PanelBuilder builder = new PanelBuilder(layout, contentPanel);
        CellConstraints cc = new CellConstraints();
        builder.addSeparator(Localizer.getString("PromptSettings_PromptSettings"), cc.xywh(1, 1, 5, 1));
        showCloseOptionsChkbx = new JCheckBox(Localizer.getString("PromptSettings_ClosePhexOptions"), InterfacePrefs.ShowCloseOptionsDialog.get().booleanValue());
        showCloseOptionsChkbx.setToolTipText(Localizer.getString("PromptSettings_TTTClosePhexOptions"));
        builder.add(showCloseOptionsChkbx, cc.xy(2, 3));
        showBetaUpdateNotificationChkbx = new JCheckBox(Localizer.getString("PromptSettings_NotifyOnNewBeta"), InterfacePrefs.ShowBetaUpdateNotification.get().booleanValue());
        showBetaUpdateNotificationChkbx.setToolTipText(Localizer.getString("PromptSettings_TTTNotifyOnNewBeta"));
        builder.add(showBetaUpdateNotificationChkbx, cc.xy(2, 5));
        showCopyrightLawsWarningChkbx = new JCheckBox(Localizer.getString("PromptSettings_RespectCopyrightNotice"), guiRegistry.isRespectCopyrightNoticeShown());
        showCopyrightLawsWarningChkbx.setToolTipText(Localizer.getString("PromptSettings_TTTRespectCopyrightNotice"));
        builder.add(showCopyrightLawsWarningChkbx, cc.xy(2, 7));
    }

    /**
     * Override this method if you like to apply and save changes made on
     * settings pane. To trigger saving of the configuration if any value was
     * changed call triggerConfigSave().
     */
    @Override
    public void saveAndApplyChanges(HashMap inputDic) {
        boolean closeOptions = showCloseOptionsChkbx.isSelected();
        InterfacePrefs.ShowCloseOptionsDialog.set(Boolean.valueOf(closeOptions));
        boolean betaUpdateNotify = showBetaUpdateNotificationChkbx.isSelected();
        InterfacePrefs.ShowBetaUpdateNotification.set(Boolean.valueOf(betaUpdateNotify));
        boolean showCopyrightWarning = showCopyrightLawsWarningChkbx.isSelected();
        if (guiRegistry.isRespectCopyrightNoticeShown() != showCopyrightWarning) {
            guiRegistry.setRespectCopyrightNoticeShown(showCopyrightWarning);
        }
    }
}
