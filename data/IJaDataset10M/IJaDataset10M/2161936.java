package net.sourceforge.docfetcher.model.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.TextExtractor;
import net.sourceforge.docfetcher.TestFiles;
import net.sourceforge.docfetcher.util.Util;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.junit.Test;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileInputStream;

/**
 * This unit test checks that all the libraries used for parsing files are
 * capable of handling input streams from zip archive entries.
 * 
 * @author Tran Nam Quang
 */
public final class TestParseFromZip {

    @Test
    public void testZippedOffice() throws Exception {
        new ZipAndRun(TestFiles.doc) {

            protected void handleInputStream(InputStream in) throws Exception {
                POIFSReader reader = new POIFSReader();
                reader.registerListener(new POIFSReaderListener() {

                    public void processPOIFSReaderEvent(POIFSReaderEvent event) {
                    }
                }, "\005SummaryInformation");
                reader.read(in);
            }
        };
        new ZipAndRun(TestFiles.doc) {

            protected void handleInputStream(InputStream in) throws Exception {
                WordExtractor extractor = new WordExtractor(in);
                extractor.getText();
            }
        };
    }

    @Test(expected = IOException.class)
    public void testZippedOfficeFail() throws Exception {
        new ZipAndRun(TestFiles.doc) {

            protected void handleInputStream(InputStream in) throws Exception {
                POIFSReader reader = new POIFSReader();
                reader.registerListener(new POIFSReaderListener() {

                    public void processPOIFSReaderEvent(POIFSReaderEvent event) {
                    }
                }, "\005SummaryInformation");
                reader.read(in);
                WordExtractor extractor = new WordExtractor(in);
                extractor.getText();
            }
        };
    }

    @Test
    public void testZippedOffice2007() throws Exception {
        new ZipAndRun(TestFiles.docx) {

            protected void handleInputStream(InputStream in) throws Exception {
                int length = ExtractorFactory.createExtractor(in).getText().length();
                assertEquals(620, length);
            }
        };
        new ZipAndRun(TestFiles.docx) {

            protected void handleInputStream(InputStream in) throws Exception {
                OPCPackage pkg = OPCPackage.open(in);
                pkg.getPackageProperties();
            }
        };
    }

    @Test(expected = IOException.class)
    public void testZippedOffice2007Fail() throws Exception {
        new ZipAndRun(TestFiles.docx) {

            protected void handleInputStream(InputStream in) throws Exception {
                int length = ExtractorFactory.createExtractor(in).getText().length();
                assertEquals(620, length);
                OPCPackage pkg = OPCPackage.open(in);
                pkg.getPackageProperties();
            }
        };
    }

    @Test
    public void testZippedHtml() throws Exception {
        new ZipAndRun(TestFiles.html) {

            protected void handleInputStream(InputStream in) throws Exception {
                Source source = new Source(in);
                source.fullSequentialParse();
                TextExtractor textExtractor = source.getTextExtractor();
                textExtractor.setIncludeAttributes(true);
                assertTrue(textExtractor.toString().contains("HTML file"));
            }
        };
    }

    @Test
    public void testZippedPdf() throws Exception {
        new ZipAndRun(TestFiles.multi_page_pdf) {

            protected void handleInputStream(InputStream in) throws Exception {
                PDDocument pdfDoc = PDDocument.load(in);
                PDFTextStripper stripper = new PDFTextStripper();
                StringWriter writer = new StringWriter();
                stripper.setForceParsing(true);
                stripper.setSortByPosition(true);
                stripper.writeText(pdfDoc, writer);
                PDDocumentInformation pdInfo = pdfDoc.getDocumentInformation();
                ParseResult result = new ParseResult(writer.getBuffer()).setTitle(pdInfo.getTitle()).addAuthor(pdInfo.getAuthor()).addMiscMetadata(pdInfo.getSubject()).addMiscMetadata(pdInfo.getKeywords());
                String expectedContents = Util.join(Util.LS, "page 1", "page 2", "page 3");
                String actualContents = result.getContent().toString().trim();
                assertEquals(expectedContents, actualContents);
            }
        };
    }

    private abstract static class ZipAndRun {

        public ZipAndRun(TestFiles testFile) throws Exception {
            TFile src = new TFile(testFile.getPath());
            File dir = Util.createTempDir();
            TFile archive = new TFile(dir, "archive.zip");
            archive.mkdir();
            TFile dst = new TFile(archive, src.getName());
            src.cp(dst);
            InputStream in = new TFileInputStream(dst);
            handleInputStream(in);
            Closeables.closeQuietly(in);
            TFile.umount(archive);
            Files.deleteRecursively(dir);
        }

        protected abstract void handleInputStream(InputStream in) throws Exception;
    }
}
