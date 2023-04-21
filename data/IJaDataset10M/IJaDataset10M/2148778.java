package net.sf.ubq.script.parser.antlr;

import org.antlr.runtime.ANTLRInputStream;
import org.eclipse.xtext.parser.antlr.ITokenDefProvider;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.parser.ParseException;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import com.google.inject.Inject;
import net.sf.ubq.script.services.UbqtGrammarAccess;

public class UbqtParser extends org.eclipse.xtext.parser.antlr.AbstractAntlrParser {

    @Inject
    protected ITokenDefProvider antlrTokenDefProvider;

    @Inject
    private UbqtGrammarAccess grammarAccess;

    @Override
    protected IParseResult parse(String ruleName, ANTLRInputStream in) {
        net.sf.ubq.script.parser.antlr.internal.InternalUbqtLexer lexer = new net.sf.ubq.script.parser.antlr.internal.InternalUbqtLexer(in);
        XtextTokenStream stream = new XtextTokenStream(lexer, antlrTokenDefProvider);
        stream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT");
        net.sf.ubq.script.parser.antlr.internal.InternalUbqtParser parser = new net.sf.ubq.script.parser.antlr.internal.InternalUbqtParser(stream, getElementFactory(), grammarAccess);
        parser.setTokenTypeMap(antlrTokenDefProvider.getTokenDefMap());
        try {
            if (ruleName != null) return parser.parse(ruleName);
            return parser.parse();
        } catch (Exception re) {
            throw new ParseException(re.getMessage(), re);
        }
    }

    @Override
    protected String getDefaultRuleName() {
        return "UbqSession";
    }

    public UbqtGrammarAccess getGrammarAccess() {
        return this.grammarAccess;
    }

    public void setGrammarAccess(UbqtGrammarAccess grammarAccess) {
        this.grammarAccess = grammarAccess;
    }
}
