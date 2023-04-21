package com.mycila.testing.plugin.db.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface SqlColumnHeader {

    int index();

    String name();

    int displaySize();

    Class<?> type();

    SqlType sqlType();

    String typeName();
}
