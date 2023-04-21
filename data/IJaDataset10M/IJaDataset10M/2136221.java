package prisms.util;

/**
 * Partially implements a PreparedSearch
 * 
 * @param <S> The sub-type of search that an extension knows how to handle
 * @param <F> The type of field that the results of the search may be sorted with
 */
public abstract class AbstractPreparedSearch<S extends Search, F extends Sorter.Field> implements SearchableAPI.PreparedSearch<F> {

    private Search theSearch;

    private Sorter<F> theSorter;

    /** The sub-type of search that an extension knows how to handle */
    protected final Class<S> theSearchType;

    private Class<?>[] theParamTypes;

    /**
	 * @param search The search to prepare
	 * @param sorter The sorter to sort the results
	 * @param searchType The sub-type of search that an extension knows how to handle
	 */
    public AbstractPreparedSearch(Search search, Sorter<F> sorter, Class<S> searchType) {
        theSearch = search;
        theSorter = sorter;
        theSearchType = searchType;
        java.util.ArrayList<Class<?>> types = new java.util.ArrayList<Class<?>>();
        compileParamTypes(search, types, -1);
        theParamTypes = types.toArray(new Class[types.size()]);
    }

    public Search getSearch() {
        return theSearch;
    }

    public Sorter<F> getSorter() {
        return theSorter;
    }

    public int getParameterCount() {
        return theParamTypes.length;
    }

    public Class<?> getParameterType(int paramIdx) {
        return theParamTypes[paramIdx];
    }

    public Search getParentSearch(int paramIdx) {
        return compileParamTypes(theSearch, new java.util.ArrayList<Class<?>>(), paramIdx);
    }

    /**
	 * Compiles the list of all parameter types that will be required to execute this search
	 * 
	 * @param search The search to get the missing parameter types from
	 * @param types The collection to add the missing types to
	 * @param limit The maximum number of types to gather before exiting and returning the search
	 *        that passed the limit
	 * @return The search whose missing parameter(s) passed the given limit
	 */
    protected Search compileParamTypes(Search search, java.util.Collection<Class<?>> types, int limit) {
        if (search == null) return null;
        if (search instanceof prisms.util.Search.CompoundSearch) for (Search srch : (prisms.util.Search.CompoundSearch) search) {
            Search ret = compileParamTypes(srch, types, limit);
            if (ret != null) return ret;
        } else if (theSearchType.isInstance(search)) {
            addParamTypes(theSearchType.cast(search), types);
            if (limit >= 0 && types.size() > limit) return search;
        } else throw new IllegalArgumentException("Unrecognized search type: " + search.getClass().getName());
        return null;
    }

    /**
	 * Adds the types of any missing parameters in the search that must be supplied in order to
	 * execute this search
	 * 
	 * @param search The search to get the parameter types for
	 * @param types The collection to add the missing parameter types to
	 */
    protected abstract void addParamTypes(S search, java.util.Collection<Class<?>> types);
}
