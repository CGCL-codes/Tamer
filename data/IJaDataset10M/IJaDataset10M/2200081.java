package com.dcivision.lucene.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.dcivision.framework.ApplicationException;
import com.snowtide.pdf.OutputTarget;
import com.snowtide.pdf.PDFTextStream;

public class PDFTextStreamHandler implements DocumentHandler {

    private static final Log log = LogFactory.getLog(PDFTextStreamHandler.class);

    public PDFTextStreamHandler() {
    }

    public String getDocumentFullText(InputStream is) throws ApplicationException {
        if (is == null) return null;
        String docText = null;
        PDFTextStream stream = null;
        try {
            stream = new PDFTextStream(is, "");
            StringBuffer sb = new StringBuffer(1024);
            OutputTarget tgt = new OutputTarget(sb);
            stream.pipe(tgt);
            docText = sb.toString();
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            try {
                stream.close();
            } catch (Exception ex) {
            }
        }
        return docText;
    }

    public static void main(String[] args) throws Exception {
        PDFBoxPDFHandler handler = new PDFBoxPDFHandler();
        String fullText = handler.getDocumentFullText(new FileInputStream(new File(args[0])));
        System.out.println(fullText);
    }
}
