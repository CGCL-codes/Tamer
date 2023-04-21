package org.mariella.persistence.test.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import oracle.jdbc.driver.OracleDriver;
import org.junit.Test;
import org.mariella.glue.service.Persistence;
import org.mariella.persistence.annotations.mapping_builder.DatabaseMetaDataDatabaseInfoProvider;
import org.mariella.persistence.annotations.mapping_builder.PersistenceBuilder;
import org.mariella.persistence.annotations.processing.OxyUnitInfo;
import org.mariella.persistence.annotations.processing.OxyUnitInfoBuilder;
import org.mariella.persistence.database.Column;
import org.mariella.persistence.database.Schema;
import org.mariella.persistence.database.Table;
import org.mariella.persistence.mapping.SchemaMapping;

public class TestPersistence implements Persistence {

    public static TestPersistence Singleton = new TestPersistence();

    private SchemaMapping schemaMapping;

    public static void main(String[] args) {
        TestPersistence persistence = new TestPersistence();
        System.out.println("Unused tables: ");
        for (Table table : persistence.schemaMapping.getUnusedTables()) {
            System.out.println("\t" + table.getName());
        }
        System.out.println();
        System.out.println("Unused columns: ");
        for (Column column : persistence.schemaMapping.getUnusedColumns()) {
            System.out.println("\t" + getTableForColumn(persistence.getSchemaMapping().getSchema(), column) + "." + column.getName());
        }
    }

    private static Table getTableForColumn(Schema schema, Column column) {
        for (Table table : schema.getTables()) {
            if (table.getColumns().contains(column)) {
                return table;
            }
        }
        return null;
    }

    public TestPersistence() {
        super();
        try {
            buildMapping();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SchemaMapping getSchemaMapping() {
        return schemaMapping;
    }

    @Test
    public void buildMapping() throws Exception {
        OxyUnitInfoBuilder b = new OxyUnitInfoBuilder();
        b.setClassLoader(getClass().getClassLoader());
        b.build();
        b.getOxyUnitInfos().get(0).debugPrint(System.out);
        DriverManager.registerDriver(new OracleDriver());
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@vievmsdrsld1.eu.boehringer.com:1521:htssd", "aim", "aim");
        try {
            DatabaseMetaData dmd = connection.getMetaData();
            DatabaseMetaDataDatabaseInfoProvider provider = new DatabaseMetaDataDatabaseInfoProvider(dmd);
            provider.setIgnoreSchema(true);
            provider.setIgnoreCatalog(true);
            OxyUnitInfo oui = b.getOxyUnitInfos().get(0);
            PersistenceBuilder persistenceBuilder = new PersistenceBuilder(oui, provider);
            persistenceBuilder.build();
            schemaMapping = persistenceBuilder.getPersistenceInfo().getSchemaMapping();
        } finally {
            connection.close();
        }
    }
}
