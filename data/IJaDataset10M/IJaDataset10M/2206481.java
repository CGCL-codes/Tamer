package org.beandb.derived;

import static org.junit.Assert.fail;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.beandb.Database;
import org.beandb.Domain;
import org.beandb.MutableRelation;
import org.beandb.Relation;
import org.beandb.Tuple;
import org.beandb.exceptions.BackendException;
import org.beandb.memory.InMemoryDatabase;
import org.beandb.testmodel.TestInterface;
import org.beandb.testmodel.TestSubInterface;
import org.junit.Test;

public class PermutationTest {

    @Test
    public void checkSimplePermutation() throws BackendException {
        Database db = new InMemoryDatabase();
        Domain<TestInterface> domainA = db.createDomain(TestInterface.class);
        TestInterface obj1 = domainA.createNewDatabaseObject();
        TestInterface obj2 = domainA.createNewDatabaseObject();
        TestInterface obj3 = domainA.createNewDatabaseObject();
        TestInterface obj4 = domainA.createNewDatabaseObject();
        TestInterface obj5 = domainA.createNewDatabaseObject();
        obj1.setPrimitiveIntegerProperty(1);
        obj2.setPrimitiveIntegerProperty(2);
        obj3.setPrimitiveIntegerProperty(3);
        obj4.setPrimitiveIntegerProperty(4);
        obj5.setPrimitiveIntegerProperty(5);
        MutableRelation rel = db.createStoredRelation("rel1", domainA, domainA, domainA);
        rel.insert(obj1, obj2, obj3);
        rel.insert(obj2, obj3, obj4);
        rel.insert(obj3, obj4, obj5);
        rel.insert(obj4, obj5, obj1);
        rel.insert(obj5, obj1, obj2);
        try {
            rel.permute(new int[] {});
            fail("Method should have thrown exception (wrong size of permutation)");
        } catch (IllegalArgumentException e) {
        }
        try {
            rel.permute(new int[] { 1, 2 });
            fail("Method should have thrown exception (wrong size of permutation)");
        } catch (IllegalArgumentException e) {
        }
        try {
            rel.permute(new int[] { 1, 2, 3, 4 });
            fail("Method should have thrown exception (wrong size of permutation)");
        } catch (IllegalArgumentException e) {
        }
        try {
            rel.permute(new int[] { 0, 1, 1 });
            fail("Method should have thrown exception (duplicate)");
        } catch (IllegalArgumentException e) {
        }
        try {
            rel.permute(new int[] { 0, 1, 3 });
            fail("Method should have thrown exception (out of range)");
        } catch (IllegalArgumentException e) {
        }
        Relation permRel = rel.permute(new int[] { 2, 1, 0 });
        assert permRel.getArity() == 3;
        MutableRelation expectedPermRel = db.createStoredRelation("expected", domainA, domainA, domainA);
        expectedPermRel.insert(obj3, obj2, obj1);
        expectedPermRel.insert(obj4, obj3, obj2);
        expectedPermRel.insert(obj5, obj4, obj3);
        expectedPermRel.insert(obj1, obj5, obj4);
        expectedPermRel.insert(obj2, obj1, obj5);
        for (Tuple tuple : expectedPermRel) {
            assert permRel.contains(tuple.toArray());
        }
        assert !permRel.contains(obj2, obj2, obj2);
        assert !permRel.contains(obj1, obj2, obj3);
        for (Tuple tuple : rel) {
            assert !permRel.contains(tuple);
            assert !permRel.contains(tuple.toArray());
        }
        Set<Tuple> expectedSet = new HashSet<Tuple>(expectedPermRel.toSet());
        for (Tuple tuple : permRel) {
            expectedSet.remove(tuple);
        }
        assert expectedSet.isEmpty();
    }

    @Test
    public void checkSignatureChange() throws BackendException {
        Database db = new InMemoryDatabase();
        Domain<TestInterface> domainA = db.createDomain(TestInterface.class);
        TestInterface obj1 = domainA.createNewDatabaseObject();
        TestInterface obj2 = domainA.createNewDatabaseObject();
        TestInterface obj3 = domainA.createNewDatabaseObject();
        Domain<TestSubInterface> domainB = db.createDomain(TestSubInterface.class);
        TestSubInterface obj4 = domainB.createNewDatabaseObject();
        TestSubInterface obj5 = domainB.createNewDatabaseObject();
        obj1.setPrimitiveIntegerProperty(1);
        obj2.setPrimitiveIntegerProperty(2);
        obj3.setPrimitiveIntegerProperty(3);
        obj4.setPrimitiveIntegerProperty(4);
        obj5.setPrimitiveIntegerProperty(5);
        MutableRelation rel = db.createStoredRelation("rel1", domainA, domainA, domainB, domainA, domainB);
        rel.insert(obj1, obj3, obj4, obj2, obj5);
        rel.insert(obj2, obj1, obj5, obj3, obj5);
        rel.insert(obj3, obj2, obj5, obj1, obj4);
        rel.insert(obj2, obj2, obj4, obj1, obj4);
        Relation permRel = rel.permute(new int[] { 2, 1, 0, 4, 3 });
        assert permRel.getArity() == 5;
        assert Arrays.equals(permRel.getSignature(), new Domain[] { domainB, domainA, domainA, domainB, domainA });
        permRel = rel.permute(new int[] { 0, 1, 2, 3, 4 });
        assert permRel.getArity() == 5;
        assert Arrays.equals(permRel.getSignature(), new Domain[] { domainA, domainA, domainB, domainA, domainB });
        permRel = rel.permute(new int[] { 4, 3, 2, 1, 0 });
        assert permRel.getArity() == 5;
        assert Arrays.equals(permRel.getSignature(), new Domain[] { domainB, domainA, domainB, domainA, domainA });
    }
}
