package com.choicemaker.shared.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import junit.framework.TestCase;
import com.choicemaker.shared.api.CMDataFormatException;

public class CMSynonymMapLoaderTest extends TestCase {

    public static final String STEM_SEPARATOR = "_";

    public static final String TEMP_FILE_INITIAL_PREFIX = CMSynonymMapLoaderTest.class.getName();

    public static final String XXX = "XXX";

    public static final String TEMP_FILE_SUFFIX = ".txt";

    protected static String createTempFilePrefix(String dataName) {
        assert dataName != null;
        assert !dataName.trim().isEmpty();
        StringBuilder sb = new StringBuilder();
        sb.append(TEMP_FILE_INITIAL_PREFIX).append(STEM_SEPARATOR).append(dataName.trim()).append(STEM_SEPARATOR).append(XXX);
        return sb.toString();
    }

    public static final String EOL = System.getProperty("line.separator");

    private static final StringBuffer BLANK_KEY1_BUFFER = new StringBuffer();

    static {
        BLANK_KEY1_BUFFER.append("  ").append(EOL);
    }

    public static final String BLANK_KEY1_DATA = BLANK_KEY1_BUFFER.toString();

    private static final StringBuffer BLANK_KEY2_BUFFER = new StringBuffer();

    static {
        BLANK_KEY2_BUFFER.append("  ").append(EOL);
    }

    public static final String BLANK_KEY2_DATA = BLANK_KEY2_BUFFER.toString();

    private static final StringBuffer MISSING_VALUE_BUFFER = new StringBuffer();

    static {
        MISSING_VALUE_BUFFER.append(" key ");
    }

    public static final String MISSING_VALUE_DATA = MISSING_VALUE_BUFFER.toString();

    private static final StringBuffer BLANK_VALUE1_BUFFER = new StringBuffer();

    static {
        BLANK_VALUE1_BUFFER.append(" key ").append(EOL);
    }

    public static final String BLANK_VALUE1_DATA = BLANK_VALUE1_BUFFER.toString();

    private static final StringBuffer BLANK_VALUE2_BUFFER = new StringBuffer();

    static {
        BLANK_VALUE2_BUFFER.append(" key ").append(EOL);
        BLANK_VALUE2_BUFFER.append("  ");
    }

    public static final String BLANK_VALUE2_DATA = BLANK_VALUE2_BUFFER.toString();

    private static final StringBuffer BLANK_VALUE3_BUFFER = new StringBuffer();

    static {
        BLANK_VALUE3_BUFFER.append(" key ").append(EOL);
        BLANK_VALUE3_BUFFER.append("  ").append(EOL);
    }

    public static final String BLANK_VALUE3_DATA = BLANK_VALUE3_BUFFER.toString();

    public enum BAD_DATA {

        BLANK_KEY1(BLANK_KEY1_DATA, CMDataFormatException.class), BLANK_KEY2(BLANK_KEY2_DATA, CMDataFormatException.class), MISSING_VALUE(MISSING_VALUE_DATA, CMDataFormatException.class), BLANK_VALUE1(BLANK_VALUE1_DATA, CMDataFormatException.class), BLANK_VALUE2(BLANK_VALUE2_DATA, CMDataFormatException.class), BLANK_VALUE3(BLANK_VALUE3_DATA, CMDataFormatException.class);

        public final String lines;

        public final Class<? extends Exception> exceptionType;

        private BAD_DATA(String lines, Class<? extends Exception> exceptionType) {
            this.lines = lines;
            this.exceptionType = exceptionType;
        }
    }

    public static final String NULL_DATA = new String();

    public static final Map<String, Set<String>> NULL_DATA_MAP = Collections.unmodifiableMap(new HashMap<String, Set<String>>());

    private static final StringBuffer KEY_VALUE1_BUFFER = new StringBuffer();

    static {
        KEY_VALUE1_BUFFER.append(" key ").append(EOL);
        KEY_VALUE1_BUFFER.append(" value ").append(EOL);
    }

    public static final String KEY_VALUE1_DATA = KEY_VALUE1_BUFFER.toString();

    @SuppressWarnings("serial")
    public static final Map<String, Set<String>> KEY_VALUE1_MAP = Collections.unmodifiableMap(new HashMap<String, Set<String>>() {

        {
            put("key", new HashSet<String>() {

                {
                    add("value");
                }
            });
        }
    });

    private static final StringBuffer KEY_VALUE2_BUFFER = new StringBuffer();

    static {
        KEY_VALUE2_BUFFER.append(" key ").append(EOL);
        KEY_VALUE2_BUFFER.append(" value ").append(EOL);
        KEY_VALUE2_BUFFER.append(" key ").append(EOL);
        KEY_VALUE2_BUFFER.append(" value ").append(EOL);
    }

    public static final String KEY_VALUE2_DATA = KEY_VALUE2_BUFFER.toString();

    public static final Map<String, Set<String>> KEY_VALUE2_MAP = KEY_VALUE1_MAP;

    private static final StringBuffer KEY_VALUE3_BUFFER = new StringBuffer();

    static {
        KEY_VALUE3_BUFFER.append(" key ").append(EOL);
        KEY_VALUE3_BUFFER.append(" value ").append(EOL);
        KEY_VALUE3_BUFFER.append(" key ").append(EOL);
        KEY_VALUE3_BUFFER.append(" value1 ").append(EOL);
    }

    public static final String KEY_VALUE3_DATA = KEY_VALUE3_BUFFER.toString();

    @SuppressWarnings("serial")
    public static final Map<String, Set<String>> KEY_VALUE3_MAP = Collections.unmodifiableMap(new HashMap<String, Set<String>>() {

        {
            put("key", new HashSet<String>() {

                {
                    add("value");
                    add("value1");
                }
            });
        }
    });

    private static final StringBuffer NAMES_BUFFER = new StringBuffer();

    static {
        NAMES_BUFFER.append(" Richard ").append(EOL);
        NAMES_BUFFER.append(" Dick ").append(EOL);
        NAMES_BUFFER.append(" Richard ").append(EOL);
        NAMES_BUFFER.append(" Rick ").append(EOL);
        NAMES_BUFFER.append(" Richard ").append(EOL);
        NAMES_BUFFER.append(" Rich ").append(EOL);
        NAMES_BUFFER.append(" Fredrick ").append(EOL);
        NAMES_BUFFER.append(" Fred ").append(EOL);
        NAMES_BUFFER.append(" Fredrick ").append(EOL);
        NAMES_BUFFER.append(" Rick ").append(EOL);
        NAMES_BUFFER.append(" Fredrick ").append(EOL);
        NAMES_BUFFER.append(" Freddy ").append(EOL);
        NAMES_BUFFER.append(" Fred ").append(EOL);
        NAMES_BUFFER.append(" Freddy ").append(EOL);
    }

    public static final String NAMES_DATA = NAMES_BUFFER.toString();

    @SuppressWarnings("serial")
    public static final Map<String, Set<String>> NAMES_MAP = Collections.unmodifiableMap(new HashMap<String, Set<String>>() {

        {
            put("Richard", new HashSet<String>() {

                {
                    add("Dick");
                    add("Rick");
                    add("Rich");
                }
            });
            put("Fredrick", new HashSet<String>() {

                {
                    add("Fred");
                    add("Rick");
                    add("Freddy");
                }
            });
            put("Fred", new HashSet<String>() {

                {
                    add("Freddy");
                }
            });
        }
    });

    @SuppressWarnings("serial")
    public static final Map<String, String[]> NAMES_SYNONYMS = Collections.unmodifiableMap(new HashMap<String, String[]>() {

        {
            put("richard", new String[] { "dick", "rich", "rick" });
            put("rich", new String[] { "richard" });
            put("dick", new String[] { "richard" });
            put("rick", new String[] { "fredrick", "richard" });
            put("fredrick", new String[] { "fred", "freddy", "rick" });
            put("fred", new String[] { "freddy", "fredrick" });
            put("freddy", new String[] { "fred", "fredrick" });
        }
    });

    public enum EXPECTED_MAP {

        NULL(NULL_DATA, NULL_DATA_MAP), KEY_VALUE1(KEY_VALUE1_DATA, KEY_VALUE1_MAP), KEY_VALUE2(KEY_VALUE2_DATA, KEY_VALUE2_MAP), KEY_VALUE3(KEY_VALUE3_DATA, KEY_VALUE3_MAP), NAMES(NAMES_DATA, NAMES_MAP);

        public final String lines;

        public final Map<String, Set<String>> map;

        private EXPECTED_MAP(String lines, Map<String, Set<String>> map) {
            this.lines = lines;
            this.map = map;
        }
    }

    public void testBadDataFromCMFileType1BufferedReader() {
        for (BAD_DATA bad : BAD_DATA.values()) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new StringReader(bad.lines));
                CMSynonymMapLoader.loadMapFromCMFileType1(br);
                fail("Failed to throw " + bad.exceptionType.getName());
            } catch (Exception x) {
                assertTrue(bad.name(), bad.exceptionType.isInstance(x));
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (Exception ignored) {
                        System.err.println("unable to close reader of " + bad.name());
                    }
                }
            }
        }
    }

    public void testLoadMapFromCMFileType1BufferedReader() {
        for (EXPECTED_MAP expected : EXPECTED_MAP.values()) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new StringReader(expected.lines));
                Map<String, Set<String>> map = CMSynonymMapLoader.loadMapFromCMFileType1(br);
                assertTrue(expected.name(), expected.map.equals(map));
            } catch (Exception x) {
                fail(expected.name() + ": " + x.toString());
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (Exception ignored) {
                        System.err.println("unable to close reader of " + expected.name());
                    }
                }
            }
        }
    }

    public void testBadDataFromCMFileType1String() {
        for (BAD_DATA bad : BAD_DATA.values()) {
            File f = null;
            String fqfn = null;
            try {
                String prefix = createTempFilePrefix(bad.name());
                f = File.createTempFile(prefix, TEMP_FILE_SUFFIX);
                f.deleteOnExit();
                fqfn = f.getAbsolutePath();
                writeDataToFile(bad.lines, f);
            } catch (Exception x) {
                fail(bad.name() + ": " + x.toString());
            }
            try {
                CMSynonymMapLoader.loadMapFromCMFileType1(fqfn);
                fail("Failed to throw " + bad.exceptionType.getName());
            } catch (Exception x) {
                assertTrue(bad.name(), bad.exceptionType.isInstance(x));
            }
        }
    }

    public void testLoadMapFromCMFileType1String() {
        for (EXPECTED_MAP expected : EXPECTED_MAP.values()) {
            String fqfn = null;
            try {
                String prefix = createTempFilePrefix(expected.name());
                File f = File.createTempFile(prefix, TEMP_FILE_SUFFIX);
                f.deleteOnExit();
                fqfn = f.getAbsolutePath();
                writeDataToFile(expected.lines, f);
            } catch (Exception x) {
                fail(expected.name() + ": " + x.toString());
            }
            try {
                Map<String, Set<String>> map = CMSynonymMapLoader.loadMapFromCMFileType1(fqfn);
                assertTrue(expected.name(), expected.map.equals(map));
            } catch (Exception x) {
                fail(expected.name() + ": " + x.toString());
            }
        }
    }

    public void testLoadSynonymsFromCMFileType1BufferedReader() {
        final String lines = NAMES_DATA;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new StringReader(lines));
            CMSynonymMap sm = CMSynonymMapLoader.loadSynonymMapFromCMFileType1(br);
            SynonymMapTest.testSynonymMap(sm, NAMES_SYNONYMS);
        } catch (Exception x) {
            fail(x.toString());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception ignored) {
                    System.err.println("unable to close reader of NAMES_DATA");
                }
            }
        }
    }

    public void testLoadSynonymsFromCMFileType1String() {
        final String lines = NAMES_DATA;
        String fqfn = null;
        try {
            String prefix = createTempFilePrefix("NAMES");
            File f = File.createTempFile(prefix, TEMP_FILE_SUFFIX);
            f.deleteOnExit();
            fqfn = f.getAbsolutePath();
            writeDataToFile(lines, f);
        } catch (Exception x) {
            fail(x.toString());
        }
        try {
            CMSynonymMap sm = CMSynonymMapLoader.loadSynonymMapFromCMFileType1(fqfn);
            SynonymMapTest.testSynonymMap(sm, NAMES_SYNONYMS);
        } catch (Exception x) {
            fail(x.toString());
        }
    }

    protected static void writeDataToFile(String lines, File f) throws IOException {
        assert lines != null;
        assert f != null;
        FileWriter fw = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new StringReader(lines));
            fw = new FileWriter(f);
            String line = br.readLine();
            while (line != null) {
                fw.append(line).append(EOL);
                fw.flush();
                line = br.readLine();
            }
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (Exception ignored) {
                    System.err.println("unable to close File writer");
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (Exception ignored) {
                    System.err.println("unable to close String reader");
                }
            }
        }
    }
}
