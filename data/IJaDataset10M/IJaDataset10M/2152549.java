package org.tmapi.index.core.test;

import org.tmapi.core.Association;
import org.tmapi.core.AssociationRole;
import org.tmapi.core.TMAPIException;
import org.tmapi.core.Topic;
import org.tmapi.index.core.AssociationRolesIndex;

public class AssociationRolesIndexTest extends IndexTest {

    public AssociationRolesIndexTest(String name) {
        super(name, org.tmapi.index.core.AssociationRolesIndex.class);
    }

    public void testgetAssociationRoleTypes() {
        try {
            AssociationRolesIndex oi = (AssociationRolesIndex) m_topicMap.getHelperObject(org.tmapi.index.core.AssociationRolesIndex.class);
            Association a = m_topicMap.createAssociation();
            AssociationRole r = a.createAssociationRole(null, null);
            Topic tt = m_topicMap.createTopic();
            oi.open();
            assertEquals("There cannot be a AssociationRoleType in TopicMap", 0, oi.getAssociationRoleTypes().size());
            r.setType(tt);
            oi.reindex();
            assertEquals("There should be one AssociationRoleType in TopicMap", 1, oi.getAssociationRoleTypes().size());
            assertEquals("The AssociationRoleType is not the right one", tt, oi.getAssociationRoleTypes().iterator().next());
            r.setType(null);
            oi.reindex();
            assertEquals("There cannot be a AssociationRoleType in TopicMap after removal", 0, oi.getAssociationRoleTypes().size());
        } catch (TMAPIException ex) {
            fail("Unexpected exception: " + ex.toString());
        }
    }

    public void testAssociationRolesByType() {
        try {
            AssociationRolesIndex oi = (AssociationRolesIndex) m_topicMap.getHelperObject(org.tmapi.index.core.AssociationRolesIndex.class);
            Association a = m_topicMap.createAssociation();
            AssociationRole r = a.createAssociationRole(null, null);
            Topic tt = m_topicMap.createTopic();
            oi.open();
            assertEquals("There cannot be an AssociationRole with this type in the TopicMap", 0, oi.getAssociationRolesByType(tt).size());
            r.setType(tt);
            oi.reindex();
            assertEquals("There should be an AssociationRole with this type in the TopicMap", 1, oi.getAssociationRolesByType(tt).size());
            assertEquals("The AssociationRole is not the right one", r, oi.getAssociationRolesByType(tt).iterator().next());
            r.setType(null);
            oi.reindex();
            assertEquals("There cannot be an AssociationRole with this type in the TopicMap after removal", 0, oi.getAssociationRolesByType(tt).size());
            assertEquals("The should be an AssociationRole with no type after setting type to null", 1, oi.getAssociationRolesByType(null).size());
            assertEquals("The AssociationRole is not the right one", r, oi.getAssociationRolesByType(null).iterator().next());
        } catch (TMAPIException ex) {
            ex.printStackTrace();
            fail("Unexpected exception: " + ex.toString());
        }
    }
}
