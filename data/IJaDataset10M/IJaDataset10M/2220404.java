package consciouscode.seedling;

import static consciouscode.seedling.NodePath.ROOT_PATH;
import static consciouscode.seedling.NodeReference.forBranch;
import static consciouscode.seedling.NodeReference.forChild;
import static consciouscode.seedling.NodeReference.forParts;
import static consciouscode.seedling.NodeReference.forPath;
import static consciouscode.seedling.Nodes.installChild;
import static consciouscode.seedling.Nodes.requiredChildBranch;
import static consciouscode.seedling.config.properties.PropertiesConfigTestCase.setConstructorProperty;
import consciouscode.seedling.junit.StandaloneSeedlingTestCase;
import consciouscode.seedling.tree.StandardBranch;
import java.util.Properties;

public class NodeReferenceTest extends StandaloneSeedlingTestCase {

    public static void missingRequiredNode(NodeReference ref) throws SeedlingException {
        try {
            ref.required();
            fail("Expected exception");
        } catch (NoSuchNodeException e) {
        }
    }

    public void testIntermediateNodeNotABranch() throws Exception {
        StandardBranch branch = getScratchBranch();
        branch.installChild("notbranch", new String());
        NodeReference addr = forPath(branch, "notbranch/node");
        assertEquals(null, addr.available());
        try {
            addr.required();
            fail("Expected exception");
        } catch (NoSuchNodeException e) {
            String path = forParts(branch, "notbranch").getLocalPath();
            assertTrue("Exception message missing path to bad node", e.getMessage().contains(path + " "));
        }
    }

    public void testdNodeCtorThrowsClassCast() throws Exception {
        StandardBranch branch = getScratchBranch();
        requiredChildBranch(branch, "intermediate");
        NodeReference addr = forPath(branch, "intermediate/node");
        Properties props = new Properties();
        setConstructorProperty(props, CtorThrowsCce.class);
        myConfigTree.setNodeConfiguration(addr.getLocalPath(), props);
        try {
            addr.available();
            fail("Expected exception");
        } catch (NodeInstantiationException e) {
            assertEquals(addr, e.getNodeReference());
        }
        try {
            addr.required();
            fail("Expected exception");
        } catch (NodeInstantiationException e) {
            assertEquals(addr, e.getNodeReference());
        }
    }

    /**
     * Isolates a bug in Seedling 0.6
     */
    public void testRequiredNodeStartServiceThrowsClassCast() throws Exception {
        StandardBranch branch = getScratchBranch();
        requiredChildBranch(branch, "intermediate");
        NodeReference addr = forPath(branch, "intermediate/node");
        Properties props = new Properties();
        setConstructorProperty(props, StartServiceThrowsCce.class);
        myConfigTree.setNodeConfiguration(addr.getLocalPath(), props);
        try {
            addr.available();
            fail("Expected exception");
        } catch (NoSuchNodeException e) {
            fail("Incorrect exception", e);
        } catch (NodeInstantiationException e) {
            assertEquals(addr, e.getNodeReference());
        }
        try {
            addr.required();
            fail("Expected exception");
        } catch (NoSuchNodeException e) {
            fail("Incorrect exception", e);
        } catch (NodeInstantiationException e) {
            assertEquals(addr, e.getNodeReference());
            assertEquals(addr.getLocalPath(), e.getLocalPath().toString());
        }
    }

    public void testConstructionFromPath() {
        StandardBranch branch = getScratchBranch();
        NodeLocation branchLoc = branch.getNodeLocation();
        NodeReference ref = forPath(branch, "");
        assertSame(branch, ref.getBaseBranch());
        assertSame(NodePath.IDENTITY_PATH, ref.getNodePath());
        assertEquals(branchLoc.toLocalPath(), ref.toLocalPath());
        assertEquals(branchLoc.getNodeName(), ref.getNodeName());
        assertFalse(ref.isRoot());
        assertEquals(branchLoc.parent(), ref.parent());
        ref = forPath(branch, "/");
        assertSame(branch, ref.getBaseBranch());
        assertSame(NodePath.ROOT_PATH, ref.getNodePath());
        assertSame(NodePath.ROOT_PATH, ref.toLocalPath());
        assertEquals("/", ref.getPath());
        assertEquals("/", ref.getLocalPath());
        assertEquals("/", ref.getNodeName());
        assertTrue(ref.isRoot());
        assertEquals(null, ref.parent());
        assertEquals(ref, branch.getLocalRoot().getNodeLocation());
        ref = forPath(branch, "/A");
        assertSame(branch, ref.getBaseBranch());
        assertEquals("/A", ref.getPath());
        assertEquals("/A", ref.getNodePath().toString());
        assertEquals("/A", ref.toLocalPath().toString());
        assertEquals("/A", ref.getLocalPath());
        assertEquals("A", ref.getNodeName());
        assertFalse(ref.isRoot());
        assertEquals(getGlobalRoot().getNodeLocation(), ref.parent());
    }

    public void testConstructionFromNoParts() {
        testConstructionFromNoParts((String[]) null);
        testConstructionFromNoParts(new String[0]);
    }

    public void testConstructionFromNoParts(String... parts) {
        StandardBranch branch = getScratchBranch();
        NodeLocation branchLoc = branch.getNodeLocation();
        NodeReference ref = forParts(branch, parts);
        assertSame(branch, ref.getBaseBranch());
        assertEquals(branchLoc.getNodeName(), ref.getNodeName());
        assertEquals(".", ref.getPath());
        assertEquals(".", ref.getNodePath().toString());
        assertEquals(branchLoc.getLocalPath(), ref.getLocalPath());
        assertEquals(branchLoc.toLocalPath(), ref.toLocalPath());
        assertFalse(ref.isRoot());
        assertEquals(branch.getParentBranch().getNodeLocation(), ref.parent());
    }

    public void testConstructionFromOnePart() {
        StandardBranch branch = getScratchBranch();
        NodeLocation branchLoc = branch.getNodeLocation();
        NodeReference ref = forParts(branch, "A");
        assertSame(branch, ref.getBaseBranch());
        assertEquals("A", ref.getNodeName());
        assertEquals("A", ref.getPath());
        assertEquals("A", ref.getNodePath().toString());
        assertEquals(branchLoc.toLocalPath().child("A"), ref.toLocalPath());
        assertFalse(ref.isRoot());
        assertEquals(branchLoc, ref.parent());
    }

    public void testConstructionFromTwoParts() {
        StandardBranch branch = getScratchBranch();
        NodeReference ref = forParts(branch, "A", "B");
        assertSame(branch, ref.getBaseBranch());
        assertEquals("B", ref.getNodeName());
        assertEquals("A/B", ref.getPath());
        assertEquals("A/B", ref.getNodePath().toString());
        assertEquals(branch.getNodeLocation().toLocalPath().parts("A", "B"), ref.toLocalPath());
        assertEquals(forParts(branch, "A"), ref.parent());
    }

    public void testGetPaths() throws Exception {
        NodeReference ref = forPath(getGlobalRoot(), ".");
        assertEquals("/", ref.getLocalPath());
        assertEquals("//", ref.getGlobalPath());
        ref = forPath(getGlobalRoot(), "Child");
        assertEquals("/Child", ref.getLocalPath());
        assertEquals("//Child", ref.getGlobalPath());
        ref = forPath(getGlobalRoot(), "/Child");
        assertEquals("/Child", ref.getLocalPath());
        assertEquals("//Child", ref.getGlobalPath());
        BranchNode branch = getScratchBranch();
        ref = forPath(branch, "/Child");
        assertEquals("/Child", ref.getLocalPath());
        assertEquals("//Child", ref.getGlobalPath());
        ref = forChild(branch, "Child");
        NodeLocation branchLoc = branch.getNodeLocation();
        assertEquals(branchLoc.getLocalPath() + "/Child", ref.getLocalPath());
        assertEquals(branchLoc.getGlobalPath() + "/Child", ref.getGlobalPath());
    }

    public void testToLocalPath() throws Exception {
        NodeReference ref = forPath(getGlobalRoot(), ".");
        assertSame(ROOT_PATH, ref.toLocalPath());
        ref = forPath(getGlobalRoot(), "Child");
        assertEquals(NodePath.forPath("/Child"), ref.toLocalPath());
        ref = forPath(getGlobalRoot(), "/Child");
        assertEquals(NodePath.forPath("/Child"), ref.toLocalPath());
        BranchNode branch = getScratchBranch();
        ref = forPath(branch, "/Child");
        assertEquals(NodePath.forPath("/Child"), ref.toLocalPath());
        ref = forChild(branch, "Child");
        NodeLocation branchLoc = branch.getNodeLocation();
        NodePath branchPath = branchLoc.toLocalPath();
        assertEquals(branchPath.child("Child"), ref.toLocalPath());
    }

    public void testRootLocation() throws Exception {
        BranchNode root = getGlobalRoot();
        NodeLocation rootLoc = root.getNodeLocation();
        assertSame(NodePath.ROOT_PATH_STRING, rootLoc.toString());
        assertSame(NodePath.ROOT_PATH_STRING, rootLoc.getLocalPath());
        assertSame(NodePath.ROOT_NAME, rootLoc.getNodeName());
        assertSame(NodePath.IDENTITY_PATH, rootLoc.getNodePath());
        assertSame(NodePath.ROOT_PATH, rootLoc.toLocalPath());
        assertSame(root, rootLoc.getBaseBranch());
        assertSame(null, rootLoc.getParentBranch());
        assertSame(root, rootLoc.available());
        assertSame(root, rootLoc.installed());
        assertSame(root, rootLoc.required());
        assertSame(null, rootLoc.parent());
        assertTrue(rootLoc.isRoot());
        assertTrue(rootLoc.child("foo").parent().isRoot());
    }

    public void testRootLocationCreation() {
        RootNode root = getGlobalRoot();
        NodeLocation rootLoc = root.getNodeLocation();
        assertSame(rootLoc, forBranch(root));
        String[] pathParts = {};
        assertSame(rootLoc, forParts(root, pathParts));
        assertSame(rootLoc, forParts(root, ((String[]) null)));
        assertSame(rootLoc, forParts(root, (new String[0])));
        assertSame(rootLoc, forPath(root, ((String) null)));
        assertSame(rootLoc, forPath(root, ""));
        assertSame(rootLoc, forPath(root, "."));
        assertSame(rootLoc, forPath(root, ((NodePath) null)));
        assertSame(rootLoc, forPath(root, NodePath.IDENTITY_PATH));
        assertSame(rootLoc, rootLoc.child(null));
        assertSame(rootLoc, rootLoc.child(""));
        assertSame(rootLoc, rootLoc.parts((String[]) null));
        assertSame(rootLoc, rootLoc.parts(new String[0]));
        assertSame(rootLoc, rootLoc.path((NodePath) null));
        assertSame(rootLoc, rootLoc.path(NodePath.IDENTITY_PATH));
        assertSame(rootLoc, rootLoc.path((String) null));
        assertSame(rootLoc, rootLoc.path(""));
        assertSame(rootLoc, rootLoc.path("."));
        assertSame(rootLoc, rootLoc.child("foo").parent());
        assertSame(null, rootLoc.parts("/").parent());
        assertSame(null, rootLoc.parts("/", "foo").parent().parent());
    }

    public void testGenerics() throws Exception {
        StandardBranch branch = getScratchBranch();
        Nodes.installChild(branch, "c", "text");
        NodeReference ref = forChild(branch, "c");
        String s = ref.available();
        s = ref.installed();
        s = ref.required();
        assertEquals("text", s);
    }

    public void testAvailableNode() throws Exception {
        BranchNode base = getGlobalRoot();
        BranchNode branchA = requiredChildBranch(base, "A");
        BranchNode branchB = requiredChildBranch(base, "B");
        Object child = new Object();
        installChild(branchA, "child", child);
        NodeReference ref = forPath(branchA, "");
        assertSame(branchA, ref.available());
        ref = forPath(branchA, "/nowhere");
        assertSame(null, ref.available());
        ref = forPath(branchA, "nowhere");
        assertSame(null, ref.available());
        ref = forPath(branchA, "/A");
        assertSame(branchA, ref.available());
        ref = forPath(branchA, "child");
        assertSame(child, ref.available());
        ref = forPath(branchA, "/A/nowhere/child");
        assertSame(null, ref.available());
        ref = forPath(branchB, "/A/nowhere/child");
        assertSame(null, ref.available());
        ref = forPath(branchA, "/A/child");
        assertSame(child, ref.available());
        ref = forPath(branchB, "/A/child");
        assertSame(child, ref.available());
        ref = forPath(branchB, "/C/child");
        assertSame(null, ref.available());
        ref = forPath(branchB, "/A/child/child");
        assertSame(null, ref.available());
    }

    public void testInstalledNode() throws Exception {
        BranchNode base = getGlobalRoot();
        BranchNode branchA = requiredChildBranch(base, "A");
        BranchNode branchB = requiredChildBranch(base, "B");
        Object child = new Object();
        installChild(branchA, "child", child);
        NodeReference ref = forPath(branchA, "");
        assertSame(branchA, ref.installed());
        ref = forPath(branchA, "/nowhere");
        assertSame(null, ref.installed());
        ref = forPath(branchA, "nowhere");
        assertSame(null, ref.installed());
        ref = forPath(branchA, "/A");
        assertSame(branchA, ref.installed());
        ref = forPath(branchA, "child");
        assertSame(child, ref.installed());
        ref = forPath(branchA, "/A/nowhere/child");
        assertSame(null, ref.installed());
        ref = forPath(branchB, "/A/nowhere/child");
        assertSame(null, ref.installed());
        ref = forPath(branchA, "/A/child");
        assertSame(child, ref.installed());
        ref = forPath(branchB, "/A/child");
        assertSame(child, ref.installed());
        ref = forPath(branchB, "/C/child");
        assertSame(null, ref.installed());
        ref = forPath(branchB, "/A/child/child");
        assertSame(null, ref.installed());
    }

    public void testRequiredNode() throws Exception {
        BranchNode base = getGlobalRoot();
        BranchNode branchA = requiredChildBranch(base, "A");
        BranchNode branchB = requiredChildBranch(base, "B");
        Object child = new Object();
        installChild(branchA, "child", child);
        NodeReference ref = forPath(branchA, "");
        assertSame(branchA, ref.required());
        ref = forPath(branchA, "/nowhere");
        missingRequiredNode(ref);
        ref = forPath(branchA, "nowhere");
        missingRequiredNode(ref);
        ref = forPath(branchA, "/A");
        assertSame(branchA, ref.required());
        ref = forPath(branchA, "child");
        assertSame(child, ref.required());
        ref = forPath(branchA, "/A/child");
        assertSame(child, ref.required());
        ref = forPath(branchB, "/A/child");
        assertSame(child, ref.required());
        ref = forPath(branchB, "/C/child");
        missingRequiredNode(ref);
        ref = forPath(branchB, "/A/child/child");
        missingRequiredNode(ref);
    }

    public static class CtorThrowsCce {

        public CtorThrowsCce() {
            throw new ClassCastException("take that!");
        }
    }

    public static class StartServiceThrowsCce implements ServiceNode {

        public void startService() {
            throw new ClassCastException("take that!");
        }

        public void stopService() {
        }
    }
}
