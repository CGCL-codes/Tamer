package net.sourceforge.basher.events;

import junitx.extensions.EqualsHashCodeTestCase;
import net.sourceforge.basher.BasherContext;

/**
 * @author Johan Lindquist
 * @version $Revision$
 */
public class TestCollectionStoppedEvent extends EqualsHashCodeTestCase {

    private BasherContext _basherContext1;

    private BasherContext _basherContext2;

    @Override
    protected void setUp() throws Exception {
        _basherContext1 = new BasherContext();
        _basherContext1.setName("1");
        _basherContext2 = new BasherContext();
        _basherContext2.setName("2");
        super.setUp();
    }

    public TestCollectionStoppedEvent(String name) {
        super(name);
    }

    public void testEqualsHashCode() {
        CollectionStoppedEvent collectionStoppedEvent1 = new CollectionStoppedEvent(_basherContext1);
        CollectionStoppedEvent collectionStoppedEvent2 = new CollectionStoppedEvent(_basherContext2);
        assertEquals("bad equals", collectionStoppedEvent1, collectionStoppedEvent1);
        assertEquals("bad hashcode", collectionStoppedEvent1.hashCode(), collectionStoppedEvent1.hashCode());
        assertNotSame("bad equals", collectionStoppedEvent1, collectionStoppedEvent2);
        assertNotSame("bad hashcode", collectionStoppedEvent2.hashCode(), collectionStoppedEvent2.hashCode());
    }

    @Override
    protected Object createInstance() throws Exception {
        CollectionStoppedEvent collectionStoppedEvent = new CollectionStoppedEvent(_basherContext1);
        collectionStoppedEvent.setCreationTime(1);
        return collectionStoppedEvent;
    }

    @Override
    protected Object createNotEqualInstance() throws Exception {
        CollectionStoppedEvent collectionStoppedEvent = new CollectionStoppedEvent(_basherContext2);
        collectionStoppedEvent.setCreationTime(2);
        return collectionStoppedEvent;
    }
}
