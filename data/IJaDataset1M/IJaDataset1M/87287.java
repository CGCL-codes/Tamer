package org.geoforge.guitlc.frame.main.spcprtshared.toolbar;

import java.awt.event.MouseListener;
import org.geoforge.guillc.button.BtnTransActAbs;
import org.geoforge.guillc.toolbar.TbrMainAbs;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class TbrSpcPrtAbs extends TbrMainAbs {

    protected BtnTransActAbs _btnNew = null;

    protected BtnTransActAbs _btnOpen = null;

    protected TbrSpcPrtAbs(MouseListener mlrEffectsBorder, String strWhat) throws Exception {
        super(mlrEffectsBorder, strWhat);
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        if (!this._btnNew.init()) return false;
        if (!this._btnOpen.init()) return false;
        int intId = 0;
        super.add(this._btnNew, intId++);
        super.add(this._btnOpen, intId++);
        super.add(new Separator(), intId++);
        return true;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (this._btnNew != null) {
            this._btnNew.destroy();
            this._btnNew = null;
        }
        if (this._btnOpen != null) {
            this._btnOpen.destroy();
            this._btnOpen = null;
        }
    }
}
