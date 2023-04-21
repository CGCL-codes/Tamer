package org.fxplayer.dao.schema;

/**
 * The Class Schema13.
 */
public class Schema13 extends Schema {

    /**
	 * Instantiates a new schema13.
	 */
    public Schema13() {
    }

    @Override
    public void update() throws Exception {
        LOG.trace("table 'folder_log' : adding column 'updated' ");
        executeUpdate("alter table folder_log add column updated smallint default 0");
        executeUpdate("update folder_log set updated = 0");
        executeUpdate("insert into schema_version values (13)");
    }
}
