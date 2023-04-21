package werkzeugkasten.twowaysql.dao.base.binder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import werkzeugkasten.twowaysql.dao.Binder;

public class ShortBinder implements Binder {

    protected Short value;

    public ShortBinder(Short value) {
        this.value = value;
    }

    @Override
    public void bind(PreparedStatement ps, int index) throws SQLException {
        ps.setShort(index, value);
    }
}
