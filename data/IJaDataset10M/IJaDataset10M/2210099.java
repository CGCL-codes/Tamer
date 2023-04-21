package ru.amse.soultakov.ereditor.model;

import static org.junit.Assert.*;
import org.junit.Test;
import static ru.amse.soultakov.ereditor.model.RelationshipMultiplicity.*;

public class RelationshipTest {

    @Test
    public void testRelationship() {
        Entity entity1 = new Entity("1");
        Entity entity2 = new Entity("2");
        Relationship relationship = new Relationship(new PKRelationshipEnd(entity1, ZERO_OR_MORE, ""), new PKRelationshipEnd(entity2, ZERO_OR_MORE, ""));
        assertFalse(EntityTest.hasRelationship(entity1, relationship));
        assertFalse(EntityTest.hasRelationship(entity2, relationship));
    }

    @Test
    public void testGetFirstEnd() {
        Entity entity1 = new Entity("1");
        Entity entity2 = new Entity("2");
        Relationship relationship = new Relationship(new PKRelationshipEnd(entity1, ZERO_OR_MORE, ""), new PKRelationshipEnd(entity2, ZERO_OR_MORE, ""));
        assertSame(entity1, relationship.getFirstEnd().getEntity());
        assertSame(entity2, relationship.getSecondEnd().getEntity());
    }

    @Test
    public void testSetFirstEnd() {
        Relationship relationship = new Relationship(new PKRelationshipEnd(new Entity(""), ZERO_OR_MORE, null), new PKRelationshipEnd(new Entity(""), ZERO_OR_MORE, null));
        Entity entity1 = new Entity("1");
        Entity entity2 = new Entity("2");
        relationship.getFirstEnd().setEntity(entity1);
        relationship.getSecondEnd().setEntity(entity2);
        assertSame(entity1, relationship.getFirstEnd().getEntity());
        assertSame(entity2, relationship.getSecondEnd().getEntity());
    }

    @Test
    public void testEqualsObject() {
        Entity entity1 = new Entity("1");
        Entity entity2 = new Entity("2");
        Relationship relationship1 = new Relationship(new PKRelationshipEnd(entity1, ZERO_OR_MORE, null), new PKRelationshipEnd(entity2, ZERO_OR_MORE, null));
        Relationship relationship2 = new Relationship(new PKRelationshipEnd(entity1, ZERO_OR_MORE, null), new PKRelationshipEnd(entity2, ZERO_OR_MORE, null));
        assertTrue(relationship1.equals(relationship2));
    }
}
