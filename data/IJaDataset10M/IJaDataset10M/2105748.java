package hu.sztaki.lpds.gemlcaquery.com;

/**
 * @author lpds
 */
public class GemlcaqueryDataBean {

    private String gemlcaUrl;

    private String glcName;

    private String usercert;

    /**
     * Required constructor for JavaBeans
     */
    public GemlcaqueryDataBean() {
        setGemlcaUrl("");
        setGlcName("");
        setUsercert("");
    }

    /**
     * Constructor for easier usage
     *
     * @param gemlcaUrl The identifier of the gemlca service (url)
     * @param usercert The content of the certificate file for the query
     */
    public GemlcaqueryDataBean(String gemlcaUrl, String usercert) {
        setGemlcaUrl(gemlcaUrl);
        setUsercert(usercert);
    }

    public String getGemlcaUrl() {
        return gemlcaUrl;
    }

    public void setGemlcaUrl(String gemlcaUrl) {
        this.gemlcaUrl = gemlcaUrl;
    }

    public String getGlcName() {
        return glcName;
    }

    public void setGlcName(String glcName) {
        this.glcName = glcName;
    }

    public String getUsercert() {
        return usercert;
    }

    public void setUsercert(String usercert) {
        this.usercert = usercert;
    }
}
