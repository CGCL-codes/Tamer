package businessClasses;

/**
 * @author Noel Alex Makumuli
 *
 */
public class EmploymentType {

    private String employmentType = "";

    /**
	 * 
	 */
    public EmploymentType() {
    }

    /**
	 * @param employmentType
	 */
    public EmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    /**
	 * @return the employmentType
	 */
    public String getEmploymentType() {
        return employmentType;
    }

    /**
	 * @param employmentType the employmentType to set
	 */
    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }
}
