package net.sf.gateway.mef.utilities;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SubmissionGenerator {

    private static String generateManifest(String submissionId, String EFIN) {
        File xmlfile = new File("attachments" + File.separator + "manifest.xml");
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.parse(xmlfile);
            XPath xpath = XPathFactory.newInstance().newXPath();
            NamespaceContext ctx = SchemaValidator.getEFileNamespaceContext();
            xpath.setNamespaceContext(ctx);
            Element submissionIdElement = (Element) (xpath.evaluate("/efil:StateSubmissionManifest/efil:SubmissionId", doc, XPathConstants.NODE));
            submissionIdElement.setTextContent(submissionId);
            Element efinElement = (Element) (xpath.evaluate("/efil:StateSubmissionManifest/efil:EFIN", doc, XPathConstants.NODE));
            efinElement.setTextContent(EFIN);
            Element submissionTypeElement = (Element) (xpath.evaluate("/efil:StateSubmissionManifest/efil:SubmissionType", doc, XPathConstants.NODE));
            submissionTypeElement.setTextContent("VTIN111");
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
            String xmlString = result.getWriter().toString();
            return xmlString;
        } catch (Exception e) {
            return "<fail/>";
        }
    }

    private static String generateHS122Submission(String EFIN) {
        File xmlfile = new File("attachments" + File.separator + "test7.xml");
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.parse(xmlfile);
            XPath xpath = XPathFactory.newInstance().newXPath();
            NamespaceContext ctx = SchemaValidator.getEFileNamespaceContext();
            xpath.setNamespaceContext(ctx);
            Element softwareIdElement = (Element) (xpath.evaluate("/efil:ReturnState/efil:ReturnHeaderState/efil:SoftwareId", doc, XPathConstants.NODE));
            softwareIdElement.setTextContent("1019");
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
            String xmlString = result.getWriter().toString();
            return xmlString;
        } catch (Exception e) {
            return "<fail/>";
        }
    }

    public static void generateAckNotifications() throws Exception {
        String subids = "";
        try {
            byte[] subidsBytes = IOUtils.byteSafeRead("attachments" + File.separator + "subids.txt");
            subids = new String(subidsBytes);
        } catch (Exception e) {
            subids = "";
        }
        String[] submissionIds = subids.split("\n");
        String ackNoteListHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<AckNotificationList xmlns=\"http://www.irs.gov/efile\">";
        String ackNoteList = "";
        String ackNoteListTail = "</AckNotificationList>";
        int count = 0;
        for (String submissionId : submissionIds) {
            Date rawTimestamp = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            String timestamp = formatter.format(rawTimestamp);
            String ackNote = "<AckNotification>" + "<SubmissionId>" + submissionId + "</SubmissionId>" + "<Timestamp>" + timestamp + "</Timestamp>" + "</AckNotification>";
            ackNoteList += ackNote;
            count++;
        }
        String ackNoteListComplete = ackNoteListHead + "<Count>" + count + "</Count>" + ackNoteList + ackNoteListTail;
        Map<String, byte[]> files = new HashMap<String, byte[]>();
        files.put("/notifications.xml", ackNoteListComplete.getBytes());
        IOUtils.byteSafeWrite(ZipUtils.zipFiles(files), "attachments" + File.separator + "attachment.zip");
        IOUtils.byteSafeWrite("".getBytes(), "attachments" + File.separator + "subids.txt");
    }

    public static void generateAckNotification() throws Exception {
        String submissionId = "00123201020013113864";
        String ackNoteListHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<AckNotificationList xmlns=\"http://www.irs.gov/efile\">";
        String ackNoteList = "";
        String ackNoteListTail = "</AckNotificationList>";
        Date rawTimestamp = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String timestamp = formatter.format(rawTimestamp);
        String ackNote = "<AckNotification>" + "<SubmissionId>" + submissionId + "</SubmissionId>" + "<Timestamp>" + timestamp + "</Timestamp>" + "</AckNotification>";
        ackNoteList += ackNote;
        String ackNoteListComplete = ackNoteListHead + "<Count>1</Count>" + ackNoteList + ackNoteListTail;
        Map<String, byte[]> files = new HashMap<String, byte[]>();
        files.put("/notifications.xml", ackNoteListComplete.getBytes());
        IOUtils.byteSafeWrite(ZipUtils.zipFiles(files), "attachments" + File.separator + "attachment.zip");
        IOUtils.byteSafeWrite("".getBytes(), "attachments" + File.separator + "subids.txt");
    }

    public static void genenerateSubmission(String submissionId, String EFIN, String EIN) throws Exception {
        Map<String, byte[]> files = new HashMap<String, byte[]>();
        String manifest = SubmissionGenerator.generateManifest(submissionId, EFIN);
        files.put("/manifest/manifest.xml", manifest.getBytes());
        String submission = SubmissionGenerator.generateHS122Submission(EFIN);
        files.put("/xml/submission.xml", submission.getBytes());
        File attachmentsDir = new File("attachments");
        if (!attachmentsDir.exists()) {
            attachmentsDir.mkdir();
        }
        Map<String, byte[]> zipFiles = new HashMap<String, byte[]>();
        byte[] zippedFiles = ZipUtils.zipFiles(files);
        zipFiles.put("/attachments", zippedFiles);
        IOUtils.byteSafeWrite(ZipUtils.zipFiles(zipFiles), "attachments" + File.separator + "attachment.zip");
        String subids = "";
        try {
            byte[] subidsBytes = IOUtils.byteSafeRead("attachments" + File.separator + "subids.txt");
            subids = new String(subidsBytes);
        } catch (IOException e) {
            subids = "";
        }
        subids = submissionId + "\n" + subids;
        IOUtils.byteSafeWrite(subids.getBytes(), "attachments" + File.separator + "subids.txt");
    }
}
