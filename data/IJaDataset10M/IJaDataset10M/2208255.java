package com.apelon.apps.dts.treebrowser.search.application.fetchers;

import java.util.Vector;
import com.apelon.dts.client.DTSException;
import com.apelon.dts.client.subset.SubsetQuery;
import com.apelon.dts.client.concept.ConceptAttributeSetDescriptor;
import com.apelon.dts.client.concept.SearchQuery;
import com.apelon.dts.client.concept.OntylogConceptQuery;
import com.apelon.dts.client.concept.NavQuery;
import com.apelon.dts.client.association.AssociationQuery;
import com.apelon.dts.client.match.MatchQuery;
import com.apelon.dts.client.namespace.NamespaceQuery;
import com.apelon.dts.client.namespace.Namespace;
import com.apelon.common.log4j.Categories;
import com.apelon.common.log4j.StackTracePrinter;
import com.apelon.apps.dts.treebrowser.search.beans.SearchBean;
import com.apelon.apps.dts.treebrowser.search.beans.OptionsBean;
import com.apelon.apps.dts.treebrowser.search.application.fetchers.*;

/**
 * Coordinates data retrieval for concept search and available search options.
 * <p>
 *
 * @author    All source code copyright (c) 2003 Apelon, Inc.  All rights reserved.
 *
 * @version   Apelon Search Widget 1.0
 */
public class ResultFetcherImpl implements ResultFetcher {

    SearchQuery searchQuery = null;

    OntylogConceptQuery conceptQuery = null;

    NavQuery navQuery = null;

    AssociationQuery associationQuery = null;

    MatchQuery matchQuery = null;

    SubsetQuery subsetQuery = null;

    NamespaceQuery namespaceQuery = null;

    ConceptAttributeSetDescriptor searchAsd = null;

    SearchBean searchEntity = null;

    Vector resultConcepts = new Vector(0);

    int namespace = -1;

    int subset = -1;

    Namespace namespaceObj = null;

    public ResultFetcherImpl() {
    }

    public ResultFetcherImpl(SearchQuery searchQuery, OntylogConceptQuery conceptQuery, NavQuery navQuery, AssociationQuery associationQuery, MatchQuery matchQuery, NamespaceQuery namespaceQuery, SearchBean searchEntity, SubsetQuery subsetQuery) {
        this.searchQuery = searchQuery;
        this.subsetQuery = subsetQuery;
        this.conceptQuery = conceptQuery;
        this.navQuery = navQuery;
        this.matchQuery = matchQuery;
        this.associationQuery = associationQuery;
        this.namespaceQuery = namespaceQuery;
        this.searchEntity = searchEntity;
        this.searchAsd = new ConceptAttributeSetDescriptor("searchAsd");
        if (!(searchEntity.getNamespace() == null)) {
            if (searchEntity.getNamespace().equals("All Namespaces")) {
                namespace = -1;
            } else {
                try {
                    namespaceObj = namespaceQuery.findNamespaceByName(searchEntity.getNamespace());
                } catch (DTSException e) {
                    Categories.dataServer().error(StackTracePrinter.getStackTrace(e));
                }
                if (!(namespaceObj == null)) {
                    namespace = namespaceObj.getId();
                }
            }
        } else if (!(searchEntity.getSubset().equals("null") || searchEntity.getSubset().equals(""))) {
            namespace = new Integer(searchEntity.getSubset()).intValue();
        }
    }

    public ResultFetcherImpl(SearchQuery searchQuery, OntylogConceptQuery conceptQuery, NavQuery navQuery, AssociationQuery associationQuery, MatchQuery matchQuery, NamespaceQuery namespaceQuery, SubsetQuery subsetQuery) {
        this.searchQuery = searchQuery;
        this.subsetQuery = subsetQuery;
        this.conceptQuery = conceptQuery;
        this.navQuery = navQuery;
        this.matchQuery = matchQuery;
        this.associationQuery = associationQuery;
        this.namespaceQuery = namespaceQuery;
        this.searchEntity = searchEntity;
        this.searchAsd = new ConceptAttributeSetDescriptor("searchAsd");
    }

    /**
   *
   */
    public OptionsBean fetchQuickOptions() {
        OptionsBean optionsEntity = new OptionsBean();
        QuickOptionsFetcher optionsFetcher = new QuickOptionsFetcher(this);
        optionsEntity.setNamespaces(optionsFetcher.fetchOptionsByNamespace());
        optionsEntity.setExactMatch("true");
        optionsEntity.setName("true");
        optionsEntity.setPropertiesWordMatch("true");
        optionsEntity.setSynonyms("true");
        optionsEntity.setProperties(optionsFetcher.fetchOptionsByProperty());
        optionsEntity.setRoles(optionsFetcher.fetchOptionsByRole());
        optionsEntity.setInverseRoles(optionsFetcher.fetchOptionsByInverseRole());
        optionsEntity.setAssociations(optionsFetcher.fetchOptionsByAssociation());
        optionsEntity.setInverseAssociations(optionsFetcher.fetchOptionsByInverseAssociation());
        optionsEntity.setMaxResult("50");
        optionsEntity.setSilos(optionsFetcher.fetchOptionsBySilo());
        optionsEntity.setSubsets(optionsFetcher.fetchOptionsBySubset());
        optionsEntity.setSiloMatchType("complete_match");
        optionsEntity.setSiloBestMatch("true");
        optionsEntity.setSiloSpellChecking("true");
        return optionsEntity;
    }

    /**
   *
   */
    public OptionsBean fetchAdvancedOptions(SearchBean searchEntity) {
        if (!(searchEntity.getNamespace() == null) && searchEntity.getSearchInRadio().equals("namespace")) {
            if (searchEntity.getNamespace().equals("All Namespaces")) {
                namespace = -1;
            } else {
                try {
                    namespaceObj = namespaceQuery.findNamespaceByName(searchEntity.getNamespace());
                } catch (DTSException e) {
                    Categories.dataServer().error(StackTracePrinter.getStackTrace(e));
                }
                if (!(namespaceObj == null)) {
                    namespace = namespaceObj.getId();
                }
            }
        }
        OptionsBean optionsEntity = new OptionsBean();
        AdvancedOptionsFetcher optionsFetcher = new AdvancedOptionsFetcher(this);
        optionsEntity.setNamespaces(optionsFetcher.fetchOptionsByNamespace());
        if (searchEntity.getSearchInRadio().equals("subset")) {
            optionsEntity.setSubsets(optionsFetcher.fetchAdvancedOptions(searchEntity));
        } else if (searchEntity.getSearchInRadio().equals("silo")) {
            optionsEntity.setSilos(optionsFetcher.fetchAdvancedOptions(searchEntity));
        } else if (searchEntity.getSelectType().equals("default")) {
            optionsEntity.setRoles(optionsFetcher.fetchAdvancedOptions(searchEntity));
        } else if (searchEntity.getSelectType().equals("roles")) {
            optionsEntity.setRoles(optionsFetcher.fetchAdvancedOptions(searchEntity));
        } else if (searchEntity.getSelectType().equals("inverse_roles")) {
            optionsEntity.setInverseRoles(optionsFetcher.fetchAdvancedOptions(searchEntity));
        } else if (searchEntity.getSelectType().equals("properties")) {
            optionsEntity.setProperties(optionsFetcher.fetchAdvancedOptions(searchEntity));
        } else if (searchEntity.getSelectType().equals("associations")) {
            optionsEntity.setAssociations(optionsFetcher.fetchAdvancedOptions(searchEntity));
        } else if (searchEntity.getSelectType().equals("inverse_associations")) {
            optionsEntity.setInverseAssociations(optionsFetcher.fetchAdvancedOptions(searchEntity));
        }
        return optionsEntity;
    }

    /**
   *
   */
    public SearchBean fetchResults(SearchBean searchEntity) {
        this.searchEntity = searchEntity;
        ConceptResultFetcher resultsFetcher = new ConceptResultFetcher(this);
        resultsFetcher.fetchAllResults();
        searchEntity.setResults(resultConcepts);
        return searchEntity;
    }
}
