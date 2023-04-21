package com.antlersoft.analyzer.query;

import com.antlersoft.analyzer.AccessFlags;
import com.antlersoft.query.DataSource;

/**
 * @author mike
 *
 */
class DeprecatedFilter extends FilterOnAccessFlagsTypes {

    protected boolean getCountPreservingFilterValue(DataSource source, Object toCheck) {
        return ((AccessFlags) toCheck).isDeprecated();
    }
}
