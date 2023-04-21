package org.geoforge.guitlcolg.dialog.edit.data.oxp.button;

import java.awt.event.ActionListener;
import org.geoforge.guitlc.dialog.edit.data.button.BtnIcnShapeSelectListAbs;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class BtnIcnShapeSelectListRotaryTable extends BtnIcnShapeSelectListAbs {

    private static final String _STR_TIP_WHAT_ = "rotary table";

    public BtnIcnShapeSelectListRotaryTable(ActionListener alr) {
        super(alr, BtnIcnShapeSelectListRotaryTable._STR_TIP_WHAT_);
        super.setEnabled(false);
    }
}
