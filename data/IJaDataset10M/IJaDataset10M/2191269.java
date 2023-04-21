package org.apache.zookeeper.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.junit.Assert;
import org.junit.Test;

public class ACLRootTest extends ClientBase {

    private static final Logger LOG = LoggerFactory.getLogger(ACLRootTest.class);

    @Test
    public void testRootAcl() throws Exception {
        ZooKeeper zk = createClient();
        try {
            zk.addAuthInfo("digest", "pat:test".getBytes());
            zk.setACL("/", Ids.CREATOR_ALL_ACL, -1);
            zk.getData("/", false, null);
            zk.close();
            zk = createClient();
            try {
                zk.getData("/", false, null);
                Assert.fail("validate auth");
            } catch (KeeperException.NoAuthException e) {
            }
            try {
                zk.create("/apps", null, Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
                Assert.fail("validate auth");
            } catch (KeeperException.InvalidACLException e) {
            }
            zk.addAuthInfo("digest", "world:anyone".getBytes());
            try {
                zk.create("/apps", null, Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
                Assert.fail("validate auth");
            } catch (KeeperException.NoAuthException e) {
            }
            zk.close();
            zk = createClient();
            zk.addAuthInfo("digest", "pat:test".getBytes());
            zk.getData("/", false, null);
            zk.create("/apps", null, Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
            zk.delete("/apps", -1);
            zk.setACL("/", Ids.OPEN_ACL_UNSAFE, -1);
            zk.close();
            zk = createClient();
            zk.getData("/", false, null);
            zk.create("/apps", null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            try {
                zk.create("/apps", null, Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
                Assert.fail("validate auth");
            } catch (KeeperException.InvalidACLException e) {
            }
            zk.delete("/apps", -1);
            zk.addAuthInfo("digest", "world:anyone".getBytes());
            zk.create("/apps", null, Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
            zk.close();
            zk = createClient();
            zk.delete("/apps", -1);
        } finally {
            zk.close();
        }
    }
}
