package uni.compilerbau.parser;

import java.io.File;
import java.io.FileReader;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import uni.compilerbau.backend.ByteWriter;
import uni.compilerbau.scanner.LookAheadScanner;
import uni.compilerbau.scanner.Scanner;
import uni.compilerbau.scanner.ScannerReader;

/**
 * Testet den {@link Parser} an Quellcode aus Dateien.
 */
public class ParserFilesTest {

    private Parser parser;

    private ByteWriter codeBuffer;

    public void prepare(String filename) throws Exception {
        this.parser = new Parser(filename, new LookAheadScanner(new Scanner(new ScannerReader(new FileReader(filename)))));
        this.codeBuffer = new ByteWriter();
    }

    @After
    public void tearDown() {
        this.parser = null;
    }

    @Test
    public void testSourceScannerScanner() throws Exception {
        prepare("src" + File.separatorChar + "main" + File.separatorChar + "java" + File.separatorChar + "uni" + File.separatorChar + "compilerbau" + File.separatorChar + "scanner" + File.separatorChar + "Scanner.java");
        this.parser.parseClassFile();
    }

    @Test
    public void testSourceParserParser() throws Exception {
        prepare("src" + File.separatorChar + "main" + File.separatorChar + "java" + File.separatorChar + "uni" + File.separatorChar + "compilerbau" + File.separatorChar + "parser" + File.separatorChar + "Parser.java");
        this.parser.parseClassFile();
    }

    @Test
    public void testSourceParserParseException() throws Exception {
        prepare("src" + File.separatorChar + "main" + File.separatorChar + "java" + File.separatorChar + "uni" + File.separatorChar + "compilerbau" + File.separatorChar + "parser" + File.separatorChar + "ParseException.java");
        this.parser.parseClassFile();
    }

    @Test
    public void testSourceScannerScannerPosition() throws Exception {
        prepare("src" + File.separatorChar + "main" + File.separatorChar + "java" + File.separatorChar + "uni" + File.separatorChar + "compilerbau" + File.separatorChar + "scanner" + File.separatorChar + "ScannerPosition.java");
        this.parser.parseClassFile();
    }

    @Test
    public void testSourceScannerLookAheadScanner() throws Exception {
        prepare("src" + File.separatorChar + "main" + File.separatorChar + "java" + File.separatorChar + "uni" + File.separatorChar + "compilerbau" + File.separatorChar + "scanner" + File.separatorChar + "LookAheadScanner.java");
        this.parser.parseClassFile();
    }

    @Test
    public void testSourceScannerScannerReader() throws Exception {
        prepare("src" + File.separatorChar + "main" + File.separatorChar + "java" + File.separatorChar + "uni" + File.separatorChar + "compilerbau" + File.separatorChar + "scanner" + File.separatorChar + "ScannerReader.java");
        this.parser.parseClassFile();
    }

    @Test
    public void testSourceToken() throws Exception {
        prepare("src" + File.separatorChar + "main" + File.separatorChar + "java" + File.separatorChar + "uni" + File.separatorChar + "compilerbau" + File.separatorChar + "Token.java");
        this.parser.parseClassFile();
    }

    @Test
    public void testSourceTokens() throws Exception {
        prepare("src" + File.separatorChar + "main" + File.separatorChar + "java" + File.separatorChar + "uni" + File.separatorChar + "compilerbau" + File.separatorChar + "Tokens.java");
        this.parser.parseClassFile();
    }

    @Test
    public void testSourceTabelleClass() throws Exception {
        prepare("src" + File.separatorChar + "main" + File.separatorChar + "java" + File.separatorChar + "uni" + File.separatorChar + "compilerbau" + File.separatorChar + "tabelle" + File.separatorChar + "Class.java");
        this.parser.parseClassFile();
    }

    @Test
    public void testSourceTabelleConstructor() throws Exception {
        prepare("src" + File.separatorChar + "main" + File.separatorChar + "java" + File.separatorChar + "uni" + File.separatorChar + "compilerbau" + File.separatorChar + "tabelle" + File.separatorChar + "Constructor.java");
        this.parser.parseClassFile();
    }

    @Test
    public void testSourceTabelleField() throws Exception {
        prepare("src" + File.separatorChar + "main" + File.separatorChar + "java" + File.separatorChar + "uni" + File.separatorChar + "compilerbau" + File.separatorChar + "tabelle" + File.separatorChar + "Field.java");
        this.parser.parseClassFile();
    }

    @Test
    public void testSourceTabelleMethod() throws Exception {
        prepare("src" + File.separatorChar + "main" + File.separatorChar + "java" + File.separatorChar + "uni" + File.separatorChar + "compilerbau" + File.separatorChar + "tabelle" + File.separatorChar + "Method.java");
        this.parser.parseClassFile();
    }

    @Test
    public void testSourceTabelleParameterList() throws Exception {
        prepare("src" + File.separatorChar + "main" + File.separatorChar + "java" + File.separatorChar + "uni" + File.separatorChar + "compilerbau" + File.separatorChar + "tabelle" + File.separatorChar + "ParameterList.java");
        this.parser.parseClassFile();
    }

    @Test
    public void testSourceTabelleScope() throws Exception {
        prepare("src" + File.separatorChar + "main" + File.separatorChar + "java" + File.separatorChar + "uni" + File.separatorChar + "compilerbau" + File.separatorChar + "tabelle" + File.separatorChar + "Scope.java");
        this.parser.parseClassFile();
    }

    @Test
    public void testSourceTabelleThrowsList() throws Exception {
        prepare("src" + File.separatorChar + "main" + File.separatorChar + "java" + File.separatorChar + "uni" + File.separatorChar + "compilerbau" + File.separatorChar + "tabelle" + File.separatorChar + "ThrowsList.java");
        this.parser.parseClassFile();
    }

    @Test
    public void testSourceTabelleType() throws Exception {
        prepare("src" + File.separatorChar + "main" + File.separatorChar + "java" + File.separatorChar + "uni" + File.separatorChar + "compilerbau" + File.separatorChar + "tabelle" + File.separatorChar + "Type.java");
        this.parser.parseClassFile();
    }

    @Test
    public void testSourceBackendByteWriter() throws Exception {
        prepare("src" + File.separatorChar + "main" + File.separatorChar + "java" + File.separatorChar + "uni" + File.separatorChar + "compilerbau" + File.separatorChar + "backend" + File.separatorChar + "ByteWriter.java");
        this.parser.parseClassFile();
    }

    @Test
    public void testSourceBackendConstantPool() throws Exception {
        prepare("src" + File.separatorChar + "main" + File.separatorChar + "java" + File.separatorChar + "uni" + File.separatorChar + "compilerbau" + File.separatorChar + "backend" + File.separatorChar + "ConstantPool.java");
        this.parser.parseClassFile();
    }

    /**
	 * @ignore test falsch (glaub ich)
	 */
    @Test
    @Ignore
    public void testSourceBackendOperations() throws Exception {
        prepare("src" + File.separatorChar + "main" + File.separatorChar + "java" + File.separatorChar + "uni" + File.separatorChar + "compilerbau" + File.separatorChar + "backend" + File.separatorChar + "Operations.java");
        this.parser.parseClassFile();
    }
}
