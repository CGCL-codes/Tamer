package org.kwantu.m2.model.ui;

import java.io.Serializable;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kwantu.m2.KwantuFaultException;
import org.kwantu.m2.xpath.KwantuXPathContext;
import org.kwantu.persistence.AbstractPersistentObject;

/**
 *
 * @author siviwe
 */
public abstract class AbstractSortKey extends AbstractPersistentObject implements Serializable {

    private static final Log LOG = LogFactory.getLog(AbstractSortKey.class);

    private String sortAttributePath;

    private SortType sortType;

    public enum SortType {

        ASCENDING, DESCENDING
    }

    public abstract KwantuTable getOwningKwantuTable();

    public String getSortAttributePath() {
        return sortAttributePath;
    }

    public void setSortAttributePath(String sortAttributePath) {
        if (sortAttributePath == null) {
            throw new KwantuFaultException("sortattributepath cannot be null");
        }
        this.sortAttributePath = sortAttributePath;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    boolean hasSortKey(String sortAttributePath) {
        for (KwantuSortKey sortKey : this.getOwningKwantuTable().getSortKeys()) {
            if (sortAttributePath.equals(sortKey.getSortAttributePath())) {
                return true;
            }
        }
        return false;
    }

    public Object valueToSort(Object object) {
        Object result = null;
        try {
            KwantuXPathContext context = new KwantuXPathContext(null, object);
            result = context.getValue(sortAttributePath);
        } catch (JXPathException e) {
            LOG.debug("XPath get context failed for " + sortAttributePath, e);
            result = null;
        }
        return result;
    }
}
