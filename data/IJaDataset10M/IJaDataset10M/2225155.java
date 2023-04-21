package org.geoforge.guitlcolg.dialog.edit.data.oxp.panel;

import org.geoforge.guitlc.dialog.edit.data.panel.PnlSelChooseUnitAngle;
import org.geoforge.guitlc.dialog.edit.data.panel.PnlSelChooseUnitDistance;
import org.geoforge.guitlcolg.dialog.edit.data.oxp.panel.PnlSettingsNew_SurveyAbs;
import javax.swing.Box;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import org.geoforge.guitlc.dialog.edit.data.panel.PnlSelEditTfdMultipleIntegerTwinAngleAzimuth;
import org.geoforge.guitlc.dialog.edit.panel.PnlSelEditTfdMultipleIntegerTwin;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 * 
 * should contain at least one value from:
 * . time
 * . depth
 *
 */
public class PnlSettingsNewBlockSeismic3d extends PnlSettingsNew_SurveyAbs {

    private PnlSelEditTfdMultipleIntegerTwin _pnlOriginGrid_ = null;

    private PnlSelEditTfdMultipleIntegerTwin _pnlOriginGeometry_ = null;

    private PnlSelEditTfdMultipleIntegerTwinAngleAzimuth _pnlAzimuthGrid_ = null;

    private PnlSelEditTfdMultipleIntegerTwin _pnlNumberGrid_ = null;

    private PnlSelEditTfdMultipleIntegerTwin _pnlBinGrid_ = null;

    private PnlSelChooseUnitDistance _pnlUnitGeometry_ = null;

    private PnlSelChooseUnitAngle _pnlUnitAngle_ = null;

    public int getOriginIl() throws Exception {
        return this._pnlOriginGrid_.getValue1();
    }

    public int getOriginXl() throws Exception {
        return this._pnlOriginGrid_.getValue2();
    }

    public int getOriginX() throws Exception {
        return this._pnlOriginGeometry_.getValue1();
    }

    public int getOriginY() throws Exception {
        return this._pnlOriginGeometry_.getValue2();
    }

    public int getAzimuthIl() throws Exception {
        return this._pnlAzimuthGrid_.getValue1();
    }

    public int getAzimuthXl() throws Exception {
        return this._pnlAzimuthGrid_.getValue2();
    }

    public int getNumberIl() throws Exception {
        return this._pnlNumberGrid_.getValue1();
    }

    public int getNumberXl() throws Exception {
        return this._pnlNumberGrid_.getValue2();
    }

    public int getBinIl() throws Exception {
        return this._pnlBinGrid_.getValue1();
    }

    public int getBinXl() throws Exception {
        return this._pnlBinGrid_.getValue2();
    }

    public boolean isUnitGeometryMeter() {
        return this._pnlUnitGeometry_.isSelectedMeters();
    }

    public boolean isUnitAngleDegrees() {
        return this._pnlUnitAngle_.isSelectedDegrees();
    }

    @Override
    public String getWrongFormatData() {
        String str = super.getWrongFormatData();
        if (str != null) return str;
        str = this._pnlOriginGrid_.getWrongFormat();
        if (str != null) return str;
        str = this._pnlOriginGeometry_.getWrongFormat();
        if (str != null) return str;
        str = this._pnlAzimuthGrid_.getWrongFormat(isUnitAngleDegrees());
        if (str != null) return str;
        str = this._pnlNumberGrid_.getWrongFormat();
        if (str != null) return str;
        str = this._pnlBinGrid_.getWrongFormat();
        if (str != null) return str;
        return null;
    }

    @Override
    public boolean isOkData() {
        if (!super.isOkData()) return false;
        if (!this._pnlOriginGrid_.isOk()) return false;
        if (!this._pnlOriginGeometry_.isOk()) return false;
        if (!this._pnlAzimuthGrid_.isOk(isUnitAngleDegrees())) return false;
        if (!this._pnlNumberGrid_.isOk()) return false;
        if (!this._pnlBinGrid_.isOk()) return false;
        return true;
    }

    @Override
    public boolean belongsTo(Document doc) {
        if (super.belongsTo(doc)) return true;
        if (this._pnlOriginGrid_.belongsTo(doc)) return true;
        if (this._pnlOriginGeometry_.belongsTo(doc)) return true;
        if (this._pnlAzimuthGrid_.belongsTo(doc)) return true;
        if (this._pnlNumberGrid_.belongsTo(doc)) return true;
        if (this._pnlBinGrid_.belongsTo(doc)) return true;
        return false;
    }

    public PnlSettingsNewBlockSeismic3d(DocumentListener lstDocument) {
        super(lstDocument);
        this._pnlOriginGrid_ = new PnlSelEditTfdMultipleIntegerTwin("Grid origin (Inline-Crossline):", true, lstDocument);
        this._pnlOriginGeometry_ = new PnlSelEditTfdMultipleIntegerTwin("Geometry origin (X-Y):", true, lstDocument);
        this._pnlAzimuthGrid_ = new PnlSelEditTfdMultipleIntegerTwinAngleAzimuth(lstDocument);
        this._pnlNumberGrid_ = new PnlSelEditTfdMultipleIntegerTwin("Grid number (Traces-Inlines):", true, lstDocument);
        this._pnlBinGrid_ = new PnlSelEditTfdMultipleIntegerTwin("Grid interval (Traces-Inlines):", true, lstDocument);
        this._pnlUnitGeometry_ = new PnlSelChooseUnitDistance("Geometry");
        this._pnlUnitAngle_ = new PnlSelChooseUnitAngle("Azimuth angle");
    }

    @Override
    public void destroy() {
        super.destroy();
        if (this._pnlOriginGrid_ != null) {
            this._pnlOriginGrid_.destroy();
            this._pnlOriginGrid_ = null;
        }
        if (this._pnlOriginGeometry_ != null) {
            this._pnlOriginGeometry_.destroy();
            this._pnlOriginGeometry_ = null;
        }
        if (this._pnlAzimuthGrid_ != null) {
            this._pnlAzimuthGrid_.destroy();
            this._pnlAzimuthGrid_ = null;
        }
        if (this._pnlNumberGrid_ != null) {
            this._pnlNumberGrid_.destroy();
            this._pnlNumberGrid_ = null;
        }
        if (this._pnlBinGrid_ != null) {
            this._pnlBinGrid_.destroy();
            this._pnlBinGrid_ = null;
        }
        if (this._pnlUnitGeometry_ != null) {
            this._pnlUnitGeometry_.destroy();
            this._pnlUnitGeometry_ = null;
        }
        if (this._pnlUnitAngle_ != null) {
            this._pnlUnitAngle_.destroy();
            this._pnlUnitAngle_ = null;
        }
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        if (!this._pnlOriginGrid_.init()) return false;
        if (!this._pnlOriginGeometry_.init()) return false;
        if (!this._pnlAzimuthGrid_.init()) return false;
        if (!this._pnlNumberGrid_.init()) return false;
        if (!this._pnlBinGrid_.init()) return false;
        if (!this._pnlUnitGeometry_.init()) return false;
        if (!this._pnlUnitAngle_.init()) return false;
        super.add(this._pnlOriginGrid_);
        super.add(Box.createVerticalStrut(10));
        super.add(this._pnlOriginGeometry_);
        super.add(Box.createVerticalStrut(10));
        super.add(this._pnlAzimuthGrid_);
        super.add(Box.createVerticalStrut(10));
        super.add(this._pnlNumberGrid_);
        super.add(Box.createVerticalStrut(10));
        super.add(this._pnlBinGrid_);
        super.add(Box.createVerticalStrut(10));
        super.add(this._pnlUnitGeometry_);
        super.add(Box.createVerticalStrut(10));
        super.add(this._pnlUnitAngle_);
        super.add(Box.createVerticalStrut(10));
        super.add(super._pnlUrl);
        super.add(Box.createVerticalStrut(10));
        return true;
    }
}
