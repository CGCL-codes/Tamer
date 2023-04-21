package werkzeugkasten.twowaysql.dao.base.binder;

import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.SQLException;
import werkzeugkasten.twowaysql.dao.Binder;

public class RefBinder implements Binder {

    protected Ref value;

    public RefBinder(Ref value) {
        this.value = value;
    }

    @Override
    public void bind(PreparedStatement ps, int index) throws SQLException {
        ps.setRef(index, value);
    }
}
