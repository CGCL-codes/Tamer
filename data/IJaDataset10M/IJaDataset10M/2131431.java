package org.emftext.language.models.resource.model.mopp;

public class ModelAntlrScanner implements org.emftext.language.models.resource.model.IModelTextScanner {

    private org.antlr.runtime.Lexer antlrLexer;

    public ModelAntlrScanner(org.antlr.runtime.Lexer antlrLexer) {
        this.antlrLexer = antlrLexer;
    }

    public org.emftext.language.models.resource.model.IModelTextToken getNextToken() {
        if (antlrLexer.getCharStream() == null) {
            return null;
        }
        final org.antlr.runtime.Token current = antlrLexer.nextToken();
        org.emftext.language.models.resource.model.IModelTextToken result = new org.emftext.language.models.resource.model.mopp.ModelTextToken(current);
        return result;
    }

    public void setText(java.lang.String text) {
        antlrLexer.setCharStream(new org.antlr.runtime.ANTLRStringStream(text));
    }
}
