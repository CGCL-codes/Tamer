package utils;

/**
 *
 * @author Pierre-Jean Ditscheid, Consultas SA
 * @version $Id$
 */
public class TranscoderAction {

    public static TranscoderAction SyntaxCheck = new TranscoderAction(true, false);

    public static TranscoderAction All = new TranscoderAction(true, true);

    private boolean m_bSyntaxCheck = false;

    private boolean m_bGeneration = false;

    private TranscoderAction(boolean bSyntax, boolean bGeneration) {
        m_bSyntaxCheck = bSyntax;
        m_bGeneration = bGeneration;
    }

    public boolean isSyntaxCheck() {
        return m_bSyntaxCheck;
    }

    public boolean isGeneration() {
        return m_bGeneration;
    }

    public String getAsString() {
        String cs = "";
        if (m_bSyntaxCheck) cs = "SyntaxCheck";
        if (m_bGeneration) cs = " Semantic Generation";
        return cs;
    }
}
