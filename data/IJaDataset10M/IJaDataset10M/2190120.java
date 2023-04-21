package nacaLib.varEx;

import nacaLib.basePrgEnv.BaseProgramManager;
import nacaLib.tempCache.TempCache;
import nacaLib.tempCache.TempCacheLocator;
import jlib.misc.AsciiEbcdicConverter;
import jlib.misc.NumberParser;

public class VarInternalInt extends Var {

    public VarInternalInt() {
        super(null);
        m_varDef = new VarDefInternalInt(this);
        m_varTypeId = m_varDef.getTypeId();
        m_n = 0;
    }

    protected VarBase allocCopy() {
        VarInternalInt v = new VarInternalInt();
        v.m_n = m_n;
        return v;
    }

    public void set(int n) {
        m_n = n;
    }

    public void set(String cs) {
        m_n = NumberParser.getAsInt(cs);
    }

    public void set(char c) {
        m_n = NumberParser.getAsInt(c);
    }

    protected String getAsLoggableString() {
        return String.valueOf(m_n);
    }

    public boolean hasType(VarTypeEnum e) {
        if (e == VarTypeEnum.Type9) return true;
        return false;
    }

    public int getInt() {
        return m_n;
    }

    public double getDouble() {
        return m_n;
    }

    public String toString() {
        return "InternalVar: " + String.valueOf(m_n);
    }

    int getSingleItemRequiredStorageSize() {
        return 0;
    }

    int m_n;

    public void transferTo(Var varDest) {
        varDest.set(m_n);
    }

    public String getSTCheckValue() {
        return "VarInternalInt(" + m_n + ")";
    }

    public int compareTo(int nValue) {
        int nVarValue = getInt();
        return nVarValue - nValue;
    }

    public int compareTo(double dValue) {
        double dVarValue = getDouble();
        double d = dVarValue - dValue;
        if (d < -0.00001) return -1; else if (d > 0.00001) return 1;
        return 0;
    }

    protected byte[] convertUnicodeToEbcdic(char[] tChars) {
        return AsciiEbcdicConverter.noConvertUnicodeToEbcdic(tChars);
    }

    protected char[] convertEbcdicToUnicode(byte[] tBytes) {
        return AsciiEbcdicConverter.noConvertEbcdicToUnicode(tBytes);
    }

    public VarType getVarType() {
        return VarType.VarInternalInt;
    }
}
