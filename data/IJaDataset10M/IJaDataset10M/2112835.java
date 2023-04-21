package org.stars.dao.sqlmapper.parser;

import org.junit.Test;
import org.stars.config.Config;
import org.stars.dao.config.DaoConfig;
import org.stars.dao.sqlmapper.SqlDefinition;
import org.stars.dao.sqlmapper.SqlMapper;

public class Test05_FullParseSql extends FullBaseTest {

    @Test
    public void test01() throws Exception {
        DaoConfig daoConfig = Config.getDaoConfig();
        SqlMapper sqlMapper = daoConfig.getSqlMapperMap().get("prova2");
        SqlDefinition sql = sqlMapper.getMap().get("select2");
        log.info(sql.toString());
    }

    @Test
    public void test02() throws Exception {
        DaoConfig daoConfig = Config.getDaoConfig();
        SqlMapper sqlMapper = daoConfig.getSqlMapperMap().get("prova2");
        SqlDefinition sql = sqlMapper.getMap().get("select2");
        log.info(sql.toString());
    }

    @Test
    public void test03() throws Exception {
        DaoConfig daoConfig = Config.getDaoConfig();
        SqlMapper sqlMapper = daoConfig.getSqlMapperMap().get("prova2");
        SqlDefinition sql = sqlMapper.getMap().get("insert1");
        log.info(sql.toString());
    }

    @Test
    public void test04() throws Exception {
        DaoConfig daoConfig = Config.getDaoConfig();
        SqlMapper sqlMapper = daoConfig.getSqlMapperMap().get("prova2");
        SqlDefinition sql = sqlMapper.getMap().get("select3");
        log.info(sql.toString());
    }
}
