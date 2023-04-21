package tm.javaLang.ast;

import tm.clc.datum.AbstractDatum;
import tm.interfaces.TypeInterface;
import tm.javaLang.datum.DoubleDatum;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;

/*******************************************************************************
Class: TyDouble

Overview:
This class represents the Java double-precision (double) type.

Review:          2002 06 12     Jonathan Anderson and Michael Burton
*******************************************************************************/
public class TyDouble extends TyFloating {

    private static TyDouble instance;

    public static TyDouble get() {
        if (instance == null) {
            instance = new TyDouble();
        }
        return instance;
    }

    public AbstractDatum makeMemberDatum(VMState vms, int address, AbstractDatum parent, String name) {
        DoubleDatum d = new DoubleDatum(address, parent, vms.getMemory(), name, vms.getTimeManager());
        return d;
    }

    public AbstractDatum makeDatum(VMState vms, MemRegion mr, String name) {
        int address = mr.findSpace(DoubleDatum.size);
        DoubleDatum d = new DoubleDatum(address, null, vms.getMemory(), name, vms.getTimeManager());
        vms.getStore().addDatum(d);
        return d;
    }

    public boolean equal_types(TypeInterface t) {
        return t instanceof TyDouble;
    }

    public int getNumBytes() {
        return DoubleDatum.size;
    }

    public String getTypeString() {
        return "double";
    }

    public String typeId() {
        return "double";
    }

    public String elementId() {
        return "D";
    }

    public boolean isReachableByWideningFrom(TyJava fromType) {
        return fromType instanceof TyIntegral || fromType instanceof TyFloat;
    }
}
