package net.sf.doolin.bus.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import net.sf.doolin.bus.Address;
import net.sf.doolin.bus.Person;
import net.sf.doolin.bus.support.PropertyChangeSupport;
import net.sf.doolin.bus.support.TriggeredSubscriberValidator;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link Bean}.
 * 
 * @author Damien Coraboeuf
 * 
 */
public class TestBean {

    private Person person;

    /**
	 * Creates the bean to test.
	 */
    @Before
    public void setUp() {
        this.person = new Person();
        this.person.setName("Damien");
        Address address = new Address();
        address.setCity("Bruxelles");
        this.person.setAddress(address);
    }

    /**
	 * Test of buffer mode
	 */
    @Test
    public void testBuffered() {
        assertEquals("Damien", this.person.getName());
        assertEquals("Bruxelles", this.person.getAddress().getCity());
        final AtomicReference<String> changes = new AtomicReference<String>();
        PropertyChangeSupport.subscribe(new TriggeredSubscriberValidator(), this.person, "address.city", new Runnable() {

            @Override
            public void run() {
                changes.set(TestBean.this.person.getAddress().getCity());
            }
        });
        this.person.bufferedNotification();
        assertNull(changes.get());
        assertFalse(this.person.isHasBufferedChanges());
        this.person.getAddress().setCity("Orléans");
        assertNull(changes.get());
        assertTrue(this.person.isHasBufferedChanges());
        this.person.getAddress().setCity("Bremen");
        assertNull(changes.get());
        assertTrue(this.person.isHasBufferedChanges());
        this.person.setNotificationMode(NotificationMode.NORMAL);
        assertFalse(this.person.isHasBufferedChanges());
        assertEquals("Bremen", this.person.getAddress().getCity());
        assertEquals("Bremen", changes.get());
    }

    /**
	 * Test of buffer mode
	 */
    @Test
    public void testBufferedCancellation() {
        assertEquals("Damien", this.person.getName());
        assertEquals("Bruxelles", this.person.getAddress().getCity());
        final AtomicReference<String> changes = new AtomicReference<String>();
        PropertyChangeSupport.subscribe(new TriggeredSubscriberValidator(), this.person, "address.city", new Runnable() {

            @Override
            public void run() {
                changes.set(TestBean.this.person.getAddress().getCity());
            }
        });
        this.person.bufferedNotification();
        assertNull(changes.get());
        assertFalse(this.person.isHasBufferedChanges());
        this.person.getAddress().setCity("Orléans");
        assertNull(changes.get());
        assertTrue(this.person.isHasBufferedChanges());
        this.person.getAddress().setCity("Bremen");
        assertNull(changes.get());
        assertTrue(this.person.isHasBufferedChanges());
        this.person.cancelBuffer();
        assertEquals("Bruxelles", changes.get());
        assertEquals("Bruxelles", this.person.getAddress().getCity());
        assertFalse(this.person.isHasBufferedChanges());
        this.person.getAddress().setCity("London");
        assertEquals("London", this.person.getAddress().getCity());
        assertEquals("London", changes.get());
        assertFalse(this.person.isHasBufferedChanges());
    }

    /**
	 * Test of change detection in buffer mode
	 */
    @Test
    public void testChangeDetection() {
        assertEquals("Damien", this.person.getName());
        assertEquals("Bruxelles", this.person.getAddress().getCity());
        final AtomicBoolean hasBufferedChanges = new AtomicBoolean(false);
        PropertyChangeSupport.subscribe(new TriggeredSubscriberValidator(), this.person, BeanNotificationSupport.HAS_BUFFERED_CHANGES, new Runnable() {

            @Override
            public void run() {
                hasBufferedChanges.set(TestBean.this.person.isHasBufferedChanges());
            }
        });
        this.person.bufferedNotification();
        assertFalse(hasBufferedChanges.get());
        this.person.getAddress().setCity("Orléans");
        assertTrue(hasBufferedChanges.get());
        this.person.getAddress().setCity("Bremen");
        assertTrue(hasBufferedChanges.get());
        this.person.setNotificationMode(NotificationMode.NORMAL);
        assertFalse(hasBufferedChanges.get());
    }
}
