package org.qedeq.kernel.base.module;

/**
 * LaTeX text part.
 *
 * @version $Revision: 1.6 $
 * @author  Michael Meyling
 */
public interface Latex {

    /**
     * Get language of LaTeX text.
     *
     * @return  Language.
     */
    public String getLanguage();

    /**
     * Get LaTeX text.
     *
     * @return  LaTeX text.
     */
    public String getLatex();
}
