package drcl.ruv;

import tcl.lang.InternalRep;
import tcl.lang.Interp;
import tcl.lang.ReflectObject;
import tcl.lang.TclObject;

public class TclGateway {

    static ShellTcl tcl = null;

    static Interp interp = null;

    public static Object tcl(String script_) throws Exception {
        if (tcl == null) {
            tcl = new ShellTcl(".tcl_gateway");
            if (drcl.ruv.System.system == null) drcl.ruv.System.main(new String[] { "-u" });
            drcl.ruv.System.system.addTerminal(null, tcl, null, null);
            interp = tcl.getInterp();
        }
        TclObject raw_ = (TclObject) tcl.eval(script_);
        if (raw_ == null) return null;
        InternalRep rep_ = raw_.getInternalRep();
        if (rep_ instanceof ReflectObject) return ReflectObject.get(interp, raw_); else return raw_.toString();
    }
}
