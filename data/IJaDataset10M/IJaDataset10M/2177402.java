package propasm.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class OperationTest {

    @Test
    public void testBasicTemplate() {
        Operation op = new Operation() {

            @Override
            public int getOpcode() {
                return 0;
            }
        };
        assertEquals(0x00BC0000, op.getTemplate());
    }

    @Test
    public void testOpcodeGeneration() {
        Operation op = new Operation() {

            @Override
            public int getOpcode() {
                return 0x1A;
            }
        };
        assertEquals(0x68BC0000, op.getTemplate());
    }

    @Test
    public void testDefaultResultFlag() {
        Operation op = new Operation() {

            @Override
            public int getOpcode() {
                return 0;
            }

            @Override
            public boolean generatesResultByDefault() {
                return false;
            }
        };
        assertEquals(0x003C0000, op.getTemplate());
    }

    @Test
    public void testDefaultImmediateFlag() {
        Operation op = new Operation() {

            @Override
            public int getOpcode() {
                return 0;
            }

            @Override
            public boolean immediateByDefault() {
                return true;
            }
        };
        assertEquals(0x00FC0000, op.getTemplate());
    }

    @Test
    public void testDefaultPredicate() {
        Operation op = new Operation() {

            @Override
            public int getOpcode() {
                return 0;
            }

            @Override
            public Predicate defaultPredicate() {
                return Predicate.IF_C_AND_NZ;
            }
        };
        assertEquals(0x00900000, op.getTemplate());
    }
}
