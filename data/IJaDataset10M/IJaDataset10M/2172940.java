package ws.prova.reference;

import org.mandarax.kernel.*;
import org.mandarax.lib.Cut;
import org.mandarax.reference.AbstractResolutionInferenceEngine;
import org.mandarax.reference.Resolution;
import org.mandarax.reference.StrictSelectionPolicy;
import org.mandarax.reference.TmpClause;
import org.mandarax.util.MultipleClauseSetIterator;
import org.mandarax.util.TermIterator;
import ws.prova.calc.CalcClauseIterator;
import ws.prova.calc.CalcClauseSet;
import ws.prova.calc.CalcPredicate;
import ws.prova.kernel.PolyPredicate;
import ws.prova.kernel.ProvaResultSet;
import ws.prova.parser.ParsingException;
import ws.prova.reagent;
import ws.prova.sql.SQLExtendedClauseIterator;
import ws.prova.RList;
import ws.prova.HelperPredicates;
import ws.prova.RListUtils;
import ws.prova.reference.Cache;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;
import java.util.Iterator;
import rbsla.owl.OWL2PROVA;

/**
 * @author <A HREF="mailto:adrian.paschke@gmx.de">Adrian Pasche</A>
 * @author <A HREF="mailto:a.kozlenkov@city.ac.uk">Alex Kozlenkov</A>
 * @author <A HREF="mailto:jens.dietrich@unforgettable.com">Jens Dietrich</A>
 *         <p>Title: ContractLog </p>
 *         <p>Description: ContractLog inference engine</p>
 *         <p>Copyright: Copyright (c) 2003-2006</p> *         
 * @version 1.8.0 <15 April 2006>
 * 
 * The RBSLA inference engine implements highly scalable data sructures (DAG) with
 * trampoline style outer loops and extended key indexing (see ProvaKnowledgeBase)
 * for faster access.
 * 
 * The inference engine supports goal memoization and provides loop checking.
 * Usage: p(X) :- cache(q(X)). % memorize subgoal q(X)
 * 
 * The inference engine support Description Logic and Java type systems
 * 
 */
public final class ResolutionInferenceEngine6 extends AbstractResolutionInferenceEngine {

    private Map complexReplacements = new HashMap();

    reagent reagent;

    Cache cache;

    /**
	 * Constructor.
	 *
	 * @param reagent
	 */
    public ResolutionInferenceEngine6(reagent reagent) {
        super();
        Map complexReplacements = new HashMap();
        selectionPolicy = new StrictSelectionPolicy();
        unificationAlgorithm = new RBSLARobinsonsUnificationAlgorithm();
        MAXSTEPS = 0;
        this.reagent = reagent;
        cache = new Cache(reagent);
    }

    /**
	 * Get the feature descriptions.
	 *
	 * @return the feature descriptions
	 */
    public InferenceEngineFeatureDescriptions getFeatureDescriptions() {
        if (featureDescriptions == null) {
            featureDescriptions = new InferenceEngineFeatureDescriptions() {

                protected void initialize() {
                    super.initialize();
                    this.supported.add(InferenceEngineFeatureDescriptions.LOOP_CHECKS);
                    this.supported.add(InferenceEngineFeatureDescriptions.MULTIPLE_RESULTS);
                    this.supported.add(InferenceEngineFeatureDescriptions.NEGATION_AS_FAILURE);
                    this.supported.add(InferenceEngineFeatureDescriptions.CUT);
                }
            };
        }
        return featureDescriptions;
    }

    /**
	 * Try to prove a goal.
	 *
	 * @param goal                      - the goal that has to be proved
	 * @param kb                        - the knowledge base used
	 * @param counter                   - the proof step counter
	 * @param renameVariables           a variable renaming
	 * @param logEnabled                - indicates whether we must log this proof step
	 * @param results                   - the list collecting the results
	 * @param exceptionHandlingStrategy - an integer decoding the exception handling strategy when fetching and resolving
	 *                                  ClauseSetExceptions
	 * @param cardinalityConstraint     - the cardinality constraint specifying the number of results we are looking for
	 * @param exhandler
	 * @return a derivation node
	 * @throws InferenceException
	 */
    private ProvaDerivationNodeImpl proof(int depth, Clause goal, final KnowledgeBase kb, final ProvaDerivationStepCounter counter, final VariableRenaming renameVariables, final boolean logEnabled, final List results, final int exceptionHandlingStrategy, final int cardinalityConstraint, ExceptionHandler exhandler) throws InferenceException, ParsingException {
        ProvaDerivationNodeImpl node = new ProvaDerivationNodeImpl();
        node.setFailed(true);
        node.setId(counter.next());
        node.setCut(false);
        node.setQuery(goal);
        Stack tabledNodes = new Stack();
        tabledNodes.push(node);
        do {
            node = (ProvaDerivationNodeImpl) tabledNodes.pop();
            goal = node.getQuery();
            boolean logOn = (counter.count % 10 == 0) ? LOG_IE_STEP.isDebugEnabled() : logEnabled;
            if (logOn) {
                LOG_IE_STEP.debug("PROOF STEP " + String.valueOf(counter.count));
                LOG_IE_STEP.debug("Goal       : " + goal.toString());
            }
            if (goal == null) {
                node.setFailed(true);
                return node;
            }
            int cutLevel = checkCut(node, goal);
            if (cutLevel != 0 && cutLevel != node.getId()) {
                while (!tabledNodes.isEmpty() && ((ProvaDerivationNodeImpl) tabledNodes.peek()).getId() > cutLevel) {
                    tabledNodes.pop();
                }
                node.setCut(false);
                node.setQuery(goal);
                tabledNodes.push(node);
                continue;
            } else {
                node.setCut(false);
            }
            if (cardinalityConstraint != 0) {
                if (goal.isEmpty()) {
                    if (logOn) {
                        LOG_IE_STEP.debug("Derivation successful !");
                    }
                    node.setResultNode(true);
                    node.setFailed(false);
                    results.add(node);
                    if (cardinalityConstraint != ALL) {
                        node.setLastNode((results.size() >= cardinalityConstraint));
                    }
                    if (tabledNodes.empty()) return node; else {
                        node.getParent().setFailed(false);
                        continue;
                    }
                }
            }
            Fact goalFact = (Fact) goal.getNegativeLiterals().get(0);
            Predicate goalPredicate = goalFact.getPredicate();
            exhandler = new ExceptionHandler(exhandler);
            if (goalPredicate.equals(ws.prova.reagent.fail)) {
                node.setFailed(true);
                if (tabledNodes.empty()) return node; else continue;
            } else if (goalPredicate.equals(ws.prova.reagent.restore_exhandler)) {
                ConstantTerm t = (ConstantTerm) goalFact.getTerms()[0];
                exhandler = (ExceptionHandler) t.getObject();
                goal.getNegativeLiterals().remove(0);
                node.setQuery(goal);
                tabledNodes.push(node);
                continue;
            }
            boolean calcProcessed = false;
            boolean calcFailed = false;
            while (goalPredicate instanceof CalcPredicate) {
                String predname = goalPredicate.getName();
                if (predname.endsWith("fcalc")) {
                    try {
                        goal = CalcClauseSet.functionalGoalProcessing(exhandler, (Prerequisite) goalFact, goal, unificationAlgorithm);
                    } catch (Exception ex) {
                        node.setFailed(true);
                        if (tabledNodes.empty()) return node; else {
                            calcFailed = true;
                            break;
                        }
                    }
                    if (goal == null) {
                        node.setFailed(true);
                        if (tabledNodes.empty()) return node; else {
                            calcFailed = true;
                            break;
                        }
                    }
                    if (goal.isEmpty()) {
                        if (logOn) {
                            LOG_IE_STEP.debug("Derivation successful !");
                        }
                        results.add(node);
                        node.setResultNode(true);
                        node.setFailed(false);
                        if (cardinalityConstraint != ALL) {
                            node.setLastNode((results.size() >= cardinalityConstraint));
                        }
                        if (tabledNodes.empty()) return node; else {
                            node.getParent().setFailed(false);
                            continue;
                        }
                    }
                    calcProcessed = true;
                    goalFact = (Fact) goal.getNegativeLiterals().get(0);
                    if (goalFact.getPredicate().equals(ws.prova.reagent.fail)) {
                        node.setFailed(true);
                        if (tabledNodes.empty()) return node; else {
                            calcFailed = true;
                            break;
                        }
                    }
                    goalPredicate = goalFact.getPredicate();
                    continue;
                }
                if (predname.endsWith("calc")) {
                    boolean res = false;
                    final boolean bcalc = predname.endsWith("bcalc");
                    if (bcalc || predname.endsWith("scalc")) {
                        try {
                            res = bcalc ? CalcClauseSet.consumeBPred(exhandler, (Prerequisite) goalFact, goal) : CalcClauseSet.consumeGoalProcessing(exhandler, (Prerequisite) goalFact, goal, null);
                        } catch (ParsingException ex) {
                            throw ex;
                        } catch (Exception ex) {
                            node.setFailed(true);
                            if (tabledNodes.empty()) return node; else {
                                calcFailed = true;
                                break;
                            }
                        }
                        if (!res) {
                            node.setFailed(true);
                            if (tabledNodes.empty()) return node; else {
                                calcFailed = true;
                                break;
                            }
                        }
                        goal.getNegativeLiterals().remove(0);
                        if (goal.getNegativeLiterals().size() == 0) {
                            if (logOn) {
                                LOG_IE_STEP.debug("Derivation successful !");
                            }
                            results.add(node);
                            node.setResultNode(true);
                            node.setFailed(false);
                            if (cardinalityConstraint != ALL) {
                                node.setLastNode((results.size() >= cardinalityConstraint));
                            }
                            if (tabledNodes.empty()) return node; else {
                                node.getParent().setFailed(false);
                                calcFailed = true;
                                break;
                            }
                        }
                        calcProcessed = true;
                        goalFact = (Fact) goal.getNegativeLiterals().get(0);
                        if (goalFact.getPredicate().equals(ws.prova.reagent.fail)) {
                            node.setFailed(true);
                            if (tabledNodes.empty()) return node; else {
                                calcFailed = true;
                                break;
                            }
                        }
                        goalPredicate = goalFact.getPredicate();
                        continue;
                    }
                }
                break;
            }
            if (calcProcessed) {
                if (!calcFailed) {
                    node.setQuery(goal);
                    tabledNodes.push(node);
                    continue;
                }
                continue;
            } else {
                if (calcFailed) continue;
            }
            boolean followOn = false;
            boolean requireFact = false;
            boolean in_builtin_subsumed_predicate = false;
            String pred = "";
            if (goalPredicate instanceof PolyPredicate) {
                Term key = goalFact.getTerms()[0];
                Predicate poly = null;
                if (key.isVariable()) {
                    String src = (goalFact.getProperty("src") != null) ? goalFact.getProperty("src") : "";
                    if (src.equals("")) throw new InferenceException("A free variable as predicate name found"); else throw new InferenceException("A free variable as predicate name found in " + src);
                } else {
                    pred = (String) ((ConstantTerm) key).getObject();
                    if (pred.equals("fact")) {
                        requireFact = true;
                    } else if (pred.equals("subsumed")) {
                        in_builtin_subsumed_predicate = true;
                    }
                    if (requireFact || in_builtin_subsumed_predicate) {
                        ComplexTerm p1 = (ComplexTerm) ((ComplexTerm) ((ComplexTerm) ((ComplexTerm) goalFact.getTerms()[1])).getTerms()[0]).getTerms()[1];
                        String pred1 = (String) ((ConstantTerm) ((ComplexTerm) p1).getTerms()[1]).getObject();
                        poly = (Predicate) reagent.getPoly(pred1 + '+' + goalPredicate.getName());
                        if (poly == null) {
                            node.setFailed(true);
                            if (tabledNodes.empty()) return node; else continue;
                        }
                        goalFact.setPredicate(poly);
                        goalFact.setTerm(new ConstantTermImpl(pred1), 0);
                        goalFact.setTerm(p1, 1);
                        requireFact = true;
                    } else {
                        poly = (Predicate) reagent.getPoly(pred + '+' + goalPredicate.getName());
                        if (poly != null) {
                            Term[] terms = goalFact.getTerms();
                            goalFact.setPredicate(poly);
                            for (int i = 0; i < terms.length; i++) {
                                goalFact.setTerm(terms[i], i);
                            }
                        }
                    }
                    if (poly == null) {
                        node.setFailed(true);
                        if (tabledNodes.empty()) return node; else continue;
                    }
                }
            }
            Stack tabledSubGoals = new Stack();
            try {
                MultipleClauseSetIterator e = null;
                if (cache.getSize() == cache.NO_CACHE) {
                    if (goalPredicate.getName().equals("cache")) {
                        ComplexTerm subGoal = (ComplexTerm) ((ComplexTerm) ((ComplexTerm) goalFact.getTerms()[1]).getTerms()[0]).getTerms()[1];
                        Term[] tmpTerm2 = { subGoal.getTerms()[1], subGoal };
                        PrerequisiteImpl tmpPre = new PrerequisiteImpl(HelperPredicates.derive(), tmpTerm2, false);
                        goal.getNegativeLiterals().set(0, tmpPre);
                        goalFact = tmpPre;
                        goalPredicate = goalFact.getPredicate();
                        node.setQuery(goal);
                        tabledNodes.push(node);
                        continue;
                    }
                } else if (goalPredicate.getName().equals("cache") || goalPredicate.getName().equals("defeasible")) {
                    ComplexTerm subGoal;
                    if (goalPredicate.getName().equals("cache")) subGoal = (ComplexTerm) ((ComplexTerm) ((ComplexTerm) goalFact.getTerms()[1]).getTerms()[0]).getTerms()[1]; else subGoal = (ComplexTerm) goalFact.getTerms()[1];
                    String call = ((ConstantTerm) goalFact.getTerms()[0]).getObject().toString();
                    if (call.startsWith("cache_update")) {
                        String key = call.substring(call.indexOf(":") + 1);
                        cache.update(key, subGoal);
                        goal.getNegativeLiterals().remove(0);
                        node.setQuery(goal);
                        tabledNodes.push(node);
                        continue;
                    }
                    List resultGoals = cache.get(subGoal);
                    if (resultGoals == null) {
                        String key = cache.getExtendedKey(subGoal);
                        Prerequisite tmpPre = new PrerequisiteImpl(goalPredicate, goalFact.getTerms(), false);
                        tmpPre.setTerm(new ConstantTermImpl("cache_update:" + key, java.lang.String.class), 0);
                        goal.getNegativeLiterals().set(0, tmpPre);
                        Term[] tmpTerm2 = { subGoal.getTerms()[1], subGoal };
                        tmpPre = new PrerequisiteImpl(HelperPredicates.derive(), tmpTerm2, false);
                        goal.getNegativeLiterals().add(0, tmpPre);
                        goalFact = tmpPre;
                        goalPredicate = goalFact.getPredicate();
                        node.setQuery(goal);
                        tabledNodes.push(node);
                        continue;
                    } else {
                        if (resultGoals.isEmpty()) {
                            node.setFailed(true);
                            if (tabledNodes.empty()) return node; else continue;
                        } else {
                            e = new MultipleClauseSetIterator(resultGoals);
                            Class[] struct = { Object.class, RList.class };
                            String pName = subGoal.getTerms()[1].toString();
                            pName = pName.substring(1, pName.length() - 1);
                            SimplePredicate p = new SimplePredicate(pName, struct);
                            Term[] tmpTerm = { subGoal.getTerms()[1], subGoal };
                            FactImpl tmpPre = new FactImpl(p, tmpTerm);
                            goal.getNegativeLiterals().set(0, tmpPre);
                            goalFact = tmpPre;
                            goalPredicate = tmpPre.getPredicate();
                            System.out.println("Use cache for " + goalFact);
                        }
                    }
                }
                if (e == null) e = (MultipleClauseSetIterator) kb.clauses(goal, exhandler);
                int maxcount = 0;
                boolean isSQL = goalPredicate.getName().equals("sql");
                boolean isCalc = false;
                if (goalPredicate instanceof ws.prova.calc.CalcPredicate) isCalc = true; else if (goalPredicate instanceof SimplePredicate) maxcount = e.collection().size();
                int count = 0;
                boolean useClause = true;
                while (e.hasMoreClauses()) {
                    count++;
                    if (isCalc) {
                        CalcClauseIterator calcit = (CalcClauseIterator) e.getNextClauseIterator();
                        maxcount = calcit.size();
                        if (calcit.isSimpleSubstitutions()) {
                            List subs = calcit.subs();
                            Clause nextGoal = getNextGoal(goal, subs);
                            tabledSubGoals.push(nextGoal);
                            if (maxcount == count) break;
                            continue;
                        }
                    }
                    if (isSQL) {
                        Clause nextGoal = getNextGoal(goal, (((SQLExtendedClauseIterator) ((MultipleClauseSetIterator) e).getNextClauseIterator()).subs));
                        tabledSubGoals.push(nextGoal);
                        continue;
                    }
                    Clause c = null;
                    useClause = true;
                    Resolution r = null;
                    Clause workCopy = null;
                    boolean isRule = true;
                    boolean isDiff = true;
                    try {
                        c = e.nextClause();
                        isRule = c.getNegativeLiterals().size() > 0;
                        if (requireFact && isRule) {
                            continue;
                        }
                        if (!isRule) {
                            try {
                                FactImpl f = (FactImpl) c.getPositiveLiterals().get(0);
                                if (!(f.getPredicate() instanceof CalcPredicate)) {
                                    List clauses = (List) e.collection();
                                    boolean updated = false;
                                    for (TermIterator it = f.terms(); it.hasMoreTerms(); ) {
                                        Term t = it.nextTerm();
                                        if (t.isCompound()) {
                                            for (Iterator it2 = ((ComplexTermImpl) t).allSubterms(); it2.hasNext(); ) {
                                                Term subT = (Term) it2.next();
                                                if (subT.isVariable()) {
                                                    String type = OWL2PROVA.getDLType(subT);
                                                    if (type != null && !type.equals("rdfs_Resource")) {
                                                        for (Iterator it3 = ((RBSLARobinsonsUnificationAlgorithm) unificationAlgorithm).getTypeURIs().iterator(); it3.hasNext(); ) {
                                                            String uri = (String) it3.next();
                                                            String reasoner = ((RBSLARobinsonsUnificationAlgorithm) unificationAlgorithm).getReasoner();
                                                            List result = OWL2PROVA.rdf(uri, reasoner, subT, "rdf_type", type);
                                                            for (ListIterator lit = result.listIterator(); lit.hasNext(); ) {
                                                                ArrayList triple = (ArrayList) lit.next();
                                                                StringBuffer tmpString = new StringBuffer();
                                                                tmpString.append(triple.get(2));
                                                                tmpString.append(":");
                                                                tmpString.append(triple.get(0));
                                                                Replacement rep = new Replacement(subT, new ConstantTermImpl(tmpString.toString()));
                                                                FactImpl tmpFact = (FactImpl) f.apply(rep);
                                                                clauses.add(count, tmpFact);
                                                                updated = true;
                                                            }
                                                        }
                                                        if (!updated) useClause = false;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (updated) {
                                        e = new org.mandarax.util.MultipleClauseSetIterator(clauses.subList(count, clauses.size()));
                                        maxcount = e.collection().size();
                                        count = 0;
                                        continue;
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        if (isDiff(c, goal) && !c.isBound()) workCopy = renameVariables.cloneAndRenameVarsInHead(c); else {
                            workCopy = c;
                            isDiff = false;
                        }
                    } catch (Exception x) {
                        String msg = "Exception fetching clause from iterator for " + goal;
                        LOG_IE_STEP.error(msg, x);
                        if (exceptionHandlingStrategy == BUBBLE_EXCEPTIONS) {
                            throw new InferenceException(msg);
                        }
                        useClause = false;
                    }
                    if (useClause) {
                        synchronized (reagent) {
                            r = Resolution.resolve(workCopy, goal, unificationAlgorithm, selectionPolicy, isRule);
                        }
                        if (r != null) {
                            if (isRule && isDiff) workCopy = renameVariables.cloneAndRenameVarsInBody((TmpClause) workCopy);
                            if (in_builtin_subsumed_predicate) {
                                if (r.unification.replacements.size() > 0) {
                                    continue;
                                }
                                goal.getNegativeLiterals().remove(0);
                                node.setQuery(goal);
                                tabledSubGoals.push(goal);
                                break;
                            }
                            int cutFound = -1;
                            if (isRule) {
                                cutFound = recordCut(node.getId(), workCopy);
                            }
                            boolean onException = c.getProperty("on_exception") != null;
                            if (onException) {
                                goal.getNegativeLiterals().add(1, ws.prova.util.DefaultLogicFactorySupport.prereq(ws.prova.reagent.restore_exhandler, new Object[] { exhandler }));
                                node.setQuery(goal);
                            }
                            Clause nextGoal = goal;
                            if (maxcount != count) nextGoal = getNextGoal(workCopy, goal, r.unification); else {
                                nextGoal.getNegativeLiterals().remove(0);
                                complexReplacements.clear();
                                if (r.unification.replacements.size() > 0) {
                                    for (ListIterator it = nextGoal.getNegativeLiterals().listIterator(); it.hasNext(); ) {
                                        Prerequisite f = (Prerequisite) it.next();
                                        f = (PrerequisiteImpl) f.applyToFactMinimally(r.unification.replacements, complexReplacements);
                                        it.set(f);
                                    }
                                }
                                if (r.unification.replacementsLocal.size() > 0) {
                                    for (ListIterator it = workCopy.getNegativeLiterals().listIterator(); it.hasNext(); ) {
                                        Prerequisite f = (Prerequisite) it.next();
                                        if (f.getPredicate().equals(ws.prova.reagent.fail)) break;
                                        f = (PrerequisiteImpl) f.applyToFactMinimally(r.unification.replacementsLocal, complexReplacements);
                                        it.set(f);
                                    }
                                }
                                nextGoal.getNegativeLiterals().addAll(0, workCopy.getNegativeLiterals());
                            }
                            if (nextGoal.isEmpty()) break;
                            if (((Clause) nextGoal.getNegativeLiterals().get(0)).getKey() instanceof Cut) {
                                followOn = true;
                                goal = nextGoal;
                                if (isRule) goal.getNegativeLiterals().remove(0);
                                tabledSubGoals.push(goal);
                                break;
                            }
                            tabledSubGoals.push(nextGoal);
                            if (maxcount == count) break;
                        }
                    } else {
                        if (logOn) {
                            LOG_IE_STEP.debug("Cannot apply the following rule:");
                            LOG_IE_STEP.debug("Clause     : " + c.toString());
                            LOG_IE_STEP.debug("Clause (rn): " + workCopy.toString());
                        }
                    }
                }
                e.close();
                ((RBSLARobinsonsUnificationAlgorithm) unificationAlgorithm).renewSubstitutions();
            } catch (ParsingException x) {
                throw x;
            } catch (ClauseSetException x) {
                String msg = "Exception getting clause iterator for " + goal;
                LOG_IE_STEP.error(msg, x);
                if (exceptionHandlingStrategy == BUBBLE_EXCEPTIONS) {
                    throw new InferenceException(msg);
                }
            }
            if (!tabledSubGoals.empty()) counter.next();
            while (!tabledSubGoals.empty()) {
                Clause nextGoal = (Clause) tabledSubGoals.pop();
                ProvaDerivationNodeImpl nextNode = new ProvaDerivationNodeImpl();
                nextNode.setFailed(true);
                nextNode.setId(node.getId() + 1);
                nextNode.setCut(false);
                nextNode.setQuery(nextGoal);
                node.setCurrentGoal(null);
                node.setQuery(null);
                nextNode.setParent(node);
                tabledNodes.push(nextNode);
            }
        } while (!tabledNodes.empty());
        return node;
    }

    /**
     * Answer a query, retrieve (multiple different) result.
     * The cardinality contraints describe how many results should be computed. It is either
     * <ol>
     * <li> <code>ONE</code> - indicating that only one answer is expected
     * <li> <code>ALL</code> - indicating that all answers should be computed
     * <li> <code>an integer value greater than 0 indicating that this number of results expected
     * </ol>
     *
     * @param query                   the query clause
     * @param kb                      the knowledge base used to answer the query
     * @param aCardinalityConstraint  the number of results expected
     * @param exceptionHandlingPolicy one of the constants definied in this class (BUBBLE_EXCEPTIONS,TRY_NEXT)
     * @return the result set of the query
     * @throws InferenceException
     * @see #ONE
     * @see #ALL
     */
    public ResultSet query(final Query query, final KnowledgeBase kb, final int aCardinalityConstraint, final int exceptionHandlingPolicy) throws InferenceException {
        List cacheSize = kb.getClauseSets(Cache.cacheSize());
        List cacheTimeout = kb.getClauseSets(Cache.cacheTimeout());
        if (!cacheSize.isEmpty() && cacheSize.get(0) instanceof Fact) cache.setSize(((Integer) ((ConstantTermImpl) ((ComplexTerm) ((ComplexTerm) ((FactImpl) cacheSize.get(0)).getTerms()[1]).getTerms()[0]).getTerms()[1]).getObject()).intValue());
        if (!cacheTimeout.isEmpty() && cacheTimeout.get(0) instanceof Fact) cache.setTimeout(((Integer) ((ConstantTermImpl) ((ComplexTerm) ((ComplexTerm) ((FactImpl) cacheTimeout.get(0)).getTerms()[1]).getTerms()[0]).getTerms()[1]).getObject()).intValue());
        List reasoner = kb.getClauseSets(this.externalReasoner());
        List typeSystemsURIs = kb.getClauseSets(this.externalTypeSystem());
        if (!reasoner.isEmpty() && reasoner.get(0) instanceof Fact) ((RBSLARobinsonsUnificationAlgorithm) unificationAlgorithm).setReasoner((((String) ((ConstantTermImpl) ((ComplexTerm) ((ComplexTerm) ((FactImpl) reasoner.get(0)).getTerms()[1]).getTerms()[0]).getTerms()[1]).getObject())));
        if (!typeSystemsURIs.isEmpty() && typeSystemsURIs.get(0) instanceof Fact) {
            for (ListIterator it = typeSystemsURIs.listIterator(); it.hasNext(); ) {
                String uri = (String) ((ConstantTermImpl) ((ComplexTerm) ((ComplexTerm) ((FactImpl) it.next()).getTerms()[1]).getTerms()[0]).getTerms()[1]).getObject();
                if (!((RBSLARobinsonsUnificationAlgorithm) unificationAlgorithm).getTypeURIs().contains(uri)) ((RBSLARobinsonsUnificationAlgorithm) unificationAlgorithm).addTypeURI(uri);
            }
        }
        ProvaResultSet rs = new ProvaResultSetImpl2(query);
        reagent.addResultSet(query.getName(), rs);
        Clause firstGoal = getGoal(query);
        VariableRenaming renameVariables = new VariableRenaming();
        List resultNodes = new ArrayList();
        try {
            ProvaDerivationNodeImpl linResult = proof(0, firstGoal, kb, new ProvaDerivationStepCounter(), renameVariables, LOG_IE_STEP.isDebugEnabled(), resultNodes, exceptionHandlingPolicy, aCardinalityConstraint, new ExceptionHandler(reagent, null));
        } catch (ParsingException ex) {
            rs.setException(ex);
        }
        if (cache.getTimeout() == cache.LOCAL) cache.clear();
        if (rs.numberOfResults() == 0) reagent.removeResultSet(query.getName());
        return rs;
    }

    /**
     * Build the next goal, i.e., remove the unified terms, join the remaining clauses,
     * and REALLY apply the unifications.
     *
     * @param c1 the first unified clause
     * @param c2 the second unified clause
     * @param u  a resolution
     * @return the next goal
     */
    protected Clause getNextGoal(final Clause c1, final Clause c2, final Unification u) {
        TmpClause nextClause = new TmpClause();
        nextClause.positiveLiterals = new ArrayList();
        nextClause.negativeLiterals = merge3(c1.getNegativeLiterals(), c2.getNegativeLiterals(), u);
        return nextClause;
    }

    protected Clause getNextGoal(final Clause c2, final List subs) {
        if (subs == null) return c2;
        TmpClause nextClause = new TmpClause();
        nextClause.positiveLiterals = new ArrayList();
        nextClause.negativeLiterals = merge3(c2.getNegativeLiterals(), subs);
        return nextClause;
    }

    /**
     * Merge the remaining rule and goal facts and apply the local and global substitutions as appropriate
     *
     * @param v1 the list with the remaining rule clauses
     * @param v2 the list with the remaining goal clauses
     * @param u  Unification with local and global substitutions to be performed
     * @return a new list
     */
    private List merge3(final List v1, final List v2, final Unification u) {
        complexReplacements.clear();
        ArrayList merged = new ArrayList(v2);
        merged.ensureCapacity(v1.size() + v2.size());
        merged.remove(0);
        merged.addAll(0, v1);
        PrerequisiteImpl f;
        if (u.replacementsLocal.size() > 0) {
            int i = 0;
            for (ListIterator it = merged.listIterator(); it.hasNext(); ) {
                f = (PrerequisiteImpl) it.next();
                if (f.getPredicate().equals(ws.prova.reagent.fail)) {
                    return merged;
                }
                f = (PrerequisiteImpl) f.applyToFactMinimally(u.replacementsLocal, complexReplacements);
                it.set(f);
                i++;
                if (i == v1.size()) break;
            }
        }
        if (u.replacements.size() > 0) {
            for (ListIterator it = merged.listIterator(v1.size()); it.hasNext(); ) {
                f = (PrerequisiteImpl) it.next();
                f = (PrerequisiteImpl) f.applyToFactMinimally(u.replacements, complexReplacements);
                it.set(f);
            }
        }
        return merged;
    }

    private List merge3(List v2, List subs) {
        complexReplacements.clear();
        ArrayList merged = new ArrayList(v2);
        merged.remove(0);
        PrerequisiteImpl f;
        if (subs.size() > 0) {
            for (ListIterator it = merged.listIterator(); it.hasNext(); ) {
                f = (PrerequisiteImpl) it.next();
                f = (PrerequisiteImpl) f.applyToFactMinimally(subs, complexReplacements);
                it.set(f);
            }
        }
        return merged;
    }

    /**
     * Check whether a goal has a cut.
     *
     * @param node
     * @param goal
     * @return 0, if no cut found; otherwise, the node-id of the CUT
     */
    private static int checkCut(final ProvaDerivationNodeImpl node, final Clause goal) {
        if (goal instanceof TmpClause) {
            ListIterator it = goal.getNegativeLiterals().listIterator();
            while (it.hasNext()) {
                Object clause = it.next();
                if (((Clause) clause).getKey() instanceof Cut) {
                    Integer extra = (Integer) ((Prerequisite) clause).getExtra();
                    it.remove();
                    node.setCutPredicate(extra.intValue());
                    node.setCut(true);
                } else break;
            }
            if (node.isCut()) {
                node.setCurrentGoal(goal);
                return node.getCutPredicate();
            }
        }
        return 0;
    }

    /**
     * Check whether a goal generates a cut and record the node id
     * in the prerequisite corresponding to this cut.
     *
     * @param id       the id of the current node
     * @param workCopy the clause about to be resolved that is checked here
     *                 for the presence of cuts.
     * @return position of the first found CUT (-1, if not found)
     */
    private static int recordCut(final int id, final Clause workCopy) {
        int cut_found = -1;
        List negLiterals = workCopy.getNegativeLiterals();
        int i = 0;
        for (ListIterator li = negLiterals.listIterator(); li.hasNext(); i++) {
            Fact clause = (Fact) li.next();
            Object pred = clause.getKey();
            if (pred instanceof Cut) {
                ((Prerequisite) clause).setExtra(new Integer(id));
                if (cut_found == -1) cut_found = i;
            }
        }
        return cut_found;
    }

    private boolean isDiff(Clause c1, Clause c2) {
        if (c1.getPositiveLiterals().get(0).equals(c2.getNegativeLiterals().get(0))) return false;
        return true;
    }

    public static FactImpl externalReasoner() {
        Class[] struct = { Object.class, RList.class };
        SimplePredicate p = new SimplePredicate("reasoner", struct);
        Term[] tmpTerm = { new ConstantTermImpl("reasoner", java.lang.String.class), RListUtils.rlist("[reasoner,Reasoner]") };
        return new FactImpl(p, tmpTerm);
    }

    public static FactImpl externalTypeSystem() {
        Class[] struct = { Object.class, RList.class };
        SimplePredicate p = new SimplePredicate("import", struct);
        Term[] tmpTerm = { new ConstantTermImpl("import", java.lang.String.class), RListUtils.rlist("[import,URI]") };
        return new FactImpl(p, tmpTerm);
    }
}
