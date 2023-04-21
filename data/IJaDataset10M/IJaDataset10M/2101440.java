package Server;

/**
 * @author Long
 *
 * @version $Revision: 1.0 $
 */
public class DoctorsOrders {

    /**
	 * Field prescription.
	 */
    private String prescription, labWork, followUp, otherInstr;

    /**
	 * Constructor for DoctorsOrders.
	 * @param prescription String
	 * @param labWork String
	 * @param followUp String
	 * @param otherInstr String
	 */
    public DoctorsOrders(String prescription, String labWork, String followUp, String otherInstr) {
        super();
        this.prescription = prescription;
        this.labWork = labWork;
        this.followUp = followUp;
        this.otherInstr = otherInstr;
    }

    /**
	 * Method toString.
	 * @return String
	 */
    public String toString() {
        final String str = "prescription is " + prescription;
        return str;
    }

    /**
	 * Method getPrescription.
	 * @return String
	 */
    public String getPrescription() {
        return prescription;
    }

    /**
	 * Method setPrescription.
	 * @param prescription String
	 */
    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    /**
	 * Method getLabWork.
	 * @return String
	 */
    public String getLabWork() {
        return labWork;
    }

    /**
	 * Method setLabWork.
	 * @param labWork String
	 */
    public void setLabWork(String labWork) {
        this.labWork = labWork;
    }

    /**
	 * Method getFollowUp.
	 * @return String
	 */
    public String getFollowUp() {
        return followUp;
    }

    /**
	 * Method setFollowUp.
	 * @param followUp String
	 */
    public void setFollowUp(String followUp) {
        this.followUp = followUp;
    }

    /**
	 * Method getOtherInstr.
	 * @return String
	 */
    public String getOtherInstr() {
        return otherInstr;
    }

    /**
	 * Method setOtherInstr.
	 * @param otherInstr String
	 */
    public void setOtherInstr(String otherInstr) {
        this.otherInstr = otherInstr;
    }
}
