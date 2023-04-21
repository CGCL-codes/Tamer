package org.geoforge.guillcolg.popupmenu.exp;

import java.awt.event.ActionListener;
import org.geoforge.guillcolg.menuitem.exp.MimTrsDelAllTlosS2d;
import org.geoforge.guillcolg.menuitem.exp.MimTrsImportShapeS2ds;
import org.geoforge.guillcolg.menuitem.exp.MimTrsNewEmptyTloS2d;
import org.geoforge.guillcolg.menuitem.exp.MimTrsSetTopS2ds;
import org.geoforge.guillc.menuitem.MimTrsAbs;
import org.geoforge.guillc.popupmenu.PmuCtlCtrFolderTopMainLclManAbs;
import org.geoforge.guillc.tree.TreAbs;

/**
 *
 * @author bantchao
 */
public class PmuCtlCtrFolderTopMainDskManS2ds extends PmuCtlCtrFolderTopMainLclManAbs {

    private MimTrsAbs _mimNewEmpty_ = null;

    public PmuCtlCtrFolderTopMainDskManS2ds(ActionListener alrController, TreAbs tree, ActionListener alrParentNode) throws Exception {
        super(tree, alrParentNode);
        super._mimImportShape = new MimTrsImportShapeS2ds(alrController);
        super._mimDeleteAll = new MimTrsDelAllTlosS2d(alrController);
        this._mimNewEmpty_ = new MimTrsNewEmptyTloS2d(alrController);
        super._mimSettings = new MimTrsSetTopS2ds(alrController);
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        if (!this._mimNewEmpty_.init()) return false;
        super.add(this._mimNewEmpty_);
        return true;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (this._mimNewEmpty_ != null) {
            this._mimNewEmpty_.destroy();
            this._mimNewEmpty_ = null;
        }
    }

    @Override
    public void loadTransient() throws Exception {
        super.loadTransient();
        this._mimNewEmpty_.loadTransient();
    }

    @Override
    public void releaseTransient() throws Exception {
        super.releaseTransient();
        this._mimNewEmpty_.releaseTransient();
    }
}
