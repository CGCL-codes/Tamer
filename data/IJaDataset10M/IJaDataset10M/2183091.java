package org.makagiga.commons.print;

import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import javax.swing.JTable;
import org.makagiga.commons.Flags;

/**
 * @since 3.2
 */
public class TablePrintInfo extends ComponentPrintInfo<JTable> {

    public TablePrintInfo(final String documentTitle, final JTable table, final String header, final String footer) {
        super(documentTitle, table, header, footer);
    }

    @Override
    public void beforePrint() {
        super.beforePrint();
        JTable table = getPrintComponent();
        if (table != null) table.clearSelection();
    }

    public Printable getPrintable(final Flags flags) throws PrinterException {
        return getPrintComponent().getPrintable(getPrintMode(flags), getPrintHeader(flags.isSet(PRINT_HEADER)), getPrintFooter(flags.isSet(PRINT_FOOTER)));
    }

    public static JTable.PrintMode getPrintMode(final Flags flags) {
        return flags.isSet(TABLE_FIT_WIDTH) ? JTable.PrintMode.FIT_WIDTH : JTable.PrintMode.NORMAL;
    }

    public PrintInfo.PrintResult printDocument(final Flags flags) throws PrinterException {
        return printDocument(getPrintComponent(), flags, getPrintHeader(flags.isSet(PRINT_HEADER)), getPrintFooter(flags.isSet(PRINT_FOOTER)), getPrintTitle());
    }

    /**
	 * @since 3.4
	 */
    public static PrintInfo.PrintResult printDocument(final JTable table, final Flags flags, final MessageFormat header, final MessageFormat footer) throws PrinterException {
        return printDocument(table, flags, header, footer, null);
    }

    /**
	 * @since 3.8.11
	 */
    public static PrintInfo.PrintResult printDocument(final JTable table, final Flags flags, final MessageFormat header, final MessageFormat footer, final String printTitle) throws PrinterException {
        if (table.print(getPrintMode(flags), header, footer, true, getPrintRequestAttributeSet(flags, printTitle), true)) return PrintInfo.PrintResult.COMPLETE;
        return PrintInfo.PrintResult.CANCELLED;
    }
}
