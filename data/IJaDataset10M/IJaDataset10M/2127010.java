package cn.webwheel.database.utils;

public interface NameTransform {

    String tableName(Class<?> cls);

    String colName(String field);
}
