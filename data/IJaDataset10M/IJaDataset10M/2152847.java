package net.sf.sasl.language.placeholder.syntax.types;

import junit.framework.Assert;
import net.sf.sasl.language.placeholder.syntax.ASTNode;
import net.sf.sasl.language.placeholder.syntax.types.BooleanNode;
import org.junit.Test;

/**
 * Test cases for the
 * {@link net.sf.sasl.language.placeholder.syntax.types.BooleanNode
 * BooleanNode} class.
 * 
 * @author Philipp Förmer
 * 
 */
public class BooleanNodeTest {

    /**
	 * Test cases for the
	 * {@link net.sf.sasl.language.placeholder.syntax.types.BooleanNode#BooleanNode(String)
	 * BooleanNode(String)} constructor.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testValueConstructor() throws Exception {
        String[] inputValue = { "true", "false" };
        Boolean[] expectedValue = new Boolean[] { Boolean.TRUE, Boolean.FALSE };
        for (int i = 0; i < inputValue.length; i++) {
            BooleanNode booleanNode = new BooleanNode(inputValue[i]);
            Assert.assertEquals(expectedValue[i], booleanNode.getTypeValue());
            Assert.assertNull(booleanNode.getParentNode());
        }
    }

    /**
	 * Test if an illegal argument exception is thrown by
	 * {@link net.sf.sasl.language.placeholder.syntax.types.BooleanNode#BooleanNode(String)
	 * BooleanNode(String)} if the input value is invalid.
	 * 
	 * @throws Exception
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testValueConstructorThrowsExceptionOnWrongValue() throws Exception {
        new BooleanNode("bac");
    }

    /**
	 * Test cases for the
	 * {@link net.sf.sasl.language.placeholder.syntax.types.BooleanNode#BooleanNode(String, ASTNode)
	 * BooleanNode(String, ASTNode)} constructor.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testValueParentNodeConstructor() throws Exception {
        ASTNode parentNode = new BooleanNode("true");
        BooleanNode booleanNode = new BooleanNode("false", parentNode);
        Assert.assertEquals(parentNode, booleanNode.getParentNode());
    }
}
