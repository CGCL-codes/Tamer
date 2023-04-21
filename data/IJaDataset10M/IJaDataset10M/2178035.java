package net.googlecode.morenko.task5.workers;

/**
 * The manager class.
 */
public class Manager extends Worker {

    private boolean perHour;

    /**
     * Constructor, creates manager object.
     * @param firstName object first name.
     * @param lastName  object last name.
     */
    public Manager(String firstName, String lastName, int code) {
        super(firstName, lastName, code);
        post = Jobs.MANAGER;
    }

    /**
     * Set is the person has per hour rate.
     * @param perHour true or false.
     */
    public void setPerHour(boolean perHour) {
        this.perHour = perHour;
    }

    /**
     * Check is the person has per hour rate.
     * @return true or false.
     */
    public boolean isPerHour() {
        return this.perHour;
    }
}
