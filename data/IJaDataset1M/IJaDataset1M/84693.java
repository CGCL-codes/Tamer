package org.openremote.controller.model.sensor;

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openremote.controller.protocol.ReadCommand;
import org.openremote.controller.statuscache.ChangedStatusTable;
import org.openremote.controller.statuscache.EventProcessorChain;
import org.openremote.controller.statuscache.StatusCache;

/**
 * Base tests for {@link org.openremote.controller.model.sensor.StateSensor} class.
 *
 * @author <a href="mailto:juha@openremote.org">Juha Lindfors</a>
 */
public class StateSensorTest {

    private StatusCache cache = null;

    @Before
    public void setup() {
        ChangedStatusTable cst = new ChangedStatusTable();
        EventProcessorChain echain = new EventProcessorChain();
        cache = new StatusCache(cst, echain);
    }

    /**
   * Simple test case of a one-state sensor.
   *
   * @throws Exception if test fails
   */
    @Test
    public void testSingleState() throws Exception {
        StateSensor.DistinctStates states = new StateSensor.DistinctStates();
        states.addState("foo");
        StateSensor s1 = new StateSensor("single", 1, cache, new StateReadCommand("foo"), states);
        cache.registerSensor(s1);
        s1.start();
        Assert.assertTrue(getSensorValueFromCache(1).equals("foo"));
        Assert.assertTrue(s1.getSensorID() == 1);
        Assert.assertTrue(s1.isPolling());
        Assert.assertFalse(s1.isEventListener());
        Assert.assertTrue(s1.getName().equals("single"));
        Assert.assertTrue(s1.getProperties().size() == 1);
        Assert.assertTrue(s1.getProperties().keySet().contains("state-1"));
        Assert.assertTrue(s1.getProperties().values().contains("foo"));
    }

    /**
   * A test case of a two-state sensor (similar to switch).
   *
   * @throws Exception if test fails
   */
    @Test
    public void testTwoState() throws Exception {
        StateSensor.DistinctStates states = new StateSensor.DistinctStates();
        states.addState("foo");
        states.addState("bar");
        final int SENSOR_ID = 2;
        StateReadCommand readCommand = new StateReadCommand("foo", "bar");
        StateSensor s1 = new StateSensor("twostate", SENSOR_ID, cache, readCommand, states);
        cache.registerSensor(s1);
        s1.start();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("foo"));
        Assert.assertTrue(s1.getSensorID() == SENSOR_ID);
        Assert.assertFalse(s1.isEventListener());
        Assert.assertTrue(s1.isPolling());
        Assert.assertTrue(s1.getName().equals("twostate"));
        Assert.assertTrue(s1.getProperties().size() == SENSOR_ID);
        Assert.assertTrue(s1.getProperties().keySet().contains("state-1"));
        Assert.assertTrue(s1.getProperties().keySet().contains("state-2"));
        Assert.assertTrue(s1.getProperties().values().contains("foo"));
        Assert.assertTrue(s1.getProperties().values().contains("bar"));
        readCommand.nextValue();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("bar"));
    }

    /**
   * Test case of a three-state sensor
   *
   * @throws Exception if test fails
   */
    @Test
    public void testThreeState() throws Exception {
        StateSensor.DistinctStates states = new StateSensor.DistinctStates();
        states.addState("foo");
        states.addState("bar");
        states.addState("acme");
        final int SENSOR_ID = 3;
        StateReadCommand readCommand = new StateReadCommand("foo", "bar", "acme");
        StateSensor s1 = new StateSensor("threestate", SENSOR_ID, cache, readCommand, states);
        cache.registerSensor(s1);
        s1.start();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("foo"));
        Assert.assertTrue(s1.getSensorID() == SENSOR_ID);
        Assert.assertTrue(s1.isPolling());
        Assert.assertFalse(s1.isEventListener());
        Assert.assertTrue(s1.getName().equals("threestate"));
        Assert.assertTrue(s1.getProperties().size() == SENSOR_ID);
        Assert.assertTrue(s1.getProperties().keySet().contains("state-1"));
        Assert.assertTrue(s1.getProperties().keySet().contains("state-2"));
        Assert.assertTrue(s1.getProperties().keySet().contains("state-3"));
        Assert.assertTrue(s1.getProperties().values().contains("foo"));
        Assert.assertTrue(s1.getProperties().values().contains("bar"));
        Assert.assertTrue(s1.getProperties().values().contains("acme"));
        readCommand.nextValue();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("bar"));
        readCommand.nextValue();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("acme"));
    }

    /**
   * Test case of a sensor with ten distinct states.
   *
   * @throws Exception if test fails
   */
    @Test
    public void testTenState() throws Exception {
        StateSensor.DistinctStates states = new StateSensor.DistinctStates();
        states.addState("one");
        states.addState("two");
        states.addState("three");
        states.addState("four");
        states.addState("five");
        states.addState("six");
        states.addState("seven");
        states.addState("eight");
        states.addState("nine");
        states.addState("ten");
        final int SENSOR_ID = 4;
        StateReadCommand readCommand = new StateReadCommand("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten");
        StateSensor s1 = new StateSensor("tenstate", 4, cache, readCommand, states);
        cache.registerSensor(s1);
        s1.start();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("one"));
        Assert.assertTrue(s1.getSensorID() == 4);
        Assert.assertTrue(s1.isPolling());
        Assert.assertFalse(s1.isEventListener());
        Assert.assertTrue(s1.getName().equals("tenstate"));
        Assert.assertTrue(s1.getProperties().size() == 10);
        Assert.assertTrue(s1.getProperties().keySet().contains("state-1"));
        Assert.assertTrue(s1.getProperties().keySet().contains("state-2"));
        Assert.assertTrue(s1.getProperties().keySet().contains("state-3"));
        Assert.assertTrue(s1.getProperties().keySet().contains("state-4"));
        Assert.assertTrue(s1.getProperties().keySet().contains("state-5"));
        Assert.assertTrue(s1.getProperties().keySet().contains("state-6"));
        Assert.assertTrue(s1.getProperties().keySet().contains("state-7"));
        Assert.assertTrue(s1.getProperties().keySet().contains("state-8"));
        Assert.assertTrue(s1.getProperties().keySet().contains("state-9"));
        Assert.assertTrue(s1.getProperties().keySet().contains("state-10"));
        Assert.assertTrue(s1.getProperties().values().contains("one"));
        Assert.assertTrue(s1.getProperties().values().contains("two"));
        Assert.assertTrue(s1.getProperties().values().contains("three"));
        Assert.assertTrue(s1.getProperties().values().contains("four"));
        Assert.assertTrue(s1.getProperties().values().contains("five"));
        Assert.assertTrue(s1.getProperties().values().contains("six"));
        Assert.assertTrue(s1.getProperties().values().contains("seven"));
        Assert.assertTrue(s1.getProperties().values().contains("eight"));
        Assert.assertTrue(s1.getProperties().values().contains("nine"));
        Assert.assertTrue(s1.getProperties().values().contains("ten"));
        readCommand.nextValue();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("two"));
        readCommand.nextValue();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("three"));
        readCommand.nextValue();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("four"));
        readCommand.nextValue();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("five"));
        readCommand.nextValue();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("six"));
        readCommand.nextValue();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("seven"));
        readCommand.nextValue();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("eight"));
        readCommand.nextValue();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("nine"));
        readCommand.nextValue();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("ten"));
    }

    /**
   * Test sensor behavior when event producer returns a value that has not been added to this
   * state sensor's configuration.
   *
   * @throws Exception if test fails
   */
    @Test
    public void testFalseReturn() throws Exception {
        StateSensor.DistinctStates states = new StateSensor.DistinctStates();
        states.addState("bar");
        states.addState("foo");
        final int SENSOR_ID = 5;
        StateReadCommand readCommand = new MixedStateReadCommand("acme", "foo", "bar");
        StateSensor s1 = new StateSensor("funky", SENSOR_ID, cache, readCommand, states);
        cache.registerSensor(s1);
        s1.start();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals(Sensor.UNKNOWN_STATUS));
        Assert.assertTrue(s1.getSensorID() == SENSOR_ID);
        Assert.assertTrue(s1.isPolling());
        Assert.assertFalse(s1.isEventListener());
        Assert.assertTrue(s1.getName().equals("funky"));
        Assert.assertTrue(s1.getProperties().size() == 2);
        Assert.assertTrue(s1.getProperties().keySet().contains("state-1"));
        Assert.assertTrue(s1.getProperties().keySet().contains("state-2"));
        Assert.assertTrue(s1.getProperties().values().contains("foo"));
        Assert.assertTrue(s1.getProperties().values().contains("bar"));
        readCommand.nextValue();
        String readVal = getSensorValueFromCache(SENSOR_ID);
        Assert.assertTrue("Expected 'foo', got : " + readVal, readVal.equals("foo"));
        readCommand.nextValue();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("bar"));
    }

    /**
   * Test mapping of state string from event producers to translated forms.
   *
   * @throws Exception if test fails
   */
    @Test
    public void testStateMapping() throws Exception {
        StateSensor.DistinctStates states = new StateSensor.DistinctStates();
        states.addStateMapping("0", "Raining");
        states.addStateMapping("1", "Cloudy");
        states.addStateMapping("2", "Sunny");
        final int SENSOR_ID = 6;
        StateReadCommand readCommand = new StateReadCommand("0", "1", "2");
        StateSensor s1 = new StateSensor("mapped", SENSOR_ID, cache, readCommand, states);
        cache.registerSensor(s1);
        s1.start();
        Assert.assertTrue("Expected 'Raining', got " + getSensorValueFromCache(SENSOR_ID), getSensorValueFromCache(SENSOR_ID).equals("Raining"));
        Assert.assertTrue(s1.getSensorID() == SENSOR_ID);
        Assert.assertTrue(s1.isPolling());
        Assert.assertFalse(s1.isEventListener());
        Assert.assertTrue(s1.getName().equals("mapped"));
        Assert.assertTrue(s1.getProperties().size() == 3);
        Assert.assertTrue(s1.getProperties().keySet().contains("state-1"));
        Assert.assertTrue(s1.getProperties().keySet().contains("state-2"));
        Assert.assertTrue(s1.getProperties().keySet().contains("state-3"));
        Assert.assertTrue(s1.getProperties().values().contains("0"));
        Assert.assertTrue(s1.getProperties().values().contains("1"));
        Assert.assertTrue(s1.getProperties().values().contains("2"));
        readCommand.nextValue();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("Cloudy"));
        readCommand.nextValue();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("Sunny"));
    }

    @Test
    public void testConstructorArgs() {
        Assert.fail("Not Yet Implemented. See ORCJAVA-106 -- http://jira.openremote.org/browse/ORCJAVA-106");
    }

    /**
   * Test sensor behavior when the event producer throws an exception.
   */
    @Test
    public void testBrokenCommand() {
        StateSensor.DistinctStates states = new StateSensor.DistinctStates();
        states.addStateMapping("0", "Raining");
        states.addStateMapping("1", "Cloudy");
        states.addStateMapping("2", "Sunny");
        StateSensor s1 = new StateSensor("broken", 7, cache, new BrokenCommand(), states);
        cache.registerSensor(s1);
        s1.start();
        Assert.assertTrue(cache.queryStatus(7).equals(Sensor.UNKNOWN_STATUS));
        Assert.assertTrue(s1.getSensorID() == 7);
        Assert.assertTrue(s1.isPolling());
        Assert.assertFalse(s1.isEventListener());
        Assert.assertTrue(s1.getName().equals("broken"));
        Assert.assertTrue(s1.getProperties().size() == 3);
        Assert.assertTrue(s1.getProperties().keySet().contains("state-1"));
        Assert.assertTrue(s1.getProperties().keySet().contains("state-2"));
        Assert.assertTrue(s1.getProperties().keySet().contains("state-3"));
        Assert.assertTrue(s1.getProperties().values().contains("0"));
        Assert.assertTrue(s1.getProperties().values().contains("1"));
        Assert.assertTrue(s1.getProperties().values().contains("2"));
    }

    @Test
    public void testMappingToNull() {
        Assert.fail("Not Yet Implemented. See ORCJAVA-108 -- http://jira.openremote.org/browse/ORCJAVA-108");
    }

    /**
   * Test state sensor that has strict state mapping set to false (allowing arbitrary
   * string values to pass through) with no explicit state declaration (so not showing
   * up in sensor properties) <p>
   *
   * Related issue was ORCJAVA-90
   *
   * @throws Exception if test fails
   */
    @Test
    public void testArbitraryStringPassThrough() throws Exception {
        final int SENSOR_ID = 21;
        MixedStateReadCommand readCommand = new MixedStateReadCommand("foo", "bar", "acme");
        StateSensor s1 = new StateSensor("arbitrary string", SENSOR_ID, cache, readCommand, null);
        s1.setStrictStateMapping(false);
        cache.registerSensor(s1);
        s1.start();
        Assert.assertTrue("Expected 'foo', got " + getSensorValueFromCache(SENSOR_ID), getSensorValueFromCache(SENSOR_ID).equals("foo"));
        Assert.assertTrue(s1.getSensorID() == SENSOR_ID);
        Assert.assertTrue(s1.isPolling());
        Assert.assertFalse(s1.isEventListener());
        Assert.assertTrue(s1.getName().equals("arbitrary string"));
        Assert.assertTrue(s1.getProperties().size() == 0);
        readCommand.nextValue();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("bar"));
        readCommand.nextValue();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("acme"));
    }

    /**
   * Test state sensor that has strict state mapping set to false (allowing arbitrary
   * string values to pass through) but has *some* of the states explicitly declared
   * (so they appear in sensor's properties which are available to protocol implementations)
   * and *some* explicitly states declared and **mapped** to another value (so we do the
   * conversion automatically).  <p>
   *
   * Related issue was ORCJAVA-90
   *
   * @throws Exception if test fails
   */
    @Test
    public void testArbitraryStringPassThroughWithMixedMapping() throws Exception {
        final int SENSOR_ID = 21;
        MixedStateReadCommand readCommand = new MixedStateReadCommand("foo", "bar", "acme");
        StateSensor.DistinctStates states = new StateSensor.DistinctStates();
        states.addState("bar");
        states.addStateMapping("acme", "emca");
        StateSensor s1 = new StateSensor("arbitrary string", SENSOR_ID, cache, readCommand, states);
        s1.setStrictStateMapping(false);
        cache.registerSensor(s1);
        s1.start();
        Assert.assertTrue("Expected 'foo', got " + getSensorValueFromCache(SENSOR_ID), getSensorValueFromCache(SENSOR_ID).equals("foo"));
        Assert.assertTrue(s1.getSensorID() == SENSOR_ID);
        Assert.assertTrue(s1.isPolling());
        Assert.assertFalse(s1.isEventListener());
        Assert.assertTrue(s1.getName().equals("arbitrary string"));
        Assert.assertTrue(s1.getProperties().size() == 2);
        Assert.assertTrue(s1.getProperties().keySet().contains("state-1"));
        Assert.assertTrue(s1.getProperties().keySet().contains("state-2"));
        Assert.assertTrue(s1.getProperties().values().contains("bar"));
        Assert.assertTrue(s1.getProperties().values().contains("acme"));
        readCommand.nextValue();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("bar"));
        readCommand.nextValue();
        Assert.assertTrue(getSensorValueFromCache(SENSOR_ID).equals("emca"));
    }

    @Test
    public void testToString() {
        StateSensor.DistinctStates states = new StateSensor.DistinctStates();
        states.addState("foo");
        StateSensor s1 = new StateSensor("single", 1, cache, new StateReadCommand("foo"), states);
        Assert.assertTrue(s1.toString().contains("single"));
        Assert.assertTrue(s1.toString().contains("foo"));
        states = new StateSensor.DistinctStates();
        states.addState("foo");
        states.addState("bar");
        s1 = new StateSensor("twostate", 2, cache, new StateReadCommand("foo", "bar"), states);
        Assert.assertTrue(s1.toString().contains("twostate"));
        Assert.assertTrue(s1.toString().contains("foo"));
        Assert.assertTrue(s1.toString().contains("bar"));
    }

    private String getSensorValueFromCache(int sensorID) throws Exception {
        Thread.sleep(ReadCommand.POLLING_INTERVAL * 2);
        return cache.queryStatus(sensorID);
    }

    private static class StateReadCommand extends ReadCommand {

        protected String[] returnValue;

        protected int index = 0;

        protected StateReadCommand(String... returnValue) {
            this.returnValue = returnValue;
        }

        @Override
        public String read(Sensor s) {
            if (index >= returnValue.length) index = 0;
            Assert.assertTrue(s instanceof StateSensor);
            List<String> vals = Arrays.asList(returnValue);
            for (int i = 1; i <= returnValue.length; ++i) {
                Assert.assertTrue(s.getProperties().keySet().contains("state-" + i));
                String state = s.getProperties().get("state-" + i);
                Assert.assertTrue(vals.contains(state));
            }
            return returnValue[index];
        }

        public void nextValue() {
            index++;
        }
    }

    private static class MixedStateReadCommand extends StateReadCommand {

        MixedStateReadCommand(String... values) {
            super(values);
        }

        @Override
        public String read(Sensor s) {
            if (index >= returnValue.length) index = 0;
            Assert.assertTrue(s instanceof StateSensor);
            return returnValue[index];
        }
    }

    private static class BrokenCommand extends ReadCommand {

        @Override
        public String read(Sensor s) {
            throw new NullPointerException("this should have been handled.");
        }
    }
}
