package org.unitmetrics.java.metrics;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.unitmetrics.IUnit;

/** 
 * @author Martin Kersten 
 */
abstract class ThreeSimpleClassesMeasurementMethodTestCase extends ComplexJavaMeasurementMethodTestCase {

    ICompilationUnit unit;

    protected IType classA;

    protected IType classB;

    protected IType classC;

    protected IUnit classAUnit;

    protected IUnit classBUnit;

    protected IUnit classCUnit;

    protected void setUpJavaFiles() throws Exception {
        unit = createTestCompilationUnit("class ClassA {}" + "class ClassB {}" + "class ClassC {}");
        classA = unit.getType("ClassA");
        classB = unit.getType("ClassB");
        classC = unit.getType("ClassC");
        classAUnit = getUnit(classA);
        classBUnit = getUnit(classB);
        classCUnit = getUnit(classC);
    }
}
