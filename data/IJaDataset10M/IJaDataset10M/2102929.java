package org.geoforge.guitlcolg.dialog.edit.data.oxp.panel;

import org.geoforge.guitlc.dialog.edit.data.panel.PnlSelChooseUnitDistance;
import javax.swing.Box;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import org.geoforge.guitlc.dialog.edit.data.panel.PnlSelEditTfdSingleFloatDepth;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 * 
 * should contain :
 * . time
 * . depth
 *
 */
public class PnlSettingsNewMarkerWellDepth extends PnlSettingsNewMarkerWellAbs {

    private PnlSelChooseUnitDistance _pnlUnitDepth_ = null;

    private PnlSelEditTfdSingleFloatDepth _pnlValue_ = null;

    public float getValueDepth() throws Exception {
        return this._pnlValue_.getValue();
    }

    public boolean isUnitDepthMeter() {
        return this._pnlUnitDepth_.isSelectedMeters();
    }

    @Override
    public String getWrongFormatData() {
        String str = this._pnlValue_.getWrongFormat();
        if (str != null) return str;
        return null;
    }

    /**
    *
    * should contain at least one value from:
    * . time
    * . depth
    *
    */
    @Override
    public boolean isOkData() {
        if (!this._pnlValue_.isOk()) return false;
        return true;
    }

    @Override
    public boolean belongsTo(Document doc) {
        if (this._pnlValue_.belongsTo(doc)) return true;
        return false;
    }

    public PnlSettingsNewMarkerWellDepth(DocumentListener lstDocument) {
        super();
        this._pnlUnitDepth_ = new PnlSelChooseUnitDistance("Depth");
        this._pnlValue_ = new PnlSelEditTfdSingleFloatDepth(lstDocument, true);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (this._pnlUnitDepth_ != null) {
            this._pnlUnitDepth_.destroy();
            this._pnlUnitDepth_ = null;
        }
        if (this._pnlValue_ != null) {
            this._pnlValue_.destroy();
            this._pnlValue_ = null;
        }
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        if (!this._pnlUnitDepth_.init()) return false;
        if (!this._pnlValue_.init()) return false;
        super.add(Box.createVerticalStrut(10));
        super.add(this._pnlUnitDepth_);
        super.add(Box.createVerticalStrut(10));
        super.add(this._pnlValue_);
        super.add(Box.createVerticalStrut(10));
        return true;
    }
}
