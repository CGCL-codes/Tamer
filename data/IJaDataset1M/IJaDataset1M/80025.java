package test.net.sourceforge.pmd.jerry.ast.xpath;

import junit.framework.TestCase;
import net.sourceforge.pmd.jerry.ast.xpath.ASTIntersectExceptExpr;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * @author Romain PELISSE, belaran@gmail.com
 *
 */
public class ASTIntersectExceptExprTest extends TestCase {

    private static final int ID = 1;

    @Test
    public void testConstructors() {
        assertNotNull(new ASTIntersectExceptExpr(ID));
    }
}
