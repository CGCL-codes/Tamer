package org.amlfilter.batch;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.amlfilter.model.SearchRecord;
import org.amlfilter.model.SearchRequest;
import org.amlfilter.service.SearchRequestXMLGenerator;
import org.amlfilter.service.XMLService;
import org.amlfilter.util.GeneralConstants;
import org.amlfilter.util.GeneralUtils;
import org.amlfilter.util.URLUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.SSLProtocolSocketFactory;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class SearchRequestXMLProcessor implements Runnable {

    private static int MAX_HTTP_CONNECTIONS = 1;

    private static int RETRY_MAX_COUNT = 2;

    private AtomicBoolean mStopProcessing = new AtomicBoolean(false);

    private static AtomicInteger mTotalProcessedCount = new AtomicInteger(0);

    private static AtomicInteger mTotalTimeForProcessing = new AtomicInteger(0);

    private static AtomicInteger mTotalPersistedCount = new AtomicInteger(0);

    private static AtomicInteger mSearchResultCount = new AtomicInteger(0);

    private static int mBatchSize;

    private String mTaskId = null;

    private boolean mDoneProcessing = false;

    private String mURL = null;

    private HttpConnectionManager mConnectionManager = null;

    private HttpClient mHttpClient = null;

    private Map<String, SearchRecord> mClientIdSearchRecordMap = new HashMap<String, SearchRecord>();

    private Map<String, List<SearchResult>> mClientIdSearchResultMap = new HashMap<String, List<SearchResult>>();

    private int mRetryCount = 0;

    private SearchRequestXMLGenerator mSearchRequestXMLGenerator;

    private AMLFBatch mAMLFBatch;

    private boolean mIdle = false;

    private boolean mDead = false;

    /**
	 * Get the retry count
	 * @return The retry count
	 */
    public int getRetryCount() {
        return mRetryCount;
    }

    /**
	 * Set the retry count
	 * @param pRetryCount The retry count
	 */
    public void setRetryCount(int pRetryCount) {
        mRetryCount = pRetryCount;
    }

    /**
	 * Get the idle state
	 * @return The idle state
	 */
    public boolean isIdle() {
        return mIdle;
    }

    /**
	 * Set the idle state
	 * @param pIdle The idle state
	 */
    public void setIdle(boolean pIdle) {
        mIdle = pIdle;
    }

    /**
	 * Get the dead state
	 * @return The dead state
	 */
    public boolean isDead() {
        return mDead;
    }

    /**
	 * Set the dead state
	 * @param pDead The dead state
	 */
    public void setDead(boolean pDead) {
        mDead = pDead;
    }

    public static int getBatchSize() {
        return mBatchSize;
    }

    public static void setBatchSize(int pBatchSize) {
        mBatchSize = pBatchSize;
    }

    public AMLFBatch getAMLFBatch() {
        return mAMLFBatch;
    }

    public void setAMLFBatch(AMLFBatch pAMLFBatch) {
        mAMLFBatch = pAMLFBatch;
    }

    /**
	 * Get the search request XML generator
	 * @return The search request XML generator
	 */
    public SearchRequestXMLGenerator getSearchRequestXMLGenerator() {
        return mSearchRequestXMLGenerator;
    }

    /**
	 * Set the search request XML generator
	 * @param pSearchRequestXMLGenerator The search request XML generator
	 */
    public void setSearchRequestXMLGenerator(SearchRequestXMLGenerator pSearchRequestXMLGenerator) {
        mSearchRequestXMLGenerator = pSearchRequestXMLGenerator;
    }

    /**
	 * Get the total persisted count
	 * @return The total persisted count
	 */
    public static AtomicInteger getTotalPersistedCount() {
        return mTotalPersistedCount;
    }

    /**
	 * Set the total persisted count
	 * @param pTotalPersistedCount The total persisted count
	 */
    public static void setTotalPersistedCount(AtomicInteger pTotalPersistedCount) {
        mTotalPersistedCount = pTotalPersistedCount;
    }

    /**
	 * Get the task id
	 * @return The task id
	 */
    public String getTaskId() {
        return mTaskId;
    }

    /**
	 * Set the task id
	 * @param pTaskId The task id
	 */
    public void setTaskId(String pTaskId) {
        mTaskId = pTaskId;
    }

    /**
     * Get the URL
     * @return The URL
     */
    public String getURL() {
        return mURL;
    }

    /**
     * Set the URL
     * @param pURL The URL
     */
    public void setURL(String pURL) {
        mURL = pURL;
    }

    /**
     * Get the http client
     * @return The http client
     */
    protected HttpClient getHttpClient() {
        return mHttpClient;
    }

    /**
     * Set the http client
     * @param pHttpClient The http client
     */
    protected void setHttpClient(HttpClient pHttpClient) {
        mHttpClient = pHttpClient;
    }

    /**
     * Get the connection manager
     * @return The connection manager
     */
    protected HttpConnectionManager getConnectionManager() {
        return mConnectionManager;
    }

    /**
     * Get the connection manager
     * @return The connection manager
     */
    protected void setConnectionManager(HttpConnectionManager pConnectionManager) {
        mConnectionManager = pConnectionManager;
    }

    /**
	 * Get the stop processing flag
	 * @return The stop processing flag
	 */
    public AtomicBoolean getStopProcessing() {
        return mStopProcessing;
    }

    /**
	 * Get the stop processing flag
	 * @return The stop processing flag
	 */
    public static AtomicInteger getTotalProcessedCount() {
        return mTotalProcessedCount;
    }

    /**
	 * Get the total time
	 * @return The total time
	 */
    public static AtomicInteger getTotalTimeForProcessing() {
        return mTotalTimeForProcessing;
    }

    /**
	 * Get the done processing flag
	 * @return The done processing flag
	 */
    public boolean getDoneProcessing() {
        return mDoneProcessing;
    }

    /**
	 * Set the done processing flag
	 * @param pDoneProcessing The done processing flag
	 */
    public void setDoneProcessing(boolean pDoneProcessing) {
        mDoneProcessing = pDoneProcessing;
    }

    /**
	 * Get the client id to search record map
	 * @return The client id to search record map
	 */
    protected Map<String, SearchRecord> getClientIdSearchRecordMap() {
        return mClientIdSearchRecordMap;
    }

    /**
	 * Set the client id to search record map
	 * @param pClientIdSearchRecordMap The client id to search record map
	 */
    protected void setClientIdSearchRecordMap(Map<String, SearchRecord> pClientIdSearchRecordMap) {
        mClientIdSearchRecordMap = pClientIdSearchRecordMap;
    }

    /**
	 * Get the client id to persistent result map
	 * @return The client id to persistent result map
	 */
    protected Map<String, List<SearchResult>> getClientIdSearchResultMap() {
        return mClientIdSearchResultMap;
    }

    /**
	 * Set the client id to persistent result map
	 * @param pClientIdSearchResultMap The client id to persistent result map
	 */
    protected void setClientIdSearchResultMap(Map<String, List<SearchResult>> pClientIdSearchResultMap) {
        mClientIdSearchResultMap = pClientIdSearchResultMap;
    }

    /**
	 * Get the persistent result count
	 * @return The persistent result count
	 */
    public static AtomicInteger getSearchResultCount() {
        return mSearchResultCount;
    }

    /**
	 * Register the URL protocol
	 * @param
	 * @throws MalformedURLException
	 */
    protected void registerProtocol(String pURL) throws MalformedURLException {
        URL url = new URL(pURL);
        int port = url.getPort();
        if (-1 == port) {
            port = url.getDefaultPort();
        }
        if (url.getProtocol().equals(URLUtils.HTTPS_PROTOCOL)) {
            Protocol httpsProtocol = new Protocol(URLUtils.HTTPS_PROTOCOL, (ProtocolSocketFactory) new SSLProtocolSocketFactory(), port);
            Protocol.registerProtocol(URLUtils.HTTPS_PROTOCOL, httpsProtocol);
        }
    }

    /**
	 * Create the connection pool
	 */
    protected void createConnectionPool() {
        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams htcmp = new HttpConnectionManagerParams();
        htcmp.setMaxTotalConnections(MAX_HTTP_CONNECTIONS);
        connectionManager.setParams(htcmp);
        mConnectionManager = connectionManager;
    }

    /**
	 * Create the http client
	 */
    protected void createHttpClient() {
        mHttpClient = new HttpClient(mConnectionManager);
    }

    /**
	 * Default constructor
	 * @param pTaskId The task id
	 * @param pSearchXMLQueue The search XML queue to act on
	 * @param pURLs The URLs to connect to
	 */
    public SearchRequestXMLProcessor(String pTaskId, String pURL, int pBatchSize, AMLFBatch pAMLFBatch) throws Exception {
        mTaskId = pTaskId;
        mURL = pURL;
        mBatchSize = pBatchSize;
        mAMLFBatch = pAMLFBatch;
        registerProtocol(pURL);
        createConnectionPool();
        createHttpClient();
    }

    /**
	 * @throws IOException
	 * @throws HttpException
	 *
	 */
    protected String search(String pSearchRequestXML) throws Exception {
        PostMethod post = new PostMethod(getURL());
        if (getAMLFBatch().isLoggingDebug()) {
            getAMLFBatch().logDebug("pSearchRequestXML: " + pSearchRequestXML);
        }
        post.addParameter("searchXML", pSearchRequestXML);
        String charSet = getAMLFBatch().getCharSet();
        post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + charSet);
        getHttpClient().executeMethod(post);
        InputStream responseInputStream = post.getResponseBodyAsStream();
        String response = IOUtils.toString(responseInputStream, "UTF-8");
        if (getAMLFBatch().isLoggingDebug()) {
            getAMLFBatch().logDebug("pSearchResponseXML: " + response);
        }
        return response;
    }

    /**
	 * Parse the response
	 * @throws DocumentException
	 */
    protected boolean parseResponse(String pRequestXML, String pResponseXML) throws Exception {
        try {
            pResponseXML = pResponseXML.trim();
            if (null == pResponseXML || pResponseXML.equals("")) {
                throw new IllegalArgumentException("responseXML is empty");
            }
            if (-1 != pResponseXML.indexOf("<error>")) {
                getAMLFBatch().logInfo("Task (" + getTaskId() + "): Encountered error");
                throw new IllegalStateException("Task (" + getTaskId() + "): Encountered error");
            }
            Document xmlDocument = new SAXReader().read(new StringReader(pResponseXML));
            List searchNames = xmlDocument.selectNodes("/search-response/search-results/search-name");
            for (int i = 0; i < searchNames.size(); i++) {
                Node searchNameNode = (Node) searchNames.get(i);
                String uniqueId = searchNameNode.valueOf("@uniqueId");
                List results = searchNameNode.selectNodes("results/result", "similarity");
                List<SearchResult> persistentResults = getClientIdSearchResultMap().get(uniqueId);
                if (null != persistentResults) {
                    String errorMessage = "Client ID(" + uniqueId + ") already exists; they must be unique!";
                    if (getAMLFBatch().isLoggingError()) {
                        if (getAMLFBatch().isLoggingError()) {
                            getAMLFBatch().logError(errorMessage);
                        }
                    }
                    throw new IllegalStateException(errorMessage);
                } else {
                    persistentResults = new ArrayList<SearchResult>();
                    getClientIdSearchResultMap().put(uniqueId, persistentResults);
                }
                for (int j = results.size() - 1; j >= 0; j--) {
                    SearchResult persistentResult = new SearchResult();
                    persistentResult.setClientUniqueId(uniqueId);
                    Node resultNode = (Node) results.get(j);
                    String resultName = resultNode.selectSingleNode("name").getText();
                    String resultNameInformationLevelStr = resultNode.selectSingleNode("information-level").getText();
                    float resultNameInformationLevel = Float.parseFloat(resultNameInformationLevelStr);
                    persistentResult.setResultNameInformationLevel(resultNameInformationLevel);
                    persistentResult.setResultEntityName(resultName);
                    String designation = resultNode.selectSingleNode("designation").getText();
                    persistentResult.setDesignation(designation);
                    String entityCodeInSource = resultNode.selectSingleNode("code-in-source").getText();
                    persistentResult.setResultEntityCodeInSource(entityCodeInSource);
                    String similarityStr = resultNode.selectSingleNode("similarity").getText();
                    float similarity = Float.parseFloat(similarityStr);
                    persistentResult.setSimilarity(similarity);
                    persistentResults.add(persistentResult);
                }
            }
            return true;
        } catch (Exception e) {
            getClientIdSearchResultMap().clear();
            getClientIdSearchRecordMap().clear();
            logBadRequestResponse(pRequestXML, pResponseXML, e);
        }
        return false;
    }

    protected void logBadRequestResponse(String pRequestXML, String pResponseXML, Exception exc) throws IOException {
        String errorMessage = GeneralUtils.getStackTraceAsString(exc, 20);
        StringBuilder sb = new StringBuilder();
        sb.append("Search Request: ");
        sb.append("\n\n");
        sb.append(pRequestXML);
        sb.append("\n\n");
        sb.append("Search Response: ");
        sb.append("\n\n");
        sb.append(pResponseXML);
        sb.append("\n\n");
        sb.append("AMLFBatch Exception: ");
        sb.append("\n\n");
        sb.append(errorMessage);
        FileOutputStream fos1 = null;
        BufferedWriter bw = null;
        try {
            String badResponseFileName = GeneralUtils.generateAbsolutePath(getAMLFBatch().getTransactionDirectoryPath(), getAMLFBatch().getBadResponseFileName(), "/");
            badResponseFileName = badResponseFileName + "_" + System.nanoTime() + ".log";
            fos1 = new FileOutputStream(badResponseFileName);
            bw = new BufferedWriter(new OutputStreamWriter(fos1, "UTF-8"));
            bw.write(sb.toString());
            bw.flush();
            if (getAMLFBatch().isLoggingError()) {
                getAMLFBatch().logError("***** Invalid response XML: " + pResponseXML + "; reason: " + errorMessage);
            }
            System.out.println("***** Invalid response XML; please check logs");
        } catch (Exception e) {
            errorMessage = GeneralUtils.getStackTraceAsString(e, 50);
            if (getAMLFBatch().isLoggingError()) {
                getAMLFBatch().logError(errorMessage);
            }
            System.out.println("***** An error happened while processing a search request; please check logs");
        } finally {
            if (null != bw) {
                bw.close();
            }
        }
    }

    /**
	 * Serialize the list field
	 * @param pListFieldToSerialize The list field to serialize
	 * @return The serialized list field
	 */
    public String serializeListField(List<String> pListFieldToSerialize, String pDelimiter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pListFieldToSerialize.size(); i++) {
            String field = pListFieldToSerialize.get(i);
            sb.append(field);
            sb.append(pDelimiter);
        }
        return sb.toString();
    }

    /**
	 * Serialize the search result
	 * @param pSearchResult The search result
	 */
    public String serializeSearchResult(SearchResult pSearchResult) {
        StringBuilder sb = new StringBuilder();
        sb.append(pSearchResult.getClientUniqueId());
        sb.append(getAMLFBatch().getFieldDelimiter());
        sb.append(XMLService.reverseXmlEscape(pSearchResult.getSearchRecord().getFullName()));
        sb.append(getAMLFBatch().getFieldDelimiter());
        sb.append(pSearchResult.getSearchRecord().getEntityType());
        sb.append(getAMLFBatch().getFieldDelimiter());
        sb.append(pSearchResult.getSearchRecord().getGender());
        sb.append(getAMLFBatch().getFieldDelimiter());
        sb.append(XMLService.reverseXmlEscape(pSearchResult.getSearchRecord().getPlacesOfInception()));
        sb.append(getAMLFBatch().getFieldDelimiter());
        sb.append(XMLService.reverseXmlEscape(pSearchResult.getSearchRecord().getDatesOfInception()));
        sb.append(getAMLFBatch().getFieldDelimiter());
        sb.append(XMLService.reverseXmlEscape(pSearchResult.getSearchRecord().getAddresses()));
        sb.append(getAMLFBatch().getFieldDelimiter());
        sb.append(XMLService.reverseXmlEscape(pSearchResult.getSearchRecord().getCitizenships()));
        sb.append(getAMLFBatch().getFieldDelimiter());
        sb.append(XMLService.reverseXmlEscape(pSearchResult.getSearchRecord().getIdentificationDocuments()));
        sb.append(getAMLFBatch().getFieldDelimiter());
        sb.append(pSearchResult.getResultEntityCodeInSource());
        sb.append(getAMLFBatch().getFieldDelimiter());
        sb.append(pSearchResult.getResultEntityName());
        sb.append(getAMLFBatch().getFieldDelimiter());
        sb.append(pSearchResult.getResultNameInformationLevel());
        sb.append(getAMLFBatch().getFieldDelimiter());
        sb.append(pSearchResult.getSimilarity());
        sb.append(getAMLFBatch().getFieldDelimiter());
        sb.append(pSearchResult.getDesignation());
        return sb.toString();
    }

    /**
	 * Persist result by first getting it and then
	 * serializing it and then persisting it to the file system
	 */
    protected void persistResult() throws IOException {
        try {
            Iterator<Map.Entry<String, List<SearchResult>>> clientIdSearchResultMapEntryIterator = getClientIdSearchResultMap().entrySet().iterator();
            int persistentResultCount = 0;
            while (clientIdSearchResultMapEntryIterator.hasNext()) {
                Map.Entry<String, List<SearchResult>> entry = clientIdSearchResultMapEntryIterator.next();
                String clientUniqueId = (String) entry.getKey();
                List<SearchResult> persistentResults = (List<SearchResult>) entry.getValue();
                for (int i = 0; i < persistentResults.size(); i++) {
                    SearchResult persistentResult = persistentResults.get(i);
                    SearchRecord searchRecord = (SearchRecord) getClientIdSearchRecordMap().get(clientUniqueId);
                    persistentResult.setSearchRecord(searchRecord);
                    persistentResultCount = getTotalPersistedCount().incrementAndGet();
                    String persistentResultStr = serializeSearchResult(persistentResult);
                    getAMLFBatch().getOutputRecordProcessor().writeRecord(persistentResultStr);
                }
            }
        } finally {
            getClientIdSearchRecordMap().clear();
            getClientIdSearchResultMap().clear();
        }
    }

    /**
	 * Create the client id search record map
	 */
    protected void createClientIdSearchRecordMap(SearchRequest pSearchRequest) {
        List<SearchRecord> searchRecordList = pSearchRequest.getSearchRecords().getSearchRecordList();
        for (int i = 0; i < searchRecordList.size(); i++) {
            SearchRecord searchRecord = searchRecordList.get(i);
            String clientUniqueId = searchRecord.getUniqueId();
            getClientIdSearchRecordMap().put(clientUniqueId, searchRecord);
        }
    }

    public void exec() {
        SearchRequest searchRequest = null;
        String searchRequestXML = null;
        String searchResponseXML = null;
        while (getAMLFBatch().getSearchXMLQueue().size() > 0) {
            setIdle(false);
            try {
                searchRequest = getAMLFBatch().getSearchXMLQueue().poll();
                if (null != searchRequest) {
                    searchRequestXML = searchRequest.getSearchXML();
                    if (null == searchRequestXML || searchRequestXML.trim().isEmpty()) {
                        throw new IllegalStateException("The search request XML cannot be empty!");
                    }
                    if (getAMLFBatch().isLoggingDebug()) {
                        getAMLFBatch().logDebug("searchRequestXML: " + searchRequestXML);
                    }
                    createClientIdSearchRecordMap(searchRequest);
                    searchResponseXML = search(searchRequestXML);
                    if (parseResponse(searchRequestXML, searchResponseXML)) {
                        persistResult();
                    } else {
                        getAMLFBatch().logInfo("BAD RESPONSE!");
                    }
                    getClientIdSearchRecordMap().clear();
                    getClientIdSearchResultMap().clear();
                    int totalProcessedCount = getTotalProcessedCount().incrementAndGet();
                    if (0 == totalProcessedCount % 10 && 0 != totalProcessedCount) {
                        showStatistics(getAMLFBatch(), getClass(), getTaskId());
                    }
                    mRetryCount = 0;
                }
            } catch (Exception e) {
                String errorMessage = GeneralUtils.getStackTraceAsString(e, 50);
                getClientIdSearchRecordMap().clear();
                getClientIdSearchResultMap().clear();
                System.out.println(getTaskId() + ": Error processing request; please check logs");
                getAMLFBatch().logError(getTaskId() + ": Error processing request: " + errorMessage);
                getAMLFBatch().logError(getTaskId() + ": URL for the request: " + getURL());
                if (mRetryCount < RETRY_MAX_COUNT) {
                    mRetryCount++;
                    getAMLFBatch().logInfo(getTaskId() + ": Retrying request by adding it back to the pool: " + errorMessage);
                    getAMLFBatch().getSearchXMLQueue().add(searchRequest);
                } else {
                    getAMLFBatch().logInfo(getTaskId() + ": Retried all!");
                    setDoneProcessing(true);
                    setIdle(true);
                    setDead(true);
                    throw new IllegalStateException("Dead state for task id: " + getTaskId());
                }
            }
        }
    }

    /**
	 * Run the task
	 * @see java.lang.Runnable#run()
	 */
    public void run() {
        while (false == getStopProcessing().get()) {
            exec();
            setIdle(true);
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
            }
        }
        getAMLFBatch().logInfo("Done Processing Task with id: " + getTaskId());
        getAMLFBatch().logInfo("The task (" + getTaskId() + ") finished with " + mRetryCount + " errors");
        setDoneProcessing(true);
    }

    /**
	 * Show the statistics
	 * @param pAMLFBatch The AMLF batch
	 * @param pClass The class using the statistics
	 * @param pContext The context of the logging
	 */
    public static void showStatistics(AMLFBatch pAMLFBatch, Class pClass, String pContext) {
        int totalProcessedCount = getTotalProcessedCount().get();
        totalProcessedCount *= getBatchSize();
        int numberRemaining = (pAMLFBatch.getNumberToProcess() - totalProcessedCount);
        if (numberRemaining < 0) {
            totalProcessedCount += numberRemaining;
            numberRemaining = 0;
        }
        long totalTimeForProcessing = System.currentTimeMillis() - pAMLFBatch.getStartTime();
        float transactionsPerSec = (float) ((float) totalProcessedCount / (float) totalTimeForProcessing) * 1000f;
        float percentProcessed = (float) ((float) totalProcessedCount / (float) pAMLFBatch.getNumberToProcess());
        percentProcessed *= 100;
        int totalPersistedCount = getTotalPersistedCount().get();
        StringBuilder sb = new StringBuilder();
        sb.append(GeneralConstants.NEW_LINE_TOKEN);
        sb.append("*** % PROCESSED = " + percentProcessed);
        sb.append(GeneralConstants.NEW_LINE_TOKEN);
        sb.append("*** RECORDS/SEC = " + (int) transactionsPerSec);
        sb.append(GeneralConstants.NEW_LINE_TOKEN);
        sb.append("*** NUMBER TO PROCESS = " + pAMLFBatch.getNumberToProcess());
        sb.append(GeneralConstants.NEW_LINE_TOKEN);
        sb.append("*** TOTAL PROCESSED COUNT = " + totalProcessedCount);
        sb.append(GeneralConstants.NEW_LINE_TOKEN);
        sb.append("*** NUMBER REMAINING = " + numberRemaining);
        sb.append(GeneralConstants.NEW_LINE_TOKEN);
        sb.append("*** TOTAL PERSISTED = " + totalPersistedCount);
        sb.append(GeneralConstants.NEW_LINE_TOKEN);
        sb.append("*** STATS GENERATED FROM " + pContext);
        sb.append(GeneralConstants.NEW_LINE_TOKEN);
        sb.append(GeneralConstants.NEW_LINE_TOKEN);
        if (pAMLFBatch.isLoggingInfo()) {
            pAMLFBatch.logInfo(sb.toString());
        }
    }

    /**
	 * Cleanup resources
	 */
    public void cleanResources() {
        if (null != getConnectionManager()) {
            ((MultiThreadedHttpConnectionManager) getConnectionManager()).shutdown();
        }
        setURL(null);
        setConnectionManager(null);
        setHttpClient(null);
        setClientIdSearchRecordMap(null);
        setClientIdSearchResultMap(null);
        setRetryCount(0);
        setSearchRequestXMLGenerator(null);
        setAMLFBatch(null);
        setIdle(true);
    }
}
