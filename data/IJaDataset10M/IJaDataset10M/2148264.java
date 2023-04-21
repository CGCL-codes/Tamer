package com.sanctuary.tools;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import com.sanctuary.annotations.SolrArray;
import com.sanctuary.annotations.SolrArrayElement;
import com.sanctuary.annotations.SolrField;
import com.sanctuary.interfaces.Model;
import com.sanctuary.interfaces.TemplateEngine;
import com.sanctuary.models.SolrDocument;
import com.sanctuary.models.SolrResponse;
import com.sanctuary.models.SolrResult;
import com.sanctuary.models.Term;

public abstract class SolrTools {

    private static final Log LOG = LogFactory.getLog(SolrTools.class);

    private static final String SOLR_URL = BaseTool.CONFIG_BEAN.getSolrUrl();

    private static CommonsHttpSolrServer server = null;

    static {
        try {
            server = new CommonsHttpSolrServer(SOLR_URL);
            server.setParser(new XMLResponseParser());
        } catch (Exception ex) {
            LOG.fatal("Solr couldn't be instantiated", ex);
        }
    }

    public static void addModel(Model m) throws Exception {
        Field[] fields = m.getClass().getDeclaredFields();
        List<SolrDocument> docs = new ArrayList<SolrDocument>();
        TemplateEngine te = (TemplateEngine) BaseTool.IOC_ENGINE.getBean("templateEngine");
        for (Field f : fields) {
            if (f.isAnnotationPresent(SolrField.class)) {
                String key = f.getAnnotation(SolrField.class).value();
                Object value = PropertyUtils.getProperty(m, f.getName());
                if (value instanceof List && f.isAnnotationPresent(SolrArray.class)) {
                    for (Model im : ((List<Model>) value)) {
                        Field[] _f = im.getClass().getDeclaredFields();
                        for (Field _f0 : _f) {
                            if (_f0.isAnnotationPresent(SolrArrayElement.class)) {
                                SolrDocument sd = new SolrDocument();
                                sd.setName(_f0.getAnnotation(SolrField.class).value());
                                sd.setValue(PropertyUtils.getProperty(im, _f0.getName()).toString());
                                docs.add(sd);
                            }
                        }
                    }
                } else {
                    SolrDocument sd = new SolrDocument();
                    sd.setName(key);
                    sd.setValue(value.toString());
                    docs.add(sd);
                }
            }
        }
        Map<String, Object> content = new HashMap<String, Object>();
        content.put("fields", docs);
        StringWriter solrXml = (StringWriter) te.getContent("system/solr-add.ftl", content);
        String url = BaseTool.CONFIG_BEAN.getSolrUrl() + "/update";
        int code = HttpTools.doPost(url, solrXml.toString(), "text/xml", "utf-8");
        code = HttpTools.doPost(url, "<commit/>", "text/xml", "utf-8");
    }

    public static List<SolrResponse> score(List<String> terms) throws Exception {
        List<SolrResponse> results = new ArrayList<SolrResponse>();
        for (String term : terms) {
            SolrQuery query = new SolrQuery();
            query.setQuery(term);
            try {
                QueryResponse rsp = server.query(query);
                SolrDocumentList docs = rsp.getResults();
                if (rsp == null) LOG.warn("QueryResponse is null!");
                if (docs == null) LOG.warn("SolrDocumentList is null!");
                SolrResponse srsp = new SolrResponse();
                if (docs.getMaxScore() == null) {
                    srsp.setMaxScore(0);
                } else {
                    srsp.setMaxScore(docs.getMaxScore());
                }
                srsp.setNumFound(docs.getNumFound());
                List<SolrResult> _sr = new ArrayList<SolrResult>();
                while (docs.listIterator().hasNext()) {
                }
            } catch (Exception ex) {
                LOG.error(StringTools.getStackTrace(ex));
            }
        }
        return results;
    }
}
