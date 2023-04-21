package dalvik.runner;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A code finder that traverses through the directory tree looking for matching
 * naming patterns.
 */
abstract class NamingPatternCodeFinder implements CodeFinder {

    private final String PACKAGE_PATTERN = "(?m)^\\s*package\\s+(\\S+)\\s*;";

    private final String TYPE_DECLARATION_PATTERN = "(?m)\\b(?:public|private)\\s+(?:final\\s+)?(?:interface|class|enum)\\b";

    public Set<TestRun> findTests(File testDirectory) {
        Set<TestRun> result = new LinkedHashSet<TestRun>();
        findTestsRecursive(result, testDirectory);
        return result;
    }

    /**
     * Returns true if {@code file} contains a test class of this type.
     */
    protected boolean matches(File file) {
        return file.getName().endsWith(".java");
    }

    protected abstract String testName(File file);

    private void findTestsRecursive(Set<TestRun> sink, File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                findTestsRecursive(sink, child);
            }
            return;
        }
        if (!matches(file)) {
            return;
        }
        String className = fileToClass(file);
        File testDirectory = file.getParentFile();
        String testName = testName(file);
        String testDescription = null;
        sink.add(new TestRun(testDirectory, file, className, className, testName, className, testDescription, getRunnerClass(), getRunnerJava(), getRunnerClasspath()));
    }

    /**
     * Returns the Java classname for the given file. For example, given the
     * input {@code luni/src/test/java/org/apache/harmony/luni/tests/java/util/ArrayListTest.java},
     * this returns {@code org.apache.harmony.luni.tests.java.util.ArrayListTest}.
     */
    private String fileToClass(File file) {
        String filePath = file.getPath();
        if (!filePath.endsWith(".java")) {
            throw new IllegalArgumentException("Not a .java file: " + file);
        }
        String filename = file.getName();
        String className = filename.substring(0, filename.length() - 5);
        try {
            String content = Strings.readFile(file);
            Pattern packagePattern = Pattern.compile(PACKAGE_PATTERN);
            Matcher packageMatcher = packagePattern.matcher(content);
            if (!packageMatcher.find()) {
                if (Pattern.compile(TYPE_DECLARATION_PATTERN).matcher(content).find()) {
                    return className;
                }
                throw new IllegalArgumentException("Not a .java file: '" + file + "'\n" + content);
            }
            String packageName = packageMatcher.group(1);
            return packageName + "." + className;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Couldn't read '" + file + "': " + ex.getMessage());
        }
    }
}
