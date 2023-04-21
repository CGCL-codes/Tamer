package record;

import java.io.Serializable;

/**
 * The Class LabWork.
 * 
 * @author Jonathan Cherry
 * @version 1.0.0
 */
public class LabWork implements Serializable {

    /** The Info. */
    private String info;

    /** The Price. */
    private double price;

    /**
	 * Instantiates a new lab work.
	 */
    public LabWork() {
        this("", 0);
    }

    /**
	 * Instantiates a new lab work.
	 * 
	 * @param info
	 *            the info
	 * @param price
	 *            the price
	 */
    public LabWork(String info, double price) {
        this.info = info;
        this.price = price;
    }

    /**
	 * Gets the info.
	 * 
	 * @return the info
	 */
    public String getInfo() {
        return info;
    }

    /**
	 * Gets the price.
	 * 
	 * @return the price
	 */
    public double getPrice() {
        return price;
    }

    /**
	 * Sets the info.
	 * 
	 * @param info
	 *            the new info
	 */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
	 * Sets the price.
	 * 
	 * @param price
	 *            the new price
	 */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
	 * New lab work for urinalysis BUN.
	 * 
	 * @return the new lab work
	 */
    public LabWork labUrinalysisBUN() {
        return new LabWork("Urinalysis (BUN)", 50.00);
    }

    /**
	 * New lab work for drug test.
	 * 
	 * @return the new lab work
	 */
    public LabWork labUrinalysisDrug() {
        return new LabWork("Urinalysis (Drug Test)", 25.00);
    }

    /**
	 * Lab work for CBC
	 * 
	 * @return the new lab work
	 */
    public LabWork labCBC() {
        return new LabWork("CBC", 75.00);
    }

    /**
	 * Lab work for cholesterol
	 * 
	 * @return the new lab work
	 */
    public LabWork labCholesterol() {
        return new LabWork("Cholesterol", 40.00);
    }

    /**
	 * Lab work for HIV
	 * 
	 * @return the new lab work
	 */
    public LabWork labHIV() {
        return new LabWork("HIV", 100.00);
    }

    /**
	 * Lab work for glucose
	 * 
	 * @return the new lab work
	 */
    public LabWork labGlucose() {
        return new LabWork("Glucose", 75.00);
    }

    /**
	 * Lab work for adrenal
	 * 
	 * @return the new lab work
	 */
    public LabWork labAdrenal() {
        return new LabWork("Adrenal", 65.00);
    }

    /**
	 * Lab work for EKG
	 * 
	 * @return the new lab work
	 */
    public LabWork labEKG() {
        return new LabWork("EKG", 100.00);
    }

    /**
	 * Lab work for MRI
	 * 
	 * @return the new lab work
	 */
    public LabWork labMRI() {
        return new LabWork("MRI", 1000.00);
    }

    /**
	 * Converts the lab work to a string.
	 * 
	 * @return the lab work in string form
	 */
    @Override
    public String toString() {
        return this.toString();
    }
}
