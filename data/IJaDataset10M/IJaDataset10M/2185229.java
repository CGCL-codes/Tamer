package de.tum.in.elitese.wahlsys.common.data;

/**
 * Container for regional information (subdistrict, electoralDistrict, state)
 * 
 * @author christoph
 */
public class RegionalInformation {

    private Integer fElectoralDistrict;

    private String fState;

    private String fSubdistrict;

    /**
	 * The Constructor.
	 * 
	 * @param subdistrict
	 * @param electoralDistrict
	 * @param state
	 */
    public RegionalInformation(String subdistrict, Integer electoralDistrict, String state) {
        super();
        this.fState = state;
        this.fSubdistrict = subdistrict;
        this.fElectoralDistrict = electoralDistrict;
    }

    public Integer getElectoralDistrict() {
        return fElectoralDistrict;
    }

    public String getState() {
        return fState;
    }

    public String getSubdistrict() {
        return fSubdistrict;
    }

    public void setElectoralDistrict(Integer electoralDistrict) {
        this.fElectoralDistrict = electoralDistrict;
    }

    public void setState(String state) {
        this.fState = state;
    }

    public void setSubdistrict(String subdistrict) {
        this.fSubdistrict = subdistrict;
    }
}
