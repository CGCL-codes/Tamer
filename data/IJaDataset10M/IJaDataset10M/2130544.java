package com.cross.cfg;

public interface CrossSessionFactory {

    public CrossSession getCrossSession(Class clazz) throws Exception;
}
