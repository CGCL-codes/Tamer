package cn.vlabs.clb.search.fulltext.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDDocumentInformation;
import org.pdfbox.util.PDFTextStripper;
import cn.vlabs.clb.utils.StackTrace;

public final class PDFParserImp extends Parser {

    ParserReadable file;

    PDDocument doc = null;

    PDDocumentInformation docInfo = null;

    public PDFParserImp(ParserReadable input) {
        super();
        file = input;
    }

    public StringBuffer getContent() {
        return buffer;
    }

    private void readContent() {
        if (!file.exists()) return;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.writeText(doc, writer);
            writer.close();
            buffer = new StringBuffer();
            buffer.append(out.toString());
        } catch (Exception e) {
            log.error(StackTrace.getStackTraceAsString(e));
        }
    }

    public String getTitle() {
        if (docInfo != null) {
            return docInfo.getTitle();
        } else return "";
    }

    public String getAuthor() {
        if (docInfo != null) {
            return docInfo.getAuthor();
        } else return "";
    }

    public String getKeywords() {
        if (docInfo != null) {
            if (docInfo.getKeywords() == null) {
                return "";
            } else {
                return docInfo.getKeywords();
            }
        } else return "";
    }

    public String getModifyDate() {
        try {
            if (docInfo != null) {
                if (docInfo.getModificationDate() == null) {
                    return (new SimpleDateFormat("yyyy-MM-dd")).format(docInfo.getCreationDate().getTime());
                } else {
                    return (new SimpleDateFormat("yyyy-MM-dd")).format(docInfo.getModificationDate().getTime());
                }
            } else return "";
        } catch (Exception e) {
            log.error(StackTrace.getStackTraceAsString(e));
            return "";
        }
    }

    @Override
    protected void init() {
        if (!file.exists()) return;
        InputStream in = null;
        try {
            in = file.getInputStream();
            doc = PDDocument.load(in);
            docInfo = doc.getDocumentInformation();
            readContent();
        } catch (Throwable e) {
            log.error(StackTrace.getStackTraceAsString(e));
        } finally {
            try {
                if (doc != null) doc.close();
                if (in != null) in.close();
            } catch (IOException e) {
            }
        }
    }

    private StringBuffer buffer;

    private static Logger log = Logger.getLogger(PDFParserImp.class);
}
