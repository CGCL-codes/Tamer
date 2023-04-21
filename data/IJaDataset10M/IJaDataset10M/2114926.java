package astcentric.structure.basic;

import astcentric.structure.basic.id.ASTID;
import astcentric.structure.basic.id.InternalID;

public class DefaultASTFactoryTest extends ExtendedTestCase {

    private static final String DOMAIN = "astcentric.sf.net";

    private static final String CREATOR = "fjelmer";

    public void testPublicConstructor() {
        ASTFactory factory = new DefaultASTFactory(DOMAIN, CREATOR);
        String name = "ast 1";
        AST ast1 = factory.create(name);
        ASTInfo info = ast1.getInfo();
        ASTID id = info.getID();
        assertEquals(DOMAIN, id.getDomain());
        assertEquals(CREATOR, id.getCreator());
        assertTrue(id.getTimestamp() <= System.currentTimeMillis());
        assertEquals(name, info.getName());
        assertFalse(ast1.isSealed());
        Node root = ast1.createChildNode(null, 0);
        ast1.setNameOf(root, "root");
        InternalID internalID = root.getInternalID();
        assertEquals(CREATOR, internalID.getCreator());
        assertEquals(0, internalID.getNumber());
    }
}
