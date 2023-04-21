package org.sablecc.sablecc.structure;

import org.sablecc.exception.*;
import org.sablecc.sablecc.lrautomaton.Alternative;
import org.sablecc.sablecc.syntax3.node.*;

public class ParserAlternative extends org.sablecc.sablecc.structure.Alternative {

    private final AParserAlternative parserAlternative;

    private final String publicName;

    private Alternative grammarAlternative;

    public ParserAlternative(AParserAlternative parserAlternative) {
        if (parserAlternative == null) {
            throw new InternalException("parserAlternative may not be null");
        }
        this.parserAlternative = parserAlternative;
        TAlternativeName alternativeName = parserAlternative.getAlternativeName();
        this.publicName = alternativeName == null ? "" : alternativeName.getText().substring(1, alternativeName.getText().length() - 2);
    }

    public String getPublicName() {
        return this.publicName;
    }

    public void setGrammarAlternative(Alternative grammarAlternative) {
        this.grammarAlternative = grammarAlternative;
    }

    public Alternative getGrammarAlternative() {
        return this.grammarAlternative;
    }
}
