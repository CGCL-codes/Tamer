package org.apache.servicemix.audit.jcr;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.io.IOException;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import org.apache.servicemix.audit.async.RepoHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RepositoryUtilsTest {

    private RepoHolder repoHolder;

    private Session session;

    @Test
    public synchronized void getReturnsNullOrNode() throws Exception {
        Node root = session.getRootNode();
        assertNull(RepositoryUtils.get(root, "my-node"));
        root.addNode("my-node", "nt:folder");
        session.save();
        assertNotNull(RepositoryUtils.get(root, "my-node"));
    }

    @Test
    public void getOrCreateAlwaysReturnsNode() throws Exception {
        Node root = session.getRootNode();
        assertNull(RepositoryUtils.get(root, "my-node"));
        assertNotNull(RepositoryUtils.getOrCreate(root, "my-node"));
        session.save();
        assertNotNull(RepositoryUtils.get(root, "my-node"));
    }

    @Test
    public void getOrCreateAddsParentNodes() throws Exception {
        Node root = session.getRootNode();
        assertNull(RepositoryUtils.get(root, "parent"));
        assertNotNull(RepositoryUtils.getOrCreate(root, "parent/my-node"));
        session.save();
        assertNotNull(RepositoryUtils.get(root, "parent/my-node"));
    }

    @Test
    public void clearRemovesAllChildren() throws Exception {
        Node root = session.getRootNode();
        RepositoryUtils.getOrCreate(root, "parent/my-node");
        RepositoryUtils.getOrCreate(root, "my-node");
        session.save();
        RepositoryUtils.clear(root);
        session.save();
        assertNull(RepositoryUtils.get(root, "parent/my-node"));
        assertNull(RepositoryUtils.get(root, "my-node"));
    }

    @BeforeClass
    public static void initRepository() throws IOException {
    }

    @Before
    public void init() throws Exception {
        repoHolder = RepoHolder.getInstance();
        session = repoHolder.getRepository().login(new SimpleCredentials("admin", "admin".toCharArray()));
        RepositoryUtils.clear(session.getRootNode());
        session.save();
    }

    @After
    public void destroy() {
        if (session != null) {
            session.logout();
            System.out.println("@@@@@@@@@@@@@@@@@@RepositoryUtilsTest: logout");
        }
    }
}
