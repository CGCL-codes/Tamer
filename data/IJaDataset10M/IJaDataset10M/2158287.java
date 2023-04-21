package ru.adv.test.repository.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import ru.adv.db.adapter.DBAdapter;

@ContextConfiguration
public class HsqldbRequestTest extends AbstractRequestTest {

    private static final String DBNAME = "hsqldbtest";

    @Test
    @DirtiesContext
    public void testDirtyContext() {
        logger.info("Reconfigure context");
    }

    @Override
    public void cleanTables() {
        try {
            simpleJdbcTemplate.getJdbcOperations().execute("SET REFERENTIAL_INTEGRITY FALSE");
            super.cleanTables();
            simpleJdbcTemplate.getJdbcOperations().execute("SET REFERENTIAL_INTEGRITY TRUE");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected List<String> getAllTableNames() {
        List<String> tableNames = new ArrayList<String>();
        List<Map<String, Object>> tables = simpleJdbcTemplate.queryForList("SELECT TABLE_NAME, TABLE_TYPE, TABLE_SCHEM " + "FROM INFORMATION_SCHEMA.SYSTEM_TABLES " + "WHERE TABLE_SCHEM='PUBLIC' AND TABLE_TYPE='TABLE'");
        for (Map<String, Object> row : tables) {
            tableNames.add(row.get("TABLE_NAME").toString());
        }
        return tableNames;
    }

    @Override
    public String getAdapterName() {
        return DBAdapter.HSQLDB;
    }

    @Override
    public String getDbName() {
        return DBNAME;
    }
}
