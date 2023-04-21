package com.cateshop.db;

import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import org.hibernate.type.StringType;
import org.hibernate.usertype.ParameterizedType;

/**
 * @author notXX
 */
public class StringMax extends StringType implements ParameterizedType {

    private static final String LENGTH = "length";

    /**
     * <code>oracle</code>�п���ֱ�ӵ���<code>java.sql.PreparedStatement.setCharacterStream(int, Reader, int)</code>�������ֵ�����.
     */
    private static final int LIMIT = 666;

    /**
     * 
     */
    private static final long serialVersionUID = -8110583332208052515L;

    /**
     * 
     */
    private int length = 4000;

    @Override
    public void set(PreparedStatement st, Object value, int index) throws SQLException {
        String string = (String) value;
        if ((string != null) && (string.length() > length)) {
            string = string.substring(0, length);
        }
        int actualLength = (string != null) ? string.length() : 0;
        if (actualLength > LIMIT) {
            st.setCharacterStream(index, new StringReader(string), actualLength);
        } else {
            st.setString(index, string);
        }
    }

    public void setParameterValues(Properties parameters) {
        if ((parameters != null) && (parameters.containsKey(LENGTH))) {
            String parameter = parameters.getProperty(LENGTH);
            try {
                length = Integer.parseInt(parameter);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("hibernate column type 'string_max' can't parse value '" + parameter + "' as a max length.  default is 4000.", e);
            }
        }
    }
}
