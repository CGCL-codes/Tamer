package fr.inria.uml4tst.papyrus.ocl4tst.ocl.resource.ocl.mopp;

public class OclAntlrScanner implements fr.inria.uml4tst.papyrus.ocl4tst.ocl.resource.ocl.IOclTextScanner {

    private org.antlr.runtime3_3_0.Lexer antlrLexer;

    public OclAntlrScanner(org.antlr.runtime3_3_0.Lexer antlrLexer) {
        this.antlrLexer = antlrLexer;
    }

    public fr.inria.uml4tst.papyrus.ocl4tst.ocl.resource.ocl.IOclTextToken getNextToken() {
        if (antlrLexer.getCharStream() == null) {
            return null;
        }
        final org.antlr.runtime3_3_0.Token current = antlrLexer.nextToken();
        if (current == null || current.getType() < 0) {
            return null;
        }
        fr.inria.uml4tst.papyrus.ocl4tst.ocl.resource.ocl.IOclTextToken result = new fr.inria.uml4tst.papyrus.ocl4tst.ocl.resource.ocl.mopp.OclTextToken(current);
        return result;
    }

    public void setText(String text) {
        antlrLexer.setCharStream(new org.antlr.runtime3_3_0.ANTLRStringStream(text));
    }
}
