package org.geoforge.guitlc.dialog.edit.panel;

import org.geoforge.guitlc.dialog.edit.panel.PnlSettingsAbs;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.logging.Logger;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import org.geoforge.guillc.optionpane.GfrOptionPaneAbs;
import org.geoforge.guillc.panel.PnlSelAbs;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class PnlContentsOkTextNewSettingsAbs extends PnlContentsOkTextNewAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(PnlContentsOkTextNewSettingsAbs.class.getName());

    static {
        PnlContentsOkTextNewSettingsAbs._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    protected PnlSettingsAbs _pnlSettings = null;

    protected PnlContentsOkTextNewSettingsAbs(DocumentListener dlr, java.awt.event.KeyListener klr, String strWhat, String strWhatPlural, String[] strsExistingItems) {
        super(dlr, klr, strWhat, strWhatPlural, strsExistingItems);
    }

    public String getWrongDataSettings() {
        return this._pnlSettings.getWrongFormatData();
    }

    public boolean isOkDataSettings() {
        return this._pnlSettings.isOkData();
    }

    public boolean belongsToDataSettings(Document doc) {
        return this._pnlSettings.belongsTo(doc);
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        if (!this._pnlSettings.init()) return false;
        super._pnlSouth.add(this._pnlSettings, BorderLayout.CENTER);
        if (!_alignLabels_()) {
            String str = "! _alignLabels_()";
            PnlContentsOkTextNewSettingsAbs._LOGGER_.severe(str);
            GfrOptionPaneAbs.s_showDialogError(null, str);
            return false;
        }
        return true;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (this._pnlSettings != null) {
            this._pnlSettings.destroy();
            this._pnlSettings = null;
        }
    }

    @Override
    protected boolean _alignLabels_() {
        java.util.Vector<PnlSelAbs> vecPanel = new java.util.Vector<PnlSelAbs>();
        Component[] cmps = null;
        cmps = this._pnlSettings.getComponents();
        for (int i = 0; i < cmps.length; i++) {
            if (!(cmps[i] instanceof PnlSelAbs)) continue;
            PnlSelAbs pnlCur = (PnlSelAbs) cmps[i];
            vecPanel.add(pnlCur);
        }
        PnlSelAbs pnlNewName = (PnlSelAbs) super._pnlSelChooserValueTarget;
        vecPanel.add(pnlNewName);
        if (super._pnlSelDisplayValuesCurrent != null) {
            PnlSelAbs pnlList = (PnlSelAbs) super._pnlSelDisplayValuesCurrent;
            vecPanel.add(pnlList);
        }
        if (!PnlSelAbs.s_alignLabels(vecPanel)) {
            String str = "! PnlSelAbs.s_alignLabels(vecPanel)";
            PnlContentsOkTextNewSettingsAbs._LOGGER_.severe(str);
            return false;
        }
        vecPanel.clear();
        vecPanel = null;
        return true;
    }
}
