package unittest.framework;

import java.lang.*;
import java.io.*;
import java.util.*;

public class TestRunner {

    static Test tests[];

    Hashtable prefs_hash;

    public static void main(String[] args) {
        TestRunner t;
        t = new TestRunner(args);
    }

    public TestRunner(String[] args) {
        String testSuiteName = args[0];
        if (args.length > 1) {
            loadPrefs(args[1]);
        }
        tests = testSuiteNamed(testSuiteName).tests;
        runTests();
    }

    private TestSuite testSuiteNamed(String aClassName) {
        try {
            return (TestSuite) Class.forName(aClassName).newInstance();
        } catch (Exception exception) {
            return new TestSuite();
        }
    }

    void runTests() {
        for (int each = 0; each < tests.length; each++) {
            runTest(each);
            showResults();
        }
        ;
    }

    void runTest(int anIndex) {
        tests[anIndex].setUp(prefs_hash);
        tests[anIndex].run();
        tests[anIndex].tearDown();
    }

    private void showResults() {
        System.out.println("test results:");
        for (int each = 0; each < tests.length; each++) {
            System.out.println(tests[each].result + each);
        }
        showScore();
        System.out.println("finished");
    }

    private void showScore() {
        int passed = numberPassed();
        float total = (float) tests.length;
        int score = (int) (passed / total * 100);
        System.out.println("Score = " + (new Integer(score).toString() + "%"));
        showPassFail(score);
    }

    private int numberPassed() {
        int passed = 0;
        for (int each = 0; each < tests.length; each++) {
            if (tests[each].success) {
                passed++;
            }
            ;
        }
        return passed;
    }

    private void showPassFail(int aScore) {
        System.out.println((aScore == 100) ? "PASS" : "FAIL");
    }

    private void loadPrefs(String prefs_file) {
        String key;
        int tok = 0;
        StreamTokenizer tokens;
        InputStream prefs_stream;
        System.out.println("reading prefs file " + prefs_file);
        System.out.flush();
        if (prefs_file != null && !prefs_file.equals("")) {
            if (!(new File(prefs_file)).canRead()) {
                System.out.println("Can't read options from " + prefs_file);
                System.exit(0);
            }
            try {
                prefs_stream = new FileInputStream(prefs_file);
                Reader r = new BufferedReader(new InputStreamReader(prefs_stream));
                tokens = new StreamTokenizer(r);
                tokens.eolIsSignificant(false);
                tokens.quoteChar('"');
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
                return;
            }
            try {
                while ((tok = tokens.nextToken()) != StreamTokenizer.TT_EOF) {
                    System.out.println("tok for key = " + tok);
                    key = tokens.sval;
                    System.out.println("key=" + key);
                    tok = tokens.nextToken();
                    System.out.println("tok for val = " + tok);
                    if (prefs_hash == null) prefs_hash = new Hashtable();
                    prefs_hash.put(key, tokens.sval);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
                return;
            }
            try {
                prefs_stream.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
