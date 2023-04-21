package org.geoforge.guillcolg.menuitem.exp;

import java.awt.event.ActionListener;
import java.util.Observer;
import org.geoforge.guillc.menuitem.MimTrsDelAllTlosAbs;
import org.geoforge.mdldatolg.oxp.GfrMdlDtaSetTlosDskWll;
import org.geoforge.wrpbasprssynolg.oxp.GfrWrpBasSynTopWlls;

/**
 *
 * @author bantchao
 * 
 * TODO: listen to model
 */
public class MimTrsDelAllTlosWll extends MimTrsDelAllTlosAbs {

    public MimTrsDelAllTlosWll(ActionListener alrController) throws Exception {
        super(alrController, "well objects");
        GfrMdlDtaSetTlosDskWll.getInstance().addObserver((Observer) this);
        _load();
    }

    @Override
    public void destroy() {
        GfrMdlDtaSetTlosDskWll.getInstance().deleteObserver((Observer) this);
        super.destroy();
    }

    @Override
    protected void _load() throws Exception {
        String[] strs = GfrWrpBasSynTopWlls.getInstance().getIds();
        if (strs != null && strs.length > 0) super.setEnabled(true);
    }

    @Override
    protected void _updateRemovedObject() throws Exception {
        String[] strs = GfrWrpBasSynTopWlls.getInstance().getIds();
        if (strs == null || strs.length < 1) super.setEnabled(false);
    }
}
