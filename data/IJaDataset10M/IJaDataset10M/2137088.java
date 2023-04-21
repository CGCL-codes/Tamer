package compiler.containers.labels;

import compiler.constants.CodeConstants;

public class BlockLabel implements CodeConstants {

    private int m_BlockId;

    private int m_Serie;

    public BlockLabel(int p_BlockId) {
        m_BlockId = p_BlockId;
        m_Serie = 1;
    }

    public String getDeclaration() {
        return "" + LABEL + " " + m_Serie + " " + m_BlockId;
    }

    public String getCode() {
        return "" + m_Serie + " " + m_BlockId;
    }
}
