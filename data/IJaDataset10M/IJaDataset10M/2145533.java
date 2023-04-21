package uk.co.simphoney.audio;

import uk.co.simphoney.util.MyMath;

public class PeakAnalyst {

    double peakShort;

    double peakLong;

    double decayShort;

    double decayLong;

    double riseShort;

    double riseLong;

    double decayShort1;

    double decayLong1;

    double riseShort1;

    double riseLong1;

    double riseInf;

    double decayInf;

    double riseInf1;

    double decayInf1;

    double average;

    double noiseLevel;

    double rate;

    int frameSize;

    Thread pulseWaitThread;

    int pulseTimeInSamples;

    long sampleCount;

    long pulseCount;

    static PeakAnalyst the;

    /**
	 * 
	 * @param feed
	 *            FramedFeed
	 * @param shortT
	 *            double decay time to smooth out freq time scale stuff.
	 * @param longT
	 *            double decay Time to suss out pulses in the music.
	 */
    public static PeakAnalyst the() {
        return the;
    }

    public PeakAnalyst() {
        assert (the == null);
        the = this;
        this.decayShort = MyMath.halfLifeToLambda(this.rate / 100);
        this.riseShort = MyMath.halfLifeToLambda(this.rate / 500);
        this.decayLong = MyMath.halfLifeToLambda(this.rate * 0.2);
        this.riseLong = MyMath.halfLifeToLambda(this.rate * 0.02);
        this.decayInf = MyMath.halfLifeToLambda(this.rate * 5);
        this.riseInf = MyMath.halfLifeToLambda(this.rate * 5);
        this.decayShort1 = 1.0 - this.decayShort;
        this.riseShort1 = 1.0 - this.riseShort;
        this.decayLong1 = 1.0 - this.decayLong;
        this.riseLong1 = 1.0 - this.riseLong;
        this.decayInf1 = 1.0 - this.decayInf;
        this.riseInf1 = 1.0 - this.riseInf;
    }

    public void fireNewFramedFeedData(short[] v) {
        int n = v.length;
        for (int i = 0; i < n; i++) {
            this.sampleCount++;
            double val = Math.abs(v[i]);
            if (val < this.peakShort) this.peakShort = this.peakShort * this.decayShort + val * this.decayShort1; else this.peakShort = this.peakShort * this.riseShort + val * this.riseShort1;
            if (this.peakShort < this.peakLong) this.peakLong = this.peakLong * this.decayLong + this.peakShort * this.decayLong1; else this.peakLong = this.peakLong * this.riseLong + this.peakShort * this.riseLong1;
            if (this.peakLong < this.average) this.average = this.average * this.decayInf + this.peakLong * this.decayInf1; else this.average = this.average * this.riseInf + this.peakLong * this.riseInf1;
            if (this.peakLong > this.average && this.pulseWaitThread != null) {
                this.pulseCount = this.sampleCount;
                this.pulseWaitThread.interrupt();
                this.pulseWaitThread = null;
            }
        }
    }

    public synchronized long waitForPulse(long milliMax) {
        assert (this.pulseWaitThread == null);
        this.pulseWaitThread = Thread.currentThread();
        try {
            wait(milliMax);
            System.out.println(" waitForPulse timed out");
            this.pulseWaitThread = null;
        } catch (InterruptedException ex) {
            this.pulseWaitThread = null;
            return (long) ((this.pulseCount * 1000.0) / this.rate);
        }
        return -1;
    }
}
