package eu.popeye.context.concepts;

import org.semanticweb.owl.model.OWLOntology;

/**
 *
 * @author Ignacio Nieto Carvajal
 */
public class WebBrowser extends Software implements OWLObjectConcept, java.io.Serializable {

    private boolean javaActivated = false;

    private boolean javaScriptActivated = false;

    private float presentableFrames = 0;

    private float presentableTables = 0;

    private String supportedHTMLVersion = "unknown";

    /** Creates a new instance of WebBrowser */
    public WebBrowser(String internationalisation, String manufacturer, String name, String[] supportedFileFormats, String type, String version) {
        super(internationalisation, manufacturer, name, supportedFileFormats, type, version);
    }

    public boolean isJavaActivated() {
        return javaActivated;
    }

    public void setJavaActivated(boolean javaActivated) {
        this.javaActivated = javaActivated;
    }

    public boolean isJavaScriptActivated() {
        return javaScriptActivated;
    }

    public void setJavaScriptActivated(boolean javaScriptActivated) {
        this.javaScriptActivated = javaScriptActivated;
    }

    public float getPresentableFrames() {
        return presentableFrames;
    }

    public void setPresentableFrames(float presentableFrames) {
        this.presentableFrames = presentableFrames;
    }

    public float getPresentableTables() {
        return presentableTables;
    }

    public void setPresentableTables(float presentableTables) {
        this.presentableTables = presentableTables;
    }

    public String getSupportedHTMLVersion() {
        return supportedHTMLVersion;
    }

    public void setSupportedHTMLVersion(String supportedHTMLVersion) {
        this.supportedHTMLVersion = supportedHTMLVersion;
    }

    public String toString() {
        return super.toString() + ", java activated=" + javaActivated + ", javaScriptActivated=" + javaScriptActivated + ", number of presentable frames=" + presentableFrames + ", number of presentable tables=" + presentableTables + ", supported HTML version=" + supportedHTMLVersion;
    }

    public String getOntologicalName() {
        return "#WebBrowser";
    }

    public String getOntologicalName(OWLOntology ontology) {
        return ontology.getURI() + this.getOntologicalName();
    }
}
