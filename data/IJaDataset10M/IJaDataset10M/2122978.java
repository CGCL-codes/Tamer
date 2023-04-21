package org.xmi.mpath;

import java.io.IOException;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;
import org.junit.Test;
import org.xmi.antlr.ModelPathLexer;
import org.xmi.antlr.ModelPathParser;
import org.xmi.antlr.ModelPathParser.mPath_return;
import org.xmi.repository.RepositoryException;

public class MPathParserTest {

    static final TreeAdaptor adaptor = new CommonTreeAdaptor() {

        public Object create(Token payload) {
            return new CommonTree(payload);
        }
    };

    @Test
    public void testSimplePath() throws Exception {
        ModelPathLexer lexer = createLexer("/AA/BB/CC");
        ModelPathParser parser = createParser(lexer);
        parser.setTreeAdaptor(adaptor);
        mPath_return re = parser.mPath();
        if (!lexer.getExceptions().isEmpty()) {
            throw new RepositoryException("cannot parse the input:" + lexer.getExceptions());
        }
        CommonTree tree = (CommonTree) re.getTree();
        assert (tree.getChildCount() == 6) : "root childs:" + tree.getChildCount();
        assert (tree.getChild(0).getChildCount() == 1) : "first childs:" + tree.getChild(0).getChildCount();
        assert (tree.getChild(0).getText().equals("PATH_TOKEN")) : "first childs:" + tree.getChild(0).getText();
        assert (tree.getChild(0).getChild(0).getText().equals("/")) : "first childs:" + tree.getChild(0).getChild(0).getText();
        assert (tree.getChild(1).getText().equals("NAME_TOKEN")) : "first childs:" + tree.getChild(1).getText();
        assert (tree.getChild(1).getChild(0).getText().equals("AA")) : "first childs:" + tree.getChild(1).getChild(0).getText();
        assert (tree.getChild(2).getText().equals("PATH_TOKEN")) : "first childs:" + tree.getChild(2).getText();
        assert (tree.getChild(2).getChild(0).getText().equals("/")) : "first childs:" + tree.getChild(2).getChild(0).getText();
        assert (tree.getChild(3).getText().equals("NAME_TOKEN")) : "first childs:" + tree.getChild(3).getText();
        assert (tree.getChild(3).getChild(0).getText().equals("BB")) : "first childs:" + tree.getChild(3).getChild(0).getText();
        assert (tree.getChild(4).getText().equals("PATH_TOKEN")) : "first childs:" + tree.getChild(4).getText();
        assert (tree.getChild(4).getChild(0).getText().equals("/")) : "first childs:" + tree.getChild(4).getChild(0).getText();
        assert (tree.getChild(5).getText().equals("NAME_TOKEN")) : "first childs:" + tree.getChild(5).getText();
        assert (tree.getChild(5).getChild(0).getText().equals("CC")) : "first childs:" + tree.getChild(5).getChild(0).getText();
    }

    private ModelPathLexer createLexer(String path) throws IOException {
        CharStream stream = new ANTLRStringStream(path);
        ModelPathLexer lexer = new ModelPathLexer(stream);
        return lexer;
    }

    private ModelPathParser createParser(ModelPathLexer lexer) throws IOException {
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ModelPathParser parser = new ModelPathParser(tokens);
        return parser;
    }
}
