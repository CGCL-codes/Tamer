package monkey.main;

import monkey.parser.Grammar;
import java.io.PrintStream;

public class ParserData {

    private String imports;

    private String declarations;

    private Grammar grammar;

    public ParserData(String imports, String declarations, Grammar grammar) {
        this.imports = imports;
        this.declarations = declarations;
        this.grammar = grammar.augmentate();
    }

    public String getImports() {
        return imports;
    }

    public String getDeclarations() {
        return declarations;
    }

    public Grammar getGrammar() {
        return grammar;
    }

    public void dump(PrintStream out) {
        out.println("imports: " + imports);
        out.println("declarations: " + declarations);
        grammar.dump(out);
    }
}
