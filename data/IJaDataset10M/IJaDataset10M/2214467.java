package net.googlecode.morenko.task3;

/**
 * The employee class.
 */
public class Employee extends Worker {

    private int experience;

    /**
     * Constructor, creates employee object.
     * @param firstName employees first name.
     * @param lastName  employees last name.
     */
    public Employee(String firstName, String lastName) {
        super(firstName, lastName);
    }

    /**
     * Set the value of employee experience.
     * @param experience the value of experience.
     */
    public void setExperience(int experience) {
        this.experience = (experience > 0) ? experience : 0;
    }

    /**
     * Get the value of employee experience.
     * @return employee experience value.
     */
    public int getExperience() {
        return this.experience;
    }
}
