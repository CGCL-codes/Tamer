package com.c2b2.ipoint.model.test.crm;

import com.c2b2.ipoint.model.Group;
import com.c2b2.ipoint.model.PersistentModelException;
import com.c2b2.ipoint.model.User;
import com.c2b2.ipoint.model.crm.UserCategory;
import junit.framework.TestCase;

/**
  * $Id: UserCategoryTestCase.java,v 1.1 2006/04/23 14:33:32 steve Exp $
  *
  * Copyright 2006 C2B2 Consulting Limited. All rights reserved.
  * Use of this code is subject to license.
  * Please check your license agreement for usage restrictions
  *
  * This class tests the UserCategory persistent class to ensure that all the
  * persistent model is correct.
  *
  * @author $Author: steve $
  * @version $Revision: 1.1 $
  * $Date: 2006/04/23 14:33:32 $
  *
  */
public class UserCategoryTestCase extends TestCase {

    public UserCategoryTestCase() {
    }

    public void testCreate() throws PersistentModelException {
        String name = "Name";
        String description = "description";
        UserCategory cat1 = UserCategory.create(name, description);
        assertNotNull(cat1);
        assertEquals(cat1.getName(), name);
        assertEquals(cat1.getDescription(), description);
        assertNull(cat1.getGroup());
        assertNull(cat1.getParent());
        assertTrue(cat1.isRoot());
        cat1 = UserCategory.create(name, description, Group.getGuestGroup());
        assertNotNull(cat1);
        assertEquals(cat1.getName(), name);
        assertEquals(cat1.getDescription(), description);
        assertEquals(cat1.getGroup(), Group.getGuestGroup());
        assertNull(cat1.getParent());
        assertTrue(cat1.isRoot());
        cat1 = UserCategory.create(name, description, Group.getGuestGroup());
        UserCategory cat2 = UserCategory.create(name, description, Group.getGuestGroup(), cat1);
        assertNotNull(cat1);
        assertEquals(cat1.getName(), name);
        assertEquals(cat1.getDescription(), description);
        assertEquals(cat1.getGroup(), Group.getGuestGroup());
        assertNull(cat1.getParent());
        assertTrue(cat1.isRoot());
        assertEquals(cat1, cat2.getParent());
    }

    public void testRelations() throws PersistentModelException {
        String name = "Name";
        String description = "description";
        UserCategory cat1 = UserCategory.create(name, description);
        UserCategory cat2 = UserCategory.create(name, description, Group.getGuestGroup(), cat1);
        UserCategory cat3 = UserCategory.create(name, description, Group.getGuestGroup(), cat2);
        UserCategory cat4 = UserCategory.create(name, description, Group.getGuestGroup(), cat2);
        UserCategory cat5 = UserCategory.create(name, description);
        assertTrue(cat1.isRoot());
        assertFalse(cat2.isRoot());
        assertEquals(cat1, cat4.getRoot());
        assertTrue(cat3.isSibling(cat4));
        assertTrue(cat3.isRelated(cat2));
        assertFalse(cat5.isRelated(cat4));
    }

    public void testGroups() throws PersistentModelException {
        Group group1 = Group.createGroup("Group1", "Group 1");
        Group group2 = Group.createGroup("Group2", "Group 2");
        String name = "Name";
        String description = "description";
        UserCategory cat1 = UserCategory.create(name, description, group1);
        UserCategory cat2 = UserCategory.create(name, description, cat1);
        UserCategory cat3 = UserCategory.create(name, description, group2, cat2);
        UserCategory cat4 = UserCategory.create(name, description, cat2);
        User user1 = User.createUser("fred", "fred");
        cat1.addUser(user1);
        assertTrue(cat1.getUsers().contains(user1));
        assertTrue(user1.getGroups().contains(group1));
        assertFalse(user1.getGroups().contains(group2));
        User user2 = User.createUser("fred1", "fred2");
        cat3.addUser(user2);
        assertTrue(cat3.getUsers().contains(user2));
        assertTrue(user2.getGroups().contains(group1));
        assertTrue(user2.getGroups().contains(group2));
        User user3 = User.createUser("fred3", "fred3");
        cat4.addUser(user3);
        assertTrue(cat4.getUsers().contains(user3));
        assertTrue(user3.getGroups().contains(group1));
        assertFalse(user3.getGroups().contains(group2));
        assertTrue(cat3.getAllGroups().contains(group2));
        assertTrue(cat3.getAllGroups().contains(group1));
        assertFalse(cat4.getAllGroups().contains(group2));
        assertTrue(cat4.getAllGroups().contains(group1));
        assertTrue(cat1.getAllChildUsers().contains(user1));
        assertTrue(cat1.getAllChildUsers().contains(user2));
        assertTrue(cat1.getAllChildUsers().contains(user3));
    }

    public void testDelete() throws PersistentModelException {
        Group group1 = Group.createGroup("Group1", "Group 1");
        Group group2 = Group.createGroup("Group2", "Group 2");
        Group group3 = Group.createGroup("Group3", "Group 3");
        String name = "Name";
        String description = "description";
        UserCategory cat1 = UserCategory.create(name, description, group1);
        UserCategory cat2 = UserCategory.create(name, description, group2, cat1);
        UserCategory cat3 = UserCategory.create(name, description, group3, cat2);
        User user1 = User.createUser("fred", "fred");
        cat1.addUser(user1);
        User user2 = User.createUser("fred1", "fred2");
        cat3.addUser(user2);
        assertTrue(user2.isInGroup(group1));
        assertTrue(user2.isInGroup(group2));
        assertTrue(user2.isInGroup(group3));
        long cat2id = cat2.getID();
        cat2.delete();
        assertFalse(cat1.getChildren().contains(cat2));
        assertTrue(cat1.getChildren().contains(cat3));
        assertEquals(cat3.getParent(), cat1);
        assertTrue(user2.isInGroup(group1));
        assertTrue(user2.isInGroup(group3));
        assertFalse(user2.isInGroup(group2));
        cat1.delete();
        assertTrue(cat3.isRoot());
        assertFalse(user2.isInGroup(group1));
        assertTrue(user2.isInGroup(group3));
        try {
            UserCategory.find(cat2id);
            fail("An Exception should be thrown");
        } catch (PersistentModelException pme) {
            cat2id = 0;
        }
    }

    public void testSetGroup() throws PersistentModelException {
        Group group1 = Group.createGroup("Group1", "Group 1");
        Group group2 = Group.createGroup("Group2", "Group 2");
        Group group3 = Group.createGroup("Group3", "Group 3");
        Group group4 = Group.createGroup("Group4", "Group 4");
        Group group5 = Group.createGroup("Group4", "Group 5");
        String name = "Name";
        String description = "description";
        UserCategory cat1 = UserCategory.create(name, description, group1);
        UserCategory cat2 = UserCategory.create(name, description, group2, cat1);
        UserCategory cat3 = UserCategory.create(name, description, group3, cat2);
        User user1 = User.createUser("fred", "fred");
        cat1.addUser(user1);
        User user2 = User.createUser("fred2", "fred2");
        cat2.addUser(user2);
        User user3 = User.createUser("fred3", "fred3");
        cat3.addUser(user3);
        cat1.setGroup(group4);
        assertEquals(cat1.getGroup(), group4);
        assertTrue(user1.isInGroup(group4));
        assertTrue(user2.isInGroup(group4));
        assertTrue(user3.isInGroup(group4));
        assertFalse(user1.isInGroup(group1));
        assertFalse(user2.isInGroup(group1));
        assertFalse(user3.isInGroup(group1));
        cat3.setGroup(group5);
        assertTrue(user3.isInGroup(group5));
        assertFalse(user3.isInGroup(group3));
        assertFalse(user2.isInGroup(group5));
        assertFalse(user1.isInGroup(group5));
    }

    public void testMoveTreeBranch() throws PersistentModelException {
        Group group1 = Group.createGroup("Group1", "Group 1");
        Group group2 = Group.createGroup("Group2", "Group 2");
        Group group3 = Group.createGroup("Group3", "Group 3");
        Group group4 = Group.createGroup("Group4", "Group 4");
        Group group5 = Group.createGroup("Group4", "Group 5");
        String name = "Name";
        String description = "description";
        UserCategory cat1 = UserCategory.create(name, description, group1);
        UserCategory cat2 = UserCategory.create(name, description, group2, cat1);
        UserCategory cat3 = UserCategory.create(name, description, group3, cat2);
        UserCategory cat4 = UserCategory.create(name, description, group4, cat1);
        UserCategory cat5 = UserCategory.create(name, description, group5, cat4);
        User user1 = User.createUser("fred", "fred");
        cat5.addUser(user1);
        assertTrue(user1.isInGroup(group5));
        assertTrue(user1.isInGroup(group4));
        assertTrue(user1.isInGroup(group1));
        assertFalse(user1.isInGroup(group2));
        assertFalse(user1.isInGroup(group3));
        assertFalse(cat3.isSibling(cat5));
        assertTrue(cat3.isRelated(cat3));
        cat3.addChild(cat5);
        assertFalse(cat4.getChildren().contains(cat5));
        assertFalse(user1.isInGroup(group4));
        assertTrue(user1.isInGroup(group1));
        assertTrue(user1.isInGroup(group2));
        assertTrue(user1.isInGroup(group3));
        assertTrue(user1.isInGroup(group5));
        assertEquals(cat5.getParent(), cat3);
        assertTrue(cat3.getChildren().contains(cat5));
    }

    public void testFinders() throws PersistentModelException {
        String name = "Name";
        String description = "description";
        UserCategory cat1 = UserCategory.create(name, description);
        UserCategory cat2 = UserCategory.create(name, description, cat1);
        assertEquals(UserCategory.find(cat1.getID()), cat1);
        assertTrue(UserCategory.findAll().contains(cat1));
        assertTrue(UserCategory.findRoots().contains(cat1));
        assertFalse(UserCategory.findRoots().contains(cat2));
        try {
            UserCategory.find("DODGY");
            fail("An exception should be thrown");
        } catch (PersistentModelException pme) {
            assertTrue(true);
        }
        try {
            UserCategory result = UserCategory.find(-99L);
            fail("An exception should be thrown");
        } catch (PersistentModelException pme) {
            assertTrue(true);
        }
    }

    public void testMakeRoot() throws PersistentModelException {
        Group group1 = Group.createGroup("Group1", "Group 1");
        Group group2 = Group.createGroup("Group2", "Group 2");
        Group group3 = Group.createGroup("Group3", "Group 3");
        String name = "Name";
        String description = "description";
        UserCategory cat1 = UserCategory.create(name, description, group1);
        UserCategory cat2 = UserCategory.create(name, description, group2, cat1);
        UserCategory cat3 = UserCategory.create(name, description, group3, cat2);
        User user1 = User.createUser("fred", "fred");
        cat1.addUser(user1);
        User user2 = User.createUser("fred2", "fred2");
        cat2.addUser(user2);
        User user3 = User.createUser("fred3", "fred3");
        cat3.addUser(user3);
        cat3.makeRoot();
        assertTrue(cat3.isRoot());
        assertFalse(user3.isInGroup(group1));
        assertFalse(user3.isInGroup(group2));
        assertTrue(user3.isInGroup(group3));
        assertFalse(cat2.getChildren().contains(cat3));
    }
}
