package ch.usi.inf.pf2.circuit;

import org.junit.Before;
import ch.usi.inf.pf2.circuit.AndGate;
import ch.usi.inf.pf2.circuit.InputPinForOutput;
import ch.usi.inf.pf2.circuit.Output;
import ch.usi.inf.pf2.circuit.OutputPin;
import ch.usi.inf.pf2.circuit.Value;
import ch.usi.inf.pf2.circuit.Wire;

public class AndGateTest extends junit.framework.TestCase {

    private AndGate andGate;

    private InputPinForGate inputPin1;

    private InputPinForGate inputPin2;

    @Before
    public void setUp() throws Exception {
        andGate = new AndGate(30, 30);
        inputPin1 = new InputPinForGate(andGate);
        inputPin2 = new InputPinForGate(andGate);
    }

    public void testCompute() {
        andGate.setDelay(0);
        andGate.setValue(Value.TRUE, inputPin1);
        andGate.compute();
        assertEquals(Value.UNKNOWN, andGate.getValueAtExit());
        andGate.setValue(Value.TRUE, inputPin1);
        andGate.setValue(Value.TRUE, inputPin2);
        andGate.compute();
        assertEquals(Value.TRUE, andGate.getValueAtExit());
        andGate.setValue(Value.TRUE, inputPin1);
        andGate.setValue(Value.FALSE, inputPin2);
        andGate.compute();
        assertEquals(Value.FALSE, andGate.getValueAtExit());
        andGate.setValue(Value.FALSE, inputPin1);
        andGate.setValue(Value.FALSE, inputPin2);
        andGate.compute();
        assertEquals(Value.FALSE, andGate.getValueAtExit());
    }
}
