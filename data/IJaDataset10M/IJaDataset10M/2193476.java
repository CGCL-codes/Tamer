package org.jaqlib;

import junit.framework.TestCase;

public class TemperatureAssert {

    private static final Temperature KITCHEN = TemperatureSetup.KITCHEN;

    private static final Temperature CELLAR = TemperatureSetup.CELLAR;

    public static void assertKitchenTemperature(Temperature temp) {
        TestCase.assertNotNull(temp);
        TestCase.assertEquals(KITCHEN.getLocation(), temp.getLocation());
        JaqlibAssert.assertEqualLists(KITCHEN.getHistory(), temp.getHistory());
    }

    public static void assertKitchenSensor(Sensor actual) {
        TestCase.assertNotNull(actual);
        Sensor expected = KITCHEN.getSensor();
        TestCase.assertEquals(expected.getName(), actual.getName());
        TestCase.assertEquals(expected.getUniqueId(), actual.getUniqueId());
        SensorType expectedType = expected.getType();
        SensorType actualType = actual.getType();
        TestCase.assertEquals(expectedType.getId(), actualType.getId());
        TestCase.assertEquals(expectedType.getName(), actualType.getName());
    }
}
