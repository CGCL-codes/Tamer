package org.dhmp.util.stylesheet;

class InclusionLoopException extends ParserException {

    static final long serialVersionUID = -7877676183802347853L;

    private InclusionPath m_inclusionPath;

    /**
    * @param s
    * @param inclusionPath
    * @roseuid 3A6ED9ED01C1
    */
    public InclusionLoopException(String s, InclusionPath inclusionPath) {
        super(s);
        m_inclusionPath = inclusionPath;
    }

    /**
    * @param inclusionPath
    * @roseuid 3A6ED9ED01AD
    */
    public InclusionLoopException(InclusionPath inclusionPath) {
        super();
        m_inclusionPath = inclusionPath;
    }

    /**
    * @return com.abnamro.br.stylesheet.InclusionPath
    * @roseuid 3A6ED9ED01CC
    */
    public InclusionPath inclusionPath() {
        return m_inclusionPath;
    }
}
