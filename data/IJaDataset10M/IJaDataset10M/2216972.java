package de.denkselbst.niffler.prologservice;

import java.util.BitSet;
import java.util.List;
import de.denkselbst.niffler.examples.Examples;
import de.denkselbst.niffler.util.Tuple2;
import de.denkselbst.prologinterface.prolog.PrologException;
import de.denkselbst.prologinterface.prolog.Solution;
import de.denkselbst.prologinterface.prolog.Solutions;
import de.denkselbst.prologinterface.terms.PrologClause;
import de.denkselbst.prologinterface.terms.PrologStructure;
import de.denkselbst.prologinterface.terms.PrologTerm;
import de.denkselbst.prologinterface.terms.PrologVariable;

/**
 * The functionality the Niffler ILP system needs from a Prolog system.
 * For single-threaded use only.  
 * 
 * assertz
 * queries
 * coverage -- for better speed than is possible with coverage calc
 * collect term values for examples (NRF, ...)
 * clear engine
 * 
 * bc-migration?! yes!
 * 
 * @author patrick
 */
public interface PrologService {

    public void assertz(PrologClause clause) throws PrologException;

    public void retractEverything() throws PrologException;

    public boolean hasSolution(PrologClause query) throws PrologException;

    public Solution firstSolution(PrologClause query) throws PrologException;

    public Solutions firstNSolutions(PrologClause query, int n) throws PrologException;

    public Solution firstSolution(PrologStructure singleQueryLiteral) throws PrologException;

    public Solutions firstNSolutions(PrologStructure singleQueryLiteral, int n) throws PrologException;

    /**
     * Returns a bitset marking the examples covered by a given clause.
     * 
     * @param c the clause whose coverage to calculate.
     * 
     * @param activeExamples the examples that have a chance to be covered;
     *        in systems that only specialise during refinement, this set
     *        is narrowed down as the refinement progresses. Thus a large
     *        number of (positive) examples can be excluded from the 
     *        coverage check.
     *        
     * @param examples the examples to learn from.
     * 
     * @return a bitset marking the <code>activeExamples<code> that are
     *         covered by <code>c</code>.
     *         
     * @throws PrologException 
     */
    public BitSet getCoverage(PrologClause c, BitSet activeExamples, Examples examples) throws PrologException;

    /**
     * Returns true if <code>c</code> covers <code>example</code>. The clause is not
     * asserted; rather it is converted into a query in the manner illustrated below:
     * <pre>
     * example: foo(marmite, toast)
     * clause:  foo(A, B) :- bar(A,C), hunoz(C, B).
     * query:   :- foo(marmite, toast), bar(marmite, C), hunoz(C, toast).
     * </pre>
     * 
     * @param c clause.
     * @param example example.
     * 
     * @return true if <code>c</code> covers <code>example</code>.
     */
    public boolean covers(PrologClause c, PrologStructure example);

    /**
     * Finds for each active example the first <code>n</code> solutions 
     * to the query <code>clause<code> and stores the binding of
     * <code>variable</code> associated with the respective example index.
     * 
     * @param   clause query clause.
     * @param   variable variable whose first intstantiation to collect for 
     *          each active example.
     * @param   activeExamples the active example indices.
     * @param   examples the actual example container.
     * @param	n max number of solutions to the query.
     * 
     * @return  a List of tuples that associate the index of an active
     *          example to the value that <code>variable</code> was first 
     *          bound to when executing the query <code>clause</code> with 
     *          the active example.
     */
    public List<Tuple2<Integer, PrologTerm>> findValues(PrologClause clause, PrologVariable variable, BitSet activeExamples, Examples examples, int n);

    /**
     * Parses a single clause from each given String.
     * @param individualClauseStrings the input strings.
     * @return the corresponding clauses (one per input string).
     */
    public List<PrologClause> parseClauses(String... individualClauseStrings);
}
