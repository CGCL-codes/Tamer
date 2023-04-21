package ca.ubc.jquery.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.ui.IMemento;
import ca.ubc.jquery.JQueryBackendPlugin;

/**
 * A precompiled query which can be executed.
 * 
 * @author lmarkle
 */
public abstract class JQuery implements Cloneable, Serializable {

    public static final long serialVersionUID = 1L;

    /** 
	 * Used when applying filters, if we pass in this value the filtered variable
	 * is treated like a variable when the filter is applied.  Otherwise the position
	 * is the position in the list we wish to apply the filter to.
	 */
    public static final int NoPosition = -1;

    protected List chosenVars;

    protected String queryString;

    private Map filters;

    private Map boundVars;

    private String recursiveVar;

    protected JQuery(String query) {
        queryString = query;
        chosenVars = new ArrayList();
        boundVars = new ConcurrentHashMap();
        filters = new HashMap();
        recursiveVar = null;
    }

    /**
	 * Saves the JQuery object to the memento.  This is to help make saving JQueryScapes 
	 * easier to manage and easier to restore (or share with your friends!)
	 * 
	 * @param memento
	 */
    public void saveState(IMemento memento) throws JQueryException {
        memento.putString("query", getString());
        if (isRecursive()) {
            memento.putString("recursiveVar", getRecursiveVar());
        } else {
            memento.putString("recursiveVar", "");
        }
        Object[] temp = getChosenVars().toArray();
        IMemento mem = memento.createChild("chosenVars");
        mem.putInteger("size", temp.length);
        for (int i = 0; i < temp.length; i++) {
            mem.putString("var" + i, temp[i].toString());
        }
        temp = getFilterMap().entrySet().toArray();
        mem = memento.createChild("filters");
        mem.putInteger("size", temp.length);
        for (int i = 0; i < temp.length; i++) {
            Map.Entry e = (Map.Entry) temp[i];
            Object[] f = (Object[]) e.getValue();
            mem.putString("name" + i, e.getValue().toString());
            mem.putString("var" + i, f[1].toString());
            mem.putInteger("position" + i, ((Integer) f[2]).intValue());
            IMemento query = mem.createChild("query" + i);
            ((JQuery) f[0]).saveState(query);
        }
        temp = getBoundVars().entrySet().toArray();
        mem = memento.createChild("boundVars");
        mem.putInteger("size", temp.length);
        for (int i = 0; i < temp.length; i++) {
            Map.Entry e = (Map.Entry) temp[i];
            mem.putString("var" + i, e.getKey().toString());
            mem.putString("val" + i, e.getValue().toString());
        }
    }

    protected static JQuery createQuery(IMemento memento) throws JQueryException {
        JQuery result = JQueryAPI.createQuery(memento.getString("query"));
        String rVar = memento.getString("recursiveVar");
        IMemento mem = memento.getChild("chosenVars");
        int size = mem.getInteger("size").intValue();
        List vars = new ArrayList();
        for (int i = 0; i < size; i++) {
            vars.add(mem.getString("var" + i));
        }
        result.setChosenVars(vars);
        mem = memento.getChild("filters");
        size = mem.getInteger("size").intValue();
        for (int i = 0; i < size; i++) {
            String name = mem.getString("name" + i);
            String var = mem.getString("var" + i);
            int position = mem.getInteger("position" + i).intValue();
            IMemento query = mem.getChild("query" + i);
            JQuery jq = JQueryAPI.createQuery(query);
            result.addFilter(name, jq, var, position);
        }
        mem = memento.getChild("boundVars");
        size = mem.getInteger("size").intValue();
        for (int i = 0; i < size; i++) {
            String var = mem.getString("var" + i);
            Object val = mem.getString("val" + i);
            result.bind(var, val);
        }
        return result;
    }

    /**
	 * Replace this query with the given one.  This method is for convenience because it's
	 * actually not easy to replace a query with another due to the way filters are applied
	 * and variables are bound.  
	 * 
	 * Basically this method forces this query to have the same filters, chose variables,
	 * and recursive properties of the replacement.  It does however keep it's bound 
	 * variables.
	 * 
	 * @param replacement query to replace
	 * @throws JQueryException if the replacement is not a valid JQuery
	 */
    public void replaceWith(JQuery replacement) throws JQueryException {
        setString(replacement.getString());
        chosenVars = replacement.chosenVars;
        filters = replacement.filters;
        recursiveVar = replacement.recursiveVar;
    }

    /**
	 * @return true if this query is to be executed recursively (see getRecursiveVar() to
	 * know how to recurse the query.
	 */
    public boolean isRecursive() {
        return (recursiveVar != null);
    }

    /** 
	 * Returns the recursive variable for this query.  Null if the query is not recursive.
	 * If a query is recursive (this variable is a valid variable) then when a user is
	 * building the results tree for this query, it is expected they re-execute the query
	 * at by binding the target to the values returned in the recursive variable.
	 * 
	 * The reason this isn't done with execute() at the API level is because most query 
	 * engines don't have good support to know when they've reached a value they've already
	 * generated and should stop searching.
	 */
    public String getRecursiveVar() {
        return recursiveVar;
    }

    /** 
	 * Sets the queries recursive variable.  When building results and we encounter a recursive
	 * variable, we are expected to re-execute the query to obtain the new results.  Support for
	 * this feature is left up to the designers of the GUI.
	 * 
	 * @param var the variable over which to recurse
	 */
    public void setRecursiveVar(String var) {
        recursiveVar = var;
    }

    /**
	 * Returns the query string used to generate this query
	 */
    public String getString() {
        return queryString;
    }

    protected boolean containsThisVar() {
        return queryString.contains(JQueryAPI.getThisVar());
    }

    @Override
    public String toString() {
        return "JQuery(" + queryString + "," + chosenVars + ")";
    }

    @Override
    public Object clone() {
        try {
            JQuery query = JQueryAPI.createQuery(queryString);
            for (Iterator it = chosenVars.iterator(); it.hasNext(); ) {
                query.chosenVars.add(it.next());
            }
            for (Iterator it = filters.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry e = (Map.Entry) it.next();
                query.filters.put(e.getKey(), e.getValue());
            }
            for (Iterator it = boundVars.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry e = (Map.Entry) it.next();
                query.boundVars.put(e.getKey(), e.getValue());
            }
            query.queryString = queryString;
            query.recursiveVar = recursiveVar;
            return query;
        } catch (JQueryException e) {
            JQueryBackendPlugin.error("Error cloning existing query: ", e);
            return null;
        }
    }

    public void setString(String query) throws JQueryException {
        queryString = query;
        for (Iterator it = boundVars.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry e = (Map.Entry) it.next();
            try {
                bind((String) e.getKey(), e.getValue());
            } catch (JQueryException ex) {
            }
        }
        chosenVars.clear();
    }

    /**
	 * Executes the query and returns the results.
	 */
    public JQueryResultGraph getGraph() throws JQueryException {
        List x = getChosenVars();
        return new JQueryResultGraph(this, (String[]) x.toArray(new String[x.size()]));
    }

    /**
	 * Executes the query and returns the results.
	 */
    public JQueryResultSet execute() throws JQueryException {
        JQuery q = applyFilters();
        q.bindVariables();
        JQueryResultSet result;
        if (chosenVars.isEmpty()) {
            String[] v = getVariables();
            result = q.execute(v);
        } else {
            String[] v = (String[]) chosenVars.toArray(new String[chosenVars.size()]);
            result = q.execute(v);
        }
        return result;
    }

    /**
	 * Executes the query and returns the results for a given variable ordering.
	 */
    protected abstract JQueryResultSet execute(String vars[]) throws JQueryException;

    /**
	 * Returns the set of all variables found in the query.
	 */
    public abstract String[] getVariables() throws JQueryException;

    /**
	 * Applies a filter to the query and returns a new query (with the filter applied).
	 * 
	 * Filters are essentially just queries with a special meaning attached to the this variable.
	 * This is substituted to match the given var so that the filter is applied to a specific 
	 * variable in the query.
	 * 
	 * Position is used if the given variable describes a list.  We want the position to specify
	 * how deep into the list we will apply the filter.  In most cases we want to ignore this
	 * parameter and set it to NoPosition.
	 * 
	 * @param filter 
	 * @param var variable to apply the filter to (a variable from query)
	 * @param position position if the variable to apply the filter to is a list (0..N)
	 * @return a query with the given filter applied
	 * @throws JQueryException
	 */
    protected abstract JQuery applyFilter(JQuery filter, String var, int position) throws JQueryException;

    /**
	 * Binds a variable ot a value
	 */
    public void bind(String var, Object value) throws JQueryException {
        boundVars.put(var, value);
    }

    /**
	 * Unbinds all variables in this query
	 */
    public void unbindVariables() {
        boundVars.clear();
    }

    protected Map getBoundVars() {
        return boundVars;
    }

    /**
	 * Sets the variables of interest for this query, preserving the given order. It is the caller's responsibility to ensure that the list contains only elements obtainable from a call to getVarsFromQuery. Any other values will result in a
	 * thrown Exception.
	 * 
	 * @param vars:
	 *            The ordered list of variables
	 */
    public void setChosenVars(List vars) throws JQueryException {
        if (!getVarsFromQuery().containsAll(vars)) {
            throw new JQueryException("Chosen variables do not match variables in the query");
        }
        chosenVars = new ArrayList(vars);
    }

    /**
	 * Sets the variables of interest for this query, preserving the given order. It is 
	 * the caller's responsibility to ensure that the list contains only elements 
	 * obtainable from a call to getVarsFromQuery. Any other values will result in a
	 * thrown Exception.
	 * 
	 * @param vars:
	 *            The ordered list of variables
	 */
    public void setChosenVars(String[] vars) throws JQueryException {
        List x = new ArrayList();
        for (int i = 0; i < vars.length; i++) {
            x.add(vars[i]);
        }
        setChosenVars(x);
    }

    /**
	 * @return the ordered list of variables currently set for this query. These are the 
	 * variables that should be used to determine the structure of the results tree after 
	 * the query has been run. The list's elements are of type String.
	 * If no variables have been selected, this method will return all available 
	 * variables, in no particular order.
	 */
    public List getChosenVars() throws JQueryException {
        if (chosenVars != null) {
            chosenVars.retainAll(getVarsFromQuery());
        }
        List chosen;
        if (chosenVars != null && !chosenVars.isEmpty()) {
            chosen = chosenVars;
        } else {
            chosen = new ArrayList(getVarsFromQuery());
            chosen.remove(JQueryAPI.getThisVar());
        }
        if (!allowQueryToBindToThisNode() && !chosen.isEmpty()) {
            chosen.remove(JQueryAPI.getThisVar());
        }
        return chosen;
    }

    /**
	 * Returns the set of all variables found in the query. Any of these variables may be 
	 * safely used when constructing a results tree for this query.
	 */
    protected Set getVarsFromQuery() throws JQueryException {
        HashSet result = new HashSet();
        String[] t;
        t = getVariables();
        if (t != null) {
            for (int i = 0; i < t.length; i++) {
                result.add(t[i]);
            }
            if (queryString.contains(JQueryAPI.getThisVar())) {
                result.add(JQueryAPI.getThisVar());
            }
        }
        return result;
    }

    /**
	 * Determines whether to allow the variable API This var to be included in the 
	 * chosenVars list. This field only has an effect if the Query is constructor contains
	 * a ResultsTreeNode argument..
	 * 
	 * When a (sub)Query is constructed for a tree node (eg: call to any constructor with 
	 * a ResultsTreeNode argument), an "optionalTargetExpression" created which 
	 * essentially binds the tree node's contained element to the variable ?this. It
	 * is often not desirable to have the ?this level showing up in the subtree, since 
	 * it already appears at the root of the subtree.
	 */
    public boolean allowQueryToBindToThisNode() {
        boolean result = queryString.contains(JQueryAPI.getThisVar());
        return result;
    }

    /**
	 * Binds the query variables.  This method is called just inside execute();
	 */
    protected abstract void bindVariables() throws JQueryException;

    /**
	 * Applies the filters to the query and returns a new query with those filters
	 * applied.  Called inside execute();
	 */
    protected JQuery applyFilters() throws JQueryException {
        JQuery result = (JQuery) clone();
        Iterator it = filters.entrySet().iterator();
        try {
            JQuery q = result;
            for (int count = 0; it.hasNext(); count++) {
                Map.Entry e = (Map.Entry) it.next();
                Object[] value = (Object[]) e.getValue();
                JQuery filter = (JQuery) value[0];
                String var = (String) value[1];
                int pos = ((Integer) value[2]).intValue();
                q = q.applyFilter(filter, var, pos);
            }
            result = q;
        } catch (JQueryException ex) {
            throw new JQueryException("Unable to filter: " + result.getString() + ": ", ex);
        }
        result.boundVars.putAll(boundVars);
        return result;
    }

    /**
	 * @return a list of filter names applied to this query
	 */
    public List getFilters() {
        List result = new ArrayList();
        for (Iterator it = filters.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry e = (Map.Entry) it.next();
            result.add(e.getKey());
        }
        return result;
    }

    protected Map getFilterMap() {
        return filters;
    }

    /**
	 * Adds a filter to the query.
	 * @param name Name of the filter (user readable)
	 * @param query Filter to apply
	 * @param var the variable the apply the filter  to
	 * @param position TODO
	 */
    public void addFilter(String name, JQuery query, String var, int position) {
        filters.put(name, new Object[] { query, var, position });
    }

    /**
	 * Removes the named filter from this query.  Does nothing if the name does not match
	 * an existing filter
	 * @param name
	 */
    public void removeFilter(String name) {
        filters.remove(name);
    }
}
