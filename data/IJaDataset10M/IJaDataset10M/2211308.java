package playground.anhorni.crossborder.unused;

public class TimeBins {

    private int hour;

    private double delta;

    private int actualTimeBin;

    private double actualBinDelta;

    public TimeBins() {
        this.actualTimeBin = 0;
        this.actualBinDelta = 0.0;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    private void go2NextBin() {
        if (this.actualTimeBin == 11) {
            this.nextBinDelta();
        }
        this.actualTimeBin = (this.actualTimeBin + 1) % 12;
    }

    private void nextBinDelta() {
        this.actualBinDelta += delta;
    }

    public int getStartTime() {
        int time = (int) (this.actualTimeBin * 300 + this.actualBinDelta);
        this.go2NextBin();
        return time;
    }
}
