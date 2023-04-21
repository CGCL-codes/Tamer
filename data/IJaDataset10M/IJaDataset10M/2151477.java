package org.mbari.vars.knowledgebase.model.dao;

/**
 * <p>Implement this class to listen for when the
 * <code>KnowledgeBaseCache</code> is cleared</p>
 * @author brian
 * @see org.mbari.vars.knowledgebase.model.dao.KnowledgeBaseCache
 * @see org.mbari.vars.knowledgebase.model.dao.IKnowledgeBaseCache
 * @see org.mbari.vars.knowledgebase.model.dao.SelfClearingKnowledgeBaseCache
 *
 */
public interface CacheClearedListener {

    /**
     * <p>This method is invoked immediately after the cache is cleared.</p>
     *
     * @param evt
     */
    void afterClear(CacheClearedEvent evt);

    /**
     * <p>This method is invoked immediately before the cache is cleared.</p>
     *
     *
     * @param evt
     */
    void beforeClear(CacheClearedEvent evt);
}
