package org.hip.kernel.bom.impl;

/**
 * The <code>NULL</code> value in a SQL statement.
 *
 * @author Luthiger
 * Created 29.08.2009 
 */
public class SQLNull {

    /**
	 * Convenience method.
	 * 
	 * @return SQLNull
	 */
    public static SQLNull getInstance() {
        return new SQLNull();
    }

    @Override
    public String toString() {
        return "NULL";
    }
}
