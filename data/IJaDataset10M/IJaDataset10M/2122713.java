package main;

/**
 * 
 * Simple class representing an amount of money in euro.
 * 
 * @author Michael Langowski
 * 
 */
public class Euro {

    private double amount;

    /**
	 * @param val the amount of money
	 */
    public Euro(double val) {
        if (val <= 0.00) {
            amount = 0.00;
        } else {
            amount = val;
        }
    }

    /**
	 * @param a
	 * @param b
	 * @return the amount of money resulting from "a + b"
	 */
    public static Euro add(Euro a, Euro b) {
        return new Euro(a.getAmount() + b.getAmount());
    }

    /**
	 * @param a
	 * @param b
	 * @return the amount of money resulting from "a - b"
	 */
    public static Euro subtract(Euro a, Euro b) {
        double result = a.getAmount() - b.getAmount();
        if (result >= 0) {
            return new Euro(result);
        } else {
            return new Euro(0.0);
        }
    }

    /**
	 * 
	 * @return the total number of cents of which this amount is made up of
	 */
    public int getCents() {
        return (int) (amount * 100);
    }

    /**
	 * @return the amount of money represented by this instance
	 */
    public double getAmount() {
        return amount;
    }
}
