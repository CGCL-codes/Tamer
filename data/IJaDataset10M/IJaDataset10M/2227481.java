package orm.com.suppx.orm.sqlparameters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Dmitry Savchenko
 */
public class ParameterString extends SqlParameter {

    private String value = null;

    public ParameterString(String name, String value) {
        super(name);
        this.value = value;
    }

    @Override
    public void process(int index, PreparedStatement prepStatement) throws SQLException {
        prepStatement.setString(index, value);
    }
}
