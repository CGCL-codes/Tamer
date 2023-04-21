package net.sourceforge.transumanza.selector.helper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import net.sourceforge.transumanza.selector.ParameterSetter;

public class SimpleArrayParameterSetter implements ParameterSetter {

    public void set(PreparedStatement ps, Object value) throws SQLException {
        Object[] paramList = (Object[]) value;
        for (int i = 0; i < paramList.length; i++) {
            ps.setObject(i + 1, paramList[i]);
        }
    }
}
