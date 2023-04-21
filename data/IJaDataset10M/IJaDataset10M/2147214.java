package edu.stanford.genetics.treeview.model;

import java.io.*;
import java.util.Vector;
import edu.stanford.genetics.treeview.*;

/**
* parses a tab-delimitted file into a vector of String []. Each String [] represents a row of the file.
*
* This object should be created and configured. None of the real action gets started until the
* loadIntoTable() routine is called. After loading, the object can be reconfigured and reused to load
* other files.
*/
public class FlatFileParser2 {

    private ProgressTrackable progressTrackable;

    public ProgressTrackable getProgressTrackable() {
        return progressTrackable;
    }

    public void setProgressTrackable(ProgressTrackable progressTrackable) {
        this.progressTrackable = progressTrackable;
    }

    private String resource;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public static final int FILE = 0;

    public static final int URL = 1;

    private int resourceType = 0;

    public int getResourceType() {
        return resourceType;
    }

    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    public RectData loadIntoTable() throws LoadException, IOException {
        InputStream stream;
        if (getResource().startsWith("http://")) {
            try {
                setResourceType(URL);
                stream = openStream();
            } catch (Exception e) {
                setResourceType(FILE);
                stream = openStream();
            }
        } else {
            try {
                setResourceType(FILE);
                stream = openStream();
            } catch (Exception e) {
                setResourceType(URL);
                stream = openStream();
            }
        }
        return loadIntoTable(stream);
    }

    private void setLength(int i) {
        if (progressTrackable != null) {
            progressTrackable.setLength(i);
        }
    }

    private void setValue(int i) {
        if (progressTrackable != null) {
            progressTrackable.setValue(i);
        }
    }

    private int getValue() {
        if (progressTrackable != null) {
            return progressTrackable.getValue();
        } else {
            return 0;
        }
    }

    private void incrValue(int i) {
        if (progressTrackable != null) {
            progressTrackable.incrValue(i);
        }
    }

    /** returns a list of vectors of String [], representing data from file.*/
    private RectData loadIntoTable(InputStream inputStream) throws IOException, LoadException {
        MeteredStream ms = new MeteredStream(inputStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(ms));
        return FlatFileReader.load(reader, getParseQuotedStrings());
    }

    /** opens a stream from the resource */
    private InputStream openStream() throws LoadException {
        InputStream is;
        String file = getResource();
        if (getResourceType() == FILE) {
            try {
                File fd = new File(file);
                is = new MeteredStream(new FileInputStream(fd));
                setLength((int) fd.length());
            } catch (Exception ioe) {
                throw new LoadException("File " + file + " could not be opened: " + ioe.getMessage(), LoadException.CDTPARSE);
            }
        } else {
            try {
                java.net.URL url = new java.net.URL(file);
                java.net.URLConnection conn = url.openConnection();
                is = new MeteredStream(conn.getInputStream());
                setLength(conn.getContentLength());
            } catch (IOException ioe2) {
                throw new LoadException("Url " + file + " could not be opened: " + ioe2.getMessage(), LoadException.CDTPARSE);
            }
        }
        return is;
    }

    class MeteredStream extends FilterInputStream {

        MeteredStream(InputStream is) {
            super(is);
        }

        public int read() throws IOException {
            incrValue(1);
            return super.read();
        }

        public int read(byte[] b, int off, int len) throws IOException {
            int ret = super.read(b, off, len);
            if (ret != -1) {
                incrValue(ret / 2);
            }
            return ret;
        }

        public long skip(long n) throws IOException {
            long ret = super.skip(n);
            if (ret != -1) {
                incrValue((int) ret / 2);
            }
            return ret;
        }

        int markedValue = 0;

        public void mark(int readLimit) {
            super.mark(readLimit);
            markedValue = getValue();
        }

        public void reset() throws IOException {
            super.reset();
            setValue(markedValue);
        }
    }

    /**
	 * parse quoted strings?
	 */
    private boolean pqs = true;

    /**
	 * @param parseQuotedStrings
	 */
    public void setParseQuotedStrings(boolean parseQuotedStrings) {
        pqs = parseQuotedStrings;
    }

    public boolean getParseQuotedStrings() {
        return pqs;
    }
}

/**
 * @author gcong
 *
 * This class loads flat files into RectData objects. 
 * It should not waste as much ram as parsing into strings.
 * Although it's not clear why the ram doesn't get returned to the heap when you parse into strings.
 * 
 * Note: null entries in number columns will be assigned the value NaN.
 * 
 */
class FlatFileReader {

    static final char DEFAULT_SEP = '\t';

    static final int DEFAULT_TESTSIZE = 10;

    static final int DEFAULT_GAPSIZE = 1000;

    static final String[][] filters = { { "NA", null } };

    private FlatFileReader() {
    }

    public static void main(String[] args) {
        try {
            BufferedReader br1 = new BufferedReader(new FileReader(args[0]));
            RectData data1 = FlatFileReader.load(br1, true);
            System.out.println("Data1 sie = " + data1.size());
            br1.close();
            BufferedReader br2 = new BufferedReader(new FileReader(args[0]));
            Vector data2 = TestLoad.load(br2);
            System.out.println("Data2 sie = " + data2.size());
            br2.close();
            for (int i = 0; i < data1.size() && i < 1000; i++) {
                String[] str1 = (String[]) data1.elementAt(i);
                String[] str2 = (String[]) data2.elementAt(i);
                for (int j = 0; j < str1.length; j++) {
                    if (str1[j] == null && str2[j] == null) {
                        continue;
                    }
                    if (str1[j] == null) {
                        System.out.println(i + " " + j + " 1-");
                    }
                    if (str2[j] == null) {
                        System.out.println(i + " " + j + " 2-");
                    }
                    if (FlatFileReader.isDoubleString(str1[j]) && FlatFileReader.isDoubleString(str2[j])) {
                        double d1 = Double.parseDouble(str1[j]);
                        double d2 = Double.parseDouble(str2[j]);
                        if (d1 != d2) {
                            System.out.println("\t*" + str1[j] + "* *" + str2[j] + "*");
                        }
                    } else {
                        if (!str2[j].equalsIgnoreCase(str1[j])) {
                            System.out.println("\t*" + str1[j] + "* *" + str2[j] + "*");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static RectData load(Reader reader, boolean parseQuotedStrings) throws IOException {
        return load(reader, DEFAULT_SEP, 1000, parseQuotedStrings);
    }

    public static RectData load(Reader reader, char sep, boolean parseQuotedStrings) throws IOException {
        return load(reader, sep, DEFAULT_GAPSIZE, DEFAULT_TESTSIZE, parseQuotedStrings);
    }

    public static RectData load(Reader reader, char sep, int gapSize, boolean parseQuotedStrings) throws IOException {
        return load(reader, sep, gapSize, DEFAULT_TESTSIZE, parseQuotedStrings);
    }

    /**
	 * Note: null entries in number columns will be assigned the value NaN.
	 * 
	 * @param br a reader from which to get data
	 * @param sep a separator character
	 * @param gapSize completely unused. What's it for?
	 * @param testSize numer of lines to fetch to determine column types.
	 * @return RectData representing the values loaded from the file.
	 * @throws IOException
	 */
    public static RectData load(Reader br, char sep, int gapSize, int testSize, boolean parseQuotedStrings) throws IOException {
        FlatFileStreamLiner st = new FlatFileStreamLiner(br, sep, parseQuotedStrings);
        String[] names = null;
        if (st.nextLine()) {
            names = (String[]) st.getLineTokens();
        }
        String[][] lines = new String[testSize][];
        int count = 0;
        while (count < testSize && st.nextLine()) {
            lines[count++] = st.getLineTokens();
        }
        if (count < testSize) {
            String[][] newLines = new String[count][];
            for (int i = 0; i < count; i++) {
                newLines[i] = lines[i];
            }
            lines = newLines;
        }
        ColumnFormat[] formats = checkColumnFormat(lines);
        formats = makeGeneAnnoString(names, formats);
        RectData data = new RectData(names, formats, 5000);
        for (int i = 0; i < count; i++) {
            data.addData(filterString(lines[i]));
        }
        while (st.nextLine()) {
            data.addData(filterString(st.getLineTokens()));
        }
        return data;
    }

    /**
	 * If GWEIGHT occurs in names, it will force GWEIGHT and all columns to the left to be String.
	 * @param names
	 * @param formats
	 * @return
	 */
    private static ColumnFormat[] makeGeneAnnoString(String[] names, ColumnFormat[] formats) {
        int len = names.length;
        int i;
        for (i = 0; i < len; i++) if ("GWEIGHT".equals(names[i])) break;
        if (i < len) for (; i >= 0; i--) formats[i] = ColumnFormat.StringFormat;
        return formats;
    }

    public static ColumnFormat[] checkColumnFormat(String[][] lines) {
        int len = lines[0].length;
        for (int i = 1; i < lines.length; i++) {
            if (lines[i] == null) {
                LogBuffer.println("FlatFileParser.checkColumnFormat got null line");
                continue;
            }
            if (len < lines[i].length) {
                len = lines[i].length;
            }
        }
        ColumnFormat[] formats = new ColumnFormat[len];
        for (int i = 0; i < len; i++) {
            boolean sawNum = false;
            boolean sawString = false;
            for (int j = 0; j < lines.length; j++) {
                if (lines[j] == null) {
                    continue;
                }
                if (lines[j].length > i) {
                    if (lines[j][i] == null) {
                        continue;
                    } else if (isDoubleString(lines[j][i])) {
                        sawNum = true;
                    } else {
                        sawString = true;
                    }
                }
            }
            if (sawString) {
                formats[i] = ColumnFormat.StringFormat;
            } else if (sawNum) {
                formats[i] = ColumnFormat.DoubleFormat;
            } else {
                formats[i] = ColumnFormat.StringFormat;
            }
        }
        return formats;
    }

    public static boolean isDoubleString(String string) {
        if (string == null) {
            return true;
        }
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }

    public static String[] filterString(String[] strings) {
        int len = strings.length;
        String[] str = new String[len];
        for (int i = 0; i < len; i++) {
            str[i] = filterString(strings[i]);
        }
        return str;
    }

    public static String filterString(String string) {
        if (string != null) {
            int len = filters.length;
            for (int i = 0; i < len; i++) {
                if (string.equalsIgnoreCase(filters[i][0])) {
                    return filters[i][1];
                }
            }
        }
        return string;
    }
}
