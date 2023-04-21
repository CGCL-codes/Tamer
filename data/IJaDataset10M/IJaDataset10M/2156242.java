package tests.ajax;

public class RandomNumberBean {

    private int range = 1;

    private double result;

    public int getRange() {
        System.out.println("[getRange] range=" + range);
        return (range);
    }

    public void setRange(int range) {
        System.out.println("[setRange] range=" + range);
        this.range = range;
    }

    public String makeResult() {
        result = Math.random() * range;
        return (null);
    }

    public double getResult() {
        return (result);
    }
}
