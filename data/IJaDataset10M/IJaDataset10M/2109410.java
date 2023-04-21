package org.dbe.studio.core.smcore.core.eb;

/**
 * @author gdorigo
 *
 */
public class ServiceDNA {

    public static final String XMLTAG_SERVICE_DNA = "ServiceDNA";

    public static final String XMLTAG_ID = "DNAID";

    public static final String XMLTAG_VERSION = "VersionNumber";

    public static final String XMLTAG_ANCESTOR_ID = "AncestorSDNAID";

    public static final String XMLTAG_ROOT_ID = "RootSDNAID";

    public static final String XMLTAG_BML_MODEL = "BML_Model";

    public static final String XMLTAG_SDL_MODEL = "SDL_Model";

    public static final String XMLTAG_AVAILABILITY = "Availability";

    public static final String XMLTAG_REGISTRARID = "Registrar_ID";

    public static final String XMLTAG_PUBLICATION_DATE = "PublicationDate";

    public static final String XMLTAG_LAST_CHANGE_DATE = "LastChangeDate";

    public static final String XMLTAG_ICON_URL = "Icon_URL";

    private String id;

    private String version;

    private String ancestorId;

    private String rootId;

    private String bmlModel;

    private String sdlModel;

    private String availability;

    private String registrarId;

    private java.util.Date publicationDate;

    private java.util.Date lastChangeDate;

    private String iconUrl;

    /**
     * @return ##
     */
    public final String getAncestorId() {
        return ancestorId;
    }

    /**
     * @param inAncestorId ##
     */
    public final void setAncestorId(final String inAncestorId) {
        this.ancestorId = inAncestorId;
    }

    /**
     * @return ##
     */
    public final String getAvailability() {
        return availability;
    }

    /**
     * @param inAvailability ##
     */
    public final void setAvailability(final String inAvailability) {
        this.availability = inAvailability;
    }

    /**
     * @return ##
     */
    public final String getBmlModel() {
        return bmlModel;
    }

    /**
     * @param inBmlModel ##
     */
    public final void setBmlModel(final String inBmlModel) {
        this.bmlModel = inBmlModel;
    }

    /**
     * @return ##
     */
    public final String getId() {
        return id;
    }

    /**
     * @param inId ##
     */
    public final void setId(final String inId) {
        this.id = inId;
    }

    /**
     * @return ##
     */
    public final String getIconUrl() {
        return iconUrl;
    }

    /**
     * @param inIconUrl ##
     */
    public final void setIconUrl(final String inIconUrl) {
        this.iconUrl = inIconUrl;
    }

    /**
     * @return ##
     */
    public final java.util.Date getLastChangeDate() {
        return lastChangeDate;
    }

    /**
     * @param inLastChangeDate ##
     */
    public final void setLastChangeDate(final java.util.Date inLastChangeDate) {
        this.lastChangeDate = inLastChangeDate;
    }

    /**
     * @return ##
     */
    public final java.util.Date getPublicationDate() {
        return publicationDate;
    }

    /**
     * @param inPublicationDate ##
     */
    public final void setPublicationDate(final java.util.Date inPublicationDate) {
        this.publicationDate = inPublicationDate;
    }

    /**
     * @return ##
     */
    public final String getRegistrarId() {
        return registrarId;
    }

    /**
     * @param inRegistrarId ##
     */
    public final void setRegistrarId(final String inRegistrarId) {
        this.registrarId = inRegistrarId;
    }

    /**
     * @return ##
     */
    public final String getRootSMID() {
        return rootId;
    }

    /**
     * @param inRootId ##
     */
    public final void setRootSMID(final String inRootId) {
        this.rootId = inRootId;
    }

    /**
     * @return ##
     */
    public final String getSdlModel() {
        return sdlModel;
    }

    /**
     * @param inSdlModel ##
     */
    public final void setSdlModel(final String inSdlModel) {
        this.sdlModel = inSdlModel;
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
     *  ##
     */
    public final void empty() {
        id = null;
        version = null;
        ancestorId = null;
        rootId = null;
        bmlModel = null;
        sdlModel = null;
        availability = null;
        registrarId = null;
        publicationDate = null;
        lastChangeDate = null;
        iconUrl = null;
    }
}
