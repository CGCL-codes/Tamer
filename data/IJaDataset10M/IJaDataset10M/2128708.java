package edu.harvard.iq.safe.saasystem.auditschema;

import edu.harvard.iq.safe.saasystem.trac1.TracAuditChecklistDataFacade;
import edu.harvard.iq.safe.saasystem.util.ConfigFile;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 *
 * @author Akio Sone
 */
public class AuditSchemaInstance implements Serializable {

    static final Logger logger = Logger.getLogger(AuditSchemaInstance.class.getName());

    static final String CONFIG_JNDI_NAME = "java:global/safearchiveauditsystem-ear/safearchiveauditsystem-ejb-1.1/ConfigFile";

    InitialContext ic = null;

    ConfigFile configFile = null;

    static final String TRAC_DATA_JNDI_NAME = "java:global/safearchiveauditsystem-ear/safearchiveauditsystem-ejb-1.1/TracAuditChecklistDataFacade";

    TracAuditChecklistDataFacade tracAuditChecklistDataFacade = null;

    {
        try {
            ic = new InitialContext();
            configFile = (ConfigFile) ic.lookup(CONFIG_JNDI_NAME);
            tracAuditChecklistDataFacade = (TracAuditChecklistDataFacade) ic.lookup(TRAC_DATA_JNDI_NAME);
        } catch (NamingException ex) {
            logger.severe("Class - JNDI lookup failed:");
        }
    }

    Audit audit;

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    Network newtwork;

    public Network getNewtwork() {
        return newtwork;
    }

    public void setNewtwork(Network newtwork) {
        this.newtwork = newtwork;
    }

    List<Host> hosts = new ArrayList<Host>();

    public List<Host> getHosts() {
        return hosts;
    }

    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
    }

    List<ArchivalUnit> aus = new ArrayList<ArchivalUnit>();

    public List<ArchivalUnit> getAus() {
        return aus;
    }

    public void setAus(List<ArchivalUnit> aus) {
        this.aus = aus;
    }

    static String XML_FILE_ENCODING = "UTF-8";

    String AUDIT_SCHEMA_XSD_FILE = configFile.getAuditReportPDFFileName();

    public AuditSchemaInstance() {
    }

    public AuditSchemaInstance(Audit audit, Network network, List<Host> hosts, List<ArchivalUnit> aus) {
        this.audit = audit;
        this.newtwork = network;
        this.hosts = hosts;
        this.aus = aus;
    }

    public String getVersionControlLogMessage(String timestampPattern) {
        StringBuilder sb = new StringBuilder("Audit Schema Instance:");
        sb.append("Id=").append(audit.getAuditId()).append("; Version=").append(audit.getSchemaVersion()).append("; date=").append(DateFormatUtils.format(new java.util.Date(), timestampPattern));
        return sb.toString();
    }

    public void write(String fileName) throws FileNotFoundException {
        write(new FileOutputStream(fileName), "utf8");
    }

    public void write(String fileName, String encoding) throws FileNotFoundException {
        write(new FileOutputStream(fileName), encoding);
    }

    public void write(File file, String encoding) throws FileNotFoundException {
        write(new FileOutputStream(file), encoding);
    }

    public void write(File file) throws FileNotFoundException {
        write(new FileOutputStream(file), "utf8");
    }

    public void write(OutputStream stream) {
        write(stream, "utf8");
    }

    public void write(OutputStream stream, String encoding) {
        PrintWriter pwout = null;
        try {
            pwout = new PrintWriter(new OutputStreamWriter(stream, encoding), true);
            pwout.println(this.toXML());
        } catch (UnsupportedEncodingException ex) {
            logger.log(Level.SEVERE, "UnsupportedEncodingException", ex);
        } finally {
            if (pwout != null) {
                pwout.close();
            }
        }
    }

    public String toXML() {
        StringBuilder sb = new StringBuilder(xmltop);
        sb.append(rootEls);
        sb.append(getAuditBlock(this.audit.auditId, this.audit.schemaVersion, this.audit.auditReportEmail, this.audit.auditReportInterval, this.audit.geographicSummaryScheme, this.audit.subjectSummaryScheme, this.audit.ownerInstSummaryScheme, this.audit.maxRegions));
        sb.append(getNetworkBlock(this.newtwork.groupName, this.newtwork.netAdminEmail, this.newtwork.geographicCoding));
        sb.append(getHostsBlock(this.hosts));
        sb.append(getArchivalUnitsBlock(this.aus));
        sb.append(rootEle);
        return sb.toString();
    }

    String xmltop = "<?xml version=\"1.0\" encoding=\"" + XML_FILE_ENCODING + "\"?>\n";

    String rootEls = "<SSP xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + " xsi:noNamespaceSchemaLocation=\"" + AUDIT_SCHEMA_XSD_FILE + "\">\n";

    String rootEle = "</SSP>\n";

    String getAuditBlock(String auditId, String version, String email, int auditReportInterval, String geographicSummaryScheme, String subjectSummaryScheme, String ownerInstSummaryScheme, int maxRegions) {
        return "\t<audit audit_id=\"" + auditId + "\">\n" + "\t\t" + "<schemaVersion>" + version + "</schemaVersion>\n" + "\t\t" + "<auditReportEmail>" + email + "</auditReportEmail>\n" + "\t\t" + "<auditReportInterval maxDays=\"" + auditReportInterval + "\"/>\n" + "\t\t" + "<geographicSummaryScheme maxRegions=\"" + maxRegions + "\">" + geographicSummaryScheme + "</geographicSummaryScheme>\n" + "\t\t" + "<subjectSummaryScheme>" + subjectSummaryScheme + "</subjectSummaryScheme>\n" + "\t\t" + "<ownerInstSummaryScheme>" + ownerInstSummaryScheme + "</ownerInstSummaryScheme>\n" + "\t</audit>\n";
    }

    String getNetworkBlock(String name, String email, String geographicCoding) {
        if (name == null) {
            name = "unspecified";
        }
        if (tracAuditChecklistDataFacade == null) {
            return "\t<network network_id=\"" + name + "\">\n" + "\t\t<networkIdentity>\n" + "\t\t\t<accessBase adminEmail=\"" + email + "\"/>\n" + "\t\t\t<geographicCoding>" + geographicCoding + "</geographicCoding>\n" + "\t\t</networkIdentity>\n" + "\t\t<networkCapabilities/>\n\t\t<networkTerms/>\n\t</network>\n";
        }
        if (!tracAuditChecklistDataFacade.isTracEvidenceAvailable()) {
            return "\t<network network_id=\"" + name + "\">\n" + "\t\t<networkIdentity>\n" + "\t\t\t<accessBase adminEmail=\"" + email + "\"/>\n" + "\t\t\t<geographicCoding>" + geographicCoding + "</geographicCoding>\n" + "\t\t</networkIdentity>\n" + "\t\t<networkCapabilities/>\n\t\t<networkTerms/>\n\t</network>\n";
        } else {
            return "\t<network network_id=\"" + name + "\">\n" + "\t\t<networkIdentity>\n" + "\t\t\t<accessBase adminEmail=\"" + email + "\"/>\n" + "\t\t\t<geographicCoding>" + geographicCoding + "</geographicCoding>\n" + "\t\t</networkIdentity>\n" + "\t\t<networkCapabilities/>\n\t\t<networkTerms>\n\t\t\t<serviceTerms>\n" + tracAuditChecklistDataFacade.getTracEvidenceInXML() + "\t\t\t</serviceTerms>\n\t\t</networkTerms>\n\t</network>\n";
        }
    }

    String getHostsBlock(List<Host> hosts) {
        StringBuilder sb = new StringBuilder("\t<hosts>\n");
        for (Host hst : hosts) {
            String template = "\t\t<host host_id=\"" + hst.getHostIpAddress() + "\">\n\t\t\t<hostIdentity>\n" + "\t\t\t\t<name>" + hst.getHostName() + "</name>\n" + "\t\t\t\t<geographicLocation>" + StringEscapeUtils.escapeXml(hst.getGeographicLocation()) + "</geographicLocation>\n" + "\t\t\t</hostIdentity>\n" + "\t\t\t<hostCapabilities>\n\t\t\t\t<storageAvailable max_size=\"" + hst.getStorageAvailable() + "\"/>\n\t\t\t</hostCapabilities>\n\t\t\t<hostTerms/>\n\t\t</host>\n";
            sb.append(template);
        }
        sb.append("\t</hosts>\n");
        return sb.toString();
    }

    String getArchivalUnitsBlock(List<ArchivalUnit> aus) {
        StringBuilder sb = new StringBuilder("\t<archivalUnits>\n");
        for (ArchivalUnit au : aus) {
            String subject = au.getSubject();
            if (subject == null || subject.equals("null")) {
                subject = "";
            } else {
                subject = StringEscapeUtils.escapeXml(au.getSubject());
            }
            String ownerInst = au.getOwnerInstitution();
            if (ownerInst == null || ownerInst.equals("null")) {
                ownerInst = "";
            } else {
                ownerInst = StringEscapeUtils.escapeXml(au.getOwnerInstitution());
            }
            String template = "\t\t<au au_id=\"" + StringEscapeUtils.escapeXml(au.getAuId()) + "\">\n" + "\t\t\t<auIdentity>\n" + "\t\t\t\t<name>" + StringEscapeUtils.escapeXml(au.getAuName()) + "</name>\n" + "\t\t\t\t<subject>" + subject + "</subject>\n" + "\t\t\t\t<ownerInstitution>" + ownerInst + "</ownerInstitution>\n" + "\t\t\t</auIdentity>\n" + "\t\t\t<auCapabilities>\n" + "\t\t\t\t<numberReplicates min=\"" + au.getNumberReplicates() + "\" " + "regions=\"" + au.geoRedudancy + "\"/>\n" + "\t\t\t\t<verificationFrequency maxDays=\"" + au.getVerificationFrequency() + "\"/>\n" + "\t\t\t\t<replicationDuration maxDays=\"" + au.getReplicationDuration() + "\"/>\n" + "\t\t\t\t<updateFrequency minDays=\"" + au.getUpdateFrequency() + "\"/>\n" + "\t\t\t\t<storageRequired max_size=\"" + au.getStorageRequired() + "\"/>\n" + "\t\t\t</auCapabilities>\n\t\t\t<auTerms/>\n\t\t</au>\n";
            sb.append(template);
        }
        sb.append("\t</archivalUnits>\n");
        return sb.toString();
    }
}
