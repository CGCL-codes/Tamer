package de.emoo.database.persistence;

import java.util.List;

/**
 * <b>KeywordQuery</b>:
 * provides the ability to search for a keyword within the properties of an 
 * objects class.
 * 
 * Each retrieved instance should have at least one property which contains 
 * the keyword.
 * 
 * This class has to be subclassed for concrete Transaction-implementations.
 * 
 * @author gandalf
 * @version $Id: KeywordQuery.java,v 1.1 2004/08/14 11:39:05 tbuchloh Exp $
 */
public abstract class KeywordQuery {

    private String[] _keywords;

    private Class _objectClass;

    /**
     * creates a new KeywordQuery
     * 
     */
    public KeywordQuery() {
        super();
    }

    /**
     * @return Returns the keyword.
     */
    public final String[] getKeywords() {
        return _keywords;
    }

    /**
     * @param keyword The keyword to set.
     */
    public final void setKeywords(String[] keyword) {
        _keywords = keyword;
    }

    /**
     * @return Returns the objectClass.
     */
    public final Class getObjectClass() {
        return _objectClass;
    }

    /**
     * @param objectClass The objectClass to set.
     */
    public final void setObjectClass(Class objectClass) {
        _objectClass = objectClass;
    }

    /**
     * @param transaction The transaction to set.
     */
    public abstract void setTransaction(Transaction transaction);

    /**
     * @return all instances with respect to the query. Each instance should
     *         have at least one property which contains the keyword.
     * @throws QueryEvaluationException if the query could not be evaluated.
     */
    public abstract List findAll() throws QueryEvaluationException;
}
