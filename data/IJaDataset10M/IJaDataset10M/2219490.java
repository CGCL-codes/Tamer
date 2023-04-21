package org.epo.jbps.OutputInterface;

/**
 * Object representing the Output Destination Printer (Print Infrastructure)
 * It contains then the information concerning the printing
 *
 * @author Infotel Conseil
 */
public class DestPrinter extends OutputDestination {

    private String printerName = "UNKNOWN";

    /**
 * DestPrinter constructor
 * @param thePrinterName theName
 */
    public DestPrinter(String thePrinterName) {
        this.setPrinterName(thePrinterName);
    }

    /**
 * printerName variable accesser
 * @return String
 */
    public String getPrinterName() {
        return printerName;
    }

    /**
 * Return type of OutputDestination the destPrinter is
 * @return char
 */
    public char getType() {
        return DEST_PRINTER;
    }

    /**
 * printerName variable setter
 * @param newValue String
 */
    private void setPrinterName(String newValue) {
        this.printerName = newValue;
    }
}
