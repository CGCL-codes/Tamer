package geneview2.def;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 * SearchData wird mit der Methode doSearch(Keyword) mit den Ergebnissen der
 * Suche nach diesem Keyword geladen. Es enthaelt dann die Beschreibung und die
 * (nicht stabile(!)) direkte Zugriffsnummer (NIHNumber), mit der die Methode
 * parseFromNIH des Parsers den GenebankEntry laden kann.
 * 
 * ACHTUNG: Das ist alles hart codiert und nicht darauf ausgelegt, da� das NIH
 * sein Format �ndert...
 */
public class SearchData implements Runnable {

    private Vector description, NIHNumber;

    private String keyword = "";

    private SearchProgress sProgress;

    public SearchData() {
        description = new Vector();
        NIHNumber = new Vector();
        sProgress = new SearchProgress(null);
    }

    /**
     * anzahlResults liefert die Anzahl der eingetragenen Resultate zurueck, ist
     * 0, wenn keine Ergebnisse auf das Keyword gefunden
     */
    public final int anzahlResults() {
        return description.size();
    }

    /**
     * addResult bekommt die anzuf�gende Beschreibung und die anzufuegende
     * NIHNumber und f�gt diese an die Liste an
     */
    public void addResult(String newDescription, String newNIHNumber) {
        description.addElement(newDescription);
        NIHNumber.addElement(newNIHNumber);
    }

    /** getDescription() liefert den kompletten Vector der Beschreibung zurueck */
    public final Vector getDescription() {
        return description;
    }

    /**
     * getDescription(int index) liefert die Beschreibung fuer den angegebenen
     * Index zurueck
     */
    public final String getDescription(int index) {
        return (String) description.elementAt(index);
    }

    /**
     * getNIHNumber(int index) liefert die NIHNumber fuer den angegebenen Index
     * zurueck
     */
    public final String getNIHNumber(int index) {
        return (String) NIHNumber.elementAt(index);
    }

    public void run() {
        try {
            URL searchURL = new URL("http://www.ncbi.nlm.nih.gov/irx/cgi-bin/genbank?" + keyword);
            sProgress.setText("Connecting to NIH...");
            URLConnection connection = searchURL.openConnection();
            sProgress.setText("Retrieving search result...");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            sProgress.setText("Parsing search result...");
            String inputLine;
            String desc, nih;
            description = new Vector();
            NIHNumber = new Vector();
            while ((inputLine = in.readLine()) != null) {
                if ((inputLine.length() > 30) && (inputLine.substring(0, 26).equals("<A HREF=\"birx_doc?genbank+"))) {
                    nih = inputLine.substring(26, inputLine.indexOf('\"', 26));
                    desc = inputLine.substring(inputLine.indexOf('['));
                    addResult(desc, nih);
                }
            }
            in.close();
            sProgress.setText("Done");
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "I/O Error:\n" + ioe.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * doSearch(String keyword) sucht auf der Seite des NIH nach dem angegebenen
     * Keyword und fuellt SearchData mit den zurueckgelieferten Ergebnissen
     */
    public void doSearch(String aKeyword) {
        keyword = aKeyword;
        Thread searchThread = new Thread(this);
        searchThread.setPriority(Thread.MIN_PRIORITY);
        searchThread.start();
        sProgress.setThread(searchThread);
        Thread infoThread = new Thread(sProgress);
        infoThread.setPriority(Thread.MIN_PRIORITY);
        infoThread.start();
        sProgress.setVisible(true);
    }
}
