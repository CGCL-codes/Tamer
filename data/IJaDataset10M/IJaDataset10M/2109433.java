package net.sf.parser4j.generator.service;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import net.sf.parser4j.generator.entity.grammardefnode.GrammarDef;
import net.sf.parser4j.generator.entity.grammardefnode.RuleDef;
import net.sf.parser4j.generator.entity.grammardefnode.RulesDef;
import net.sf.parser4j.generator.entity.grammardefnode.RulesGroupListDef;
import net.sf.parser4j.generator.entity.grammardefnode.StatementDef;
import net.sf.parser4j.generator.service.grammardefnode.IGrammarDefVisitor;
import net.sf.parser4j.kernelgenerator.entity.grammarnode.IGrammarNode;
import net.sf.parser4j.kernelgenerator.service.GeneratorException;
import net.sf.parser4j.parser.entity.EnumSource;
import net.sf.parser4j.parser.entity.data.NonTerminalMap;
import net.sf.parser4j.parser.entity.data.Pair;
import net.sf.parser4j.parser.service.DebugUtil;
import org.apache.log4j.Logger;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class StatementDefToGrammarNodeAssocier implements IGrammarDefVisitor {

    private static final Logger LOGGER = Logger.getLogger(StatementDefToGrammarNodeAssocier.class);

    private static final String START_NON_TERMINAL = "Start";

    private static final String EMPTY_STRING = "";

    private static final StatementToGrammarNode STATEMENT_TO_GRAMMAR_NODE = StatementToGrammarNode.getInstance();

    private final transient NonTerminalMap nonTerminalMap;

    private transient Set<String> toNotExpandSet;

    private transient Map<String, Pair<StatementDef, IGrammarNode>> statementDefAnGrammarNodeMap;

    private transient Map<String, StatementDef> statementDefByNonTerminalNameMap;

    private transient String defaultEquMatchClassName;

    private final transient Set<String> notReferenced;

    private transient Map<String, Exception> nonTerminalDefinedNameSet;

    /**
	 * @return the statementDefAnGrammarNodeMap
	 */
    public Map<String, Pair<StatementDef, IGrammarNode>> getStatementDefAnGrammarNodeMap() {
        return statementDefAnGrammarNodeMap;
    }

    public StatementDefToGrammarNodeAssocier(final NonTerminalMap nonTerminalMap, final Set<String> notReferenced) {
        super();
        this.nonTerminalMap = nonTerminalMap;
        this.notReferenced = notReferenced;
    }

    @Override
    public void beginVisit(final GrammarDef def) {
        toNotExpandSet = new TreeSet<String>();
        statementDefByNonTerminalNameMap = def.getStatementDefByNonTerminalNameMap();
        statementDefAnGrammarNodeMap = new TreeMap<String, Pair<StatementDef, IGrammarNode>>();
        defaultEquMatchClassName = def.getDefaultEquMatchClassName();
        nonTerminalDefinedNameSet = new TreeMap<String, Exception>();
    }

    @Override
    public void endVisit(final GrammarDef def) {
        toNotExpandSet = null;
        statementDefByNonTerminalNameMap = null;
        nonTerminalDefinedNameSet = null;
    }

    @Override
    public void beginVisit(final RulesGroupListDef def) {
    }

    @Override
    public void endVisit(final RulesGroupListDef def) {
    }

    @Override
    public void beginVisit(final RulesDef def) {
    }

    @Override
    public void endVisit(final RulesDef def) {
    }

    @Override
    public void beginVisit(final RuleDef def) {
    }

    @Override
    public void endVisit(final RuleDef def) {
    }

    @Override
    public boolean beginVisit(final StatementDef statementDef) throws GrammarDefNodeVisitException {
        final Exception previous = nonTerminalDefinedNameSet.put(statementDef.getNonTerminalDefinedName(), new Exception("previous visit trace"));
        if (previous != null) {
            throw new GrammarDefNodeVisitException("duplicate statement " + statementDef, previous);
        }
        final boolean visit;
        final String nonTerminalDefinedName = statementDef.getNonTerminalDefinedName();
        if (notReferenced.contains(nonTerminalDefinedName)) {
            visit = false;
        } else {
            final String whiteSpaceNonTerminalName = statementDef.getWhiteSpaceNonTerminalName();
            if (whiteSpaceNonTerminalName == null) {
                throw new IllegalStateException("white space non terminal must be defined");
            }
            try {
                final boolean whiteSpaceSection = !EMPTY_STRING.equals(whiteSpaceNonTerminalName);
                if (toNotExpandSet.contains(nonTerminalDefinedName)) {
                    createGrammarNode(statementDef);
                    visit = true;
                } else if (statementDef.isNonTerminalDefType()) {
                    final String nonTerminalName = statementDef.getNonTerminalName();
                    final boolean nonTerminalAsToken = statementDef.isNonTerminalAsToken();
                    final StatementDef nonTerminalStatementDef = statementDefByNonTerminalNameMap.get(nonTerminalName);
                    if (nonTerminalStatementDef == null) {
                        throw new GrammarDefNodeVisitException("missing statementDef for non terminal " + nonTerminalDefinedName);
                    }
                    if (statementDef.isToken() || nonTerminalAsToken) {
                        statementDef.changeToConcat(nonTerminalStatementDef);
                        statementDef.setSubAsToken(nonTerminalAsToken);
                        if (statementDef.getMatchClassName() == null) {
                            statementDef.setMatchClassName(defaultEquMatchClassName);
                        }
                        createGrammarNode(statementDef);
                        visit = false;
                    } else if (statementDef.isStringToken()) {
                        statementDef.changeToConcat(nonTerminalStatementDef);
                        if (statementDef.getMatchClassName() != null) {
                            throw new IllegalStateException("non terminal \"" + nonTerminalName + "\" can not have match manager");
                        }
                        createGrammarNode(statementDef);
                        visit = false;
                    } else if (statementDef.getMatchClassName() != null || START_NON_TERMINAL.equals(nonTerminalDefinedName) || (nonTerminalStatementDef.isStringToken() && !statementDef.isStringTokenSon()) || (nonTerminalStatementDef.isToken()) && !statementDef.isTokenSon()) {
                        changeStatementDefToConcat(statementDef);
                        if (statementDef.getMatchClassName() == null) {
                            statementDef.setMatchClassName(defaultEquMatchClassName);
                        }
                        createGrammarNode(statementDef);
                        visit = true;
                    } else {
                        createGrammarNode(statementDef);
                        visit = true;
                    }
                } else if (whiteSpaceSection && !(statementDef.isTokenSon() || statementDef.isStringTokenSon()) && statementDef.isTerminal() && !statementDef.isEmptyDefType() && noFatherOrFatherIsNotATerminal(statementDef)) {
                    changeStatementDefToConcat(statementDef);
                    createGrammarNode(statementDef);
                    visit = true;
                } else if (whiteSpaceSection && START_NON_TERMINAL.equals(nonTerminalDefinedName) && statementDef.isAlternative()) {
                    changeStatementDefToConcat(statementDef);
                    if (statementDef.getMatchClassName() == null) {
                        statementDef.setMatchClassName(defaultEquMatchClassName);
                    }
                    createGrammarNode(statementDef);
                    visit = true;
                } else {
                    createGrammarNode(statementDef);
                    visit = true;
                }
            } catch (GeneratorException exception) {
                throw new GrammarDefNodeVisitException(exception);
            }
        }
        return visit;
    }

    private boolean noFatherOrFatherIsNotATerminal(final StatementDef statementDef) {
        final boolean noFatherOrFatherNotATerminal;
        final StatementDef fatherStatementDef = statementDef.getFatherStatementDef();
        if (fatherStatementDef == null) {
            noFatherOrFatherNotATerminal = true;
        } else {
            noFatherOrFatherNotATerminal = true ^ fatherStatementDef.isTerminal();
        }
        return noFatherOrFatherNotATerminal;
    }

    /**
	 * change statement definition to a concatenation of its copy. match class
	 * name is not copied, is null in copy
	 * 
	 * @param statementDef
	 */
    private void changeStatementDefToConcat(final StatementDef statementDef) {
        if (statementDef.isEmptyDefType()) {
            statementDef.changeToConcat();
        } else {
            final String nonTerminalDefinedName = statementDef.getNonTerminalDefinedName();
            final String subNonTerminalDefinedName = "_" + nonTerminalDefinedName;
            final StatementDef statementDefCopy = new StatementDef(statementDef, subNonTerminalDefinedName, EnumSource.WHITESPACE_INTERMEDIATE);
            statementDef.changeToConcat(statementDefCopy);
            toNotExpandSet.add(subNonTerminalDefinedName);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("statement def of " + statementDef.getNonTerminalDefinedName() + " changed to concat " + DebugUtil.getInstance().stackTrace());
        }
    }

    /**
	 * create grammar node for statement definition
	 * 
	 * @param statementDef
	 * @throws GeneratorException
	 * @throws GrammarDefNodeVisitException
	 */
    private void createGrammarNode(final StatementDef statementDef) throws GeneratorException, GrammarDefNodeVisitException {
        final IGrammarNode grammarNode = STATEMENT_TO_GRAMMAR_NODE.create(statementDef, nonTerminalMap);
        addToStatementDefAnGrammarNodeMap(statementDef, grammarNode);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("for statement def of " + statementDef.getNonTerminalDefinedName() + " " + statementDef.getStatementDefType() + " create grammar node " + grammarNode.getGrammarNodeType());
        }
    }

    /**
	 * set statement definition and the grammar node for the non terminal
	 * defined by statement definition, update
	 * {@link #statementDefAnGrammarNodeMap}.
	 * 
	 * @param statementDef
	 *            the statement definition for the the non terminal defined
	 * @param grammarNode
	 *            the grammar node for the the non terminal defined
	 * @throws GrammarDefNodeVisitException
	 */
    private void addToStatementDefAnGrammarNodeMap(final StatementDef statementDef, final IGrammarNode grammarNode) throws GrammarDefNodeVisitException {
        final Pair<StatementDef, IGrammarNode> value = new Pair<StatementDef, IGrammarNode>(statementDef, grammarNode);
        final String nonTerminalDefinedName = statementDef.getNonTerminalDefinedName();
        if (statementDefAnGrammarNodeMap.put(nonTerminalDefinedName, value) != null) {
            throw new GrammarDefNodeVisitException("duplicate definition of " + nonTerminalDefinedName);
        }
    }

    @Override
    public void endVisit(final StatementDef statementDef) {
    }
}
