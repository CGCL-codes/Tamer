package edu.neu.ccs.demeterf.batch.classes;

import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.demfgen.classes.*;
import edu.neu.ccs.demeterf.demfgen.classes.Package;
import edu.neu.ccs.demeterf.*;
import edu.neu.ccs.demeterf.demfgen.StrLTrip.StrPair;
import edu.neu.ccs.demeterf.demfgen.classes.*;

/** Representation of TravControl */
public class TravControl {

    protected final TypeUse start;

    protected final List<TypeUse> builtins;

    protected final List<ConcreteEdge> bypass;

    /** Construct a(n) TravControl Instance */
    public TravControl(TypeUse start, List<TypeUse> builtins, List<ConcreteEdge> bypass) {
        this.start = start;
        this.builtins = builtins;
        this.bypass = bypass;
    }

    /** Is the given object Equal to this TravControl? */
    public boolean equals(Object o) {
        if (!(o instanceof TravControl)) return false;
        if (o == this) return true;
        TravControl oo = (TravControl) o;
        return (((Object) start).equals(oo.start)) && (((Object) builtins).equals(oo.builtins)) && (((Object) bypass).equals(oo.bypass));
    }

    /** Parse an instance of TravControl from the given String */
    public static TravControl parse(String inpt) throws ParseException {
        return new TheParser(new java.io.StringReader(inpt)).parse_TravControl();
    }

    /** Parse an instance of TravControl from the given Stream */
    public static TravControl parse(java.io.InputStream inpt) throws ParseException {
        return new TheParser(inpt).parse_TravControl();
    }

    /** Parse an instance of TravControl from the given Reader */
    public static TravControl parse(java.io.Reader inpt) throws ParseException {
        return new TheParser(inpt).parse_TravControl();
    }

    /** Field Class for TravControl.start */
    public static class start extends edu.neu.ccs.demeterf.Fields.any {
    }

    /** Field Class for TravControl.builtins */
    public static class builtins extends edu.neu.ccs.demeterf.Fields.any {
    }

    /** Field Class for TravControl.bypass */
    public static class bypass extends edu.neu.ccs.demeterf.Fields.any {
    }

    /** DGP method from Class PrintToString */
    public String toString() {
        return edu.neu.ccs.demeterf.batch.classes.PrintToString.PrintToStringM(this);
    }

    /** Getter for field TravControl.bypass */
    public List<ConcreteEdge> getBypass() {
        return bypass;
    }

    /** Getter for field TravControl.builtins */
    public List<TypeUse> getBuiltins() {
        return builtins;
    }

    /** Getter for field TravControl.start */
    public TypeUse getStart() {
        return start;
    }
}
