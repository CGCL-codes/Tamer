package com.tacitknowledge.util.migration;

import java.util.List;
import junit.framework.TestCase;

/**
 * Exercise the ClassMigrationTaskSource object
 * 
 * @author Mike Hardy <mailto:mike@tacitknowledge.com/>
 */
public class ClassMigrationTaskSourceTest extends TestCase {

    /**
     * Make sure class instantiation fails on null package name
     */
    public void testInstantiateTasksNullPackage() {
        ClassMigrationTaskSource source = new ClassMigrationTaskSource();
        try {
            source.getMigrationTasks(null);
            fail("We should have gotten an exception for the null package");
        } catch (MigrationException me) {
        }
    }

    /**
     * Make sure class instantiation fails on package with no types
     */
    public void testInstantiateTasksNoTasks() {
        ClassMigrationTaskSource source = new ClassMigrationTaskSource();
        try {
            List tasks = source.getMigrationTasks("com.tacitknowledge.foo.bar");
            assertEquals(0, tasks.size());
        } catch (MigrationException me) {
            fail("We should not have gotten an exception");
        }
    }

    /**
     * Make sure class instantiation fails on package with bad tasks
     */
    public void testInstantiateTasksInstantiationException() {
        ClassMigrationTaskSource source = new ClassMigrationTaskSource();
        try {
            source.getMigrationTasks(getClass().getPackage().getName() + ".tasks.instantiation");
            fail("We should have gotten an exception");
        } catch (MigrationException me) {
            assertTrue(me.getCause() instanceof RuntimeException);
        }
    }
}
