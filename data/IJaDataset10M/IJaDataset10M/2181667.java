package uk.ac.essex.common.lang.xml;

import java.util.Locale;

/**
 * <br>
 * Created Date: 11-Jan-2004<br>
 * <p/>
 * You should have received a copy of Lesser GNU public license with this code.
 * If not please visit <a href="http://www.gnu.org/copyleft/lesser.html">this site </a>
 *
 * @author Laurence Smith
 * @version $Revision: 1.1 $
 */
public interface ResourceConfigXML {

    Locale[] getAvailableLocales(String resourceBaseName);

    String[] getResourceBaseNames();
}
