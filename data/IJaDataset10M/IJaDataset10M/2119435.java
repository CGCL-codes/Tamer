package de.grogra.pf.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import de.grogra.util.IOWrapException;
import de.grogra.util.XByteArrayOutputStream;
import de.grogra.vfs.FileSystem;

public class StreamAdapter extends SAXFilterBase implements InputStreamSource, OutputStreamSource, ReaderSource, WriterSource, FileWriterSource, DOMSource, VirtualFileWriterSource {

    private static class ArrayWriter extends CharArrayWriter {

        ArrayWriter(int size) {
            super(size);
        }

        char[] getBuffer() {
            return buf;
        }
    }

    public StreamAdapter(FilterSource source, IOFlavor targetFlavor) {
        super(null, source, targetFlavor);
    }

    public InputStream getInputStream() throws IOException {
        IOFlavor f = source.getFlavor();
        if (f.isInputStreamSupported()) {
            return ((InputStreamSource) source).getInputStream();
        } else if (f.isOutputStreamSupported() || f.isWriterSupported() || f.isReaderSupported() || f.isSAXSupported() || f.isDOMSupported()) {
            XByteArrayOutputStream out = new XByteArrayOutputStream(100000);
            write(out);
            out.flush();
            out.close();
            return out.createInputStream();
        } else {
            throw new IOException("Can't adapt " + source + " to " + this);
        }
    }

    public long length() {
        return source.getFlavor().isInputStreamSupported() ? ((InputStreamSource) source).length() : -1;
    }

    public void write(OutputStream out) throws IOException {
        IOFlavor f = source.getFlavor();
        if (f.isOutputStreamSupported()) {
            ((OutputStreamSource) source).write(out);
        } else if (f.isInputStreamSupported()) {
            byte[] buf = new byte[0x10000];
            InputStream in = ((InputStreamSource) source).getInputStream();
            int n;
            while ((n = in.read(buf)) >= 0) {
                out.write(buf, 0, n);
            }
            in.close();
        } else if (f.isSAXSupported() && !(f.isWriterSupported() || f.isReaderSupported())) {
            transformSAX(new StreamResult(out));
        } else if (f.isDOMSupported() && !(f.isWriterSupported() || f.isReaderSupported())) {
            transformDOM(new StreamResult(out));
        } else {
            OutputStreamWriter w = new OutputStreamWriter(out);
            write(w);
            w.flush();
        }
    }

    public Reader getReader() throws IOException {
        IOFlavor f = source.getFlavor();
        if (f.isReaderSupported()) {
            return ((ReaderSource) source).getReader();
        } else if (f.isWriterSupported() || ((f.isSAXSupported() || f.isDOMSupported()) && !f.isInputStreamSupported() && !f.isOutputStreamSupported())) {
            ArrayWriter out = new ArrayWriter(100000);
            write(out);
            out.flush();
            out.close();
            return new CharArrayReader(out.getBuffer(), 0, out.size());
        } else {
            return new InputStreamReader(getInputStream());
        }
    }

    public void write(Writer out) throws IOException {
        IOFlavor f = source.getFlavor();
        if (f.isWriterSupported()) {
            ((WriterSource) source).write(out);
        } else if (f.isSAXSupported()) {
            transformSAX(new StreamResult(out));
        } else if (f.isDOMSupported()) {
            transformDOM(new StreamResult(out));
        } else {
            Reader in = getReader();
            char[] buf = new char[0x10000];
            int n;
            while ((n = in.read(buf)) >= 0) {
                out.write(buf, 0, n);
            }
            in.close();
        }
    }

    private XMLReader xmlReader = null;

    private XMLReader getXMLReader() throws SAXException {
        if (xmlReader == null) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            try {
                xmlReader = factory.newSAXParser().getXMLReader();
            } catch (ParserConfigurationException e) {
                throw de.grogra.util.Utils.newSAXException(e);
            }
        }
        return xmlReader;
    }

    private String getXMLSystemId() {
        String s = source.getSystemId();
        File f = IO.toLocalFile(s);
        return ((f != null) && f.exists()) ? f.getAbsolutePath() : s;
    }

    public void parse(ContentHandler ch, ErrorHandler eh, LexicalHandler lh, DTDHandler dh, EntityResolver er) throws IOException, SAXException {
        IOFlavor f = source.getFlavor();
        if (f.isSAXSupported()) {
            ((SAXSource) source).parse(ch, eh, lh, dh, er);
        } else if (f.isDOMSupported()) {
            SAXResult r = new SAXResult(ch);
            if (lh != null) {
                r.setLexicalHandler(lh);
            }
            transformDOM(r);
        } else {
            InputSource xmlSource = (f.isInputStreamSupported() || f.isOutputStreamSupported()) ? new InputSource(getInputStream()) : new InputSource(getReader());
            xmlSource.setSystemId(getXMLSystemId());
            XMLReader xr = getXMLReader();
            xr.setContentHandler(ch);
            if (eh != null) {
                xr.setErrorHandler(eh);
            }
            if (lh != null) {
                xr.setProperty(LEX_HANDLER, lh);
            }
            if (dh != null) {
                xr.setDTDHandler(dh);
            }
            if (er != null) {
                xr.setEntityResolver(er);
            }
            xr.parse(xmlSource);
        }
    }

    private void transformSAX(Result res) throws IOException {
        transform(new javax.xml.transform.sax.SAXSource(this, new InputSource(source.getSystemId())), res);
    }

    private void transformDOM(Result res) throws IOException {
        transform(new javax.xml.transform.dom.DOMSource(((DOMSource) source).getDocument(), source.getSystemId()), res);
    }

    private void transform(Source src, Result res) throws IOException {
        src.setSystemId(getXMLSystemId());
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            if (res instanceof StreamResult) {
                t.setOutputProperty(OutputKeys.INDENT, "yes");
                try {
                    t.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "1");
                } catch (IllegalArgumentException e) {
                }
            }
            t.transform(src, res);
        } catch (TransformerException e) {
            throw new IOWrapException(e);
        }
    }

    @Override
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (source.getFlavor().isSAXSupported()) {
            return ((SAXSource) source).getFeature(name);
        } else {
            try {
                getXMLReader();
            } catch (SAXException e) {
                throw new SAXNotRecognizedException(name);
            }
            return xmlReader.getFeature(name);
        }
    }

    @Override
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (source.getFlavor().isSAXSupported()) {
            ((SAXSource) source).setFeature(name, value);
        } else {
            try {
                getXMLReader();
            } catch (SAXException e) {
                throw new SAXNotRecognizedException(name);
            }
            xmlReader.setFeature(name, value);
        }
    }

    public void write(File out) throws IOException {
        write(this, null, out);
    }

    public void write(FileSystem fs, Object out) throws IOException {
        OutputStream fout = null;
        try {
            fout = fs.getOutputStream(out, false);
            write(fout);
            fout.flush();
        } finally {
            if (fout != null) {
                fout.close();
            }
        }
    }

    public static void write(OutputStreamSource src, File out) throws IOException {
        write(src, null, out);
    }

    public static void write(WriterSource src, File out) throws IOException {
        write(null, src, out);
    }

    private static void write(OutputStreamSource os, WriterSource ws, File out) throws IOException {
        OutputStream fout = null;
        File tmp = (out.exists() && (out.length() > 0)) ? File.createTempFile(out.getName(), null, out.getParentFile()) : out;
        try {
            fout = new FileOutputStream(tmp);
            OutputStream b = new BufferedOutputStream(fout);
            if (os != null) {
                os.write(b);
            } else {
                OutputStreamWriter w = new OutputStreamWriter(b);
                ws.write(w);
                w.flush();
            }
            b.flush();
        } finally {
            if (fout != null) {
                fout.close();
            }
        }
        if (out == tmp) {
            return;
        }
        if (out.exists()) {
            File backup = new File(out.getParentFile(), out.getName() + '~');
            if (backup.exists()) {
                backup.delete();
            }
            out.renameTo(backup);
        }
        if (!tmp.renameTo(out)) {
            InputStream in = new BufferedInputStream(new FileInputStream(tmp));
            byte[] buf = new byte[0x10000];
            int n;
            fout = new BufferedOutputStream(new FileOutputStream(out));
            while ((n = in.read(buf)) >= 0) {
                fout.write(buf, 0, n);
            }
            in.close();
            fout.flush();
            fout.close();
            tmp.delete();
        }
    }

    public Document getDocument() throws IOException, DOMException {
        IOFlavor f = source.getFlavor();
        if (f.isDOMSupported()) {
            return ((DOMSource) source).getDocument();
        } else {
            DOMResult r = new DOMResult();
            if (f.isSAXSupported()) {
                transformSAX(r);
            } else {
                StreamSource s = f.isFileReaderSupported() ? new StreamSource(((FileReaderSource) source).getInputFile()) : (f.isInputStreamSupported() || f.isOutputStreamSupported()) ? new StreamSource(getInputStream()) : new StreamSource(getReader());
                s.setSystemId(getXMLSystemId());
                transform(s, r);
            }
            return (Document) r.getNode();
        }
    }
}
