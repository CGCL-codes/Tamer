package org.sodbeans.tests.compiler.TreeSet;

import org.sonify.vm.execution.ExpressionValue;
import org.sodbeans.tests.compiler.CompilerTestSuite;
import org.sonify.vm.hop.HopVirtualMachine;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Zachary Gillette
 */
public class TreeSetTester {

    private HopVirtualMachine vm;

    public TreeSetTester() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        CompilerTestSuite.forceSetup();
        vm = CompilerTestSuite.getVirtualMachine();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test_Add() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.TREESET + CompilerTestSuite.PASS + "Add.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue v1 = vm.getDataEnvironment().getVariableValue("textTotal");
        String a1 = v1.getResult().text;
        if (a1.equals("ABCDEFG") == false) {
            fail();
        }
        ExpressionValue v2 = vm.getDataEnvironment().getVariableValue("booleanTotal");
        String a2 = v2.getResult().text;
        if (a2.equals("falsetrue") == false) {
            fail();
        }
        ExpressionValue v3 = vm.getDataEnvironment().getVariableValue("integerTotal");
        String a3 = v3.getResult().text;
        if (a3.equals("123457") == false) {
            fail();
        }
        ExpressionValue v4 = vm.getDataEnvironment().getVariableValue("numberTotal");
        String a4 = v4.getResult().text;
        if (a4.equals("1.02.03.04.05.07.0") == false) {
            fail();
        }
    }

    @Test
    public void test_AddAll() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.TREESET + CompilerTestSuite.PASS + "AddAll.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue v1 = vm.getDataEnvironment().getVariableValue("textTotal");
        String a1 = v1.getResult().text;
        if (a1.equals("ABCDEFG") == false) {
            fail();
        }
        ExpressionValue v2 = vm.getDataEnvironment().getVariableValue("booleanTotal");
        String a2 = v2.getResult().text;
        if (a2.equals("falsetrue") == false) {
            fail();
        }
        ExpressionValue v3 = vm.getDataEnvironment().getVariableValue("integerTotal");
        String a3 = v3.getResult().text;
        if (a3.equals("123457") == false) {
            fail();
        }
        ExpressionValue v4 = vm.getDataEnvironment().getVariableValue("numberTotal");
        String a4 = v4.getResult().text;
        if (a4.equals("1.02.03.04.05.07.0") == false) {
            fail();
        }
    }

    @Test
    public void test_Clear() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.TREESET + CompilerTestSuite.PASS + "Clear.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue v1 = vm.getDataEnvironment().getVariableValue("textTotal");
        String a1 = v1.getResult().text;
        if (a1.equals("true") == false) {
            fail();
        }
        ExpressionValue v2 = vm.getDataEnvironment().getVariableValue("booleanTotal");
        String a2 = v2.getResult().text;
        if (a2.equals("true") == false) {
            fail();
        }
        ExpressionValue v3 = vm.getDataEnvironment().getVariableValue("integerTotal");
        String a3 = v3.getResult().text;
        if (a3.equals("true") == false) {
            fail();
        }
        ExpressionValue v4 = vm.getDataEnvironment().getVariableValue("numberTotal");
        String a4 = v4.getResult().text;
        if (a4.equals("true") == false) {
            fail();
        }
    }

    @Test
    public void test_Contains() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.TREESET + CompilerTestSuite.PASS + "Contains.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue v1 = vm.getDataEnvironment().getVariableValue("textTotal");
        String a1 = v1.getResult().text;
        if (a1.equals("true") == false) {
            fail();
        }
        ExpressionValue v2 = vm.getDataEnvironment().getVariableValue("booleanTotal");
        String a2 = v2.getResult().text;
        if (a2.equals("true") == false) {
            fail();
        }
        ExpressionValue v3 = vm.getDataEnvironment().getVariableValue("integerTotal");
        String a3 = v3.getResult().text;
        if (a3.equals("false") == false) {
            fail();
        }
        ExpressionValue v4 = vm.getDataEnvironment().getVariableValue("numberTotal");
        String a4 = v4.getResult().text;
        if (a4.equals("false") == false) {
            fail();
        }
    }

    @Test
    public void test_IsEmpty() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.TREESET + CompilerTestSuite.PASS + "IsEmpty.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue v1 = vm.getDataEnvironment().getVariableValue("textTotal");
        String a1 = v1.getResult().text;
        if (a1.equals("true") == false) {
            fail();
        }
        ExpressionValue v2 = vm.getDataEnvironment().getVariableValue("booleanTotal");
        String a2 = v2.getResult().text;
        if (a2.equals("false") == false) {
            fail();
        }
        ExpressionValue v3 = vm.getDataEnvironment().getVariableValue("integerTotal");
        String a3 = v3.getResult().text;
        if (a3.equals("true") == false) {
            fail();
        }
        ExpressionValue v4 = vm.getDataEnvironment().getVariableValue("numberTotal");
        String a4 = v4.getResult().text;
        if (a4.equals("false") == false) {
            fail();
        }
    }

    @Test
    public void test_Copy() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.TREESET + CompilerTestSuite.PASS + "Copy.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue v1 = vm.getDataEnvironment().getVariableValue("textTotal");
        String a1 = v1.getResult().text;
        if (a1.equals("ABCDEFG") == false) {
            fail();
        }
        ExpressionValue v2 = vm.getDataEnvironment().getVariableValue("booleanTotal");
        String a2 = v2.getResult().text;
        if (a2.equals("falsetrue") == false) {
            fail();
        }
        ExpressionValue v3 = vm.getDataEnvironment().getVariableValue("integerTotal");
        String a3 = v3.getResult().text;
        if (a3.equals("123457") == false) {
            fail();
        }
        ExpressionValue v4 = vm.getDataEnvironment().getVariableValue("numberTotal");
        String a4 = v4.getResult().text;
        if (a4.equals("1.02.03.04.05.07.0") == false) {
            fail();
        }
    }

    @Test
    public void test_Remove() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.TREESET + CompilerTestSuite.PASS + "Remove.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue v1 = vm.getDataEnvironment().getVariableValue("textTotal");
        String a1 = v1.getResult().text;
        if (a1.equals("ABCDEFG") == false) {
            fail();
        }
        ExpressionValue v2 = vm.getDataEnvironment().getVariableValue("booleanTotal");
        String a2 = v2.getResult().text;
        if (a2.equals("true") == false) {
            fail();
        }
        ExpressionValue v3 = vm.getDataEnvironment().getVariableValue("integerTotal");
        String a3 = v3.getResult().text;
        if (a3.equals("123457") == false) {
            fail();
        }
        ExpressionValue v4 = vm.getDataEnvironment().getVariableValue("numberTotal");
        String a4 = v4.getResult().text;
        if (a4.equals("1.02.03.04.05.07.0") == false) {
            fail();
        }
    }

    @Test
    public void test_Size() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.TREESET + CompilerTestSuite.PASS + "Size.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue v1 = vm.getDataEnvironment().getVariableValue("textTotal");
        String a1 = v1.getResult().text;
        if (a1.equals("7") == false) {
            fail();
        }
        ExpressionValue v2 = vm.getDataEnvironment().getVariableValue("booleanTotal");
        String a2 = v2.getResult().text;
        if (a2.equals("2") == false) {
            fail();
        }
        ExpressionValue v3 = vm.getDataEnvironment().getVariableValue("integerTotal");
        String a3 = v3.getResult().text;
        if (a3.equals("6") == false) {
            fail();
        }
        ExpressionValue v4 = vm.getDataEnvironment().getVariableValue("numberTotal");
        String a4 = v4.getResult().text;
        if (a4.equals("6") == false) {
            fail();
        }
    }

    @Test
    public void test_Union() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.TREESET + CompilerTestSuite.PASS + "Union.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue v1 = vm.getDataEnvironment().getVariableValue("textTotal");
        String a1 = v1.getResult().text;
        if (a1.equals("ABCDEFGXYZ") == false) {
            fail();
        }
        ExpressionValue v2 = vm.getDataEnvironment().getVariableValue("booleanTotal");
        String a2 = v2.getResult().text;
        if (a2.equals("falsetrue") == false) {
            fail();
        }
        ExpressionValue v3 = vm.getDataEnvironment().getVariableValue("integerTotal");
        String a3 = v3.getResult().text;
        if (a3.equals("12710304050") == false) {
            fail();
        }
        ExpressionValue v4 = vm.getDataEnvironment().getVariableValue("numberTotal");
        String a4 = v4.getResult().text;
        if (a4.equals("1.02.07.010.030.040.050.0") == false) {
            fail();
        }
    }

    @Test
    public void test_Intersect() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.TREESET + CompilerTestSuite.PASS + "Intersect.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue v1 = vm.getDataEnvironment().getVariableValue("textTotal");
        String a1 = v1.getResult().text;
        if (a1.equals("ABCD") == false) {
            fail();
        }
        ExpressionValue v2 = vm.getDataEnvironment().getVariableValue("booleanTotal");
        String a2 = v2.getResult().text;
        if (a2.equals("true") == false) {
            fail();
        }
        ExpressionValue v3 = vm.getDataEnvironment().getVariableValue("integerTotal");
        String a3 = v3.getResult().text;
        if (a3.equals("127") == false) {
            fail();
        }
        ExpressionValue v4 = vm.getDataEnvironment().getVariableValue("numberTotal");
        String a4 = v4.getResult().text;
        if (a4.equals("1.02.07.0") == false) {
            fail();
        }
    }

    @Test
    public void test_Difference() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.TREESET + CompilerTestSuite.PASS + "Difference.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue v1 = vm.getDataEnvironment().getVariableValue("textTotal");
        String a1 = v1.getResult().text;
        if (a1.equals("EFGXYZ") == false) {
            fail();
        }
        ExpressionValue v2 = vm.getDataEnvironment().getVariableValue("booleanTotal");
        String a2 = v2.getResult().text;
        if (a2.equals("") == false) {
            fail();
        }
        ExpressionValue v3 = vm.getDataEnvironment().getVariableValue("integerTotal");
        String a3 = v3.getResult().text;
        if (a3.equals("10304050") == false) {
            fail();
        }
        ExpressionValue v4 = vm.getDataEnvironment().getVariableValue("numberTotal");
        String a4 = v4.getResult().text;
        if (a4.equals("10.030.040.050.0") == false) {
            fail();
        }
    }
}
