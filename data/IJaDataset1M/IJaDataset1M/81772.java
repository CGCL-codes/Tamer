package sketch.specs;

import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import sketch.ast.ASTUtils;
import sketch.util.Globals;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SketchedTryCatchBlockTest extends TestCase {

    public static Test suite() {
        return new TestSuite(SketchedTryCatchBlockTest.class);
    }

    public void testCreateNewTryCatchBlock() {
        System.out.println("Testing testCreateNewTryCatchBlock!");
        String filePath = "./testfiles/sketch/specs/ParenRemove.java";
        ;
        CompilationUnit unit = ASTUtils.astRoot(filePath);
        TryStatement tryCatch = unit.getAST().newTryStatement();
        VariableDeclarationFragment vdf = unit.getAST().newVariableDeclarationFragment();
        vdf.setName(unit.getAST().newSimpleName("s"));
        VariableDeclarationStatement vds = unit.getAST().newVariableDeclarationStatement(vdf);
        Block b = unit.getAST().newBlock();
        b.statements().add(vds);
        tryCatch.setBody(b);
        CatchClause cc = unit.getAST().newCatchClause();
        SingleVariableDeclaration exceptDecl = unit.getAST().newSingleVariableDeclaration();
        exceptDecl.setName(unit.getAST().newSimpleName("e$$_wrapped"));
        exceptDecl.setType(unit.getAST().newSimpleType(unit.getAST().newSimpleName("Throwable")));
        cc.setException(exceptDecl);
        tryCatch.catchClauses().add(cc);
        System.out.println(tryCatch);
    }

    public void testWrappingTryCatchBlock() {
        System.out.println("Testing testWrappingTryCatchBlock!");
        String filePath = "./testfiles/sketch/specs/SampleCode.java";
        ;
        SketchedMethodsCreator creator = new SketchedMethodsCreator(filePath);
        List<SketchedMethodDeclaration> methods = creator.createSketchedMethodList();
        Map<Comment, SketchedBlock> sketchedBlocks = creator.getSketchedBlocks();
        System.out.println(sketchedBlocks.size());
        assertTrue(sketchedBlocks.size() == 10);
        for (SketchedBlock block : sketchedBlocks.values()) {
            SketchedTryCatchBlock tryCatchBlock = new SketchedTryCatchBlock(block.realBlock);
            List<Block> blocks = tryCatchBlock.replicate();
            assertEquals(1, blocks.size());
            System.out.println(blocks.get(0));
        }
    }

    public void testTryCatchWithOtherNotations() {
        System.out.println("Testing testTryCatchWithOtherNotations!");
        String filePath = "./testfiles/sketch/specs/TryCatch.java";
        ;
        SketchedMethodsCreator creator = new SketchedMethodsCreator(filePath);
        List<SketchedMethodDeclaration> methods = creator.createSketchedMethodList();
        Map<Comment, SketchedBlock> sketchedBlocks = creator.getSketchedBlocks();
        System.out.println(sketchedBlocks.size());
        assertTrue(sketchedBlocks.size() == 2);
        for (SketchedBlock block : sketchedBlocks.values()) {
            System.out.println(block);
            System.out.print("after expansion, we get: ");
            List<Block> blocks = block.replicate();
            System.out.println(blocks.size());
            for (Block b : blocks) {
                System.out.println(b);
            }
            System.out.println(Globals.lineSep);
            System.out.println(Globals.lineSep);
        }
    }

    public void testTryCatchExpandAll() {
        System.out.println("Testing testTryCatchExpandAll!");
        String filePath = "./testfiles/sketch/specs/TryCatch.java";
        ;
        SketchedMethodsCreator creator = new SketchedMethodsCreator(filePath);
        List<SketchedMethodDeclaration> methods = creator.createSketchedMethodList();
        assertEquals(1, methods.size());
        SketchedMethodDeclaration method = methods.get(0);
        System.out.println("see the results after expansion: ");
        List<MethodDeclaration> method_decls = method.getMethodsAfterExpansion();
        for (MethodDeclaration method_decl : method_decls) {
            System.out.println(method_decl.toString());
        }
    }
}
