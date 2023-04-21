package net.ontopia.topicmaps.impl.basic.index;

import java.util.Iterator;
import net.ontopia.topicmaps.core.AbstractTopicMapTest;
import net.ontopia.topicmaps.core.AssociationIF;
import net.ontopia.topicmaps.core.AssociationRoleIF;
import net.ontopia.topicmaps.core.OccurrenceIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicMapBuilderIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.core.index.ClassInstanceIndexIF;
import net.ontopia.topicmaps.impl.basic.InMemoryTopicMapStore;
import junit.framework.TestCase;

public class ClassInstanceTest extends TestCase {

    protected ClassInstanceIndexIF index;

    protected TopicMapBuilderIF builder;

    protected TopicMapIF topicmap;

    public ClassInstanceTest(String name) {
        super(name);
    }

    protected void setUp() {
        topicmap = makeTopicMap();
        index = (ClassInstanceIndexIF) topicmap.getIndex("net.ontopia.topicmaps.core.index.ClassInstanceIndexIF");
    }

    protected TopicMapIF makeTopicMap() {
        InMemoryTopicMapStore store = new InMemoryTopicMapStore();
        builder = store.getTopicMap().getBuilder();
        return store.getTopicMap();
    }

    public void testAssociationRoles() {
        assertTrue("index finds role types in empty topic map", index.getAssociationRoleTypes().size() == 0);
        TopicIF at = builder.makeTopic();
        TopicIF art1 = builder.makeTopic();
        TopicIF art2 = builder.makeTopic();
        TopicIF art3 = builder.makeTopic();
        TopicIF t1 = builder.makeTopic();
        TopicIF t2 = builder.makeTopic();
        AssociationIF a1 = builder.makeAssociation(at);
        AssociationRoleIF r1 = builder.makeAssociationRole(a1, art1, t1);
        AssociationRoleIF r2 = builder.makeAssociationRole(a1, art2, t2);
        AssociationIF a2 = builder.makeAssociation(at);
        AssociationRoleIF r3 = builder.makeAssociationRole(a2, art3, t1);
        AssociationRoleIF r4 = builder.makeAssociationRole(a2, art3, t2);
        assertTrue("role type not found", index.getAssociationRoles(art1).size() == 1);
        assertTrue("roles not found via type", index.getAssociationRoles(art1).iterator().next().equals(r1));
        assertTrue("index claims role type not used", index.usedAsAssociationRoleType(art1));
        assertTrue("role type not found", index.getAssociationRoles(art2).size() == 1);
        assertTrue("roles not found via type", index.getAssociationRoles(art2).iterator().next().equals(r2));
        assertTrue("index claims role type not used", index.usedAsAssociationRoleType(art2));
        assertTrue("spurious association roles found", index.getAssociationRoles(t1).size() == 0);
        assertTrue("index claims ordinary topic used as role type", !index.usedAsAssociationRoleType(t1));
        assertTrue("roles with art3 types not found", index.getAssociationRoles(art3).size() == 2);
        assertTrue("index claims art3 not used as role type", index.usedAsAssociationRoleType(art3));
        assertTrue("index loses or invents role types", index.getAssociationRoleTypes().size() == 3);
        assertTrue("index forgets that topic is used as a type", index.usedAsType(art1));
        AssociationIF a3 = builder.makeAssociation(at);
        AssociationRoleIF r5 = builder.makeAssociationRole(a3, art1, t1);
        AssociationRoleIF r6 = builder.makeAssociationRole(a3, art2, t2);
        assertTrue("role type not found", index.getAssociationRoles(art1).size() == 2);
        assertTrue("roles not found via type", index.getAssociationRoles(art1).contains(r5));
        assertTrue("duplicate role types not suppressed", index.getAssociationRoleTypes().size() == 3);
    }

    public void testAssociations() {
        assertTrue("index finds association types in empty topic map", index.getAssociationTypes().size() == 0);
        TopicIF at1 = builder.makeTopic();
        TopicIF at2 = builder.makeTopic();
        TopicIF t1 = builder.makeTopic();
        TopicIF t2 = builder.makeTopic();
        AssociationIF a1 = builder.makeAssociation(at1);
        AssociationRoleIF r1 = builder.makeAssociationRole(a1, builder.makeTopic(), t1);
        AssociationRoleIF r2 = builder.makeAssociationRole(a1, builder.makeTopic(), t2);
        AssociationIF a2 = builder.makeAssociation(at2);
        AssociationRoleIF r3 = builder.makeAssociationRole(a2, builder.makeTopic(), t1);
        AssociationRoleIF r4 = builder.makeAssociationRole(a2, builder.makeTopic(), t2);
        assertTrue("association type not found", index.getAssociations(at1).size() == 1);
        assertTrue("associations not found via type", index.getAssociations(at1).iterator().next().equals(a1));
        assertTrue("index claims association type not used", index.usedAsAssociationType(at1));
        assertTrue("spurious association types found", index.getAssociations(t1).size() == 0);
        assertTrue("index claims ordinary topic used as association type", !index.usedAsAssociationType(t1));
        assertTrue("associations with at2 type not found", index.getAssociations(at2).size() == 1);
        assertTrue("associations not found via at2 type", index.getAssociations(at2).iterator().next().equals(a2));
        assertTrue("index claims at2 not used as association type", index.usedAsAssociationType(at2));
        assertTrue("index loses or invents association types", index.getAssociationTypes().size() == 2);
        assertTrue("index forgets that topic is used as a type", index.usedAsType(at1));
        AssociationIF a3 = builder.makeAssociation(at1);
        assertTrue("association type not found", index.getAssociations(at1).size() == 2);
        assertTrue("associations not found via type", index.getAssociations(at1).contains(a3));
        assertTrue("duplicate association types not suppressed", index.getAssociationTypes().size() == 2);
    }

    public void testOccurrences() {
        assertTrue("index finds occurrence types in empty topic map", index.getOccurrenceTypes().size() == 0);
        TopicIF ot1 = builder.makeTopic();
        TopicIF t1 = builder.makeTopic();
        OccurrenceIF o1 = builder.makeOccurrence(t1, ot1, "");
        assertTrue("occurrence type not found", index.getOccurrences(ot1).size() == 1);
        assertTrue("occurrence not found via type", index.getOccurrences(ot1).iterator().next().equals(o1));
        assertTrue("index claims occurrence type not used", index.usedAsOccurrenceType(ot1));
        assertTrue("spurious occurrence types found", index.getOccurrences(t1).size() == 0);
        assertTrue("index claims ordinary topic used as occurrence type", !index.usedAsOccurrenceType(t1));
        assertTrue("index forgets that topic is used as a type", index.usedAsType(ot1));
        OccurrenceIF o3 = builder.makeOccurrence(t1, ot1, "");
        assertTrue("occurrence type not found", index.getOccurrences(ot1).size() == 2);
        assertTrue("occurrence not found via type", index.getOccurrences(ot1).contains(o3));
        assertTrue("duplicate occurrence types not suppressed", index.getOccurrenceTypes().size() == 1);
    }

    public void testTopics() {
        assertTrue("index finds spurious topic types", index.getTopics(null).size() == 0);
        assertTrue("null used as topic type in empty topic map", !index.usedAsTopicType(null));
        assertTrue("index finds topic types in empty topic map", index.getTopicTypes().size() == 0);
        TopicIF tt1 = builder.makeTopic();
        TopicIF tt2 = builder.makeTopic();
        TopicIF t1 = builder.makeTopic();
        TopicIF t2 = builder.makeTopic();
        t1.addType(tt1);
        t1.addType(tt2);
        assertTrue("topic type not found", index.getTopics(tt1).size() == 1);
        assertTrue("topic not found via type", index.getTopics(tt1).iterator().next().equals(t1));
        assertTrue("index claims topic type not used", index.usedAsTopicType(tt1));
        assertTrue("topic type not found", index.getTopics(tt2).size() == 1);
        assertTrue("topic not found via type", index.getTopics(tt2).iterator().next().equals(t1));
        assertTrue("index claims topic type not used", index.usedAsTopicType(tt2));
        assertTrue("spurious topic types found", index.getTopics(t1).size() == 0);
        assertTrue("index claims ordinary topic used as topic type", !index.usedAsTopicType(t1));
        assertTrue("topic with null type found", index.getTopics(null).size() == 3);
        assertTrue("index claims null used as topic type", index.usedAsTopicType(null));
        assertTrue("index loses or invents topic types", index.getTopicTypes().size() == 2);
        assertTrue("index forgets that topic is used as a type", index.usedAsType(tt1));
        assertTrue("index forgets that topic is used as a type", index.usedAsType(tt2));
        TopicIF t3 = builder.makeTopic();
        t3.addType(tt1);
        assertTrue("topic type not found", index.getTopics(tt1).size() == 2);
        assertTrue("topic not found via type", index.getTopics(tt1).contains(t3));
        assertTrue("duplicate topic types not suppressed", index.getTopicTypes().size() == 2);
    }

    public void testTopicsDynamic() {
        TopicIF tt1 = builder.makeTopic();
        TopicIF t1 = builder.makeTopic();
        t1.addType(tt1);
        assertTrue("TopicMapIF.addTopic does not update index", index.getTopics(tt1).size() == 1);
        t1.removeType(tt1);
        assertTrue("TopicIF.removeType does not update index", index.getTopics(tt1).size() == 0);
        t1.addType(tt1);
        assertTrue("TopicIF.addType does not update index", index.getTopics(tt1).size() == 1);
        t1.remove();
        assertTrue("TopicMapIF.removeTopic does not update index", index.getTopics(tt1).size() == 0);
    }

    public void testAssociationsDynamic() {
        TopicIF at1 = builder.makeTopic();
        TopicIF at2 = builder.makeTopic();
        AssociationIF a = builder.makeAssociation(at1);
        assertTrue("TopicMapIF.addAssociation does not update index", index.getAssociations(at1).size() == 1);
        a.setType(at2);
        assertTrue("AssociationIF.setType(at2) does not update index", index.getAssociations(at1).size() == 0);
        a.setType(at1);
        assertTrue("AssociationIF.addType does not update index", index.getAssociations(at1).size() == 1);
        a.remove();
        assertTrue("TopicMapIF.removeAssociation does not update index", index.getAssociations(at1).size() == 0);
    }

    public void testAssociationRolesDynamic() {
        TopicIF at1 = builder.makeTopic();
        TopicIF art1 = builder.makeTopic();
        TopicIF art2 = builder.makeTopic();
        TopicIF player = builder.makeTopic();
        AssociationIF a = builder.makeAssociation(at1);
        AssociationRoleIF r1 = builder.makeAssociationRole(a, art1, player);
        assertTrue("TopicMapIF.addAssociation does not update role type index", index.getAssociationRoles(art1).size() == 1);
        r1.setType(art2);
        assertTrue("AssociationRoleIF.setType(art2) does not update index", index.getAssociationRoles(art1).size() == 0);
        assertTrue("AssociationRoleIF.setType(art2) does not update index", index.getAssociationRoles(art2).size() == 1);
        r1.setType(art1);
        assertTrue("AssociationRoleIF.setType does not update index", index.getAssociationRoles(art1).size() == 1);
        assertTrue("AssociationRoleIF.setType does not update index", index.getAssociationRoles(art2).size() == 0);
        a.remove();
        assertTrue("TopicMapIF.removeAssociation does not update role type index", index.getAssociationRoles(art1).size() == 0);
    }

    public void testOccurrencesDynamic() {
        TopicIF ot1 = builder.makeTopic();
        TopicIF ot2 = builder.makeTopic();
        TopicIF t1 = builder.makeTopic();
        OccurrenceIF o1 = builder.makeOccurrence(t1, ot1, "");
        assertTrue("OccurrenceIF.setType does not update index", index.getOccurrences(ot1).size() == 1);
        o1.setType(ot2);
        assertTrue("OccurrenceIF.setType(ot2) does not update index", index.getOccurrences(ot1).size() == 0);
        OccurrenceIF o2 = builder.makeOccurrence(t1, ot1, "");
        assertTrue("TopicIF.addOccurrence does not update index", index.getOccurrences(ot1).size() == 1);
        t1.remove();
        assertTrue("TopicMapIF.removeTopic does not update occurrence index", index.getOccurrences(ot1).size() == 0);
    }

    public void testConcurrentModification() {
        TopicIF ot1 = builder.makeTopic();
        TopicIF ot2 = builder.makeTopic();
        TopicIF t1 = builder.makeTopic();
        OccurrenceIF occ = builder.makeOccurrence(t1, ot1, "");
        try {
            Iterator it = index.getOccurrences(ot1).iterator();
            occ.setType(ot2);
            it.next();
        } catch (java.util.ConcurrentModificationException e) {
            fail("ClassInstanceIndex returns live collections");
        }
    }
}
