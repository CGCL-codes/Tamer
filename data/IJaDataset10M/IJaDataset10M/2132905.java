package com.misyshealthcare.connect.ihe;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.xml.soap.SOAPMessage;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.xml.serialize.Method;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.SerializerFactory;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Element;
import com.misyshealthcare.connect.base.Document;
import com.misyshealthcare.connect.base.DocumentException;
import com.misyshealthcare.connect.base.PatientID;
import com.misyshealthcare.connect.base.SharedEnums;
import com.misyshealthcare.connect.base.SharedEnums.SexType;
import com.misyshealthcare.connect.base.SharedEnums.XdsClassCode;
import com.misyshealthcare.connect.base.SharedEnums.XdsContentCode;
import com.misyshealthcare.connect.base.SharedEnums.XdsFormatCode;
import com.misyshealthcare.connect.base.SharedEnums.XdsTypeCode;
import com.misyshealthcare.connect.base.demographicdata.AuthorDescriptor;
import com.misyshealthcare.connect.base.demographicdata.PatientDescriptor;
import com.misyshealthcare.connect.ihe.audit.IheAuditTrail;
import com.misyshealthcare.connect.ihe.audit.ParticipantObject;
import com.misyshealthcare.connect.ihe.configuration.IheConfigurationException;
import com.misyshealthcare.connect.ihe.registry.XdsDocumentEntry;
import com.misyshealthcare.connect.ihe.registry.XdsSubmissionSet;
import com.misyshealthcare.connect.net.IConnectionDescription;
import com.misyshealthcare.connect.net.StandardConnectionDescription;
import com.misyshealthcare.connect.util.OID;

/**
 * @author Michael Traum
 * @version 2.0, December 12, 2006
 *
 */
public class XdmMessenger {

    private static final Logger log = Logger.getLogger(XdmMessenger.class.getName());

    private IConnectionDescription connection = null;

    private IheAuditTrail auditTrail = null;

    private MimeTypeToFileSuffixConverter mimeTypeMap = new MimeTypeToFileSuffixConverter();

    public XdmMessenger(IConnectionDescription description, IheAuditTrail auditTrail) {
        connection = description;
        this.auditTrail = auditTrail;
    }

    private void copyFile(File in, File out) throws Exception {
        FileChannel sourceChannel = new FileInputStream(in).getChannel();
        FileChannel destinationChannel = new FileOutputStream(out).getChannel();
        sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        sourceChannel.close();
        destinationChannel.close();
    }

    public void send(Collection<Document> documents, String description, XdsContentCode contentCode, String directory, String mediaDescription) throws DocumentException {
        String workingDir = directory;
        String xdmDir = workingDir + File.separator + "IHE_XDM";
        new File(xdmDir).mkdir();
        try {
            copyFile(new File(XdmMessenger.class.getResource("xdm/README.TXT").getPath()), new File(workingDir + File.separator + "README.TXT"));
        } catch (Exception e) {
            log.error("Error copying README.TXT", e);
        }
        XdsDocumentSource xdsSource = new XdsDocumentSource(connection, auditTrail);
        XdsMessenger xdsMessenger = new XdsMessenger(connection, false);
        XdsSubmissionSet submissionSet = null;
        try {
            submissionSet = xdsSource.createSubmissionSetMetadata(documents, description, contentCode);
        } catch (IheConfigurationException e) {
            log.error("Error generating the submission set metadata", e);
        }
        ArrayList documentFileNames = new ArrayList();
        int count = 1;
        for (Iterator i = documents.iterator(); i.hasNext(); count++) {
            String currentDir = xdmDir + File.separator + "SUBSET" + count;
            new File(currentDir).mkdir();
            Document doc = (Document) i.next();
            XdsDocumentEntry entry = null;
            try {
                entry = xdsSource.createDocumentMetadata(doc);
            } catch (IheConfigurationException e) {
                log.error("Error generating the document metadata", e);
            }
            String suffix = mimeTypeMap.getSuffix(entry.getMimeType());
            String embeddedFilename = suffix + "." + suffix;
            entry.setContent(doc.getContents());
            entry.setEntryUuid("doc" + count);
            entry.setUri(embeddedFilename);
            List documentEntries = new ArrayList<XdsDocumentEntry>();
            documentEntries.add(entry);
            SOAPMessage xdsSubmission = xdsMessenger.createSubmitObjectsRequest(submissionSet, documentEntries);
            try {
                StringWriter content = new StringWriter();
                XMLSerializer ser = (XMLSerializer) SerializerFactory.getSerializerFactory(Method.XML).makeSerializer(content, new OutputFormat()).asDOMSerializer();
                ser.setNamespaces(true);
                ser.serialize((Element) xdsSubmission.getSOAPBody().getChildNodes().item(0));
                File file = new File(currentDir + File.separator + "METADATA.XML");
                FileWriter fw = new FileWriter(file);
                fw.write(content.toString());
                fw.flush();
                fw.close();
            } catch (Exception e) {
                log.error("Error writing metatdata" + e);
            }
            String contentsFilename = currentDir + File.separator + embeddedFilename;
            if ("text/x-cda-r2+xml".equals(doc.getMimeType()) || "text/xml".equals(doc.getMimeType())) {
                try {
                    doc.setContents(doc.getContents().toString().replaceFirst("(?s)(?i)<?xml-stylesheet.*?>", "xml-stylesheet type=\"text/xsl\" href=\"IMPCDAR2.XSL\"?>"));
                    BufferedWriter file = new BufferedWriter(new FileWriter(contentsFilename));
                    file.write(doc.getContents().toString());
                    file.close();
                    copyFile(new File(XdmMessenger.class.getResource("xdm/IMPL_CDAR2.xsl").getPath()), new File(currentDir + File.separator + "IMPCDAR2.XSL"));
                    InputStream contentsStream = new BufferedInputStream(new FileInputStream(contentsFilename));
                } catch (Exception e) {
                    log.error("Error writing xml contents", e);
                }
            } else {
                try {
                    URL url = new URL(doc.getUri());
                    byte buf[] = new byte[1000];
                    InputStream input = url.openStream();
                    FileOutputStream fout = new FileOutputStream(contentsFilename);
                    int ch;
                    int currentCount = 0;
                    while (true) {
                        int n = input.read(buf, 0, 1000);
                        if (n == -1) break;
                        fout.write(buf, 0, n);
                        currentCount += n;
                    }
                    fout.close();
                    input.close();
                } catch (Exception e) {
                    log.error("Error retrieving url contents", e);
                }
            }
            documentFileNames.add(contentsFilename.substring(workingDir.length() + 1));
            if (auditTrail != null) {
                ParticipantObject logobject = new ParticipantObject(doc);
                auditTrail.recordExported(logobject, mediaDescription);
            }
        }
        try {
            Properties props = new Properties();
            String vmroot = new File(XdmMessenger.class.getResource("xdm").getPath()).getAbsolutePath();
            props.setProperty("resource.loader", "file");
            props.setProperty("file.resource.loader.path", vmroot);
            Velocity.init(props);
            VelocityContext context = new VelocityContext();
            PatientDescriptor patient = documents.iterator().next().getPatientDescriptor();
            context.put("patient", patient);
            Date date = new Date();
            Format formatter = new SimpleDateFormat("MMMM d, yyyy");
            context.put("date", formatter.format(date));
            if (patient.getBirthDateTime() != null) {
                context.put("dob", formatter.format(patient.getBirthDateTime()));
            }
            context.put("docs", documentFileNames);
            Template template = Velocity.getTemplate("INDEX.HTM.vm");
            FileWriter writer = new FileWriter(workingDir + File.separator + "INDEX.HTM");
            if (template != null) {
                template.merge(context, writer);
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            log.error("Error generating INDEX.HTM", e);
        }
    }

    public class MimeTypeToFileSuffixConverter {

        private class MimeTypeSuffixSet {

            String mimeType;

            Collection<String> suffixes;

            MimeTypeSuffixSet(String mimeType) {
                this.mimeType = mimeType.toLowerCase();
                suffixes = new ArrayList<String>();
            }

            String getMimeType() {
                return mimeType;
            }

            void addSuffix(String ending) {
                suffixes.add(ending.toUpperCase());
            }

            boolean hasSuffix(String ending) {
                return suffixes.contains(ending.toUpperCase());
            }

            String getDefaultSuffix() {
                String suffix = "";
                if (!suffixes.isEmpty()) suffix = suffixes.iterator().next();
                return suffix;
            }
        }

        private HashMap<String, MimeTypeSuffixSet> mimeSuffixTable;

        public String getSuffix(String mimeType) {
            MimeTypeSuffixSet set = mimeSuffixTable.get(mimeType.toLowerCase());
            String suffix = set == null ? "" : set.getDefaultSuffix();
            if (suffix.length() == 0) log.warn("No suffix found for this mime type: " + mimeType);
            return suffix;
        }

        public String getMimeType(String suffix) {
            String mimeType = "";
            for (MimeTypeSuffixSet set : mimeSuffixTable.values()) {
                if (set.hasSuffix(suffix)) {
                    mimeType = set.getMimeType();
                    break;
                }
            }
            if (mimeType.length() == 0) log.warn("No mime type found for suffix: " + suffix);
            return mimeType;
        }

        MimeTypeToFileSuffixConverter() {
            mimeSuffixTable = new HashMap<String, MimeTypeSuffixSet>();
            MimeTypeSuffixSet entry = new MimeTypeSuffixSet("text/plain");
            entry.addSuffix("TXT");
            entry.addSuffix("TEXT");
            mimeSuffixTable.put(entry.getMimeType(), entry);
            entry = new MimeTypeSuffixSet("text/xml");
            entry.addSuffix("XML");
            mimeSuffixTable.put(entry.getMimeType(), entry);
            entry = new MimeTypeSuffixSet("application/pdf");
            entry.addSuffix("PDF");
            mimeSuffixTable.put(entry.getMimeType(), entry);
        }
    }

    public static void main(String[] args) {
        ArrayList ds = new ArrayList();
        PatientDescriptor patient = new PatientDescriptor(new PatientID("076686eb2be743e^^^&1.3.6.1.4.1.21367.2005.3.7&ISO"));
        patient.setNameFirst("Marvin");
        patient.setNameLast("Jones");
        patient.setAdministrativeSex(SexType.MALE);
        List<SharedEnums.ConfidentialityCode> confidentialityCodes = new ArrayList<SharedEnums.ConfidentialityCode>();
        confidentialityCodes.add(SharedEnums.ConfidentialityCode.Normal);
        AuthorDescriptor author = new AuthorDescriptor();
        author.addAuthorInstitution("Test Hospital");
        Document doc = new Document(patient);
        doc.addAuthorDescriptor(author);
        doc.setClassCode(XdsClassCode.Transfer_Summarization);
        doc.setFormatCode(XdsFormatCode.IHE_CDAR2_10);
        doc.setTypeCode(XdsTypeCode.Transfer_Summarization_Note);
        doc.setMimeType("text/xml");
        doc.setCreationTime(new Date());
        doc.setPracticeCode("SoothingDiscussion");
        doc.setFacilityCode("BoobyHatch");
        doc.setUniqueId(OID.getDocumentUID());
        doc.setContents("Some sample text 1");
        doc.setSourceId("Misys_Connect");
        doc.setConfidentialityCodes(confidentialityCodes);
        ds.add(doc);
        Document doc2 = new Document(patient);
        doc2.addAuthorDescriptor(author);
        doc2.setClassCode(XdsClassCode.Transfer_Summarization);
        doc2.setFormatCode(XdsFormatCode.IHE_CDAR2_10);
        doc2.setTypeCode(XdsTypeCode.Transfer_Summarization_Note);
        doc2.setMimeType("text/xml");
        doc2.setCreationTime(new Date());
        doc2.setPracticeCode("SoothingDiscussion");
        doc2.setFacilityCode("BoobyHatch");
        doc2.setUniqueId(OID.getDocumentUID());
        doc2.setContents("Some sample text 2");
        doc2.setSourceId("Misys_Connect");
        doc2.setConfidentialityCodes(confidentialityCodes);
        ds.add(doc2);
        IConnectionDescription connection = null;
        StandardConnectionDescription conn = new StandardConnectionDescription();
        conn.setHostname("localhost");
        conn.loadConfiguration("../../Mesa/testkit/codes.xml");
        conn.loadConfiguration("../../Mesa/testkit/configuration.xml");
        XdmMessenger xdmMessenger = new XdmMessenger(connection, null);
        String tempDir = System.getProperty("java.io.tmpdir");
        Date now = new Date();
        String workingDir = tempDir + File.separator + now.getTime();
        new File(workingDir).mkdir();
        System.out.println("DONE WITH TEST");
    }
}
