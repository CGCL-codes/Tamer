package net.sourceforge.transmogrify.symtab.test;

import net.sourceforge.transmogrify.symtab.*;
import java.io.File;
import net.sourceforge.transmogrify.test.TestStarter;

public class ExtendsExternalClassTest extends DefinitionLookupTest {

    private File file;

    public ExtendsExternalClassTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        file = new File("test/symtab/ExtendsExternalClass.java");
        createQueryEngine(new File[] { file });
    }

    public void testReferenceInExtendsClause() throws Exception {
        IDefinition ref = getDefinition(file, "TestCase", 5, 43);
        assertNotNull("Reference not found.", ref);
        IDefinition def = new ExternalClass(junit.framework.TestCase.class);
        assertEquals("Reference does not point to correct definition.", def, ref);
    }

    public static void main(String[] args) {
        TestStarter.startRun(ExtendsExternalClassTest.class);
    }
}
