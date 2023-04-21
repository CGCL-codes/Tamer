package test.net.sourceforge.pmd.jerry.ast.xpath;

import junit.framework.TestCase;
import net.sourceforge.pmd.jerry.ast.xpath.ASTPredicateList;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * @author Romain PELISSE, belaran@gmail.com
 *
 */
public class ASTPredicateListTest extends TestCase {

    private static final int ID = 1;

    @Test
    public void testConstructors() {
        assertNotNull(new ASTPredicateList(ID));
    }
}
