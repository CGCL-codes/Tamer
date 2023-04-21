package com.dbxml.db.core.indexer.helpers;

import java.util.Arrays;
import com.dbxml.db.core.data.Value;
import com.dbxml.db.core.indexer.IndexPattern;
import com.dbxml.db.core.indexer.IndexQuery;

/**
 * IndexQueryNIN
 */
public final class IndexQueryNIN extends IndexQuery {

    public IndexQueryNIN(IndexPattern pattern, Value[] vals) {
        super(pattern, NIN, vals);
    }

    public int getOperator() {
        return NIN;
    }

    public boolean testValue(Value v) {
        return Arrays.binarySearch(vals, v) < 0;
    }
}
