package org.jmlspecs.eclipse.jdt.core.tests.rac;

import org.jmlspecs.jml4.rac.runtime.JMLAssertError;
import org.jmlspecs.jml4.rac.runtime.JMLAssumeError;
import org.jmlspecs.jml4.rac.runtime.JMLEvaluationError;

public class InlineAssertionTest extends RacTestCompiler {

    public InlineAssertionTest(String name) {
        super(name);
    }

    /**
   * Is the assume statement translated? The assume statement is 
   * implemented as the same way as the assert statement, so
   * we have only a couple of tests for the assume statement.
   */
    public void test_assume_01() {
        compileAndExecGivenStatement("X.java", "public class X{\n" + "   public void m() {\n" + "		//@ assume true;" + "   }\n" + "}\n", "new X().m()");
    }

    /** Is an assume statement translated? */
    public void test_assume_02() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "   public void m() {\n" + "		//@ assume false;" + "   }\n" + "}\n", "new X().m()", null, JMLAssumeError.class);
    }

    /** Is inline assertion translated with no block */
    public void test_inline_no_block_01() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "   public void m() {\n" + "		//@ assume false;\n" + "   }\n" + "}\n", "new X().m()", null, JMLAssumeError.class);
    }

    public void test_inline_no_block_02() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "   public void m() {\n" + "		//@ assert false;\n" + "   }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_inline_no_block_03() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "	//@ ghost boolean val = false;\n" + "   public void m() {\n" + "		//@ set val = true;\n" + "		//@ assert !val;\n" + "   }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_inline_no_block_04() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "	//@ ghost boolean val = false;\n" + "   public void m() {\n" + "		if (true)\n" + "			//@ set val = true;\n" + "		//@ assert !val;\n" + "   }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_inline_no_block_05() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "   public void m() {\n" + "		if (true)\n" + "			//@ assert false;\n" + "   }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_inline_no_block_06() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "	//@ ghost boolean val = false;\n" + "   public void m() {\n" + "		if (true)\n" + "			//@ assume false;\n" + "   }\n" + "}\n", "new X().m()", null, JMLAssumeError.class);
    }

    public void test_complicated_all_inline() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "	//@ ghost boolean val = false;\n" + "   public void m() {\n" + "		if (true)\n" + "			//@ set val = true;\n" + "		//@ assert !val;\n" + "		int i = 0;\n" + "		//@ assume new X() != null;\n" + "		if (true)\n" + "         //@ maintaining true;\n" + "		  while (i++<3)\n" + "			//@ assume !false;\n" + "		if (true){\n" + "			//@ assume true;}" + "		if (true){\n" + "			if(false)\n" + "			//@ assume false;}" + "		if (true)\n" + "			i = 0;\n" + "		else\n" + "			//@ assume false;" + "		if (true)\n" + "			 i = 0;\n" + "		else if(i == 0)\n" + "			//@ assume false;" + "		else if(i == 1)\n" + "			//@ assume true;" + "		else\n" + "			//@ assume !true;" + "		while (i++ < 3)\n" + "			//@ assume true;\n" + "		//@ maintaining false;\n" + "		while (i++ < 4);\n" + "		//@ maintaining true;\n" + "		for(; i < 10; i++);\n" + "		for(; i < 10; i++)\n" + "			//@ assert true;\n" + "   }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_0001_assert_throw_error() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "   public void m() {\n" + "		//@ assert false;" + "   }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_0002_assert_no_error() {
        compileAndExecGivenStatement("X.java", "public class X{\n" + "   public void m() {\n" + "		//@ assert true;" + "   }\n" + "}\n", "new X().m()");
    }

    public void test_0003_assert_throw_error() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "   public void m() {\n" + "      int i = 0;\n" + "      i++;\n" + "		//@ assert false;" + "   }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_nested_assert_if_throw_error_01() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "   public void m() {\n" + "      int i = 0;\n" + "      if (i == 0) {\n" + "		   //@ assert false;" + "      }\n" + "   }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_nested_assert_if_throw_no_error_02() {
        compileAndExecGivenStatement("X.java", "public class X {\n" + "   public void m() {\n" + "      int i = 0;\n" + "      if (i == 0) {\n" + "		   //@ assert true;" + "      }\n" + "   }\n" + "}\n", "new X().m()");
    }

    public void test_nested_assert_if_throw_error_03() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "   public void m() {\n" + "      int i = 0;\n" + "		//@ assert true;\n" + "      if (i == 0) {\n" + "		   //@ assert false;" + "      }\n" + "   }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_nested_assert_if_throw_error_04() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "   public void m() {\n" + "      int i = 0;\n" + "		//@ assert false;\n" + "      if (i == 0) {\n" + "		   //@ assert true;" + "      }\n" + "   }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_nested_assert_if_throw_error_05() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "   public void m() {\n" + "      int i = 0;\n" + "		//@ assert false;\n" + "      if (i == 0) {\n" + "		   //@ assert false;" + "      }\n" + "   }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_nested_assert_if_throw_no_error_06() {
        compileAndExecGivenStatement("X.java", "public class X {\n" + "   public void m() {\n" + "      int i = 0;\n" + "		//@ assert true;\n" + "      if (i == 0) {\n" + "		   //@ assert true;" + "      }\n" + "   }\n" + "}\n", "new X().m()");
    }

    public void test_nested_assert_if_throw_error_07() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "   public void m() {\n" + "      int i = 0;\n" + "      if (i == 0) {\n" + "		   //@ assert false;\n" + "		   //@ assert true;\n" + "      }\n" + "   }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_nested_assert_if_throw_error_08() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "   public void m() {\n" + "      int i = 0;\n" + "      if (i == 0) {\n" + "		   //@ assert true;\n" + "		   //@ assert false;\n" + "      }\n" + "   }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_nested_assert_if_throw_error_09() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "   public void m() {\n" + "      int i = 0;\n" + "      if (i == 0) {\n" + "		   //@ assert false;" + "		   //@ assert false;\n" + "      }\n" + "   }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_nested_assert_if_throw_no_error_10() {
        compileAndExecGivenStatement("X.java", "public class X {\n" + "   public void m() {\n" + "      int i = 0;\n" + "      if (i == 0) {\n" + "		   //@ assert true;\n" + "		   //@ assert true;\n" + "      }\n" + "   }\n" + "}\n", "new X().m()");
    }

    public void test_nested_assert_if_throw_error_11() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "   public void m() {\n" + "      int i = 0;\n" + "		//@ assert false;\n" + "		//@ assert false;" + "   }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_nested_assert_if_throw_no_error_12() {
        compileAndExecGivenStatement("X.java", "public class X {\n" + "   public void m() {\n" + "      int i = 0;\n" + "		//@ assert true;\n" + "		//@ assert true;" + "   }\n" + "}\n", "new X().m()");
    }

    public void test_nested_assert_if_throw_error_13() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "   public void m() {\n" + "      int i = 0;\n" + "		//@ assert false;\n" + "		//@ assert true;" + "   }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_nested_assert_if_throw_error_14() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "   public void m() {\n" + "      int i = 0;\n" + "		//@ assert true;\n" + "		//@ assert false;" + "   }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_constructor_01() {
        compileAndExecGivenStatement("X.java", "public class X {\n" + "  X() {\n" + "	  //@ assert true;" + "  }\n" + "}\n", "new X()");
    }

    public void test_constructor_02() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "  X() {\n" + "	  //@ assert false;" + "  }\n" + "}\n", "new X()", null, JMLAssertError.class);
    }

    public void test_static_block_01() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "  {\n" + "	  //@ assert false;" + "  }\n" + "}\n", "new X()", null, JMLAssertError.class);
    }

    public void test_static_block_02() {
        compileAndExecGivenStatement("X.java", "public class X {\n" + "   {\n" + "	  //@ assert true;" + "  }\n" + "}\n", "new X()");
    }

    public void test_static_block_03() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "   static int i, j;\n" + "   {\n" + "	  i = 0;\n" + "	  //@ assert i > 0;\n" + "   }\n" + "   static {\n" + "	  j = 0;\n" + "	  //@ assert j == 0;\n" + "   }\n" + "}\n", "new X()", null, JMLAssertError.class);
    }

    public void test_static_block_04() {
    }

    public void test_static_block_05() {
    }

    public void test_static_block_06() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "  {\n" + "	  //@ assume false;" + "  }\n" + "}\n", "new X()", null, JMLAssumeError.class);
    }

    public void test_static_block_07() {
        compileAndExecGivenStatement("X.java", "public class X {\n" + "   {\n" + "	  //@ assume true;" + "  }\n" + "}\n", "new X()");
    }

    public void test_static_block_08() {
    }

    public void test_static_block_09() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class Y{" + "}\n" + "public class X {\n" + "   //@ ghost boolean val1 = true;\n" + "   static int i, j;\n" + "   //@ ghost boolean val2;\n" + "   {\n" + "	  i = 0;\n" + "	  //@ assume false;" + "   }\n" + "   //@ ghost boolean val3;\n" + "   static {\n" + "	  //@ assume true;" + "	  j = 0;\n" + "   }\n" + "   //@ ghost boolean val4 = true;\n" + "}\n", "new X()", null, JMLAssumeError.class);
    }

    public void test_static_block_10() {
        if (testIsDisabled("misc errors; investigate")) return;
        compileAndExecGivenStatement("X.java", "class Y{" + "}\n" + "public class X {\n" + "   //@ ghost boolean val1 = true;\n" + "   static int i, j;\n" + "   //@ ghost boolean val2;\n" + "   {\n" + "	  i = 0;\n" + "	  //@ set val2 = true;\n" + " 	  //@ set this.x = null;\n" + "   }\n" + "   //@ ghost boolean val3;\n" + "   static {\n" + "	  j = 0;\n" + "   }\n" + "   //@ public ghost Object x;\n" + "}\n", "new X()");
    }

    public void test_static_block_11() {
        compileAndExecGivenStatement("X.java", "class Y {\n" + "  //@ public ghost double x;\n" + "}\n" + "class X extends Y {\n" + "  //@ requires this.x > 0;\n" + "  public void m() {  }\n" + "	{\n" + "  //@ set x = 2;\n" + "	}\n" + "}\n", "new X().m()");
    }

    public void test_static_block_12() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "class Y {\n" + "  //@ public ghost @Nullable int[] x;\n" + "}\n" + "class X extends Y {\n" + "	{\n" + "  //@ set x = new int[] { 10 };}\n" + "  //@ requires x != null;\n" + "  public void m() {  }\n" + "}\n", "new X().m()");
    }

    public void test_static_block_13() {
        compileAndExecGivenStatement("X.java", "class Y {\n" + "  //@ public static ghost int x;\n" + "}\n" + "class X extends Y {\n" + "  //@ ensures x > 0;\n" + "  public void m() { }\n" + "  static {\n" + "  	 //@ set x = 10;\n" + "  }\n" + "}\n", "new X().m()");
    }

    public void test_static_block_14() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "class Y {\n" + "  //@ public ghost @Nullable int[] x;\n" + "}\n" + "class X extends Y {\n" + "	{\n" + "  //@ set x = new int[] { 10 };\n" + "  }\n" + "  //@ ensures x.length == 1;\n" + "  public void m() { }\n" + "}\n", "new X().m()");
    }

    public void test_static_block_15() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "class Y {\n" + "  //@ public ghost @Nullable int[] x;\n" + "}\n" + "class X extends Y {\n" + "  {\n" + "  //@ set x = new int[] { 10 };\n" + "  }\n" + "  //@ ensures this.x.length == 1;\n" + "  public void m() { }\n" + "}\n", "new X().m()");
    }

    public void test_static_block_16() {
        if (testIsDisabled("misc errors; investigate")) return;
        compileAndExecGivenStatement("X.java", "class Y {\n" + "  //@ public ghost nullable int[] x;\n" + "  {\n" + "  //@ set x = new int[10];\n" + "  }\n" + "}\n" + "class X extends Y {\n" + "   public Y y = new Y();\n" + "   {\n" + "   //@ set x = null;\n" + "   }\n" + "  //@ ensures y.x.length == 10;\n" + "  public void m() { }\n" + "}\n", "new X().m()");
    }

    public void test_static_block_18() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class Y {\n" + "  //@ public ghost int x;\n" + "  {\n" + "	//@ set x = 10;\n" + "  }\n" + "}\n" + "public class X extends Y {" + "  public void m() {\n" + "      //@ assert x == 20;\n" + "  }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_static_block_19() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class Y {\n" + "  //@ public ghost int x;\n" + "  {\n" + "	//@ set x = 10;\n" + "  }\n" + "}\n" + "public class X extends Y {" + "  public void m() {\n" + "      //@ assume x == 20;\n" + "  }\n" + "}\n", "new X().m()", null, JMLAssumeError.class);
    }

    public void test_0001_assert_with_label() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "   public void m() {\n" + "		//@ assert false: \"False error\";" + "   }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_0002_assert_with_label() {
        compileAndExecGivenStatement("X.java", "public class X{\n" + "   public void m() {\n" + "		//@ assert true: new String(\"False error\");" + "   }\n" + "}\n", "new X().m()");
    }

    public void test_0003_assert_with_label() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "   public void m() {\n" + "		//@ assert false: new String(\"False error\");" + "   }\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_0004_assert_with_label() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "   public void m() {\n" + "		//@ assert false: X.toLabel();" + "   }\n" + "	public static String toLabel(){\n" + "		return new String(\"assert with false\");\n" + "	}\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_0005_assert_with_label() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "   public void m() {\n" + "		//@ assert false: this.toLabel().toString();" + "   }\n" + "	public StringBuffer toLabel(){\n" + "		return new StringBuffer(\"assert with false\");\n" + "	}\n" + "}\n", "new X().m()", null, JMLAssertError.class);
    }

    public void test_0006_assert_with_label() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "   public void m() {\n" + "		//@ assume false: \"False error\";" + "   }\n" + "}\n", "new X().m()", null, JMLAssumeError.class);
    }

    public void test_0007_assert_with_label() {
        compileAndExecGivenStatement("X.java", "public class X{\n" + "   public void m() {\n" + "		//@ assume true: new String(\"False error\");" + "   }\n" + "}\n", "new X().m()");
    }

    public void test_0008_assert_with_label() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "   public void m() {\n" + "		//@ assume false: new String(\"False error\");" + "   }\n" + "}\n", "new X().m()", null, JMLAssumeError.class);
    }

    public void test_0009_assert_with_label() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "   public void m() {\n" + "		//@ assume false: X.toLabel();" + "   }\n" + "	public static String toLabel(){\n" + "		return new String(\"assert with false\");\n" + "	}\n" + "}\n", "new X().m()", null, JMLAssumeError.class);
    }

    public void test_0010_assert_with_label() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "   public void m() {\n" + "		//@ assume false: this.toLabel().toString();" + "   }\n" + "	public StringBuffer toLabel(){\n" + "		return new StringBuffer(\"assert with false\");\n" + "	}\n" + "}\n", "new X().m()", null, JMLAssumeError.class);
    }

    public void test_assume_exception_01() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "   public void m() {\n" + "       int x = 1, y = 0;\n" + "		//@ assume x/y == 1;\n" + "   }\n" + "}\n", "new X().m()", null, JMLEvaluationError.class);
    }

    public void test_assume_exception_02() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X{\n" + "   public void m() {\n" + "       int x = 1, y = 0;\n" + "		//@ assert x/y == 1;\n" + "   }\n" + "}\n", "new X().m()", null, JMLEvaluationError.class);
    }
}
