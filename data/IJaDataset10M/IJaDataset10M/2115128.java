package nl.utwente.ewi.tpl.grammar;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestTreeGrammar extends AbstractTestGrammar<TPLTreeLexer, TPLTreeParser> {

    @Parameters
    public static Collection data() {
        return Arrays.asList(new Object[][] { { "treeCase1" }, { "treeCase2" }, { "treeCase3" }, { "treeCase4" } });
    }

    private String name;

    public TestTreeGrammar(String name) {
        this.name = name;
    }

    protected TPLTreeLexer createLexer(CharStream input) {
        return new TPLTreeSwitchingLexer(input);
    }

    protected TPLTreeParser createParser(CommonTokenStream tokens) {
        return new TPLTreeParser(tokens);
    }

    protected Tree getTree(TPLTreeParser parser) throws RecognitionException {
        return (Tree) parser.definition().getTree();
    }

    @Test
    public void passCase() throws IOException, RecognitionException {
        runTest(name);
    }
}
