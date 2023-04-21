package edu.upmc.opi.caBIG.caTIES.server;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.xpath.XPath;
import edu.upmc.opi.caBIG.caTIES.client.deid.CaTIES_DeIDClient;
import edu.upmc.opi.caBIG.caTIES.client.dispatcher.CaTIES_Dispatcher;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_DateTimeUtils;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_GlobusUtils;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_JDomUtils;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_PropertyLoader;
import edu.upmc.opi.caBIG.caTIES.server.dispatcher.DeID;
import edu.upmc.opi.caBIG.caTIES.services.dispatcher.client.DispatcherClient;
import edu.upmc.opi.caBIG.common.CaBIG_UUIdGenerator;

/**
 * Populate caTIES data stores from existing archive.
 * 
 * @author mitchellkj@upmc.edu
 * @version $Id: CaTIES_ParaffinAcquisitionLoader.java,v 1.2 2006/06/30 19:30:01
 * 1upmc-acct\mitchellkj Exp $
 * @since 1.4.2_04
 */
class CaTIES_ParaffinAcquisitionLoader {

    /**
     * Field logger.
     */
    private static Logger logger = Logger.getLogger(CaTIES_ParaffinAcquisitionLoader.class);

    /**
     * Field sqlServer.
     */
    private Connection sqlServer = null;

    /**
     * Field mySqlPublic.
     */
    private Connection mySqlPublic = null;

    /**
     * Field mySqlPrivate.
     */
    private Connection mySqlPrivate = null;

    /**
     * Field mySqlPublicStmt.
     */
    private PreparedStatement mySqlPublicStmt = null;

    /**
     * Field mySqlPrivateStmt.
     */
    private PreparedStatement mySqlPrivateStmt = null;

    /**
     * Field query.
     */
    private String query = null;

    /**
     * Field organizationID.
     */
    private String organizationID = null;

    /**
     * Field deIDAccessMode.
     */
    private String deIDAccessMode = null;

    /**
     * Field deIDServiceHandleAsString.
     */
    private String deIDServiceHandleAsString = null;

    /**
     * Field srcDriver.
     */
    private String mySqlDriver = null;

    /**
     * Field pennTableName.
     */
    private String pennTableName = null;

    /**
     * Field publicDB.
     */
    private String publicDB = null;

    /**
     * Field publicUserImpl.
     */
    private String publicUserImpl = null;

    /**
     * Field publicPwd.
     */
    private String publicPwd = null;

    /**
     * Field privateDB.
     */
    private String privateDB = null;

    /**
     * Field privateUserImpl.
     */
    private String privateUserImpl = null;

    /**
     * Field privatePwd.
     */
    private String privatePwd = null;

    /**
     * Field organizationName.
     */
    private String organizationName = null;

    /**
     * Field currentRequestDocument.
     */
    private Document currentRequestDocument = null;

    /**
     * Field numberOfReportsProcessed.
     */
    private int numberOfReportsProcessed = 0;

    /**
     * Field batchSize.
     */
    private int batchSize = 0;

    /**
     * Field maxWaitTime.
     */
    private long maxWaitTime = 120000L;

    /**
     * Field currentResultSet.
     */
    private ResultSet currentResultSet = null;

    /**
     * Field patientName.
     */
    private String patientName = null;

    /**
     * Field clientNumber.
     */
    private String clientNumber = null;

    /**
     * Field patientMRN.
     */
    private String patientMRN = null;

    /**
     * Field dob.
     */
    private java.sql.Date dob = null;

    /**
     * Field gender.
     */
    private String gender = null;

    /**
     * Field race.
     */
    private String race = null;

    /**
     * Field accessionNum.
     */
    private String accessionNum = null;

    /**
     * Field orderDate.
     */
    private java.sql.Date orderDate = null;

    /**
     * Field collectedDate.
     */
    private java.sql.Date collectedDate = null;

    /**
     * Field sprText.
     */
    private String sprText = null;

    /**
     * Field deidSprText.
     */
    private String deidSprText = null;

    /**
     * Field publicPatientID.
     */
    private String publicPatientID = null;

    /**
     * Field privatePatientID.
     */
    private String privatePatientID = null;

    /**
     * Field publicSprID.
     */
    private String publicSprID = null;

    /**
     * Field privateSprID.
     */
    private String privateSprID = null;

    /**
     * Field gridFactory.
     */
    private DispatcherClient gridFactory = null;

    /**
     * Field deID.
     */
    private DeID deID = null;

    /**
     * The min key num.
     */
    private long minKeyNum;

    /**
     * The max key num.
     */
    private long maxKeyNum;

    /**
     * Field sectionHeadings.
     */
    private static final String[] sectionHeadings = { "8006603", "8000036", "8000046", "8000048", "8000056", "8000016", "8008005", "8008000", "8006613", "8001055", "8001057", "8001058", "8001065", "8006609", "8006631" };

    /**
     * Method main.
     * 
     * @param args String[]
     */
    public static void main(String args[]) {
        PropertyConfigurator.configure(args[0]);
        new CaTIES_PropertyLoader();
        new CaTIES_ParaffinAcquisitionLoader();
    }

    /**
     * Constructor for CaTIES_ParaffinAcquisitionLoader.
     */
    public CaTIES_ParaffinAcquisitionLoader() {
        try {
            this.deIDAccessMode = (String) System.getProperty("deid.access.mode");
            this.deIDServiceHandleAsString = (String) System.getProperty("deid.service.factory.handle");
            this.mySqlDriver = (String) System.getProperty("acquisition.dst.db.driver");
            this.publicDB = (String) System.getProperty("acquisition.dst.public.url");
            this.publicUserImpl = (String) System.getProperty("acquisition.dst.public.user");
            this.publicPwd = (String) System.getProperty("acquisition.dst.public.password");
            this.privateDB = (String) System.getProperty("acquisition.dst.private.url");
            this.privateUserImpl = (String) System.getProperty("acquisition.dst.private.user");
            this.privatePwd = (String) System.getProperty("acquisition.dst.private.password");
            this.organizationName = (String) System.getProperty("acquisition.organization");
            this.batchSize = Integer.parseInt(System.getProperty("acquisition.batch.size"));
            this.maxWaitTime = Long.parseLong(System.getProperty("acquisition.max.wait.time"));
            Class.forName(this.mySqlDriver);
            openPublicDbConnection();
            openPrivateDbConnection();
            if (deIDAccessMode.equals("service")) {
                openDeIDGridService();
            } else {
                openDeIDDirect();
            }
            process();
            if (deIDAccessMode.equals("service")) {
                closeDeIDGridService();
            } else {
                closeDeIDDirect();
            }
            closePrivateDbConnection();
            closePublicDbConnection();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Method process.
     */
    protected void process() {
        try {
            fetchOrganizationID();
            this.currentRequestDocument = new Document(new Element("Dataset"));
            Statement mySqlPrivateStmt = this.mySqlPrivate.createStatement();
            String query = "SELECT * FROM caties_spr ";
            this.currentResultSet = mySqlPrivateStmt.executeQuery(query);
            while (this.currentResultSet.next()) {
                cacheDemographics();
                String oid = this.currentResultSet.getString("oid");
                if (!alreadyExists(oid)) {
                    this.sprText = this.currentResultSet.getString("spr");
                    this.sprText = "CCL8000036\n\n" + this.sprText;
                    logger.debug(this.sprText);
                    Element reportElement = buildReportElement();
                    this.currentRequestDocument.getRootElement().addContent(reportElement);
                    this.numberOfReportsProcessed++;
                    if (this.numberOfReportsProcessed > this.batchSize) {
                        String request = CaTIES_JDomUtils.convertDocumentToString(this.currentRequestDocument, Format.getPrettyFormat());
                        logger.debug(request);
                        String response = requestDeIDService(request);
                        extractReport(response);
                        this.currentRequestDocument = new Document(new Element("Dataset"));
                        this.numberOfReportsProcessed = 0;
                    }
                }
                clearCache();
            }
            this.currentResultSet.close();
            mySqlPrivateStmt.close();
            if (this.numberOfReportsProcessed > 0) {
                String request = CaTIES_JDomUtils.convertDocumentToString(this.currentRequestDocument, Format.getPrettyFormat());
                logger.debug(request);
                String response = requestDeIDService(request);
                extractReport(response);
            }
        } catch (Exception e) {
            logger.fatal(e.getMessage());
        }
    }

    /**
     * Method requestDeIDService.
     * 
     * @param request String
     * 
     * @return String
     */
    protected String requestDeIDService(String request) {
        String response = null;
        try {
            if (deIDAccessMode.equals("service")) {
                response = requestDeIDGridService(request);
            } else {
                response = requestDeIDServiceDirect(request);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return response;
    }

    /**
     * Method cacheDemographics.
     */
    protected void cacheDemographics() {
        try {
            this.patientName = "unknown";
            this.clientNumber = "unknown";
            this.patientMRN = "unknown";
            this.dob = new java.sql.Date((new java.util.Date()).getTime());
            this.gender = "U";
            this.race = "U";
            this.accessionNum = this.currentResultSet.getString("ACCESSION_NUMBER");
            this.orderDate = new java.sql.Date((new java.util.Date()).getTime());
            this.collectedDate = new java.sql.Date((new java.util.Date()).getTime());
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method clearCache.
     */
    protected void clearCache() {
        try {
            this.patientName = null;
            this.clientNumber = null;
            this.patientMRN = null;
            this.dob = null;
            this.gender = null;
            this.race = null;
            this.accessionNum = null;
            this.orderDate = null;
            this.collectedDate = null;
            this.sprText = null;
            this.deidSprText = null;
            this.publicSprID = null;
            this.privateSprID = null;
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method buildReportFromSections.
     */
    protected void buildReportFromSections() {
        this.sprText = "";
        try {
            for (int idx = 0; idx < sectionHeadings.length; idx++) {
                String sectionHeader = sectionHeadings[idx];
                String sectionText = this.currentResultSet.getString(sectionHeader);
                if (sectionText != null && sectionText.trim().length() > 0) {
                    this.sprText += "CCL" + sectionHeader + "\n" + sectionText + "\n\n";
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method buildReportElement.
     * 
     * @return Element
     */
    protected Element buildReportElement() {
        Element reportElement = null;
        try {
            reportElement = new Element("Report");
            Element patientIdElement = new Element("Patient_ID");
            patientIdElement.addContent(this.patientMRN);
            Element reportTypeElement = new Element("Report_Type");
            reportTypeElement.addContent("SP");
            Element reportHeaderElement = new Element("Report_Header");
            reportHeaderElement.addContent(buildHeaderPersonElement("patient_name", this.patientName, "patient"));
            reportHeaderElement.addContent(buildHeaderPersonElement("clientnum", this.clientNumber, "patient"));
            reportHeaderElement.addContent(buildHeaderDateElement("date_of_birth", this.dob));
            reportHeaderElement.addContent(buildHeaderPersonElement("gender", this.gender, "patient"));
            reportHeaderElement.addContent(buildHeaderPersonElement("race", this.race, "patient"));
            reportHeaderElement.addContent(buildHeaderDataElement("accessionNum", this.accessionNum));
            reportHeaderElement.addContent(buildHeaderDateElement("order_date", this.orderDate));
            reportHeaderElement.addContent(buildHeaderDateElement("collected_date", this.collectedDate));
            Element reportTextElement = new Element("Report_Text");
            this.sprText = removeIllegalXmlCharacters(this.sprText);
            CDATA reportCDATA = new CDATA(this.sprText);
            reportTextElement.addContent(reportCDATA);
            reportElement.addContent(patientIdElement);
            reportElement.addContent(reportTypeElement);
            reportElement.addContent(reportHeaderElement);
            reportElement.addContent(reportTextElement);
        } catch (Exception x) {
            x.printStackTrace();
        }
        return reportElement;
    }

    /**
     * Method removeIllegalXmlCharacters.
     * 
     * @param sprText String
     * 
     * @return String
     */
    protected String removeIllegalXmlCharacters(String sprText) {
        String result = sprText;
        try {
            StringBuffer sb = new StringBuffer(sprText);
            for (int idx = 0; idx < sb.length(); idx++) {
                if (Character.isLetterOrDigit(sb.charAt(idx)) || Character.isWhitespace(sb.charAt(idx)) || Character.isISOControl(sb.charAt(idx)) || sb.charAt(idx) == '*' || sb.charAt(idx) == ':' || sb.charAt(idx) == '.' || sb.charAt(idx) == ',' || sb.charAt(idx) == ';') {
                    ;
                } else {
                    sb.setCharAt(idx, ' ');
                }
            }
            result = sb.toString();
        } catch (Exception x) {
            x.printStackTrace();
        }
        return result;
    }

    /**
     * Method buildHeaderPersonElement.
     * 
     * @param value String
     * @param role String
     * @param variable String
     * 
     * @return Element
     */
    protected Element buildHeaderPersonElement(String variable, String value, String role) {
        Element headerPersonElement = null;
        try {
            headerPersonElement = new Element("Header_Person");
            headerPersonElement.setAttribute("role", role);
            Element variableElement = new Element("Variable");
            Element valueElement = new Element("Value");
            variableElement.addContent(variable);
            valueElement.addContent(value);
            headerPersonElement.addContent(variableElement);
            headerPersonElement.addContent(valueElement);
        } catch (Exception x) {
            x.printStackTrace();
        }
        return headerPersonElement;
    }

    /**
     * Method buildHeaderDataElement.
     * 
     * @param value String
     * @param variable String
     * 
     * @return Element
     */
    protected Element buildHeaderDataElement(String variable, String value) {
        Element headerDataElement = null;
        try {
            headerDataElement = new Element("Header_Data");
            Element variableElement = new Element("Variable");
            Element valueElement = new Element("Value");
            variableElement.addContent(variable);
            valueElement.addContent(value);
            headerDataElement.addContent(variableElement);
            headerDataElement.addContent(valueElement);
        } catch (Exception x) {
            x.printStackTrace();
        }
        return headerDataElement;
    }

    /**
     * Method buildHeaderDateElement.
     * 
     * @param elementName String
     * @param elementDate java.sql.Date
     * 
     * @return Element
     */
    protected Element buildHeaderDateElement(String elementName, java.sql.Date elementDate) {
        Element headerDateElement = null;
        try {
            headerDateElement = new Element("Header_Date");
            Element variableElement = new Element("Variable");
            Element dateElement = new Element("Date");
            Element yearElement = new Element("Year");
            Element monthElement = new Element("Month");
            Element dayElement = new Element("Day");
            Element hoursElement = new Element("Hours");
            Element minutesElement = new Element("Minutes");
            variableElement.addContent(elementName);
            Calendar conversionCalendar = new GregorianCalendar();
            conversionCalendar.setTime(new java.util.Date(elementDate.getTime()));
            yearElement.addContent(conversionCalendar.get(Calendar.YEAR) + "");
            monthElement.addContent(conversionCalendar.get(Calendar.MONTH) + "");
            dayElement.addContent(conversionCalendar.get(Calendar.DAY_OF_MONTH) + "");
            hoursElement.addContent(conversionCalendar.get(Calendar.HOUR_OF_DAY) + "");
            minutesElement.addContent(conversionCalendar.get(Calendar.MINUTE) + "");
            dateElement.addContent(yearElement);
            dateElement.addContent(monthElement);
            dateElement.addContent(dayElement);
            dateElement.addContent(hoursElement);
            dateElement.addContent(minutesElement);
            headerDateElement.addContent(variableElement);
            headerDateElement.addContent(dateElement);
        } catch (Exception x) {
            x.printStackTrace();
        }
        return headerDateElement;
    }

    /**
     * Method requestDeIDServiceDirect.
     * 
     * @param request String
     * 
     * @return String
     */
    protected String requestDeIDServiceDirect(String request) {
        String response = "";
        try {
            response = this.deID.processCommand(request);
        } catch (Exception x) {
            x.printStackTrace();
        }
        return response;
    }

    /**
     * Method requestDeIDGridService.
     * 
     * @param request String
     * 
     * @return String
     */
    protected String requestDeIDGridService(String request) {
        String response = "";
        try {
            CaTIES_Dispatcher dispatcher = new CaTIES_Dispatcher();
            dispatcher.setDispatcherUriName(deIDServiceHandleAsString);
            dispatcher.setSecure(true);
            dispatcher.setCommandProcessorClassName(CaTIES_DeIDClient.class.getName());
            dispatcher.connect();
            response = dispatcher.processCommandDirectly(request);
            dispatcher.disconnect();
        } catch (Exception x) {
            x.printStackTrace();
        }
        return response;
    }

    /**
     * Method extractReport.
     * 
     * @param deIDResponse String
     */
    protected void extractReport(String deIDResponse) {
        try {
            if (deIDResponse != null && deIDResponse.trim().length() > 0) {
                SAXBuilder builder = new SAXBuilder();
                byte[] byteArray = deIDResponse.getBytes();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
                Document deIDResponseDocument = builder.build(byteArrayInputStream);
                logger.debug(CaTIES_JDomUtils.convertDocumentToString(deIDResponseDocument, Format.getPrettyFormat()));
                Element deIDResponseRootElement = deIDResponseDocument.getRootElement();
                XPath xpath = XPath.newInstance("//Report");
                List deIdResults = xpath.selectNodes(deIDResponseDocument);
                List phiResults = xpath.selectNodes(this.currentRequestDocument);
                Iterator deIdIterator = deIdResults.iterator();
                Iterator phiIterator = phiResults.iterator();
                while (deIdIterator.hasNext()) {
                    Element deIdReportElement = (Element) deIdIterator.next();
                    Element phiReportElement = (Element) phiIterator.next();
                    this.patientMRN = deIdReportElement.getChild("Patient_ID").getText();
                    Element header = phiReportElement.getChild("Report_Header");
                    this.patientName = extractVariableValue(header, "Header_Person", "patient_name");
                    this.clientNumber = extractVariableValue(header, "Header_Person", "clientnum");
                    this.dob = extractVariableDate(header, "Header_Date", "date_of_birth");
                    this.gender = extractVariableValue(header, "Header_Person", "gender");
                    this.race = extractVariableValue(header, "Header_Person", "race");
                    this.accessionNum = extractVariableValue(header, "Header_Data", "accessionNum");
                    this.orderDate = extractVariableDate(header, "Header_Date", "order_date");
                    this.collectedDate = extractVariableDate(header, "Header_Date", "collected_date");
                    this.deidSprText = deIdReportElement.getChild("Report_Text").getText();
                    writeToPublicSpr();
                    logger.info("Processed report " + this.accessionNum);
                }
            } else {
                logger.fatal("NO DeID response");
            }
        } catch (Exception x) {
            logger.fatal("Failed parsing response \n\n" + deIDResponse + "\n\n");
        }
    }

    /**
     * Method extractVariableValue.
     * 
     * @param element Element
     * @param variable String
     * @param headerName String
     * 
     * @return String
     */
    protected String extractVariableValue(Element element, String headerName, String variable) {
        String result = null;
        try {
            String xpathQuery = headerName + "[Variable=\"" + variable + "\"]/Value";
            logger.debug(xpathQuery);
            XPath xpath = XPath.newInstance(xpathQuery);
            Element valueElement = (Element) xpath.selectSingleNode(element);
            if (valueElement != null) {
                result = valueElement.getText();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return result;
    }

    /**
     * Method extractVariableDate.
     * 
     * @param element Element
     * @param variable String
     * @param headerName String
     * 
     * @return java.sql.Date
     */
    protected java.sql.Date extractVariableDate(Element element, String headerName, String variable) {
        java.sql.Date result = null;
        try {
            String xpathQuery = headerName + "[Variable=\"" + variable + "\"]/Date";
            logger.debug(xpathQuery);
            XPath xpath = XPath.newInstance(xpathQuery);
            Element dateElement = (Element) xpath.selectSingleNode(element);
            if (dateElement != null) {
                Calendar conversionCalendar = new GregorianCalendar();
                conversionCalendar.clear();
                conversionCalendar.set(Calendar.YEAR, Integer.parseInt(dateElement.getChild("Year").getText()));
                conversionCalendar.set(Calendar.MONTH, Integer.parseInt(dateElement.getChild("Month").getText()));
                conversionCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateElement.getChild("Day").getText()));
                conversionCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dateElement.getChild("Hours").getText()));
                conversionCalendar.set(Calendar.MINUTE, Integer.parseInt(dateElement.getChild("Minutes").getText()));
                result = new java.sql.Date(conversionCalendar.getTimeInMillis());
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return result;
    }

    /**
     * Method fetchRecordBoundaries.
     */
    protected void fetchRecordBoundaries() {
        try {
            Statement mySqlPrivateStmt = sqlServer.createStatement();
            query = "SELECT min(keynum) FROM " + this.pennTableName;
            ResultSet rs = mySqlPrivateStmt.executeQuery(query);
            while (rs.next()) {
                this.minKeyNum = rs.getLong(1);
            }
            rs.close();
            mySqlPrivateStmt.close();
            mySqlPrivateStmt = sqlServer.createStatement();
            query = "SELECT max(keynum) FROM " + this.pennTableName;
            rs = mySqlPrivateStmt.executeQuery(query);
            while (rs.next()) {
                this.maxKeyNum = rs.getLong(1);
            }
            rs.close();
            mySqlPrivateStmt.close();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method fetchOrganizationID.
     */
    protected void fetchOrganizationID() {
        try {
            this.query = "select * from caties_organization where NAME=\"" + this.organizationName + "\"";
            Statement mySqlPublicStmt = mySqlPublic.createStatement();
            ResultSet organizationIDs = mySqlPublicStmt.executeQuery(query);
            organizationIDs.next();
            this.organizationID = organizationIDs.getString("OID");
            organizationIDs.close();
            mySqlPublicStmt.close();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method fetchPatientIDs.
     */
    protected void fetchPatientIDs() {
        try {
            this.query = "select OID, DEIDENTIFIED_ID " + "from catiesprivate.caties_patient " + "where MRN = '" + this.patientMRN + "'";
            Statement mySqlPrivateStmt = mySqlPrivate.createStatement();
            ResultSet patientResultSet = mySqlPrivateStmt.executeQuery(query);
            if (patientResultSet != null && patientResultSet.next()) {
                this.privatePatientID = patientResultSet.getString(1);
                this.publicPatientID = patientResultSet.getString(2);
            } else {
                this.privatePatientID = CaBIG_UUIdGenerator.getUUID();
                this.publicPatientID = CaBIG_UUIdGenerator.getUUID();
                writeToPrivatePatient();
            }
            mySqlPrivateStmt.close();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method alreadyExists.
     * 
     * @param oid the oid
     * 
     * @return boolean
     */
    protected boolean alreadyExists(String oid) {
        boolean result = true;
        try {
            this.query = "select count(*) NUM_RECORDS " + "from caties_spr " + "where OID = '" + oid + "'";
            Statement mySqlPublicStmt = mySqlPublic.createStatement();
            ResultSet sprResultSet = mySqlPublicStmt.executeQuery(query);
            sprResultSet.next();
            long numberOfRecords = sprResultSet.getInt(1);
            if (numberOfRecords == 0) {
                result = false;
            }
            mySqlPublicStmt.close();
        } catch (Exception x) {
            x.printStackTrace();
        }
        return result;
    }

    /**
     * Method writeToPublicSpr.
     */
    protected void writeToPublicSpr() {
        try {
            this.publicSprID = CaBIG_UUIdGenerator.getUUID();
            int age = CaTIES_DateTimeUtils.computeAgeAtAcquisition(new java.util.Date(this.collectedDate.getTime()), new java.util.Date(this.dob.getTime()));
            this.query = "insert into catiespublicparaffin.caties_spr (";
            this.query += "OID, " + "ORGANIZATION_ID, " + "PATIENT_ID, " + "AGE, " + "RACE, " + "SEX, " + "ACCESSION_NUMBER, " + "SPR, " + "DEID_VERSION, " + "DEID_DATE, " + "CODER_VERSION) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            logger.debug(this.query);
            this.mySqlPublicStmt = mySqlPublic.prepareStatement(this.query);
            this.mySqlPublicStmt.setString(1, this.publicSprID);
            this.mySqlPublicStmt.setString(2, this.organizationID);
            this.mySqlPublicStmt.setString(3, this.publicPatientID);
            this.mySqlPublicStmt.setInt(4, age);
            this.mySqlPublicStmt.setString(5, this.race);
            this.mySqlPublicStmt.setString(6, this.gender);
            this.mySqlPublicStmt.setString(7, this.publicSprID);
            this.mySqlPublicStmt.setString(8, this.deidSprText);
            this.mySqlPublicStmt.setString(9, "DeID 6");
            this.mySqlPublicStmt.setString(10, "CURRENT_TIMESTAMP()");
            this.mySqlPublicStmt.setString(11, "PENDING");
            this.mySqlPublicStmt.executeUpdate();
            this.mySqlPublicStmt.close();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method writeToPrivateSpr.
     */
    protected void writeToPrivateSpr() {
        try {
            this.privateSprID = CaBIG_UUIdGenerator.getUUID();
            this.query = "insert into catiesprivate.caties_spr (";
            this.query += "OID, " + "DEIDENTIFIED_ID, " + "PATIENT_ID, " + "ORGANIZATION_ID, " + "SPR, " + "ACCESSION_NUMBER, " + "AQUISITION_DATE, " + "REPORT_DATE) values (?, ?, ?, ?, ?, ?, ?, ?)";
            logger.debug(this.query);
            this.mySqlPrivateStmt = this.mySqlPrivate.prepareStatement(this.query);
            this.mySqlPrivateStmt.setString(1, this.privateSprID);
            this.mySqlPrivateStmt.setString(2, this.publicSprID);
            this.mySqlPrivateStmt.setString(3, this.privatePatientID);
            this.mySqlPrivateStmt.setString(4, this.organizationID);
            this.mySqlPrivateStmt.setString(5, this.sprText);
            this.mySqlPrivateStmt.setString(6, this.accessionNum);
            this.mySqlPrivateStmt.setDate(7, this.collectedDate);
            this.mySqlPrivateStmt.setDate(8, this.orderDate);
            this.mySqlPrivateStmt.executeUpdate();
            this.mySqlPrivateStmt.close();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method writeToPrivatePatient.
     */
    protected void writeToPrivatePatient() {
        try {
            this.query = "insert into catiesprivate.caties_patient ";
            this.query += "(" + "OID, " + "ORGANIZATION_ID, " + "DEIDENTIFIED_ID, " + "LNAME, " + "MRN, " + "DOB, " + "RACE, " + "SEX) values (?, ?, ?, ?, ?, ?, ?, ?)";
            logger.debug(this.query);
            this.mySqlPrivateStmt = this.mySqlPrivate.prepareStatement(this.query);
            this.mySqlPrivateStmt.setString(1, this.privatePatientID);
            this.mySqlPrivateStmt.setString(2, this.organizationID);
            this.mySqlPrivateStmt.setString(3, this.publicPatientID);
            this.mySqlPrivateStmt.setString(4, this.patientName);
            this.mySqlPrivateStmt.setString(5, this.patientMRN);
            this.mySqlPrivateStmt.setDate(6, this.dob);
            this.mySqlPrivateStmt.setString(7, this.race);
            this.mySqlPrivateStmt.setString(8, this.gender);
            this.mySqlPrivateStmt.executeUpdate();
            this.mySqlPrivateStmt.close();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method openPublicDbConnection.
     */
    protected void openPublicDbConnection() {
        try {
            this.mySqlPublic = DriverManager.getConnection(this.publicDB, this.publicUserImpl, this.publicPwd);
            this.mySqlPublic.setAutoCommit(true);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method closePublicDbConnection.
     */
    protected void closePublicDbConnection() {
        try {
            this.mySqlPublic.close();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method openPrivateDbConnection.
     */
    protected void openPrivateDbConnection() {
        try {
            this.mySqlPrivate = DriverManager.getConnection(this.privateDB, this.privateUserImpl, this.privatePwd);
            this.mySqlPrivate.setAutoCommit(true);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method closePrivateDbConnection.
     */
    protected void closePrivateDbConnection() {
        try {
            this.mySqlPrivate.close();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method openDeIDDirect.
     */
    protected void openDeIDDirect() {
        try {
            this.deID = new DeID();
            this.deID.setDeidHome(System.getProperty("deid.home"));
            this.deID.setDeidTempDirectory(System.getProperty("deid.temp.directory"));
            this.deID.setMaxWaitTime(this.maxWaitTime);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method closeDeIDDirect.
     */
    protected void closeDeIDDirect() {
        ;
    }

    /**
     * Method openDeIDGridService.
     */
    protected void openDeIDGridService() {
        try {
            URL handle = new URL(this.deIDServiceHandleAsString);
            this.gridFactory = CaTIES_GlobusUtils.getDispatcherFromHandle(" ");
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method closeDeIDGridService.
     */
    protected void closeDeIDGridService() {
        try {
            this.gridFactory = null;
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
