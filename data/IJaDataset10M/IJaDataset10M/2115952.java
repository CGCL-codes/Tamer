package org.equanda.tool.print;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.print.JRPrinterAWT;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.print.PrintService;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;

/**
 * Printer class which extends the JRPrinterAWT but uses the printer service provided.
 *
 * @author NetRom team
 */
public class Printer extends JRPrinterAWT {

    protected Log log = LogFactory.getLog(this.getClass());

    JasperPrint jasperPrint = null;

    PrintService printService = null;

    public Printer(JasperPrint jrPrint, PrintService printService) throws JRException {
        super(jrPrint);
        jasperPrint = jrPrint;
        this.printService = printService;
    }

    public void print() throws JRException {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        try {
            if (printService != null) printJob.setPrintService(printService);
        } catch (Exception e) {
        }
        if (log.isDebugEnabled()) log.debug("using printer :" + printJob.getPrintService().getName());
        PageFormat pageFormat = printJob.defaultPage();
        Paper paper = pageFormat.getPaper();
        switch(jasperPrint.getOrientation()) {
            case JRReport.ORIENTATION_LANDSCAPE:
                pageFormat.setOrientation(PageFormat.LANDSCAPE);
                paper.setSize(jasperPrint.getPageHeight(), jasperPrint.getPageWidth());
                paper.setImageableArea(0, 0, jasperPrint.getPageHeight(), jasperPrint.getPageWidth());
                break;
            case JRReport.ORIENTATION_PORTRAIT:
            default:
                pageFormat.setOrientation(PageFormat.PORTRAIT);
                paper.setSize(jasperPrint.getPageWidth(), jasperPrint.getPageHeight());
                paper.setImageableArea(0, 0, jasperPrint.getPageWidth(), jasperPrint.getPageHeight());
        }
        pageFormat.setPaper(paper);
        Book book = new Book();
        book.append(this, pageFormat, jasperPrint.getPages().size());
        printJob.setPageable(book);
        try {
            printJob.print();
        } catch (Exception ex) {
            throw new JRException("Error printing report.", ex);
        }
    }
}
