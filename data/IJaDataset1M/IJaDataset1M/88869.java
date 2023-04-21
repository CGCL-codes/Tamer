package edu.colorado.emml.construction;

import weka.core.Instances;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import edu.colorado.emml.classifiers.BatchClassifier;
import edu.colorado.emml.construction.parser.FileExpansion;
import edu.colorado.emml.util.FileUtils;
import edu.colorado.emml.util.IncrementalFileUtils;
import edu.colorado.emml.util.Util;
import edu.colorado.emml.util.WekaUtil;
import edu.colorado.emml.util.argsparser.Arg;
import edu.colorado.emml.util.argsparser.ArgsParser;
import edu.colorado.emml.util.argsparser.OptionalArg;

/**
 * Created by: Sam
 * Apr 20, 2008 at 11:18:30 AM
 */
public class ModelLibraryConstructorMain {

    private File classifiers;

    private File dir;

    private Integer start = null;

    private Integer end = null;

    private Integer increment = null;

    private File train;

    private File test;

    private ModelLibraryIterator iterator;

    public ModelLibraryConstructorMain(File classifiers, File dir) {
        this.classifiers = classifiers;
        this.dir = dir;
        this.train = new File(dir, "train.arff");
        this.test = new File(dir, "test.arff");
    }

    public ModelLibraryConstructorMain(String args) {
        this(Util.toStringArray(args));
    }

    public ModelLibraryConstructorMain(String[] args) {
        ArgsParser<ModelLibraryConstructorMain> modelLibraryBuilderArgsParser = new ArgsParser<ModelLibraryConstructorMain>(argsList);
        directoryArg.apply(this, args);
        dir.mkdirs();
        modelLibraryBuilderArgsParser.parse(this, args);
    }

    public void setClassifiers(File classifiers) {
        this.classifiers = classifiers;
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public File getClassifiers() {
        return classifiers;
    }

    public File getDir() {
        return dir;
    }

    private static Instances loadInstances(File filename) {
        try {
            return WekaUtil.loadInstances(filename);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static final String DEFAULT_CLASSIFIER_FILE_NAME = "classifiers.txt";

    public static final String DEFAULT_TRAIN_FILE_NAME = "train.arff";

    public static final String DEFAULT_TEST_FILE_NAME = "test.arff";

    static OptionalArg<ModelLibraryConstructorMain> directoryArg = new OptionalArg<ModelLibraryConstructorMain>("dir", "the directory in which to create the new model library") {

        public void parseArgs(ModelLibraryConstructorMain m, String[] params) {
            m.setDir(new File(params[0]));
        }

        protected void handleArgNotSpecified(ModelLibraryConstructorMain modelLibraryBuilder) {
            if (modelLibraryBuilder.getDir() == null) {
                modelLibraryBuilder.setNextUnusedDir(new File("."));
            }
        }
    };

    public void setNextUnusedDir(File parent) {
        setDir(IncrementalFileUtils.getNextDirectory(new File(parent, "model-library")));
    }

    private static final Arg[] argsList = new Arg[] { new OptionalArg<ModelLibraryConstructorMain>("classifiers", "the file that specifies the classifiers to use (defaults to " + DEFAULT_CLASSIFIER_FILE_NAME + ")") {

        public void parseArgs(ModelLibraryConstructorMain m, String[] params) {
            m.setClassifiers(new File(params[0]));
        }

        protected void handleArgNotSpecified(ModelLibraryConstructorMain modelLibraryBuilder) {
            modelLibraryBuilder.setClassifiers(new File(DEFAULT_CLASSIFIER_FILE_NAME));
        }
    }, new OptionalArg<ModelLibraryConstructorMain>("train", "ARFF file to be used for training (defaults to " + DEFAULT_TRAIN_FILE_NAME + ")") {

        public void parseArgs(ModelLibraryConstructorMain m, String[] params) {
            m.setTrain(new File(params[0]));
        }

        protected void handleArgNotSpecified(ModelLibraryConstructorMain modelLibraryBuilder) {
            modelLibraryBuilder.setTrain(new File(DEFAULT_TRAIN_FILE_NAME));
        }
    }, new OptionalArg<ModelLibraryConstructorMain>("test", "ARFF file to be used for testing (defaults to " + DEFAULT_TEST_FILE_NAME + ")") {

        public void parseArgs(ModelLibraryConstructorMain m, String[] params) {
            m.setTest(new File(params[0]));
        }

        protected void handleArgNotSpecified(ModelLibraryConstructorMain modelLibraryBuilder) {
            modelLibraryBuilder.setTest(new File(DEFAULT_TEST_FILE_NAME));
        }
    }, directoryArg, new OptionalArg<ModelLibraryConstructorMain>("iterator", "specifies iterator type, 'index' start end increment or 'auto' for automatic integer selection, defaults to 'auto'") {

        public void parseArgs(ModelLibraryConstructorMain m, String[] params) {
            if (params[0].equals("index")) {
                m.setIterator(new IncrementalIterator(Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3])));
            }
        }
    }, new OptionalArg<ModelLibraryConstructorMain>("help", "displays help information and exits") {

        public void parseArgs(ModelLibraryConstructorMain m, String[] params) {
            printUsage();
            System.exit(0);
        }
    } };

    private void setIterator(ModelLibraryIterator iterator) {
        this.iterator = iterator;
    }

    private void setTest(File test) {
        this.test = test;
    }

    private void setTrain(File train) {
        this.train = train;
    }

    private static void printUsage() {
        for (Arg arg : argsList) {
            System.out.println(arg);
        }
    }

    public void buildModelLibrary() throws Exception {
        dir.mkdirs();
        if (!classifiers.exists()) {
            System.out.println("No classifiers specified.\n");
            printUsage();
            return;
        }
        System.out.println("Started building model library, config=" + toString());
        final BatchClassifier[] algorithms = loadAlgorithms(classifiers);
        if (start == null) {
            start = 0;
        }
        if (end == null) {
            end = algorithms.length - 1;
        }
        if (increment == null) {
            increment = +1;
        }
        ModelLibraryConstructorJava modelLibraryConstructor = new ModelLibraryConstructorJava(dir, loadInstances(train), loadInstances(test));
        if (iterator == null) {
            System.out.println("No iterator specified, using auto in " + dir.getParentFile() + " on classifierFile=" + classifiers);
            iterator = new UnfinishedModelIterator(dir.getParentFile(), classifiers);
        }
        modelLibraryConstructor.constructModelLibrary(algorithms, iterator);
    }

    public static BatchClassifier[] loadAlgorithms(File classifierFile) throws IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
        BatchClassifier[] classifiers = new FileExpansion().getClassifiers(classifierFile);
        File expanded = new File(classifierFile.getAbsoluteFile().getParent(), "classifiers-full.txt");
        if (!expanded.exists()) {
            FileUtils.writeString(toString(classifiers), expanded);
        } else {
            BatchClassifier[] loaded = new FileExpansion().getClassifiers(expanded);
            if (new HashSet<BatchClassifier>(Arrays.asList(classifiers)).equals(new HashSet<BatchClassifier>(Arrays.asList(loaded)))) {
                throw new RuntimeException("mismatched classifier sets; expansion didn't match compressed.  Perhaps the parser changed...  Or if the classifiers.txt file changed, you can delete the classifiers-full.txt and re-run this program to continue.");
            }
        }
        return classifiers;
    }

    private static String toString(BatchClassifier[] classifiers) {
        StringBuffer s = new StringBuffer();
        for (BatchClassifier classifier : classifiers) {
            s.append(classifier).append("\n");
        }
        return s.toString();
    }

    public String toString() {
        return "ModelLibraryBuilder{" + "classifiers=" + classifiers + ", train=" + train.getAbsolutePath() + ", test=" + test.getAbsolutePath() + ", dir=" + dir + ", start=" + start + ", end=" + end + ", increment=" + increment + '}';
    }

    public static void main(String[] args) throws Exception {
        new ModelLibraryConstructorMain(args).buildModelLibrary();
    }
}
