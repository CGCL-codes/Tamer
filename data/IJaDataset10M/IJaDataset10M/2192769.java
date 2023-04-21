package it.unibo.is.communication.sharedSpace.space;

import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Term;
import alice.tuprolog.lib.InvalidObjectIdException;

public interface ILocalSpace {

    /**
	 * Load a theory from the classpath
	 * @param theoryFileName the file name in the classpath
	 * @throws Exception
	 */
    public void loadTheory(String theoryFileName) throws Exception;

    /**
	 * Add a theory loaded from the classpath
	 * @param theoryFileName
	 * @throws Exception
	 */
    public void addTheory(String theoryFileName) throws Exception;

    public void addTheoryRules(String theory) throws Exception;

    /**
	 * Performs a function-like query that includes a var symbol X
	 * intended as the result
	 * @param queryS must contain a variable X
	 * @return the value bound to X
	 * @throws Exception
	 */
    public String standardQuery(String queryS) throws Exception;

    /**
	 * Gives the next solution of a previous standard query
	 * @throws Exception
	 */
    public String nextSolutionStandardQuery() throws Exception;

    /**
	 * Performs a  query returning a String
	 * @param queryS
	 * @return the result as a solution
	 * @throws Exception
	 */
    public String query(String queryS) throws Exception;

    /**
	 * Performs a  query returning a Prolog Term
	 * @param queryS
	 * @throws Exception
	 */
    public Term queryT(String queryS) throws Exception;

    /**
	 * Gives the next solution of a previous query
	 * @return the result as a solution
	 * @throws Exception
	 */
    public String nextSolution() throws Exception;

    /**
	 * Performs a  query
	 * @param queryS
	 * @return the result given by the interpreter
	 * @throws Exception
	 */
    public SolveInfo solve(String queryS) throws Exception;

    /**
	 * Gives the next solution of a previous query
	 * @return the result given by the interpreter
	 * @throws NoMoreSolutionException
	 */
    public SolveInfo nextSolInfo() throws NoMoreSolutionException;

    /**
	 * Register an object with the name term
	 * @param term
	 * @param obj
	 * @throws InvalidObjectIdException
	 */
    public void register(String term, Object obj) throws InvalidObjectIdException;

    /**
	 * Gives the object registred with the name therm
	 * @param term
	 * @throws InvalidObjectIdException
	 */
    public Object getRegisteredObject(String term) throws InvalidObjectIdException;

    /**
	 * Unregister the object with the name term
	 * @param term
	 * @throws InvalidObjectIdException
	 */
    public void unregister(String term) throws InvalidObjectIdException;
}
