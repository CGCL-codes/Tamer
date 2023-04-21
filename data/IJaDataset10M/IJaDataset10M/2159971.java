package com.xy.sframe.component.memdb;

public class SqlTransformFactory {

    public static ISqlTransform getSqlTransform(String srcDriverType, String destDriverType) {
        if (srcDriverType.substring(srcDriverType.lastIndexOf(".") + 1).equalsIgnoreCase("DB2Driver") && destDriverType.substring(destDriverType.lastIndexOf(".") + 1).equalsIgnoreCase("jdbcDriver")) {
            return new Db2ToHSqlImpl();
        }
        return null;
    }
}
