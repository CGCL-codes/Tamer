package org.amlfilter.batch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.amlfilter.model.SearchPreference;
import org.amlfilter.model.SearchPreferences;
import org.amlfilter.model.SearchRecord;
import org.amlfilter.model.SearchRequest;
import org.amlfilter.service.GenericService;
import org.amlfilter.service.SearchRequestXMLGenerator;
import org.amlfilter.service.XMLService;
import org.amlfilter.util.GeneralConstants;
import org.amlfilter.util.GeneralUtils;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Performs the BATCH process.
 * This essentially involves:
 * + Reading the input file name line by line
 * + When the threshold is reached it constructs a search XML and searches the system
 * + The resulting XML is parsed and flattened to a flat file and a SQL file
 * + It then logs the total time taken and other interesting metrics.
 * Note: The formats for the flat file and sql file are externalized velocity
 * templates in order to facilitate the maniputation of the same.
 * @author Harish Seshadri
 * @version $Id$
 */
public class AMLFBatch extends GenericService implements InitializingBean {

    private String mCharSet = GeneralConstants.UTF8;

    private float mSimilarityThreshold = 0.75f;

    private String mSearchURLs;

    private long mProcessId;

    private int mNumberOfRequestThreads = -1;

    private String mFieldDelimiter = GeneralConstants.AMLF_TAB_FORMAT_SEPARATOR;

    private String mInputRecordFileName;

    private String mOutputRecordFileName;

    private String mBadResponseFileName;

    private SearchRequestXMLGenerator mSearchRequestXMLGenerator;

    private BlockingQueue<SearchRequest> mSearchXMLQueue = new LinkedBlockingQueue<SearchRequest>();

    private SearchRequestXMLThreadPoolExecutor mSearchRequestXMLThreadPoolExecutor = null;

    private List<SearchRequestXMLProcessor> mSearchRequestXMLProcessors = new ArrayList<SearchRequestXMLProcessor>();

    private int mNumberToProcess = 0;

    private int mBatchSize;

    private int mSearchRequestQueueUpperBound = 200;

    private int mSearchRequestQueueLowerBound = (int) (mSearchRequestQueueUpperBound * 0.75f);

    private InputRecordProcessor mInputRecordProcessor;

    private OutputRecordProcessor mOutputRecordProcessor;

    private long mStartTime;

    private String mWorkingDirectory;

    private boolean mUseTransaction = false;

    private boolean mUseAnalytics = false;

    private boolean mValidateInputFile = true;

    private String mTransactionId;

    private String mTransactionDescription = "";

    private String mProxyHost;

    private int mProxyPort = -1;

    private String mBadRecordLogFileName;

    /**
	 * Get the bad record log file name
	 * @return The bad record log file name
	 */
    public String getBadRecordLogFileName() {
        return mBadRecordLogFileName;
    }

    /**
	 * Set the bad record log file name
	 * @param pBadRecordLogFileName The bad record log file name
	 */
    public void setBadRecordLogFileName(String pBadRecordLogFileName) {
        mBadRecordLogFileName = pBadRecordLogFileName;
    }

    /**
	 * Get the proxy host
	 * @return The proxy host
	 */
    public String getProxyHost() {
        return mProxyHost;
    }

    /**
	 * Get the proxy host
	 * @return The proxy host
	 */
    public void setProxyHost(String pProxyHost) {
        mProxyHost = pProxyHost;
    }

    /**
	 * Get the proxy port
	 * @return The proxy port
	 */
    public int getProxyPort() {
        return mProxyPort;
    }

    /**
	 * Get the proxy port
	 * @return The proxy port
	 */
    public void setProxyPort(int pProxyPort) {
        mProxyPort = pProxyPort;
    }

    /**
	 * Get the transaction description
	 * @return The transaction description
	 */
    public String getTransactionDescription() {
        return mTransactionDescription;
    }

    /**
	 * Set the transaction description
	 * @param pTransactionDescription The transaction description
	 */
    public void setTransactionDescription(String pTransactionDescription) {
        mTransactionDescription = pTransactionDescription;
    }

    /**
	 * Get the transaction id
	 * @return The transaction id
	 */
    public String getTransactionId() {
        if (null == mTransactionId) {
            mTransactionId = generateTransactionId();
            mTransactionId = mTransactionId + "_" + getTransactionDescription();
        }
        return mTransactionId;
    }

    /**
	 * Get the use analytics flag
	 * @return The use analytics flag
	 */
    public boolean getUseAnalytics() {
        return mUseAnalytics;
    }

    /**
	 * Set the use analytics flag
	 * @param pUseAnalytics The use analytics flag
	 */
    public void setUseAnalytics(boolean pUseAnalytics) {
        mUseAnalytics = pUseAnalytics;
    }

    /**
	 * Get the use transaction flag
	 * @return The use transaction flag
	 */
    public boolean getUseTransaction() {
        return mUseTransaction;
    }

    /**
	 * Set the use transaction flag
	 * @param pUseTransaction The use transaction flag
	 */
    public void setUseTransaction(boolean pUseTransaction) {
        mUseTransaction = pUseTransaction;
    }

    /**
	 * Get the validate input file flag
	 * @return The validate input file flag
	 */
    public boolean getValidateInputFile() {
        return mValidateInputFile;
    }

    /**
	 * Set the validate input file flag
	 * @param pValidateInputFile The validate input file flag
	 */
    public void setValidateInputFile(boolean pValidateInputFile) {
        mValidateInputFile = pValidateInputFile;
    }

    /**
	 * Get the working directory
	 * @return The working directory
	 */
    public String getWorkingDirectory() {
        return mWorkingDirectory;
    }

    /**
	 * Set the working directory
	 * @param pWorkingDirectory The working directory
	 */
    public void setWorkingDirectory(String pWorkingDirectory) {
        mWorkingDirectory = pWorkingDirectory;
    }

    /**
	 * Get the start time
	 */
    public long getStartTime() {
        return mStartTime;
    }

    /**
	 * Set the start time
	 */
    public void setStartTime(long pStartTime) {
        mStartTime = pStartTime;
    }

    /**
	 * Get the bad response file name
	 * @return The bad response file name
	 */
    public String getBadResponseFileName() {
        return mBadResponseFileName;
    }

    /**
	 * Set the bad response file name
	 * @param pBadResponseFileName The bad response file name
	 */
    public void setBadResponseFileName(String pBadResponseFileName) {
        mBadResponseFileName = pBadResponseFileName;
    }

    /**
	 * Get the bad response file path
	 * @return The bad response file path
	 */
    public String getBadResponseFilePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(getWorkingDirectory());
        sb.append("/");
        sb.append(getBadResponseFileName());
        return sb.toString();
    }

    /**
	 * Get the input record processor
	 * @return The input record processor
	 */
    public InputRecordProcessor getInputRecordProcessor() {
        return mInputRecordProcessor;
    }

    /**
	 * Set the input record processor
	 * @param pInputRecordProcessor The input record processor
	 */
    public void setInputRecordProcessor(InputRecordProcessor pInputRecordProcessor) {
        mInputRecordProcessor = pInputRecordProcessor;
    }

    /**
	 * Get the output record processor
	 * @return The output record processor
	 */
    public OutputRecordProcessor getOutputRecordProcessor() {
        return mOutputRecordProcessor;
    }

    /**
	 * Set the output record processor
	 * @param pOutputRecordProcessor The output record processor
	 */
    public void setOutputRecordProcessor(OutputRecordProcessor pOutputRecordProcessor) {
        mOutputRecordProcessor = pOutputRecordProcessor;
    }

    /**
	 * Get the batch size
	 * @return The batch size
	 */
    public int getBatchSize() {
        return mBatchSize;
    }

    /**
	 * Set the batch size
	 * @param pBatchSize The batch size
	 */
    public void setBatchSize(int pBatchSize) {
        mBatchSize = pBatchSize;
    }

    /**
	 * Get the number to process
	 * @return The number to process
	 */
    public int getNumberToProcess() {
        return mNumberToProcess;
    }

    /**
	 * Set the number to process
	 * @param pNumberToProcess The number to process
	 */
    public void setNumberToProcess(int pNumberToProcess) {
        mNumberToProcess = pNumberToProcess;
    }

    /**
	 * Get the charset
	 * @return The charset
	 */
    public String getCharSet() {
        return mCharSet;
    }

    /**
	 * Set the charset
	 * @param pCharSet The charset
	 */
    public void setCharSet(String pCharSet) {
        mCharSet = pCharSet;
    }

    /**
	 * Get the similarity threshold
	 * @return The similarity threshold
	 */
    public float getSimilarityThreshold() {
        return mSimilarityThreshold;
    }

    /**
	 * Set the similarity threshold
	 * @param pSimilarityThreshold The similarity threshold
	 */
    public void setSimilarityThreshold(float pSimilarityThreshold) {
        mSimilarityThreshold = pSimilarityThreshold;
    }

    /**
	 * Get the search URLs
	 * @return The search URLs
	 */
    public String getSearchURLs() {
        return mSearchURLs;
    }

    /**
	 * Set the search names batch size
	 * @param pSearchURLs The search URLs
	 */
    public void setSearchURLs(String pSearchURLs) {
        mSearchURLs = pSearchURLs;
    }

    /**
	 * Get the process id
	 * @return The process id
	 */
    public long getProcessId() {
        return mProcessId;
    }

    /**
	 * Set the process id
	 * @param pProcessId The process id
	 */
    public void setProcessId(long pProcessId) {
        mProcessId = pProcessId;
    }

    /**
	 * Get the number of request threads
	 * @return The number of request threads
	 */
    public int getNumberOfRequestThreads() {
        return mNumberOfRequestThreads;
    }

    /**
	 * Set the number of request threads
	 * @param pNumberOfRequestThreads The number of request threads
	 */
    public void setNumberOfRequestThreads(int pNumberOfRequestThreads) {
        mNumberOfRequestThreads = pNumberOfRequestThreads;
    }

    /**
	 * Get the field delimiter
	 * @return The field delimiter
	 */
    public String getFieldDelimiter() {
        return mFieldDelimiter;
    }

    /**
	 * Set the field delimiter
	 * @param pFieldDelimiter The field delimiter
	 */
    public void setFieldDelimiter(String pFieldDelimiter) {
        mFieldDelimiter = pFieldDelimiter;
    }

    /**
	 * Get the input record file name
	 * @return The input record file name
	 */
    public String getInputRecordFileName() {
        return mInputRecordFileName;
    }

    /**
	 * Set the input record file name
	 * @param The input record file name
	 */
    public void setInputRecordFileName(String pInputRecordFileName) {
        mInputRecordFileName = pInputRecordFileName;
    }

    /**
	 * Get the input record file path
	 * @return The input record file path
	 */
    public String getInputRecordFilePath() {
        StringBuilder sb = new StringBuilder();
        if (getUseTransaction()) {
            sb.append(getTransactionDirectoryPath());
        } else {
            sb.append(getWorkingDirectory());
        }
        sb.append("/");
        sb.append(getInputRecordFileName());
        return sb.toString();
    }

    /**
	 * Get the output record file name
	 * @return The output record file name
	 */
    public String getOutputRecordFileName() {
        return mOutputRecordFileName;
    }

    /**
	 * Set the output record file name
	 * @param The output record file name
	 */
    public void setOutputRecordFileName(String pOutputRecordFileName) {
        mOutputRecordFileName = pOutputRecordFileName;
    }

    /**
	 * Get the output record file path
	 * @return The output record file path
	 */
    public String getOutputRecordFilePath() {
        StringBuilder sb = new StringBuilder();
        if (getUseTransaction()) {
            sb.append(getTransactionDirectoryPath());
        } else {
            sb.append(getWorkingDirectory());
        }
        sb.append("/");
        sb.append(getOutputRecordFileName());
        return sb.toString();
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
	 * Get the search XML queue
	 * @return The search XML queue
	 */
    public BlockingQueue<SearchRequest> getSearchXMLQueue() {
        return mSearchXMLQueue;
    }

    /**
	 * Set the search XML queue
	 * @param pSearchXMLQueue The search XML queue
	 */
    public void setSearchXMLQueue(BlockingQueue<SearchRequest> pSearchXMLQueue) {
        mSearchXMLQueue = pSearchXMLQueue;
    }

    /**
	 * Get the search request XML thread pool executor
	 * @return The search request XML thread pool executor
	 */
    public SearchRequestXMLThreadPoolExecutor getSearchRequestXMLThreadPoolExecutor() {
        return mSearchRequestXMLThreadPoolExecutor;
    }

    /**
	 * Set the search request XML thread pool executor
	 * @param pSearchRequestXMLThreadPoolExecutor The search request XML thread pool executor
	 */
    public void setSearchRequestXMLThreadPoolExecutor(SearchRequestXMLThreadPoolExecutor pSearchRequestXMLThreadPoolExecutor) {
        mSearchRequestXMLThreadPoolExecutor = pSearchRequestXMLThreadPoolExecutor;
    }

    /**
	 * Get the search request XML processors
	 * @return The search request XML processors
	 */
    public List<SearchRequestXMLProcessor> getSearchRequestXMLProcessors() {
        return mSearchRequestXMLProcessors;
    }

    /**
	 * The usage
	 */
    protected void usage() {
        logInfo("Usage: java org.amlfilter.batch.AMLFBatch");
        System.exit(1);
    }

    /**
	 * Get the input record file stream
	 * @return The input record file stream
	 * @throws IOException
	 */
    protected InputStream getInputRecordFileStream() throws IOException {
        return new FileInputStream(getInputRecordFilePath());
    }

    /**
	 * Get the output record file stream
	 * @return The output record file stream
	 * @throws IOException
	 */
    protected OutputStream getOutputRecordFileStream() throws IOException {
        return new FileOutputStream(getOutputRecordFilePath());
    }

    /**
	 * Create the search record
	 * @param pSearchRecordTokens The search record tokens
	 */
    protected SearchRecord createSearchRecord(String pSearchRecordStr) {
        SearchRecord searchRecord = SearchRecord.getSearchRecord(pSearchRecordStr);
        searchRecord.setFullName(XMLService.xmlEscape(searchRecord.getFullName()));
        searchRecord.setPlacesOfInception(XMLService.xmlEscape(searchRecord.getPlacesOfInception()));
        searchRecord.setDatesOfInception(XMLService.xmlEscape(searchRecord.getDatesOfInception()));
        searchRecord.setAddresses(XMLService.xmlEscape(searchRecord.getAddresses()));
        searchRecord.setCitizenships(XMLService.xmlEscape(searchRecord.getCitizenships()));
        searchRecord.setIdentificationDocuments(XMLService.xmlEscape(searchRecord.getIdentificationDocuments()));
        return searchRecord;
    }

    /**
	 * Create a search request
	 * @return The search request
	 */
    protected SearchRequest createSearchRequest() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setProcessId(Long.toString(getProcessId()));
        SearchPreferences searchPreferences = new SearchPreferences();
        SearchPreference searchPreference = new SearchPreference();
        searchPreference.setPreferenceKey(SearchPreferences.SIMILARITY_THRESHOLD);
        searchPreference.setPreferenceValue(Float.toString(getSimilarityThreshold()));
        searchPreferences.putPreference(SearchPreferences.SIMILARITY_THRESHOLD, searchPreference);
        searchPreference = new SearchPreference();
        searchPreference.setPreferenceKey(SearchPreferences.USE_ANALYTICS);
        searchPreference.setPreferenceValue(Boolean.toString(getUseAnalytics()));
        searchPreferences.putPreference(SearchPreferences.USE_ANALYTICS, searchPreference);
        searchRequest.setSearchPreferences(searchPreferences);
        return searchRequest;
    }

    /**
	 * Add the search record to the search request
	 */
    protected void addSearchRecord(SearchRequest pSearchRequest, SearchRecord pSearchRecord) {
        pSearchRequest.getSearchRecords().getSearchRecordList().add(pSearchRecord);
    }

    /**
	 * Prepare the thread pool executor by creating search request XML processors
	 */
    protected int prepareProcessors() throws Exception {
        String[] searchURLTokens = getSearchURLs().split(",");
        for (int i = 0; i < searchURLTokens.length; i++) {
            String url = searchURLTokens[i].trim();
            String taskName = SearchRequestXMLProcessor.class.getName() + "-" + (i + 1);
            SearchRequestXMLProcessor srxp = new SearchRequestXMLProcessor(taskName, url, getBatchSize(), this);
            getSearchRequestXMLProcessors().add(srxp);
            logInfo("Using URL (" + url + ") with batch size (" + getBatchSize() + ") for task name [" + taskName + "]");
        }
        return getSearchRequestXMLProcessors().size();
    }

    /**
	 * Execute the processors
	 */
    public void executeProcessors() {
        for (int i = 0; i < getSearchRequestXMLProcessors().size(); i++) {
            SearchRequestXMLProcessor srxp = getSearchRequestXMLProcessors().get(i);
            getSearchRequestXMLThreadPoolExecutor().runTask(srxp);
            logInfo("Started task name: " + srxp.getTaskId());
        }
    }

    /**
	 * Read each record from the input file create a search request
	 * XML out of it and inject it into a queue to be consumed later
	 * @param args
	 */
    public int processRecords() throws Exception {
        final String methodSignature = "int processRecords(): ";
        int numSearchRequests = 0;
        String record = null;
        InputRecordProcessor irp = null;
        boolean refillSearchRequest = true;
        int i = 0;
        try {
            irp = new InputRecordProcessor(getInputRecordFileStream(), getCharSet());
            SearchRequest searchRequest = createSearchRequest();
            SearchRecord searchRecord = null;
            while (null != (record = irp.readRecord())) {
                String[] tokens = record.concat(GeneralConstants.SPACE_TOKEN).split(getFieldDelimiter());
                if (tokens.length != 9) {
                    continue;
                }
                try {
                    searchRecord = createSearchRecord(record);
                } catch (Exception e) {
                    continue;
                }
                addSearchRecord(searchRequest, searchRecord);
                i++;
                if (0 == (i % getBatchSize())) {
                    String searchXML = getSearchRequestXMLGenerator().generateSearchRequestXML(searchRequest);
                    searchRequest.setSearchXML(searchXML);
                    getSearchXMLQueue().add(searchRequest);
                    numSearchRequests++;
                    searchRequest = createSearchRequest();
                }
                if (getSearchXMLQueue().size() >= mSearchRequestQueueUpperBound) {
                    refillSearchRequest = false;
                }
                while (false == refillSearchRequest) {
                    if (getSearchXMLQueue().size() < mSearchRequestQueueLowerBound) {
                        refillSearchRequest = true;
                        if (isLoggingDebug()) {
                            logDebug("Populated search request count: " + (numSearchRequests));
                        }
                    } else {
                        Thread.sleep(5);
                    }
                    if (areThreadsDead()) {
                        throw new IllegalStateException("All threads are dead");
                    }
                }
            }
            if (searchRequest.getSearchRecords().getSearchRecordList().size() > 0) {
                String searchXML = getSearchRequestXMLGenerator().generateSearchRequestXML(searchRequest);
                searchRequest.setSearchXML(searchXML);
                getSearchXMLQueue().add(searchRequest);
            }
            int totalRequests = numSearchRequests;
            logInfo("Number of search requests created: " + totalRequests);
            return i;
        } finally {
            irp.cleanResources();
        }
    }

    /**
	 * Create the bad record log
	 * @return A buffered writer to write to the log
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
    protected BufferedWriter createBadRecordLog() throws FileNotFoundException, UnsupportedEncodingException {
        String badRecordLogFilePath = GeneralUtils.generateAbsolutePath(getTransactionDirectoryPath(), getBadRecordLogFileName(), "/");
        badRecordLogFilePath = badRecordLogFilePath + ".log";
        FileOutputStream fos = new FileOutputStream(badRecordLogFilePath);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, GeneralConstants.UTF8));
        return bw;
    }

    /**
	 * Count the number of file records and validate the file
	 * Note: This will be the number of records to process
	 * @return The number of file records
	 */
    public int validateFile() throws Exception {
        final String methodSignature = "int validateFile(): ";
        LRUMap uniqueIdMap = new LRUMap(10000, true);
        BufferedReader br = new BufferedReader(new InputStreamReader(getInputRecordFileStream(), getCharSet()));
        BufferedWriter bw_log = createBadRecordLog();
        int count = 0;
        int ignoreCount = 0;
        try {
            String record = null;
            String uniqueId = null;
            SearchRecord searchRecord = null;
            while (null != (record = br.readLine())) {
                String[] tokens = record.concat(GeneralConstants.SPACE_TOKEN).split(getFieldDelimiter());
                if (tokens.length != 9) {
                    String message = "Ignoring record [" + record + "]; cause invalid number of fields expected [9], received [" + tokens.length + "]) at input file line #: " + (count + 1);
                    message = new String(message.getBytes(GeneralConstants.UTF8), GeneralConstants.UTF8);
                    if (isLoggingDebug()) {
                        logDebug(message);
                    }
                    bw_log.write(message);
                    bw_log.write("\n");
                    ignoreCount++;
                    continue;
                }
                try {
                    searchRecord = createSearchRecord(record);
                } catch (Exception e) {
                    String message = "Ignoring record [" + record + "]; cause (" + e.getMessage() + ") at input file line #: " + (count + 1);
                    message = new String(message.getBytes(GeneralConstants.UTF8), GeneralConstants.UTF8);
                    if (isLoggingDebug()) {
                        logDebug(message);
                    }
                    bw_log.write(message);
                    bw_log.write("\n");
                    ignoreCount++;
                    continue;
                }
                uniqueId = searchRecord.getUniqueId();
                if (uniqueIdMap.containsKey(uniqueId)) {
                    throw new IllegalStateException("Client ids must be unique; uniqueId: " + uniqueId + "; at input file line #: " + (count + 1));
                } else {
                    uniqueIdMap.put(uniqueId, null);
                }
                count++;
            }
            return count;
        } finally {
            if (isLoggingInfo()) {
                logInfo("Number of records ignored: " + ignoreCount + "; please check the bad records log file: " + this.getBadRecordLogFileName());
            }
            if (null != br) {
                br.close();
            }
            if (null != bw_log) {
                bw_log.flush();
                bw_log.close();
            }
        }
    }

    /**
	 * This signals all the tasks to finish
	 * This typically happens when the producer has nothing more to produce
	 */
    public void signalFinish() {
        for (int i = 0; i < getSearchRequestXMLProcessors().size(); i++) {
            SearchRequestXMLProcessor srxp = (SearchRequestXMLProcessor) getSearchRequestXMLProcessors().get(i);
            srxp.getStopProcessing().set(true);
        }
    }

    /**
	 * Checks to see if it is finished
	 * by checking the finished count flag
	 * @return True if finished, false otherwise
	 */
    public boolean areThreadsFinished() {
        int finishedCount = 0;
        for (int i = 0; i < getSearchRequestXMLProcessors().size(); i++) {
            SearchRequestXMLProcessor srxp = (SearchRequestXMLProcessor) getSearchRequestXMLProcessors().get(i);
            if (true == srxp.getDoneProcessing()) {
                finishedCount++;
            }
        }
        if (getSearchRequestXMLProcessors().size() == finishedCount) {
            return true;
        }
        return false;
    }

    /**
     * Are all the processors idle?
     */
    public boolean areThreadsIdle() {
        int idleCount = 0;
        for (int i = 0; i < getSearchRequestXMLProcessors().size(); i++) {
            SearchRequestXMLProcessor srxp = (SearchRequestXMLProcessor) getSearchRequestXMLProcessors().get(i);
            if (true == srxp.isIdle()) {
                idleCount++;
            }
        }
        logDebug("idleCount: " + idleCount);
        if (getSearchRequestXMLProcessors().size() == idleCount) {
            return true;
        }
        return false;
    }

    /**
     * Are all the processors dead?
     */
    public boolean areThreadsDead() {
        int deadCount = 0;
        for (int i = 0; i < getSearchRequestXMLProcessors().size(); i++) {
            SearchRequestXMLProcessor srxp = (SearchRequestXMLProcessor) getSearchRequestXMLProcessors().get(i);
            if (true == srxp.isDead()) {
                deadCount++;
            }
        }
        logDebug("deadCount: " + deadCount);
        if (getSearchRequestXMLProcessors().size() == deadCount) {
            return true;
        }
        return false;
    }

    /**
     * Cleanup the resources like the processors and any other resource in use
     * @param args
     * @throws IOException
     * @throws Exception
     */
    public void cleanupResources() throws IOException {
        for (int i = 0; i < getSearchRequestXMLProcessors().size(); i++) {
            SearchRequestXMLProcessor srxp = (SearchRequestXMLProcessor) getSearchRequestXMLProcessors().get(i);
            srxp.cleanResources();
        }
        getInputRecordProcessor().cleanResources();
        getOutputRecordProcessor().cleanResources();
        SearchRequestXMLProcessor.getSearchResultCount().set(0);
        SearchRequestXMLProcessor.getTotalPersistedCount().set(0);
        SearchRequestXMLProcessor.getTotalProcessedCount().set(0);
        SearchRequestXMLProcessor.getTotalTimeForProcessing().set(0);
    }

    /**
     * Generate transaction id
     * @return The transaction id
     */
    public String generateTransactionId() {
        String transactionId = Long.toString(System.nanoTime());
        if (isLoggingInfo()) {
            logInfo("Generated transaction id: " + transactionId);
        }
        return transactionId;
    }

    /**
     * Get the AMLF Batch (whether from SPRING or creating a new instance manually)
     * @return The AMLF Batch object
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static AMLFBatch getAMLFBatch() throws UnsupportedEncodingException, IOException {
        String log4jPath = new ClassPathResource("amlf-batch_log4j.properties").getFile().getPath();
        System.out.println("log4jPath: " + log4jPath);
        PropertyConfigurator.configure(log4jPath);
        XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("amlf-batch_applicationContext.xml"));
        PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
        cfg.setLocation(new ClassPathResource("amlf-batch_admin-config.properties"));
        cfg.postProcessBeanFactory(beanFactory);
        AMLFBatch amlfBatch = (AMLFBatch) beanFactory.getBean("amlfBatch");
        if (null == amlfBatch) {
            amlfBatch = new AMLFBatch();
        }
        return amlfBatch;
    }

    /**
     * Get the transaction directory path
     * @return The transaction directory path
     */
    public String getTransactionDirectoryPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(getWorkingDirectory());
        if (getUseTransaction()) {
            sb.append("/");
            sb.append(getTransactionId());
        }
        return sb.toString();
    }

    /**
     * Ensure transaction resources
     * - Creates a transaction directory
     * - Copies the input file into it
     * - Points the input and output files into this directory
     * - Points analytic_rejection log (from servers) into this directory
     * - Points bad response logs into this directory
     */
    protected void ensureTransactionResources() throws Exception {
        if (getUseTransaction()) {
            FileUtils.forceMkdir(new File(getTransactionDirectoryPath()));
            String originalInputRecordFilePath = getWorkingDirectory() + "/" + getInputRecordFileName();
            FileUtils.copyFile(new File(originalInputRecordFilePath), new File(getInputRecordFilePath()));
        }
    }

    public static void main(String[] args) throws Exception {
        AMLFBatch amlfBatch = AMLFBatch.getAMLFBatch();
        amlfBatch.start();
    }

    public void start() throws Exception {
        logInfo("STARTING");
        ensureTransactionResources();
        InputRecordProcessor irp = new InputRecordProcessor(getInputRecordFileStream(), getCharSet());
        setInputRecordProcessor(irp);
        OutputRecordProcessor orp = new OutputRecordProcessor(getOutputRecordFileStream(), getCharSet());
        setOutputRecordProcessor(orp);
        long startTime = -1L;
        SearchRequestXMLThreadPoolExecutor srxtpe = null;
        try {
            logInfo("*** Validating file: " + getInputRecordFilePath());
            int numFileRecords = validateFile();
            startTime = System.currentTimeMillis();
            setStartTime(startTime);
            setNumberToProcess(numFileRecords);
            logInfo("*** Number to process: " + getNumberToProcess());
            int numProcessors = prepareProcessors();
            setNumberOfRequestThreads(numProcessors);
            logInfo("*** Number of threads in pool: " + getNumberOfRequestThreads());
            srxtpe = new SearchRequestXMLThreadPoolExecutor(getNumberOfRequestThreads());
            setSearchRequestXMLThreadPoolExecutor(srxtpe);
            executeProcessors();
            processRecords();
        } catch (Exception e) {
            System.out.println("***** An error happened while processing; please check logs");
            logError(GeneralUtils.getStackTraceAsString(e, 50));
            cleanupResources();
            return;
        } finally {
            if (null != srxtpe) {
                srxtpe.shutDown();
            }
        }
        while (!areThreadsFinished()) {
            if (areThreadsIdle()) {
                if (isLoggingDebug()) {
                    logDebug("Threads are idle, signaling finish");
                }
                signalFinish();
            }
            while (getSearchXMLQueue().size() > 0 && !areThreadsIdle()) {
                if (isLoggingDebug()) {
                    logDebug("Thread Pool Active Count: " + srxtpe.getActiveCount() + "queueSize: " + getSearchXMLQueue().size() + "; idle: " + areThreadsIdle());
                }
                Thread.sleep(1000);
            }
            Thread.sleep(100);
        }
        if (getSearchXMLQueue().size() > 0) {
            getSearchRequestXMLProcessors().get(0).exec();
            logInfo("Searching remainder in queue: " + getSearchXMLQueue().size());
        }
        SearchRequestXMLProcessor.showStatistics(this, getClass(), getClass().getName());
        cleanupResources();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        logInfo("totalTime: " + totalTime);
        logInfo("DONE");
    }
}
