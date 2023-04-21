package com.atech.print;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 *  Filename:     PrintAbstract
 *  Description:  Abstract class for printing via creation of PDF (iText)
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */
public abstract class PrintAbstractIText extends PdfPageEventHelper {

    private static Log log = LogFactory.getLog(PrintAbstractIText.class);

    protected ATDataAccessAbstract m_da = null;

    protected I18nControlAbstract ic = null;

    String name = "";

    protected BaseFont base_helvetica = null;

    protected BaseFont base_times = null;

    protected Font text_normal = null;

    protected Font text_bold = null;

    protected Font text_italic = null;

    /**
     * Print root must always contain trailing slash, so ../data/print/ is correct, while ../data/print is incorrect.
     * It should be stored under GGC main structure, so that parent of last directory (in this case data) already 
     * exists. And of course unix path divider must be used. (/ instead of \ on windows) 
     */
    private static String print_root = "../data/print/";

    /**
     * Constructor
     * 
     * @param da 
     * @param do_init 
     */
    public PrintAbstractIText(ATDataAccessAbstract da, boolean do_init) {
        this.m_da = da;
        this.ic = da.getI18nControlInstance();
        if (do_init) init();
    }

    /**
     * Constructor
     * 
     * @param ic
     * @param do_init 
     */
    public PrintAbstractIText(I18nControlAbstract ic, boolean do_init) {
        this.ic = ic;
        if (do_init) init();
    }

    protected void init() {
        createName();
        try {
            base_helvetica = BaseFont.createFont("Helvetica", BaseFont.CP1250, BaseFont.NOT_EMBEDDED);
            base_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1250, BaseFont.NOT_EMBEDDED);
            text_normal = new Font(this.base_helvetica, getTextSize(), Font.NORMAL);
            text_bold = new Font(this.base_helvetica, getTextSize(), Font.BOLD);
            text_italic = new Font(this.base_helvetica, getTextSize(), Font.ITALIC);
        } catch (Exception ex) {
            System.out.println("Exception on font create: " + ex);
        }
        createDocument();
    }

    /**
     * Get Text Size
     * 
     * @return
     */
    public abstract int getTextSize();

    /**
     * Get Name
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Returns Name of report with Full Path
     * 
     * @return
     */
    public String getNameWithPath() {
        File f = new File(print_root + getName());
        return f.getAbsolutePath();
    }

    /**
     * Returns Name of report with Full Path
     * 
     * @return
     */
    public String getRelativeNameWithPath() {
        return print_root + getName();
    }

    /**
     * Returns report name as File instance
     * 
     * @return
     */
    public File getNameFile() {
        File f = new File(print_root + getName());
        return f;
    }

    /**
     * Create Name
     */
    public void createName() {
        checkIfRootExists();
        this.name = this.getFileNameBase() + "_" + this.getFileNameRange() + "_";
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            File f = new File(PrintAbstractIText.print_root + this.name + i + ".pdf");
            if (!f.exists()) {
                name += i + ".pdf";
                break;
            }
        }
    }

    /**
     * Get Title
     * 
     * @return
     */
    public abstract Paragraph getTitle();

    /**
     * Create Document
     */
    public void createDocument() {
        File fl = new File(PrintAbstractIText.print_root + this.getName());
        Document document = new Document(PageSize.A4, 30, 30, 40, 20);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fl.getAbsoluteFile()));
            writer.setPageEvent(this);
            document.open();
            document.add(getTitle());
            fillDocumentBody(document);
        } catch (Exception de) {
            log.error("Error on document creation [" + de.getMessage() + "]: " + de, de);
            de.printStackTrace();
        }
        document.close();
    }

    protected void setBackground(int element_cnt, PdfPTable table) {
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_BASELINE);
        if (element_cnt % 2 == 1) table.getDefaultCell().setGrayFill(0.9f); else table.getDefaultCell().setBackgroundColor(Color.white);
    }

    private void checkIfRootExists() {
        File f = new File(print_root);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception ex) {
                log.error("Error creating new print directory ! [" + PrintAbstractIText.print_root + "]. Ex: " + ex, ex);
            }
        }
    }

    /**
     * On End Page
     * 
     * @see com.lowagie.text.pdf.PdfPageEventHelper#onEndPage(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
     */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            Rectangle page = document.getPageSize();
            PdfPTable foot = new PdfPTable(1);
            PdfPCell pc = new PdfPCell();
            pc.setBorderColor(Color.white);
            Font f = new Font(this.base_times, 10, Font.ITALIC | Font.BOLD);
            pc.setPhrase(new Phrase(new Chunk(ic.getMessage("REPORT_FOOTER"), f)));
            pc.setHorizontalAlignment(Element.ALIGN_CENTER);
            foot.addCell(pc);
            foot.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
            foot.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    /**
     * Create document body.
     * 
     * @param document
     * @throws Exception
     */
    public abstract void fillDocumentBody(Document document) throws Exception;

    /**
     * Returns base filename for printing job, this is just part of end filename (starting part)
     * 
     * @return 
     */
    public abstract String getFileNameBase();

    /**
     * Returns data part of filename for printing job, showing which data is being printed
     * 
     * @return 
     */
    public abstract String getFileNameRange();
}
