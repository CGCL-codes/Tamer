package com.gorillalogic.dal.teller;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.common.*;
import com.gorillalogic.dal.model.*;
import com.gorillalogic.config.*;
import com.gorillalogic.dal.utils.StringTransforms;
import java.io.StringWriter;
import java.io.PrintWriter;

class Sentence {

    private String line;

    private final boolean isUpdate;

    private boolean needed;

    private BaseTeller invokedOn = null;

    private BaseTeller defines;

    private BaseTeller dependsOn1 = null;

    private BaseTeller dependsOn2 = null;

    private boolean isNull = false;

    private Exception fault = null;

    private boolean sideEffect = false;

    private String returnValue = null;

    Sentence(String line, boolean isUpdate) {
        this.line = line + ';';
        this.isUpdate = needed = isUpdate;
    }

    Sentence(String line, boolean isUpdate, BaseTeller invokedOn) {
        this(line, isUpdate);
        this.invokedOn = invokedOn;
        if (isUpdate) {
            Sentence on = invokedOn.definedBy();
            if (on != null) {
                on.makeNeeded();
            }
        }
    }

    void addDependent(BaseTeller teller) {
        if (dependsOn1 == null) dependsOn1 = teller; else if (dependsOn2 == null) dependsOn2 = teller; else {
            throw new InternalException("Too many sentence dependents");
        }
        if (needed) teller.definedBy().makeNeeded();
    }

    void makeNeeded() {
        if (!needed) {
            needed = true;
            if (invokedOn != null && invokedOn.definedBy() != null) {
                invokedOn.definedBy().makeNeeded();
            }
            if (dependsOn1 != null) {
                dependsOn1.definedBy().makeNeeded();
                if (dependsOn2 != null) {
                    dependsOn2.definedBy().makeNeeded();
                }
            }
        }
    }

    boolean isNeeded() {
        return needed || (sideEffect && invokedOn.definedBy().isNeeded());
    }

    void fault(StructureException e) throws StructureException {
        makeNeeded();
        fault = e;
        throw e;
    }

    void fault(BoundsException e) throws BoundsException {
        makeNeeded();
        fault = e;
        throw e;
    }

    void fault(OperationException e) throws OperationException {
        makeNeeded();
        fault = e;
        throw e;
    }

    void fault(OperationException.Permission e) throws OperationException.Permission {
        makeNeeded();
        fault = e;
        throw e;
    }

    void fault(AccessException e) throws AccessException {
        makeNeeded();
        fault = e;
        throw e;
    }

    void fault(RuntimeException e) {
        makeNeeded();
        fault = e;
        throw e;
    }

    String line(String s) {
        return "\n    		" + s;
    }

    String comment(String s) {
        return line("// " + s);
    }

    String toPrintln(String s) {
        s = BaseTeller.quote(s);
        return "\n/**/    		System.out.println(" + s + ");";
    }

    String tell() {
        String rez = "";
        if (isNull) {
            rez = "/*null*/ ";
        }
        if (fault != null) {
            rez = tellFault();
        } else {
            rez = line;
            if (returnValue != null) {
                if (rez.length() < 40) rez += "  // returned " + returnValue; else rez += comment("Last call returned: " + returnValue);
            }
        }
        return rez;
    }

    private String tellFault() {
        String rez = line("try {");
        rez += line("    " + line);
        String fn = fault.getClass().getName();
        rez += line("} catch (" + fn + " e) {");
        rez += line("    System.out.println(\"Caught " + fn + "\");");
        rez += line("    System.out.println(\"Exp: \\\"" + fault.getMessage() + "\\\");");
        rez += line("    System.out.println(\"Got: \" + e.getMessage());");
        if (fault instanceof AccessException.Accumulator) {
            AccessException.Accumulator acc = (AccessException.Accumulator) fault;
            if (acc.count() > 0 && acc.getFirst() instanceof ConstraintViolation) {
                String border = "\\\\*****************************************\\\\";
                border = "System.out.println(\"" + border + "\");";
                rez += line("    " + border);
                rez += line("    ((AccessException)e).elaborate(out);");
                rez += line("    " + border);
            }
        }
        rez += line("};");
        return rez;
    }

    String tell1() {
        String rez = "";
        if (isNull) {
            rez = "/*null*/ ";
        }
        rez += line;
        if (fault != null) {
            String msg = fault.getClass().getName() + " was generated here";
            rez += "\n" + toPrintln(msg);
            String fm = fault.getMessage();
            if (fm != null) {
                fm = BaseTeller.quote(fm);
                rez += toPrintln(fm);
            }
            if (fault instanceof AccessException) {
                AccessException e = (AccessException) fault;
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw, true);
                e.elaborate(pw);
                String es = sw.toString();
                if (es != null && es.length() > 0) {
                    rez += "\n/* AccessException.elaborate() output: \n" + es + "\n*/";
                }
                if (fault instanceof AccessException.Accumulator) {
                    AccessException.Accumulator acc = (AccessException.Accumulator) fault;
                    if (acc.count() > 0 && acc.getFirst() instanceof ConstraintViolation) {
                        String border = "\\\\*****************************************\\\\";
                        border = "System.out.println(\"" + border + "\");";
                        rez = line("try {") + line(rez) + line("} catch (AccessException e) {") + line("   System.out.println(\"\\nCaught constraint violation as expected!\");") + line("   " + border) + line("   PrintWriter out = new PrintWriter(System.out,true);") + line("   e.elaborate(out);") + line("   " + border) + line("   System.out.println(\"Continuing after constraint violation...\\n\");") + line("};");
                    }
                }
            }
        } else {
            if (returnValue != null) {
                if (rez.length() < 40) rez += "  // returned " + returnValue; else rez += comment("Last call returned: " + returnValue);
            }
        }
        return rez;
    }

    void call() {
        ;
    }

    void callWithSideEffect() {
        setSideEffect();
    }

    void call(String rez) {
        setSideEffect();
        this.returnValue = rez;
    }

    private void setSideEffect() {
        this.sideEffect = true;
        if (invokedOn == null) {
            throw new InternalException("trc-sentence corrupted");
        }
    }

    void xdef(BaseTeller teller) {
        line = teller.defn(line);
        defines = teller;
        if (isUpdate && invokedOn != null) invokedOn.definedBy().makeNeeded();
    }

    Type defn(Type t) {
        if (t == null) {
            isNull = true;
        } else {
            TypeTeller teller = TypeTeller.wrap(t, this);
            xdef(teller);
            t = teller;
        }
        return t;
    }

    Table defn(Table t) {
        if (t == null) {
            isNull = true;
        } else {
            TableTeller teller = TableTeller.wrap(t, this);
            xdef(teller);
            t = teller;
        }
        return t;
    }

    Table.Row defn(Table.Row t) {
        if (t == null) {
            isNull = true;
        } else {
            RowTeller teller = RowTeller.wrap(t, this);
            xdef(teller);
            t = teller;
        }
        return t;
    }

    Table.Itr defn(Table.Itr t) {
        if (t == null) {
            isNull = true;
        } else {
            RowTeller.ItrTeller teller = RowTeller.ItrTeller.wrap(t, this);
            xdef(teller);
            t = teller;
        }
        return t;
    }

    PkgTable defn(PkgTable t) {
        if (t == null) {
            isNull = true;
        } else {
            PkgTeller teller = PkgTeller.wrap(t, this);
            xdef(teller);
            t = teller;
        }
        return t;
    }

    PkgTable.PkgRow defn(PkgTable.PkgRow t) {
        if (t == null) {
            isNull = true;
        } else {
            PkgTeller.PkgRowTeller teller = PkgTeller.PkgRowTeller.wrap(t, this);
            xdef(teller);
            t = teller;
        }
        return t;
    }

    PkgTable.PkgItr defn(PkgTable.PkgItr t) {
        if (t == null) {
            isNull = true;
        } else {
            PkgTeller.PkgItrTeller teller = PkgTeller.PkgItrTeller.wrap(t, this);
            xdef(teller);
            t = teller;
        }
        return t;
    }

    ExtendedExpr defn(Expr t) {
        ExtendedExpr ex = (ExtendedExpr) t;
        if (ex == null) {
            isNull = true;
        } else {
            ExprTeller teller = ExprTeller.wrap(ex, this);
            xdef(teller);
            ex = teller;
        }
        return ex;
    }

    ParameterizedExpr defn(ParameterizedExpr t) {
        if (t == null) {
            isNull = true;
        } else {
            ParameterizedExprTeller teller = ParameterizedExprTeller.wrap(t, this);
            xdef(teller);
            t = teller;
        }
        return t;
    }

    ColumnHdr defn(ColumnHdr t) {
        if (t == null) {
            isNull = true;
        } else {
            ColumnHdrTeller teller = ColumnHdrTeller.wrap(t, this);
            xdef(teller);
            t = teller;
        }
        return t;
    }

    ExtendedScope defn(Scope t) {
        ExtendedScope es = (ExtendedScope) t;
        if (es == null) {
            isNull = true;
        } else {
            ScopeTeller teller = ScopeTeller.wrap(es, this);
            xdef(teller);
            es = teller;
        }
        return es;
    }

    TableExtender defn(TableExtender t) {
        if (t == null) {
            isNull = true;
        } else {
            TableExtenderTeller teller = TableExtenderTeller.wrap(t, this);
            xdef(teller);
            t = teller;
        }
        return t;
    }

    PkgExtender defn(PkgExtender t) {
        if (t == null) {
            isNull = true;
        } else {
            PkgExtenderTeller teller = PkgExtenderTeller.wrap(t, this);
            xdef(teller);
            t = teller;
        }
        return t;
    }

    Universe defn(Universe t) {
        if (t == null) {
            isNull = true;
        } else {
            UniverseTeller teller = UniverseTeller.wrap(t, this);
            xdef(teller);
            t = teller;
        }
        return t;
    }

    World defn(World t) {
        if (t == null) {
            isNull = true;
        } else {
            WorldTeller teller = WorldTeller.wrap(t, this);
            xdef(teller);
            t = teller;
        }
        return t;
    }

    Universe.WorldItr defn(Universe.WorldItr t) {
        if (t == null) {
            isNull = true;
        } else {
            UniverseTeller.WorldItrTeller teller = UniverseTeller.WorldItrTeller.wrap(t, this);
            xdef(teller);
            t = teller;
        }
        return t;
    }
}
