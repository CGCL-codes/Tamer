package dsp.dummy;

import dsp.DataChunk;
import dsp.Filter;

/**
 *
 * @author Luke
 */
public class NullFilter implements Filter {

    public void start() {
    }

    public DataChunk process(DataChunk d) {
        return d;
    }

    public void stop() {
    }
}
