package backend.tools.tm;

public class WordFrequenceSet {

    int freq1 = 0;

    int freq2 = 0;

    String name = "";

    double norm = 0;

    public WordFrequenceSet() {
        this.freq1 = 1;
        this.freq2 = 1;
        this.norm = 1;
    }

    public double getOddFraction() {
        return Math.log((double) freq1 / (double) freq2);
    }

    public double getFraction() {
        return (double) freq1 / (double) freq2;
    }

    public String getName() {
        return name;
    }

    public void setFirst(int i) {
        this.freq1 = i;
    }

    public void setSecond(int i) {
        this.freq2 = i;
    }

    public int getFirst() {
        return this.freq1;
    }

    public int getSecond() {
        return this.freq2;
    }

    public void setName(String n) {
        this.name = n;
    }

    public void setNormFraction(double n) {
        this.norm = n;
    }

    public double getNormFraction() {
        return this.norm;
    }
}
