package org.dcm4chee.xds.repository.mbean;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import org.dcm4chee.xds.repository.XDSDocumentWriter;
import org.dcm4chee.xds.repository.XDSDocumentWriterFactory;
import org.jboss.system.ServiceMBeanSupport;
import org.jboss.system.server.ServerConfigLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * 
 * @author franz.willer@agfa.com
 * @version $Revision$ $Date$
 * @since 07.09.2007
 */
public class Store2DcmService extends ServiceMBeanSupport {

    private static final String DEFAULT_XSL_RESOURCE = "submission2dcm.xsl";

    private static final String DEFAULT = "DEFAULT";

    private String xslFilename;

    private String xmlFilename;

    private static final XDSDocumentWriterFactory fac = XDSDocumentWriterFactory.getInstance();

    private static final String NONE = "NONE";

    static Logger log = LoggerFactory.getLogger(Store2DcmService.class);

    private DocumentSendCfg dcmCfg = new DocumentSendCfg();

    private StgCmtSCU stgCmtScu = new StgCmtSCU(dcmCfg);

    public String getXslFile() {
        return xslFilename;
    }

    public void setXslFile(String name) {
        this.xslFilename = name;
    }

    public String getXmlFile() {
        return xmlFilename;
    }

    public void setXmlFile(String name) {
        this.xmlFilename = name;
    }

    public final void setCalledAET(String called) {
        dcmCfg.setCalledAET(called);
    }

    public String getCalledAET() {
        return dcmCfg.getCalledAET();
    }

    public final void setRemoteHost(String hostname) {
        dcmCfg.setRemoteHost(hostname);
    }

    public String getRemoteHost() {
        return dcmCfg.getRemoteHost();
    }

    public final void setRemotePort(int port) {
        dcmCfg.setRemotePort(port);
    }

    public int getRemotePort() {
        return dcmCfg.getRemotePort();
    }

    public final void setCallingAET(String calling) {
        dcmCfg.setCallingAET(calling);
    }

    public String getCallingAET() {
        return dcmCfg.getCallingAET();
    }

    public final void setLocalHost(String hostname) {
        dcmCfg.setLocalHost(hostname);
    }

    public String getLocalHost() {
        return dcmCfg.getLocalHost();
    }

    public final void setLocalPort(int port) {
        dcmCfg.setLocalPort(port);
    }

    public int getLocalPort() {
        return dcmCfg.getLocalPort();
    }

    public void setKeyStoreURL(String url) {
        dcmCfg.setKeyStoreURL(url);
    }

    public String getKeyStoreURL() {
        return dcmCfg.getKeyStoreURL();
    }

    public final void setKeyStorePassword(String pw) {
        dcmCfg.setKeyStorePassword(pw);
    }

    public String getKeyStorePassword() {
        return dcmCfg.getKeyStorePassword();
    }

    public final void setKeyPassword(String pw) {
        dcmCfg.setKeyPassword(pw);
    }

    public String getKeyPassword() {
        return dcmCfg.getKeyPassword();
    }

    public final void setTrustStorePassword(String pw) {
        dcmCfg.setTrustStorePassword(pw);
    }

    public String getTrustStorePassword() {
        return dcmCfg.getTrustStorePassword();
    }

    public final void setTrustStoreURL(String url) {
        dcmCfg.setTrustStoreURL(url);
    }

    public String getTrustStoreURL() {
        return dcmCfg.getTrustStoreURL();
    }

    public void setUsername(String username) {
        dcmCfg.setUsername(NONE.equals(username) ? null : username);
    }

    public String getUsername() {
        String user = dcmCfg.getUsername();
        return user != null ? user : NONE;
    }

    public void setPasscode(String passcode) {
        dcmCfg.setPasscode(passcode);
    }

    public String getPasscode() {
        return dcmCfg.getPasscode();
    }

    public final void setShutdownDelay(long shutdownDelay) {
        dcmCfg.setShutdownDelay(shutdownDelay);
    }

    public long getShutdownDelay() {
        return dcmCfg.getShutdownDelay();
    }

    public final void setConnectTimeout(int connectTimeout) {
        dcmCfg.setConnectTimeout(connectTimeout);
    }

    public long getConnectTimeout() {
        return dcmCfg.getConnectTimeout();
    }

    public final void setMaxPDULengthReceive(int maxPDULength) {
        dcmCfg.setMaxPDULengthReceive(maxPDULength);
    }

    public int getMaxPDULengthReceive() {
        return dcmCfg.getMaxPDULengthReceive();
    }

    public final void setMaxOpsInvoked(int maxOpsInvoked) {
        dcmCfg.setMaxOpsInvoked(maxOpsInvoked);
    }

    public int getMaxOpsInvoked() {
        return dcmCfg.getMaxOpsInvoked();
    }

    public final void setPackPDV(boolean packPDV) {
        dcmCfg.setPackPDV(packPDV);
    }

    public boolean isPackPDV() {
        return dcmCfg.isPackPDV();
    }

    public final void setDimseRspTimeout(int timeout) {
        dcmCfg.setDimseRspTimeout(timeout);
    }

    public int getDimseRspTimeout() {
        return dcmCfg.getDimseRspTimeout();
    }

    public final void setPriority(int priority) {
        dcmCfg.setPriority(priority);
    }

    public int getPriority() {
        return dcmCfg.getPriority();
    }

    public final void setTcpNoDelay(boolean tcpNoDelay) {
        dcmCfg.setTcpNoDelay(tcpNoDelay);
    }

    public boolean isTcpNoDelay() {
        return dcmCfg.isTcpNoDelay();
    }

    public final void setAcceptTimeout(int timeout) {
        dcmCfg.setAcceptTimeout(timeout);
    }

    public int getAcceptTimeout() {
        return dcmCfg.getAcceptTimeout();
    }

    public final void setReleaseTimeout(int timeout) {
        dcmCfg.setReleaseTimeout(timeout);
    }

    public int getReleaseTimeout() {
        return dcmCfg.getReleaseTimeout();
    }

    public final void setStgCmtTimeout(int timeout) {
        stgCmtScu.setStgCmtTimeout(timeout);
    }

    public long getStgCmtTimeout() {
        return stgCmtScu.getStgCmtTimeout();
    }

    public final void setSocketCloseDelay(int timeout) {
        dcmCfg.setSocketCloseDelay(timeout);
    }

    public int getSocketCloseDelay() {
        return dcmCfg.getSocketCloseDelay();
    }

    public final void setMaxPDULengthSend(int maxPDULength) {
        dcmCfg.setMaxPDULengthSend(maxPDULength);
    }

    public int getMaxPDULengthSend() {
        return dcmCfg.getMaxPDULengthSend();
    }

    public final void setReceiveBufferSize(int bufferSize) {
        dcmCfg.setReceiveBufferSize(bufferSize);
    }

    public int getReceiveBufferSize() {
        return dcmCfg.getReceiveBufferSize();
    }

    public final void setSendBufferSize(int bufferSize) {
        dcmCfg.setSendBufferSize(bufferSize);
    }

    public int getSendBufferSize() {
        return dcmCfg.getSendBufferSize();
    }

    public String getMime2CuidMap() {
        return map2String(Store2Dcm.getMime2CuidMap());
    }

    public void setMime2CuidMap(String s) {
        string2Map(s, Store2Dcm.getMime2CuidMap());
    }

    public boolean isRequestStgCmt() {
        return stgCmtScu.isRequestStgCmt();
    }

    public void setRequestStgCmt(boolean requestStgCmt) throws IOException {
        if (stgCmtScu.setRequestStgCmt(requestStgCmt)) {
            if (requestStgCmt) {
                stgCmtScu.startStgCmtListener();
            } else {
                stgCmtScu.stopStgCmtListener();
            }
        }
    }

    public byte[] storeDocument(String docFilename, String submissionsetFilename) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        File docFile = locateFile(docFilename);
        File submFile = locateFile(submissionsetFilename);
        FileInputStream submStream = submFile == null ? null : new FileInputStream(submFile);
        XDSDocumentWriter docWriter = fac.getDocumentWriter(docFile);
        Store2Dcm store = new Store2Dcm(locateFile(xmlFilename), docWriter, new StreamSource(submStream), locateFile(xslFilename));
        byte[] hash = new DocumentSend(dcmCfg).sendDocument(store);
        return hash;
    }

    public byte[] storeDocument(AttachmentPart doc, Source metadata) throws ParserConfigurationException, SAXException, IOException, TransformerException, SOAPException {
        XDSDocumentWriter docWriter = fac.getDocumentWriter(doc);
        Store2Dcm store = new Store2Dcm(locateFile(xmlFilename), docWriter, metadata, locateFileAsSource(xslFilename, DEFAULT_XSL_RESOURCE));
        return store(store);
    }

    private byte[] store(Store2Dcm store) {
        DocumentSend docSend = null;
        try {
            docSend = new DocumentSend(dcmCfg);
            byte[] hash = docSend.sendDocument(store);
            if (hash == null) return null;
            if (stgCmtScu != null && stgCmtScu.isRequestStgCmt()) {
                String tuid = stgCmtScu.requestStgCmt(docSend.getActiveAssociation(), store.getSOPClassUID(), store.getSOPInstanceUID());
                log.info("StgCmt Request with Transaction UID:" + tuid);
                if (tuid != null) {
                    if (stgCmtScu.isStgCmtSynchronized()) {
                        log.info("StgCmtSynchronized! Waiting for Storage Commitment Result..");
                        try {
                            stgCmtScu.waitForStgCmtResult(store);
                            log.info("store:" + store);
                            if (store.isCommitted()) {
                                return hash;
                            }
                        } catch (InterruptedException e) {
                            log.error("ERROR:" + e.getMessage());
                        }
                    } else {
                        return hash;
                    }
                }
            } else {
                store.setCommitted(true);
                return hash;
            }
            return hash;
        } finally {
            if (docSend != null) docSend.close();
        }
    }

    private static Source locateFileAsSource(String name, String defaultResource) throws FileNotFoundException {
        if (name == null || NONE.equalsIgnoreCase(name)) return null;
        if (DEFAULT.equalsIgnoreCase(name)) {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            return new StreamSource(cl.getResourceAsStream(defaultResource));
        } else {
            return new StreamSource(new BufferedInputStream(new FileInputStream(locateFile(name))));
        }
    }

    private static File locateFile(String name) {
        if (name == null) return null;
        File f = new File(name);
        if (!f.isAbsolute()) {
            f = new File(ServerConfigLocator.locate().getServerHomeDir(), f.getPath());
        }
        return f;
    }

    private String map2String(Map map) {
        if (map == null || map.isEmpty()) return "NONE";
        String nl = System.getProperty("line.separator", "\n");
        StringBuffer sb = new StringBuffer();
        Iterator iter = map.entrySet().iterator();
        Map.Entry entry;
        while (iter.hasNext()) {
            entry = (Map.Entry) iter.next();
            sb.append(entry.getKey()).append(':').append(entry.getValue()).append(nl);
        }
        return sb.toString();
    }

    private static void string2Map(String s, Map map) {
        if (!NONE.equalsIgnoreCase(s)) {
            StringTokenizer st = new StringTokenizer(s, " \t\r\n;");
            String token;
            int pos;
            while (st.hasMoreTokens()) {
                token = st.nextToken();
                pos = token.indexOf(':');
                map.put(token.substring(0, pos), token.substring(++pos));
            }
        } else {
            map.clear();
        }
    }

    protected void startService() throws Exception {
        if (this.isRequestStgCmt()) {
            try {
                stgCmtScu.startStgCmtListener();
            } catch (Exception x) {
                log.error("Failed to start server for receiving Storage Commitment results!:", x);
            }
        }
    }

    protected void stopService() throws Exception {
        if (this.isRequestStgCmt()) {
            try {
                stgCmtScu.stopStgCmtListener();
            } catch (Exception x) {
                log.error("Failed to stop server for receiving Storage Commitment results!:", x);
            }
        }
        super.stopService();
    }
}
