package com.san.utils;

import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pdfbox.cos.COSDocument;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

public class PDFTextParser {

    /**
	 * Extract text from PDF Document
	 */
    public String pdfToText(String fileName) {
        logger.debug("Parsing text from PDF file " + fileName + "....");
        File f = new File(fileName);
        if (!f.isFile()) {
            throw new RuntimeException("File " + fileName + " does not exist.");
        }
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        String parsedText;
        try {
            PDFParser parser = new PDFParser(new FileInputStream(f));
            PDFTextStripper pdfStripper = new PDFTextStripper();
            parser.parse();
            cosDoc = parser.getDocument();
            pdDoc = new PDDocument(cosDoc);
            parsedText = pdfStripper.getText(pdDoc);
        } catch (Exception e) {
            throw new RuntimeException("An exception occured in parsing the PDF Document.", e);
        } finally {
            try {
                if (cosDoc != null) cosDoc.close();
                if (pdDoc != null) pdDoc.close();
            } catch (Exception e1) {
                throw new RuntimeException(e1);
            }
        }
        return parsedText;
    }

    private PDFTextParser() {
    }

    public static synchronized PDFTextParser getInstance() {
        return _instance;
    }

    private static Log logger = LogFactory.getLog(PDFTextParser.class.getName());

    private static PDFTextParser _instance = new PDFTextParser();
}
