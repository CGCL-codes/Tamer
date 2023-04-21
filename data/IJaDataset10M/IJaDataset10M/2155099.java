package com.google.code.ftspc.lector.parsers.PDF;

import com.google.code.ftspc.lector.indexers.AddDataToIndex;
import com.google.code.ftspc.lector.ini_and_vars.Vars;
import com.google.code.ftspc.lector.parsers.Parser;
import java.io.File;
import java.io.FileInputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 *
 * @author Arthur Khusnutdinov
 */
public class PDFParserLocal extends Thread implements Parser {

    private String pathToFile;

    private String fileName;

    @Override
    public void run() {
        try {
            String fileContent = "";
            File filePDF = new File(pathToFile);
            PDDocument pdDoc = PDDocument.load(new FileInputStream(filePDF));
            PDFTextStripper PDFTextStripper = null;
            Integer numberOfPages = pdDoc.getNumberOfPages();
            for (int page = 0; page < numberOfPages; page++) {
                PDFTextStripper = new PDFTextStripper("UTF-8");
                PDFTextStripper.setStartPage(page);
                PDFTextStripper.setEndPage(page);
                String text = PDFTextStripper.getText(pdDoc);
                fileContent += " " + text.replaceAll("\\s+", " ").trim();
                text = null;
            }
            pdDoc.close();
            pdDoc = null;
            filePDF = null;
            PDFTextStripper = null;
            if (fileContent.length() < 1) {
            }
            AddDataToIndex AddDataToIndex = new AddDataToIndex(null);
            AddDataToIndex.doAddData(fileContent, pathToFile, fileName);
            fileContent = null;
            Vars.current_run_indexes--;
        } catch (Exception ex) {
            Vars.current_run_indexes--;
            Vars.logger.fatal("Error: ", ex);
        }
    }

    @Override
    public void start_th(String pathToFile, String fileName) {
        this.pathToFile = pathToFile;
        this.fileName = fileName;
        this.start();
    }
}
