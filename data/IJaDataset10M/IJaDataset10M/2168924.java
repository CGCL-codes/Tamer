package no.computanse.open4610.util;

import no.computanse.open4610.Print4610Service;
import no.computanse.open4610.PrintExecutor;
import no.computanse.open4610.Printer4610;
import no.computanse.open4610.Printer4610Setup;

/**
 * @author Tony Olsen
 * 
 */
public class PrintLogoTestUtil {

    private PrintLogoTestUtil() {
        super();
    }

    public static void main(String[] args) {
        PrintLogoTestUtil test = new PrintLogoTestUtil();
        test.printLogoNr1();
    }

    private void printLogoNr1() {
        Printer4610Setup setup = new Printer4610Setup();
        Print4610Service service = setup.getService();
        service.executePrint(new PrintExecutor() {

            public void doPrint(Printer4610 printer) {
                printer.printLine(" ");
                printer.printLogo(1);
                printer.printLine(" ");
                printer.printLine(" ");
                printer.cutPaper();
            }
        });
    }
}
