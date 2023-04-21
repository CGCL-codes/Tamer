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

/** Representation of EmptyDefParams */
public class EmptyDefParams extends TypeDefParams implements EmptyList {

    /** Construct a(n) EmptyDefParams Instance */
    public EmptyDefParams() {
    }

    /** Is the given object Equal to this EmptyDefParams? */
    public boolean equals(Object o) {
        if (!(o instanceof EmptyDefParams)) return false;
        if (o == this) return true;
        EmptyDefParams oo = (EmptyDefParams) o;
        return true;
    }

    /** Parse an instance of EmptyDefParams from the given String */
    public static EmptyDefParams parse(String inpt) throws edu.neu.ccs.demeterf.demfgen.classes.ParseException {
        return new edu.neu.ccs.demeterf.demfgen.classes.TheParser(new java.io.StringReader(inpt)).parse_EmptyDefParams();
    }

    /** Parse an instance of EmptyDefParams from the given Stream */
    public static EmptyDefParams parse(java.io.InputStream inpt) throws edu.neu.ccs.demeterf.demfgen.classes.ParseException {
        return new edu.neu.ccs.demeterf.demfgen.classes.TheParser(inpt).parse_EmptyDefParams();
    }

    /** Parse an instance of EmptyDefParams from the given Reader */
    public static EmptyDefParams parse(java.io.Reader inpt) throws edu.neu.ccs.demeterf.demfgen.classes.ParseException {
        return new edu.neu.ccs.demeterf.demfgen.classes.TheParser(inpt).parse_EmptyDefParams();
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
