package com.cokemi.utils.mvc.collect;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 * �̳�dbutils��RowProcessor����ʵ��JdbcTemplate�е�����ݻص��ӿ�
 * @author Jammy Zhou
 *
 */
public class BeanRowMapper extends BeanRowProcessor implements RowMapper {

    private Class beanClass;

    public BeanRowMapper() {
    }

    public BeanRowMapper(Class cls) {
        this.setBeanClass(cls);
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    /**
	 * ʵ��JdbcTemplate��RowMapper����
	 */
    public Object mapRow(ResultSet rs, int i) throws SQLException {
        return this.toBean(rs, this.beanClass);
    }
}
