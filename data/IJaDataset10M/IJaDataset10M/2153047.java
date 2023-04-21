package net.sf.isolation.sql.spi.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import net.sf.isolation.bean.IsoBeanPropertyManager;
import net.sf.isolation.context.IsoContext;
import net.sf.isolation.sql.IsoSQLParameter;
import net.sf.isolation.sql.spi.IsoPreparedStatementParameterSetter;

public class IsoPreparedStatementParameterSetterImpl implements IsoPreparedStatementParameterSetter {

    private final IsoContext context;

    public IsoPreparedStatementParameterSetterImpl(IsoContext context) {
        this.context = context;
    }

    @Override
    public void setParameters(PreparedStatement statement, List<IsoSQLParameter> parameters, Object param) throws SQLException {
        if (param != null) {
            if (param.getClass().isArray()) {
                Object[] list = (Object[]) param;
                int length = list.length;
                for (int i = 0; i < length; i++) {
                    Object value = list[i];
                    statement.setObject(i + 1, value);
                }
            } else {
                if (param instanceof List) {
                    @SuppressWarnings("unchecked") List<Object> list = (List<Object>) param;
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        Object value = list.get(i);
                        statement.setObject(i + 1, value);
                    }
                } else {
                    if (param instanceof Map) {
                        @SuppressWarnings("unchecked") Map<String, Object> map = (Map<String, Object>) param;
                        for (IsoSQLParameter parameter : parameters) {
                            Object value = map.get(parameter);
                            statement.setObject(parameter.getIndex(), value);
                        }
                    } else {
                        for (IsoSQLParameter parameter : parameters) {
                            Object value = context.getInstance(IsoBeanPropertyManager.class).getProperty(param, parameter.getName());
                            statement.setObject(parameter.getIndex(), value);
                        }
                    }
                }
            }
        }
    }
}
