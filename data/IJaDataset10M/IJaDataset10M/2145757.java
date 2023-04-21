package org.sablecc.sablecc.walker;

import org.sablecc.exception.*;
import org.sablecc.sablecc.lrautomaton.*;
import org.sablecc.sablecc.lrautomaton.Alternative;
import org.sablecc.sablecc.lrautomaton.Production;
import org.sablecc.sablecc.structure.*;
import org.sablecc.sablecc.syntax3.node.*;

public class ParserDeclarationCollector extends TreeWalker {

    private final GlobalIndex globalIndex;

    private Context currentContext;

    private Grammar currentGrammar;

    private Production currentProduction;

    private Alternative currentAlternative;

    private String currentElementName;

    private NormalParserProduction currentParserProduction;

    public ParserDeclarationCollector(GlobalIndex globalIndex) {
        if (globalIndex == null) {
            throw new InternalException("globalIndex may not be null");
        }
        this.globalIndex = globalIndex;
    }

    @Override
    public void caseAGrammar(AGrammar node) {
        visit(node.getParser());
    }

    @Override
    public void caseAParser(AParser node) {
        for (PParserContext parserContext : node.getParserContexts()) {
            visit(parserContext);
        }
        this.currentGrammar.stabilize();
        this.globalIndex.setGrammar(this.currentGrammar);
    }

    @Override
    public void caseAParserContext(AParserContext node) {
        this.currentContext = this.globalIndex.getContext(node);
        for (PParserProduction parserProduction : node.getParserProductions()) {
            visit(parserProduction);
        }
        this.currentContext = null;
    }

    @Override
    public void inANormalParserProduction(ANormalParserProduction node) {
        String name = node.getName().getText();
        if (this.currentGrammar == null) {
            this.currentGrammar = new Grammar(name);
        }
        this.currentParserProduction = (NormalParserProduction) this.globalIndex.getProduction(node);
        this.currentProduction = this.currentGrammar.getProduction(name);
        this.currentParserProduction.setGrammarProduction(this.currentProduction);
    }

    @Override
    public void outANormalParserProduction(ANormalParserProduction node) {
        this.currentProduction = null;
        this.currentParserProduction = null;
    }

    @Override
    public void inAParserAlternative(AParserAlternative node) {
        String name;
        if (node.getAlternativeName() == null) {
            name = "";
        } else {
            name = node.getAlternativeName().getText();
            name = name.substring(1, name.length() - 2);
        }
        this.currentAlternative = this.currentProduction.addAlternative(name);
        ParserAlternative alternative = this.currentParserProduction.getAlternative(node);
        alternative.setGrammarAlternative(this.currentAlternative);
    }

    @Override
    public void outAParserAlternative(AParserAlternative node) {
        this.currentAlternative = null;
    }

    @Override
    public void inAUnitElement(AUnitElement node) {
        if (node.getElementName() != null) {
            String name = node.getElementName().getText();
            this.currentElementName = name.substring(1, name.length() - 2);
        }
    }

    @Override
    public void outAUnitElement(AUnitElement node) {
        this.currentElementName = null;
    }

    @Override
    public void caseANameUnit(ANameUnit node) {
        String elementName = this.currentElementName;
        if (elementName == null) {
            elementName = node.getIdentifier().getText();
        }
        NameUnit nameUnit = this.globalIndex.getParserResolution(node.getIdentifier());
        if (nameUnit instanceof Expression) {
            MatchedToken matchedToken = this.currentContext.getMatchedTokenOrNull(node);
            if (matchedToken == null) {
                this.currentContext.addMatchedToken(node, false);
                matchedToken = this.currentContext.getMatchedToken(node);
            }
            this.currentAlternative.addTokenElement(elementName, this.currentGrammar.getToken(matchedToken.getName()));
        } else {
            this.currentAlternative.addProductionElement(elementName, this.currentGrammar.getProduction(node.getIdentifier().getText()));
        }
    }

    @Override
    public void caseAStringUnit(AStringUnit node) {
        MatchedToken matchedToken = this.currentContext.getMatchedTokenOrNull(node);
        if (matchedToken == null) {
            this.currentContext.addMatchedToken(node, false);
            matchedToken = this.currentContext.getMatchedToken(node);
        }
        String elementName = this.currentElementName;
        if (elementName == null) {
            elementName = "$" + ((AnonymousToken) matchedToken).getId();
        }
        this.currentAlternative.addTokenElement(elementName, this.currentGrammar.getToken(matchedToken.getName()));
    }

    @Override
    public void caseAEpsilonUnit(AEpsilonUnit node) {
        MatchedToken matchedToken = this.currentContext.getMatchedTokenOrNull(node);
        if (matchedToken == null) {
            this.currentContext.addMatchedToken(node, false);
            matchedToken = this.currentContext.getMatchedToken(node);
        }
        String elementName = this.currentElementName;
        if (elementName == null) {
            elementName = "$" + ((AnonymousToken) matchedToken).getId();
        }
        this.currentAlternative.addTokenElement(elementName, this.currentGrammar.getToken(matchedToken.getName()));
    }

    @Override
    public void caseAAnyUnit(AAnyUnit node) {
        MatchedToken matchedToken = this.currentContext.getMatchedTokenOrNull(node);
        if (matchedToken == null) {
            this.currentContext.addMatchedToken(node, false);
            matchedToken = this.currentContext.getMatchedToken(node);
        }
        String elementName = this.currentElementName;
        if (elementName == null) {
            elementName = "$" + ((AnonymousToken) matchedToken).getId();
        }
        this.currentAlternative.addTokenElement(elementName, this.currentGrammar.getToken(matchedToken.getName()));
    }

    @Override
    public void caseACharCharacter(ACharCharacter node) {
        MatchedToken matchedToken = this.currentContext.getMatchedTokenOrNull(node);
        if (matchedToken == null) {
            this.currentContext.addMatchedToken(node, false);
            matchedToken = this.currentContext.getMatchedToken(node);
        }
        String elementName = this.currentElementName;
        if (elementName == null) {
            elementName = "$" + ((AnonymousToken) matchedToken).getId();
        }
        this.currentAlternative.addTokenElement(elementName, this.currentGrammar.getToken(matchedToken.getName()));
    }

    @Override
    public void caseADecCharacter(ADecCharacter node) {
        MatchedToken matchedToken = this.currentContext.getMatchedTokenOrNull(node);
        if (matchedToken == null) {
            this.currentContext.addMatchedToken(node, false);
            matchedToken = this.currentContext.getMatchedToken(node);
        }
        String elementName = this.currentElementName;
        if (elementName == null) {
            elementName = "$" + ((AnonymousToken) matchedToken).getId();
        }
        this.currentAlternative.addTokenElement(elementName, this.currentGrammar.getToken(matchedToken.getName()));
    }

    @Override
    public void caseAHexCharacter(AHexCharacter node) {
        MatchedToken matchedToken = this.currentContext.getMatchedTokenOrNull(node);
        if (matchedToken == null) {
            this.currentContext.addMatchedToken(node, false);
            matchedToken = this.currentContext.getMatchedToken(node);
        }
        String elementName = this.currentElementName;
        if (elementName == null) {
            elementName = "$" + ((AnonymousToken) matchedToken).getId();
        }
        this.currentAlternative.addTokenElement(elementName, this.currentGrammar.getToken(matchedToken.getName()));
    }
}
