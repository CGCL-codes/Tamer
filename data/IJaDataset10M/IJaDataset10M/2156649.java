package fhire.xml;

import org.jdom.Element;
import fhire.database.hibernate.util.DatabaseManager;
import fhire.language.Language;
import fhire.utils.Initialisator;

/**
 * Class to create an XML Document to show the navigation
 * 
 * @author Joerg Doppelreiter, Johann Zagler, Robert Maierhofer
 *
 */
public abstract class NavigationManagement {

    private byte bytLanguage;

    private DatabaseManager dbmDBManager;

    private String strContextPath;

    private Element elmNavigation = null;

    public NavigationManagement() {
    }

    /**
	 * @return Returns the bytLanguage.
	 */
    protected byte getLanguage() {
        return bytLanguage;
    }

    /**
	 * @param bytLanguage The bytLanguage to set.
	 */
    protected void setLanguage(byte language) {
        this.bytLanguage = language;
    }

    /**
     * 
     * @return String
     */
    protected String getContextPath() {
        return strContextPath;
    }

    /**
     * 
     * @param strContextPath
     */
    protected void setContextPath(String strContextPath) {
        this.strContextPath = strContextPath;
    }

    /**
	 * 
	 * @return DatabaseManager
	 */
    protected DatabaseManager getDatabaseManager() {
        return dbmDBManager;
    }

    /**
	 * 
	 * @param dbmDBManager
	 */
    protected void setDatabaseManager(DatabaseManager dbmDBManager) {
        this.dbmDBManager = dbmDBManager;
    }

    /**
     * 
     * @return Element
     */
    protected Element getNavigationManagement() {
        if (elmNavigation == null) {
            Element elmLink;
            elmNavigation = new Element("navigation");
            elmLink = new Element("link");
            elmLink.addContent(Language.getText("navigation-usermanagement", bytLanguage, Language.TEXT));
            elmLink.setAttribute("href", strContextPath + Initialisator.getDispatcherName() + "?" + Initialisator.getActionParameter() + "=" + Initialisator.getActionUsermanagement());
            elmNavigation.addContent(elmLink);
            elmLink = new Element("link");
            elmLink.addContent(Language.getText("navigation-graphicpath", bytLanguage, Language.TEXT));
            elmLink.setAttribute("href", strContextPath + Initialisator.getDispatcherName() + "?" + Initialisator.getActionParameter() + "=" + Initialisator.getActionGraphicpath());
            elmNavigation.addContent(elmLink);
        }
        return elmNavigation;
    }
}
