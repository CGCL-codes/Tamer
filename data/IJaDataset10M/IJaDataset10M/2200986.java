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

/** Representation of NoLook */
public class NoLook extends LookDef {

    /** Construct a(n) NoLook Instance */
    public NoLook() {
    }

    /** Is the given object Equal to this NoLook? */
    public boolean equals(Object o) {
        if (!(o instanceof NoLook)) return false;
        if (o == this) return true;
        NoLook oo = (NoLook) o;
        return true;
    }

    /** Parse an instance of NoLook from the given String */
    public static NoLook parse(String inpt) throws edu.neu.ccs.demeterf.demfgen.classes.ParseException {
        return new edu.neu.ccs.demeterf.demfgen.classes.TheParser(new java.io.StringReader(inpt)).parse_NoLook();
    }

    /** Parse an instance of NoLook from the given Stream */
    public static NoLook parse(java.io.InputStream inpt) throws edu.neu.ccs.demeterf.demfgen.classes.ParseException {
        return new edu.neu.ccs.demeterf.demfgen.classes.TheParser(inpt).parse_NoLook();
    }

    /** Parse an instance of NoLook from the given Reader */
    public static NoLook parse(java.io.Reader inpt) throws edu.neu.ccs.demeterf.demfgen.classes.ParseException {
        return new edu.neu.ccs.demeterf.demfgen.classes.TheParser(inpt).parse_NoLook();
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
