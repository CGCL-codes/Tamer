package net.url404.umodular.components;

import java.io.IOException;
import net.url404.umodular.*;

/**
 * Mono pulse oscillator.
 * <p>
 * INPUT 0: oscillation frequency (FreqComponentConnector)<br>
 * [OPTIONAL]: if not connected, use internal freq
 * <p>
 * INPUT 1: pulsewidth (LevelComponentConnector), range -1.00 - 1.00.<br>
 * [OPTIONAL]: if not connected, pulsewidth = 0
 * TODO: pulsewidth internal!
 * <p>
 * OUTPUT 0: output signal (SignalComponentConnector)
 *
 * @author      makela@url404.net
 */
public class PulseOscillatorComponent extends OscillatorComponent {

    private double pulseWidth = 0.0;

    /**
   * Constructor
   */
    public PulseOscillatorComponent() {
        this.vendorName = "U4PSC-1";
        this.numInputConnectors = 2;
        setInputConnectorName(0, "Freq");
        setInputConnectorName(1, "Pw");
    }

    /**
   * Constructor
   *
   * @param sampleRate    Sound system sample rate in Hz
   */
    public PulseOscillatorComponent(double sampleRate) {
        super(sampleRate);
        this.vendorName = "U4PSC-1";
        this.numInputConnectors = 2;
        setInputConnectorName(0, "Freq");
        setInputConnectorName(1, "Pw");
    }

    /**
   *
   * Read implementation. See SoundComponent read() method.
   *
   * @return                Next value
   */
    public double[] read(int portNum) {
        return outputBuf;
    }

    /**
   *
   * Advance implementation. See SoundComponent advance() method.
   *
   * @return                Next value
   * @throws    IOException Bad connection
   */
    public void advance() throws IOException {
        boolean inConn0 = (getInput(0) != null ? true : false);
        double freqHz[] = new double[0];
        if (inConn0) {
            freqHz = getInput(0).read();
        }
        double pulseW[] = new double[0];
        boolean pwConn = (getInput(1) != null ? true : false);
        if (pwConn) pulseW = getInput(1).read();
        for (int i = 0; i < UModularProperties.READ_BUFFER_SIZE; i++) {
            double w = (pwConn ? pulseW[i] : pulseWidth);
            outputBuf[i] = OscillatorComponent.lookupSine(oscRad) >= w ? 1.0 : -1.0;
            oscRad += Math.PI * 2 * getSampleFrameSize() * (inConn0 ? freqHz[i] : oscFrequency);
            if (oscRad > Math.PI * 2.0) {
                oscRad -= Math.PI * 2.0;
            }
        }
    }

    /**
   * Return a XML string representation of the component.
   */
    public String toXML() {
        return "<pulseoscillator " + "frequency=\"" + oscFrequency + "\" " + "initphase=\"" + getInitPhase() + "\" " + "pulsewidth=\"" + getPulseWidth() + "\"" + "/>";
    }

    /**
   * Set internal pulsewidth. Range -1.00 - 1.00.
   */
    public void setPulseWidth(double pw) {
        pulseWidth = pw;
    }

    /**
   * Get internal pulsewidth.
   */
    public double getPulseWidth() {
        return pulseWidth;
    }
}
