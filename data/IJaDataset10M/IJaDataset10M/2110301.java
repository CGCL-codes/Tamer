package org.geoforge.guillcogc.menuitem;

import java.awt.event.ActionListener;
import java.util.Observable;
import org.geoforge.mdldsp.state.singleton.globe.man.GfrMdlSttSngGlbMan;
import org.geoforge.wrpbasprssynogc.GfrWrpBasTloSynOgcWms;

/**
 *
 * @author bantchao
 */
public class MimTransientDisplayWmsNoTersMan extends MimTransientDisplayWmsNoTersAbs {

    public MimTransientDisplayWmsNoTersMan(ActionListener alrParentNode, ActionListener alrParentCad, String strId) throws Exception {
        super(alrParentNode, alrParentCad, strId);
        String strs[] = GfrWrpBasTloSynOgcWms.getInstance().getIdsTerrain(strId);
        if (strs == null || strs.length < 1) super.setEnabled(false); else super.setEnabled(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void _doJob() throws Exception {
        String[] strs = GfrWrpBasTloSynOgcWms.getInstance().getIdsTerrain(super.getId());
        for (int i = 0; i < strs.length; i++) GfrMdlSttSngGlbMan.getInstance().doJob(strs[i], false);
    }
}
