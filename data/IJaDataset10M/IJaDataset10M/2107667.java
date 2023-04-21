package org.geoforge.guitlc.dialog.panel.group.single;

import java.awt.BorderLayout;
import org.geoforge.guillc.label.GfrLbl;
import org.geoforge.guitlc.dialog.panel.group.GfSizeComponent;

/**
 *
 * @author Amadeus.Sowerby
 *
 * email: Amadeus.Sowerby_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 */
public class GfrPnlGrpSngLbl extends GfrPnlGrpSngAbs {

    public GfrPnlGrpSngLbl(String strWhat, String strLblContent) {
        super(strWhat, GfSizeComponent.LBL_HEIGHT_);
        this._lblContent_ = new GfrLbl(strLblContent);
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        super.setMandatory(false);
        this._pnlContent.add(this._lblContent_, BorderLayout.CENTER);
        return true;
    }

    public void setText(String str) {
        this._lblContent_.setText(str);
    }

    private GfrLbl _lblContent_;
}
