package tm.cpp.ast;

import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.cpp.analysis.Cpp_Specifiers;
import tm.cpp.datum.LongDoubleDatum;
import tm.interfaces.TypeInterface;
import tm.utilities.Assert;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;

public class TyLongDouble extends TypeNode implements TyFloating, TyCpp {

    private TyLongDouble() {
        super();
    }

    private static TyLongDouble[] qualified = new TyLongDouble[4];

    /**
	 * Returns a <code>TyLongDouble</code> instance with default (none/auto) 
	 * cv-qualification.
	 * @return a <code>TyLongDouble</code> instance
	 * @see Cpp.Analysis.SpecifierSet
	 */
    public static TyLongDouble get() {
        return get(0);
    }

    /**
	 * Returns a <code>TyLongDouble</code> instance with the indicated 
	 * cv-qualification.
	 * @param cv_qual a flag indicating cv-qualification as follows:
	 * 0 : none (auto), 1 : const, 2 : volatile, 3 : const volatile.
	 * @return a <code>TyLongDouble</code> instance
	 * @see Cpp.Analysis.SpecifierSet
	 */
    public static TyLongDouble get(int cv_qual) {
        TyLongDouble instance = null;
        if (cv_qual >= qualified.length) {
            Assert.apology(INVALID_CV_QUALIFICATION);
        } else {
            if (qualified[cv_qual] == null) {
                qualified[cv_qual] = new TyLongDouble();
                qualified[cv_qual].setAttributes(cv_qual);
            }
            instance = qualified[cv_qual];
        }
        return instance;
    }

    public TypeNode getQualified(int cv_qual) {
        return get(cv_qual);
    }

    public AbstractDatum makeMemberDatum(VMState vms, int address, AbstractDatum parent, String name) {
        LongDoubleDatum d = new LongDoubleDatum(address, parent, vms.getMemory(), name, vms.getTimeManager());
        return d;
    }

    public AbstractDatum makeDatum(VMState vms, MemRegion mr, String name) {
        int address = mr.findSpace(LongDoubleDatum.size);
        LongDoubleDatum d = new LongDoubleDatum(address, null, vms.getMemory(), name, vms.getTimeManager());
        vms.getStore().addDatum(d);
        return d;
    }

    public boolean equal_types(TypeInterface t) {
        return t instanceof TyLongDouble;
    }

    public String getTypeString() {
        return "long double";
    }

    public int getNumBytes() {
        return LongDoubleDatum.size;
    }

    public String typeId() {
        return typeId("", false);
    }

    public String typeId(String seed, boolean lastWasLeft) {
        seed = "long double" + seed;
        if ((getAttributes() & Cpp_Specifiers.CVQ_CONST) != 0) seed = "const " + seed;
        return seed;
    }
}
