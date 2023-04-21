package org.unicef.doc.ibis.nut.export;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import org.apache.fop.apps.FOPException;
import org.unicef.doc.ibis.nut.*;
import org.unicef.doc.ibis.nut.persistence.*;
import org.unicef.doc.ibis.nut.exceptions.*;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.sax.SAXResult;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

/**
 *
 * @author Nathan
 */
public final class FOP {

    private static final Config fConfig = Config.getInstance();

    private static final FopFactory fopFactory = FopFactory.newInstance();

    public static void exportPDF(WebStory pStory, InputStream pXSLT, File pPDF) {
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        JAXBContext fContext = fConfig.getJAXBContext();
        OutputStream out = null;
        Fop fop = null;
        TransformerFactory factory = null;
        Transformer transformer = null;
        Source src = null;
        Result res = null;
        Marshaller marshaller = null;
        byte[] bytes = null;
        ByteArrayOutputStream byteOut = null;
        ByteArrayInputStream byteIn = null;
        File correctedFile = null;
        try {
            if (pPDF != null) {
                if (!pPDF.getAbsolutePath().endsWith(".pdf")) {
                    correctedFile = new File(pPDF.getAbsolutePath() + ".pdf");
                    try {
                        correctedFile.createNewFile();
                    } catch (IOException ex) {
                        Logger.getLogger(FOP.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    correctedFile = pPDF;
                }
            }
            byteOut = new java.io.ByteArrayOutputStream();
            marshaller = fContext.createMarshaller();
            out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(correctedFile));
            marshaller.marshal(pStory, byteOut);
            bytes = byteOut.toByteArray();
            byteIn = new java.io.ByteArrayInputStream(bytes);
            fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            factory = TransformerFactory.newInstance();
            transformer = factory.newTransformer(new StreamSource(pXSLT));
            transformer.setParameter("versionParam", "2.0");
            src = new javax.xml.transform.stream.StreamSource(byteIn);
            res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);
        } catch (JAXBException ex) {
            Logger.getLogger(FOP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FOPException ex) {
            Logger.getLogger(FOP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(FOP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FOP.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(FOP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void exportRTF(WebStory pStory, InputStream pXSLT, File pRTF) {
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        JAXBContext fContext = fConfig.getJAXBContext();
        OutputStream out = null;
        Fop fop = null;
        TransformerFactory factory = null;
        Transformer transformer = null;
        Source src = null;
        Result res = null;
        Marshaller marshaller = null;
        byte[] bytes = null;
        ByteArrayOutputStream byteOut = null;
        ByteArrayInputStream byteIn = null;
        File correctedFile = null;
        try {
            if (pRTF != null) {
                if (!pRTF.getAbsolutePath().endsWith(".rtf")) {
                    correctedFile = new File(pRTF.getAbsolutePath() + ".rtf");
                    try {
                        correctedFile.createNewFile();
                    } catch (IOException ex) {
                        Logger.getLogger(FOP.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    correctedFile = pRTF;
                }
            }
            byteOut = new java.io.ByteArrayOutputStream();
            marshaller = fContext.createMarshaller();
            out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(correctedFile));
            marshaller.marshal(pStory, byteOut);
            bytes = byteOut.toByteArray();
            byteIn = new java.io.ByteArrayInputStream(bytes);
            fop = fopFactory.newFop(MimeConstants.MIME_RTF, foUserAgent, out);
            factory = TransformerFactory.newInstance();
            transformer = factory.newTransformer(new StreamSource(pXSLT));
            transformer.setParameter("versionParam", "2.0");
            src = new javax.xml.transform.stream.StreamSource(byteIn);
            res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);
        } catch (JAXBException ex) {
            Logger.getLogger(FOP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FOPException ex) {
            Logger.getLogger(FOP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(FOP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FOP.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(FOP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void exportTXT(WebStory pStory, InputStream pXSLT, File pTXT) {
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        JAXBContext fContext = fConfig.getJAXBContext();
        OutputStream out = null;
        Fop fop = null;
        TransformerFactory factory = null;
        Transformer transformer = null;
        Source src = null;
        Result res = null;
        Marshaller marshaller = null;
        byte[] bytes = null;
        ByteArrayOutputStream byteOut = null;
        ByteArrayInputStream byteIn = null;
        File correctedFile = null;
        try {
            if (pTXT != null) {
                if (!pTXT.getAbsolutePath().endsWith(".txt")) {
                    correctedFile = new File(pTXT.getAbsolutePath() + ".txt");
                    try {
                        correctedFile.createNewFile();
                    } catch (IOException ex) {
                        Logger.getLogger(FOP.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    correctedFile = pTXT;
                }
            }
            byteOut = new java.io.ByteArrayOutputStream();
            marshaller = fContext.createMarshaller();
            out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(correctedFile));
            marshaller.marshal(pStory, byteOut);
            bytes = byteOut.toByteArray();
            byteIn = new java.io.ByteArrayInputStream(bytes);
            fop = fopFactory.newFop(MimeConstants.MIME_PLAIN_TEXT, foUserAgent, out);
            factory = TransformerFactory.newInstance();
            transformer = factory.newTransformer(new StreamSource(pXSLT));
            transformer.setParameter("versionParam", "2.0");
            src = new javax.xml.transform.stream.StreamSource(byteIn);
            res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);
        } catch (JAXBException ex) {
            Logger.getLogger(FOP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FOPException ex) {
            Logger.getLogger(FOP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(FOP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FOP.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(FOP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
