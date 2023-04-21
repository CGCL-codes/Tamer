package net.sf.dewdrop.util;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class short description goes here.
 * <p/>
 * Class long description goes here.
 * <p/>
 * [optional JavaDoc tags here]
 *
 * @author Les A. Hazlewood
 */
public class LanguageIsoCodeReader {

    public static final String ISO_639_2_HOST = "www.loc.gov";

    public static final String ISO_639_2_HTML_PATH = "/standards/iso639-2/langcodes.html";

    public static final int HTTP_PORT = 80;

    public List findLanguages() throws IOException {
        List langEntries = new ArrayList();
        boolean readColumns = false;
        int columnCounter = 0;
        LanguageIsoCodeReader.LanguageEntry current = null;
        String request = "GET " + ISO_639_2_HTML_PATH + "\n";
        Socket sock = new Socket(ISO_639_2_HOST, HTTP_PORT);
        InputStream is = sock.getInputStream();
        BufferedReader input = new BufferedReader(new InputStreamReader(is));
        DataOutputStream output = new DataOutputStream(sock.getOutputStream());
        output.writeBytes(request);
        String line = input.readLine();
        while (line != null) {
            line = line.trim();
            if (line.indexOf("<a name=\"ab\">") > -1) {
                readColumns = true;
                current = new LanguageIsoCodeReader.LanguageEntry();
                columnCounter = 0;
            }
            if (readColumns && line.startsWith("<td")) {
                columnCounter++;
                if (columnCounter == 4) {
                    if (!langEntriesContains(langEntries, current.getThreeLetterCode()) && !current.getThreeLetterCode().equals("qaa-qtz")) {
                        langEntries.add(current);
                    }
                    current = new LanguageIsoCodeReader.LanguageEntry();
                    columnCounter = 0;
                }
            }
            if (readColumns && line.startsWith("<td") && line.endsWith("</td>")) {
                int gtIndex = line.indexOf('>');
                int ltIndex = line.lastIndexOf("<");
                String value = line.substring(gtIndex + 1, ltIndex);
                if (value.startsWith("<div align=\"left\">")) {
                    value = value.substring(18);
                }
                if (value.endsWith("</div>")) {
                    int divIndex = value.lastIndexOf("</div>");
                    value = value.substring(0, divIndex);
                }
                if (value.endsWith("<a href=\"#two\">*</a>")) {
                    int hrefIndex = value.lastIndexOf("<a href=\"#two\">*</a>");
                    value = value.substring(0, hrefIndex);
                }
                if (value.indexOf("&ccedil;") > -1) {
                    value = value.replaceAll("\\&ccedil\\;", "ç");
                }
                if (value.indexOf("&aring;") > -1) {
                    value = value.replaceAll("\\&aring\\;", "Å");
                }
                if (value.indexOf("&#180;") > -1) {
                    value = value.replaceAll("\\&\\#180\\;", "''");
                }
                if (value.indexOf("&#231;") > -1) {
                    value = value.replaceAll("\\&\\#231\\;", "ç");
                }
                if (value.indexOf("&#252;") > -1) {
                    value = value.replaceAll("\\&\\#252\\;", "ü");
                }
                switch(columnCounter) {
                    case 1:
                        int slashIndex = value.indexOf('/');
                        if (slashIndex > -1) {
                            value = value.substring(slashIndex + 1);
                        }
                        current.setThreeLetterCode(value);
                        break;
                    case 2:
                        if (!value.equals("&nbsp;") && !value.equals("")) {
                            current.setTwoLetterCode(value);
                        }
                        break;
                    case 3:
                        current.setName(value);
                        break;
                }
            }
            line = input.readLine();
        }
        return langEntries;
    }

    /**
     * Some countries have two 3-letter codes.  The one following a slash '/' is the one we want to
     * enter, so we will discard an entry that already exists in the list.
     *
     * @return true if a country with <code>threeLetterCode</code> already exists in the langEntries
     *         list, false otherwise.
     */
    private boolean langEntriesContains(List langEntries, String threeLetterCode) {
        Iterator iter = langEntries.iterator();
        while (iter.hasNext()) {
            LanguageIsoCodeReader.LanguageEntry langEntry = (LanguageIsoCodeReader.LanguageEntry) iter.next();
            if (langEntry.getThreeLetterCode().equals(threeLetterCode)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        LanguageIsoCodeReader licr = new LanguageIsoCodeReader();
        List langEntries = licr.findLanguages();
        System.out.print("-- This list was generated by the DewDrop database");
        System.out.println(" consolidation engine.");
        System.out.print("-- All language codes and names are the ISO");
        System.out.println(" standards and are retrieved in real-time from: ");
        System.out.println("-- http://" + ISO_639_2_HOST + ISO_639_2_HTML_PATH);
        System.out.println();
        Iterator iter = langEntries.iterator();
        while (iter.hasNext()) {
            LanguageIsoCodeReader.LanguageEntry entry = (LanguageIsoCodeReader.LanguageEntry) iter.next();
            System.out.print("insert into languages (iso_three_letter_code, ");
            if (entry.getTwoLetterCode() != null) {
                System.out.print("iso_two_letter_code, ");
            }
            System.out.print("name) values ('");
            System.out.print(entry.getThreeLetterCode() + "', '");
            if (entry.getTwoLetterCode() != null) {
                System.out.print(entry.getTwoLetterCode() + "', '");
            }
            System.out.println(entry.getName() + "');");
        }
        System.exit(0);
    }

    private class LanguageEntry {

        private String twoLetterCode = null;

        private String threeLetterCode = null;

        private String name = null;

        public LanguageEntry() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getThreeLetterCode() {
            return threeLetterCode;
        }

        public void setThreeLetterCode(String threeLetterCode) {
            this.threeLetterCode = threeLetterCode;
        }

        public String getTwoLetterCode() {
            return twoLetterCode;
        }

        public void setTwoLetterCode(String twoLetterCode) {
            this.twoLetterCode = twoLetterCode;
        }
    }
}
