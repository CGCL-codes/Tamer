package ag.ion.noa.internal.printing;

import ag.ion.bion.officelayer.document.DocumentException;
import ag.ion.bion.officelayer.document.IDocument;
import ag.ion.noa.NOAException;
import ag.ion.noa.printing.IPrintProperties;
import ag.ion.noa.printing.IPrintService;
import ag.ion.noa.printing.IPrinter;
import com.sun.star.beans.PropertyValue;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.view.XPrintable;

/**
 * Service for printing documents.
 * 
 * @author Markus Kr�ger
 * @version $Revision: 10398 $
 */
public class PrintService implements IPrintService {

    private IDocument document = null;

    /**
	 * Constructs new PrintService.
	 * 
	 * @param document
	 *            the document using this print service
	 * 
	 * @author Markus Kr�ger
	 * @date 16.08.2007
	 */
    public PrintService(IDocument document) {
        if (document == null) throw new NullPointerException("Invalid document for print service.");
        this.document = document;
    }

    /**
	 * Prints the document to the active printer.
	 * 
	 * @throws DocumentException
	 *             if printing fails
	 * 
	 * @author Markus Kr�ger
	 * @date 16.08.2007
	 */
    public void print() throws DocumentException {
        print(null);
    }

    /**
	 * Prints the document to the active printer with the given print
	 * properties.
	 * 
	 * @param printProperties
	 *            the properties to print with, or null to use default settings
	 * 
	 * @throws DocumentException
	 *             if printing fails
	 * 
	 * @author Markus Kr�ger
	 * @date 16.08.2007
	 */
    public void print(IPrintProperties printProperties) throws DocumentException {
        try {
            XPrintable xPrintable = (XPrintable) UnoRuntime.queryInterface(XPrintable.class, document.getXComponent());
            PropertyValue[] printOpts = null;
            if (printProperties != null) {
                if (printProperties.getPages() != null) printOpts = new PropertyValue[2]; else printOpts = new PropertyValue[1];
                printOpts[0] = new PropertyValue();
                printOpts[0].Name = "CopyCount";
                printOpts[0].Value = printProperties.getCopyCount();
                if (printProperties.getPages() != null) {
                    printOpts[1] = new PropertyValue();
                    printOpts[1].Name = "Pages";
                    printOpts[1].Value = printProperties.getPages();
                }
            } else printOpts = new PropertyValue[0];
            xPrintable.print(printOpts);
        } catch (Throwable throwable) {
            throw new DocumentException(throwable);
        }
    }

    /**
	 * Returns if the active printer is busy.
	 * 
	 * @return if the active printer is busy
	 * 
	 * @throws NOAException
	 *             if the busy state could not be retrieved
	 * 
	 * @author Markus Kr�ger
	 * @date 16.08.2007
	 */
    public boolean isActivePrinterBusy() throws NOAException {
        try {
            XPrintable xPrintable = (XPrintable) UnoRuntime.queryInterface(XPrintable.class, document.getXComponent());
            PropertyValue[] printerProps = xPrintable.getPrinter();
            Boolean busy = new Boolean(false);
            for (int i = 0; i < printerProps.length; i++) {
                if (printerProps[i].Name.equals("IsBusy")) busy = (Boolean) printerProps[i].Value;
            }
            return busy.booleanValue();
        } catch (Throwable throwable) {
            throw new NOAException(throwable);
        }
    }

    /**
	 * Returns the active printer.
	 * 
	 * @return the active printer
	 * 
	 * @throws NOAException
	 *             if printer could not be retrieved
	 * 
	 * @author Markus Kr�ger
	 * @date 16.08.2007
	 */
    public IPrinter getActivePrinter() throws NOAException {
        try {
            XPrintable xPrintable = (XPrintable) UnoRuntime.queryInterface(XPrintable.class, document.getXComponent());
            PropertyValue[] printerProps = xPrintable.getPrinter();
            String name = null;
            for (int i = 0; i < printerProps.length; i++) {
                if (printerProps[i].Name.equals("Name")) name = (String) printerProps[i].Value;
            }
            return new Printer(name);
        } catch (Throwable throwable) {
            throw new NOAException(throwable);
        }
    }

    /**
	 * Sets the active printer.
	 * 
	 * @param printer
	 *            the printer to be set to be active
	 * 
	 * @throws NOAException
	 *             if printer could not be set
	 * 
	 * @author Markus Kr�ger
	 * @date 16.08.2007
	 */
    public void setActivePrinter(IPrinter printer) throws NOAException {
        try {
            if (printer == null) throw new NullPointerException("Invalid printer to be set");
            XPrintable xPrintable = (XPrintable) UnoRuntime.queryInterface(XPrintable.class, document.getXComponent());
            PropertyValue[] printerDesc = new PropertyValue[1];
            printerDesc[0] = new PropertyValue();
            printerDesc[0].Name = "Name";
            printerDesc[0].Value = printer.getName();
            xPrintable.setPrinter(printerDesc);
        } catch (Throwable throwable) {
            throw new NOAException(throwable);
        }
    }

    /**
	 * Constructs a printer with the given properties and returns it.
	 * 
	 * @param name
	 *            the name of the printer cue to be used
	 * 
	 * @return the constructed printer
	 * 
	 * @throws NOAException
	 *             if printer could not be constructed
	 * 
	 * @author Markus Kr�ger
	 * @date 16.08.2007
	 */
    public IPrinter createPrinter(String name) throws NOAException {
        return new Printer(name);
    }
}
