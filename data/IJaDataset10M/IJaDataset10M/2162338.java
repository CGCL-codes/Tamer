package nacaLib.varEx;

import nacaLib.tempCache.CStr;
import nacaLib.tempCache.TempCacheLocator;

/**
 * @author U930DI
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VarAlphaNum extends Var {

    VarAlphaNum(DeclareTypeX declareTypeX) {
        super(declareTypeX);
    }

    protected VarAlphaNum() {
        super();
    }

    protected VarBase allocCopy() {
        VarAlphaNum v = new VarAlphaNum();
        return v;
    }

    protected String getAsLoggableString() {
        CStr cstr = m_bufferPos.getOwnCStr(m_varDef.getLength());
        String cs = cstr.getAsString();
        return cs;
    }

    public boolean hasType(VarTypeEnum e) {
        if (e == VarTypeEnum.TypeX) return true;
        return false;
    }

    public int compareTo(int nValue) {
        int nVarValue;
        if (getString().trim().equals("")) nVarValue = -1; else nVarValue = getInt();
        return nVarValue - nValue;
    }

    public int compareTo(double dValue) {
        double dVarValue = getDouble();
        double d = dVarValue - dValue;
        if (d < -0.00001) return -1; else if (d > 0.00001) return 1;
        return 0;
    }

    protected byte[] convertUnicodeToEbcdic(char[] tChars) {
        return doConvertUnicodeToEbcdic(tChars);
    }

    protected char[] convertEbcdicToUnicode(byte[] tBytes) {
        return doConvertEbcdicToUnicode(tBytes);
    }

    public VarType getVarType() {
        return VarType.VarAlphaNum;
    }
}
