package ocumed.teams.teamb.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterGraphics;
import java.awt.print.PrinterJob;

public class PrintableRezept implements Printable {

    public int iResMul = 4;

    RezeptErstellenModel m_tablemodel;

    public PrintableRezept(RezeptErstellenModel tablemodel) {
        m_tablemodel = tablemodel;
    }

    public int print(Graphics g, PageFormat pf, int iPage) throws PrinterException {
        final int FONTSIZE = 12;
        final double PNT_MM = 25.4 / 72.;
        if (0 != iPage) return NO_SUCH_PAGE;
        try {
            int iPosX = 1;
            int iPosY = 1;
            int iAddY = FONTSIZE * 3 / 2 * iResMul;
            Graphics2D g2 = (Graphics2D) g;
            PrinterJob prjob = ((PrinterGraphics) g2).getPrinterJob();
            g2.translate(pf.getImageableX(), pf.getImageableY());
            g2.scale(1.0 / iResMul, 1.0 / iResMul);
            g2.setFont(new Font("SansSerif", Font.PLAIN, FONTSIZE * iResMul));
            g2.setColor(Color.black);
            iPosX += iAddY;
            iPosY += iAddY / 2;
            g2.drawString("Rezept", iPosX, iPosY += iAddY);
            g2.drawString("", iPosX, iPosY += (iAddY * 2));
            for (int i = 0; i < m_tablemodel.getRowCount(); i++) {
                String val = (String) m_tablemodel.getValueAt(i, 0);
                Integer intVal = Integer.valueOf(val);
                if (intVal > 0) {
                    g2.drawString(m_tablemodel.getValueAt(i, 0) + "x " + m_tablemodel.getValueAt(i, 1) + ", Packungsgr��e: " + m_tablemodel.getValueAt(i, 2) + ", Dosierung: " + m_tablemodel.getValueAt(i, 3), iPosX, iPosY += iAddY);
                }
            }
            g2.drawString("Unterschrift", iPosX, iPosY += (iAddY * 3));
        } catch (Exception ex) {
            throw new PrinterException(ex.getMessage());
        }
        return PAGE_EXISTS;
    }

    private static double dbldgt(double d) {
        return Math.round(d * 10.) / 10.;
    }
}
