package ch.byteality.copperhead.business;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import ch.byteality.copperhead.model.PrintableColumnElement;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * The Class LogExporterPdfGenerator.
 */
public class TableGenerator implements IPdfGenerator {

    /** The big font. */
    Font bigFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL);

    /** The normal font. */
    Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

    /** The table font. */
    Font tableFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

    /** The byte utilities. */
    ByteUtilities byteUtilities = new ByteUtilities();

    public void createAndShowPdf(List<?> aList, List<PrintableColumnElement> captions, boolean portraidMode, String title, Properties languageProperties) throws IOException, DocumentException {
        Desktop.getDesktop().open(createPdf(aList, captions, portraidMode, title, languageProperties));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Gets the obect's pdf table.
	 * 
	 * @param aList
	 *            the a list
	 * @param captions
	 *            the captions
	 * @return the transport log table
	 * @throws DocumentException
	 *             the document exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    private Element getDataTable(List<?> aList, List<PrintableColumnElement> captions) throws DocumentException, IOException {
        Paragraph paragraph = new Paragraph();
        PdfPTable datatable = new PdfPTable(captions.size());
        datatable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        datatable.setWidthPercentage(100f);
        datatable.getDefaultCell().setPadding(3);
        datatable.getDefaultCell().setBorderWidth(0.7f);
        datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        for (PrintableColumnElement element : captions) {
            datatable.addCell(new Phrase(element.getCaption(), tableFont));
        }
        datatable.setHeaderRows(1);
        int i = 0;
        float gray1 = 0.97f;
        float gray2 = 0.91f;
        for (Object obj : aList) {
            if (i++ % 2 == 1) datatable.getDefaultCell().setGrayFill(gray1); else datatable.getDefaultCell().setGrayFill(gray2);
            datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            for (PrintableColumnElement element : captions) {
                try {
                    datatable.addCell(new Phrase(byteUtilities.getFieldValue(obj, element), tableFont));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
        paragraph.add(datatable);
        return paragraph;
    }

    @Override
    public File createPdf(List<?> aList, List<PrintableColumnElement> captions, boolean portraidMode, String title, Properties languageProperties) throws IOException, DocumentException {
        if (aList.size() == 0) return null;
        File tempFile = File.createTempFile("generated", ".pdf");
        tempFile.deleteOnExit();
        Document document = new Document();
        if (portraidMode) document.setPageSize(PageSize.A4); else document.setPageSize(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(tempFile));
        document.open();
        Paragraph para = new Paragraph(languageProperties.getProperty("copperhead.pdf_title"), bigFont);
        document.add(para);
        SimpleDateFormat formatter = new SimpleDateFormat(languageProperties.getProperty("copperhead.pdf_title_date_format"));
        document.add(new Paragraph(formatter.format(new Date()), normalFont));
        document.add(new Paragraph());
        document.add(new Paragraph(" "));
        document.add(new Paragraph(title, normalFont));
        document.add(new Paragraph(" "));
        document.add(this.getDataTable(aList, captions));
        document.close();
        return tempFile;
    }
}
