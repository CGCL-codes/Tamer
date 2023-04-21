package com.antlersoft.analyzer.query;

import com.antlersoft.analyzer.DBClass;
import com.antlersoft.analyzer.DBType;
import com.antlersoft.query.CountPreservingValueExpression;
import com.antlersoft.query.DataSource;

/**
 * @author Michael A. MacDonald
 *
 */
class ClassForType extends CountPreservingValueExpression {

    ClassForType() {
        super(DBClass.class, DBType.class);
    }

    protected Object transformSingleObject(DataSource source, Object to_transform) {
        return ((DBType) to_transform).getReferencedClass();
    }
}
