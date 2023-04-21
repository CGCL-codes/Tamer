package com.entelience.probe.patch;

/**
 *   All PatchProbe should have a reference of this object to store their configuration
 */
public class VulnProbe {

    public static final int DEFAULT_RANK = 0;

    public static final int DEFAULT_RANK_NIST = 10;

    public static final int DEFAULT_RANK_SECURITYFOCUS = 11;

    public static final int DEFAULT_RANK_CERTIST = 12;

    public static final int DEFAULT_RANK_LEXSI = 13;

    public static final int DEFAULT_RANK_MICROSOFT_XML = 100;

    public static final int DEFAULT_RANK_MICROSOFT_RSS_BULLETIN = 110;

    public static final int DEFAULT_RANK_MICROSOFT_RSS_ADVISORY = 111;

    public static final int DEFAULT_RANK_MICROSOFT_RSS_TEXTONLY = 112;

    public static final int DEFAULT_RANK_SCAN = 1000;

    public static final int RANK_REJECTED = 10000;

    private int rank = RANK_REJECTED;

    private boolean requiresCVE = false;

    private boolean investigateRepairedWithExploit = false;

    private boolean investigateAcceptRiskWithExploit = false;

    private boolean renewIgnoredWithExploit = false;

    private boolean renewAcceptRiskWithExploit = false;

    private boolean renewIgnoredWithUpdate = false;

    private boolean renewRepairedWithExploit = false;

    private boolean renewAcceptRiskWithUpdate = false;

    private boolean investigateAcceptRiskWithUpdate = false;

    private boolean rejectVulnerabilityWithoutProduct = true;

    private boolean newVulnerabilityPriorityMatchVendorSeverity = false;

    private boolean addNewVPV = false;

    private boolean resetVPV = false;

    private int minSeverityToImport = 0;

    /**
      * Renew a vulnerability that was previously ignored if
      * there is an updated advisory
      */
    public boolean renewIgnoredWithUpdate() {
        return renewIgnoredWithUpdate;
    }

    /**
      * Renew a vulnerability that was previously ignored if
      * there is an updated advisory with exploit (version > 2.0)
      */
    public boolean renewIgnoredWithExploit() {
        return renewIgnoredWithExploit;
    }

    /**
      * Renew a vulnerability that was previously repaired
      * if there is an updated advisory
      */
    public boolean renewRepairedWithExploit() {
        return renewRepairedWithExploit;
    }

    /**
      * Sets back to investigate a "RepairED", so fixed,
      * vulnerability on publication of an exploit
      */
    public boolean investigateRepairedWithExploit() {
        return investigateRepairedWithExploit;
    }

    /**
      * Sets back to New a vulnerability that was
      * Risk Accepted on the publication of an exploit
      */
    public boolean renewAcceptRiskWithExploit() {
        return renewAcceptRiskWithExploit;
    }

    /**
      * Sets back to New a vulnerability that was 
      * Risk Accepted on the publication of an exploit
      */
    public boolean renewAcceptRiskWithUpdate() {
        return renewAcceptRiskWithUpdate;
    }

    /**
      * Sets back to Investigate a vulnerability that was
      * Risk Accepted on the publication of an update
      */
    public boolean investigateAcceptRiskWithUpdate() {
        return investigateAcceptRiskWithUpdate;
    }

    /**
      * Sets back to Investigate a vulnerability that was
      * Risk Accepted on the publication of an exploit
	*/
    public boolean investigateAcceptRiskWithExploit() {
        return investigateAcceptRiskWithExploit;
    }

    /**
     * Rejects a vulnerability without products
     */
    public boolean rejectVulnerabilityWithoutProduct() {
        return rejectVulnerabilityWithoutProduct;
    }

    /**
	 * Returns the rank for this source
	*/
    public int getRank() {
        return rank;
    }

    /**
	 * Sets the rank for this source. The minimum - enforced -  is RANK_DEFAULT (0),
 	 * the maximum authorized value is RANK_REJECTED. If the provided value is
	 * outside of this range then RANGE_DEFAULT is used.
	 */
    public void setRank(int rank) {
        if (rank < 0 || rank > RANK_REJECTED) {
            this.rank = DEFAULT_RANK;
        } else {
            this.rank = rank;
        }
    }

    /**
     * Ignores advisories with severity lower to this one.
     */
    public int getMinSeverityToImport() {
        return minSeverityToImport;
    }

    /**
	 * Sets the minimum severity to import. The minimum - enforced - value
	 * is zero (0), the maximum - enforced - value is four (4).
	 */
    public void setMinSeverityToImport(int sev) {
        if (sev < 0) minSeverityToImport = 0; else if (sev > 4) minSeverityToImport = 4; else minSeverityToImport = sev;
    }

    public void setInvestigateAcceptRiskWithExploit(boolean investigateAcceptRiskWithExploit) {
        this.investigateAcceptRiskWithExploit = investigateAcceptRiskWithExploit;
    }

    public void setInvestigateAcceptRiskWithUpdate(boolean investigateAcceptRiskWithUpdate) {
        this.investigateAcceptRiskWithUpdate = investigateAcceptRiskWithUpdate;
    }

    public void setInvestigateRepairedWithExploit(boolean investigateRepairedWithExploit) {
        this.investigateRepairedWithExploit = investigateRepairedWithExploit;
    }

    public void setRejectVulnerabilityWithoutProduct(boolean rejectVulnerabilityWithoutProduct) {
        this.rejectVulnerabilityWithoutProduct = rejectVulnerabilityWithoutProduct;
    }

    public void setRenewAcceptRiskWithExploit(boolean renewAcceptRiskWithExploit) {
        this.renewAcceptRiskWithExploit = renewAcceptRiskWithExploit;
    }

    public void setRenewAcceptRiskWithUpdate(boolean renewAcceptRiskWithUpdate) {
        this.renewAcceptRiskWithUpdate = renewAcceptRiskWithUpdate;
    }

    public void setRenewIgnoredWithExploit(boolean renewIgnoredWithExploit) {
        this.renewIgnoredWithExploit = renewIgnoredWithExploit;
    }

    public void setRenewIgnoredWithUpdate(boolean renewIgnoredWithUpdate) {
        this.renewIgnoredWithUpdate = renewIgnoredWithUpdate;
    }

    public void setRenewRepairedWithExploit(boolean renewRepairedWithExploit) {
        this.renewRepairedWithExploit = renewRepairedWithExploit;
    }

    /**
	 * Sets the option requires CVE which will force to ignore all
	 * vulnerabilities without any CVE reference (direct or alias).
	 */
    public void setRequiresCVE(boolean requiresCVE) {
        this.requiresCVE = requiresCVE;
    }

    /**
      * Should only advisory with a CVE alias be imported ?
      */
    public boolean requiresCVE() {
        return requiresCVE;
    }

    /**
     * do newly imported vunerability priority match the vendor severity 
     */
    public boolean isNewVulnerabilityPriorityMatchVendorSeverity() {
        return newVulnerabilityPriorityMatchVendorSeverity;
    }

    public void setNewVulnerabilityPriorityMatchVendorSeverity(boolean newVulnerabilityPriorityMatchVendorSeverity) {
        this.newVulnerabilityPriorityMatchVendorSeverity = newVulnerabilityPriorityMatchVendorSeverity;
    }

    /**
	 * Returns the status of the add new vpv option.
	 */
    public boolean isAddNewVPV() {
        return addNewVPV;
    }

    /**
	 * Returns the status of the reset vpv option.
	 */
    public boolean isResetVPV() {
        return resetVPV;
    }

    /**
	* Sets the add new and reset vpv options. If resetVPV is true then add new is forced
	* to true.
	*/
    public void setVPVOptions(boolean addNewVPV, boolean resetVPV) {
        this.addNewVPV = addNewVPV;
        this.resetVPV = resetVPV;
        if (resetVPV) this.addNewVPV = true;
    }
}
