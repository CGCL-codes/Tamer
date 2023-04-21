package org.bee.tl.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.runtime.tree.DOTTreeGenerator;
import org.antlr.stringtemplate.StringTemplate;
import org.bee.tl.core.*;

public class TestRun {

    /**
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        BufferedInputStream fis = new BufferedInputStream(TestRun.class.getResourceAsStream("/input.txt"));
        ANTLRInputStream input = new ANTLRInputStream(fis);
        BeeLexer lexer = new BeeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BeeParser parser = new BeeParser(tokens);
        BeeParser.prog_return r = parser.prog();
        CommonTree t = (CommonTree) r.getTree();
        if (args.length > 0 && args[0].equals("-dot")) {
            DOTTreeGenerator gen = new DOTTreeGenerator();
            StringTemplate st = gen.toDOT(t);
            System.out.println(st);
        } else {
            System.out.println(t.toStringTree());
        }
    }
}
