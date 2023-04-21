package org.geoforge.guitlcolg.dialog.tabs.settings.oxp.panel;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
import org.geoforge.guitlc.dialog.tabs.settings.panel.PnlDspLineAbs;
import org.geoforge.guitlc.dialog.tabs.settings.panel.PnlDspLineColorDft;
import org.geoforge.guitlc.dialog.tabs.settings.panel.PnlDspLineSliderAbs;
import org.geoforge.guitlc.dialog.tabs.settings.panel.PnlDspLineSliderThicknessDft;
import org.geoforge.lang.util.logging.FileHandlerLogger;
import org.geoforge.mdldspolg.render.logs.oxp.GfrMdlDspRndLogs;
import org.geoforge.wrpbasprsdspolg.render.oxp.logs.WrpRenderDefaultLogs;

/**
 *
 * @author bantchao
 */
public class PnlTabsSettingsDspLogTopWlls extends PnlTabsSettingsDspLogAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(PnlTabsSettingsDspLogTopWlls.class.getName());

    static {
        PnlTabsSettingsDspLogTopWlls._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public static final String STR_TITLE = "Default well logs viewer display";

    private PnlDspLineAbs _pnlColorLogs_ = null;

    private PnlDspLineAbs _pnlColorMarkers_ = null;

    private PnlDspLineAbs _pnlThicknessLogs_ = null;

    private PnlDspLineAbs _pnlThicknessMarkers_ = null;

    private PnlDspLineAbs _pnlShapeMarkers_ = null;

    public PnlTabsSettingsDspLogTopWlls(ActionListener alrParent, ChangeListener clrParent) throws Exception {
        super();
        Color colCustomLogs = null;
        if (!WrpRenderDefaultLogs.getInstance().isColorRandomLog()) colCustomLogs = WrpRenderDefaultLogs.getInstance().getColorLog();
        Color colCustomMarkers = null;
        if (!WrpRenderDefaultLogs.getInstance().isColorRandomMarker()) colCustomMarkers = WrpRenderDefaultLogs.getInstance().getColorMarker();
        int intThicknessLogs = WrpRenderDefaultLogs.getInstance().getThicknessLog();
        int intThicknessMarkers = WrpRenderDefaultLogs.getInstance().getThicknessMarker();
        boolean blnIsSolidLineMarker = WrpRenderDefaultLogs.getInstance().isShapeSolidMarker();
        this._pnlColorLogs_ = new PnlDspLineColorDft(alrParent, "Logs color:", colCustomLogs);
        this._pnlColorMarkers_ = new PnlDspLineColorDft(alrParent, "Markers color:", colCustomMarkers);
        this._pnlThicknessLogs_ = new PnlDspLineSliderThicknessDft(clrParent, intThicknessLogs, "Logs");
        this._pnlThicknessMarkers_ = new PnlDspLineSliderThicknessDft(clrParent, intThicknessMarkers, "Markers");
        this._pnlShapeMarkers_ = new PnlDspLineLogsShapeMarkerDft(alrParent, blnIsSolidLineMarker, "Markers shape");
    }

    @Override
    public void doJob() throws Exception {
        if (this._pnlColorLogs_.hasChangedValue()) {
            Color colValue = ((PnlDspLineColorDft) this._pnlColorLogs_).getValue();
            boolean bln = this._pnlColorLogs_.isApplyToAll();
            GfrMdlDspRndLogs.getInstance().setColorDefaultLog(colValue, bln);
        }
        if (this._pnlColorMarkers_.hasChangedValue()) {
            Color colValue = ((PnlDspLineColorDft) this._pnlColorMarkers_).getValue();
            boolean bln = this._pnlColorMarkers_.isApplyToAll();
            GfrMdlDspRndLogs.getInstance().setColorDefaultMarker(colValue, bln);
        }
        if (this._pnlThicknessLogs_.hasChangedValue()) {
            int intValue = ((PnlDspLineSliderAbs) this._pnlThicknessLogs_).getValue();
            boolean bln = this._pnlThicknessLogs_.isApplyToAll();
            GfrMdlDspRndLogs.getInstance().setThicknessDefaultLog(intValue, bln);
        }
        if (this._pnlThicknessMarkers_.hasChangedValue()) {
            int intValue = ((PnlDspLineSliderAbs) this._pnlThicknessMarkers_).getValue();
            boolean bln = this._pnlThicknessMarkers_.isApplyToAll();
            GfrMdlDspRndLogs.getInstance().setThicknessDefaultMarker(intValue, bln);
        }
        if (this._pnlShapeMarkers_.hasChangedValue()) {
            boolean blnValue = ((PnlDspLineLogsShapeMarkerDft) this._pnlShapeMarkers_).getValue();
            boolean bln = this._pnlShapeMarkers_.isApplyToAll();
            GfrMdlDspRndLogs.getInstance().setShapeMarkerDefault(blnValue, bln);
        }
        this._pnlColorLogs_.doJob();
        this._pnlColorMarkers_.doJob();
        this._pnlThicknessLogs_.doJob();
        this._pnlThicknessMarkers_.doJob();
        this._pnlShapeMarkers_.doJob();
    }

    @Override
    public boolean hasChangedValue() {
        if (this._pnlColorLogs_.hasChangedValue()) return true;
        if (this._pnlColorMarkers_.hasChangedValue()) return true;
        if (this._pnlThicknessLogs_.hasChangedValue()) return true;
        if (this._pnlThicknessMarkers_.hasChangedValue()) return true;
        if (this._pnlShapeMarkers_.hasChangedValue()) return true;
        return false;
    }

    @Override
    protected boolean _alignLabels() {
        java.util.Vector<PnlDspLineAbs> vecPanel = new java.util.Vector<PnlDspLineAbs>();
        vecPanel.add(_pnlColorLogs_);
        vecPanel.add(_pnlColorMarkers_);
        vecPanel.add(_pnlThicknessLogs_);
        vecPanel.add(_pnlThicknessMarkers_);
        vecPanel.add(_pnlShapeMarkers_);
        if (!PnlDspLineAbs.s_alignLabels(vecPanel, 2)) {
            String str = "! PnlSelAbs.s_alignLabels(vecPanel)";
            PnlTabsSettingsDspLogTopWlls._LOGGER_.severe(str);
            return false;
        }
        vecPanel.clear();
        vecPanel = null;
        return true;
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        if (!this._pnlColorLogs_.init()) return false;
        if (!this._pnlColorMarkers_.init()) return false;
        if (!this._pnlThicknessLogs_.init()) return false;
        if (!this._pnlThicknessMarkers_.init()) return false;
        if (!this._pnlShapeMarkers_.init()) return false;
        super.add(this._pnlColorLogs_);
        super.add(this._pnlColorMarkers_);
        super.add(this._pnlThicknessLogs_);
        super.add(this._pnlThicknessMarkers_);
        super.add(this._pnlShapeMarkers_);
        _alignLabels();
        return true;
    }

    @Override
    public void destroy() {
        if (this._pnlColorLogs_ != null) {
            this._pnlColorLogs_.destroy();
            this._pnlColorLogs_ = null;
        }
        if (this._pnlColorMarkers_ != null) {
            this._pnlColorMarkers_.destroy();
            this._pnlColorMarkers_ = null;
        }
        if (this._pnlThicknessLogs_ != null) {
            this._pnlThicknessLogs_.destroy();
            this._pnlThicknessLogs_ = null;
        }
        if (this._pnlThicknessMarkers_ != null) {
            this._pnlThicknessMarkers_.destroy();
            this._pnlThicknessMarkers_ = null;
        }
        if (this._pnlShapeMarkers_ != null) {
            this._pnlShapeMarkers_.destroy();
            this._pnlShapeMarkers_ = null;
        }
        super.destroy();
    }
}
