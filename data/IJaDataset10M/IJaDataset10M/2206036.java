package com.db4o.internal.query.result;

import com.db4o.ext.*;
import com.db4o.foundation.*;
import com.db4o.query.*;

/**
 * @exclude
 */
public interface QueryResult extends Iterable4 {

    public Object get(int index);

    public IntIterator4 iterateIDs();

    public Object lock();

    public ExtObjectContainer objectContainer();

    public int indexOf(int id);

    public int size();

    public void sort(QueryComparator cmp);

    public void sortIds(IntComparator cmp);

    public void skip(int count);
}
