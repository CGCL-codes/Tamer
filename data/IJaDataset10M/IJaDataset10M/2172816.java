package self.micromagic.eterna.sql.preparer;

import java.sql.SQLException;
import self.micromagic.eterna.sql.PreparedStatementWrap;

public class ShortPreparer extends AbstractValuePreparer {

    private short value;

    public ShortPreparer(int index, short value) {
        this.setRelativeIndex(index);
        this.value = value;
    }

    public void setValueToStatement(int index, PreparedStatementWrap stmtWrap) throws SQLException {
        stmtWrap.setShort(this.getName(), index, this.value);
    }
}
