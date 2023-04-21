package test.net.sourceforge.entrainer.eeg.core;

import static junit.framework.Assert.*;
import static net.sourceforge.entrainer.eeg.utils.EEGUtils.mean;
import static net.sourceforge.entrainer.util.Utils.snooze;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.entrainer.eeg.core.AbstractEEGDevice;
import net.sourceforge.entrainer.eeg.core.AbstractEEGSignalProcessor;
import net.sourceforge.entrainer.eeg.core.EEGChannelState;
import net.sourceforge.entrainer.eeg.core.EEGChannelValue;
import net.sourceforge.entrainer.eeg.core.EEGDeviceStatusEvent;
import net.sourceforge.entrainer.eeg.core.EEGDeviceStatusListener;
import net.sourceforge.entrainer.eeg.core.EEGException;
import net.sourceforge.entrainer.eeg.core.EEGReadEvent;
import net.sourceforge.entrainer.eeg.core.EEGReadListener;
import net.sourceforge.entrainer.eeg.core.EEGSignalProcessor;
import net.sourceforge.entrainer.eeg.core.FrequencyType;
import org.junit.Before;
import org.junit.Test;

public class TestAbstractEEGDevice {

    private TestSignalProcessor signalProcessor;

    private TestDevice device;

    private List<EEGChannelState> states;

    private EEGReadListener readListener;

    private EEGDeviceStatusListener statusListener;

    private List<EEGChannelValue> values;

    private String status;

    @Before
    public void setup() {
        signalProcessor = new TestSignalProcessor();
        device = new TestDevice();
        device.setSignalProcessor(signalProcessor);
        createStates();
        for (EEGChannelState state : states) {
            device.addChannelState(state);
        }
        readListener = new EEGReadListener() {

            public void readEventPerformed(EEGReadEvent e) {
                setValues(e.getChannels());
            }
        };
        statusListener = new EEGDeviceStatusListener() {

            public void statusChanged(EEGDeviceStatusEvent e) {
                setStatus(e.getNewStatus());
            }
        };
        device.addDeviceStatusListener(statusListener);
        device.addEEGReadListener(readListener);
    }

    @Test
    public void testStatus() {
        device.notifyDeviceInfo();
        snooze(50);
        assertTrue("Unexpected message " + getStatus(), getStatus().equals("Test Device using Signal Proc for Testing"));
        try {
            device.openDevice();
            snooze(50);
        } catch (EEGException e) {
        }
        assertTrue("Unexpected message " + getStatus(), getStatus().equals("Device opened"));
        try {
            device.closeDevice();
            snooze(50);
        } catch (EEGException e) {
        }
        assertTrue("Unexpected message " + getStatus(), getStatus().equals("Device closed"));
    }

    @Test
    public void testCalibration() {
        double[] d = getArrayForProcessing();
        try {
            device.calibrate();
            snooze(100);
            device.applyTestSignal(d);
            while (device.isCalibrating()) {
                snooze(50);
            }
        } catch (EEGException e) {
            fail("Unexpected calibration problem :" + e.getMessage());
        }
        device.applyTestSignal(d);
        validateValues(getCalibratedArray());
    }

    @Test
    public void testFilters() {
        double[] d = getArrayForProcessing();
        setLowPass(false);
        setHighPass(false);
        device.applyTestSignal(d);
        snooze(50);
        validateValues(getExpectedNoFilterArray());
        setLowPass(true);
        device.applyTestSignal(d);
        snooze(50);
        validateValues(getExpectedLowFilterArray());
        setLowPass(false);
        setHighPass(true);
        device.applyTestSignal(d);
        snooze(50);
        validateValues(getExpectedHighFilterArray());
        setLowPass(true);
        setHighPass(true);
        device.applyTestSignal(d);
        snooze(50);
        validateValues(getExpectedLowAndHighFilterArray());
    }

    private double[] getCalibratedArray() {
        double[] d = new double[6];
        d[0] = roundValue(0.018777271549862674);
        d[1] = roundValue(0.015813698110281126);
        d[2] = roundValue(0.011941921123289564);
        d[3] = roundValue(0.011593844532762068);
        d[4] = roundValue(0.009566662935723697);
        d[5] = roundValue(0.009329031386100597);
        return d;
    }

    private double[] getExpectedNoFilterArray() {
        double[] d = new double[6];
        d[0] = roundValue(0.016601007085194363);
        d[1] = roundValue(0.01398485710029594);
        d[2] = roundValue(0.010581925387956511);
        d[3] = roundValue(0.01023916384628087);
        d[4] = roundValue(0.008474026283412404);
        d[5] = roundValue(0.008244377556838139);
        return d;
    }

    private double[] getExpectedLowFilterArray() {
        double[] d = new double[6];
        d[0] = roundValue(0.011650670781256974);
        d[1] = roundValue(0.009852143384236187);
        d[2] = roundValue(0.00742317133367551);
        d[3] = roundValue(0.0071959422054489516);
        d[4] = roundValue(0.005951717081983204);
        d[5] = roundValue(0.005781852386176939);
        return d;
    }

    private double[] getExpectedHighFilterArray() {
        double[] d = new double[6];
        d[0] = roundValue(0.011637548566605743);
        d[1] = roundValue(0.009763201182720579);
        d[2] = roundValue(0.007404973567106715);
        d[3] = roundValue(0.007175218501980018);
        d[4] = roundValue(0.005924803760603585);
        d[5] = roundValue(0.005785860329521189);
        return d;
    }

    private double[] getExpectedLowAndHighFilterArray() {
        double[] d = new double[6];
        d[0] = roundValue(0.009388635774931337);
        d[1] = roundValue(0.007906849055140563);
        d[2] = roundValue(0.005970960561644782);
        d[3] = roundValue(0.005796922266381034);
        d[4] = roundValue(0.004783331467861848);
        d[5] = roundValue(0.004664515693050299);
        return d;
    }

    private double roundValue(double d) {
        return new BigDecimal(d, new MathContext(10, RoundingMode.HALF_UP)).doubleValue();
    }

    private void setLowPass(boolean lowPass) {
        for (EEGChannelState state : states) {
            state.setLowPass(lowPass);
        }
    }

    private void setHighPass(boolean highPass) {
        for (EEGChannelState state : states) {
            state.setHighPass(highPass);
        }
    }

    private void validateValues(double[] expected) {
        EEGChannelValue value;
        for (int i = 0; i < getValues().size(); i++) {
            value = getValues().get(i);
            double val = roundValue(value.getChannelStrengthWithCalibration());
            assertTrue("Expected: " + expected[i] + ", actual: " + val + " for channel " + value.getChannelState().getFrequencyType().getDescription(), val == expected[i]);
        }
    }

    private double[] getArrayForProcessing() {
        double[] array = new double[10];
        for (double i = 1; i < 11; i++) {
            array[(int) i - 1] = ((double) i) / 10;
        }
        return array;
    }

    protected List<EEGChannelValue> getValues() {
        return values;
    }

    protected void setValues(List<EEGChannelValue> values) {
        this.values = values;
    }

    protected String getStatus() {
        return status;
    }

    protected void setStatus(String status) {
        this.status = status;
    }

    private void createStates() {
        states = new ArrayList<EEGChannelState>();
        states.add(new EEGChannelState(FrequencyType.ALPHA_LOW, 6, 8, device.getSampleFrequencyInHertz()));
        states.add(new EEGChannelState(FrequencyType.ALPHA_MID, 8, 10, device.getSampleFrequencyInHertz()));
        states.add(new EEGChannelState(FrequencyType.ALPHA_HIGH, 10, 12, device.getSampleFrequencyInHertz()));
        states.add(new EEGChannelState(FrequencyType.BETA_LOW, 12, 14, device.getSampleFrequencyInHertz()));
        states.add(new EEGChannelState(FrequencyType.BETA_MID, 14, 16, device.getSampleFrequencyInHertz()));
        states.add(new EEGChannelState(FrequencyType.BETA_HIGH, 16, 18, device.getSampleFrequencyInHertz()));
    }

    @SuppressWarnings("serial")
    private class TestSignalProcessor extends AbstractEEGSignalProcessor {

        private double mean;

        @Override
        protected void calculateCalibrationImpl() {
            double[] d = new double[getValuesForCalibration().size()];
            for (int i = 0; i < d.length; i++) {
                d[i] = getValuesForCalibration().get(i).getChannelStrength();
            }
            setMean(mean(d));
        }

        public double applyCalibration(double signal) {
            return signal + getMean();
        }

        public void clearCalibration() {
            setMean(0);
        }

        public String getDescription() {
            return "Signal Proc for Testing";
        }

        public EEGSignalProcessor getNewSignalProcessor() {
            return new TestSignalProcessor();
        }

        protected double getMean() {
            return mean;
        }

        protected void setMean(double mean) {
            this.mean = mean;
        }
    }

    @SuppressWarnings("serial")
    private class TestDevice extends AbstractEEGDevice {

        public TestDevice() {
            super("Test Device");
        }

        public void closeDevice() throws EEGException {
            setStatusOfDevice("Device closed");
        }

        public void openDevice() throws EEGException {
            setStatusOfDevice("Device opened");
        }

        public void applyTestSignal(double[] rawData) {
            applySignalToChannels(rawData);
            notifyEEGReadListeners();
        }
    }
}
