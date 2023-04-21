package org.xtext.example.parser.antlr;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.TokenSource;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.parser.ParseException;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import com.google.inject.Inject;
import org.xtext.example.services.SwrtjGrammarAccess;

public class SwrtjParser extends org.eclipse.xtext.parser.antlr.AbstractAntlrParser {

    @Inject
    private SwrtjGrammarAccess grammarAccess;

    @Override
    protected IParseResult parse(String ruleName, CharStream in) {
        TokenSource tokenSource = createLexer(in);
        XtextTokenStream tokenStream = createTokenStream(tokenSource);
        tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT");
        org.xtext.example.parser.antlr.internal.InternalSwrtjParser parser = createParser(tokenStream);
        parser.setTokenTypeMap(getTokenDefProvider().getTokenDefMap());
        parser.setSyntaxErrorProvider(getSyntaxErrorProvider());
        parser.setUnorderedGroupHelper(getUnorderedGroupHelper().get());
        try {
            if (ruleName != null) return parser.parse(ruleName);
            return parser.parse();
        } catch (Exception re) {
            throw new ParseException(re.getMessage(), re);
        }
    }

    protected org.xtext.example.parser.antlr.internal.InternalSwrtjParser createParser(XtextTokenStream stream) {
        return new org.xtext.example.parser.antlr.internal.InternalSwrtjParser(stream, getElementFactory(), getGrammarAccess());
    }

    @Override
    protected String getDefaultRuleName() {
        return "File";
    }

    public SwrtjGrammarAccess getGrammarAccess() {
        return this.grammarAccess;
    }

    public void setGrammarAccess(SwrtjGrammarAccess grammarAccess) {
        this.grammarAccess = grammarAccess;
    }
}
