package tests;

public class typechecking extends TCBase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        options.put("-noPurityCheck", "");
    }

    /** Test something very simple with no errors*/
    public void testSomeJava() {
        helpTC(" class A {}");
    }

    /** Test scanning something very simple */
    public void testSomeJava2() {
        helpTC(" class A { int k = true; }", "/TEST.java:1: incompatible types\n  required: int\n  found:    boolean", 20);
    }

    /** Test scanning something very simple */
    public void testSomeJML() {
        helpTC(" class A { int k; boolean b; void m() { \n//@ assert k;\n}}", "/TEST.java:2: incompatible types\n  required: boolean\n  found:    int", 12);
    }

    public void testTypeArgs() {
        helpTC(" class A { int k; boolean b; <T> int mm() {} void m() { int t = this.<Integer>mm(); \n//@ assert <Object>\\old(k);\n}}", "/TEST.java:2: illegal start of expression", 20, "/TEST.java:2: Incorrectly formed or terminated assert statement near here", 20);
    }

    public void testOld1() {
        helpTC(" class A { int k; boolean b; void m() { \n//@ assert \\old;\n}}", "/TEST.java:2: A \\old expression must have an argument list", 12);
    }

    public void testOld2() {
        helpTC(" class A { int k; boolean b; void m() { \n//@ assert \\old();\n}}", "/TEST.java:2: A \\old expression expects just 1 or 2 argument, not 0", 16);
    }

    public void testOld2a() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\pre();\n}}", "/A.java:2: A \\pre expression expects just 1 argument, not 0", 16);
    }

    public void testOld3() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\old(k);\n}}", "/A.java:2: incompatible types\n  required: boolean\n  found:    int", 16);
    }

    public void testOld4() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\old(b);\n}}");
    }

    public void testOld5() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\pre(b,k);\n}}", "/A.java:2: A \\pre expression expects just 1 argument, not 2", 16, "/A.java:2: There is no label named k", 19);
    }

    public void testOld6() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\old(b,5);\n}}", "/A.java:2: The second argument of an \\old expression must be a simple identifier that is a label", 19);
    }

    public void testOld7() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\old(b,k);\n}}", "/A.java:2: There is no label named k", 19);
    }

    public void testOld8() {
        helpTCF("A.java", " class A { int k; boolean b; //@ requires \\old(b); \n void m() { }}", "/A.java:1: A \\old token with no label may not be present in a requires clause", 48);
    }

    public void testOld9() {
        helpTCF("A.java", " class A { int k; boolean b; //@ ensures \\old(b,k); \n void m() { }}", "/A.java:1: A \\old token with a label may not be present in a ensures clause", 47);
    }

    public void testOld10() {
        helpTCF("A.java", " class A { int k; boolean b; //@ requires \\pre(b); \n void m() { }}", "/A.java:1: A \\pre token may not be present in a requires clause", 48);
    }

    public void testOld11() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n k: k=1;\n //@ assert \\old(b,k);\n}}");
    }

    public void testOld12() {
        helpTCF("A.java", " class A { boolean b; void m() { \n k: {};\n //@ assert \\old(b,k);\n}}");
    }

    public void testMax() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\max(\\lockset);\n}}", "/A.java:2: incompatible types\n  required: boolean\n  found:    java.lang.Object", 16);
    }

    public void testMax1() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\max;\n}}", "/A.java:2: illegal start of type", 16);
    }

    public void testMax2() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\max();\n}}", "/A.java:2: A \\max expression expects just 1 argument, not 0", 16);
    }

    public void testMax3() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\max(k);\n}}", "/A.java:2: A \\max function expects an argument of type org.jmlspecs.lang.JMLSetType<E> rather than int", 17, "/A.java:2: incompatible types\n  required: boolean\n  found:    java.lang.Object", 16);
    }

    public void testMax5() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\max(b,k);\n}}", "/A.java:2: A \\max expression expects just 1 argument, not 2", 16, "/A.java:2: A \\max function expects an argument of type org.jmlspecs.lang.JMLSetType<E> rather than boolean", 17, "/A.java:2: incompatible types\n  required: boolean\n  found:    java.lang.Object", 16);
    }

    public void testType() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\type(A,k);\n}}", "/A.java:2: More than one argument or otherwise ill-formed type expression as argument of \\type", 19, "/A.java:2: incompatible types\n  required: boolean\n  found:    org.jmlspecs.utils.IJMLTYPE", 12);
    }

    public void testType2() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\type();\n}}", "/A.java:2: illegal start of type", 18, "/A.java:2: incompatible types\n  required: boolean\n  found:    org.jmlspecs.utils.IJMLTYPE", 12);
    }

    public void testType3() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\type(b);\n}}", "/A.java:2: cannot find symbol\n  symbol:   class b\n  location: class A", 18, "/A.java:2: incompatible types\n  required: boolean\n  found:    org.jmlspecs.utils.IJMLTYPE", 12);
    }

    public void testType4() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\type(true);\n}}", "/A.java:2: illegal start of type", 18, "/A.java:2: incompatible types\n  required: boolean\n  found:    org.jmlspecs.utils.IJMLTYPE", 12);
    }

    public void testType5() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\type(int);\n}}", "/A.java:2: incompatible types\n  required: boolean\n  found:    org.jmlspecs.utils.IJMLTYPE", 12);
    }

    public void testType6() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\type(int[][]);\n}}", "/A.java:2: incompatible types\n  required: boolean\n  found:    org.jmlspecs.utils.IJMLTYPE", 12);
    }

    public void testType7() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\type(Object);\n}}", "/A.java:2: incompatible types\n  required: boolean\n  found:    org.jmlspecs.utils.IJMLTYPE", 12);
    }

    public void testType8() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\type(java.lang.Object);\n}}", "/A.java:2: incompatible types\n  required: boolean\n  found:    org.jmlspecs.utils.IJMLTYPE", 12);
    }

    public void testType9() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\type(java.lang.Object[][]);\n}}", "/A.java:2: incompatible types\n  required: boolean\n  found:    org.jmlspecs.utils.IJMLTYPE", 12);
    }

    public void testType10() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\type(A);\n}}", "/A.java:2: incompatible types\n  required: boolean\n  found:    org.jmlspecs.utils.IJMLTYPE", 12);
    }

    public void testType11() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\type(void);\n}}", "/A.java:2: incompatible types\n  required: boolean\n  found:    org.jmlspecs.utils.IJMLTYPE", 12);
    }

    public void testType12() {
        helpTCF("A.java", " class A { int k; boolean b; void m() { \n//@ assert \\type(Void);\n}}", "/A.java:2: incompatible types\n  required: boolean\n  found:    org.jmlspecs.utils.IJMLTYPE", 12);
    }

    public void testTypeof() {
        helpTCF("A.java", " class A { int k; Boolean b; void m() { \n//@ assert \\typeof(b);\n}}", "/A.java:2: incompatible types\n  required: boolean\n  found:    org.jmlspecs.utils.IJMLTYPE", 19);
    }

    public void testResult() {
        helpTC(" class A { int k; Boolean b; void m() { \n//@ assert \\result;\n}}", "/TEST.java:2: A \\result expression may not be used in the specification of a method that returns void", 13, "/TEST.java:2: A \\result expression may not be in a assert clause", 13);
    }

    public void testResult3() {
        helpTCF("A.java", " public class A { int k; Boolean b;\n //@ ensures \\result;\n void m() { \n}}", "/A.java:2: A \\result expression may not be used in the specification of a method that returns void", 15);
    }

    public void testResult4() {
        helpTC(" class A { int k; Boolean b;\n //@ assert \\result;\n void m() { \n}}", "/TEST.java:2: The token assert is illegal or not implemented for a type or method clause (JmlParser.classOrInterfaceBodyDeclaration)", 6);
    }

    public void testResult2() {
        String s = " class A { int k; Boolean b;\n/*@ ensures \\result == 1; */\nboolean m() { \n return true;\n}}";
        helpTCF("A.java", s, "/A.java:2: incomparable types: boolean and int", 21);
    }

    public void testResult5() {
        String s = " class A { int k; Boolean b;\n/*@ ensures \\result == 1; */\n void m() { }}";
        helpTCF("A.java", s, "/A.java:2: A \\result expression may not be used in the specification of a method that returns void", 14);
    }

    /** Tests an input that gave bugs once before */
    public void testMisc1() {
        helpTC(" class A { /*@ ensures \\result     ; */\nboolean m() { \n//@ return true;\n}}", "/TEST.java:3: Expected a declaration or a JML construct inside the JML annotation here", 5);
    }

    public void testJmlTypes() {
        helpTCF("A.java", "public class A {  int i; /*@ ghost \\TYPE t; */ } ");
    }

    public void testJmlTypes0() {
        helpTCF("A.java", "public class A {  int i,j; /*@ ghost \\TYPE t,tt; */ } ");
    }

    public void testJmlTypes1() {
        helpTCF("A.java", "public class A {  /*@ ghost \\bigint i; model \\real r; ghost \\TYPE t; */ } ");
    }

    /** Missing model or ghost modifier */
    public void testJmlTypes2() {
        helpTCF("A.java", "public class A {  int i; /*@  \\TYPE t; */ } ", "/A.java:1: A declaration within a JML annotation must be either ghost or model", 37);
    }

    /** Wrong position model or ghost modifier */
    public void testJmlTypes3() {
        helpTCF("A.java", "import org.jmlspecs.annotation.*; public class A {  @Ghost int i; } ", "/A.java:1: A Java declaration (not within a JML annotation) may not be either ghost or model", 64);
    }

    public void testJmlTypes4() {
        helpTCF("A.java", "import org.jmlspecs.annotation.*; public class A {  /*@ @Ghost int i; */ } ");
    }

    public void testSubtype() {
        helpTCF("A.java", "public class A { Object o; /*@ ghost \\TYPE t; */Class c;\n//@ensures t <: t;\nvoid m() {}}");
    }

    public void testSubtype2() {
        helpTCF("A.java", "public class A { Object o; /*@ ghost \\TYPE t; */ Class c;\n//@ensures c <: c;\nvoid m() {}}");
    }

    public void testSubtype2a() {
        helpTCF("A.java", "public class A { Object o; /*@ ghost \\TYPE t; */ Class<Object> c;\n//@ensures c <: c;\nvoid m() {}}");
    }

    public void testSubtype2b() {
        helpTCF("A.java", "public class A { Object o; /*@ ghost \\TYPE t; */ Class<? extends Object> c;\n//@ensures c <: c;\nvoid m() {}}");
    }

    public void testSubtype3() {
        expectedExit = 0;
        helpTCF("A.java", "public class A { Object o; /*@ ghost \\TYPE t; */ Class<Object> c;\n//@ensures t <: \\typeof(o);\nvoid m() {}}");
    }

    public void testSubtype4() {
        expectedExit = 0;
        helpTCF("A.java", "public class A { Object o; /*@ ghost \\TYPE t; */ Class<Object> c;\n//@ensures o.getClass() <: Object.class;\nvoid m() {}}");
    }

    public void testSubtype5() {
        helpTCF("A.java", "public class A { Object o; /*@ ghost \\TYPE t; */ Class<Object> c;\n//@ensures t.erasure() <: c;\nvoid m() {}}");
    }

    public void testSubtype6() {
        helpTCF("A.java", "public class A { Object o; /*@ ghost \\TYPE t; */ Class<Object> c;\n//@ensures t <: 5;\nvoid m() {}}", "/A.java:2: The type of the arguments of the subtype operator (<:) must be either \\TYPE or java.lang.Class, not int", 17);
    }

    public void testSubtype7() {
        helpTCF("A.java", "public class A { Object o; /*@ ghost \\TYPE t; */ Class<Object> c;\n//@ensures true <: c;\nvoid m() {}}", "/A.java:2: The type of the arguments of the subtype operator (<:) must be either \\TYPE or java.lang.Class, not boolean", 12);
    }

    public void testMisplacedResult() {
        helpTCF("A.java", "public class A {  \n//@requires \\result == 0;\n int m() {return 0;}}", "/A.java:2: A \\result expression may not be in a requires clause", 14);
    }

    public void testSetComp() {
        helpTCF("A.java", "public class A {  \n java.util.Collection c; //@ invariant new JMLSetType { Integer i | c.contains(i) && i<10}; \n \n }", "/A.java:2: incompatible types\n  required: boolean\n  found:    org.jmlspecs.lang.JMLSetType", 55);
    }

    public void testSetCompA() {
        helpTCF("A.java", "public class A {  \n java.util.Collection c; //@ requires new JMLSetType { Integer i | c.contains(i) && i<10}; \n void m() {} \n }", "/A.java:2: incompatible types\n  required: boolean\n  found:    org.jmlspecs.lang.JMLSetType", 54);
    }

    public void testQuantifierA() {
        helpTCF("A.java", "public class A {  \n Object i; //@ ghost Object j; \n //@ requires m( (\\exists int i; 0 < i && i <10; m(i)) ); \n/*@pure*/boolean m(int k) { return false; }\n }", "/A.java:3: method m in class A cannot be applied to given types;\n  required: int\n  found: boolean\n  reason: actual argument boolean cannot be converted to int by method invocation conversion", 15);
    }

    public void testSetCompB() {
        helpTCF("A.java", "public class A {  \n java.util.Collection c; //@ ghost int k = new JMLSetType { Integer i | c.contains(i) && i<10}; \n void m() {} \n }", "/A.java:2: incompatible types\n  required: int\n  found:    org.jmlspecs.lang.JMLSetType", 59);
    }

    public void testSetCompB3() {
        helpTCF("A.java", "public class A {  boolean p; \n java.util.Collection c; //@ ghost Object k = new JMLSetType { Integer i | c.contains(i) && p<10}; \n void m() {} \n }", "/A.java:2: bad operand types for binary operator '<'\n  first type:  boolean\n  second type: int", 94);
    }

    public void testSetCompB2() {
        helpTCF("A.java", "public class A {  \n java.util.Collection c; //@ ghost Object k = new JMLSetType { Integer i | c.contains(i) && i<10}; \n void m() {} \n }");
    }

    public void testQuantifierB() {
        helpTCF("A.java", "public class A {  \n  //@ ghost Object j = m( (\\exists int i; 0 < i && i <10; m(i)) ); \nboolean m(int k) { return false; }\n }", "/A.java:2: method m in class A cannot be applied to given types;\n  required: int\n  found: boolean\n  reason: actual argument boolean cannot be converted to int by method invocation conversion", 24);
    }

    public void testQuantifierB2() {
        helpTCF("A.java", "public class A {  \n  //@ ghost Object j = m( (\\exists int i; 0 < i && i <10; m(i)) ); \nboolean m(boolean k) { return false; } boolean m(int p) { return false; }\n }");
    }

    public void testQuantifierB3() {
        helpTCF("A.java", "public class A {  \n  //@ ghost Object j = m( (\\exists int i; 0 < i && i <10; m(i)) ); \nboolean m(boolean k) { return false; } \n }", "/A.java:2: method m in class A cannot be applied to given types;\n  required: boolean\n  found: int\n  reason: actual argument int cannot be converted to boolean by method invocation conversion", 59);
    }

    public void testQuantifierB4() {
        helpTCF("A.java", "public class A { boolean p;  \n  //@ ghost boolean j = ( (\\exists int i; 0 < i && i <10; m(p)) ); \nboolean m(int k) { return false; } \n }", "/A.java:2: method m in class A cannot be applied to given types;\n  required: int\n  found: boolean\n  reason: actual argument boolean cannot be converted to int by method invocation conversion", 59);
    }

    public void testSetCompC() {
        helpTCF("A.java", "public class A {  \n java.util.Collection c;  void m() { //@ ghost int k = new JMLSetType { Integer i | c.contains(i) && i<10}; \n} \n }", "/A.java:2: incompatible types\n  required: int\n  found:    org.jmlspecs.lang.JMLSetType", 71);
    }

    public void testSetCompC3() {
        helpTCF("A.java", "public class A {  \n java.util.Collection c;  void m() { boolean p; //@ ghost Object k = new JMLSetType { Integer i | c.contains(i) && p<10}; \n} \n }", "/A.java:2: bad operand types for binary operator '<'\n  first type:  boolean\n  second type: int", 117);
    }

    public void testSetCompC2() {
        helpTCF("A.java", "public class A {  \n java.util.Collection c;  void m() { //@ ghost Object k = new JMLSetType { Integer i | c.contains(i) && i<10}; \n} \n }");
    }

    public void testQuantifierC() {
        helpTCF("A.java", "public class A {  \n  boolean m(int k) { //@ ghost Object j = m( (\\exists int i; 0 < i && i <10; m(i)) ); \n return false; }\n }", "/A.java:2: method m in class A cannot be applied to given types;\n  required: int\n  found: boolean\n  reason: actual argument boolean cannot be converted to int by method invocation conversion", 43);
    }

    public void testQuantifierC2() {
        helpTCF("A.java", "public class A {  \n  boolean m(int k) { //@ ghost boolean j = ( (\\exists int i; 0 < i && i <10; m(i)) ); \n return false; }\n }");
    }

    public void testQuantifierC3() {
        helpTCF("A.java", "public class A {  \n  boolean m(int k) { boolean p ; //@ ghost boolean j = ( (\\exists int i; 0 < i && i <10; m(p)) ); \n return false; }\n }", "/A.java:2: method m in class A cannot be applied to given types;\n  required: int\n  found: boolean\n  reason: actual argument boolean cannot be converted to int by method invocation conversion", 90);
    }

    public void testSetCompD() {
        helpTCF("A.java", "public class A {//@ ghost Object k;  \n java.util.Collection c;  void m() { //@ set k = new JMLSetType { Integer i | c.contains(i) && i<10}; \n} \n }");
    }

    public void testQuantifierD() {
        helpTCF("A.java", "public class A { //@ ghost int j;\n  \n  boolean m(int k) { //@ set j = m( (\\exists int i; 0 < i && i <10; m(i)) ); \n return false; }\n }", "/A.java:3: method m in class A cannot be applied to given types;\n  required: int\n  found: boolean\n  reason: actual argument boolean cannot be converted to int by method invocation conversion", 34);
    }

    public void testQuantifier() {
        helpTCF("A.java", "public class A {  \n Object i; //@ ghost Object j; \n /*@pure*/ boolean m(int i) { return false; }\n//@ invariant m( (\\exists int i; 0 < i && i <10; m(i)) ); \n }", "/A.java:4: method m in class A cannot be applied to given types;\n  required: int\n  found: boolean\n  reason: actual argument boolean cannot be converted to int by method invocation conversion", 15);
    }

    public void testQuantifier1() {
        helpTCF("A.java", "public class A {  \n Object i; //@ ghost Object j; \n /*@pure*/ boolean m(int i) { return false; }\n//@ invariant m( (\\forall int i; 0 < i && i <10; m(i)) ); \n }", "/A.java:4: method m in class A cannot be applied to given types;\n  required: int\n  found: boolean\n  reason: actual argument boolean cannot be converted to int by method invocation conversion", 15);
    }

    public void testQuantifier2() {
        helpTCF("A.java", "public class A {  \n Object i; //@ ghost Object j; \n /*@pure*/ boolean m(int i) { return false; }\n//@ invariant (\\num_of int i; 0 < i && i <10; m(i)) ; \n }", "/A.java:4: incompatible types\n  required: boolean\n  found:    int", 16);
    }

    public void testQuantifier3() {
        helpTCF("A.java", "public class A {  \n Object i; //@ ghost Object j; \n boolean m(int i) { return false; }\n//@ invariant (\\max long i; 0 < i && i <10; i) ; \n }", "/A.java:4: incompatible types\n  required: boolean\n  found:    long", 16);
    }

    public void testQuantifier4() {
        helpTCF("A.java", "public class A {  \n Object i; //@ ghost Object j; \n boolean m(float i) { return false; }\n//@ invariant (\\sum long i; 0 < i && i <10; i) ; \n }", "/A.java:4: incompatible types\n  required: boolean\n  found:    long", 16);
    }

    public void testQuantifier5() {
        helpTCF("A.java", "public class A {  \n Object i; //@ ghost Object j; \n boolean m(double i) { return false; }\n//@ invariant (\\product long i,k; 0 < i && k <10; i) ; \n }", "/A.java:4: incompatible types\n  required: boolean\n  found:    long", 16);
    }

    public void testQuantifier6() {
        helpTCF("A.java", "public class A {  \n Object i; Object q = i; //@ ghost Object j; \n boolean m(double i) { return false; }\n//@ invariant (\\product long i; j; i) ; \n }", "/A.java:4: incompatible types\n  required: boolean\n  found:    java.lang.Object", 33, "/A.java:4: incompatible types\n  required: boolean\n  found:    long", 16);
    }

    public void testQuantifier7() {
        helpTCF("A.java", "public class A {  \n Object i; Object j; \n boolean m(double i) { return false; }\n//@ invariant (\\product long i; 0 < j && i <10; i) ; \n }", "/A.java:4: bad operand types for binary operator '<'\n  first type:  int\n  second type: java.lang.Object", 35, "/A.java:4: incompatible types\n  required: boolean\n  found:    long", 16);
    }

    public void testSame() {
        helpTCF("A.java", "public class A { //@ requires  i; also requires \\same; \n boolean m(boolean i) { return false; }\n}");
    }

    public void testSame1() {
        helpTCF("A.java", "public class A { //@ requires 1+\\same; \n boolean m(double i) { return false; }\n}", "/A.java:1: bad operand types for binary operator '+'\n  first type:  int\n  second type: boolean", 32);
    }

    public void testSame2() {
        helpTCF("A.java", "public class A { //@ requires i; also requires !\\same; \n boolean m(boolean i) { return false; }\n}");
    }

    public void testSame3() {
        helpTCF("A.java", "public class A { //@ requires \\same; \n boolean m(double i) { return false; }\n}");
    }

    public void testSame4() {
        helpTCF("A.java", "public class A { //@ ensures \\same; \n boolean m(double i) { return false; }\n}", "/A.java:1: A \\same token may only be used in requires clauses", 30);
    }

    public void testLockCompare() {
        expectedExit = 0;
        helpTCF("A.java", "public class A { Object o,oo; //@ invariant o < oo; \n }", "/A.java:1: warning: Operators < and <= are deprecated as lock comparisons - use <# and <#= instead", 47);
    }

    public void testLockCompareX() {
        helpTCF("A.java", "public class A { Integer o,oo; //@ invariant o < oo; \n }");
    }

    public void testLockCompare1() {
        expectedExit = 0;
        helpTCF("A.java", "public class A { Object o,oo; //@ invariant o <= oo; \n }", "/A.java:1: warning: Operators < and <= are deprecated as lock comparisons - use <# and <#= instead", 47);
    }

    public void testLockCompare1X() {
        helpTCF("A.java", "public class A { Integer o,oo; //@ invariant o <= oo; \n }");
    }

    public void testLockCompare2() {
        helpTCF("A.java", "public class A { Object o,oo; int i; //@ invariant o < true; \n }", "/A.java:1: bad operand types for binary operator '<'\n  first type:  java.lang.Object\n  second type: boolean", 54);
    }

    public void testLockCompare2X() {
        helpTCF("A.java", "public class A { Integer o,oo; int i; //@ invariant o < 5; \n }");
    }

    public void testLockCompare2Y() {
        helpTCF("A.java", "public class A { Object o,oo; int i; //@ invariant o < 5; \n }", "/A.java:1: bad operand types for binary operator '<'\n  first type:  java.lang.Object\n  second type: int", 54);
    }

    public void testLockCompare3() {
        helpTCF("A.java", "public class A { Object o,oo; boolean b = o <= oo;  \n }", "/A.java:1: bad operand types for binary operator '<='\n  first type:  java.lang.Object\n  second type: java.lang.Object", 45);
    }

    public void testLockCompare4() {
        helpTCF("A.java", "public class A { Object o,oo; boolean b = o <= oo;  \n }", "/A.java:1: bad operand types for binary operator '<='\n  first type:  java.lang.Object\n  second type: java.lang.Object", 45);
    }

    public void testLockCompareA() {
        helpTCF("A.java", "public class A { Object o,oo; //@ invariant o <# oo; \n }");
    }

    public void testLockCompare1A() {
        helpTCF("A.java", "public class A { Object o,oo; //@ invariant o <#= oo; \n }");
    }

    public void testFreshBad() {
        helpTCF("A.java", "public class A { Object o,oo; //@ invariant \\fresh(o);  \n }", "/A.java:1: A \\fresh expression may not be in a invariant clause", 52);
    }

    public void testFresh() {
        helpTCF("A.java", "public class A { Object o,oo; //@ ensures \\fresh(o); \n void m() {} \n }");
    }

    public void testFresh2() {
        helpTCF("A.java", "public class A { Object o,oo; //@ ensures \\fresh(o,oo); \n void m() {}  \n }");
    }

    public void testFresh3() {
        helpTCF("A.java", "public class A { Object o,oo; //@ ensures \\fresh(); \n void m() {}  \n }");
    }

    public void testFresh4() {
        helpTCF("A.java", "public class A { int i; Object o,oo; //@ ensures   \\fresh(i); \n void m() {}  \n }", "/A.java:1: The argument of \\fresh must be of reference type", 59);
    }

    public void testFresh5() {
        helpTCF("A.java", "public class A { int i; Object o,oo; //@ ensures   \\fresh(o) + 1 == 0; \n void m() {}  \n }", "/A.java:1: bad operand types for binary operator '+'\n  first type:  boolean\n  second type: int", 62);
    }

    public void testFresh5Bad() {
        helpTCF("A.java", "public class A { int i; Object o,oo; //@ ghost boolean k = \\fresh(o);  \n }", "/A.java:1: A \\fresh expression may not be in a jml declaration clause", 67);
    }

    public void testOnlyAssigned() {
        helpTCF("A.java", "public class A { Object o,oo; //@ invariant \\only_assigned(o) || \\only_accessed(o) || \\only_captured(o) || \\not_assigned(o) || \\not_modified(o);  \n }", "/A.java:1: A \\only_assigned expression may not be in a invariant clause", 46, "/A.java:1: A \\only_accessed expression may not be in a invariant clause", 67, "/A.java:1: A \\only_captured expression may not be in a invariant clause", 88, "/A.java:1: A \\not_assigned expression may not be in a invariant clause", 109, "/A.java:1: A \\not_modified expression may not be in a invariant clause", 129);
    }

    public void testOnlyAssigned1() {
        helpTCF("A.java", "public class A { Object o,oo; //@ ensures \\only_assigned(o) || \\only_accessed(o) || \\only_captured(o) || \\not_assigned(o) || \\not_modified(o); \n void m() {} \n }");
    }

    public void testOnlyAssigned2() {
        helpTCF("A.java", "public class A { int i; Object o,oo; //@ ghost boolean k = \\only_assigned(o) || \\only_accessed(o) || \\only_captured(o) || \\not_assigned(o) || \\not_modified(o);  \n }", "/A.java:1: A \\only_assigned expression may not be in a jml declaration clause", 61, "/A.java:1: A \\only_accessed expression may not be in a jml declaration clause", 82, "/A.java:1: A \\only_captured expression may not be in a jml declaration clause", 103, "/A.java:1: A \\not_assigned expression may not be in a jml declaration clause", 124, "/A.java:1: A \\not_modified expression may not be in a jml declaration clause", 144);
    }

    public void testInformalComment() {
        helpTCF("A.java", "public class A {\n //@ invariant (* stuff *);\n //@ ghost int k = (* stuff *);  \n }", "/A.java:3: incompatible types\n  required: int\n  found:    boolean", 20);
    }

    public void testModelImport1() {
        helpTCF("A.java", "//@ model import java.util.List;\n public class A {\n //@ ghost List k;\n List n;  \n }");
    }

    public void testModelImport2() {
        helpTCF("A.java", "import java.awt.*; //@ model import java.util.*;\n public class A {\n //@ ghost List k;\n List n;  \n }", "/A.java:4: reference to List is ambiguous, both interface java.util.List in java.util and class java.awt.List in java.awt match", 2, "/A.java:3: reference to List is ambiguous, both interface java.util.List in java.util and class java.awt.List in java.awt match", 12);
    }

    public void testModelImport3() {
        helpTCF("A.java", "import java.awt.*; import java.util.*;\n//@ model import java.util.List;\n public class A {\n //@ ghost List k;\n List n;  \n }");
    }

    public void testBadModelImport1() {
        helpTCF("A.java", "//@ import java.util.List;\n public class A {\n //@ ghost List k;\n List n;  \n }", "/A.java:1: An import statement in a JML comment must have a model modifier", 5);
    }

    public void testBadModelImport2() {
        helpTCF("A.java", "/*@ model */ import java.util.List;\n public class A {\n  \n }", "/A.java:1: A model import declaration must be completely within a JML comment", 5);
    }

    public void testBadModelImport2a() {
        helpTCF("A.java", "/*@ model */  public class A {\n  \n }", "/A.java:1: A Java declaration (not within a JML annotation) may not be either ghost or model", 22);
    }

    public void testBadModelImport3() {
        helpTCF("A.java", "/*@ model import */ java.util.List;\n public class A {\n  \n }", "/A.java:1: <identifier> expected", 17, "/A.java:1: package <error>.java.util does not exist", 30, "/A.java:1: package <error>.java.util does not exist", 30);
    }

    public void testEnum1() {
        helpTCF("A.java", "public class A {\n  enum E { X {} }; \n }");
    }

    public void testEnum2() {
        helpTCF("A.java", "public class A {\n  enum E { X {}; } \n }");
    }

    public void testEnum3() {
        helpTCF("A.java", "public class A {\n  public enum X { Y; X(){}; } \n }");
    }

    public void testEnum3a() {
        helpTCF("A.java", "public class A {\n  public enum X { Y; public X(){}; } \n }", "/A.java:2: modifier public not allowed here", 29);
    }

    public void testEnum3b() {
        helpTCF("A.java", "public class A {\n  public enum X { Y; protected X(){}; } \n }", "/A.java:2: modifier protected not allowed here", 32);
    }

    public void testEnum3c() {
        helpTCF("A.java", "public class A {\n  public enum X { Y; private X(){}; } \n }");
    }

    public void testEnum4() {
        helpTCF("A.java", "public class A {\n  public enum X { Y; public X m() { for (X c: values()) break; return Y; } } \n }");
    }

    public void testBug4() {
        helpTCF("A.java", "interface A<V> { /*@ instance ghost V r; @*/ \n }");
    }

    public void testBug5() {
        helpTCF("A.java", "public class A {\n" + "  public void test1(Object[] blub) {\n" + "    //@ loop_invariant 0<=i && i <= blub.length;\n" + "    for(int i=0; i< blub.length; i++) {\n" + "      /*@nullable @*/ Object b = blub[i];\n" + "      if (b == null)\n" + "        continue;\n" + "    }\n" + "  }\n" + "  public void test2(Object[] blub) {\n" + "    for(Object b : blub) {\n" + "      if (b == null)\n" + "        continue;\n" + "    }\n" + "  }\n" + "}");
    }

    public void testBug5a() {
        helpTCF("A.java", "public class A {\n" + "  public void test1(Object[] blub) {\n" + "    //@ loop_invariant 0<=i && i <= blub.length;\n" + "    for(int i=0; i< blub.length; i++) {\n" + "      /*@nullable @*/ Object b = blub[i];\n" + "      if (b == null)\n" + "        break;\n" + "    }\n" + "  }\n" + "  public void test2(Object[] blub) {\n" + "    for(Object b : blub) {\n" + "      if (b == null)\n" + "        break;\n" + "    }\n" + "  }\n" + "}");
    }

    public void testBug6() {
        helpTCF("Test.java", "public class Test {\n" + "private final int my_height; /*@ in height; @*/\n" + "  /*@ public model int height;\n" + "      in_redundantly height;\n" + "      public invariant 0 < height;\n" + "      public constraint \\old(height) == height;\n" + "      private represents height = my_height;\n" + "      private invariant 0 < my_height;\n" + "  @*/\n" + "  public Test() {\n" + "    my_height = 1;\n" + "  }\n" + "}\n");
    }

    public void testBug6a() {
        helpTCF("Test.java", "public class Test {\n" + "private final int my_height; /*@ in height; @*/\n" + "  /*@ public model int height;\n" + "      in_redundantly height2;\n" + "  @*/\n" + "  /*@ public model int height2;\n" + "      in_redundantly height;\n" + "  @*/\n" + "  public Test() {\n" + "    my_height = 1;\n" + "  }\n" + "}\n", "/Test.java:6: This field participates in a circular datagroup inclusion chain: height2", 24);
    }

    public void testSwitchWithStrings() {
        helpTCF("A.java", " class A { public void m(String s) { switch (s) { case \"David\": case \"Cok\": System.out.println(\"me\"); break; default: System.out.println(\"not me\"); } } }");
    }

    public void testMultiCatch() {
        helpTCF("A.java", "public class A { public void m(int i) { try { if (i == 0) throw new ArrayIndexOutOfBoundsException(); if (i == 1) throw new NullPointerException(); } catch ( final ArrayIndexOutOfBoundsException | NullPointerException e) {}  } }");
    }

    public void testTryWithResources() {
        helpTCF("A.java", "import java.io.*; public class A { public void m(int i) { try ( FileReader r = new FileReader(\"\") ) {   } catch (final IOException e) {} finally {} } }");
    }

    public void testJmlLabelExpression() {
        helpTCF("TestJava.java", "package tt; \n" + "public class TestJava { \n" + "  public int m1bad(boolean b, int k) {\n" + "    int j = 0;\n" + "    //@ ghost boolean bb = (\\forall int i; 0<=i && i <=4; 0!=(\\lbl LBL i));\n" + "    return 1;\n" + "  }\n" + "}", "/TestJava.java:5: A JML label expression may not be within a quantified or set-comprehension expression", 63);
    }
}
