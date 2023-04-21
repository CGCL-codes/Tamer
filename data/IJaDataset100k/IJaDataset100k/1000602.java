package magoffin.matt.ieat.biz.impl.lucene;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import magoffin.matt.ieat.biz.BizContext;
import magoffin.matt.ieat.biz.BizContextHolder;
import magoffin.matt.ieat.biz.DomainObjectFactory;
import magoffin.matt.ieat.biz.RecipeSearchBiz;
import magoffin.matt.ieat.biz.RecipeSearchIndexBiz;
import magoffin.matt.ieat.domain.AdvancedSearchCriteria;
import magoffin.matt.ieat.domain.RecipeIngredient;
import magoffin.matt.ieat.domain.RecipeSearchCriteria;
import magoffin.matt.ieat.domain.UiIndex;
import magoffin.matt.ieat.domain.UiPaginationSupport;
import magoffin.matt.ieat.domain.UiSearchResults;
import magoffin.matt.ieat.domain.UiUserMatch;
import magoffin.matt.ieat.domain.Unit;
import magoffin.matt.lucene.IndexStatusCallback;
import magoffin.matt.lucene.LuceneService;
import magoffin.matt.lucene.SearchCriteria;
import magoffin.matt.lucene.SearchMatch;
import magoffin.matt.lucene.SearchResults;
import magoffin.matt.lucene.LuceneService.IndexQueryOp;
import magoffin.matt.util.DelegatingInvocationHandler;
import org.apache.commons.lang.mutable.MutableInt;
import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;

/**
 * RecipeSearchBiz implementation using Lucene to perform the searches.
 * 
 * <p>info</p>
 * 
 * @author Matt Magoffin (spamsqr@msqr.us)
 * @version $Revision: 39 $ $Date: 2009-05-05 23:30:47 -0400 (Tue, 05 May 2009) $
 */
public class RecipeSearchBizLuceneImpl implements RecipeSearchBiz, RecipeSearchIndexBiz {

    /** 
	 * The start error message Lucene throws as a RuntimeException when 
	 * there are no indexed Documents when trying to perform a sorted search.
	 */
    private static final String LUCENE_SORT_ERROR_EMPTY_INDEX = "no terms";

    private static final Integer ZERO = new Integer(0);

    private final Logger log = Logger.getLogger(RecipeSearchBizLuceneImpl.class);

    private LuceneService lucene;

    private DomainObjectFactory domainObjectFactory;

    private String recipeIndexType = IndexType.RECIPE.toString();

    private String ingredientIndexType = IndexType.INGREDIENT.toString();

    private String unitIndexType = IndexType.UNIT.toString();

    private String userIndexType = IndexType.USER.toString();

    private int topLevelIndexSectionNameLength = DEFAULT_TOP_LEVEL_INDEX_SECTION_NAME_LENGTH;

    private int[] allowableRecipeIndexPageSizes = new int[] { 5, 10, 25 };

    private int defaultRecipeIndexPageSize = 25;

    private int[] allowableUserIndexPageSizes = new int[] { 5, 10, 25 };

    private int defaultUserIndexPageSize = 10;

    private UiPaginationSupport recipePagination = null;

    private UiPaginationSupport userPagination = null;

    /**
	 * Initialize this instance after properties configured.
	 */
    @SuppressWarnings("unchecked")
    public synchronized void init() {
        if (domainObjectFactory == null) {
            throw new RuntimeException("domainObjectFactory not configured");
        }
        if (topLevelIndexSectionNameLength < 1) {
            topLevelIndexSectionNameLength = DEFAULT_TOP_LEVEL_INDEX_SECTION_NAME_LENGTH;
        }
        if (allowableRecipeIndexPageSizes == null || allowableRecipeIndexPageSizes.length < 1) {
            throw new RuntimeException("allowableRecipeIndexPageSizes not configured");
        }
        if (allowableUserIndexPageSizes == null || allowableUserIndexPageSizes.length < 1) {
            throw new RuntimeException("allowableUserIndexPageSizes not configured");
        }
        Set<Integer> tmpSet = new TreeSet<Integer>();
        for (int i = 0; i < allowableRecipeIndexPageSizes.length; i++) {
            if (allowableRecipeIndexPageSizes[i] < 1) {
                tmpSet.add(new Integer(-1));
            } else {
                tmpSet.add(new Integer(allowableRecipeIndexPageSizes[i]));
            }
        }
        if (!tmpSet.contains(new Integer(defaultRecipeIndexPageSize))) {
            throw new RuntimeException("defaultRecipeIndexPageSize value '" + defaultRecipeIndexPageSize + "' not available in allowableRecipeIndexPageSizes set " + tmpSet);
        }
        int[] tmpArray = new int[tmpSet.size()];
        int i = 0;
        this.recipePagination = domainObjectFactory.getPaginationSupportInstance();
        this.recipePagination.setDefaultPageSize(new Integer(defaultRecipeIndexPageSize));
        for (Iterator itr = tmpSet.iterator(); itr.hasNext(); i++) {
            Integer anInt = (Integer) itr.next();
            tmpArray[i] = anInt.intValue();
            if (tmpArray[i] < 0) {
                this.recipePagination.setAllowUnlimited(true);
            } else {
                this.recipePagination.getAllowablePageSize().add(anInt);
            }
        }
        allowableRecipeIndexPageSizes = tmpArray;
        if (allowableUserIndexPageSizes == null || allowableUserIndexPageSizes.length < 1) {
            throw new RuntimeException("allowableUserIndexPageSizes not configured");
        }
        if (allowableUserIndexPageSizes == null || allowableUserIndexPageSizes.length < 1) {
            throw new RuntimeException("allowableUserIndexPageSizes not configured");
        }
        tmpSet.clear();
        for (i = 0; i < allowableUserIndexPageSizes.length; i++) {
            if (allowableUserIndexPageSizes[i] < 1) {
                tmpSet.add(new Integer(-1));
            } else {
                tmpSet.add(new Integer(allowableUserIndexPageSizes[i]));
            }
        }
        if (!tmpSet.contains(new Integer(defaultUserIndexPageSize))) {
            throw new RuntimeException("defaultUserIndexPageSize value '" + defaultUserIndexPageSize + "' not available in allowableUserIndexPageSizes set " + tmpSet);
        }
        tmpArray = new int[tmpSet.size()];
        i = 0;
        this.userPagination = domainObjectFactory.getPaginationSupportInstance();
        this.userPagination.setDefaultPageSize(new Integer(defaultUserIndexPageSize));
        for (Iterator itr = tmpSet.iterator(); itr.hasNext(); i++) {
            Integer anInt = (Integer) itr.next();
            tmpArray[i] = anInt.intValue();
            if (tmpArray[i] < 0) {
                this.userPagination.setAllowUnlimited(true);
            } else {
                this.userPagination.getAllowablePageSize().add(anInt);
            }
        }
        allowableUserIndexPageSizes = tmpArray;
    }

    @SuppressWarnings("unchecked")
    public UiSearchResults findIngredientsSimilarlyNamed(IngredientCriteria criteria) {
        UiSearchResults sr = domainObjectFactory.getSearchResultsInstance();
        sr.setTotalResults(ZERO);
        sr.setPageStart(ZERO);
        if (criteria == null) {
            return sr;
        }
        SearchCriteria searchCriteria = (SearchCriteria) DelegatingInvocationHandler.wrapObject(criteria, SearchCriteria.class);
        SearchResults searchResults = lucene.find(ingredientIndexType, searchCriteria);
        sr.setTotalResults(searchResults.getTotalMatches());
        sr.getIngredient().addAll(searchResults.getMatches());
        return sr;
    }

    @SuppressWarnings("unchecked")
    public UiSearchResults findRecipes(final RecipeSearchCriteria criteria, final BizContext context) {
        UiSearchResults sr = domainObjectFactory.getSearchResultsInstance();
        sr.setTotalResults(ZERO);
        sr.setPageStart(ZERO);
        if (criteria == null) {
            return sr;
        }
        sr.setRecipeCriteria(criteria);
        RecipeSearchCriteriaBizContextHolder sCriteria = new RecipeSearchCriteriaBizContextHolder(criteria, context);
        SearchResults searchResults = lucene.find(recipeIndexType, sCriteria);
        sr.setTotalResults(searchResults.getTotalMatches());
        sr.getRecipe().addAll(searchResults.getMatches());
        return sr;
    }

    private static class RecipeSearchCriteriaBizContextHolder implements RecipeSearchCriteria, BizContextHolder, SearchCriteria {

        private RecipeSearchCriteria delegate;

        private BizContext bizContext;

        private int maxResults = -1;

        private int pageSize = -1;

        private int page = 1;

        private boolean countOnly = false;

        private RecipeSearchCriteriaBizContextHolder(RecipeSearchCriteria delegate, BizContext bizContext) {
            this.delegate = delegate;
            this.bizContext = bizContext;
        }

        public BizContext getBizContext() {
            return bizContext;
        }

        @SuppressWarnings("unchecked")
        public List<AdvancedSearchCriteria> getAdvanced() {
            return delegate.getAdvanced();
        }

        @SuppressWarnings("unchecked")
        public List<RecipeIngredient> getIngredient() {
            return delegate.getIngredient();
        }

        public String getMode() {
            return delegate.getMode();
        }

        public String getName() {
            return delegate.getName();
        }

        public String getSimpleQuery() {
            return delegate.getSimpleQuery();
        }

        public void setMode(String value) {
            delegate.setMode(value);
        }

        public void setName(String value) {
            delegate.setName(value);
        }

        public void setSimpleQuery(String value) {
            delegate.setSimpleQuery(value);
        }

        /**
		 * @return the maxResults
		 */
        public int getMaxResults() {
            return maxResults;
        }

        /**
		 * @param maxResults the maxResults to set
		 */
        public void setMaxResults(int maxResults) {
            this.maxResults = maxResults;
        }

        /**
		 * @return the pageSize
		 */
        public int getPageSize() {
            return pageSize;
        }

        /**
		 * @param pageSize the pageSize to set
		 */
        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        /**
		 * @return the page
		 */
        public int getPage() {
            return page;
        }

        /**
		 * @param page the page to set
		 */
        public void setPage(int page) {
            this.page = page;
        }

        /**
		 * @return the countOnly
		 */
        public boolean isCountOnly() {
            return countOnly;
        }

        /**
		 * @param countOnly the countOnly to set
		 */
        public void setCountOnly(boolean countOnly) {
            this.countOnly = countOnly;
        }
    }

    private List<SearchMatch> handleIndexSearch(final IndexCriteria criteria, final String indexType, final UiSearchResults results, final int[] allowedIndexPageSizes, final int defaultPageSize) {
        String displaySection = criteria.getDisplaySection();
        int sectionNameLength = displaySection == null ? topLevelIndexSectionNameLength : displaySection.length();
        UiIndex index = getUiIndex(indexType, sectionNameLength, displaySection);
        if (displaySection == null && index.getIndexSection().size() > 0) {
            UiIndex.IndexSectionType section = (UiIndex.IndexSectionType) index.getIndexSection().get(0);
            displaySection = section.getIndexKey();
            section.setSelected(true);
        }
        int pageSize = criteria.getPageSize();
        if (Arrays.binarySearch(allowedIndexPageSizes, pageSize) < 0) {
            if (log.isDebugEnabled()) {
                log.debug("Specified index page size '" + pageSize + "' not allowed, changing to default value '" + defaultPageSize + "'");
            }
            pageSize = defaultPageSize;
        }
        final MutableInt pageSizeInt = new MutableInt(pageSize);
        final List<SearchMatch> matches = new LinkedList<SearchMatch>();
        if (displaySection != null) {
            try {
                Query query = getIndexQuery(displaySection);
                lucene.doIndexQueryOp(indexType, query, false, new IndexQueryOp() {

                    public void doSearcherOp(String type, IndexSearcher searcher, Query searchQuery, Hits hits) throws IOException {
                        int start = pageSizeInt.intValue() * criteria.getPageStart();
                        results.setTotalResults(hits.length());
                        matches.addAll(lucene.build(indexType, hits, start, start + pageSizeInt.intValue()));
                    }
                });
            } catch (RuntimeException e) {
                if (!(e.getMessage() != null && e.getMessage().startsWith(LUCENE_SORT_ERROR_EMPTY_INDEX))) {
                    throw e;
                }
            }
        }
        if (results.getTotalResults() == null) {
            results.setTotalResults(ZERO);
        }
        results.setPageSize(new Integer(pageSize));
        results.setPageStart(new Integer(criteria.getPageStart()));
        results.setUiIndex(index);
        return matches;
    }

    @SuppressWarnings("unchecked")
    public UiSearchResults findRecipesForIndex(final IndexCriteria criteria) {
        UiSearchResults results = domainObjectFactory.getSearchResultsInstance();
        List<SearchMatch> matches = handleIndexSearch(criteria, recipeIndexType, results, allowableRecipeIndexPageSizes, defaultRecipeIndexPageSize);
        results.getRecipe().addAll(matches);
        results.setPagination(this.recipePagination);
        return results;
    }

    public Unit findUnitSimilarlyNamed(Unit criteria) {
        UnitCriteriaImpl sCriteria = new UnitCriteriaImpl(criteria, true);
        SearchResults searchResults = lucene.find(unitIndexType, sCriteria);
        if (searchResults.getMatches() != null && searchResults.getMatches().size() > 0) {
            return (Unit) searchResults.getMatches().get(0);
        }
        return null;
    }

    private static class UnitCriteriaImpl implements UnitCriteria, SearchCriteria {

        private Unit unit;

        private boolean useApproximateSearch;

        private int maxResults = -1;

        private int pageSize = -1;

        private int page = 1;

        private boolean countOnly = false;

        private UnitCriteriaImpl(Unit unit, boolean useApproximateSearch) {
            this.unit = unit;
            this.useApproximateSearch = useApproximateSearch;
        }

        public Unit getUnit() {
            return unit;
        }

        public boolean useApproximateSearch() {
            return useApproximateSearch;
        }

        /**
		 * @return the maxResults
		 */
        public int getMaxResults() {
            return maxResults;
        }

        /**
		 * @param maxResults the maxResults to set
		 */
        public void setMaxResults(int maxResults) {
            this.maxResults = maxResults;
        }

        /**
		 * @return the pageSize
		 */
        public int getPageSize() {
            return pageSize;
        }

        /**
		 * @param pageSize the pageSize to set
		 */
        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        /**
		 * @return the page
		 */
        public int getPage() {
            return page;
        }

        /**
		 * @param page the page to set
		 */
        public void setPage(int page) {
            this.page = page;
        }

        /**
		 * @return the countOnly
		 */
        public boolean isCountOnly() {
            return countOnly;
        }

        /**
		 * @param countOnly the countOnly to set
		 */
        public void setCountOnly(boolean countOnly) {
            this.countOnly = countOnly;
        }
    }

    @SuppressWarnings("unchecked")
    public UiSearchResults findUsersForIndex(IndexCriteria criteria) {
        UiSearchResults results = domainObjectFactory.getSearchResultsInstance();
        List<SearchMatch> matches = handleIndexSearch(criteria, userIndexType, results, allowableUserIndexPageSizes, defaultUserIndexPageSize);
        results.getUser().addAll(matches);
        results.setPagination(userPagination);
        return results;
    }

    public List<UiUserMatch> searchForUsers(UserCriteria criteria) {
        SearchCriteria searchCriteria = null;
        if (criteria instanceof SearchCriteria) {
            searchCriteria = (SearchCriteria) criteria;
        } else {
            throw new RuntimeException("Unknown criteria.");
        }
        SearchResults searchResults = lucene.find(userIndexType, searchCriteria);
        List<UiUserMatch> results = new LinkedList<UiUserMatch>();
        for (SearchMatch match : searchResults.getMatches()) {
            results.add((UiUserMatch) match);
        }
        return results;
    }

    public void indexIngredient(Integer ingredientId) {
        getLucene().indexObjectById(ingredientIndexType, ingredientId);
    }

    public void indexRecipe(Long recipeId) {
        getLucene().indexObjectById(recipeIndexType, recipeId);
    }

    public void indexUser(Integer userId) {
        getLucene().indexObjectById(userIndexType, userId);
    }

    public void recreateIngredientIndex() {
        IndexStatusCallback callback = lucene.reindex(ingredientIndexType);
        callback.waitUntilDone();
    }

    public void recreateRecipeIndex() {
        IndexStatusCallback callback = lucene.reindex(recipeIndexType);
        callback.waitUntilDone();
    }

    public synchronized void recreateSearchIndicies() {
        recreateIngredientIndex();
        recreateRecipeIndex();
        recreateUserIndex();
        recreateUnitIndex();
    }

    public void recreateUnitIndex() {
        IndexStatusCallback callback = lucene.reindex(unitIndexType);
        callback.waitUntilDone();
    }

    public void recreateUserIndex() {
        IndexStatusCallback callback = lucene.reindex(userIndexType);
        callback.waitUntilDone();
    }

    public void removeIngredientFromIndex(Integer ingredientId) {
        getLucene().deleteObjectById(ingredientIndexType, ingredientId);
    }

    public void removeRecipeFromIndex(Long recipeId) {
        getLucene().deleteObjectById(recipeIndexType, recipeId);
    }

    public void removeUserFromIndex(Integer userId) {
        getLucene().deleteObjectById(userIndexType, userId);
    }

    @SuppressWarnings("unchecked")
    private UiIndex getUiIndex(String indexType, int sectionNameLength, String displaySection) {
        UiIndex index = domainObjectFactory.getIndexInstance();
        Set<String> keys = lucene.getFieldTerms(indexType, IndexField.ITEM_INDEX_KEY.getFieldName());
        String currSection = "";
        for (String aSection : keys) {
            aSection = aSection.substring(0, sectionNameLength > aSection.length() ? aSection.length() : sectionNameLength);
            if (!aSection.equals(currSection)) {
                UiIndex.IndexSectionType section = domainObjectFactory.getIndexSelectionTypeInstance();
                section.setIndexKey(aSection);
                int count = getIndexCount(indexType, aSection);
                section.setCount(new Integer(count));
                if (aSection.equals(displaySection)) {
                    section.setSelected(true);
                }
                index.getIndexSection().add(section);
                currSection = aSection;
            }
        }
        return index;
    }

    private Query getIndexQuery(String section) {
        return new PrefixQuery(new Term(IndexField.ITEM_INDEX_KEY.getFieldName(), section));
    }

    private int getIndexCount(String indexType, String section) {
        try {
            Query query = getIndexQuery(section);
            if (log.isDebugEnabled()) {
                log.debug("Searching index [" + indexType + "] for section [" + query + "] sorted by field '" + IndexField.ITEM_INDEX_KEY.getFieldName() + "'");
            }
            final MutableInt result = new MutableInt(0);
            lucene.doIndexQueryOp(indexType, query, false, new LuceneService.IndexQueryOp() {

                public void doSearcherOp(String type, IndexSearcher searcher, Query searchQuery, Hits hits) throws IOException {
                    result.setValue(hits.length());
                }
            });
            return result.intValue();
        } catch (RuntimeException e) {
            if (!(e.getMessage() != null && e.getMessage().startsWith(LUCENE_SORT_ERROR_EMPTY_INDEX))) {
                throw e;
            }
            return 0;
        }
    }

    /**
	 * @return the domainObjectFactory
	 */
    public DomainObjectFactory getDomainObjectFactory() {
        return domainObjectFactory;
    }

    /**
	 * @param domainObjectFactory the domainObjectFactory to set
	 */
    public void setDomainObjectFactory(DomainObjectFactory domainObjectFactory) {
        this.domainObjectFactory = domainObjectFactory;
    }

    /**
	 * @return the lucene
	 */
    public LuceneService getLucene() {
        return lucene;
    }

    /**
	 * @param lucene the lucene to set
	 */
    public void setLucene(LuceneService lucene) {
        this.lucene = lucene;
    }

    /**
	 * @return the recipeIndexType
	 */
    public String getRecipeIndexType() {
        return recipeIndexType;
    }

    /**
	 * @param recipeIndexType the recipeIndexType to set
	 */
    public void setRecipeIndexType(String recipeIndexType) {
        this.recipeIndexType = recipeIndexType;
    }

    /**
	 * @return the ingredientIndexType
	 */
    public String getIngredientIndexType() {
        return ingredientIndexType;
    }

    /**
	 * @param ingredientIndexType the ingredientIndexType to set
	 */
    public void setIngredientIndexType(String ingredientIndexType) {
        this.ingredientIndexType = ingredientIndexType;
    }

    /**
	 * @return the unitIndexType
	 */
    public String getUnitIndexType() {
        return unitIndexType;
    }

    /**
	 * @param unitIndexType the unitIndexType to set
	 */
    public void setUnitIndexType(String unitIndexType) {
        this.unitIndexType = unitIndexType;
    }

    /**
	 * @return the userIndexType
	 */
    public String getUserIndexType() {
        return userIndexType;
    }

    /**
	 * @param userIndexType the userIndexType to set
	 */
    public void setUserIndexType(String userIndexType) {
        this.userIndexType = userIndexType;
    }
}
