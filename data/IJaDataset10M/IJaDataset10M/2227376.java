package whf.framework.datasource.impl;

import java.util.Collection;
import whf.framework.datasource.DataRow;

/**
 * @author king
 * @modify 2007-12-13 下午01:04:38
 * @param <E>
 */
public class CollectionDataSource<E extends DataRow> extends AbstractDataSource<E> {

    public CollectionDataSource(Collection<E> coll) {
        this.dataCollection = coll;
    }
}
