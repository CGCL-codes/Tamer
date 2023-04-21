package com.customwars.client.model.gameobject;

import com.customwars.client.model.TestData;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author stefan
 */
public class UnitEventTest implements PropertyChangeListener {

    private Unit unit;

    private GameObjectState oldState;

    @BeforeClass
    public static void beforeAllTests() {
        TestData.storeTestData();
    }

    @Before
    public void beforeEachTest() {
        unit = UnitFactory.getUnit(TestData.INF);
        unit.addPropertyChangeListener(this);
    }

    @After
    public void afterEachTest() {
        unit.removePropertyChangeListener(this);
        unit = null;
    }

    @AfterClass
    public static void afterAllTests() {
        TestData.clearTestData();
    }

    @Test
    public void testEvents() {
        oldState = unit.getState();
        unit.setState(GameObjectState.DESTROYED);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (propertyName.equals("state")) {
            Assert.assertEquals(oldState, evt.getOldValue());
            Assert.assertEquals(GameObjectState.DESTROYED, evt.getNewValue());
        } else {
            throw new AssertionError("Received changed property " + propertyName);
        }
    }
}
