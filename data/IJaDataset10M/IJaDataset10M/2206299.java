package EDU.Washington.grad.noth.cda.component;

import java.util.Vector;
import java.awt.Point;
import EDU.Washington.grad.gjb.cassowary.*;

public class EditConstantList {

    public Vector<ClDouble> ec;

    private Vector<SelPoint> selPointAssocList;

    public EditConstantList() {
        this(1);
    }

    public EditConstantList(int n) {
        if (n < 1) n = 1;
        ec = new Vector<ClDouble>(n);
        for (int a = 0; a < n; a++) {
            ec.addElement(new ClDouble(0.0));
        }
        selPointAssocList = new Vector<SelPoint>(n);
    }

    public void registerDelta(ClVariable v, double delta) {
        System.out.println("Sorry, not yet implemented.");
    }

    public void registerDelta(SelPoint sp, Point delta) {
        int spIdx = -1, a;
        for (a = 0; a < selPointAssocList.size(); a++) {
            if (sp == selPointAssocList.elementAt(a)) {
                spIdx = a;
                break;
            }
        }
        if (spIdx == -1) {
            selPointAssocList.addElement(sp);
            spIdx = selPointAssocList.indexOf(sp);
        }
        ClDouble d;
        d = ec.elementAt(2 * spIdx);
        d.setValue(sp.clX.value() + delta.x);
        d = ec.elementAt(2 * spIdx + 1);
        d.setValue(sp.clY.value() + delta.y);
    }

    public void setSize(int n) {
        int oldSize = ec.size();
        int numNewElems = n - oldSize;
        int a;
        ec.setSize(n);
        if (numNewElems > 0) for (a = oldSize; a < n; a++) ec.setElementAt(new ClDouble(0.0), a);
        reset();
        selPointAssocList.setSize(0);
    }

    public final synchronized String toString() {
        String retstr = new String("ECL: size = " + ec.size());
        retstr = retstr.concat(", Elems = [");
        for (int a = 0; a < ec.size() - 1; a++) {
            retstr = retstr.concat(String.valueOf(ec.elementAt(a).doubleValue()));
            retstr = retstr.concat(", ");
        }
        if (ec.size() > 0) retstr = retstr.concat(String.valueOf(ec.elementAt(ec.size() - 1).doubleValue()));
        retstr = retstr.concat("]");
        return retstr;
    }

    public void reset() {
    }
}
