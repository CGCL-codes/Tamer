package uk.ac.lkl.common.util.database.type;

import java.sql.Types;
import uk.ac.lkl.common.util.TernaryValue;

public class VarCharColumnType extends JDBCType {

    public int getJDBCTypeCode() {
        return Types.VARCHAR;
    }

    public ColumnTypeInstance createColumnTypeInstance(TernaryValue nullable, TernaryValue autoIncrement, int precision, int scale) {
        return new VarCharColumnTypeInstance(nullable, precision);
    }
}
