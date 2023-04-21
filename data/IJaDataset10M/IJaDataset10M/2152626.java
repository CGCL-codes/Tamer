package org.hardtokenmgmt.core.print;

import java.awt.print.PrinterException;
import java.util.List;
import java.util.logging.Level;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import org.hardtokenmgmt.core.InterfaceFactory;
import org.hardtokenmgmt.core.log.LocalLog;
import org.hardtokenmgmt.core.settings.AdministratorSettings;
import org.hardtokenmgmt.core.settings.BadConfigurationException;
import org.hardtokenmgmt.core.settings.GlobalSettings;
import org.hardtokenmgmt.ws.gen.PinDataWS;
import org.hardtokenmgmt.ws.gen.PrintDataParameter;
import org.hardtokenmgmt.ws.gen.UserDataVOWS;

/**
 * External interface used for printing functionality in the framework.
 * Should only be fetched through the InterfaceFactory.
 * 
 * Uses the configuration in administrator settings and global configuration.
 * 
 * 
 * 
 * @author Philip Vendil 25 jul 2009
 *
 * @version $Id$
 */
public class PrintManager {

    public static final String PRINTEVENT_CARDISSUE = "cardissue";

    public static final String PRINTEVENT_CARDUNBLOCK = "cardunblock";

    public static final String PRINTEVENT_CARDRENEW = "cardrenew";

    public static final String PRINTEVENT_CARDREVOKE = "cardrevoke";

    public static final String PRINTEVENT_CARDREACTIVATE = "cardreactivate";

    public static final String PRINTERTYPE_VISUALPRINTER = "visualprinter";

    public static final String PRINTERTYPE_LETTER = "letter";

    public static final String PRINTERTYPE_LABEL = "label";

    public static final String PRINTERTYPE_ENVELOPE = "envelope";

    /**
	 * Method that will print the printing templates connected to the given printEvent.
	 * 
	 * See documentation for more details regarding configuring global.properties for
	 * printing.
	 * 
	 * @param printEvent one of the PRINTEVENT_ constants
	 * @param uniqueId the unique Id of the user
	 * @param commonName the common name of the user.
	 * @param pinData optional pin data of the token
	 * @param userDataVO user data fetch from the user data source
	 * @param printParameters optional print parameter fetched from the user data source
	 * 
	 * @throws BadConfigurationException if the global configuration or administrative settings is badly configured.
	 * @throws PrinterException if some error happened during printing
	 */
    public void print(String printEvent, String uniqueId, String commonName, List<PinDataWS> pinData, UserDataVOWS userDataVO, List<PrintDataParameter> printParameters) throws BadConfigurationException, PrinterException {
    }

    /**
	 * Print method that will fetch user data from the configured user data source if 
	 * printing have been enabled for the given printEvent in the global configuration.
	 * 
	 * @param printEvent one of the PRINTEVENT_ constants
	 * @param uniqueId the unique Id of the user
	 * @param commonName the common name of the user
	 * @param pinData optional pin data of the token
	 * 
	 * @throws BadConfigurationException if the global configuration or administrative settings is badly configured.
	 * @throws PrinterException if some error happened during printing
	 */
    public void print(String printEvent, String uniqueId, String commonName, List<PinDataWS> pinData) throws BadConfigurationException, PrinterException {
    }

    /**
	 * Returns the currently available printers on the workstation.
	 * @return an array of the printer id's of available printers.
	 */
    public String[] getAvailablePrinters() {
        String[] printerNames = new String[0];
        try {
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
            printerNames = new String[printService.length];
            for (int i = 0; i < printService.length; i++) {
                printerNames[i] = printService[i].getName();
            }
        } catch (Throwable e) {
            LocalLog.getLogger().log(Level.SEVERE, "Error occured when listing available printers : " + e.getMessage(), e);
        }
        return printerNames;
    }

    protected GlobalSettings getGlobalProperties() {
        return InterfaceFactory.getGlobalSettings();
    }

    protected AdministratorSettings getAdminSetting() {
        return InterfaceFactory.getAdministratorSettings();
    }
}
