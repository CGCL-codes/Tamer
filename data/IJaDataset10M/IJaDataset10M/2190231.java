package edu.harvard.iq.safe.saasystem.auditschema;

import java.math.BigInteger;
import java.util.logging.*;
import javax.xml.parsers.*;
import java.io.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.util.*;

/**
 *
 * @author Akio Sone
 */
public class SAASAuditSchemaDOMParser {

    private static Logger logger = Logger.getLogger(SAASAuditSchemaDOMParser.class.getName());

    static String placeholder = null;

    static int[] scalarItems = { 0, 1, 2, 3, 14, 15, 17, 18, 19, 20, 23 };

    static String[] xpathList = { "/SSP/audit/@audit_id", "/SSP/audit/schemaVersion/text()", "/SSP/audit/auditReportEmail/text()", "/SSP/audit/auditReportInterval/@maxDays", "/SSP/hosts/host/hostIdentity/name/text()", "/SSP/hosts/host/@host_id", "/SSP/hosts/host/hostCapabilities/storageAvailable/@max_size", "/SSP/archivalUnits/au/auIdentity/name/text()", "/SSP/archivalUnits/au/@au_id", "/SSP/archivalUnits/au/auCapabilities/updateFrequency/@minDays", "/SSP/archivalUnits/au/auCapabilities/storageRequired/@max_size", "/SSP/archivalUnits/au/auCapabilities/numberReplicates/@min", "/SSP/archivalUnits/au/auCapabilities/replicationDuration/@maxDays", "/SSP/archivalUnits/au/auCapabilities/verificationFrequency/@maxDays", "/SSP/network/@network_id", "/SSP/network/networkIdentity/accessBase/@adminEmail", "/SSP/hosts/host/hostIdentity/geographicLocation/text()", "/SSP/network/networkIdentity/geographicCoding/text()", "/SSP/audit/geographicSummaryScheme/text()", "/SSP/audit/subjectSummaryScheme/text()", "/SSP/audit/ownerInstSummaryScheme/text()", "/SSP/archivalUnits/au/auIdentity/subject/text()", "/SSP/archivalUnits/au/auIdentity/ownerInstitution/text()", "/SSP/audit/geographicSummaryScheme/@maxRegions", "/SSP/archivalUnits/au/auCapabilities/numberReplicates/@regions" };

    /**
     *
     * @param name
     * @param encoding
     * @throws FileNotFoundException
     */
    public AuditSchemaInstance read(String name, String encoding) throws FileNotFoundException {
        return read(new FileInputStream(name), encoding);
    }

    /**
     *
     * @param name
     * @throws FileNotFoundException
     */
    public AuditSchemaInstance read(String name) throws FileNotFoundException {
        return read(new FileInputStream(name), "utf8");
    }

    /**
     *
     * @param file
     * @param encoding
     * @throws FileNotFoundException
     */
    public AuditSchemaInstance read(File file, String encoding) throws FileNotFoundException {
        return read(new FileInputStream(file), encoding);
    }

    /**
     *
     * @param file
     * @throws FileNotFoundException
     */
    public AuditSchemaInstance read(File file) throws FileNotFoundException {
        return read(new FileInputStream(file), "utf8");
    }

    /**
     *
     * @param stream
     */
    public AuditSchemaInstance read(InputStream stream) {
        return read(stream, "utf8");
    }

    /**
     *
     * @param stream
     * @param encoding
     */
    public AuditSchemaInstance read(InputStream stream, String encoding) {
        AuditSchemaInstance auditSchemaInstance = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(stream);
            XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xPath = xpathfactory.newXPath();
            XPathExpression expr = null;
            List<String> audit = new ArrayList<String>();
            List<String> network = new ArrayList<String>();
            for (int i : scalarItems) {
                expr = xPath.compile(xpathList[i]);
                String result = (String) expr.evaluate(doc, XPathConstants.STRING);
                if (i < 4 || i > 17) {
                    audit.add(result);
                } else {
                    network.add(result);
                }
            }
            logger.info("audit[0,1,2,3, 18, 19, 20, 23]:" + audit);
            logger.info("network[14,15, 17]:" + network);
            Audit iAudit = new Audit(audit.get(0), audit.get(1), audit.get(2), Integer.parseInt(audit.get(3)), audit.get(4), audit.get(5), audit.get(6), Integer.parseInt(audit.get(7)));
            Network iNetwork = new Network(network.get(0), network.get(1), network.get(2));
            int itemCounterH = 0;
            expr = xPath.compile(xpathList[4]);
            NodeList nodesHostName = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            List<String> hostName = getStringData(nodesHostName);
            itemCounterH++;
            logger.info("4-th(hostName)=" + hostName);
            expr = xPath.compile(xpathList[5]);
            NodeList nodesIp = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            List<String> hostIpAddress = getStringData(nodesIp);
            itemCounterH++;
            logger.info("5-th(hostIpAddress)=" + hostIpAddress);
            expr = xPath.compile(xpathList[6]);
            NodeList nodesStorage = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            List<Long> storageAvailable = getLongData(nodesStorage);
            itemCounterH++;
            logger.info("5-th(storageAvailable)=" + storageAvailable);
            expr = xPath.compile(xpathList[16]);
            NodeList nodesGeoLoc = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            List<String> geoLocation = getStringData(nodesGeoLoc);
            itemCounterH++;
            logger.info("16-th(geoLocation)=" + geoLocation);
            int totalNodeCountH = hostName.size() + hostIpAddress.size() + storageAvailable.size() + geoLocation.size();
            if (totalNodeCountH != hostName.size() * itemCounterH) {
                logger.severe("parsing error(s) is suspected during the host block");
                return null;
            }
            List<Host> iHost = new ArrayList<Host>();
            for (int i = 0; i < hostName.size(); i++) {
                iHost.add(new Host(hostName.get(i), hostIpAddress.get(i), storageAvailable.get(i), geoLocation.get(i)));
            }
            int itemCounterA = 0;
            expr = xPath.compile(xpathList[7]);
            NodeList nodesAuName = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            List<String> auName = getStringData(nodesAuName);
            itemCounterA++;
            logger.info("7-th(auName)=" + auName);
            expr = xPath.compile(xpathList[8]);
            NodeList nodesAuId = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            List<String> auId = getStringData(nodesAuId);
            itemCounterA++;
            logger.info("8-th(auId)=" + auId);
            expr = xPath.compile(xpathList[9]);
            NodeList nodesUF = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            List<Integer> updateFrequency = getIntegerData(nodesUF);
            itemCounterA++;
            logger.info("9-th(updateFrequency)=" + updateFrequency);
            expr = xPath.compile(xpathList[10]);
            NodeList nodesSR = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            List<BigInteger> storageRequired = getBigIntegerData(nodesSR);
            itemCounterA++;
            logger.info("10-th(storageRequired)=" + storageRequired);
            expr = xPath.compile(xpathList[11]);
            NodeList nodesNRep = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            List<Long> numberReplicates = getLongData(nodesNRep);
            itemCounterA++;
            logger.info("11-th(numberReplicates)=" + numberReplicates);
            expr = xPath.compile(xpathList[12]);
            NodeList nodesRD = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            List<Integer> replicationDuration = getIntegerData(nodesRD);
            itemCounterA++;
            logger.info("12-th(replicationDuration)=" + replicationDuration);
            expr = xPath.compile(xpathList[13]);
            NodeList nodesVF = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            List<Integer> verificationFrequency = getIntegerData(nodesVF);
            itemCounterA++;
            logger.info("13-th(verificationFrequency)=" + verificationFrequency);
            expr = xPath.compile(xpathList[21]);
            NodeList nodesSubject = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            List<String> subject = null;
            if (nodesSubject.getLength() < auName.size()) {
                logger.info("21-st(subject):node-length=" + nodesSubject.getLength());
            }
            subject = getStringData(nodesSubject);
            itemCounterA++;
            logger.info("21-st(subject)=" + subject);
            if (subject.isEmpty()) {
                for (int i = 0; i < auName.size(); i++) {
                    subject.add(placeholder);
                }
            }
            expr = xPath.compile(xpathList[22]);
            NodeList nodesOwnerInst = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            List<String> ownerInst = null;
            if (nodesSubject.getLength() < auName.size()) {
                logger.info("22-nd(ownerInst):node-length=" + nodesSubject.getLength());
            }
            ownerInst = getStringData(nodesOwnerInst);
            itemCounterA++;
            logger.info("22-nd(ownerInst)=" + ownerInst);
            if (ownerInst.isEmpty()) {
                for (int i = 0; i < auName.size(); i++) {
                    ownerInst.add(placeholder);
                }
            }
            expr = xPath.compile(xpathList[24]);
            NodeList nodesGeoRdndncy = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            List<Integer> geoRedundancy = getIntegerData(nodesGeoRdndncy);
            if (nodesGeoRdndncy.getLength() < auName.size()) {
                logger.info("24-st(geoRedundancy):node-length=" + nodesGeoRdndncy.getLength());
            }
            itemCounterA++;
            logger.info("24-th(geoRedundancy)=" + geoRedundancy);
            itemCounterA = itemCounterA - 2;
            int totalNodeCountA = auName.size() + auId.size() + updateFrequency.size() + storageRequired.size() + numberReplicates.size() + replicationDuration.size() + verificationFrequency.size() + geoRedundancy.size();
            if (totalNodeCountA != auId.size() * itemCounterA) {
                logger.severe("parsing error(s) is suspected during the AU block");
                return null;
            }
            List<ArchivalUnit> iAu = new ArrayList<ArchivalUnit>();
            for (int i = 0; i < auName.size(); i++) {
                iAu.add(new ArchivalUnit(auName.get(i), numberReplicates.get(i), verificationFrequency.get(i), replicationDuration.get(i), updateFrequency.get(i), storageRequired.get(i), auId.get(i), subject.get(i), ownerInst.get(i), geoRedundancy.get(i)));
            }
            auditSchemaInstance = new AuditSchemaInstance(iAudit, iNetwork, iHost, iAu);
        } catch (ParserConfigurationException ex) {
            logger.log(Level.SEVERE, "Dom parser configuration error", ex);
        } catch (SAXException ex) {
            logger.log(Level.SEVERE, "sax exception occurred", ex);
        } catch (XPathExpressionException ex) {
            logger.log(Level.SEVERE, "xpath-related error", ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "io error occurred", ex);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "failed to close inputsream", ex);
                }
            }
        }
        return auditSchemaInstance;
    }

    public List<String> getStringData(NodeList nodes) {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < nodes.getLength(); i++) {
            result.add(nodes.item(i).getNodeValue());
        }
        return result;
    }

    public List<Long> getLongData(NodeList nodes) {
        List<Long> result = new ArrayList<Long>();
        for (int i = 0; i < nodes.getLength(); i++) {
            result.add(Long.parseLong(nodes.item(i).getNodeValue()));
        }
        return result;
    }

    public List<Integer> getIntegerData(NodeList nodes) {
        List<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < nodes.getLength(); i++) {
            result.add(Integer.parseInt(nodes.item(i).getNodeValue()));
        }
        return result;
    }

    public List<BigInteger> getBigIntegerData(NodeList nodes) {
        List<BigInteger> result = new ArrayList<BigInteger>();
        for (int i = 0; i < nodes.getLength(); i++) {
            result.add(new BigInteger(nodes.item(i).getNodeValue()));
        }
        return result;
    }
}
