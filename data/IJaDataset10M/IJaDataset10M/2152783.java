package com.siemens.ct.exi.grammar;

import com.siemens.ct.exi.context.GrammarContext;
import com.siemens.ct.exi.grammar.rule.Rule;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.8
 */
public abstract class AbstractGrammar implements Grammar {

    private static final long serialVersionUID = 1328500655881102889L;

    protected Rule urTypeGrammar;

    protected Rule documentGrammar;

    protected Rule fragmentGrammar;

    private final GrammarContext grammarContext;

    private final boolean isSchemaInformed;

    public AbstractGrammar(boolean isSchemaInformed, GrammarContext grammarContext) {
        this.isSchemaInformed = isSchemaInformed;
        this.grammarContext = grammarContext;
    }

    public GrammarContext getGrammarContext() {
        return this.grammarContext;
    }

    public boolean isSchemaInformed() {
        return isSchemaInformed;
    }

    public Rule getUrTypeGrammar() {
        if (urTypeGrammar == null) {
            urTypeGrammar = XSDGrammarBuilder.getUrTypeRule();
        }
        return urTypeGrammar;
    }

    public Rule getDocumentGrammar() {
        return documentGrammar;
    }
}
