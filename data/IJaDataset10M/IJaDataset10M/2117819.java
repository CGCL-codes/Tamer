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

/** Representation of BehDefCons */
public class BehDefCons extends BehDefList {

    protected final BehDef first;

    protected final BehDefList rest;

    /** Construct a(n) BehDefCons Instance */
    public BehDefCons(BehDef first, BehDefList rest) {
        this.first = first;
        this.rest = rest;
    }

    /** Is the given object Equal to this BehDefCons? */
    public boolean equals(Object o) {
        if (!(o instanceof BehDefCons)) return false;
        if (o == this) return true;
        BehDefCons oo = (BehDefCons) o;
        return (((Object) first).equals(oo.first)) && (((Object) rest).equals(oo.rest));
    }

    /** Parse an instance of BehDefCons from the given String */
    public static BehDefCons parse(String inpt) throws edu.neu.ccs.demeterf.demfgen.classes.ParseException {
        return new edu.neu.ccs.demeterf.demfgen.classes.TheParser(new java.io.StringReader(inpt)).parse_BehDefCons();
    }

    /** Parse an instance of BehDefCons from the given Stream */
    public static BehDefCons parse(java.io.InputStream inpt) throws edu.neu.ccs.demeterf.demfgen.classes.ParseException {
        return new edu.neu.ccs.demeterf.demfgen.classes.TheParser(inpt).parse_BehDefCons();
    }

    /** Parse an instance of BehDefCons from the given Reader */
    public static BehDefCons parse(java.io.Reader inpt) throws edu.neu.ccs.demeterf.demfgen.classes.ParseException {
        return new edu.neu.ccs.demeterf.demfgen.classes.TheParser(inpt).parse_BehDefCons();
    }

    /** Field Class for BehDefCons.first */
    public static class first extends edu.neu.ccs.demeterf.Fields.any {
    }

    /** Field Class for BehDefCons.rest */
    public static class rest extends edu.neu.ccs.demeterf.Fields.any {
    }

    public BehDef findDef(String n) {
        return (n.equals("" + first.name) ? first : rest.findDef(n));
    }

    public boolean hasDef(String n) {
        return (n.equals("" + first.name) || rest.hasDef(n));
    }

    public List<BehDef> toList() {
        return rest.toList().push(first);
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

    /** Getter for field BehDefCons.rest */
    public BehDefList getRest() {
        return rest;
    }

    /** Getter for field BehDefCons.first */
    public BehDef getFirst() {
        return first;
    }

    /** Updater for field BehDefCons.first */
    public BehDefCons updateFirst(BehDef _first) {
        return new BehDefCons(_first, rest);
    }

    /** Updater for field BehDefCons.rest */
    public BehDefCons updateRest(BehDefList _rest) {
        return new BehDefCons(first, _rest);
    }
}
