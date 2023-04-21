package werkzeugkasten.twowaysql.dao.base.binder;

import java.sql.PreparedStatement;
import java.sql.RowId;
import java.sql.SQLException;
import werkzeugkasten.twowaysql.dao.Binder;

public class RowIdBinder implements Binder {

    protected RowId value;

    public RowIdBinder(RowId value) {
        this.value = value;
    }

    @Override
    public void bind(PreparedStatement ps, int index) throws SQLException {
        ps.setRowId(index, value);
    }
}
