package mini.java.semantics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import mini.java.lex.Tokenizer;
import mini.java.syntax.legacy.Node;
import mini.java.syntax.legacy.Parser;
import mini.java.syntax.legacy.Symbol;
import mini.java.syntax.legacy.Token;
import mini.java.syntaxtree.Program;

public class Test {

    public static void main(String[] args) throws Exception {
        Node<Token> root = null;
        Builder builder = Builder.newInstance();
        Program p = builder.buildProgram(root.getChildren().get(0));
        List<ErrorMsg> errors = new ArrayList<ErrorMsg>();
        SymbolTable tbl = new SymbolTable();
        SymbolTableVisitor visi = new SymbolTableVisitor(tbl, errors);
        p.accept(visi);
        tbl.dump();
        TypeCheckerVisitor tcv = new TypeCheckerVisitor(tbl, errors);
        p.accept(tcv);
        int errNum = errors.size();
        if (errNum > 0) {
            for (ErrorMsg em : errors) {
                System.out.println(em.getMsg() + "\n");
            }
            if (errNum > 1) {
                System.out.println(errNum + " errors");
            } else {
                System.out.println("1 error");
            }
        } else {
            System.out.println("Type Checking Passed!");
        }
    }

    public static Node<Symbol> getAST() {
        List<Token> stream = getTokens("MiniJavaPrograms/binarytree.java");
        Parser parser = Parser.getInstance();
        parser.initProductions("MiniJavaGrammarDefinition.spec");
        parser.buildAnalysisTable();
        return parser.buildAST(stream).getRootElement();
    }

    public static List<Token> getTokens(String fileName) {
        Tokenizer t = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            System.exit(-1);
        }
        String line = "";
        int count = 0;
        while (true) {
            try {
                line = br.readLine();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(-1);
            }
            if (line == null) {
                break;
            }
            if (count == 0) count++;
        }
        return null;
    }
}
