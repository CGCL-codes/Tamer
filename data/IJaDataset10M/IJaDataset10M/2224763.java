package com.siemens.ct.exi.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xml.sax.helpers.NamespaceSupport;
import com.siemens.ct.exi.EXIFactory;
import com.siemens.ct.exi.FidelityOptions;
import com.siemens.ct.exi.exceptions.EXIException;
import com.siemens.ct.exi.exceptions.ErrorHandler;
import com.siemens.ct.exi.grammar.ElementKey;
import com.siemens.ct.exi.grammar.Grammar;
import com.siemens.ct.exi.grammar.GrammarSchemaInformed;
import com.siemens.ct.exi.grammar.TypeGrammar;
import com.siemens.ct.exi.grammar.event.Attribute;
import com.siemens.ct.exi.grammar.event.AttributeGeneric;
import com.siemens.ct.exi.grammar.event.Characters;
import com.siemens.ct.exi.grammar.event.CharactersGeneric;
import com.siemens.ct.exi.grammar.event.EndDocument;
import com.siemens.ct.exi.grammar.event.EndElement;
import com.siemens.ct.exi.grammar.event.StartElement;
import com.siemens.ct.exi.grammar.event.StartElementGeneric;
import com.siemens.ct.exi.grammar.rule.Rule;
import com.siemens.ct.exi.grammar.rule.RuleStartTagSchemaLess;
import com.siemens.ct.exi.helpers.DefaultErrorHandler;
import com.siemens.ct.exi.util.ExpandedName;

/**
 * Shared functionality between EXI Encoder and Decoder.
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.3.20090331
 */
public abstract class AbstractEXICoder {

    protected final EndDocument eventED;

    protected final StartElement eventSE;

    protected final StartElementGeneric eventSEg;

    protected final EndElement eventEE;

    protected final Attribute eventAT;

    protected final AttributeGeneric eventATg;

    protected final Characters eventCH;

    protected final CharactersGeneric eventCHg;

    protected EXIFactory exiFactory;

    protected Grammar grammar;

    protected FidelityOptions fidelityOptions;

    protected ErrorHandler errorHandler;

    protected Map<String, Map<String, Rule>> runtimeDispatcher;

    protected List<String> scopeURI;

    protected List<String> scopeLocalName;

    protected List<String> scopeTypeURI;

    protected List<String> scopeTypeLocalName;

    protected NamespaceSupport namespaces;

    protected List<Rule> openRules;

    protected Rule currentRule;

    private ElementKey ruleKey;

    private ExpandedName ruleName;

    private ExpandedName ruleScope;

    private ExpandedName ruleScopeType;

    public AbstractEXICoder(EXIFactory exiFactory) {
        this.exiFactory = exiFactory;
        this.grammar = exiFactory.getGrammar();
        this.fidelityOptions = exiFactory.getFidelityOptions();
        eventED = new EndDocument();
        eventSE = new StartElement(null, null);
        eventSEg = new StartElementGeneric();
        eventEE = new EndElement();
        eventAT = new Attribute(null, null);
        eventATg = new AttributeGeneric();
        eventCH = new Characters(null, null);
        eventCHg = new CharactersGeneric();
        ruleKey = new ElementKey(null);
        ruleName = new ExpandedName(null, "");
        ruleScope = new ExpandedName(null, "");
        ruleScopeType = new ExpandedName(null, "");
        namespaces = new NamespaceSupport();
        this.errorHandler = new DefaultErrorHandler();
        initOnce();
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    protected void initOnce() {
        runtimeDispatcher = new HashMap<String, Map<String, Rule>>();
        openRules = new ArrayList<Rule>();
        scopeURI = new ArrayList<String>();
        scopeLocalName = new ArrayList<String>();
        scopeTypeURI = new ArrayList<String>();
        scopeTypeLocalName = new ArrayList<String>();
    }

    protected void initForEachRun() throws EXIException {
        runtimeDispatcher.clear();
        openRules.clear();
        currentRule = null;
        pushRule(null);
        scopeURI.clear();
        scopeLocalName.clear();
        pushScope(null, null);
        scopeTypeURI.clear();
        scopeTypeLocalName.clear();
        pushScopeType(null, null);
        namespaces.reset();
    }

    protected void pushScope(String uri, String localName) {
        scopeURI.add(uri);
        scopeLocalName.add(localName);
        namespaces.pushContext();
    }

    protected final void pushScopeType(String uri, String localName) {
        scopeTypeURI.add(uri);
        scopeTypeLocalName.add(localName);
    }

    protected void popScope() {
        scopeURI.remove(scopeURI.size() - 1);
        scopeLocalName.remove(scopeLocalName.size() - 1);
        namespaces.popContext();
    }

    public final String getScopeURI() {
        return scopeURI.get(scopeURI.size() - 1);
    }

    public final String getScopeLocalName() {
        return scopeLocalName.get(scopeLocalName.size() - 1);
    }

    public NamespaceSupport getNamespaces() {
        return this.namespaces;
    }

    protected final String getScopeTypeURI() {
        return scopeTypeURI.get(scopeTypeURI.size() - 1);
    }

    protected final String getScopeTypeLocalName() {
        return scopeTypeLocalName.get(scopeTypeLocalName.size() - 1);
    }

    protected final void replaceRuleAtTheTop(Rule top) {
        assert (!openRules.isEmpty());
        assert (top != null);
        if (top != currentRule) {
            openRules.set(openRules.size() - 1, currentRule = top);
        }
    }

    protected final void pushRule(Rule r) {
        openRules.add(currentRule = r);
    }

    protected final void popRule() {
        assert (!openRules.isEmpty());
        int size = openRules.size();
        openRules.remove(size - 1);
        currentRule = openRules.get(size - 2);
    }

    protected void pushRule(final String namespaceURI, final String localName) {
        Rule ruleToPush = null;
        if (grammar.isSchemaInformed()) {
            if ((ruleToPush = getSchemaRuleForElement(namespaceURI, localName)) == null) {
                TypeGrammar urType = ((GrammarSchemaInformed) grammar).getUrType();
                ruleToPush = urType.getType();
            }
        } else {
            ruleToPush = getRuntimeRuleForElement(namespaceURI, localName);
        }
        pushRule(ruleToPush);
    }

    private Rule getSchemaRuleForElement(final String namespaceURI, final String localName) {
        Rule ruleSchema = null;
        ruleName.setLocalName(localName);
        ruleName.setNamespaceURI(namespaceURI);
        ruleKey.setName(ruleName);
        ruleKey.setScope(null);
        ruleKey.setScopeType(null);
        ruleSchema = grammar.getRule(ruleKey);
        if (ruleSchema == null) {
            if (getScopeLocalName() != null) {
                ruleScope.setLocalName(getScopeLocalName());
                ruleScope.setNamespaceURI(getScopeURI());
                ruleKey.setScope(ruleScope);
            } else {
                ruleKey.setScope(null);
            }
            ruleSchema = grammar.getRule(ruleKey);
            if (ruleSchema == null && getScopeTypeLocalName() != null) {
                ruleKey.setScope(null);
                ruleScopeType.setLocalName(getScopeTypeLocalName());
                ruleScopeType.setNamespaceURI(getScopeTypeURI());
                ruleKey.setScopeType(ruleScopeType);
                ruleSchema = grammar.getRule(ruleKey);
            }
        }
        return ruleSchema;
    }

    private Rule getRuntimeRuleForElement(String namespaceURI, String localName) {
        Map<String, Rule> mapNS;
        Rule r;
        if ((mapNS = runtimeDispatcher.get(namespaceURI)) == null) {
            mapNS = new HashMap<String, Rule>();
            runtimeDispatcher.put(namespaceURI, mapNS);
            r = new RuleStartTagSchemaLess();
            mapNS.put(localName, r);
        } else {
            if ((r = mapNS.get(localName)) == null) {
                r = new RuleStartTagSchemaLess();
                mapNS.put(localName, r);
            }
        }
        return r;
    }
}
