package tm.cpp.ast;

import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.cpp.analysis.Cpp_Specifiers;
import tm.cpp.datum.LongDatum;
import tm.interfaces.TypeInterface;
import tm.utilities.Assert;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;

public class TyLong extends TypeNode implements TyIntegral, TyCpp {

    private TyLong() {
        super();
    }

    private static TyLong[] qualified = new TyLong[4];

    /**
	 * Returns a <code>TyLong</code> instance with default (none/auto) 
	 * cv-qualification.
	 * @return a <code>TyLong</code> instance
	 * @see Cpp.Analysis.SpecifierSet
	 */
    public static TyLong get() {
        return get(0);
    }

    /**
	 * Returns a <code>TyLong</code> instance with the indicated 
	 * cv-qualification.
	 * @param cv_qual a flag indicating cv-qualification as follows:
	 * 0 : none (auto), 1 : const, 2 : volatile, 3 : const volatile.
	 * @return a <code>TyLong</code> instance
	 * @see Cpp.Analysis.SpecifierSet
	 */
    public static TyLong get(int cv_qual) {
        TyLong instance = null;
        if (cv_qual >= qualified.length) {
            Assert.apology(INVALID_CV_QUALIFICATION);
        } else {
            if (qualified[cv_qual] == null) {
                qualified[cv_qual] = new TyLong();
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
        LongDatum d = new LongDatum(address, parent, vms.getMemory(), name, vms.getTimeManager());
        return d;
    }

    public AbstractDatum makeDatum(VMState vms, MemRegion mr, String name) {
        int address = mr.findSpace(LongDatum.size);
        LongDatum d = new LongDatum(address, null, vms.getMemory(), name, vms.getTimeManager());
        vms.getStore().addDatum(d);
        return d;
    }

    public boolean equal_types(TypeInterface t) {
        return t instanceof TyLong;
    }

    public String getTypeString() {
        return "long int";
    }

    public int getNumBytes() {
        return LongDatum.size;
    }

    public String typeId() {
        return typeId("", false);
    }

    public String typeId(String seed, boolean lastWasLeft) {
        seed = "long" + seed;
        if ((getAttributes() & Cpp_Specifiers.CVQ_CONST) != 0) seed = "const " + seed;
        return seed;
    }
}
