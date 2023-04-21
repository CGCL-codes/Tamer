package com.narirelays.ems.utils.Interpreters.Recursive;

import java.io.IOException;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

/**!
 * 测试
 * 对于函数调用，同一个解析器来进行计算
 * @author jinhao
 *
 */
public class TestB {

    public static void main(String[] argv) throws Exception {
        ANTLRFileStream input = new ANTLRFileStream("E:\\workspace\\java\\ems\\src\\com\\narirelays\\ems\\utils\\Interpreters\\Recursive\\__Test___input.txt", "UTF8");
        RecursiveBLexer lexer = new RecursiveBLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RecursiveBParser parser = new RecursiveBParser(tokens);
        CommonTree t = (CommonTree) parser.prog().getTree();
        System.out.println(t.toStringTree());
        CommonTreeNodeStream nodes = new CommonTreeNodeStream(t);
        RecursiveTreeParserB evaluator = new RecursiveTreeParserB(nodes);
        evaluator.prog();
    }
}
