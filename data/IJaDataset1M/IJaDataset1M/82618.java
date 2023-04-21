package nacaLib.varEx;

public class CLevel {

    CLevel(VarDefBuffer varDef, int nLevel) {
        m_nLevel = nLevel;
        m_varDef = varDef;
    }

    ;

    void setWith(CLevel levelSource) {
        m_nLevel = levelSource.m_nLevel;
        m_varDef = levelSource.m_varDef;
    }

    VarDefBuffer getVarDef() {
        return m_varDef;
    }

    boolean hasLowerLevel(int nLevel) {
        if (m_nLevel < nLevel) return true;
        return false;
    }

    int m_nLevel = 0;

    private VarDefBuffer m_varDef = null;
}
