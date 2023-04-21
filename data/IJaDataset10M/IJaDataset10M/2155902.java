package edu.neu.ccs.demeterf.demfgen.traversals;

import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.demfgen.*;
import edu.neu.ccs.demeterf.demfgen.dgp.DGPFunc;
import edu.neu.ccs.demeterf.demfgen.traversals.Travs;
import edu.neu.ccs.demeterf.demfgen.pcdgp.PCDGPFunc;
import edu.neu.ccs.demeterf.dispatch.*;
import edu.neu.ccs.demeterf.*;
import edu.neu.ccs.demeterf.demfgen.ClassHier.InhrtPair;
import edu.neu.ccs.demeterf.demfgen.DemFGenMain;
import java.io.*;
import java.io.FileInputStream;
import edu.neu.ccs.demeterf.util.Util;
import edu.neu.ccs.demeterf.util.CLI;
import edu.neu.ccs.demeterf.demfgen.classes.Package;
import edu.neu.ccs.demeterf.demfgen.DemFGenMain;
import edu.neu.ccs.demeterf.demfgen.classes.*;

public class ParseGenTrav {

    private ParseGen.CombStr func;

    public ParseGenTrav(ParseGen.CombStr f) {
        func = f;
    }

    public String traverse(final List<TypeDef> _h, final DoGen _targ_) {
        return traverseList_TypeDef_(_h, _targ_);
    }

    public String traverseList_TypeDef_(final List<TypeDef> _h, final DoGen _targ_) {
        if (_h instanceof Cons) return traverseCons_TypeDef_((Cons<TypeDef>) _h, _targ_);
        if (_h instanceof Empty) return traverseEmpty_TypeDef_((Empty<TypeDef>) _h, _targ_); else throw new RuntimeException("Unknown List Variant");
    }

    public String traverseEmpty_TypeDef_(final Empty<TypeDef> _h, final DoGen _targ_) {
        return func.combine((List<?>) _h);
    }

    public String traverseCons_TypeDef_(final Cons<TypeDef> _h, final DoGen _targ_) {
        String _first = func.combine(_h.getFirst());
        String _rest = traverseList_TypeDef_(_h.getRest(), _targ_);
        if ((_h instanceof Cons)) {
            if ((_first instanceof String)) {
                if ((_rest instanceof String)) {
                    return func.combine((Cons<?>) _h, (String) _first, (String) _rest);
                } else {
                    return func.combine((List<?>) _h);
                }
            } else {
                return func.combine((List<?>) _h);
            }
        } else {
            return func.combine((List<?>) _h);
        }
    }
}
