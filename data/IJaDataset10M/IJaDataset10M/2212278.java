package org.dbe.studio.core.smcore.core.eb.ent;

/**
 * @author gdorigo
 *
 */
public class SCS {

    private String name = new String();

    private String description = new String();

    private String version = new String();

    private SCSData data;

    private SCSZippedCode zippedCode = new SCSZippedCode();

    /**
     * @return ##
     */
    public SCSData getData() {
        return data;
    }

    /**
     * @param inData ##
     */
    public final void setData(final SCSData inData) {
        this.data = inData;
    }

    /**
     * @return ##
     */
    public final String getDescription() {
        return description;
    }

    /**
     * @param inDescription ##
     */
    public final void setDescription(final String inDescription) {
        this.description = inDescription;
    }

    /**
     * @return ##
     */
    public final String getName() {
        return name;
    }

    /**
     * @param inName ##
     */
    public final void setName(final String inName) {
        this.name = inName;
    }

    /**
     * @return ##
     */
    public final String getVersion() {
        return version;
    }

    /**
     * @param inVersion ##
     */
    public final void setVersion(final String inVersion) {
        this.version = inVersion;
    }

    /**
     * @return ##
     */
    public final SCSZippedCode getZippedCode() {
        return zippedCode;
    }

    /**
     * @param inZippedCode ##
     */
    public final void setZippedCode(final SCSZippedCode inZippedCode) {
        this.zippedCode = inZippedCode;
    }
}
