package uk.ac.essex.common.gui;

/**
 *
 * <br>
 * Date: 15-Jul-2002 <br>
 * 
 * @author Laurence Smith
 * 
 * You should have received a copy of GNU public license with this code.
 * If not please visit <a href="www.gnu.org/copyleft/gpl.html">this site </a>
 */
public interface HelpHandler {

    /**
     *
     * @return
     */
    public String getHelpID();

    /**
     *
     * @return
     */
    public String getTutorialID();

    /**
     *
     * @param o
     * @return
     */
    public String getHelpIDFor(Object o);
}
