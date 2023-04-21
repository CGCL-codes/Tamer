package org.xfc.print;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.print.PrintService;
import javax.swing.ImageIcon;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xfc.XApp;

/**
 * 
 * 
 * @author Devon Carew
 */
public class XPrintUtilities {

    private static Log log = LogFactory.getLog(XPrintUtilities.class);

    static ImageIcon PRINT_ICON = XApp.getApp().getResources().getIcon("/eclipse/org.eclipse.ui/icons/full/etool16/print_edit.gif");

    private static PageFormat lastUserPageFormat;

    private static PrintService lastUserPrintService;

    public static PrinterJob createPrintJob(String jobName) {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setJobName(jobName);
        if (getLastUserPageFormat() != null) setLastUserPageFormat(printJob.validatePage(getLastUserPageFormat()));
        try {
            if (getLastUserPrintService() != null) printJob.setPrintService(getLastUserPrintService());
        } catch (PrinterException pe) {
            log.error("Exception creating print job.", pe);
        }
        return printJob;
    }

    public static void print(Printable printable, String jobName) {
        PrinterJob printJob = createPrintJob(jobName);
        if (printJob.printDialog()) {
            if (printJob.getPrintService() != null) setLastUserPrintService(printJob.getPrintService());
            printJob.setPrintable(printable, printJob.defaultPage());
            XPrintTask printTask = new XPrintTask(printJob);
            printTask.start();
        }
    }

    public static void printPreview(Printable printable, String jobName) {
        XPrintPreviewDialog dialog = new XPrintPreviewDialog();
        dialog.setPrintable(printable, jobName);
        dialog.show();
    }

    public static void printPreview(Pageable pageable, String jobName) {
        XPrintPreviewDialog dialog = new XPrintPreviewDialog();
        dialog.setPageable(pageable, jobName);
        dialog.show();
    }

    public static PageFormat getLastUserPageFormat() {
        return lastUserPageFormat;
    }

    public static void setLastUserPageFormat(PageFormat pageFormat) {
        lastUserPageFormat = pageFormat;
    }

    public static PrintService getLastUserPrintService() {
        return lastUserPrintService;
    }

    public static void setLastUserPrintService(PrintService userPrintService) {
        XPrintUtilities.lastUserPrintService = userPrintService;
    }

    /**
	 * Returns a Pageable object that delegates all work through to the given Printable.
	 * 
	 * @param printable
	 * @return Returns a Pageable object that delegates all work through to the given Printable.
	 */
    public static Pageable createPageablePrintable(Printable printable) {
        return new PageablePrintable(printable);
    }

    /**
	 * Returns a Pageable object that delegates all work through to the given Printable.
	 * 
	 * @param printable
	 * @return Returns a Pageable object that delegates all work through to the given Printable.
	 */
    public static Pageable createPageablePrintable(Printable printable, PageFormat pageFormat) {
        return new PageablePrintable(printable, pageFormat);
    }

    /**
	 * A wrapper around a Printable to make it appear as a Pageable.
	 */
    private static class PageablePrintable implements Pageable {

        private Printable printable;

        private PageFormat pageFormat;

        private PageablePrintable(Printable printable) {
            this.printable = printable;
        }

        private PageablePrintable(Printable printable, PageFormat pageFormat) {
            this.printable = printable;
            this.pageFormat = pageFormat;
        }

        public int getNumberOfPages() {
            return Pageable.UNKNOWN_NUMBER_OF_PAGES;
        }

        public PageFormat getPageFormat(int pageIndex) {
            return pageFormat;
        }

        public Printable getPrintable(int pageIndex) {
            return printable;
        }
    }
}
