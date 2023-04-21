package uni.compilerbau.parser;

import static org.junit.Assert.fail;
import java.io.StringReader;
import org.junit.Test;
import uni.compilerbau.backend.ByteWriter;
import uni.compilerbau.backend.Operations;
import uni.compilerbau.scanner.LookAheadScanner;
import uni.compilerbau.scanner.Scanner;
import uni.compilerbau.scanner.ScannerReader;
import uni.compilerbau.tabelle.Class;
import uni.compilerbau.tabelle.Field;
import uni.compilerbau.tabelle.Method;
import uni.compilerbau.tabelle.Package;
import uni.compilerbau.tabelle.ParameterList;
import uni.compilerbau.tabelle.Type;

public class ParserBooleanTest {

    private Parser parser;

    private Class aClass;

    private Method method;

    ByteWriter expected;

    private Operations Operations = new Operations();

    public void prepare(String string) throws Exception {
        prepare(string, null);
    }

    public void prepare(String string, Field field) throws Exception {
        this.parser = new Parser("", new LookAheadScanner(new Scanner(new ScannerReader(new StringReader(string)))));
        this.parser.getParserState().setPackage(new Package("a"));
        this.parser.getParserState().setThisClassName("A");
        this.aClass = this.parser.getParserState().getThisClass();
        if (field != null) this.aClass.addField(field);
        this.method = new Method(this.parser.getParserState(), "a", new Type(new Type().VOID), this.aClass.getType(), new ParameterList(), false);
        this.aClass.addMethod(this.method);
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
	 * @ignore vorfahrtsregeln: bitte (g|i|h)&g klammern!
	 */
    @Test
    public void testBooleanMisc() throws Exception {
        prepare("void a(){ boolean g = true | false;boolean i = false;boolean h = g | i;boolean v = g | i | ( h & g );boolean a = i & g | ((i |v) & h);return;}");
        this.expected.write1Byte(this.Operations.ICONST_1);
        this.expected.write1Byte(this.Operations.ISTORE);
        this.expected.write1Byte(1);
        this.expected.write1Byte(this.Operations.ICONST_0);
        this.expected.write1Byte(this.Operations.ISTORE);
        this.expected.write1Byte(2);
        this.expected.write1Byte(this.Operations.ILOAD);
        this.expected.write1Byte(1);
        this.expected.write1Byte(this.Operations.ILOAD);
        this.expected.write1Byte(2);
        this.expected.write1Byte(this.Operations.IOR);
        this.expected.write1Byte(this.Operations.ISTORE);
        this.expected.write1Byte(3);
        this.expected.write1Byte(this.Operations.ILOAD);
        this.expected.write1Byte(1);
        this.expected.write1Byte(this.Operations.ILOAD);
        this.expected.write1Byte(2);
        this.expected.write1Byte(this.Operations.IOR);
        this.expected.write1Byte(this.Operations.ILOAD);
        this.expected.write1Byte(3);
        this.expected.write1Byte(this.Operations.ILOAD);
        this.expected.write1Byte(1);
        this.expected.write1Byte(this.Operations.IAND);
        this.expected.write1Byte(this.Operations.IOR);
        this.expected.write1Byte(this.Operations.ISTORE);
        this.expected.write1Byte(4);
        this.expected.write1Byte(this.Operations.ILOAD);
        this.expected.write1Byte(2);
        this.expected.write1Byte(this.Operations.ILOAD);
        this.expected.write1Byte(1);
        this.expected.write1Byte(this.Operations.IAND);
        this.expected.write1Byte(this.Operations.ILOAD);
        this.expected.write1Byte(2);
        this.expected.write1Byte(this.Operations.ILOAD);
        this.expected.write1Byte(4);
        this.expected.write1Byte(this.Operations.IOR);
        this.expected.write1Byte(this.Operations.ILOAD);
        this.expected.write1Byte(3);
        this.expected.write1Byte(this.Operations.IAND);
        this.expected.write1Byte(this.Operations.IOR);
        this.expected.write1Byte(this.Operations.ISTORE);
        this.expected.write1Byte(5);
        this.expected.write1Byte(this.Operations.RETURN);
        check();
    }
}
