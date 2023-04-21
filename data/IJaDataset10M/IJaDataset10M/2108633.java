package text_adventure;

public class LimitedValue {

    private int maxValue;

    private int curValue;

    private String name;

    public LimitedValue(String name, int maximum) {
        this.curValue = maximum;
        this.maxValue = maximum;
        this.name = name;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getCurrentValue() {
        return curValue;
    }

    public void resetValue() {
        this.curValue = this.maxValue;
    }

    public void decrementValue(Integer amount) {
        this.curValue -= amount.intValue();
        if (this.curValue < 0) {
            this.curValue = 0;
        }
    }

    public void decrementValue() {
        this.decrementValue(1);
    }

    @Override
    public String toString() {
        return this.name + ": " + this.getCurrentValue() + "/" + this.getMaxValue();
    }

    public static void main(String[] args) {
        LimitedValue v = new LimitedValue("HP", 5);
        System.out.println(v);
        v.decrementValue(2);
        System.out.println(v);
        v.decrementValue();
        System.out.println(v);
        v.decrementValue(5);
        System.out.println(v);
    }
}
