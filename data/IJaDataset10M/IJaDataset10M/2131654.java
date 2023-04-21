package org.enerj.core;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import org.odmg.OQLQuery;
import org.odmg.QueryException;
import org.odmg.QueryInvalidException;
import org.odmg.QueryParameterCountInvalidException;
import org.odmg.QueryParameterTypeInvalidException;
import org.enerj.jga.fn.EvaluationException;
import org.enerj.jga.fn.UnaryFunctor;
import org.enerj.jga.fn.adaptor.ApplyUnary;
import org.enerj.query.oql.EvaluatorContext;
import org.enerj.query.oql.OQLLexer;
import org.enerj.query.oql.OQLParser;
import org.enerj.query.oql.ParserException;
import org.enerj.query.oql.TrackedPositionReader;
import org.enerj.query.oql.ast.AST;

/**
 * Ener-J client-side implementation of OQLQuery. <p>
 * 
 * @version $Id: EnerJOQLQuery.java,v 1.8 2006/02/24 03:00:41 dsyrstad Exp $
 * @author <a href="mailto:dsyrstad@ener-j.org">Dan Syrstad </a>
 */
public class EnerJOQLQuery implements OQLQuery {

    private UnaryFunctor[] mFunctors;

    /**
     * Construct an empty EnerJOQLQuery. You must call create() to configure the query. 
     *
     */
    public EnerJOQLQuery() {
    }

    /**
     * Construct a EnerJOQLQuery with the given query string. You need not call create() to configure the query.
     * See create() for more details.
     *  
     * @param aQuery an OQL query string.
     * 
     * @throws QueryInvalidException if the query syntax is invalid.
     */
    public EnerJOQLQuery(String aQuery) throws QueryInvalidException {
        create(aQuery);
    }

    /**
     * Construct a EnerJOQLQuery with the given query Reader. You need not call create() to configure the query.
     * See create() for more details.
     *  
     * @param aQueryReader the query input.
     * 
     * @throws QueryInvalidException if the query syntax is invalid.
     */
    public EnerJOQLQuery(Reader aQueryReader) throws QueryInvalidException {
        create(aQueryReader);
    }

    /** 
     * {@inheritDoc}
     * @see org.odmg.OQLQuery#create(java.lang.String)
     */
    public void create(String aQuery) throws QueryInvalidException {
        create(new StringReader(aQuery));
    }

    /** 
     * Creates a query from a Reader. 
     * 
     * @param aQueryReader the query input.
     * 
     * @throws QueryInvalidException if the query syntax is invalid.
     * 
     * @see org.odmg.OQLQuery#create(java.lang.String)
     */
    public void create(Reader aQueryReader) throws QueryInvalidException {
        EvaluatorContext context = new EvaluatorContext();
        EvaluatorContext.setContext(context);
        context.pushVariableScope();
        try {
            OQLLexer lexer = new OQLLexer(new TrackedPositionReader("OQL String", aQueryReader, 2048));
            OQLParser parser = new OQLParser(lexer);
            AST ast = parser.parse();
            if (ast == null) {
                throw new QueryInvalidException("No expression to evaluate.");
            }
            UnaryFunctor functor = ast.resolve();
            if (functor == null) {
                throw new QueryInvalidException("No expression to evaluate.");
            }
            if (functor instanceof ApplyUnary) {
                mFunctors = ((ApplyUnary) functor).getFunctors();
            } else {
                mFunctors = new UnaryFunctor[] { functor };
            }
        } catch (ParserException e) {
            throw new QueryInvalidException(e.getMessage());
        } catch (IOException e) {
            throw new QueryInvalidException(e.toString());
        } catch (QueryInvalidException e) {
            throw e;
        } catch (QueryException e) {
            throw new QueryInvalidException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new QueryInvalidException(e.getMessage());
        } finally {
            EvaluatorContext.getContext().dispose();
            EvaluatorContext.setContext(null);
        }
    }

    /** 
     * {@inheritDoc}
     * @see org.odmg.OQLQuery#bind(java.lang.Object)
     */
    public void bind(Object anParameter) throws QueryParameterCountInvalidException, QueryParameterTypeInvalidException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /** 
     * {@inheritDoc}
     * @see org.odmg.OQLQuery#execute()
     */
    public Object execute() throws QueryException {
        EvaluatorContext context = new EvaluatorContext();
        EvaluatorContext.setContext(context);
        try {
            if (mFunctors.length == 1) {
                return executeFunctor(mFunctors[0]);
            }
            ArrayList results = new ArrayList(mFunctors.length);
            for (UnaryFunctor f : mFunctors) {
                results.add(executeFunctor(f));
            }
            return results;
        } catch (IllegalArgumentException e) {
            throw new QueryException(e.getMessage());
        } catch (EvaluationException e) {
            throw new QueryException(e.getMessage());
        } catch (Exception e) {
            throw new QueryException("Execution error: " + e, e);
        } finally {
            EvaluatorContext.getContext().dispose();
            EvaluatorContext.setContext(null);
        }
    }

    /**
     * Executes a functor (fn()) and if the result is a Collection, it is wrapped in
     * a ResultList. 
     *
     * @param aFunctor the functor to execute.
     * 
     * @return the result.
     * 
     * @throws QueryException if an error occurs.
     */
    private Object executeFunctor(UnaryFunctor aFunctor) throws QueryException {
        Object result = aFunctor.fn(null);
        return result;
    }
}
