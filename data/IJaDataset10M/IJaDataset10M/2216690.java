package org.helianto.core.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.easymock.EasyMock;
import org.helianto.core.Credential;
import org.helianto.core.Identity;
import org.helianto.core.Province;
import org.helianto.core.PublicEntity;
import org.helianto.core.User;
import org.helianto.core.UserAssociation;
import org.helianto.core.UserGroup;
import org.helianto.core.UserLog;
import org.helianto.core.UserRole;
import org.helianto.core.def.ActivityState;
import org.helianto.core.filter.Filter;
import org.helianto.core.filter.TestingFilter;
import org.helianto.core.repository.FilterDao;
import org.helianto.core.test.CredentialTestSupport;
import org.helianto.core.test.UserGroupTestSupport;
import org.helianto.core.test.UserRoleTestSupport;
import org.helianto.core.test.UserTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mauricio Fernandes de Castro
 */
public class UserMgrImplTests {

    @Test
    public void userState() {
        User user = UserTestSupport.createUser();
        Credential credential = CredentialTestSupport.createCredential(user.getIdentity());
        assertEquals(ActivityState.ACTIVE.getValue(), user.getUserState());
        assertEquals(ActivityState.INITIAL.getValue(), credential.getCredentialState());
    }

    @Test
    public void findUsers() {
        Filter userFilter = new TestingFilter();
        List<UserGroup> userList = new ArrayList<UserGroup>();
        expect(userGroupDao.find(userFilter)).andReturn(userList);
        replay(userGroupDao);
        assertSame(userList, userMgr.findUsers(userFilter));
        verify(userGroupDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void storeUserGroupNullKey() {
        UserGroup userGroup = new UserGroup();
        userGroup.setUserKey(null);
        userMgr.storeUserGroup(userGroup);
    }

    @Test(expected = IllegalArgumentException.class)
    public void storeUserGroupEmptyKey() {
        UserGroup userGroup = new UserGroup();
        userGroup.setUserKey("");
        userMgr.storeUserGroup(userGroup);
    }

    @Test
    public void storeUserGroup() {
        UserGroup userGroup = UserGroupTestSupport.createUserGroup();
        userGroupDao.saveOrUpdate(userGroup);
        userGroupDao.flush();
        replay(userGroupDao);
        assertSame(userGroup, userMgr.storeUserGroup(userGroup));
        verify(userGroupDao);
    }

    @Test
    public void publicEntity() {
        UserGroup userGroup = UserGroupTestSupport.createUserGroup();
        userGroup.getEntity().setNature("S, E");
        PublicEntity publicEntity = new PublicEntity(userGroup.getEntity());
        userGroupDao.saveOrUpdate(userGroup);
        userGroupDao.flush();
        replay(userGroupDao);
        EasyMock.expect(publicEntityMgr.installPublicEntity(userGroup.getEntity())).andReturn(publicEntity);
        EasyMock.expect(publicEntityMgr.storePublicEntity(EasyMock.eq(publicEntity))).andReturn(publicEntity);
        replay(publicEntityMgr);
        assertSame(userGroup, userMgr.storeUserGroup(userGroup));
        verify(userGroupDao);
    }

    @Test
    public void findUserGroupParentRoot() {
        UserGroup userGroup = UserGroupTestSupport.createUserGroup();
        userGroupDao.saveOrUpdate(userGroup);
        userGroupDao.refresh(userGroup);
        replay(userGroupDao);
        List<UserGroup> expectedUserGroupList = userMgr.findParentChain(userGroup);
        verify(userGroupDao);
        assertEquals(0, expectedUserGroupList.size());
    }

    @Test
    public void findUserGroupParentLevel1() {
        UserGroup userGroup = UserGroupTestSupport.createUserGroup();
        User user = UserTestSupport.createUser();
        UserAssociation association = new UserAssociation(userGroup, user);
        user.getParentAssociations().add(association);
        userGroupDao.saveOrUpdate(user);
        userGroupDao.refresh(user);
        replay(userGroupDao);
        List<UserGroup> expectedUserGroupList = userMgr.findParentChain(user);
        verify(userGroupDao);
        assertSame(userGroup, expectedUserGroupList.get(0));
    }

    @Test
    public void storeUserAssociation() {
        UserAssociation parentAssociation = new UserAssociation();
        userAssociationDao.saveOrUpdate(parentAssociation);
        replay(userAssociationDao);
        assertSame(parentAssociation, userMgr.storeUserAssociation(parentAssociation));
        verify(userAssociationDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void persistUserLogError() {
        userMgr.storeUserLog(new User(), new Date());
    }

    @Test
    public void storeUserLog() {
        Date date = new Date();
        Identity identity = new Identity();
        User user = new User();
        user.setIdentity(identity);
        userLogDao.saveOrUpdate(isA(UserLog.class));
        replay(userLogDao);
        userMgr.storeUserLog(user, date);
        verify(userLogDao);
    }

    @Test
    public void findUserRoles() {
        List<UserRole> userRoleList = new ArrayList<UserRole>();
        Filter filter = new TestingFilter();
        expect(userRoleDao.find(filter)).andReturn(userRoleList);
        replay(userRoleDao);
        assertSame(userRoleList, userMgr.findUserRoles(filter));
        verify(userRoleDao);
    }

    @Test
    public void storeUserRole() {
        UserRole userRole = UserRoleTestSupport.createUserRole();
        userRoleDao.saveOrUpdate(userRole);
        userRoleDao.flush();
        replay(userRoleDao);
        assertSame(userRole, userMgr.storeUserRole(userRole));
        verify(userRoleDao);
    }

    private UserMgrImpl userMgr;

    private FilterDao<UserGroup> userGroupDao;

    private FilterDao<UserAssociation> userAssociationDao;

    private FilterDao<Province> provinceDao;

    private FilterDao<UserLog> userLogDao;

    private FilterDao<UserRole> userRoleDao;

    private IdentityMgr identityMgr;

    private PublicEntityMgr publicEntityMgr;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        userMgr = new UserMgrImpl();
        userGroupDao = createMock(FilterDao.class);
        userMgr.setUserGroupDao(userGroupDao);
        userAssociationDao = createMock(FilterDao.class);
        userMgr.setUserAssociationDao(userAssociationDao);
        provinceDao = createMock(FilterDao.class);
        userMgr.setProvinceDao(provinceDao);
        userLogDao = createMock(FilterDao.class);
        userMgr.setUserLogDao(userLogDao);
        userRoleDao = createMock(FilterDao.class);
        userMgr.setUserRoleDao(userRoleDao);
        identityMgr = createMock(IdentityMgr.class);
        userMgr.setIdentityMgr(identityMgr);
        publicEntityMgr = createMock(PublicEntityMgr.class);
        userMgr.setPublicEntityMgr(publicEntityMgr);
    }

    @After
    public void tearDown() {
        reset(userGroupDao);
        reset(userAssociationDao);
        reset(provinceDao);
        reset(userLogDao);
        reset(userRoleDao);
        reset(identityMgr);
        reset(publicEntityMgr);
    }
}
