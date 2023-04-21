package dataExtraction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

/**
 *
 * @author isuru
 */
public class DocumentReader {

    private PreprocessingError err = PreprocessingError.None;

    private String txtToString(String fileName) {
        BufferedReader myBufferedReader = null;
        try {
            File file = new File(fileName);
            try {
                if (file.exists() && file.isFile()) {
                    FileReader myFileReader = new FileReader(file);
                    myBufferedReader = new BufferedReader(myFileReader);
                } else {
                    err = PreprocessingError.ProcessError;
                }
            } catch (FileNotFoundException e) {
                err = PreprocessingError.FileNotFound;
            }
        } catch (Exception e) {
            err = PreprocessingError.ProcessError;
        }
        return bufferedReaderToString(myBufferedReader);
    }

    private String pdfToString(String fileName) {
        String fileAsText = null;
        PDDocument pdfDoc = null;
        try {
            PDFTextStripper myPDFTextStripper = new PDFTextStripper();
            pdfDoc = PDDocument.load(fileName);
            myPDFTextStripper.setStartPage(1);
            fileAsText = myPDFTextStripper.getText(pdfDoc);
            pdfDoc.close();
        } catch (Exception e) {
            err = PreprocessingError.ProcessError;
        } finally {
            try {
                if (pdfDoc != null) {
                    pdfDoc.close();
                }
            } catch (Exception e) {
                err = PreprocessingError.ProcessError;
            }
        }
        return fileAsText;
    }

    private String rtfToString(String fileName) {
        String rtfContents = null;
        try {
            FileInputStream stream = new FileInputStream(fileName);
            RTFEditorKit kit = new RTFEditorKit();
            Document doc = kit.createDefaultDocument();
            kit.read(stream, doc, 0);
            rtfContents = doc.getText(0, doc.getLength());
        } catch (Exception e) {
            err = PreprocessingError.ProcessError;
        }
        return rtfContents;
    }

    private String docToString(String fileName) {
        String fileAsText = null;
        try {
            InputStream in = new FileInputStream(fileName);
            WordExtractor extractor = new WordExtractor(in);
            fileAsText = extractor.getText();
        } catch (Exception e) {
            err = PreprocessingError.ProcessError;
        }
        return fileAsText;
    }

    private String bufferedReaderToString(BufferedReader inputBufferedReader) {
        StringBuffer fileAsText = new StringBuffer();
        try {
            String line = null;
            while ((line = inputBufferedReader.readLine()) != null) {
                line = line.replaceAll("[^\\p{ASCII}]", " ");
                line = line + "\n";
                fileAsText.append(line);
            }
        } catch (Exception e) {
            err = PreprocessingError.ProcessError;
        }
        return fileAsText.toString();
    }

    public String processFileAndGetText(String fileName) {
        String inputText = null;
        if (fileName.endsWith(".txt")) {
            inputText = txtToString(fileName);
        } else if (fileName.endsWith(".pdf")) {
            inputText = pdfToString(fileName);
        } else if (fileName.endsWith(".rtf")) {
            inputText = rtfToString(fileName);
        } else if (fileName.endsWith(".doc")) {
            inputText = docToString(fileName);
        }
        try {
            inputText = inputText.replaceAll("[^\\p{ASCII}]", " ");
            inputText = inputText.replaceAll("\\r\\n?", "\n");
        } catch (Exception e) {
            err = PreprocessingError.ProcessError;
        }
        return inputText;
    }

    public PreprocessingError getpreprocessingError() {
        return err;
    }
}
