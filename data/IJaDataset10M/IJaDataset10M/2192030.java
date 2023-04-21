package sketch.issta11.specifystatements;

import org.eclipse.jdt.core.dom.CompilationUnit;
import sketch.ast.ASTUtils;
import sketch.specs.SketchTestProcessor;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ReplaceStatementTest extends TestCase {

    public static Test suite() {
        return new TestSuite(ReplaceStatementTest.class);
    }

    public void testReplace() {
        String filePath = "./tests/sketch/issta11/specifystatements/Z_ReplaceStatement_Example.java";
        SketchTestProcessor sketchProcessor = new SketchTestProcessor(filePath);
        CompilationUnit unit = sketchProcessor.getProcessedCompilationUnit();
        System.out.println(unit);
        assertEquals(12, ASTUtils.getAllMethods(unit).size());
    }
}
