package org.rubypeople.rdt.refactoring.tests.util;

import junit.framework.TestCase;
import org.jruby.ast.BlockNode;
import org.jruby.ast.ClassNode;
import org.jruby.ast.DefnNode;
import org.jruby.ast.DefsNode;
import org.jruby.ast.InstVarNode;
import org.jruby.ast.IterNode;
import org.jruby.ast.RootNode;
import org.jruby.parser.BlockStaticScope;
import org.jruby.runtime.DynamicScope;
import org.rubypeople.rdt.refactoring.util.NodeUtil;

public class TC_NodeUtil extends TestCase {

    public void testHasScope_RootNode() {
        assertTrue(NodeUtil.hasScope(new RootNode(null, new DynamicScope(new BlockStaticScope(null), null), null)));
    }

    public void testHasScope_DefnNode() {
        assertTrue(NodeUtil.hasScope(new DefnNode(null, null, null, null, null, null)));
    }

    public void testHasScope_DefsNode() {
        assertTrue(NodeUtil.hasScope(new DefsNode(null, null, null, null, null, null)));
    }

    public void testHasScope_IterNode() {
        assertTrue(NodeUtil.hasScope(new IterNode(null, null, null, null)));
    }

    public void testHasScope_ClassNode() {
        assertTrue(NodeUtil.hasScope(new ClassNode(null, null, null, null, null)));
    }

    public void testHasNoScope_BlockNode() {
        assertFalse(NodeUtil.hasScope(new BlockNode(null)));
    }

    public void testHasNoScope_VarNode() {
        assertFalse(NodeUtil.hasScope(new InstVarNode(null, "")));
    }
}
