package vavi.sound.mfi.vavi.nec;

import vavi.sound.mfi.InvalidMfiDataException;
import vavi.sound.mfi.vavi.sequencer.MachineDependFunction;
import vavi.sound.mfi.vavi.track.MachineDependMessage;
import vavi.util.Debug;

/**
 * NEC System exclusive message function 0xf2, 0x04 processor.
 * (vibrator)
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 030827 nsano initial version <br>
 */
public class Function242_4 implements MachineDependFunction {

    /**
     * 0xf2, 0x04 Vibrator, length 5
     *
     * @param message see below
     * <pre>
     * 0    delta
     * 1    ff
     * 2    ff
     * 3-4  length
     * 5    vendor
     * 6    f1
     * </pre>
     */
    public void process(MachineDependMessage message) throws InvalidMfiDataException {
        byte[] data = message.getMessage();
        int channel = (data[7] & 0xc0) >> 6;
        Debug.println("Vibrator: " + channel);
        Debug.dump(data);
    }
}
