package org.geoforge.guitlc.dialog.edit.data.button;

import java.awt.event.ActionListener;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class BtnIcnShapeSelectListUnitGeometry extends BtnIcnShapeSelectListAbs {

    private static final String _STR_TIP_WHAT_ = "rotary table";

    public BtnIcnShapeSelectListUnitGeometry(ActionListener alr) {
        super(alr, BtnIcnShapeSelectListUnitGeometry._STR_TIP_WHAT_);
        super.setEnabled(false);
    }
}
