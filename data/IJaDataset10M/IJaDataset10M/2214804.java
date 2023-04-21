package com.c2b2.ipoint.model.test;

import com.c2b2.ipoint.model.*;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class GroupTestCase extends TestCase {

    public GroupTestCase(String sTestName) {
        super(sTestName);
    }

    public void testCreateGroup() throws PersistentModelException {
        Group group = Group.createGroup("Test", "Test Group");
        assertNotNull(group);
        assertEquals(group.getName(), "Test");
        assertEquals(group.getDescription(), "Test Group");
        Group findGroup = Group.findGroup(group.getID());
        assertNotNull(findGroup);
        assertEquals(findGroup.getID(), group.getID());
        List groups = Group.getAllGroups();
        assertTrue(groups.contains(group));
        Group guest = Group.getGuestGroup();
        assertFalse(guest.getID() == group.getID());
        assertFalse(Group.getRegisteredGroup().getID() == group.getID());
        assertFalse(Group.getAdminGroup().getID() == group.getID());
    }

    public void testDeleteGroup() throws PersistentModelException {
        Group group = Group.createGroup("Test", "Test Group");
        assertNotNull(group);
        group.delete();
        try {
            Group deletedGroup = Group.findGroup(group.getID());
            fail("An Exception should be thrown");
        } catch (PersistentModelException pme) {
        }
        assertFalse(Group.getAllGroups().contains(group));
        Group adminGroup = Group.getAdminGroup();
        try {
            adminGroup.delete();
            fail("An Exception should be thrown");
        } catch (PersistentModelException pme) {
        }
        Group regGroup = Group.getRegisteredGroup();
        try {
            regGroup.delete();
            fail("An Exception should be thrown");
        } catch (PersistentModelException pme) {
        }
        Group guestGroup = Group.getGuestGroup();
        try {
            guestGroup.delete();
            fail("An Exception should be thrown");
        } catch (PersistentModelException pme) {
        }
    }

    public void testUserCleanUpOnDelete() throws PersistentModelException {
        User user = User.createUser("test", "test");
        Group group = Group.createGroup("test", "test");
        group.addUser(user);
        assertTrue(user.isInGroup(group));
        group.delete();
        assertFalse(user.isInGroup(group));
    }

    public void testClearAllUsers() throws PersistentModelException {
        User user = User.createUser("test", "test");
        Group group = Group.createGroup("test", "test");
        group.addUser(user);
        assertTrue(user.isInGroup(group));
        group.clearAllUsers();
        assertFalse(user.isInGroup(group));
    }

    public void testSetDescription() throws PersistentModelException {
        Group group = Group.createGroup("test", "test");
        assertEquals(group.getDescription(), "test");
        group.setDescription("New Description");
        assertEquals(group.getDescription(), "New Description");
    }

    public void testIsGuestGroup() throws PersistentModelException {
        Group guest = Group.getGuestGroup();
        assertTrue(guest.isGuestGroup());
        assertFalse(guest.isAdminGroup());
        Group admin = Group.getAdminGroup();
        assertFalse(admin.isGuestGroup());
    }
}
