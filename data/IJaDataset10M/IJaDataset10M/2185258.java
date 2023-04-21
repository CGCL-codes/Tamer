package gov.lanl.ingest.oaitape.aps;

import gov.lanl.arc.heritrixImpl.ARCFileWriter;
import gov.lanl.ingest.IngestProperties;
import gov.lanl.ingest.oaitape.DerefProcessor;
import gov.lanl.ingest.oaitape.IngestException;
import gov.lanl.ingest.oaitape.IngestRecord;
import gov.lanl.ingest.oaitape.ProcessInfo;
import gov.lanl.ingest.oaitape.SigUtils;
import gov.lanl.util.DigestUtil;
import gov.lanl.util.StreamUtil;
import gov.lanl.util.csv.CSVWriter;
import gov.lanl.util.uuid.UUIDFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import org.apache.log4j.Logger;
import org.apache.xml.security.signature.SignedInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.Constants;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * DidlSigProcessor is a plugin for DIDL metadata with xmlsignatures
 */
public class DidlSigProcessor extends DerefProcessor {

    private static final String SIGNS = Constants.SignatureSpecNS;

    static javax.xml.parsers.DocumentBuilderFactory dbf = null;

    static Logger log = Logger.getLogger(DidlSigProcessor.class.getName());

    private static CSVWriter logger = null;

    private boolean debug = true;

    private boolean debugInit = false;

    static {
        dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);
        try {
            if (logger != null) logger.close();
            PrintWriter pw = new PrintWriter(new FileOutputStream(new File("benchmark.csv")));
            logger = new CSVWriter(pw);
            logger.writeCommentln("pk, id, type, count");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int cnt = 0;

    public void finalize() {
        if (logger != null) logger.close();
    }

    /**
     * this method is need to be implemented for specific metadata
     */
    public ProcessInfo processContent(String record, ARCFileWriter arcwriter) {
        ProcessInfo info = new ProcessInfo();
        IngestRecord csv = null;
        try {
            long start = System.currentTimeMillis();
            OAIRecordParser oairecord = new OAIRecordParser(record);
            if (debug) {
                cnt++;
                logger.writeln(cnt + ",,2," + getDuration(start));
            }
            String metadata = oairecord.getMetadata();
            SigUtils util = new SigUtils();
            javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new org.apache.xml.security.utils.IgnoreAllErrorHandler());
            InputSource IS = new InputSource(new StringReader(metadata));
            org.w3c.dom.Document doc = db.parse(IS);
            if (!util.verify_record(record)) {
                String msg = "Metadata Verification Problem:" + util.getMessage();
                info.setStatus(false);
                info.setMessage(msg);
                throw new IngestException(msg);
            }
            NodeList cnodes = doc.getElementsByTagNameNS(IngestProperties.DIDL_NS, "Component");
            for (int i = 0; i < cnodes.getLength(); i++) {
                csv = new IngestRecord();
                Element cElement = (Element) cnodes.item(i);
                NodeList rnodes = cElement.getElementsByTagNameNS(IngestProperties.DIDL_NS, "Resource");
                Element resource = (Element) rnodes.item(0);
                String ref = resource.getAttribute("ref");
                String mimetype = resource.getAttribute("mimeType");
                if (ref != null) {
                    String derefxpath = "//didl:Component[" + i + "]/didl:Resource[0]/@ref";
                    csv.setRef(ref);
                    csv.setDerefXPath(derefxpath);
                } else {
                    String derefxpath = "//didl:Component[" + i + "]/didl:Resource[0]/*";
                    csv.setDerefXPath(derefxpath);
                }
                Element sigElement = null;
                byte streamsigned[] = null;
                Constants.setSignatureSpecNSprefix("dsig");
                NodeList nodes = cElement.getElementsByTagNameNS(SIGNS, "Signature");
                if (nodes.getLength() < 1) {
                    String msg = "validation problem: xmlsignature missing for element";
                    throw new IngestException(msg);
                }
                sigElement = (Element) nodes.item(0);
                XMLSignature signature = new XMLSignature(sigElement, "");
                SignedInfo xmlsiginfo = signature.getSignedInfo();
                String apsurl = xmlsiginfo.item(0).getURI();
                if (ref != null) {
                    if (!ref.equals(apsurl)) {
                        String message = "validation problem:" + "ref attribute diffs from sig url";
                        throw new IngestException(message);
                    }
                }
                long time = System.currentTimeMillis();
                boolean valid = false;
                if (ref.contains(".gz&")) {
                    log.debug("Validation Problem: " + ref);
                    streamsigned = StreamUtil.getByteArray(new GZIPInputStream(StreamUtil.getInputStream(new URL(ref))));
                    if (debug) {
                        logger.writeln(new String[] { Integer.toString(cnt), ref, Integer.toString(7), Long.toString(getDuration(time)) });
                        double size = streamsigned.length / 1024;
                        double dur = getDuration(time) / 1000;
                        String calc = String.valueOf(size / dur);
                        if (!calc.contains("Infinity")) {
                            logger.writeln(new String[] { Integer.toString(cnt), ref, Integer.toString(8), calc });
                            System.out.println("**** " + String.valueOf(size / dur) + " ****");
                        }
                    }
                    csv.setDigest("urn:sha1:" + DigestUtil.getSHA1Digest(streamsigned));
                    String arcid = IngestProperties.getLocalDataStreamPrefix() + UUIDFactory.generateUUID().toString().substring(9);
                    time = System.currentTimeMillis();
                    arcwriter.write(arcid, "0.0.0.0", mimetype, streamsigned);
                    if (debug) {
                        logger.writeln(new String[] { Integer.toString(cnt), ref, Integer.toString(4), Long.toString(getDuration(time)) });
                        logger.writeln(new String[] { Integer.toString(cnt), ref, Integer.toString(5), Integer.toString(streamsigned.length) });
                    }
                    csv.setLocalIdentifier(arcid);
                    csv.setSuccess(true);
                    String[] strArray = csv.toArray();
                    info.setStatus(true);
                    info.addLogInfo(strArray);
                } else {
                    valid = util.verify(signature);
                    if (valid) {
                        time = System.currentTimeMillis();
                        streamsigned = xmlsiginfo.getSignedContentItem(0);
                        if (debug) {
                            logger.writeln(new String[] { Integer.toString(cnt), ref, Integer.toString(7), Long.toString(getDuration(time)) });
                            double size = streamsigned.length / 1024;
                            double dur = getDuration(time) / 1000;
                            String calc = String.valueOf(size / dur);
                            if (!ref.contains(".gz") && !calc.contains("Infinity")) {
                                logger.writeln(new String[] { Integer.toString(cnt), ref, Integer.toString(8), calc });
                                System.out.println("**** " + String.valueOf(size / dur) + " ****");
                            }
                        }
                        csv.setDigest("urn:sha1:" + util.getDigest(xmlsiginfo));
                        String arcid = IngestProperties.getLocalDataStreamPrefix() + UUIDFactory.generateUUID().toString().substring(9);
                        time = System.currentTimeMillis();
                        arcwriter.write(arcid, "0.0.0.0", mimetype, streamsigned);
                        if (debug) {
                            logger.writeln(new String[] { Integer.toString(cnt), ref, Integer.toString(4), Long.toString(getDuration(time)) });
                            logger.writeln(new String[] { Integer.toString(cnt), ref, Integer.toString(5), Integer.toString(streamsigned.length) });
                        }
                        csv.setLocalIdentifier(arcid);
                        csv.setSuccess(true);
                        String[] strArray = csv.toArray();
                        info.setStatus(true);
                        info.addLogInfo(strArray);
                    } else {
                        String msg = util.getMessage();
                        csv.setMessage(msg + apsurl);
                        throw new IngestException(msg);
                    }
                }
            }
            if (debug) {
                logger.writeln(new String[] { Integer.toString(cnt), null, Integer.toString(1), Long.toString(getDuration(start)) });
                logger.writeln(new String[] { Integer.toString(cnt), null, Integer.toString(6), Long.toString(getMemoryStatus()) });
            }
            long end = System.currentTimeMillis();
            double elapsed = end - start;
            log.info("totaltime:" + elapsed);
        } catch (Exception e) {
            if (e.getMessage() != null) csv.setMessage(e.getMessage()); else csv.setMessage("");
            csv.setSuccess(false);
            String[] strArray = new String[1];
            strArray[0] = csv.getMessage();
            info.setStatus(false);
            info.addLogInfo(strArray);
            info.setMessage("DidlSigProcessor Exception:" + e.getMessage());
            log.warn("DidlSigProcessor:" + Arrays.toString((String[]) strArray));
        }
        return info;
    }

    private String getAboutsStr(ArrayList abouts) {
        StringBuffer aboutsb = new StringBuffer();
        aboutsb.append("<abouts>");
        String xmlsigabout;
        for (int j = 0; j < abouts.size(); j++) {
            aboutsb.append(abouts.get(j));
            xmlsigabout = (String) abouts.get(j);
            if (xmlsigabout.indexOf("<dsig:Signature") > 0) {
                return xmlsigabout;
            }
        }
        aboutsb.append("</abouts>");
        return abouts.toString();
    }

    private long getDuration(long start) {
        long end = System.currentTimeMillis();
        return end - start;
    }

    private static long getMemoryStatus() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long usedMemory = totalMemory - runtime.freeMemory();
        return usedMemory;
    }
}
