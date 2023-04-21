package org.gaea.demo.historyClasses;

/**
 * Class to test history. This is the mother of a children (girls of course).
 * 
 * @author mdmajor
 */
public class Mother extends Human {

    Child elderGirl;

    Child middleGirl;

    Child babyGirl;

    /**
	 * Constructor.
	 * 
	 * @param name
	 *            Name of the human.
	 * @param numberOfHair
	 *            Number of hair on the head of the person.
	 * @param elderGirl
	 *            The first born.
	 * @param middleGirl
	 *            The one born right after, but still called the middle one.
	 * @param babyGirl
	 *            The little and cute baby.
	 */
    public Mother(String name, int numberOfHair, Child elderGirl, Child middleGirl, Child babyGirl) {
        super(name, numberOfHair);
        this.elderGirl = elderGirl;
        this.middleGirl = middleGirl;
        this.babyGirl = babyGirl;
    }

    /**
	 * @return the babyGirl
	 */
    public Child getBabyGirl() {
        return babyGirl;
    }

    /**
	 * @return the elderGirl
	 */
    public Child getElderGirl() {
        return elderGirl;
    }

    /**
	 * @return the middleGirl
	 */
    public Child getMiddleGirl() {
        return middleGirl;
    }
}
