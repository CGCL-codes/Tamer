package org.javadatabasemigrations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.List;
import org.javadatabasemigrations.exceptions.IllegalMigrationClassException;
import org.javadatabasemigrations.exceptions.MigrationVersionExistsException;
import org.javadatabasemigrations.testmigrations.M1_TestMigration;
import org.javadatabasemigrations.testmigrations.M2_TestMigration;
import org.javadatabasemigrations.testmigrations.M4_TestMigration;
import org.javadatabasemigrations.testmigrations.MigrationClassWithIllegalName;
import org.javadatabasemigrations.testmigrations.TestMigrationsHelpers;
import org.junit.Test;

public class MigrationRunnerTest {

    @Test
    public void addNullMigrations() {
        MigrationRunner runner = new MigrationRunner();
        try {
            runner.addMigrations(null);
            fail("Should not be able to add null");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test(expected = IllegalMigrationClassException.class)
    public void addMigrationWithIllegalName() {
        Migration migration = new MigrationClassWithIllegalName();
        MigrationRunner runner = new MigrationRunner();
        runner.addMigration(migration);
    }

    @Test
    public void addMigration() {
        Migration migration = new M1_TestMigration();
        MigrationRunner runner = new MigrationRunner();
        runner.addMigration(migration);
        assertEquals(1, runner.getMigrationCount());
    }

    @Test
    public void addMigrations() {
        List<Migration> migrations = new ArrayList<Migration>();
        migrations.add(new M1_TestMigration());
        migrations.add(new M2_TestMigration());
        MigrationRunner runner = new MigrationRunner();
        runner.addMigrations(migrations);
        runner.addMigration(new M4_TestMigration());
        assertEquals(3, runner.getMigrationCount());
    }

    @Test
    public void addExistingMigrationFails() {
        List<Migration> migrations = new ArrayList<Migration>();
        migrations.add(new M1_TestMigration());
        migrations.add(new M2_TestMigration());
        MigrationRunner runner = new MigrationRunner();
        runner.addMigrations(migrations);
        assertEquals(2, runner.getMigrationCount());
        try {
            runner.addMigration(new M2_TestMigration());
            fail("Should not be able to add migration with existing version number");
        } catch (MigrationVersionExistsException ex) {
        }
        assertEquals(2, runner.getMigrationCount());
    }

    @Test
    public void highestVersion() {
        MigrationRunner runner = new MigrationRunner();
        runner.addMigration(new M1_TestMigration());
        assertEquals(new Version(1), runner.getHighestVersion());
        runner.addMigration(new M4_TestMigration());
        assertEquals(new Version(4), runner.getHighestVersion());
        runner.addMigration(new M2_TestMigration());
        assertEquals(new Version(4), runner.getHighestVersion());
    }

    @Test
    public void runToHighestVersion() {
        Database db = new MockDatabase();
        List<Migration> migrations = MigrationUtils.migrationsFromPackage(MigrationTestConstants.TEST_MIGRATIONS_PACKAGE);
        TestMigrationsHelpers.executionOrder = new ArrayList<Class>();
        List<Class> expectedExecutionOrder = new ArrayList<Class>();
        expectedExecutionOrder.add(M1_TestMigration.class);
        expectedExecutionOrder.add(M2_TestMigration.class);
        expectedExecutionOrder.add(M4_TestMigration.class);
        MigrationRunner runner = new MigrationRunner();
        runner.addMigrations(migrations);
        assertEquals("Database version should be at 0", 0, db.getVersion().getIntValue());
        runner.runToHighestVersion(db);
        assertEquals("Database version should be at highest version", runner.getHighestVersion(), db.getVersion());
        assertEquals("Unexpected execution order of migrations", expectedExecutionOrder, TestMigrationsHelpers.executionOrder);
    }
}
