package shellkk.qiq.math.ml;

import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import shellkk.qiq.math.StopException;
import shellkk.qiq.math.StopHandle;

public class ArraySampleSet implements SampleSet {

    private static Log log = LogFactory.getLog(ArraySampleSet.class);

    private Sample[] samples;

    private int index = -1;

    public static ArraySampleSet toArraySampleSet(SampleSet source, StopHandle stopHandle) throws StopException, TrainException {
        if (source instanceof ArraySampleSet) {
            return (ArraySampleSet) source;
        }
        try {
            source.open();
            ArrayList<Sample> samples = new ArrayList();
            while (source.hasNext()) {
                if (stopHandle != null && stopHandle.isStoped()) {
                    throw new StopException();
                }
                samples.add(source.next());
            }
            return new ArraySampleSet(samples.toArray(new Sample[samples.size()]));
        } catch (StopException e) {
            throw e;
        } catch (Exception e) {
            throw new TrainException(e);
        } finally {
            try {
                source.close();
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    public ArraySampleSet() {
        samples = new Sample[0];
    }

    public ArraySampleSet(Sample[] samples) {
        this.samples = samples;
    }

    public Sample[] getSamples() {
        return samples;
    }

    public void setSamples(Sample[] samples) {
        this.samples = samples;
    }

    public boolean available() {
        return index >= 0;
    }

    public void close() {
        index = -1;
    }

    public boolean hasNext() throws Exception {
        if (index < 0) {
            throw new Exception("array closed!");
        }
        return index < samples.length;
    }

    public Sample next() throws Exception {
        int pos = index;
        if (pos < 0) {
            throw new Exception("array closed!");
        }
        if (pos >= samples.length) {
            throw new Exception("no more data!");
        }
        index++;
        return samples[pos];
    }

    public void open() throws Exception {
        index = 0;
    }

    public void first() throws Exception {
        index = 0;
    }
}
