package org.unitmetrics.java.metrics;

import org.eclipse.jdt.core.IType;
import org.unitmetrics.java.junit.JavaTestCase;

/** 
 * @author Martin Kersten 
 */
public class TypesToMeasureTest extends JavaTestCase {

    TypesToMeasure typesToMeasure = new TypesToMeasure();

    IType classA;

    IType classB;

    protected void setUp() throws Exception {
        super.setUp();
        classA = createCompilationUnitOfType("ClassA", "ClassA.java", "public class AClass {}");
        classB = createCompilationUnitOfType("ClassB", "ClassB.java", "public class BClass {}");
    }

    public void testMarkTypeIdentifier() throws Exception {
        assertNull("No type identifier should be next", typesToMeasure.getNextTypeToMeasure());
        typesToMeasure.mark(classA);
        assertEquals("String should be next", classA, typesToMeasure.getNextTypeToMeasure().getElementName());
    }

    public void testMarkTypeIdentifiers() {
        typesToMeasure.mark(new String[] { classA.getHandleIdentifier(), classB.getHandleIdentifier() });
        IType[] types = new IType[] { typesToMeasure.getNextTypeToMeasure(), typesToMeasure.getNextTypeToMeasure() };
        assertContains("ClassA should be marked", classA, types);
        assertContains("ClassB should be marked", classB, types);
    }

    public void testUnmarkTypeIdentifier() throws Exception {
        typesToMeasure.mark(classA);
        typesToMeasure.unmark(classA);
        assertNull("No type identifier should be next", typesToMeasure.getNextTypeToMeasure());
    }

    public void testUnmarkTypeIdentifiers() {
        typesToMeasure.mark(new String[] { classA.getHandleIdentifier(), classB.getHandleIdentifier() });
        typesToMeasure.unmark(new String[] { classA.getHandleIdentifier(), classB.getHandleIdentifier() });
        assertNull("No type identifier should be next", typesToMeasure.getNextTypeToMeasure());
    }
}
