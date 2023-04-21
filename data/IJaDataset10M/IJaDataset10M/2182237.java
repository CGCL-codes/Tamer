package com.newisys.behsim;

import junit.framework.TestCase;
import com.newisys.eventsched.EventScheduler;
import com.newisys.verilog.*;
import com.newisys.verilog.util.Bit;
import com.newisys.verilog.util.BitVector;

public class BehavioralRegTest extends TestCase {

    private class CallbackHandlerImpl implements VerilogCallbackHandler {

        public CallbackHandlerImpl() {
            super();
        }

        public void run(VerilogCallback cb, VerilogCallbackData data) {
        }
    }

    private static final class ValueChangeHandler implements VerilogCallbackHandler {

        private final Object expectedValue;

        private final VerilogTime expectedTime;

        private final ValueType valueType;

        public ValueChangeHandler(Object expectedValue, VerilogTime expectedTime, ValueType valueType) {
            this.expectedValue = expectedValue;
            this.expectedTime = expectedTime;
            this.valueType = valueType;
        }

        public void run(VerilogCallback cb, VerilogCallbackData data) {
            BehavioralReg localCallbackReg = (BehavioralReg) cb.getObject();
            BehavioralReg localDataReg = (BehavioralReg) data.getObject();
            if (valueType != ValueType.SUPPRESS) {
                assertTrue(expectedValue.equals(localCallbackReg.getValue(valueType)));
                assertTrue(expectedValue.equals(localDataReg.getValue(valueType)));
                assertTrue(expectedValue.equals(data.getValue()));
            }
            assertTrue(cb.getReason().equals(CallbackReason.VALUE_CHANGE));
            if (expectedTime != null) {
                assertTrue(expectedTime.equals(cb.getTime()));
                assertTrue(expectedTime.equals(data.getTime()));
            }
            assertTrue(cb.getType().equals(ObjectType.REG));
            assertTrue(cb.getValueType().equals(valueType));
            assertTrue(cb.isRecurring());
            assertTrue(this.equals(cb.getHandler()));
        }
    }

    private BehavioralSimulation simulation;

    private BehavioralReg reggie;

    private EventScheduler eventScheduler;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(BehavioralRegTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        simulation = new BehavioralSimulation();
        reggie = new BehavioralReg(simulation, "Reggie", 32);
        eventScheduler = simulation.getEventScheduler();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public BehavioralRegTest(String name) {
        super(name);
    }

    public final void testBasic() {
        String expected = "Reggie";
        assertTrue(expected.equals(reggie.getName()));
        BitVector value = new BitVector("32'hcafebabe", 32);
        reggie.putValue(value);
        assertTrue(value.equals(reggie.getValue()));
        String actualExceptionString = "NOPE";
        String expectedExceptionString = "java.lang.UnsupportedOperationException";
        try {
            reggie.getFile();
        } catch (UnsupportedOperationException e) {
            actualExceptionString = e.toString();
        }
        assertTrue(actualExceptionString.equals(expectedExceptionString));
        VerilogCallbackHandler x = new CallbackHandlerImpl();
        BehavioralRegCallback cb = (BehavioralRegCallback) reggie.addValueChangeCallback(x);
        reggie.cancelCallback(cb);
        reggie.cancelCallback(cb);
    }

    public void testDriveDelayMode() {
        doDriveDelayModeTest(DriveDelayMode.NO_DELAY, false);
        doDriveDelayModeTest(DriveDelayMode.INERTIAL_DELAY, true);
        doDriveDelayModeTest(DriveDelayMode.TRANSPORT_DELAY, true);
        doDriveDelayModeTest(DriveDelayMode.PURE_TRANSPORT_DELAY, false);
    }

    private void doDriveDelayModeTest(DriveDelayMode mode, boolean throwException) {
        VerilogSimTime delay = new VerilogSimTime(80);
        String expectedExceptionString = "NOPE";
        try {
            reggie.putValueDelay(new BitVector("16'h0001"), delay, mode);
        } catch (UnsupportedOperationException e) {
            expectedExceptionString = e.toString();
        }
        assertTrue(expectedExceptionString.equals(throwException ? "java.lang.UnsupportedOperationException" : "NOPE"));
    }

    public void testValueChangeCallback() {
        Object value = new BitVector("32'hcafebabe", 32);
        ValueChangeHandler handler = new ValueChangeHandler(value, new VerilogSimTime(0), ValueType.OBJ_TYPE);
        reggie.addValueChangeCallback(handler);
        reggie.putValue(value);
        eventScheduler.processNextSimulationTimeStep();
    }

    public void testValueChangeCallbackDefault() {
        Object value = new BitVector("32'hcafebabe", 32);
        TimeType timeType = TimeType.SIM;
        ValueType valueType = ValueType.OBJ_TYPE;
        VerilogTime time = new VerilogSimTime(0);
        valueChangeCallbackTest(value, timeType, valueType, time);
    }

    public void testValueChangeCallbackScaledRealInt() {
        Object value = new Integer(299772);
        TimeType timeType = TimeType.SCALED_REAL;
        ValueType valueType = ValueType.INT;
        VerilogTime time = new VerilogScaledRealTime(0.0);
        valueChangeCallbackTest(value, timeType, valueType, time);
    }

    public void testValueChangeCallbackSupressTimeScalar() {
        Object value = Bit.ONE;
        TimeType timeType = TimeType.SUPPRESS;
        ValueType valueType = ValueType.SCALAR;
        VerilogTime time = null;
        valueChangeCallbackTest(value, timeType, valueType, time);
    }

    public void testValueChangeCallbackScaledRealVector() {
        Object value = new BitVector("32'hcafebabe", 32);
        TimeType timeType = TimeType.SCALED_REAL;
        ValueType valueType = ValueType.VECTOR;
        VerilogTime time = new VerilogScaledRealTime(0.0);
        valueChangeCallbackTest(value, timeType, valueType, time);
    }

    public void testValueChangeCallbackSupressValueScalar() {
        Object value = new BitVector("32'hcafebabe", 32);
        TimeType timeType = TimeType.SCALED_REAL;
        ValueType valueType = ValueType.SUPPRESS;
        VerilogTime time = new VerilogScaledRealTime(0.0);
        valueChangeCallbackTest(value, timeType, valueType, time);
    }

    private void valueChangeCallbackTest(Object value, TimeType timeType, ValueType valueType, VerilogTime time) {
        ValueChangeHandler handler = new ValueChangeHandler(value, time, valueType);
        reggie.addValueChangeCallback(timeType, valueType, handler);
        reggie.putValue(value);
        eventScheduler.processNextSimulationTimeStep();
    }
}
