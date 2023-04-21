package org.gbif.checklistbank.model.rowmapper.rs;

import org.gbif.checklistbank.service.impl.PgSqlBaseService;
import org.gbif.checklistbank.utils.InjectingTestClassRunner;
import org.gbif.checklistbank.utils.SqlStatement;
import com.google.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author markus
 */
@RunWith(InjectingTestClassRunner.class)
public class VernacularNameRowMapperIT {

    @Inject
    PgSqlBaseService service;

    @Test
    public void testSql() {
        IdRowMapper mapper = new VernacularNameRowMapper();
        SqlStatement sql = mapper.sql();
        sql.limit = 100;
        service.queryForList(sql, mapper);
    }

    @Test
    public void testSqlWhereId() {
        IdRowMapper mapper = new VernacularNameRowMapper();
        SqlStatement sql = mapper.sqlWhereId();
        service.queryForList(sql, mapper, 12345);
    }
}
