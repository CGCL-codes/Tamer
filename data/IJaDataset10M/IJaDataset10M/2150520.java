package org.geoforge.guitlc.dialog.tabs.settings.panel;

import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import org.geoforge.guillc.radiobutton.RbnIcnFxSmpAbs;
import org.geoforge.lang.IShrObj;

/**
 *
 * @author bantchao
 */
public abstract class PnlChoiceRadiosAbs extends PnlChoiceAbs {

    protected IShrObj _cmpChoice1 = null;

    protected RbnIcnFxSmpAbs _rbnChoice2 = null;

    protected ButtonGroup _bgp = null;

    protected PnlChoiceRadiosAbs() {
        super();
        super.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this._bgp = new ButtonGroup();
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        if (!_cmpChoice1.init()) return false;
        if (!_rbnChoice2.init()) return false;
        super.add((Component) this._cmpChoice1);
        super.add(this._rbnChoice2);
        this._bgp.add(_rbnChoice2);
        return true;
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
