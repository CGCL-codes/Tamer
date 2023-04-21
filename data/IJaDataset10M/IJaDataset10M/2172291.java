package nacaLib.fpacPrgEnv;

import nacaLib.varEx.Var;
import nacaLib.varEx.VarBuffer;

public class VarFPacAlphaNumLengthUndef extends VarFPacLengthUndef {

    public VarFPacAlphaNumLengthUndef(FPacVarManager fpacVarManager, VarBuffer varBuffer, int nAbsolutePosition1Based) {
        super(fpacVarManager, varBuffer, nAbsolutePosition1Based);
    }

    public Var createVar(int nBufferSize) {
        return m_fpacVarManager.createFPacVarAlphaNum(m_varBuffer, m_nAbsolutePosition1Based, nBufferSize);
    }

    public Var createVar() {
        return m_fpacVarManager.createFPacVarAlphaNum(m_varBuffer, m_nAbsolutePosition1Based, 100);
    }

    int getParamLength(String cs) {
        return cs.length();
    }

    int getParamLength(int n) {
        if (n < 0) n = -n;
        String cs = String.valueOf(n);
        return cs.length();
    }

    int getParamLength(Var varSource) {
        return varSource.getLength();
    }
}
