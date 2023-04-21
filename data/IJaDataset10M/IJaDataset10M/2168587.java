package nl.utwente.ewi.tpl.context;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import nl.utwente.ewi.tpl.AbstractParsingTest;
import nl.utwente.ewi.tpl.ast.tree.Definition;
import nl.utwente.ewi.tpl.ast.tree.NodeDef;
import nl.utwente.ewi.tpl.ast.logic.NodeSpec;
import nl.utwente.ewi.tpl.ast.logic.Specification;
import nl.utwente.ewi.tpl.context.ContextualException;
import nl.utwente.ewi.tpl.JavaTarget;
import nl.utwente.ewi.tpl.Target;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestContextCheckerLogic extends AbstractParsingTest {

    @Parameters
    public static Collection data() {
        return Arrays.asList(new Object[][] { { "case1" }, { "case2" }, { "case3" }, { "case5" }, { "case6" }, { "case7" }, { "case8" }, { "case9" }, { "case10" }, { "case11" }, { "case12" }, { "case13" }, { "case14" }, { "case15" }, { "case16" } });
    }

    private String name;

    public TestContextCheckerLogic(String name) {
        this.name = name;
    }

    @Test
    public void correctCase() throws IOException, RecognitionException, ContextualException {
        Target target = new JavaTarget();
        Definition def = parseTree("context/" + name + "Logic.tree");
        Specification spec = parseLogic("context/" + name + "Correct.logic");
        driver.checkTree(target, def, PARSER_NAME);
        driver.checkLogic(target, def, Arrays.asList(spec), Arrays.asList(name + ".logic"));
    }

    @Test(expected = ContextualException.class)
    public void incorrectCase() throws IOException, RecognitionException, ContextualException {
        Target target = new JavaTarget();
        Definition def = parseTree("context/" + name + "Logic.tree");
        Specification spec = parseLogic("context/" + name + "Incorrect.logic");
        driver.checkTree(target, def, PARSER_NAME);
        driver.checkLogic(target, def, Arrays.asList(spec), Arrays.asList(name + ".logic"));
    }
}
