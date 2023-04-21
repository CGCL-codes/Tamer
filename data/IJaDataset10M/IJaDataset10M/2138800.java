package org.gbif.checklistbank.search.service;

import org.gbif.api.search.SearchRequest;
import org.gbif.api.search.SearchResponse;
import org.gbif.api.search.SearchSuggestRequest;
import org.gbif.checklistbank.api.Constants;
import org.gbif.checklistbank.api.model.search.NameUsageFacetParameter;
import org.gbif.checklistbank.api.service.NameUsageSearchService;
import org.gbif.checklistbank.search.model.NameUsageSolrSearchResult;
import org.gbif.common.search.model.Field;
import org.gbif.common.search.model.SuggestService;
import org.gbif.common.search.service.SolrSearchSuggestService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import static org.gbif.common.search.util.SearchConstants.LANG_SEPARATOR;
import static org.gbif.common.search.util.SearchConstants.SOLR_SHARDS_KEY;

/**
 * Checklist Bank Search interface implementation using Solr/lucene
 * This class uses a remote to interact with CLB index.
 * Uses a
 */
@SuggestService(field = "canonical_name", limit = 4, fieldsMapping = { @Field(property = NameUsageSearchServiceImpl.CHECKLIST_KEY_PARAM, field = "checklist_key") })
public class NameUsageSearchServiceImpl extends SolrSearchSuggestService<NameUsageSolrSearchResult, NameUsageFacetParameter, String> implements NameUsageSearchService<NameUsageSolrSearchResult> {

    private static final LinkedHashMap<String, SolrQuery.ORDER> primarySortOrder = new LinkedHashMap<String, SolrQuery.ORDER>();

    protected static final String CHECKLIST_KEY_PARAM = "checklistKey";

    static {
        primarySortOrder.put("score", SolrQuery.ORDER.desc);
        primarySortOrder.put("num_descendants", SolrQuery.ORDER.desc);
        primarySortOrder.put("num_occurrences", SolrQuery.ORDER.desc);
        primarySortOrder.put("scientific_name", SolrQuery.ORDER.asc);
    }

    @Inject
    public NameUsageSearchServiceImpl(@Named(SOLR_SHARDS_KEY) String shards, SolrServer server) {
        super(server, shards, NameUsageSolrSearchResult.class, NameUsageSearchServiceImpl.class, NameUsageFacetParameter.class, primarySortOrder);
    }

    /**
   * Method that removes the vernacular names that don't have the same language as the language parameter.
   * 
   * @param language to filter the results.
   * @param response to be filtered.
   * @return
   */
    private SearchResponse<NameUsageSolrSearchResult> filterResponseByLanguage(String language, SearchResponse<NameUsageSolrSearchResult> response) {
        if (!Strings.isNullOrEmpty(language)) {
            String lowerCaseLanguage = language.toLowerCase();
            for (NameUsageSolrSearchResult nu : response.getResults()) {
                if (nu.getVernacularNamesLanguages() != null) {
                    ArrayList<String> vNamesLanguages = new ArrayList<String>();
                    for (String vname : nu.getVernacularNamesLanguages()) {
                        int sepPos = vname.indexOf(LANG_SEPARATOR);
                        String vnameLang = vname.substring(0, sepPos).trim().toLowerCase();
                        if (!Strings.isNullOrEmpty(vnameLang) && vnameLang.equals(lowerCaseLanguage)) {
                            vNamesLanguages.add(vname.substring(sepPos + 1));
                        }
                    }
                    nu.setVernacularNamesLanguages(vNamesLanguages);
                }
            }
        }
        return response;
    }

    @Override
    public SearchResponse<NameUsageSolrSearchResult> search(SearchRequest searchRequest) {
        SearchResponse<NameUsageSolrSearchResult> response = super.search(searchRequest);
        filterResponseByLanguage(searchRequest.getLanguage(), response);
        return response;
    }

    @Override
    public List<String> suggest(SearchSuggestRequest searchSuggestRequest) {
        if (searchSuggestRequest.getParameters().isEmpty()) {
            searchSuggestRequest.addParameter(CHECKLIST_KEY_PARAM, Constants.NUB_TAXONOMY_KEY.toString());
        }
        return super.suggest(searchSuggestRequest);
    }
}
