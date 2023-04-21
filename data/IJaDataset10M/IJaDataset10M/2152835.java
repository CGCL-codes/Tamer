package org.nexopenframework.persistence.support;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Extension of the {@link ArrayList} for dealing with pagination features such as first result,
 * max result and row count. Moreover, it keeps teh result of the query</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class PaginationList extends ArrayList implements List {

    public static final String PAGINATION_ENABLED_HINT = "openfrwk.pagination.enabled";

    private static final long serialVersionUID = 1L;

    private int firstResult;

    private int maxResult;

    private int rowCount;

    public PaginationList(List values, int firstResult, int maxResult, int rowCount) {
        this.addAll(values);
        this.firstResult = firstResult;
        this.maxResult = maxResult;
        this.rowCount = rowCount;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public int getRowCount() {
        return rowCount;
    }
}
