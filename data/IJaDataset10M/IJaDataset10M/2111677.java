package gov.nasa.jpf.symbolic.dp;

import gov.nasa.jpf.jvm.JVM;

/**
 * @author Saswat Anand (saswat@gatech.edu)
 *  
 */
public class NativeInterface extends DPInterface {

    public static native boolean isSatisfiable(String query);

    public static native boolean isSubsumed(String shape, String pc);

    public static native boolean isSubsumedInc(String shape, String valuations, int pcid);

    public static native int assertFormula(String query, int id);

    public static native String getStringRep(int id);

    public static native void initializeCVCL();

    public static native void initializeOmega();

    public static native void initializeSTP();

    public static native void initializeYices();

    private static String dp = null;

    private static String mode = null;

    public NativeInterface(String dpName, String mode) {
        initializeDP(dpName, mode);
    }

    public boolean needPrefix() {
        return true;
    }

    public String and() {
        return ",";
    }

    private boolean isDFS() {
        return JVM.getVM().getConfig().getString("search.class").equals("gov.nasa.jpf.search.DFSearch");
    }

    private void initializeDP(String dpName, String m) {
        if (m.length() > 0) {
            if (m.equals("inc")) {
                this.mode = "-inc";
            } else if (m.equals("incsolve")) this.mode = "-incsolve"; else assert false : "Unknown mode " + m + ". It must be one of the {inc, incsolve}";
        } else this.mode = "";
        if (dpName.equals("cvcl")) {
            dp = dpName;
            System.loadLibrary("cvcl-interface" + mode);
            initializeCVCL();
            System.out.println("Native cvcl lib loaded. [" + mode + "]");
        } else if (dpName.equals("omega")) {
            dp = dpName;
            System.loadLibrary("omega-interface" + mode);
            initializeOmega();
            System.out.println("Native omega lib loaded. [" + mode + "]");
        } else if (dpName.equals("stp")) {
            dp = dpName;
            System.loadLibrary("stp-interface" + mode);
            initializeSTP();
            System.out.println("Native STP lib loaded. [" + mode + "]");
        } else if (dpName.equals("yices")) {
            dp = dpName;
            System.loadLibrary("yices-interface" + mode);
            initializeYices();
            System.out.println("Native Yices lib loaded. [" + mode + "]");
        } else throw new RuntimeException(dpName + " not supported!");
    }

    public boolean checkSat(String query) {
        return isSatisfiable(query);
    }

    public int checkSat(String conjunct, int pcid) {
        return assertFormula(conjunct, pcid);
    }

    public boolean checkSubsumption(String shape, String pc) {
        if (dp.equals("omega")) return isSubsumed(shape, pc); else throw new RuntimeException("Works only for Native Omega Interface. ie. +symbolic.dp=omega.native");
    }

    public boolean checkSubsumption(String shape, String valuation, int pcid) {
        if (dp.equals("omega")) return isSubsumedInc(shape, valuation, pcid); else throw new RuntimeException("Works only for Native Omega Interface. ie. +symbolic.dp=omega.native.inc");
    }

    public String getString(int id) {
        return getStringRep(id);
    }
}
