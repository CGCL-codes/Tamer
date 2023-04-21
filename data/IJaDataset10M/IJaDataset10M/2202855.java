package net.sourceforge.djindent;

import net.sourceforge.djindent.parser.Parser;
import net.sourceforge.djindent.parser.JavaParser;
import net.sourceforge.djindent.parser.ParserPipe;

public class Djindent {

    public static void main(String args[]) {
        Djindent indenter = new Djindent();
        indenter.format("F:\\code\\java\\MyPrograms\\Hartford Life\\Dataframe\\Source\\hli\\prime\\bus\\affiliate\\AffiliateManager.java");
    }

    public Djindent() {
    }

    public void format(String a_fileNameString) {
        Parser javaParser = new JavaParser();
        ParserPipe parserOutput = javaParser.getParserPipe();
        javaParser.parse(new FileDataSource(a_fileNameString));
        while (parserOutput.symbolAvailable()) {
            System.out.print(parserOutput.getNextSymbol().getText());
        }
    }
}
