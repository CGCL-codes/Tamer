package org.sodbeans.tests.compiler.Stack;

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
 * Unit test for arrays are to be added here.
 *
 * @author Elliot Motl
 */
public class StackTester {

    private HopVirtualMachine vm;

    public StackTester() {
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
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.STACK + CompilerTestSuite.PASS + "Add.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue variableValue = vm.getDataEnvironment().getVariableValue("result");
        String a = variableValue.getResult().text;
        if (!a.equals("FiveFourThreeTwoOne")) {
            fail();
        }
        vm.stop();
    }

    @Test
    public void test_Push() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.STACK + CompilerTestSuite.PASS + "Push.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue variableValue = vm.getDataEnvironment().getVariableValue("result");
        String a = variableValue.getResult().text;
        if (!a.equals("FiveFourThreeTwoOne")) {
            fail();
        }
        vm.stop();
    }

    @Test
    public void test_Clear() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.STACK + CompilerTestSuite.PASS + "Empty.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue variableValue = vm.getDataEnvironment().getVariableValue("result");
        String a = variableValue.getResult().text;
        if (!a.equals("FiveFourThreeTwoOne")) {
            fail();
        }
        vm.stop();
    }

    @Test
    public void test_Contains() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.STACK + CompilerTestSuite.PASS + "Contains.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue variableValue = vm.getDataEnvironment().getVariableValue("bool1");
        boolean a1 = variableValue.getResult().boolean_value;
        variableValue = vm.getDataEnvironment().getVariableValue("bool2");
        boolean a2 = variableValue.getResult().boolean_value;
        variableValue = vm.getDataEnvironment().getVariableValue("bool3");
        boolean a3 = variableValue.getResult().boolean_value;
        variableValue = vm.getDataEnvironment().getVariableValue("bool4");
        boolean a4 = variableValue.getResult().boolean_value;
        if (a1 != true) {
            fail();
        }
        if (a2 != false) {
            fail();
        }
        if (a3 != true) {
            fail();
        }
        if (a4 != false) {
            fail();
        }
        vm.stop();
    }

    @Test
    public void test_Copy() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.STACK + CompilerTestSuite.PASS + "Copy.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue variableValue = vm.getDataEnvironment().getVariableValue("result");
        String a = variableValue.getResult().text;
        if (!a.equals("FiveFourThreeTwoOne")) {
            fail();
        }
        vm.stop();
    }

    @Test
    public void test_IsEmpty() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.STACK + CompilerTestSuite.PASS + "IsEmpty.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue variableValue = vm.getDataEnvironment().getVariableValue("bool1");
        boolean bool1 = variableValue.getResult().boolean_value;
        variableValue = vm.getDataEnvironment().getVariableValue("bool2");
        boolean bool2 = variableValue.getResult().boolean_value;
        if (bool1 != false) {
            fail();
        }
        if (bool2 != true) {
            fail();
        }
        vm.stop();
    }

    @Test
    public void test_Iterator() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.STACK + CompilerTestSuite.PASS + "Iterator.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue variableValue = vm.getDataEnvironment().getVariableValue("result");
        String a1 = variableValue.getResult().text;
        if (!a1.equals("SevenSixFiveFourThreeTwoOne")) {
            fail();
        }
        vm.stop();
    }

    @Test
    public void test_Peek() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.STACK + CompilerTestSuite.PASS + "Peek.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue variableValue = vm.getDataEnvironment().getVariableValue("result");
        String a1 = variableValue.getResult().text;
        if (!a1.equals("Five")) {
            fail();
        }
        vm.stop();
    }

    @Test
    public void test_RemoveAll() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.STACK + CompilerTestSuite.PASS + "RemoveAll.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue variableValue = vm.getDataEnvironment().getVariableValue("result");
        String a1 = variableValue.getResult().text;
        if (!a1.equals("SixFiveFourTwo")) {
            fail();
        }
        vm.stop();
    }

    @Test
    public void test_Pop() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.STACK + CompilerTestSuite.PASS + "Pop.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue variableValue = vm.getDataEnvironment().getVariableValue("result");
        String a1 = variableValue.getResult().text;
        if (!a1.equals("Six")) {
            fail();
        }
        vm.stop();
    }

    @Test
    public void test_RemoveValue() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.STACK + CompilerTestSuite.PASS + "RemoveValue.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue variableValue = vm.getDataEnvironment().getVariableValue("result");
        String a1 = variableValue.getResult().text;
        if (!a1.equals("FiveFourTwoOne")) {
            fail();
        }
        vm.stop();
    }

    @Test
    public void test_Size() {
        CompilerTestSuite.build(CompilerTestSuite.getHopFile(CompilerTestSuite.STACK + CompilerTestSuite.PASS + "Size.hop"));
        if (!vm.getCompilerErrors().isCompilationErrorFree()) {
            fail();
        }
        vm.blockRun();
        ExpressionValue variableValue = vm.getDataEnvironment().getVariableValue("result");
        int a = variableValue.getResult().integer;
        if (a != 5) {
            fail();
        }
        vm.stop();
    }
}
