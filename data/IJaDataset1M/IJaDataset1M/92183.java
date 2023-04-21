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

/** Representation of NameCons */
public class NameCons extends NameList implements ConsList {

    protected final NameDef first;

    protected final NameList rest;

    /** Construct a(n) NameCons Instance */
    public NameCons(NameDef first, NameList rest) {
        this.first = first;
        this.rest = rest;
    }

    /** Is the given object Equal to this NameCons? */
    public boolean equals(Object o) {
        if (!(o instanceof NameCons)) return false;
        if (o == this) return true;
        NameCons oo = (NameCons) o;
        return (((Object) first).equals(oo.first)) && (((Object) rest).equals(oo.rest));
    }

    /** Parse an instance of NameCons from the given String */
    public static NameCons parse(String inpt) throws edu.neu.ccs.demeterf.demfgen.classes.ParseException {
        return new edu.neu.ccs.demeterf.demfgen.classes.TheParser(new java.io.StringReader(inpt)).parse_NameCons();
    }

    /** Parse an instance of NameCons from the given Stream */
    public static NameCons parse(java.io.InputStream inpt) throws edu.neu.ccs.demeterf.demfgen.classes.ParseException {
        return new edu.neu.ccs.demeterf.demfgen.classes.TheParser(inpt).parse_NameCons();
    }

    /** Parse an instance of NameCons from the given Reader */
    public static NameCons parse(java.io.Reader inpt) throws edu.neu.ccs.demeterf.demfgen.classes.ParseException {
        return new edu.neu.ccs.demeterf.demfgen.classes.TheParser(inpt).parse_NameCons();
    }

    /** Field Class for NameCons.first */
    public static class first extends edu.neu.ccs.demeterf.Fields.any {
    }

    /** Field Class for NameCons.rest */
    public static class rest extends edu.neu.ccs.demeterf.Fields.any {
    }

    public int length() {
        return rest.length() + 1;
    }

    public String toString() {
        return "," + first + rest;
    }

    public List<String> toList() {
        return rest.toList().push("" + first);
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

    /** Getter for field NameCons.rest */
    public NameList getRest() {
        return rest;
    }

    /** Getter for field NameCons.first */
    public NameDef getFirst() {
        return first;
    }

    /** Updater for field NameCons.first */
    public NameCons updateFirst(NameDef _first) {
        return new NameCons(_first, rest);
    }

    /** Updater for field NameCons.rest */
    public NameCons updateRest(NameList _rest) {
        return new NameCons(first, _rest);
    }
}
