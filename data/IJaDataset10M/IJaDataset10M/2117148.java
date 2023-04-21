package org.jmlspecs.eclipse.jdt.core.tests.rac;

import org.jmlspecs.jml4.rac.runtime.JMLInternalNormalPostconditionError;
import org.jmlspecs.jml4.rac.runtime.JMLInternalPreconditionError;
import org.jmlspecs.jml4.rac.runtime.JMLInvariantError;

public class NullityCheckTest extends RacTestCompiler {

    public NullityCheckTest(String name) {
        super(name);
    }

    /** default nullity checks */
    public void test_default_nullity_method_nospec_01() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class X {\n" + "  public int m(Object y) {\n" + "  	return 0;}\n" + "}\n", "new X().m(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_default_nullity_constructor_nospec_02() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class X {\n" + "  public X() {}\n" + "  public X(Object y) {}\n" + "}\n", "new X(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_default_nullity_method_nospec_03() {
        compileAndExecGivenStatement("X.java", "class X {\n" + "  public int m(Object y) {\n" + "  	return 0;}\n" + "}\n", "new X().m(new Object())");
    }

    public void test_default_nullity_constructor_nospec_04() {
        compileAndExecGivenStatement("X.java", "class X {\n" + "  public X() {}\n" + "  public X(Object y) {}\n" + "}\n", "new X(new Object())");
    }

    public void test_default_nullity_method_reqspec_05() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class X {\n" + "  //@ requires true;\n" + "  public int m(Object y) {\n" + "  	return 0;}\n" + "}\n", "new X().m(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_default_nullity_constructor_reqspec_06() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class X {\n" + "  public X() {}\n" + "  //@ requires true;\n" + "  public X(Object y) {}\n" + "}\n", "new X(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_default_nullity_method_enspec_07() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class X {\n" + "  //@ ensures true;\n" + "  public int m(Object y) {\n" + "  	return 0;}\n" + "}\n", "new X().m(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_default_nullity_constructor_enspec_08() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class X {\n" + "  public X() {}\n" + "  //@ ensures true;\n" + "  public X(Object y) {}\n" + "}\n", "new X(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_default_nullity_method_nospec_09() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class X {\n" + "  public int m(Object y, Object x) {\n" + "  	return 0;}\n" + "}\n", "new X().m(new Object(), null)", null, JMLInternalPreconditionError.class);
    }

    public void test_default_nullity_constructor_nospec_10() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class X {\n" + "  public X() {}\n" + "  public X(Object x, Object y) {}\n" + "}\n", "new X(new Object(), null)", null, JMLInternalPreconditionError.class);
    }

    public void test_default_nullity_method_nospec_11() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class X {\n" + "  public int m(int z, Object y, Object x) {\n" + "  	return 0;}\n" + "}\n", "new X().m(1, new Object(), null)", null, JMLInternalPreconditionError.class);
    }

    public void test_default_nullity_constructor_nospec_12() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class X {\n" + "  public X() {}\n" + "  public X(int z, Object x, Object y) {}\n" + "}\n", "new X(1, new Object(), null)", null, JMLInternalPreconditionError.class);
    }

    public void test_default_nullity_method_nospec_13() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class X {\n" + "  public int m(Object y, int z, Object x) {\n" + "  	return 0;}\n" + "}\n", "new X().m(new Object(), 1, null)", null, JMLInternalPreconditionError.class);
    }

    public void test_default_nullity_constructor_nospec_14() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class X {\n" + "  public X() {}\n" + "  public X(Object x, int z, Object y) {}\n" + "}\n", "new X(new Object(), 1, null)", null, JMLInternalPreconditionError.class);
    }

    /** explicit nullity checks */
    public void test_explicit_nullity_method_nospec_15() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class X {\n" + "  public int m(@NonNull Object y) {\n" + "  	return 0;}\n" + "}\n", "new X().m(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_explicit_nullity_constructor_nospec_16() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class X {\n" + "  public X() {}\n" + "  public X(@NonNull Object y) {}\n" + "}\n", "new X(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_explicit_nullable_method_nospec_17() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class X {\n" + "  public int m(@NonNull Object y) {\n" + "  	return 0;}\n" + "}\n", "new X().m(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_explicit_nullable_constructor_nospec_18() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class X {\n" + "  public X() {}\n" + "  public X(@NonNull Object y) {}\n" + "}\n", "new X(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_explicit_nullity_method_nospec_19() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class X {\n" + "  public int m(@Nullable Object y, @NonNull Object z) {\n" + "  	return 0;}\n" + "}\n", "new X().m(null, null)", null, JMLInternalPreconditionError.class);
    }

    public void test_explicit_nullity_constructor_nospec_20() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class X {\n" + "  public X() {}\n" + "  public X(@Nullable Object y, @NonNull Object z) {}\n" + "}\n", "new X(null, null)", null, JMLInternalPreconditionError.class);
    }

    public void test_explicit_nullity_return_method_nospec_21() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class X {\n" + "  //@ ensures \\result != null;\n" + "  public @NonNull Object m(@Nullable Object y, @NonNull Object z) {\n" + "  	return null;}\n" + "}\n", "new X().m(null, null)", null, JMLInternalPreconditionError.class);
    }

    public void test_default_nullity_return_method_nospec_22() {
        compileAndExecGivenStatement("X.java", "class X {\n" + "  public Object m(Object y) {\n" + "  	return null;}\n" + "}\n", "new X().m(new Object())");
    }

    public void test_explicit_nullity_return_method_nospec_23() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "class X {\n" + "  public @NonNull Object m(Object y) {\n" + "  	return new Object();}\n" + "}\n", "new X().m(new Object())");
    }

    public void test_explicit_nullity_return_method_nospec_24() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "class X {\n" + "  public @Nullable Object m(Object y) {\n" + "  	return null;}\n" + "}\n", "new X().m(new Object())");
    }

    public void test_explicit_nullity_return_method_nospec_24a() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class X {\n" + "  public @Nullable Object m(Object y) {\n" + "  	return new Object();}\n" + "}\n", "new X().m(new Object())", null, JMLInternalNormalPostconditionError.class);
    }

    public void test_explicit_nullity_return_method_reqspec_25() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class X {\n" + "  //@ requires true;\n" + "  public @NonNull Object m(Object y) {\n" + "  	return null;}\n" + "}\n", "new X().m(new Object())", null, JMLInternalNormalPostconditionError.class);
    }

    public void test_explicit_nullity_return_method_ensspec_26() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class X {\n" + "  //@ ensures true;\n" + "  public @NonNull Object m(Object y) {\n" + "  	return null;}\n" + "}\n", "new X().m(new Object())", null, JMLInternalNormalPostconditionError.class);
    }

    public void test_heavy_weight_27() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class X {\n" + "  /*@ public normal_behavior\n" + "	 @	requires true;\n" + "	 @*/\n" + "  protected int m(Object x) throws Exception{\n" + "	 return -1;\n" + "  }\n" + "}\n", "new X().m(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_heavy_weight_28() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class X {\n" + "  /*@ public behavior\n" + "	 @	requires true;\n" + "	 @*/\n" + "  protected int m(@NonNull Object x) throws Exception{\n" + "	 return -1;\n" + "  }\n" + "}\n", "new X().m(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_heavy_weight_29() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class X {\n" + "  /*@ public exceptional_behavior\n" + "	 @	requires true;\n" + "	 @*/\n" + "  protected int m(@Nullable Object x) throws Exception{\n" + "	 return -1;\n" + "  }\n" + "}\n", "new X().m(null)", null, JMLInternalNormalPostconditionError.class);
    }

    public void test_heavy_weight_30() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class X {\n" + "/*@ protected normal_behavior\n" + "  @ requires true;\n" + "  @ ensures false;\n" + "  @ also\n" + "  @ protected exceptional_behavior\n" + "  @ requires false;\n" + "  @ signals (Exception e) true;\n" + "  @*/\n" + "  protected int m(Object x) throws Exception{\n" + "	 return -1;\n" + "  }\n" + "}\n", "new X().m(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_classic_31() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "public class X {\n" + " /*@ public behavior\n" + "	@ {|\n" + "	@ requires !true;\n" + "	@ ensures \\result == new Object();\n" + "	@ signals (Exception e) false;\n" + "	@ also\n" + "	@ requires true;\n" + "	@ ensures false;\n" + "	@ signals (Exception e) true;\n" + "	@ |}\n" + "	@*/\n" + "   public Object top(@NonNull Object y) throws Exception{\n" + "    return new Object();" + "   }\n" + "}", "new X().top(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_classic_32() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + " /*@ public normal_behavior\n" + "	@ requires true;\n" + "	@ also\n" + "   @ public exceptional_behavior\n" + "	@ signals (Exception e) true;\n" + "	@*/\n" + "   public Object top(Object y) throws Exception{\n" + "    return new Object();" + "   }\n" + "}", "new X().top(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_classic_33() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + " /*@ also\n" + "	@ signals (Exception e) true;\n" + "	@*/\n" + "   public Object top(Object y) throws Exception{\n" + "    return new Object();" + "   }\n" + "}", "new X().top(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_classic_34() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "public class X {\n" + "/*@ also\n" + "  @   public normal_behavior\n" + "  @     requires true;\n" + "  @     ensures \\result == null;\n" + "  @ also\n" + "  @   public normal_behavior\n" + "  @     requires !true;\n" + "  @     ensures \\result == null;\n" + "  @*/\n" + "   public Object top(@Nullable Object y) throws Exception{\n" + "    return new Object();" + "   }\n" + "}", "new X().top(null)", null, JMLInternalNormalPostconditionError.class);
    }

    public void test_classic_35() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "/*@ public normal_behavior\n" + "  @   requires true;\n" + "  @   {|\n" + "  @      requires !true;\n" + "  @      assignable \\nothing;\n" + "  @      ensures \\result == null;\n" + "  @    also\n" + "  @      requires !false;\n" + "  @      assignable \\nothing;\n" + "  @      ensures \\result == new Object();\n" + "  @   |}\n" + "  @*/\n" + "   public Object top(Object x) throws Exception{\n" + "    return new Object();" + "   }\n" + "}", "new X().top(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_classic_36() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "/*@ also\n" + "  @ public normal_behavior\n" + "  @   {|\n" + "  @      requires !true;\n" + "  @      assignable \\nothing;\n" + "  @      ensures \\result == null;\n" + "  @    also\n" + "  @      requires !false;\n" + "  @      assignable \\nothing;\n" + "  @      ensures \\result == new Object();\n" + "  @   |}\n" + "  @*/\n" + "   public Object top(Object y) throws Exception{\n" + "    return new Object();" + "   }\n" + "}", "new X().top(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_classic_37() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + " /*@ behavior\n" + "	@ requires true;\n" + "	@ also\n" + "	@ signals (Exception e) true;\n" + "   @ also\n" + "	@ signals (Exception e) false;\n" + "	@*/\n" + "   public Object top(Object y) throws Exception{\n" + "    return new Object();" + "   }\n" + "}", "new X().top(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_heavy_weight_38() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class X {\n" + "  /*@ public normal_behavior\n" + "	 @	requires true;\n" + "	 @*/\n" + "  protected Object m(Object x) throws Exception{\n" + "	 return null;\n" + "  }\n" + "}\n", "new X().m(null)", null, JMLInternalPreconditionError.class);
    }

    public void test_heavy_weight_39() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class X {\n" + "  /*@ public behavior\n" + "	 @	requires true;\n" + "	 @*/\n" + "  protected @NonNull Object m(@NonNull Object x) throws Exception{\n" + "	 return null;\n" + "  }\n" + "}\n", "new X().m(new Object())", null, JMLInternalNormalPostconditionError.class);
    }

    public void test_heavy_weight_40() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class X {\n" + "  /*@ public exceptional_behavior\n" + "	 @	requires true;\n" + "	 @*/\n" + "  protected @Nullable Object m(@Nullable Object x) throws Exception{\n" + "	 return null;\n" + "  }\n" + "}\n", "new X().m(new Object())", null, JMLInternalPreconditionError.class);
    }

    public void test_heavy_weight_41() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class X {\n" + "/*@ protected normal_behavior\n" + "  @ requires true;\n" + "  @ ensures false;\n" + "  @ also\n" + "  @ protected exceptional_behavior\n" + "  @ requires false;\n" + "  @ signals (Exception e) true;\n" + "  @*/\n" + "  protected @NonNull Object m(Object x) throws Exception{\n" + "	 return null;\n" + "  }\n" + "}\n", "new X().m(new Object())", null, JMLInternalNormalPostconditionError.class);
    }

    public void test_classic_42() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "public class X {\n" + " /*@ public behavior\n" + "	@ {|\n" + "	@ requires !true;\n" + "	@ ensures true;\n" + "	@ signals (Exception e) false;\n" + "	@ also\n" + "	@ requires true;\n" + "	@ ensures true;\n" + "	@ signals (Exception e) true;\n" + "	@ |}\n" + "	@*/\n" + "   public Object top(@NonNull Object y) throws Exception{\n" + "    return new Object();" + "   }\n" + "}", "new X().top(new Object())");
    }

    public void test_classic_43() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "public class X {\n" + " /*@ public normal_behavior\n" + "	@ requires true;\n" + "	@ also\n" + "   @ public exceptional_behavior\n" + "	@ signals (Exception e) true;\n" + "	@*/\n" + "   public @Nullable Object top(Object y) throws Exception{\n" + "    return new Object();" + "   }\n" + "}", "new X().top(new Object())", null, JMLInternalNormalPostconditionError.class);
    }

    public void test_classic_44() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "public class X {\n" + " /*@ also\n" + "	@ signals (Exception e) true;\n" + "	@*/\n" + "   public @NonNull Object top(Object y) throws Exception{\n" + "    return new Object();" + "   }\n" + "}", "new X().top(new Object())");
    }

    public void test_classic_45() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "public class X {\n" + "/*@ also\n" + "  @   public normal_behavior\n" + "  @     requires true;\n" + "  @     ensures true;\n" + "  @ also\n" + "  @   public normal_behavior\n" + "  @     requires !true;\n" + "  @     ensures true;\n" + "  @*/\n" + "   public @Nullable Object top(@Nullable Object y) throws Exception{\n" + "    return new Object();" + "   }\n" + "}", "new X().top(null)", null, JMLInternalNormalPostconditionError.class);
    }

    public void test_classic_46() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "public class X {\n" + "/*@ public normal_behavior\n" + "  @   requires true;\n" + "  @   {|\n" + "  @      requires !true;\n" + "  @      assignable \\nothing;\n" + "  @      ensures true;\n" + "  @    also\n" + "  @      requires !false;\n" + "  @      assignable \\nothing;\n" + "  @      ensures true;\n" + "  @   |}\n" + "  @*/\n" + "   public @NonNull Object top(Object x) throws Exception{\n" + "    return new Object();" + "   }\n" + "}", "new X().top(new Object())");
    }

    public void test_classic_47() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "public class X {\n" + "/*@ also\n" + "  @ public normal_behavior\n" + "  @   {|\n" + "  @      requires !true;\n" + "  @      assignable \\nothing;\n" + "  @      ensures true;\n" + "  @    also\n" + "  @      requires !false;\n" + "  @      assignable \\nothing;\n" + "  @      ensures true;\n" + "  @   |}\n" + "  @*/\n" + "   public @NonNull Object top(Object y) throws Exception{\n" + "    return new Object();" + "   }\n" + "}", "new X().top(new Object())");
    }

    public void test_classic_48() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "public class X {\n" + " /*@ behavior\n" + "	@ requires true;\n" + "	@ also\n" + "	@ signals (Exception e) true;\n" + "   @ also\n" + "	@ signals (Exception e) false;\n" + "	@*/\n" + "   public @Nullable Object top(Object y) throws Exception{\n" + "    return new Object();" + "   }\n" + "}", "new X().top(new Object())", null, JMLInternalNormalPostconditionError.class);
    }

    public void test_implict_instance_invariant_field_49() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "  public Object x;\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_instance_invariant_field_50() {
        compileAndExecGivenStatement("X.java", "public class X {\n" + "  public Object x = new Object();\n" + "}\n", "new X()");
    }

    public void test_implict_static_invariant_field_51() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "public class X {\n" + "  public static Object x;\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_52() {
        compileAndExecGivenStatement("X.java", "public class X {\n" + "  public static Object x = new Object();\n" + "}\n", "new X()");
    }

    public void test_implict_instance_invariant_field_53() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "interface Y {\n" + "  public Object x = null;\n" + "}\n" + "public class X implements Y{ }\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_instance_invariant_field_54() {
        compileAndExecGivenStatement("X.java", "interface Y {\n" + "  public Object x = new Object();\n" + "}\n" + "public class X implements Y { }\n", "new X()");
    }

    public void test_implict_static_invariant_field_55() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "interface Y {\n" + "  public static Object x = null;\n" + "}\n" + "public class X implements Y { }\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_56() {
        compileAndExecGivenStatement("X.java", "interface Y {\n" + "  public static Object x = new Object();\n" + "}\n" + "public class X implements Y{ }\n", "new X()");
    }

    public void test_implict_instance_invariant_field_57() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "interface Z { public Object x = null; }\n" + "interface Y extends Z {\n" + "  public Object x = null;\n" + "}\n" + "public class X implements Y{ }\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_instance_invariant_field_58() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "interface Z { public Object x = null; }\n" + "interface Y extends Z {\n" + "  public Object x = new Object();\n" + "}\n" + "public class X implements Y { }\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_59() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "interface Z { public Object x = null; }\n" + "interface Y extends Z {\n" + "  public static Object x = null;\n" + "}\n" + "public class X implements Y { }\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_60() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "interface Z { public Object x = null; }\n" + "interface Y extends Z {\n" + "  public static Object x = new Object();\n" + "}\n" + "public class X implements Y{ }\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_instance_invariant_field_61() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class Y { public Object y; }\n" + "public class X extends Y{\n" + "  public Object x;\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_instance_invariant_field_62() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class Y { public Object y; }\n" + "public class X extends Y{\n" + "  public Object x = new Object();\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_63() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class Y { public Object y; }\n" + "public class X extends Y{\n" + "  public static Object x;\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_64() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class Y { public Object y; }\n" + "public class X extends Y{\n" + "  public static Object x = new Object();\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_instance_invariant_field_65() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "class Y { public Object y; }\n" + "public class X extends Y{\n" + "  public Object x;\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_instance_invariant_field_66() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "abstract class Y { public Object y; }\n" + "public class X extends Y{\n" + "  public Object x = new Object();\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_67() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "abstract class Y { public Object y; }\n" + "public class X extends Y{\n" + "  public static Object x;\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_68() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "abstract class Y { public Object y; }\n" + "public class X extends Y{\n" + "  public static Object x = new Object();\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_instance_invariant_field_69() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "public class X {\n" + "  public @NonNull Object x;\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_instance_invariant_field_70() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "public class X {\n" + "  public @NonNull Object x = new Object();\n" + "}\n", "new X()");
    }

    public void test_implict_static_invariant_field_71() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "public class X {\n" + "  public static Object x;\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_72() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "public class X {\n" + "  public static Object x = new Object();\n" + "}\n", "new X()");
    }

    public void test_implict_instance_invariant_field_73() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "interface Y {\n" + "  public @NonNull Object x = null;\n" + "}\n" + "public class X implements Y{ }\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_instance_invariant_field_74() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "interface Y {\n" + "  public @NonNull Object x = new Object();\n" + "}\n" + "public class X implements Y { }\n", "new X()");
    }

    public void test_implict_static_invariant_field_75() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "interface Y {\n" + "  public static Object x = null;\n" + "}\n" + "public class X implements Y { }\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_76() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "interface Y {\n" + "  public static Object x = new Object();\n" + "}\n" + "public class X implements Y{ }\n", "new X()");
    }

    public void test_implict_instance_invariant_field_77() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "interface Z { public @NonNull Object x = null; }\n" + "interface Y extends Z {\n" + "  public @NonNull Object x = null;\n" + "}\n" + "public class X implements Y{ }\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_instance_invariant_field_78() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "interface Z { public @NonNull Object x = null; }\n" + "interface Y extends Z {\n" + "  public @NonNull Object x = new Object();\n" + "}\n" + "public class X implements Y { }\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_79() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "interface Z { public @NonNull Object x = null; }\n" + "interface Y extends Z {\n" + "  public static Object x = null;\n" + "}\n" + "public class X implements Y { }\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_80() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "interface Z { public @NonNull Object x = null; }\n" + "interface Y extends Z {\n" + "  public static Object x = new Object();\n" + "}\n" + "public class X implements Y{ }\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_instance_invariant_field_81() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class Y { public @NonNull Object y; }\n" + "public class X extends Y{\n" + "  public @NonNull Object x;\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_instance_invariant_field_82() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class Y { public @NonNull Object y; }\n" + "public class X extends Y{\n" + "  public @NonNull Object x = new Object();\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_83() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class Y { public @NonNull Object y; }\n" + "public class X extends Y{\n" + "  public static Object x;\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_84() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class Y { public @NonNull Object y; }\n" + "public class X extends Y{\n" + "  public static Object x = new Object();\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_instance_invariant_field_85() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class Y { public @NonNull Object y; }\n" + "public class X extends Y{\n" + "  public @NonNull Object x;\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_instance_invariant_field_86() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "abstract class Y { public @NonNull Object y; }\n" + "public class X extends Y{\n" + "  public @NonNull Object x = new Object();\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_87() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "abstract class Y { public @NonNull Object y; }\n" + "public class X extends Y{\n" + "  public static Object x;\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_88() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "abstract class Y { public @NonNull Object y; }\n" + "public class X extends Y{\n" + "  public static Object x = new Object();\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_instance_invariant_field_89() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "public class X {\n" + "  public @Nullable Object x;\n" + "}\n", "new X()");
    }

    public void test_implict_instance_invariant_field_90() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "public class X {\n" + "  public @Nullable Object x = new Object();\n" + "}\n", "new X()");
    }

    public void test_implict_static_invariant_field_91() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "public class X {\n" + "  public static Object x;\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_92() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "public class X {\n" + "  public static Object x = new Object();\n" + "}\n", "new X()");
    }

    public void test_implict_instance_invariant_field_93() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "interface Y {\n" + "  public @Nullable Object x = null;\n" + "}\n" + "public class X implements Y{ }\n", "new X()");
    }

    public void test_implict_instance_invariant_field_94() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "interface Y {\n" + "  public @Nullable Object x = new Object();\n" + "}\n" + "public class X implements Y { }\n", "new X()");
    }

    public void test_implict_static_invariant_field_95() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "interface Y {\n" + "  public static Object x = null;\n" + "}\n" + "public class X implements Y { }\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_96() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "interface Y {\n" + "  public static Object x = new Object();\n" + "}\n" + "public class X implements Y{ }\n", "new X()");
    }

    public void test_implict_instance_invariant_field_97() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "interface Z { public @Nullable Object x = null; }\n" + "interface Y extends Z {\n" + "  public @Nullable Object x = null;\n" + "}\n" + "public class X implements Y{ }\n", "new X()");
    }

    public void test_implict_instance_invariant_field_98() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "interface Z { public @Nullable Object x = null; }\n" + "interface Y extends Z {\n" + "  public @Nullable Object x = new Object();\n" + "}\n" + "public class X implements Y { }\n", "new X()");
    }

    public void test_implict_static_invariant_field_99() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "interface Z { public @Nullable Object x = null; }\n" + "interface Y extends Z {\n" + "  public static Object x = null;\n" + "}\n" + "public class X implements Y { }\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_100() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "interface Z { public @Nullable Object x = null; }\n" + "interface Y extends Z {\n" + "  public static Object x = new Object();\n" + "}\n" + "public class X implements Y{ }\n", "new X()");
    }

    public void test_implict_instance_invariant_field_101() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "class Y { public @Nullable Object y; }\n" + "public class X extends Y{\n" + "  public @Nullable Object x;\n" + "}\n", "new X()");
    }

    public void test_implict_instance_invariant_field_102() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "class Y { public @Nullable Object y; }\n" + "public class X extends Y{\n" + "  public @Nullable Object x = new Object();\n" + "}\n", "new X()");
    }

    public void test_implict_static_invariant_field_103() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "class Y { public @Nullable Object y; }\n" + "public class X extends Y{\n" + "  public static Object x;\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_104() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "class Y { public @Nullable Object y; }\n" + "public class X extends Y{\n" + "  public static Object x = new Object();\n" + "}\n", "new X()");
    }

    public void test_implict_instance_invariant_field_105() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "class Y { public @Nullable Object y; }\n" + "public class X extends Y{\n" + "  public @Nullable Object x;\n" + "}\n", "new X()");
    }

    public void test_implict_instance_invariant_field_106() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "abstract class Y { public @Nullable Object y; }\n" + "public class X extends Y{\n" + "  public @Nullable Object x = new Object();\n" + "}\n", "new X()");
    }

    public void test_implict_static_invariant_field_107() {
        compileAndExecGivenStatementExpectRuntimeError("X.java", "import org.jmlspecs.annotation.*;\n" + "abstract class Y { public @Nullable Object y; }\n" + "public class X extends Y{\n" + "  public static Object x;\n" + "}\n", "new X()", null, JMLInvariantError.class);
    }

    public void test_implict_static_invariant_field_108() {
        compileAndExecGivenStatement("X.java", "import org.jmlspecs.annotation.*;\n" + "abstract class Y { public @Nullable Object y; }\n" + "public class X extends Y{\n" + "  public static Object x = new Object();\n" + "}\n", "new X()");
    }
}
