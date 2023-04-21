package org.crap4j.complexity;

import java.io.File;
import java.util.List;
import org.crap4j.MethodComplexity;
import org.crap4j.MethodInfo;
import org.crap4j.complexity.CyclomaticComplexity;
import junit.framework.TestCase;

public class CyclomaticComplexityTestCase extends TestCase {

    public void testComplexity() throws Exception {
        String file = getClass().getClassLoader().getResource("org/crap4j/complexity/ComplexitySubject1TestFixture.class").getFile();
        File classFile = new File(file);
        CyclomaticComplexity cc = new CyclomaticComplexity();
        List<MethodComplexity> complexities = cc.getMethodComplexitiesFor(classFile);
        assertEquals(1, complexities.get(0).getComplexity());
        assertEquals("org.crap4j.complexity.ComplexitySubject1TestFixture.<init>()V", complexities.get(0).getMatchingMethodSignature());
        assertEquals(1, complexities.get(1).getComplexity());
        assertEquals("org.crap4j.complexity.ComplexitySubject1TestFixture.method0()V", complexities.get(1).getMatchingMethodSignature());
        assertEquals(2, complexities.get(2).getComplexity());
        assertEquals("org.crap4j.complexity.ComplexitySubject1TestFixture.method1(I)V", complexities.get(2).getMatchingMethodSignature());
        assertEquals(4, complexities.get(3).getComplexity());
        assertEquals("org.crap4j.complexity.ComplexitySubject1TestFixture.method2(Ljava/lang/String;)Ljava/lang/String;", complexities.get(3).getMatchingMethodSignature());
        assertEquals(2, complexities.get(4).getComplexity());
        assertEquals("org.crap4j.complexity.ComplexitySubject1TestFixture.getAgitarEclipseApiPluginDirectory()Ljava/lang/String;", complexities.get(4).getMatchingMethodSignature());
        assertEquals(1, complexities.get(5).getComplexity());
        assertEquals("org.crap4j.complexity.ComplexitySubject1TestFixture.readFile()I", complexities.get(5).getMatchingMethodSignature());
        assertEquals(3, complexities.get(6).getComplexity());
        assertEquals("org.crap4j.complexity.ComplexitySubject1TestFixture.switcheroo(I)V", complexities.get(6).getMatchingMethodSignature());
    }

    public void testAbstract() throws Exception {
        String file = getClass().getClassLoader().getResource("org/crap4j/complexity/AbstractFixture.class").getFile();
        File classFile = new File(file);
        CyclomaticComplexity cc = new CyclomaticComplexity();
        List<MethodComplexity> complexities = cc.getMethodComplexitiesFor(classFile);
        assertEquals(1, complexities.size());
    }

    public void testInterface() throws Exception {
        String file = getClass().getClassLoader().getResource("org/crap4j/complexity/InterfaceFixture.class").getFile();
        File classFile = new File(file);
        CyclomaticComplexity cc = new CyclomaticComplexity();
        List<MethodComplexity> complexities = cc.getMethodComplexitiesFor(classFile);
        assertEquals(0, complexities.size());
    }
}
