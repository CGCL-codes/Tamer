package net.sourceforge.basher.events;

import junitx.extensions.EqualsHashCodeTestCase;

/**
 * @author Johan Lindquist
 * @version $Revision$
 */
public class TestTickEvent extends EqualsHashCodeTestCase {

    public TestTickEvent(String name) {
        super(name);
    }

    public void testValue() {
        TickEvent tickEvent = new TickEvent(3);
        assertEquals("bad value", 3, tickEvent.getTick());
    }

    @Override
    protected Object createInstance() throws Exception {
        return new TickEvent(5);
    }

    @Override
    protected Object createNotEqualInstance() throws Exception {
        return new TickEvent(6);
    }
}
