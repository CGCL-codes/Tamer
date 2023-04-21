package newgen.presentation.opencatalog;

/**
 *
 * @author sandeep
 */
public class OCStaticValues {

    private String OCServerHost = "localhost";

    private String OCServerPort = "8080";

    private String OCURL = "";

    private String organizationId = "";

    private String verificationCode = "";

    private static OCStaticValues thisInstance;

    private OCStaticValues() {
    }

    public static OCStaticValues getInstance() {
        if (thisInstance == null) thisInstance = new OCStaticValues();
        return thisInstance;
    }

    /**
     * @return the OCServerHost
     */
    public String getOCServerHost() {
        return OCServerHost;
    }

    /**
     * @param OCServerHost the OCServerHost to set
     */
    public void setOCServerHost(String OCServerHost) {
        this.OCServerHost = OCServerHost;
    }

    /**
     * @return the OCServerPort
     */
    public String getOCServerPort() {
        return OCServerPort;
    }

    /**
     * @param OCServerPort the OCServerPort to set
     */
    public void setOCServerPort(String OCServerPort) {
        this.OCServerPort = OCServerPort;
    }

    /**
     * @return the OCURL
     */
    public String getOCURL() {
        return OCURL;
    }

    /**
     * @param OCURL the OCURL to set
     */
    public void setOCURL(String OCURL) {
        this.OCURL = OCURL;
    }

    /**
     * @return the organizationId
     */
    public String getOrganizationId() {
        return organizationId;
    }

    /**
     * @param organizationId the organizationId to set
     */
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @return the verificationCode
     */
    public String getVerificationCode() {
        return verificationCode;
    }

    /**
     * @param verificationCode the verificationCode to set
     */
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
