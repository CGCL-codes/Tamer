package at.co.ait.domain.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.io.FileUtils;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.DOMOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import at.co.ait.domain.oais.DigitalObject;
import at.co.ait.domain.oais.DigitalObjectType;
import at.co.ait.domain.oais.InformationPackageObject;
import at.co.ait.utils.ConfigUtils;
import at.co.ait.utils.DOM;
import at.co.ait.utils.TikaUtils;
import at.co.ait.web.common.UserPreferences;
import au.edu.apsr.mtk.base.Agent;
import au.edu.apsr.mtk.base.Div;
import au.edu.apsr.mtk.base.DmdSec;
import au.edu.apsr.mtk.base.FLocat;
import au.edu.apsr.mtk.base.FileGrp;
import au.edu.apsr.mtk.base.FileSec;
import au.edu.apsr.mtk.base.METS;
import au.edu.apsr.mtk.base.METSException;
import au.edu.apsr.mtk.base.METSWrapper;
import au.edu.apsr.mtk.base.MdWrap;
import au.edu.apsr.mtk.base.MetsHdr;
import au.edu.apsr.mtk.base.StructMap;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class FedoraMetsMarshallerService {

    private static final Logger logger = LoggerFactory.getLogger(FedoraMetsMarshallerService.class);

    private METS mets = null;

    private METSWrapper createMETS(InformationPackageObject obj, UserPreferences prefs) throws JDOMException, ParserConfigurationException, METSException, IOException, SAXException {
        METSWrapper mw = null;
        mw = new METSWrapper();
        mets = mw.getMETSObject();
        mets.setObjID("BHLE" + obj.getIdentifier().replace('/', ':'));
        mets.setProfile("http://www.bhl-europe.eu/profiles/bhle-mets-profile-1.0");
        mets.setType("FedoraObject");
        mets.setLabel("External ID: " + obj.getExternalIdentifier() + ", " + "Collection: " + prefs.getOrganization() + ", " + "ID: " + obj.getIdentifier());
        MetsHdr mh = mets.newMetsHdr();
        mh.setRecordStatus("A");
        Agent agent = mh.newAgent();
        agent.setRole("IPOWNER");
        agent.setType("ORGANIZATION");
        agent.setName(prefs.getOrganization());
        agent.setNote("BHL-Europe Content Provider");
        mh.addAgent(agent);
        mets.setMetsHdr(mh);
        DmdSec dmd = null;
        boolean addDC = true;
        for (DigitalObject digobj : obj.getDigitalobjects()) {
            if (digobj.getObjecttype() == DigitalObjectType.METADATA) {
                String label = "derivative";
                File marc = digobj.getSubmittedFile();
                dmd = newDmdEntry(digobj.getOrder(), "OTHER", label, digobj.getSmtoutput());
                mets.addDmdSec(dmd);
                if (addDC) {
                    File dublinCore = ConfigUtils.getAipFile(digobj.getPrefs().getBasedirectoryFile(), marc, "_dc.xml");
                    extractDublinCore(digobj, dublinCore);
                    if (dublinCore.exists()) {
                        DmdSec dmdDC = mets.newDmdSec();
                        dmdDC.setID("DC");
                        MdWrap mdw = dmdDC.newMdWrap();
                        mdw.setMDType("DC");
                        mdw.setLabel(label + "-DC");
                        mdw.setXmlData(DOM.parse(dublinCore).getDocumentElement());
                        dmdDC.setMdWrap(mdw);
                        mets.addDmdSec(dmdDC);
                        addDC = false;
                    }
                }
            }
        }
        FileSec fs = mets.newFileSec();
        FileGrp datastream = fs.newFileGrp();
        String id = "DATASTREAMS";
        datastream.setID(id);
        for (DigitalObjectType value : DigitalObjectType.values()) {
            for (DigitalObject digobj : obj.getDigitalobjects()) {
                if (digobj.getObjecttype().equals(value)) {
                    FileGrp fg = fs.newFileGrp();
                    fg.setID(value.name() + "." + digobj.getOrder());
                    au.edu.apsr.mtk.base.File f = fg.newFile();
                    f.setID(value.name() + "." + digobj.getOrder() + ".FILE");
                    f.setSize(FileUtils.sizeOf(digobj.getSubmittedFile()));
                    f.setMIMEType(digobj.getMimetype());
                    f.setChecksum(digobj.getDigestValueinHex());
                    f.setChecksumType("SHA-1");
                    f.setOwnerID("M");
                    FLocat loc = createLocat(digobj.getSubmittedFile(), prefs.getBasedirectory(), f);
                    f.addFLocat(loc);
                    fg.addFile(f);
                    datastream.addFileGrp(fg);
                }
            }
        }
        for (DigitalObject digobj : obj.getDigitalobjects()) {
            if (digobj.getTaxa() != null) {
                FileGrp fg = fs.newFileGrp();
                fg.setID("TAXA." + digobj.getOrder());
                au.edu.apsr.mtk.base.File f = fg.newFile();
                f.setID("TAXA." + digobj.getOrder() + ".FILE");
                f.setSize(FileUtils.sizeOf(digobj.getTaxa()));
                f.setMIMEType(TikaUtils.detectedMimeType(digobj.getTaxa()));
                f.setOwnerID("M");
                FLocat loc = createLocat(digobj.getTaxa(), prefs.getBasedirectory(), f);
                f.addFLocat(loc);
                fg.addFile(f);
                datastream.addFileGrp(fg);
            }
        }
        for (DigitalObject digobj : obj.getDigitalobjects()) {
            if (digobj.getOcr() != null) {
                FileGrp fg = fs.newFileGrp();
                fg.setID("OCR." + digobj.getOrder());
                au.edu.apsr.mtk.base.File f = fg.newFile();
                f.setID("OCR." + digobj.getOrder() + ".FILE");
                f.setSize(FileUtils.sizeOf(digobj.getOcr()));
                f.setMIMEType(TikaUtils.detectedMimeType(digobj.getOcr()));
                f.setOwnerID("M");
                FLocat loc = createLocat(digobj.getOcr(), prefs.getBasedirectory(), f);
                f.addFLocat(loc);
                fg.addFile(f);
                datastream.addFileGrp(fg);
            }
        }
        for (DigitalObject digobj : obj.getDigitalobjects()) {
            if (digobj.getTechMetadata() != null) {
                FileGrp fg = fs.newFileGrp();
                fg.setID("JHOVE." + digobj.getOrder());
                au.edu.apsr.mtk.base.File f = fg.newFile();
                f.setID("JHOVE." + digobj.getOrder() + ".FILE");
                f.setSize(FileUtils.sizeOf(digobj.getTechMetadata()));
                f.setOwnerID("M");
                f.setMIMEType(TikaUtils.detectedMimeType(digobj.getTechMetadata()));
                FLocat loc = createLocat(digobj.getTechMetadata(), prefs.getBasedirectory(), f);
                f.addFLocat(loc);
                fg.addFile(f);
                datastream.addFileGrp(fg);
            }
        }
        FileGrp fg = fs.newFileGrp();
        fg.setID("NFO");
        au.edu.apsr.mtk.base.File f = fg.newFile();
        f.setID("NFO.FILE");
        f.setSize(FileUtils.sizeOf(obj.getNepomukFileOntology()));
        f.setOwnerID("M");
        f.setMIMEType(TikaUtils.detectedMimeType(obj.getNepomukFileOntology()));
        FLocat loc = createLocat(obj.getNepomukFileOntology(), prefs.getBasedirectory(), f);
        f.addFLocat(loc);
        fg.addFile(f);
        datastream.addFileGrp(fg);
        fs.addFileGrp(datastream);
        mets.setFileSec(fs);
        StructMap sm = mets.newStructMap();
        Div d = sm.newDiv();
        sm.addDiv(d);
        mets.addStructMap(sm);
        mw.validate();
        return mw;
    }

    private void extractDublinCore(DigitalObject digobj, File output) {
        File marc21File = digobj.getSubmittedFile();
        Document marc21 = DOM.parse(marc21File);
        if (marc21 == null) {
            logger.warn(marc21File.getAbsolutePath() + " is no valid XML");
            return;
        }
        try {
            Transformer marc21ToDC = DOM.getTransformer(new File(this.getClass().getResource("/gov/loc/MARC21slim2OAIDC.xsl").toURI()));
            marc21ToDC.transform(new DOMSource(marc21), new StreamResult(output));
            if (output.exists()) {
                Document dc = DOM.parse(output);
                if (dc == null || dc.getDocumentElement() == null || !dc.getDocumentElement().hasChildNodes()) {
                    output.delete();
                } else {
                    File basedir = digobj.getPrefs().getBasedirectoryFile();
                    XPathExpression findIdentifier = DOM.getXPath("//dc:identifier");
                    search_dp_info: for (File folder = marc21File; (folder = folder.getParentFile()) != null; ) {
                        File dp = new File(folder, ".dataprovider");
                        if (dp.isDirectory()) {
                            for (File dpInfo : dp.listFiles()) {
                                if (!dpInfo.getName().toLowerCase().endsWith(".xml")) continue;
                                Document dpDC = DOM.parse(dpInfo);
                                if (dpDC == null) continue;
                                NodeList ids = (NodeList) findIdentifier.evaluate(dpDC, XPathConstants.NODESET);
                                if (ids.getLength() == 0) continue;
                                Node insPos = DOM.xpathSingle("//dc:*[position()=last()]", dc);
                                for (int ofs = ids.getLength(); ofs-- > 0; ) {
                                    Element dpIdentifier = (Element) ids.item(0);
                                    Node insertee = dc.createElementNS(DOM.NS.dc.URI, "dc:relation");
                                    insertee.appendChild(dc.createTextNode(dpIdentifier.getTextContent()));
                                    insPos.getParentNode().insertBefore(insertee, insPos.getNextSibling());
                                }
                                break search_dp_info;
                            }
                        }
                        if (folder.equals(basedir)) break;
                    }
                    DOM.save(dc, output);
                }
            }
        } catch (TransformerException e) {
            logger.warn("Transformation of " + marc21File.getAbsolutePath() + " to DC failed: " + e.getMessage());
        } catch (URISyntaxException e) {
            logger.error("Missing XSL file " + e.getMessage());
        } catch (IOException e) {
            logger.warn("Transformation of " + marc21File.getAbsolutePath() + " to DC failed: " + e.getMessage());
        } catch (XPathExpressionException e) {
            logger.error("XPath invalid: " + e.getMessage());
        }
    }

    private DmdSec newDmdEntry(int id, String type, String lbl, File metadata) throws METSException, JDOMException, IOException {
        SAXBuilder parser = new SAXBuilder();
        org.jdom.Document modsdoc = parser.build(metadata);
        DmdSec dmd = mets.newDmdSec();
        dmd.setID(lbl + "-" + id);
        MdWrap mdw = dmd.newMdWrap();
        mdw.setMDType(type);
        mdw.setLabel(lbl);
        DOMOutputter domOutputter = new DOMOutputter();
        org.w3c.dom.Document w3cDoc = domOutputter.output(modsdoc);
        mdw.setXmlData(w3cDoc.getDocumentElement());
        dmd.setMdWrap(mdw);
        return dmd;
    }

    private FLocat createLocat(File file, String basedir, au.edu.apsr.mtk.base.File f) throws METSException, IOException {
        String fileurl = ConfigUtils.createFileURL(file);
        FLocat loc = f.newFLocat();
        loc.setHref(fileurl);
        loc.setLocType("URL");
        return loc;
    }

    /**
	 * Enriches the information package object by adding METS metadata
	 * 
	 * @param infPkg
	 * @return
	 * @throws ParserConfigurationException
	 * @throws JDOMException
	 * @throws IOException
	 * @throws SAXException
	 * @throws METSException
	 */
    public InformationPackageObject marshal(InformationPackageObject infPkg, UserPreferences prefs) throws JDOMException, ParserConfigurationException, IOException, METSException, SAXException {
        logger.info("Fedora METS Marshaller");
        infPkg.setMets(createMETS(infPkg, prefs));
        File metsfile = ConfigUtils.getAipFile(prefs.getBasedirectoryFile(), infPkg.getSubmittedFile(), ".mets.xml");
        infPkg.setMetsfileurl(ConfigUtils.createFileURL(metsfile));
        Document doc = infPkg.getMets().getMETSDocument();
        Element metsRoot = doc.getDocumentElement();
        metsRoot.setAttribute("EXT_VERSION", "1.1");
        metsRoot.setAttribute("xmlns", "http://www.loc.gov/METS/");
        metsRoot.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
        metsRoot.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        metsRoot.setAttribute("xmlns:schemaLocation", "http://www.loc.gov/METS/ http://www.fedora.info/definitions/1/0/mets-fedora-ext1-1.xsd");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"));
        String currentTime = df.format(cal.getTime());
        int idNum = 0;
        for (Node node = metsRoot.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (!"dmdSec".equals(node.getNodeName())) continue;
            Element dmdSec = (Element) node;
            Element descMD = doc.createElement("descMD");
            if ("DC".equals(dmdSec.getAttribute("ID"))) {
                descMD.setAttribute("ID", "DC1.0");
            } else {
                descMD.setAttribute("ID", "DESC." + String.valueOf(idNum++));
            }
            for (Node ch = node.getFirstChild(); ch != null; ch = ch.getNextSibling()) {
                descMD.appendChild(ch);
            }
            node.appendChild(descMD);
            doc.renameNode(node, "", "dmdSecFedora");
        }
        String remElement = "structMap";
        Element structMap = (Element) doc.getElementsByTagName(remElement).item(0);
        structMap.getParentNode().removeChild(structMap);
        doc.normalize();
        OutputFormat format = new OutputFormat(doc);
        format.setLineWidth(65);
        format.setIndenting(true);
        format.setIndent(2);
        FileOutputStream out = FileUtils.openOutputStream(metsfile);
        XMLSerializer serializer = new XMLSerializer(out, format);
        serializer.serialize(doc);
        out.close();
        return infPkg;
    }
}
