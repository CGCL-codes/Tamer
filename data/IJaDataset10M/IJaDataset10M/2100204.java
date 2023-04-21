package ch.usi.inf.pf2.circuit;

import org.junit.Before;
import ch.usi.inf.pf2.circuit.InputPinForOutput;
import ch.usi.inf.pf2.circuit.NotGate;
import ch.usi.inf.pf2.circuit.Output;
import ch.usi.inf.pf2.circuit.OutputPin;
import ch.usi.inf.pf2.circuit.Value;
import ch.usi.inf.pf2.circuit.Wire;

public class NotGateTest extends junit.framework.TestCase {

    private NotGate notGate;

    private InputPinForGate inputPin1;

    @Before
    public void setUp() throws Exception {
        notGate = new NotGate(30, 30);
        inputPin1 = new InputPinForGate(notGate);
    }

    public void testCompute() {
        notGate.setDelay(0);
        notGate.setValue(Value.UNKNOWN, inputPin1);
        notGate.compute();
        assertEquals(Value.UNKNOWN, notGate.getValueAtExit());
        notGate.setValue(Value.TRUE, inputPin1);
        notGate.compute();
        assertEquals(Value.FALSE, notGate.getValueAtExit());
        notGate.setValue(Value.FALSE, inputPin1);
        notGate.compute();
        assertEquals(Value.TRUE, notGate.getValueAtExit());
    }
}
