package org.gbif.checklistbank.model.rowmapper.tab;

import org.gbif.checklistbank.service.impl.PgSqlBaseService;
import org.gbif.checklistbank.utils.InjectingTestClassRunner;
import org.gbif.checklistbank.utils.SqlStatement;
import com.google.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectingTestClassRunner.class)
public class NameStringLiteMapperIT {

    @Inject
    PgSqlBaseService service;

    @Test
    public void testSql() {
        NameStringLiteMapper mapper = new NameStringLiteMapper();
        SqlStatement sql = mapper.sql();
        sql.limit = 100;
        service.bulkRead(sql, mapper);
    }
}
