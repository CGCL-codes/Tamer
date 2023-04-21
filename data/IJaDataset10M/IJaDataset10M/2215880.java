package com.pawlowsky.zhu.mediafiler.database;

import java.util.Vector;
import com.pawlowsky.zhu.mediafiler.database.query.QueryClause;

public interface Query {

    /**
	 * Set the clause that determines the results of executing
	 * the query.
	 * 
	 * @param clause
	 * @throws DatabaseException
	 */
    public void setQuery(QueryClause clause) throws DatabaseException;

    /**
	 * Retrieve the XML that makes up a query.
	 * @return Clause that defines the results of executing
	 *   the qeury.
	 * @throws DatabaseException
	 */
    public QueryClause getQuery() throws DatabaseException;

    /**
     * Retrieve the media files that match the query.
     * @return Collection of media files.
     */
    public Vector getMediaFiles() throws DatabaseException;

    /**
     * Retrieve the category name for the current locale.
     * @return Returns the name.
     * If there is no specific name for a locale then a best effort will be 
     * made.
     */
    public String getName() throws DatabaseException;

    /**
     * Retrieve the translations for the category's name.
     * @return Returns the known translations.
     * If there is no specific name for a locale then a best effort will be 
     * made.
     */
    public TranslationSet getNameTranslationSet() throws DatabaseException;

    /** 
     * Set the category name for the current locale. 
     * @param newName The new name for the category for the current locale.
     * @throws DatabaseException
     */
    public void setName(String newName) throws DatabaseException;

    /** 
     * Set the category name for the current locale. 
     * @param newName The new translations for the category.
     * @throws DatabaseException
     */
    public void setName(TranslationSet newName) throws DatabaseException;
}
