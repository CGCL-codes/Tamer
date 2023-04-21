package hu.cubussapiens.modembed.notation.highlevelprogram.parser.antlr;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.TokenSource;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.parser.ParseException;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import com.google.inject.Inject;
import hu.cubussapiens.modembed.notation.highlevelprogram.services.HighLevelProgramNotationGrammarAccess;

public class HighLevelProgramNotationParser extends org.eclipse.xtext.parser.antlr.AbstractAntlrParser {

    @Inject
    private HighLevelProgramNotationGrammarAccess grammarAccess;

    @Override
    protected IParseResult parse(String ruleName, CharStream in) {
        TokenSource tokenSource = createLexer(in);
        XtextTokenStream tokenStream = createTokenStream(tokenSource);
        tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT");
        hu.cubussapiens.modembed.notation.highlevelprogram.parser.antlr.internal.InternalHighLevelProgramNotationParser parser = createParser(tokenStream);
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

    protected hu.cubussapiens.modembed.notation.highlevelprogram.parser.antlr.internal.InternalHighLevelProgramNotationParser createParser(XtextTokenStream stream) {
        return new hu.cubussapiens.modembed.notation.highlevelprogram.parser.antlr.internal.InternalHighLevelProgramNotationParser(stream, getElementFactory(), getGrammarAccess());
    }

    @Override
    protected String getDefaultRuleName() {
        return "HighLevelProgramNotation";
    }

    public HighLevelProgramNotationGrammarAccess getGrammarAccess() {
        return this.grammarAccess;
    }

    public void setGrammarAccess(HighLevelProgramNotationGrammarAccess grammarAccess) {
        this.grammarAccess = grammarAccess;
    }
}
