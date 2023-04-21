package jpcsp.sound;

/**
 * @author gid15
 *
 */
public class SampleSourceWithDelay implements ISampleSource {

    private ISampleSource sampleSource;

    private int delay;

    private int sampleIndex;

    public SampleSourceWithDelay(ISampleSource sampleSource, int delay) {
        this.sampleSource = sampleSource;
        this.delay = delay;
    }

    @Override
    public short getNextSample() {
        short sample;
        if (sampleIndex < delay) {
            sample = 0;
            sampleIndex++;
        } else {
            sample = sampleSource.getNextSample();
        }
        return sample;
    }

    @Override
    public int getNumberSamples() {
        return delay + sampleSource.getNumberSamples();
    }

    @Override
    public int getSampleIndex() {
        if (sampleIndex < delay) {
            return sampleIndex;
        }
        return delay + sampleSource.getSampleIndex();
    }

    @Override
    public void setSampleIndex(int index) {
        if (index < delay) {
            sampleIndex = index;
            sampleSource.setSampleIndex(0);
        } else {
            sampleIndex = delay;
            sampleSource.setSampleIndex(index - delay);
        }
    }
}
