package net.sourceforge.transmogrify.symtab.test;

import net.sourceforge.transmogrify.symtab.*;
import net.sourceforge.transmogrify.symtab.parser.*;
import java.io.*;
import junit.extensions.*;
import junit.framework.*;
import net.sourceforge.transmogrify.test.TestStarter;

public class LabelDefTest extends TestCase {

    QueryEngine query;

    public LabelDefTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        try {
            FileParser fileParser = new FileParser();
            String[] args = { "test/LabelTest.java" };
            for (int i = 0; i < args.length; i++) {
                fileParser.doFile(new File(args[i]));
            }
            TableMaker maker = new TableMaker((SymTabAST) (fileParser.getTree()));
            SymbolTable table = maker.getTable();
            query = new QueryEngine(table);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void tearDown() {
    }

    public void testLabelDefinition() {
        IDefinition defOfReference = query.getDefinition("bar", new Occurrence(new File("test/LabelTest.java"), 9, 17));
        IDefinition defOfDefinition = query.getDefinition("bar", new Occurrence(new File("test/LabelTest.java"), 5, 5));
        assertNotNull(defOfDefinition);
        assertEquals(defOfDefinition, defOfReference);
    }

    public static void main(String[] args) {
        TestStarter.startRun(LabelDefTest.class);
    }
}
