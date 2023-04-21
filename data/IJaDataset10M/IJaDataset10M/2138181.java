package net.sf.betterj.metal;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import net.sf.betterj.metal.antlr.MetalLexer;
import net.sf.betterj.metal.antlr.MetalParser;

/**
 * @author Oleh Sklyarenko
 */
@Test(suiteName = "MetalSuite", testName = "GrammarTest")
public class MetalGrammarTest {

    @Test
    public void test1() throws IOException, RecognitionException {
        String inputString = "const volatile v:int=?";
        ANTLRInputStream is = new ANTLRInputStream(new ByteArrayInputStream(inputString.getBytes()));
        MetalLexer lexer = new MetalLexer(is);
        CommonTokenStream tStream = new CommonTokenStream(lexer);
        MetalParser parser = new MetalParser(tStream);
        MetalParser.variableDeclarator_return variableDeclarator_return = parser.variableDeclarator();
        CommonTree tree = (CommonTree) variableDeclarator_return.getTree();
        Assert.assertEquals(tree.getToken().getText(), "VARDECL");
        Assert.assertEquals(tree.getToken().getType(), MetalParser.VARDECL);
        CommonTree varIdTree = (CommonTree) tree.getChildren().get(0);
        Assert.assertEquals(varIdTree.getToken().getText(), "v");
        CommonTree modsTree = (CommonTree) tree.getChildren().get(1);
        Assert.assertEquals(modsTree.getToken().getText(), "VARMODS");
        Assert.assertEquals(modsTree.getToken().getType(), MetalParser.VARMODS);
        CommonTree mod1Tree = (CommonTree) modsTree.getChildren().get(0);
        Assert.assertEquals(mod1Tree.getToken().getText(), "const");
        Assert.assertEquals(mod1Tree.getToken().getType(), MetalParser.CONST_KEYWORD);
        CommonTree mod2Tree = (CommonTree) modsTree.getChildren().get(1);
        Assert.assertEquals(mod2Tree.getToken().getText(), "volatile");
        Assert.assertEquals(mod2Tree.getToken().getType(), MetalParser.VOLATILE_KEYWORD);
    }
}
