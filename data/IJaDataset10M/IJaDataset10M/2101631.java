package org.jcyclone.core.internal;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.jcyclone.core.cfg.JCycloneConfig;
import org.jcyclone.core.cfg.MapConfig;
import java.util.Properties;
import java.util.HashSet;
import java.util.Arrays;

/**
 * Tests the correctness of the {@link TPSSchedulerConcurrent} scheduling algorithm.
 * This is less of a unit test and more of a full-on integration test.
 * Tries to setup producer/consumer scenarios of various complexity to make sure
 * that schedule works and there's no starvation.
 *
 * @author toli
 * @version $Id: TPSSchedulerConcurrentTest.java,v 1.1 2006/09/27 01:37:25 tolikuznets Exp $
 */
public class TPSSchedulerConcurrentTest extends SchedulerTestBase {

    public static final String CONSUMER_CLASS = "org.jcyclone.core.internal.Consumer";

    public static final String PRODUCER_CLASS = "org.jcyclone.core.internal.Producer";

    public TPSSchedulerConcurrentTest(String inName) {
        super(inName);
    }

    public static Test suite() {
        return new TestSuite(TPSSchedulerConcurrentTest.class);
    }

    /** Helper function to be overridden by subclsses to substitute different thread schedulers but run the same tests */
    protected void setThreadManager(Properties inProps) {
        inProps.setProperty("global.defaultThreadManager", JCycloneConfig.THREADMGR_TPSTM_CONCURRENT);
    }

    /** test one producer and one consumer */
    public void test1x1() throws Exception {
        Properties props = new Properties();
        setThreadManager(props);
        props.setProperty("stages.producer.class", getProducerClass());
        props.setProperty("stages.producer.initargs." + NEXT_STAGE, "consumer");
        props.setProperty("stages.producer.initargs." + WAIT_INTERFVAL, "4");
        props.setProperty("stages.producer.initargs." + TO_SEND, "abc");
        props.setProperty("stages.consumer.class", getConsumerClass());
        final MapConfig mc = generateMapConfig(props);
        runTest(mc, 1);
        assertEquals(messages.get(0).toString(), "abc", messages.get(0).getData());
        props.setProperty("stages.producer.initargs." + TO_SEND, "vasya" + MSG_SEPARATOR + "pupkin");
        runTest(generateMapConfig(props), 2);
        assertEquals(messages.get(0).toString(), "vasya", messages.get(0).getData());
        assertEquals(messages.get(1).toString(), "pupkin", messages.get(1).getData());
    }

    /** test one producer and one consumer but producer sends many messages
     * Test that args are in the right order as being sent.
     * */
    public void test1x1_manyMessages() throws Exception {
        Properties props = new Properties();
        setThreadManager(props);
        props.setProperty("stages.producer.class", getProducerClass());
        props.setProperty("stages.producer.initargs." + NEXT_STAGE, "consumer");
        props.setProperty("stages.producer.initargs." + WAIT_INTERFVAL, "2");
        props.setProperty("stages.consumer.class", getConsumerClass());
        String toSendStr = createStringToSend(20);
        props.setProperty("stages.producer.initargs." + TO_SEND, toSendStr);
        final MapConfig mc = generateMapConfig(props);
        String[] expected = toSendStr.split(MSG_SEPARATOR);
        runTest(mc, expected.length);
        for (int i = 0; i < messages.size(); i++) {
            assertEquals("event[" + i + "]: " + messages.get(i), expected[i], messages.get(i).getData());
        }
    }

    /** Let's have 10 producers and 1 consumer, and each producer sends a different chunk of messages */
    public void testManyP_oneC() throws Exception {
        Properties props = new Properties();
        setThreadManager(props);
        int nProducers = 5;
        int totalMsgs = 0;
        HashSet<String> allExpectedMessages = new HashSet<String>();
        String[][] expectedMessages = new String[nProducers][10];
        for (int i = 0; i < nProducers; i++) {
            props.setProperty("stages.producer-" + i + ".class", getProducerClass());
            props.setProperty("stages.producer-" + i + ".initargs." + NEXT_STAGE, "consumer");
            props.setProperty("stages.producer-" + i + ".initargs." + WAIT_INTERFVAL, "3");
            String toSendStr = createStringToSend(expectedMessages[i].length);
            expectedMessages[i] = toSendStr.split(MSG_SEPARATOR);
            allExpectedMessages.addAll(Arrays.asList(expectedMessages[i]));
            totalMsgs += expectedMessages[i].length;
            props.setProperty("stages.producer-" + i + ".initargs." + TO_SEND, toSendStr);
            props.setProperty("stages.consumer.class", getConsumerClass());
        }
        final MapConfig mc = generateMapConfig(props);
        runTest(mc, totalMsgs);
        for (ThreadSampleEvent event : messages) {
            allExpectedMessages.remove(event.getData());
        }
        assertEquals("there were some leftover events that were unmatched: " + getStringFromEventArr(allExpectedMessages.toArray(new String[0])), 0, allExpectedMessages.size());
    }

    /** Setup one producer to send events to many consumers.
     * We expect all the events to arrive in order
     * @throws Exception
     */
    public void testOneP_ManyC() throws Exception {
        Properties props = new Properties();
        setThreadManager(props);
        props.setProperty("stages.producer.class", getProducerClass());
        props.setProperty("stages.producer.initargs." + WAIT_INTERFVAL, "2");
        String toSendStr = createStringToSend(20);
        props.setProperty("stages.producer.initargs." + TO_SEND, toSendStr);
        int nConsumers = 5;
        String stageNamesString = "";
        for (int i = 0; i < nConsumers; i++) {
            props.setProperty("stages.consumer-" + i + ".class", getConsumerClass());
            stageNamesString += "consumer-" + i + ",";
        }
        props.setProperty("stages.producer.initargs." + NEXT_STAGE, stageNamesString);
        final MapConfig mc = generateMapConfig(props);
        final String[] expected = toSendStr.split(MSG_SEPARATOR);
        runTest(mc, expected.length);
        for (int i = 0; i < messages.size(); i++) {
            assertEquals("event[" + i + "]: " + messages.get(i), expected[i], messages.get(i).getData());
        }
    }

    /** Let's have multiple producers and multiple consumers:
     * p1 - c1
     * p2 - c2, c3
     * p3 - c3, c4, c5, c6
     * p4 - c7, c8, c9, c10, c11, c12
     * p5 - C3
     *
     * Each producer can send a random # of events
     *
     * NOTE: all arrays here are 1-based, it's easier with debugging and naming
     *
     * @throws Exception
     */
    public void testMany_Many() throws Exception {
        Properties props = new Properties();
        setThreadManager(props);
        int nProducers = 5;
        int nConsumers = 12;
        int totalMsgs = 0;
        String[][] expectedMessages = new String[nProducers + 1][];
        String[] toSendStr = new String[nProducers + 1];
        HashSet<String> allExpectedMessages = new HashSet<String>();
        for (int i = 1; i <= nProducers; i++) {
            toSendStr[i] = createStringToSend(sRandom.nextInt(25) + 1);
            expectedMessages[i] = toSendStr[i].split(MSG_SEPARATOR);
            totalMsgs += expectedMessages[i].length;
            allExpectedMessages.addAll(Arrays.asList(expectedMessages[i]));
            props.setProperty("stages.producer-" + i + ".class", getProducerClass());
            props.setProperty("stages.producer-" + i + ".initargs." + WAIT_INTERFVAL, "3");
            props.setProperty("stages.producer-" + i + ".initargs." + TO_SEND, toSendStr[i]);
        }
        props.setProperty("stages.producer-1.initargs." + NEXT_STAGE, "consumer-1");
        props.setProperty("stages.producer-2.initargs." + NEXT_STAGE, "consumer-2,consumer-3");
        props.setProperty("stages.producer-3.initargs." + NEXT_STAGE, "consumer-3,consumer-4,consumer-5,consumer-6");
        props.setProperty("stages.producer-4.initargs." + NEXT_STAGE, "consumer-7,consumer-8,consumer-9,consumer-10,consumer-11,consumer-12");
        props.setProperty("stages.producer-5.initargs." + NEXT_STAGE, "consumer-3");
        for (int i = 1; i <= nConsumers; i++) {
            props.setProperty("stages.consumer-" + i + ".class", getConsumerClass());
        }
        System.out.println("Properties are: " + props.toString());
        final MapConfig mc = generateMapConfig(props);
        runTest(mc, totalMsgs);
        for (ThreadSampleEvent event : messages) {
            allExpectedMessages.remove(event.getData());
        }
        assertEquals("there were some leftover events that were unmatched: " + getStringFromEventArr(allExpectedMessages.toArray(new String[0])), 0, allExpectedMessages.size());
        int expectedSum = 0;
        for (int i = 1; i <= nProducers; i++) {
            for (int j = 0; j < expectedMessages[i].length; j++) {
                expectedSum += Integer.parseInt(expectedMessages[i][j]);
            }
        }
        int collectedSum = 0;
        for (ThreadSampleEvent event : messages) {
            collectedSum += Integer.parseInt(event.getData());
        }
        assertEquals("total sum of values sent and received does not add up", expectedSum, collectedSum);
    }

    public void testCreateStringToSend() throws Exception {
        String oneEvent = createStringToSend(1);
        assertTrue(oneEvent.length() > 0);
        assertEquals(1, oneEvent.split(MSG_SEPARATOR).length);
        assertNotNull(oneEvent.split(MSG_SEPARATOR)[0]);
        assertFalse("".equals(oneEvent.split(MSG_SEPARATOR)[0]));
        String manyEvent = createStringToSend(10);
        assertTrue(manyEvent.length() > 0);
        assertEquals(10, manyEvent.split(MSG_SEPARATOR).length);
        assertNotNull(manyEvent.split(MSG_SEPARATOR)[0]);
        assertFalse("".equals(manyEvent.split(MSG_SEPARATOR)[0]));
        assertNotNull(manyEvent.split(MSG_SEPARATOR)[9]);
        assertFalse("".equals(manyEvent.split(MSG_SEPARATOR)[9]));
    }

    /** Creates a string separated with {@link #MSG_SEPARATOR} number of events.
     * Each event is a random numer [0,99]
     * @param nEvents
     * @return string of random numbers
     */
    private String createStringToSend(int nEvents) {
        if (nEvents == 0) fail("need to have at least one event to send");
        String toSendStr = "";
        for (int i = 0; i < nEvents; i++) {
            toSendStr += sRandom.nextInt(100) + MSG_SEPARATOR;
        }
        return toSendStr;
    }

    public static String getStringFromEventArr(String[] inArr) {
        StringBuffer buf = new StringBuffer(1024);
        for (String event : inArr) {
            buf.append(event);
            buf.append(",");
        }
        return buf.toString();
    }

    /** Subclasses should override if they want to substitute differnet kinds of consumer */
    protected String getConsumerClass() {
        return CONSUMER_CLASS;
    }

    /** Subclasses should override if they want to substitute differnet kinds of producer */
    protected String getProducerClass() {
        return PRODUCER_CLASS;
    }
}
