package net.messze.jimposition.transformations;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.messze.jimposition.BasicTransformation;

/**
 *
 */
public abstract class AbstractFilter extends BasicTransformation {

    private Rectangle newPageSize;

    private Rectangle oldPageSize;

    /** Creates a new instance of AbstractFilter */
    public AbstractFilter() {
    }

    protected Rectangle getCurrentOldPageSize() {
        return oldPageSize;
    }

    protected Rectangle getCurrentNewPageSize() {
        return oldPageSize;
    }

    public byte[] alter(byte[] input) {
        try {
            PdfReader inputReader = new PdfReader(input);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            PdfContentByte cb = null;
            int pagenum = 1;
            int maxPageNum = inputReader.getNumberOfPages();
            for (int page = 1; page < maxPageNum; page++) {
                int oldPage = getOldPageNo(page);
                if (oldPage != -1) {
                    Rectangle oldPageSize = inputReader.getPageSize(page);
                    Rectangle newPageSize = getNewPageSize(oldPageSize);
                    document.setPageSize(newPageSize);
                    if (cb == null) {
                        document.open();
                        cb = writer.getDirectContent();
                    } else {
                        document.newPage();
                    }
                    process(cb, writer, inputReader, page);
                }
            }
            document.close();
            return outputStream.toByteArray();
        } catch (DocumentException ex) {
            ex.printStackTrace();
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Rectangle getNewPageSize(Rectangle oldPageSize) {
        return oldPageSize;
    }

    public int getOldPageNo(int page) {
        return page;
    }

    public abstract void process(PdfContentByte cb, PdfWriter writer, PdfReader inputReader, int page);

    public void setPanelSettings() {
    }

    public void getPanelSettings() {
    }
}
