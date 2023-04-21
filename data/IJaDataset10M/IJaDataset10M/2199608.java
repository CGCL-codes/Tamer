package com.entelience.test.test07directory;

import java.util.List;
import java.util.Iterator;
import org.junit.*;
import static org.junit.Assert.*;
import com.entelience.directory.Company;
import com.entelience.directory.Group;
import com.entelience.directory.People;
import com.entelience.directory.Location;
import com.entelience.directory.PeopleFactory;
import com.entelience.objects.geography.Country;
import com.entelience.objects.geography.Region;
import com.entelience.objects.geography.Timezone;
import com.entelience.objects.directory.PeopleInfoLine;
import com.entelience.objects.directory.LocationInfoLine;
import com.entelience.objects.directory.GroupLocationHistory;
import com.entelience.objects.DropDown;
import com.entelience.objects.directory.GroupInformation;
import com.entelience.util.Config;
import com.entelience.provider.geography.DbGeography;
import com.entelience.test.OurDbTestCase;

public class test05Department extends OurDbTestCase {

    private static long salt;

    private static Integer groupId1 = null;

    private static Integer groupId2 = null;

    private static Integer groupId3 = null;

    private String[] groupsToClean = new String[] { "t07t05t01", "t07t05t02", "t07t05t03" };

    private static Integer cieId = null;

    private static Integer slaveId = null;

    private static Integer esisId = null;

    private static Integer slaveEsisId = null;

    private static Integer userId1 = null;

    private static Integer userId2 = null;

    private static Integer userId3 = null;

    private static Integer userId4 = null;

    private static Integer locId1 = null;

    private static Integer locId2 = null;

    private static Integer locId3 = null;

    private static Integer switzerland = null;

    private static Integer regionId = null;

    private static String timezone = null;

    private static int domainId1 = -1;

    private static int domainId2 = -1;

    private static int domainId3 = -1;

    @Test
    public void test00_init() throws Exception {
        salt = System.currentTimeMillis();
        db.disableTx();
        db.enter();
        db.begin();
        db.executeSql("TRUNCATE TABLE e_company_group");
        db.executeSql("TRUNCATE TABLE e_group_location_history");
        db.executeSql("TRUNCATE TABLE e_group_location");
        db.executeSql("TRUNCATE TABLE e_group_to_people");
        db.executeSql("TRUNCATE TABLE e_group_membership_history");
        cieId = Company.getDefaultCompany();
        assertNotNull("Default Company not found", cieId);
        slaveId = Company.lookupCompanyName("slave");
        assertNotNull("Company Slave not found", slaveId);
        esisId = PeopleFactory.lookupUserName(db, "esis");
        assertNotNull("user esis not found", esisId);
        slaveEsisId = PeopleFactory.lookupUserName(db, "slave_esis");
        assertNotNull("user slave_esis not found", esisId);
        List<Country> l = DbGeography.searchCountry(db, "Switzerland");
        Iterator<Country> it = l.iterator();
        if (it.hasNext()) switzerland = new Integer(it.next().getCountryIso());
        assertNotNull("Switzerland country", switzerland);
        List<Region> r = DbGeography.listRegions(db, switzerland);
        Iterator<Region> rit = r.iterator();
        if (rit.hasNext()) regionId = new Integer(rit.next().getRegionId());
        assertNotNull("geneve county", regionId);
        List<Timezone> z = DbGeography.listTimezones(db, switzerland);
        Iterator<Timezone> zit = z.iterator();
        if (zit.hasNext()) timezone = zit.next().getName();
        assertNotNull("timezone", timezone);
        LocationInfoLine lille = new LocationInfoLine();
        lille.setLocationName("Site 1" + salt);
        lille.setCompanyId(cieId);
        lille.setBuilding("Building 1");
        lille.setSite("Main site");
        lille.setCity("Geneva");
        lille.setCountryIsoCode(switzerland.intValue());
        lille.setZipCode("CH-1204");
        lille.setTimezone(timezone);
        lille.setRegionId(regionId);
        locId1 = new Integer(Location.createLocation(db, lille, PeopleFactory.anonymousId));
        lille.setLocationName("Site 2" + salt);
        lille.setCompanyId(slaveId);
        locId2 = new Integer(Location.createLocation(db, lille, PeopleFactory.anonymousId));
        lille.setLocationName("Site 3" + salt);
        lille.setCompanyId(cieId);
        locId3 = new Integer(Location.createLocation(db, lille, PeopleFactory.anonymousId));
        People p = PeopleFactory.createPeople(db, "t07t05u1" + salt, "QWs0!DFE" + salt, null, "test_first_name", "test_last_name", "+41 00.000.0000", "foobar" + salt + "@example.com", null, cieId, PeopleFactory.anonymousId);
        userId1 = p.getPeopleId();
        p = PeopleFactory.createPeople(db, "t07t05u2" + salt, "QWs0!DFE" + salt, null, "test_first_name", "test_last_name", "+41 00.000.0000", "foobar" + salt + "@example.com", locId1, cieId, PeopleFactory.anonymousId);
        userId2 = p.getPeopleId();
        p = PeopleFactory.createPeople(db, "t07t05u3" + salt, "QWs0!DFE" + salt, null, "test_first_name", "test_last_name", "+41 00.000.0000", "foobar" + salt + "@example.com", locId2, slaveId, PeopleFactory.anonymousId);
        userId3 = p.getPeopleId();
        p = PeopleFactory.createPeopleNoPwd(db, "t07t05u4" + salt, "test_first_name", "test_last_name", "+41 00.000.0000", "foobar" + salt + "@example.com", null, slaveId, PeopleFactory.anonymousId);
        userId4 = p.getPeopleId();
        db.commit();
        domainId1 = Company.addOrGetDomainId(db, "example.com");
        domainId2 = Company.addOrGetDomainId(db, "example.org");
        domainId3 = Company.addOrGetDomainId(db, "example.net");
        db.exit();
    }

    @Test
    public void test01_create_department() throws Exception {
        db.begin();
        try {
            db.enter();
            GroupInformation gi = new GroupInformation();
            gi.setName("t07t05t01");
            groupId1 = Group.createGroup(db, db, gi, PeopleFactory.anonymousId);
            assertNotNull(groupId1);
            GroupInformation g = Group.getGroup(db, groupId1.intValue());
            g.setDepartment(true);
            g.setCompanyId(cieId);
            Group.updateGroup(db, db, g, PeopleFactory.anonymousId);
            db.commit();
        } finally {
            db.exit();
        }
    }

    @Test
    public void test02_create_department() throws Exception {
        db.begin();
        try {
            db.enter();
            GroupInformation gi = new GroupInformation();
            gi.setName("t07t05t02");
            gi.setDepartment(true);
            gi.setCompanyId(cieId);
            groupId2 = Group.createGroup(db, db, gi, PeopleFactory.anonymousId);
            assertNotNull(groupId2);
            db.commit();
        } finally {
            db.exit();
        }
    }

    @Test
    public void test03_create_department_bad() throws Exception {
        {
            db.begin();
            GroupInformation gi = new GroupInformation();
            gi.setName("t07t05t03");
            gi.setDepartment(true);
            boolean exc = false;
            try {
                Group.createGroup(db, db, gi, PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("A department has to be related to a company", exc);
            db.rollback();
        }
        {
            db.begin();
            GroupInformation gi = new GroupInformation();
            gi.setName(null);
            gi.setDepartment(true);
            boolean exc = false;
            try {
                Group.createGroup(db, db, gi, PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("A department name cannot be null", exc);
            db.rollback();
        }
        {
            db.begin();
            GroupInformation gi = new GroupInformation();
            gi.setName("t07t05t02");
            gi.setDepartment(true);
            boolean exc = false;
            try {
                Group.createGroup(db, db, gi, PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("A department name is unique", exc);
            db.rollback();
        }
    }

    @Test
    public void test04_create_department_other_company() throws Exception {
        db.begin();
        try {
            db.enter();
            GroupInformation gi = new GroupInformation();
            gi.setName("t07t05t03");
            gi.setDepartment(true);
            gi.setCompanyId(slaveId);
            groupId3 = Group.createGroup(db, db, gi, PeopleFactory.anonymousId);
            assertNotNull(groupId3);
            db.commit();
        } finally {
            db.exit();
        }
    }

    @Test
    public void test05_addMembers() throws Exception {
        {
            db.begin();
            Group.addUserToGroup(db, groupId1, userId1, PeopleFactory.anonymousId);
            Group.addUserToGroup(db, groupId1, PeopleFactory.lookupUserName(db, "guest"), PeopleFactory.anonymousId);
            Group.addUserToGroup(db, groupId1, PeopleFactory.lookupUserName(db, "exten"), PeopleFactory.anonymousId);
            db.commit();
        }
        {
            List<GroupInformation> lgi = Group.getDepartmentsForUser(db, userId1);
            assertEquals("one group for user", 1, lgi.size());
            List<PeopleInfoLine> lpi = PeopleFactory.getGroupMembers(db, groupId1, false, cieId);
            assertEquals("users for group", 3, lpi.size());
        }
    }

    @Test
    public void test06_addMembers_bad() throws Exception {
        db.begin();
        {
            boolean exc = false;
            try {
                Group.addUserToGroup(db, groupId1.intValue(), slaveEsisId.intValue(), PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("Bad company match group / user", exc);
        }
        {
            boolean exc = false;
            try {
                Group.addUserToGroup(db, groupId1.intValue(), userId1, PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("Already a member", exc);
        }
        {
            boolean exc = false;
            try {
                Group.addUserToGroup(db, groupId1.intValue(), PeopleFactory.anonymousId, PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("anonymous cannot be member", exc);
        }
        {
            boolean exc = false;
            try {
                Group.addUserToGroup(db, -1, userId2, PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("invalid group", exc);
        }
        {
            boolean exc = false;
            try {
                Group.addUserToGroup(db, groupId1.intValue(), -1, PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("invalid user id", exc);
        }
        {
            boolean exc = false;
            try {
                Group.addUserToGroup(db, groupId1.intValue(), userId3, PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("not same company", exc);
        }
        {
            boolean exc = false;
            try {
                Group.addUserToGroup(db, groupId1.intValue(), userId4, PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("disabled user", exc);
        }
        {
            boolean exc = false;
            try {
                Group.addUserToGroup(db, groupId1.intValue(), userId4, userId3);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("modifier is not in the same company", exc);
        }
        db.rollback();
    }

    @Test
    public void test07_department_becomes_global_group() throws Exception {
        db.begin();
        {
            GroupInformation g = Group.getGroup(db, groupId1.intValue());
            g.setDepartment(false);
            g.setCompanyId(null);
            Group.updateGroup(db, db, g, PeopleFactory.anonymousId);
        }
        {
            List<PeopleInfoLine> lpi = PeopleFactory.getGroupMembers(db, groupId1, false, cieId);
            assertEquals("users for group have been moved over", 3, lpi.size());
        }
        db.commit();
    }

    @Test
    public void test08_addMembers_to_global_group() throws Exception {
        db.begin();
        Group.addUserToGroup(db, groupId1.intValue(), userId3, PeopleFactory.anonymousId);
        List<PeopleInfoLine> lpi = PeopleFactory.getGroupMembers(db, groupId1, false, cieId);
        assertEquals("users of cie for global group", 3, lpi.size());
        lpi = PeopleFactory.getGroupMembers(db, groupId1, false, null);
        assertEquals("users for global group", 4, lpi.size());
        db.commit();
    }

    @Test
    public void test09_global_group_becomes_department_bad() throws Exception {
        db.begin();
        GroupInformation g = Group.getGroup(db, groupId1.intValue());
        g.setDepartment(true);
        g.setCompanyId(cieId);
        boolean exc = false;
        try {
            Group.updateGroup(db, db, g, esisId.intValue());
        } catch (Exception e) {
            exc = true;
        }
        assertTrue("Bad company match group / user", exc);
        db.rollback();
    }

    @Test
    public void test10_removeMembers() throws Exception {
        db.begin();
        Group.removeUserFromGroup(db, groupId1.intValue(), userId3, PeopleFactory.anonymousId, Boolean.FALSE);
        List<PeopleInfoLine> lpi = PeopleFactory.getGroupMembers(db, groupId1, false, null);
        assertEquals("users for global group", 3, lpi.size());
        db.commit();
    }

    @Test
    public void test11_global_group_becomes_department() throws Exception {
        db.begin();
        GroupInformation g = Group.getGroup(db, groupId1.intValue());
        g.setDepartment(true);
        g.setCompanyId(cieId);
        Group.updateGroup(db, db, g, esisId.intValue());
        GroupInformation newGroup = Group.getGroup(db, groupId1.intValue());
        assertNotNull(newGroup);
        db.commit();
    }

    @Test
    public void test12_department_cannot_be_managed_by_users_from_other_company() throws Exception {
        db.begin();
        GroupInformation g = Group.getGroup(db, groupId1.intValue());
        boolean exc = false;
        try {
            Group.updateGroup(db, db, g, slaveEsisId.intValue());
        } catch (Exception e) {
            exc = true;
        }
        assertTrue("An admin from another company cannot modify a department", exc);
        db.rollback();
    }

    @Test
    public void test13_list_all_groups() throws Exception {
        List<GroupInformation> l = Group.getGroups(db, null);
        assertEquals("# of groups", 3, l.size());
        l = Group.getGroups(db, userId1);
        assertNotNull(l);
        assertEquals("one not in the same company", 2, l.size());
    }

    @Test
    public void test14_list_groups_where_not() throws Exception {
        db.begin();
        Group.removeAllMembershipsForUser(db, userId1.intValue(), PeopleFactory.anonymousId, Boolean.FALSE);
        List<GroupInformation> l = Group.getGroupsWhereUserIsNotIn(db, userId1.intValue());
        assertNotNull(l);
        int totalGroups = l.size();
        l = Group.getGroupsWhereUserIsNotIn(db, userId3.intValue());
        assertNotNull(l);
        assertEquals("other company", 1, l.size());
        db.commit();
    }

    @Test
    public void test15_add_domain() throws Exception {
        db.begin();
        DropDown[] domains = Group.listDomains(db, groupId1);
        assertNotNull(domains);
        assertEquals(0, domains.length);
        assertTrue(Group.addDomain(db, groupId1, domainId1, 0));
        domains = Group.listDomains(db, groupId1);
        assertNotNull(domains);
        assertEquals(1, domains.length);
        assertEquals("example.com", domains[0].getLabel());
        assertTrue(Group.addDomain(db, groupId1, domainId2, 0));
        domains = Group.listDomains(db, groupId1);
        assertNotNull(domains);
        assertEquals(2, domains.length);
        db.commit();
    }

    @Test
    public void test16_remove_domain() throws Exception {
        db.begin();
        assertTrue(Group.removeDomain(db, groupId1, domainId1, PeopleFactory.anonymousId));
        DropDown[] domains = Group.listDomains(db, groupId1);
        assertNotNull(domains);
        assertEquals(1, domains.length);
        assertEquals("example.org", domains[0].getLabel());
        assertTrue(Group.removeDomain(db, groupId1, domainId2, PeopleFactory.anonymousId));
        domains = Group.listDomains(db, groupId1);
        assertNotNull(domains);
        assertEquals(0, domains.length);
        db.commit();
    }

    @Test
    public void test17_add_location_direct_to_group() throws Exception {
        db.begin();
        Group.addLocationToGroup(db, groupId1, locId1, PeopleFactory.anonymousId);
        List<GroupInformation> lgi = Group.getGroupListForLocation(db, locId1);
        assertEquals("one loc in group", 1, lgi.size());
        lgi = Group.getGroupListForLocation(db, locId2);
        assertEquals("no loc in group", 0, lgi.size());
        lgi = Group.getGroupListNotInLocation(db, locId2, null);
        assertEquals("groups not in loc", 3, lgi.size());
        lgi = Group.getGroupListNotInLocation(db, locId2, slaveId);
        assertEquals("groups not in loc other comp", 1, lgi.size());
        assertTrue(Group.isGroupInLocation(db, groupId1));
        assertFalse(Group.isGroupInLocation(db, -1));
        assertTrue(Group.isGroupInLocation(db, groupId1, locId1));
        assertFalse(Group.isGroupInLocation(db, groupId1, locId2));
        List<GroupLocationHistory> lglh = Group.getLocationToGroupHistory(db, groupId1, locId1, null, null);
        assertTrue("history", lglh.size() > 0);
        assertTrue(Group.hasDepartmentLocationsWithoutUsers(db, groupId1));
        db.commit();
    }

    @Test
    public void test18_fail_to_add_location_to_group() throws Exception {
        db.begin();
        {
            boolean exc = false;
            try {
                Group.addLocationToGroup(db, Integer.valueOf(-1), locId1, PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("bad group id", exc);
        }
        {
            boolean exc = false;
            try {
                Group.addLocationToGroup(db, (Integer) null, locId1, PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("null group id", exc);
        }
        {
            boolean exc = false;
            try {
                Group.addLocationToGroup(db, groupId1, (Integer) null, PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("null loc id", exc);
        }
        {
            boolean exc = false;
            try {
                Group.addLocationToGroup(db, groupId1, Integer.valueOf(-1), PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("bad loc id", exc);
        }
        {
            boolean exc = false;
            try {
                Group.addLocationToGroup(db, groupId1, locId1, PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("already member", exc);
        }
        {
            boolean exc = false;
            try {
                Group.addLocationToGroup(db, groupId1, locId2, userId3);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("wrong user company", exc);
        }
        {
            boolean exc = false;
            try {
                Group.addLocationToGroup(db, groupId1, locId2, PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("not same company loc & group", exc);
        }
        db.commit();
    }

    @Test
    public void test19_fail_remove_direct_location_from_group() throws Exception {
        db.begin();
        {
            boolean exc = false;
            try {
                Group.removeLocationToGroup(db, (Integer) null, locId2, PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("null groupId", exc);
        }
        {
            boolean exc = false;
            try {
                Group.removeLocationToGroup(db, groupId1, (Integer) null, PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("null locId", exc);
        }
        {
            boolean exc = false;
            try {
                Group.removeLocationToGroup(db, groupId1, Integer.valueOf(-1), PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("bad locId", exc);
        }
        {
            boolean exc = false;
            try {
                Group.removeLocationToGroup(db, Integer.valueOf(-1), locId1, PeopleFactory.anonymousId);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("bad groupId", exc);
        }
        {
            boolean exc = false;
            try {
                Group.removeLocationToGroup(db, groupId1, locId1, userId3);
            } catch (Exception e) {
                exc = true;
            }
            assertTrue("modifier not in company", exc);
        }
        db.commit();
    }

    @Test
    public void test20_remove_direct_location_from_group() throws Exception {
        db.begin();
        Group.removeLocationToGroup(db, groupId1, locId1, PeopleFactory.anonymousId);
        List<GroupInformation> lgi = Group.getGroupListForLocation(db, locId1);
        assertEquals("no loc in group", 0, lgi.size());
        db.commit();
    }

    @Test
    public void test21_add_user_with_location_to_group() throws Exception {
        db.begin();
        Group.addUserToGroup(db, groupId1, userId2, PeopleFactory.anonymousId);
        List<GroupInformation> lgi = Group.getGroupListForLocation(db, locId1);
        assertNotNull(lgi);
        assertEquals("one loc in group", 1, lgi.size());
        assertTrue(Group.isGroupInLocation(db, groupId1.intValue(), locId1.intValue()));
        db.commit();
    }

    @Test
    public void test22_remove_loc_from_user() throws Exception {
        db.begin();
        People p = null;
        {
            p = new People(db, userId2, PeopleFactory.anonymousId);
            assertNotNull(p);
            p.setLocationId(db, (Integer) null, Boolean.FALSE);
        }
        {
            LocationInfoLine lil = Location.getLocationOfPeople(db, userId2);
            assertNull(lil);
        }
        {
            List<GroupInformation> lgi = Group.getGroupListForLocation(db, locId1);
            assertNotNull(lgi);
            assertEquals("one loc in group", 1, lgi.size());
        }
        {
            p = new People(db, userId2, PeopleFactory.anonymousId);
            assertNotNull(p);
            p.setLocationId(db, locId3, Boolean.FALSE);
        }
        {
            LocationInfoLine lil = Location.getLocationOfPeople(db, userId2);
            assertNotNull(lil);
            List<LocationInfoLine> lils = Location.getLocationsInDepartment(db, groupId1);
            assertNotNull(lils);
            assertEquals("locs in group", 2, lils.size());
        }
        {
            p = new People(db, userId2, PeopleFactory.anonymousId);
            assertNotNull(p);
            p.setLocationId(db, (Integer) null, Boolean.TRUE);
        }
        {
            LocationInfoLine lil = Location.getLocationOfPeople(db, userId2);
            assertNull(lil);
            List<LocationInfoLine> lils = Location.getLocationsInDepartment(db, groupId1);
            assertNotNull(lils);
            assertEquals("locs in group", 0, lils.size());
        }
        db.commit();
    }

    @Test
    public void test22_loc_from_user_and_direct() throws Exception {
        db.begin();
        People p = null;
        {
            Group.addLocationToGroup(db, groupId1, locId1, PeopleFactory.anonymousId);
            p = new People(db, userId2, PeopleFactory.anonymousId);
            assertNotNull(p);
            p.setLocationId(db, locId3, Boolean.FALSE);
            assertTrue(Group.userInGroup(db, groupId1, userId2));
        }
        {
            LocationInfoLine lil = Location.getLocationOfPeople(db, userId2);
            assertNotNull(lil);
            List<LocationInfoLine> lils = Location.getLocationsInDepartment(db, groupId1);
            assertNotNull(lils);
            assertEquals("locs in group", 2, lils.size());
        }
        {
            p = new People(db, userId2, PeopleFactory.anonymousId);
            assertNotNull(p);
            p.setLocationId(db, (Integer) null, Boolean.TRUE);
        }
        {
            LocationInfoLine lil = Location.getLocationOfPeople(db, userId2);
            assertNull(lil);
            List<LocationInfoLine> lils = Location.getLocationsInDepartment(db, groupId1);
            assertNotNull(lils);
            assertEquals("locs in group", 1, lils.size());
        }
        {
            Group.removeAllLocationsFromDepartment(db, groupId1.intValue(), PeopleFactory.anonymousId);
        }
        {
            List<LocationInfoLine> lils = Location.getLocationsInDepartment(db, groupId1);
            assertNotNull(lils);
            assertEquals("locs in group", 0, lils.size());
        }
        db.commit();
    }

    @Test
    public void test23_same_loc_direct_and_user() throws Exception {
        db.begin();
        People p = null;
        {
            Group.addLocationToGroup(db, groupId1, locId1, PeopleFactory.anonymousId);
        }
        {
            p = new People(db, userId2, PeopleFactory.anonymousId);
            assertNotNull(p);
            p.setLocationId(db, locId1, Boolean.FALSE);
            assertTrue(Group.userInGroup(db, groupId1, userId2));
            assertTrue(Group.isGroupInLocation(db, groupId1, locId1));
        }
        {
            try {
                Group.removeLocationToGroup(db, groupId1, locId1, PeopleFactory.anonymousId);
                assertFalse("there is still a user with this loc", true);
            } catch (Exception e) {
                assertTrue("still one user with this loc", true);
            }
        }
        {
            p = new People(db, userId2, PeopleFactory.anonymousId);
            assertNotNull(p);
            p.setLocationId(db, (Integer) null, Boolean.FALSE);
            Group.removeUserFromGroup(db, groupId1, userId2, PeopleFactory.anonymousId, Boolean.TRUE);
            assertFalse(Group.userInGroup(db, groupId1, userId2));
            assertTrue(Group.isGroupInLocation(db, groupId1, locId1));
        }
        {
            Group.removeLocationToGroup(db, groupId1, locId1, PeopleFactory.anonymousId);
            assertFalse(Group.isGroupInLocation(db, groupId1, locId1));
        }
        db.commit();
    }

    @Test
    public void test24_same_loc_direct_and_user() throws Exception {
        db.begin();
        People p = null;
        {
            assertFalse(Group.isGroupInLocation(db, groupId1, locId1));
            assertFalse(Group.userInGroup(db, groupId1, userId2));
        }
        {
            p = new People(db, userId2, PeopleFactory.anonymousId);
            assertNotNull("user 2", p);
            p.setLocationId(db, locId1, Boolean.FALSE);
            Group.addUserToGroup(db, groupId1, userId2, PeopleFactory.anonymousId);
            assertTrue(Group.userInGroup(db, groupId1, userId2));
            assertTrue(Group.isGroupInLocation(db, groupId1, locId1));
        }
        db.commit();
        db.begin();
        {
            Group.addLocationToGroup(db, groupId1, locId1, PeopleFactory.anonymousId);
        }
        {
            p.setLocationId(db, (Integer) null, Boolean.TRUE);
            p = new People(db, userId2, PeopleFactory.anonymousId);
            assertTrue(p.getLocationId() == null);
            assertTrue(Group.userInGroup(db, groupId1, userId2));
            assertTrue(Group.isGroupInLocation(db, groupId1, locId1));
        }
        {
            Group.removeLocationToGroup(db, groupId1, locId1, PeopleFactory.anonymousId);
            Group.removeUserFromGroup(db, groupId1, userId2, PeopleFactory.anonymousId, Boolean.TRUE);
        }
        db.commit();
    }

    @Test
    public void test25_location_domain_to_group() throws Exception {
        db.begin();
        DropDown[] gd = null;
        DropDown[] ld = null;
        {
            gd = Group.listDomains(db, groupId1);
            assertNotNull(gd);
            assertEquals("no domain for group", 0, gd.length);
            ld = Location.listDomains(db, locId1);
            assertNotNull(ld);
            assertEquals("no domain for location", 0, ld.length);
        }
        {
            Location.addDomain(db, locId1, domainId1, PeopleFactory.anonymousId);
            Group.addLocationToGroup(db, groupId1, locId1, PeopleFactory.anonymousId);
            gd = Group.listDomains(db, groupId1);
            assertEquals("0 direct domain", 0, gd.length);
            gd = Group.listDomainsFull(db, groupId1);
            assertEquals("1 indirect domain", 1, gd.length);
        }
        {
            Group.addDomain(db, groupId1, domainId2, PeopleFactory.anonymousId);
            gd = Group.listDomains(db, groupId1);
            assertEquals("1 direct domain", 1, gd.length);
            gd = Group.listDomainsFull(db, groupId1);
            assertEquals("2  domains", 2, gd.length);
        }
        {
            assertTrue(Location.removeDomain(db, locId1, domainId1, PeopleFactory.anonymousId));
            assertTrue(Group.removeDomain(db, groupId1, domainId2, PeopleFactory.anonymousId));
            Group.removeLocationToGroup(db, groupId1, locId1, PeopleFactory.anonymousId);
        }
        db.commit();
    }

    @Test
    public void test26_location_domain_to_group_weird() throws Exception {
        db.begin();
        DropDown[] gd = null;
        DropDown[] ld = null;
        {
            Location.addDomain(db, locId1, domainId1, PeopleFactory.anonymousId);
            Group.addDomain(db, groupId1, domainId1, PeopleFactory.anonymousId);
            Group.addLocationToGroup(db, groupId1, locId1, PeopleFactory.anonymousId);
            gd = Group.listDomains(db, groupId1);
            assertEquals("1 direct domain ", 1, gd.length);
            gd = Group.listDomainsFull(db, groupId1);
            assertEquals("1 domain", 1, gd.length);
        }
        {
            Group.addDomain(db, groupId1, domainId2, PeopleFactory.anonymousId);
            gd = Group.listDomains(db, groupId1);
            assertEquals("2 direct domains ", 2, gd.length);
            Group.removeDomain(db, groupId1, domainId1, PeopleFactory.anonymousId);
            gd = Group.listDomainsFull(db, groupId1);
            assertEquals("2 domain ", 2, gd.length);
        }
        {
            assertTrue(Location.removeDomain(db, locId1, domainId1, PeopleFactory.anonymousId));
            assertTrue(Group.removeDomain(db, groupId1, domainId2, PeopleFactory.anonymousId));
            Group.removeLocationToGroup(db, groupId1, locId1, PeopleFactory.anonymousId);
        }
        db.commit();
    }

    @Test
    public void test99_cleanup() throws Exception {
        db.begin();
        for (int i = 0; i < groupsToClean.length; i++) {
            Integer gid = Group.lookupGroupName(db, groupsToClean[i]);
            if (gid != null) {
                Group.emptyGroup(db, gid.intValue(), null, PeopleFactory.anonymousId, Boolean.TRUE);
                Group.deleteGroup(db, db, gid.intValue(), true, null, PeopleFactory.anonymousId);
            }
        }
        PeopleFactory.deletePeople(db, db, userId1, null, PeopleFactory.anonymousId, Boolean.TRUE);
        PeopleFactory.deletePeople(db, db, userId2, null, PeopleFactory.anonymousId, Boolean.TRUE);
        PeopleFactory.deletePeople(db, db, userId3, null, PeopleFactory.anonymousId, Boolean.TRUE);
        PeopleFactory.deletePeople(db, db, userId4, null, PeopleFactory.anonymousId, Boolean.TRUE);
        db.commit();
    }
}
