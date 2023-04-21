package net.sf.doolin.app.mt.back.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.sf.doolin.db.hibernate.AbstractUserType;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;

public class ParameterListUserType extends AbstractUserType {

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, Object holder) throws HibernateException, SQLException {
        String text = rs.getString(names[0]);
        if (StringUtils.isBlank(text)) {
            return Collections.EMPTY_LIST;
        } else {
            String[] values = StringUtils.split(text, "|");
            return Arrays.asList(values);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement ps, Object value, int index) throws HibernateException, SQLException {
        @SuppressWarnings("unchecked") List<String> list = (List<String>) value;
        if (list == null) {
            ps.setString(index, "");
        } else {
            String text = StringUtils.join(list, "|");
            ps.setString(index, text);
        }
    }

    @Override
    public Class<?> returnedClass() {
        return List.class;
    }

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.VARCHAR };
    }
}
