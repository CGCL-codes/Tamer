package de.sendorian.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Diese Klasse bietet die M�glichkeit CSV-Dateien (Comma Seperated Values)
 * einzulesen und die Daten �ber #onReadLine weiterzuverarbeiten. Diese Methode
 * muss dazu in der erbenden Klasse �berschrieben werden.
 * </p>
 * 
 * <b>Hier einige Beispiele zur Anwendung der Klasse (sind auch in
 * utiltest.CsvFileReaderTest enthalten):<br>
 * 1. Einfache Ausgabe mit || als Trennzeichen:</b>
 * <p>
 * <pre>
 *
 *  
 CsvFileReader csv = new CsvFileReader() {

    protected void onReadLine(int line, String[] colums) {
        StringBuffer buf = new StringBuffer(20);

        for (int i = 0; i < colums.length; i++) {
            buf.append(colums[i]);
            if (i < colums.length - 1)
                buf.append("||");
        }
        if (line > 1) {
            CsvFileReaderTest.out = out + "\n" + "Line " + line + ":  "
                    + buf.toString();
        } else {
            CsvFileReaderTest.out = "Line " + line + ":  "
                    + buf.toString();
        }
    }
};

try {
    csv.readData(";a;b;c;\"semi;\"");
} catch (java.text.ParseException e) {
} catch (java.io.IOException e) {
}
 *
 * </pre></p> 
 * <b>2. Ausgabe in XML:</b>
 * <p>
 * <pre>
 * 
import java.util.*;
import java.io.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

[...]

final Element root = new Element("Sample");

CsvFileReader csv = new CsvFileReader() {

    protected void onReadLine(int line, String[] colums) {
        if (line == 1) {
            // erste zeile als �berschrift => attributnamen
            columnNames = colums;
        } else {
            Element element = new Element("DataElement");
            for (int i = 0; i < colums.length; i++) {
                Element innerElement = new Element(columnNames[i]);
                innerElement.addContent(colums[i]);
                element.addContent(innerElement);
            }
            root.addContent(element);
        }
    }

    private String[] columnNames = null;
};

try {
    csv
            .readData("Spalte1;Spalte2;Spalte3;Spalte4;Spalte5;Spalte6\n;a;b;c;\"semi;\"");
} catch (java.text.ParseException e) {
} catch (java.io.IOException e) {
}

List list_content = new ArrayList();
list_content.add(root);
Document document = new Document(list_content);
XMLOutputter outputter = new XMLOutputter("  ", true, "ISO-8859-1");
String xml_string = outputter.outputString(document);
try {
    FileWriter out = new FileWriter(new File("c:\\temp\\csv.xml"));
    out.write(xml_string);
    out.close();
} catch (IOException e) {
}
 *  
 *
 * </pre></p>
 * 
 * @author Sendorian
 * @since 22. Mai 2002
 * @version $Revision: 1.7 $ $Date: 2006/10/02 18:08:41 $
 */
public abstract class CsvFileReader {

    /**
     * Liest die Datei ein und gibt die Daten Zeilenweise an #onReadLine weiter.
     * 
     * @param fileName Der Dateiname der CSV-Datei
     * @exception IOException Wird geworfen, wenn die Datei nicht gefunden
     *                wird...
     * @exception java.text.ParseException Wird geworfen, wenn die Daten Fehler
     *                auf- weisen, d.h. nicht den Konventionen des CSV-Formates
     *                entsprechen.
     */
    public void readFile(String fileName) throws IOException, java.text.ParseException {
        readFile(new File(fileName));
    }

    /**
     * Liest die Datei ein und gibt die Daten Zeilenweise an #onReadLine weiter.
     * 
     * @param file Description of Parameter
     * @exception IOException Wird geworfen, wenn die Datei nicht gefunden
     *                wird...
     * @exception java.text.ParseException Wird geworfen, wenn die Daten Fehler
     *                auf- weisen, d.h. nicht den Konventionen des CSV-Formates
     *                entsprechen.
     */
    public void readFile(File file) throws IOException, java.text.ParseException {
        in = new FileReader(file);
        convert();
        in.close();
    }

    /**
     * Parst den String und gibt die Daten Zeilenweise an #onReadLine weiter.
     * 
     * @param data Die Daten
     * @exception java.text.ParseException Wird geworfen, wenn die Daten Fehler
     *                auf- weisen, d.h. nicht den Konventionen des CSV-Formates
     *                entsprechen.
     * @exception IOException
     */
    public void readData(String data) throws IOException, java.text.ParseException {
        in = new StringReader(data);
        convert();
        in.close();
    }

    /**
     * Parst den Stream und gibt die Daten Zeilenweise an #onReadLine weiter.
     * 
     * @param data Die Daten
     * @exception java.text.ParseException Wird geworfen, wenn die Daten Fehler
     *                auf- weisen, d.h. nicht den Konventionen des CSV-Formates
     *                entsprechen.
     * @exception IOException
     */
    public void readData(InputStream data) throws IOException, java.text.ParseException {
        in = new InputStreamReader(data);
        convert();
        in.close();
    }

    /**
     * Gets the Delimiter attribute of the CsvFileReader object
     * 
     * @return The Delimiter value
     */
    public char getDelimiter() {
        return delimiter;
    }

    /**
     * Sets the Delimiter attribute of the CsvFileReader object Formattreue
     * Werte: ; und ,
     * 
     * @param c The new Delimiter value
     */
    public void setDelimiter(char c) {
        delimiter = c;
    }

    /**
     * Sets the Delimiter attribute of the CsvFileReader object Formattreue
     * Werte: ; und , Gets the Quote attribute of the CsvFileReader object
     * 
     * @return The Quote value
     * @return The delimiter value
     */
    public char getQuote() {
        return quote;
    }

    /**
     * Sets the Quote attribute of the CsvFileReader object Formattreuer Wert: "
     * 
     * @param c The new Quote value
     */
    public void setQuote(char c) {
        quote = c;
    }

    /**
     * Diese Methode parst die Datei oder den Eingabe-Stream gem�� den
     * Konventionen des CSV-Formates. Ist eine Zeile fertig erkannt, wird sie an
     * die Methode #onReadLine weitergegeben, wo sie weiterverarbeitet werden
     * kann.
     * 
     * @exception IOException
     * @exception java.text.ParseException @ exception java.text.ParseException
     *                Wird geworfen, wenn die Daten Fehler aufweisen, d.h. nicht
     *                den Konventionen des CSV-Formates entsprechen.
     */
    protected void convert() throws IOException, java.text.ParseException {
        int mode = 0;
        int line = 1;
        int input = -1;
        List<String> v = new ArrayList<String>(20);
        StringBuffer t = new StringBuffer();
        while ((input = in.read()) != -1) {
            char c = (char) input;
            switch(mode) {
                case 0:
                    if (c == '\r' || c == '\n') {
                        if (v.size() != 0) {
                            v.add(t.toString());
                            t = new StringBuffer();
                            onReadLine(line++, convertObjToStr(v.toArray()));
                            v = new ArrayList<String>(20);
                        }
                    } else if (c == delimiter) {
                        v.add("");
                    } else if (c == quote) {
                        mode = 1;
                    } else {
                        t.append(c);
                        mode = 3;
                    }
                    break;
                case 1:
                    if (c == quote) {
                        mode = 2;
                    } else {
                        t.append(c);
                    }
                    break;
                case 2:
                    if (c == quote) {
                        t.append(c);
                        mode = 1;
                    } else if (c == delimiter) {
                        v.add(t.toString());
                        t = new StringBuffer();
                        mode = 0;
                    } else if (c == '\r' || c == '\n') {
                        v.add(t.toString());
                        t = new StringBuffer();
                        mode = 0;
                        onReadLine(line++, convertObjToStr(v.toArray()));
                        v = new ArrayList<String>(20);
                    } else {
                        throw new java.text.ParseException("Illegal format of data!", line);
                    }
                    break;
                case 3:
                    if (c == delimiter) {
                        v.add(t.toString());
                        t = new StringBuffer();
                        mode = 0;
                    } else if (c == '\n') {
                        v.add(t.toString());
                        t = new StringBuffer();
                        mode = 0;
                        onReadLine(line++, convertObjToStr(v.toArray()));
                        v = new ArrayList<String>(20);
                    } else {
                        t.append(c);
                    }
                    break;
            }
        }
        v.add(t.toString());
        onReadLine(line, convertObjToStr(v.toArray()));
    }

    /**
     * Konvertiert die Spalten in ein String-Array.
     */
    private String[] convertObjToStr(Object[] colums) {
        String[] converted = new String[colums.length];
        for (int i = 0; i < colums.length; i++) {
            converted[i] = (String) colums[i];
        }
        return converted;
    }

    /**
     * Diese Methode wird von den erbenden Klassen �berschrieben, um die Daten,
     * die aud der CSV-Datei gelesen wurden, zu verarbeiten. Jeweils nachdem
     * eine Zeile gelesen wurde, wird diese Methode aufgerufen und bekommt die
     * Zeile und die Daten zur weiteren Verarbeitung geliefert.
     * 
     * @param line Die aktuelle Daten-Zeile
     * @param columns Die Daten der aktuellen Zeile befinden sich in diesem
     *            Iterator.
     * @param eof Zeigt an, dass die Datei mit dieser Zeile endet.
     */
    protected abstract void onReadLine(int line, String[] columns);

    private Reader in;

    private char delimiter = ';';

    private char quote = '"';
}
