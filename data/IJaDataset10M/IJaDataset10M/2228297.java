package org.datanucleus.store.rdbms.sql.expression;

import org.datanucleus.ClassNameConstants;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.store.rdbms.sql.SQLStatement;
import org.datanucleus.store.rdbms.sql.SQLTable;

/**
 * Expression representing a field/property that can be stored as a String or as a Numeric.
 * For example a java.lang.Enum can be represented using this.
 * Delegates any operation to the same operation on the delegate.
 */
public class StringNumericExpression extends DelegatedExpression {

    /**
     * Constructor for an expression for a field/property that can be represented as String or numeric.
     * @param stmt The SQL statement
     * @param table Table
     * @param mapping Mapping
     */
    public StringNumericExpression(SQLStatement stmt, SQLTable table, JavaTypeMapping mapping) {
        super(stmt, table, mapping);
        if (mapping.getJavaTypeForDatastoreMapping(0).equals(ClassNameConstants.JAVA_LANG_STRING)) {
            delegate = new StringExpression(stmt, table, mapping);
        } else {
            delegate = new NumericExpression(stmt, table, mapping);
        }
    }
}
