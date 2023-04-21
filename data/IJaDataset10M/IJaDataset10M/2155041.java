package uni.compilerbau.parser;

import static org.junit.Assert.fail;
import java.io.StringReader;
import org.junit.Ignore;
import org.junit.Test;
import uni.compilerbau.backend.ByteWriter;
import uni.compilerbau.backend.ConstantPool;
import uni.compilerbau.backend.Operations;
import uni.compilerbau.scanner.LookAheadScanner;
import uni.compilerbau.scanner.Scanner;
import uni.compilerbau.scanner.ScannerReader;
import uni.compilerbau.tabelle.Class;
import uni.compilerbau.tabelle.Field;
import uni.compilerbau.tabelle.FullyQualifiedClassName;
import uni.compilerbau.tabelle.Method;
import uni.compilerbau.tabelle.Package;
import uni.compilerbau.tabelle.Signature;
import uni.compilerbau.tabelle.Type;

public class ParseExceptionTest {

    private Parser parser;

    private Class aClass;

    private Method method;

    ByteWriter expected;

    private ConstantPool constantPool;

    private Operations Operations = new Operations();

    public void prepare(String string) throws Exception {
        prepare(string, new Field[0]);
    }

    public void prepare(String string, Field... fields) throws Exception {
        prepare(string, null, fields);
    }

    private void prepare(String string, Class otherClass, Field... fields) throws Exception {
        this.parser = new Parser("", new LookAheadScanner(new Scanner(new ScannerReader(new StringReader(string)))));
        this.constantPool = this.parser.getConstantPool();
        this.parser.getParserState().setPackage(new Package("a"));
        this.aClass = new Class(new FullyQualifiedClassName(new Package("a"), "A"), this.parser.getParserState());
        if (otherClass != null) this.parser.getCompilerState().getOrMakeClass(otherClass.getName());
        for (Field f : fields) this.aClass.addField(f);
        this.parser.parseMethodDeclaration();
        this.expected = new ByteWriter();
    }

    private void check() {
        byte[] expectedArray = this.expected.getByteArray();
        byte[] actualArray = this.method.getCode().getByteArray();
        String message = "Erwarte, dass der Code stimmt. Sollte sein [ ";
        int position = -1;
        if (expectedArray.length != actualArray.length) {
            if (expectedArray.length < actualArray.length) {
                for (int i = 0; i < expectedArray.length; i++) if (expectedArray[i] != actualArray[i]) if (position == -1) position = i;
            } else for (int i = 0; i < actualArray.length; i++) if (expectedArray[i] != actualArray[i]) if (position == -1) position = i;
        } else {
            for (int i = 0; i < expectedArray.length; i++) if (expectedArray[i] != actualArray[i]) if (position == -1) position = i;
        }
        if (position != -1) {
            if (position > 0) message += "... ";
            for (int i = 0; i < 5 && position + i < expectedArray.length; i++) {
                message += expectedArray[position + i] + " ";
            }
            if (position < expectedArray.length - 1) message += "... ";
            message += "], ist aber [ ";
            if (position > 0) message += "... ";
            for (int i = 0; i < 5 && position + i < actualArray.length; i++) {
                message += actualArray[position + i] + " ";
            }
            if (position < actualArray.length - 1) message += "... ";
            message += "]. Unterscheidung an Position: " + position + ". Länge exp: " + expectedArray.length + ", Länge act: " + actualArray.length;
            fail(message);
        }
    }

    /**
	 * @ignore test falsch: kompiler ist zu schlau
	 */
    @Test
    @Ignore
    public void testThrowEX() throws Exception {
        prepare("void a ()throws AException{int a = 3;if (2<a){throw new AException(\"abc\");}}return;}");
        this.expected.write1Byte(Operations.BIPUSH);
        this.expected.write1Byte(3);
        this.expected.write1Byte(Operations.ISTORE);
        this.expected.write1Byte(1);
        this.expected.write1Byte(Operations.BIPUSH);
        this.expected.write1Byte(2);
        this.expected.write1Byte(Operations.ILOAD);
        this.expected.write1Byte(1);
        this.expected.write1Byte(Operations.IF_ICMPGE);
        this.expected.write2Byte(12);
        this.expected.write1Byte(Operations.NEW);
        this.expected.write2Byte(this.parser.getParserState().getClassByName("AException").getConstantPoolIndex());
        this.expected.write1Byte(Operations.DUP);
        this.expected.write1Byte(Operations.LDC);
        this.expected.write1Byte(this.constantPool.getStringIndex("abc"));
        this.expected.write1Byte(Operations.INVOKESPECIAL);
        this.expected.write1Byte(this.parser.getParserState().getClassByName("AException").getConstructorBySignature(new Signature(new Type[] { new Type(new Class(new FullyQualifiedClassName(new Package("java.lang"), "String"), this.parser.getParserState())) })).getConstantPoolIndex());
        this.expected.write1Byte(Operations.ATHROW);
        this.expected.write1Byte(Operations.RETURN);
        check();
    }

    /**
	 * Warum wird bei
	 * this.parser.getClassByName("AException").getConstantPoolIndex() der
	 * falsche CP-Index zurückgegeben? (5, sollte aber 2 sein, oder
	 * umgekehrt.... ;) )
	 * 
	 * @ignore test falsch: Kompiler ist zu schlau
	 */
    @Test
    @Ignore
    public void testTryNCatchEX() throws Exception {
        prepare("void a ()throws AException{int a = 3;try{throw new AException(\"abc\");}catch(AException e){a = 4;}}return;}");
        this.expected.write1Byte(Operations.BIPUSH);
        this.expected.write1Byte(3);
        this.expected.write1Byte(Operations.ISTORE);
        this.expected.write1Byte(1);
        this.expected.write1Byte(Operations.NEW);
        this.expected.write2Byte(this.parser.getParserState().getClassByName("AException").getConstantPoolIndex());
        this.expected.write1Byte(Operations.DUP);
        this.expected.write1Byte(Operations.LDC);
        this.expected.write1Byte(this.constantPool.getStringIndex("abc"));
        this.expected.write1Byte(Operations.INVOKESPECIAL);
        this.expected.write2Byte(this.parser.getParserState().getClassByName("AException").getConstructorBySignature(new Signature(new Type[] { new Type(new Class(new FullyQualifiedClassName(new Package("java.lang"), "String"), this.parser.getParserState())) })).getConstantPoolIndex());
        this.expected.write1Byte(Operations.ATHROW);
        this.expected.write1Byte(Operations.ASTORE);
        this.expected.write1Byte(2);
        this.expected.write1Byte(Operations.BIPUSH);
        this.expected.write1Byte(4);
        this.expected.write1Byte(Operations.ISTORE);
        this.expected.write1Byte(1);
        this.expected.write1Byte(Operations.RETURN);
        check();
    }

    /**
	 * @ignore test falsch: Kompiler ist zu schlau
	 */
    @Test
    @Ignore
    public void testTryCatchBlockEX() throws Exception {
        prepare("void a ()throws AException{int a = 3;try{try{throw new AException(\"abc\");}catch(AException e){throw new BException(\"xyz\");}}catch(BException e){a = 4;}}return;}");
        this.expected.write1Byte(Operations.BIPUSH);
        this.expected.write1Byte(3);
        this.expected.write1Byte(Operations.ISTORE);
        this.expected.write1Byte(1);
        this.expected.write1Byte(Operations.NEW);
        this.expected.write2Byte(this.parser.getParserState().getClassByName("AException").getConstantPoolIndex());
        this.expected.write1Byte(Operations.DUP);
        this.expected.write1Byte(Operations.LDC);
        this.expected.write1Byte(this.constantPool.getStringIndex("abc"));
        this.expected.write1Byte(Operations.INVOKESPECIAL);
        this.expected.write2Byte(this.parser.getParserState().getClassByName("AException").getConstructorBySignature(new Signature(new Type[] { new Type(new Class(new FullyQualifiedClassName(new Package("java.lang"), "String"), this.parser.getParserState())) })).getConstantPoolIndex());
        this.expected.write1Byte(Operations.ATHROW);
        this.expected.write1Byte(Operations.ASTORE);
        this.expected.write1Byte(2);
        this.expected.write1Byte(Operations.NEW);
        this.expected.write2Byte(this.parser.getParserState().getClassByName("BException").getConstantPoolIndex());
        this.expected.write1Byte(Operations.DUP);
        this.expected.write1Byte(Operations.LDC);
        this.expected.write1Byte(this.parser.getCompilerState().getConstantPool().getStringIndex("xyz"));
        this.expected.write1Byte(Operations.INVOKESPECIAL);
        this.expected.write2Byte(this.parser.getParserState().getClassByName("BException").getConstructorBySignature(new Signature(new Type[] { new Type(new Class(new FullyQualifiedClassName(new Package("java.lang"), "String"), this.parser.getParserState())) })).getConstantPoolIndex());
        this.expected.write1Byte(Operations.ATHROW);
        this.expected.write1Byte(Operations.ASTORE);
        this.expected.write1Byte(2);
        this.expected.write1Byte(Operations.BIPUSH);
        this.expected.write1Byte(4);
        this.expected.write1Byte(Operations.ISTORE);
        this.expected.write1Byte(1);
        this.expected.write1Byte(Operations.RETURN);
        check();
    }
}
