package grobid.engines;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Properties;
import java.io.*;
import grobid.exceptions.GROBIDServiceException;
import grobid.utilities.GrobidProperties;
import grobid.utilities.Utilities;
import grobid.utilities.TextUtilities;
import grobid.data.Date;
import grobid.features.FeaturesVectorDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.chasen.crfpp.Tagger;

/**
 *  @author Patrice Lopez
 */
public class DateParser {

    static {
        try {
            System.loadLibrary("CRFPP");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Cannot load the example native code.\n" + "Make sure your LD_LIBRARY_PATH or java.library.path contains the jni lib. \n" + e);
        }
    }

    private Tagger tagger = null;

    private String modelPath = null;

    private void getModelPath() {
        modelPath = System.getProperty(GrobidProperties.PROP_RESOURCE_PATH) + "/dataset/dates/crfpp-models";
    }

    public DateParser() {
        getModelPath();
        tagger = new Tagger("-m " + modelPath + "/model.crf ");
    }

    /**
     *  Processing of authors in header
     */
    public ArrayList<Date> processing(String input) {
        if (input == null) return null;
        ArrayList<Date> dates = null;
        ArrayList<String> dateBlocks = new ArrayList<String>();
        try {
            StringTokenizer st = new StringTokenizer(input, "([" + TextUtilities.punctuations, true);
            if (st.countTokens() == 0) return null;
            while (st.hasMoreTokens()) {
                String tok = st.nextToken();
                if (!tok.equals(" ")) {
                    dateBlocks.add(tok + " <date>");
                }
            }
            dateBlocks.add("\n");
            String headerDate = FeaturesVectorDate.addFeaturesDate(dateBlocks);
            tagger.clear();
            st = new StringTokenizer(headerDate, "\n");
            while (st.hasMoreTokens()) {
                tagger.add(st.nextToken());
                tagger.add("\n");
            }
            if (!tagger.parse()) {
                throw new Exception("CRF++ parsing failed.");
            }
            StringBuffer res = new StringBuffer();
            for (int i = 0; i < tagger.size(); i++) {
                for (int j = 0; j < tagger.xsize(); j++) {
                    res.append(tagger.x(i, j) + "\t");
                }
                res.append("<date>" + "\t");
                res.append(tagger.y2(i));
                res.append("\n");
            }
            StringTokenizer st2 = new StringTokenizer(res.toString(), "\n");
            String lastTag = null;
            grobid.data.Date date = new Date();
            int lineCount = 0;
            String currentMarker = null;
            while (st2.hasMoreTokens()) {
                String line = st2.nextToken();
                if ((line.trim().length() == 0)) {
                    if (date.notNull()) {
                        if (dates == null) dates = new ArrayList<Date>();
                        normalize(date);
                        dates.add(date);
                    }
                    date = new Date();
                    continue;
                }
                StringTokenizer st3 = new StringTokenizer(line, "\t");
                int ll = st3.countTokens();
                int i = 0;
                String s1 = null;
                String s2 = null;
                String s3 = null;
                while (st3.hasMoreTokens()) {
                    String s = st3.nextToken().trim();
                    if (i == 0) {
                        s2 = s;
                    } else if (i == ll - 2) {
                        s3 = s;
                    } else if (i == ll - 1) {
                        s1 = s;
                    }
                    i++;
                }
                if (s1.equals("<year>") || s1.equals("I-<year>")) {
                    if (s3.equals("<date>")) {
                        if (date.getYearString() != null) {
                            if ((s1.equals("I-<year>")) || (!s1.equals(lastTag) && !lastTag.equals("I-<year>"))) {
                                if (date.notNull()) {
                                    if (dates == null) dates = new ArrayList<Date>();
                                    normalize(date);
                                    dates.add(date);
                                }
                                date = new Date();
                                date.setYearString(s2);
                            } else {
                                if (date.getYearString().length() == 0) date.setYearString(s2); else if ((date.getYearString().charAt(date.getYearString().length() - 1) == '-') | (date.getYearString().charAt(date.getYearString().length() - 1) == '\'')) date.setYearString(date.getYearString() + s2); else date.setYearString(date.getYearString() + " " + s2);
                            }
                        } else {
                            date.setYearString(s2);
                        }
                    }
                } else if (s1.equals("<month>") || s1.equals("I-<month>")) {
                    if (s3.equals("<date>")) {
                        if (date.getMonthString() != null) {
                            if ((s1.equals("I-<month>")) || (!s1.equals(lastTag) && !lastTag.equals("I-<month>"))) {
                                if (date.notNull()) {
                                    if (dates == null) dates = new ArrayList<Date>();
                                    normalize(date);
                                    dates.add(date);
                                }
                                date = new Date();
                                date.setMonthString(s2);
                            } else {
                                if (date.getMonthString().length() == 0) date.setMonthString(s2); else if ((date.getMonthString().charAt(date.getMonthString().length() - 1) == '-') | (date.getMonthString().charAt(date.getMonthString().length() - 1) == '\'')) date.setMonthString(date.getMonthString() + s2); else date.setMonthString(date.getMonthString() + " " + s2);
                            }
                        } else {
                            date.setMonthString(s2);
                        }
                    }
                } else if (s1.equals("<day>") || s1.equals("I-<day>")) {
                    if (s3.equals("<date>")) {
                        if (date.getDayString() != null) {
                            if ((s1.equals("I-<day>")) || (!s1.equals(lastTag) && !lastTag.equals("I-<day>"))) {
                                if (date.notNull()) {
                                    if (dates == null) dates = new ArrayList<Date>();
                                    normalize(date);
                                    dates.add(date);
                                }
                                date = new Date();
                                date.setDayString(s2);
                            } else {
                                if (date.getDayString().length() == 0) date.setDayString(s2); else if ((date.getDayString().charAt(date.getDayString().length() - 1) == '-') | (date.getDayString().charAt(date.getDayString().length() - 1) == '\'')) date.setDayString(date.getDayString() + s2); else date.setDayString(date.getDayString() + " " + s2);
                            }
                        } else {
                            date.setDayString(s2);
                        }
                    }
                }
                lastTag = s1;
                lineCount++;
            }
            if (date.notNull()) {
                if (dates == null) dates = new ArrayList<Date>();
                normalize(date);
                dates.add(date);
            }
        } catch (Exception e) {
            throw new GROBIDServiceException("An exception occured while running Grobid.", e);
        }
        return dates;
    }

    public static Pattern jan = Pattern.compile("(((J|j)an)|((J|j)anuary)|((J|j)anvier)|((J|j)annewaori)|((J|j)anuar)|((E|e)nero)|((J|j)anuaro)|((J|j)anuari)|((J|j)aneiro)|((G|g)ennaio)|((G|g)en)|((O|o)cak)|((J|j)a)|(^1$)|(^01$)|(1月))");

    public static Pattern feb = Pattern.compile("((F|f)eb|February|(F|f)(e|é)vrier|(F|f)ebruar|(F|f)ebrewaori|(F|f)ebrero|(F|f)evereiro|(F|f)ebbraio|(L|l)uty|(S|s)tyczeń|Ş|ubat|(F|f)e|^2$|^02$|2月)");

    public static Pattern mar = Pattern.compile("((M|m)ar|(M|m)arch|(M|m)ars|(M|m)eert|(M|m)ärz|(M|m)arzo|(M|m)arço|(M|m)art|(M|m)a|(M|m)a|^3$|^03$|3月)");

    public static Pattern apr = Pattern.compile("((A|a)pr|(A|a)br|(A|a)vr|(A|a)pril|(A|a)vril|(A|a)pril|(A|a)prile|(A|a)bril|(N|n)isan|(A|a)p|^4$|^04$|4月)");

    public static Pattern may = Pattern.compile("((M|m)ay|(M|m)ai|(M|m)ay|(M|m)ayıs|(M|m)ei|(M|m)aio|(M|m)aggio|(M|m)eie|(M|m)a|^5$|^05$|5月)");

    public static Pattern jun = Pattern.compile("((J|j)un|(J|j)une|(J|j)uin|(J|j)uni|(J|j)unho|(G|g)iugno|(H|h)aziran|^6$|^06$|6月)");

    public static Pattern jul = Pattern.compile("((J|j)ul|(J|j)uly|(J|j)uillet|(J|j)uli|(T|t)emmuz|(L|l)uglio|(J|j)ulho|^7$|^07$|7月)");

    public static Pattern aug = Pattern.compile("((A|a)ug|(A|a)ugust|(A|a)o(u|û)t|(A|a)ugust|(A|a)gosto|(A|a)ugustus|(A|a)ğustos|^8$|^08$|8月)");

    public static Pattern sep = Pattern.compile("((S|s)ep|(S|s)ept|(S|s)eptember|(S|s)eptembre|(S|s)eptember|(S|s)ettembre|(S|s)etembro|(E|e)ylül|^9$|^09$|9月)");

    public static Pattern oct = Pattern.compile("((O|o)ct|(O|o)cto|(O|o)ctober|(O|o)ctobre|(E|e)kim|(O|o)ktober|(O|o)ttobre|(O|o)utubro|^10$|10月)");

    public static Pattern nov = Pattern.compile("((N|n)ov|(N|n)ovember|(N|n)ovembre|(K|k)asım|(N|n)oviembre|(D|d)icembre|(N|n)ovembro|^11$|11月)");

    public static Pattern dec = Pattern.compile("((D|d)ec|(D|d)ecember|(D|d)(e|é)cembre|(D|d)iciembre|(A|a)ralık|^12$|12月)");

    public static Pattern[] months = { jan, feb, mar, apr, may, jun, jul, aug, sep, oct, nov, dec };

    public void normalize(Date date) {
        if (date.getDayString() != null) {
            String dayStringBis = "";
            String dayString = date.getDayString().trim();
            for (int n = 0; n < dayString.length(); n++) {
                char c = dayString.charAt(n);
                if (Character.isDigit(c)) {
                    dayStringBis += c;
                }
            }
            try {
                int day = Integer.parseInt(dayStringBis);
                date.setDay(day);
            } catch (Exception e) {
            }
        }
        if (date.getMonthString() != null) {
            String month = date.getMonthString().trim();
            int n = 0;
            while (n < 12) {
                Matcher ma = months[n].matcher(month);
                if (ma.find()) {
                    date.setMonth(n + 1);
                    break;
                }
                n++;
            }
        }
        if (date.getYearString() != null) {
            String yearStringBis = "";
            String yearString = date.getYearString().trim();
            for (int n = 0; n < yearString.length(); n++) {
                char c = yearString.charAt(n);
                if (Character.isDigit(c)) {
                    yearStringBis += c;
                }
            }
            try {
                int year = Integer.parseInt(yearStringBis);
                if ((year >= 20) && (year < 100)) {
                    year = year + 1900;
                } else if ((year >= 0) && (year < 20)) {
                    year = year + 2000;
                }
                date.setYear(year);
            } catch (Exception e) {
            }
        }
    }

    /**
	 *  Extract results from a date string in the training format without any string modification. 
	 */
    public StringBuffer trainingExtraction(ArrayList<String> inputs) {
        StringBuffer buffer = new StringBuffer();
        try {
            if (inputs == null) return null;
            if (inputs.size() == 0) return null;
            ArrayList<String> tokenizations = new ArrayList<String>();
            ArrayList<String> dateBlocks = new ArrayList<String>();
            for (String input : inputs) {
                if (input == null) continue;
                StringTokenizer st = new StringTokenizer(input, "([" + TextUtilities.punctuations, true);
                if (st.countTokens() == 0) return null;
                while (st.hasMoreTokens()) {
                    String tok = st.nextToken();
                    if (tok.equals("\n")) {
                        dateBlocks.add("@newline");
                    } else if (!tok.equals(" ")) {
                        dateBlocks.add(tok + " <date>");
                    }
                    tokenizations.add(tok);
                }
                dateBlocks.add("\n");
            }
            String headerDate = FeaturesVectorDate.addFeaturesDate(dateBlocks);
            tagger.clear();
            StringTokenizer st = new StringTokenizer(headerDate, "\n");
            while (st.hasMoreTokens()) {
                tagger.add(st.nextToken());
                tagger.add("\n");
            }
            if (!tagger.parse()) {
                throw new Exception("CRF++ parsing failed.");
            }
            StringBuffer res = new StringBuffer();
            for (int i = 0; i < tagger.size(); i++) {
                for (int j = 0; j < tagger.xsize(); j++) {
                    res.append(tagger.x(i, j) + "\t");
                }
                res.append("<date>" + "\t");
                res.append(tagger.y2(i));
                res.append("\n");
            }
            StringTokenizer st2 = new StringTokenizer(res.toString(), "\n");
            String lastTag = null;
            boolean tagClosed = false;
            int q = 0;
            boolean addSpace = false;
            boolean hasYear = false;
            boolean hasMonth = false;
            boolean hasDay = false;
            String lastTag0 = null;
            String currentTag0 = null;
            boolean start = true;
            while (st2.hasMoreTokens()) {
                String line = st2.nextToken();
                addSpace = false;
                if ((line.trim().length() == 0)) {
                    buffer.append("</date>\n");
                    hasYear = false;
                    hasMonth = false;
                    hasDay = false;
                    buffer.append("\t<date>");
                    continue;
                } else {
                    String theTok = tokenizations.get(q);
                    while (theTok.equals(" ")) {
                        addSpace = true;
                        q++;
                        theTok = tokenizations.get(q);
                    }
                    q++;
                }
                StringTokenizer st3 = new StringTokenizer(line, "\t");
                int ll = st3.countTokens();
                int i = 0;
                String s1 = null;
                String s2 = null;
                String s3 = null;
                while (st3.hasMoreTokens()) {
                    String s = st3.nextToken().trim();
                    if (i == 0) {
                        s2 = TextUtilities.HTMLEncode(s);
                    } else if (i == ll - 2) {
                        s3 = s;
                    } else if (i == ll - 1) {
                        s1 = s;
                    }
                    i++;
                }
                if (start && (s1 != null)) {
                    buffer.append("\t<date>");
                    start = false;
                }
                lastTag0 = null;
                if (lastTag != null) {
                    if (lastTag.startsWith("I-")) {
                        lastTag0 = lastTag.substring(2, lastTag.length());
                    } else {
                        lastTag0 = lastTag;
                    }
                }
                currentTag0 = null;
                if (s1 != null) {
                    if (s1.startsWith("I-")) {
                        currentTag0 = s1.substring(2, s1.length());
                    } else {
                        currentTag0 = s1;
                    }
                }
                if (lastTag0 != null) {
                    tagClosed = testClosingTag(buffer, currentTag0, lastTag0);
                } else tagClosed = false;
                String output = writeField(s1, lastTag0, s2, "<day>", "<day>", addSpace, 0);
                if (output != null) {
                    if (lastTag0 != null) {
                        if (hasDay && !lastTag0.equals("<day>")) {
                            buffer.append("</date>\n");
                            hasYear = false;
                            hasMonth = false;
                            buffer.append("\t<date>");
                        }
                    }
                    hasDay = true;
                    buffer.append(output);
                    lastTag = s1;
                    continue;
                } else {
                    output = writeField(s1, lastTag0, s2, "<other>", "<other>", addSpace, 0);
                }
                if (output == null) {
                    output = writeField(s1, lastTag0, s2, "<month>", "<month>", addSpace, 0);
                } else {
                    buffer.append(output);
                    lastTag = s1;
                    continue;
                }
                if (output == null) {
                    output = writeField(s1, lastTag0, s2, "<year>", "<year>", addSpace, 0);
                } else {
                    if (lastTag0 != null) {
                        if (hasMonth && !lastTag0.equals("<month>")) {
                            buffer.append("</date>\n");
                            hasYear = false;
                            hasDay = false;
                            buffer.append("\t<date>");
                        }
                    }
                    buffer.append(output);
                    hasMonth = true;
                    lastTag = s1;
                    continue;
                }
                if (output != null) {
                    if (lastTag0 != null) {
                        if (hasYear && !lastTag0.equals("<year>")) {
                            buffer.append("</date>\n");
                            hasDay = false;
                            hasMonth = false;
                            buffer.append("\t<date>");
                        }
                    }
                    buffer.append(output);
                    hasYear = true;
                    lastTag = s1;
                    continue;
                }
                lastTag = s1;
            }
            if (lastTag != null) {
                if (lastTag.startsWith("I-")) {
                    lastTag0 = lastTag.substring(2, lastTag.length());
                } else {
                    lastTag0 = lastTag;
                }
                currentTag0 = "";
                testClosingTag(buffer, currentTag0, lastTag0);
                buffer.append("</date>\n");
            }
        } catch (Exception e) {
            throw new GROBIDServiceException("An exception occured while running Grobid.", e);
        }
        return buffer;
    }

    private String writeField(String s1, String lastTag0, String s2, String field, String outField, boolean addSpace, int nbIndent) {
        String result = null;
        if ((s1.equals(field)) || (s1.equals("I-" + field))) {
            if ((s1.equals("<other>") || s1.equals("I-<other>"))) {
                if (addSpace) result = " " + s2; else result = s2;
            } else if (s1.equals(lastTag0) || s1.equals("I-" + lastTag0)) {
                if (addSpace) result = " " + s2; else result = s2;
            } else {
                result = "";
                for (int i = 0; i < nbIndent; i++) {
                    result += "\t";
                }
                if (addSpace) result += " " + outField + s2; else result += outField + s2;
            }
        }
        return result;
    }

    private boolean testClosingTag(StringBuffer buffer, String currentTag0, String lastTag0) {
        boolean res = false;
        if (!currentTag0.equals(lastTag0)) {
            res = true;
            if (lastTag0.equals("<other>")) {
                buffer.append("");
            } else if (lastTag0.equals("<day>")) {
                buffer.append("</day>");
            } else if (lastTag0.equals("<month>")) {
                buffer.append("</month>");
            } else if (lastTag0.equals("<year>")) {
                buffer.append("</year>");
            } else {
                res = false;
            }
        }
        return res;
    }
}
