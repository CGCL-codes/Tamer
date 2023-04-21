package net.sf.gluent.doc.distillery.javaparserunparser;

import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import net.sf.gluent.doc.distillery.chars.CharSink;
import net.sf.gluent.doc.distillery.indentedtokens.IndentedTokens;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CharToFromJavaTest {

    private StringBuilder src;

    private StringBuilder genOut;

    private StringBuilder parserGeneratedSrc;

    private StringBuilder expectedGeneratedGenerator;

    private StringBuilder actualGeneratedGenerator;

    @Before
    public void before() {
        src = new StringBuilder();
        genOut = new StringBuilder();
        parserGeneratedSrc = new StringBuilder();
        expectedGeneratedGenerator = new StringBuilder();
        actualGeneratedGenerator = new StringBuilder();
    }

    @After
    public void after() {
        Char2Java.parse(new ByteArrayInputStream(src.toString().getBytes()), Java2IndentedTokens.to(IndentedTokens.to(new CharSinkImpl(parserGeneratedSrc)).start()).start());
        Char2Java.parse(new ByteArrayInputStream(src.toString().getBytes()), Java2JavaGenerator.to(new CharSinkImpl(actualGeneratedGenerator)).start());
        assertEquals(src.toString(), genOut.toString());
        assertEquals(src.toString(), parserGeneratedSrc.toString());
        assertEquals(expectedGeneratedGenerator.toString(), actualGeneratedGenerator.toString());
    }

    private class CharSinkImpl implements CharSink {

        private final StringBuilder out;

        CharSinkImpl(StringBuilder out) {
            this.out = out;
        }

        @Override
        public void str(String string) {
            out.append(string);
        }

        @Override
        public void end() {
        }
    }

    private JavaStart java() {
        return Java2IndentedTokens.to(IndentedTokens.to(new CharSinkImpl(genOut)).start()).start();
    }

    private void srcLn(String srcLine) {
        src.append(srcLine).append("\n");
    }

    private void genLn(String generatedGeneratorsrcLine) {
        expectedGeneratedGenerator.append(generatedGeneratorsrcLine).append("\n");
    }

    @Test
    public void minimalClass() {
        java().javaTypes().class_().packagePrivate().name("Minimal").endClass().endCompilationUnit().end();
        srcLn("class Minimal {");
        srcLn("");
        srcLn("}");
        genLn("public class Generator {");
        genLn("");
        genLn("  public static void gen(" + "net.sf.gluent.doc.distillery.javaparserunparser.Java2Char.JavaStart out) {");
        genLn("    return out.javaTypes().class_()" + ".name(\"Minimal\")" + ".endClass().endCompilationUnit().end();");
        genLn("  }");
        genLn("");
        genLn("}");
    }

    @Test
    public void minimalClassWithDifferentNameAndExplicitDefaultVisibilityAndExtensibility() {
        java().javaTypes().class_().packagePrivate().notFinalNotAbstract().name("Minimal2").endClass().endCompilationUnit().end();
        srcLn("class Minimal2 {");
        srcLn("");
        srcLn("}");
        genLn("public class Generator {");
        genLn("");
        genLn("  public static void gen(" + "net.sf.gluent.doc.distillery.javaparserunparser.Java2Char.JavaStart out) {");
        genLn("    return out.javaTypes().class_()" + ".name(\"Minimal2\")" + ".endClass().endCompilationUnit().end();");
        genLn("  }");
        genLn("");
        genLn("}");
    }

    @Test
    public void methodDeclarations() {
        java().javaTypes().class_().name("Methods").method().type("void").name("packagePrivate").endParamList().body().endBody().endClass().endCompilationUnit().end();
        srcLn("class Methods {");
        srcLn("");
        srcLn("  void packagePrivate() {");
        srcLn("  }");
        srcLn("");
        srcLn("}");
        genLn("public class Generator {");
        genLn("");
        genLn("  public static void gen(" + "net.sf.gluent.doc.distillery.javaparserunparser.Java2Char.JavaStart out) {");
        genLn("    return out.javaTypes().class_().name(\"Methods\").method().type(\"void\")" + ".name(\"packagePrivate\").endParamList().body().endBody()" + ".endClass().endCompilationUnit().end();");
        genLn("  }");
        genLn("");
        genLn("}");
    }

    @Test
    public void methodCallAsStatement() {
        java().javaTypes().class_().name("MethodCalls").method().type("void").name("asStatement").endParamList().body().methodCall().scope().ref("System").name("gc").endParams().endBody().endClass().endCompilationUnit().end();
        srcLn("class MethodCalls {");
        srcLn("");
        srcLn("  void asStatement() {");
        srcLn("    System.gc();");
        srcLn("  }");
        srcLn("");
        srcLn("}");
        genLn("public class Generator {");
        genLn("");
        genLn("  public static void gen(" + "net.sf.gluent.doc.distillery.javaparserunparser.Java2Char.JavaStart out) {");
        genLn("    return out.javaTypes().class_().name(\"MethodCalls\").method().type(\"void\")" + ".name(\"asStatement\").endParamList().body().methodCall().scope()" + ".ref(\"System\").name(\"gc\").endParams().endBody().endClass()" + ".endCompilationUnit().end();");
        genLn("  }");
        genLn("");
        genLn("}");
    }

    @Test
    public void methodCallAsExpression() {
        java().javaTypes().class_().name("MethodCalls").method().type("long").name("asExpression").endParamList().body().return_().methodCall().scope().ref("System").name("currentTimeMillis").endParams().endBody().endClass().endCompilationUnit().end();
        srcLn("class MethodCalls {");
        srcLn("");
        srcLn("  long asExpression() {");
        srcLn("    return System.currentTimeMillis();");
        srcLn("  }");
        srcLn("");
        srcLn("}");
        genLn("public class Generator {");
        genLn("");
        genLn("  public static void gen(" + "net.sf.gluent.doc.distillery.javaparserunparser.Java2Char.JavaStart out) {");
        genLn("    return out.javaTypes().class_().name(\"MethodCalls\").method().type(\"long\")" + ".name(\"asExpression\").endParamList().body().return_()" + ".methodCall().scope().ref(\"System\").name(\"currentTimeMillis\")" + ".endParams().endBody().endClass().endCompilationUnit().end();");
        genLn("  }");
        genLn("");
        genLn("}");
    }

    @Test
    public void methodCallOfFieldAccess() {
        java().javaTypes().class_().name("MethodCalls").method().type("void").name("fieldAccess").endParamList().body().methodCall().scope().fieldRef().scope().ref("System").name("out").name("println").endParams().endBody().endClass().endCompilationUnit().end();
        srcLn("class MethodCalls {");
        srcLn("");
        srcLn("  void fieldAccess() {");
        srcLn("    System.out.println();");
        srcLn("  }");
        srcLn("");
        srcLn("}");
        genLn("public class Generator {");
        genLn("");
        genLn("  public static void gen(" + "net.sf.gluent.doc.distillery.javaparserunparser.Java2Char.JavaStart out) {");
        genLn("    return out.javaTypes().class_().name(\"MethodCalls\").method().type(\"void\")" + ".name(\"fieldAccess\").endParamList().body().methodCall().scope()" + ".fieldRef().scope().ref(\"System\").name(\"out\").name(\"println\")" + ".endParams().endBody().endClass().endCompilationUnit().end();");
        genLn("  }");
        genLn("");
        genLn("}");
    }

    @Test
    public void methodCallWithOneArgument() {
        java().javaTypes().class_().name("MethodCalls").method().type("void").name("oneArg").endParamList().body().methodCall().scope().fieldRef().scope().ref("System").name("out").name("println").literalString("hi").endParams().endBody().endClass().endCompilationUnit().end();
        srcLn("class MethodCalls {");
        srcLn("");
        srcLn("  void oneArg() {");
        srcLn("    System.out.println(\"hi\");");
        srcLn("  }");
        srcLn("");
        srcLn("}");
        genLn("public class Generator {");
        genLn("");
        genLn("  public static void gen(" + "net.sf.gluent.doc.distillery.javaparserunparser.Java2Char.JavaStart out) {");
        genLn("    return out.javaTypes().class_().name(\"MethodCalls\").method().type(\"void\")" + ".name(\"oneArg\").endParamList().body().methodCall().scope()" + ".fieldRef().scope().ref(\"System\").name(\"out\").name(\"println\")" + ".literalString(\"hi\").endParams().endBody().endClass()" + ".endCompilationUnit().end();");
        genLn("  }");
        genLn("");
        genLn("}");
    }

    @Test
    public void plusOfThreeIntsIsLeftFolded() {
        java().javaTypes().class_().name("Plus").method().type("int").name("threeLiterals").type("int").name("a").type("int").name("b").type("int").name("c").endParamList().body().return_().plus().plus().ref("a").ref("b").ref("c").endBody().endClass().endCompilationUnit().end();
        srcLn("class Plus {");
        srcLn("");
        srcLn("  int threeLiterals(int a, int b, int c) {");
        srcLn("    return a + b + c;");
        srcLn("  }");
        srcLn("");
        srcLn("}");
        genLn("public class Generator {");
        genLn("");
        genLn("  public static void gen(" + "net.sf.gluent.doc.distillery.javaparserunparser.Java2Char.JavaStart out) {");
        genLn("    return out.javaTypes().class_().name(\"Plus\").method().type(\"int\")" + ".name(\"threeLiterals\").type(\"int\").name(\"a\").type(\"int\")" + ".name(\"b\").type(\"int\").name(\"c\").endParamList().body()" + ".return_().plus().plus().ref(\"a\").ref(\"b\").ref(\"c\").endBody()" + ".endClass().endCompilationUnit().end();");
        genLn("  }");
        genLn("");
        genLn("}");
    }

    @Test
    public void classWithAllSupportedFeatures() {
        java().javaTypes().class_().public_().final_().name("ClassWithAllSupportedFeatures").field().private_().final_().type("String").name("s1").uninitialized().field().protected_().static_().type("java.util.List<String>").name("list").initAs().new_("java.util.ArrayList<String>").endParamList().constructor().public_().type("String").name("s1").endParamList().body().assign("this.s1").ref("s1").endBody().method().public_().type("String").name("getS1").endParamList().body().return_().ref("s1").endBody().method().private_().static_().type("void").name("setList").type("java.util.List<String>").name("list").endParamList().body().assign("ClassWithAllSupportedFeatures.list").ref("list").endBody().method().public_().static_().type("int").name("add").type("int").name("v1").type("int").name("v2").endParamList().body().return_().plus().ref("v1").ref("v2").endBody().method().public_().static_().type("String").name("stringLiteralAndMethodCall").endParamList().body().return_().plus().literalString("time=").methodCall().scope().ref("System").name("currentTimeMillis").endParams().endBody().endClass().endCompilationUnit().end();
        srcLn("public final class ClassWithAllSupportedFeatures {");
        srcLn("");
        srcLn("  private final String s1;");
        srcLn("");
        srcLn("  protected static java.util.List<String> list = new java.util.ArrayList<String>();");
        srcLn("");
        srcLn("  public ClassWithAllSupportedFeatures(String s1) {");
        srcLn("    this.s1 = s1;");
        srcLn("  }");
        srcLn("");
        srcLn("  public String getS1() {");
        srcLn("    return s1;");
        srcLn("  }");
        srcLn("");
        srcLn("  private static void setList(java.util.List<String> list) {");
        srcLn("    ClassWithAllSupportedFeatures.list = list;");
        srcLn("  }");
        srcLn("");
        srcLn("  public static int add(int v1, int v2) {");
        srcLn("    return v1 + v2;");
        srcLn("  }");
        srcLn("");
        srcLn("  public static String stringLiteralAndMethodCall() {");
        srcLn("    return \"time=\" + System.currentTimeMillis();");
        srcLn("  }");
        srcLn("");
        srcLn("}");
        genLn("public class Generator {");
        genLn("");
        genLn("  public static void gen(" + "net.sf.gluent.doc.distillery.javaparserunparser.Java2Char.JavaStart out) {");
        genLn("    return out.javaTypes().class_().public_().final_()" + ".name(\"ClassWithAllSupportedFeatures\").field().private_()" + ".final_().type(\"String\").name(\"s1\").uninitialized().field()" + ".protected_().static_().type(\"java.util.List<String>\")" + ".name(\"list\").initAs().new_(\"java.util.ArrayList<String>\")" + ".endParamList().constructor().public_().type(\"String\")" + ".name(\"s1\").endParamList().body().assign(\"this.s1\").ref(\"s1\")" + ".endBody().method().public_().type(\"String\").name(\"getS1\")" + ".endParamList().body().return_().ref(\"s1\").endBody().method()" + ".private_().static_().type(\"void\").name(\"setList\")" + ".type(\"java.util.List<String>\").name(\"list\").endParamList()" + ".body().assign(\"ClassWithAllSupportedFeatures.list\")" + ".ref(\"list\").endBody().method().public_().static_().type(\"int\")" + ".name(\"add\").type(\"int\").name(\"v1\").type(\"int\").name(\"v2\")" + ".endParamList().body().return_().plus().ref(\"v1\").ref(\"v2\")" + ".endBody().method().public_().static_().type(\"String\")" + ".name(\"stringLiteralAndMethodCall\").endParamList().body()" + ".return_().plus().literalString(\"time=\").methodCall().scope()" + ".ref(\"System\").name(\"currentTimeMillis\").endParams().endBody()" + ".endClass().endCompilationUnit().end();");
        genLn("  }");
        genLn("");
        genLn("}");
    }
}
