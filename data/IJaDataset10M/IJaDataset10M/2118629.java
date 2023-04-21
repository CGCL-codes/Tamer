package com.justin.workaround;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import jess.Rete;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class JessRulePerformanceTest {

    private static String srcRoot;

    private static String packageName;

    private static String baseClassName;

    private static String baseRuleFileName;

    private static String baseRuleName;

    private static int ruleFilesNumber;

    private static int objectsInteration;

    private static int objectsNumber;

    private static int matchedRulesNumber;

    private static int rulesNumber;

    private static boolean deleteAllGeneratedAfterTest;

    private static boolean regenerateClasses;

    private static boolean regenerateRules;

    private static DynamicRuleGenerator drg = new DynamicRuleGenerator();

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @BeforeClass
    public static void prepare() throws Exception {
        initContext();
        srcRoot = new File(JessRulePerformanceTest.class.getClassLoader().getResource("").getFile()).getCanonicalPath().replaceAll("%20", " ").replace("bin", "src");
        if (regenerateClasses) {
            deleteClassFiles();
            genAndCompileClass(objectsNumber, packageName, baseClassName);
        }
        if (regenerateRules) {
            deleteRuleFiles();
            drg.generateRuleFile(srcRoot, packageName, baseClassName, baseRuleFileName, baseRuleName, objectsNumber, ruleFilesNumber, rulesNumber, matchedRulesNumber, "name", "testPersonName");
        }
    }

    @AfterClass
    public static void clearAll() {
        if (deleteAllGeneratedAfterTest) {
            deleteClassFiles();
            deleteRuleFiles();
        }
    }

    @Test
    public void theFirstTestCase() throws Exception {
        List datas = new ArrayList();
        for (int i = 0; i < objectsNumber; i++) {
            Class[] params = { String.class };
            Object[] paramsObj = { "testPersonName" };
            Class personType = Class.forName(packageName + "." + baseClassName + i);
            Method thisMethod = personType.getDeclaredMethod("setName", params);
            for (int j = 0; j < objectsInteration; j++) {
                Object iClass = personType.newInstance();
                thisMethod.invoke(iClass, paramsObj);
                datas.add(iClass);
            }
        }
        runTest(datas);
    }

    private void runTest(Collection datas) throws Exception {
        long timeStart = System.currentTimeMillis();
        Rete engine = new Rete();
        engine.reset();
        for (int i = 0; i < ruleFilesNumber; i++) {
            engine.batch(srcRoot + "\\" + baseRuleFileName + i + ".clp");
        }
        engine.addAll(datas);
        engine.run();
        long timeEnd = System.currentTimeMillis();
        System.out.println("This case cost time: " + (timeEnd - timeStart));
    }

    private static void genAndCompileClass(int classNumbers, String packageName, String className) throws Exception {
        DynamicClassGenerator dcg = new DynamicClassGenerator();
        for (int i = 0; i < classNumbers; i++) {
            dcg.createClass(srcRoot, packageName, className + i);
            dcg.compileClass(packageName, className + i + ".java", srcRoot.replaceAll("src", "bin"));
        }
    }

    private static void deleteClassFiles() {
        for (int i = 0; i < objectsNumber; i++) {
            deleteFile(srcRoot + "\\" + getFilePath(packageName, baseClassName + i + ".java"));
        }
    }

    private static void deleteRuleFiles() {
        for (int i = 0; i < ruleFilesNumber; i++) {
            deleteFile(srcRoot + "\\" + baseRuleFileName + i + ".clp");
        }
    }

    private static void deleteFile(String filePath) {
        new File(filePath).delete();
    }

    private static String getFilePath(String packageName, String classFileName) {
        return packageName.replaceAll("\\.", "\\\\") + "\\" + classFileName;
    }

    private static void initContext() throws Exception {
        Properties props = new Properties();
        InputStream is = JessRulePerformanceTest.class.getClassLoader().getResourceAsStream("JessRulePerformanceTestConfigure.properties");
        props.load(is);
        packageName = props.getProperty("packageName");
        baseClassName = props.getProperty("baseClassName");
        baseRuleFileName = props.getProperty("baseRuleFileName");
        baseRuleName = props.getProperty("baseRuleName");
        ruleFilesNumber = Integer.valueOf(props.getProperty("ruleFilesNumber"));
        objectsNumber = Integer.valueOf(props.getProperty("objectsNumber"));
        matchedRulesNumber = Integer.valueOf(props.getProperty("matchedRulesNumber"));
        rulesNumber = Integer.valueOf(props.getProperty("rulesNumber"));
        objectsInteration = Integer.valueOf(props.getProperty("objectsInteration"));
        deleteAllGeneratedAfterTest = Boolean.valueOf(props.getProperty("deleteAllGeneratedAfterTest"));
        regenerateClasses = Boolean.valueOf(props.getProperty("regenerateClasses"));
        regenerateRules = Boolean.valueOf(props.getProperty("regenerateRules"));
    }

    public void insertDatasIntoEngine(int objectNumber, int objectiterations) throws Exception {
        List datas = new ArrayList();
        for (int i = 0; i < objectNumber; i++) {
            Class[] params = { String.class };
            Object[] paramsObj = { "testPersonName" };
            Class personType = Class.forName(packageName + "." + baseClassName + i);
            Method thisMethod = personType.getDeclaredMethod("setName", params);
            for (int j = 0; j < objectiterations; j++) {
                Object iClass = personType.newInstance();
                thisMethod.invoke(iClass, paramsObj);
                datas.add(iClass);
            }
        }
        runTest(datas);
    }
}
