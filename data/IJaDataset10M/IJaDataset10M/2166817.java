package businessClasses;

/**
 * @author Noel Alex Makumuli
 *
 */
public class CompanyBranch {

    private String companyBranchID = "";

    private String companyBranchName = "";

    private String companyBranchLocation = "";

    private String companyBranchCity = "";

    private String companyBranchCountry = "";

    /**
	 * 
	 */
    public CompanyBranch() {
    }

    /**
	 * @param companyBranchID
	 * @param companyBranchName
	 * @param companyBranchLocation
	 * @param companyBranchCity
	 * @param companyBranchCountry
	 */
    public CompanyBranch(String companyBranchID, String companyBranchName, String companyBranchLocation, String companyBranchCity, String companyBranchCountry) {
        this.companyBranchID = companyBranchID;
        this.companyBranchName = companyBranchName;
        this.companyBranchLocation = companyBranchLocation;
        this.companyBranchCity = companyBranchCity;
        this.companyBranchCountry = companyBranchCountry;
    }

    /**
	 * @return the companyBranchID
	 */
    public String getCompanyBranchID() {
        return companyBranchID;
    }

    /**
	 * @return the companyBranchName
	 */
    public String getCompanyBranchName() {
        return companyBranchName;
    }

    /**
	 * @return the companyBranchLocation
	 */
    public String getCompanyBranchLocation() {
        return companyBranchLocation;
    }

    /**
	 * @return the companyBranchCity
	 */
    public String getCompanyBranchCity() {
        return companyBranchCity;
    }

    /**
	 * @return the companyBranchCountry
	 */
    public String getCompanyBranchCountry() {
        return companyBranchCountry;
    }

    /**
	 * @param companyBranchID the companyBranchID to set
	 */
    public void setCompanyBranchID(String companyBranchID) {
        this.companyBranchID = companyBranchID;
    }

    /**
	 * @param companyBranchName the companyBranchName to set
	 */
    public void setCompanyBranchName(String companyBranchName) {
        this.companyBranchName = companyBranchName;
    }

    /**
	 * @param companyBranchLocation the companyBranchLocation to set
	 */
    public void setCompanyBranchLocation(String companyBranchLocation) {
        this.companyBranchLocation = companyBranchLocation;
    }

    /**
	 * @param companyBranchCity the companyBranchCity to set
	 */
    public void setCompanyBranchCity(String companyBranchCity) {
        this.companyBranchCity = companyBranchCity;
    }

    /**
	 * @param companyBranchCountry the companyBranchCountry to set
	 */
    public void setCompanyBranchCountry(String companyBranchCountry) {
        this.companyBranchCountry = companyBranchCountry;
    }
}
