package org.dbmaintain.launch.ant;

import org.dbmaintain.launch.DbMaintain;

/**
 * Task that removes the data of all database tables, except for the DBMAINTAIN_SCRIPTS table.
 * 
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public class CleanDatabaseTask extends BaseDatabaseTask {

    protected void performTask(DbMaintain dbMaintain) {
        dbMaintain.cleanDatabase();
    }
}
