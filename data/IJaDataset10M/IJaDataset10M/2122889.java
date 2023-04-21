package org.lacson.utils;

import java.net.*;
import java.io.*;
import java.util.*;
import org.lacson.*;

/**
 * GospelComReader class.  This class is used to read verses
 * from gospelcom.net
 *
 * (this may need to be modified to accomodated other versions)
 * @author Patrick Lacson
 */
public class GospelComReader implements Serializable {

    /**
     * Constructor
     */
    public GospelComReader() {
        version = "NASB";
        knownVerses = new Hashtable();
    }

    public void setVersion(String v) {
        version = v;
    }

    public void setTimeout(long t) {
        this.timeout = t;
    }

    /**
     * Gets the actual passage as the book, chapter, 
     * and verse are passed
     *
     * @param book - String type stating the book as 
     * defined in org.lacson.BibleAddress.GOSPELCOM_BOOKS[book_id]
     * @return String of actual verse or passage
     */
    public String getPassage(String book, int chapter, int fromVerse, int toVerse) {
        if (chapter == -1) return "Enter a chapter!";
        String answerVerse = "<unknown>";
        this.book = book;
        this.chapter = chapter;
        this.fromVerse = fromVerse;
        this.toVerse = toVerse;
        String passage = "";
        passage = book + " " + chapter + ":" + fromVerse + "," + version + "," + showVerseNumber;
        boolean gotWholePassage = true;
        if (knownVerses.containsKey(passage)) {
            if (toVerse != -1 && toVerse > fromVerse) {
                for (int i = fromVerse; i <= toVerse; i++) {
                    passage = book + " " + chapter + ":" + i + "," + version + "," + showVerseNumber;
                    if (!knownVerses.containsKey(passage)) {
                        gotWholePassage = false;
                        break;
                    }
                }
                if (gotWholePassage) {
                    debug("GOT WHOLE PASSAGE!");
                    System.out.println("** Getting passage from cache **");
                    StringBuffer passageBuffer = new StringBuffer("");
                    String passageKey = "";
                    for (int j = fromVerse; j <= toVerse; j++) {
                        passageKey = book + " " + chapter + ":" + j + "," + version + "," + showVerseNumber;
                        if (showVerseNumber) {
                            if (System.getProperty("os.name").startsWith("Win")) passageBuffer.append(knownVerses.get(passageKey)); else passageBuffer.append(knownVerses.get(passageKey) + "\r\n");
                        } else {
                            passageBuffer.append(knownVerses.get(passageKey));
                        }
                    }
                    return passageBuffer.toString();
                }
            } else {
                System.out.println("** Getting verse from cache **");
                return (String) knownVerses.get(passage);
            }
        }
        try {
            String builtUrl = "", rawUrl = "", parsedUrl = "";
            builtUrl = buildURL();
            OpenGospel og = new OpenGospel(new URL(builtUrl));
            long start = System.currentTimeMillis();
            long curr = System.currentTimeMillis();
            og.start();
            while (!og.isConnected()) {
                curr = System.currentTimeMillis();
                if ((curr - start) >= timeout) return "TIME OUT EXCEEDED try again later ..";
            }
            StringBuffer sb = new StringBuffer("");
            BufferedReader bin = og.getBufferedReader();
            int ch;
            while ((ch = bin.read()) != -1) {
                sb.append((char) ch);
            }
            rawUrl = sb.toString();
            if ((parsedUrl = parseURL(rawUrl)).startsWith("Unknown")) {
                return parsedUrl;
            }
            answerVerse = stripGarbage(parsedUrl);
            while ((answerVerse = stripSectionOut("<B>", "</B>", answerVerse)).indexOf("<B>") != -1) ;
            while ((answerVerse = stripSectionOut("<I>", "</I>", answerVerse)).indexOf("<I>") != -1) ;
            if (rawUrl.indexOf("<DD>") == -1 && !version.equals("NKJV")) answerVerse = "Unknown Chapter or Verse!"; else if (version.equals("NKJV") && answerVerse.trim().length() == 0) answerVerse = "Unknown Chapter or Verse!";
        } catch (IOException io) {
            io.printStackTrace();
        }
        if (answerVerse.toLowerCase().indexOf("unknown") == -1) {
            String passKey = "";
            String verseSection = "";
            if (toVerse != -1 && toVerse > fromVerse) {
                for (int k = fromVerse; k <= toVerse; k++) {
                    passKey = book + " " + chapter + ":" + k + "," + version + "," + showVerseNumber;
                    if (k == fromVerse) verseSection = answerVerse.substring(answerVerse.indexOf("" + (k)), answerVerse.indexOf("" + (k + 1))); else {
                        try {
                            if (k < toVerse) verseSection = answerVerse.substring(answerVerse.indexOf("" + (k)), answerVerse.indexOf("" + (k + 1))); else if (k == toVerse) verseSection = answerVerse.substring(answerVerse.indexOf("" + (k)), answerVerse.length()); else verseSection = answerVerse.substring(answerVerse.indexOf("" + (k)), answerVerse.indexOf("" + (k)));
                        } catch (StringIndexOutOfBoundsException se) {
                            debug("To verse: " + toVerse + " does not exist!");
                            return ("To verse: " + toVerse + " does not exist!");
                        }
                    }
                    debug("storing key: -" + passKey + "-");
                    knownVerses.put(passKey, verseSection);
                }
            } else if (toVerse == -1 && fromVerse == -1) {
                int k = 1;
                try {
                    for (k = 1; ; k++) {
                        passKey = book + " " + chapter + ":" + k + "," + version + "," + showVerseNumber;
                        if (k >= 1) verseSection = answerVerse.substring(answerVerse.indexOf("" + (k)), answerVerse.indexOf("" + (k + 1))); else verseSection = answerVerse.substring(answerVerse.indexOf("" + (k)), answerVerse.indexOf("" + (k)));
                        debug("storing key: -" + passKey + "-");
                        knownVerses.put(passKey, verseSection);
                    }
                } catch (StringIndexOutOfBoundsException s) {
                    verseSection = answerVerse.substring(answerVerse.indexOf("" + k), answerVerse.length());
                    knownVerses.put(passKey, verseSection);
                    debug("found end of rope!");
                    return answerVerse.trim();
                }
            } else {
                debug("storing key: -" + passage + "-");
                knownVerses.put(passage, answerVerse);
            }
        }
        return answerVerse.trim();
    }

    /**
     * Store this class into a serialized object
     * default file name is bibleCache.ser
     */
    public void storeBibleCache() throws IOException {
        FileOutputStream out = new FileOutputStream(BC_NAME);
        ObjectOutputStream s = new ObjectOutputStream(out);
        s.writeObject(BC_NAME);
        s.writeObject(this);
        s.flush();
    }

    /**
     * Read from the locally stored serialized object
     */
    public static GospelComReader getBibleCache() throws IOException {
        FileInputStream in = new FileInputStream(BC_NAME);
        ObjectInputStream s = new ObjectInputStream(in);
        try {
            new String((String) s.readObject());
            return (GospelComReader) s.readObject();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return null;
    }

    /**
     * Inner OpenGospel Class
     * Description: A separate thread is used for
     * 		  opening a connection to the web server
     *		  This is also useful for timeout handling
     */
    class OpenGospel extends Thread {

        private URLConnection uconn = null;

        private URL url;

        private BufferedReader bin;

        private boolean isConnected = false;

        public OpenGospel(URL url) {
            this.url = url;
        }

        public void run() {
            try {
                uconn = url.openConnection();
                open();
            } catch (MalformedURLException mfe) {
                System.out.println(".. malformed url exception in run() ..");
            } catch (UnknownHostException uhe) {
                System.out.println(" .. unknown host in run() ..");
            } catch (IOException io) {
                System.out.println(" .. ioexception in run() ..");
            }
        }

        public boolean isConnected() {
            return (bin != null);
        }

        public BufferedReader getBufferedReader() {
            return bin;
        }

        private void open() throws IOException, MalformedURLException, UnknownHostException {
            bin = new BufferedReader(new InputStreamReader(uconn.getInputStream()));
        }
    }

    /**
     * Recursive method for parsing middle
     * sections of HTML junk
     */
    public static String stripOut(String token, String s) {
        if (s.indexOf(token) != -1) {
            String front = s.substring(0, s.indexOf(token));
            String back = s.substring(s.indexOf(token) + token.length());
            return stripOut(token, front + back);
        } else {
            return s;
        }
    }

    public static String stripSectionOut(String start, String end, String s) {
        String retVal = "";
        if (s.indexOf(start) == -1) {
            return s;
        } else {
            retVal = s.substring(0, s.indexOf(start));
            retVal += s.substring(s.indexOf(end) + end.length(), s.length());
            return retVal;
        }
    }

    /**
     * Builds the URL to connect to the web server
     */
    private String buildURL() {
        StringBuffer retURL = new StringBuffer("http://bible.gospelcom.net/bible?passage=");
        retURL.append(book);
        retURL.append("+");
        retURL.append(chapter);
        if (fromVerse != -1) retURL.append(":" + fromVerse);
        if (toVerse != -1 && toVerse >= fromVerse) retURL.append("-" + toVerse);
        retURL.append("&language=english&version=");
        retURL.append(version);
        retURL.append("&showfn=off&showxref=off");
        debug("buildURL value: " + retURL.toString());
        return retURL.toString();
    }

    /**
     * Gets the raw HTML that is used for the
     * verse collection
     */
    private String parseURL(String html) {
        debug("parseURL() invoked with param value: " + html);
        int start = -1;
        int end = -1;
        if (version.equals("NKJV")) {
            start = html.indexOf("<DL COMPACT>") + 12;
            end = html.indexOf("<A HREF=", start);
        } else {
            if (html.indexOf("<DL COMPACT> <DT>") != -1) start = html.indexOf("<DL COMPACT> <DT>") + 13; else start = html.indexOf("<DL COMPACT><DT>") + 12;
            end = html.indexOf("</DL>", start);
        }
        String returnURL = "";
        try {
            returnURL = html.substring(start, end);
        } catch (Exception e) {
            return "Unknown Chapter or Verse!";
        }
        boolean openSUP = false;
        boolean closeSUP = false;
        boolean openD = false;
        boolean closeD = false;
        int rawLength = returnURL.length();
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i <= rawLength; i++) {
            if (i < rawLength) {
                if (returnURL.charAt(i) == '<' && returnURL.charAt(i + 1) == 's') openSUP = true;
                if (returnURL.charAt(i) == '<' && returnURL.charAt(i + 1) == '/' && returnURL.charAt(i) == 's') closeSUP = true;
                if (!openSUP && !closeSUP) {
                    sb.append(returnURL.charAt(i));
                }
                if (openSUP && closeSUP) {
                    openSUP = false;
                    closeSUP = false;
                }
            }
        }
        return sb.toString();
    }

    private String stripGarbage(String s) {
        StringBuffer sb = new StringBuffer("");
        boolean openD = false;
        boolean closeD = false;
        int verse = (fromVerse == -1) ? 1 : fromVerse;
        for (int i = 0; i < s.length(); i++) {
            if (i <= s.length()) {
                if (s.charAt(i) == '<' && s.charAt(i + 1) == 'D' && s.charAt(i + 2) == 'T' && s.charAt(i + 3) == '>') {
                    openD = true;
                    if (showVerseNumber && !version.equals("NKJV")) sb.append("\n" + verse++ + ") ");
                }
                if (s.charAt(i) == '<' && s.charAt(i + 1) == 'D' && s.charAt(i + 2) == 'D' && s.charAt(i + 3) == '>') {
                    closeD = true;
                    i += 3;
                }
                if (!openD && !closeD) sb.append(s.charAt(i));
                if (openD && closeD) {
                    openD = false;
                    closeD = false;
                }
            }
        }
        String thusFar = sb.toString();
        while (thusFar.indexOf("&quot;") != -1) {
            thusFar = thusFar.substring(0, thusFar.indexOf("&quot;")) + "'" + thusFar.substring(thusFar.indexOf("&quot;") + 6);
        }
        thusFar = thusFar.trim();
        try {
            Integer.parseInt(thusFar.substring(thusFar.length() - 1));
            thusFar = thusFar.substring(0, thusFar.length() - 1);
            debug("stripping out last number ..");
        } catch (NumberFormatException nfe) {
            ;
        }
        if (version.equals("NKJV")) {
            String retVal = "";
            do {
                retVal = stripOut("<BR>", thusFar);
            } while (retVal.indexOf("<BR>") != -1);
            do {
                retVal = stripOut("<SUP>", retVal);
            } while (retVal.indexOf("<SUP>") != -1);
            do {
                retVal = stripOut("</SUP>", retVal);
            } while (retVal.indexOf("</SUP>") != -1);
            do {
                retVal = stripOut("&nbsp;", retVal);
            } while (retVal.indexOf("&nbsp;") != -1);
            while ((retVal = stripSectionOut("<B>", "</B>", retVal)).indexOf("<B>") != -1) ;
            while ((retVal = stripSectionOut("<I>", "</I>", retVal)).indexOf("<I>") != -1) ;
            return retVal.trim();
        } else {
            return thusFar.trim();
        }
    }

    private void debug(String d) {
        if (setDebug) System.out.println(d);
    }

    private String rawURL;

    private String rawContent;

    private String book;

    private int chapter;

    private int fromVerse = -1;

    private int toVerse = -1;

    private String version;

    private Hashtable knownVerses;

    private Hashtable bible;

    private int counter = 0;

    private long timeout = 10 * 1000;

    private static String BC_NAME = "bibleCache.ser";

    public boolean showVerseNumber = true;

    public boolean setDebug = true;
}
