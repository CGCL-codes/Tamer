package org.javalite.activejdbc;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.test.ActiveJDBCTest;
import org.javalite.activejdbc.test_models.*;
import org.junit.Ignore;
import org.junit.Test;
import java.util.List;
import java.util.Map;

/**
 * @author Stephane Restani
 */
public class HierarchyTest extends ActiveJDBCTest {

    /**
	 * This test verifies that single-table inheritance is not possible.
	 * Cheese extends Meal which are both non-abstract classes
	 */
    @Test(expected = org.javalite.activejdbc.DBException.class)
    public void shouldFailSTI() {
        Cheese.count();
    }

    /**
	 * This test verifies that one level of abstract inheritance is possible.
	 * Sword extends (abstract) Weapon
	 */
    @Test
    public void shouldAcceptSingleLevelInheritance() {
        Sword.count();
    }

    /**
	 * This test verifies that multiple levels of abstract inheritance are possible.
	 * Cake extends (abstract) Pastry which extends (abstract) Dessert
	 */
    @Test
    public void shouldAcceptMultipleLevelsInheritance() {
        Cake.count();
    }
}
