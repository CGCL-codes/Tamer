package werkzeugkasten.twowaysql.dao.base.binder;

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import werkzeugkasten.twowaysql.dao.Binder;

public class ClobBinder implements Binder {

    protected Clob value;

    public ClobBinder(Clob value) {
        this.value = value;
    }

    @Override
    public void bind(PreparedStatement ps, int index) throws SQLException {
        ps.setClob(index, value);
    }
}
