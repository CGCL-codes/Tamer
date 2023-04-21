package edu.neu.ccs.demeterf.demfgen.dgp;

import edu.neu.ccs.demeterf.*;
import edu.neu.ccs.demeterf.demfgen.*;
import edu.neu.ccs.demeterf.demfgen.classes.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.demfgen.TypeCollect.UseCollect;
import edu.neu.ccs.demeterf.demfgen.TypeCollect.DefCollect;

/** The goal of TravGeneric is to traverse instantiations of generic classes
 *    used in a CD.  We start with a traversal that gets us to TypeDefs by
 *    making it a 'builtin', from there we jump to a new traversal (in recur)
 *    that does what we want. */
public abstract class TravGeneric extends DGPFunc {

    DGPFunc.Trav trav, realTrav;

    UseCollect uses = new UseCollect();

    public TravGeneric(String beh) {
        super(beh);
        trav = DGPFunc.wrapTrav(Factory.newTraversal(this, control()));
        realTrav = realTraversalObj();
    }

    /** Important that we call the "realTrav" Here... */
    public String recur(TypeDef td) {
        return realTrav.traverseTypeDef(td);
    }

    public final Control control() {
        return Control.builtins(TypeDef.class);
    }

    public Control realControl() {
        return Control.builtins(DoGen.class, TypeDefParams.class, Impl.class);
    }

    /** The initial traversal object that will be used to traverse the list
     *    of typedefs once we get started.  We create a new function object
     *    because the BEH that was used to create *this* FO was a dummy
     *    empty string to make Java reflection happy. */
    public Trav traversalObj(final String beh) {
        return this.functionObj(beh).trav;
    }

    /** This method will be specialized by DGPFs to give faster traversals
     *    that use inlining.  The control and dispatch will be built in, so
     *    we have to keep this one separate as an instance method. */
    public Trav realTraversalObj() {
        return DGPFunc.wrapTrav(Factory.newTraversal(this, realControl()));
    }

    public String combine(TypeDef td) {
        return (td.typeParams().isEmpty()) ? recur(td) : "";
    }

    public String combine(ClassDef td, DoGen g) {
        return "";
    }

    public String combine(IntfcDef td, DoGen g) {
        return "";
    }

    /** Collect al the uses of parametrized types... */
    public TypeUse combine(TypeUse tu) {
        if (!tu.getTparams().isEmpty() && !uses.has(UseCollect.comp(tu))) {
            uses.add(tu);
        }
        return tu;
    }

    /**  */
    public NETypeUseList combine(NETypeUseList tl) {
        return tl;
    }

    public TypeUseList combine(TypeUseList tl) {
        return tl;
    }

    public TypeUseParams combine(TypeUseParams tp) {
        return tp;
    }

    public String combine(None<List<TypeDef>> td) {
        return "";
    }

    /**  */
    public abstract TravGeneric functionObj(String beh);

    /**  */
    public abstract String primitive(String p);

    /**  */
    public String builtin(String b) {
        return "";
    }

    /**  */
    public String superClass() {
        return "edu.neu.ccs.demeterf.FC";
    }

    /**  */
    public String typeargs() {
        return "";
    }

    /**  */
    public boolean isAbstract() {
        return false;
    }

    /**  */
    public String fileOpening() {
        return ("\n\n" + "/** " + docComment() + " */\n" + "public " + (isAbstract() ? "abstract " : "") + "class " + fileName() + typeargs() + " " + Diff.d.inherit + " " + superClass() + "{\n" + stubMethod());
    }

    /**  */
    public String fileClosing() {
        return "\n" + behavior + "\n}\n";
    }

    /** Compare ganeric/parametrized types for equality */
    public List.GComp<TypeUse, TypeDef> genericComp() {
        return genericByFullType();
    }

    /** Compare by type name and parameters */
    public static List.GComp<TypeUse, TypeDef> genericByFullType() {
        return DefCollect.comp();
    }

    /** Only by the first part of the type name */
    public static List.GComp<TypeUse, TypeDef> genericByNameOnly() {
        if (Diff.isCS()) return genericByFullType();
        return new DefCollect.Comp() {

            public String fullname(TypeUse tu) {
                return tu.getName().toString();
            }

            public String fullname(TypeDef td) {
                return td.name();
            }
        };
    }

    /** By the full type when the type has fields */
    public static List.GComp<TypeUse, TypeDef> genericByNameForNoFields() {
        if (Diff.isCS()) return genericByFullType();
        return new DefCollect.Comp() {

            public boolean comp(TypeUse tu, TypeDef td) {
                if (td.fieldList().length() > 0 || td.isAbstr()) return super.comp(tu, td);
                return (tu.getName().toString().equals(td.name()));
            }
        };
    }

    public String finish(List<TypeDef> allTypes, String pre, String body) {
        String prims = "";
        for (String p : Diff.d.primitives) prims += primitive(p);
        for (String b : builtins()) prims += builtin(b);
        String generic = "";
        DefCollect done = new DefCollect();
        List.GComp<TypeUse, TypeDef> comp = genericComp();
        while (!uses.isEmpty()) {
            final TypeUse use = uses.top();
            TypeDef instdef = DemFGenMain.instantiate(use, allTypes);
            uses.pop();
            if (!done.has(comp.revCurry(use))) {
                done.add(instdef);
                generic += recur(instdef);
            }
        }
        return (pre + fileOpening() + prims + "\n" + body + "\n" + generic + fileClosing());
    }
}
