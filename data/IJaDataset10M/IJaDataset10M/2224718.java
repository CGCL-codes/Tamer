package whiteoak.tools.openjdk.compiler.testing;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.junit.Assert;
import org.junit.Ignore;
import whiteoak.lang.StructuralDispatcher;
import whiteoak.tools.openjdk.compiler.WocMain;
import com.sun.tools.javac.util.Pair;

public class CompilationDevice {

    private List<String> classPathElements = new ArrayList<String>();

    public CompilationDevice() {
        classPathElements.add("lib/junit-4.4.jar");
        classPathElements.add("bin");
    }

    public void addClassPath(String cp) {
        classPathElements.add(cp);
    }

    public void removeClassPath(String cp) {
        classPathElements.remove(cp);
    }

    public static class C {

        public void f() {
        }

        public void g() {
            f();
        }
    }

    private String binDirPath = "run/bin";

    private String srcDirPath = "run/src";

    private boolean cleanBinDir = true;

    private static final String failureStr = "/*WHITEOAK COMPILATION ERROR EXPECTED: ";

    private static final String outputStr = "/*OUTPUT: ";

    private static final String outputWithSpaceStr = "/* OUTPUT: ";

    public void setBinDirPath(String arg) {
        binDirPath = arg;
    }

    public void setSrcDirPath(String arg) {
        srcDirPath = arg;
    }

    public void setCleanDirDir(boolean arg) {
        cleanBinDir = arg;
    }

    public String getBinDirPath() {
        return binDirPath;
    }

    public static class DidNotCompile extends RuntimeException {

        /**
       * 
       */
        private static final long serialVersionUID = 3627980182187346105L;

        public final List<Err> errors;

        public DidNotCompile(String message, List<Err> errors_) {
            super(message);
            errors = errors_;
        }
    }

    public void compile(String folder_, String mainClass) {
        if (binDirPath != null) {
            File binDir = new File(binDirPath);
            if (cleanBinDir || !binDir.exists()) {
                binDir.delete();
                binDir.mkdirs();
            }
        }
        WocMain inst = new WocMain();
        String fullClassPath = "";
        for (String curr : classPathElements) {
            if (fullClassPath.length() > 0) fullClassPath += File.pathSeparator;
            fullClassPath += curr;
        }
        String folder = folder_;
        if (folder.length() > 0) folder += File.separator;
        String mainSourceFile = mainClass.replace('.', File.separatorChar) + ".java";
        File f = new File(srcDirPath + File.separator + folder + mainSourceFile).getAbsoluteFile();
        System.out.println("f=" + f);
        if (!f.exists()) mainSourceFile = mainClass.replace('.', File.separatorChar) + ".wo";
        List<String> list = new ArrayList<String>();
        if (binDirPath != null) {
            list.add("-d");
            list.add(binDirPath);
        }
        String[] addtionalArgs = { "-sourcepath", srcDirPath + File.separator + folder, "-cp", fullClassPath, srcDirPath + File.separator + folder + mainSourceFile };
        list.addAll(Arrays.asList(addtionalArgs));
        String[] args = list.toArray(new String[0]);
        System.out.println("args=" + Arrays.toString(args));
        System.out.println("Compiling: " + mainSourceFile);
        int exitValue = inst.compile(args);
        Iterable<String> classFiles = inst.getClassFiles();
        for (String s : classFiles) System.out.println("Generated class " + s);
        Iterable<Pair<Long, String>> pairs = inst.getErrors();
        List<Err> errors = toErrList(pairs);
        if (exitValue != 0) throw new DidNotCompile("Compilation of " + mainClass + " has failed (exit value=" + exitValue + ")", errors);
    }

    protected Class<?> loadCompiledClass(String mainClass) throws Exception {
        String[] cp = { "lib/junit-4.4.jar", binDirPath };
        List<URL> lst = new ArrayList<URL>();
        for (String tok : cp) {
            File f = new File(tok);
            URL url = f.toURI().toURL();
            lst.add(url);
        }
        URL[] urls = lst.toArray(new URL[0]);
        ClassLoader parent = ClassLoader.getSystemClassLoader();
        ClassLoader cl = new URLClassLoader(urls, parent);
        StructuralDispatcher.setClassLoader(cl);
        Class<?> c = cl.loadClass(mainClass);
        return c;
    }

    public static boolean isReady(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line = br.readLine();
            while (line != null) {
                if (line.contains(failureStr) || line.contains(outputStr) || line.contains(outputWithSpaceStr)) return true;
                line = br.readLine();
            }
        } catch (Exception e) {
            Assert.fail();
        }
        return false;
    }

    private List<String> getExpectedOutput(String mainClassName) {
        String mainSourceFile = "run/src/" + mainClassName.replace('.', '/') + ".java";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(mainSourceFile));
        } catch (FileNotFoundException e) {
            mainSourceFile = "run/src/" + mainClassName.replace('.', '/') + ".wo";
            try {
                br = new BufferedReader(new FileReader(mainSourceFile));
            } catch (FileNotFoundException e1) {
                Assert.fail();
            }
        }
        try {
            String line = br.readLine();
            List<String> output = new ArrayList<String>();
            while (line != null) {
                if (!line.startsWith(outputWithSpaceStr) && !line.startsWith(outputStr)) {
                    line = br.readLine();
                    continue;
                }
                while (!line.contains("*/")) {
                    String addLine = br.readLine();
                    if (addLine == null) return null;
                    line += addLine;
                }
                while (line.contains("\"")) {
                    line = line.substring(line.indexOf("\"") + 1);
                    if (!line.contains("\"")) break;
                    output.add(line.substring(0, line.indexOf("\"")));
                    line = line.substring(line.indexOf("\"") + 1);
                }
                return output;
            }
        } catch (FileNotFoundException e) {
            Assert.fail();
        } catch (IOException e) {
            Assert.fail();
        }
        return null;
    }

    private List<Err> getDeclaredErrors(String className) {
        String mainSourceFile = "run/src/" + className.replace('.', '/') + ".java";
        List<Pair<Long, String>> declaredErrors = new ArrayList<Pair<Long, String>>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(mainSourceFile));
        } catch (FileNotFoundException e) {
            mainSourceFile = "run/src/" + className.replace('.', '/') + ".wo";
            try {
                br = new BufferedReader(new FileReader(mainSourceFile));
            } catch (FileNotFoundException e1) {
                Assert.fail();
            }
        }
        try {
            String line = br.readLine();
            long lineCtr = 1;
            while (line != null) {
                while (line.contains(failureStr)) {
                    Long errLine = lineCtr;
                    String errMsg;
                    if (line.contains("*/")) errMsg = line.substring(line.indexOf(failureStr) + failureStr.length(), line.indexOf("*/")); else errMsg = line.substring(line.indexOf(failureStr) + failureStr.length());
                    while (line != null && !line.contains("*/")) {
                        line = br.readLine();
                        lineCtr++;
                        errMsg += "\n";
                        if (line.contains("*/")) errMsg += line.substring(0, line.indexOf("*/")); else errMsg += line;
                    }
                    declaredErrors.add(new Pair<Long, String>(errLine, errMsg));
                    line = line.substring(line.indexOf("*/") + 2);
                }
                line = br.readLine();
                lineCtr++;
            }
        } catch (IOException e) {
            Assert.fail();
        }
        return toErrList(declaredErrors);
    }

    private void run(String mainClassName, Object... expectedOutput) {
        try {
            Class<?> c = loadCompiledClass(mainClassName);
            run(c, expectedOutput);
        } catch (InvocationTargetException ite) {
            Throwable t = ite.getTargetException();
            t.printStackTrace();
            RuntimeException re = new RuntimeException(t);
            re.setStackTrace(t.getStackTrace());
            throw re;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            RuntimeException re = new RuntimeException(e);
            re.setStackTrace(e.getStackTrace());
            throw re;
        }
    }

    private void run(Class<?> c, Object... expectedOutput) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method m = c.getMethod("main", String[].class);
        PrintStream old = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        try {
            System.setOut(ps);
            m.invoke(null, new Object[] { new String[0] });
        } finally {
            System.setOut(old);
        }
        ps.flush();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        List<String> list = new ArrayList<String>();
        for (Scanner sc = new Scanner(bais); sc.hasNext(); ) list.add(sc.nextLine());
        String[] actual = list.toArray(new String[0]);
        String[] expectedStrings;
        List<String> expectedStringsList = getExpectedOutput(c.getCanonicalName());
        if (expectedStringsList != null) expectedStrings = expectedStringsList.toArray(new String[0]); else {
            expectedStrings = new String[expectedOutput.length];
            for (int i = 0; i < expectedOutput.length; ++i) expectedStrings[i] = "" + expectedOutput[i];
        }
        Assert.assertArrayEquals("actual=" + Arrays.toString(actual), expectedStrings, actual);
    }

    private static class Err {

        private final Pair<Long, String> pair;

        public Err(Pair<Long, String> pair_) {
            pair = pair_;
        }

        @Override
        public String toString() {
            return "Line " + pair.fst + " - " + pair.snd;
        }

        @Override
        public int hashCode() {
            return pair.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (this == o) return true;
            if (!this.getClass().equals(o.getClass())) return false;
            Err that = (Err) o;
            return this.pair.equals(that.pair);
        }
    }

    private static List<Err> toErrList(Iterable<Pair<Long, String>> pairs) {
        List<Err> result = new ArrayList<Err>();
        if (pairs == null) return result;
        for (Pair<Long, String> p : pairs) result.add(new Err(p));
        return result;
    }

    protected void compileAndRun(String folderName, String className, Object... expectedOutput) throws Exception {
        List<Err> expectedErrors = getDeclaredErrors(folderName + "." + className);
        try {
            compile("", folderName + "." + className);
        } catch (DidNotCompile e) {
            List<Err> actualErrors = e.errors;
            if (expectedErrors.isEmpty()) {
                Assert.fail("This program failed to compile: " + toStr(actualErrors));
                return;
            }
            for (Err err : actualErrors) {
                if (!expectedErrors.contains(err)) Assert.fail("An unanticipated error was issued: " + err); else expectedErrors.remove(err);
            }
            if (!expectedErrors.isEmpty()) {
                Assert.fail("Expected errors were not issued: " + toStr(expectedErrors));
            }
            return;
        }
        if (!expectedErrors.isEmpty()) {
            Assert.fail("This program should have failed with the following error(s):\n" + toStr(expectedErrors));
        }
        String classFileName = folderName + "." + className.replace('/', '.');
        if (loadCompiledClass(classFileName).isAnnotationPresent(Ignore.class)) return;
        try {
            loadCompiledClass(classFileName).getMethod("main", String[].class);
        } catch (NoSuchMethodException e) {
            return;
        }
        run(classFileName, expectedOutput);
    }

    private static String toStr(List<Err> errors) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        for (Err e : errors) pw.println(e);
        pw.flush();
        return sw.toString();
    }

    public List<Pair<String, Boolean>> compileAndRunDir(String filename) throws Exception {
        File file = new File(srcDirPath, filename);
        List<Pair<String, Boolean>> failedTests = new ArrayList<Pair<String, Boolean>>();
        if (file.isDirectory()) {
            String name = file.getName();
            if (name.endsWith("benchmarking")) return failedTests;
        }
        if (!file.isDirectory() && !filename.endsWith(".java") && !filename.endsWith(".wo")) return failedTests;
        if (!file.isDirectory()) {
            String type = filename.endsWith(".java") ? ".java" : ".wo";
            Pair<String, Boolean> failedTest = new Pair<String, Boolean>(filename, isReady("run/src/" + filename.substring(0, filename.indexOf(type)).replace('.', '/') + type));
            try {
                String folderName = filename.substring(0, filename.indexOf("/"));
                String className = filename.substring(filename.indexOf("/") + 1, filename.indexOf(type));
                compileAndRun(folderName, className);
            } catch (Exception e) {
                failedTests.add(failedTest);
            } catch (Error e) {
                failedTests.add(failedTest);
            }
            return failedTests;
        }
        for (String childName : file.list()) failedTests.addAll(compileAndRunDir(filename + "/" + childName));
        return failedTests;
    }
}
