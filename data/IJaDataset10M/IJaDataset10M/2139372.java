package org.gridbus.broker.common;

import org.gridbus.broker.constants.ScheduleOptimisationType;

/**
 * @author Krishna
 *
 */
public final class Qos {

    private long deadline;

    private float budget;

    private int optimisation;

    private float budgetSpent;

    private long timeSpent;

    private static final transient long ONE_DAY_MILLIS = 86400000;

    /**
	 */
    public Qos() {
        deadline = System.currentTimeMillis() + ONE_DAY_MILLIS;
        budget = Long.MAX_VALUE;
        optimisation = ScheduleOptimisationType.NONE;
    }

    /**
	 * @return Returns the budget.
	 */
    public float getBudget() {
        return budget;
    }

    /**
	 * @param budget The budget to set.
	 */
    public void setBudget(float budget) {
        this.budget = budget;
    }

    /**
	 * @return Returns the deadline.
	 */
    public long getDeadline() {
        return deadline;
    }

    /**
	 * @param deadline The deadline to set.
	 */
    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    /**
	 * @return Returns the optimisation.
	 */
    public int getOptimisation() {
        return optimisation;
    }

    /**
	 * @param optimisation The optimisation to set.
	 */
    public void setOptimisation(int optimisation) {
        this.optimisation = optimisation;
    }

    /**
	 * @return budget spent
	 */
    public float getBudgetSpent() {
        return budgetSpent;
    }

    /**
	 * @param budgetSpent The budgetSpent to set.
	 */
    void setBudgetSpent(float budgetSpent) {
        this.budgetSpent = budgetSpent;
    }

    /**
	 * @return Returns the timeSpent.
	 */
    public long getTimeSpent() {
        return timeSpent;
    }

    /**
	 * @param timeSpent The timeSpent to set.
	 */
    void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    public boolean equals(Object o) {
        boolean isEqual = false;
        if (o instanceof Qos) {
            Qos other = (Qos) o;
            if (other.getBudget() == this.getBudget()) {
                if (other.getBudgetSpent() == this.getBudgetSpent()) {
                    if (other.getDeadline() == this.getDeadline()) {
                        if (other.getTimeSpent() == this.getTimeSpent()) {
                            if (other.getOptimisation() == this.getOptimisation()) {
                                isEqual = true;
                            }
                        }
                    }
                }
            }
        }
        return isEqual;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Qos: ").append(this.getClass().getName()).append("\n");
        sb.append("Deadline: ").append(getDeadline()).append("\n");
        sb.append("Time Spent: ").append(getTimeSpent()).append("\n");
        sb.append("Budget: ").append(getBudget()).append("\n");
        sb.append("Budget Spent: ").append(getBudgetSpent()).append("\n");
        sb.append("Optimisation: ").append(ScheduleOptimisationType.stringValue(getOptimisation()));
        sb.append("\n");
        return sb.toString();
    }
}
