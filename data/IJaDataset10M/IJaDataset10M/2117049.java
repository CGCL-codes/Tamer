package net.sf.josceleton.core.api.async;

import net.sf.josceleton.commons.test.jmock.AbstractMockeryTest;
import org.testng.annotations.Test;

public abstract class AsyncForTest extends AbstractMockeryTest {

    protected abstract <K, L extends Listener> AsyncFor<K, L> createTestee(Class<K> keyType, Class<L> listenerType);

    protected abstract <K, L extends Listener> void assertListenersCount(AsyncFor<K, L> asyncFor, K key, int expectedCount);

    @Test
    public final void twoDifferent() {
        final String key1 = "foo";
        final String key2 = "bar";
        final Listener listener1 = this.mock(Listener.class, "listener1");
        final Listener listener2 = this.mock(Listener.class, "listener2");
        final AsyncFor<String, Listener> testee = this.createTestee(String.class, Listener.class);
        this.assertListenersCount(testee, key1, 0);
        this.assertListenersCount(testee, key2, 0);
        testee.addListenerFor(key1, listener1);
        this.assertListenersCount(testee, key1, 1);
        testee.addListenerFor(key1, listener2);
        this.assertListenersCount(testee, key1, 2);
        this.assertListenersCount(testee, key2, 0);
        testee.addListenerFor(key2, listener2);
        this.assertListenersCount(testee, key1, 2);
        this.assertListenersCount(testee, key2, 1);
        testee.removeListenerFor(key1, listener2);
        this.assertListenersCount(testee, key1, 1);
        this.assertListenersCount(testee, key2, 1);
        testee.addListenerFor(key1, listener1);
        testee.addListenerFor(key1, listener1);
        this.assertListenersCount(testee, key1, 1);
        this.assertListenersCount(testee, key2, 1);
    }

    @Test
    public final void repeatedInvocationsWithSameKeyAndListener() {
        final String key = "asdf";
        final Listener listener = this.mock(Listener.class);
        final AsyncFor<String, Listener> testee = this.createTestee(String.class, Listener.class);
        this.assertListenersCount(testee, key, 0);
        testee.addListenerFor(key, listener);
        this.assertListenersCount(testee, key, 1);
        testee.addListenerFor(key, listener);
        this.assertListenersCount(testee, key, 1);
        testee.removeListenerFor(key, listener);
        this.assertListenersCount(testee, key, 0);
        testee.removeListenerFor(key, listener);
        this.assertListenersCount(testee, key, 0);
    }

    @Test
    public final void removeThoughNothingWasAddedYet() {
        final Object key = new Object();
        final AsyncFor<Object, Listener> testee = this.createTestee(Object.class, Listener.class);
        testee.removeListenerFor(key, this.mock(Listener.class, "listener1"));
        testee.removeListenerFor(key, this.mock(Listener.class, "listener2"));
        assertListenersCount(testee, key, 0);
    }
}
