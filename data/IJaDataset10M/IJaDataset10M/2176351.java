package test;

import java.util.Map;
import jayu.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class Test_ASNClassFactory implements TestConst {

    public void testAlu() {
        Field rootField = ASNClassFactory.getField(ALU_GRAMMAR_FILE);
        ASNClass asnClass = rootField.type;
        assertTrue(asnClass.name.equals("CallEventRecord"));
        assertTrue(asnClass.isArray() == true);
        assertTrue(asnClass.isReference() == true);
        assertTrue(asnClass.fields.length == 11);
        assertTrue(asnClass.fields[0].name.equals("moCallRecord"));
        assertTrue(asnClass.fields[10].name.equals("termCAMELRecord"));
        assertTrue(asnClass.fields[0].longName.equals(Field.ROOTFIELD + ".moCallRecord"));
        assertTrue(asnClass.fields[10].longName.equals(Field.ROOTFIELD + ".termCAMELRecord"));
        Field firstField = asnClass.fields[0];
        assertTrue(firstField.type.name.equals("MOCallRecord"));
        assertTrue(firstField.type.isArray() == false);
        assertTrue(firstField.type.isReference() == false);
        assertTrue(firstField.type.isSet() == true);
        assertTrue(firstField.type.fields.length == 53);
        assertTrue(firstField.type.fields[0].name.equals("recordType"));
        assertTrue(firstField.type.fields[52].name.equals("maximumBitRate"));
        assertTrue(firstField.type.fields[0].longName.equals(Field.ROOTFIELD + ".moCallRecord.recordType"));
        assertTrue(firstField.type.fields[52].longName.equals(Field.ROOTFIELD + ".moCallRecord.maximumBitRate"));
        Field lastField = asnClass.fields[10];
        assertTrue(lastField.type.name.equals("TermCAMELRecord"));
        assertTrue(lastField.type.isArray() == false);
        assertTrue(lastField.type.isReference() == false);
        assertTrue(lastField.type.isSet() == true);
        assertTrue(lastField.type.fields.length == 36);
        assertTrue(lastField.type.fields[0].name.equals("recordtype"));
        assertTrue(lastField.type.fields[35].name.equals("lrnQuryStatus"));
        assertTrue(lastField.type.fields[0].longName.equals(Field.ROOTFIELD + ".termCAMELRecord.recordtype"));
        assertTrue(lastField.type.fields[35].longName.equals(Field.ROOTFIELD + ".termCAMELRecord.lrnQuryStatus"));
        Field subField = firstField.type.fields[12];
        assertTrue(subField.name.equals("changeOfLocation"));
        assertTrue(subField.type.name.equals("LocationChange"));
        assertTrue(subField.pos == 13);
        assertTrue(subField.type.isArray() == true);
        assertTrue(subField.type.isReference() == false);
        assertTrue(subField.type.isSequence() == false);
        assertTrue(subField.type.isSet() == false);
    }

    public void testEri() {
        Field rootField = ASNClassFactory.getField(ERI_GRAMMAR_FILE);
        ASNClass asnClass = rootField.type;
        assertTrue(asnClass.name.equals("CallDataRecord"));
        assertTrue(asnClass.isArray() == true);
        assertTrue(asnClass.isReference() == true);
        assertTrue(asnClass.fields.length == 2);
        assertTrue(asnClass.fields[0].name.equals("uMTSGSMPLMNCallDataRecord"));
        assertTrue(asnClass.fields[1].name.equals("compositeCallDataRecord"));
        assertTrue(asnClass.fields[0].longName.equals(Field.ROOTFIELD + ".uMTSGSMPLMNCallDataRecord"));
        assertTrue(asnClass.fields[1].longName.equals(Field.ROOTFIELD + ".compositeCallDataRecord"));
        Field firstField = asnClass.fields[0];
        assertTrue(firstField.type.name.equals("UMTSGSMPLMNCallDataRecord"));
        assertTrue(firstField.type.isArray() == false);
        assertTrue(firstField.type.isReference() == false);
        assertTrue(firstField.type.isSequence() == true);
        assertTrue(firstField.type.fields.length == 2);
        assertTrue(firstField.type.fields[0].name.equals("recordType"));
        assertTrue(firstField.type.fields[1].name.equals("eventModule"));
        assertTrue(firstField.type.fields[0].longName.equals(Field.ROOTFIELD + ".uMTSGSMPLMNCallDataRecord.recordType"));
        assertTrue(firstField.type.fields[1].longName.equals(Field.ROOTFIELD + ".uMTSGSMPLMNCallDataRecord.eventModule"));
        Field lastField = asnClass.fields[1];
        assertTrue(lastField.type.name.equals("UMTSGSMPLMNCallDataRecord"));
        assertTrue(lastField.type.isArray() == true);
        assertTrue(lastField.type.isReference() == false);
        assertTrue(lastField.type.isSequence() == false);
        assertTrue(lastField.type.fields.length == 2);
        assertTrue(lastField.type.fields[0].name.equals("recordType"));
        assertTrue(lastField.type.fields[1].name.equals("eventModule"));
        assertTrue(lastField.type.fields[0].longName.equals(Field.ROOTFIELD + ".compositeCallDataRecord.recordType"));
        assertTrue(lastField.type.fields[1].longName.equals(Field.ROOTFIELD + ".compositeCallDataRecord.eventModule"));
    }

    public void test_ErroneousEri() {
        boolean exceptionThrown = true;
        try {
            Field rootField = ASNClassFactory.getField(ERROR1_ERI_GRAMMAR_FILE);
            ASNClass asnClass = rootField.type;
            exceptionThrown = false;
        } catch (ASNException e) {
            assertTrue(exceptionThrown == true);
        }
        assertTrue(exceptionThrown == true);
    }

    @Test
    public void test_Zte() {
        System.out.printf("\n test_Zte() called ");
        Node rootNode = NodeFactory.parse(ZTE_DATA_FILE, ZTE_GRAMMAR_FILE);
    }
}
