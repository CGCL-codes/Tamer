package com.globalretailtech.pos.hardware;

import jpos.JposException;
import jpos.POSPrinterConst;
import com.globalretailtech.data.Site;
import com.globalretailtech.data.TransItem;
import com.globalretailtech.data.TransItemLink;
import com.globalretailtech.data.TransPromotion;
import com.globalretailtech.data.TransTax;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.ej.Ej;
import com.globalretailtech.pos.ej.EjLine;
import com.globalretailtech.pos.ej.EjTender;
import com.globalretailtech.util.Format;
import org.apache.log4j.Logger;

/**
 * PosPrinter hardware class.
 *
 *
 * @author  Quentin Olson
 * @author  Igor Semenko
 * @see jpos.PosPrinter
 */
public class PosPrinter extends Printer {

    static Logger logger = Logger.getLogger(PosPrinter.class);

    private jpos.POSPrinter control;

    private PosParameters posParameters;

    private PosContext context;

    public PosPrinter(jpos.POSPrinter c, String devicename, PosContext context) {
        super(c, devicename);
        setContext(context);
        control = c;
        if (isOpen()) {
            logger.debug("init PosPrinter");
            try {
                control.setRecLetterQuality(true);
            } catch (JposException e) {
                logger.error("Can't init PosPrinter", e);
            }
        }
    }

    public void setContext(PosContext context) {
        this.context = context;
    }

    public PosContext context() {
        return context;
    }

    public jpos.POSPrinter getControl() {
        return (jpos.POSPrinter) control();
    }

    public int getColumns() {
        try {
            logger.debug("device claimed:" + control.getClaimed());
            logger.debug("device enabled:" + control.getDeviceEnabled());
            return control.getRecLineChars();
        } catch (jpos.JposException e) {
            logger.warn(e.toString(), e);
        }
        return 0;
    }

    public void println() {
        try {
            control.printNormal(0, "\n");
        } catch (jpos.JposException e) {
            logger.warn(e.toString(), e);
        }
    }

    public void println(String value) {
        try {
            control.printNormal(POSPrinterConst.PTR_S_RECEIPT, value + "\n");
        } catch (jpos.JposException e) {
            logger.warn(e.toString(), e);
        }
    }

    /** 
	 * Prints Ej as a receipt 
	 **/
    public void printReceipt(Ej ej) throws JposException {
        if (!isOpen()) return;
        printHeader();
        for (int ejIndex = 0; ejIndex < ej.size(); ejIndex++) {
            EjLine line = (EjLine) ej.line(ejIndex);
            switch(line.lineType()) {
                case EjLine.BANK:
                    break;
                case EjLine.ITEM:
                    TransItem transItem = (TransItem) line.dataRecord();
                    if (transItem.state() != TransItem.VOID) {
                        printItem(transItem);
                    }
                    break;
                case EjLine.ITEM_LINK:
                    TransItemLink itemLink = (TransItemLink) line.dataRecord();
                    printItem(itemLink);
                    break;
                case EjLine.PROMOTION:
                    TransPromotion itemPromo = (TransPromotion) line.dataRecord();
                    printItemAdjustment(itemPromo);
                    break;
                case EjLine.TAX:
                    TransTax itemTax = (TransTax) line.dataRecord();
                    break;
                case EjLine.TENDER:
                    EjTender itemTender = (EjTender) line;
                    printTender(itemTender);
                default:
            }
        }
        printTotal(ej);
        printTrailer();
    }

    public void printHeader() throws JposException {
        Site site = context().site();
        control.printNormal(POSPrinterConst.PTR_S_RECEIPT, "|cA" + site.name() + "\n");
        control.printNormal(POSPrinterConst.PTR_S_RECEIPT, "|cA" + site.addr1() + "\n");
        control.printNormal(POSPrinterConst.PTR_S_RECEIPT, "|cA" + site.phone() + "\n");
        control.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\n");
    }

    public void printTrailer() throws JposException {
        control.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\n");
        control.printNormal(POSPrinterConst.PTR_S_RECEIPT, "|cA" + "Thank You" + "\n");
    }

    protected void printTotal(Ej ej) throws JposException {
        double total = ej.ejSubTotal();
        control.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\n");
        control.printNormal(POSPrinterConst.PTR_S_RECEIPT, "|bC|2C" + "TOTAL" + "\n");
        control.printNormal(POSPrinterConst.PTR_S_RECEIPT, "|rA" + Double.toString(total / 100) + "\n");
    }

    /**
	 * @param itemPromo
	 */
    private void printItemAdjustment(TransPromotion itemPromo) throws JposException {
    }

    /**
	 * @param itemTender
	 */
    private void printTender(EjTender itemTender) throws JposException {
        int width = control.getRecLineChars();
        control.printNormal(POSPrinterConst.PTR_S_RECEIPT, Format.print("Cash: ", Double.toString(itemTender.amount() / 100), " ", width) + "\n");
        if (itemTender.change() > 0) control.printNormal(POSPrinterConst.PTR_S_RECEIPT, Format.print("Change: ", Double.toString(itemTender.change() / 100), " ", width) + "\n");
    }

    /**
	 * @param itemLink
	 */
    private void printItem(TransItemLink itemLink) throws JposException {
        int width = control.getRecLineChars();
        control.printNormal(POSPrinterConst.PTR_S_RECEIPT, Format.print("*" + itemLink.itemLinkDesc(), Double.toString(itemLink.amount() / 100), " ", width) + "\n");
    }

    /**
	 * Prints item on RECEIPT station
	 */
    private void printItem(TransItem transItem) throws JposException {
        int width = control.getRecLineChars();
        control.printNormal(POSPrinterConst.PTR_S_RECEIPT, Format.print(transItem.quantity() + " " + transItem.itemDesc(), Double.toString(transItem.amount() / 100), " ", width) + "\n");
    }
}
