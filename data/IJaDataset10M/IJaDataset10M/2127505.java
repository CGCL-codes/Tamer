package fitbook;

public class DiscountGroup {

    public double maxOwing, minPurchase;

    public String futureValue, description;

    public double discountPercent;

    public DiscountGroup(String futureValue, double maxOwing, double minPurchase, double discountPercent) {
        this.futureValue = futureValue;
        this.maxOwing = maxOwing;
        this.minPurchase = minPurchase;
        this.discountPercent = discountPercent;
        this.description = "";
    }

    public static DiscountGroup[] getElements() {
        return new DiscountGroup[] { new DiscountGroup("low", 0, 0, 0), new DiscountGroup("low", 0, 2000, 3), new DiscountGroup("medium", 500, 600, 3), new DiscountGroup("medium", 0, 500, 5), new DiscountGroup("high", 2000, 2000, 10) };
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public String getFutureValue() {
        return futureValue;
    }

    public double getMaxOwing() {
        return maxOwing;
    }

    public double getMinPurchase() {
        return minPurchase;
    }
}
