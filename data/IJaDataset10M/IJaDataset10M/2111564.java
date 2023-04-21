package palus.testgen;

import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import palus.Log;
import palus.PalusUtil;
import palus.analysis.MethodRecommender;
import palus.main.PalusOptions;
import palus.model.ClassModel;
import palus.model.Transition;
import palus.theory.TheoryCheckingVisitor;
import palus.theory.TheoryFinder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map;
import randoop.AbstractGenerator;
import randoop.ContractCheckingVisitor;
import randoop.EqualsHashcode;
import randoop.EqualsReflexive;
import randoop.EqualsSymmetric;
import randoop.EqualsToNullRetFalse;
import randoop.ExecutableSequence;
import randoop.ExecutionVisitor;
import randoop.ForwardGenerator;
import randoop.Globals;
import randoop.JunitFileWriter;
import randoop.ObjectContract;
import randoop.RConstructor;
import randoop.RMethod;
import randoop.RegressionCaptureVisitor;
import randoop.SeedSequences;
import randoop.Sequence;
import randoop.SequenceCollection;
import randoop.StatementKind;
import randoop.main.GenInputsAbstract;
import randoop.util.Randomness;
import randoop.util.Reflection;

/**
 * The entry class which outlines the test generation flow.
 * 
 * @author saizhang@google.com (Sai Zhang)
 *
 */
public class TestGenMain {

    /**
     * the random seed
     * */
    public static int randomseed = (int) Randomness.SEED;

    /**
     * the default output dir
     * */
    public static String outputDir = "./tests";

    /**
     * the default package name
     * */
    public static String packageName = "tests";

    /**
     * the default junit test file name
     * */
    public static String testName = "GeneratedTest";

    /**
     * the number of tests per file
     * */
    public static int testsPerFile = 500;

    /**
     * print the model coverage report or not
     * */
    public static boolean printModelCoverage = false;

    /**
     * model-based testing or pure random testing
     * */
    public static boolean useModel = false;

    /**
     * add the modelled class automatically or not
     * */
    public static boolean addModelClass = true;

    /**
     * the default time limit
     * */
    public static int timelimit = 20;

    /**
     * the output size (max output junit sequence num)
     * */
    public static int inputlimit = 100000000;

    /**
     * add relevant classes or not
     * */
    public static boolean addRelevantClass = true;

    /**
     * check the theory oracle or not
     * */
    public static boolean checkTheory = true;

    /**
     * exhaustively enumerate all possible objects
     * */
    public static boolean exhaustiveTheoryChecking = false;

    /**
     * use param value specified for each method
     * */
    public static boolean useMethodSpecificValue = true;

    /**
     * remove all IsNull checkers
     * */
    public static boolean removeIsNotNullChecker = true;

    /**
     * remove all time out checker
     * */
    public static boolean removeTimeOutChecker = true;

    /**
     * append related method use the result of static analysis
     * */
    public static boolean diversifySequence = true;

    /**
     * the file containing all classes to be tested. Other classes will be
     * ignored.
     * */
    public static String classFilePath = null;

    /**
     * the file containing all classes for modelling. If the value is not provided,
     * the tool will use {@code classFilePath} as default.
     * */
    public static String modelClassPath = null;

    /**
     * a collection to store all programmer-specified values
     * */
    private ParamValueCollections paramValueCollection = null;

    /**
     * the sequence collection storing all temp created sequences
     * */
    private static SequenceCollection components;

    /**
     * the method recommender for get related methods
     * */
    private MethodRecommender recommender = null;

    /**
     * The handle of test generator used in this tool
     * */
    private static AbstractGenerator testgenhandler = null;

    /**
     * Constructor, register a JVM shutdown hook here
     * */
    public TestGenMain() {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                if (testgenhandler == null) {
                    return;
                } else {
                    System.out.println("Output already generated tests even when Palus crashes: ");
                    testName = testName + "_Crash_" + System.currentTimeMillis() + "_";
                    TestGenMain.outputGeneratedTests(testgenhandler);
                }
            }
        });
    }

    /**
     * Entry for generating tests
     * */
    public void generateTests(String[] args, Map<Class<?>, ClassModel> models) {
        GenInputsAbstract.long_format = true;
        GenInputsAbstract.no_args_statement_heuristic = false;
        this.nonStaticMain(args, models);
    }

    /**
     * The main entry for test generation
     * */
    private void nonStaticMain(String[] args, Map<Class<?>, ClassModel> models) {
        if (models != null) {
            useModel = true;
        }
        randomseed = PalusOptions.random_seed;
        Randomness.reset(randomseed);
        if (randomseed != Randomness.SEED) {
            System.out.println("Change to a new seed: " + randomseed);
        }
        Set<Class<?>> allClasses = this.findAllClassesInModels(models);
        allClasses.addAll(readClassFromFile(TestGenMain.classFilePath));
        if (addRelevantClass) {
            allClasses.addAll(this.getModelOwnerClasses(models));
        }
        Set<Class<?>> classesToTest = this.filterUntestableClasses(allClasses);
        System.out.println("There are " + allClasses.size() + " classes as input");
        Log.log("Classes as inputs: ");
        Log.log(allClasses.toString());
        System.out.println("There are " + classesToTest.size() + " classes to test after filtering");
        Log.log("Classes as inputs after filtering: ");
        Log.log(classesToTest.toString());
        List<StatementKind> model = Reflection.getStatements(classesToTest, null);
        this.addObjectConstructor(model);
        this.addPublicStaticMethodFromAbstracts(model, allClasses);
        this.removeTheoryCheckingStatement(model);
        this.removeIgnorableStatements(model);
        if (model.size() == 0) {
            System.out.println("There is nothing to test!");
            System.exit(0);
        } else {
            System.out.println("Num of all public methods under test: " + model.size());
        }
        this.recommender = new MethodRecommender(classesToTest);
        if (diversifySequence && !PalusOptions.random) {
            System.out.println(Globals.lineSep + "Building up method dependence model for diversifying...");
            recommender.buildDependence(model);
            Log.log("---- all method dependence ----");
            Log.log(recommender.showDependence());
            System.out.println("Finishing method dependence model building");
            System.out.println();
        }
        components = new SequenceCollection();
        components.addAll(SeedSequences.objectsToSeeds(SeedSequences.primitiveSeeds));
        components.addAll(SeedSequences.getSeedsFromAnnotatedFields(allClasses.toArray(new Class<?>[0])));
        if (PalusOptions.use_enum_type) {
            components.addAll(SeedSequences.enumsToSeeds(allClasses));
        }
        if (useMethodSpecificValue) {
            ParamValueProcessor paramValueProcessor = new ParamValueProcessor(allClasses);
            this.paramValueCollection = paramValueProcessor.processAnnotations();
            components.addAll(SeedSequences.objectsToSeeds(this.paramValueCollection.allPrimitiveObjects()));
            ObjectSequenceProcessor objSeqProcessor = new ObjectSequenceProcessor(allClasses);
            Collection<Sequence> userProvidedSeqs = objSeqProcessor.processAnnotations();
            components.addAll(userProvidedSeqs);
        }
        AbstractGenerator explorer = null;
        if (useModel) {
            PalusUtil.checkNull(models, "The constructed class models could not be null.");
            explorer = new ModelBasedGenerator(model, null, timelimit * 1000, inputlimit, components, models, paramValueCollection, recommender);
        } else {
            System.out.println("Fall back to use Randoop...");
            explorer = new ForwardGenerator(model, null, timelimit * 1000, inputlimit, components);
        }
        TestGenMain.testgenhandler = explorer;
        List<ExecutionVisitor> visitors = this.getExecutionVisitors();
        if (checkTheory) {
            TheoryFinder theoryFinder = new TheoryFinder(classesToTest);
            List<ObjectContract> theories = theoryFinder.findAllTheories();
            ExecutionVisitor theoryVisitor = new TheoryCheckingVisitor(theories, true);
            visitors.add(theoryVisitor);
            System.out.println("Find " + theories.size() + " theories in total.");
            for (ObjectContract theory : theories) {
                System.out.println("   " + theory.toCommentString());
            }
        }
        explorer.executionVisitor.visitors.addAll(visitors);
        System.out.println("All visitors used: " + visitors);
        System.out.println("Using explorer: " + explorer.getClass().getName());
        System.out.println("Start generating tests ...");
        explorer.explore();
        System.out.println("Finish generating tests.");
        TestGenMain.outputGeneratedTests(explorer);
        if (diversifySequence && explorer instanceof ModelBasedGenerator) {
            Set<ExecutableSequence> diversifiedSequence = ((ModelBasedGenerator) explorer).getAllDiversifiedSequences();
            if (diversifiedSequence.size() != 0) {
                TestGenMain.write_junit_tests(outputDir, "tests", "Diversified", testsPerFile, new LinkedList<ExecutableSequence>(diversifiedSequence));
            }
        }
        if (printModelCoverage && useModel && explorer instanceof ModelBasedGenerator) {
            System.out.println("-------------  overall model coverage ---------------");
            System.out.println(((ModelBasedGenerator) explorer).reportOnModelCoverage());
        }
        TestGenMain.testgenhandler = null;
    }

    /**
     * Find all class-under-test from argument and the input model.
     * The classes to be test are composed from 2 parts, The first from
     * the randoop argument, the other one is from the Model
     * */
    private Set<Class<?>> findAllClassesInModels(Map<Class<?>, ClassModel> models) {
        Set<Class<?>> retClasses = new LinkedHashSet<Class<?>>();
        if (models != null && TestGenMain.addModelClass) {
            Set<Class<?>> clazzSet = models.keySet();
            for (Class<?> clazz : clazzSet) {
                if (Reflection.isVisible(clazz)) {
                    retClasses.add(clazz);
                }
            }
        }
        return retClasses;
    }

    /**
     * Filters un-testable classes from the whole list
     * */
    private Set<Class<?>> filterUntestableClasses(Set<Class<?>> classes) {
        Set<Class<?>> retClasses = new LinkedHashSet<Class<?>>();
        for (Class<?> clz : classes) {
            if (Reflection.isAbstract(clz) || !Reflection.isVisible(clz.getModifiers())) {
                continue;
            }
            retClasses.add(clz);
        }
        return retClasses;
    }

    /**
     * Adds Object.<init>() to the testing method candidates
     * */
    private void addObjectConstructor(List<StatementKind> model) {
        RConstructor objectConstructor;
        try {
            objectConstructor = RConstructor.getRConstructor(Object.class.getConstructor());
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        if (!model.contains(objectConstructor)) model.add(objectConstructor);
    }

    /**
     * Adds testable abstract class public static method
     * */
    private void addPublicStaticMethodFromAbstracts(List<StatementKind> model, Set<Class<?>> allClasses) {
        for (Class<?> clazz : allClasses) {
            if (Reflection.isAbstract(clazz) && Reflection.isVisible(clazz)) {
                Method[] methods = Reflection.getDeclaredMethodsOrdered(clazz);
                for (Method method : methods) {
                    int modifier = method.getModifiers();
                    Class<?> returnType = method.getReturnType();
                    Class<?>[] paramTypes = method.getParameterTypes();
                    if (Modifier.isStatic(modifier) && Modifier.isPublic(modifier) && Reflection.isVisible(returnType) && PalusUtil.areAllVisible(paramTypes)) {
                        RMethod mc = RMethod.getRMethod(method);
                        model.add(mc);
                    }
                }
            }
        }
    }

    /**
     * Removes the theory checking statements
     * */
    private void removeTheoryCheckingStatement(List<StatementKind> model) {
        List<StatementKind> toBeRemoved = new LinkedList<StatementKind>();
        for (StatementKind statement : model) {
            if (statement instanceof RMethod) {
                RMethod rmethod = (RMethod) statement;
                Theory t = rmethod.getMethod().getAnnotation(Theory.class);
                if (t != null) {
                    toBeRemoved.add(statement);
                }
            } else if (statement instanceof RConstructor) {
                RConstructor rconstructor = (RConstructor) statement;
                Constructor<?> constr = rconstructor.getConstructor();
                Class<?> ownerClazz = constr.getDeclaringClass();
                RunWith annotation = ownerClazz.getAnnotation(RunWith.class);
                if (annotation != null) {
                    toBeRemoved.add(statement);
                }
            }
        }
        model.removeAll(toBeRemoved);
    }

    /**
     * Removes the ignorable statements
     * */
    private void removeIgnorableStatements(List<StatementKind> model) {
        List<StatementKind> toBeRemoved = new LinkedList<StatementKind>();
        for (StatementKind statement : model) {
            if (statement instanceof RMethod) {
                RMethod rmethod = (RMethod) statement;
                IgnorableMethod t = rmethod.getMethod().getAnnotation(IgnorableMethod.class);
                if (t != null) {
                    toBeRemoved.add(statement);
                }
            }
        }
        model.removeAll(toBeRemoved);
    }

    /**
     * Gets all execution visitors to serve as oracle checking
     * */
    private List<ExecutionVisitor> getExecutionVisitors() {
        List<ExecutionVisitor> visitors = new ArrayList<ExecutionVisitor>();
        List<ObjectContract> contracts = new ArrayList<ObjectContract>();
        contracts.add(new EqualsReflexive());
        contracts.add(new EqualsToNullRetFalse());
        contracts.add(new EqualsHashcode());
        contracts.add(new EqualsSymmetric());
        ContractCheckingVisitor contractVisitor = new ContractCheckingVisitor(contracts, GenInputsAbstract.offline ? false : true);
        visitors.add(contractVisitor);
        visitors.add(new RegressionCaptureVisitor());
        return visitors;
    }

    /**
     * Postprocess all the generated sequence from explorer
     * */
    private static void outputGeneratedTests(AbstractGenerator explorer) {
        System.out.println("Outputing generated tests ... total num: " + explorer.stats.outSeqs.size());
        List<ExecutableSequence> sequences = new ArrayList<ExecutableSequence>();
        List<ExecutableSequence> generated_sequences = explorer.stats.outSeqs;
        List<ExecutableSequence> unique_generated_sequences = SequenceFilters.filterRepetitiveSequences(generated_sequences);
        for (ExecutableSequence p : unique_generated_sequences) {
            sequences.add(p);
        }
        System.out.println("Remove: " + (generated_sequences.size() - unique_generated_sequences.size()) + " redundant sequences. ");
        RConstructor objectConstructor = null;
        try {
            objectConstructor = RConstructor.getRConstructor(Object.class.getConstructor());
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Sequence newObj = new Sequence().extend(objectConstructor);
        if (GenInputsAbstract.remove_subsequences) {
            List<ExecutableSequence> unique_seqs = new ArrayList<ExecutableSequence>();
            Set<Sequence> subsumed_seqs = explorer.subsumed_sequences();
            for (ExecutableSequence es : sequences) {
                if (!subsumed_seqs.contains(es.sequence) && !es.sequence.equals(newObj)) {
                    unique_seqs.add(es);
                }
            }
            System.out.printf("%d subsumed tests removed%n", sequences.size() - unique_seqs.size());
            sequences = unique_seqs;
        }
        if (GenInputsAbstract.outputlimit < sequences.size()) {
            List<ExecutableSequence> seqs = new ArrayList<ExecutableSequence>();
            for (int ii = 0; ii < GenInputsAbstract.outputlimit; ii++) {
                seqs.add(sequences.get(ii));
            }
            sequences = seqs;
        }
        if (removeIsNotNullChecker) {
            SequenceCheckFilters.removeIsNotNullChecks(sequences);
        }
        if (removeTimeOutChecker) {
            TimeoutCheckerFilters.removeTimeOutSequence(sequences);
        }
        System.out.println("Dumping all potential buggy method.");
        BuggyMethodSummarizer.dumpBuggyMethods(sequences);
        if (PalusOptions.exception_dump_file != null) {
            SequenceExceptions seqExceptions = new SequenceExceptions(sequences, PalusOptions.exception_dump_file);
            seqExceptions.dumpMethodExceptions();
            System.out.println("Dump exception files to: " + seqExceptions.dumpFilePath());
        }
        if (PalusOptions.outputtests) {
            write_junit_tests(outputDir, packageName, testName, testsPerFile, sequences);
        } else {
            System.out.println("Finished: not output any tests as the user specifies.");
        }
    }

    /**
     * Borrowed from Randoop, outputs all generated tests
     * */
    private static void write_junit_tests(String output_dir, String junit_package_name, String junit_classname, int testsperfile, List<ExecutableSequence> seq) {
        if (PalusOptions.only_output_failed_tests) {
            System.out.println("Only output failed tests!");
            seq = SequenceFilters.filterNonFailedSequences(seq);
        }
        if (PalusOptions.filter_redundant_failures) {
            int num = seq.size();
            System.out.println("Remove redundant failrues by heuristic!");
            seq = SequenceFilters.filterMightBeRepeatedFailingSequences(seq);
            System.out.println("Total number removed: " + (num - seq.size()));
        }
        System.out.printf("Writing %d junit tests%n", seq.size());
        JunitFileWriter jfw = new JunitFileWriter(output_dir, junit_package_name, junit_classname, testsperfile);
        List<File> files = jfw.createJunitFiles(seq);
        System.out.println();
        for (File f : files) {
            System.out.println("Created file: " + f.getAbsolutePath());
        }
    }

    /**
     * Returns a set of model covered classes
     * */
    private Set<Class<?>> getModelOwnerClasses(Map<Class<?>, ClassModel> models) {
        Set<Class<?>> set = new LinkedHashSet<Class<?>>();
        if (models != null) {
            for (ClassModel model : models.values()) {
                for (Transition t : model.getAllTransitions()) {
                    set.add(t.getOwnerClass());
                }
            }
            System.out.println("Add " + set.size() + " more classes");
        }
        return set;
    }

    /**
     * Read from the provided class file 
     * */
    public static List<Class<?>> readClassFromFile(String filePath) {
        List<Class<?>> classToTest = new LinkedList<Class<?>>();
        if (filePath != null) {
            System.out.println("Read tested class from file: " + filePath);
            String line = null;
            try {
                BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
                line = br.readLine();
                while (line != null) {
                    String className = line.trim();
                    if (!className.equals("") && !className.startsWith("#")) {
                        Class<?> clazz = Class.forName(className);
                        classToTest.add(clazz);
                    }
                    line = br.readLine();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Errors in reading class: " + line);
                e.printStackTrace();
            } catch (Error e) {
                System.err.println("Errors in reading class: " + line);
                e.printStackTrace();
            }
        }
        return classToTest;
    }
}
