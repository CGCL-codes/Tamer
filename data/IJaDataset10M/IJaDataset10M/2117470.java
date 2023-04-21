package nl.mwensveen.csv.db.type;

import nl.mwensveen.csv.db.type.api.DbType;

/**
 * @author mwensveen
 *
 */
public class NumericDbType extends DecimalDbType implements DbType {

    /**
	 * @see nl.mwensveen.csv.db.type.api.DbType#getSqlType()
	 */
    @Override
    public String getSqlType() {
        return "NUMERIC";
    }
}
