package lang4j.parser.generated;

import junit.framework.TestCase;
import java.io.StringReader;
import antlr.TokenStreamException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;

public class Lang4jParserTest extends TestCase {

    public void testProductionList() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        java.util.List productionList = parser.productionList();
    }

    public void testLang4jGrammar() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        Lang4jGrammar lang4jGrammar = parser.lang4jGrammar();
    }

    public void testProduction() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        Production production = parser.production();
    }

    public void testTypeProduction() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        TypeProduction typeProduction = parser.typeProduction();
    }

    public void testListProduction() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        ListProduction listProduction = parser.listProduction();
    }

    public void testListProductionStar() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        ListProductionStar listProductionStar = parser.listProductionStar();
    }

    public void testListProductionPow() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        ListProductionPow listProductionPow = parser.listProductionPow();
    }

    public void testListElementType() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        ListElementType listElementType = parser.listElementType();
    }

    public void testReferenceElement() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        ReferenceElement referenceElement = parser.referenceElement();
    }

    public void testProductionReference() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        ProductionReference productionReference = parser.productionReference();
    }

    public void testEnumProduction() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        EnumProduction enumProduction = parser.enumProduction();
    }

    public void testLabelledTerminal() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        LabelledTerminal labelledTerminal = parser.labelledTerminal();
    }

    public void testLabelledTerminalList() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        java.util.List labelledTerminalList = parser.labelledTerminalList();
    }

    public void testInterfaceProduction() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        InterfaceProduction interfaceProduction = parser.interfaceProduction();
    }

    public void testProductionReferenceList() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        java.util.List productionReferenceList = parser.productionReferenceList();
    }

    public void testConstructorProduction() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        ConstructorProduction constructorProduction = parser.constructorProduction();
    }

    public void testFactorList() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        java.util.List factorList = parser.factorList();
    }

    public void testProductionFactor() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        ProductionFactor productionFactor = parser.productionFactor();
    }

    public void testOptionalAttributeList() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        OptionalAttributeList optionalAttributeList = parser.optionalAttributeList();
    }

    public void testDeepAttribute() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        DeepAttribute deepAttribute = parser.deepAttribute();
    }

    public void testFlatAttribute() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        FlatAttribute flatAttribute = parser.flatAttribute();
    }

    public void testCharLiteralAttribute() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        CharLiteralAttribute charLiteralAttribute = parser.charLiteralAttribute();
    }

    public void testStringLiteralAttribute() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        StringLiteralAttribute stringLiteralAttribute = parser.stringLiteralAttribute();
    }

    public void testMultiLineStringAttribute() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        MultiLineStringAttribute multiLineStringAttribute = parser.multiLineStringAttribute();
    }

    public void testIntLiteralAttribute() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        IntLiteralAttribute intLiteralAttribute = parser.intLiteralAttribute();
    }

    public void testIdentifierAttribute() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        IdentifierAttribute identifierAttribute = parser.identifierAttribute();
    }

    public void testDoubleLiteralAttribute() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        DoubleLiteralAttribute doubleLiteralAttribute = parser.doubleLiteralAttribute();
    }

    public void testBooleanLiteralAttribute() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        BooleanLiteralAttribute booleanLiteralAttribute = parser.booleanLiteralAttribute();
    }

    public void testReferenceAttribute() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        ReferenceAttribute referenceAttribute = parser.referenceAttribute();
    }

    public void testAttribute() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        Attribute attribute = parser.attribute();
    }

    public void testCharToken() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        CharToken charToken = parser.charToken();
    }

    public void testKeyword() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        Keyword keyword = parser.keyword();
    }

    public void testTerminal() throws Exception {
        Lang4jParser parser = getParser("YOUR TEST INPUT GOES HERE");
        Terminal terminal = parser.terminal();
    }

    private Lang4jParser getParser(String source) throws Exception {
        StringReader reader = new StringReader(source);
        return new Lang4jParser(new CommonTokenStream(new Lang4jLexer(new ANTLRReaderStream(reader))));
    }
}
