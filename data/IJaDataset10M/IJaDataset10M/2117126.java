package org.dcm4chex.rid.mbean.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;
import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmElement;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.data.PersonName;
import org.dcm4che.dict.Tags;
import org.dcm4che.dict.UIDs;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author franz.willer
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IHEDocumentList implements XMLResponseObject {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyyMMdd");

    private static final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("yyyyMMddHHmmss");

    private static final AttributesImpl EMPTY_ATTRIBUTES = new AttributesImpl();

    private static Logger log = Logger.getLogger(IHEDocumentList.class.getName());

    private List<Dataset> datasets = new ArrayList<Dataset>();

    private HashSet<String> iuids = new HashSet<String>();

    private Dataset queryDS = null;

    private TransformerHandler th = null;

    private XMLUtil util = null;

    private String docCode;

    private String docCodeSystem;

    private String docDisplayName;

    private long lowerDateTime = Long.MIN_VALUE;

    private long upperDateTime = Long.MAX_VALUE;

    private int mostRecentResults = 0;

    private String xslFile;

    /** the request URL which is used from client to get this document list (with query string!). */
    private String reqURL = "";

    private String docRIDUrl = "http://localhost:8080";

    private URL xslt;

    private Collection<String> docSopCuids;

    public IHEDocumentList() {
    }

    public IHEDocumentList(Collection<Dataset> datasets) {
        addAll(datasets);
    }

    public void setQueryDS(Dataset ds) {
        queryDS = ds;
    }

    public boolean add(Dataset ds) {
        if (iuids.contains(ds.getString(Tags.SOPInstanceUID))) {
            return false;
        }
        Date date = ds.getDateTime(Tags.ContentDate, Tags.ContentTime);
        if (date != null) {
            long ms = date.getTime();
            if (ms < lowerDateTime || ms > upperDateTime) {
                return false;
            }
        }
        iuids.add(ds.getString(Tags.SOPInstanceUID));
        return datasets.add(ds);
    }

    /**
     * @param ds
     * @return
     */
    private void applyMostRecentResults() {
        Collections.sort(datasets, new DatasetDateComparator());
        if (mostRecentResults > 0 && datasets.size() > mostRecentResults) {
            datasets.subList(mostRecentResults, datasets.size()).clear();
        }
    }

    /**
     * @param ds
     * @return
     */
    private Date getDateFromDS(Dataset ds) {
        Date d = ds.getDateTime(Tags.ContentDate, Tags.ContentTime);
        if (d == null) d = ds.getDate(Tags.AcquisitionDatetime);
        return d;
    }

    public void addAll(Collection<Dataset> col) {
        if (col == null || col.isEmpty()) return;
        for (Dataset ds : col) {
            add(ds);
        }
    }

    public int size() {
        return datasets.size();
    }

    /**
     * @return Returns the docCode.
     */
    public String getDocCode() {
        return docCode;
    }

    /**
     * @param docCode The docCode to set.
     */
    public void setDocCode(String docCode) {
        this.docCode = docCode;
    }

    /**
     * @return Returns the docCodeSystem.
     */
    public String getDocCodeSystem() {
        return docCodeSystem;
    }

    /**
     * @param docCodeSystem The docCodeSystem to set.
     */
    public void setDocCodeSystem(String docCodeSystem) {
        this.docCodeSystem = docCodeSystem;
    }

    /**
     * @return Returns the docDisplayName.
     */
    public String getDocDisplayName() {
        return docDisplayName;
    }

    /**
     * @param docDisplayName The docDisplayName to set.
     */
    public void setDocDisplayName(String docDisplayName) {
        this.docDisplayName = docDisplayName;
    }

    /**
     * @param lowerDateTime The lowerDateTime to set.
     */
    public void setLowerDateTime(Date lowerDateTime) {
        this.lowerDateTime = lowerDateTime.getTime();
    }

    /**
     * @return Returns the mostRecentResults.
     */
    public int getMostRecentResults() {
        return mostRecentResults;
    }

    /**
     * @param mostRecentResults The mostRecentResults to set.
     */
    public void setMostRecentResults(int mostRecentResults) {
        this.mostRecentResults = mostRecentResults;
    }

    /**
     * @param upperDateTime The upperDateTime to set.
     */
    public void setUpperDateTime(Date upperDateTime) {
        this.upperDateTime = upperDateTime.getTime();
    }

    /**
     * @return Returns the xslFile.
     */
    public String getXslFile() {
        return xslFile;
    }

    /**
     * @param xslFile The xslFile to set.
     */
    public void setXslFile(String xslFile) {
        this.xslFile = xslFile;
    }

    /**
     * @return Returns the xslt.
     */
    public URL getXslt() {
        return xslt;
    }

    /**
     * Set the URL to an xsl file that is used to transform the xml result of this DocumentList.
     * 
     * @param xslt The xslt to set.
     * @throws MalformedURLException
     */
    public void setXslt(String xslt) throws MalformedURLException {
        if (xslt != null) {
            if (xslt.startsWith("http:")) {
                this.xslt = new URL(xslt);
            } else {
                this.xslt = new URL(getDocRIDUrl() + "/" + xslt);
            }
        } else {
            this.xslt = null;
        }
    }

    /**
     * @param reqURL The reqURL to set.
     */
    public void setReqURL(String reqURL) {
        this.reqURL = reqURL;
    }

    /**
     * @return Returns the docRIDUrl.
     */
    public String getDocRIDUrl() {
        return docRIDUrl;
    }

    /**
     * @param docRIDUrl The docRIDUrl to set.
     */
    public void setDocRIDUrl(String docRIDUrl) {
        this.docRIDUrl = docRIDUrl;
    }

    public void toXML(OutputStream out) throws TransformerConfigurationException, SAXException {
        SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();
        applyMostRecentResults();
        if (xslt != null) {
            try {
                th = tf.newTransformerHandler(new StreamSource(xslt.openStream(), xslt.toExternalForm()));
            } catch (IOException x) {
                log.error("Cant open xsl file:" + xslt, x);
            }
        } else {
            th = tf.newTransformerHandler();
            th.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
        }
        th.setResult(new StreamResult(out));
        th.startDocument();
        if (xslFile != null) {
            th.processingInstruction("xml-stylesheet", "href='" + xslFile + "' type='text/xsl'");
        }
        toXML();
        th.endDocument();
        try {
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("Could not close stream.", e);
        }
    }

    private void toXML() throws SAXException {
        util = new XMLUtil(th);
        util.startElement("IHEDocumentList", EMPTY_ATTRIBUTES);
        addDocCode();
        addActivityTime();
        Dataset ds;
        if (datasets.size() > 0) {
            ds = (Dataset) datasets.get(0);
        } else {
            if (queryDS != null) {
                ds = queryDS;
            } else {
                ds = DcmObjectFactory.getInstance().newDataset();
                ds.putLO(Tags.PatientID, "");
            }
        }
        addRecordTarget(ds);
        addAuthor();
        addDocuments();
        util.endElement("IHEDocumentList");
    }

    public void embedXML(TransformerHandler th) throws SAXException {
        this.th = th;
        toXML();
    }

    /**
     * @throws SAXException
     */
    private void addDocCode() throws SAXException {
        AttributesImpl attr = new AttributesImpl();
        if (docCode != null) util.addAttribute(attr, "code", docCode);
        if (docCodeSystem != null) util.addAttribute(attr, "codeSystem", docCodeSystem);
        if (docDisplayName != null) util.addAttribute(attr, "displayName", docDisplayName);
        util.startElement("code", attr);
        util.endElement("code");
    }

    /**
     * @throws SAXException
     */
    private void addActivityTime() throws SAXException {
        AttributesImpl attr = new AttributesImpl();
        util.addAttribute(attr, "value", DATETIME_FORMATTER.format(new Date()));
        util.startElement("activityTime", attr);
        util.endElement("activityTime");
    }

    /**
     * @throws SAXException
     */
    private void addRecordTarget(Dataset ds) throws SAXException {
        util.startElement("recordTarget", EMPTY_ATTRIBUTES);
        util.startElement("patient", EMPTY_ATTRIBUTES);
        AttributesImpl attrsPatID = new AttributesImpl();
        util.addAttribute(attrsPatID, "root", ds.getString(Tags.IssuerOfPatientID));
        util.addAttribute(attrsPatID, "extension", ds.getString(Tags.PatientID));
        util.startElement("id", attrsPatID);
        util.endElement("id");
        addPatientPatient(ds);
        util.startElement("providerOrganization", EMPTY_ATTRIBUTES);
        AttributesImpl attrsOrgID = new AttributesImpl();
        util.addAttribute(attrsOrgID, "id", "");
        util.startElement("id", attrsOrgID);
        util.endElement("id");
        util.startElement("name", EMPTY_ATTRIBUTES);
        String orgName = ds.getString(Tags.InstitutionName);
        if (orgName != null) th.characters(orgName.toCharArray(), 0, orgName.length());
        util.endElement("name");
        util.endElement("providerOrganization");
        util.endElement("patient");
        util.endElement("recordTarget");
    }

    /**
     * @throws SAXException
     */
    private void addPatientPatient(Dataset ds) throws SAXException {
        String familyName = "";
        String givenName = "";
        String genderCode = "121103";
        String birthDate = "";
        try {
            PersonName pn = ds.getPersonName(Tags.PatientName);
            if (pn != null) {
                familyName = pn.get(PersonName.FAMILY);
                givenName = pn.get(PersonName.GIVEN);
                if (givenName == null) givenName = "";
            }
            String s = ds.getString(Tags.PatientSex);
            if ("M".equals(s) || "F".equals(s)) genderCode = s;
            Date date = ds.getDate(Tags.PatientBirthDate);
            if (date != null) birthDate = DATE_FORMATTER.format(date);
        } catch (Exception x) {
            log.warn("Exception getting person informations:", x);
        }
        util.startElement("patientPatient", EMPTY_ATTRIBUTES);
        util.startElement("name", EMPTY_ATTRIBUTES);
        util.startElement("family", EMPTY_ATTRIBUTES);
        th.characters(familyName.toCharArray(), 0, familyName.length());
        util.endElement("family");
        util.startElement("given", EMPTY_ATTRIBUTES);
        th.characters(givenName.toCharArray(), 0, givenName.length());
        util.endElement("given");
        util.endElement("name");
        AttributesImpl attr = new AttributesImpl();
        util.addAttribute(attr, "code", genderCode);
        util.addAttribute(attr, "codeSystem", "1.2.840.10008.2.16.4");
        util.startElement("administrativeGenderCode", attr);
        util.endElement("administrativeGenderCode");
        AttributesImpl attrBirth = new AttributesImpl();
        util.addAttribute(attrBirth, "value", birthDate);
        util.startElement("birthTime", attrBirth);
        util.endElement("birthTime");
        util.endElement("patientPatient");
    }

    private void addAuthor() throws SAXException {
        util.startElement("author", EMPTY_ATTRIBUTES);
        util.startElement("noteText", EMPTY_ATTRIBUTES);
        AttributesImpl attr = new AttributesImpl();
        util.addAttribute(attr, "value", reqURL);
        util.startElement("reference", attr);
        util.endElement("reference");
        util.endElement("noteText");
        util.startElement("assignedAuthor", EMPTY_ATTRIBUTES);
        AttributesImpl attrsID = new AttributesImpl();
        util.addAttribute(attrsID, "root", "");
        util.addAttribute(attrsID, "extension", "");
        util.startElement("id", attrsID);
        util.endElement("id");
        util.startElement("assignedDevice", EMPTY_ATTRIBUTES);
        AttributesImpl attrsCode = new AttributesImpl();
        util.addAttribute(attrsCode, "code", "");
        util.addAttribute(attrsCode, "codeSystem", "");
        util.addAttribute(attrsCode, "displayName", "");
        util.startElement("code", attrsCode);
        util.endElement("code");
        util.startElement("manufacturerModelName", EMPTY_ATTRIBUTES);
        util.endElement("manufacturerModelName");
        util.startElement("softwareName", EMPTY_ATTRIBUTES);
        util.endElement("softwareName");
        util.endElement("assignedDevice");
        util.endElement("assignedAuthor");
        util.endElement("author");
    }

    private void addDocuments() throws SAXException {
        for (Dataset ds : datasets) {
            addComponent(ds);
        }
    }

    private void addComponent(Dataset ds) throws SAXException {
        String uid = ds.getString(Tags.SOPInstanceUID);
        if (uid == null) uid = "---";
        Date date = ds.getDateTime(Tags.ContentDate, Tags.ContentTime);
        if (date == null) {
            date = ds.getDate(Tags.AcquisitionDatetime);
        }
        String acquisTime = date == null ? "" : DATETIME_FORMATTER.format(date);
        String title = "DocumentTitle";
        String mime = ds.getString(Tags.MIMETypeOfEncapsulatedDocument, "application/pdf");
        String link = docRIDUrl + "/IHERetrieveDocument?requestType=DOCUMENT&documentUID=" + uid + "&preferredContentType=" + mime;
        util.startElement("component", EMPTY_ATTRIBUTES);
        util.startElement("documentInformation", EMPTY_ATTRIBUTES);
        AttributesImpl attrID = new AttributesImpl();
        util.addAttribute(attrID, "root", uid);
        util.startElement("id", attrID);
        util.endElement("id");
        addComponentCode(ds);
        util.startElement("title", EMPTY_ATTRIBUTES);
        th.characters(title.toCharArray(), 0, title.length());
        util.endElement("title");
        util.startElement("text", EMPTY_ATTRIBUTES);
        AttributesImpl attrTxt = new AttributesImpl();
        util.addAttribute(attrTxt, "value", link);
        util.startElement("reference", attrTxt);
        util.endElement("reference");
        util.endElement("text");
        addComponentStatusCode(ds);
        AttributesImpl attrEff = new AttributesImpl();
        util.addAttribute(attrEff, "value", acquisTime);
        util.startElement("effectiveTime", attrEff);
        util.endElement("effectiveTime");
        util.endElement("documentInformation");
        util.endElement("component");
    }

    /**
     * @param ds
     * @throws SAXException
     */
    private void addComponentStatusCode(Dataset ds) throws SAXException {
        String statusCode = ds.getString(Tags.CompletionFlag, "") + "/" + ds.getString(Tags.VerificationFlag, "");
        AttributesImpl attrStatusCode = new AttributesImpl();
        util.addAttribute(attrStatusCode, "code", statusCode);
        util.addAttribute(attrStatusCode, "codeSystem", "");
        util.startElement("statusCode", attrStatusCode);
        util.endElement("statusCode");
    }

    /**
     * @param ds
     * @throws SAXException
     */
    private void addComponentCode(Dataset ds) throws SAXException {
        String code = "";
        String codeSystem = "";
        String displayname = "";
        String cuid = ds.getString(Tags.SOPClassUID);
        if ("SR".equals(ds.getString(Tags.Modality)) || (this.docSopCuids != null && docSopCuids.contains(cuid))) {
            DcmElement elem = ds.get(Tags.ConceptNameCodeSeq);
            if (elem != null && elem.countItems() > 0) {
                Dataset ds1 = elem.getItem(0);
                code = ds1.getString(Tags.CodeValue);
                codeSystem = ds1.getString(Tags.CodingSchemeDesignator);
                displayname = ds1.getString(Tags.CodeMeaning);
            } else {
                displayname = ds.getString(Tags.DocumentTitle);
            }
            if (displayname == null) displayname = ds.getString(Tags.StudyDescription);
        } else {
            if (UIDs.TwelveLeadECGWaveformStorage.equals(cuid)) displayname = "12-lead ECG"; else if (UIDs.GeneralECGWaveformStorage.equals(cuid)) displayname = "General ECG"; else if (UIDs.AmbulatoryECGWaveformStorage.equals(cuid)) displayname = "Ambulatory ECG"; else if (UIDs.HemodynamicWaveformStorage.equals(cuid)) displayname = "Hemodynamic"; else if (UIDs.CardiacElectrophysiologyWaveformStorage.equals(cuid)) displayname = "Cardiac Electrophysiology";
        }
        AttributesImpl attrCode = new AttributesImpl();
        util.addAttribute(attrCode, "code", code);
        util.addAttribute(attrCode, "codeSystem", codeSystem);
        util.addAttribute(attrCode, "displayName", displayname);
        util.startElement("code", attrCode);
        util.endElement("code");
    }

    public class DatasetDateComparator implements Comparator<Dataset> {

        public DatasetDateComparator() {
        }

        /**
         * Compares the modification time of two File objects.
         * <p>
         * Compares its two arguments for order. Returns a negative integer, zero, or a positive integer 
         * as the first argument is less than, equal to, or greater than the second.
         * <p>
         * Throws an Exception if one of the arguments is null or not a Dataset object.
         *  
         * @param arg0 	First argument
         * @param arg1	Second argument
         * 
         * @return <0 if arg0<arg1, 0 if equal and >0 if arg0>arg1
         */
        public int compare(Dataset arg0, Dataset arg1) {
            Date d1 = getDateFromDS((Dataset) arg0);
            if (d1 == null) return 1;
            Date d2 = getDateFromDS((Dataset) arg1);
            return d2 == null ? -1 : d2.compareTo(d1);
        }
    }

    public void setDocSopCuids(Collection<String> cuids) {
        docSopCuids = cuids;
    }
}
