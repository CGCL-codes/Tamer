package module.Pc;

import framework.Packet;
import module.DataUI;
import module.Module;
import module.ModuleUI;

/**
 * @author Nirupam
 * 
 */
public class PCUI extends ModuleUI {

    public PCUI(Module m) {
        super(m);
    }

    protected DataUI getNewDataUI(Packet p) {
        DataUI dui = super.getNewDataUI(p);
        dui.setDisplayString(p.getFromId() + "->" + p.getToId());
        return dui;
    }
}
