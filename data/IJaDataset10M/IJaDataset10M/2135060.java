package net.url404.umodular.components;

import java.io.IOException;
import net.url404.umodular.*;

/**
 * Mono triangle oscillator.
 * <p>
 * INPUT 0: oscillation frequency (FreqComponentConnector)<br>
 * [OPTIONAL]: if not connected, use internal freq
 * <p>
 * OUTPUT 0: output signal (SignalComponentConnector)
 *
 * @author      makela@url404.net
 */
public class TriangleOscillatorComponent extends OscillatorComponent {

    private double period1 = Math.PI / 2.0;

    private double period2 = Math.PI * 1.5;

    /**
   * Constructor
   */
    public TriangleOscillatorComponent() {
        this.vendorName = "U4TRI-1";
        this.numInputConnectors = 1;
        setInputConnectorName(0, "Freq");
    }

    /**
   * Constructor
   *
   * @param sampleRate    Sound system sample rate in Hz
   */
    public TriangleOscillatorComponent(double sampleRate) {
        super(sampleRate);
        this.vendorName = "U4TRI-1";
        this.numInputConnectors = 1;
        setInputConnectorName(0, "Freq");
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
   * Advance implementation. See SoundComponent advance() method.
   *
   * @throws    IOException Bad connection
   */
    public void advance() throws IOException {
        boolean inConn0 = (getInput(0) != null ? true : false);
        double freqHz[] = new double[0];
        if (inConn0) {
            freqHz = getInput(0).read();
        }
        for (int i = 0; i < UModularProperties.READ_BUFFER_SIZE; i++) {
            double oscLevel;
            if (oscRad < period1) {
                oscLevel = (1.0 / period1) * oscRad;
            } else if (oscRad < period2) {
                oscLevel = 1.0 - ((oscRad - period1) / Math.PI * 2.0);
            } else {
                oscLevel = -1.0 + ((oscRad - period2) / (Math.PI / 2.0));
            }
            outputBuf[i] = oscLevel;
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
        return "<triangleoscillator " + "frequency=\"" + oscFrequency + "\" " + "initphase=\"" + getInitPhase() + "\"" + "/>";
    }
}
