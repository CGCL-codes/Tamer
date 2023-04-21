package com.volantis.mcs.runtime.policies.cache;

import com.volantis.cache.CacheMock;
import com.volantis.cache.group.Group;
import com.volantis.cache.group.GroupMock;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.project.remote.RemotePolicySourceMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.value.ExpectedValue;

/**
 * Test cases for {@link PolicyCache}.
 */
public class PolicyCacheTestCase extends TestCaseAbstract {

    private static final ExpectedValue REMOTE_GROUP_KEY = mockFactory.expectsToStringOf("<REMOTE_GROUP_KEY>");

    private static final ExpectedValue LOCAL_GROUP_KEY = mockFactory.expectsToStringOf("<LOCAL_GROUP_KEY>");

    private static final ExpectedValue DEFAULT_REMOTE_GROUP_KEY = mockFactory.expectsToStringOf("<REMOTE_DEFAULT_GROUP_KEY>");

    private static final ExpectedValue DEFAULT_LOCAL_GROUP_KEY = mockFactory.expectsToStringOf("<LOCAL_DEFAULT_GROUP_KEY>");

    private CacheMock cacheMock;

    private RemotePartitionsMock partitionsMock;

    private GroupMock rootGroupMock;

    private GroupMock defaultRemoteGroupMock;

    private RuntimeProjectMock projectMock;

    private GroupMock defaultLocalGroupMock;

    private PolicyCachePartitionConstraintsMock localPartitionConstraintsMock;

    private PolicyCachePartitionConstraintsMock remotePartitionConstraintsMock;

    private GroupMock localGroupMock;

    private GroupMock remoteGroupMock;

    private PolicyCacheImpl policyCache;

    private RemotePolicySourceMock sourceMock;

    protected void setUp() throws Exception {
        super.setUp();
        cacheMock = new CacheMock("cacheMock", expectations);
        rootGroupMock = new GroupMock("rootGroupMock", expectations);
        partitionsMock = new RemotePartitionsMock("partitionsMock", expectations);
        localPartitionConstraintsMock = new PolicyCachePartitionConstraintsMock("localPartitionConstraintsMock", expectations);
        remotePartitionConstraintsMock = new PolicyCachePartitionConstraintsMock("remotePartitionConstraintsMock", expectations);
        localGroupMock = new GroupMock("localGroupMock", expectations);
        defaultLocalGroupMock = new GroupMock("defaultLocalGroupMock", expectations);
        remoteGroupMock = new GroupMock("remoteGroupMock", expectations);
        defaultRemoteGroupMock = new GroupMock("defaultRemoteGroupMock", expectations);
        projectMock = new RuntimeProjectMock("projectMock", expectations);
        sourceMock = new RemotePolicySourceMock("sourceMock", expectations);
        cacheMock.expects.getRootGroup().returns(rootGroupMock).any();
        rootGroupMock.expects.getGroup(PolicyCacheImpl.LOCAL_GROUP_KEY).returns(localGroupMock).any();
        localGroupMock.expects.getGroup(PolicyCacheImpl.LOCAL_DEFAULT_GROUP_KEY).returns(defaultLocalGroupMock).any();
        rootGroupMock.expects.getGroup(PolicyCacheImpl.REMOTE_GROUP_KEY).returns(remoteGroupMock).any();
        remoteGroupMock.expects.getGroup(PolicyCacheImpl.REMOTE_DEFAULT_GROUP_KEY).returns(defaultRemoteGroupMock).any();
        projectMock.expects.getPolicySource().returns(sourceMock).any();
        policyCache = new PolicyCacheImpl(cacheMock, partitionsMock, localPartitionConstraintsMock, remotePartitionConstraintsMock);
    }

    /**
     * Ensure that it is possible to select a group for a remote policy that
     * could not be found and does not belong to a partiton.
     */
    public void testSelectGroupForUnavailableRemotePolicyNoPartition() throws Exception {
        partitionsMock.expects.getRemotePartition("http://host/partition").returns(null);
        Group group = policyCache.selectGroup("http://host/partition", null);
        assertSame(defaultRemoteGroupMock, group);
    }

    /**
     * Ensure that it is possible to select a group for a local policy that
     * could not be found and is in a project that has no cache group.
     */
    public void testSelectGroupForUnavailableLocalPolicyNoProjectCacheGroup() throws Exception {
        ProjectSpecificKey key = new ProjectSpecificKey(projectMock, "/fred.mimg");
        projectMock.expects.isRemote().returns(false).any();
        projectMock.expects.getCacheGroup().returns(null);
        Group group = policyCache.selectGroup(key, null);
        assertSame(defaultLocalGroupMock, group);
    }

    /**
     * Ensure that the key for a local project relative policy is a key
     * consisting ot the project and the path.
     */
    public void testGetKeyProjectRelativeLocalProject() {
        projectMock.expects.getContainsOrphans().returns(false).any();
        projectMock.expects.isRemote().returns(false).any();
        Object key = policyCache.getKey(projectMock, "/fred.mimg");
        assertEquals(new ProjectSpecificKey(projectMock, "/fred.mimg"), key);
    }

    /**
     * Ensure that the key for a remote project relative policy is the fully
     * qualified URL.
     */
    public void testGetKeyProjectRelativeRemoteProject() {
        projectMock.expects.makeAbsolutePolicyURL("/fred.mimg").returns("http://remote:8080/project/fred.mimg").any();
        projectMock.expects.getContainsOrphans().returns(false).any();
        projectMock.expects.isRemote().returns(true).any();
        Object key = policyCache.getKey(projectMock, "/fred.mimg");
        assertEquals("http://remote:8080/project/fred.mimg", key);
    }

    /**
     * Ensure that the key for the global project absolute policy is the fully
     * qualified URL.
     */
    public void testGetKeyProjectRelativeGlobalProject() {
        projectMock.expects.getContainsOrphans().returns(true).any();
        projectMock.expects.isRemote().returns(true).any();
        try {
            Object key = policyCache.getKey(projectMock, "/fred.mimg");
            fail("No key was expected but created key of '" + key + "'");
        } catch (IllegalArgumentException expected) {
            assertEquals("Project is global but name '/fred.mimg' " + "is project relative", expected.getMessage());
        }
    }

    /**
     * Ensure that an error is thrown if an attempt is made to get a key for a
     * policy with an absolute name from a local project.
     */
    public void testGetKeyAbsoluteLocalProject() {
        projectMock.expects.getContainsOrphans().returns(false).any();
        projectMock.expects.isRemote().returns(false).any();
        try {
            Object key = policyCache.getKey(projectMock, "http://remote:8080/project/fred.mimg");
            fail("No key was expected but created key of '" + key + "'");
        } catch (IllegalArgumentException expected) {
            assertEquals("Project is not remote and name " + "'http://remote:8080/project/fred.mimg' " + "is not project relative", expected.getMessage());
        }
    }

    /**
     * Ensure that an error is thrown if an attempt is made to get a key for a
     * policy with an absolute name from a remote, non global project.
     */
    public void testGetKeyAbsoluteRemoteProject() {
        sourceMock.expects.getBaseURLWithoutTrailingSlash().returns("http://remote:8080/project").any();
        projectMock.expects.getContainsOrphans().returns(false).any();
        projectMock.expects.isRemote().returns(true).any();
        Object key = policyCache.getKey(projectMock, "http://remote:8080/project/fred.mimg");
        assertEquals("http://remote:8080/project/fred.mimg", key);
    }

    /**
     * Ensure that the key for the global project absolute policy is the fully
     * qualified URL.
     */
    public void testGetKeyAbsoluteGlobalProject() {
        projectMock.expects.getContainsOrphans().returns(true).any();
        projectMock.expects.isRemote().returns(true).any();
        Object key = policyCache.getKey(projectMock, "http://remote:8080/project/fred.mimg");
        assertEquals("http://remote:8080/project/fred.mimg", key);
    }
}
