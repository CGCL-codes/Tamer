package edu.rice.cs.jlbench;

import junit.framework.TestCase;
import edu.rice.cs.jlbench.parser.Parser;
import edu.rice.cs.jlbench.parser.ParseException;
import edu.rice.cs.plt.tuple.Quint;
import edu.rice.cs.plt.tuple.Pair;
import edu.rice.cs.plt.iter.IterUtil;
import edu.rice.cs.plt.iter.ReadOnceIterable;
import java.io.IOException;
import java.util.LinkedList;
import java.util.StringTokenizer;
import static edu.rice.cs.jlbench.BenchmarkFileTest.RunnerEvent.*;

public class BenchmarkFileTest extends TestCase {

    public static void assertTrue(String message, Object expected, Object actual, boolean val) {
        assertTrue(message + " expected:<" + expected + "> but was:<" + actual + ">", val);
    }

    /**
   * Translates the given string into a regexp where all '*'s match any number of characters
   * and all '?'s match a single arbitrary character.
   */
    public static String makeSimpleRegexp(String s) {
        StringBuilder result = new StringBuilder("(?s)");
        StringTokenizer t = new StringTokenizer(s, "?*", true);
        for (String piece : ReadOnceIterable.make(IterUtil.asIterator(t))) {
            if (piece.equals("?")) {
                result.append(".");
            } else if (piece.equals("*")) {
                result.append(".*");
            } else {
                result.append("\\Q" + piece + "\\E");
            }
        }
        return result.toString();
    }

    protected static enum RunnerEvent {

        DEFINE_CLASS, RUN_TEST, RUN_STATIC_ERROR, RUN_RUNTIME_ERROR, CLEAN_UP
    }

    private static class TestBenchmarkRunner implements BenchmarkRunner {

        private LinkedList<Quint<RunnerEvent, PackageName, String, String, String>> _events;

        public TestBenchmarkRunner() {
            _events = new LinkedList<Quint<RunnerEvent, PackageName, String, String, String>>();
        }

        /**
     * Assert that the given event and parameters occurred
     * @param type  The type of event
     * @param p  The expected package name ({@code null}) for {@code CLEAN_UP})
     * @param name  Name of the defined class, or {@code ""} in other cases
     * @param header  A simple regexp matching the expected header string ({@code ""}) for {@code CLEAN_UP})
     * @param body  A simple regexp matching the expected body ({@code ""}) for {@code CLEAN_UP})
     */
        public void assertEvent(RunnerEvent type, PackageName p, String name, String header, String body) {
            assertTrue("Another event occurred", !_events.isEmpty());
            Quint<RunnerEvent, PackageName, String, String, String> event = _events.removeFirst();
            assertEquals("Next event has the expected type", type, event.first());
            assertEquals("Next event has the expected package", p, event.second());
            assertEquals("Next event has the expected name", name, event.third());
            assertTrue("Next event has the expected header", header, event.fourth(), event.fourth().matches(makeSimpleRegexp(header)));
            assertTrue("Next event has the expected declaration", body, event.fifth(), event.fifth().matches(makeSimpleRegexp(body)));
        }

        public void dumpEvents() {
            for (Quint<RunnerEvent, PackageName, String, String, String> event : _events) {
                System.out.println(event.first() + ": " + event);
            }
        }

        public void defineClass(PackageName p, String name, String header, String declaration) {
            _events.add(Quint.make(DEFINE_CLASS, p, name, header, declaration));
        }

        public BenchmarkResult runTest(PackageName p, String header, String body) {
            _events.add(Quint.make(RUN_TEST, p, "", header, body));
            return BenchmarkResult.success();
        }

        public BenchmarkResult runStaticError(PackageName p, String header, String body) {
            _events.add(Quint.make(RUN_STATIC_ERROR, p, "", header, body));
            return BenchmarkResult.success();
        }

        public BenchmarkResult runRuntimeError(PackageName p, String header, String body) {
            _events.add(Quint.make(RUN_RUNTIME_ERROR, p, "", header, body));
            return BenchmarkResult.success();
        }

        public void cleanUp() {
            _events.add(new Quint<RunnerEvent, PackageName, String, String, String>(CLEAN_UP, null, "", "", ""));
        }
    }

    public void testExampleFile() throws IOException, ParseException {
        TestBenchmarkRunner r = new TestBenchmarkRunner();
        BenchmarkFile f = Parser.parseFile("benchmarks/Example.jlbench");
        @SuppressWarnings("unused") Iterable<Pair<BenchmarkFile.Range, BenchmarkResult>> results = f.run(r);
        String defaultImport = "import java.util*import foo.bar.zot*";
        r.assertEvent(DEFINE_CLASS, new PackageName("foo", "bar", "zot"), "ZotClass", "package foo.bar.zot;*" + defaultImport, "public class ZotClass {*}*");
        r.assertEvent(DEFINE_CLASS, new PackageName("foo", "bar", "zot"), "ZotClass2", "package foo.bar.zot;*" + defaultImport, "public class ZotClass2 extends ZotClass {*}*");
        r.assertEvent(DEFINE_CLASS, new PackageName("foo", "bar", "zoo"), "ZooClass", "package foo.bar.zoo;*" + defaultImport, "public class ZooClass {*}*");
        PackageName fooBar = new PackageName("foo", "bar");
        String defaultHeader = "package foo.bar;*" + defaultImport;
        r.assertEvent(DEFINE_CLASS, fooBar, "Something", defaultHeader, "public class Something {*int x = 27;*}*");
        r.assertEvent(DEFINE_CLASS, fooBar, "SomeOtherThing", defaultHeader, "public class SomeOtherThing*");
        String decl = "*String hello = \"Hello\"*";
        r.assertEvent(RUN_TEST, fooBar, "", defaultHeader, decl + "int someX*assertTrue(someX == 27)*");
        r.assertEvent(RUN_STATIC_ERROR, fooBar, "", defaultHeader, decl + "Integer i = new Something()*");
        r.assertEvent(RUN_RUNTIME_ERROR, fooBar, "", defaultHeader, decl + "Integer i = null*i.toString()*");
        r.assertEvent(RUN_STATIC_ERROR, fooBar, "", defaultHeader, decl + "int x = 23;*if (true) { x = 24; }}*");
        r.assertEvent(RUN_TEST, fooBar, "", defaultHeader, decl + "Object z = new ZotClass*hello +=*hello.equals*");
        r.assertEvent(RUN_TEST, fooBar, "", defaultHeader, decl + "hello += \" world\"*hello.equals*");
        r.assertEvent(CLEAN_UP, null, "", "", "");
    }
}
