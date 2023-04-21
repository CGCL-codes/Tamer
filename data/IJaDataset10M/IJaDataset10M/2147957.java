package org.tm4j.tologx;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.tm4j.tologx.TologResultsSet;

/**
 * This is the high-level interface to the tolog 1.0
 * query engine. Using this interface you can initialise
 * the evaluator by loading one or more rules module files or
 * by adding rules as strings.
 * This class also supports the registration of extension
 * predicates.
 */
public interface QueryEvaluator {

    /**
	 * Loads a rules module from the specified stream.
	 * @param src the input stream to be parsed
	 * @throws IOException if the module file could not be read.
	 * @throws TologParserException if the module file could be
	 *         read but contained syntax errors.
	 */
    public void addRulesModule(InputStream src, String prefix) throws IOException, TologParserException;

    /**
	 * Adds a new rule to the QueryEvaluator.
	 * @param rule the rule string in tolog syntax
	 * @throws TologParserException if there is a syntax error in the rule string.
	 */
    public void addRule(String rule) throws TologParserException;

    /**
	 * Parses (and possibly optimizes) the specified tolog query string.
	 * The return value is the parsed query which can then be executed
	 * simply by calling {PreparedQuery#execute()}. 
	 * @param queryString the tolog query string to be parsed.
	 * @return the PreparedQuery that represents the parsed, optimized tolog query.
	 * @throws TologParserException  if a syntax error or parse-time error is found in <code>queryString</code>
	 * @throws TologProcessingException if an error occurs during optimisation of the query.
	 */
    public PreparedQuery prepareQuery(String queryString) throws TologParserException, TologProcessingException;

    /**
	 * A convenience method that parses and optimizes the specified
	 * tolog query and then executes it immediately.
	 * @param queryString the tolog query string to be evaluated
	 * @return the results of the query evaluation.
	 * @throws TologParserException if a syntax error or parse-time error is found in <code>queryString</code>
	 * @throws TologProcessingException if an error occurrs during the optimisation or evaluation of the query.
	 */
    public TologResultsSet execute(String queryString) throws TologParserException, TologProcessingException;

    /**
     * A convenience method that parses and optimizes the specified
     * tolog query and then executes it immediately.
     * @param queryString the tolog query string to be evaluated
     * @param queryParams the replacement values for % references in the query string
     * @return  the results of the query evaluation.
     * @throws TologParserException if a syntax error or parse-time error is found in <code>queryString</code>
     * @throws TologProcessingException if an error occurrs during the optimisation or evaluation of the query.
     */
    public TologResultsSet execute(String queryString, Object[] queryParams) throws TologParserException, TologProcessingException;

    /**
	 * Registers an extension predicate with the QueryEvaluator.
	 * All implementation classes MUST implement the {@link Predicate} interface.
	 * If <code>predicateName</code> is the name of an already registered predicate,
	 * then the new registration overwrites the old one.
	 * @param predicateName the name to be used to reference the predicate from a tolog query.
	 * @param predicateClass the implementation class for the predicate.
	 * @throws IllegalArgumentException if the specified implementation class does not implement the Predicate interface.
	 */
    public void registerPredicateClass(String predicateName, Class predicateClass) throws IllegalArgumentException;

    /**
	 * Returns a Map of the predicates currently registered with the 
	 * QueryEvaluator. The Map key is the name of the predicate and
	 * the value is the implementation Class of the predicate.
	 */
    public Map getRegisteredPredicates();
}
