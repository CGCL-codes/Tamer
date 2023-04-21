package com.jeantessier.diff;

import java.util.*;
import com.jeantessier.classreader.*;

public class TestDifferencesFactoryWithIncompatibleDifferenceStrategy extends TestDifferencesFactoryBase {

    private DifferencesFactory factory;

    protected void setUp() throws Exception {
        super.setUp();
        factory = new DifferencesFactory(new IncompatibleDifferenceStrategy(new NoDifferenceStrategy()));
    }

    public void testFieldDeclarationDifference() {
        String className = "ModifiedPackage.ModifiedInterface";
        Classfile oldClass = getOldJar().getClassfile(className);
        Classfile newClass = getNewJar().getClassfile(className);
        ClassDifferences classDifferences = (ClassDifferences) factory.createClassDifferences(className, oldClass, newClass);
        FieldDifferences fieldDifferences = null;
        Iterator i = classDifferences.getFeatureDifferences().iterator();
        while (i.hasNext()) {
            Differences differences = (Differences) i.next();
            if (differences.getName().equals(className + ".modifiedField")) {
                fieldDifferences = (FieldDifferences) differences;
            }
        }
        assertEquals("public static final int modifiedField", fieldDifferences.getOldDeclaration());
        assertEquals("public static final float modifiedField", fieldDifferences.getNewDeclaration());
    }

    public void testFieldConstantValueDifference() {
        String className = "ModifiedPackage.ModifiedInterface";
        Classfile oldClass = getOldJar().getClassfile(className);
        Classfile newClass = getNewJar().getClassfile(className);
        ClassDifferences classDifferences = (ClassDifferences) factory.createClassDifferences(className, oldClass, newClass);
        FieldDifferences fieldDifferences = null;
        Iterator i = classDifferences.getFeatureDifferences().iterator();
        while (i.hasNext()) {
            Differences differences = (Differences) i.next();
            if (differences.getName().equals(className + ".modifiedValueField")) {
                fieldDifferences = (FieldDifferences) differences;
            }
        }
        assertNull(fieldDifferences);
    }

    public void testConstructorDifference() {
        String className = "ModifiedPackage.ModifiedClass";
        Classfile oldClass = getOldJar().getClassfile(className);
        Classfile newClass = getNewJar().getClassfile(className);
        ClassDifferences classDifferences = (ClassDifferences) factory.createClassDifferences(className, oldClass, newClass);
        ConstructorDifferences constructorDifferences = null;
        Iterator i = classDifferences.getFeatureDifferences().iterator();
        while (i.hasNext()) {
            Differences differences = (Differences) i.next();
            if (differences.getName().equals(className + ".ModifiedClass(int, int, int)")) {
                constructorDifferences = (ConstructorDifferences) differences;
            }
        }
        assertFalse(constructorDifferences.isCodeDifference());
    }

    public void testConstructorCodeDifference() {
        String className = "ModifiedPackage.ModifiedClass";
        Classfile oldClass = getOldJar().getClassfile(className);
        Classfile newClass = getNewJar().getClassfile(className);
        ClassDifferences classDifferences = (ClassDifferences) factory.createClassDifferences(className, oldClass, newClass);
        ConstructorDifferences constructorDifferences = null;
        Iterator i = classDifferences.getFeatureDifferences().iterator();
        while (i.hasNext()) {
            Differences differences = (Differences) i.next();
            if (differences.getName().equals(className + ".ModifiedClass(float)")) {
                constructorDifferences = (ConstructorDifferences) differences;
            }
        }
        assertNull(constructorDifferences);
    }

    public void testMethodDifference() {
        String className = "ModifiedPackage.ModifiedClass";
        Classfile oldClass = getOldJar().getClassfile(className);
        Classfile newClass = getNewJar().getClassfile(className);
        ClassDifferences classDifferences = (ClassDifferences) factory.createClassDifferences(className, oldClass, newClass);
        MethodDifferences methodDifferences = null;
        Iterator i = classDifferences.getFeatureDifferences().iterator();
        while (i.hasNext()) {
            Differences differences = (Differences) i.next();
            if (differences.getName().equals(className + ".modifiedMethod()")) {
                methodDifferences = (MethodDifferences) differences;
            }
        }
        assertNotNull(methodDifferences);
        assertTrue(methodDifferences.isModified());
        assertFalse(methodDifferences.isCodeDifference());
    }

    public void testMethodCodeDifference() {
        String className = "ModifiedPackage.ModifiedClass";
        Classfile oldClass = getOldJar().getClassfile(className);
        Classfile newClass = getNewJar().getClassfile(className);
        ClassDifferences classDifferences = (ClassDifferences) factory.createClassDifferences(className, oldClass, newClass);
        MethodDifferences methodDifferences = null;
        Iterator i = classDifferences.getFeatureDifferences().iterator();
        while (i.hasNext()) {
            Differences differences = (Differences) i.next();
            if (differences.getName().equals(className + ".modifiedCodeMethod()")) {
                methodDifferences = (MethodDifferences) differences;
            }
        }
        assertNull(methodDifferences);
    }
}
