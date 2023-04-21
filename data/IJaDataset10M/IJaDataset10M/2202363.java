package org.unitils.database.example6;

import org.unitils.UnitilsJUnit4;
import org.unitils.database.annotations.TestDataSource;
import org.unitils.database.annotations.Transactional;
import javax.sql.DataSource;

@Transactional
public class MyDaoTest extends UnitilsJUnit4 {

    @TestDataSource
    private DataSource dataSource;
}
