package org.tamacat.dao.test;

import org.tamacat.dao.meta.DefaultColumn;
import org.tamacat.dao.meta.DefaultTable;
import org.tamacat.dao.meta.DataType;
import org.tamacat.dao.orm.MapBasedORMappingBean;

public class User extends MapBasedORMappingBean {

    private static final long serialVersionUID = 1L;

    public static final DefaultTable TABLE = new DefaultTable("users");

    public static final DefaultColumn USER_ID = new DefaultColumn();

    public static final DefaultColumn PASSWORD = new DefaultColumn();

    public static final DefaultColumn DEPT_ID = new DefaultColumn();

    static {
        USER_ID.setType(DataType.STRING).setPrimaryKey(true).setColumnName("user_id");
        PASSWORD.setType(DataType.STRING).setColumnName("password");
        DEPT_ID.setType(DataType.STRING).setColumnName("dept_id");
        TABLE.registerColumn(USER_ID, PASSWORD, DEPT_ID);
    }
}
