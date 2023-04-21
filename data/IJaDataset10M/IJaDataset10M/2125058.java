package jpdltest;

import java.io.IOException;
import java.lang.annotation.Annotation;
import jpdl.introspection.asm.AsmDriver;
import jpdl.joinpoints.JoinPoint;
import jpdl.parser.lexer.LexerException;
import jpdl.parser.parser.ParserException;
import jpdl.query.QueryEvaluator;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.textui.TestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PerformanceTest {

    private static final String rtJarFile = "/usr/lib/jvm/java-6-sun-1.6.0.03/jre/lib/rt.jar";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(timeout = 30000)
    public void simplePatternTest() throws Exception {
        Iterable<JoinPoint> jps = QueryEvaluator.Execute("'*' || '* *.*' || '* *.*(...)';", new AsmDriver(rtJarFile));
        int count = 0;
        for (JoinPoint jp : jps) {
            count++;
        }
        System.out.println("Total count " + count);
    }

    @Test(timeout = 30000)
    public void simpleTest() throws Exception {
        Iterable<JoinPoint> jps = QueryEvaluator.Execute("public || !public;", new AsmDriver(rtJarFile));
        int count = 0;
        for (JoinPoint jp : jps) {
            count++;
        }
        System.out.println("Total count " + count);
    }

    @Test(timeout = 60000)
    public void allMethods() throws Exception {
        Iterable<JoinPoint> jps = QueryEvaluator.Execute("method(sourceType);", new AsmDriver(rtJarFile));
        int count = 0;
        for (JoinPoint jp : jps) {
            count++;
        }
        System.out.println("Total count " + count);
    }
}
