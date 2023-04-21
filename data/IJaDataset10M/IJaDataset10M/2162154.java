package net.sf.wubiq.utils;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Locale;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.PrintJobAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import net.sf.wubiq.print.jobs.RemotePrintJob;
import net.sf.wubiq.wrappers.PageFormatWrapper;
import net.sf.wubiq.wrappers.PageableWrapper;
import net.sf.wubiq.wrappers.PrintableWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Sends the document object directly to print service.
 * @author Federico Alcantara
 *
 */
public final class ClientPrintDirectUtils {

    private static Log LOG = LogFactory.getLog(ClientPrintDirectUtils.class);

    /**
	 * Sends the input stream file with the given preferences to the print service.
	 * @param jobId Identifying job id.
	 * @param printService PrintService to print to.
	 * @param printRequestAttributeSet Attributes to be set on the print service.
	 * @param printJobAttributeSet Attributes for the print job.
	 * @param docAttributeSet Attributes for the document.
	 * @param docFlavor Document flavor.
	 * @param printData Document as input stream to sent to the print service.
	 * @throws IOException if service is not found and no default service.
	 */
    public static void print(String jobId, PrintService printService, PrintRequestAttributeSet printRequestAttributeSet, PrintJobAttributeSet printJobAttributeSet, DocAttributeSet docAttributeSet, DocFlavor docFlavor, InputStream printData) throws IOException {
        try {
            if (printService == null) {
                throw new IOException(("error.print.noPrintDevice"));
            }
            if (printData != null) {
                int replicatePrinting = PrintServiceUtils.getCopies(printRequestAttributeSet);
                Doc doc = null;
                printRequestAttributeSet.add(new JobName("Remote Print:" + jobId, Locale.getDefault()));
                if (docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE) || docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) {
                    ObjectInputStream input = new ObjectInputStream(printData);
                    if (docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) {
                        PageableWrapper pageable = (PageableWrapper) input.readObject();
                        PageableWrapper formattedPageable = new PageableWrapper();
                        formattedPageable.setNotSerialized(false);
                        for (int index = 0; index < pageable.getNumberOfPages(); index++) {
                            PrintableWrapper printable = (PrintableWrapper) pageable.getPrintable(index);
                            PageFormat originalPageFormat = pageable.getPageFormat(index);
                            Paper originalPaper = originalPageFormat.getPaper();
                            PageFormat newPageFormat = new PageFormat();
                            newPageFormat.setOrientation(originalPageFormat.getOrientation());
                            Paper newPaper = new Paper();
                            newPaper.setSize(originalPaper.getWidth(), originalPaper.getHeight());
                            newPaper.setImageableArea(originalPaper.getImageableX(), originalPaper.getImageableY(), originalPaper.getImageableWidth(), originalPaper.getHeight());
                            newPageFormat.setPaper(newPaper);
                            PageFormat formattedPageFormat = PageableUtils.INSTANCE.getPageFormat(newPageFormat, printRequestAttributeSet);
                            formattedPageable.addPageFormat(new PageFormatWrapper(formattedPageFormat));
                            formattedPageable.addPrintable(printable);
                        }
                        formattedPageable.setNumberOfPages(pageable.getNumberOfPages());
                        doc = new SimpleDoc(formattedPageable, DocFlavor.SERVICE_FORMATTED.PAGEABLE, new HashDocAttributeSet());
                    } else if (docFlavor.equals(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) {
                        PrintableWrapper printable = (PrintableWrapper) input.readObject();
                        doc = new SimpleDoc(printable, DocFlavor.SERVICE_FORMATTED.PRINTABLE, new HashDocAttributeSet());
                    }
                    printRequestAttributeSet = PrintServiceUtils.removeCategoryAttribute(printRequestAttributeSet, Copies.class);
                } else {
                    doc = new SimpleDoc(printData, docFlavor, docAttributeSet);
                }
                for (int count = 0; count < replicatePrinting; count++) {
                    DocPrintJob printJob = printService.createPrintJob();
                    if (printJob instanceof RemotePrintJob) {
                        ((RemotePrintJob) printJob).setAttributes(printJobAttributeSet);
                    }
                    printJob.print(doc, printRequestAttributeSet);
                }
            }
        } catch (PrintException e) {
            LOG.error(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
