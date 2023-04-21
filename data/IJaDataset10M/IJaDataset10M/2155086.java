package nts.math;

import nts.noad.Noad;
import nts.noad.Field;
import nts.noad.SuperScriptNoad;

public class SuperScriptPrim extends ScriptPrim {

    public SuperScriptPrim(String name) {
        super(name);
    }

    protected boolean conflicting(Noad noad) {
        return noad.alreadySuperScripted();
    }

    protected Noad scripted(Noad noad, Field script) {
        return new SuperScriptNoad(noad, script);
    }

    protected String errorIdent() {
        return "DoubleSuperscript";
    }
}
