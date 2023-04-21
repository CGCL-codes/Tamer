package org.jpedal.examples.text.extractheadlines;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.jpedal.examples.text.ExtractTextInRectangle;
import org.jpedal.exception.PdfException;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.objects.PdfData;
import org.jpedal.utils.Strip;

/**
 * This example was written to show extraction from a page location of
 * repetitive information (ie Section)<br>Scope:<b>(1.4/1.5 Ent only)</b>
 * 
 */
public class ExtractDate extends ExtractTextInRectangle {

    /**debug flag to exit if no section found*/
    private final boolean debug = false;

    /**holds configuration data*/
    DateConfiguration configDate;

    /**default value*/
    String[] dateTokens = null;

    private int[] date_x1, date_x2, date_y1, date_y2;

    private String day, date, month, year;

    /**holds configuration data*/
    SectionConfiguration sectionConfig;

    /**default value*/
    String[] sectionTokens = null;

    boolean isDate = false;

    private int[] section_x1, section_x2, section_y1, section_y2;

    private String section = null;

    private String folio = null;

    public String getFolio() {
        return folio;
    }

    public String getSection() {
        return section;
    }

    public String getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    private void initSection(String configDir) {
        sectionConfig = new SectionConfiguration(configDir);
        section = sectionConfig.getValue("default_section");
        int tagCount = Integer.parseInt(sectionConfig.getValue("xmlCount"));
        sectionTokens = new String[tagCount];
        for (int j = 0; j < tagCount; j++) {
            sectionTokens[j] = sectionConfig.getValue("xmlTag_" + j);
            if (showMessages) System.out.println(sectionTokens[j]);
        }
        tagCount = Integer.parseInt(sectionConfig.getValue("locationCount"));
        section_x1 = new int[tagCount];
        section_x2 = new int[tagCount];
        section_y1 = new int[tagCount];
        section_y2 = new int[tagCount];
        String key = "locTag";
        String[] coords = { "x1", "y1", "x2", "y2" };
        for (int i = 0; i < tagCount; i++) {
            for (int coord = 0; coord < 4; coord++) {
                String currentKey = key + "_" + i + "_" + coords[coord];
                String value = sectionConfig.getValue(currentKey);
                int numberValue = Integer.parseInt(value);
                switch(coord) {
                    case 0:
                        section_x1[i] = numberValue;
                        break;
                    case 1:
                        section_y1[i] = numberValue;
                        break;
                    case 2:
                        section_x2[i] = numberValue;
                        break;
                    case 3:
                        section_y2[i] = numberValue;
                        break;
                }
            }
        }
    }

    /**
     * extract section using tags
     */
    private String extractSection(String extractedText) {
        String pageNumber, section = null, currentToken;
        try {
            if (showMessages) System.out.println(extractedText);
            if (extractedText == null) return null;
            Map sections = new HashMap();
            int sectionTokenCount = sectionTokens.length;
            for (int i = 0; i < sectionTokenCount; i++) sections.put(sectionTokens[i], "x");
            pageNumber = null;
            section = null;
            currentToken = null;
            StringTokenizer tokens = new StringTokenizer(extractedText, "<>");
            while (tokens.hasMoreTokens()) {
                if ((section != null) && (pageNumber != null)) break;
                currentToken = tokens.nextToken();
                if ((sections.get(currentToken) != null)) {
                    String font = currentToken;
                    currentToken = tokens.nextToken();
                    boolean isNumber = false;
                    if ((!isNumber) && (currentToken.length() > 2)) {
                        StringBuffer sectionName = new StringBuffer();
                        while (tokens.hasMoreTokens() && (!currentToken.equals("/font"))) {
                            if (currentToken.indexOf("SpaceC") != -1) sectionName.append(' '); else sectionName.append(currentToken);
                            currentToken = tokens.nextToken();
                        }
                        section = sectionName.toString().trim();
                        if (font.equals("font face=\"TimesClassicDisplay\" style=\"font-size:16pt\"")) break;
                    }
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return section;
    }

    /**
	 * extract date using tags
	 */
    private String extractDate(String extractedText) {
        String pageNumber, date = "", currentToken;
        try {
            if (showMessages) System.out.println(extractedText);
            if (extractedText == null) return null;
            Map dates = new HashMap();
            int dateTokenCount = dateTokens.length;
            for (int i = 0; i < dateTokenCount; i++) dates.put(dateTokens[i], "x");
            pageNumber = null;
            currentToken = null;
            StringTokenizer tokens = new StringTokenizer(extractedText, "<>");
            while (tokens.hasMoreTokens()) {
                if ((date != null) && (pageNumber != null)) break;
                currentToken = tokens.nextToken();
                if ((dates.get(currentToken) != null)) {
                    String font = currentToken;
                    currentToken = tokens.nextToken();
                    boolean isNumber = false;
                    if ((!isNumber) && (currentToken.length() > 2)) {
                        StringBuffer dateName = new StringBuffer();
                        while (tokens.hasMoreTokens() && (!currentToken.equals("/font"))) {
                            if (currentToken.indexOf("SpaceC") != -1) dateName.append(' '); else dateName.append(currentToken);
                            currentToken = tokens.nextToken();
                        }
                        date = date + " " + dateName.toString().trim();
                    }
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**example method to open a file and extract the raw text*/
    public ExtractDate(String file_name, String configDir, PdfData pdf_data) {
        boolean hasDay = (file_name.indexOf("TIM") != -1);
        try {
            showMessages = false;
            initDate(configDir);
            initSection(configDir);
            extractValues(pdf_data, 1, hasDay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDate(String configDir) {
        configDate = new DateConfiguration(configDir);
        date = configDate.getValue("default_date");
        int tagCount = Integer.parseInt(configDate.getValue("xmlCount"));
        dateTokens = new String[tagCount];
        for (int j = 0; j < tagCount; j++) {
            dateTokens[j] = configDate.getValue("xmlTag_" + j);
            if (showMessages) System.out.println(dateTokens[j]);
        }
        tagCount = Integer.parseInt(configDate.getValue("locationCount"));
        date_x1 = new int[tagCount];
        date_x2 = new int[tagCount];
        date_y1 = new int[tagCount];
        date_y2 = new int[tagCount];
        String key = "locTag";
        String[] coords = { "x1", "y1", "x2", "y2" };
        for (int i = 0; i < tagCount; i++) {
            for (int coord = 0; coord < 4; coord++) {
                String currentKey = key + "_" + i + "_" + coords[coord];
                String value = configDate.getValue(currentKey);
                int numberValue = Integer.parseInt(value);
                switch(coord) {
                    case 0:
                        date_x1[i] = numberValue;
                        break;
                    case 1:
                        date_y1[i] = numberValue;
                        break;
                    case 2:
                        date_x2[i] = numberValue;
                        break;
                    case 3:
                        date_y2[i] = numberValue;
                        break;
                }
            }
        }
    }

    private void extractValues(PdfData pdfdata, int page, boolean hasDay) {
        PdfGroupingAlgorithms currentGrouping = new PdfGroupingAlgorithms(pdfdata);
        date = null;
        int x1, x2, y1, y2;
        int possSetsCoordinates = date_x2.length;
        for (int coordSet = 0; coordSet < possSetsCoordinates; coordSet++) {
            x1 = date_x1[coordSet];
            x2 = date_x2[coordSet];
            y1 = date_y1[coordSet];
            y2 = date_y2[coordSet];
            try {
                text = currentGrouping.extractTextInRectangle(x1, y1, x2, y2, page, false, true);
                if (showMessages) System.out.println("Using (" + x1 + "," + y1 + ") (" + x2 + "," + y2 + ") text=" + text);
                if (text != null) {
                    String rawDate = extractDate(text);
                    if (showMessages) System.out.println("Date=" + rawDate + "<");
                    if (rawDate != null && !rawDate.equals(configDate.getValue("default_date"))) {
                        StringTokenizer st = new StringTokenizer(rawDate, ", ");
                        int count = st.countTokens();
                        if (count >= 3 || (hasDay && count >= 4)) {
                            if (hasDay) this.day = st.nextToken();
                            this.month = st.nextToken();
                            this.date = st.nextToken();
                            this.year = st.nextToken();
                            if (!isString(month) || !isNumber(this.date) || !isNumber(year)) {
                                month = null;
                                rawDate = null;
                                date = null;
                                year = null;
                            }
                        }
                    }
                    if (month != null && rawDate != null && year != null) coordSet = possSetsCoordinates;
                }
            } catch (Exception ee) {
                System.out.println(ee);
                month = null;
                date = null;
                year = null;
            }
        }
        int possSetSectionCoordinates = section_x2.length;
        String section = null;
        for (int coordSet = 0; coordSet < possSetSectionCoordinates; coordSet++) {
            x1 = section_x1[coordSet];
            x2 = section_x2[coordSet];
            y1 = section_y1[coordSet];
            y2 = section_y2[coordSet];
            if (showMessages) System.out.println("Using (" + x1 + "," + y1 + ") (" + x2 + "," + y2 + ")");
            try {
                text = currentGrouping.extractTextInRectangle(x1, y1, x2, y2, page, false, true);
                if (text != null) {
                    this.folio = Strip.stripXML(text).toString();
                    section = extractSection(text);
                    if (section != null) {
                        coordSet = possSetSectionCoordinates;
                        if (showMessages) System.out.println("section=" + section);
                        this.section = section;
                    }
                }
            } catch (PdfException e) {
                text = null;
                System.err.println("Exception " + e.getMessage() + " in file " + decodePdf.getObjectStore().fullFileName);
                e.printStackTrace();
            }
        }
        if (section == null && debug) {
            System.out.println("section=" + section);
            System.exit(1);
        }
    }

    private boolean isString(String testValue) {
        boolean isString = true;
        StringBuffer chars = new StringBuffer(testValue);
        int count = chars.length();
        for (int ii = 0; ii < count; ii++) {
            char c = chars.charAt(ii);
            if (!Character.isLetter(c)) {
                isString = false;
                ii = count;
            }
        }
        return isString;
    }

    private boolean isNumber(String testValue) {
        boolean isNumber = true;
        StringBuffer chars = new StringBuffer(testValue);
        int count = chars.length();
        for (int ii = 0; ii < count; ii++) {
            char c = chars.charAt(ii);
            if (!Character.isDigit(c)) {
                isNumber = false;
                ii = count;
            }
        }
        return isNumber;
    }
}
