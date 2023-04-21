package org.azrul.mewit.client;

import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.azrul.epice.dao.query.SearchItemsQuery;
import org.azrul.epice.domain.Item;
import org.azrul.epice.domain.ChildItem;
import org.azrul.epice.rest.dto.FindItemByIdRequest;
import org.azrul.epice.rest.dto.FindItemByIdResponse;
import org.azrul.epice.rest.dto.SearchRequest;
import org.azrul.epice.rest.dto.SearchResponse;
import org.azrul.epice.dao.query.QueryResult;
import org.azrul.epice.dao.query.SearchPage;
import com.wavemaker.runtime.RuntimeAccess;

/**
 * This is a client-facing service class.  All
 * public methods will be exposed to the client.  Their return
 * values and parameters will be passed to the client or taken
 * from the client, respectively.  This will be a singleton
 * instance, shared between all requests. 
 * 
 * To log, call the superclass method log(LOG_LEVEL, String) or log(LOG_LEVEL, String, Exception).
 * LOG_LEVEL is one of FATAL, ERROR, WARN, INFO and DEBUG to modify your log level.
 * For info on these levels, look for tomcat/log4j documentation
 */
public class SearchItemsService extends com.wavemaker.runtime.javaservice.JavaServiceSuperClass {

    public SearchItemsService() {
        super(INFO);
    }

    public Item getItem() {
        return new Item();
    }

    public SearchPage getSearchPage() {
        return new SearchPage();
    }

    public ChildItem getChildItem() {
        return new ChildItem();
    }

    public Item findById(String itemId) throws UnsupportedEncodingException, IOException {
        String sessionId = (String) RuntimeAccess.getInstance().getSession().getAttribute("SESSION_ID");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        FindItemByIdRequest request = new FindItemByIdRequest();
        request.setItemID(itemId);
        request.setSessionId(sessionId);
        XStream writer = new XStream();
        writer.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        writer.alias("FindItemByIdRequest", FindItemByIdRequest.class);
        XStream reader = new XStream();
        reader.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        reader.alias("FindItemByIdResponse", FindItemByIdResponse.class);
        String strRequest = URLEncoder.encode(reader.toXML(request), "UTF-8");
        HttpGet httpget = new HttpGet(MewitProperties.getMewitUrl() + "/resources/findItemById?REQUEST=" + strRequest);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = URLDecoder.decode(EntityUtils.toString(entity), "UTF-8");
            FindItemByIdResponse fibiResponse = (FindItemByIdResponse) reader.fromXML(result);
            return fibiResponse.getItem();
        }
        return null;
    }

    public QueryResult doSearch(String searchTerm, Integer searchInReceivedItems, Integer searchInSentItems, Integer searchInSupervisedItems, Integer startRow, Integer resultCount, Boolean searchArchived, Boolean searchInItemsNeededAttentionOnly) throws UnsupportedEncodingException, IOException {
        String sessionId = (String) RuntimeAccess.getInstance().getSession().getAttribute("SESSION_ID");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        QueryResult queryResult = new QueryResult();
        SearchRequest request = new SearchRequest();
        SearchItemsQuery query = new SearchItemsQuery();
        query.setArchiveIncluded(searchArchived);
        log(INFO, "searchTerm=" + searchTerm);
        log(INFO, "search in received=" + searchInReceivedItems);
        log(INFO, "search in sent=" + searchInSentItems);
        log(INFO, "search in supervised=" + searchInSupervisedItems);
        List<String> filters = new ArrayList<String>();
        if (searchInItemsNeededAttentionOnly == false) {
            if (searchInReceivedItems != null) {
                filters.add("ALL_RECEIVED_ITEMS");
            }
            if (searchInSentItems != null) {
                filters.add("ALL_SENT_ITEMS");
            }
            if (searchInSupervisedItems != null) {
                filters.add("ALL_SUPERVISED_ITEMS");
            }
        } else {
            if (searchInReceivedItems != null) {
                filters.add("RECEIVED_ITEMS_NEEDED_ATTENTION");
            }
            if (searchInSentItems != null) {
                filters.add("SENT_ITEMS_NEEDED_ATTENTION");
            }
        }
        query.setFilters(filters);
        query.setId("1234");
        query.setOwner(sessionId);
        query.setReferenceOnly(false);
        query.setSearchTerm(searchTerm);
        query.setUseOR(false);
        request.setStartRow(startRow);
        request.setResultCount(resultCount);
        request.setQuery(query);
        request.setSessionId(sessionId);
        XStream writer = new XStream();
        writer.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        writer.alias("SearchRequest", SearchRequest.class);
        XStream reader = new XStream();
        reader.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        reader.alias("SearchResponse", SearchResponse.class);
        String strRequest = URLEncoder.encode(reader.toXML(request), "UTF-8");
        HttpGet httpget = new HttpGet(MewitProperties.getMewitUrl() + "/resources/search?REQUEST=" + strRequest);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = URLDecoder.decode(EntityUtils.toString(entity), "UTF-8");
            SearchResponse searchResponse = (SearchResponse) reader.fromXML(result);
            List<Item> items = searchResponse.getItems();
            queryResult.setItems(items);
            queryResult.setTotal(searchResponse.getTotalResultCount());
            queryResult.setStartRow(searchResponse.getStartRow());
        }
        return queryResult;
    }
}
