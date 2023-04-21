package org.tastefuljava.sceyefi.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.tastefuljava.sceyefi.capture.conf.EyeFiCard;
import org.tastefuljava.sceyefi.capture.conf.EyeFiConf;
import org.tastefuljava.sceyefi.conf.EyeFiConfTest;
import org.tastefuljava.sceyefi.capture.server.ChecksumInputStream;
import org.tastefuljava.sceyefi.capture.server.SoapEnvelope;
import org.tastefuljava.sceyefi.tar.TarReaderTest;
import org.tastefuljava.sceyefi.capture.util.Bytes;
import org.tastefuljava.sceyefi.capture.util.LogWriter;

public class EyeFiClient {

    private static final Logger LOG = Logger.getLogger(EyeFiClient.class.getName());

    private static final int EYEFI_PORT = 59278;

    private static final Namespace REQUEST_NAMESPACE = Namespace.getNamespace("EyeFi/SOAP/EyeFilm");

    private String hostName;

    private EyeFiCard card;

    private byte[] cnonce = Bytes.randomBytes(16);

    private byte[] snonce;

    private long fileId;

    public EyeFiClient(String hostName, EyeFiCard card) {
        this.hostName = hostName;
        this.card = card;
    }

    public void close() {
    }

    private HttpURLConnection createConnection(boolean multipart) throws IOException {
        StringBuilder buf = new StringBuilder();
        buf.append("http://");
        buf.append(hostName);
        buf.append(':');
        buf.append(EYEFI_PORT);
        buf.append("/api/soap/eyefilm/v1");
        if (multipart) {
            buf.append("/upload");
        }
        URL url = new URL(buf.toString());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("User-agent", "Eye-Fi Card/4.5022");
        con.setRequestProperty("Host", "api.eye.fi");
        return con;
    }

    private Element simpleAction(Element req) throws IOException, JDOMException {
        HttpURLConnection con = createConnection(false);
        try {
            con.setRequestProperty("SOAPAction", "urn:" + req.getName());
            OutputStream out = con.getOutputStream();
            try {
                XMLOutputter outp = new XMLOutputter();
                Document doc = SoapEnvelope.wrap(req);
                logXML(Level.FINE, doc);
                outp.output(doc, out);
            } finally {
                out.close();
            }
            InputStream in = con.getInputStream();
            try {
                SAXBuilder builder = new SAXBuilder();
                Document doc = builder.build(in);
                logXML(Level.FINE, doc);
                return SoapEnvelope.strip(doc);
            } finally {
                in.close();
            }
        } finally {
            con.disconnect();
        }
    }

    public void startSession() throws IOException, JDOMException {
        Element req = new Element("StartSession", REQUEST_NAMESPACE);
        addChildText(req, "macaddress", card.getMacAddress());
        addChildText(req, "cnonce", Bytes.bin2hex(cnonce));
        addChildText(req, "transfermode", Integer.toString(card.getTransferMode()));
        addChildText(req, "transfermodetimestamp", Long.toString(card.getTimestamp()));
        Element resp = simpleAction(req);
        byte[] credential = Bytes.md5(Bytes.hex2bin(card.getMacAddress()), cnonce, card.getUploadKey());
        byte[] actualCred = Bytes.hex2bin(childText(resp, "credential"));
        if (!Bytes.equals(credential, actualCred)) {
            throw new IOException("Invalid credential");
        }
        snonce = Bytes.hex2bin(childText(resp, "snonce"));
    }

    public void getPhotoStatus(String archiveName, long size) throws IOException, JDOMException {
        byte[] credential = Bytes.md5(Bytes.hex2bin(card.getMacAddress()), card.getUploadKey(), snonce);
        Element req = new Element("GetPhotoStatus", REQUEST_NAMESPACE);
        addChildText(req, "credential", Bytes.bin2hex(credential));
        addChildText(req, "macaddress", card.getMacAddress());
        addChildText(req, "filename", archiveName);
        addChildText(req, "filesize", Long.toString(size));
        addChildText(req, "filesignature", "343afd9e4e84d3d4f5969cd97214f7f2");
        addChildText(req, "flags", "4");
        Element resp = simpleAction(req);
        fileId = Long.parseLong(childText(resp, "fileid"));
    }

    public void uploadArchive(InputStream input, String fileName, long size, Date timestamp) throws IOException, JDOMException {
        HttpURLConnection con = createConnection(true);
        try {
            String boundary = "aaaaaaaaaaaaazzzzzzzzzz";
            con.setRequestProperty("SOAPAction", "urn:UploadPhoto");
            con.setRequestProperty("Content-type", "multipart/form-data; boundary=" + boundary);
            OutputStream out = con.getOutputStream();
            try {
                Element req = new Element("UploadPhoto", REQUEST_NAMESPACE);
                addChildText(req, "fileid", Long.toString(fileId));
                addChildText(req, "macaddress", card.getMacAddress());
                addChildText(req, "filename", fileName);
                addChildText(req, "filesize", Long.toString(size));
                addChildText(req, "filesignature", "343afd9e4e84d3d4f5969cd97214f7f2");
                addChildText(req, "encryption", "none");
                addChildText(req, "flags", "4");
                out.write(("\r\n--" + boundary + "\r\n").getBytes("ASCII"));
                out.write("Content-Disposition: form-data; name=\"SOAPENVELOPE\"\r\n\r\n".getBytes("ASCII"));
                XMLOutputter outp = new XMLOutputter();
                Document doc = SoapEnvelope.wrap(req);
                outp.output(doc, out);
                logXML(Level.FINE, doc);
                out.write(("\r\n--" + boundary + "\r\n").getBytes("ASCII"));
                out.write(("Content-Disposition: form-data; name=\"FILENAME\"" + "; filename=\"" + fileName + "\"\r\n" + "Content-Type: application/x-tar\r\n\r\n").getBytes("ASCII"));
                ChecksumInputStream in = new ChecksumInputStream(input);
                try {
                    byte buf[] = new byte[4096];
                    for (int n = in.read(buf); n > 0; n = in.read(buf)) {
                        out.write(buf, 0, n);
                    }
                } finally {
                    in.close();
                }
                byte[] digest = in.checksum(card.getUploadKey());
                out.write(("\r\n--" + boundary + "\r\n").getBytes("ASCII"));
                out.write("Content-Disposition: form-data; name=\"INTEGRITYDIGEST\"\r\n\r\n".getBytes("ASCII"));
                out.write(Bytes.bin2hex(digest).getBytes("ASCII"));
                out.write(("\r\n--" + boundary + "--\r\n").getBytes("ASCII"));
            } finally {
                out.close();
            }
            int st = con.getResponseCode();
            if (st != HttpURLConnection.HTTP_OK) {
                throw new IOException("Upload failed: " + st + " " + con.getResponseMessage());
            }
            InputStream in = con.getInputStream();
            try {
                SAXBuilder builder = new SAXBuilder();
                Document doc = builder.build(in);
                logXML(Level.FINE, doc);
                Element resp = SoapEnvelope.strip(doc);
                boolean success = "true".equalsIgnoreCase(childText(resp, "success"));
                if (!success) {
                    throw new IOException("Upload failed");
                }
            } finally {
                in.close();
            }
        } finally {
            con.disconnect();
        }
    }

    public void markLastPhotoInRoll() throws JDOMException, IOException {
        Element req = new Element("MarkLastPhotoInRoll", REQUEST_NAMESPACE);
        addChildText(req, "macaddress", card.getMacAddress());
        addChildText(req, "mergedelta", "0");
        Element resp = simpleAction(req);
    }

    public void uploadArchive(URL url, String fileName) throws IOException, JDOMException {
        URLConnection con = url.openConnection();
        InputStream in = con.getInputStream();
        try {
            startSession();
            getPhotoStatus(fileName, con.getContentLength());
            uploadArchive(in, fileName, con.getContentLength(), new Date(con.getLastModified()));
            markLastPhotoInRoll();
        } finally {
            in.close();
        }
    }

    public static void main(String args[]) {
        try {
            URL confURL = EyeFiConfTest.class.getResource("Settings.xml");
            EyeFiConf conf = EyeFiConf.load(confURL);
            EyeFiCard card = conf.getCards()[0];
            EyeFiClient client = new EyeFiClient("localhost", card);
            URL url = TarReaderTest.class.getResource("P1030001.JPG.tar");
            client.uploadArchive(url, "P1030001.JPG.tar");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error", ex);
        }
    }

    private static void addChildText(Element elm, String name, String text) {
        Element child = new Element(name, elm.getNamespace());
        child.setText(text);
        elm.addContent(child);
    }

    private static String childText(Element elm, String name) {
        String s = elm.getChildText(name);
        if (s != null) {
            return s;
        }
        return elm.getChildText(name, elm.getNamespace());
    }

    private static void logXML(Level level, Document doc) throws IOException {
        if (LOG.isLoggable(level)) {
            Writer out = new LogWriter(LOG, level);
            try {
                XMLOutputter outp = new XMLOutputter();
                outp.setFormat(Format.getPrettyFormat());
                outp.output(doc, out);
            } finally {
                out.close();
            }
        }
    }
}
