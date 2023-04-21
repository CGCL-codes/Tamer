package tournoi.component;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Logger;
import tournoi.*;

/**
 * permet d'imprimer une poule
 */
public class PrintingPoule implements Printable {

    private static Logger logger = Logger.getLogger(PrintingMatch.class);

    private ArrayList printedPoulesList;

    public PrintingPoule() {
        if (printedPoulesList == null) {
            printedPoulesList = new ArrayList();
        }
        printedPoulesList.clear();
    }

    public void addPrintedPoule(PrintedPoule printedPoule) {
        printedPoulesList.add(printedPoule);
    }

    private int getTotalPage(PageFormat format) {
        int totalPages = 0;
        totalPages = (int) (printedPoulesList.size() / getPoulesParPage(format));
        if ((printedPoulesList.size() % getPoulesParPage(format)) > 0) {
            totalPages += 1;
        }
        return totalPages;
    }

    private int getMaxPouleSize() {
        int size = 0;
        for (Iterator iter = printedPoulesList.iterator(); iter.hasNext(); ) {
            Poule poule = (Poule) ((PrintedPoule) iter.next()).getPoule();
            int pouleSize = PrintingPoule.getPouleHeight(poule);
            if (pouleSize > size) {
                size = pouleSize;
            }
        }
        return size;
    }

    private int getPoulesParPage(PageFormat format) {
        int size = getMaxPouleSize();
        int result = (int) (format.getImageableHeight() / size);
        if (result == 0) return 1; else return result;
    }

    /**
	 * a revoir !!!!
	 * @param poule
	 * @return
	 */
    public static synchronized int getPouleHeight(Poule poule) {
        int line = 150;
        line += poule.getJoueurs().size() * 15;
        line += poule.getMatchs().size() * 30;
        return line;
    }

    public static synchronized int getPouleWidth(Poule poule) {
        return 400;
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        Poule poule = null;
        int poulesParPage = getPoulesParPage(pageFormat);
        Graphics2D g2 = (Graphics2D) graphics;
        Font f = Font.getFont("Courier");
        double scaleW = 1.0;
        double scaleH = 1.0;
        if (poulesParPage == 1) {
            poule = ((PrintedPoule) printedPoulesList.get(0)).getPoule();
            if (pageFormat.getImageableWidth() < PrintingPoule.getPouleWidth(poule)) {
                scaleW = pageFormat.getImageableWidth() / PrintingPoule.getPouleWidth(poule);
            }
            if (pageFormat.getImageableHeight() < PrintingPoule.getPouleHeight(poule)) {
                scaleH = pageFormat.getImageableHeight() / PrintingPoule.getPouleHeight(poule);
            }
            g2.setClip(0, 0, PrintingPoule.getPouleWidth(poule) + 110, PrintingPoule.getPouleHeight(poule) + 50);
            g2.scale(scaleW, scaleH);
        }
        int current = 0;
        int totalPages = getTotalPage(pageFormat);
        String tabName = "";
        if (pageIndex >= totalPages) return NO_SUCH_PAGE;
        if (pageIndex == 0) current = 0;
        g2.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());
        g2.setColor(Color.black);
        int line = 30;
        for (int x = 0; x < poulesParPage; x++) {
            current = (poulesParPage * pageIndex) + x;
            if (current < printedPoulesList.size()) {
                poule = ((PrintedPoule) printedPoulesList.get(current)).getPoule();
                tabName = ((PrintedPoule) printedPoulesList.get(current)).getTabName();
                ArrayList joueurs = poule.getJoueurs();
                g2.setFont(new Font("Helvetica", Font.PLAIN, 20));
                g2.drawString(poule.getName() + "  -  " + tabName, 0, line);
                line += 30;
                g2.setFont(new Font("Helvetica", Font.PLAIN, 15));
                g2.drawString("Liste des joueurs : ", 0, line);
                g2.drawString("Classement final : ", 270, line);
                line += 20;
                g2.setFont(new Font("Helvetica", Font.PLAIN, 10));
                int numJoueur = 1;
                for (Iterator iter = joueurs.iterator(); iter.hasNext(); ) {
                    Joueur j = (Joueur) iter.next();
                    g2.drawString("      " + numJoueur + " - " + j.toString(), 0, line);
                    g2.drawString("      " + numJoueur + " - ", 270, line);
                    line += 15;
                    numJoueur++;
                }
                line += 10;
                g2.setFont(new Font("Helvetica", Font.PLAIN, 15));
                g2.drawString("Liste des rencontres : ", 0, line);
                line += 20;
                int x1 = 0;
                int y1 = line - 15;
                g2.setFont(new Font("Helvetica", Font.PLAIN, 10));
                for (Iterator iter = poule.getMatchs().iterator(); iter.hasNext(); ) {
                    Match match = (Match) iter.next();
                    String strJ1 = match.getJoueur1().getNom() + " " + match.getJoueur1().getPrenom().substring(0, 1) + ".";
                    String strJ2 = match.getJoueur2().getNom() + " " + match.getJoueur2().getPrenom().substring(0, 1) + ".";
                    if (strJ1.length() > 12) strJ1 = strJ1.substring(0, 12);
                    if (strJ2.length() > 12) strJ2 = strJ2.substring(0, 12);
                    g2.drawString("  " + (joueurs.indexOf(match.getJoueur1()) + 1) + "-" + strJ1, 0, line);
                    line += 15;
                    g2.drawString("  " + (joueurs.indexOf(match.getJoueur2()) + 1) + "-" + strJ2, 0, line);
                    line += 15;
                }
                int height = (poule.getMatchs().size() * 15 * 2) + 5;
                int width = 200;
                g2.drawRect(x1, y1, width, height);
                int yTemp = y1 + 35;
                int xTemp = x1 + 100;
                for (Iterator iter = poule.getMatchs().iterator(); iter.hasNext(); ) {
                    Match match = (Match) iter.next();
                    g2.drawLine(x1, yTemp, x1 + width, yTemp);
                    g2.drawLine(x1 + 100, yTemp - 15, x1 + width, yTemp - 15);
                    yTemp += 30;
                }
                for (int i = 0; i < 5; i++) {
                    g2.drawLine(xTemp, y1, xTemp, y1 + height);
                    xTemp += 20;
                }
                line = yTemp + 20;
            }
        }
        return PAGE_EXISTS;
    }
}
