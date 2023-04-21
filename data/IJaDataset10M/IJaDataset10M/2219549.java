package org.jsemantic.core.knowledgedb;

import java.util.Iterator;

/**
 * The Interface KnowledgeDB.
 */
public interface KnowledgeDB {

    /**
	 * Gets the knowledge.
	 * 
	 * @return the knowledge
	 */
    public Iterator getKnowledge();

    /**
	 * Execute query.
	 * 
	 * @param query the query
	 * 
	 * @return the iterator
	 */
    public Iterator executeQuery(String query);

    /**
	 * Dispose.
	 */
    public void dispose();
}
