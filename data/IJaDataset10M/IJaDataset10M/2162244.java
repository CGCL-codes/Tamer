package sketch.specs;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SketchedBlockExpansionTest extends TestCase {

    public static Test suite() {
        return new TestSuite(SketchedBlockExpansionTest.class);
    }

    public void testSketchedChooseOne() {
        System.out.println("Testing expanding sketched choosing one block?");
        String filePath = "./testfiles/sketch/specs/SampleCode.java";
        ;
        SketchedMethodsCreator creator = new SketchedMethodsCreator(filePath);
        List<SketchedMethodDeclaration> methods = creator.createSketchedMethodList();
        Map<Comment, SketchedBlock> sketchedBlocks = creator.getSketchedBlocks();
        System.out.println(sketchedBlocks.size());
        assertTrue(sketchedBlocks.size() == 10);
        Collection<SketchedBlock> blockList = sketchedBlocks.values();
        for (SketchedBlock block : blockList) {
            if (block instanceof SketchedChooseOneBlock) {
                System.out.println(" -------- expanding choose one block -------- ");
                List<Block> expanded = block.expandAll();
                for (Block b : expanded) {
                    System.out.println(b);
                    System.out.println("++++++");
                }
                System.out.println("\n");
            }
        }
        System.out.println("\n\n\n");
    }

    public void testSketchedChooseAll() {
        System.out.println("Testing expanding sketched choosing all block?");
        String filePath = "./testfiles/sketch/specs/SampleCode.java";
        ;
        SketchedMethodsCreator creator = new SketchedMethodsCreator(filePath);
        List<SketchedMethodDeclaration> methods = creator.createSketchedMethodList();
        Map<Comment, SketchedBlock> sketchedBlocks = creator.getSketchedBlocks();
        System.out.println(sketchedBlocks.size());
        assertTrue(sketchedBlocks.size() == 10);
        Collection<SketchedBlock> blockList = sketchedBlocks.values();
        for (SketchedBlock block : blockList) {
            if (block instanceof SketchedChooseAllBlock) {
                System.out.println(" -------- expanding choose all block -------- ");
                List<Block> expanded = block.expandAll();
                for (Block b : expanded) {
                    System.out.println(b);
                    System.out.println("++++++");
                }
                System.out.println("\n");
            }
        }
        System.out.println("\n\n\n");
    }

    public void testSketchedRepeat() {
        System.out.println("Testing expanding sketched choosing repeat block?");
        String filePath = "./testfiles/sketch/specs/SampleCode.java";
        ;
        SketchedMethodsCreator creator = new SketchedMethodsCreator(filePath);
        List<SketchedMethodDeclaration> methods = creator.createSketchedMethodList();
        Map<Comment, SketchedBlock> sketchedBlocks = creator.getSketchedBlocks();
        System.out.println(sketchedBlocks.size());
        assertTrue(sketchedBlocks.size() == 10);
        Collection<SketchedBlock> blockList = sketchedBlocks.values();
        for (SketchedBlock block : blockList) {
            if (block instanceof SketchedRepeatBlock) {
                List<Block> expanded = block.expandAll();
                int count = 0;
                System.out.println(" -------- expanding repeat block --------:  " + expanded.size());
                for (Block b : expanded) {
                    System.out.println((count++) + ", ....");
                    System.out.println(b);
                    System.out.println("++++++");
                }
                System.out.println("\n");
                break;
            }
        }
        System.out.println("\n\n\n");
    }

    public void testSketchedRepeat2() {
        System.out.println("Testing expanding nested repeat!");
        String filePath = "./testfiles/sketch/specs/NestedRepeat.java";
        SketchedMethodsCreator creator = new SketchedMethodsCreator(filePath);
        List<SketchedMethodDeclaration> methods = creator.createSketchedMethodList();
        Map<Comment, SketchedBlock> sketchedBlocks = creator.getSketchedBlocks();
        System.out.println(sketchedBlocks.size());
        assertTrue(sketchedBlocks.size() == 3);
        Collection<SketchedBlock> blockList = sketchedBlocks.values();
        for (SketchedBlock block : blockList) {
            List<Block> expanded = block.expandAll();
            int count = 0;
            System.out.println(" -------- expanding block --------:  type: " + block.getClass() + ", size:" + expanded.size());
            for (Block b : expanded) {
                System.out.println((count++) + ", ....");
                System.out.println(b);
                System.out.println("++++++");
            }
            System.out.println("\n");
        }
        System.out.println("\n\n\n");
    }
}
