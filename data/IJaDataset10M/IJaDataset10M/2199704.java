package de.humanfork.treemerge.match;

import de.humanfork.treemerge.help.TestHelper;
import de.humanfork.treemerge.indentation.tree.TestNode;
import de.humanfork.treemerge.tree.Node;
import de.humanfork.treemerge.tree.TreeType;
import de.humanfork.treemerge.tree.creator.RoleCreator;
import junit.framework.TestCase;

public class MatchTest extends TestCase {

    /**
     * Test method for 'de.humanfork.treemerge.match.Match.inSequence(TMergeNode)'
     * No change - no sequence problem
     */
    public void testInSequence_0() {
        RoleCreator roleCreator = new RoleCreator();
        TestNode rB = new TestNode("root");
        TestNode aB = new TestNode("a", rB);
        TestNode bB = new TestNode("b", rB);
        roleCreator.addBaseMergeRoleToTree(rB);
        TestNode r1 = new TestNode("root");
        TestNode a1 = new TestNode("a", r1);
        TestNode b1 = new TestNode("b", r1);
        roleCreator.addTMergeRoleToTree(r1, TreeType.T1);
        TestHelper.registerMatchesPairwise(new Node[] { rB, aB, bB }, new Node[] { r1, a1, b1 });
        assertTrue(Match.inSequence(a1.getTMergeRole()));
        assertTrue(Match.inSequence(b1.getTMergeRole()));
    }

    /**
     * Test method for 'de.humanfork.treemerge.match.Match.inSequence(TMergeNode)'
     * order changed
     */
    public void testInSequence_1() {
        RoleCreator roleCreator = new RoleCreator();
        TestNode rB = new TestNode("root");
        TestNode aB = new TestNode("a", rB);
        TestNode bB = new TestNode("b", rB);
        roleCreator.addBaseMergeRoleToTree(rB);
        TestNode r1 = new TestNode("root");
        TestNode b1 = new TestNode("b", r1);
        TestNode a1 = new TestNode("a", r1);
        roleCreator.addTMergeRoleToTree(r1, TreeType.T1);
        TestHelper.registerMatchesPairwise(new Node[] { rB, aB, bB }, new Node[] { r1, a1, b1 });
        assertFalse(Match.inSequence(a1.getTMergeRole()));
        assertFalse(Match.inSequence(b1.getTMergeRole()));
    }

    /**
     * Test method for 'de.humanfork.treemerge.match.Match.inSequence(TMergeNode)'
     * first node deleted
     */
    public void testInSequence_2() {
        RoleCreator roleCreator = new RoleCreator();
        TestNode rB = new TestNode("root");
        new TestNode("a", rB);
        TestNode bB = new TestNode("b", rB);
        roleCreator.addBaseMergeRoleToTree(rB);
        TestNode r1 = new TestNode("root");
        TestNode b1 = new TestNode("b", r1);
        roleCreator.addTMergeRoleToTree(r1, TreeType.T1);
        TestHelper.registerMatchesPairwise(new Node[] { rB, bB }, new Node[] { r1, b1 });
        assertTrue(Match.inSequence(b1.getTMergeRole()));
    }

    /**
     * Test method for 'de.humanfork.treemerge.match.Match.inSequence(TMergeNode)'
     * Middle node deleted
     */
    public void testInSequence_3() {
        RoleCreator roleCreator = new RoleCreator();
        TestNode rB = new TestNode("root");
        TestNode aB = new TestNode("a", rB);
        new TestNode("b", rB);
        TestNode cB = new TestNode("c", rB);
        roleCreator.addBaseMergeRoleToTree(rB);
        TestNode r1 = new TestNode("root");
        TestNode a1 = new TestNode("a", r1);
        TestNode c1 = new TestNode("c", r1);
        roleCreator.addTMergeRoleToTree(r1, TreeType.T1);
        TestHelper.registerMatchesPairwise(new Node[] { rB, aB, cB }, new Node[] { r1, a1, c1 });
        assertTrue(Match.inSequence(a1.getTMergeRole()));
        assertTrue(Match.inSequence(c1.getTMergeRole()));
    }

    /**
     * Test method for 'de.humanfork.treemerge.match.Match.inSequence(TMergeNode)'
     * Middle and last node changed
     */
    public void testInSequence_4() {
        RoleCreator roleCreator = new RoleCreator();
        TestNode rB = new TestNode("root");
        TestNode aB = new TestNode("a", rB);
        TestNode bB = new TestNode("b", rB);
        TestNode cB = new TestNode("c", rB);
        roleCreator.addBaseMergeRoleToTree(rB);
        TestNode r1 = new TestNode("root");
        TestNode a1 = new TestNode("a", r1);
        TestNode c1 = new TestNode("c", r1);
        TestNode b1 = new TestNode("b", r1);
        roleCreator.addTMergeRoleToTree(r1, TreeType.T1);
        TestHelper.registerMatchesPairwise(new Node[] { rB, aB, bB, cB }, new Node[] { r1, a1, b1, c1 });
        assertTrue("expected a in sequence", Match.inSequence(a1.getTMergeRole()));
        assertFalse("expected b not in sequence", Match.inSequence(b1.getTMergeRole()));
        assertFalse("expected c not in sequence", Match.inSequence(c1.getTMergeRole()));
    }

    /**
     * Test method for 'de.humanfork.treemerge.match.Match.inSequence(TMergeNode)'
     * first Node moved far away
     */
    public void testInSequence_5() {
        RoleCreator roleCreator = new RoleCreator();
        TestNode rB = new TestNode("root");
        TestNode aB = new TestNode("a", rB);
        TestNode bB = new TestNode("b", rB);
        TestNode cB = new TestNode("c", rB);
        roleCreator.addBaseMergeRoleToTree(rB);
        TestNode r1 = new TestNode("root");
        TestNode b1 = new TestNode("b", r1);
        TestNode c1 = new TestNode("c", r1);
        TestNode a1 = new TestNode("a", c1);
        roleCreator.addTMergeRoleToTree(r1, TreeType.T1);
        TestHelper.registerMatchesPairwise(new Node[] { rB, aB, bB, cB }, new Node[] { r1, a1, b1, c1 });
        assertTrue("expected a in sequence", Match.inSequence(a1.getTMergeRole()));
        assertTrue("expected b in sequence", Match.inSequence(b1.getTMergeRole()));
        assertTrue("expected c in sequence", Match.inSequence(c1.getTMergeRole()));
    }

    /**
    * Test method for 'de.humanfork.treemerge.match.Match.inSequence(TMergeNode)'
    * middle Node moved far away
    */
    public void testInSequence_6() {
        RoleCreator roleCreator = new RoleCreator();
        TestNode rB = new TestNode("root");
        TestNode aB = new TestNode("a", rB);
        TestNode bB = new TestNode("b", rB);
        TestNode cB = new TestNode("c", rB);
        roleCreator.addBaseMergeRoleToTree(rB);
        TestNode r1 = new TestNode("root");
        TestNode a1 = new TestNode("a", r1);
        TestNode c1 = new TestNode("c", r1);
        TestNode b1 = new TestNode("b", c1);
        roleCreator.addTMergeRoleToTree(r1, TreeType.T1);
        TestHelper.registerMatchesPairwise(new Node[] { rB, aB, bB, cB }, new Node[] { r1, a1, b1, c1 });
        assertTrue("expected a in sequence", Match.inSequence(a1.getTMergeRole()));
        assertTrue("expected b in sequence", Match.inSequence(b1.getTMergeRole()));
        assertTrue("expected c in sequence", Match.inSequence(c1.getTMergeRole()));
    }
}
