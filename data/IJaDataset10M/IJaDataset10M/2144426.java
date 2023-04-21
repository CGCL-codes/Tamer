package com.primeton.fbsearch.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;
import com.primeton.fbsearch.framework.DocumentHandler;
import com.primeton.fbsearch.framework.DocumentHandlerException;

public class PDFBoxPDFHandler implements DocumentHandler {

    public static String password = "-password";

    public Document getDocument(InputStream is) throws DocumentHandlerException {
        Document doc = null;
        try {
            PDDocument document = PDDocument.load(is);
            boolean sort = false;
            int startPage = 1;
            int endPage = Integer.MAX_VALUE;
            PDFTextStripper stripper = null;
            stripper = new PDFTextStripper();
            stripper.setSortByPosition(sort);
            stripper.setStartPage(startPage);
            stripper.setEndPage(endPage);
            String bodyText = stripper.getText(document);
            if (bodyText != null && bodyText.length() > 20) {
                String tmp = bodyText.substring(0, 20);
                System.out.println("PDFBoxPDFHandler=" + tmp);
            }
            document.close();
            document = null;
            stripper = null;
            if ((bodyText != null) && (bodyText.trim().length() > 0)) {
                doc = new Document();
                doc.add(new Field("body", bodyText, Field.Store.YES, Field.Index.TOKENIZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
            }
        } catch (Exception e) {
            throw new DocumentHandlerException("Cannot extract text from a PDF document", e);
        }
        return doc;
    }

    public static void main(String[] args) throws Exception {
        String file = "D:\\OpenSource\\search_data\\EOS5.3报表培训教程.pdf";
        PDFBoxPDFHandler handler = new PDFBoxPDFHandler();
        Document doc = handler.getDocument(new FileInputStream(new File(file)));
        System.out.println(doc);
    }
}
