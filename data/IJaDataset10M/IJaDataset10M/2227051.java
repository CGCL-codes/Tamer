package basis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author David V
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HighscoreXMLWriter extends XMLWriter {

    private String[] highScoresStrings;

    private ArrayList highScores;

    private boolean highscoreGevonden = false;

    private boolean highScoreTeamGevonden = false;

    private boolean highScoreTijdGevonden = false;

    private boolean teamGevonden = false;

    private boolean scoreTeamGevonden = false;

    private boolean tijdTeamGevonden = false;

    private boolean rondeTeamGevonden = false;

    private boolean spelerGevonden = false;

    private String teamNaam, highScoreTeam;

    private int spelerTeller = 0, teamTeller = 0, scoreTeam = 0, tijdTeam = 0, rondeTeam = 0;

    private Integer highScoreTijd, highScoreScore;

    private File file;

    public HighscoreXMLWriter(String bestandsNaam) {
        super(bestandsNaam);
        this.highScores = new ArrayList();
    }

    public boolean laadBestand(String bestandsNaam) {
        tekstTag = "";
        eindTag = "";
        highScores.clear();
        boolean gelezen = false;
        try {
            saxParser = factory.newSAXParser();
            file = new File(bestandsNaam);
            if (file.length() == 0) {
            } else {
                saxParser.parse(file, handler);
            }
            gelezen = true;
        } catch (Throwable t) {
            t.printStackTrace();
            gelezen = false;
        }
        return gelezen;
    }

    public void nieuweHighscoreSet() throws SAXException {
        tekstTag = "";
        eindTag = "</highscores>";
        startDocument();
        emit("<highscores>");
        nl();
    }

    public void startElement(String namespaceURI, String sName, String qName, Attributes attrs) throws SAXException {
        String eName = sName;
        if ("".equals(eName)) eName = qName;
        emit("<" + eName);
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                String aName = attrs.getLocalName(i);
                if ("".equals(aName)) aName = attrs.getQName(i);
                emit(" ");
                emit(aName + "=\"" + attrs.getValue(i) + "\"");
            }
        }
        emit(">");
        if (eName.equals("highscore")) {
            if (attrs != null) {
                for (int i = 0; i < attrs.getLength(); i++) {
                    String aName = attrs.getLocalName(i);
                    if ("".equals(aName)) aName = attrs.getQName(i);
                    if (aName.equals("score")) {
                        highScoreScore = Integer.valueOf(attrs.getValue(i));
                    }
                }
            }
        }
        if (eName.equals("Hteam")) {
            highScoreTeamGevonden = true;
        }
        if (eName.equals("Htijd")) {
            highScoreTijdGevonden = true;
        }
        if (eName.equals("score")) {
            scoreTeamGevonden = true;
        }
        if (eName.equals("tijd")) {
            tijdTeamGevonden = true;
        }
        if (eName.equals("ronde")) {
            rondeTeamGevonden = true;
        }
    }

    public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
        if (qName.equals("score")) {
            scoreTeamGevonden = false;
        }
        if (qName.equals("tijd")) {
            tijdTeamGevonden = false;
        }
        if (qName.equals("ronde")) {
            rondeTeamGevonden = false;
        }
        if (qName.equals("Hteam")) {
            highScoreTeamGevonden = false;
        }
        if (qName.equals("Htijd")) {
            highScoreTijdGevonden = false;
        }
        if (qName.equals("highscore")) {
            highScores.add(highScoreScore);
            highScores.add(highScoreTeam);
            highScores.add(highScoreTijd);
        }
        if (qName.equals("highscores")) eindTag = "</highscores>"; else emit("</" + qName + ">");
    }

    public void characters(char buf[], int offset, int len) throws SAXException {
        String s = new String(buf, offset, len);
        emit(s);
        if (scoreTeamGevonden) {
            scoreTeam = Integer.parseInt(s);
        }
        if (tijdTeamGevonden) {
            tijdTeam = Integer.parseInt(s);
        }
        if (rondeTeamGevonden) {
            rondeTeam = Integer.parseInt(s);
        }
        if (highScoreTeamGevonden) {
            highScoreTeam = String.valueOf(s);
        }
        if (highScoreTijdGevonden) {
            highScoreTijd = Integer.valueOf(s);
        }
    }

    public void startDocument() throws SAXException {
        emit("<?xml version='1.0' encoding='UTF-8'?>");
        nl();
    }

    public void endDocument() throws SAXException {
        nl();
    }

    public void emit(String s) throws SAXException {
        tekstTag += s;
    }

    public void nl() throws SAXException {
        String lineEnd = System.getProperty("line.separator");
        tekstTag += lineEnd;
    }

    public void voegHighscoreToe(int score, String naamTeam, int tijd) throws SAXException {
        char quote = (char) 34;
        emit("<highscore score=" + quote + score + quote + ">");
        nl();
        emit("<Hteam>" + naamTeam + "</Hteam>");
        nl();
        emit("<Htijd>" + tijd + "</Htijd>");
        nl();
        emit("</highscore>");
        nl();
    }

    public void schrijfHighscores() {
        try {
            out = new FileOutputStream(bestandsNaam);
        } catch (FileNotFoundException e) {
            System.out.println("Foutje bij het openen van de FileOutputStream");
        }
        p = new PrintStream(out);
        try {
            wOut = new OutputStreamWriter(out, "UTF8");
        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            wOut.write(tekstTag);
            wOut.write(eindTag);
            wOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        highScores = null;
        highScores = new ArrayList();
        tekstTag = "";
        eindTag = "";
        laadBestand("XML/highscores.xml");
    }

    public String[] getHighScores() {
        highScoresStrings = new String[highScores.size()];
        int highscoreTeller = 0;
        Iterator highIt = highScores.iterator();
        while (highIt.hasNext()) {
            highScoresStrings[highscoreTeller] = highIt.next().toString();
            highscoreTeller++;
        }
        return highScoresStrings;
    }
}
