package org.geonetwork.gaap.services;

import org.junit.Test;
import org.geonetwork.gaap.domain.operation.MetadataPermissions;
import org.geonetwork.gaap.domain.operation.Operation;
import org.geonetwork.gaap.domain.operation.Permission;
import org.geonetwork.gaap.domain.group.Group;
import org.geonetwork.gaap.domain.user.User;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/**
 * Tests for MetadataPermissionsService class
 */
public class MetadataPermissionsServiceTest extends AbstractServiceTest {

    MetadataPermissionsService metadataPermissionsService;

    public void setMetadataPermissionsService(MetadataPermissionsService metadataPermissionsService) {
        this.metadataPermissionsService = metadataPermissionsService;
    }

    @Test
    public void testFilterMetadata() {
        try {
            Operation op = new Operation();
            op.setName("view");
            operationDao.saveOperation(op);
            Group group = new Group();
            group.setUuid("group1");
            group.setName("group1");
            groupDao.saveGroup(group);
            Group group2 = new Group();
            group2.setUuid("group2");
            group2.setName("group2");
            groupDao.saveGroup(group2);
            Group group3 = new Group();
            group3.setUuid("all");
            group3.setName("All");
            groupDao.saveGroup(group3);
            Set<Group> userGroups = new HashSet<Group>();
            userGroups.add(group);
            User user = new User();
            user.setName("john");
            user.setUsername("john");
            user.setPassword("password");
            user.setProfile("ADMIN");
            user.setUserGroups(userGroups);
            userDao.saveUser(user);
            MetadataPermissions metadataPermissions = new MetadataPermissions();
            metadataPermissions.setMetadataUuid("aaa-bbb-ccc-mmm");
            Set<Permission> permissions = new HashSet<Permission>();
            Operation opJibx = new Operation();
            opJibx.setName(op.getName());
            Permission permission = new Permission();
            Group groupJibx = new Group();
            groupJibx.setUuid(group.getUuid());
            permission.setGroup(groupJibx);
            permission.setOperation(opJibx);
            permissions.add(permission);
            metadataPermissions.setPermissions(permissions);
            metadataPermissionsService.savePermissions(metadataPermissions);
            metadataPermissions = new MetadataPermissions();
            metadataPermissions.setMetadataUuid("aaa-bbb-ccc-jjj");
            permissions = new HashSet<Permission>();
            opJibx = new Operation();
            opJibx.setName(op.getName());
            permission = new Permission();
            groupJibx = new Group();
            groupJibx.setUuid(group2.getUuid());
            permission.setGroup(groupJibx);
            permission.setOperation(opJibx);
            permissions.add(permission);
            metadataPermissions.setPermissions(permissions);
            metadataPermissionsService.savePermissions(metadataPermissions);
            metadataPermissions = new MetadataPermissions();
            metadataPermissions.setMetadataUuid("aaa-bbb-ccc-kkk");
            permissions = new HashSet<Permission>();
            opJibx = new Operation();
            opJibx.setName(op.getName());
            permission = new Permission();
            groupJibx = new Group();
            groupJibx.setUuid(group3.getUuid());
            permission.setGroup(groupJibx);
            permission.setOperation(opJibx);
            permissions.add(permission);
            metadataPermissions.setPermissions(permissions);
            metadataPermissionsService.savePermissions(metadataPermissions);
            setComplete();
            endTransaction();
            startNewTransaction();
            List<String> metadataUuids = new ArrayList<String>();
            metadataUuids.add("aaa-bbb-ccc-mmm");
            metadataUuids.add("aaa-bbb-ccc-jjj");
            metadataUuids.add("aaa-bbb-ccc-kkk");
            List<String> filteredMetadata = metadataPermissionsService.filterMetadata(metadataUuids, "john");
            assertEquals(2, filteredMetadata.size());
            metadataUuids = new ArrayList<String>();
            metadataUuids.add("aaa-bbb-ccc-jjjj");
            filteredMetadata = metadataPermissionsService.filterMetadata(metadataUuids, "john");
            assertEquals(0, filteredMetadata.size());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testSavePermissions() {
        Operation op = new Operation();
        op.setName("view");
        operationDao.saveOperation(op);
        Group group = new Group();
        group.setName("group1");
        groupDao.saveGroup(group);
        Group group2 = new Group();
        group2.setName("group2");
        groupDao.saveGroup(group2);
        MetadataPermissions metadataPermissions = new MetadataPermissions();
        metadataPermissions.setMetadataUuid("aaa-bbb-ccc-mmm");
        Set<Permission> permissions = new HashSet<Permission>();
        Operation opJibx = new Operation();
        opJibx.setName(op.getName());
        Permission permission = new Permission();
        Group groupJibx = new Group();
        groupJibx.setUuid(group.getUuid());
        permission.setGroup(groupJibx);
        permission.setOperation(opJibx);
        permissions.add(permission);
        permission = new Permission();
        Group group2Jibx = new Group();
        group2Jibx.setUuid(group2.getUuid());
        permission.setGroup(group2Jibx);
        permission.setOperation(opJibx);
        permissions.add(permission);
        metadataPermissions.setPermissions(permissions);
        try {
            metadataPermissionsService.savePermissions(metadataPermissions);
            List<Permission> results = permissionDao.findPermissionsByMetadataUuid("aaa-bbb-ccc-mmm");
            assertEquals(2, results.size());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetPermissions() {
        Operation op = new Operation();
        op.setName("view");
        operationDao.saveOperation(op);
        Group group = new Group();
        group.setName("group1");
        groupDao.saveGroup(group);
        Group group2 = new Group();
        group2.setName("group2");
        groupDao.saveGroup(group2);
        MetadataPermissions metadataPermissions = new MetadataPermissions();
        metadataPermissions.setMetadataUuid("aaa-bbb-ccc-mmm");
        Set<Permission> permissions = new HashSet<Permission>();
        Operation opJibx = new Operation();
        opJibx.setName(op.getName());
        Permission permission = new Permission();
        Group groupJibx = new Group();
        groupJibx.setUuid(group.getUuid());
        permission.setGroup(groupJibx);
        permission.setOperation(opJibx);
        permissions.add(permission);
        permission = new Permission();
        Group group2Jibx = new Group();
        group2Jibx.setUuid(group2.getUuid());
        permission.setGroup(group2Jibx);
        permission.setOperation(opJibx);
        permissions.add(permission);
        metadataPermissions.setPermissions(permissions);
        try {
            metadataPermissionsService.savePermissions(metadataPermissions);
            MetadataPermissions result = metadataPermissionsService.getPermissions("aaa-bbb-ccc-mmm");
            assertEquals("aaa-bbb-ccc-mmm", result.getMetadataUuid());
            assertEquals(2, result.getPermissions().size());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
