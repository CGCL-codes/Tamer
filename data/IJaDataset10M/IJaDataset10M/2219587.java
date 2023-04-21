package com.germinus.xpression.cms.cache;

/**
 * This exception should be throwed by a cache implementator if an
 * entry that is being obtained from the cache, is out of date or
 * does not exist (so it needs refresh).
 * @author agonzalez
 *
 */
public class NeedsRefreshException extends Exception {

    private static final long serialVersionUID = -1759072492404442383L;

    private com.opensymphony.oscache.base.NeedsRefreshException osCacheException;

    private String key;

    public NeedsRefreshException(com.opensymphony.oscache.base.NeedsRefreshException e) {
        this.osCacheException = e;
    }

    public NeedsRefreshException(String key) {
        this.key = key;
    }

    public com.opensymphony.oscache.base.NeedsRefreshException getOsCacheException() {
        return osCacheException;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
