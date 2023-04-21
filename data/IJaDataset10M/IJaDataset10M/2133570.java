package realtimesound;

public class AudioBuffer {

    private AudioAnalyser analyser;

    private int frameSize;

    private int hopSize;

    private int numberOfHops;

    private int[] buffer;

    private double[] frame;

    private int headWrite, headRead;

    public AudioBuffer(int frameSize, int numberOfHops, AudioAnalyser analyser) {
        this.analyser = analyser;
        headWrite = 0;
        headRead = 0;
        this.frameSize = frameSize;
        this.hopSize = frameSize / numberOfHops;
        buffer = new int[frameSize];
        frame = new double[frameSize];
    }

    public void addSamples(int[] newData) {
        int N = newData.length;
        for (int i = 0; i < N; i++) {
            buffer[headWrite] = newData[i];
            headWrite++;
            if (headWrite == buffer.length) headWrite = 0;
            if (headWrite == headRead) {
                for (int n = 0; n < frameSize; n++) {
                    frame[n] = ((double) buffer[(headRead + n) % frameSize]) / 32768;
                }
                headRead += hopSize;
                if (headRead == buffer.length) headRead = 0;
                analyser.bufferReady();
            }
        }
    }

    public int getHopSize() {
        return hopSize;
    }

    public void setHopSize(int hopSize) {
        this.hopSize = hopSize;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public void setFrameSize(int frameSize) {
        this.frameSize = frameSize;
        buffer = new int[frameSize];
        frame = new double[frameSize];
    }

    public double[] getFrame() {
        return frame;
    }

    public int getNumberOfHops() {
        return numberOfHops;
    }

    public void setNumberOfHops(int numberOfHops) {
        this.numberOfHops = numberOfHops;
        hopSize = frameSize / numberOfHops;
    }

    public static void main(String[] args) {
    }
}
