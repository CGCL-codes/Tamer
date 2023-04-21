package com.netx.config.R1;

import com.netx.generics.R1.sql.JdbcDriver;

public class TL_JdbcDriver extends SimpleTypeLoader<JdbcDriver> {

    public JdbcDriver parse(String value, PropertyDef pDef) throws TypeLoadException {
        JdbcDriver driver = JdbcDriver.getRegisteredDriver(value);
        if (driver == null) {
            throw new TypeLoadException(L10n.CONFIG_MSG_CFG_JDBC_DRIVER_NOT_FOUND, value);
        }
        return driver;
    }
}
