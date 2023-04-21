package regr.new_grammar;

import java.io.IOException;
import java.util.Collections;
import org.junit.Test;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import ar.uba.dc.rfm.dynalloy.DynalloyController;
import ar.uba.dc.rfm.dynalloy.DynalloyOptions;
import ar.uba.dc.rfm.dynalloy.parser.AssertionNotFound;

public class New_GrammarTest {

    @Test
    public void grammar_ext_test() throws RecognitionException, TokenStreamException, IOException, AssertionNotFound {
        DynalloyController c = new DynalloyController();
        DynalloyOptions options = new DynalloyOptions("grammarExtAssert", 3, false, false, false);
        c.translate(Collections.<String>singletonList("test/regr/new_grammar/grammar_ext.dals"), Collections.<String>singletonList("test/regr/new_grammar/grammar_ext.als"), options);
    }

    @Test
    public void new_gramma_test() throws RecognitionException, TokenStreamException, IOException, AssertionNotFound {
        DynalloyController c = new DynalloyController();
        DynalloyOptions options = new DynalloyOptions("newGrammarAssert", 3, false, false, false);
        c.translate(Collections.<String>singletonList("test/regr/new_grammar/new_grammar.dals"), Collections.<String>singletonList("test/regr/new_grammar/new_grammar.als"), options);
    }

    @Test
    public void old_grammar_test() throws RecognitionException, TokenStreamException, IOException, AssertionNotFound {
        DynalloyController c = new DynalloyController();
        DynalloyOptions options = new DynalloyOptions(null, 3, false, false, false);
        c.translate(Collections.<String>singletonList("test/regr/new_grammar/old_grammar.dals"), Collections.<String>singletonList("test/regr/new_grammar/old_grammar.als"), options);
    }
}
