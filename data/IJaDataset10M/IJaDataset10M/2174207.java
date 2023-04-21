package org.geoforge.guillcolg.popupmenu.exp;

import java.awt.event.ActionListener;
import org.geoforge.guillcolg.menuitem.exp.MimTransientCheckAllNoTloS3d;
import org.geoforge.guillcolg.menuitem.exp.MimTransientCheckAllYesTloS3d;
import org.geoforge.guillcolg.menuitem.exp.MimTrsDelAllTlosS3d;
import org.geoforge.guillcolg.menuitem.exp.MimTrsNewFilledEditTloBndS3d;
import org.geoforge.guillcolg.menuitem.exp.MimTrsSetTopS3ds;
import org.geoforge.guillc.menuitem.MimTrsAbs;
import org.geoforge.guillc.popupmenu.PmuCtlCtrFolderTopSecAbs;
import org.geoforge.guillc.tree.TreAbs;

/**
 *
 * @author bantchao
 */
public class PmuCtlCtrFolderTopSecDskS3ds extends PmuCtlCtrFolderTopSecAbs {

    private transient MimTrsAbs _mimNewEdit_ = null;

    public PmuCtlCtrFolderTopSecDskS3ds(ActionListener alrController, TreAbs tree, ActionListener alrParentNode, ActionListener alrParentPanelMvc) throws Exception {
        super(tree);
        ActionListener[] alrs = { (ActionListener) this, alrParentPanelMvc };
        super._mimAllCheckTlo = new MimTransientCheckAllYesTloS3d(alrs);
        super._mimAllUncheckTlo = new MimTransientCheckAllNoTloS3d(alrs);
        super._mimSettings = new MimTrsSetTopS3ds(alrController);
        super._mimDeleteAll = new MimTrsDelAllTlosS3d(alrController);
        this._mimNewEdit_ = new MimTrsNewFilledEditTloBndS3d(alrController);
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        if (!this._mimNewEdit_.init()) return false;
        super.add(this._mimNewEdit_);
        return true;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (this._mimNewEdit_ != null) {
            this._mimNewEdit_.destroy();
            this._mimNewEdit_ = null;
        }
    }

    @Override
    public void loadTransient() throws Exception {
        super.loadTransient();
        this._mimNewEdit_.loadTransient();
    }

    @Override
    public void releaseTransient() throws Exception {
        super.releaseTransient();
        this._mimNewEdit_.releaseTransient();
    }
}
