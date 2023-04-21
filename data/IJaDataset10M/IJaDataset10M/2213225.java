package de.sudokuloeser.gui.actions;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import de.sudokuloeser.gui.JPanelSudokuField;
import de.sudokuloeser.gui.Messages;

/**
 * @author daju
 * @since 1.0.0
 *
 */
public class PrintAction extends AbstractAction {

    JPanelSudokuField toPrint = null;

    PageFormat pf = null;

    public PrintAction(JPanelSudokuField toPrint) {
        this.toPrint = toPrint;
        toolName = Messages.getString("PrintAction.toolName");
        toolDescription = Messages.getString("PrintAction.toolDescription");
        iconPath = Messages.getString("PrintAction.iconPath");
    }

    public void actionPerformed(ActionEvent e) {
        try {
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            if (pf == null) {
                pf = printerJob.defaultPage();
            }
            PageFormat npf = printerJob.pageDialog(pf);
            if (npf == pf) return; else {
                Toolkit tk = Toolkit.getDefaultToolkit();
                PrintJob pj = tk.getPrintJob(new Frame(), Messages.getString("PrintAction.PrintJobName"), null);
                if (pj != null) {
                    Graphics g = pj.getGraphics();
                    toPrint.print(g, pf, 1);
                    g.dispose();
                    pj.end();
                }
            }
        } catch (HeadlessException e1) {
            e1.printStackTrace();
        } catch (PrinterException e1) {
            e1.printStackTrace();
        }
    }
}
