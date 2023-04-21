package util;

import gui.SudokuHauptfensterSWT;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import core.Position;
import core.Sudoku;

/**
 * 
 * @author Viktor Koop
 * 
 */
public class StoreUtil {

    public static Sudoku loadSudokuFromXML(File f) {
        Sudoku s = new Sudoku(false);
        try {
            Document doc = new SAXBuilder().build(f);
            List e = doc.getRootElement().getChildren();
            Element elm = (Element) e.remove(0);
            for (Object elem : e) {
                Element sudokufeld = (Element) elem;
                Attribute sudokufeld_nummer = sudokufeld.getAttribute("nummer");
                Attribute sudokufeld_value = sudokufeld.getAttribute("wert");
                Attribute sudokufeld_changeable = sudokufeld.getAttribute("changable");
                int feldnummer = sudokufeld_nummer.getIntValue();
                boolean changeable = sudokufeld_changeable.getBooleanValue();
                int wert = sudokufeld_value.getIntValue();
                Position p = new Position(feldnummer);
                s.setFeld(wert, p);
                if (changeable) {
                    s.setFeld(p.getZeile(), p.getSpalte(), wert);
                    s.setChangeable(p, true);
                } else {
                    s.setFeld(p.getZeile(), p.getSpalte(), wert);
                    s.setChangeable(p, false);
                }
                Element möglichkeiten = sudokufeld.getChild("moeglichezahlen");
                List lm = möglichkeiten.getChildren();
                for (Object tmp : lm) {
                    Element möglichkeit = (Element) tmp;
                    Attribute möglichkeit_int = möglichkeit.getAttribute("int");
                    int möglichezahl = möglichkeit_int.getIntValue();
                    s.getMöglichkeiten(feldnummer).addElement(möglichezahl);
                }
            }
        } catch (DataConversionException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static Sudoku loadSudokuFromXML(File f, SudokuHauptfensterSWT sp) {
        Sudoku s = new Sudoku(false);
        try {
            Document doc = new SAXBuilder().build(f);
            List e = doc.getRootElement().getChildren();
            Element elm = (Element) e.remove(0);
            if (sp != null) {
                long l = elm.getAttribute("passed").getLongValue();
                sp.r.stoppuhr.stUhr.addAdditionalTime(l);
            }
            for (Object elem : e) {
                Element sudokufeld = (Element) elem;
                Attribute sudokufeld_nummer = sudokufeld.getAttribute("nummer");
                Attribute sudokufeld_value = sudokufeld.getAttribute("wert");
                Attribute sudokufeld_changeable = sudokufeld.getAttribute("changable");
                int feldnummer = sudokufeld_nummer.getIntValue();
                boolean changeable = sudokufeld_changeable.getBooleanValue();
                int wert = sudokufeld_value.getIntValue();
                Position p = new Position(feldnummer);
                s.setFeld(wert, p);
                if (changeable) {
                    s.setFeld(p.getZeile(), p.getSpalte(), wert);
                    s.setChangeable(p, true);
                } else {
                    s.setFeld(p.getZeile(), p.getSpalte(), wert);
                    s.setChangeable(p, false);
                }
                Element möglichkeiten = sudokufeld.getChild("moeglichezahlen");
                List lm = möglichkeiten.getChildren();
                for (Object tmp : lm) {
                    Element möglichkeit = (Element) tmp;
                    Attribute möglichkeit_int = möglichkeit.getAttribute("int");
                    int möglichezahl = möglichkeit_int.getIntValue();
                    s.getMöglichkeiten(feldnummer).addElement(möglichezahl);
                }
            }
            if (sp != null) {
                sp.sudPanel.control.init(s);
            }
        } catch (DataConversionException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static void saveSudokuToXML(Sudoku s, File f, long passedTime) {
        try {
            f.createNewFile();
            PrintStream pt = new PrintStream(f);
            Document doc = new Document();
            Element sudoku = new Element("sudoku");
            doc.addContent(sudoku);
            Element time = new Element("time");
            time.setAttribute("passed", passedTime + "");
            sudoku.addContent(time);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Element sudokuFeld = new Element("sudokufeld");
                    Element möglichkeiten = new Element("moeglichezahlen");
                    sudokuFeld.setAttribute("nummer", (new Position(i, j).getFeldnummer()) + "");
                    sudokuFeld.setAttribute("wert", s.getFeld(i, j) + "");
                    if (s.changeableField[i][j] == true) {
                        sudokuFeld.setAttribute("changable", "true");
                    } else {
                        sudokuFeld.setAttribute("changable", "false");
                    }
                    sudoku.addContent(sudokuFeld);
                    sudokuFeld.addContent(möglichkeiten);
                    System.out.println("Zeile: " + i + " Spalte: " + j + " " + s.getMöglichkeiten(new Position(i, j)));
                    for (int k = 0; k < s.getMöglichkeiten(new Position(i, j)).size(); k++) {
                        int value = s.getMöglichkeiten(new Position(i, j)).elementAt(k);
                        Element möglichkeit = new Element("moeglichezahl");
                        möglichkeit.setAttribute("int", value + "");
                        möglichkeiten.addContent(möglichkeit);
                    }
                }
            }
            XMLOutputter out = new XMLOutputter();
            out.output(doc, pt);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
