package org.gbif.portal.dao;

/**
 * The Institution Code DAO
 * @author trobertson
 */
public interface InstitutionCodeDAO {

    /**
	 * Creates the record if there isn't one already
	 * @param identifier To create
	 * @return The id of the created record, or the id of one that already exists
	 */
    public Long createIfNotExists(String identifier);
}
