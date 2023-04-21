package simpleorm.core;

import java.sql.ResultSet;
import simpleorm.core.validators.SValidatorFactory;

/** Represents String field meta data. */
public class SFieldString extends SFieldScalar {

    static final long serialVersionUID = 3L;

    /**
	 * <code>maxSize</code> is the maximum size in bytes (not characters) of
	 * the column. This fairly meaningless number is required for all database
	 * DDL except PostgreSQL, for which it is ignored.
	 */
    public SFieldString(SRecordMeta<?> meta, String columnName, int maxSize, SScalarFlags... pvals) {
        super(meta, columnName, pvals);
        setMaxSize(maxSize);
    }

    Object queryFieldValue(ResultSet rs, int sqlIndex) throws Exception {
        return rs.getString(sqlIndex);
    }

    protected Object convertToField(Object raw) {
        return raw == null ? null : raw.toString();
    }

    @Override
    String defaultSqlDataType() {
        if (getFlags().contains(SScalarFlags.ASCHAR)) {
            return "CHAR(" + maxSize() + ")";
        }
        return "VARCHAR(" + maxSize() + ")";
    }

    public boolean isCompatible(SFieldScalar field) {
        if (!(field instanceof SFieldString)) return false;
        SFieldString strField = (SFieldString) field;
        if (strField.maxSize() != this.maxSize()) return false;
        return true;
    }

    @Override
    int javaSqlType() {
        return java.sql.Types.VARCHAR;
    }
}
