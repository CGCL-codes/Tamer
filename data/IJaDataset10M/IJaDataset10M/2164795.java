package org.crap4j;

import com.agitar.lib.junit.AgitarTestCase;
import com.agitar.lib.mockingbird.Mockingbird;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OptionsAgitarTest extends AgitarTestCase {

    public Class getTargetClass() {
        return Options.class;
    }

    public void testConstructor() throws Throwable {
        Options options = new Options();
        assertTrue("options.getDownloadAverages()", options.getDownloadAverages());
    }

    public void testCheckAssignment() throws Throwable {
        Options options = new Options();
        callPrivateMethod("org.crap4j.Options", "checkAssignment", new Class[] { int.class, String.class }, options, new Object[] { new Integer(100), "=5)cqp*KW6)nk IY~6m.SFW1kRo4T<K8 " });
        assertFalse("options.valid()", options.valid());
    }

    public void testGetClassDirs() throws Throwable {
        AbstractList result = (AbstractList) new Options().getClassDirs();
        assertEquals("result.size()", 0, result.size());
    }

    public void testGetClassDirs1() throws Throwable {
        Options options = new Options();
        options.setClassDirs("testOptionsParam1");
        AbstractList result = (AbstractList) options.getClassDirs();
        assertEquals("result.size()", 1, result.size());
        Object actual = callPrivateMethod("java.util.List", "get", new Class[] { int.class }, result, new Object[] { new Integer(0) });
        assertEquals("(AbstractList) result.get(0)", "testOptionsParam1", actual);
    }

    public void testGetLibClasspaths() throws Throwable {
        AbstractList result = (AbstractList) new Options().getLibClasspaths();
        assertEquals("result.size()", 0, result.size());
    }

    public void testGetLibClasspaths1() throws Throwable {
        Options options = new Options();
        options.setLibClasspaths("testOptionsParam1");
        AbstractList result = (AbstractList) options.getLibClasspaths();
        assertEquals("result.size()", 1, result.size());
        Object actual = callPrivateMethod("java.util.List", "get", new Class[] { int.class }, result, new Object[] { new Integer(0) });
        assertEquals("(AbstractList) result.get(0)", "testOptionsParam1", actual);
    }

    public void testGetLinesFromConfigFileWithAggressiveMocks() throws Throwable {
        Options options = (Options) Mockingbird.getProxyObject(Options.class, true);
        Mockingbird.enterRecordingMode();
        File file = (File) Mockingbird.getProxyObject(File.class);
        Mockingbird.replaceObjectForRecording(File.class, "<init>(java.lang.String)", file);
        setPrivateField(options, "configFile", "");
        Mockingbird.setReturnValue(false, file, "exists", "()boolean", new Object[] {}, Boolean.TRUE, 1);
        Mockingbird.replaceObjectForRecording(FileReader.class, "<init>(java.io.File)", Mockingbird.getProxyObject(FileReader.class));
        BufferedReader bufferedReader = (BufferedReader) Mockingbird.getProxyObject(BufferedReader.class);
        Mockingbird.replaceObjectForRecording(BufferedReader.class, "<init>(java.io.Reader)", bufferedReader);
        ArrayList arrayList = (ArrayList) Mockingbird.getProxyObject(ArrayList.class);
        Mockingbird.replaceObjectForRecording(ArrayList.class, "<init>()", arrayList);
        Mockingbird.setReturnValue(false, bufferedReader, "readLine", "()java.lang.String", new Object[] {}, "", 1);
        Boolean boolean2 = Boolean.FALSE;
        Mockingbird.setReturnValue(false, arrayList, "add", "(java.lang.Object)boolean", boolean2, 1);
        Mockingbird.setReturnValue(false, bufferedReader, "readLine", "()java.lang.String", new Object[] {}, "", 1);
        Mockingbird.setReturnValue(false, arrayList, "add", "(java.lang.Object)boolean", boolean2, 1);
        Mockingbird.setReturnValue(false, bufferedReader, "readLine", "()java.lang.String", new Object[] {}, null, 1);
        Mockingbird.setReturnValue(false, bufferedReader, "close", "()void", new Object[] {}, null, 1);
        Mockingbird.enterTestMode(Options.class);
        List result = (List) callPrivateMethod("org.crap4j.Options", "getLinesFromConfigFile", new Class[] {}, options, new Object[] {});
        assertNotNull("result", result);
    }

    public void testGetServer() throws Throwable {
        Options options = new Options();
        options.setServer("");
        String result = options.getServer();
        assertEquals("options.server", "http://www.crap4j.org/benchmark/", getPrivateField(options, "server"));
        assertEquals("result", "http://www.crap4j.org/benchmark/", result);
    }

    public void testGetServer1() throws Throwable {
        Options options = new Options();
        options.setServer(" ");
        String result = options.getServer();
        assertEquals("result", " ", result);
    }

    public void testGetServer2() throws Throwable {
        Options options = new Options();
        String result = options.getServer();
        assertEquals("options.server", "http://www.crap4j.org/benchmark/", getPrivateField(options, "server"));
        assertEquals("result", "http://www.crap4j.org/benchmark/", result);
    }

    public void testGetSourceDirs() throws Throwable {
        AbstractList result = (AbstractList) new Options().getSourceDirs();
        assertEquals("result.size()", 0, result.size());
    }

    public void testGetSourceDirs1() throws Throwable {
        Options options = new Options();
        options.setSourceDirs("testOptionsParam1");
        AbstractList result = (AbstractList) options.getSourceDirs();
        assertEquals("result.size()", 1, result.size());
        Object actual = callPrivateMethod("java.util.List", "get", new Class[] { int.class }, result, new Object[] { new Integer(0) });
        assertEquals("(AbstractList) result.get(0)", "testOptionsParam1", actual);
    }

    public void testGetTestClassDirs() throws Throwable {
        Options options = new Options();
        options.setTestClassDirs("testOptionsParam1");
        AbstractList result = (AbstractList) options.getTestClassDirs();
        assertEquals("result.size()", 1, result.size());
        Object actual = callPrivateMethod("java.util.List", "get", new Class[] { int.class }, result, new Object[] { new Integer(0) });
        assertEquals("(AbstractList) result.get(0)", "testOptionsParam1", actual);
    }

    public void testGetTestClassDirs1() throws Throwable {
        AbstractList result = (AbstractList) new Options().getTestClassDirs();
        assertEquals("result.size()", 0, result.size());
    }

    public void testIsAssignment() throws Throwable {
        Options options = new Options();
        boolean result = ((Boolean) callPrivateMethod("org.crap4j.Options", "isAssignment", new Class[] { String.class }, options, new Object[] { "=}C`kJ-(0yQD9?~Y" })).booleanValue();
        assertTrue("result", result);
    }

    public void testIsAssignment1() throws Throwable {
        Options options = new Options();
        boolean result = ((Boolean) callPrivateMethod("org.crap4j.Options", "isAssignment", new Class[] { String.class }, options, new Object[] { "testOptionsString" })).booleanValue();
        assertFalse("result", result);
    }

    public void testIsBlank() throws Throwable {
        Options options = new Options();
        boolean result = ((Boolean) callPrivateMethod("org.crap4j.Options", "isBlank", new Class[] { String.class }, options, new Object[] { "" })).booleanValue();
        assertTrue("result", result);
    }

    public void testIsBlank1() throws Throwable {
        Options options = new Options();
        boolean result = ((Boolean) callPrivateMethod("org.crap4j.Options", "isBlank", new Class[] { String.class }, options, new Object[] { "testOptionsString" })).booleanValue();
        assertFalse("result", result);
    }

    public void testIsComment() throws Throwable {
        Options options = new Options();
        boolean result = ((Boolean) callPrivateMethod("org.crap4j.Options", "isComment", new Class[] { String.class }, options, new Object[] { "testOptionsString" })).booleanValue();
        assertFalse("result", result);
    }

    public void testIsEmpty() throws Throwable {
        Options options = new Options();
        boolean result = ((Boolean) callPrivateMethod("org.crap4j.Options", "isEmpty", new Class[] { String.class, String.class }, options, new Object[] { "testOptionsProperty", "testOptionsValue" })).booleanValue();
        assertFalse("result", result);
    }

    public void testIsEmpty1() throws Throwable {
        Options options = new Options();
        boolean result = ((Boolean) callPrivateMethod("org.crap4j.Options", "isEmpty", new Class[] { String.class, String.class }, options, new Object[] { null, "testOptionsValue" })).booleanValue();
        assertTrue("result", result);
    }

    public void testIsEmpty2() throws Throwable {
        Options options = new Options();
        boolean result = ((Boolean) callPrivateMethod("org.crap4j.Options", "isEmpty", new Class[] { String.class, String.class }, options, new Object[] { "testOptionsProperty", null })).booleanValue();
        assertTrue("result", result);
    }

    public void testJoin() throws Throwable {
        List arrayList = new ArrayList(100);
        arrayList.add("testString");
        arrayList.add(" > ");
        String result = new Options().join(arrayList);
        assertEquals("result", "testString: > ", result);
    }

    public void testJoin1() throws Throwable {
        String result = new Options().join(new ArrayList(100));
        assertEquals("result", "", result);
    }

    public void testJoin2() throws Throwable {
        List arrayList = new ArrayList(100);
        arrayList.add("M: ");
        String result = new Options().join(arrayList);
        assertEquals("result", "M: ", result);
    }

    public void testParseLine() throws Throwable {
        Options options = new Options();
        String[] result = (String[]) callPrivateMethod("org.crap4j.Options", "parseLine", new Class[] { String.class }, options, new Object[] { "o0s,k-aLX=pr2isIA \r28wMX:_>B;z^,:mt@cG=6" });
        assertEquals("result.length", 2, result.length);
        assertNull("(String[]) result[0]", ((String[]) result)[0]);
    }

    public void testParseLine1() throws Throwable {
        Options options = new Options();
        String[] result = (String[]) callPrivateMethod("org.crap4j.Options", "parseLine", new Class[] { String.class }, options, new Object[] { "=r" });
        assertEquals("result.length", 2, result.length);
        assertEquals("(String[]) result[0]", "", ((String[]) result)[0]);
    }

    public void testParseLine2() throws Throwable {
        Options options = new Options();
        String[] result = (String[]) callPrivateMethod("org.crap4j.Options", "parseLine", new Class[] { String.class }, options, new Object[] { "testOptionsLine" });
        assertEquals("result.length", 2, result.length);
        assertNull("(String[]) result[0]", ((String[]) result)[0]);
    }

    public void testParseLines() throws Throwable {
        Options options = new Options();
        List arrayList = new ArrayList(100);
        arrayList.add("");
        callPrivateMethod("org.crap4j.Options", "parseLines", new Class[] { List.class }, options, new Object[] { arrayList });
        assertNull("options.getProjectDir()", options.getProjectDir());
        assertNull("options.libClasspaths", getPrivateField(options, "libClasspaths"));
        assertNull("options.testClassDirs", getPrivateField(options, "testClassDirs"));
        assertNull("options.classDirs", getPrivateField(options, "classDirs"));
        assertNull("options.sourceDirs", getPrivateField(options, "sourceDirs"));
        assertEquals("(ArrayList) arrayList.size()", 1, arrayList.size());
    }

    public void testParseLines1() throws Throwable {
        Options options = new Options();
        List arrayList = new ArrayList(100);
        callPrivateMethod("org.crap4j.Options", "parseLines", new Class[] { List.class }, options, new Object[] { arrayList });
        assertNull("options.getProjectDir()", options.getProjectDir());
        assertNull("options.libClasspaths", getPrivateField(options, "libClasspaths"));
        assertNull("options.testClassDirs", getPrivateField(options, "testClassDirs"));
        assertNull("options.classDirs", getPrivateField(options, "classDirs"));
        assertNull("options.sourceDirs", getPrivateField(options, "sourceDirs"));
        assertEquals("(ArrayList) arrayList.size()", 0, arrayList.size());
    }

    public void testReadConfigFileWithAggressiveMocks() throws Throwable {
        Options options = (Options) Mockingbird.getProxyObject(Options.class, true);
        Mockingbird.enterRecordingMode();
        File file = (File) Mockingbird.getProxyObject(File.class);
        Mockingbird.replaceObjectForRecording(File.class, "<init>(java.lang.String)", file);
        IOException iOException = (IOException) Mockingbird.getProxyObject(IOException.class);
        IOException iOException2 = (IOException) Mockingbird.getProxyObject(IOException.class);
        Iterator iterator = (Iterator) Mockingbird.getProxyObject(Iterator.class);
        String[] strings = new String[2];
        String[] strings2 = new String[2];
        setPrivateField(options, "configFile", "");
        strings[0] = "";
        strings[1] = "";
        strings2[0] = "";
        strings2[1] = "";
        Mockingbird.setReturnValue(false, options, "checkConfigFileExists", "(java.io.File)void", new Object[] { file }, null, 1);
        Mockingbird.replaceObjectForRecording(FileReader.class, "<init>(java.io.File)", Mockingbird.getProxyObject(FileReader.class));
        BufferedReader bufferedReader = (BufferedReader) Mockingbird.getProxyObject(BufferedReader.class);
        Mockingbird.replaceObjectForRecording(BufferedReader.class, "<init>(java.io.Reader)", bufferedReader);
        ArrayList arrayList = (ArrayList) Mockingbird.getProxyObject(ArrayList.class);
        Mockingbird.replaceObjectForRecording(ArrayList.class, "<init>()", arrayList);
        Mockingbird.setReturnValue(false, bufferedReader, "readLine", "()java.lang.String", new Object[] {}, "", 1);
        Boolean boolean2 = Boolean.FALSE;
        Mockingbird.setReturnValue(false, arrayList, "add", "(java.lang.Object)boolean", boolean2, 1);
        Mockingbird.setReturnValue(false, bufferedReader, "readLine", "()java.lang.String", new Object[] {}, "", 1);
        Mockingbird.setReturnValue(false, arrayList, "add", "(java.lang.Object)boolean", boolean2, 1);
        Mockingbird.setException(false, bufferedReader, "readLine", "()java.lang.String", new Object[] {}, iOException, 1);
        Mockingbird.setReturnValue(false, iOException, "printStackTrace", "()void", new Object[] {}, null, 1);
        Mockingbird.setException(false, bufferedReader, "close", "()void", new Object[] {}, iOException2, 1);
        Mockingbird.setReturnValue(false, iOException2, "printStackTrace", "()void", new Object[] {}, null, 1);
        Mockingbird.setReturnValue(arrayList.iterator(), iterator);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), "");
        Boolean boolean3 = Boolean.TRUE;
        Mockingbird.setReturnValue(false, options, "isComment", "(java.lang.String)boolean", boolean3, 1);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), "");
        Mockingbird.setReturnValue(false, options, "isComment", "(java.lang.String)boolean", boolean3, 1);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), "");
        Mockingbird.setReturnValue(false, options, "isComment", "(java.lang.String)boolean", boolean2, 1);
        Mockingbird.setReturnValue(false, options, "isBlank", "(java.lang.String)boolean", boolean3, 1);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), "");
        Mockingbird.setReturnValue(false, options, "isComment", "(java.lang.String)boolean", boolean2, 1);
        Mockingbird.setReturnValue(false, options, "isBlank", "(java.lang.String)boolean", boolean2, 1);
        Mockingbird.setReturnValue(false, options, "checkAssignment", "(int,java.lang.String)void", null, 1);
        Mockingbird.setReturnValue(false, options, "parseLine", "(java.lang.String)java.lang.String[]", strings, 1);
        Mockingbird.setReturnValue(false, options, "setProperty", "(java.lang.String,java.lang.String)void", null, 1);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), "");
        Mockingbird.setReturnValue(false, options, "isComment", "(java.lang.String)boolean", boolean2, 1);
        Mockingbird.setReturnValue(false, options, "isBlank", "(java.lang.String)boolean", boolean2, 1);
        Mockingbird.setReturnValue(false, options, "checkAssignment", "(int,java.lang.String)void", null, 1);
        Mockingbird.setReturnValue(false, options, "parseLine", "(java.lang.String)java.lang.String[]", strings2, 1);
        Mockingbird.setReturnValue(false, options, "setProperty", "(java.lang.String,java.lang.String)void", null, 1);
        Mockingbird.setReturnValue(iterator.hasNext(), true);
        Mockingbird.setReturnValue(iterator.next(), "");
        Mockingbird.setReturnValue(false, options, "isComment", "(java.lang.String)boolean", boolean2, 1);
        Mockingbird.setReturnValue(false, options, "isBlank", "(java.lang.String)boolean", boolean3, 1);
        Mockingbird.setReturnValue(iterator.hasNext(), false);
        Mockingbird.enterTestMode(Options.class);
        options.readConfigFile();
        assertTrue("Test call resulted in expected outcome", true);
    }

    public void testReturnAsList() throws Throwable {
        Options options = new Options();
        AbstractList result = (AbstractList) callPrivateMethod("org.crap4j.Options", "returnAsList", new Class[] { String.class }, options, new Object[] { null });
        assertEquals("result.size()", 0, result.size());
    }

    public void testReturnAsList1() throws Throwable {
        Options options = new Options();
        AbstractList result = (AbstractList) callPrivateMethod("org.crap4j.Options", "returnAsList", new Class[] { String.class }, options, new Object[] { "testOptionsProp" });
        assertEquals("result.size()", 1, result.size());
        Object actual = callPrivateMethod("java.util.List", "get", new Class[] { int.class }, result, new Object[] { new Integer(0) });
        assertEquals("(AbstractList) result.get(0)", "testOptionsProp", actual);
    }

    public void testSetClassDirs() throws Throwable {
        Options options = new Options();
        options.setClassDirs("testOptionsParam1");
        assertEquals("options.classDirs", "testOptionsParam1", getPrivateField(options, "classDirs"));
    }

    public void testSetConfigFile() throws Throwable {
        Options options = new Options();
        options.setConfigFile(null);
        assertNull("options.getConfigFile()", options.getConfigFile());
    }

    public void testSetDebug() throws Throwable {
        Options options = new Options();
        options.setDebug(true);
        assertTrue("options.getDebug()", options.getDebug());
    }

    public void testSetDontTest() throws Throwable {
        Options options = new Options();
        options.setDontTest(true);
        assertTrue("options.getDontTest()", options.getDontTest());
    }

    public void testSetDownloadAverages() throws Throwable {
        Options options = new Options();
        options.setDownloadAverages(true);
        assertTrue("options.getDownloadAverages()", options.getDownloadAverages());
    }

    public void testSetLibClasspaths() throws Throwable {
        Options options = new Options();
        options.setLibClasspaths("testOptionsParam1");
        assertEquals("options.libClasspaths", "testOptionsParam1", getPrivateField(options, "libClasspaths"));
    }

    public void testSetOutputDir() throws Throwable {
        Options options = new Options();
        options.setOutputDir("testOptionsParam1");
        assertEquals("options.getOutputDir()", "testOptionsParam1", options.getOutputDir());
    }

    public void testSetProjectDir() throws Throwable {
        Options options = new Options();
        options.setProjectDir("testOptionsParam1");
        assertEquals("options.getProjectDir()", "testOptionsParam1", options.getProjectDir());
    }

    public void testSetProperty() throws Throwable {
        Options options = new Options();
        options.setProperty("sourceDirs", "testOptionsParam2");
        assertEquals("options.sourceDirs", "testOptionsParam2", getPrivateField(options, "sourceDirs"));
    }

    public void testSetProperty1() throws Throwable {
        Options options = new Options();
        options.setProperty("projectDir", "testOptionsParam2");
        assertEquals("options.getProjectDir()", "testOptionsParam2", options.getProjectDir());
    }

    public void testSetProperty2() throws Throwable {
        Options options = new Options();
        options.setProperty(null, "testOptionsParam2");
        assertNull("options.getProjectDir()", options.getProjectDir());
        assertNull("options.classDirs", getPrivateField(options, "classDirs"));
        assertNull("options.testClassDirs", getPrivateField(options, "testClassDirs"));
        assertNull("options.libClasspaths", getPrivateField(options, "libClasspaths"));
        assertNull("options.sourceDirs", getPrivateField(options, "sourceDirs"));
    }

    public void testSetProperty3() throws Throwable {
        Options options = new Options();
        options.setProperty("libClasspaths", "testOptionsParam2");
        assertEquals("options.libClasspaths", "testOptionsParam2", getPrivateField(options, "libClasspaths"));
    }

    public void testSetProperty4() throws Throwable {
        Options options = new Options();
        options.setProperty("testOptionsParam1", null);
        assertNull("options.getProjectDir()", options.getProjectDir());
        assertNull("options.classDirs", getPrivateField(options, "classDirs"));
        assertNull("options.testClassDirs", getPrivateField(options, "testClassDirs"));
        assertNull("options.libClasspaths", getPrivateField(options, "libClasspaths"));
        assertNull("options.sourceDirs", getPrivateField(options, "sourceDirs"));
    }

    public void testSetProperty5() throws Throwable {
        Options options = new Options();
        options.setProperty("classDirs", "testOptionsParam2");
        assertEquals("options.classDirs", "testOptionsParam2", getPrivateField(options, "classDirs"));
    }

    public void testSetProperty6() throws Throwable {
        Options options = new Options();
        options.setProperty("testClassDirs", "testOptionsParam2");
        assertEquals("options.testClassDirs", "testOptionsParam2", getPrivateField(options, "testClassDirs"));
    }

    public void testSetServer() throws Throwable {
        Options options = new Options();
        options.setServer("testOptionsParam1");
        assertEquals("options.server", "testOptionsParam1", getPrivateField(options, "server"));
    }

    public void testSetSourceDirs() throws Throwable {
        Options options = new Options();
        options.setSourceDirs("testOptionsParam1");
        assertEquals("options.sourceDirs", "testOptionsParam1", getPrivateField(options, "sourceDirs"));
    }

    public void testSetTestClassDirs() throws Throwable {
        Options options = new Options();
        options.setTestClassDirs("testOptionsParam1");
        assertEquals("options.testClassDirs", "testOptionsParam1", getPrivateField(options, "testClassDirs"));
    }

    public void testToString() throws Throwable {
        String result = new Options().toString();
        assertEquals("result", "-p null -s  -c  -t  -l  -o null -debug false -dontTest false", result);
    }

    public void testToString1() throws Throwable {
        Options options = new Options();
        options.setClassDirs("testOptionsClassDirs");
        String result = options.toString();
        assertEquals("result", "-p null -s  -c testOptionsClassDirs -t  -l  -o null -debug false -dontTest false", result);
    }

    public void testToString2() throws Throwable {
        Options options = new Options();
        options.setClassDirs("testOptionsClassDirs");
        options.setTestClassDirs("testOptionsTestClassDirs");
        options.setLibClasspaths("testOptionsLibClasspaths");
        options.setSourceDirs("testOptionsSourceDirs");
        String result = options.toString();
        assertEquals("result", "-p null -s testOptionsSourceDirs -c testOptionsClassDirs -t testOptionsTestClassDirs -l testOptionsLibClasspaths -o null -debug false -dontTest false", result);
    }

    public void testToStringWithAggressiveMocks() throws Throwable {
        Options options = (Options) Mockingbird.getProxyObject(Options.class, true);
        List list = (List) Mockingbird.getProxyObject(List.class);
        Iterator iterator = (Iterator) Mockingbird.getProxyObject(Iterator.class);
        List list2 = (List) Mockingbird.getProxyObject(List.class);
        Iterator iterator2 = (Iterator) Mockingbird.getProxyObject(Iterator.class);
        List list3 = (List) Mockingbird.getProxyObject(List.class);
        Iterator iterator3 = (Iterator) Mockingbird.getProxyObject(Iterator.class);
        List list4 = (List) Mockingbird.getProxyObject(List.class);
        Iterator iterator4 = (Iterator) Mockingbird.getProxyObject(Iterator.class);
        setPrivateField(options, "configFile", "");
        options.setProjectDir("");
        options.setSourceDirs("");
        options.setClassDirs("");
        options.setTestClassDirs("");
        options.setLibClasspaths("");
        options.setOutputDir("");
        options.setDebug(false);
        options.setDontTest(false);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(false, options, "returnAsList", "(java.lang.String)java.util.List", list, 1);
        Mockingbird.setReturnValue(list.iterator(), iterator);
        Mockingbird.setReturnValue(iterator.hasNext(), false);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(StringBuffer.class), "toString", "()java.lang.String", "", 1);
        Mockingbird.setReturnValue(false, options, "returnAsList", "(java.lang.String)java.util.List", list2, 1);
        Mockingbird.setReturnValue(list2.iterator(), iterator2);
        Mockingbird.setReturnValue(iterator2.hasNext(), false);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(StringBuffer.class), "toString", "()java.lang.String", "", 1);
        Mockingbird.setReturnValue(false, options, "returnAsList", "(java.lang.String)java.util.List", list3, 1);
        Mockingbird.setReturnValue(list3.iterator(), iterator3);
        Mockingbird.setReturnValue(iterator3.hasNext(), true);
        Mockingbird.setReturnValue(iterator3.next(), "");
        Mockingbird.setReturnValue(iterator3.hasNext(), false);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(StringBuffer.class), "toString", "()java.lang.String", "", 1);
        Mockingbird.setReturnValue(false, options, "returnAsList", "(java.lang.String)java.util.List", list4, 1);
        Mockingbird.setReturnValue(list4.iterator(), iterator4);
        Mockingbird.setReturnValue(iterator4.hasNext(), true);
        Mockingbird.setReturnValue(iterator4.next(), "");
        Mockingbird.setReturnValue(iterator4.hasNext(), false);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(StringBuffer.class), "toString", "()java.lang.String", "", 1);
        Mockingbird.setReturnValue(false, Mockingbird.getProxyObject(StringBuilder.class), "toString", "()java.lang.String", "", 1);
        Mockingbird.enterTestMode(Options.class);
        String result = options.toString();
        assertEquals("result", "", result);
    }

    public void testValid() throws Throwable {
        Options options = new Options();
        options.setClassDirs("");
        options.setProjectDir("testOptionsParam1");
        boolean result = options.valid();
        assertFalse("result", result);
    }

    public void testValid1() throws Throwable {
        Options options = new Options();
        options.setClassDirs(" ");
        options.setProjectDir("testOptionsParam1");
        boolean result = options.valid();
        assertTrue("result", result);
    }

    public void testValid2() throws Throwable {
        boolean result = new Options().valid();
        assertFalse("result", result);
    }

    public void testCheckAssignmentThrowsIllegalArgumentException() throws Throwable {
        Options options = new Options();
        try {
            callPrivateMethod("org.crap4j.Options", "checkAssignment", new Class[] { int.class, String.class }, options, new Object[] { new Integer(100), "testOptionsLine" });
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ex.getMessage()", "Config file is not well-formed. Bad line #(100): testOptionsLine.", ex.getMessage());
            assertThrownBy(Options.class, ex);
        }
    }

    public void testCheckAssignmentThrowsNullPointerException() throws Throwable {
        String[] args = new String[2];
        args[0] = " :9 ";
        Options args2 = Main.parseArgs(args);
        try {
            callPrivateMethod("org.crap4j.Options", "checkAssignment", new Class[] { int.class, String.class }, args2, new Object[] { new Integer(100), null });
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(Options.class, ex);
        }
    }

    public void testCheckConfigFileExistsThrowsFileNotFoundException() throws Throwable {
        File file = new File("testOptionsParam1");
        Options options = new Options();
        try {
            callPrivateMethod("org.crap4j.Options", "checkConfigFileExists", new Class[] { File.class }, options, new Object[] { file });
            fail("Expected FileNotFoundException to be thrown");
        } catch (FileNotFoundException ex) {
            assertEquals("ex.getClass()", FileNotFoundException.class, ex.getClass());
            assertThrownBy(Options.class, ex);
            assertEquals("file.getName()", "testOptionsParam1", file.getName());
        }
    }

    public void testCheckConfigFileExistsThrowsNullPointerException() throws Throwable {
        Options options = new Options();
        try {
            callPrivateMethod("org.crap4j.Options", "checkConfigFileExists", new Class[] { File.class }, options, new Object[] { null });
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(Options.class, ex);
        }
    }

    public void testGetLinesFromConfigFileThrowsFileNotFoundException() throws Throwable {
        Options options = new Options();
        Mockingbird.enterRecordingMode();
        File file = (File) Mockingbird.getProxyObject(File.class);
        Mockingbird.replaceObjectForRecording(File.class, "<init>(java.lang.String)", file);
        Mockingbird.setReturnValue(false, file, "exists", "()boolean", new Object[] {}, Boolean.FALSE, 1);
        Mockingbird.enterTestMode(Options.class);
        try {
            callPrivateMethod("org.crap4j.Options", "getLinesFromConfigFile", new Class[] {}, options, new Object[] {});
            fail("Expected FileNotFoundException to be thrown");
        } catch (FileNotFoundException ex) {
            assertEquals("ex.getClass()", FileNotFoundException.class, ex.getClass());
            assertThrownBy(Options.class, ex);
        }
    }

    public void testGetLinesFromConfigFileThrowsNullPointerException() throws Throwable {
        Options options = new Options();
        Mockingbird.enterRecordingMode();
        File file = (File) Mockingbird.getProxyObject(File.class);
        Mockingbird.replaceObjectForRecording(File.class, "<init>(java.lang.String)", file);
        Mockingbird.setReturnValue(false, file, "exists", "()boolean", new Object[] {}, Boolean.TRUE, 1);
        Mockingbird.setConstructorForException(FileReader.class, "<init>(java.io.File)", (Throwable) Mockingbird.getProxyObject(FileNotFoundException.class));
        Mockingbird.enterTestMode(Options.class);
        try {
            callPrivateMethod("org.crap4j.Options", "getLinesFromConfigFile", new Class[] {}, options, new Object[] {});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(Options.class, ex);
        }
    }

    public void testGetLinesFromConfigFileThrowsNullPointerException1() throws Throwable {
        Options options = new Options();
        try {
            callPrivateMethod("org.crap4j.Options", "getLinesFromConfigFile", new Class[] {}, options, new Object[] {});
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(File.class, ex);
        }
    }

    public void testIsAssignmentThrowsNullPointerException() throws Throwable {
        Options options = new Options();
        try {
            callPrivateMethod("org.crap4j.Options", "isAssignment", new Class[] { String.class }, options, new Object[] { null });
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(Options.class, ex);
        }
    }

    public void testIsBlankThrowsNullPointerException() throws Throwable {
        Options options = new Options();
        try {
            callPrivateMethod("org.crap4j.Options", "isBlank", new Class[] { String.class }, options, new Object[] { null });
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(Options.class, ex);
        }
    }

    public void testIsCommentThrowsNullPointerException() throws Throwable {
        String[] args = new String[0];
        Options args2 = Main.parseArgs(args);
        try {
            callPrivateMethod("org.crap4j.Options", "isComment", new Class[] { String.class }, args2, new Object[] { null });
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(Options.class, ex);
        }
    }

    public void testJoinThrowsNullPointerException() throws Throwable {
        try {
            new Options().join(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(Options.class, ex);
        }
    }

    public void testParseLineThrowsNullPointerException() throws Throwable {
        Options options = new Options();
        try {
            callPrivateMethod("org.crap4j.Options", "parseLine", new Class[] { String.class }, options, new Object[] { null });
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(Options.class, ex);
        }
    }

    public void testParseLinesThrowsIllegalArgumentException() throws Throwable {
        String[] args = new String[2];
        args[0] = "1";
        Options args2 = Main.parseArgs(args);
        List arrayList = new ArrayList(100);
        arrayList.add("=T");
        try {
            callPrivateMethod("org.crap4j.Options", "parseLines", new Class[] { List.class }, args2, new Object[] { arrayList });
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ex.getMessage()", "Unknown property in ConfigFile: , with value:T", ex.getMessage());
            assertThrownBy(Options.class, ex);
            assertNull("args2.getProjectDir()", args2.getProjectDir());
            assertNull("args2.libClasspaths", getPrivateField(args2, "libClasspaths"));
            assertNull("args2.testClassDirs", getPrivateField(args2, "testClassDirs"));
            assertNull("args2.classDirs", getPrivateField(args2, "classDirs"));
            assertNull("args2.sourceDirs", getPrivateField(args2, "sourceDirs"));
            assertEquals("(ArrayList) arrayList.size()", 1, arrayList.size());
        }
    }

    public void testParseLinesThrowsIllegalArgumentException1() throws Throwable {
        Options options = new Options();
        List arrayList = new ArrayList(100);
        arrayList.add("=");
        arrayList.add("testString");
        try {
            callPrivateMethod("org.crap4j.Options", "parseLines", new Class[] { List.class }, options, new Object[] { arrayList });
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ex.getMessage()", "Config file is not well-formed. Bad line #(2): testString.", ex.getMessage());
            assertThrownBy(Options.class, ex);
            assertNull("options.getProjectDir()", options.getProjectDir());
            assertNull("options.libClasspaths", getPrivateField(options, "libClasspaths"));
            assertNull("options.testClassDirs", getPrivateField(options, "testClassDirs"));
            assertNull("options.classDirs", getPrivateField(options, "classDirs"));
            assertNull("options.sourceDirs", getPrivateField(options, "sourceDirs"));
            assertEquals("(ArrayList) arrayList.size()", 2, arrayList.size());
        }
    }

    public void testParseLinesThrowsIllegalArgumentException2() throws Throwable {
        Options options = new Options();
        List arrayList = new ArrayList(100);
        arrayList.add("AkV?]l3K5+c7MQv[i>\fi=");
        arrayList.add("testString");
        try {
            callPrivateMethod("org.crap4j.Options", "parseLines", new Class[] { List.class }, options, new Object[] { arrayList });
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ex.getMessage()", "Config file is not well-formed. Bad line #(2): testString.", ex.getMessage());
            assertThrownBy(Options.class, ex);
            assertNull("options.getProjectDir()", options.getProjectDir());
            assertNull("options.libClasspaths", getPrivateField(options, "libClasspaths"));
            assertNull("options.testClassDirs", getPrivateField(options, "testClassDirs"));
            assertNull("options.classDirs", getPrivateField(options, "classDirs"));
            assertNull("options.sourceDirs", getPrivateField(options, "sourceDirs"));
            assertEquals("(ArrayList) arrayList.size()", 2, arrayList.size());
        }
    }

    public void testParseLinesThrowsIllegalArgumentException3() throws Throwable {
        Options options = new Options();
        List arrayList = new ArrayList(100);
        arrayList.add("");
        arrayList.add("N");
        try {
            callPrivateMethod("org.crap4j.Options", "parseLines", new Class[] { List.class }, options, new Object[] { arrayList });
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ex.getMessage()", "Config file is not well-formed. Bad line #(1): N.", ex.getMessage());
            assertThrownBy(Options.class, ex);
            assertNull("options.getProjectDir()", options.getProjectDir());
            assertNull("options.libClasspaths", getPrivateField(options, "libClasspaths"));
            assertNull("options.testClassDirs", getPrivateField(options, "testClassDirs"));
            assertNull("options.classDirs", getPrivateField(options, "classDirs"));
            assertNull("options.sourceDirs", getPrivateField(options, "sourceDirs"));
            assertEquals("(ArrayList) arrayList.size()", 2, arrayList.size());
        }
    }

    public void testParseLinesThrowsIllegalArgumentException4() throws Throwable {
        Options options = new Options();
        List arrayList = new ArrayList(100);
        arrayList.add("1");
        try {
            callPrivateMethod("org.crap4j.Options", "parseLines", new Class[] { List.class }, options, new Object[] { arrayList });
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ex.getMessage()", "Config file is not well-formed. Bad line #(1): 1.", ex.getMessage());
            assertThrownBy(Options.class, ex);
            assertNull("options.getProjectDir()", options.getProjectDir());
            assertNull("options.libClasspaths", getPrivateField(options, "libClasspaths"));
            assertNull("options.testClassDirs", getPrivateField(options, "testClassDirs"));
            assertNull("options.classDirs", getPrivateField(options, "classDirs"));
            assertNull("options.sourceDirs", getPrivateField(options, "sourceDirs"));
            assertEquals("(ArrayList) arrayList.size()", 1, arrayList.size());
        }
    }

    public void testParseLinesThrowsIllegalArgumentException5() throws Throwable {
        Options options = new Options();
        List arrayList = new ArrayList(100);
        arrayList.add("#");
        arrayList.add("m:");
        try {
            callPrivateMethod("org.crap4j.Options", "parseLines", new Class[] { List.class }, options, new Object[] { arrayList });
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ex.getMessage()", "Config file is not well-formed. Bad line #(1): m:.", ex.getMessage());
            assertThrownBy(Options.class, ex);
            assertNull("options.getProjectDir()", options.getProjectDir());
            assertNull("options.libClasspaths", getPrivateField(options, "libClasspaths"));
            assertNull("options.testClassDirs", getPrivateField(options, "testClassDirs"));
            assertNull("options.classDirs", getPrivateField(options, "classDirs"));
            assertNull("options.sourceDirs", getPrivateField(options, "sourceDirs"));
            assertEquals("(ArrayList) arrayList.size()", 2, arrayList.size());
        }
    }

    public void testParseLinesThrowsNullPointerException() throws Throwable {
        List arrayList = new ArrayList(100);
        arrayList.add(null);
        Options options = new Options();
        try {
            callPrivateMethod("org.crap4j.Options", "parseLines", new Class[] { List.class }, options, new Object[] { arrayList });
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(Options.class, ex);
            assertNull("options.getProjectDir()", options.getProjectDir());
            assertNull("options.libClasspaths", getPrivateField(options, "libClasspaths"));
            assertNull("options.testClassDirs", getPrivateField(options, "testClassDirs"));
            assertNull("options.classDirs", getPrivateField(options, "classDirs"));
            assertNull("options.sourceDirs", getPrivateField(options, "sourceDirs"));
            assertEquals("(ArrayList) arrayList.size()", 1, arrayList.size());
        }
    }

    public void testParseLinesThrowsNullPointerException1() throws Throwable {
        Options options = new Options();
        try {
            callPrivateMethod("org.crap4j.Options", "parseLines", new Class[] { List.class }, options, new Object[] { null });
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(Options.class, ex);
            assertNull("options.getProjectDir()", options.getProjectDir());
            assertNull("options.libClasspaths", getPrivateField(options, "libClasspaths"));
            assertNull("options.testClassDirs", getPrivateField(options, "testClassDirs"));
            assertNull("options.classDirs", getPrivateField(options, "classDirs"));
            assertNull("options.sourceDirs", getPrivateField(options, "sourceDirs"));
        }
    }

    public void testReadConfigFileThrowsFileNotFoundException() throws Throwable {
        Options options = new Options();
        Mockingbird.enterRecordingMode();
        File file = (File) Mockingbird.getProxyObject(File.class);
        Mockingbird.replaceObjectForRecording(File.class, "<init>(java.lang.String)", file);
        Mockingbird.setReturnValue(false, file, "exists", "()boolean", new Object[] {}, Boolean.FALSE, 1);
        Mockingbird.enterTestMode(Options.class);
        try {
            options.readConfigFile();
            fail("Expected FileNotFoundException to be thrown");
        } catch (FileNotFoundException ex) {
            assertEquals("ex.getClass()", FileNotFoundException.class, ex.getClass());
            assertThrownBy(Options.class, ex);
            assertNull("options.getProjectDir()", options.getProjectDir());
            assertNull("options.classDirs", getPrivateField(options, "classDirs"));
            assertNull("options.testClassDirs", getPrivateField(options, "testClassDirs"));
            assertNull("options.libClasspaths", getPrivateField(options, "libClasspaths"));
            assertNull("options.sourceDirs", getPrivateField(options, "sourceDirs"));
        }
    }

    public void testReadConfigFileThrowsNullPointerException() throws Throwable {
        Options options = new Options();
        Mockingbird.enterRecordingMode();
        File file = (File) Mockingbird.getProxyObject(File.class);
        Mockingbird.replaceObjectForRecording(File.class, "<init>(java.lang.String)", file);
        Mockingbird.setReturnValue(false, file, "exists", "()boolean", new Object[] {}, Boolean.TRUE, 1);
        Mockingbird.setConstructorForException(FileReader.class, "<init>(java.io.File)", (Throwable) Mockingbird.getProxyObject(FileNotFoundException.class));
        Mockingbird.enterTestMode(Options.class);
        try {
            options.readConfigFile();
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(Options.class, ex);
            assertNull("options.getProjectDir()", options.getProjectDir());
            assertNull("options.classDirs", getPrivateField(options, "classDirs"));
            assertNull("options.testClassDirs", getPrivateField(options, "testClassDirs"));
            assertNull("options.libClasspaths", getPrivateField(options, "libClasspaths"));
            assertNull("options.sourceDirs", getPrivateField(options, "sourceDirs"));
        }
    }

    public void testReadConfigFileThrowsNullPointerException1() throws Throwable {
        Options options = new Options();
        try {
            options.readConfigFile();
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(File.class, ex);
            assertNull("options.getProjectDir()", options.getProjectDir());
            assertNull("options.classDirs", getPrivateField(options, "classDirs"));
            assertNull("options.testClassDirs", getPrivateField(options, "testClassDirs"));
            assertNull("options.libClasspaths", getPrivateField(options, "libClasspaths"));
            assertNull("options.sourceDirs", getPrivateField(options, "sourceDirs"));
        }
    }

    public void testSetConfigFileThrowsFileNotFoundException() throws Throwable {
        Options options = new Options();
        try {
            options.setConfigFile("testOptionsParam1");
            fail("Expected FileNotFoundException to be thrown");
        } catch (FileNotFoundException ex) {
            assertEquals("options.getConfigFile()", "testOptionsParam1", options.getConfigFile());
            assertEquals("ex.getClass()", FileNotFoundException.class, ex.getClass());
            assertThrownBy(Options.class, ex);
            assertNull("options.getProjectDir()", options.getProjectDir());
            assertNull("options.libClasspaths", getPrivateField(options, "libClasspaths"));
            assertNull("options.testClassDirs", getPrivateField(options, "testClassDirs"));
            assertNull("options.classDirs", getPrivateField(options, "classDirs"));
            assertNull("options.sourceDirs", getPrivateField(options, "sourceDirs"));
        }
    }

    public void testSetConfigFileThrowsNullPointerException() throws Throwable {
        Options options = new Options();
        try {
            options.setConfigFile(".");
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertEquals("options.getConfigFile()", ".", options.getConfigFile());
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(Options.class, ex);
            assertNull("options.getProjectDir()", options.getProjectDir());
            assertNull("options.libClasspaths", getPrivateField(options, "libClasspaths"));
            assertNull("options.testClassDirs", getPrivateField(options, "testClassDirs"));
            assertNull("options.classDirs", getPrivateField(options, "classDirs"));
            assertNull("options.sourceDirs", getPrivateField(options, "sourceDirs"));
        }
    }

    public void testSetPropertyThrowsIllegalArgumentException() throws Throwable {
        Options options = new Options();
        try {
            options.setProperty("testOptionsParam1", "testOptionsParam2");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ex.getMessage()", "Unknown property in ConfigFile: testOptionsParam1, with value:testOptionsParam2", ex.getMessage());
            assertThrownBy(Options.class, ex);
            assertNull("options.getProjectDir()", options.getProjectDir());
            assertNull("options.classDirs", getPrivateField(options, "classDirs"));
            assertNull("options.testClassDirs", getPrivateField(options, "testClassDirs"));
            assertNull("options.libClasspaths", getPrivateField(options, "libClasspaths"));
            assertNull("options.sourceDirs", getPrivateField(options, "sourceDirs"));
        }
    }

    public void testValidThrowsNullPointerException() throws Throwable {
        Options options = new Options();
        options.setProjectDir("testOptionsParam1");
        try {
            options.valid();
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(Options.class, ex);
            assertNull("options.classDirs", getPrivateField(options, "classDirs"));
        }
    }
}
