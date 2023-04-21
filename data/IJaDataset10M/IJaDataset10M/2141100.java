package de.iritgo.aktera.journal.module;

import de.iritgo.aktera.model.ModelException;
import de.iritgo.aktera.model.ModelRequest;
import de.iritgo.aktera.persist.CreateHandler;
import de.iritgo.aktera.persist.PersistenceException;
import de.iritgo.aktera.persist.PersistentFactory;
import org.apache.avalon.framework.logger.Logger;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Database creation.
 */
public class ModuleCreateHandler extends CreateHandler {

    /**
	 * @see de.iritgo.aktera.persist.CreateHandler#createTables(ModelRequest,
	 *      de.iritgo.aktera.persist.PersistentFactory, java.sql.Connection,
	 *      Logger)
	 */
    @Override
    public void createTables(ModelRequest request, PersistentFactory persistentFactory, Connection connection, Logger logger) throws ModelException, PersistenceException, SQLException {
        createTable("JournalEntry", "id serial primary key", "occurredat timestamp not null", "producerId int4", "producerType varchar(255)", "ownerId int4", "ownerType varchar(255)", "ownerGroupId int4", "ownerGroupType varchar(255)", "extendedInfoId int4", "extendedInfoType varchar(255)", "message varchar(255)", "shortMessage varchar(255)", "searchableText varchar(255)", "tags varchar(255)", "misc varchar(255)", "rawData varchar(255)", "primaryType varchar(255)", "secondaryType varchar(255)", "newFlag boolean");
        createIndex("JournalEntry", "occurredat");
        createTable("JournalData", "id serial primary key", "type varchar(255)", "category varchar(255)", "timestamp1 timestamp", "timestamp2 timestamp", "key int4", "integer1 int4", "integer2 int4", "string1 varchar(255)", "string2 varchar(255)", "string3 varchar(255)", "string4 varchar(255)", "string5 varchar(255)", "string6 varchar(255)");
    }

    @Override
    public void createData(PersistentFactory persistentFactory, Connection connection, Logger logger, ModelRequest request) throws ModelException, PersistenceException, SQLException {
        createInstanceSecurity("de.iritgo.aktera.ui.listing.List", "aktera.journal.list", "user", "*");
        createInstanceSecurity("de.iritgo.aktera.ui.listing.List", "aktera.journal" + ".list.notvisible", "user", "*");
        createComponentSecurity("aktera.journal.delete-journal-entry", "user", "*");
        createComponentSecurity("aktera.journal.execute-journal-entry", "user", "*");
    }
}
