package net.sf.mustang.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import net.sf.mustang.util.BindUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.jdbc.core.RowMapper;

@SuppressWarnings("unchecked")
public class JdbcBeanRowMapper implements RowMapper {

    private Class beanClass;

    public JdbcBeanRowMapper(Class beanClass) {
        this.beanClass = beanClass;
    }

    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Object bean = null;
        try {
            bean = beanClass.newInstance();
            MutablePropertyValues pvs = new MutablePropertyValues();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                pvs.addPropertyValue(rs.getMetaData().getColumnName(i), rs.getObject(i));
            }
            BindUtils.bind(bean, pvs);
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
        return bean;
    }
}
