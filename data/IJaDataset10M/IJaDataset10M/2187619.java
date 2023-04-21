package edu.berkeley.cs.db.yfilter.icdedemo;

import edu.berkeley.cs.db.yfilterplus.dtdscanner.DTDStat;
import edu.berkeley.cs.db.yfilterplus.querygenerator.PathGenerator;
import edu.berkeley.cs.db.yfilterplus.queryparser.*;
import edu.berkeley.cs.db.yfilterplus.xmltree.XMLTree;
import edu.berkeley.cs.db.yfilterplus.utility.DocumentReader;
import edu.berkeley.cs.db.yfilter.filter.*;
import java.util.*;
import java.io.*;

public class ICDEDemo extends Thread {

    public static String m_yfilter_home;

    int m_msecs = 60000 * 10;

    DTDStat m_stat;

    String m_dtdFilename;

    PathGenerator m_qg;

    double m_queryRate = 0.003;

    /**
   * The query buffer
   */
    Vector m_queryBuffer;

    int m_maxQueryBufferSize = 10000;

    boolean m_dynamicUpdate = true;

    int m_initialQueryLoad = 100;

    String m_queriesFilename = "queries.txt";

    RandomAccessFile m_file;

    int m_queryCount = 0;

    Vector m_queryIndex;

    double m_theta = 0;

    public int m_maxDepth = 6;

    public double m_wildcard = 0.2;

    public double m_dSlash = 0.2;

    public double m_predProb = 0.03;

    public char m_levelDist = 'u';

    public double m_nestedPath = 0.2;

    String m_filename_suffix = "outFile";

    String m_indirXML;

    String m_outdirXML = "xmldocs";

    int m_docsCount = 0;

    /**
   * The document buffer
   */
    Vector m_xmlBuffer;

    int m_maxXMLBufferSize = 20;

    String m_currentXMLdoc = null;

    boolean m_batchMode = false;

    public int m_maxLevel = 6;

    public int m_maxRepeats = 3;

    int m_algo = 0;

    boolean m_evalAll = false;

    boolean m_checkPredicates = true;

    /**
   * Query processor
   */
    static EXfilterBasic m_xfilter;

    boolean m_run = false;

    Object m_runLock = new Object();

    int m_cycleCount = 0;

    boolean m_step = false;

    Framework parent = null;

    int m_noFilterData = 20;

    int m_noThroughputData = 10;

    Vector m_filteringtime = new Vector();

    Vector m_throughput = new Vector();

    long m_accumTime = 0;

    long m_accumElements = 0;

    String m_dir = ".";

    Vector m_allQueriesInFilter = new Vector();

    public ICDEDemo(String dtdAbsolutePath, Framework frame) {
        parent = frame;
        m_dtdFilename = dtdAbsolutePath;
        m_stat = new DTDStat(dtdAbsolutePath);
        m_queryBuffer = new Vector();
        m_queryIndex = new Vector();
        m_queryIndex.add(new Long(0));
        m_qg = new PathGenerator(m_stat, m_theta);
        try {
            m_file = new RandomAccessFile(m_queriesFilename, "rw");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (m_yfilter_home != null) m_indirXML = m_yfilter_home + "/tests/xmldocs-500";
        m_xmlBuffer = new Vector();
        m_xfilter = new EXfilterBasic(m_stat, this);
        if (parent != null) {
            parent.write(m_stat.getReportStat());
            parent.writeln("Dynamic Rate is initially " + m_queryRate + " queries per ms\n");
        }
    }

    public ICDEDemo(String dtdAbsolutePath) {
        m_dtdFilename = dtdAbsolutePath;
        m_stat = new DTDStat(dtdAbsolutePath);
        m_queryBuffer = new Vector();
        m_queryIndex = new Vector();
        m_queryIndex.add(new Long(0));
        m_qg = new PathGenerator(m_stat, m_theta);
        try {
            m_file = new RandomAccessFile(m_queriesFilename, "rw");
        } catch (Exception e) {
            e.printStackTrace();
        }
        m_xmlBuffer = new Vector();
        m_xfilter = new EXfilterBasic(m_stat, this);
    }

    /**
   * Returns an instance of the EXfilter
   * 
   * @return
   */
    public EXfilter getEXfilter() {
        return m_xfilter;
    }

    public void pause() {
        synchronized (m_runLock) {
            m_run = false;
            m_cycleCount = 1;
        }
    }

    public void runAgain() {
        synchronized (m_runLock) {
            m_run = true;
            m_runLock.notifyAll();
        }
    }

    public void cycle() {
        try {
            synchronized (m_runLock) {
                m_cycleCount = 1;
                m_runLock.notifyAll();
                m_runLock.wait();
            }
        } catch (InterruptedException inte) {
            inte.printStackTrace();
        }
    }

    public void step() {
        synchronized (m_runLock) {
            m_step = true;
            m_runLock.notifyAll();
        }
    }

    public void stepEnd(String elementName) {
        if (!m_step) return;
        try {
            synchronized (m_runLock) {
                if (m_step && parent != null) {
                    parent.writeln("------------------ Stepping element: " + elementName + "...");
                    parent.searchXML(elementName);
                    ArrayList v = m_xfilter.getNewlyMatchedQueries();
                    int size = v.size();
                    for (int i = 0; i < size; i++) {
                        int queryID = ((Integer) v.get(i)).intValue();
                        parent.writeln("Query #" + queryID + ": " + getQuery(queryID));
                    }
                    parent.writeln("------------------ Finished stepping <" + elementName + ">: " + size + " queries matched");
                    parent.writeln("\n\n");
                }
                m_step = false;
                while (m_step != true && m_run != true && m_cycleCount <= 0) {
                    m_runLock.notifyAll();
                    m_runLock.wait();
                }
            }
        } catch (InterruptedException inte) {
            inte.printStackTrace();
        }
    }

    public void generateQueries(long amount) {
        StringBuffer queryString;
        Query query;
        long i = 0;
        synchronized (m_queryBuffer) {
            while (i < amount && m_queryBuffer.size() < m_maxQueryBufferSize) {
                queryString = m_qg.generateOnePath(m_maxDepth, 0, m_wildcard, m_dSlash, m_predProb, m_levelDist, m_nestedPath, m_stat.getRoot());
                addQueryToBuffer(queryString.toString());
                i++;
            }
        }
        if (parent != null) {
            parent.writeln("Generated " + i + " queries");
        }
    }

    /** Adds one query string to buffer and also stores it on file.
   * 
   * @param queryString
   */
    public void addQueryToBuffer(String queryString) {
        Query query;
        query = XPQuery.parseQuery(queryString.toString(), ++m_queryCount);
        m_queryBuffer.add(query);
        String queryDisplay = new String("Query #: " + m_queryCount + " -> " + queryString.toString());
        m_allQueriesInFilter.add(queryDisplay);
        try {
            m_queryIndex.add(new Long(m_file.getFilePointer()));
            m_file.writeBytes(queryString + "\n");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
   * Removes the last element in the query buffer
   *
   */
    public void dequeueQuery() {
        synchronized (m_queryBuffer) {
            m_queryBuffer.remove(m_queryBuffer.size() - 1);
        }
    }

    public String getQuery(int queryID) {
        long offset;
        String queryString = null;
        long original;
        if (queryID >= m_queryIndex.size()) {
            return null;
        }
        offset = ((Long) m_queryIndex.elementAt(queryID)).longValue();
        try {
            original = m_file.getFilePointer();
            m_file.seek(offset);
            queryString = m_file.readLine();
            m_file.seek(original);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return queryString;
    }

    public ArrayList getMatchedQueries() {
        return m_xfilter.getMatchedQueries();
    }

    public ArrayList getMatchingElements(int id) throws Exception {
        return m_xfilter.getMatchingElements(id);
    }

    public String getStringOfMatchingElements(int id) throws Exception {
        return m_xfilter.stringOfMatchingElements(id);
    }

    /**
   * Enqueues queries from a file into the query listener
   * @param filename the file containing the queries
   */
    public void enqueueQueries(String filename) {
        Query query;
        int amount = 0;
        String queryString = null;
        synchronized (m_queryBuffer) {
            QueryParser qp = new XFQueryParser(filename);
            while ((queryString = qp.readNextQueryString()) != null) {
                addQueryToBuffer(queryString);
                amount++;
            }
        }
        if (parent != null) {
            parent.writeln("BulkLoaded " + amount + " queries");
        }
        addQueriesToFilter();
    }

    public void dynamicQueries(long ms) {
        if (parent != null) {
            parent.writeln("Accepting queries for " + ms + "ms");
        }
        synchronized (m_queryBuffer) {
            generateQueries(Math.round(m_queryRate * ms));
        }
    }

    public boolean isBatchMode() {
        return m_batchMode;
    }

    /**
   * Toggles the batch processing mode
   * @param b 
   */
    public void setBatchMode(boolean b) {
        m_batchMode = b;
        m_currentXMLdoc = null;
    }

    public boolean isDynamicUpdateEnabled() {
        return m_dynamicUpdate;
    }

    public void setDynamicUpdateEnabled(boolean b) {
        m_dynamicUpdate = b;
    }

    /**
   * Sets the query rate
   * 
   * @param rate
   */
    public void setQueryRate(double rate) {
        m_queryRate = rate;
    }

    public double getQueryRate() {
        return m_queryRate;
    }

    public int getQueryBufferSize() {
        return m_maxQueryBufferSize;
    }

    public int getXMLBufferSize() {
        return m_maxXMLBufferSize;
    }

    public void changeQueryBufferSize(int size) {
        m_maxQueryBufferSize = size;
    }

    public void changeXMLBufferSize(int size) {
        m_maxXMLBufferSize = size;
    }

    /**
   *  Adds all queries in the query buffer to Xfilter
   */
    public void addQueriesToFilter() {
        Query query;
        int count = 0;
        synchronized (m_queryBuffer) {
            while (m_queryBuffer.size() > 0) {
                query = (Query) m_queryBuffer.firstElement();
                m_queryBuffer.removeElement(query);
                m_xfilter.addQuery(query);
                count++;
            }
        }
        if (parent != null) {
            parent.writeln("Added " + count + " queries to Filter\n");
        }
    }

    /**
   * Enqueues an XML document or documents to the document queue
   * @param strFilename
   */
    public void enqueueXML(String strFileNameOrDir) {
        XMLTree xmltree = null;
        File file = new File(strFileNameOrDir);
        if (file.isFile()) {
            xmltree = new XMLTree(strFileNameOrDir);
            enqueueXML(xmltree);
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                String strFile = files[i].getAbsolutePath();
                if (strFile.endsWith("xml")) xmltree = new XMLTree(strFile);
                enqueueXML(xmltree);
            }
        }
    }

    public void fillXMLBuffer() {
        String xmldoc;
        XMLTree xmltree;
        int i = 0;
        while (m_xmlBuffer.size() < m_maxXMLBufferSize) {
            i++;
            xmltree = new XMLTree(m_dir + "/outFile" + i + ".xml");
            enqueueXML(xmltree);
        }
        if (parent != null) {
            parent.writeln("");
        }
    }

    public void changeDirectory(String dir) {
        m_dir = dir;
    }

    public void batchMode() {
        fillXMLBuffer();
        while (!m_xmlBuffer.isEmpty()) {
            XMLTree xmltree = dequeueXML();
            processXMLTreeAndReport(xmltree);
        }
    }

    /**
   * Process all documents in the document buffer
   * 
   * @return Vector contains an ArrayList of matched queries, one per document 
   */
    public Vector processDocuments() {
        Vector queries = new Vector();
        while (!m_xmlBuffer.isEmpty()) {
            XMLTree xmltree = dequeueXML();
            if (xmltree != null) queries.add(processXMLTree(xmltree));
        }
        return queries;
    }

    public String generateXML() {
        if (m_indirXML != null) {
            int docIndex = (m_docsCount) % 500 + 1;
            m_docsCount++;
            String filename = m_indirXML + "/" + m_filename_suffix + docIndex + ".xml";
            return DocumentReader.readDoctoString(filename);
        } else {
            System.err.println("\nYFilter System Error! ICDEDemo::generateXML() -- no XML documents are available for this demo.");
            return null;
        }
    }

    public void storeData(long time, int noElements) {
        m_filteringtime.add(0, new Long(time));
        if (m_filteringtime.size() > m_noFilterData) {
            m_filteringtime.removeElementAt(m_noFilterData);
        }
        m_accumElements += noElements;
        m_accumTime += time;
        if (m_accumTime >= 1000) {
            long diff_time = m_accumTime - 1000;
            m_accumElements -= noElements * diff_time / time;
            m_throughput.add(0, new Long(m_accumElements));
            if (m_throughput.size() > m_noThroughputData) {
                m_throughput.removeElementAt(m_noThroughputData);
            }
            m_accumElements = 0;
            m_accumTime = 0;
        }
    }

    public Vector getFilteringtime() {
        return m_filteringtime;
    }

    public Vector getThroughput() {
        return m_throughput;
    }

    public String getFilteringArray() {
        int size = m_filteringtime.size();
        String out = "Filtering Array (" + size + ") : ";
        for (int i = 0; i < size; i++) {
            out += ((Long) m_filteringtime.get(i)).toString() + " ";
        }
        return out;
    }

    public String getThroughputArray() {
        int size = m_throughput.size();
        String out = "Throughput Array (" + size + ") : ";
        for (int i = 0; i < size; i++) {
            out += ((Long) m_throughput.get(i)).toString() + " ";
        }
        return out;
    }

    /** Processes an XML Tree and returns the matched results.
   * 
   * @param xmltree 	contains parsed XML document
   * @return ArrayList contains matched query id's.
   */
    public ArrayList processXMLTree(XMLTree xmltree) {
        m_xfilter.clear();
        m_xfilter.setXMLTree(xmltree);
        m_xfilter.allocateStreams();
        m_xfilter.startParsing();
        return m_xfilter.getMatchedQueries();
    }

    public void processXMLTreeAndReport(XMLTree xmltree) {
        long start_time;
        long process_time;
        m_xfilter.clear();
        m_xfilter.setXMLTree(xmltree);
        m_xfilter.allocateStreams();
        start_time = System.currentTimeMillis();
        if (m_step == true) {
            m_xfilter.startParsing();
            process_time = System.currentTimeMillis() - start_time;
        } else {
            m_xfilter.startParsing();
            process_time = System.currentTimeMillis() - start_time;
            storeData(process_time, m_xfilter.getNoElements());
        }
        m_xfilter.getMatchedQueries();
        if (parent != null) {
            parent.writeln("Finished processing the xml document.");
            update();
            parent.writeln("Took " + process_time + "ms.\n XML contains: " + m_xfilter.getNoElements() + "elements.\n# of matches: " + m_xfilter.getNoMatchedQueries() + "\n");
        }
    }

    public void oneXMLdoc() {
        XMLTree xmltree;
        m_currentXMLdoc = generateXML();
        if (parent != null) {
            parent.setXML(m_currentXMLdoc);
        }
        xmltree = new XMLTree(new StringReader(m_currentXMLdoc));
        processXMLTreeAndReport(xmltree);
    }

    public String getCurrentXML() {
        return m_currentXMLdoc;
    }

    public String getCurrentQueries() {
        StringBuffer allqueries = new StringBuffer();
        for (int i = 0; i < m_allQueriesInFilter.size(); i++) {
            allqueries.append((String) (m_allQueriesInFilter.get(i)) + "\n");
        }
        return allqueries.toString();
    }

    public void enqueueXML(XMLTree xmltree) {
        m_xmlBuffer.add(xmltree);
        if (parent != null) {
            parent.writeln("Enqueued the xmltree");
            parent.updateXMLInBuffer(m_xmlBuffer.size());
        }
    }

    /** Dequeue the last document in the queue
   * 
   * @return XMLTree the document de-queued
   */
    public XMLTree dequeueXML() {
        XMLTree xmltree;
        synchronized (m_xmlBuffer) {
            if (m_xmlBuffer.isEmpty()) {
                return null;
            }
            xmltree = (XMLTree) m_xmlBuffer.firstElement();
            m_xmlBuffer.removeElement(xmltree);
        }
        if (parent != null) {
            parent.writeln("Dequeued the xmltree");
            parent.updateXMLInBuffer(m_xmlBuffer.size());
        }
        return xmltree;
    }

    public void update() {
        if (parent == null) return;
        parent.updateFrame();
    }

    public void run() {
        Query query;
        long interval;
        long start_time;
        long end_time;
        while (true) {
            try {
                synchronized (m_runLock) {
                    while (m_run != true && m_cycleCount <= 0 && m_step != true) {
                        m_runLock.notifyAll();
                        m_runLock.wait();
                    }
                }
            } catch (InterruptedException inte) {
                inte.printStackTrace();
            }
            if (parent != null) {
                parent.clearText();
            }
            start_time = System.currentTimeMillis();
            if (m_batchMode) {
                batchMode();
            } else {
                oneXMLdoc();
            }
            interval = System.currentTimeMillis() - start_time;
            if (m_dynamicUpdate) {
                dynamicQueries(interval);
                addQueriesToFilter();
            }
            if (parent != null) {
                update();
                parent.writeln("Finished one cycle\n");
            }
            synchronized (m_runLock) {
                m_cycleCount--;
            }
        }
    }

    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer();
        ICDEDemo demo = new ICDEDemo("nitf-2-5.dtd", null);
        demo.enqueueQueries("bulkloadqueries.txt");
        demo.start();
        demo.runAgain();
        while (true) ;
    }
}
