package my.application.beans;

/**
 * @author mgeisinger
 *
 */
public class System6_49Bean {

    private String systemName = "6 aus 49";

    private Integer numbersCount = Integer.valueOf(6);

    private Integer lowestNumber = Integer.valueOf(1);

    private Integer maximumNumber = Integer.valueOf(49);

    private Integer[] numbers = new Integer[0];

    public Integer getLowestNumber() {
        return this.lowestNumber;
    }

    /**
	 * 
	 * @param lowestNumber
	 */
    public void setLowestNumber(Integer lowestNumber) {
        this.lowestNumber = lowestNumber;
    }

    public Integer getMaximumNumber() {
        return this.maximumNumber;
    }

    /**
	 * 
	 * @param maximumNumber
	 */
    public void setMaximumNumber(Integer maximumNumber) {
        this.maximumNumber = maximumNumber;
    }

    public Integer getNumbersCount() {
        return this.numbersCount;
    }

    /**
	 * 
	 * @param numbersCount
	 */
    public void setNumbersCount(Integer numbersCount) {
        this.numbersCount = numbersCount;
    }

    public String getSystemName() {
        return this.systemName;
    }

    /**
	 * 
	 * @param systemName
	 */
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Integer[] getNumbers() {
        return this.numbers;
    }

    public void setNumbers(Integer[] numbers) {
        this.numbers = numbers;
    }
}
