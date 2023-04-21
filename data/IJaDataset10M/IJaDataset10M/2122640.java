package org.wsml.reasoner.datalog.wrapper.mins;

import java.util.*;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.deri.mins.*;
import org.deri.mins.api.DBInterface;
import org.deri.mins.builtins.*;
import org.deri.mins.terms.ConstTerm;
import org.deri.mins.terms.Term;
import org.deri.mins.terms.Variable;
import org.wsml.reasoner.api.queryanswering.VariableBinding;
import org.wsml.reasoner.datalog.ConjunctiveQuery;
import org.wsml.reasoner.datalog.Constant;
import org.wsml.reasoner.datalog.DataTypeValue;
import org.wsml.reasoner.datalog.Literal;
import org.wsml.reasoner.datalog.Predicate;
import org.wsml.reasoner.datalog.Program;
import org.wsml.reasoner.datalog.QueryResult;
import org.wsml.reasoner.datalog.wrapper.DatalogReasonerFacade;
import org.wsml.reasoner.datalog.wrapper.ExternalToolException;
import org.wsml.reasoner.datalog.wrapper.UnsupportedFeatureException;
import org.wsml.reasoner.impl.VariableBindingImpl;

/**
 * Package: package org.wsml.reasoner.datalog.wrapper.mins;

 * Author: Darko Anicic, DERI Innsbruck, 
 *         Holger Lausen, DERI Innsbruck
 * Date: 15.09.2005  17:24:24
 */
public class MinsFacade implements DatalogReasonerFacade {

    private Logger logger = Logger.getLogger("org.wsml.reasoner.wsmlcore.wrapper.mins");

    private static final Predicate PRED_CONSTRAINT = new Predicate("mins-constraint", 1);

    ;

    /**
	 * Here we store a MINS Engine which contains the compiled KB for each
	 * registered ontology
	 */
    private Map<String, RuleSet> registeredKbs = new HashMap<String, RuleSet>();

    private MinsSymbolMap symbTransfomer = new MinsSymbolMap(new MinsSymbolFactory());

    /**
	 * Creates a facade object that allows to invoke the MINS rule system for
	 * performing query evaluation tasks.
	 */
    public MinsFacade() {
        super();
        logger.setLevel(Level.OFF);
    }

    /**
	 * Evaluates a Query on a Datalog knowledgebase. The actual evaluation is
	 * done by the MINS system.
	 * 
	 * @throws ExternalToolException
	 */
    public QueryResult evaluate(ConjunctiveQuery q, String ontologyIRI) throws ExternalToolException {
        VariableBinding varBinding;
        int evaluationMethod = 2;
        try {
            QueryResult result = new QueryResult(q);
            RuleSet minsEngine = registeredKbs.get(ontologyIRI);
            minsEngine.debuglevel = 0;
            Rule query = translateQuery(q, minsEngine);
            List<Literal> cqlits = new LinkedList<Literal>();
            org.wsml.reasoner.datalog.Variable v = new org.wsml.reasoner.datalog.Variable("query");
            cqlits.add(new Literal(PRED_CONSTRAINT, new org.wsml.reasoner.datalog.Term[] { v }));
            Rule cQuery = translateQuery(new ConjunctiveQuery(cqlits), minsEngine);
            logger.info("Starting MINS evaluation");
            minsEngine.setEvaluationMethod(evaluationMethod);
            if (evaluationMethod == 0 || evaluationMethod == 1) {
                minsEngine.Stratify();
            }
            minsEngine.evalQueries();
            logger.info("Computing substitutions");
            Substitution s0 = minsEngine.getSubstitution(cQuery);
            Enumeration enm0 = s0.elements();
            if (enm0.hasMoreElements()) {
                String error = "";
                GroundAtom a = (GroundAtom) enm0.nextElement();
                for (int i = 0; i < a.terms.length; i++) {
                    String wsmlTerm = symbTransfomer.convertToWSML(a.terms[i]);
                    error += wsmlTerm;
                }
                throw new ConstraintViolationError("Constraint Violated:" + error);
            }
            Substitution s = minsEngine.getSubstitution(query);
            Enumeration enm = s.elements();
            while (enm.hasMoreElements()) {
                GroundAtom a = (GroundAtom) enm.nextElement();
                varBinding = new VariableBindingImpl();
                for (int i = 0; i < a.terms.length; i++) {
                    if (!(a.terms[i] instanceof Variable)) {
                        String wsmlVariable = symbTransfomer.convertToWSML(i, q);
                        String wsmlTerm = symbTransfomer.convertToWSML(a.terms[i]);
                        varBinding.put(wsmlVariable, wsmlTerm);
                    }
                }
                result.getVariableBindings().add(varBinding);
                logger.info("Added new variable binding to result set: " + varBinding);
            }
            return result;
        } catch (Exception e) {
            System.out.println("\n\n\n\n\n\n");
            e.printStackTrace();
            throw new ExternalToolException("MINS can not handle given query", e, q);
        }
    }

    /**
	 * Translate a knowledgebase
	 * 
	 * @param p -
	 *            the datalog program that constitutes the knowledgebase
	 * @throws UnsupportedFeatureException
	 */
    private void translateKnowledgebase(Program p, RuleSet minsEngine) throws ExternalToolException, UnsupportedFeatureException {
        logger.info("Translate knowledgebase :" + p);
        if (p == null) {
            logger.info("KB is not referenced. Assume empty KB.");
            return;
        }
        for (org.wsml.reasoner.datalog.Rule r : p) {
            translateRule(r, minsEngine);
        }
    }

    /**
	 * Translate the query
	 * 
	 * @param p -
	 *            the datalog program that constitutes the knowledgebase
	 * @return an object that represents the same query in the Mandrax system
	 * @throws UnsupportedFeatureException
	 */
    private Rule translateQuery(ConjunctiveQuery query, RuleSet minsEngine) throws ExternalToolException, UnsupportedFeatureException {
        Atom atom;
        Body body;
        Body[] bodies;
        Literal l;
        int no;
        List<Literal> qBody = query.getLiterals();
        bodies = new Body[qBody.size()];
        for (int i = 0; i < qBody.size(); i++) {
            l = qBody.get(i);
            no = symbTransfomer.convertToTool(l.getSymbol());
            atom = translateLiteral2Atom(l, query);
            body = new Body(no, false, atom.terms);
            bodies[i] = body;
        }
        Rule rule = new Rule(null, bodies);
        minsEngine.addRule(rule);
        return rule;
    }

    /**
	 * Translate a datalog rule
	 * 
	 * @throws UnsupportedFeatureException
	 */
    private void translateRule(org.wsml.reasoner.datalog.Rule r, RuleSet minsEngine) throws ExternalToolException, UnsupportedFeatureException {
        Rule rule;
        GroundAtom groundAtom;
        Body body;
        Body[] bodies;
        Head head;
        Predicate p;
        int no;
        Atom atom;
        Literal l;
        if (r.isConstraint()) {
            Constant cBody = new Constant(r.getBody().toString());
            r = new org.wsml.reasoner.datalog.Rule(new Literal(PRED_CONSTRAINT, new org.wsml.reasoner.datalog.Term[] { cBody }), r.getBody());
        }
        l = r.getHead();
        no = symbTransfomer.convertToTool(l.getSymbol());
        atom = translateLiteral2Atom(l, r);
        head = new Head(no, atom.terms);
        List<Literal> rBody = r.getBody();
        if (rBody.size() == 0) {
            groundAtom = new GroundAtom(atom.terms);
            minsEngine.addFact(no, groundAtom);
        } else {
            bodies = new Body[rBody.size()];
            for (int i = 0; i < rBody.size(); i++) {
                l = rBody.get(i);
                no = symbTransfomer.convertToTool(l.getSymbol());
                atom = translateLiteral2Atom(l, r);
                body = new Body(no, false, atom.terms);
                bodies[i] = body;
            }
            rule = new Rule(new Head[] { head }, bodies);
            minsEngine.addRule(rule);
        }
    }

    /**
     * 
     * @param literal
     * @param queryOrRuleContext a datalog rule or query to keep context for Variable mapping
     * @return
     * @throws ExternalToolException
	 */
    private Atom translateLiteral2Atom(Literal literal, Object queryOrRuleContext) throws ExternalToolException {
        org.wsml.reasoner.datalog.Term[] args = literal.getArguments();
        Term[] terms = new Term[args.length];
        Atom atom;
        long predName = symbTransfomer.convertToTool(literal.getSymbol());
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof org.wsml.reasoner.datalog.Variable) {
                terms[i] = new Variable(symbTransfomer.convertToTool((org.wsml.reasoner.datalog.Variable) args[i], queryOrRuleContext));
            } else if (args[i] instanceof Constant) {
                terms[i] = new ConstTerm(symbTransfomer.convertToTool((Constant) args[i]));
            } else if (args[i] instanceof DataTypeValue) {
            } else {
                throw new RuntimeException("Uwe says we will not arrive here.");
            }
        }
        atom = new Atom(terms);
        return atom;
    }

    /** (non-Javadoc)
	 * 
	 * @see org.wsml.reasoner.wsmlcore.wrapper.mins.DatalogReasonerFacade#useSymbolFactory(org.wsml.reasoner.wsmlcore.wrapper.mins.SymbolFactory)
	 */
    public void useSymbolFactory(org.wsml.reasoner.datalog.wrapper.SymbolFactory sf) {
    }

    public void register(String ontologyIRI, Program kb) throws ExternalToolException {
        Builtin[] buildInList = new Builtin[] { new Builtin("Add", 3, new Add()), new Builtin("evaluable_", 2, new Evaluable()), new Builtin("unify", 2, new Equal()), new Builtin("isNum", 1, new IsNum()), new Builtin("isString", 1, new IsString()), new Builtin("isConst", 1, new IsConst()), new Builtin("less", 2, new Less()), new Builtin("lessorequal", 2, new Lessorequal()), new Builtin("greater", 2, new Greater()), new Builtin("greaterorequal", 2, new Greaterorequal()), new Builtin("between", 3, new Between()), new Builtin("starts", 2, new Starts()), new Builtin("ends", 2, new Ends()), new Builtin("lives", 2, new Lives()) };
        BuiltinConfig builtInConfig = new BuiltinConfig(buildInList);
        DBInterface db = new DB();
        RuleSet minsEngine = new RuleSet(builtInConfig, db);
        minsEngine.debuglevel = 0;
        try {
            translateKnowledgebase(kb, minsEngine);
        } catch (UnsupportedFeatureException e) {
            e.printStackTrace();
            throw new ExternalToolException("Unsupported feature for MINS in knowledgebase.");
        }
        registeredKbs.put(ontologyIRI, minsEngine);
    }
}
