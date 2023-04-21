package OfficeServer.report;

import java.io.Serializable;

/**
 * This is the subclass of Treatment called Prescriptions. This class will
 * contain the information relating to a certain prescription that is issued to
 * a certain patient.
 * 
 * @author So-mi
 */
public class Prescription extends Treatment implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String doctor;

    private String patient;

    private int dueDate;

    private String instructions;

    private double cost;

    private int dosage;

    private String drug;

    private int treatmentID;

    /**
     * Creates Prescription
     * 
     * @param doctor
     *            String representing who issued the prescription
     * @param patient
     *            String representing the name of the patient the prescription
     *            is issued to
     * @param dueDate
     *            int representing the date which the prescription is issued
     * @param instructions
     *            String representing additional instructions (e.g. follow up
     *            instructions) from the doctor
     * @param cost
     *            double representing the cost of the prescription
     * @param dosage
     *            int representing the dosage
     * @param drug
     *            String representing what drug is prescribed.
     */
    public Prescription(String doctor, String patient, int dueDate, String instructions, double cost, int dosage, String drug) {
        this.doctor = doctor;
        this.patient = patient;
        this.dueDate = dueDate;
        this.instructions = instructions;
        this.cost = cost;
        this.dosage = dosage;
        this.drug = drug;
        treatmentID = super.tCount;
        super.tCount++;
    }

    /**
     * gets the name of the doctor who issued the prescription
     * 
     * @return doctor (String)
     */
    public String getDoctor() {
        return doctor;
    }

    /**
     * sets the name of the doctor
     * 
     * @param doctor
     */
    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    /**
     * gets the name of the patient whose the prescription is referring to
     * 
     * @return patient (String)
     */
    public String getPatient() {
        return patient;
    }

    /**
     * sets the patient
     * 
     * @param patient
     */
    public void setPatient(String patient) {
        this.patient = patient;
    }

    /**
     * gets the date which the prescription is issued
     * 
     * @return dueDate (int)
     */
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
     * gets the instructions the doctor wrote with the prescription
     * 
     * @return instructions (String)
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * sets instructions
     * 
     * @param instructions
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * gets the cost of the prescription issued
     * 
     * @return cost (double)
     */
    public double getCost() {
        return cost;
    }

    /**
     * sets the cost of the prescription
     * 
     * @param cost
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * gets the dosage of prescription
     * 
     * @return dosage (int)
     */
    public int getDosage() {
        return dosage;
    }

    /**
     * sets the dosage
     * 
     * @param dosage
     */
    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    /**
     * gets the name of the drug which was prescribed
     * 
     * @return drug (String)
     */
    public String getDrug() {
        return drug;
    }

    /**
     * sets the name of the drug
     * 
     * @param drug
     */
    public void setDrug(String drug) {
        this.drug = drug;
    }

    /**
     * Updates the prescription It will check (for Strings) if there is a field
     * in Prescription update that is filled out. If not, it'll use the current
     * prescription's information. If it is, it will set the current
     * prescription's information to update's information.
     * 
     * @param update
     *            Prescription object with the newest information
     */
    public void updatePrescription(Prescription update) {
        if (update != null) {
            this.doctor = (update.getDoctor() == null) ? this.doctor : new String(update.getDoctor());
            this.patient = (update.getPatient() == null) ? this.patient : new String(update.getPatient());
            this.dueDate = update.getDueDate();
            this.instructions = (update.getInstructions() == null) ? this.instructions : new String(update.getInstructions());
            this.cost = update.getCost();
            this.dosage = update.getDosage();
            this.drug = (update.getDrug() == null) ? this.drug : new String(update.getDrug());
        }
    }

    @Override
    public int getTID() {
        return treatmentID;
    }
}
