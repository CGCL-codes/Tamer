package sketch.specs;

import org.eclipse.jdt.core.dom.CompilationUnit;
import sketch.ast.ASTUtils;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SymbolicCommandTest extends TestCase {

    public static Test suite() {
        return new TestSuite(SymbolicCommandTest.class);
    }

    public void testSymbolicCommand() {
        String filePath = "./testfiles/sketch/specs/TreeMapSimple.java";
        CompilationUnit unit = ASTUtils.astRoot(filePath);
        SymbolicValueVisitor visitor = new SymbolicValueVisitor();
        unit.accept(visitor);
        assertTrue(visitor.getNumOfReplace() == 7);
        String[] methodSigs = visitor.getSymbolicMethodSignatures();
        assertEquals(7, methodSigs.length);
        System.out.println("command: " + SymbolicCommand.jpfSymbolicOptionsAsFlatString(methodSigs, "Mock_put"));
        String[] removeSigs = visitor.getSymbolicMethodSignaturesForMethod("testRemove");
        assertEquals(1, removeSigs.length);
        System.out.println("command: " + SymbolicCommand.jpfSymbolicOptionsAsFlatString(removeSigs, "Mock_remove"));
        String[] putSigs = visitor.getSymbolicMethodSignaturesForMethod("testPut");
        assertEquals(1, putSigs.length);
        System.out.println("command: " + SymbolicCommand.jpfSymbolicOptionsAsFlatString(putSigs, "Mock_puts"));
        String[] put2Sigs = visitor.getSymbolicMethodSignaturesForMethod("test2Puts");
        assertEquals(1, put2Sigs.length);
        System.out.println("command: " + SymbolicCommand.jpfSymbolicOptionsAsFlatString(put2Sigs, "Mock_puts2"));
        String[] putRemoveSigs = visitor.getSymbolicMethodSignaturesForMethod("testPutandRemove");
        assertEquals(2, putRemoveSigs.length);
        System.out.println("command: " + SymbolicCommand.jpfSymbolicOptionsAsFlatString(putRemoveSigs, "testPutandRemove"));
    }
}
