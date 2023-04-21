package edu.neu.ccs.demeterf.demfgen.classes;

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

/** Representation of EmptyUseParams */
public class EmptyUseParams extends TypeUseParams implements EmptyList {

    /** Construct a(n) EmptyUseParams Instance */
    public EmptyUseParams() {
    }

    /** Is the given object Equal to this EmptyUseParams? */
    public boolean equals(Object o) {
        if (!(o instanceof EmptyUseParams)) return false;
        if (o == this) return true;
        EmptyUseParams oo = (EmptyUseParams) o;
        return true;
    }

    /** Parse an instance of EmptyUseParams from the given String */
    public static EmptyUseParams parse(String inpt) throws edu.neu.ccs.demeterf.demfgen.classes.ParseException {
        return new edu.neu.ccs.demeterf.demfgen.classes.TheParser(new java.io.StringReader(inpt)).parse_EmptyUseParams();
    }

    /** Parse an instance of EmptyUseParams from the given Stream */
    public static EmptyUseParams parse(java.io.InputStream inpt) throws edu.neu.ccs.demeterf.demfgen.classes.ParseException {
        return new edu.neu.ccs.demeterf.demfgen.classes.TheParser(inpt).parse_EmptyUseParams();
    }

    /** Parse an instance of EmptyUseParams from the given Reader */
    public static EmptyUseParams parse(java.io.Reader inpt) throws edu.neu.ccs.demeterf.demfgen.classes.ParseException {
        return new edu.neu.ccs.demeterf.demfgen.classes.TheParser(inpt).parse_EmptyUseParams();
    }

    /** DGP method from Class Print */
    public String print() {
        return edu.neu.ccs.demeterf.demfgen.classes.Print.PrintM(this);
    }

    /** DGP method from Class ToStr */
    public String toStr() {
        return edu.neu.ccs.demeterf.demfgen.classes.ToStr.ToStrM(this);
    }

    /** DGP method from Class ToXML */
    public String toXML() {
        return edu.neu.ccs.demeterf.demfgen.classes.ToXML.ToXMLM(this);
    }
}
