package org.geoforge.guitlc.dialog.tabs.options.panel;

import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import org.geoforge.guillc.checkbox.GfrCbx;
import org.geoforge.guillc.color.ClrJcomponent;
import org.geoforge.guillc.panel.PnlAbs;
import org.geoforge.guitlc.dialog.tabs.handler.IGfrDlgTabTaskHandler;
import org.geoforge.guitlc.dialog.tabs.handler.IHandledChangedValue;

/**
 *
 * @author bantchao
 */
public abstract class PnlChkAbs extends PnlAbs implements IGfrDlgTabTaskHandler, IHandledChangedValue {

    @Override
    public abstract void doJob() throws Exception;

    protected GfrCbx _cbx = null;

    protected boolean _blnStateOri = false;

    protected PnlChkAbs(String strWhat, ActionListener alrParent, boolean blnStateOri) throws Exception {
        super();
        this._blnStateOri = blnStateOri;
        this._cbx = new GfrCbx(strWhat);
        this._cbx.setSelected(blnStateOri);
        if (alrParent != null) this._cbx.addActionListener(alrParent);
        super.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    }

    @Override
    public boolean hasChangedValue() {
        if (this._blnStateOri && this._cbx.isSelected()) return false;
        if (!this._blnStateOri && !this._cbx.isSelected()) return false;
        return true;
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        if (!this._cbx.init()) return false;
        this._cbx.setBackground(ClrJcomponent.COL_PANE_BG_NORMAL);
        super.add(this._cbx);
        return true;
    }

    @Override
    public void destroy() {
        super.destroy();
        this._cbx = null;
        this._blnStateOri = false;
    }

    @Override
    public void loadTransient() throws Exception {
    }

    @Override
    public void releaseTransient() throws Exception {
    }
}
