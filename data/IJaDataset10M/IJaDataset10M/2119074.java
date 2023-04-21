package edu.neu.ccs.demeterf.lexer.classes;

import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.*;
import edu.neu.ccs.demeterf.demfgen.classes.*;
import edu.neu.ccs.demeterf.demfgen.classes.Package;

/** Representation of GrpRE */
public class GrpRE extends RegExp {

    protected final List<GrpPart> gs;

    /** Construct a(n) GrpRE Instance */
    public GrpRE(List<GrpPart> gs) {
        this.gs = gs;
    }

    /** Is the given object Equal to this GrpRE? */
    public boolean equals(Object o) {
        if (!(o instanceof GrpRE)) return false;
        if (o == this) return true;
        GrpRE oo = (GrpRE) o;
        return (((Object) gs).equals(oo.gs));
    }

    /** Parse an instance of GrpRE from the given String */
    public static GrpRE parse(String inpt) throws ParseException {
        return new TheParser(new java.io.StringReader(inpt)).parse_GrpRE();
    }

    /** Parse an instance of GrpRE from the given Stream */
    public static GrpRE parse(java.io.InputStream inpt) throws ParseException {
        return new TheParser(inpt).parse_GrpRE();
    }

    /** Parse an instance of GrpRE from the given Reader */
    public static GrpRE parse(java.io.Reader inpt) throws ParseException {
        return new TheParser(inpt).parse_GrpRE();
    }

    /** Field Class for GrpRE.gs */
    public static class gs extends edu.neu.ccs.demeterf.Fields.any {
    }

    /** DGP method from Class PrintToString */
    public String toString() {
        return edu.neu.ccs.demeterf.lexer.classes.PrintToString.PrintToStringM(this);
    }

    /** Getter for field GrpRE.gs */
    public List<GrpPart> getGs() {
        return gs;
    }
}
