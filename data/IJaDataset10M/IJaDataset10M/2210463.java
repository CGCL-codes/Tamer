package org.geoforge.guitlc.dialog.tabs.settings;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.geoforge.guillc.button.BtnChoiceColorRectangle;
import org.geoforge.guillc.button.BtnCmdApply;
import org.geoforge.guillc.button.BtnCmdOk;
import org.geoforge.guillc.dialog.panel.GfrPnlCmdCancelApplyOk;
import org.geoforge.guillc.optionpane.GfrOptionPaneAbs;
import org.geoforge.guitlc.dialog.tabs.settings.combobox.CmbAnnotation;
import org.geoforge.guitlc.dialog.tabs.settings.radiobutton.GfrRbnSettingsColAbs;
import org.geoforge.guitlc.dialog.tabs.settings.radiobutton.GfrRbnSettingsTipAbs;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 */
public abstract class GfrDlgTabsSettingsDspGlbDftActionAbs extends GfrDlgTabsSettingsDspGlbAbs implements ChangeListener {

    private static final Logger _LOGGER_ = Logger.getLogger(GfrDlgTabsSettingsDspGlbDftActionAbs.class.getName());

    static {
        GfrDlgTabsSettingsDspGlbDftActionAbs._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    protected GfrDlgTabsSettingsDspGlbDftActionAbs(Frame frmOwner, String strKindObject) {
        super(frmOwner, strKindObject);
        super._pnlCommands = new GfrPnlCmdCancelApplyOk((ActionListener) this);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Object objSource = evt.getSource();
        if (objSource instanceof CmbAnnotation) {
            boolean bln = super._tabContents_.hasChangedValue();
            ((GfrPnlCmdCancelApplyOk) super._pnlCommands).setEnabledOk(bln);
            return;
        }
        if (objSource instanceof GfrRbnSettingsTipAbs) {
            boolean bln = super._tabContents_.hasChangedValue();
            ((GfrPnlCmdCancelApplyOk) super._pnlCommands).setEnabledOk(bln);
            return;
        }
        try {
            if (evt.getSource() instanceof BtnCmdApply) {
                this._tabContents_.doJob();
                ((GfrPnlCmdCancelApplyOk) this._pnlCommands).setEnabledOk(false);
                return;
            }
            if (evt.getSource() instanceof BtnCmdOk) {
                this._tabContents_.doJob();
                super.destroy();
                return;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            String str = exc.getMessage();
            GfrDlgTabsSettingsDspGlbDftActionAbs._LOGGER_.severe(str);
            GfrOptionPaneAbs.s_showDialogError(this, str);
            System.exit(1);
        }
        if (evt.getSource() instanceof GfrRbnSettingsColAbs) {
            boolean bln = super._tabContents_.hasChangedValue();
            ((GfrPnlCmdCancelApplyOk) super._pnlCommands).setEnabledOk(bln);
            return;
        }
        if (evt.getSource() instanceof BtnChoiceColorRectangle) {
            boolean bln = super._tabContents_.hasChangedValue();
            ((GfrPnlCmdCancelApplyOk) super._pnlCommands).setEnabledOk(bln);
            return;
        }
        super.actionPerformed(evt);
    }

    @Override
    public void stateChanged(ChangeEvent evt) {
        Object objSource = evt.getSource();
        if (objSource instanceof JSlider) {
            JSlider sld = (JSlider) objSource;
            if (sld.getValueIsAdjusting()) return;
            boolean bln = super._tabContents_.hasChangedValue();
            ((GfrPnlCmdCancelApplyOk) super._pnlCommands).setEnabledOk(bln);
            return;
        }
        String str = "!! FORCING AN EXIT - uncaught objSource.getClass().getName()" + objSource.getClass().getName();
        GfrDlgTabsSettingsDspGlbDftActionAbs._LOGGER_.severe(str);
        GfrOptionPaneAbs.s_showDialogError(super.getOwner(), str);
        System.exit(1);
    }
}
