package de.sonivis.tool.mwapiconnector;

import de.sonivis.tool.core.datamodel.exceptions.DataModelInstantiationException;

/**
 * A <code>LangLink</code> is pointing from one wiki article to the corresponding article in a
 * different language within the same wiki.
 * 
 * The class provides own {@link #equals(Object) equals()} and {@link #hashCode() hashCode()}
 * methods for iterable collections.
 * 
 * @author Andreas Erber
 * @version 0.0.3, 2008-08-13
 */
public class LangLink {

    /**
	 * ID of the revision as the link source
	 * 
	 * required
	 */
    private Long fromRevID;

    /**
	 * Title string of existing target page
	 * 
	 * required
	 */
    private String title;

    /**
	 * International two-letter language abbreviation as defined in
	 * {@link http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes ISO 639-1} standard.
	 * 
	 * required
	 */
    private String langAbbr;

    /**
	 * Full Constructor.
	 * 
	 * The link source's revision ID has to be greater than 0, the target's namespace value can be
	 * one of {@link MWUtils#getNamespaces() these} or greater than 99. The target's title is only
	 * limited to be not <code>null</code> or empty. The <i>lang</i> parameter should provide the
	 * two letter international language code as defined in
	 * {@link http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes ISO 639-1} standard.
	 * 
	 * @param fromRevID
	 *            ID of source page
	 * @param title
	 *            Title of target page
	 * @param lang
	 *            language abbreviation
	 * @throws de.sonivis.tool.connector.exceptions.DataModelInstantiationException
	 *             if one of the parameters is not within its allowed range.
	 * @see MWUtils#getNamespaces()
	 */
    public LangLink(final Long fromRevID, final String title, final String lang) {
        if (fromRevID < 0) {
            throw new DataModelInstantiationException("Invalid parameter value fromRevID.");
        } else if (title == null || title.isEmpty()) {
            throw new DataModelInstantiationException("Invalid parameter value title.");
        } else if (lang == null || lang.isEmpty() || lang.length() > 2) {
            throw new DataModelInstantiationException("Invalid parameter value lang.");
        } else {
            this.fromRevID = fromRevID;
            this.title = title;
            this.langAbbr = lang;
        }
    }

    /**
	 * Get ID of revision the link belongs to
	 * 
	 * @return revision ID
	 */
    public final Long getFromRevID() {
        return this.fromRevID;
    }

    /**
	 * Get the langlink's title
	 * 
	 * @return title
	 */
    public final String getTitle() {
        return this.title;
    }

    /**
	 * Get language abbreviation string
	 * 
	 * @return language abbreviation
	 */
    public final String getLangAbbr() {
        return this.langAbbr;
    }

    /**
	 * {@inheritDoc}
	 */
    public final void print() {
        System.out.println("LangLink from RevID " + this.getFromRevID() + ", Title: \"" + this.getTitle() + ", Language: " + this.getLangAbbr());
    }
}
