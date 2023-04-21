package net.sourceforge.transmogrify.symtab.test;

import net.sourceforge.transmogrify.symtab.*;
import net.sourceforge.transmogrify.symtab.parser.*;
import java.io.*;
import java.util.Vector;
import junit.framework.*;
import net.sourceforge.transmogrify.test.TestStarter;

public class PackageTest extends TestCase {

    QueryEngine query;

    public PackageTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        try {
            FileParser fileParser = new FileParser();
            String[] args = { "test/Implementor.java", "test/Implementee.java" };
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

    public void testFoo() {
        Definition def = (Definition) query.getDefinition("Implementor", new Occurrence(new File("test/Implementor.java"), 3, 14));
        scopeDef(def);
        def = (Definition) query.getDefinition("Implementee", new Occurrence(new File("test/Implementor.java"), 3, 37));
        scopeDef(def);
        fail();
    }

    private void scopeDef(Definition def) {
        assertNotNull(def);
        while (def != null) {
            System.out.println("  Definition: " + def);
            def = def.getParentScope();
        }
    }

    public static void main(String[] args) {
        TestStarter.startRun(PackageTest.class);
    }
}
