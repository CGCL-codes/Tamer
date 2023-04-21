package tests;

/** Tests emitting counterexample information and tracing, though the text
 * of the output is not actually checked - needs visual observation.
 * @author David Cok
 *
 */
public class counterexamples extends EscBase {

    protected void setUp() throws Exception {
        super.setUp();
        options.put("-noPurityCheck", "");
        options.put("-trace", "");
    }

    /** Tests an explicit assertion */
    public void testCE1() {
        helpTCX("tt.TestJava", "package tt; \n" + "public class TestJava { \n" + "  //@ requires k > 0;\n" + "  public void m1(int k) {\n" + "    //@ assert k == 0;\n" + "  }\n" + "}", "/tt/TestJava.java:5: warning: The prover cannot establish an assertion (Assert) in method m1", 9);
    }

    /** Tests a postcondition */
    public void testCE2() {
        helpTCX("tt.TestJava", "package tt; \n" + "public class TestJava { \n" + "  //@ requires k > 0;\n" + "  //@ ensures \\result < 0;\n" + "  public int m1(int k) {\n" + "    return k;\n" + "  }\n" + "}", "/tt/TestJava.java:6: warning: The prover cannot establish an assertion (Postcondition) in method m1", 5, "/tt/TestJava.java:4: warning: Associated declaration", 7);
    }

    /** Tests a called precondition and method and constructor arguments */
    public void testCE3() {
        helpTCX("tt.TestJava", "package tt; \n" + "public class TestJava { \n" + "  public TestJava(int i) {}\n" + "  public void m1(int k) {\n" + "    c1(k,k!=0);\n" + "    TestJava j = new TestJava(2+3);" + "    (k==0?this:j).m1(0);" + "  }\n" + "  //@ requires k == 0;\n" + "  public void c1(int k, boolean b) {};\n" + "}", "/tt/TestJava.java:5: warning: The prover cannot establish an assertion (Precondition) in method m1", 7, "/tt/TestJava.java:7: warning: Associated declaration", 16);
    }

    /** Tests assignments */
    public void testCE4() {
        helpTCX("tt.TestJava", "package tt; \n" + "public class TestJava { \n" + "  int j; static int sj; static TestJava t;\n" + "  public TestJava(int i) {}\n" + "  //@ requires t != null;\n" + "  public void m1(Object[] c) {\n" + "    int k; boolean b;\n" + "    //@ assume c != null && c.length == 10;\n" + "    k = 8;\n" + "    k += 8;\n" + "    k += (j+=7);\n" + "    b = k > 8;\n" + "    c[4] = t;\n" + "    c[0] = c[3];\n" + "    t.j = 9;\n" + "    t.sj = 10;\n" + "    TestJava.sj = 11;\n" + "    //@ assert false;\n" + "  }\n" + "}\n", "/tt/TestJava.java:18: warning: The prover cannot establish an assertion (Assert) in method m1", 9);
    }

    /** Tests if statements */
    public void testCE5() {
        helpTCX("tt.TestJava", "package tt; \n" + "public class TestJava { \n" + "  int j; static int sj; static TestJava t;\n" + "  public TestJava(int i) {}\n" + "  //@ requires j == 0;\n" + "  public void m1(int j) {\n" + "    if (j==0) j=1;\n" + "    if (j == 2) j=7;\n" + "    if (j == 3) j=6;\n" + "    else if (j==4) j=7;\n" + "    else if (j==1) j=10;\n" + "    else j=80;\n" + "    //@ assert false;\n" + "  }\n" + "}\n", "/tt/TestJava.java:13: warning: The prover cannot establish an assertion (Assert) in method m1", 9);
    }

    /** Tests loops */
    public void testCE6() {
        helpTCX("tt.TestJava", "package tt; \n" + "public class TestJava { \n" + "  //@ requires j > 0;\n" + "  public void m1(int j) {\n" + "    int n = 0;\n" + "    //@ loop_invariant 0<=i && i <=j;\n" + "    //@ loop_invariant n == i;\n" + "    for (int i=0; i<j; i++) {\n" + "        n = n+1;\n" + "        //@ assert n != 20;\n" + "    }\n" + "  }\n" + "  //@ requires j > 0;\n" + "  public void m2(int j) {\n" + "    int n = 0;\n" + "    //@ loop_invariant 0<=i && i <=j;\n" + "    //@ loop_invariant n == i;\n" + "    for (int i=0; i<j; i++) {\n" + "        n = n+1;\n" + "    }\n" + "    //@ assert n == -10;\n" + "  }\n" + "}\n", "/tt/TestJava.java:10: warning: The prover cannot establish an assertion (Assert) in method m1", 13, "/tt/TestJava.java:21: warning: The prover cannot establish an assertion (Assert) in method m2", 9);
    }

    /** Tests pure methods */
    public void testCE7() {
        helpTCX("tt.TestJava", "package tt; \n" + "public class TestJava { \n" + "  public void m1() {\n" + "      //@ assert c(2) != -2;\n" + "  }\n" + "  public void m2() {\n" + "      //@ assert cc(2) != -3;\n" + "  }\n" + "  public void m3() {\n" + "      //@ assert b();\n" + "  }\n" + "  public void m4() {\n" + "      //@ assert bb(0);\n" + "  }\n" + "  //@ normal_behavior requires z > 0; ensures \\result < 0;\n" + "  /*@ pure */ public int c(int z) {\n" + "      return -z;\n" + "  }\n" + "  //@ normal_behavior requires z > 0; ensures \\result == -z;\n" + "  /*@ pure */ public int cc(int z) {\n" + "      return -z;\n" + "  }\n" + "  //@ normal_behavior requires true; \n" + "  /*@ pure */ static public boolean b() {\n" + "      return true;\n" + "  }\n" + "  //@ normal_behavior requires true; \n" + "  /*@ pure */ static public boolean bb(int z) {\n" + "      return true;\n" + "  }\n" + "}\n", "/tt/TestJava.java:4: warning: The prover cannot establish an assertion (Assert) in method m1", 11, "/tt/TestJava.java:10: warning: The prover cannot establish an assertion (Assert) in method m3", 11, "/tt/TestJava.java:13: warning: The prover cannot establish an assertion (Assert) in method m4", 11);
    }

    /** Tests alternate returns */
    public void testCE8() {
        helpTCX("tt.TestJava", "package tt; \n" + "public class TestJava { \n" + "  //@ requires i>=0; ensures \\result==0;\n" + "  public int m1(int i) {\n" + "      if (i==0) return 0;\n" + "      if (i==1) return i+20;\n" + "      if (i==2) return 0;\n" + "      if (i==3) return 0;\n" + "      return 0;\n" + "  }\n" + "  static int k;\n" + "  //@ requires i>=0; ensures k == 0;\n" + "  public void m2(int i) {\n" + "      k = 0;\n" + "      if (i==0) return ;\n" + "      if (i==1) return ;\n" + "      if (i==2) { k = 1; return ;}\n" + "      if (i==3) return ;\n" + "      return ;\n" + "  }\n" + "}\n", "/tt/TestJava.java:6: warning: The prover cannot establish an assertion (Postcondition) in method m1", 17, "/tt/TestJava.java:3: warning: Associated declaration", 22, "/tt/TestJava.java:17: warning: The prover cannot establish an assertion (Postcondition) in method m2", 26, "/tt/TestJava.java:12: warning: Associated declaration", 22);
    }

    /** Tests switch statements */
    public void testCE9() {
        helpTCX("tt.TestJava", "package tt; \n" + "public class TestJava { \n" + "  //@ requires i>=0; ensures \\result==0;\n" + "  public int m1(int i) {\n" + "      int r = 0;\n" + "      switch (i+5) {\n" + "        case 3: r = 0; break;\n" + "        case 5: r = 4; break;\n" + "        case 7: r = 0; break;\n" + "      }\n" + "      return r;\n" + "  }\n" + "  //@ requires i>=0; ensures \\result==0;\n" + "  public int m2(int i) {\n" + "      int r = 0;\n" + "      switch (i+5) {\n" + "        case 3: r = 0; break;\n" + "        case 5: r = 4; break;\n" + "        case 7: r = 0; break;\n" + "        default: r = 0; break;\n" + "      }\n" + "      return r;\n" + "  }\n" + "}\n", "/tt/TestJava.java:11: warning: The prover cannot establish an assertion (Postcondition) in method m1", 7, "/tt/TestJava.java:3: warning: Associated declaration", 22, "/tt/TestJava.java:22: warning: The prover cannot establish an assertion (Postcondition) in method m2", 7, "/tt/TestJava.java:13: warning: Associated declaration", 22);
    }

    /** Tests called method return */
    public void testCE10() {
        helpTCX("tt.TestJava", "package tt; \n" + "public class TestJava { \n" + "  //@ normal_behavior requires i>=0; ensures k==0;\n" + "  public void m1(int i) {\n" + "      cc(i);\n" + "  }\n" + "  //@ requires i>=0; ensures k==1;\n" + "  public void m2(int i) {\n" + "      cc(i);\n" + "  }\n" + "  int k;\n" + "  //@ requires i>0; \n" + "  //@ ensures k==0;\n" + "  //@ signals (Exception e) false;\n" + "  //@ also requires i==0; \n" + "  //@ ensures false;\n" + "  //@ signals (RuntimeException e) k==1;\n" + "  public void cc(int i) {\n" + "      k=1; if (i==0) throw new RuntimeException();\n" + "      k=0; return ;\n" + "  }\n" + "}\n", "/tt/TestJava.java:5: warning: The prover cannot establish an assertion (ExceptionalPostcondition) in method m1", 9, "/tt/TestJava.java:3: warning: Associated declaration", 7, "/tt/TestJava.java:8: warning: The prover cannot establish an assertion (Postcondition) in method m2", 15, "/tt/TestJava.java:7: warning: Associated declaration", 22);
    }

    /** Tests method calls in expressions */
    public void testCE11() {
        helpTCX("tt.TestJava", "package tt; \n" + "public class TestJava { \n" + "  public void m1(int i) {\n" + "      int k = c(i) + c(i-15);\n" + "      //@ assert k != 9;\n" + "  }\n" + "  //@ signals (Exception) false;\n" + "  public void m2(int i) {\n" + "      int k = c(i);\n" + "      //@ assert true;\n" + "  }\n" + "  //@ ensures \\result == i+10;\n" + "  public int c(int i) {\n" + "    return i+10;\n" + "  }\n" + "}\n", "/tt/TestJava.java:5: warning: The prover cannot establish an assertion (Assert) in method m1", 11, "/tt/TestJava.java:9: warning: The prover cannot establish an assertion (ExceptionalPostcondition) in method m2", 16, "/tt/TestJava.java:7: warning: Associated declaration", 7);
    }

    /** Tests misc statements: synchronized, block */
    public void testCE12() {
        helpTCX("tt.TestJava", "package tt; \n" + "public class TestJava { \n" + "  public void m1(int i) {\n" + "      int k= 0;\n" + "      synchronized (this) {k=1;}\n" + "      { k = 2; k = 3+k;}\n" + "      //@ assert false;\n" + "  }\n" + "}\n", "/tt/TestJava.java:7: warning: The prover cannot establish an assertion (Assert) in method m1", 11);
    }

    /** Tests initializations */
    public void testCE13() {
        helpTCX("tt.TestJava", "package tt; \n" + "public class TestJava { \n" + "  public void m1(int i) {\n" + "      int k = 0;\n" + "      int[] a = {1,2,3,4};\n" + "      int[][] b = new int[2][3];\n" + "      int[][] c = new int[2][];\n" + "      int[][] d = new int[][]{{1},{2,3}};\n" + "      //@ assert false;\n" + "  }\n" + "}\n", "/tt/TestJava.java:9: warning: The prover cannot establish an assertion (Assert) in method m1", 11);
    }

    /** Tests JML statements */
    public void testCE14() {
        options.put("-bb", "");
        helpTCX("tt.TestJava", "package tt; \n" + "public class TestJava { \n" + "  int k = 98; Object o; \n" + "  public void m1(int i) {\n" + "      //@ assume k == 98; \n" + "      //@ ghost int kk = 0;\n" + "      //@ set kk = 5;\n" + "      //@ debug kk = 7;\n" + "      k = 65;\n" + "      //@ set kk = \\old(k) - k;\n" + "      //@ assume (k==k) && (\\lblpos X (k == 65));\n" + "      //@ assume o!= null && \\typeof(o) <: \\type(Object);\n" + "      //@ unreachable;\n" + "  }\n" + "}\n", "/tt/TestJava.java:13: warning: The prover cannot establish an assertion (Unreachable) in method m1", 11, "/tt/TestJava.java:11: warning: Label X reported", 29);
    }

    /** Tests try/catch/finally */
    public void testCE15() {
        helpTCX("tt.TestJava", "package tt; \n" + "public class TestJava { \n" + "  //@ ensures false; \n" + "  public void m1(int i) {\n" + "    int k = 0;\n" + "    try {\n" + "      k = 5;\n" + "      try {\n" + "         k = 7;\n" + "         return;\n" + "      } finally {\n" + "         k = 9;\n" + "      }\n" + "    } finally {\n" + "       k = 13;\n" + "       return;\n" + "    }\n" + "  }\n" + "  //@ requires i != 0; ensures false; \n" + "  public void m2(int i) throws Exception {\n" + "    int k = 0;\n" + "    try {\n" + "      k = 5;\n" + "      try {\n" + "         k = 7;\n" + "         if (i==0) throw new RuntimeException();\n" + "         return;\n" + "      } catch (Exception e) {\n" + "         k = 25;\n" + "         throw e;\n" + "      } finally {\n" + "         k = 9;\n" + "      }\n" + "    } catch (RuntimeException e) {\n" + "       k = 27;\n" + "    } finally {\n" + "       k = 13;\n" + "    }\n" + "  }\n" + "  //@ requires i == 0; ensures false; \n" + "  public void m3(int i) throws Exception {\n" + "    int k = 0;\n" + "    try {\n" + "      k = 5;\n" + "      try {\n" + "         k = 7;\n" + "         if (i==0) throw new RuntimeException();\n" + "         return;\n" + "      } catch (Exception e) {\n" + "         k = 25;\n" + "         throw e;\n" + "      } finally {\n" + "         k = 9;\n" + "      }\n" + "    } catch (RuntimeException e) {\n" + "       k = 27;\n" + "    } finally {\n" + "       k = 13;\n" + "    }\n" + "  }\n" + "}\n", "/tt/TestJava.java:16: warning: The prover cannot establish an assertion (Postcondition) in method m1", 8, "/tt/TestJava.java:3: warning: Associated declaration", 7, "/tt/TestJava.java:27: warning: The prover cannot establish an assertion (Postcondition) in method m2", 10, "/tt/TestJava.java:19: warning: Associated declaration", 24, "/tt/TestJava.java:41: warning: The prover cannot establish an assertion (Postcondition) in method m3", 15, "/tt/TestJava.java:40: warning: Associated declaration", 24);
    }
}
