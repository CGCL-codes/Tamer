package edu.upmc.opi.caBIG.caTIES.server.ties;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import edu.upmc.opi.caBIG.caTIES.client.ApplicationLauncher;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_Constants;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_DateTimeUtils;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_ExceptionLogger;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_JDomUtils;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_PropertyLoader;
import edu.upmc.opi.caBIG.caTIES.common.GeneralUtilities;
import edu.upmc.opi.caBIG.caTIES.connector.CaTIES_DataSourceManager;
import edu.upmc.opi.caBIG.caTIES.connector.CaTIES_DataSourceManager.MODE;
import edu.upmc.opi.caBIG.caTIES.database.domain.impl.DocumentImpl;
import edu.upmc.opi.caBIG.caTIES.database.domain.impl.SectionImpl;
import edu.upmc.opi.caBIG.caTIES.database.domain.impl.SectionTypeImpl;
import edu.upmc.opi.caBIG.caTIES.middletier.CaTIES_DocumentImpl;
import edu.upmc.opi.caBIG.caTIES.server.CaTIES_ConceptAccumulator;
import edu.upmc.opi.caBIG.caTIES.server.CaTIES_ExporterPR;
import edu.upmc.opi.caBIG.caTIES.server.section.CaTIES_SectionManager;
import edu.upmc.opi.caBIG.common.CaBIG_LobUtilities;

/**
 * Constructs GATE Pipeline and executes it against data store.
 * 
 * @author mitchellkj@upmc.edu
 * @version $Id: CaTIES_TiesPipeController.java,v 1.13 2006/09/06 13:54:42
 *          1upmc-acct\chavgx Exp $
 * @since 1.4.2_04
 */
public class CaTIES_TiesPipeController implements Runnable {

    public static final String revision = "3.6";

    private static final Logger logger = Logger.getLogger(CaTIES_TiesPipeController.class);

    private int coderTotal = 1;

    private int coderNumber = 0;

    private int currentCoderNumber = 0;

    private long numberReportsProcessed;

    private CaTIES_DataSourceManager publicDataSourceManager;

    private CaTIES_SectionManager sectionMgr;

    private DocumentImpl currentDocumentToCode;

    private Date startTime;

    private Date endTime;

    private CaTIES_TiesPipe tiesPipe;

    private Session publicSession;

    private CaTIES_ExporterPR exporterPR;

    private String minOID = "";

    private String currentReportText = "";

    private String tiesResponse = "";

    private String conceptCodeSet = "";

    private Collection<SectionImpl> currentSections = new ArrayList<SectionImpl>();

    private String gateXML = "";

    private String chirpsXML = "";

    private String theCodes = "";

    private String gateHome;

    private String creoleUrlName;

    private String caseInsensitiveGazetteerUrlName;

    private String sectionHeaderDetectorUrlName;

    private String sectionChunkerUrlName;

    private String conceptFilterUrlName;

    private String negExUrlName;

    private String conceptCategorizerUrlName;

    private boolean isRunning = true;

    private boolean isGeneratingSections = false;

    public static final String IS_GENERATING_REFERENTS = "caties.is.generating.referents";

    public static final String IS_GENERATING_SECTIONS = "caties.is.generating.sections";

    public static final String CLIENT_SIDE_CODE_BOOK_HANDLE = "caties.hibernate.public.cfg";

    public static final String CREOLE_URL_NAME = "caties.creole.url.name";

    public static final String CASE_INSENSITIVE_GAZETTEER_URL_NAME = "caties.case.insensitive.gazetteer.url.name";

    public static final String SECTION_HEADER_DETECTOR_URL_NAME = "caties.section.header.detector.url.name";

    public static final String SECTION_CHUNKER_URL_NAME = "caties.section.chunker.url.name";

    public static final String CONCEPT_FILTER_URL_NAME = "caties.concept.filter.url.name";

    public static final String NEG_EX_URL_NAME = "caties.neg.ex.url.name";

    public static final String CONCEPT_CATEGORIZER_URL_NAME = "caties.concept.categorizer.url.name";

    public long SLEEP_SIZE = 1000;

    public static final String name = CaTIES_TiesPipeController.class.getName();

    private int codeCount = 0;

    private long coderTotalTime = 0L;

    private long coderAverageTime = 0L;

    private long coderTotalBytes = 0L;

    private long coderMinimumBytes = Long.MAX_VALUE;

    private long coderMaximumBytes = Long.MIN_VALUE;

    private long minimumCoderTime = Long.MAX_VALUE;

    private long maximumCoderTime = 0L;

    private int minimumCodingTimeReportNumber = -1;

    private int maximumCodingTimeReportNumber = -1;

    private String applicationStatusSearchString = CaTIES_DocumentImpl.APPLICATION_STATUS_CODING;

    private String HIBERNATE_DIALECT;

    private String HIBERNATE_DRIVER;

    private String HIBERNATE_HBM2DDL;

    private String DATABASE_URL;

    private String DATABASE_PASSWORD;

    private String DATABASE_USERNAME;

    public static void main(String[] args) {
        if (args.length > 0 && args[0] != null) {
            PropertyConfigurator.configure(args[0]);
        } else ApplicationLauncher.doLog4JSetup();
        CaTIES_TiesPipeController controller = new CaTIES_TiesPipeController();
        new CaTIES_PropertyLoader();
        controller.configureFromSystemProperties();
        if (controller.initialize()) (new Thread(controller)).start();
    }

    public void configureFromSystemProperties() {
        String isGeneratingSectionsAsString = System.getProperty(IS_GENERATING_SECTIONS);
        this.setGeneratingSections((isGeneratingSectionsAsString == null) ? false : (new Boolean(isGeneratingSectionsAsString)).booleanValue());
        this.setGateHome((String) System.getProperties().get(CaTIES_Constants.PROPERTY_KEY_GATE_HOME));
        this.setCreoleUrlName((String) System.getProperties().get(CREOLE_URL_NAME));
        this.setCaseInsensitiveGazetteerUrlName((String) System.getProperties().get(CASE_INSENSITIVE_GAZETTEER_URL_NAME));
        this.setSectionHeaderDetectorUrlName((String) System.getProperties().get(SECTION_HEADER_DETECTOR_URL_NAME));
        this.setSectionChunkerUrlName((String) System.getProperties().get(SECTION_CHUNKER_URL_NAME));
        this.setConceptFilterUrlName((String) System.getProperties().get(CONCEPT_FILTER_URL_NAME));
        this.setNegExUrlName((String) System.getProperties().get(NEG_EX_URL_NAME));
        this.setConceptCategorizerUrlName((String) System.getProperties().get(CONCEPT_CATEGORIZER_URL_NAME));
        if (System.getProperty(CaTIES_Constants.PROPERTY_KEY_CODER_SLEEPSIZE) == null) this.SLEEP_SIZE = 500; else this.SLEEP_SIZE = Long.parseLong(System.getProperty("caties.coder.sleepsize").trim());
    }

    class ShutDownListener implements Runnable {

        public void run() {
            shutdown();
        }
    }

    public void shutdown() {
        logger.info("Shutting down...");
        publicDataSourceManager.destroy();
    }

    public boolean initialize() {
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new Thread(new ShutDownListener()));
        try {
            establishPublicDatabaseManager();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Could not connect to public database: " + e.getMessage());
            return false;
        }
        logger.debug("Established Public Data Store Manager.");
        establishCaTIESExporterPR();
        logger.debug("Established Exporter PR.");
        establishSectionManager();
        logger.debug("Established Section Manager.");
        establishTiesPipeForDirectAccess();
        logger.debug("Established Ties Pipe.");
        logger.debug("Established Gate Interface.");
        setCoderNumber(System.getProperty(CaTIES_Constants.PROPERTY_KEY_CODER_NO));
        setCoderTotal(System.getProperty(CaTIES_Constants.PROPERTY_KEY_CODER_TOTAL));
        if (!isDelegationTiesPipe()) {
            this.applicationStatusSearchString = CaTIES_DocumentImpl.APPLICATION_STATUS_CODING + "-" + this.coderNumber;
        }
        logger.info("Using the following parameters:");
        logger.info("   Main TIES Coder: " + isDelegationTiesPipe());
        logger.info("   Coder Number: " + getCoderNumber());
        logger.info("   Total # of Coders: " + getCoderTotal());
        logger.info("   Hibernate Database Update: " + HIBERNATE_HBM2DDL);
        logger.info("   Sleep Duration: " + (new Long(SLEEP_SIZE / 60000)).intValue() + " min.");
        logger.info("	Is generating sections:" + isGeneratingSections());
        logger.info("-------------------------");
        DecimalFormat format = new DecimalFormat();
        logger.info("Free Memory: " + format.format(Runtime.getRuntime().freeMemory()) + " Bytes");
        logger.info("Max Memory: " + format.format(Runtime.getRuntime().maxMemory()) + " Bytes");
        logger.info("Total Memory: " + format.format(Runtime.getRuntime().freeMemory()) + " Bytes");
        logger.info("-------------------------");
        return true;
    }

    private void establishCaTIESExporterPR() {
        this.exporterPR = new CaTIES_ExporterPR();
    }

    public boolean establishTiesPipeForDirectAccess() {
        try {
            this.tiesPipe = new CaTIES_TiesPipe();
            this.tiesPipe.setSectionManager(this.sectionMgr);
            this.tiesPipe.setGateHome(this.gateHome);
            this.tiesPipe.setCreoleUrlName(this.creoleUrlName);
            this.tiesPipe.setCaseInsensitiveGazetteerUrlName(this.caseInsensitiveGazetteerUrlName);
            this.tiesPipe.setSectionHeaderDetectorUrlName(this.sectionHeaderDetectorUrlName);
            this.tiesPipe.setSectionChunkerUrlName(this.sectionChunkerUrlName);
            this.tiesPipe.setConceptFilterUrlName(this.conceptFilterUrlName);
            this.tiesPipe.setNegExUrlName(this.negExUrlName);
            this.tiesPipe.setConceptCategorizerUrlName(this.conceptCategorizerUrlName);
            return true;
        } catch (Exception x) {
            CaTIES_ExceptionLogger.logException(logger, x);
            logger.fatal(x.getMessage());
            return false;
        }
    }

    private void establishPublicDatabaseManager() throws Exception {
        HIBERNATE_DIALECT = System.getProperty(CaTIES_Constants.PROPERTY_KEY_HIBERNATE_DIALECT);
        HIBERNATE_DRIVER = System.getProperty(CaTIES_Constants.PROPERTY_KEY_HIBERNATE_DRIVER);
        HIBERNATE_HBM2DDL = System.getProperty(CaTIES_Constants.PROPERTY_KEY_HIBERNATE_HBM2DDL);
        DATABASE_URL = System.getProperty(CaTIES_Constants.PROPERTY_KEY_PUBLIC_DATABASE_URL);
        DATABASE_USERNAME = System.getProperty(CaTIES_Constants.PROPERTY_KEY_PUBLIC_DATABASE_USERNAME);
        DATABASE_PASSWORD = System.getProperty(CaTIES_Constants.PROPERTY_KEY_PUBLIC_DATABASE_PASSWORD);
        Configuration config = CaTIES_DataSourceManager.buildConfiguration(HIBERNATE_DIALECT, HIBERNATE_DRIVER, DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD, HIBERNATE_HBM2DDL, CaTIES_DataSourceManager.MODE.CLIENT);
        this.publicDataSourceManager = new CaTIES_DataSourceManager(MODE.CLIENT);
        if (!this.publicDataSourceManager.initialize(config)) throw new Exception("Could not initialize database connection");
    }

    private void establishSectionManager() {
        try {
            this.sectionMgr = new CaTIES_SectionManager();
            this.sectionMgr.setSessionPub(this.publicDataSourceManager.getSession());
            this.sectionMgr.setCurrentSession(this.sectionMgr.getSessionPub());
            String configName = System.getProperty(CaTIES_Constants.PROPERTY_KEY_SECTION_HEADER_CFG_PROPERTY_NAME);
            logger.debug("Prioritized section headers file is " + configName);
            URL url = this.getClass().getResource("/" + configName);
            this.sectionMgr.setSectionHeaderBootstrapFile(GeneralUtilities.urlToFile(url));
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private void openPublicSession() {
        try {
            this.publicSession = this.publicDataSourceManager.getSession();
            this.publicSession.beginTransaction();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private void openPublicSessionWithoutTransaction() {
        try {
            this.publicSession = this.publicDataSourceManager.getSession();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private void closePublicSession() {
        try {
            this.publicSession = null;
            this.publicDataSourceManager.closeSession();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void run() {
        while (isRunning) {
            Thread.yield();
            try {
                process();
            } catch (Exception x) {
                CaTIES_ExceptionLogger.logException(logger, x);
                logger.fatal(x.getMessage());
            }
        }
    }

    private synchronized void process() throws Exception {
        openPublicSessionWithoutTransaction();
        fetchUnProcessedDocument();
        if (this.currentDocumentToCode == null) {
            logger.info("No more reports found. Going to sleep...");
            closePublicSession();
            pause(this.SLEEP_SIZE);
            return;
        }
        if (isDelegationTiesPipe()) {
            if (this.currentCoderNumber != this.coderNumber) {
                delegateDocument();
            } else {
                processDocument();
            }
            this.currentCoderNumber++;
            if (this.currentCoderNumber == this.coderTotal) {
                this.currentCoderNumber = 0;
            }
        } else {
            processDocument();
        }
        closePublicSession();
    }

    int reportCount = 0;

    private void delegateDocument() {
        if (this.currentDocumentToCode == null) {
            ;
        } else {
            this.currentDocumentToCode.setApplicationStatus(CaTIES_DocumentImpl.APPLICATION_STATUS_CODING + "-" + this.currentCoderNumber);
            this.publicSession.saveOrUpdate(this.currentDocumentToCode);
            commitChangesToDatabaseWithTransaction();
            reportCount++;
            logger.info("Delegated Report# " + reportCount + " to Coder " + currentCoderNumber);
        }
    }

    private void processDocument() {
        if (this.currentDocumentToCode == null) {
            ;
        } else {
            this.startTime = new Date();
            this.minOID = this.currentDocumentToCode.getUuid();
            this.currentReportText = this.currentDocumentToCode.getDocumentData().getDocumentText();
            try {
                this.tiesResponse = executeTiesPipeDirect();
                if (this.tiesResponse.equals(CaTIES_TiesPipe.ERROR_FAILED_GATE_PIPE)) {
                    logger.fatal("Could not initialize GATE.");
                    setIsRunning(false);
                } else {
                    disassembleTiesResponse();
                    extractCodesFromChirps();
                    if (isGeneratingSections()) {
                        deleteFromCurrentDocument();
                    }
                    updateReportDirect();
                    codeCount++;
                    reportCount++;
                    if (codeCount > 1) {
                        logEvent();
                    } else {
                        logger.info("Coded Report# " + reportCount);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Error coding document. UUID: " + currentDocumentToCode.getUuid() + " Error:" + e.getMessage());
                this.currentDocumentToCode.setApplicationStatus(CaTIES_DocumentImpl.APPLICATION_STATUS_CODINGERROR);
                this.publicSession.saveOrUpdate(this.currentDocumentToCode);
                commitChangesToDatabaseWithTransaction();
            }
        }
    }

    public void logEvent() {
        this.endTime = new Date();
        long codingTime = this.endTime.getTime() - this.startTime.getTime();
        this.coderTotalTime += codingTime;
        this.coderAverageTime = this.coderTotalTime / codeCount;
        if (codingTime > this.maximumCoderTime) {
            this.maximumCoderTime = codingTime;
            this.maximumCodingTimeReportNumber = codeCount;
        }
        if (codingTime < this.minimumCoderTime) {
            this.minimumCoderTime = codingTime;
            this.minimumCodingTimeReportNumber = codeCount;
        }
        long reportBytes = this.currentDocumentToCode.getDocumentData().getDocumentText().length();
        if (reportBytes < this.coderMinimumBytes) {
            this.coderMinimumBytes = reportBytes;
        }
        if (reportBytes > this.coderMaximumBytes) {
            this.coderMaximumBytes = reportBytes;
        }
        this.coderTotalBytes += reportBytes;
        logger.info("Coded Report# " + reportCount + "\t" + this.currentDocumentToCode.getUuid() + "\tavg: " + this.coderAverageTime + "\ttot: " + this.coderTotalTime + "\tmin #: " + this.minimumCodingTimeReportNumber + "\tmin: " + this.minimumCoderTime + "\tmax #: " + this.maximumCodingTimeReportNumber + "\tmax: " + this.maximumCoderTime);
    }

    private boolean isDelegationTiesPipe() {
        return this.coderNumber == 0;
    }

    @SuppressWarnings("unused")
    private void fetchUnProcessedDocumentModulus() {
        this.currentDocumentToCode = null;
        Transaction tx = null;
        try {
            tx = this.publicSession.beginTransaction();
            String hqlQueryString = "";
            hqlQueryString += "from ";
            hqlQueryString += "   DocumentImpl p ";
            hqlQueryString += "where ";
            hqlQueryString += "   p.applicationStatus = 'CODING' and ";
            hqlQueryString += "   mod ( p.id,   " + this.coderTotal + ") = " + this.coderNumber;
            this.currentDocumentToCode = (DocumentImpl) this.publicSession.createQuery(hqlQueryString).setMaxResults(2).uniqueResult();
            tx.commit();
        } catch (Exception x) {
            x.printStackTrace();
            if (tx != null) {
                try {
                    tx.commit();
                } catch (Exception xx) {
                }
            }
        }
    }

    private void fetchUnProcessedDocument() {
        this.currentDocumentToCode = null;
        this.currentDocumentToCode = (DocumentImpl) this.publicSession.createCriteria(DocumentImpl.class).add(Restrictions.eq("applicationStatus", this.applicationStatusSearchString)).setMaxResults(1).uniqueResult();
    }

    private String executeTiesPipeDirect() throws Exception {
        return executeTIESPipe(this.currentReportText);
    }

    public String executeTIESPipe(String text) throws Exception {
        org.jdom.Document requestDocument = new org.jdom.Document(new Element("Report"));
        requestDocument.getRootElement().setAttribute("name", this.minOID);
        CDATA cdata = new CDATA(text);
        Element bodyElement = new Element("Body");
        bodyElement.addContent(cdata);
        requestDocument.getRootElement().addContent(bodyElement);
        String requestAsString = CaTIES_JDomUtils.convertDocumentToString(requestDocument, null);
        String tiesResponse = this.tiesPipe.processMessage(requestAsString);
        logger.debug("Got ties response of length " + tiesResponse.length());
        return tiesResponse;
    }

    private void updateReportDirect() {
        try {
            String elapsedTimeAsString = CaTIES_DateTimeUtils.getElapsedTime(new Timestamp(this.startTime.getTime()), new Timestamp((new Date()).getTime()));
            logger.debug("Processed " + this.currentDocumentToCode.getUuid() + " in " + elapsedTimeAsString);
            if (this.conceptCodeSet == null || this.conceptCodeSet.trim().length() == 0) this.conceptCodeSet = null;
            this.currentDocumentToCode.getDocumentData().setConceptCodeSet(this.conceptCodeSet);
            if (this.gateXML == null || this.gateXML.trim().length() == 0) {
                this.gateXML = null;
                this.currentDocumentToCode.getDocumentData().setDocumentBinary(null);
            } else this.currentDocumentToCode.getDocumentData().setDocumentBinary(CaBIG_LobUtilities.encodeLob(this.gateXML));
            if (isGeneratingSections()) {
                for (SectionImpl section : this.currentSections) {
                    this.currentDocumentToCode.addSection(section);
                    this.sectionMgr.setSessionPub(this.publicSession);
                    this.sectionMgr.setCurrentSession(this.sectionMgr.getSessionPub());
                    this.sectionMgr.registerSectionTypeForReportSection(this.currentReportText, section);
                    SectionTypeImpl sectionType = this.sectionMgr.getCurrentSectionType();
                    section.setSectionType(sectionType);
                    if (!sectionType.getIsHistogrammed()) {
                        section.setDocumentFragment(null);
                    }
                }
            }
            this.currentDocumentToCode.setApplicationStatus(CaTIES_DocumentImpl.APPLICATION_STATUS_SEQUENCING);
            this.currentDocumentToCode.setLastCoded(new Date());
            this.publicSession.saveOrUpdate(this.currentDocumentToCode);
            commitChangesToDatabaseWithTransaction();
        } catch (StaleObjectStateException staleException) {
            ;
        } catch (Exception x) {
            x.printStackTrace();
            logger.error("Error saving coded report. UUID: " + currentDocumentToCode.getUuid() + ". Setting status to " + CaTIES_DocumentImpl.APPLICATION_STATUS_SAVEERROR);
            this.currentDocumentToCode.setApplicationStatus(CaTIES_DocumentImpl.APPLICATION_STATUS_SAVEERROR);
            this.publicSession.saveOrUpdate(this.currentDocumentToCode);
        }
    }

    /**
	 * Remove existing concept referents related to current pathology report.
	 */
    private void deleteFromCurrentDocument() {
        try {
            final ArrayList<SectionImpl> sectionsToDelete = new ArrayList<SectionImpl>();
            for (Object sectionObj : this.currentDocumentToCode.getSectionCollection()) {
                SectionImpl reportSection = (SectionImpl) sectionObj;
                SectionTypeImpl sectionType = reportSection.getSectionType();
                if (sectionType != null) {
                    this.sectionMgr.setSessionPub(this.publicSession);
                    this.sectionMgr.setCurrentSession(this.sectionMgr.getSessionPub());
                    this.sectionMgr.unRegisterSectionTypeForReportSection(reportSection);
                }
                sectionsToDelete.add(reportSection);
            }
            for (SectionImpl reportSection : sectionsToDelete) {
                Transaction tx = this.publicSession.beginTransaction();
                this.currentDocumentToCode.removeSection(reportSection);
                this.publicSession.saveOrUpdate(reportSection);
                this.publicSession.delete(reportSection);
                tx.commit();
            }
        } catch (StaleObjectStateException staleException) {
            ;
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void disassembleTiesResponse() throws Exception {
        try {
            SAXBuilder builder = new SAXBuilder();
            byte[] byteArray = this.tiesResponse.getBytes();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            org.jdom.Document responseDocument = builder.build(byteArrayInputStream);
            Element responseRootElement = responseDocument.getRootElement();
            Element reportCodesElement = responseRootElement.getChild("ReportCodes");
            CaTIES_ConceptAccumulator reportCodesAccumulator = new CaTIES_ConceptAccumulator();
            reportCodesAccumulator.xmlDeSerialize(reportCodesElement);
            this.conceptCodeSet = reportCodesAccumulator.toString();
            if (isGeneratingSections()) {
                this.currentSections = new ArrayList<SectionImpl>();
                Collection<Element> sectionElements = responseRootElement.getChildren("Section");
                for (Iterator<Element> sectionIterator = sectionElements.iterator(); sectionIterator.hasNext(); ) {
                    Element sectionElement = (Element) sectionIterator.next();
                    String sectionName = sectionElement.getAttributeValue("name");
                    if (sectionName != null && isNonNullAlpha(sectionName)) {
                        if (sectionName.startsWith("[") && sectionName.endsWith("]") && sectionName.length() > 2) {
                            sectionName = sectionName.substring(1, sectionName.length() - 1);
                        }
                        Element startPositionElement = sectionElement.getChild("SectionStartPosition");
                        Element endPositionElement = sectionElement.getChild("SectionEndPosition");
                        if (startPositionElement != null && isNonNullNumeric(startPositionElement.getText()) && endPositionElement != null && isNonNullNumeric(endPositionElement.getText())) {
                            SectionImpl sectionToAdd = new SectionImpl();
                            sectionToAdd.setName(sectionName);
                            Element documentFragmentElement = sectionElement.getChild("DocumentFragment");
                            String documentFragment = documentFragmentElement.getText();
                            sectionToAdd.setDocumentFragment(documentFragment);
                            if (sectionToAdd.getDocumentFragment() != null && sectionToAdd.getDocumentFragment().trim().length() == 0) sectionToAdd.setDocumentFragment(null);
                            CaTIES_ConceptAccumulator accumulator = new CaTIES_ConceptAccumulator();
                            accumulator.xmlDeSerialize(sectionElement.getChild("ConceptCodeSet"));
                            sectionToAdd.setConceptCodeSet(accumulator.toString());
                            if (sectionToAdd.getConceptCodeSet() != null && sectionToAdd.getConceptCodeSet().trim().length() == 0) sectionToAdd.setConceptCodeSet(null);
                            Long sPos = Long.valueOf(startPositionElement.getText());
                            sectionToAdd.setStartOffset(sPos);
                            Long ePos = Long.valueOf(endPositionElement.getText());
                            sectionToAdd.setEndOffset(ePos);
                            this.currentSections.add(sectionToAdd);
                            logger.debug("Successfully added a ReportSectionImpl record for Section ==> " + sectionName);
                        } else {
                            logger.warn("Failed to get positions for Section ==> " + sectionName);
                        }
                    }
                }
            }
            Element gateXMLElement = responseRootElement.getChild("GateXML");
            gateXMLElement = gateXMLElement.getChild("GateDocument");
            Element chirpsXMLElement = (Element) responseRootElement.getChild("ChirpsXML");
            chirpsXMLElement = chirpsXMLElement.getChild("Envelope");
            Document gateXMLDocument = new Document((Element) gateXMLElement.clone());
            Document chirpsXMLDocument = new Document((Element) chirpsXMLElement.clone());
            this.gateXML = CaTIES_JDomUtils.convertDocumentToString(gateXMLDocument, null);
            this.chirpsXML = CaTIES_JDomUtils.convertDocumentToString(chirpsXMLDocument, null);
            logger.debug("Got gateXML of length " + this.gateXML.length());
            logger.debug("Got chirpsXML of length " + this.chirpsXML.length());
        } catch (Exception x) {
            this.gateXML = "";
            this.chirpsXML = "";
            x.printStackTrace();
            throw new Exception("Failed to parse the pay load XML:" + x.getMessage());
        }
    }

    private boolean isNonNullAlpha(String src) {
        return src != null && src.length() > 0 && !src.matches("^[\\W]*$") && !src.equalsIgnoreCase("null");
    }

    private boolean isNonNullNumeric(String src) {
        return src != null && src.length() > 0 && src.matches("^[0-9]+$");
    }

    private void extractCodesFromChirps() {
        this.exporterPR.setCHIRPsDocument(chirpsXML);
        this.exporterPR.execute();
        this.theCodes = this.exporterPR.getCodesAsString();
        logger.debug("Got codes of length " + this.theCodes.length());
    }

    private void pause(long milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException ie) {
            this.isRunning = false;
        }
    }

    public long getNumberReportsProcessed() {
        return this.numberReportsProcessed;
    }

    public void setGateHome(String gateHome) {
        this.gateHome = gateHome;
    }

    public void setCreoleUrlName(String creoleUrlName) {
        this.creoleUrlName = creoleUrlName;
    }

    public void setCaseInsensitiveGazetteerUrlName(String caseInsensitiveGazetteerUrlName) {
        this.caseInsensitiveGazetteerUrlName = caseInsensitiveGazetteerUrlName;
    }

    public void setSectionHeaderDetectorUrlName(String sectionHeaderDetectorUrlName) {
        this.sectionHeaderDetectorUrlName = sectionHeaderDetectorUrlName;
    }

    public void setSectionChunkerUrlName(String sectionChunkerUrlName) {
        this.sectionChunkerUrlName = sectionChunkerUrlName;
    }

    public void setConceptFilterUrlName(String conceptFilterUrlName) {
        this.conceptFilterUrlName = conceptFilterUrlName;
    }

    public void setNegExUrlName(String negExUrlName) {
        this.negExUrlName = negExUrlName;
    }

    public void setConceptCategorizerUrlName(String conceptCategorizerUrlName) {
        this.conceptCategorizerUrlName = conceptCategorizerUrlName;
    }

    public synchronized void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public String getCoderNumber() {
        return this.coderNumber + "";
    }

    public void setCoderNumber(String coderNumber) {
        if (coderNumber != null && coderNumber.length() > 0) {
            this.coderNumber = Integer.parseInt(coderNumber);
        }
    }

    public String getCoderTotal() {
        return this.coderTotal + "";
    }

    public void setCoderTotal(String coderTotal) {
        if (coderTotal != null && coderTotal.length() > 0) {
            this.coderTotal = Integer.parseInt(coderTotal);
        }
    }

    public boolean isGeneratingSections() {
        return isGeneratingSections;
    }

    public void setGeneratingSections(boolean isGeneratingSections) {
        this.isGeneratingSections = isGeneratingSections;
    }

    public void commitChangesToDatabase() {
        try {
            publicSession.flush();
            Transaction tx = publicSession.beginTransaction();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            logger.fatal("Error occured when trying to commit changed to database. Halting...");
            System.exit(0);
        }
        publicSession.beginTransaction();
    }

    public void commitChangesToDatabaseWithTransaction() {
        try {
            Transaction tx = publicSession.beginTransaction();
            publicSession.flush();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            logger.fatal("Error occured when trying to commit changed to database. Halting...");
            System.exit(0);
        }
    }
}
