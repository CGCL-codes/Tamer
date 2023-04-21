package net.sf.dbchanges.database;

/**
 * @author olex
 */
public interface DatabaseTD {

    ConnectionConfig HSQLDB_CONN = new ConnectionConfig.Builder("localhost", "sa", "").build();

    Database HSQLDB = new Database.Builder("testdb1").conn(HSQLDB_CONN).build();

    Database TESTDB1 = new Database.Builder("testdb1").build();

    Database TESTDB2 = new Database.Builder("testdb2").build();

    Database EXTDB = new Database.Builder("extdb").conn(HSQLDB_CONN).build();

    Database NEW = new Database("new");
}
