package org.webguitoolkit.ui.util.export;

import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.webguitoolkit.ui.controls.table.Table;
import org.webguitoolkit.ui.controls.util.TextService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author & Lars Br��ler
 * 
 */
public class PDFEvent extends PdfPageEventHelper {

    private static final Logger logger = Logger.getLogger(PDFEvent.class);

    private Table wgtTable;

    public PDFEvent() {
        super();
    }

    public PDFEvent(Table wgtTable) {
        super();
        this.wgtTable = wgtTable;
    }

    public void onEndPage(PdfWriter writer, Document document) {
        TableExportOptions exportOptions = wgtTable.getExportOptions();
        try {
            Rectangle page = document.getPageSize();
            if (exportOptions.isShowDefaultHeader() || StringUtils.isNotEmpty(exportOptions.getHeaderImage())) {
                PdfPTable head = new PdfPTable(3);
                head.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                Paragraph title = new Paragraph(wgtTable.getTitle());
                title.setAlignment(Element.ALIGN_LEFT);
                head.addCell(title);
                Paragraph empty = new Paragraph("");
                head.addCell(empty);
                if (StringUtils.isNotEmpty(exportOptions.getHeaderImage())) {
                    try {
                        URL absoluteFileUrl = wgtTable.getPage().getClass().getResource("/" + exportOptions.getHeaderImage());
                        if (absoluteFileUrl != null) {
                            String path = absoluteFileUrl.getPath();
                            Image jpg = Image.getInstance(path);
                            jpg.scaleAbsoluteHeight(40);
                            jpg.scaleAbsoluteWidth(200);
                            head.addCell(jpg);
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        Paragraph noImage = new Paragraph("Image not found!");
                        head.addCell(noImage);
                    }
                } else {
                    head.addCell(empty);
                }
                head.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
                head.writeSelectedRows(0, -1, document.leftMargin(), page.getHeight() - document.topMargin() + head.getTotalHeight(), writer.getDirectContent());
            }
            if (exportOptions.isShowDefaultFooter() || StringUtils.isNotEmpty(exportOptions.getFooterText()) || exportOptions.isShowPageNumber()) {
                PdfPTable foot = new PdfPTable(3);
                String footerText = exportOptions.getFooterText() != null ? exportOptions.getFooterText() : "";
                if (!exportOptions.isShowDefaultFooter()) {
                    foot.addCell(new Paragraph(footerText));
                    foot.addCell(new Paragraph(""));
                } else {
                    foot.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                    String leftText = "";
                    if (StringUtils.isNotEmpty(exportOptions.getFooterText())) {
                        leftText = exportOptions.getFooterText();
                    }
                    Paragraph left = new Paragraph(leftText);
                    left.setAlignment(Element.ALIGN_LEFT);
                    foot.addCell(left);
                    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM, TextService.getLocale());
                    Date today = new Date();
                    String date = df.format(today);
                    Paragraph center = new Paragraph(date);
                    center.setAlignment(Element.ALIGN_CENTER);
                    foot.addCell(center);
                }
                if (exportOptions.isShowPageNumber()) {
                    Paragraph right = new Paragraph(TextService.getString("pdf.page@Page:") + " " + writer.getPageNumber());
                    right.setAlignment(Element.ALIGN_LEFT);
                    foot.addCell(right);
                    foot.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
                    foot.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());
                } else {
                    foot.addCell(new Paragraph(""));
                }
            }
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public void onOpenDocument(PdfWriter writer, Document document) {
    }

    public void onCloseDocument(PdfWriter writer, Document document) {
    }

    public void onStartPage(PdfWriter writer, Document document) {
    }

    public void onParagraph(PdfWriter writer, Document document, float paragraphPosition) {
    }

    public void onParagraphEnd(PdfWriter writer, Document document, float paragraphPosition) {
    }

    public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {
    }

    public void onChapterEnd(PdfWriter writer, Document document, float paragraphPosition) {
    }

    public void onSection(PdfWriter writer, Document document, float paragraphPosition, int depth, Paragraph title) {
    }

    public void onSectionEnd(PdfWriter writer, Document document, float paragraphPosition) {
    }

    public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text) {
    }
}
