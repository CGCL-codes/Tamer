package net.sf.paperclips.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.*;
import net.sf.paperclips.*;
import net.sf.paperclips.ui.PrintPreview;

/**
 * Demonstrate use of BigPrint.
 * 
 * @author Matthew
 */
public class Snippet3 {

    public static Print createPrint() {
        DefaultGridLook look = new DefaultGridLook();
        look.setCellBorder(new LineBorder());
        GridPrint grid = new GridPrint(look);
        final int ROWS = 60;
        final int COLS = 10;
        for (int i = 0; i < COLS; i++) grid.addColumn("p");
        for (int r = 0; r < ROWS; r++) for (int c = 0; c < COLS; c++) grid.add(new TextPrint("Row " + r + " Col " + c));
        return new BigPrint(grid);
    }

    /**
   * Executes the snippet.
   * 
   * @param args command-line args.
   */
    public static void main(String[] args) {
        Display display = Display.getDefault();
        final Shell shell = new Shell(display);
        shell.setText("Snippet3.java");
        shell.setBounds(100, 100, 640, 480);
        shell.setLayout(new GridLayout(3, false));
        Button prevPage = new Button(shell, SWT.PUSH);
        prevPage.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT, false, false));
        prevPage.setText("Previous Page");
        Button nextPage = new Button(shell, SWT.PUSH);
        nextPage.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT, false, false));
        nextPage.setText("Next Page");
        Button printButton = new Button(shell, SWT.PUSH);
        printButton.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT, false, false));
        printButton.setText("Print");
        final PrintPreview preview = new PrintPreview(shell, SWT.BORDER);
        preview.setFitHorizontal(true);
        preview.setFitVertical(true);
        preview.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        final PrintJob job = new PrintJob("Snippet3.java", createPrint());
        job.setMargins(72);
        preview.setPrintJob(job);
        prevPage.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                int page = Math.max(preview.getPageIndex() - 1, 0);
                preview.setPageIndex(page);
            }
        });
        nextPage.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                int page = Math.min(preview.getPageIndex() + 1, preview.getPageCount() - 1);
                preview.setPageIndex(page);
            }
        });
        printButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                PrintDialog dialog = new PrintDialog(shell, SWT.NONE);
                PrinterData printerData = dialog.open();
                if (printerData != null) {
                    PaperClips.print(job, printerData);
                    preview.setPrinterData(printerData);
                }
            }
        });
        shell.setVisible(true);
        while (!shell.isDisposed()) if (!display.readAndDispatch()) display.sleep();
        display.dispose();
    }
}
