package es.eucm.eadventure.editor.data.meta.lom;

public class LOMTechnical {

    private String minimumVersion;

    private String maximumVersion;

    public LOMTechnical() {
        minimumVersion = null;
        maximumVersion = null;
    }

    /**
     * @return the minimumVersion
     */
    public String getMinimumVersion() {
        return minimumVersion;
    }

    /**
     * @param minimumVersion
     *            the minimumVersion to set
     */
    public void setMinimumVersion(String minimumVersion) {
        this.minimumVersion = minimumVersion;
    }

    /**
     * @return the maximumVersion
     */
    public String getMaximumVersion() {
        return maximumVersion;
    }

    /**
     * @param maximumVersion
     *            the maximumVersion to set
     */
    public void setMaximumVersion(String maximumVersion) {
        this.maximumVersion = maximumVersion;
    }
}
