package org.datascooter.impl;

import java.io.Serializable;
import org.datascooter.utils.policy.SnipType;

/**
 * The root object it is main and only one agent for moving data inside datascooter - it is container for moving query and return results
 * 
 * @author nemo
 * 
 */
public final class Snip implements Serializable {

    private static final long serialVersionUID = 5046677360141817513L;

    public final String tranId;

    public final String entity;

    public final SnipType type;

    public final String query;

    public final String[] fields;

    public final int parentIndex;

    public final int hashCode;

    public int fetchSize = 0;

    public int fromRow = 1;

    private Snip[] fetch;

    private Object[][] data;

    public Snip(String entity, SnipType type, String query, Object[][] params, String[] fields, Snip[] fetch, int parentIndex, String transactionId) {
        this.entity = entity;
        this.type = type;
        this.query = query;
        this.tranId = transactionId;
        setData(params);
        this.fields = fields;
        setFetch(fetch);
        this.parentIndex = parentIndex;
        hashCode = SnipFactory.buildhash(this);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }

    /**
	 * Returns two-dimension array of data - it may be data for query or result data
	 * 
	 * @return Object[][]
	 */
    public Object[][] getData() {
        return data;
    }

    /**
	 * Sets sub snips - it will be processed after but in one transaction
	 * 
	 * @param fetch
	 */
    public void setFetch(Snip[] fetch) {
        this.fetch = fetch;
    }

    /**
	 * Returns an array of snips will be executed after this
	 * 
	 * @return Snip[]
	 */
    public Snip[] getFetch() {
        return fetch;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Snip other = (Snip) obj;
        if (other.hashCode != hashCode) return false;
        return true;
    }
}
