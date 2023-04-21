package OfficeServer.report;

import java.io.Serializable;

/**
 * @author So-mi
 * 
 */
public abstract class Treatment implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String doctor;

    private String patient;

    private int dueDate;

    private String instructions;

    private double cost;

    protected static int tCount = 0;

    /**
     * gets the due date
     * 
     * @return duedate
     */
    public abstract int getTID();

    public int getDueDate() {
        return dueDate;
    }

    /**
     * sets the due date
     * 
     * @param dueDate
     */
    public void setDueDate(int dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * gets the instructions
     * 
     * @return instructions
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * sets the instructions
     * 
     * @param instructions
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * gets the cost
     * 
     * @return cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * sets the cost
     * 
     * @param cost
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Deletes the treatment from the array (technically sets that "null"
     * 
     * @param ID of the Treatment that needs to be deleted
     */
    public void deleteTreatment(int ID) {
    }

    public Treatment clone() {
        return null;
    }

    public void update(Treatment update) {
    }
}
