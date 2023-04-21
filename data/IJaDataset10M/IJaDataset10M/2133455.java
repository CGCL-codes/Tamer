package org.geoforge.guillcolg.treenode.exp;

import java.awt.event.ActionListener;
import org.geoforge.guillcolg.popupmenu.exp.PmuCtlCtrLeafLloDskMrkGlbSec;
import org.geoforge.guillc.tree.TreAbs;

/**
 *
 * @author bantchao
 */
public class GfrNodCtrMovLeafLblLloMrkSecGlobe extends GfrNodCtrMovLeafLblLloMrkAbs {

    public GfrNodCtrMovLeafLblLloMrkSecGlobe(ActionListener alrController, String strUniqueIdThis, TreAbs tre, String strIdParent) throws Exception {
        super(alrController, strUniqueIdThis, tre);
        super._pop_ = new PmuCtlCtrLeafLloDskMrkGlbSec(alrController, strUniqueIdThis, tre, strIdParent);
    }
}
