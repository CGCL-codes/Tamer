package net.sf.sasl.aop.common.grammar.placeholder.syntax;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the
 * {@link net.sf.sasl.aop.common.grammar.placeholder.syntax.ASTNode ASTNode}
 * class.
 * 
 * @author Philipp Förmer
 * 
 */
public class ASTNodeTest {

    /**
	 * The unit under test.
	 */
    private ASTNode underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new ASTNodeImpl();
    }

    /**
	 * Test cases for
	 * {@link net.sf.sasl.aop.common.grammar.placeholder.syntax.ASTNode#ASTNode()
	 * ASTNode()}
	 * 
	 * @throws Exception
	 */
    @Test
    public void testDefaultConstructor() throws Exception {
        Assert.assertNull(underTest.getParentNode());
        Assert.assertTrue(underTest.isRootNode());
    }

    /**
	 * Test cases for
	 * {@link net.sf.sasl.aop.common.grammar.placeholder.syntax.ASTNode#ASTNode(ASTNode)
	 * ASTNode(ASTNode)}
	 * 
	 * @throws Exception
	 */
    @Test
    public void testParentNodeConstructor() throws Exception {
        ASTNode parentNode = new ASTNodeImpl();
        underTest = new ASTNodeImpl(parentNode);
        Assert.assertEquals(parentNode, underTest.getParentNode());
        Assert.assertFalse(underTest.isRootNode());
    }

    /**
	 * Test cases for the getter and setter of the parent node.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testGetSetParentNode() throws Exception {
        ASTNode parentNode = new ASTNodeImpl();
        underTest.setParentNode(parentNode);
        Assert.assertEquals(parentNode, underTest.getParentNode());
        underTest.setParentNode(null);
        Assert.assertNull(underTest.getParentNode());
    }

    /**
	 * Dummy implementation for the abstract ASTNode class.
	 * 
	 */
    private class ASTNodeImpl extends ASTNode {

        public ASTNodeImpl() {
        }

        public ASTNodeImpl(ASTNode parentNode) {
            super(parentNode);
        }

        /**
		 * @see net.sf.sasl.aop.common.grammar.placeholder.syntax.ASTNode#isLeafNode()
		 */
        @Override
        public boolean isLeafNode() {
            return false;
        }

        /**
		 * @see net.sf.sasl.aop.common.grammar.placeholder.syntax.ASTNode#prettyPrint(int)
		 */
        @Override
        public String prettyPrint(int indent) throws IllegalArgumentException {
            return null;
        }
    }
}
