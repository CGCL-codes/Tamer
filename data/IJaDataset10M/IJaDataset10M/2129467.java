package alice.semantics.tuple;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import alice.logictuple.InvalidLogicTupleException;
import alice.logictuple.LogicTuple;
import alice.semantics.TestUtil.TestUtils;
import alice.semantics.data.assertion.IndividualDescr;
import alice.semantics.exception.SemanticsException;
import alice.semantics.semanticNode.*;
import alice.semantics.tuple.processor.TupleProcessor;
import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import alice.tuprolog.Var;

@SuppressWarnings("serial")
public class SemanticLogicTuple extends LogicTuple {

    private IndividualDescr idescr;

    private String ind_name;

    private static Prolog prolog_engine = null;

    private static final String theory_file = "assert_parser.prolog";

    /**
	 * Constructs a semantic logic tuple providing the individual
	 * 
	 * @param idescr
	 * 			  @see alice.semantics.data.IndividualDescr
	 *            the description of the tuple (the individual)
	 */
    public SemanticLogicTuple(IndividualDescr idescr) {
        super(idescr.getStruct());
        ind_name = idescr.getName();
        this.idescr = idescr;
    }

    /**
	 * Constructs a semantic logic tuple providing the PrologString and the Ontology used
	 * 
	 * @return the resulting semantic tuple
	 * @throws InvalidLogicTupleException
	 *          if the text does not represent a valid logic tuple
	 * @throws InvalidTheoryException
	 *          if a not valid tuProlog theory has been specified
	 * @throws FileNotFoundException
	 *          if a not valid ontology file path has been specified
	 * @throws NoSolutionException
	 *          if a solution is asked, but actually none exists	
	 * @throws MalformedGoalException
	 *           means that a not well formed goal has been specified.
	 * @throws SemanticsException
	 *          if occourred an error in the semantics-level of the operation    
	 */
    public static SemanticLogicTuple parse(String st, IOntology ont) throws InvalidLogicTupleException, InvalidTheoryException, FileNotFoundException, NoSolutionException, MalformedGoalException, IOException, SemanticsException {
        long s = System.currentTimeMillis();
        Struct struct = parsePrologSentence(st);
        long e = System.currentTimeMillis();
        TestUtils.PrintResult("Parse Tuple", e - s, TestUtils._FileNameTupleParse);
        IndividualDescr ind_descr = TupleProcessor.makeIndividualDescr(struct, ont);
        s = System.currentTimeMillis();
        TestUtils.PrintResult("make Time", s - e, TestUtils._FileNameMakeIndividual);
        return new SemanticLogicTuple(ind_descr);
    }

    /**
	 * Constructs a semantic logic tuple providing the Logic Struct and the Ontology used
	 * 
	 * @return the resulting semantic tuple
	 * @throws FileNotFoundException
	 *          if a not valid ontology file path has been specified
	 * @throws MalformedGoalException
	 *          means that a not well formed goal has been specified.
	 * @throws SemanticsException
	 *          if occourred an error in the semantics-level of the operation    
	 */
    public static SemanticLogicTuple parse(Struct struct, IOntology ont) throws FileNotFoundException, IOException, SemanticsException {
        IndividualDescr ind_descr = TupleProcessor.makeIndividualDescr(struct, ont);
        return new SemanticLogicTuple(ind_descr);
    }

    /**
	 * Gets the semantic tuple individual description
	 */
    public IndividualDescr getIndividualDescr() {
        return idescr;
    }

    /**
	 * Set the semantic tuple individual description
	 */
    public void setIndividualDescr(IndividualDescr idescr) {
        this.idescr = idescr;
        ind_name = idescr.getName();
    }

    private static Struct parsePrologSentence(String st) throws InvalidTheoryException, FileNotFoundException, IOException, NoSolutionException, MalformedGoalException {
        setupPrologEngine();
        String goal = "parseAssertionWithPrefix(" + st + ",TERM).";
        SolveInfo info = prolog_engine.solve(goal);
        Term t = info.getSolution();
        Var v = (Var) (((Struct) t).getArg(1));
        Struct r = (Struct) v.getTerm();
        return r;
    }

    /**
	 * Gets the semantic tuple individual name
	 */
    public String getInd_name() {
        return ind_name;
    }

    public static void setupPrologEngine() throws InvalidTheoryException, FileNotFoundException, IOException {
        if (prolog_engine == null) {
            Theory th = new Theory(new FileInputStream(theory_file));
            prolog_engine = new Prolog();
            prolog_engine.setTheory(th);
        }
    }
}
