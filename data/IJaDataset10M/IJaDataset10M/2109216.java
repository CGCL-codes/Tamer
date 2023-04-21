package lang_features;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import framework.ErrorHandlingCompileTest;
import framework.IPtolemyCompileTest;
import framework.IPtolemyTestAdaptor;

/**
 * Create tests for basic language features.
 * Most of the language feature tests are
 * not provided by this class because the
 * Ptolemy compiler test frame work was set up after
 * those tests and refactoring has not yet been a priority.
 * @author Sean Mooney
 *
 */
public class LanguageFeaturesTestProvider {

    private static final String ROOT_TEST_FOLDER = "pyc_tests";

    private static final String LANG_FEATURE_FOLDER = "LanguageFeatures";

    private static final String LANG_TEST_FOLDER = ROOT_TEST_FOLDER + "/" + LANG_FEATURE_FOLDER;

    private static final String PTOLEMY_BASE = "PtolemyBase";

    public static IPtolemyCompileTest createBadBindingTest() {
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(LANG_TEST_FOLDER);
        pathBuilder.append(File.separator);
        pathBuilder.append(PTOLEMY_BASE);
        pathBuilder.append(File.separator);
        pathBuilder.append("BindingStatement");
        return new IPtolemyTestAdaptor(pathBuilder.toString(), "BadBinding.java");
    }

    /**
     * Use a file chooser to select a source code file.
     * @return the selected file, or null if the user cancels.
     */
    private static File chooserTestFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileFilter() {

            @Override
            public String getDescription() {
                return ".java files";
            }

            public boolean accept(File paramFile) {
                String s = paramFile.getName();
                boolean javaFile = s.endsWith(".java");
                return (javaFile || paramFile.isDirectory());
            }
        });
        int choice = chooser.showOpenDialog(null);
        if (choice == JFileChooser.APPROVE_OPTION) return chooser.getSelectedFile();
        return null;
    }
}
