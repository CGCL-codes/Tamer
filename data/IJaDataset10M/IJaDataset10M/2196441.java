package vqwiki;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.util.ReflectionUtils;
import junit.framework.TestCase;
import vqwiki.persistence.PersistenceHandler;
import vqwiki.utils.Clock;
import vqwiki.utils.SystemTime;

/**
 * A base testcase for PersistenceHandler
 *
 * @author bossola
 */
public abstract class AbstractHandlerTest extends TestCase {

    protected static class Lock {

        private String key;

        private long at;

        public Lock(String key, long at) {
            this.key = key;
            this.at = at;
        }

        public boolean equals(Object o) {
            boolean eq = false;
            try {
                Lock other = (Lock) o;
                eq = key.equals(other.key);
                eq = at == 0 || other.at == 0 || at == other.at;
            } catch (Exception ex) {
            }
            return eq;
        }
    }

    protected static final String MY_WIKI = Constants.DEFAULT_VWIKI;

    protected static final String MY_TOPIC = "WhatYouSeeIsWhatYouGet";

    protected static final String MY_CONTENTS = "Hello, world!";

    protected static final String NEW_TOPIC_NAME = "NewTopicName";

    protected PersistenceHandler persistenceHandler;

    protected long now;

    protected Environment env;

    /**
    * Standard constructor
    *
    * @param name the name of this test
    */
    public AbstractHandlerTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        SystemTime.setClock(new Clock() {

            public long now() {
                if (now == 0) {
                    return System.currentTimeMillis();
                } else {
                    return now;
                }
            }
        });
        Field environmentInstanceField = ReflectionUtils.findField(Environment.class, "instance");
        environmentInstanceField.setAccessible(true);
        ReflectionUtils.setField(environmentInstanceField, null, null);
        env = Environment.getInstance();
        env.setSetting(Environment.PROPERTY_VERSIONING_ON, "false");
        env.setSetting(Environment.PROPERTY_EDIT_TIMEOUT, "10");
        persistenceHandler = createHandler();
    }

    public void tearDown() throws Exception {
        super.tearDown();
        SystemTime.reset();
    }

    public final void testExistWhenTopicNotPresent() throws Exception {
        assertFalse(persistenceHandler.exists(MY_WIKI, MY_TOPIC));
    }

    public void testExistWhenTopicPresent() throws Exception {
        writeContents(MY_WIKI, MY_TOPIC, MY_CONTENTS);
        assertTrue(persistenceHandler.exists(MY_WIKI, MY_TOPIC));
    }

    public void testWriteNewTopic() throws Exception {
        persistenceHandler.write(MY_WIKI, MY_CONTENTS, false, MY_TOPIC);
        assertTopicPresent(MY_WIKI, MY_TOPIC, MY_CONTENTS);
    }

    public void testWriteExistingTopic() throws Exception {
        persistenceHandler.write(MY_WIKI, "12345", false, MY_TOPIC);
        persistenceHandler.write(MY_WIKI, MY_CONTENTS, false, MY_TOPIC);
        assertTopicPresent(MY_WIKI, MY_TOPIC, MY_CONTENTS);
    }

    public void testReadTopic() throws Exception {
        writeContents(MY_WIKI, MY_TOPIC, MY_CONTENTS);
        assertEquals(MY_CONTENTS, readContents(MY_WIKI, MY_TOPIC));
    }

    public void testWriteWhenVersioningOn() throws Exception {
        env.setSetting(Environment.PROPERTY_VERSIONING_ON, "true");
        final long timeV1 = setCurrentTime(toMillis("19-08-2008 17:00:00"));
        final String contentsV1 = MY_CONTENTS + "V1";
        persistenceHandler.write(MY_WIKI, contentsV1, false, MY_TOPIC);
        final long timeV2 = setCurrentTime(toMillis("19-08-2008 17:00:05"));
        final String contentsV2 = MY_CONTENTS + "V2";
        persistenceHandler.write(MY_WIKI, contentsV2, false, MY_TOPIC);
        assertVersionPresent(MY_WIKI, MY_TOPIC, timeV1, contentsV1);
        assertVersionPresent(MY_WIKI, MY_TOPIC, timeV2, contentsV2);
        assertTopicPresent(MY_WIKI, MY_TOPIC, contentsV2);
    }

    public void testTopicUnlocked() throws Exception {
        writeContents(MY_WIKI, MY_TOPIC, MY_CONTENTS);
        assertLockEquals(MY_WIKI, MY_TOPIC, null);
    }

    public void testLockTopicWhenUnlocked() throws Exception {
        final long at = setCurrentTime(toMillis("19-08-2008 17:00:00"));
        writeContents(MY_WIKI, MY_TOPIC, MY_CONTENTS);
        persistenceHandler.lockTopic(MY_WIKI, MY_TOPIC, "123");
        assertLockEquals(MY_WIKI, MY_TOPIC, new Lock("123", at));
    }

    public void testLockTopicWhenLockedUsingOldKeyImmediately() throws Exception {
        writeContents(MY_WIKI, MY_TOPIC, MY_CONTENTS);
        persistenceHandler.lockTopic(MY_WIKI, MY_TOPIC, "123");
        simulateTimeElapsed(Environment.getInstance().getEditTimeout() / 2);
        assertTrue(persistenceHandler.lockTopic(MY_WIKI, MY_TOPIC, "123"));
    }

    public void testLockTopicWhenLockedUsingNewKeyImmediately() throws Exception {
        writeContents(MY_WIKI, MY_TOPIC, MY_CONTENTS);
        persistenceHandler.lockTopic(MY_WIKI, MY_TOPIC, "123");
        simulateTimeElapsed(Environment.getInstance().getEditTimeout() / 2);
        assertFalse(persistenceHandler.lockTopic(MY_WIKI, MY_TOPIC, "999"));
    }

    public void testLockTopicWhenLockedUsingNewKeyAndTimeoutExpired() throws Exception {
        setCurrentTime(System.currentTimeMillis());
        writeContents(MY_WIKI, MY_TOPIC, MY_CONTENTS);
        persistenceHandler.lockTopic(MY_WIKI, MY_TOPIC, "123");
        simulateTimeElapsed(Environment.getInstance().getEditTimeout() * 2);
        assertTrue(persistenceHandler.lockTopic(MY_WIKI, MY_TOPIC, "999"));
    }

    public void testUnlockTopic() throws Exception {
        writeContents(MY_WIKI, MY_TOPIC, MY_CONTENTS);
        persistenceHandler.lockTopic(MY_WIKI, MY_TOPIC, "123");
        persistenceHandler.unlockTopic(MY_WIKI, MY_TOPIC);
        assertLockEquals(MY_WIKI, MY_TOPIC, null);
    }

    public void testRenameTopic() throws Exception {
        writeContents(MY_WIKI, MY_TOPIC, MY_CONTENTS);
        persistenceHandler.rename(MY_WIKI, MY_TOPIC, NEW_TOPIC_NAME);
        assertTopicPresent(MY_WIKI, NEW_TOPIC_NAME, MY_CONTENTS);
    }

    public void testRenameTopicWhenMissing() throws Exception {
        try {
            persistenceHandler.rename(MY_WIKI, MY_TOPIC, NEW_TOPIC_NAME);
            fail("An exception was expected!");
        } catch (Exception thisWasExpected) {
        }
    }

    public void testLocksRemovedOnRenameTopic() throws Exception {
        writeContents(MY_WIKI, MY_TOPIC, MY_CONTENTS);
        persistenceHandler.lockTopic(MY_WIKI, MY_TOPIC, randomKey());
        persistenceHandler.rename(MY_WIKI, MY_TOPIC, NEW_TOPIC_NAME);
        assertLockEquals(MY_WIKI, MY_TOPIC, null);
        assertLockEquals(MY_WIKI, NEW_TOPIC_NAME, null);
    }

    public void testHistoryContentsOnRenameTopic() throws Exception {
        env.setSetting(Environment.PROPERTY_VERSIONING_ON, "true");
        final long historyTime = setCurrentTime(toMillis("19-08-2008 17:00:00"));
        final String historyContents = MY_CONTENTS + "history";
        persistenceHandler.write(MY_WIKI, historyContents, false, MY_TOPIC);
        final long actualTime = setCurrentTime(toMillis("19-08-2008 17:00:10"));
        final String actualContents = MY_CONTENTS + "actual";
        persistenceHandler.write(MY_WIKI, actualContents, false, MY_TOPIC);
        persistenceHandler.rename(MY_WIKI, MY_TOPIC, NEW_TOPIC_NAME);
        assertVersionPresent(MY_WIKI, NEW_TOPIC_NAME, historyTime, historyContents);
        assertVersionPresent(MY_WIKI, NEW_TOPIC_NAME, actualTime, actualContents);
    }

    protected PersistenceHandler createHandler() throws Exception {
        WikiBase wb = WikiBase.getInstance();
        wb.init(Environment.getInstance());
        return wb.getPersistenceHandler();
    }

    protected abstract void writeContents(String wiki, String topicName, String contents) throws Exception;

    protected abstract String readContents(String wiki, String topicName) throws Exception;

    protected abstract String readVersionedContents(String wiki, String topicName, long at) throws Exception;

    protected abstract Lock readLock(String wiki, String topicName) throws Exception;

    protected void assertLockEquals(String wiki, String topicName, Lock lock) throws Exception {
        assertEquals(lock, readLock(wiki, topicName));
    }

    protected void assertVersionPresent(String wiki, String topicName, long timeOf, String contentsOf) throws Exception {
        assertEquals(contentsOf, readVersionedContents(wiki, topicName, timeOf));
    }

    protected void assertTopicPresent(String wiki, String topicName, String topicContents) throws Exception {
        assertEquals(topicContents, readContents(wiki, topicName));
    }

    protected long simulateTimeElapsed(int secs) {
        now = SystemTime.asMillis() + 60000L * secs;
        return now;
    }

    protected long setCurrentTime(long millis) throws ParseException {
        now = millis;
        return now;
    }

    protected long toMillis(String time) throws ParseException {
        Date date = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(time);
        return date.getTime();
    }

    protected String randomKey() {
        return Double.toString(Math.random() * 100000000.0);
    }
}
