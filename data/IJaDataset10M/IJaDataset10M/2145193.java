package util;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.TTCCLayout;

public class LogFrame extends JFrame {

    /**
     * Der TextPaneAppender schreibt die Meldungen in eine JTextPane
     */
    private util.TextPaneAppender tpa = null;

    /**
     * Das Category-Objekt zum Erzeugen der Meldungen
     */
    private Category cat = null;

    /**
     * Liefert das Category-Objekt
     */
    public Category getCategory() {
        return cat;
    }

    /**
     * Konstruktor
     */
    public LogFrame(String title, String name) {
        super(title);
        tpa = new util.TextPaneAppender(new PatternLayout("%d{HH:mm:ss} [%-5p]  %m%n"), name);
        cat = Category.getInstance(name);
        cat.addAppender(tpa);
        getContentPane().add(new javax.swing.JScrollPane(tpa.getTextPane()));
        tpa.getTextPane().setBackground(Color.white);
    }

    /**
     * Beispielanwendung
     */
    public static void main(String[] args) {
        LogFrame logFrame = new LogFrame("LogFrame", "SpeaR-Control");
        logFrame.setSize(600, 600);
        logFrame.setVisible(true);
        for (int i = 0; i < 30; i++) logFrame.cat.info("TEST " + i);
    }
}
