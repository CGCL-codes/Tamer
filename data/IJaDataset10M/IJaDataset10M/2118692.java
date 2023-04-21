package com.googlecode.objectify.test;

import java.util.Iterator;
import java.util.logging.Logger;
import org.testng.annotations.Test;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.KeyRange;
import com.googlecode.objectify.test.entity.Child;
import com.googlecode.objectify.test.entity.Criminal;
import com.googlecode.objectify.test.entity.Trivial;
import com.googlecode.objectify.test.util.TestBase;

/**
 * Tests of simple key allocations
 * 
 * @author Jeff Schnitzer <jeff@infohazard.org>
 */
public class AllocateTests extends TestBase {

    /** */
    private static Logger log = Logger.getLogger(AllocateTests.class.getName());

    /** */
    @Test
    public void testBasicAllocation() throws Exception {
        fact.register(Trivial.class);
        KeyRange<Trivial> range = this.fact.allocateIds(Trivial.class, 5);
        Iterator<Key<Trivial>> it = range.iterator();
        long previousId = 0;
        for (int i = 0; i < 5; i++) {
            Key<Trivial> next = it.next();
            assert next.getId() > previousId;
            previousId = next.getId();
        }
        Trivial triv = new Trivial("foo", 3);
        this.fact.begin().save().entity(triv).now();
        assert triv.getId() > previousId;
    }

    /** */
    @Test
    public void testParentAllocation() throws Exception {
        fact.register(Trivial.class);
        fact.register(Child.class);
        Key<Trivial> parentKey = Key.create(Trivial.class, 123);
        KeyRange<Child> range = this.fact.allocateIds(parentKey, Child.class, 5);
        Iterator<Key<Child>> it = range.iterator();
        long previousId = 0;
        for (int i = 0; i < 5; i++) {
            Key<Child> next = it.next();
            assert next.getId() > previousId;
            previousId = next.getId();
        }
        Child ch = new Child(parentKey, "foo");
        this.fact.begin().save().entity(ch).now();
        assert ch.getId() > previousId;
    }

    /** */
    @Test
    public void testKindNamespaceAllocation() throws Exception {
        fact.register(Trivial.class);
        fact.register(Criminal.class);
        KeyRange<Trivial> rangeTrivial = this.fact.allocateIds(Trivial.class, 1);
        KeyRange<Criminal> rangeCriminal = this.fact.allocateIds(Criminal.class, 1);
        Iterator<Key<Trivial>> itTrivial = rangeTrivial.iterator();
        Key<Trivial> trivialKey = itTrivial.next();
        Iterator<Key<Criminal>> itCriminal = rangeCriminal.iterator();
        Key<Criminal> criminalKey = itCriminal.next();
        log.warning("Trivial key is " + trivialKey);
        log.warning("Criminal key is " + criminalKey);
    }
}
