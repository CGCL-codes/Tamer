package net.url404.umodular.components;

import java.io.IOException;
import net.url404.umodular.*;

/**
 * Mono noise oscillator.
 * <p>
 * OUTPUT 0: output signal (SignalComponentConnector)
 *
 * @author      makela@url404.net
 */
public class NoiseOscillatorComponent extends OscillatorComponent {

    /**
   * Constructor - does nothing at all
   */
    public NoiseOscillatorComponent() {
        this.vendorName = "U4NSC-1";
        this.numInputConnectors = 0;
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
   * @throws    IOException Bad connection
   */
    public void advance() throws IOException {
        for (int i = 0; i < UModularProperties.READ_BUFFER_SIZE; i++) {
            outputBuf[i] = -1.0 + (Math.random() * 2.0);
        }
    }

    /**
   * Return a XML string representation of the component.
   */
    public String toXML() {
        return "<noiseoscillator/>";
    }
}
