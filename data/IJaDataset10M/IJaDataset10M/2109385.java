package gov.nasa.jpf.jvm.untracked;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.jvm.ElementInfo;
import gov.nasa.jpf.jvm.KernelState;
import gov.nasa.jpf.jvm.StaticArea;

/**
 * StaticArea that support the @UntrackedField annotation
 *
 * @author Milos Gligoric (milos.gligoric@gmail.com)
 * @author Tihomir Gvero (tihomir.gvero@gmail.com)
 */
public class UntrackedStaticArea extends StaticArea {

    public UntrackedStaticArea(Config config, KernelState ks) {
        super(config, ks);
    }

    protected void remove(int index, boolean nullOk) {
        ElementInfo ei = elements.get(index);
        if (nullOk && ei == null) return;
        assert (ei != null) : "trying to remove null object at index: " + index;
        if (UntrackedManager.getProperty() && UntrackedManager.getInstance().isUntracked(ei)) return;
        super.remove(index, nullOk);
    }
}
