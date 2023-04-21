package com.google.code.solrdimension.core;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.google.code.solrdimension.config.XmlConfigurationService;
import com.google.code.solrdimension.indexmodifiers.IndexModService;
import com.google.code.solrdimension.parsers.exception.SolrDimensionException;
import com.google.code.solrdimension.querymodifiers.QueryModifier;
import com.google.code.solrdimension.queryresponse.QueryResponseInterpreter;
import com.google.code.solrdimension.spring.ApplicationConfig;
import com.google.code.solrdimension.spring.ConfKeyConstants;

public class DimensionFactory {

    private static final String BEAN_ID_INDEX_MOD_SERVICE = "indexModService";

    private static final String BEAN_ID_QUERY_MODIFIER = "queryModifier";

    private static final String BEAN_ID_RESPONSEINTERPRETERE = "responseInterpreter";

    private static final String BEAN_ID_XML_CONF_SERVICE = "solrDimensionXmlConfigurationService";

    private static final String BEAN_ID_APPLICATION_CONFIG = "applicationConfig";

    private static ApplicationContext appContext;

    private static DimensionFactory _INSTANCE = null;

    private static XmlConfigurationService solrDimensionXmlservice;

    private static IndexModService indexModService;

    private static QueryModifier queryModifier;

    private static QueryResponseInterpreter responseInterpreter;

    private static ApplicationConfig configurationService;

    private static Logger logger = LoggerFactory.getLogger(DimensionFactory.class);

    private static boolean isAppEnabled = false;

    private DimensionFactory() {
        appContext = new ClassPathXmlApplicationContext("classpath:/com/google/code/solrdimension/spring/application-context.xml");
        configurationService = (ApplicationConfig) appContext.getBean(BEAN_ID_APPLICATION_CONFIG);
        if (configurationService.isApplicationEnabled()) {
            isAppEnabled = true;
            solrDimensionXmlservice = (XmlConfigurationService) appContext.getBean(BEAN_ID_XML_CONF_SERVICE);
            indexModService = (IndexModService) appContext.getBean(BEAN_ID_INDEX_MOD_SERVICE);
            queryModifier = (QueryModifier) appContext.getBean(BEAN_ID_QUERY_MODIFIER);
            responseInterpreter = (QueryResponseInterpreter) appContext.getBean(BEAN_ID_RESPONSEINTERPRETERE);
        } else {
            logger.info("SolrDimension has been disabled, please check the property in the property file: " + ConfKeyConstants.APPLICATION_ENABLED);
        }
    }

    public static DimensionFactory getInstance() {
        if (_INSTANCE == null) {
            _INSTANCE = new DimensionFactory();
        }
        return _INSTANCE;
    }

    public DimensionInputDocument createSolrDimensionInputDocument(SolrInputDocument inputdoc) throws SolrDimensionException {
        if (!isAppEnabled) {
            return null;
        }
        return new DimensionInputDocument(inputdoc, solrDimensionXmlservice.getSolrDimensionConfig(), indexModService);
    }

    public SolrQuery processSolrQuery(SolrQuery solrQuery) throws SolrDimensionException {
        if (!isAppEnabled) {
            return null;
        }
        return queryModifier.prepareQuery(solrQuery, solrDimensionXmlservice.getSolrDimensionConfig());
    }

    public DimensionResponse createSolrDimensionResponse(QueryResponse queryResponse) throws SolrDimensionException {
        if (!isAppEnabled) {
            return null;
        }
        return responseInterpreter.interpretQueryResponse(queryResponse, solrDimensionXmlservice.getSolrDimensionConfig());
    }
}
