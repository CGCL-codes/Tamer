package nl.BobbinWork.bwlib.gui;

import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import static java.awt.event.KeyEvent.VK_P;
import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * A menu to print the diagram
 *
 */
@SuppressWarnings("serial")
public class PrintMenu extends JMenu {

    private PrinterJob printJob = null;

    private PageFormat pageFormat = null;

    private PrintablePreviewer printablePreviewer;

    public interface PrintablePreviewer extends Printable {

        public void setPageFormat(PageFormat pageFormat);
    }

    private boolean initPrint() {
        if (printJob != null && pageFormat != null) return true;
        try {
            printJob = PrinterJob.getPrinterJob();
            pageFormat = printJob.defaultPage();
            return true;
        } catch (Exception e) {
            printJob = null;
            pageFormat = null;
            return false;
        }
    }

    /** Print the diagram with a chance to adjust options or cancel. */
    private void adjustablePrint() {
        if (!initPrint()) return;
        printJob.setPrintable(printablePreviewer, pageFormat);
        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /** Changes the page format with a dialog. */
    private void updatePageFormat() {
        if (initPrint()) {
            pageFormat = printJob.pageDialog(pageFormat);
            printablePreviewer.setPageFormat(pageFormat);
        }
    }

    /** Creates a fully dressed JMenu, to print the diagram */
    public PrintMenu(final PrintablePreviewer printablePreviewer) {
        this.printablePreviewer = printablePreviewer;
        applyStrings(this, "MenuPrint");
        JMenuItem jMenuItem;
        jMenuItem = new LocaleMenuItem("MenuPrint_PageSetup");
        jMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                updatePageFormat();
            }
        });
        add(jMenuItem);
        jMenuItem = new LocaleMenuItem("MenuPrint_print", VK_P, CTRL_DOWN_MASK);
        jMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                adjustablePrint();
            }
        });
        add(jMenuItem);
    }
}
