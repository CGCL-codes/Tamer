package self.micromagic.eterna.sql.preparer;

import java.sql.SQLException;
import self.micromagic.eterna.sql.PreparedStatementWrap;

public class BytePreparer extends AbstractValuePreparer {

    private byte value;

    public BytePreparer(int index, byte value) {
        this.setRelativeIndex(index);
        this.value = value;
    }

    public void setValueToStatement(int index, PreparedStatementWrap stmtWrap) throws SQLException {
        stmtWrap.setByte(this.getName(), index, this.value);
    }
}
