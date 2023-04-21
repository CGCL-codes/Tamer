package com.ibatis.dao.client;

import com.ibatis.dao.engine.builder.xml.XmlDaoManagerBuilder;
import java.io.Reader;
import java.util.Properties;

public class DaoManagerBuilder {

    private DaoManagerBuilder() {
    }

    public static DaoManager buildDaoManager(Reader reader, Properties props) throws DaoException {
        return new XmlDaoManagerBuilder().buildDaoManager(reader, props);
    }

    public static DaoManager buildDaoManager(Reader reader) {
        return new XmlDaoManagerBuilder().buildDaoManager(reader);
    }
}
